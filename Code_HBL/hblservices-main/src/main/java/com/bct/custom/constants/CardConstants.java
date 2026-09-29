package com.bct.custom.constants;

public class CardConstants {
	public static final String CARD_EMI_ERR_000 = "Success";
	public static final String CARD_EMI_ERR_001 = "USER NOT FOUND";
	public static final String CARD_EMI_ERR_002 = "Card Code not valid";
	public static final String CARD_EMI_ERR_003 = "Card number not valid";
	public static final String CARD_EMI_ERR_004 = "Card doesn’t belong to the bank";
	public static final String CARD_EMI_ERR_005 = "Card not active";
	public static final String CARD_EMI_ERR_006 = "CARD OPPOSED";
	public static final String CARD_EMI_ERR_007 = "CARD EXPIRED";
	public static final String CARD_EMI_ERR_008 = "Invalid Currency";
	public static final String CARD_EMI_ERR_009 = "Invalid Cardholder Routing";
	public static final String CARD_EMI_ERR_010 = "Invalid Account";
	public static final String CARD_EMI_ERR_011 = "Entred Currency is different from Account Currency";
	public static final String CARD_EMI_ERR_012 = "Invalid Card Type";
	public static final String CARD_EMI_ERR_013 = "Invalid Transaction Amount";
	public static final String CARD_EMI_ERR_014 = "Invalid Account Program";
	public static final String CARD_EMI_ERR_015 = "Invalid Bank";
	public static final String CARD_EMI_ERR_016 = "Charge Revolve Transation Not Found";
	public static final String CARD_EMI_ERR_017 = "Installement Number should be greater than 0";
	public static final String CARD_EMI_ERR_018 = "Cannot Found Valid Transaction";
	public static final String CARD_EMI_ERR_019 = "No routing for this customer";
	public static final String CARD_EMI_ERR_020 = "Invalid Input for - [Param]";
	public static final String CARD_EMI_ERR_021 = "[Param] is NULL";
	public static final String CARD_EMI_ERR_022 = "EMI Parameter not found";
	public static final String CARD_EMI_ERR_023 = "EMI Fee not found";
	public static final String CARD_EMI_ERR_024 = "Authorization Not Found";
	public static final String CARD_EMI_ERR_025 = "Amount of Transaction Not Allow This EMI Request";
	public static final String CARD_EMI_ERR_026 = "Number of Unpaid Terms Not Allow This EMI Request";
	public static final String CARD_EMI_ERR_027 = "Instalment number should be less than expiry date of the card";
	public static final String CARD_EMI_ERR_99 = "System Error";

	public static String errMessage(String code) {
		switch (code) {
		case "000":
			return "Success";
		case "001":
			return "USER NOT FOUND";
		case "002":
			return "Card Code not valid";
		case "003":
			return "Card number not valid";
		case "004":
			return "Card doesn’t belong to the bank";
		case "005":
			return "Card not active";
		case "006":
			return "CARD OPPOSED";
		case "007":
			return "CARD EXPIRED";
		case "008":
			return "Invalid Currency";
		case "009":
			return "Invalid Cardholder Routing";
		case "010":
			return "Invalid Account";
		case "011":
			return "Entred Currency is different from Account Currency";
		case "012":
			return "Invalid Card Type";
		case "013":
			return "Invalid Transaction Amount";
		case "014":
			return "Invalid Transaction Amount";
		case "015":
			return "Invalid Bank";
		case "016":
			return "Charge Revolve Transation Not Found";
		case "017":
			return "Installement Number should be greater than 0";
		case "018":
			return "Cannot Found Valid Transaction";
		case "019":
			return "No routing for this customer";
		case "020":
			return "Invalid Input for - [Param]";
		case "021":
			return "[Param] is NULL";
		case "022":
			return "EMI Parameter not found";
		case "023":
			return "EMI Fee not found";
		case "024":
			return "Authorization Not Found";
		case "025":
			return "Amount of Transaction Not Allow This EMI Request";
		case "026":
			return "Number of Unpaid Terms Not Allow This EMI Request";
		case "027":
			return "Instalment number should be less than expiry date of the card";
		case "999":
			return "System Error";
		default:
			// Unknown Error message ID
			return "System Error";
		}
	}
	
	public static String CardStausChangeErrMessage(String code) {
		switch (code) {
		case "000":
			return "Success";
		case "001":
			return "Card not found";
		case "002":
			return "System Error";
		case "003":
			return "Input data not valid";
		case "004":
			return "Card Hot Listed";
		case "005":
			return "Card Status Not Authorized";
		case "006":
			return "Card Not Linked To Bank";
		case "007":
			return "User Not Found";
		case "008":
			return "CARD REF NUMBER NOT VALID";
		case "009":
			return "CARD NOT LINKED TO BANK";
		case "010":
			return "INPUT DATA NOT VALID";
		default:
			// Unknown Error message ID
			return "System Error";
		}
	}
	
	public static String StmtEnquiryErrMessage(String code) {
		switch (code) {
		case "000":
			return "Success";
		case "001":
			return "Card not found";
		case "002":
			return "System Error";
		case "003":
			return "Input data not valid";
		case "004":
			return "Input Data Not Valid";
		case "005":
			return "SYSTEM PROBLEM";
		case "007":
			return "User Not Found";
		case "009":
			return "Costumer doesn’t belong to the bank";
		default:
			// Unknown Error message ID
			return "System Error";
		}
	}
	
	public static String PendingTransErrMessage(String code) {
		switch (code) {
		case "000":
			return "Success";
		case "001":
			return "USER NOT FOUND";
		case "002":
			return "Card Code not valid";
		case "003":
			return "Card number not valid";
		case "004":
			return "Card doesn’t belong to the bank";
		case "005":
			return "Card not active";
		case "006":
			return "CARD OPPOSED";
		case "007":
			return "CARD EXPIRED";
		case "999":
			return "System Error";
		default:
			// Unknown Error message ID
			return "System Error";
		}
	}
	
	public static String TopupErrMessage(String code) {
		switch (code) {
		case "000":
			return "Success";
		case "001":
			return "USER NOT FOUND";
		case "002":
			return "Card Code not valid";
		case "003":
			return "Card number not valid";
		case "004":
			return "Card doesn’t belong to the bank";
		case "005":
			return "Card not active";
		case "006":
			return "CARD OPPOSED";
		case "007":
			return "CARD EXPIRED";
		case "008":
			return "Invalid Currency";
		case "009":
			return "Invalid Cardholder Routing";
		case "010":
			return "Invalid Account";
		case "011":
			return "Entred Currency is different from Account Currency";
		case "012":
			return "Invalid Card Type";
		case "013":
			return "Invalid Transaction Amount";
		case "014":
			return "Invalid Account Program";
		case "015":
			return "Invalid Bank";
		case "018":
			return "Cannot Found Valid Transaction";
		case "019":
			return "Invalid Customer Cardholder";
		case "020":
			return "Invalid Input for - [Param]";
		case "021":
			return "[Param] is NULL";
		case "022":
			return "Invalid Prepaid Program";
		case "023":
			return "Card Not Reloadable";
		case "024":
			return "Topup Limit Error";
		case "025":
			return "Topup Amount Less Than Fee";
		case "029":
			return "Unable to Update Account Balance";
		case "999":
			return "System Error";
		default:
			// Unknown Error message ID
			return "System Error";
		}
	}
	
	public static String ShowCVVErrMessage(String code) {
		switch (code) {
		case "000":
			return "Success";
		case "00":
			return "Success";
		case "002":
			return "System Error";
		case "003":
			return "Input data not valid";
		case "004":
			return "CARD REF NUMBER NOT VALID";
		case "005":
			return "CARD NUMBER NOT VALID";
		case "006":
			return "CARD NOT LINKED TO BANK";
		case "007":
			return "User Not Found";
		case "008":
			return "CARD NOT LINKED TO BANK";
		case "009":
			return "Costumer doesn’t belong to the bank";
		case "010":
			return "CARD NOT LINKED TO BANK";
		case "011":
			return "ERROR WHILE RETRIEVING ACCOUNT BALANCE";
		case "012":
			return "RESPONSE DATE ERROR";
		case "013":
			return "COULD_NOT_RETRIEVE_EXPIRY";
		case "014":
			return "COULD_NOT_RETRIEVE_PAN";
		case "015":
			return "COULD_NOT_RETRIEVE_CARD_PROGRAM";
		case "016":
			return "COULD_NOT_RETRIEVE_BIN";
		case "017":
			return "COULD_NOT_GET_CVK";
		case "018":
			return "CW_COMMAND_HSM_ERROR";
		default:
			// Unknown Error message ID
			return "System Error";
		}
	}
	
	public static String GetCardsErrMessage(String code) {
		switch (code) {
		case "000":
			return "Success";
		case "001":
			return "Card not found";
		case "002":
			return "System Error";
		case "003":
			return "Input data not valid";
		case "007":
			return "User Not Found";
		case "010":
			return "Wrong Customer";
		default:
			// Unknown Error message ID
			return "System Error";
		}
	}
	
	public static String changePINerrMessage(String code) {
		switch (code) {
		case "000":
			return "Success";
		case "107":
			return "USER NOT FOUND";
		case "010":
			return "Error while getting card program";
		case "007":
			return "Card number not valid";
		case "011":
			return "Green PIN deactivated";
		case "013":
			return "Error while calculating clear PIN";
		case "014":
			return "Error while getting alert message";
		case "015":
			return "Error message template";
		case "016":
			return "Error Card Number is NULL";
		case "017":
			return "Error Card Alert Flag is disabled";
		case "018":
			return "Error while retreiving Clear PIN Fees";
		case "019":
			return "Posting fee error";
		case "999":
			return "System Error";
		default:
			// Unknown Error message ID
			return "System Error";
		}
	}
}
