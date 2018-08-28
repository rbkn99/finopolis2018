package com.example.nesadimsergej.test;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tuples.generated.Tuple2;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static android.support.v4.app.NotificationCompat.DEFAULT_ALL;
import static android.support.v4.app.NotificationCompat.DEFAULT_VIBRATE;

public class Utils {
    public static final String CHANNEL_ID = "1";
    public static NotificationManager notificationManager;

    public static final String longLoadingMsg = "Идёт %s, это может занять несколько минут. Вы можете " +
            "свернуть приложение, но пожалуйста, не выключайте его.";

    public static void createNotificationChannel(Context ctx) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "blp";
            String description = "blockchain loyalty program";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(Utils.CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager = ctx.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static void sendNotification(Context ctx, String msg, int not_id) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setDefaults(DEFAULT_VIBRATE)
                .setContentTitle("Blockchain loyalty")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(msg))
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setAutoCancel(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mBuilder.setPriority(NotificationManager.IMPORTANCE_HIGH);
        }
        else {
            mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(ctx);
        notificationManager.notify(not_id, mBuilder.build());
    }


    public static ArrayList<TokenWrapper> CalculatePossibleTokens(Web3j web3, Credentials credentials, Company company){
        Set<TokenWrapper> tokens = new HashSet<>();
        tokens.add(getToken(web3,credentials,company.token));
        String startCompany = company._address;
        Loyalty contract = Loyalty.load(Config.contractAdress,web3,credentials,Loyalty.GAS_PRICE,Loyalty.GAS_LIMIT);

        ArrayList<String> coalitionAddresses = GetCoalitions(credentials,web3,contract,startCompany);

        for (String currentCoalition:coalitionAddresses)
        {
            ArrayList<String> coalitionMembers = GetCoalitionMember(credentials,web3,contract,currentCoalition);

            for (String currentCompanyAddress: coalitionMembers) {

                try {
                    Company currentCompany = new Company(contract.companies(currentCompanyAddress).send());
                    if(currentCompany.hasToken) {
                        tokens.add(getToken(web3, credentials, currentCompany.token));
                    }
                }catch (Exception e){

                }
            }
        }



        //Set<TokenWrapper> uniqueGas = new HashSet<TokenWrapper>(tokens);
        //System.out.println();
        ArrayList<TokenWrapper> result = new ArrayList<>();
        for (TokenWrapper token: tokens) {
            result.add(token);
        }

        return result;
    }

    public static ArrayList<String> GetCoalitionMember(Credentials credentials,Web3j web3,Loyalty contract, String coalitionAddress){
        BigInteger coalitionSize = BigInteger.ZERO;
        try {
            coalitionSize = contract.getCoalitionSize(coalitionAddress).send();
        }catch (Exception e){
            e.printStackTrace();
        }

        ArrayList<String> coalitionMembers = new ArrayList<>();
        for(BigInteger i = BigInteger.ZERO ; i.compareTo(coalitionSize) == -1 ; i = i.add( BigInteger.ONE)) {
            try {
                String s = contract.getCoalitionMember(coalitionAddress,i).send();
                coalitionMembers.add(s);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return coalitionMembers;
    }
    public static ArrayList<String> GetCoalitions(Credentials credentials,Web3j web3,Loyalty contract, String companyAddress){

        BigInteger coalitionCount = BigInteger.ZERO;
        try {
            coalitionCount = contract.getCompanyCoalitionCount(companyAddress).send();

        }catch (Exception e){
            e.printStackTrace();
        }

        ArrayList<String> coalitions = new ArrayList<>();


        for(BigInteger i = BigInteger.ZERO ; i.compareTo(coalitionCount) == -1 ; i = i.add( BigInteger.ONE)) {
            try {
                String coalitionAddress = contract.getCompanyCoalition(companyAddress,i).sendAsync().get();
                Tuple2<Boolean, String> coalition = contract.coalitions(coalitionAddress).send();
                CoalitionWrapper coalitionWrapper = new CoalitionWrapper(coalition,coalitionAddress);
                coalitions.add(coalitionWrapper.address);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return coalitions;
    }


    public static Map<String, TokenWrapper> tokens = new HashMap<>();

    public static TokenWrapper getToken(Web3j web3,Credentials credential,String address){

        if(tokens.containsKey(address))
            return tokens.get(address);

        Token tokenContract = Token.load(address,web3,credential,Token.GAS_PRICE,Token.GAS_LIMIT);
        String tokenName = "ERROR";
        String owner = "ERROR";
        String nominal_owner = "ERROR";
        boolean had_error = false;

        BigInteger inPrice = BigInteger.ZERO;
        BigInteger outPrice = BigInteger.ZERO;
        BigInteger exchangePrice = BigInteger.ZERO;

        try{
            tokenName = tokenContract.name().send();
            owner = tokenContract.owner().send();
            nominal_owner = tokenContract.nominal_owner().send();
            inPrice = tokenContract.inPrice().send();
            outPrice = tokenContract.outPrice().send();
            exchangePrice = tokenContract.exchangePrice().send();
        }catch (Exception e){
            had_error = true;
            e.printStackTrace();
        }


        TokenWrapper token  = new TokenWrapper(address,tokenName,owner,nominal_owner,inPrice,outPrice,exchangePrice);
        if(!had_error){
            tokens.put(address,token);
        }
        return token;
    }

    public static Map<String, Company> companyMap = new HashMap<>();

    public static Company getCompany(Web3j web3,Credentials credential,String companyAddress){
        Loyalty contract = Loyalty.load(Config.contractAdress,web3,credential,Loyalty.GAS_PRICE,Loyalty.GAS_LIMIT);

        Company company = null;
        try {
            company =new Company( contract.companies(companyAddress).send());
            companyMap.put(companyAddress,company);
        }catch (Exception e){
            e.printStackTrace();
        }
        return company;
    }

    public static Company getLastRequestedCompany(Web3j web3,Credentials credential,String companyAddress){
        if( companyMap.containsKey(companyAddress)){
            return companyMap.get(companyAddress);
        }
        return getCompany(web3,credential,companyAddress);
    }

    public static CoalitionWrapper getCoalition(Web3j web3,Credentials credential,String coalitionAddress){
        Loyalty contract = Loyalty.load(Config.contractAdress,web3,credential,Loyalty.GAS_PRICE,Loyalty.GAS_LIMIT);

        try {

            return new CoalitionWrapper(contract.coalitions(coalitionAddress).send(),coalitionAddress);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
