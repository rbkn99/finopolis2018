pragma solidity ^0.4.0;

import "./coalition.sol";
import "./token.sol";

contract Loyalty {
    struct Customer {
        bool exists;
        uint phoneNumber;
        mapping (uint => bool) tokens;
    }
    
    
    mapping (address => Customer) private customers;
    mapping (address => Coalition.Company) public companies;
    mapping (address => Token) public allTokens;
    
    event AddCompany(address companyAddress, string name, uint phoneNumber);
    event AddCustomer(address customerAddress, uint number);
    event LoggedIn(address _address, uint number);
    
    function addCustomer(uint _phoneNumber) public
                customerNotExists(msg.sender)
                companyNotExists(msg.sender) {
        customers[msg.sender].exists = true;
        customers[msg.sender].phoneNumber = _phoneNumber;
        emit AddCustomer(msg.sender, customers[msg.sender].phoneNumber);
    }
    
    function addCompany(string _name, uint _phoneNumber) public
                companyNotExists(msg.sender)
                customerNotExists(msg.sender) {
        companies[msg.sender].exists = true;
        companies[msg.sender].name = _name;
        companies[msg.sender].phoneNumber = _phoneNumber;
        emit AddCompany(msg.sender, companies[msg.sender].name,
                                    customers[msg.sender].phoneNumber);
    }
    
    function logIn(uint phoneNumber) public {
        require(customers[msg.sender].phoneNumber == phoneNumber ||
                companies[msg.sender].phoneNumber == phoneNumber);
        emit LoggedIn(msg.sender, phoneNumber);
    }
    
    function chargeBonuses(address customer, uint amount) public 
                                customerExists(customer) 
                                companyExists(msg.sender) {
        companies[msg.sender].token.transfer(msg.sender, customer, amount);
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
