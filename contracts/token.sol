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
    address public owner;
    
    // tokens per rouble
    uint public inPrice;
    uint public outPrice;
    uint public exchangePrice;
    
    mapping (address => uint) policies;
    
    // where address belongs to an owner
    mapping (address => bool) acceptableTokens;
    
    event Transfer(address indexed from, address indexed to, uint tokens);
    
    constructor(string _name, uint _inPrice, 
                uint _outPrice, uint _exchangePrice) public {
        owner = msg.sender;
        updValue(_name, _inPrice, _outPrice, _exchangePrice);
    }
    
    function updValue(string _name, uint _inPrice, 
                      uint _outPrice, uint _exchangePrice) onlyOwner public {
        name = _name;
        inPrice = _inPrice;
        outPrice = _outPrice;
        exchangePrice = _exchangePrice;
    }
    
    function transfer(address from, address to, uint amount) public onlyOwner {
        // fuck economic laws
        if (from == owner && balances[owner] < amount) {
            emitToken(amount);
        }
        balances[from] = balances[from].sub(amount);
        balances[to] = balances[to].add(amount);
        emit Transfer(from, to, amount);
    }
    
    function emitToken(uint amount) public onlyOwner {
        balances[owner] = balances[owner].add(amount);
    }
    
    function balanceOf(address _address) public view returns (uint) {
        return balances[_address];
    }
    
    modifier onlyOwner() {
        require(msg.sender == owner);
        _;
    }
}
