package com.example.nesadimsergej.test;

import org.web3j.tuples.generated.Tuple6;

import java.math.BigInteger;

public class Order {
    public BigInteger id;
    public String seller;
    public String sellToken;
    public String wantedToken;
    public BigInteger sellAmount;
    public BigInteger buyAmount;

    public Order(Tuple6<BigInteger, String, String, String, BigInteger, BigInteger> t6) {
        id = t6.getValue1();
        seller = t6.getValue2();
        sellToken = t6.getValue3();
        wantedToken = t6.getValue4();
        sellAmount = t6.getValue5();
        buyAmount = t6.getValue6();
    }
}
