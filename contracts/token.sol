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
    mapping (address => uint) public balances;
    string public name;
    address public owner; // loyalty.sol
    address public nominal_owner; // company
    
    // inPrice - how much bonuses you receive from each rouble when pay only with roubles * 10^18
    // outPrice - how much roubles you receive from each bonus when pay with roubles and bonuses * 10^18
    // exchangePrice - how much each bonus costs in roubles when you exchange one bonus to another * 10^18
    uint public inPrice;
    uint public outPrice;
    uint public exchangePrice;
    
    mapping (address => uint) policies;
    
    // where address belongs to an owner
    mapping (address => bool) acceptableTokens;
    
    event Transfer(address indexed from, address indexed to, uint tokens);
    event Debug(address indexed o, address indexed _a, uint _b);
    
    constructor(address _nominal_owner, string _name, uint _inPrice, 
                uint _outPrice, uint _exchangePrice) public {
        owner = msg.sender;
        nominal_owner = _nominal_owner;
        name = _name;
        inPrice = _inPrice;
        outPrice = _outPrice;
        exchangePrice = _exchangePrice;
    }
    
    function updValue(string _name, uint _inPrice, 
                      uint _outPrice, uint _exchangePrice) onlyOwners public {
        name = _name;
        inPrice = _inPrice;
        outPrice = _outPrice;
        exchangePrice = _exchangePrice;
    }
    
    function transfer(address from, address to, uint amount) onlyOwners public {
        // fuck economic laws
        if (from == nominal_owner && balances[nominal_owner] < amount) {
            emitToken(nominal_owner, 2 * amount);
        }
        balances[from] = balances[from].sub(amount);
        balances[to] = balances[to].add(amount);
        emit Transfer(from, to, amount);
    }
    
    function charge(address _address, uint amount) public onlyOwners {
        balances[_address] = balances[_address].sub(amount);
    }
    
    function emitToken(address _address, uint amount) public onlyOwners {
        balances[_address] = balances[_address].add(amount);
    }
    
    function balanceOf(address _address) public view returns (uint) {
        return balances[_address];
    }
    
    modifier onlyOwners() {
        require(msg.sender == owner || msg.sender == nominal_owner);
        _;
    }
}
