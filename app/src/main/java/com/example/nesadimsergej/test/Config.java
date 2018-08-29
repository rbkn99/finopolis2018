package com.example.nesadimsergej.test;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Config {
    public static String web3Address = "https://kovan.infura.io/v3/6107ae917f254c9780385c9d1c734e2b";//https://ropsten.infura.io/v3/6107ae917f254c9780385c9d1c734e2b

    public static String secretKey1 = "f739e58223c9fbd55ae321afcd15c3ef2d31d437ec11d5938fc9ab1b968ca7cc";

    // f739e58223c9fbd55ae321afcd15c3ef2d31d437ec11d5938fc9ab1b968ca7cc - secretKey(ropsten)

    public static String IS_TCP = "IS_TCP";
    public static String AccountInfo = "AccountInfo";

    public static String contractAdress = "0xe53c8c93667746f473d13ebe7fd5aab3c1b32203";//"0xfb23492e71e52780c3b7f10f595e4b5736a72446";

    public static String bankPrivateKey = "761145e16dae910bfd2c7c849471fca6031b00e0d68ab5aaf134f2cc50cc40a7";
    public static String bankPublicKey = "ce433fb98cd81c389b020c61383f7d0df17afd4be5504e18fc19d737a1c4752d57e3478919bcef793bc07cb04b83f7e1fcc9be8390a15db6b8fa93cddf419aca";

    public static BigInteger tene18 =new BigInteger( "1000000000000000000");
    public static BigDecimal tene18_decimal =new BigDecimal( "1000000000000000000");
    public static float AddBalance = 0.3f;
    public static BigInteger AddToToken = new BigInteger("50000000000000000");// =0.05 eth
}