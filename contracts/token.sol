pragma solidity ^0.4.0;

library SafeMath {
    function add(uint a, uint b) internal pure returns (uint c) {
        c = a + b;
        require(c >= a, "Invalid addition");
    }
    function sub(uint a, uint b) internal pure returns (uint c) {
        require(b <= a, "Invalid subtraction");
        c = a - b;
    }
    function mul(uint a, uint b) internal pure returns (uint c) {
        c = a * b;
        require(a == 0 || c / a == b, "Invalid multiplication");
    }
    function div(uint a, uint b) internal pure returns (uint c) {
        require(b > 0, "Invalid division");
        c = a / b;
    }
}

contract Token {
    using SafeMath for uint;
    mapping (address => uint) balances;
    string name;
    address owner;
    
    mapping (address => uint) policies;
    
    event Transfer(address indexed from, address indexed to, uint tokens);
    
    constructor(string _name, uint policy) public {
        owner = msg.sender;
        name = _name;
        policies[owner] = policy;
    }
    
    function transfer(address from, address to, uint amount) public onlyOwner {
        balances[from] = balances[from].sub(amount);
        balances[to] = balances[to].add(amount);
        emit Transfer(from, to, amount);
    }
    
    function balanceOf(address _address) public view returns (uint) {
        return balances[_address];
    }
    
    modifier onlyOwner() {
        require(msg.sender == owner);
        _;
    }
}
