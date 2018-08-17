pragma solidity ^0.4.0;

import "./coalition.sol";

library SafeMath {
    function add(uint a, uint b) internal pure returns (uint c) {
        c = a + b;
        require(c >= a);
    }
    function sub(uint a, uint b) internal pure returns (uint c) {
        require(b <= a);
        c = a - b;
    }
    function mul(uint a, uint b) internal pure returns (uint c) {
        c = a * b;
        require(a == 0 || c / a == b);
    }
    function div(uint a, uint b) internal pure returns (uint c) {
        require(b > 0);
        c = a / b;
    }
}

contract Token {
    using SafeMath for uint;
    mapping (address => uint) balances;
    string name;
    address owner;
    Coalition coalitions;
    
    event Transfer(address indexed from, address indexed to, uint tokens);
    
    constructor(string _name) public {
        owner = msg.sender;
        name = _name;
    }
    
    function transfer(address from, address to, uint amount) public onlyOwner {
        balances[from] = balances[from].sub(amount);
        balances[to] = balances[to].add(amount);
        emit Transfer(from, to, amount);
    }
    
    modifier onlyOwner() {
        require(msg.sender == owner);
        _;
    }
} 
