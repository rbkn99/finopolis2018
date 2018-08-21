pragma solidity ^0.4.0;

import "./token.sol";

contract Loyalty {
    using SafeMath for uint;
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
        address[] coalitionNames;
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
    address public owner;
    
    
    mapping (address => Customer) public customers;
    mapping (address => Company) public companies;
    mapping (address => Coalition) public coalitions;

    Company[] public companySet;
    
    // map from company (owner) address to Token
    mapping (address => Token) public allTokens;
    
    // events for debug and output
    event AddCompany(address companyAddress, string name, uint phoneNumber);
    event AddCustomer(address customerAddress, uint number);
    event LoggedIn(address _address, uint number);
    
    constructor() public {
        owner = msg.sender;
    }
    
    // bank calls
    function addCustomer(address customer, uint _phoneNumber) public
                onlyOwner
                customerNotExists(customer)
                companyNotExists(customer) {
        customers[customer].exists = true;
        customers[customer].phoneNumber = _phoneNumber;
        emit AddCustomer(customer, customers[customer].phoneNumber);
    }
    
    // bank calls
    function addCompany(address company, string _name, uint _phoneNumber) public
                onlyOwner
                companyNotExists(company)
                customerNotExists(company) {
        companySet.push(companies[company]);
        companies[company].exists = true;
        companies[company].name = _name;
        companies[company].phoneNumber = _phoneNumber;
        emit AddCompany(company, companies[company].name, customers[company].phoneNumber);
    }
    
    // bank calls
    function transferBonuses(address company,
                             address customer,
                             uint roublesAmount,
                             uint bonusesAmount,
                             address tokenOwner) // 0 if bonusesAmount == 0
                                public
                                onlyOwner
                                customerExists(customer)
                                companyExists(company) returns (uint) // if bonusesAmount == 0 returns charged bonuses amount,
                                                                      // in another case returns roubles amount
                                {
        Token token = companies[company].token;
        // charge bonuses to customer                            
        if (bonusesAmount == 0) {
            
            // the simpliest case - when token belongs to the company
            if (token.owner() == company) {
                uint tokensAmount = roublesAmount.mul(token.inPrice());
                token.transfer(company, customer, tokensAmount);
                customers[customer].tokens[token] = true;
            }
            else {
                address current_coalition = isMatch(companies[company], 
                                                    companies[tokenOwner]);
                require(current_coalition != address(0));
                
            }
            return tokensAmount;
        }
        // write off bonuses
        else {
            if (token.owner() == company) {
                uint deltaMoney = tokensAmount.mul(token.outPrice());
                roublesAmount = roublesAmount.add(deltaMoney);
                token.transfer(customer, company, tokensAmount);
            }
            else {
                // TODO: hard case, needs merge with Slavique
            }
            return roublesAmount;
        }
    }
    
    // check if 2 companies belongs to the one coalition and returns its name
    function isMatch(Company c1, Company c2) private returns (address) {
        for (uint i = 0; i < c1.coalitionNames.length; i++) {
            for (uint j = 0; j < c2.coalitionNames.length; j++) {
                if (c1.coalitionNames[i] == c2.coalitionNames[j])
                    return c1.coalitionNames[i];
            }
        }
        return address(0);
    }
    
    // company calls
    // name of the token, tokens per spent rouble, price when you spend tokens
    function setToken(string _name, uint _inPrice, uint _outPrice,
                         uint _exchangePrice) public
        companyExists(msg.sender) {
        Token token;
        // doesn't exist => create
        if (companies[msg.sender].token.owner() == address(0)) {
            token = new Token(_name, _inPrice, _outPrice, _exchangePrice);
            companies[msg.sender].token = token;
            allTokens[msg.sender] = token;
        }
        else
            token.updValue(_name, _inPrice, _outPrice, _exchangePrice);
    }
// --------------------------------------------------- NAHUI S MOEGO BOLOTA --------------------------------------------------------------------------    
    // company calls - it becomes coalition owner
    function addCoalition(address coalition, string _name) public
                                companyExists(msg.sender)
                                coalitionNotExists(coalition) {
        coalitions[coalition].exists = true;
        coalitions[coalition].name = _name;
        coalitions[coalition].members[msg.sender] = true;
        coalitions[coalition].leader = msg.sender;
        companies[msg.sender].coalitions[coalition] = true;
    }
    
    // falcon calls
    function inviteToCoalition(address coalition) public 
                                companyExists(msg.sender)
                                coalitionExists(coalition) {
        Request join_request;
        join_request.message = "Idi nahui gomofobny pidaras";
        join_request.sender = msg.sender;
        join_request._type = RequestType.INVITE;
        companies[coalitions[coalition].leader].request_pool.push(join_request);
    }
    
    function getRequest () public // call while not tresnesh' 
                            companyExists(msg.sender)
                            {
        var request = companies[msg.sender].request_pool[
            companies[msg.sender].request_pool.length - 1];
        //TODO: if request is confirmed, push the coalition to company's coalitions
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
    
    modifier companyExists(address company) {
        require(companies[company].exists, "Company doesn't exist.");
        _;
    }
    
    modifier customerNotExists(address customer) {
        require(!customers[customer].exists, "Customer already exists.");
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
