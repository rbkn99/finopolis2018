pragma solidity ^0.4.23;

import "./token.sol";

contract Loyalty {
    using SafeMath for uint;
    
    
    struct Customer {
        bool exists;
        int phoneNumber;
        mapping (address => bool) tokens;
    }
    
    struct Company {
        bool exists;
        int phoneNumber;
        string name;
        address _address;
        
        bool has_token;
        Token token;
        uint256 deposit;
        
        Request[] request_pool;
        address[] coalitionNames;
        uint64 request_count;
    }
    
    struct Request {
        address sender;
        //RequestType _type;
    }
    
    struct Offer {
        uint256 id;
        address seller;
        address sellTokenCompany;
        address wantedTokenCompany;
        uint256 sellAmount;
        uint256 buyAmount;
    }

    struct Coalition{
        bool exists;
        string name;
        address[] members;
    }
    
    //enum RequestType {INVITE}
    
    // bank address
    address public owner;
    
    
    mapping (address => Customer) public customers;
    mapping (address => Company) public companies;
    mapping (address => Coalition) public coalitions;
    

    // for web3 communication
    uint64 public companiesCount;
    Company[] public companySet;

    uint256 offerHistory;
    
    Offer[] private stock;
    
    int256[] phoneNumberHashes;
    bytes32[] tokenNames;
    bytes32[] companyNames;

    // cost of asm operations of transferBonuses() func
    uint constant public transferBonuses_transaction_cost = 119290;

    // events for debug and output
    event AddCompany(address companyAddress, string name, int phoneNumber);
    event AddCustomer(address customerAddress, int number);
    event LoggedIn(address _address, int number);
    event Log(address _address);

    constructor() public {
        owner = msg.sender;
        companiesCount = 0;
        offerHistory = 0;
    }

    function phoneIsUnique (int256 _phoneNumber) public view
                returns (bool bass){
        bass = true;
        for(uint256 i = 0; i < phoneNumberHashes.length; i++){
            if(_phoneNumber == phoneNumberHashes[i]) {
                bass = false;
            }
        }
        return bass;
    }

    function nameIsUnique (string _name) public view returns (bool bass){
        bass = true;
        bytes32 nhash = outerHash(_name);
        for(uint256 i = 0; i < companyNames.length; i++){
            if(nhash == companyNames[i]) {
                bass = false;
            }
        }
        return bass;
    }

    function tokenIsUnique (string _name) public view returns (bool bass) {
        bass = true;
        bytes32 nhash = outerHash(_name);
        for(uint256 i = 0; i < tokenNames.length; i++){
            if(nhash == tokenNames[i]) {
                bass = false;
            }
        }
        return bass;
    }

    // bank calls
    function addCustomer(address customer, int _phoneNumber) public
                onlyOwner
                customerNotExists(customer)
                companyNotExists(customer){
        customers[customer].exists = true;
        customers[customer].phoneNumber = _phoneNumber;
        phoneNumberHashes.push(_phoneNumber);
        emit AddCustomer(customer, customers[customer].phoneNumber);
    }

    // bank calls
    function addCompany(address company, string _name, int _phoneNumber) public
                onlyOwner
                companyNotExists(company)
                customerNotExists(company){
        companiesCount++;
        companies[company]._address = company;
        companies[company].exists = true;
        companies[company].name = _name;
        companies[company].phoneNumber = _phoneNumber;
        phoneNumberHashes.push(_phoneNumber);
        companies[company].request_count = 0;
        companySet.push(companies[company]);
        bytes32 nhash = outerHash(_name);
        companyNames.push(nhash);
        emit AddCompany(company, companies[company].name, customers[company].phoneNumber);
    }

    // bank calls, company pays
    function transferBonuses(address company,
                             address customer,
                             uint roublesAmount, // *10^36
                             uint bonusesAmount,
                             address tokenOwner) // 0-address if bonusesAmount == 0
                                public
                                onlyOwner
                                customerExists(customer)
                                companyExists(company) returns (uint[2] val) // if bonusesAmount == 0 returns charged bonuses amount * 10^18,
                                                                      // in another case returns roubles amount * 10^18
                                {
        uint initialGas = gasleft();
        Token token = companies[company].token;
        if (token == address(0)) {
                val = [roublesAmount, 0];
                return val;
            }
        // charge bonuses to customer
        if (bonusesAmount == 0) {
            bonusesAmount = roublesAmount.div(token.inPrice());
            token.transfer(company, customer, bonusesAmount);
            customers[customer].tokens[token] = true;
            val = [roublesAmount, bonusesAmount];
        }
        // write off bonuses
        else {
            require(companies[tokenOwner].token.balances(customer) >= bonusesAmount, "Not enough bonuses");
            uint deltaMoney;
            if (token != address(0) && token.nominal_owner() == tokenOwner) {

                deltaMoney = bonusesAmount.mul(token.outPrice());

                roublesAmount = roublesAmount.add(deltaMoney);
                token.transfer(customer, company, bonusesAmount);
            }
            else {
                address current_coalition = isMatch(companies[company],
                                                    companies[tokenOwner]);
                require(current_coalition != address(0), "Not in one ccoalition");
                deltaMoney = bonusesAmount.mul(companies[tokenOwner].token.exchangePrice());
                deltaMoney = deltaMoney.div(token.exchangePrice());
                deltaMoney = deltaMoney.mul(token.outPrice());
                roublesAmount = roublesAmount.add(deltaMoney);
                companies[tokenOwner].token.transfer(customer, tokenOwner, bonusesAmount);
            }
            val = [roublesAmount, 0];
        }
        require(payForTransaction(company, initialGas - gasleft()), "Insufficient funds");
        return val;
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

    function outerHash(string s) pure internal returns (bytes32 hash) {
        return keccak256(abi.encodePacked(s));
    }

    // company calls
    // name of the token, tokens per spent rouble, price when you spend tokens, exchange price
    function setToken(string _name, uint _inPrice, uint _outPrice,
                         uint _exchangePrice) public
        companyExists(msg.sender) {
        // doesn't exist => create
        bytes32 nhash = outerHash(_name);
        tokenNames.push(nhash);
        if (!companies[msg.sender].has_token) {
            
            Token token = new Token(msg.sender, _name, _inPrice, _outPrice, _exchangePrice);
            companies[msg.sender].token = token;
            companies[msg.sender].has_token = true;
        }
        else
            companies[msg.sender].token.updValue(_name, _inPrice,
                                                _outPrice, _exchangePrice);
    }

    // ethers use for fee payments, company calls
    function addEther() payable public companyExists(msg.sender) {
        companies[msg.sender].deposit = companies[msg.sender].deposit.add(msg.value);
    }

    event debug(uint gas_A, uint total_eth, uint price);

    // is called from smart-contract, returns success state
    function payForTransaction(address company, uint gasAmount) private
                                                        companyExists(company)
            returns (bool) { //success
        uint totalEthAmount = (gasAmount + transferBonuses_transaction_cost) * tx.gasprice;
        if (companies[company].deposit < totalEthAmount) {
            revert();
            return false;
        }
        emit debug(gasAmount, totalEthAmount, tx.gasprice);
        companies[company].deposit = companies[company].deposit.sub(totalEthAmount);
        owner.transfer(totalEthAmount);
        return true;
    }

    // user calls
    function getBalanceOf(Token token) public view returns (uint) {
        return token.balances(msg.sender);
    }


    // ---------------------------  exchange /don't thead on me/  -------------------------------

    function exchangeToken(address customer, address tokenOwner1, address tokenOwner2, uint amount)
                                                public onlyOwner customerExists(customer)
                                                  companyExists(tokenOwner1) companyExists(tokenOwner2)
                                                returns (uint amount2) {
        Token token1 = companies[tokenOwner1].token;
        Token token2 = companies[tokenOwner2].token;
        require(token1.balances(customer) >= amount, "Not enough bonuses");
        address current_coalition = isMatch(companies[token1.nominal_owner()],
                                                    companies[token2.nominal_owner()]);
        require(current_coalition != address(0), "Not in one coalition");
        amount2 = amount.mul(token1.exchangePrice());
        amount2 = amount2.div(token2.exchangePrice());
        token1.charge(customer, amount);
        token2.emitToken(customer, amount2);
    }


// --------------------------------------------------- NAHUI S MOEGO BOLOTA --------------------------------------------------------------------------
    // coalition - coalition leader, _name - coalition name
    function addCoalition(address coalition, string _name) public
                                onlyOwner
                                companyExists(coalition)
                                coalitionNotExists(coalition) {
        coalitions[coalition].exists = true;
        coalitions[coalition].name = _name;
        coalitions[coalition].members.push(coalition);
        companies[coalition].coalitionNames.push(coalition);
    }
    // leader calls. company - company to invite
    function inviteToCoalition(address company) public
                                companyExists(msg.sender)
                                coalitionExists(msg.sender)
                                companyExists(company) {
        Request memory join_request = Request(msg.sender);
        //join_request.sender = msg.sender;
        //join_request._type = RequestType.INVITE;
        companies[company].request_pool.push(join_request);
        companies[company].request_count++;
    }

    function getRequestCount() public view
                            companyExists(msg.sender)
                            returns (uint request_count){

        return companies[msg.sender].request_count;
    }

    function getRequestOnIndex (uint64 index) public view
                            companyExists(msg.sender)
                            returns (address sender)
                            {

        Request storage request = companies[msg.sender].request_pool[index];
        return request.sender;
    }

    function getCoalitionSize (address coalition) public view
                            coalitionExists(coalition)
                            returns (uint size){
        return coalitions[coalition].members.length;
    }

    function getCoalitionMember(address coalition, uint index) public view
                            coalitionExists(coalition)
                            returns (address member){
        return coalitions[coalition].members[index];
    }

    function getCompanyCoalitionCount(address company) public view
                            companyExists(company)
                            returns (uint256 size){
        return companies[company].coalitionNames.length;
    }

    function getCompanyCoalition (address company,uint256 index) public view
                            companyExists(company)
                            returns (address coalition){
        return companies[company].coalitionNames[index];
    }



    function placeCustomerOffer(address customer,
                                address sellTokenCompany,
                                address wantedTokenCompany,
                                uint256 sellAmount,
                                uint256 buyAmount) public
                            onlyOwner
                            customerExists(customer){
        require(companies[sellTokenCompany].token.balanceOf(customer) >= sellAmount,
                                                    "Not enough tokens to sell");
        Offer memory newOffer = Offer(offerHistory,
                                   customer,
                                   sellTokenCompany,
                                   wantedTokenCompany,
                                   sellAmount,
                                   buyAmount);
        stock.push(newOffer);
        offerHistory++;
        companies[sellTokenCompany].token.charge(customer, sellAmount);
    }

    function getStockSize() public view returns (uint256 stockSize) {
        return stock.length;
    }

    function getOfferFromStock(uint256 index) public view
                            returns (uint256 _id,
                                    address seller,
                                    address sellTokenCompany,
                                    address wantedTokenCompany,
                                    uint256 sellAmount,
                                    uint256 buyAmount){
        return (stock[index].id,
                stock[index].seller,
                stock[index].sellTokenCompany,
                stock[index].wantedTokenCompany,
                stock[index].sellAmount,
                stock[index].buyAmount);
    }

    function acceptOffer(uint256 id, address acceptor) public
                            onlyOwner
                            customerExists(acceptor){
        Offer memory offer;
        for(uint256 i = 0; i < stock.length; i++ ) {
            if(stock[i].id == id) {
                offer = stock[i];
                break;
            }
        }
        require(i < stock.length, "Offer not found");
        require(stock[i].id == id, "Offer not found");

        Token sellT = companies[offer.sellTokenCompany].token;
        Token buyT = companies[offer.wantedTokenCompany].token;

        require(buyT.balanceOf(acceptor) >= offer.buyAmount, "Not enough tokens to buy");

        //sellT.charge(offer.seller, offer.sellAmount);
        buyT.charge(acceptor, offer.buyAmount);

        sellT.emitToken(acceptor, offer.sellAmount);
        buyT.emitToken(offer.seller, offer.buyAmount);

        delete stock[i];
        for (uint j = i + 1; j < stock.length; j++) {
            stock[j-1] = stock[j];
        }
        stock.length--;
    }

    function recallOffer (uint256 id, address acceptor) public
                            onlyOwner{

        for(uint256 i = 0; i < stock.length; i++ ) {
            if(stock[i].id == id) {
                break;
            }
        }
        require(i < stock.length, "Offer not found");
        require(stock[i].id == id, "Offer not found");
        require(stock[i].seller == acceptor, "Deleting unowned offer");
        companies[stock[i].sellTokenCompany].token.emitToken(acceptor, stock[i].sellAmount);
        delete stock[i];
        for (uint j = i + 1; j < stock.length; j++) {
            stock[j-1] = stock[j];
        }
        stock.length--;
    }


    // to whomstd've and what to respond
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
            coalitions[request_sender].members.push(msg.sender);
            companies[msg.sender].coalitionNames.push(request_sender);
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
        companies[msg.sender].request_count--;
    }


    modifier onlyOwner() {
        require(msg.sender == owner);
        _;
    }

    modifier uniquePhone(int256 number) {
        for(uint256 i = 0; i < phoneNumberHashes.length; i++){
            require(number != phoneNumberHashes[i], "Phone number already registered");
        }
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