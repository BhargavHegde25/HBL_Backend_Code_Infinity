package com.bct.javaservices;

public class QRRequest {
	public String merchantAccountInfo, merchantCategoryCode, transactionCurrency, countryCode;
	public String customerName, merchantCity, billNumber, branchCode, accountNumber, transactionCategory;

	public QRRequest(String merchantAccountInfo, String merchantCategoryCode, String transactionCurrency,
			String countryCode, String customerName, String merchantCity, String billNumber, String branchCode,
			String accountNumber, String transactionCategory) {
		this.merchantAccountInfo = merchantAccountInfo;
		this.merchantCategoryCode = merchantCategoryCode;
		this.transactionCurrency = transactionCurrency;
		this.countryCode = countryCode;
		this.customerName = customerName;
		this.merchantCity = merchantCity;
		this.billNumber = billNumber;
		this.branchCode = branchCode;
		this.accountNumber = accountNumber;
		this.transactionCategory = transactionCategory;
	}
}