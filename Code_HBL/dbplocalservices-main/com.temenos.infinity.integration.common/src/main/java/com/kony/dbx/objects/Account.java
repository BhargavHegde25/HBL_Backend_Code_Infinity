package com.kony.dbx.objects;

import java.io.Serializable;

public class Account implements Serializable {

	/**
	 * Unique ID for Serialization
	 */
	private static final long serialVersionUID = 887984530375346143L;

	/*
	 * Unique id for the account
	 */
	private String accountId;

	/*
	 * Name of the account
	 */
	private String accountName;

	/*
	 * Type of the account or Category of the account
	 */
	private String accountType;
	/*
	 * Type of the account or Category of the account in core
	 */
	private String coreAccountType;
	/*
	 * Available Balance of the account from core banking backend for Savings,
	 * Checking and Deposit etc...
	 */
	private String availableBalance;

	/*
	 * Available Credite for an account from core banking backend for Credit Cards
	 * etc...
	 */
	private String availableCredit;

	/*
	 * Bank Name of an account from core banking backend for Savings, Checking,
	 * Credit Card, Loan and Mortgage etc...
	 */
	private String bankName;

	/*
	 * Code of the account from backend
	 */
	private String code;

	/*
	 * Credit Limit for an account from core banking backend for Credit Cards etc...
	 */
	private String creditLimit;

	/*
	 * Current Balance of the account from core banking backend for Savings,
	 * Checking and Deposit etc...
	 */
	private String currentBalance;

	/*
	 * Last Divident Amount Paid for an account for the year so far from core
	 * banking backend for Savings, Checking etc...
	 */
	private String dividendLastPaidAmount;

	/*
	 * Divident Paid for an account for the year so far from core banking backend
	 * for Savings, Checking etc...
	 */
	private String dividendPaidYTD;

	/*
	 * Divident Rate for an account from core banking backend for Savings, Checking
	 * etc...
	 */
	private String dividendRate;

	/*
	 * Due Date of the account from core banking backend for Credit Card, Loan and
	 * Mortgage etc...
	 */
	private String dueDate;

	/*
	 * Interest Paid for an account for last year from core banking backend for
	 * Credit Cards, Loans & External Loans etc...
	 */
	private String interestPaidLastYear;

	/*
	 * Interest Paid for an account for the year so far from core banking backend
	 * for Credit Cards, Loans & External Loans etc...
	 */
	private String interestPaidYTD;

	/*
	 * Interest Rate for an account from core banking backend for Credit Cards,
	 * Loans & External Loans etc...
	 */
	private String interestRate;

	/*
	 * Last Payment Amount for an account from core banking backend for Credit
	 * Cards, Loans & External Loans etc...
	 */
	private String lastPaymentAmount;

	/*
	 * Last Payment Date for an account from core banking backend for Credit Cards,
	 * Loans & External Loans etc...
	 */
	private String lastPaymentDate;

	/*
	 * Maturity Date for an account from core banking backend for CD etc...
	 */
	private String maturityDate;

	/*
	 * Maturity Option for an account from core banking backend for CD etc...
	 */
	private String maturityOption;

	/*
	 * Nick Name of an account from core banking backend for Savings, Checking,
	 * Credit Card, Loan and Mortgage etc...
	 */
	private String nickName;

	/*
	 * Open Date for an account from core banking backend for Savings, Checking,
	 * Credit Cards, Loans & External Loans etc...
	 */
	private String openingDate;

	/*
	 * Outstanging Balance of the account from core banking backend for Credit Card,
	 * Loan and Mortgage etc...
	 */
	private String outstandingBalance;

	/*
	 * Payoff Amount for an account from core banking backend for Credit Cards,
	 * Loans & External Loans etc...
	 */
	private String payoffAmount;

	/*
	 * Pending Deposit of the account from core banking backend for Savings &
	 * Checking etc...
	 */
	private String pendingDeposit;

	/*
	 * Principal Balance of the account from core banking backend for Credit Card,
	 * Loan and Mortgage etc...
	 */
	private String principalBalance;

	/*
	 * Is the account eligible for source account for transfer
	 */
	private String supportTransferFrom;

	/*
	 * Is the account eligible for destination account for transfer
	 */
	private String supportTransferTo;

	/*
	 * Type code of the account from backend
	 */
	private String type;

	private String supportBillPay;

	private String micrNumber;
	private String supportDeposit;
	private String accountGuid;
	private String currencyCode;

	/*
	 * Name of the account holder
	 */
	private String accountHolder;

	/*
	 * Payment date
	 */
	private String paymentDue;

	/*
	 * Current amount due
	 */
	private String currentAmountDue;

	/*
	 * Original amount
	 */
	private String originalAmount;

	/*
	 * Principal value
	 */
	private String principalValue;

	/*
	 * Account arrangementId Value
	 */
	private String arrangementId;

	/*
	 * ProductId value
	 */
	private String productId;
	
	private String userId;
	
	private String Membership_id;
	private String MembershipName;
	private String isBusinessAccount;

	public String getMembership_id() {
		return Membership_id;
	}

	public void setMembership_id(String membership_id) {
		Membership_id = membership_id;
	}

	public String getMembershipName() {
		return MembershipName;
	}

	public void setMembershipName(String membershipName) {
		MembershipName = membershipName;
	}

	public String getIsBusinessAccount() {
		return isBusinessAccount;
	}

	public void setIsBusinessAccount(String isBusinessAccount) {
		this.isBusinessAccount = isBusinessAccount;
	}


	public String getAccountGuid() {
		return accountGuid;
	}

	public void setAccountGuid(String accountGuid) {
		this.accountGuid = accountGuid;
	}

	/*
	 * Constructor
	 */
	public Account() {

		this.accountId = "";
		this.accountName = "";
		this.accountType = "";
		this.availableBalance = "";
		this.availableCredit = "";
		this.bankName = "";
		this.code = "";
		this.creditLimit = "";
		this.currentBalance = "";
		this.dividendLastPaidAmount = "";
		this.dividendPaidYTD = "";
		this.dividendRate = "";
		this.dueDate = "";
		this.interestPaidLastYear = "";
		this.interestPaidYTD = "";
		this.interestRate = "";
		this.lastPaymentAmount = "";
		this.lastPaymentDate = "";
		this.maturityDate = "";
		this.maturityOption = "";
		this.nickName = "";
		this.openingDate = "";
		this.outstandingBalance = "";
		this.payoffAmount = "";
		this.pendingDeposit = "";
		this.principalBalance = "";
		this.supportTransferFrom = "";
		this.supportTransferTo = "";
		this.type = "";
		this.coreAccountType = "";
		this.currencyCode = "";
		this.accountHolder = "";
		this.paymentDue = "";
		this.currentAmountDue = "";
		this.originalAmount = "";
		this.arrangementId = "";
		this.productId = "";
		this.userId="";

	}

	/**
	 * @return the accountId
	 */
	public String getSupportBillPay() {
		return supportBillPay;
	}

	public String getAccountId() {
		return accountId;
	}

	/**
	 * @return the accountName
	 */
	public String getAccountName() {
		String name = accountName;
		if (nickName != null && !nickName.equals("")) {
			name = nickName;
		}
		return name;
	}

	/**
	 * @return the accountType
	 */
	public String getAccountType() {
		return accountType;
	}

	/**
	 * @return the availableBalance
	 */
	public String getAvailableBalance() {
		return availableBalance;
	}

	/**
	 * @return the availableCredit
	 */
	public String getAvailableCredit() {
		return availableCredit;
	}

	/**
	 * @return the bankName
	 */
	public String getBankName() {
		return bankName;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @return the creditLimit
	 */
	public String getCreditLimit() {
		return creditLimit;
	}

	/**
	 * @return the currentBalance
	 */
	public String getCurrentBalance() {
		return currentBalance;
	}

	/**
	 * @return the dividendLastPaidAmount
	 */
	public String getDividendLastPaidAmount() {
		return dividendLastPaidAmount;
	}

	/**
	 * @return the dividendPaidYTD
	 */
	public String getDividendPaidYTD() {
		return dividendPaidYTD;
	}

	/**
	 * @return the dividendRate
	 */
	public String getDividendRate() {
		return dividendRate;
	}

	/**
	 * @return the dueDate
	 */
	public String getDueDate() {
		return dueDate;
	}

	/**
	 * @return the interestPaidLastYear
	 */
	public String getInterestPaidLastYear() {
		return interestPaidLastYear;
	}

	/**
	 * @return the interestPaidYTD
	 */
	public String getInterestPaidYTD() {
		return interestPaidYTD;
	}

	/**
	 * @return the interestRate
	 */
	public String getInterestRate() {
		return interestRate;
	}

	/**
	 * @return the lastPaymentAmount
	 */
	public String getLastPaymentAmount() {
		return lastPaymentAmount;
	}

	/**
	 * @return the lastPaymentDate
	 */
	public String getLastPaymentDate() {
		return lastPaymentDate;
	}

	/**
	 * @return the maturityDate
	 */
	public String getMaturityDate() {
		return maturityDate;
	}

	/**
	 * @return the maturityOption
	 */
	public String getMaturityOption() {
		return maturityOption;
	}

	/**
	 * @return the nickName
	 */
	public String getNickName() {
		return nickName;
	}

	/**
	 * @return the openingDate
	 */
	public String getOpeningDate() {
		return openingDate;
	}

	/**
	 * @return the outstandingBalance
	 */
	public String getOutstandingBalance() {
		return outstandingBalance;
	}

	/**
	 * @return the payoffAmount
	 */
	public String getPayoffAmount() {
		return payoffAmount;
	}

	/**
	 * @return the pendingDeposit
	 */
	public String getPendingDeposit() {
		return pendingDeposit;
	}

	/**
	 * @return the principalBalance
	 */
	public String getPrincipalBalance() {
		return principalBalance;
	}

	/**
	 * @return the supportTransferFrom
	 */
	public String getSupportTransferFrom() {
		return supportTransferFrom;
	}

	/**
	 * @return the supportTransferTo
	 */
	public String getSupportTransferTo() {
		return supportTransferTo;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param accountId the accountId to set
	 */
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public void setSupportBillPay(String supportBillPay) {
		this.supportBillPay = supportBillPay;
	}

	/**
	 * @param accountName the accountName to set
	 */
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	/**
	 * @param accountType the accountType to set
	 */
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	/**
	 * @param availableBalance the availableBalance to set
	 */
	public void setAvailableBalance(String availableBalance) {
		this.availableBalance = availableBalance;
	}

	/**
	 * @param availableCredit the availableCredit to set
	 */
	public void setAvailableCredit(String availableCredit) {
		this.availableCredit = availableCredit;
	}

	/**
	 * @param bankName the bankName to set
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @param creditLimit the creditLimit to set
	 */
	public void setCreditLimit(String creditLimit) {
		this.creditLimit = creditLimit;
	}

	/**
	 * @param currentBalance the currentBalance to set
	 */
	public void setCurrentBalance(String currentBalance) {
		this.currentBalance = currentBalance;
	}

	/**
	 * @param dividendLastPaidAmount the dividendLastPaidAmount to set
	 */
	public void setDividendLastPaidAmount(String dividendLastPaidAmount) {
		this.dividendLastPaidAmount = dividendLastPaidAmount;
	}

	/**
	 * @param dividendPaidYTD the dividendPaidYTD to set
	 */
	public void setDividendPaidYTD(String dividendPaidYTD) {
		this.dividendPaidYTD = dividendPaidYTD;
	}

	/**
	 * @param dividendRate the dividendRate to set
	 */
	public void setDividendRate(String dividendRate) {
		this.dividendRate = dividendRate;
	}

	/**
	 * @param dueDate the dueDate to set
	 */
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	/**
	 * @param interestPaidLastYear the interestPaidLastYear to set
	 */
	public void setInterestPaidLastYear(String interestPaidLastYear) {
		this.interestPaidLastYear = interestPaidLastYear;
	}

	/**
	 * @param interestPaidYTD the interestPaidYTD to set
	 */
	public void setInterestPaidYTD(String interestPaidYTD) {
		this.interestPaidYTD = interestPaidYTD;
	}

	/**
	 * @param interestRate the interestRate to set
	 */
	public void setInterestRate(String interestRate) {
		this.interestRate = interestRate;
	}

	/**
	 * @param lastPaymentAmount the lastPaymentAmount to set
	 */
	public void setLastPaymentAmount(String lastPaymentAmount) {
		this.lastPaymentAmount = lastPaymentAmount;
	}

	/**
	 * @param lastPaymentDate the lastPaymentDate to set
	 */
	public void setLastPaymentDate(String lastPaymentDate) {
		this.lastPaymentDate = lastPaymentDate;
	}

	/**
	 * @param maturityDate the maturityDate to set
	 */
	public void setMaturityDate(String maturityDate) {
		this.maturityDate = maturityDate;
	}

	/**
	 * @param maturityOption the maturityOption to set
	 */
	public void setMaturityOption(String maturityOption) {
		this.maturityOption = maturityOption;
	}

	/**
	 * @param nickName the nickName to set
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	/**
	 * @param openingDate the openingDate to set
	 */
	public void setOpeningDate(String openingDate) {
		this.openingDate = openingDate;
	}

	/**
	 * @param outstandingBalance the outstandingBalance to set
	 */
	public void setOutstandingBalance(String outstandingBalance) {
		this.outstandingBalance = outstandingBalance;
	}

	/**
	 * @param payoffAmount the payoffAmount to set
	 */
	public void setPayoffAmount(String payoffAmount) {
		this.payoffAmount = payoffAmount;
	}

	/**
	 * @param pendingDeposit the pendingDeposit to set
	 */
	public void setPendingDeposit(String pendingDeposit) {
		this.pendingDeposit = pendingDeposit;
	}

	/**
	 * @param principalBalance the principalBalance to set
	 */
	public void setPrincipalBalance(String principalBalance) {
		this.principalBalance = principalBalance;
	}

	/**
	 * @param supportTransferFrom the supportTransferFrom to set
	 */
	public void setSupportTransferFrom(String supportTransferFrom) {
		this.supportTransferFrom = supportTransferFrom;
	}

	/**
	 * @param supportTransferTo the supportTransferTo to set
	 */
	public void setSupportTransferTo(String supportTransferTo) {
		this.supportTransferTo = supportTransferTo;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	public String getMicrNumber() {
		return micrNumber;
	}

	public void setMicrNumber(String micrNumber) {
		this.micrNumber = micrNumber;
	}

	public String getSupportDeposit() {
		return supportDeposit;
	}

	public void setSupportDeposit(String supportDeposit) {
		this.supportDeposit = supportDeposit;
	}

	public String getCoreAccountType() {
		return coreAccountType;
	}

	public void setCoreAccountType(String coreAccountType) {
		this.coreAccountType = coreAccountType;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	/**
	 * @return the accountHolder
	 */
	public String getAccountHolder() {
		return accountHolder;
	}

	/**
	 * @param accountHolder the accountHolder to set
	 */
	public void setAccountHolder(String accountHolder) {
		this.accountHolder = accountHolder;
	}

	/**
	 * @return the paymentDue
	 */
	public String getPaymentDue() {
		return paymentDue;
	}

	/**
	 * @param paymentDue the paymentDue to set
	 */
	public void setPaymentDue(String paymentDue) {
		this.paymentDue = paymentDue;
	}

	/**
	 * @return the currentAmountDue
	 */
	public String getCurrentAmountDue() {
		return currentAmountDue;
	}

	/**
	 * @param currentAmountDue the currentAmountDue to set
	 */
	public void setCurrentAmountDue(String currentAmountDue) {
		this.currentAmountDue = currentAmountDue;
	}

	/**
	 * @return the originalAmount
	 */
	public String getOriginalAmount() {
		return originalAmount;
	}

	/**
	 * @param originalAmount the originalAmount to set
	 */
	public void setOriginalAmount(String originalAmount) {
		this.originalAmount = originalAmount;
	}

	/**
	 * @return the principalValue
	 */
	public String getPrincipalValue() {
		return principalValue;
	}

	/**
	 * @param principalValue the principalValue to set
	 */
	public void setPrincipalValue(String principalValue) {
		this.principalValue = principalValue;
	}

	public String getArrangementId() {
		return arrangementId;
	}

	public void setArrangementId(String arrangementId) {
		this.arrangementId = arrangementId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}