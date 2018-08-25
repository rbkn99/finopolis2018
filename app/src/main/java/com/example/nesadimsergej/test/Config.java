package com.example.nesadimsergej.test;

import android.content.Context;
import android.content.SharedPreferences;

import java.math.BigInteger;

public class Config {
    public static String web3Address = "HTTP://192.168.1.34:8545";//https://ropsten.infura.io/v3/6107ae917f254c9780385c9d1c734e2b
    //"0x6848a3372c9707ee342614afcfbcec3967a67314";
    // 0xcb94ceac938dec39254f57b384cfd9e2d9db06f9 - kovan

    public static String secretKey1 = "6b194546eef8f3f84deae03e7806dadcd981ebb6164f853e4a090fcaf36809ce";

    // f739e58223c9fbd55ae321afcd15c3ef2d31d437ec11d5938fc9ab1b968ca7cc - secretKey(ropsten)

    public static String address1 = "0xffCEF01631268eBc760eB3343372048f6c36122c";
    public static int TCP_START_BALANCE = 1000;
    public static String ADDRESS = "ADDRESS";
    public static String IS_TCP = "IS_TCP";
    public static String AccountInfo = "AccountInfo";
    // 0x3f65550164f4a8dac7393515dbacea4265a98f24 - kovan(address)


    public static String contractAdress = "0xea37a0f818592e644db85789a0ec9a3d7e54a779";//"0xfb23492e71e52780c3b7f10f595e4b5736a72446";

    //a23bba064c6fffd33184219ee5b6267f0c795723367d9a40c3b32fc818dd32b6 - kovan
    public static String prk = "333e866ab7d48453aae2e1883a466ffb4da6c6434d9669dcc06a10731a19fa3b";
    public static String puk = "13b9f8a73ed435342a58d246f6e9efaed209d62da6b4a8bf5bc34598e94a4430c20d3b84d2f7afefe3925f006740292056a79d767d2ee04ca6bee7932aba6a51";
    //public static String path ="/data/user/0/com.example.nesadimsergej.test/files/UTC--2018-08-25T15-51-59.841--445b8361b6aca422d156eccdc7b83566f4e2882f.json";
    // c483a91517c87d998f0a817b8e9615ffc14b064be874a631fa8532af84e856100a8b29c8c1a509014bdc0a52c961b03e4e711324883427f38e3621396909c4b1 - kovan

    //0x116a631417bde0911274b71f5ae7d21b3c0f812e

    public static String CONTRACT = "CONTRACT";
    public static String PRIVATE_KEY = "PRIVATE_KEY";
    public static String PUBLIC_KEY = "PUBLIC_KEY";

    // 0x43465cde93ad61b924c0c39c7bd4c868a951ff05 - bank address(ropstern)
    // 0x96d12bd43358fff503d9c492c0eab8b3ff2f61c1 - GB address
    // 0xbfe997685746fcfe9e11f4bf68b77821cb852d81 - dvach
    //0x53e76067d94e3abf9b3eacb6abdd1416db59fe5f
    //0x882923ddb40ce0b7f9a679841ad689ce5659d673
    //public static String PATH = "/data/user/0/com.example.nesadimsergej.test/files";
    //public static String NAME = "UTC--2018-08-19T22-57-58.757--31001d666e22e94e864239fd406c536b609b4901.json";


    public static float AddBalance = 0.2f;
    public static BigInteger AddToToken = new BigInteger("100000000000000000");// =0.5 eth
}
