package com.example.nesadimsergej.test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tuples.generated.Tuple4;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import rx.Observable;
import rx.functions.Func1;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.3.1.
 */
public class Loyalty extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b50336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550611953806100606000396000f30060806040526004361061008e576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff168063336989ae14610093578063355e6ce8146100f55780638da5cb5b146101f657806395ded45c1461024d578063b831c3f9146102d0578063bbb9b58f1461037b578063c21ab7f91461040e578063dedf34f314610451575b600080fd5b34801561009f57600080fd5b506100d4600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061049e565b60405180831515151581526020018281526020019250505060405180910390f35b34801561010157600080fd5b50610136600480360381019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506104cf565b60405180851515151581526020018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200180602001838152602001828103825284818151815260200191508051906020019080838360005b838110156101b857808201518184015260208101905061019d565b50505050905090810190601f1680156101e55780820380516001836020036101000a031916815260200191505b509550505050505060405180910390f35b34801561020257600080fd5b5061020b6105c4565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561025957600080fd5b5061028e600480360381019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506105e9565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b3480156102dc57600080fd5b50610365600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff1690602001909291908035906020019092919080359060200190929190803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061061c565b6040518082815260200191505060405180910390f35b34801561038757600080fd5b5061040c600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001908201803590602001908080601f016020809104026020016040519081016040528093929190818152602001838380828437820191505050505050919291929080359060200190929190505050610c73565b005b34801561041a57600080fd5b5061044f600480360381019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506110d2565b005b34801561045d57600080fd5b5061049c600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001909291905050506114ad565b005b60016020528060005260406000206000915090508060000160009054906101000a900460ff16908060010154905082565b60026020528060005260406000206000915090508060000160009054906101000a900460ff16908060000160019054906101000a900473ffffffffffffffffffffffffffffffffffffffff1690806001018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156105b45780601f10610589576101008083540402835291602001916105b4565b820191906000526020600020905b81548152906001019060200180831161059757829003601f168201915b5050505050908060020154905084565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60036020528060005260406000206000915054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60008060008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614151561067c57600080fd5b86600160008273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900460ff161515610741576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260178152602001807f437573746f6d657220646f65736e27742065786973742e00000000000000000081525060200191505060405180910390fd5b88600260008273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900460ff161515610806576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260168152602001807f436f6d70616e7920646f65736e27742065786973742e0000000000000000000081525060200191505060405180910390fd5b600260008b73ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160019054906101000a900473ffffffffffffffffffffffffffffffffffffffff1693506000871415610b8c578973ffffffffffffffffffffffffffffffffffffffff168473ffffffffffffffffffffffffffffffffffffffff16638da5cb5b6040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b1580156108ef57600080fd5b505af1158015610903573d6000803e3d6000fd5b505050506040513d602081101561091957600080fd5b810190808051906020019092919050505073ffffffffffffffffffffffffffffffffffffffff161415610b87576109f78473ffffffffffffffffffffffffffffffffffffffff1663d3d1b0966040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b1580156109ad57600080fd5b505af11580156109c1573d6000803e3d6000fd5b505050506040513d60208110156109d757600080fd5b8101908080519060200190929190505050896117e890919063ffffffff16565b92508373ffffffffffffffffffffffffffffffffffffffff1663beabacc88b8b866040518463ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019350505050600060405180830381600087803b158015610ad057600080fd5b505af1158015610ae4573d6000803e3d6000fd5b5050505060018060008b73ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060020160008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff021916908315150217905550829450610c66565b610c65565b8973ffffffffffffffffffffffffffffffffffffffff168473ffffffffffffffffffffffffffffffffffffffff16638da5cb5b6040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b158015610c0757600080fd5b505af1158015610c1b573d6000803e3d6000fd5b505050506040513d6020811015610c3157600080fd5b810190808051906020019092919050505073ffffffffffffffffffffffffffffffffffffffff161415610c6357610c64565b5b5b5b5050505095945050505050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610cce57600080fd5b82600260008273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900460ff16151515610d94576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260178152602001807f436f6d70616e7920616c7265616479206578697374732e00000000000000000081525060200191505060405180910390fd5b83600160008273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900460ff16151515610e5a576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260188152602001807f437573746f6d657220616c7265616479206578697374732e000000000000000081525060200191505060405180910390fd5b6001600260008773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160006101000a81548160ff02191690831515021790555083600260008773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206001019080519060200190610f0b929190611882565b5082600260008773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600201819055507f9d70bee0bb385f274571a8a687d1021a577dfcda5c0005f2d367eff2a390bcf885600260008873ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600101600160008973ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060010154604051808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001806020018381526020018281038252848181546001816001161561010002031660029004815260200191508054600181600116156101000203166002900480156110bb5780601f10611090576101008083540402835291602001916110bb565b820191906000526020600020905b81548152906001019060200180831161109e57829003601f168201915b505094505050505060405180910390a15050505050565b33600260008273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900460ff161515611197576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260168152602001807f436f6d70616e7920646f65736e27742065786973742e0000000000000000000081525060200191505060405180910390fd5b600073ffffffffffffffffffffffffffffffffffffffff16600260003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160019054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16638da5cb5b6040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b15801561127557600080fd5b505af1158015611289573d6000803e3d6000fd5b505050506040513d602081101561129f57600080fd5b810190808051906020019092919050505073ffffffffffffffffffffffffffffffffffffffff161415156112d257600080fd5b3373ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff16638da5cb5b6040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b15801561134d57600080fd5b505af1158015611361573d6000803e3d6000fd5b505050506040513d602081101561137757600080fd5b810190808051906020019092919050505073ffffffffffffffffffffffffffffffffffffffff161415156113aa57600080fd5b81600260003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160016101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555081600360003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614151561150857600080fd5b81600160008273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900460ff161515156115ce576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260188152602001807f437573746f6d657220616c7265616479206578697374732e000000000000000081525060200191505060405180910390fd5b82600260008273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900460ff16151515611694576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260178152602001807f436f6d70616e7920616c7265616479206578697374732e00000000000000000081525060200191505060405180910390fd5b60018060008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160006101000a81548160ff02191690831515021790555082600160008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600101819055507fb01f03e9e9f2b7bc3cc53c04150b1bb15c120b77d3fa2edadbfaf05c799a0da584600160008773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060010154604051808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019250505060405180910390a150505050565b600081830290506000831480611808575081838281151561180557fe5b04145b151561187c576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260168152602001807f496e76616c6964206d756c7469706c69636174696f6e0000000000000000000081525060200191505060405180910390fd5b92915050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106118c357805160ff19168380011785556118f1565b828001600101855582156118f1579182015b828111156118f05782518255916020019190600101906118d5565b5b5090506118fe9190611902565b5090565b61192491905b80821115611920576000816000905550600101611908565b5090565b905600a165627a7a723058207b018c8f9c7f0214c89ae5127946797da3621a9e34c688a97385c3643268c0060029";

    protected Loyalty(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Loyalty(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public List<AddCompanyEventResponse> getAddCompanyEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("AddCompany",
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}));
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<AddCompanyEventResponse> responses = new ArrayList<AddCompanyEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AddCompanyEventResponse typedResponse = new AddCompanyEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.companyAddress = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.name = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.phoneNumber = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<AddCompanyEventResponse> addCompanyEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("AddCompany",
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, AddCompanyEventResponse>() {
            @Override
            public AddCompanyEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                AddCompanyEventResponse typedResponse = new AddCompanyEventResponse();
                typedResponse.log = log;
                typedResponse.companyAddress = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.name = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.phoneNumber = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public List<AddCustomerEventResponse> getAddCustomerEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("AddCustomer",
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<AddCustomerEventResponse> responses = new ArrayList<AddCustomerEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AddCustomerEventResponse typedResponse = new AddCustomerEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.customerAddress = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.number = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<AddCustomerEventResponse> addCustomerEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("AddCustomer",
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, AddCustomerEventResponse>() {
            @Override
            public AddCustomerEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                AddCustomerEventResponse typedResponse = new AddCustomerEventResponse();
                typedResponse.log = log;
                typedResponse.customerAddress = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.number = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public List<LoggedInEventResponse> getLoggedInEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("LoggedIn",
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<LoggedInEventResponse> responses = new ArrayList<LoggedInEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            LoggedInEventResponse typedResponse = new LoggedInEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._address = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.number = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<LoggedInEventResponse> loggedInEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("LoggedIn",
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, LoggedInEventResponse>() {
            @Override
            public LoggedInEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                LoggedInEventResponse typedResponse = new LoggedInEventResponse();
                typedResponse.log = log;
                typedResponse._address = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.number = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public RemoteCall<Tuple2<Boolean, BigInteger>> customers(String param0) {
        final Function function = new Function("customers",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}, new TypeReference<Uint256>() {}));
        return new RemoteCall<Tuple2<Boolean, BigInteger>>(
                new Callable<Tuple2<Boolean, BigInteger>>() {
                    @Override
                    public Tuple2<Boolean, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<Boolean, BigInteger>(
                                (Boolean) results.get(0).getValue(),
                                (BigInteger) results.get(1).getValue());
                    }
                });
    }

    public RemoteCall<Tuple4<Boolean, String, String, BigInteger>> companies(String param0) {
        final Function function = new Function("companies",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}, new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}));
        return new RemoteCall<Tuple4<Boolean, String, String, BigInteger>>(
                new Callable<Tuple4<Boolean, String, String, BigInteger>>() {
                    @Override
                    public Tuple4<Boolean, String, String, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple4<Boolean, String, String, BigInteger>(
                                (Boolean) results.get(0).getValue(),
                                (String) results.get(1).getValue(),
                                (String) results.get(2).getValue(),
                                (BigInteger) results.get(3).getValue());
                    }
                });
    }

    public RemoteCall<String> owner() {
        final Function function = new Function("owner",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> allTokens(String param0) {
        final Function function = new Function("allTokens",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> transferBonuses(String company, String customer, BigInteger roublesAmount, BigInteger bonusesAmount, String tokenOwner) {
        final Function function = new Function(
                "transferBonuses",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(company),
                        new org.web3j.abi.datatypes.Address(customer),
                        new org.web3j.abi.datatypes.generated.Uint256(roublesAmount),
                        new org.web3j.abi.datatypes.generated.Uint256(bonusesAmount),
                        new org.web3j.abi.datatypes.Address(tokenOwner)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> addCompany(String company, String _name, BigInteger _phoneNumber) {
        final Function function = new Function(
                "addCompany",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(company),
                        new org.web3j.abi.datatypes.Utf8String(_name),
                        new org.web3j.abi.datatypes.generated.Uint256(_phoneNumber)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> createToken(String token) {
        final Function function = new Function(
                "createToken",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(token)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> addCustomer(String customer, BigInteger _phoneNumber) {
        final Function function = new Function(
                "addCustomer",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(customer),
                        new org.web3j.abi.datatypes.generated.Uint256(_phoneNumber)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static RemoteCall<Loyalty> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Loyalty.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<Loyalty> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Loyalty.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static Loyalty load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Loyalty(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static Loyalty load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Loyalty(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class AddCompanyEventResponse {
        public Log log;

        public String companyAddress;

        public String name;

        public BigInteger phoneNumber;
    }

    public static class AddCustomerEventResponse {
        public Log log;

        public String customerAddress;

        public BigInteger number;
    }

    public static class LoggedInEventResponse {
        public Log log;

        public String _address;

        public BigInteger number;
    }
}
