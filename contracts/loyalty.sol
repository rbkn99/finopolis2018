pragma solidity ^0.4.23;

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
        address _a;
        Token token;
        string name;
        uint256 deposit;
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
        mapping (address => bool) members;
    }
    
    enum RequestType {INVITE}
    
    // bank address
    address public owner;
    
    
    mapping (address => Customer) public customers;
    mapping (address => Company) public companies;
    mapping (address => Coalition) public coalitions;

    uint64 public companiesCount;
    Company[] public companySet;
    
    // map from company (owner) address to Token
    mapping (address => Token) public allTokens;
    
    // events for debug and output
    event AddCompany(address companyAddress, string name, uint phoneNumber);
    event AddCustomer(address customerAddress, uint number);
    event LoggedIn(address _address, uint number);
    event Log(address _address);
    
    constructor() public {
        owner = msg.sender;
        companiesCount = 0;
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
        companiesCount++;
        companies[company]._a = company;
        companies[company].exists = true;
        companies[company].name = _name;
        companies[company].phoneNumber = _phoneNumber;
        companySet.push(companies[company]);
        emit AddCompany(company, companies[company].name, customers[company].phoneNumber);
    }
    
    // bank calls, company pays
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
        uint initialGas = gasleft();
        Token token = companies[company].token;
        // charge bonuses to customer
        if (bonusesAmount == 0) {
            uint tokensAmount = roublesAmount.mul(token.inPrice());
            token.transfer(company, customer, tokensAmount);
            customers[customer].tokens[token] = true;
            if (!payForTransaction(initialGas - gasleft()))
                revert();
            return tokensAmount;
        }
        // write off bonuses
        else {
            uint deltaMoney;
            if (token.owner() == tokenOwner) {
                deltaMoney = tokensAmount.mul(token.outPrice());
                roublesAmount = roublesAmount.add(deltaMoney);
                token.transfer(customer, company, tokensAmount);
            }
            else {
                address current_coalition = isMatch(companies[company], 
                                                    companies[tokenOwner]);
                require(current_coalition != address(0));
                deltaMoney = tokensAmount.mul(token.exchangePrice());
                roublesAmount = roublesAmount.add(deltaMoney);
                token.transfer(customer, tokenOwner, tokensAmount);
            }
            if (!payForTransaction(initialGas - gasleft()))
                revert();
            return roublesAmount;
        }
    }
    
    // check if 2 companies belongs to the one coalition and returns its name
    function isMatch(Company c1, Company c2) private pure returns (address) {
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
        // doesn't exist => create
        if (companies[msg.sender].token.owner() == address(0)) {
            Token token;
            companies[msg.sender].token = token;
            allTokens[msg.sender] = token;
        }
        //else
        //    companies[msg.sender].token.updValue(_name, _inPrice, _outPrice, _exchangePrice);
    }
    
    function addEther() payable public companyExists(msg.sender) {
        companies[msg.sender].deposit = companies[msg.sender].deposit.add(msg.value);
    }
    
    function payForTransaction(uint gasAmount) private companyExists(msg.sender)
            returns (bool) { //success
        uint totalEthAmount = gasAmount * tx.gasprice;
        if (companies[msg.sender].deposit < totalEthAmount) {
            revert();
            return false;
        }
        companies[msg.sender].deposit = companies[msg.sender].deposit.sub(totalEthAmount);
        owner.transfer(totalEthAmount);
        return true;
    }
    
// --------------------------------------------------- NAHUI S MOEGO BOLOTA --------------------------------------------------------------------------    
    // coalition - coalition leader, _name - coalition name
    function addCoalition(address coalition, string _name) public
                                onlyOwner
                                companyExists(coalition)
                                coalitionNotExists(coalition) {
        coalitions[coalition].exists = true;
        coalitions[coalition].name = _name;
        coalitions[coalition].members[coalition] = true;
        companies[msg.sender].coalitions[coalition] = true;
    }
    // leader calls. company - company to invite
    function inviteToCoalition(address company) public 
                                companyExists(msg.sender)
                                coalitionExists(msg.sender) {
        Request join_request;
        join_request.message = "Idi nahui gomofobny pidaras";
        join_request.sender = msg.sender;
        join_request._type = RequestType.INVITE;
        companies[company].request_pool.push(join_request);
    }
    
    function getRequest () public view // Not working
                            companyExists(msg.sender)
                            returns (string message, address sender)
                            {
            
        Request request = companies[msg.sender].request_pool[
            companies[msg.sender].request_pool.length - 1];
        //TODO: if request is confirmed, push the coalition to company's coalitions
        return (request.message, request.sender);
    }
    // to whom and what to respond
    function respond (address request_sender,  bool answer) public 
                            companyExists(msg.sender)
                            {
        bool requestExists;
        uint request_index;
        for (uint i = 0; i < companies[msg.sender].request_pool.length; i++){
            if (companies[msg.sender].request_pool[i].sender == request_sender){
                requestExists = true;
                request_index = i;
            }
        }
        require(requestExists, "You\'re trying to asnwer a nonexisting request");
        if (answer) {
            coalitions[request_sender].members[msg.sender] = true;
        }
        else {
            
        }
        delete companies[msg.sender].request_pool[request_index];
        for(i = request_index + 1; 
            i < companies[msg.sender].request_pool.length;
            i++){
                companies[msg.sender].request_pool[i - 1] = companies[msg.sender].request_pool[i];
        }
        companies[msg.sender].request_pool.length -= 1;
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
