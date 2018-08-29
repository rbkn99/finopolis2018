package com.example.nesadimsergej.test;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Config {
    public static String web3Address = "https://kovan.infura.io/v3/476ee0f8aa864c86816f33649c708206";//https://ropsten.infura.io/v3/6107ae917f254c9780385c9d1c734e2b

    public static String secretKey1 = "f739e58223c9fbd55ae321afcd15c3ef2d31d437ec11d5938fc9ab1b968ca7cc";

    // f739e58223c9fbd55ae321afcd15c3ef2d31d437ec11d5938fc9ab1b968ca7cc - secretKey(ropsten)

    public static String IS_TCP = "IS_TCP";
    public static String AccountInfo = "AccountInfo";

    public static String contractAdress = "0x38b117dbccb528de0635298196490c6b7e737808";//"0xfb23492e71e52780c3b7f10f595e4b5736a72446";

    public static String bankPrivateKey = "c718ab55513d1aa79c4a8956985e892cd267aa5d8b887d149a715392e706a8e3";
    public static String bankPublicKey = "91c4bf8d608e1684fdae0618988dd6c7304b9fc6004c1c94c25e7db19b7a02434b2c2162c92d5fbd3dfd4a9f68c0262e3f9bda4bf86b9ee2a5f7dfd8967e1e9b";

    public static BigInteger tene18 =new BigInteger( "1000000000000000000");
    public static BigDecimal tene18_decimal =new BigDecimal( "1000000000000000000");
    public static float AddBalance = 0.3f;
    public static BigInteger AddToToken = new BigInteger("50000000000000000");// =0.05 eth
}