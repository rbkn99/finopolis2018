package com.example.nesadimsergej.test;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Config {
    public static String web3Address = "https://kovan.infura.io/v3/6107ae917f254c9780385c9d1c734e2b";//https://ropsten.infura.io/v3/6107ae917f254c9780385c9d1c734e2b

    public static String secretKey1 = "f739e58223c9fbd55ae321afcd15c3ef2d31d437ec11d5938fc9ab1b968ca7cc";

    // f739e58223c9fbd55ae321afcd15c3ef2d31d437ec11d5938fc9ab1b968ca7cc - secretKey(ropsten)

    public static String IS_TCP = "IS_TCP";
    public static String AccountInfo = "AccountInfo";

    public static String contractAdress = "0xea451b7ea4714d5f51a9a4560969873e9c06cd89";//"0xfb23492e71e52780c3b7f10f595e4b5736a72446";

    public static String bankPrivateKey = "63bf1b92330c83d7c7515422dcbff2478592659505692e2f7b339341aa326df7";
    public static String bankPublicKey = "753a8f55eec86e250d2f9671b8f08f85cbdf897aeee5944abfa8a32b794c3b5988a38eb91025cb4560c38095bcbf51263509d47b70db4ef75d51efe9cfd82ce9";

    public static BigInteger tene18 =new BigInteger( "1000000000000000000");
    public static BigDecimal tene18_decimal =new BigDecimal( "1000000000000000000");
    public static float AddBalance = 0.3f;
    public static BigInteger AddToToken = new BigInteger("100000000000000000");// =0.1 eth
}