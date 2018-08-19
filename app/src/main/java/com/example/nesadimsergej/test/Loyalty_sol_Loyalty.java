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
public class Loyalty_sol_Loyalty extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b50336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550611a3a806100606000396000f300608060405260043610610078576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff168063103f2ea41461007d578063355e6ce8146100aa57806372b3b620146101ab57806380f5e4901461021e578063bbb9b58f1461028b578063dedf34f31461031e575b600080fd5b34801561008957600080fd5b506100a86004803603810190808035906020019092919050505061036b565b005b3480156100b657600080fd5b506100eb600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610475565b60405180851515151581526020018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200180602001838152602001828103825284818151815260200191508051906020019080838360005b8381101561016d578082015181840152602081019050610152565b50505050905090810190601f16801561019a5780820380516001836020036101000a031916815260200191505b509550505050505060405180910390f35b3480156101b757600080fd5b5061021c600480360381019080803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091929192908035906020019092919050505061056a565b005b34801561022a57600080fd5b50610289600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610751565b005b34801561029757600080fd5b5061031c600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001908201803590602001908080601f016020809104026020016040519081016040528093929190818152602001838380828437820191505050505050919291929080359060200190929190505050610b87565b005b34801561032a57600080fd5b50610369600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610fe6565b005b80600160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206001015414806103fc575080600260003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060020154145b151561040757600080fd5b7f6185086b13806a5922b8314f9fff4b38b2fff65384339340a1479f7ed3490aab3382604051808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019250505060405180910390a150565b60026020528060005260406000206000915090508060000160009054906101000a900460ff16908060000160019054906101000a900473ffffffffffffffffffffffffffffffffffffffff1690806001018054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561055a5780601f1061052f5761010080835404028352916020019161055a565b820191906000526020600020905b81548152906001019060200180831161053d57829003601f168201915b5050505050908060020154905084565b33600160008273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900460ff16151561062f576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260168152602001807f436f6d70616e7920646f65736e27742065786973742e0000000000000000000081525060200191505060405180910390fd5b8282610639611321565b8080602001838152602001828103825284818151815260200191508051906020019080838360005b8381101561067c578082015181840152602081019050610661565b50505050905090810190601f1680156106a95780820380516001836020036101000a031916815260200191505b509350505050604051809103906000f0801580156106cb573d6000803e3d6000fd5b50600260003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160016101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550505050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156107ac57600080fd5b81600160008273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900460ff161515610871576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260178152602001807f437573746f6d657220646f65736e27742065786973742e00000000000000000081525060200191505060405180910390fd5b83600160008273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900460ff161515610936576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260168152602001807f436f6d70616e7920646f65736e27742065786973742e0000000000000000000081525060200191505060405180910390fd5b600260003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160019054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663beabacc88686866040518463ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019350505050600060405180830381600087803b158015610a6f57600080fd5b505af1158015610a83573d6000803e3d6000fd5b5050505060018060008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206002016000600260008973ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160019054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff0219169083151502179055505050505050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610be257600080fd5b82600260008273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900460ff16151515610ca8576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260178152602001807f436f6d70616e7920616c7265616479206578697374732e00000000000000000081525060200191505060405180910390fd5b83600160008273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900460ff16151515610d6e576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260188152602001807f437573746f6d657220616c7265616479206578697374732e000000000000000081525060200191505060405180910390fd5b6001600260008773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160006101000a81548160ff02191690831515021790555083600260008773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206001019080519060200190610e1f929190611331565b5082600260008773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600201819055507f9d70bee0bb385f274571a8a687d1021a577dfcda5c0005f2d367eff2a390bcf885600260008873ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600101600160008973ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060010154604051808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200180602001838152602001828103825284818154600181600116156101000203166002900481526020019150805460018160011615610100020316600290048015610fcf5780601f10610fa457610100808354040283529160200191610fcf565b820191906000526020600020905b815481529060010190602001808311610fb257829003601f168201915b505094505050505060405180910390a15050505050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614151561104157600080fd5b81600160008273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900460ff16151515611107576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260188152602001807f437573746f6d657220616c7265616479206578697374732e000000000000000081525060200191505060405180910390fd5b82600260008273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900460ff161515156111cd576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260178152602001807f436f6d70616e7920616c7265616479206578697374732e00000000000000000081525060200191505060405180910390fd5b60018060008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160006101000a81548160ff02191690831515021790555082600160008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600101819055507fb01f03e9e9f2b7bc3cc53c04150b1bb15c120b77d3fa2edadbfaf05c799a0da584600160008773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060010154604051808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019250505060405180910390a150505050565b604051610638806113d783390190565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061137257805160ff19168380011785556113a0565b828001600101855582156113a0579182015b8281111561139f578251825591602001919060010190611384565b5b5090506113ad91906113b1565b5090565b6113d391905b808211156113cf5760008160009055506001016113b7565b5090565b905600608060405234801561001057600080fd5b50604051610638380380610638833981018060405281019080805182019291906020018051906020019092919050505033600260006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508160019080519060200190610097929190610105565b508060036000600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000208190555050506101aa565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061014657805160ff1916838001178555610174565b82800160010185558215610174579182015b82811115610173578251825591602001919060010190610158565b5b5090506101819190610185565b5090565b6101a791905b808211156101a357600081600090555060010161018b565b5090565b90565b61047f806101b96000396000f30060806040526004361061004c576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806370a0823114610051578063beabacc8146100a8575b600080fd5b34801561005d57600080fd5b50610092600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610115565b6040518082815260200191505060405180910390f35b3480156100b457600080fd5b50610113600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff1690602001909291908035906020019092919050505061015d565b005b60008060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020549050919050565b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156101b957600080fd5b61020a816000808673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205461034990919063ffffffff16565b6000808573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000208190555061029d816000808573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020546103ce90919063ffffffff16565b6000808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055508173ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef836040518082815260200191505060405180910390a3505050565b60008282111515156103c3576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260138152602001807f496e76616c6964207375627472616374696f6e0000000000000000000000000081525060200191505060405180910390fd5b818303905092915050565b6000818301905082811015151561044d576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260108152602001807f496e76616c6964206164646974696f6e0000000000000000000000000000000081525060200191505060405180910390fd5b929150505600a165627a7a72305820a9f46f59c7b82d52d0730ec8f6b53cdb356caab1a1698de8090d5bdbec0697440029a165627a7a72305820aa8481af42d69e4f7f7d76d9216e17d5998f190fe3f87dc224bef1f2852343140029";

    protected Loyalty_sol_Loyalty(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Loyalty_sol_Loyalty(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
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

    public RemoteCall<TransactionReceipt> logIn(BigInteger phoneNumber) {
        final Function function = new Function(
                "logIn",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(phoneNumber)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
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

    public RemoteCall<TransactionReceipt> createToken(String _name, BigInteger ownerPolicy) {
        final Function function = new Function(
                "createToken",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_name),
                        new org.web3j.abi.datatypes.generated.Uint256(ownerPolicy)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> chargeBonuses(String company, String customer, BigInteger amount) {
        final Function function = new Function(
                "chargeBonuses",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(company),
                        new org.web3j.abi.datatypes.Address(customer),
                        new org.web3j.abi.datatypes.generated.Uint256(amount)),
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

    public RemoteCall<TransactionReceipt> addCustomer(String customer, BigInteger _phoneNumber) {
        final Function function = new Function(
                "addCustomer",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(customer),
                        new org.web3j.abi.datatypes.generated.Uint256(_phoneNumber)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static RemoteCall<Loyalty_sol_Loyalty> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Loyalty_sol_Loyalty.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<Loyalty_sol_Loyalty> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Loyalty_sol_Loyalty.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static Loyalty_sol_Loyalty load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Loyalty_sol_Loyalty(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static Loyalty_sol_Loyalty load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Loyalty_sol_Loyalty(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
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
