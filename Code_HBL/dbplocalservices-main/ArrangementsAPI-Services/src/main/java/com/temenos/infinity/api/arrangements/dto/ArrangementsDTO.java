package com.temenos.infinity.api.arrangements.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class ArrangementsDTO implements DBPDTO {

    private static final long serialVersionUID = 5035451677589972439L;
	

	public ArrangementsDTO(String backendUserId, String expiresAt, String connectionAlertDays, String bankCode,
			String customerType, String customerID, String authToken, String account_id, String arrangementId,
			String companyCode,String currentDue,String dueDate, String statusDesc, String product, String accountName, String typeDescription,
			double availableBalance, String bankname, String currencyCode, double currentBalance, String nickName,
			String displayName, String openingDate, double workingBalance, String swiftCode, String accountHolder,
			String jointHolders, String dividendLastPaidDate, String lastPaymentDate, double dividendPaidYTD,
			double dividendLastPaidAmount, float dividendRate, String accountCategory, String productLineName,
			String routingNumber, String availableLimit, double pendingDeposit, double pendingWithdrawal,
			String supportBillPay, String supportChecks, String supportDeposit, String supportTransferFrom,
			String supportTransferTo, String externalIndicator, String processingTime, String logoURL, String iBAN,
			String isBusinessAccount, double outstandingOverdraftLimit, String totalCreditMonths,
			String totalDebitsMonth, Double interestEarned, String originalAmount, String paymentTerm,
			String maturityAmount, String maturityOption, Double lockedAmount, Double clearedBalance,
			Double onlineActualBalance, String availableBalanceWithLimit, String portfolioId, String taxId,
			String membership_id, String limitReference, String productGroupId, String productGroupName,
			String dbpErrCode, String dbpErrMsg, String currentAmountDue, String paymentDue, String deviceId,
			String channelDescription, String payoffAmount, String outstandingBalance, String principalBalance,
			Double onlineClearedBalance, String userName, String isTypeBusiness, String favouriteStatus,
			String blockedAmount, String transferLimit, String paidInstallmentsCount, String overDueInstallmentsCount,
			String futureInstallmentsCount, String email, String electronicStatementEnabled, double disbursedAmount,
			String lastPaymentAmount, String termAmount, String totalDueAmount, String sanctionedDate,
			String maturityDate, String nextPaymentAmount, String nextPaymentDate, String interestRate,
			String accruedInterest, String principalValue, String termInterestDue, String interestDueDate,
			String accountType,String actions,String partyRole, String rePaymentFrequency , String isPortFolioAccount, String interestPaidYTD, String isNew, String productGroup) {
		super();
		this.backendUserId = backendUserId;
		this.expiresAt = expiresAt;
		this.connectionAlertDays = connectionAlertDays;
		this.bankCode = bankCode;
		this.customerType = customerType;
		this.customerID = customerID;
		this.currentDue = currentDue;
		this.dueDate = dueDate;
		this.authToken = authToken;
		Account_id = account_id;
		this.arrangementId = arrangementId;
		this.companyCode = companyCode;
		this.statusDesc = statusDesc;
		this.product = product;
		this.accountName = accountName;
		this.typeDescription = typeDescription;
		this.availableBalance = availableBalance;
		this.bankname = bankname;
		this.currencyCode = currencyCode;
		this.currentBalance = currentBalance;
		this.nickName = nickName;
		this.displayName = displayName;
		this.openingDate = openingDate;
		this.workingBalance = workingBalance;
		this.swiftCode = swiftCode;
		this.accountHolder = accountHolder;
		this.jointHolders = jointHolders;
		this.dividendLastPaidDate = dividendLastPaidDate;
		this.lastPaymentDate = lastPaymentDate;
		this.dividendPaidYTD = dividendPaidYTD;
		this.dividendLastPaidAmount = dividendLastPaidAmount;
		this.dividendRate = dividendRate;
		this.accountCategory = accountCategory;
		this.productLineName = productLineName;
		this.routingNumber = routingNumber;
		this.availableLimit = availableLimit;
		this.pendingDeposit = pendingDeposit;
		this.pendingWithdrawal = pendingWithdrawal;
		this.supportBillPay = supportBillPay;
		this.supportChecks = supportChecks;
		this.supportDeposit = supportDeposit;
		this.supportTransferFrom = supportTransferFrom;
		this.supportTransferTo = supportTransferTo;
		this.externalIndicator = externalIndicator;
		this.processingTime = processingTime;
		this.logoURL = logoURL;
		IBAN = iBAN;
		this.isBusinessAccount = isBusinessAccount;
		this.outstandingOverdraftLimit = outstandingOverdraftLimit;
		this.totalCreditMonths = totalCreditMonths;
		this.totalDebitsMonth = totalDebitsMonth;
		this.interestEarned = interestEarned;
		this.originalAmount = originalAmount;
		this.paymentTerm = paymentTerm;
		this.maturityAmount = maturityAmount;
		this.maturityOption = maturityOption;
		this.lockedAmount = lockedAmount;
		this.clearedBalance = clearedBalance;
		this.onlineActualBalance = onlineActualBalance;
		this.availableBalanceWithLimit = availableBalanceWithLimit;
		this.portfolioId = portfolioId;
		TaxId = taxId;
		Membership_id = membership_id;
		this.limitReference = limitReference;
		this.productGroupId = productGroupId;
		this.productGroupName = productGroupName;
		this.dbpErrCode = dbpErrCode;
		this.dbpErrMsg = dbpErrMsg;
		this.currentAmountDue = currentAmountDue;
		this.paymentDue = paymentDue;
		this.deviceId = deviceId;
		this.channelDescription = channelDescription;
		this.payoffAmount = payoffAmount;
		this.outstandingBalance = outstandingBalance;
		this.principalBalance = principalBalance;
		this.onlineClearedBalance = onlineClearedBalance;
		this.userName = userName;
		this.isTypeBusiness = isTypeBusiness;
		this.favouriteStatus = favouriteStatus;
		this.blockedAmount = blockedAmount;
		this.transferLimit = transferLimit;
		this.paidInstallmentsCount = paidInstallmentsCount;
		this.overDueInstallmentsCount = overDueInstallmentsCount;
		this.futureInstallmentsCount = futureInstallmentsCount;
		this.email = email;
		this.electronicStatementEnabled = electronicStatementEnabled;
		this.disbursedAmount = disbursedAmount;
		this.lastPaymentAmount = lastPaymentAmount;
		this.termAmount = termAmount;
		this.totalDueAmount = totalDueAmount;
		this.sanctionedDate = sanctionedDate;
		this.maturityDate = maturityDate;
		this.nextPaymentAmount=nextPaymentAmount;
		this.nextPaymentDate=nextPaymentDate;
		this.interestRate=interestRate;
		this.accruedInterest=accruedInterest;
		this.principalValue=principalValue;
		this.termInterestDue=termInterestDue;
		this.interestDueDate=interestDueDate;
		this.accountType=accountType;
		this.actions=actions; 
		this.partyRole=partyRole;
		this.rePaymentFrequency=rePaymentFrequency;
		this.isPortFolioAccount=isPortFolioAccount;
		this.interestPaidYTD=interestPaidYTD;
		this.isNew=isNew;
		this.productGroup=productGroup;
		this.serviceType=serviceType;
		this.valueDate=valueDate;
		this.marketValue=marketValue;
	}
    private String backendUserId;
    private String expiresAt;
    private String connectionAlertDays;
    private String bankCode;
    private String customerType;
    private String customerID;
    private String authToken;
    private String Account_id;
    private String arrangementId;
    private String companyCode;
    private String statusDesc;
    private String product;
    private String accountName;
    private String typeDescription;
    private double availableBalance;
    private String bankname;
    private String currencyCode;
    private double currentBalance;
    private String nickName;
    private String displayName;
    private String openingDate;
    private double workingBalance;
    private String swiftCode;
    private String accountHolder;
    private String jointHolders;
    private String dividendLastPaidDate;
    private String lastPaymentDate;
    private double dividendPaidYTD;
    private double dividendLastPaidAmount;
    private float dividendRate;
    private String accountCategory;
    private String currentDue;
    private String dueDate;
    private String productLineName;
    private String routingNumber;
    private String availableLimit;
    private double pendingDeposit;
    private double pendingWithdrawal;
    private String supportBillPay;
    private String supportChecks;
    private String supportDeposit;
    private String supportTransferFrom;
    private String supportTransferTo;
    private String externalIndicator;
    private String processingTime;
    private String logoURL;
    private String IBAN;
    private String isBusinessAccount;
    private double outstandingOverdraftLimit;
    private String totalCreditMonths;
    private String totalDebitsMonth;
    private Double interestEarned;
    private String originalAmount;
    private String paymentTerm;
    private String maturityAmount;
    private String maturityOption;
    private Double lockedAmount;
    private Double clearedBalance;
    private Double onlineActualBalance;
    private String availableBalanceWithLimit;
    private String portfolioId;
    private String TaxId;
    private String Membership_id;
    private String limitReference;
    private String productGroupId;
    private String productGroupName;
    private String dbpErrCode;
    private String dbpErrMsg;
    private String currentAmountDue;
    private String paymentDue;
    private String deviceId;
    private String channelDescription;
    private String payoffAmount;
    private String outstandingBalance;
    private String principalBalance;
    private Double onlineClearedBalance;
    private String userName;
    private String isTypeBusiness;
    private String favouriteStatus;
    private String blockedAmount;
    private String transferLimit;
    private String paidInstallmentsCount;
    private String overDueInstallmentsCount;
    private String futureInstallmentsCount;
    private String email;
    private String electronicStatementEnabled;
    private double disbursedAmount;
    private String lastPaymentAmount;    
    private String totalDueAmount;    
    private String termAmount;
    private String sanctionedDate;
    private String maturityDate;
    private String nextPaymentAmount;
    private String nextPaymentDate;
    private String interestRate;
    private String accruedInterest;
    private String principalValue;
    private String termInterestDue;
    private String interestDueDate;
    private String accountType;
    private String actions;
    private String partyRole;
    private String rePaymentFrequency;
    private String isPortFolioAccount;
    private String interestPaidYTD;
    private String isNew = "false";
    private String productGroup;
    private String serviceType;
	private String valueDate;
	private String marketValue;

    public String getNextPaymentAmount() {
		return nextPaymentAmount;
	}

	public void setNextPaymentAmount(String nextPaymentAmount) {
		this.nextPaymentAmount = nextPaymentAmount;
	}
	
	public String getCurrentDue() {
		return currentDue;
	}

	public void setCurrentDue(String currentDue) {
		this.currentDue = currentDue;
	}
	
	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getNextPaymentDate() {
		return nextPaymentDate;
	}

	public void setNextPaymentDate(String nextPaymentDate) {
		this.nextPaymentDate = nextPaymentDate;
	}

	public String getPaidInstallmentsCount() {
		return paidInstallmentsCount;
	}

	public void setPaidInstallmentsCount(String paidInstallmentsCount) {
		this.paidInstallmentsCount = paidInstallmentsCount;
	}

	public String getOverDueInstallmentsCount() {
		return overDueInstallmentsCount;
	}

	public void setOverDueInstallmentsCount(String overDueInstallmentsCount) {
		this.overDueInstallmentsCount = overDueInstallmentsCount;
	}

	public String getFutureInstallmentsCount() {
		return futureInstallmentsCount;
	}

	public void setFutureInstallmentsCount(String futureInstallmentsCount) {
		this.futureInstallmentsCount = futureInstallmentsCount;
	}

    
    public String getPartyRole() {
        return partyRole;
    }

    public void setPartyRole(String partyRole) {
        this.partyRole = partyRole;
    }


	public Double getInterestEarned() {
        return interestEarned;
    }

    public void setInterestEarned(Double interestEarned) {
        this.interestEarned = interestEarned;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getConnectionAlertDays() {
        return connectionAlertDays;
    }

    public void setConnectionAlertDays(String connectionAlertDays) {
        this.connectionAlertDays = connectionAlertDays;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getChannelDescription() {
        return channelDescription;
    }

    public void setChannelDescription(String channelDescription) {
        this.channelDescription = channelDescription;
    }

    public double getOutstandingOverdraftLimit() {
        return outstandingOverdraftLimit;
    }

    public double getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(double availableBalance) {
        this.availableBalance = availableBalance;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(double currentBalance) {
        this.currentBalance = currentBalance;
    }

    public double getDividendPaidYTD() {
        return dividendPaidYTD;
    }

    public void setDividendPaidYTD(double dividendPaidYTD) {
        this.dividendPaidYTD = dividendPaidYTD;
    }

    public double getDividendLastPaidAmount() {
        return dividendLastPaidAmount;
    }

    public void setDividendLastPaidAmount(double dividendLastPaidAmount) {
        this.dividendLastPaidAmount = dividendLastPaidAmount;
    }

    public double getPendingDeposit() {
        return pendingDeposit;
    }

    public void setPendingDeposit(double pendingDeposit) {
        this.pendingDeposit = pendingDeposit;
    }

    public double getPendingWithdrawal() {
        return pendingWithdrawal;
    }

    public void setPendingWithdrawal(double pendingWithdrawal) {
        this.pendingWithdrawal = pendingWithdrawal;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }

    public String getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(String processingTime) {
        this.processingTime = processingTime;
    }

    public ArrangementsDTO() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getExternalIndicator() {
        return externalIndicator;
    }

    public void setExternalIndicator(String externalIndicator) {
        this.externalIndicator = externalIndicator;
    }

    public String getAvailableLimit() {
        return availableLimit;
    }

    public void setAvailableLimit(String availableLimit) {
        this.availableLimit = availableLimit;
    }

    public String getAccount_id() {
        return Account_id;
    }

    public void setAccount_id(String Account_id) {
        this.Account_id = Account_id;
    }

    public String getArrangementId() {
        return arrangementId;
    }

    public void setArrangementId(String arrangementId) {
        this.arrangementId = arrangementId;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getTypeDescription() {
        return typeDescription;
    }

    public void setTypeDescription(String typeDescription) {
        this.typeDescription = typeDescription;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(String openingDate) {
        this.openingDate = openingDate;
    }

    public double getWorkingBalance() {
        return workingBalance;
    }

    public void setWorkingBalance(double workingBalance) {
        this.workingBalance = workingBalance;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public String getDividendLastPaidDate() {
        return dividendLastPaidDate;
    }

    public void setDividendLastPaidDate(String dividendLastPaidDate) {
        this.dividendLastPaidDate = dividendLastPaidDate;
    }

    public float getDividendRate() {
        return dividendRate;
    }

    public void setDividendRate(float dividendRate) {
        this.dividendRate = dividendRate;
    }

    public String getAccountCategory() {
        return accountCategory;
    }

    public void setAccountCategory(String accountCategory) {
        this.accountCategory = accountCategory;
    }

    public String getProductLineName() {
        return productLineName;
    }

    public void setProductLineName(String productLineName) {
        this.productLineName = productLineName;
    }

    public String getRoutingNumber() {
        return routingNumber;
    }

    public void setRoutingNumber(String routingNumber) {
        this.routingNumber = routingNumber;
    }

    public String getSupportBillPay() {
        return supportBillPay;
    }

    public void setSupportBillPay(String supportBillPay) {
        this.supportBillPay = supportBillPay;
    }

    public String getSupportChecks() {
        return supportChecks;
    }

    public void setSupportChecks(String supportChecks) {
        this.supportChecks = supportChecks;
    }

    public String getSupportDeposit() {
        return supportDeposit;
    }

    public void setSupportDeposit(String supportDeposit) {
        this.supportDeposit = supportDeposit;
    }

    public String getSupportTransferFrom() {
        return supportTransferFrom;
    }

    public void setSupportTransferFrom(String supportTransferFrom) {
        this.supportTransferFrom = supportTransferFrom;
    }

    public String getSupportTransferTo() {
        return supportTransferTo;
    }

    public void setSupportTransferTo(String supportTransferTo) {
        this.supportTransferTo = supportTransferTo;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public void setAccountHolder(String accountHolder) {
        this.accountHolder = accountHolder;
    }

    public String getJointHolders() {
        return jointHolders;
    }

    public void setJointHolders(String jointHolders) {
        this.jointHolders = jointHolders;
    }

    public String getLastPaymentDate() {
        return lastPaymentDate;
    }

    public void setLastPaymentDate(String lastPaymentDate) {
        this.lastPaymentDate = lastPaymentDate;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String iBAN) {
        IBAN = iBAN;
    }

    public String getBackendUserId() {
        return backendUserId;
    }

    public void setBackendUserId(String backendUserId) {
        this.backendUserId = backendUserId;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getIsBusinessAccount() {
        return isBusinessAccount;
    }

    public void setIsBusinessAccount(String isBusinessAccount) {
        this.isBusinessAccount = isBusinessAccount;
    }

    public String getTotalCreditMonths() {
        return totalCreditMonths;
    }

    public void setTotalCreditMonths(String totalCreditMonths) {
        this.totalCreditMonths = totalCreditMonths;
    }

    public String getTotalDebitsMonth() {
        return totalDebitsMonth;
    }

    public void setTotalDebitsMonth(String totalDebitsMonth) {
        this.totalDebitsMonth = totalDebitsMonth;
    }

    public String getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(String originalAmount) {
        this.originalAmount = originalAmount;
    }

    public String getPaymentTerm() {
        return paymentTerm;
    }

    public void setPaymentTerm(String paymentTerm) {
        this.paymentTerm = paymentTerm;
    }

    public String getMaturityAmount() {
        return maturityAmount;
    }

    public void setMaturityAmount(String maturityAmount) {
        this.maturityAmount = maturityAmount;
    }

    public String getMaturityOption() {
        return maturityOption;
    }

    public void setMaturityOption(String maturityOption) {
        this.maturityOption = maturityOption;
    }

    public Double getLockedAmount() {
        return lockedAmount;
    }

    public void setLockedAmount(Double lockedAmount) {
        this.lockedAmount = lockedAmount;
    }

    public Double getClearedBalance() {
        return clearedBalance;
    }

    public void setClearedBalance(Double clearedBalance) {
        this.clearedBalance = clearedBalance;
    }

    public Double getOnlineActualBalance() {
        return onlineActualBalance;
    }

    public void setOnlineActualBalance(Double onlineActualBalance) {
        this.onlineActualBalance = onlineActualBalance;
    }

    public String getAvailableBalanceWithLimit() {
        return availableBalanceWithLimit;
    }

    public void setAvailableBalanceWithLimit(String availableBalanceWithLimit) {
        this.availableBalanceWithLimit = availableBalanceWithLimit;
    }

    public String getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(String portfolioId) {
        this.portfolioId = portfolioId;
    }

    public String getTaxId() {
        return TaxId;
    }

    public void setTaxId(String taxid) {
        TaxId = taxid;
    }

    public String getMembership_id() {
        return Membership_id;
    }

    public void setMembership_id(String membership_id) {
        Membership_id = membership_id;
    }

    public String getLimitReference() {
        return limitReference;
    }

    public void setLimitReference(String limitReference) {
        this.limitReference = limitReference;
    }

    public String getProductGroupId() {
        return productGroupId;
    }

    public void setProductGroupId(String productGroupId) {
        this.productGroupId = productGroupId;
    }

    public String getProductGroupName() {
        return productGroupName;
    }

    public void setProductGroupName(String productGroupName) {
        this.productGroupName = productGroupName;
    }

    public String getDbpErrCode() {
        return dbpErrCode;
    }

    public void setDbpErrCode(String dbpErrCode) {
        this.dbpErrCode = dbpErrCode;
    }

    public String getDbpErrMsg() {
        return dbpErrMsg;
    }

    public void setDbpErrMsg(String dbpErrMsg) {
        this.dbpErrMsg = dbpErrMsg;
    }

    public String getCurrentAmountDue() {
        return currentAmountDue;
    }

    public void setCurrentAmountDue(String currentAmountDue) {
        this.currentAmountDue = currentAmountDue;
    }

    public String getPaymentDue() {
        return paymentDue;
    }

    public void setPaymentDue(String paymentDue) {
        this.paymentDue = paymentDue;
    }

    public String getPayoffAmount() {
        return payoffAmount;
    }

    public void setPayoffAmount(String payoffAmount) {
        this.payoffAmount = payoffAmount;
    }

    public String getOutstandingBalance() {
        return outstandingBalance;
    }

    public void setOutstandingBalance(String outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }

    public String getPrincipalBalance() {
        return principalBalance;
    }

    public void setPrincipalBalance(String principalBalance) {
        this.principalBalance = principalBalance;
    }

    public Double getOnlineClearedBalance() {
        return onlineClearedBalance;
    }

    public void setOnlineClearedBalance(Double onlineClearedBalance) {
        this.onlineClearedBalance = onlineClearedBalance;
    }

    public void setOutstandingOverdraftLimit(double outstandingOverdraftLimit) {
        this.outstandingOverdraftLimit = outstandingOverdraftLimit;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getIsTypeBusiness() {
        return isTypeBusiness;
    }

    public void setIsTypeBusiness(String isTypeBusiness) {
        this.isTypeBusiness = isTypeBusiness;
    }

    public String getFavouriteStatus() {
        return favouriteStatus;
    }

    public void setFavouriteStatus(String favouriteStatus) {
        this.favouriteStatus = favouriteStatus;
    }

    public String getBlockedAmount() {
        return blockedAmount;
    }

    public void setBlockedAmount(String blockedAmount) {
        this.blockedAmount = blockedAmount;
    }
    public String getTransferLimit() {
        return transferLimit;
    }

    public void setTransferLimit(String transferLimit) {
        this.transferLimit = transferLimit;
    }

    public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getElectronicStatementEnabled() {
		return electronicStatementEnabled;
	}

	public void setElectronicStatementEnabled(String electronicStatementEnabled) {
		this.electronicStatementEnabled = electronicStatementEnabled;
	}

	public double getDisbursedAmount() {
		return disbursedAmount;
	}

	public void setDisbursedAmount(double disbursedAmount) {
		this.disbursedAmount = disbursedAmount;
	}

	public String getLastPaymentAmount() {
		return lastPaymentAmount;
	}

	public void setLastPaymentAmount(String lastPaymentAmount) {
		this.lastPaymentAmount = lastPaymentAmount;
	}

	public String getTotalDueAmount() {
		return totalDueAmount;
	}

	public void setTotalDueAmount(String totalDueAmount) {
		this.totalDueAmount = totalDueAmount;
	}

	public String getTermAmount() {
		return termAmount;
	}

	public void setTermAmount(String termAmount) {
		this.termAmount = termAmount;
	}

	public String getSanctionedDate() {
		return sanctionedDate;
	}

	public void setSanctionedDate(String sanctionedDate) {
		this.sanctionedDate = sanctionedDate;
	}

	public String getMaturityDate() {
		return maturityDate;
	}

	public void setMaturityDate(String maturityDate) {
		this.maturityDate = maturityDate;
	}

	public String getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(String interestRate) {
		this.interestRate = interestRate;
	}

	public String getAccruedInterest() {
		return accruedInterest;
	}

	public void setAccruedInterest(String accruedInterest) {
		this.accruedInterest = accruedInterest;
	}

	public String getPrincipalValue() {
		return principalValue;
	}

	public void setPrincipalValue(String principalValue) {
		this.principalValue = principalValue;
	}

	public String getTermInterestDue() {
		return termInterestDue;
	}

	public void setTermInterestDue(String termInterestDue) {
		this.termInterestDue = termInterestDue;
	}

	public String getInterestDueDate() {
		return interestDueDate;
	}

	public void setInterestDueDate(String interestDueDate) {
		this.interestDueDate = interestDueDate;
	}

	public String getRePaymentFrequency() {
		return rePaymentFrequency;
	}

	public void setRePaymentFrequency(String rePaymentFrequency) {
		this.rePaymentFrequency = rePaymentFrequency;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Account_id == null) ? 0 : Account_id.hashCode());
		result = prime * result + ((currentDue == null) ? 0 : currentDue.hashCode());
		result = prime * result + ((dueDate == null) ? 0 : dueDate.hashCode());
		result = prime * result + ((IBAN == null) ? 0 : IBAN.hashCode());
		result = prime * result + ((Membership_id == null) ? 0 : Membership_id.hashCode());
		result = prime * result + ((TaxId == null) ? 0 : TaxId.hashCode());
		result = prime * result + ((accountCategory == null) ? 0 : accountCategory.hashCode());
		result = prime * result + ((accountHolder == null) ? 0 : accountHolder.hashCode());
		result = prime * result + ((accountName == null) ? 0 : accountName.hashCode());
		result = prime * result + ((accountType == null) ? 0 : accountType.hashCode());
		result = prime * result + ((accruedInterest == null) ? 0 : accruedInterest.hashCode());
		result = prime * result + ((actions == null) ? 0 : actions.hashCode());
		result = prime * result + ((arrangementId == null) ? 0 : arrangementId.hashCode());
		result = prime * result + ((authToken == null) ? 0 : authToken.hashCode());
		long temp;
		temp = Double.doubleToLongBits(availableBalance);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((availableBalanceWithLimit == null) ? 0 : availableBalanceWithLimit.hashCode());
		result = prime * result + ((availableLimit == null) ? 0 : availableLimit.hashCode());
		result = prime * result + ((backendUserId == null) ? 0 : backendUserId.hashCode());
		result = prime * result + ((bankCode == null) ? 0 : bankCode.hashCode());
		result = prime * result + ((bankname == null) ? 0 : bankname.hashCode());
		result = prime * result + ((blockedAmount == null) ? 0 : blockedAmount.hashCode());
		result = prime * result + ((channelDescription == null) ? 0 : channelDescription.hashCode());
		result = prime * result + ((clearedBalance == null) ? 0 : clearedBalance.hashCode());
		result = prime * result + ((companyCode == null) ? 0 : companyCode.hashCode());
		result = prime * result + ((connectionAlertDays == null) ? 0 : connectionAlertDays.hashCode());
		result = prime * result + ((currencyCode == null) ? 0 : currencyCode.hashCode());
		result = prime * result + ((currentAmountDue == null) ? 0 : currentAmountDue.hashCode());
		temp = Double.doubleToLongBits(currentBalance);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((customerID == null) ? 0 : customerID.hashCode());
		result = prime * result + ((customerType == null) ? 0 : customerType.hashCode());
		result = prime * result + ((dbpErrCode == null) ? 0 : dbpErrCode.hashCode());
		result = prime * result + ((dbpErrMsg == null) ? 0 : dbpErrMsg.hashCode());
		result = prime * result + ((deviceId == null) ? 0 : deviceId.hashCode());
		temp = Double.doubleToLongBits(disbursedAmount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
		temp = Double.doubleToLongBits(dividendLastPaidAmount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((dividendLastPaidDate == null) ? 0 : dividendLastPaidDate.hashCode());
		temp = Double.doubleToLongBits(dividendPaidYTD);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + Float.floatToIntBits(dividendRate);
		result = prime * result + ((electronicStatementEnabled == null) ? 0 : electronicStatementEnabled.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((expiresAt == null) ? 0 : expiresAt.hashCode());
		result = prime * result + ((externalIndicator == null) ? 0 : externalIndicator.hashCode());
		result = prime * result + ((favouriteStatus == null) ? 0 : favouriteStatus.hashCode());
		result = prime * result + ((futureInstallmentsCount == null) ? 0 : futureInstallmentsCount.hashCode());
		result = prime * result + ((interestDueDate == null) ? 0 : interestDueDate.hashCode());
		result = prime * result + ((interestEarned == null) ? 0 : interestEarned.hashCode());
		result = prime * result + ((interestPaidYTD == null) ? 0 : interestPaidYTD.hashCode());
		result = prime * result + ((interestRate == null) ? 0 : interestRate.hashCode());
		result = prime * result + ((isBusinessAccount == null) ? 0 : isBusinessAccount.hashCode());
		result = prime * result + ((isPortFolioAccount == null) ? 0 : isPortFolioAccount.hashCode());
		result = prime * result + ((isTypeBusiness == null) ? 0 : isTypeBusiness.hashCode());
		result = prime * result + ((jointHolders == null) ? 0 : jointHolders.hashCode());
		result = prime * result + ((lastPaymentAmount == null) ? 0 : lastPaymentAmount.hashCode());
		result = prime * result + ((lastPaymentDate == null) ? 0 : lastPaymentDate.hashCode());
		result = prime * result + ((limitReference == null) ? 0 : limitReference.hashCode());
		result = prime * result + ((lockedAmount == null) ? 0 : lockedAmount.hashCode());
		result = prime * result + ((logoURL == null) ? 0 : logoURL.hashCode());
		result = prime * result + ((maturityAmount == null) ? 0 : maturityAmount.hashCode());
		result = prime * result + ((maturityDate == null) ? 0 : maturityDate.hashCode());
		result = prime * result + ((maturityOption == null) ? 0 : maturityOption.hashCode());
		result = prime * result + ((nextPaymentAmount == null) ? 0 : nextPaymentAmount.hashCode());
		result = prime * result + ((nextPaymentDate == null) ? 0 : nextPaymentDate.hashCode());
		result = prime * result + ((nickName == null) ? 0 : nickName.hashCode());
		result = prime * result + ((onlineActualBalance == null) ? 0 : onlineActualBalance.hashCode());
		result = prime * result + ((onlineClearedBalance == null) ? 0 : onlineClearedBalance.hashCode());
		result = prime * result + ((openingDate == null) ? 0 : openingDate.hashCode());
		result = prime * result + ((originalAmount == null) ? 0 : originalAmount.hashCode());
		result = prime * result + ((outstandingBalance == null) ? 0 : outstandingBalance.hashCode());
		temp = Double.doubleToLongBits(outstandingOverdraftLimit);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((overDueInstallmentsCount == null) ? 0 : overDueInstallmentsCount.hashCode());
		result = prime * result + ((paidInstallmentsCount == null) ? 0 : paidInstallmentsCount.hashCode());
		result = prime * result + ((partyRole == null) ? 0 : partyRole.hashCode());
		result = prime * result + ((paymentDue == null) ? 0 : paymentDue.hashCode());
		result = prime * result + ((paymentTerm == null) ? 0 : paymentTerm.hashCode());
		result = prime * result + ((payoffAmount == null) ? 0 : payoffAmount.hashCode());
		temp = Double.doubleToLongBits(pendingDeposit);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(pendingWithdrawal);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((portfolioId == null) ? 0 : portfolioId.hashCode());
		result = prime * result + ((principalBalance == null) ? 0 : principalBalance.hashCode());
		result = prime * result + ((principalValue == null) ? 0 : principalValue.hashCode());
		result = prime * result + ((processingTime == null) ? 0 : processingTime.hashCode());
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		result = prime * result + ((productGroupId == null) ? 0 : productGroupId.hashCode());
		result = prime * result + ((productGroupName == null) ? 0 : productGroupName.hashCode());
		result = prime * result + ((productLineName == null) ? 0 : productLineName.hashCode());
		result = prime * result + ((rePaymentFrequency == null) ? 0 : rePaymentFrequency.hashCode());
		result = prime * result + ((routingNumber == null) ? 0 : routingNumber.hashCode());
		result = prime * result + ((sanctionedDate == null) ? 0 : sanctionedDate.hashCode());
		result = prime * result + ((statusDesc == null) ? 0 : statusDesc.hashCode());
		result = prime * result + ((supportBillPay == null) ? 0 : supportBillPay.hashCode());
		result = prime * result + ((supportChecks == null) ? 0 : supportChecks.hashCode());
		result = prime * result + ((supportDeposit == null) ? 0 : supportDeposit.hashCode());
		result = prime * result + ((supportTransferFrom == null) ? 0 : supportTransferFrom.hashCode());
		result = prime * result + ((supportTransferTo == null) ? 0 : supportTransferTo.hashCode());
		result = prime * result + ((swiftCode == null) ? 0 : swiftCode.hashCode());
		result = prime * result + ((termAmount == null) ? 0 : termAmount.hashCode());
		result = prime * result + ((termInterestDue == null) ? 0 : termInterestDue.hashCode());
		result = prime * result + ((totalCreditMonths == null) ? 0 : totalCreditMonths.hashCode());
		result = prime * result + ((totalDebitsMonth == null) ? 0 : totalDebitsMonth.hashCode());
		result = prime * result + ((totalDueAmount == null) ? 0 : totalDueAmount.hashCode());
		result = prime * result + ((transferLimit == null) ? 0 : transferLimit.hashCode());
		result = prime * result + ((typeDescription == null) ? 0 : typeDescription.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		result = prime * result + ((isNew == null) ? 0 : isNew.hashCode());
		result = prime * result + ((productGroup == null) ? 0 : productGroup.hashCode());
		result = prime * result + ((marketValue == null) ? 0 : marketValue.hashCode());
		result = prime * result + ((valueDate == null) ? 0 : valueDate.hashCode());
		result = prime * result + ((serviceType == null) ? 0 : serviceType.hashCode());
		temp = Double.doubleToLongBits(workingBalance);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

    @Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ArrangementsDTO other = (ArrangementsDTO) obj;
		if (Account_id == null) {
			if (other.Account_id != null)
				return false;
		} else if (!Account_id.equals(other.Account_id))
			return false;
		if (IBAN == null) {
			if (other.IBAN != null)
				return false;
		} else if (!IBAN.equals(other.IBAN))
			return false;
		if (Membership_id == null) {
			if (other.Membership_id != null)
				return false;
		} else if (!Membership_id.equals(other.Membership_id))
			return false;
		if (TaxId == null) {
			if (other.TaxId != null)
				return false;
		} else if (!TaxId.equals(other.TaxId))
			return false;
		if (accountCategory == null) {
			if (other.accountCategory != null)
				return false;
		} else if (!accountCategory.equals(other.accountCategory))
			return false;
		if (accountHolder == null) {
			if (other.accountHolder != null)
				return false;
		} else if (!accountHolder.equals(other.accountHolder))
			return false;
		if (accountName == null) {
			if (other.accountName != null)
				return false;
		} else if (!accountName.equals(other.accountName))
			return false;
		if (accountType == null) {
			if (other.accountType != null)
				return false;
		} else if (!accountType.equals(other.accountType))
			return false;
		if (accruedInterest == null) {
			if (other.accruedInterest != null)
				return false;
		} else if (!accruedInterest.equals(other.accruedInterest))
			return false;
		if (actions == null) {
			if (other.actions != null)
				return false;
		} else if (!actions.equals(other.actions))
			return false;
		if (arrangementId == null) {
			if (other.arrangementId != null)
				return false;
		} else if (!arrangementId.equals(other.arrangementId))
			return false;
		if (authToken == null) {
			if (other.authToken != null)
				return false;
		} else if (!authToken.equals(other.authToken))
			return false;
		if (Double.doubleToLongBits(availableBalance) != Double.doubleToLongBits(other.availableBalance))
			return false;
		if (availableBalanceWithLimit == null) {
			if (other.availableBalanceWithLimit != null)
				return false;
		} else if (!availableBalanceWithLimit.equals(other.availableBalanceWithLimit))
			return false;
		if (availableLimit == null) {
			if (other.availableLimit != null)
				return false;
		} else if (!availableLimit.equals(other.availableLimit))
			return false;
		if (backendUserId == null) {
			if (other.backendUserId != null)
				return false;
		} else if (!backendUserId.equals(other.backendUserId))
			return false;
		if (bankCode == null) {
			if (other.bankCode != null)
				return false;
		} else if (!bankCode.equals(other.bankCode))
			return false;
		if (bankname == null) {
			if (other.bankname != null)
				return false;
		} else if (!bankname.equals(other.bankname))
			return false;
		if (blockedAmount == null) {
			if (other.blockedAmount != null)
				return false;
		} else if (!blockedAmount.equals(other.blockedAmount))
			return false;
		if (channelDescription == null) {
			if (other.channelDescription != null)
				return false;
		} else if (!channelDescription.equals(other.channelDescription))
			return false;
		if (clearedBalance == null) {
			if (other.clearedBalance != null)
				return false;
		} else if (!clearedBalance.equals(other.clearedBalance))
			return false;
		if (companyCode == null) {
			if (other.companyCode != null)
				return false;
		} else if (!companyCode.equals(other.companyCode))
			return false;
		if (connectionAlertDays == null) {
			if (other.connectionAlertDays != null)
				return false;
		} else if (!connectionAlertDays.equals(other.connectionAlertDays))
			return false;
		if (currencyCode == null) {
			if (other.currencyCode != null)
				return false;
		} else if (!currencyCode.equals(other.currencyCode))
			return false;
		if (currentDue == null) {
			if (other.currentDue != null)
				return false;
		} else if (!currentDue.equals(other.currentDue))
			return false;
		if (currentAmountDue == null) {
			if (other.currentAmountDue != null)
				return false;
		} else if (!currentAmountDue.equals(other.currentAmountDue))
			return false;
		if (Double.doubleToLongBits(currentBalance) != Double.doubleToLongBits(other.currentBalance))
			return false;
		if (customerID == null) {
			if (other.customerID != null)
				return false;
		} else if (!customerID.equals(other.customerID))
			return false;
		if (customerType == null) {
			if (other.customerType != null)
				return false;
		} else if (!customerType.equals(other.customerType))
			return false;
		if (dbpErrCode == null) {
			if (other.dbpErrCode != null)
				return false;
		} else if (!dbpErrCode.equals(other.dbpErrCode))
			return false;
		if (dbpErrMsg == null) {
			if (other.dbpErrMsg != null)
				return false;
		} else if (!dbpErrMsg.equals(other.dbpErrMsg))
			return false;
		if (deviceId == null) {
			if (other.deviceId != null)
				return false;
		} else if (!deviceId.equals(other.deviceId))
			return false;
		if (Double.doubleToLongBits(disbursedAmount) != Double.doubleToLongBits(other.disbursedAmount))
			return false;
		if (displayName == null) {
			if (other.displayName != null)
				return false;
		} else if (!displayName.equals(other.displayName))
			return false;
		if (Double.doubleToLongBits(dividendLastPaidAmount) != Double.doubleToLongBits(other.dividendLastPaidAmount))
			return false;
		if (dividendLastPaidDate == null) {
			if (other.dividendLastPaidDate != null)
				return false;
		} else if (!dividendLastPaidDate.equals(other.dividendLastPaidDate))
			return false;
		if (Double.doubleToLongBits(dividendPaidYTD) != Double.doubleToLongBits(other.dividendPaidYTD))
			return false;
		if (Float.floatToIntBits(dividendRate) != Float.floatToIntBits(other.dividendRate))
			return false;
		if (dueDate == null) {
			if (other.dueDate != null)
				return false;
		} else if (!dueDate.equals(other.dueDate))
			return false;
		if (electronicStatementEnabled == null) {
			if (other.electronicStatementEnabled != null)
				return false;
		} else if (!electronicStatementEnabled.equals(other.electronicStatementEnabled))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (expiresAt == null) {
			if (other.expiresAt != null)
				return false;
		} else if (!expiresAt.equals(other.expiresAt))
			return false;
		if (externalIndicator == null) {
			if (other.externalIndicator != null)
				return false;
		} else if (!externalIndicator.equals(other.externalIndicator))
			return false;
		if (favouriteStatus == null) {
			if (other.favouriteStatus != null)
				return false;
		} else if (!favouriteStatus.equals(other.favouriteStatus))
			return false;
		if (futureInstallmentsCount == null) {
			if (other.futureInstallmentsCount != null)
				return false;
		} else if (!futureInstallmentsCount.equals(other.futureInstallmentsCount))
			return false;
		if (interestDueDate == null) {
			if (other.interestDueDate != null)
				return false;
		} else if (!interestDueDate.equals(other.interestDueDate))
			return false;
		if (interestEarned == null) {
			if (other.interestEarned != null)
				return false;
		} else if (!interestEarned.equals(other.interestEarned))
			return false;
		if (interestPaidYTD == null) {
			if (other.interestPaidYTD != null)
				return false;
		} else if (!interestPaidYTD.equals(other.interestPaidYTD))
			return false;
		if (interestRate == null) {
			if (other.interestRate != null)
				return false;
		} else if (!interestRate.equals(other.interestRate))
			return false;
		if (isBusinessAccount == null) {
			if (other.isBusinessAccount != null)
				return false;
		} else if (!isBusinessAccount.equals(other.isBusinessAccount))
			return false;
		if (isPortFolioAccount == null) {
			if (other.isPortFolioAccount != null)
				return false;
		} else if (!isPortFolioAccount.equals(other.isPortFolioAccount))
			return false;
		if (isTypeBusiness == null) {
			if (other.isTypeBusiness != null)
				return false;
		} else if (!isTypeBusiness.equals(other.isTypeBusiness))
			return false;
		if (jointHolders == null) {
			if (other.jointHolders != null)
				return false;
		} else if (!jointHolders.equals(other.jointHolders))
			return false;
		if (lastPaymentAmount == null) {
			if (other.lastPaymentAmount != null)
				return false;
		} else if (!lastPaymentAmount.equals(other.lastPaymentAmount))
			return false;
		if (lastPaymentDate == null) {
			if (other.lastPaymentDate != null)
				return false;
		} else if (!lastPaymentDate.equals(other.lastPaymentDate))
			return false;
		if (limitReference == null) {
			if (other.limitReference != null)
				return false;
		} else if (!limitReference.equals(other.limitReference))
			return false;
		if (lockedAmount == null) {
			if (other.lockedAmount != null)
				return false;
		} else if (!lockedAmount.equals(other.lockedAmount))
			return false;
		if (logoURL == null) {
			if (other.logoURL != null)
				return false;
		} else if (!logoURL.equals(other.logoURL))
			return false;
		if (maturityAmount == null) {
			if (other.maturityAmount != null)
				return false;
		} else if (!maturityAmount.equals(other.maturityAmount))
			return false;
		if (maturityDate == null) {
			if (other.maturityDate != null)
				return false;
		} else if (!maturityDate.equals(other.maturityDate))
			return false;
		if (maturityOption == null) {
			if (other.maturityOption != null)
				return false;
		} else if (!maturityOption.equals(other.maturityOption))
			return false;
		if (nextPaymentAmount == null) {
			if (other.nextPaymentAmount != null)
				return false;
		} else if (!nextPaymentAmount.equals(other.nextPaymentAmount))
			return false;
		if (nextPaymentDate == null) {
			if (other.nextPaymentDate != null)
				return false;
		} else if (!nextPaymentDate.equals(other.nextPaymentDate))
			return false;
		if (nickName == null) {
			if (other.nickName != null)
				return false;
		} else if (!nickName.equals(other.nickName))
			return false;
		if (onlineActualBalance == null) {
			if (other.onlineActualBalance != null)
				return false;
		} else if (!onlineActualBalance.equals(other.onlineActualBalance))
			return false;
		if (onlineClearedBalance == null) {
			if (other.onlineClearedBalance != null)
				return false;
		} else if (!onlineClearedBalance.equals(other.onlineClearedBalance))
			return false;
		if (openingDate == null) {
			if (other.openingDate != null)
				return false;
		} else if (!openingDate.equals(other.openingDate))
			return false;
		if (originalAmount == null) {
			if (other.originalAmount != null)
				return false;
		} else if (!originalAmount.equals(other.originalAmount))
			return false;
		if (outstandingBalance == null) {
			if (other.outstandingBalance != null)
				return false;
		} else if (!outstandingBalance.equals(other.outstandingBalance))
			return false;
		if (Double.doubleToLongBits(outstandingOverdraftLimit) != Double
				.doubleToLongBits(other.outstandingOverdraftLimit))
			return false;
		if (overDueInstallmentsCount == null) {
			if (other.overDueInstallmentsCount != null)
				return false;
		} else if (!overDueInstallmentsCount.equals(other.overDueInstallmentsCount))
			return false;
		if (paidInstallmentsCount == null) {
			if (other.paidInstallmentsCount != null)
				return false;
		} else if (!paidInstallmentsCount.equals(other.paidInstallmentsCount))
			return false;
		if (partyRole == null) {
			if (other.partyRole != null)
				return false;
		} else if (!partyRole.equals(other.partyRole))
			return false;
		if (paymentDue == null) {
			if (other.paymentDue != null)
				return false;
		} else if (!paymentDue.equals(other.paymentDue))
			return false;
		if (paymentTerm == null) {
			if (other.paymentTerm != null)
				return false;
		} else if (!paymentTerm.equals(other.paymentTerm))
			return false;
		if (payoffAmount == null) {
			if (other.payoffAmount != null)
				return false;
		} else if (!payoffAmount.equals(other.payoffAmount))
			return false;
		if (Double.doubleToLongBits(pendingDeposit) != Double.doubleToLongBits(other.pendingDeposit))
			return false;
		if (Double.doubleToLongBits(pendingWithdrawal) != Double.doubleToLongBits(other.pendingWithdrawal))
			return false;
		if (portfolioId == null) {
			if (other.portfolioId != null)
				return false;
		} else if (!portfolioId.equals(other.portfolioId))
			return false;
		if (principalBalance == null) {
			if (other.principalBalance != null)
				return false;
		} else if (!principalBalance.equals(other.principalBalance))
			return false;
		if (principalValue == null) {
			if (other.principalValue != null)
				return false;
		} else if (!principalValue.equals(other.principalValue))
			return false;
		if (processingTime == null) {
			if (other.processingTime != null)
				return false;
		} else if (!processingTime.equals(other.processingTime))
			return false;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		if (productGroupId == null) {
			if (other.productGroupId != null)
				return false;
		} else if (!productGroupId.equals(other.productGroupId))
			return false;
		if (productGroupName == null) {
			if (other.productGroupName != null)
				return false;
		} else if (!productGroupName.equals(other.productGroupName))
			return false;
		if (productLineName == null) {
			if (other.productLineName != null)
				return false;
		} else if (!productLineName.equals(other.productLineName))
			return false;
		if (rePaymentFrequency == null) {
			if (other.rePaymentFrequency != null)
				return false;
		} else if (!rePaymentFrequency.equals(other.rePaymentFrequency))
			return false;
		if (routingNumber == null) {
			if (other.routingNumber != null)
				return false;
		} else if (!routingNumber.equals(other.routingNumber))
			return false;
		if (sanctionedDate == null) {
			if (other.sanctionedDate != null)
				return false;
		} else if (!sanctionedDate.equals(other.sanctionedDate))
			return false;
		if (statusDesc == null) {
			if (other.statusDesc != null)
				return false;
		} else if (!statusDesc.equals(other.statusDesc))
			return false;
		if (supportBillPay == null) {
			if (other.supportBillPay != null)
				return false;
		} else if (!supportBillPay.equals(other.supportBillPay))
			return false;
		if (supportChecks == null) {
			if (other.supportChecks != null)
				return false;
		} else if (!supportChecks.equals(other.supportChecks))
			return false;
		if (supportDeposit == null) {
			if (other.supportDeposit != null)
				return false;
		} else if (!supportDeposit.equals(other.supportDeposit))
			return false;
		if (supportTransferFrom == null) {
			if (other.supportTransferFrom != null)
				return false;
		} else if (!supportTransferFrom.equals(other.supportTransferFrom))
			return false;
		if (supportTransferTo == null) {
			if (other.supportTransferTo != null)
				return false;
		} else if (!supportTransferTo.equals(other.supportTransferTo))
			return false;
		if (swiftCode == null) {
			if (other.swiftCode != null)
				return false;
		} else if (!swiftCode.equals(other.swiftCode))
			return false;
		if (termAmount == null) {
			if (other.termAmount != null)
				return false;
		} else if (!termAmount.equals(other.termAmount))
			return false;
		if (termInterestDue == null) {
			if (other.termInterestDue != null)
				return false;
		} else if (!termInterestDue.equals(other.termInterestDue))
			return false;
		if (totalCreditMonths == null) {
			if (other.totalCreditMonths != null)
				return false;
		} else if (!totalCreditMonths.equals(other.totalCreditMonths))
			return false;
		if (totalDebitsMonth == null) {
			if (other.totalDebitsMonth != null)
				return false;
		} else if (!totalDebitsMonth.equals(other.totalDebitsMonth))
			return false;
		if (totalDueAmount == null) {
			if (other.totalDueAmount != null)
				return false;
		} else if (!totalDueAmount.equals(other.totalDueAmount))
			return false;
		if (transferLimit == null) {
			if (other.transferLimit != null)
				return false;
		} else if (!transferLimit.equals(other.transferLimit))
			return false;
		if (typeDescription == null) {
			if (other.typeDescription != null)
				return false;
		} else if (!typeDescription.equals(other.typeDescription))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		if (Double.doubleToLongBits(workingBalance) != Double.doubleToLongBits(other.workingBalance))
			return false;
		if (isNew == null) {
            if (other.isNew != null)
                return false;
		}
		else if (!isNew.equals(other.isNew))
            return false;
		
		if (productGroup == null) {
            if (other.productGroup != null)
                return false;
        }
        else if (!productGroup.equals(other.productGroup))
            return false;
		if (valueDate == null) {
            if (other.valueDate != null)
                return false;
        }
        else if (!valueDate.equals(other.valueDate))
            return false;
		if (marketValue == null) {
            if (other.marketValue != null)
                return false;
        }
        else if (!marketValue.equals(other.marketValue))
            return false;
		if (serviceType == null) {
            if (other.serviceType != null)
                return false;
        }
        else if (!serviceType.equals(other.serviceType))
            return false;
		return true;
	}

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    public String getIsPortFolioAccount() {
        return isPortFolioAccount;
    }

    public void setIsPortFolioAccount(String isPortFolioAccount) {
        this.isPortFolioAccount = isPortFolioAccount;
    }

	public String getInterestPaidYTD() {
		return interestPaidYTD;
	}

	public void setInterestPaidYTD(String interestPaidYTD) {
		this.interestPaidYTD = interestPaidYTD;
	}

    /**
     * @return the isNew
     */
    public String getIsNew() {
        return isNew;
    }

    /**
     * @param isNew the isNew to set
     */
    public void setIsNew(String isNew) {
        this.isNew = isNew;
    }

    /**
     * @return the productGroup
     */
    public String getProductGroup() {
        return productGroup;
    }

    /**
     * @param productGroup the productGroup to set
     */
    public void setProductGroup(String productGroup) {
        this.productGroup = productGroup;
    }

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getValueDate() {
		return valueDate;
	}

	public void setValueDate(String valueDate) {
		this.valueDate = valueDate;
	}

	public String getMarketValue() {
		return marketValue;
	}

	public void setMarketValue(String marketValue) {
		this.marketValue = marketValue;
	}

}
