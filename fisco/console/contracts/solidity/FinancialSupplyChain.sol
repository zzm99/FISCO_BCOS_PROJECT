pragma solidity ^0.4.24;

contract FinancialSupplyChain {

	// 企业数据结构
	struct Company {
		uint256 company_id;		// 企业ID
		string company_name;	// 企业名称
        address company_address;	// 企业“地址”
		uint256 company_fund;		// 企业资产
		uint256 company_receipts_in;	// 单据收入（别人欠我的钱）
		uint256 company_receipts_out;	// 单据支出（我欠别人的钱）
		mapping (address => Receipt) receipts;	// 企业账款单据表（谁欠我钱）
	}

	// 账款单据数据结构： from 欠 to 钱 money
	struct Receipt {
		bool valid;		// 有效标志
		address from;	// 来源企业“地址”
		address to;		// 传入企业“地址”
		uint256 money;	// 账款单据金额
	}

	// 便于查询的MAP
	mapping(address => Company) public Address2Company;
	mapping(uint256 => Company) public Id2Company;
	mapping(bytes32 => address) public String2Address;

	// 将使用到的企业地址
	address[6] private myAddress = [
		0x41EA12CC9310D97F650E23E24DAB90742B184621,
		0x968AF62EE21DA34AC5CB7EF1BE29A1894F4CBA94,
		0xB413CD139813FF849EA2346FC46EE65FA3A2CBEF,
		0xA3611B445CD23EF643FE7631DC288946B23A4209,
		0xBB2B34C6445CD23E895389164ABAFF3C8946B209,
		0xDD611B447422BBE643AD325E7631DC288946A209
	];

	constructor() {
		// 创建银行
		Id2Company[1] = Company(1, "Bank", myAddress[0], 1000000, 0, 0);
		Address2Company[myAddress[0]] = Id2Company[1];
		String2Address[stringToBytes32("Bank")] = myAddress[0];
		// 创建汽车公司
		Id2Company[2] = Company(2, "CarCompany", myAddress[1], 100000, 0, 0);
		Address2Company[myAddress[1]] = Id2Company[2];
		String2Address[stringToBytes32("CarCompany")] = myAddress[1];
		// 创建轮胎公司
		Id2Company[3] = Company(3, "TireCompany", myAddress[2], 100000, 0, 0);
		Address2Company[myAddress[2]] = Id2Company[3];
		String2Address[stringToBytes32("TireCompany")] = myAddress[2];
		// 创建轮毂公司
		Id2Company[4] = Company(4, "HubCompany", myAddress[3], 100000, 0, 0);
		Address2Company[myAddress[3]] = Id2Company[4];
		String2Address[stringToBytes32("HubCompany")] = myAddress[3];
		// 创建铝锭公司
		Id2Company[5] = Company(5, "IngotCompany", myAddress[4], 100000, 0, 0);
		Address2Company[myAddress[4]] = Id2Company[5];
		String2Address[stringToBytes32("IngotCompany")] = myAddress[4];
		// 创建铝矿公司
		Id2Company[6] = Company(6, "AluminumCompany", myAddress[5], 100000, 0, 0);
		Address2Company[myAddress[5]] = Id2Company[6];
		String2Address[stringToBytes32("AluminumCompany")] = myAddress[5];
	}

	// uint256 转化为 string
	function Int2String(uint256 num) public returns(string) {
		bytes memory ret = new bytes(32);
		assembly { mstore(add(ret, 32), num) }
		return string(ret);
	}

	// string 转 byte32 : 原因： map不支持 不确定长度的数据类型
	function stringToBytes32(string memory source) returns (bytes32 result) {
    	assembly { result := mload(add(source, 32)) }
  	}

	// 获取企业的资产
	function getCompanyFund(string company_name) public returns(string) {
		return Int2String(Address2Company[String2Address[stringToBytes32(company_name)]].company_fund);
	}

	// 获取企业的单据收入
	function getCompanyReceiptIn(string company_name) public returns(string) {
		return Int2String(Address2Company[String2Address[stringToBytes32(company_name)]].company_receipts_in);
	}

	// 获取企业的单据支出
	function getCompanyReceiptOut(string company_name) public returns(string) {
		return Int2String(Address2Company[String2Address[stringToBytes32(company_name)]].company_receipts_out);
	}

	// 获取账款单据的金额
	function getReceiptMoney(string from_company, string to_company) public returns(string) {
		address from_company_address = String2Address[stringToBytes32(from_company)];
		address to_company_address = String2Address[stringToBytes32(to_company)];

		if(Address2Company[to_company_address].receipts[from_company_address].valid) {
			return Int2String(Address2Company[to_company_address].receipts[from_company_address].money);
		}

		return "0";
	}

	// 生成单据
	function createReceipt(string from_company, string to_company, int money_int) public {
		uint256 money = uint256(money_int);
		address from_company_address = String2Address[stringToBytes32(from_company)];
		address to_company_address = String2Address[stringToBytes32(to_company)];

		if(Address2Company[to_company_address].receipts[from_company_address].valid) {
			Address2Company[to_company_address].receipts[from_company_address].money += money;
		} else {
			Address2Company[to_company_address].receipts[from_company_address] = Receipt(true, from_company_address, to_company_address, money);
		}

		Address2Company[from_company_address].company_receipts_out += money;
		Address2Company[to_company_address].company_receipts_in += money;
	}

	// 自定金额的单据转移: B 欠 A 钱，A 转移其中部分或全部给C，使得B 也欠C钱
	function transferReceipt(string company_A, string company_B, string company_C, int money_int) public returns(string){
		uint256 money = uint256(money_int);
		address company_A_address = String2Address[stringToBytes32(company_A)];
		address company_B_address = String2Address[stringToBytes32(company_B)];
		address company_C_address = String2Address[stringToBytes32(company_C)];
		
		if(Address2Company[company_A_address].receipts[company_B_address].valid == false) {
			return "Failure";
		}

		if(Address2Company[company_A_address].receipts[company_B_address].money < money) {
			return "Failure";
		}

		Address2Company[company_A_address].receipts[company_B_address].money -= money;
		if(Address2Company[company_A_address].receipts[company_B_address].money == 0) {
			Address2Company[company_A_address].receipts[company_B_address].valid = false;
		}
		Address2Company[company_A_address].company_receipts_in -= money;

		if(Address2Company[company_C_address].receipts[company_B_address].valid) {
			Address2Company[company_C_address].receipts[company_B_address].money += money;
		} else {
			Address2Company[company_C_address].receipts[company_B_address] = Receipt(true, company_B_address, company_C_address, money);
		}
		Address2Company[company_C_address].company_receipts_in += money;

		return "Success";
	}


	// 凭借别人给我的欠条单据来向银行拿钱
	function financingWithReceipt(string company_name, int money_int) public returns(string) {
		uint256 money = uint256(money_int);
		address cur_company_address = String2Address[stringToBytes32(company_name)];
		address bank_company_address = String2Address[stringToBytes32("Bank")];

		if(Address2Company[cur_company_address].company_receipts_in < money || Address2Company[bank_company_address].company_fund < money) {
			return "Failure";
		}

		if(Address2Company[bank_company_address].receipts[cur_company_address].valid) {
			Address2Company[bank_company_address].receipts[cur_company_address].money += money;
		} else {
			Address2Company[bank_company_address].receipts[cur_company_address] = Receipt(true, cur_company_address, Address2Company[bank_company_address].company_address, money);
		}
		Address2Company[cur_company_address].company_receipts_out += money;
		Address2Company[cur_company_address].company_fund += money;
		Address2Company[bank_company_address].company_receipts_in += money;
		Address2Company[bank_company_address].company_fund -= money;
		return "Success";
	}

	// 履行账款单据，清除欠款
	function clearReceipt(string from_company, string to_company) public returns(string) {
		address from_company_address = String2Address[stringToBytes32(from_company)];
		address to_company_address = String2Address[stringToBytes32(to_company)];

		if(Address2Company[to_company_address].receipts[from_company_address].valid == false) {
			return "Failure";
		}

		if(Address2Company[from_company_address].company_fund < Address2Company[to_company_address].receipts[from_company_address].money) {
			return "Failure";			
		}

		uint256 receipt_money = Address2Company[to_company_address].receipts[from_company_address].money;
		Address2Company[to_company_address].receipts[from_company_address].money = 0;
		Address2Company[to_company_address].receipts[from_company_address].valid = false;
		Address2Company[to_company_address].company_fund += receipt_money;
		Address2Company[to_company_address].company_receipts_in -= receipt_money;
		Address2Company[from_company_address].company_fund -= receipt_money;
		Address2Company[from_company_address].company_receipts_out -= receipt_money;
		return "Success";
	}
}