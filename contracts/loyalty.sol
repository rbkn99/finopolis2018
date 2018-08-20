pragma solidity ^0.4.0;

import "./coalition.sol";
import "./token.sol";

contract Loyalty {
    struct Customer {
        bool exists;
        uint phoneNumber;
        mapping (address => bool) tokens;
    }
    
    struct Company {
        bool exists;
        Token token;
        string name;
        uint phoneNumber;
        Request[] request_pool; 
        mapping (address => bool) coalitions;
    }
    
    struct Request {
        string message;
        address sender;
        RequestType _type;
    }
    
    struct Coalition{
        bool exists;
        string name;
        address leader;
        mapping (address => bool) members;
    }
    
    enum RequestType {INVITE}
    
    // bank address
    address owner;
    
    mapping (address => Customer) private customers;
    mapping (address => Company) public companies;
    mapping (address => Coalition) public coalitions;
    
    
    //mapping (address => Token) public allTokens;
    
    event AddCompany(address companyAddress, string name, uint phoneNumber);
    event AddCustomer(address customerAddress, uint number);
    event LoggedIn(address _address, uint number);
    
    constructor() public {
        owner = msg.sender;
    }
    
    function addCustomer(address customer, uint _phoneNumber) public
                onlyOwner
                customerNotExists(customer)
                companyNotExists(customer) {
        customers[customer].exists = true;
        customers[customer].phoneNumber = _phoneNumber;
        emit AddCustomer(customer, customers[customer].phoneNumber);
    }
    
    function addCompany(address company, string _name, uint _phoneNumber) public
                onlyOwner
                companyNotExists(company)
                customerNotExists(company) {
        companies[company].exists = true;
        companies[company].name = _name;
        companies[company].phoneNumber = _phoneNumber;
        emit AddCompany(company, companies[company].name, customers[company].phoneNumber);
    }
    
    
    
    function logIn(uint phoneNumber) public {
        require(customers[msg.sender].phoneNumber == phoneNumber ||
                companies[msg.sender].phoneNumber == phoneNumber);
        emit LoggedIn(msg.sender, phoneNumber);
    }
    
    function chargeBonuses(address company, address customer, uint amount) public
                                onlyOwner
                                customerExists(customer)
                                companyExists(company) {
        companies[msg.sender].token.transfer(company, customer, amount);
        customers[customer].tokens[companies[company].token] = true;
    }
    
    function createToken(string _name, uint ownerPolicy) public companyExists(msg.sender) {
        companies[msg.sender].token = new Token(_name, ownerPolicy);
    }
    
    function addCoalition(address coalition, string _name) public 
                                companyExists(msg.sender)
                                coalitionNotExists(coalition){
        coalitions[coalition].exists = true;
        coalitions[coalition].name = _name;
        coalitions[coalition].members[msg.sender] = true;
        coalitions[coalition].leader = msg.sender;
        companies[msg.sender].coalitions[coalition] = true;
    }
    
    function inviteToCoalition(address coalition) public 
                                companyExists(msg.sender)
                                coalitionExists(coalition){
        Request join_request;
        join_request.message = "Idi nahui gomofobny pidaras";
        join_request.sender = msg.sender;
        join_request._type = RequestType.INVITE;
        companies[coalitions[coalition].leader].request_pool.push(join_request);
    }
    
    function getRequest () public // вызывать пока не треснешь 
                            companyExists(msg.sender)
                            {
        var request = companies[msg.sender].request_pool[companies[msg.sender].request_pool.length - 1];
        //return (request.message, request.sender);
    }
    
    modifier onlyOwner() {
        require(msg.sender == owner);
        _;
    }
    
    modifier customerExists(address customer) {
        require(customers[customer].exists, "Customer doesn't exist.");
        _;
    }
    
    modifier customerNotExists(address customer) {
        require(!customers[customer].exists, "Customer already exists.");
        _;
    }
    
    modifier companyExists(address company) {
        require(companies[company].exists, "Company doesn't exist.");
        _;
    }
    
    modifier companyNotExists(address company) {
        require(!companies[company].exists, "Company already exists.");
        _;
    }
    
    modifier coalitionExists(address coalition) {
        require(coalitions[coalition].exists, "Coalition doesn't exist.");
        _;
    }
    
    modifier coalitionNotExists(address coalition) {
        require(!coalitions[coalition].exists, "Coalition already exists.");
        _;
    }
}
