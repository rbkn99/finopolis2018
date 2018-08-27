package com.example.nesadimsergej.test;

import java.math.BigInteger;

public class Config {
    public static String web3Address = "https://kovan.infura.io/v3/6107ae917f254c9780385c9d1c734e2b";//https://ropsten.infura.io/v3/6107ae917f254c9780385c9d1c734e2b

    public static String secretKey1 = "f739e58223c9fbd55ae321afcd15c3ef2d31d437ec11d5938fc9ab1b968ca7cc";

    // f739e58223c9fbd55ae321afcd15c3ef2d31d437ec11d5938fc9ab1b968ca7cc - secretKey(ropsten)

    public static String IS_TCP = "IS_TCP";
    public static String AccountInfo = "AccountInfo";

    public static String contractAdress = "0xe09df7adfd82dab177c3922d5d0a8bbb58b2672a";//"0xfb23492e71e52780c3b7f10f595e4b5736a72446";

    public static String bankPrivateKey = "4738fb20c23701aac01c1e30e7e6382c5bc1ee2156fedec6bd5c5d0a7b71da37";
    public static String bankPublicKey = "76483faed5a9790b722323bec6f505bd68d83864d2110ac5284417611e7637380b3ce17c6e8d6a15109a0001a3b1900bfce07c88ebba77ce5358009cc206e09d";

    public static BigInteger tene18 =new BigInteger( "1000000000000000000");

    public static float AddBalance = 0.2f;
    public static BigInteger AddToToken = new BigInteger("100000000000000000");// =0.5 eth
}