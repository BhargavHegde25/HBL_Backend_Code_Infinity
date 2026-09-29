package com.temenos.dbx.product.commonsutils;

import java.util.Date;

import com.google.gson.JsonObject;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.FeatureAction;

public class AuditLog {
	
	public JsonObject buildCustomParamsForAlertEngine(String fromAccountNumber, String toAccountNumber, JsonObject customParams) {
		
		if (fromAccountNumber != null && !fromAccountNumber.isEmpty()) {

			String lastFourDigits ;
			if(fromAccountNumber.length() > 4)
				lastFourDigits= fromAccountNumber.substring(fromAccountNumber.length() - 4);
			else
				lastFourDigits= fromAccountNumber;
			
			customParams.addProperty("MaskedFromAccount", "XXXX" + lastFourDigits);
		}

		if (toAccountNumber != null && !toAccountNumber.isEmpty()) {
			
			String lastFourDigits ;
			if(toAccountNumber.length() > 4)
				lastFourDigits= toAccountNumber.substring(toAccountNumber.length() - 4);
			else
				lastFourDigits= toAccountNumber;
			
			customParams.addProperty("MaskedToAccount", "XXXX" + lastFourDigits);
		}
		customParams.addProperty("Server Date", new Date().toString());

		return customParams;
	}
	
	public String deriveSubTypeForInternalTransfer(String isScheduled, String frequencyType) {

		if (isScheduled.equals("1") && frequencyType.equalsIgnoreCase(Constants.ONCE)) {
			return Constants.SCHEDULED_OWN_ACCOUNT_TRANSFER;
		} else if (isScheduled.equals("1")) {
			return Constants.RECURRING_OWN_ACCOUNT_TRANSFER;
		} else if (isScheduled.equals("0") && frequencyType.equalsIgnoreCase(Constants.ONCE)) {
			return Constants.ONETIME_OWN_ACCOUNT_TRANSFER;
		}
		return "";
	}

	public String deriveSubTypeForBillPayment(Boolean payeeId) {
		
		if (payeeId) {
			return Constants.REGISTERED_BILL_PAYMENT;
		} else {
			return Constants.NON_REGISTERED_BILL_PAYMENT;
		}
	}

	public String deriveSubTypeForWireTransfer(Boolean payeeId, String serviceName,String wireAccountType) {

		if (payeeId) {
			if (serviceName.equals(FeatureAction.DOMESTIC_WIRE_TRANSFER_CREATE)) {
				return Constants.REG_DOM_WIRE_TRANSFER;
			} else {
				return Constants.REG_INTERNATIONAL_WIRE_TRANSFER;
			}

		} else {
			
			if (wireAccountType.equalsIgnoreCase("domestic")) {
				return Constants.NON_REG_DOM_WIRE_TRANSFER;
			} else {
				return Constants.NON_REG_INTERNATIONAL_WIRE_TRANSFER;
			}
		}
	}

	public String deriveSubTypeForP2PTransfer(String isScheduled, String frequencyType,Boolean payeeId) {
		
		String eventSubType = "";
		
		if (isScheduled.equals("1") && frequencyType.equalsIgnoreCase(Constants.ONCE)) {
			eventSubType = Constants.SCHEDULED_;
		} else if (isScheduled.equals("1")) {
			eventSubType = Constants.RECURRING_;
		} else if (isScheduled.equals("0") && frequencyType.equalsIgnoreCase(Constants.ONCE)) {
			eventSubType = Constants.ONETIME_;
		}

		if (payeeId) {
			eventSubType = eventSubType + Constants.REGISTERED_P2P_TRANSFER;
			return eventSubType;
		} else {
			eventSubType = eventSubType + Constants.NON_REGISTERED_P2P_TRANSFER;
			return eventSubType;
		}
	}

	public String deriveSubTypeForExternalTransfer(String isScheduled, String frequencyType, String serviceName) {

		if (serviceName.equals(FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE)) {

			if (isScheduled.equals("1") && frequencyType.equalsIgnoreCase(Constants.ONCE)) {
				return Constants.SCHEDULED_OTHER_BANK_TRANSFER;
			} else if (isScheduled.equals("1")) {
				return Constants.RECURRING_OTHER_BANK_TRANSFER;
			} else if (isScheduled.equals("0") && frequencyType.equalsIgnoreCase(Constants.ONCE)) {
				return Constants.ONETIME_OTHER_BANK_TRANSFER;
			}
		} else if (serviceName.equals(FeatureAction.INTRA_BANK_FUND_TRANSFER_CREATE)) {// in this case service name will be
			// SERVICE_ID_1
			// checking type of internal other person transfer

			if (isScheduled.equals("1") && frequencyType.equalsIgnoreCase(Constants.ONCE)) {
				return Constants.SCHEDULED_SAMEBANK_OTHER_ACCOUNT_TRANSFER;
			} else if (isScheduled.equals("1")) {
				return Constants.RECURRING_SAMEBANK_OTHER_ACCOUNT_TRANSFER;
			} else if (isScheduled.equals("0") && frequencyType.equalsIgnoreCase(Constants.ONCE)) {
				return Constants.ONETIME_SAMEBANK_OTHER_ACCOUNT_TRANSFER;
			}
		} else {// in this case service name will be SERVICE_ID_4
			if (isScheduled.equals("1") && frequencyType.equalsIgnoreCase(Constants.ONCE)) {
				return Constants.SCHEDULED_INTERNATIONAL_BANK_TRANSFER;
			} else if (isScheduled.equals("1")) {
				return Constants.RECURRING_INTERNATIONAL_BANK_TRANSFER;
			} else if (isScheduled.equals("0") && frequencyType.equalsIgnoreCase(Constants.ONCE)) {
				return Constants.ONETIME_INTERNATIONAL_BANK_TRANSFER;
			}
		}
		return "";
	}
}
