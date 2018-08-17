pragma solidity ^0.4.0;

import "./coalition.sol";
import "./token.sol";

contract Loyalty {
    struct Customer {
        bool exists;
        uint phoneNumber;
        mapping (uint => bool) tokens;
    }
    
    struct Company {
        bool exists;
        address[] tokens;
        string name;
        mapping (address => bool) coalitions;
    }
    
    mapping (address => Customer) private customers;
    mapping (address => Company) public companies;
    mapping (address => Token) public allTokens;
    
    function addCustomer() public {
        require(!customers[msg.sender].exists, "Customer already exists.");
        customers[msg.sender].exists = true;
    }
    
    function chargeBonuses(address customer, Token token) public {
        require(customers[customer].exists, "Customer doesn't exist.");
        require(companies[msg.sender].exists, "Company doesn't exist");
        
    }
}
 
