package com.example.nesadimsergej.test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
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
public class Token extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b50604051610935380380610935833981016040908152815160208084015192840151606085015160808601516002805433600160a060020a03199182161790915560038054909116600160a060020a038716179055949095018051939590949193909261008291600191870190610097565b50600492909255600555600655506101329050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106100d857805160ff1916838001178555610105565b82800160010185558215610105579182015b828111156101055782518255916020019190600101906100ea565b50610111929150610115565b5090565b61012f91905b80821115610111576000815560010161011b565b90565b6107f4806101416000396000f3006080604052600436106100b95763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166306fdde0381146100be5780630d4830fc1461014857806327e235e31461016f5780632828fa0e146101905780632e6f2136146101f857806370a082311461021c578063858f23c21461023d5780638da5cb5b1461026e5780639e65741e14610283578063a3ffa9cd14610298578063beabacc8146102bc578063d3d1b096146102e6575b600080fd5b3480156100ca57600080fd5b506100d36102fb565b6040805160208082528351818301528351919283929083019185019080838360005b8381101561010d5781810151838201526020016100f5565b50505050905090810190601f16801561013a5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561015457600080fd5b5061015d610388565b60408051918252519081900360200190f35b34801561017b57600080fd5b5061015d600160a060020a036004351661038e565b34801561019c57600080fd5b506040805160206004803580820135601f81018490048402850184019095528484526101f6943694929360249392840191908190840183828082843750949750508435955050506020830135926040013591506103a09050565b005b34801561020457600080fd5b506101f6600160a060020a03600435166024356103f1565b34801561022857600080fd5b5061015d600160a060020a0360043516610468565b34801561024957600080fd5b50610252610483565b60408051600160a060020a039092168252519081900360200190f35b34801561027a57600080fd5b50610252610492565b34801561028f57600080fd5b5061015d6104a1565b3480156102a457600080fd5b506101f6600160a060020a03600435166024356104a7565b3480156102c857600080fd5b506101f6600160a060020a03600435811690602435166044356104fe565b3480156102f257600080fd5b5061015d610638565b60018054604080516020600284861615610100026000190190941693909304601f810184900484028201840190925281815292918301828280156103805780601f1061035557610100808354040283529160200191610380565b820191906000526020600020905b81548152906001019060200180831161036357829003601f168201915b505050505081565b60055481565b60006020819052908152604090205481565b600254600160a060020a03163314806103c35750600354600160a060020a031633145b15156103ce57600080fd5b83516103e190600190602087019061072d565b5060049290925560055560065550565b600254600160a060020a03163314806104145750600354600160a060020a031633145b151561041f57600080fd5b600160a060020a038216600090815260208190526040902054610448908263ffffffff61063e16565b600160a060020a0390921660009081526020819052604090209190915550565b600160a060020a031660009081526020819052604090205490565b600354600160a060020a031681565b600254600160a060020a031681565b60065481565b600254600160a060020a03163314806104ca5750600354600160a060020a031633145b15156104d557600080fd5b600160a060020a038216600090815260208190526040902054610448908263ffffffff6106b616565b600254600160a060020a03163314806105215750600354600160a060020a031633145b151561052c57600080fd5b600354600160a060020a0384811691161480156105625750600354600160a060020a031660009081526020819052604090205481115b156105805760035461058090600160a060020a0316600283026103f1565b600160a060020a0383166000908152602081905260409020546105a9908263ffffffff6106b616565b600160a060020a0380851660009081526020819052604080822093909355908416815220546105de908263ffffffff61063e16565b600160a060020a038084166000818152602081815260409182902094909455805185815290519193928716927fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef92918290030190a3505050565b60045481565b818101828110156106b057604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601060248201527f496e76616c6964206164646974696f6e00000000000000000000000000000000604482015290519081900360640190fd5b92915050565b60008282111561072757604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601360248201527f496e76616c6964207375627472616374696f6e00000000000000000000000000604482015290519081900360640190fd5b50900390565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061076e57805160ff191683800117855561079b565b8280016001018555821561079b579182015b8281111561079b578251825591602001919060010190610780565b506107a79291506107ab565b5090565b6107c591905b808211156107a757600081556001016107b1565b905600a165627a7a723058206bfc0ea49e715f260c3fb012a5dca376be039722b27b0bd5fa2c95f6bd68f7d00029";

    protected Token(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Token(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public List<TransferEventResponse> getTransferEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Transfer", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<TransferEventResponse> responses = new ArrayList<TransferEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TransferEventResponse typedResponse = new TransferEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.tokens = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<TransferEventResponse> transferEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Transfer", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, TransferEventResponse>() {
            @Override
            public TransferEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                TransferEventResponse typedResponse = new TransferEventResponse();
                typedResponse.log = log;
                typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.tokens = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public List<DebugEventResponse> getDebugEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Debug", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<DebugEventResponse> responses = new ArrayList<DebugEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            DebugEventResponse typedResponse = new DebugEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.o = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._a = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse._b = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<DebugEventResponse> debugEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Debug", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, DebugEventResponse>() {
            @Override
            public DebugEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                DebugEventResponse typedResponse = new DebugEventResponse();
                typedResponse.log = log;
                typedResponse.o = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse._a = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse._b = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public RemoteCall<String> name() {
        final Function function = new Function("name", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<BigInteger> outPrice() {
        final Function function = new Function("outPrice", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> balances(String param0) {
        final Function function = new Function("balances", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> updValue(String _name, BigInteger _inPrice, BigInteger _outPrice, BigInteger _exchangePrice) {
        final Function function = new Function(
                "updValue", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_name), 
                new org.web3j.abi.datatypes.generated.Uint256(_inPrice), 
                new org.web3j.abi.datatypes.generated.Uint256(_outPrice), 
                new org.web3j.abi.datatypes.generated.Uint256(_exchangePrice)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> emitToken(String _address, BigInteger amount) {
        final Function function = new Function(
                "emitToken", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_address), 
                new org.web3j.abi.datatypes.generated.Uint256(amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> balanceOf(String _address) {
        final Function function = new Function("balanceOf", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_address)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> nominal_owner() {
        final Function function = new Function("nominal_owner", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> owner() {
        final Function function = new Function("owner", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<BigInteger> exchangePrice() {
        final Function function = new Function("exchangePrice", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> charge(String _address, BigInteger amount) {
        final Function function = new Function(
                "charge", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_address), 
                new org.web3j.abi.datatypes.generated.Uint256(amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> transfer(String from, String to, BigInteger amount) {
        final Function function = new Function(
                "transfer", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(from), 
                new org.web3j.abi.datatypes.Address(to), 
                new org.web3j.abi.datatypes.generated.Uint256(amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> inPrice() {
        final Function function = new Function("inPrice", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public static RemoteCall<Token> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _nominal_owner, String _name, BigInteger _inPrice, BigInteger _outPrice, BigInteger _exchangePrice) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_nominal_owner), 
                new org.web3j.abi.datatypes.Utf8String(_name), 
                new org.web3j.abi.datatypes.generated.Uint256(_inPrice), 
                new org.web3j.abi.datatypes.generated.Uint256(_outPrice), 
                new org.web3j.abi.datatypes.generated.Uint256(_exchangePrice)));
        return deployRemoteCall(Token.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<Token> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _nominal_owner, String _name, BigInteger _inPrice, BigInteger _outPrice, BigInteger _exchangePrice) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_nominal_owner), 
                new org.web3j.abi.datatypes.Utf8String(_name), 
                new org.web3j.abi.datatypes.generated.Uint256(_inPrice), 
                new org.web3j.abi.datatypes.generated.Uint256(_outPrice), 
                new org.web3j.abi.datatypes.generated.Uint256(_exchangePrice)));
        return deployRemoteCall(Token.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static Token load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Token(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static Token load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Token(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class TransferEventResponse {
        public Log log;

        public String from;

        public String to;

        public BigInteger tokens;
    }

    public static class DebugEventResponse {
        public Log log;

        public String o;

        public String _a;

        public BigInteger _b;
    }
}
