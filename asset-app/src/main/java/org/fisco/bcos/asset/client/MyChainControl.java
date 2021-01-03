package org.fisco.bcos.asset.client;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Properties;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.fisco.bcos.asset.contract.Asset;
import org.fisco.bcos.asset.contract.Asset.RegisterEventEventResponse;
import org.fisco.bcos.asset.contract.Asset.TransferEventEventResponse;
import org.fisco.bcos.channel.client.Service;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.crypto.Keys;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.channel.ChannelEthereumService;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.tuples.generated.Tuple2;
import org.fisco.bcos.web3j.tx.gas.StaticGasProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import org.fisco.bcos.asset.contract.FinancialSupplyChain;

public class MyChainControl {
	static Logger logger = LoggerFactory.getLogger(AssetClient.class);

    private Web3j web3j;
    
    private String username;

	private Credentials credentials;

	public Web3j getWeb3j() {
		return web3j;
	}

	public void setWeb3j(Web3j web3j) {
		this.web3j = web3j;
	}

	public Credentials getCredentials() {
		return credentials;
	}

	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}

	public void recordAssetAddr(String address) throws FileNotFoundException, IOException {
		Properties prop = new Properties();
		prop.setProperty("address", address);
		final Resource contractResource = new ClassPathResource("contract.properties");
		FileOutputStream fileOutputStream = new FileOutputStream(contractResource.getFile());
		prop.store(fileOutputStream, "contract address");
	}

	public String loadAssetAddr() throws Exception {
		// load Asset contact address from contract.properties
		Properties prop = new Properties();
		final Resource contractResource = new ClassPathResource("contract.properties");
		prop.load(contractResource.getInputStream());

		String contractAddress = prop.getProperty("address");
		if (contractAddress == null || contractAddress.trim().equals("")) {
			throw new Exception(" load Asset contract address failed, please deploy it first. ");
		}
		logger.info(" load Asset address from contract.properties, address is {}", contractAddress);
		return contractAddress;
	}

	public void initialize() throws Exception {

		// init the Service
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		Service service = context.getBean(Service.class);
		service.run();

		ChannelEthereumService channelEthereumService = new ChannelEthereumService();
		channelEthereumService.setChannelService(service);
		Web3j web3j = Web3j.build(channelEthereumService, 1);

		// init Credentials
		Credentials credentials = Credentials.create(Keys.createEcKeyPair());

		setCredentials(credentials);
		setWeb3j(web3j);

		logger.debug(" web3j is " + web3j + " ,credentials is " + credentials);
	}

	private static BigInteger gasPrice = new BigInteger("30000000");
	private static BigInteger gasLimit = new BigInteger("30000000");

	public void deployAssetAndRecordAddr() {

		try {
			FinancialSupplyChain asset = FinancialSupplyChain.deploy(web3j, credentials, gasPrice, gasLimit).send();
			System.out.println(" deploy FinancialSupplyChain success, contract address is " + asset.getContractAddress());

			recordAssetAddr(asset.getContractAddress());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			System.out.println(" deploy FinancialSupplyChain contract failed, error message is  " + e.getMessage());
		}
	}

	public String formatInt(String str) {
        int result = 0;
        String tmp = "";
        boolean flag = true;
        int len = str.length();
        String hex_string = "0123456789abcdef";
        for (int i = len - 15; i < str.length(); i++) {
            if (flag) {
                if(str.charAt(i) != '0') {
                    flag = false;
                    tmp += str.charAt(i);
                }
            } else {
                tmp += str.charAt(i);
            }
        }
        for (int i = 0; i < tmp.length(); i++) {
            result *= 16;
            result += (hex_string.indexOf(tmp.charAt(i)));
        }
        return String.valueOf(result);
    }

    public String formatWord(String str) {
        int cnt = 0;
        String tmp = "";
        Boolean flag = true;
        String hex_string = "0123456789abcdef";
        if (str != "") {
            for(int i = 2; i < str.length(); i++) {
                if (flag) {
                    if(str.charAt(i)!='0') {
                        flag = false;
                        tmp += str.charAt(i);
                    }
                } else {
                    tmp += str.charAt(i);
                }
            }
            str = tmp;
        }
        char[] str_cArr = str.toCharArray();
        byte[] str_bytes = new byte[str.length() / 2];
		for (int i = 0; i < str_bytes.length; i++) {
			cnt = hex_string.indexOf(str_cArr[2 * i]) * 16;
			cnt += hex_string.indexOf(str_cArr[2 * i + 1]);
			str_bytes[i] = (byte) (cnt & 0xff);
		}
		return new String(str_bytes);
    }

    public void myChainInit() {
    	try {
    		initialize();
    		deployAssetAndRecordAddr();
    		String contractAddr = loadAssetAddr();
            FinancialSupplyChain contr = FinancialSupplyChain.load(contractAddr, web3j, credentials, gasPrice, gasLimit);
    	} catch (Exception e) {
    		System.out.println("error: "+e.getMessage());
    	}
    }

    public void loginControl(String company_name) {
        username = company_name;
    }

    public String getCompanyFundControl() {
    	try {
			String contractAddr = loadAssetAddr();
            FinancialSupplyChain contr = FinancialSupplyChain.load(contractAddr, web3j, credentials, gasPrice, gasLimit);
            TransactionReceipt result = contr.getCompanyFund(username).send();
            return formatInt(result.getOutput());
		} catch (Exception e) {
			System.out.println("error: "+e.getMessage());
        }
        return "0";
    }

    public String getReceiptMoneyControl(String from_company) {
    	try {
			String contractAddr = loadAssetAddr();
            FinancialSupplyChain contr = FinancialSupplyChain.load(contractAddr, web3j, credentials, gasPrice, gasLimit);
            TransactionReceipt result = contr.getReceiptMoney(from_company, username).send();
            return formatInt(result.getOutput());
		} catch (Exception e) {
			System.out.println("error: "+e.getMessage());
        }
        return "0";
    }

    public void createReceiptControl(String to_company, int money) { 
        try {
			String contractAddr = loadAssetAddr();
            FinancialSupplyChain contr = FinancialSupplyChain.load(contractAddr, web3j, credentials, gasPrice, gasLimit);
        	contr.createReceipt(username, to_company, BigInteger.valueOf(money)).send();
		} catch (Exception e) {
			System.out.println("error: "+e.getMessage());
		}
    }

    public void transferReceiptControl(String company_B, String company_C, int money) {
        try {
			String contractAddr = loadAssetAddr();
            FinancialSupplyChain contr = FinancialSupplyChain.load(contractAddr, web3j, credentials, gasPrice, gasLimit);
            contr.transferReceipt(username, company_B, company_C, BigInteger.valueOf(money)).send();
		} catch (Exception e) {
			System.out.println("error: "+e.getMessage());
		}
    }

    public String financingWithReceiptControl(int money) {
        try {
			String contractAddr = loadAssetAddr();
            FinancialSupplyChain contr = FinancialSupplyChain.load(contractAddr, web3j, credentials, gasPrice, gasLimit);
            TransactionReceipt result = contr.financingWithReceipt(username, BigInteger.valueOf(money)).send();
            return formatWord(result.getOutput());
		} catch (Exception e) {
			System.out.println("error: "+e.getMessage());
        }
        return "false";
    }

    public String clearReceiptControl(String from_company) {
        try {
			String contractAddr = loadAssetAddr();
            FinancialSupplyChain contr = FinancialSupplyChain.load(contractAddr, web3j, credentials, gasPrice, gasLimit);
            TransactionReceipt result = contr.clearReceipt(from_company, username).send();
            return formatWord(result.getOutput());
		} catch (Exception e) {
			System.out.println("error: "+e.getMessage());
        }
        return "false";
    }
}