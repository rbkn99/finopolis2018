pragma solidity ^0.4.0;

import "./token.sol";

contract Coalition {
    struct Company {
        bool exists;
        Token ownToken;
        string name;
        uint phoneNumber;
        mapping (address => bool) coalitions;
    }
}
