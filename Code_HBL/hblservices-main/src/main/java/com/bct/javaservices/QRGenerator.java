package com.bct.javaservices;

import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

public class QRGenerator {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	public String generateQRData(JsonObject account) {
		alert.prepareError("Inside generateQRData :::").log();
		String accountNumber = account.has("accountID") ? account.get("accountID").getAsString() : "";
		String accountName = account.has("accountName") ? account.get("accountName").getAsString() : "";
		String currencyCode = account.has("currencyCode") ? account.get("currencyCode").getAsString() : "";
		String categoryId = account.has("categoryId") ? account.get("categoryId").getAsString() : "";
		if (accountNumber == null || accountNumber.trim().isEmpty())
			return "";

		try {
			String message = validateAccount(categoryId, currencyCode);
			alert.prepareError("Inside generateQRData message:::" + message).log();

			if (message != null) {
				return message;
			}

			QRRequest request = buildQRRequest(accountName, accountNumber);
			String payload = buildEMVCoPayload(request);
			alert.prepareError("Inside generateQRData payload:::" + payload).log();
			return payload;

		} catch (Exception e) {
			alert.prepareError("Exception occurred while generating QR:::", e).log();
			return "Exception occurred while generating QR";
		}

	}

	private String validateAccount(String categoryId, String currencyCode) {
		if (isEmpty(categoryId) || isEmpty(currencyCode)) {
			return generateQRJson("Unknown", "Unknown");
		}

		if (!"NPR".equalsIgnoreCase(currencyCode.trim())) {
			return "Fund Transfer allowed on NPR Accounts only";
		}

		switch (categoryId) {
		case "6039":
			return "Fund Transfer Not allowed on HRSA Account";
		case "6042":
			return "Fund Transfer Not allowed on EQ Relief Account";
		case "6044":
			return "Fund Transfer Not allowed on Social Security Account";
		case "6016":
		case "6017":
		case "6036":
			return "Fund Transfer Not allowed on Annuity/Recurring Accounts";
		case "1002":
		case "1003":
		case "1004":
			return "Fund Transfer Not allowed on Call Current Account";
		default:
			return null;
		}
	}

	private QRRequest buildQRRequest(String accountName, String accountNumber) {
		String configJson = EnvironmentConfigurationsHandler.getServerProperty("ACCOUNT_LIST_QR_PARAMS");
		alert.prepareError("Inside buildQRRequest configJson:::" + configJson).log();
		JsonObject configObj = JsonParser.parseString(configJson).getAsJsonObject();
		String merchantAccountInfo = configObj.has("MerchantAccountInfo")
				? configObj.get("MerchantAccountInfo").getAsString()
				: "";
		String merchantCategoryCode = configObj.has("MerchantCategoryCode")
				? configObj.get("MerchantCategoryCode").getAsString()
				: "";
		String transactionCurrency = configObj.has("TransactionCurrency")
				? configObj.get("TransactionCurrency").getAsString()
				: "";
		String countryCode = configObj.has("CountryCode") ? configObj.get("CountryCode").getAsString() : "";
		String merchantCity = configObj.has("MerchantCity") ? configObj.get("MerchantCity").getAsString() : "";
		String billNumber = configObj.has("BillNumber") ? configObj.get("BillNumber").getAsString() : "";
		String branchCode = configObj.has("BranchCode") ? configObj.get("BranchCode").getAsString() : "";
		String transactionCategory = configObj.has("TransactionCategory")
				? configObj.get("TransactionCategory").getAsString()
				: "";

		return new QRRequest(merchantAccountInfo, merchantCategoryCode, transactionCurrency, countryCode, accountName,
				merchantCity, billNumber, branchCode, accountNumber, transactionCategory);
	}

	private String generateQRJson(String accountName, String accountNumber) {
		BankDetail bd = new BankDetail(accountName, accountNumber, "HIMANPKA");
		return new Gson().toJson(bd);
	}

	private String buildEMVCoPayload(QRRequest r) {
		StringBuilder sb = new StringBuilder();
		sb.append(tag("00", "01")).append(tag("01", "11"));
		sb.append(tag("29", "0028" + r.merchantAccountInfo));
		sb.append(tag("52", r.merchantCategoryCode));
		sb.append(tag("53", r.transactionCurrency));
		sb.append(tag("58", r.countryCode));
		sb.append(tag("59", r.customerName));
		sb.append(tag("60", r.merchantCity));

		String tag62 = tag("01", r.billNumber) + tag("03", r.branchCode) + tag("07", r.accountNumber)
				+ tag("08", r.transactionCategory);

		sb.append(tag("62", tag62));

		String payload = sb.toString() + "6304";
		sb.append("6304").append(calculateCRC16(payload));
		return sb.toString();
	}

	private String tag(String id, String value) {
		return id + String.format("%02d", value.length()) + value;
	}

	private String calculateCRC16(String input) {
		byte[] bytes = input.getBytes(StandardCharsets.US_ASCII);
		int crc = 0xFFFF;

		for (byte b : bytes) {
			crc ^= b << 8;
			for (int i = 0; i < 8; i++) {
				crc = (crc & 0x8000) != 0 ? (crc << 1) ^ 0x1021 : crc << 1;
			}
		}

		return String.format("%04X", crc & 0xFFFF);
	}

	private boolean isEmpty(String s) {
		return s == null || s.trim().isEmpty();
	}
}
