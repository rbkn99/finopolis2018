pragma solidity ^0.4.0;

import "./coalition.sol";
import "./token.sol";

contract Loyalty {
    struct Customer {
        bool exists;
        uint phoneNumber;
        mapping (address => bool) tokens;
    }
    
    // bank address
    address owner;
    
    mapping (address => Customer) private customers;
    mapping (address => Coalition.Company) public companies;
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
    
    modifier onlyOwner() {
        require(msg.sender == owner);
        _;
    }
    
    modifier customerExists(address customer) {
        require(customers[customer].exists, "Customer doesn't exist.");
        _;
    }
    
    modifier companyExists(address company) {
        require(customers[company].exists, "Company doesn't exist.");
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
}
