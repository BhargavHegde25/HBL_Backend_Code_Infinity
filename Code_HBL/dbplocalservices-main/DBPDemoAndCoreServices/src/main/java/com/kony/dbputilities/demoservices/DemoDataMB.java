package com.kony.dbputilities.demoservices;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class DemoDataMB {

    private Map<String, String> generatedAccounts;
    private Map<String, String> generatedPayees;
    private Map<String, String> generatedPersons;
    private Map<String, String> generatedBills;
    private Map<String, String> accountTypes;
    private Map<String, String> transactionTypes;
    private Map<String, String> generatedExternalAccounts;
    private String MBOrgId;
    
    public String getMBOrgId() {
		return MBOrgId;
	}

	public void setMBOrgId(String mBOrgId) {
		this.MBOrgId = mBOrgId;
	}

	public void addAccountNum(String key, String accountNum) {
        if (null == generatedAccounts) {
            generatedAccounts = new HashMap<>();
        }
        generatedAccounts.put(key, accountNum);
    }

    public String getAccountNum(String key) {
        if (null != generatedAccounts) {
            return generatedAccounts.get(key);
        }
        return null;
    }

    public void addExternalAccountNum(String key, String accountNum) {
        if (null == generatedExternalAccounts) {
            generatedExternalAccounts = new HashMap<>();
        }
        generatedExternalAccounts.put(key, accountNum);
    }

    public String getExternalAccountNum(String key) {
        if (null != generatedExternalAccounts) {
            return generatedExternalAccounts.get(key);
        }
        return null;
    }

    public void addPerson(String key, String personId) {
        if (null == generatedPersons) {
            generatedPersons = new HashMap<>();
        }
        generatedPersons.put(key, personId);
    }

    public String getPerson(String key) {
        if (null != generatedPersons) {
            return generatedPersons.get(key);
        }
        return null;
    }

    public void addPayee(String key, String payeeId) {
        if (null == generatedPayees) {
            generatedPayees = new HashMap<>();
        }
        generatedPayees.put(key, payeeId);
    }

    public String getPayee(String key) {
        if (null != generatedPayees) {
            return generatedPayees.get(key);
        }
        return null;
    }

    public void addBill(String key, String billId) {
        if (null == generatedBills) {
            generatedBills = new HashMap<>();
        }
        generatedBills.put(key, billId);
    }

    public String getBill(String key) {
        if (null != generatedBills) {
            return generatedBills.get(key);
        }
        return null;
    }

    public String getAccountId(String accountType) {
        if (null != accountTypes) {
            return accountTypes.get(accountType);
        }
        return null;
    }

    public String getTransactionId(String transType) {
        if (null != transactionTypes) {
            return transactionTypes.get(transType);
        }
        return null;
    }

    public void init(DataControllerRequest dcRequest) throws HttpCallException {
        this.accountTypes = getAccountTypes(dcRequest, "US");
        this.transactionTypes = getTransTypes(dcRequest);
    }

    private Map<String, String> getAccountTypes(DataControllerRequest dcRequest, String countryCode)
            throws HttpCallException {
        Result accountTypes = HelperMethods.callGetApi(dcRequest, null, HelperMethods.getHeaders(dcRequest),
                URLConstants.ACCOUNTTYPE_GET);
        Map<String, String> accountTypeMap = new HashMap<>();
        List<Record> types = accountTypes.getAllDatasets().get(0).getAllRecords();
        for (Record type : types) {
            accountTypeMap.put(HelperMethods.getFieldValue(type, "TypeDescription"),
                    HelperMethods.getFieldValue(type, "TypeID"));
        }
        return accountTypeMap;
    }

    private Map<String, String> getTransTypes(DataControllerRequest dcRequest) throws HttpCallException {
        Result accountTypes = HelperMethods.callApi(dcRequest, null, HelperMethods.getHeaders(dcRequest),
                URLConstants.TRANSACTION_TYPE_GET);
        Map<String, String> accountTypeMap = new HashMap<>();
        List<Record> types = accountTypes.getAllDatasets().get(0).getAllRecords();
        for (Record type : types) {
            accountTypeMap.put(HelperMethods.getFieldValue(type, "description"),
                    HelperMethods.getFieldValue(type, "Id"));
        }
        return accountTypeMap;
    }

    public Map<String, String> getAdminCustomerDemoData() {
        Map<String, String> inputParam = new HashMap<>();

        inputParam.put("FirstName", "Michael");
        inputParam.put("LastName", "Davis");
        // inputParam.put("UserImageURL", "https://retailbanking1.konycloud.com/dbimages/displayPicture.png");

        inputParam.put("Ssn", "906545256");
        inputParam.put("areUserAlertsTurnedOn", "1");
        inputParam.put("areDepositTermsAccepted", "0");
        inputParam.put("areAccountStatementTermsAccepted", "1");
        inputParam.put("IsPhoneEnabled", "0");
        inputParam.put("isBillPaySupported", "1");
        inputParam.put("isP2PSupported", "1");
        inputParam.put("isBillPayActivated", "1");
        inputParam.put("isP2PActivated", "1");
        inputParam.put("isWireTransferEligible", "1");
        inputParam.put("isWireTransferActivated", "1");
        inputParam.put("IsEmailEnabled", "1");

        inputParam.put("Lastlogintime", "2019-11-02");
        inputParam.put("DateOfBirth", "1985-05-05");
        inputParam.put("isEagreementSigned", "1");
        return inputParam;
    }

    public Map<String, String> getAdminCustomerCommunicationDemoData() {
        Map<String, String> inputParam = new HashMap<>();

        inputParam.put("Email", "michael.davis@gmail.com");
        inputParam.put("Phone", "+91-4258303691");
        return inputParam;
    }

    public Map<String, String> getMBOrganisationCommunication() {
        Map<String, String> inputParam = new HashMap<>();

        inputParam.put("Email", "michael.davis@gmail.com");
        inputParam.put("Phone", "+91-4258303691");
        return inputParam;
    }

    public Map<String, String> getAdminCustomerPreferenceDemoData() {
        Map<String, String> inputParam = new HashMap<>();

        inputParam.put("isBillPaySupported", "1");
        inputParam.put("isP2PSupported", "1");
        inputParam.put("isBillPayActivated", "1");
        inputParam.put("isP2PActivated", "1");
        inputParam.put("isWireTransferEligible", "1");
        inputParam.put("isWireTransferActivated", "1");
        inputParam.put("ShowBillPayFromAccPopup", "1");
        return inputParam;
    }
    
    public Map<String, String> getAddress1DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("Region_id", "R35");
        inputParam.put("isPreferredAddress", "1");
        inputParam.put("City_id", "CITY1437");
        inputParam.put("cityName", "Santa Clara");
        inputParam.put("addressLine1", "7380,");
        inputParam.put("addressLine2", "West Sand Lake Road,");
        inputParam.put("zipCode", "32819");
        inputParam.put("country", "USA");
        inputParam.put("type", "home");
        inputParam.put("state", "Florida");
        return inputParam;
    }

    public Map<String, String> getMBAccount1DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("Type_id", "Savings");
        inputParam.put("Name", "Rewards Savings");
        inputParam.put("AccountName", "Rewards Savings");
        inputParam.put("NickName", "My Savings");
        inputParam.put("OpeningDate", "2016-10-10");
        inputParam.put("StatusDesc", "Active");
        inputParam.put("FavouriteStatus", "1");
        inputParam.put("AvailableBalance", "6885.39");
        inputParam.put("CurrentBalance", "7332.39");
        inputParam.put("PendingDeposit", "53");
        inputParam.put("PendingWithdrawal", "500");
        inputParam.put("ShowTransactions", "1");
        inputParam.put("SupportBillPay", "1");
        inputParam.put("SupportDeposit", "1");
        inputParam.put("SupportTransferFrom", "1");
        inputParam.put("SupportTransferTo", "1");
        inputParam.put("SupportCardlessCash", "1");
        inputParam.put("TransactionLimit", "2500");
        inputParam.put("TransferLimit", "5000");
        inputParam.put("IsPFM", "1");
        inputParam.put("EStatementmentEnable", "0");
        inputParam.put("AccountPreference", "0");
        inputParam.put("CurrencyCode", "USD");
        inputParam.put("AccountHolder", "John");
        inputParam.put("RoutingNumber", "648721691");
        inputParam.put("SwiftCode", "CTBAUS124");
        inputParam.put("BsbNum", "123456789");
        inputParam.put("JointHolders", "Jane Bailey");
        inputParam.put("DividendRate", "0");
        inputParam.put("DividendYTD", "0");
        inputParam.put("LastDividendPaidAmount", "0");
        inputParam.put("PreviousYearDividend", "0");
        inputParam.put("DividendPaidYTD", "0");
        inputParam.put("DividendLastPaidAmount", "0");
        inputParam.put("PreviousYearsDividends", "0");
        inputParam.put("TotalCreditMonths", "370");
        inputParam.put("TotalDebitsMonth", "734");
        inputParam.put("adminProductId", "PRODUCT3");
        inputParam.put("AccountHolder2", "John Doe");
        return inputParam;
    }

    public Map<String, String> getMBAccount2DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("Type_id", "Checking");
        inputParam.put("Name", "Core Checking");
        inputParam.put("AccountName", "Core Checking");
        inputParam.put("NickName", "My Checking");
        inputParam.put("OpeningDate", "2017-09-10");
        inputParam.put("StatusDesc", "Active");
        inputParam.put("FavouriteStatus", "1");
        inputParam.put("AvailableBalance", "5937.69");
        inputParam.put("CurrentBalance", "3670.78");
        inputParam.put("PendingDeposit", "2416.91");
        inputParam.put("PendingWithdrawal", "150");
        inputParam.put("ShowTransactions", "1");
        inputParam.put("SupportBillPay", "1");
        inputParam.put("SupportDeposit", "1");
        inputParam.put("SupportTransferFrom", "1");
        inputParam.put("SupportTransferTo", "1");
        inputParam.put("SupportCardlessCash", "1");
        inputParam.put("TransactionLimit", "1500");
        inputParam.put("TransferLimit", "1000");
        inputParam.put("IsPFM", "1");
        inputParam.put("EStatementmentEnable", "0");
        inputParam.put("AccountPreference", "1");
        inputParam.put("CurrencyCode", "USD");
        inputParam.put("AccountHolder", "John");
        inputParam.put("RoutingNumber", "648721615");
        inputParam.put("SwiftCode", "CTBAUS124");
        inputParam.put("BsbNum", "123456789");
        inputParam.put("JointHolders", "Jane Bailey");
        inputParam.put("DividendRate", "0");
        inputParam.put("DividendYTD", "0");
        inputParam.put("LastDividendPaidAmount", "0");
        inputParam.put("PreviousYearDividend", "0");
        inputParam.put("DividendPaidYTD", "0");
        inputParam.put("DividendLastPaidAmount", "0");
        inputParam.put("DividendLastPaidDate", "");
        inputParam.put("PreviousYearsDividends", "0");
        inputParam.put("LateFeesDue", "43");
        inputParam.put("BondInterest", "0");
        inputParam.put("BondInterestLastYear", "0");
        inputParam.put("TotalCreditMonths", "370");
        inputParam.put("TotalDebitsMonth", "734");
        inputParam.put("InterestEarned", "23");
        inputParam.put("adminProductId", "PRODUCT4");
        inputParam.put("AccountHolder2", "John Doe");
        return inputParam;
    }

    public Map<String, String> getMBAccount3DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("Type_id", "CreditCard");
        inputParam.put("Name", "Freedom Credit Card");
        inputParam.put("AccountName", "Freedom Credit Card");
        inputParam.put("NickName", "My Credit Card");
        inputParam.put("OpeningDate", "2016-10-10");
        inputParam.put("ClosingDate", "");
        inputParam.put("StatusDesc", "Active");
        inputParam.put("FavouriteStatus", "0");
        inputParam.put("AvailableBalance", "0");
        inputParam.put("CurrentBalance", "3119.61");
        inputParam.put("PendingDeposit", "");
        inputParam.put("PendingWithdrawal", "");
        inputParam.put("LastStatementBalance", "3070.64");
        inputParam.put("ShowTransactions", "1");
        inputParam.put("SupportBillPay", "1");
        inputParam.put("SupportDeposit", "1");
        inputParam.put("SupportTransferFrom", "1");
        inputParam.put("SupportTransferTo", "1");
        inputParam.put("SupportCardlessCash", "0");
        inputParam.put("TransactionLimit", "5000");
        inputParam.put("TransferLimit", "500");
        inputParam.put("IsPFM", "1");
        inputParam.put("EStatementmentEnable", "0");
        inputParam.put("AccountPreference", "2");
        inputParam.put("CurrencyCode", "USD");
        inputParam.put("AccountHolder", "Michael Davis");
        inputParam.put("RoutingNumber", "648721691");
        inputParam.put("SwiftCode", "CTBAUS124");
        inputParam.put("BsbNum", "123456789");
        inputParam.put("JointHolders", "Susan Davis");
        inputParam.put("DividendRate", "");
        inputParam.put("DividendYTD", "");
        inputParam.put("LastDividendPaidAmount", "");
        inputParam.put("LastDividendPaidDate", "");
        inputParam.put("PreviousYearDividend", "");
        inputParam.put("DividendPaidYTD", "");
        inputParam.put("DividendLastPaidAmount", "");
        inputParam.put("DividendLastPaidDate", "");
        inputParam.put("PreviousYearsDividends", "");
        inputParam.put("AvailableCredit", "4880.39");
        inputParam.put("DueDate", "2018-12-08");
        inputParam.put("MinimumDue", "276");
        inputParam.put("OutstandingBalance", "619.609999999989");
        inputParam.put("CreditCardNumber", "4541982333084990");
        inputParam.put("AvailablePoints", "35");
        inputParam.put("CurrentAmountDue", "3070.64");
        inputParam.put("LastPaymentAmount", "3019.26");
        inputParam.put("LateFeesDue", "10");
        inputParam.put("CreditLimit", "8000");
        inputParam.put("InterestRate", "14.62");
        inputParam.put("MaturityDate", "");
        inputParam.put("MaturityOption", "");
        inputParam.put("BondInterest", "");
        inputParam.put("BondInterestLastYear", "");
        inputParam.put("TotalCreditMonths", "");
        inputParam.put("TotalDebitsMonth", "");
        inputParam.put("InterestEarned", "");
        inputParam.put("maturityAmount", "");
        inputParam.put("PaymentTerm", "");
        inputParam.put("PrincipalValue", "");
        inputParam.put("PaymentDue", "28");
        inputParam.put("InterestPaidYTD", "");
        inputParam.put("InterestPaidPreviousYTD", "");
        inputParam.put("UnpaidInterest", "");
        inputParam.put("RegularPaymentAmount", "");
        inputParam.put("principalBalance", "");
        inputParam.put("OriginalAmount", "");
        inputParam.put("payoffAmount", "");
        inputParam.put("PayOffCharge", "");
        inputParam.put("InterestPaidLastYear", "");
        inputParam.put("BankName", "");
        inputParam.put("AccountHolder2", "John Doe");
        return inputParam;
    }

    public Map<String, String> getMBAccount4DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("Type_id", "Deposit");
        inputParam.put("Name", "12 Months Term Deposit");
        inputParam.put("AccountName", "12 Months Term Deposit");
        inputParam.put("NickName", "12 Months Term Deposit");
        inputParam.put("OpeningDate", "2018-11-08");
        inputParam.put("ClosingDate", "");
        inputParam.put("StatusDesc", "Active");
        inputParam.put("FavouriteStatus", "0");
        inputParam.put("AvailableBalance", "0");
        inputParam.put("CurrentBalance", "5000");
        inputParam.put("PendingDeposit", "");
        inputParam.put("PendingWithdrawal", "");
        inputParam.put("LastStatementBalance", "");
        inputParam.put("ShowTransactions", "1");
        inputParam.put("SupportBillPay", "0");
        inputParam.put("SupportDeposit", "0");
        inputParam.put("SupportTransferFrom", "0");
        inputParam.put("SupportTransferTo", "0");
        inputParam.put("SupportCardlessCash", "0");
        inputParam.put("TransactionLimit", "");
        inputParam.put("TransferLimit", "");
        inputParam.put("IsPFM", "0");
        inputParam.put("EStatementmentEnable", "0");
        inputParam.put("AccountPreference", "4");
        inputParam.put("CurrencyCode", "USD");
        inputParam.put("AccountHolder", "Michael Davis");
        inputParam.put("RoutingNumber", "648721691");
        inputParam.put("SwiftCode", "CTBAUS124");
        inputParam.put("BsbNum", "123456789");
        inputParam.put("JointHolders", "Susan Davis");
        inputParam.put("DividendRate", "0.45");
        inputParam.put("DividendYTD", "34");
        inputParam.put("LastDividendPaidAmount", "34");
        inputParam.put("LastDividendPaidDate", "2018-08-01");
        inputParam.put("PreviousYearDividend", "70");
        inputParam.put("DividendPaidYTD", "34");
        inputParam.put("DividendLastPaidAmount", "34");
        inputParam.put("DividendLastPaidDate", "218-08-01");
        inputParam.put("PreviousYearsDividends", "70");
        inputParam.put("AvailableCredit", "");
        inputParam.put("DueDate", "");
        inputParam.put("MinimumDue", "");
        inputParam.put("OutstandingBalance", "");
        inputParam.put("CreditCardNumber", "null");
        inputParam.put("AvailablePoints", "0");
        inputParam.put("CurrentAmountDue", "");
        inputParam.put("LastPaymentAmount", "");
        inputParam.put("LateFeesDue", "23");
        inputParam.put("CreditLimit", "");
        inputParam.put("InterestRate", "0.45");
        inputParam.put("MaturityDate", "2018-08-11");
        inputParam.put("MaturityOption", "Renew");
        inputParam.put("BondInterest", "");
        inputParam.put("BondInterestLastYear", "");
        inputParam.put("TotalCreditMonths", "");
        inputParam.put("TotalDebitsMonth", "");
        inputParam.put("InterestEarned", "45");
        inputParam.put("maturityAmount", "5600");
        inputParam.put("PaymentTerm", "4");
        inputParam.put("PrincipalValue", "");
        inputParam.put("PaymentDue", "");
        inputParam.put("InterestPaidYTD", "");
        inputParam.put("InterestPaidPreviousYTD", "");
        inputParam.put("UnpaidInterest", "");
        inputParam.put("RegularPaymentAmount", "");
        inputParam.put("principalBalance", "");
        inputParam.put("OriginalAmount", "");
        inputParam.put("payoffAmount", "");
        inputParam.put("PayOffCharge", "");
        inputParam.put("InterestPaidLastYear", "");
        inputParam.put("BankName", "");
        inputParam.put("AccountHolder2", "John Doe");
        return inputParam;
    }

    public Map<String, String> getMBAccount5DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("Type_id", "Loan");
        inputParam.put("Name", "Turbo Auto Loan");
        inputParam.put("AccountName", "Turbo Auto Loan");
        inputParam.put("NickName", "Turbo Auto Loan");
        inputParam.put("OpeningDate", "2018-08-08");
        inputParam.put("ClosingDate", "2019-09-09");
        inputParam.put("StatusDesc", "Active");
        inputParam.put("FavouriteStatus", "0");
        inputParam.put("AvailableBalance", "0");
        inputParam.put("CurrentBalance", "0");
        inputParam.put("PendingDeposit", "");
        inputParam.put("PendingWithdrawal", "");
        inputParam.put("LastStatementBalance", "");
        inputParam.put("ShowTransactions", "1");
        inputParam.put("SupportBillPay", "0");
        inputParam.put("SupportDeposit", "0");
        inputParam.put("SupportTransferFrom", "0");
        inputParam.put("SupportTransferTo", "1");
        inputParam.put("SupportCardlessCash", "0");
        inputParam.put("TransactionLimit", "");
        inputParam.put("TransferLimit", "");
        inputParam.put("IsPFM", "0");
        inputParam.put("EStatementmentEnable", "0");
        inputParam.put("AccountPreference", "3");
        inputParam.put("CurrencyCode", "USD");
        inputParam.put("AccountHolder", "Michael Davis");
        inputParam.put("RoutingNumber", "648721691");
        inputParam.put("SwiftCode", "CTBAUS124");
        inputParam.put("BsbNum", "123456789");
        inputParam.put("JointHolders", "");
        inputParam.put("DividendRate", "");
        inputParam.put("DividendYTD", "");
        inputParam.put("LastDividendPaidAmount", "");
        inputParam.put("LastDividendPaidDate", "");
        inputParam.put("PreviousYearDividend", "");
        inputParam.put("DividendPaidYTD", "");
        inputParam.put("DividendLastPaidAmount", "");
        inputParam.put("DividendLastPaidDate", "");
        inputParam.put("PreviousYearsDividends", "");
        inputParam.put("AvailableCredit", "");
        inputParam.put("DueDate", "2018-12-08");
        inputParam.put("MinimumDue", "");
        inputParam.put("OutstandingBalance", "16760.2288358465");
        inputParam.put("CreditCardNumber", "");
        inputParam.put("AvailablePoints", "");
        inputParam.put("CurrentAmountDue", "736");
        inputParam.put("LastPaymentAmount", "368");
        inputParam.put("LateFeesDue", "11");
        inputParam.put("CreditLimit", "");
        inputParam.put("InterestRate", "2.25");
        inputParam.put("MaturityDate", "");
        inputParam.put("MaturityOption", "");
        inputParam.put("BondInterest", "");
        inputParam.put("BondInterestLastYear", "");
        inputParam.put("TotalCreditMonths", "");
        inputParam.put("TotalDebitsMonth", "");
        inputParam.put("InterestEarned", "");
        inputParam.put("maturityAmount", "");
        inputParam.put("PaymentTerm", "");
        inputParam.put("PrincipalValue", "");
        inputParam.put("PaymentDue", "13");
        inputParam.put("InterestPaidYTD", "242.302407608797");
        inputParam.put("InterestPaidPreviousYTD", "289");
        inputParam.put("UnpaidInterest", "");
        inputParam.put("RegularPaymentAmount", "");
        inputParam.put("principalBalance", "16024.2288358465");
        inputParam.put("OriginalAmount", "20000");
        inputParam.put("payoffAmount", "16773.2288358465");
        inputParam.put("PayOffCharge", "749");
        inputParam.put("InterestPaidLastYear", "289");
        inputParam.put("BankName", "");
        inputParam.put("AccountHolder2", "John Doe");
        return inputParam;
    }

    public Map<String, String> getPhone1DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("receivePromotions", "1");
        inputParam.put("isPrimary", "1");
        inputParam.put("phoneNumber", "+91-4258303691");
        inputParam.put("extension", "Mobile");
        inputParam.put("countryType", "Domestic");
        inputParam.put("type", "Mobile");
        return inputParam;
    }

    public Map<String, String> getPayee1DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("accountNumber", "7790396982957");
        inputParam.put("addressLine1", "ABC Energy LLC");
        inputParam.put("addressLine2", "200 East Post Road");
        inputParam.put("state", "Florida");
        inputParam.put("cityName", "Orlando");
        inputParam.put("nickName", "Electricity - ABC");
        inputParam.put("companyName", "ABC Energy");
        inputParam.put("email", "electricity@gmail.com");
        inputParam.put("firstName", "Electricity");
        inputParam.put("lastName", "Bill");
        inputParam.put("name", "ABC Energy");
        inputParam.put("eBillEnable", "1");
        inputParam.put("isAutoPayEnabled", "0");
        inputParam.put("nameOnBill", "John Bailey");
        inputParam.put("phone", "+91-4351348905");
        inputParam.put("zipCode", "759634");
        inputParam.put("softDelete", "0");
        return inputParam;
    }

    public Map<String, String> getPayee2DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("accountNumber", "158930572850356");
        inputParam.put("addressLine1", "American Family Insurance");
        inputParam.put("addressLine2", "70 Bowman Street");
        inputParam.put("state", "Texas");
        inputParam.put("cityName", "Austin");
        inputParam.put("nickName", "Family Insurance");
        inputParam.put("companyName", "American Family Insurance");
        inputParam.put("email", "american@gmail.com");
        inputParam.put("firstName", "Family");
        inputParam.put("lastName", "Insurance");
        inputParam.put("name", "American Family Insurance");
        inputParam.put("eBillEnable", "0");
        inputParam.put("isAutoPayEnabled", "0");
        inputParam.put("nameOnBill", "John Bailey");
        inputParam.put("phone", "+91-1501273981");
        inputParam.put("zipCode", "459667");
        inputParam.put("softDelete", "0");
        return inputParam;
    }

    public Map<String, String> getPayee3DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("accountNumber", "6890396982929");
        inputParam.put("addressLine1", "CITI Business");
        inputParam.put("addressLine2", "15 Boltonfield St");
        inputParam.put("state", "Florida");
        inputParam.put("cityName", "Orlando");
        inputParam.put("nickName", "Citi Credit Card");
        inputParam.put("companyName", "CitiBank Credit Cards");
        inputParam.put("email", "citiBank@gmail.com");
        inputParam.put("firstName", "Citi");
        inputParam.put("lastName", "Card");
        inputParam.put("name", "CitiBank Credit Cards");
        inputParam.put("eBillEnable", "1");
        inputParam.put("isAutoPayEnabled", "0");
        inputParam.put("nameOnBill", "John Bailey");
        inputParam.put("phone", "+91-1273981272");
        inputParam.put("zipCode", "759634");
        inputParam.put("softDelete", "0");
        return inputParam;
    }

    public Map<String, String> getPayee4DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("accountNumber", "6890396982939");
        inputParam.put("addressLine1", "AT&T");
        inputParam.put("addressLine2", "12 Down St");
        inputParam.put("state", "Texas");
        inputParam.put("cityName", "Dallas");
        inputParam.put("nickName", "AT&T_Mobile Phone");
        inputParam.put("companyName", "AT&T Inc");
        inputParam.put("email", "cableBill@gmail.com");
        inputParam.put("firstName", "My Mobile");
        inputParam.put("lastName", "Bill");
        inputParam.put("name", "AT&T Mobile");
        inputParam.put("eBillEnable", "1");
        inputParam.put("isAutoPayEnabled", "0");
        inputParam.put("nameOnBill", "John Bailey");
        inputParam.put("phone", "+91-938899898");
        inputParam.put("zipCode", "42110");
        inputParam.put("softDelete", "0");
        return inputParam;
    }

    public Map<String, String> getPayee5DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("accountNumber", "11433468761745");
        inputParam.put("addressLine1", "514S");
        inputParam.put("addressLine2", "Magnolia St.");
        inputParam.put("state", "Florida");
        inputParam.put("cityName", "Orlando");
        inputParam.put("nickName", "City Water Works");
        inputParam.put("companyName", "Virginia Home Rentals");
        inputParam.put("email", "rent@gmail.com");
        inputParam.put("firstName", "Water");
        inputParam.put("lastName", "Works");
        inputParam.put("name", "Water Works");
        inputParam.put("eBillEnable", "1");
        inputParam.put("isAutoPayEnabled", "0");
        inputParam.put("nameOnBill", "John Bailey");
        inputParam.put("phone", "");
        inputParam.put("zipCode", "42110");
        inputParam.put("softDelete", "0");
        return inputParam;
    }

    public Map<String, String> getPayee6DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("accountNumber", "9611932872351");
        inputParam.put("addressLine1", "44 Shirley Ave");
        inputParam.put("addressLine2", "West Chicago");
        inputParam.put("state", "Texas");
        inputParam.put("cityName", "San Antonio");
        inputParam.put("nickName", "City Utilities Company");
        inputParam.put("companyName", "Beam Teleservices");
        inputParam.put("email", "wifi@gmail.com");
        inputParam.put("firstName", "City");
        inputParam.put("lastName", "Utilities");
        inputParam.put("name", "Beam Teleservices");
        inputParam.put("eBillEnable", "0");
        inputParam.put("isAutoPayEnabled", "0");
        inputParam.put("nameOnBill", "John Bailey");
        inputParam.put("phone", "+91-4523746832");
        inputParam.put("zipCode", "50098");
        inputParam.put("softDelete", "0");
        return inputParam;
    }

    public Map<String, String> getPerson1DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("email", "judy.blume@gmail.com");
        inputParam.put("secondaryEmail", "jblume@gmail.com");
        inputParam.put("secondoryPhoneNumber", "+91-4567483920");
        inputParam.put("primaryContactForSending", "+91-8790146356");
        inputParam.put("firstName", "Judy");
        inputParam.put("lastName", "Blume");
        inputParam.put("phone", "+91-8790146356");
        inputParam.put("name", "Judy Blume");
        inputParam.put("nickName", "Judy");
        return inputParam;
    }

    public Map<String, String> getPerson2DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("primaryContactForSending", "6565178276");
        inputParam.put("firstName", "Arthur");
        inputParam.put("lastName", "Miller");
        inputParam.put("phone", "+91-5638294057");
        inputParam.put("name", "Arthur Miller");
        inputParam.put("nickName", "Miller");
        return inputParam;
    }

    public Map<String, String> getPerson3DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("email", "francis@gmail.com");
        inputParam.put("primaryContactForSending", "fordfrancis@gmail.com");
        inputParam.put("firstName", "Francis");
        inputParam.put("lastName", "Ford");
        inputParam.put("name", "Francis Ford");
        inputParam.put("nickName", "Francis F");
        return inputParam;
    }

    public Map<String, String> getPerson4DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("email", "peter@gmail.com");
        inputParam.put("secondaryEmail", "JacksonP@yahoo.com");
        inputParam.put("primaryContactForSending", "peter@gmail.com");
        inputParam.put("firstName", "Peter");
        inputParam.put("lastName", "Jackson");
        inputParam.put("name", "Peter Jackson");
        inputParam.put("nickName", "Peter J");
        return inputParam;
    }

    public Map<String, String> getExtAccount1DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("nickName", "Alex Sion");
        inputParam.put("beneficiaryName", "Alex Sion");
        inputParam.put("accountType", "Savings");
        inputParam.put("countryName", "USA");
        inputParam.put("firstName", "Alex");
        inputParam.put("lastName", "Sion");
        inputParam.put("bankName", "National Bank");
        inputParam.put("notes", " ");
        inputParam.put("routingNumber", "765678987");
        inputParam.put("swiftCode", "0");
        inputParam.put("isInternationalAccount", "0");
        inputParam.put("isSameBankAccount", "0");
        inputParam.put("isVerified", "1");
        inputParam.put("createdOn", "2017-10-10");
        inputParam.put("accountNumber", "98287890273");
        return inputParam;
    }

    public Map<String, String> getExtAccount2DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("nickName", "Tom Purnell");
        inputParam.put("beneficiaryName", "Tom Purnell");
        inputParam.put("accountType", "Checking");
        inputParam.put("countryName", "USA");
        inputParam.put("firstName", "Tom");
        inputParam.put("lastName", "Purnell");
        inputParam.put("bankName", "CitiBank");
        inputParam.put("notes", " ");
        inputParam.put("routingNumber", "765678987");
        inputParam.put("swiftCode", "0");
        inputParam.put("isInternationalAccount", "0");
        inputParam.put("isSameBankAccount", "0");
        inputParam.put("isVerified", "1");
        inputParam.put("createdOn", "2016-11-07");
        inputParam.put("accountNumber", "5528789466");
        return inputParam;
    }

    public Map<String, String> getExtAccount3DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("nickName", "My BoA Account");
        inputParam.put("beneficiaryName", "John Bailey");
        inputParam.put("accountType", "Checking");
        inputParam.put("countryName", "USA");
        inputParam.put("firstName", "John");
        inputParam.put("lastName", "Bailey");
        inputParam.put("bankName", "Bank of America");
        inputParam.put("notes", " ");
        inputParam.put("routingNumber", "765678922");
        inputParam.put("swiftCode", "0");
        inputParam.put("isInternationalAccount", "0");
        inputParam.put("isSameBankAccount", "0");
        inputParam.put("isVerified", "0");
        inputParam.put("createdOn", "2018-01-07");
        inputParam.put("accountNumber", "29475930375");
        return inputParam;
    }

    public Map<String, String> getExtAccount4DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("nickName", "Dan Markus");
        inputParam.put("beneficiaryName", "Dan Markus");
        inputParam.put("accountType", "Checking");
        inputParam.put("countryName", "USA");
        inputParam.put("firstName", "Dan");
        inputParam.put("lastName", "Markus");
        inputParam.put("bankName", "Infinity");
        inputParam.put("notes", " ");
        inputParam.put("routingNumber", "0");
        inputParam.put("swiftCode", "0");
        inputParam.put("isInternationalAccount", "0");
        inputParam.put("isSameBankAccount", "1");
        inputParam.put("isVerified", "1");
        inputParam.put("createdOn", "2015-10-13");
        inputParam.put("accountNumber", "47834984272");
        return inputParam;
    }

    public Map<String, String> getExtAccount5DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("nickName", "Henry");
        inputParam.put("beneficiaryName", "Henry James");
        inputParam.put("accountType", "Savings");
        inputParam.put("countryName", "India");
        inputParam.put("firstName", "Henry");
        inputParam.put("lastName", "James");
        inputParam.put("bankName", "ICICI bank");
        inputParam.put("notes", " ");
        inputParam.put("routingNumber", "0");
        inputParam.put("swiftCode", "461894792");
        inputParam.put("isInternationalAccount", "1");
        inputParam.put("isSameBankAccount", "0");
        inputParam.put("isVerified", "1");
        inputParam.put("createdOn", "2017-12-28");
        inputParam.put("accountNumber", "64314698173");
        return inputParam;
    }

    public Map<String, String> getCard1DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("action", "Activate");
        inputParam.put("card_Status", "Active");
        inputParam.put("availableCredit", "87161.00");
        inputParam.put("serviceProvider", "Visa");
        inputParam.put("billingAddress", "2076, Burnett Ln, Austin, Texas, 78790");
        inputParam.put("cardProductName", "Gold Debit Card");
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("expirationDate", "2019-12-12");
        inputParam.put("pinNumber", "1578");
        inputParam.put("withdrawlLimit", "214");
        inputParam.put("reason", "");
        inputParam.put("cardHolderName", "John Bailey");
        inputParam.put("secondaryCardHolder", "John Dane");
        inputParam.put("isInternational", "1");
        return inputParam;
    }

    public Map<String, String> getCard2DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("action", "Activate");
        inputParam.put("card_Status", "Active");
        inputParam.put("availableCredit", "12380");
        inputParam.put("serviceProvider", "Master Card");
        inputParam.put("billingAddress", "2076, Burnett Ln, Austin, Texas, 78791");
        inputParam.put("cardProductName", "Eazee Food Card");
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("expirationDate", "2018-12-11");
        inputParam.put("pinNumber", "1579");
        inputParam.put("withdrawlLimit", "524");
        inputParam.put("reason", "");
        inputParam.put("cardHolderName", "John Bailey");
        inputParam.put("secondaryCardHolder", "John Doe");
        return inputParam;
    }

    public Map<String, String> getCard3DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("action", "Activate");
        inputParam.put("card_Status", "Active");
        inputParam.put("availableCredit", "8513");
        inputParam.put("serviceProvider", "Master Card");
        inputParam.put("billingAddress", "2076, Burnett Ln, Austin, Texas, 78792");
        inputParam.put("cardProductName", "My Platinum Credit Card");
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("expirationDate", "2020-09-09");
        inputParam.put("pinNumber", "1580");
        inputParam.put("creditLimit", "20000");
        inputParam.put("reason", "");
        inputParam.put("cardHolderName", "John Bailey");
        inputParam.put("secondaryCardHolder", "John Doe");
        return inputParam;
    }

    public Map<String, String> geUserAlertDemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("bankingIDChange", "0");
        inputParam.put("communicationChange", "1");
        inputParam.put("dealsExpiring", "1");
        inputParam.put("newDealsAvailable", "1");
        inputParam.put("newPayeeAdded", "0");
        inputParam.put("passwordChange", "1");
        inputParam.put("passwordExpired", "1");
        inputParam.put("payeeDetailsUpdated", "1");
        return inputParam;
    }

    public Map<String, String> geAccountAlert1DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("creditLimit", "0");
        inputParam.put("debitLimit", "1");
        inputParam.put("isEnabled", "1");
        inputParam.put("minimumBalance", "1");
        inputParam.put("successfulTransfer", "0");
        inputParam.put("checkClearance", "1");
        inputParam.put("balanceUpdate_PeriodId", "5");
        inputParam.put("PayementDueReminder_PeriodId", "4");
        inputParam.put("depositMaturityReminder_PeriodId", "4");
        return inputParam;
    }

    public Map<String, String> geAccountAlert2DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("creditLimit", "0");
        inputParam.put("debitLimit", "1");
        inputParam.put("isEnabled", "1");
        inputParam.put("minimumBalance", "1");
        inputParam.put("successfulTransfer", "1");
        inputParam.put("checkClearance", "1");
        inputParam.put("balanceUpdate_PeriodId", "5");
        inputParam.put("PayementDueReminder_PeriodId", "4");
        inputParam.put("depositMaturityReminder_PeriodId", "4");
        return inputParam;
    }

    public Map<String, String> geAccountAlert3DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("creditLimit", "0");
        inputParam.put("debitLimit", "1");
        inputParam.put("isEnabled", "1");
        inputParam.put("minimumBalance", "1");
        inputParam.put("successfulTransfer", "0");
        inputParam.put("checkClearance", "1");
        inputParam.put("balanceUpdate_PeriodId", "5");
        inputParam.put("PayementDueReminder_PeriodId", "4");
        inputParam.put("depositMaturityReminder_PeriodId", "4");
        return inputParam;
    }

    public Map<String, String> geAccountAlert4DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("creditLimit", "0");
        inputParam.put("debitLimit", "1");
        inputParam.put("isEnabled", "1");
        inputParam.put("minimumBalance", "1");
        inputParam.put("successfulTransfer", "0");
        inputParam.put("checkClearance", "1");
        inputParam.put("balanceUpdate_PeriodId", "5");
        inputParam.put("PayementDueReminder_PeriodId", "4");
        inputParam.put("depositMaturityReminder_PeriodId", "4");
        return inputParam;
    }

    public Map<String, String> getUserBill1DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("billDueDate", "2019-05-18");
        inputParam.put("paidDate", "2017-12-20");
        inputParam.put("description", "AT&T_Mobile Phone");
        inputParam.put("dueAmount", "1200.00");
        inputParam.put("paidAmount", "1080.00");
        inputParam.put("balanceAmount", "120.00");
        inputParam.put("minimumDue", "10.00");
        inputParam.put("ebillURL", "https://retailbanking1.konycloud.com/dbimages/billpay_ebill.png");
        inputParam.put("statusDesc", "TRANSACTION_STATUS_SUCCESSFUL");
        inputParam.put("billGeneratedDate", "2016-12-21");
        return inputParam;
    }

    public Map<String, String> getUserBill2DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("billDueDate", "2019-09-21");
        inputParam.put("paidDate", "2017-09-02");
        inputParam.put("description", "Electricity - ABC");
        inputParam.put("dueAmount", "1570.00");
        inputParam.put("paidAmount", "1000.00");
        inputParam.put("balanceAmount", "570.00");
        inputParam.put("minimumDue", "10.00");
        inputParam.put("ebillURL", "https://retailbanking1.konycloud.com/dbimages/billpay_ebill.png");
        inputParam.put("statusDesc", "TRANSACTION_STATUS_SUCCESSFUL");
        inputParam.put("billGeneratedDate", "2016-09-18");
        return inputParam;
    }

    public Map<String, String> getRecentChkDeposit1DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("amount", "500");
        inputParam.put("checkNumber", "19805");
        inputParam.put("description", "Check 19805");
        inputParam.put("notes", "From Mom");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("toAccountBalance", "5941.78");
        inputParam.put("fromAccountBalance", "5941.78");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("frequencyType", "Once");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    public Map<String, String> getRecentChkDeposit2DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("amount", "1450");
        inputParam.put("checkNumber", "19802");
        inputParam.put("description", "Check 19802");
        inputParam.put("notes", "Received from Jane for rent");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("toAccountBalance", "5940");
        inputParam.put("fromAccountBalance", "5940");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("frequencyType", "Once");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    public Map<String, String> getRecentCardLessWithdrawal1DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("amount", "500");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Cardless withdrawal to Self");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("toAccountBalance", "37000");
        inputParam.put("fromAccountBalance", "5792");
        inputParam.put("cashlessMode", "Self");
        inputParam.put("cashlessOTP", "789429");
        inputParam.put("cashlessOTPValidDate", "1");
        inputParam.put("cashWithdrawalTransactionStatus", "pending");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("frequencyType", "Once");
        inputParam.put("frequencyStartDate", "2018-08-31");
        inputParam.put("frequencyEndDate", "2018-08-31");
        inputParam.put("isScheduled", "0");
        inputParam.put("fromAccountNumber", "Core Checking");

        return inputParam;
    }

    public Map<String, String> getRecentCardLessWithdrawal2DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("amount", "750");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Cardless withdrawal to Henry");
        inputParam.put("notes", "To plumber");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("toAccountBalance", "62782");
        inputParam.put("fromAccountBalance", "5020");
        inputParam.put("cashlessMode", "others");
        inputParam.put("cashlessOTP", "572839");
        inputParam.put("cashlessOTPValidDate", "1");
        inputParam.put("cashWithdrawalTransactionStatus", "pending");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("frequencyType", "Once");
        inputParam.put("frequencyStartDate", "2018-08-29");
        inputParam.put("frequencyEndDate", "2018-08-29");
        inputParam.put("isScheduled", "0");
        inputParam.put("fromAccountNumber", "Core Checking");
        return inputParam;
    }

    public Map<String, String> getRecentCardLessWithdrawal3DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("amount", "200");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Cardless withdrawal to Jane Doe");
        inputParam.put("notes", "Misc expenses");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("toAccountBalance", "25649");
        inputParam.put("fromAccountBalance", "9834");
        inputParam.put("cashlessMode", "others");
        inputParam.put("cashlessOTP", "324692");
        inputParam.put("cashlessOTPValidDate", "1");
        inputParam.put("cashWithdrawalTransactionStatus", "pending");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("frequencyType", "Once");
        inputParam.put("frequencyStartDate", "2018-08-13");
        inputParam.put("frequencyEndDate", "2018-08-13");
        inputParam.put("isScheduled", "0");
        inputParam.put("fromAccountNumber", "Core Checking");
        return inputParam;
    }

    public Map<String, String> getInternalTransfer_MBSAV1DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternalTransfer");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "Rewards Savings");
        inputParam.put("amount", "1600");
        inputParam.put("description", "Online transfer from Core Checking Masked Account Number");
        inputParam.put("notes", "Saving for a car");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6932.39");
        inputParam.put("createdDate", "-8");
        inputParam.put("transactionDate", "-8");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");

        return inputParam;
    }

    public Map<String, String> getCheckDeposit_MBSAV2DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Deposit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Rewards Savings");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "53");
        inputParam.put("checkNumber", "9812");
        inputParam.put("description", "Check 9812");
        inputParam.put("notes", "Insurance refund");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6885.39");
        inputParam.put("createdDate", "-6");
        inputParam.put("transactionDate", "-6");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getExternalTransfer_MBSAV3DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "ExternalTransfer");
        inputParam.put("fromAccountNumber", "Rewards Savings");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "Alex Sion");
        inputParam.put("amount", "645");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online external fund transfer to Alex Sion");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7332.39");
        inputParam.put("createdDate", "-9");
        inputParam.put("transactionDate", "-9");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        inputParam.put("beneficiaryName", "Alex Sion");
        return inputParam;
    }

    public Map<String, String> getRemoteCheckDeposit_MBSAV4DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Deposit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Rewards Savings");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "500");
        inputParam.put("checkNumber", "19805");
        inputParam.put("description", "Check 19805");
        inputParam.put("notes", "From Mom");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7977.39");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternalTransfer_MBSAV5DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternalTransfer");
        inputParam.put("fromAccountNumber", "Rewards Savings");
        inputParam.put("toAccountNumber", "Core Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "500");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online transfer to Core Checking Masked Account Number");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "5477.39");
        inputParam.put("createdDate", "-11");
        inputParam.put("transactionDate", "-11");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInterest_MBSAV6DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Interest");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Rewards Savings");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "1.78");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Interest earned");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7477.39");
        inputParam.put("createdDate", "-12");
        inputParam.put("transactionDate", "-12");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getRemoteCheckDeposit_MBSAV7DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Deposit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Rewards Savings");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "1450");
        inputParam.put("checkNumber", "19802");
        inputParam.put("description", "Check 19802");
        inputParam.put("notes", "Received from Jane for rent");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7475.61");
        inputParam.put("createdDate", "-13");
        inputParam.put("transactionDate", "-13");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_MBSAV8DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Rewards Savings");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "35.61");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Card reward points");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6025.61");
        inputParam.put("createdDate", "-14");
        inputParam.put("transactionDate", "-14");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getFee_MBSAV9DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Fee");
        inputParam.put("fromAccountNumber", "Rewards Savings");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "10");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Charge for low account balance");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "5990");
        inputParam.put("createdDate", "-15");
        inputParam.put("transactionDate", "-15");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternalTransfer_MBSAV10DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternalTransfer");
        inputParam.put("fromAccountNumber", "Rewards Savings");
        inputParam.put("toAccountNumber", "Core Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "1500");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online transfer  to Core Checking Masked Account Number");
        inputParam.put("notes", "For credit card payment");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4500");
        inputParam.put("createdDate", "-16");
        inputParam.put("transactionDate", "-16");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getExternalTransfer_MBSAV11DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "ExternalTransfer");
        inputParam.put("fromAccountNumber", "Rewards Savings");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "500");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Wire out TRN:2017082900259510 SERVICE REF:007047 BNF: ABA Inc");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6000");
        inputParam.put("createdDate", "-17");
        inputParam.put("transactionDate", "-17");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        inputParam.put("beneficiaryName", "John Bailey");
        return inputParam;
    }

    public Map<String, String> getLoan_MBSAV12DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Loan");
        inputParam.put("fromAccountNumber", "Rewards Savings");
        inputParam.put("toAccountNumber", "Turbo Auto Loan");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "2391.65");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "J P Morgan Chase loan payment ACH ID:XXXXX18921");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "");
        inputParam.put("createdDate", "-18");
        inputParam.put("transactionDate", "");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "1");
        inputParam.put("scheduledDate", "14");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCardlessWithdrawl_MBSAVDemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Cardless");
        inputParam.put("fromAccountNumber", "Rewards Savings");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "500");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Cardless withdrawal to Self");
        inputParam.put("notes", "Insurance refund");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6832.39");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        inputParam.put("cashlessOTP", "789429");
        inputParam.put("cashlessMode", "Self");
        inputParam.put("cashWithdrawalTransactionStatus", "Pending");
        return inputParam;
    }

    public Map<String, String> getCredit_MBChecking1DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Core Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "2416.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Acme Inc. Payroll ID: 1921");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "5937.69");
        inputParam.put("createdDate", "-3");
        inputParam.put("transactionDate", "-3");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternalTransfer_MBChecking2DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternalTransfer");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "Freedom Credit Card");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "1500");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online transfer Freedom Credit Card Masked Card Number");
        inputParam.put("notes", "Jan credit card bill");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "920.78");
        inputParam.put("createdDate", "-4");
        inputParam.put("transactionDate", "-4");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternalTransfer_MBChecking3DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternalTransfer");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "Rewards Savings");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "1600");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online transfer Rewards Savings Masked Account Number");
        inputParam.put("notes", "Saving for car");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2570.78");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternalTransfer_MBChecking4DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternalTransfer");
        inputParam.put("fromAccountNumber", "Rewards Savings");
        inputParam.put("toAccountNumber", "Core Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "500");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online transfer Rewards Savings Masked Account Number");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4170.78");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCheckDeposit_MBChecking5DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Deposit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Core Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "209.34");
        inputParam.put("checkNumber", "32174");
        inputParam.put("description", "Check 32174");
        inputParam.put("notes", "AT & T refund");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3670.78");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getP2P_MBChecking6DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "P2P");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "30");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Zelle transfer Conf# 2fqjh376b; Arthur");
        inputParam.put("notes", "dinner at Zorros");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3461.44");
        inputParam.put("createdDate", "-8");
        inputParam.put("transactionDate", "-8");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        inputParam.put("Person_Id", "person2");
        return inputParam;
    }

    public Map<String, String> getInterest_MBChecking7DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Interest");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Core Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "0.25");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Interest credit Jan");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3491.44");
        inputParam.put("createdDate", "-8");
        inputParam.put("transactionDate", "-8");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getWithdrawal_MBChecking8DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Withdrawal");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "200");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "ATM-KonyBank\\Austin\\70123");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3491.20");
        inputParam.put("createdDate", "-8");
        inputParam.put("transactionDate", "-8");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getP2P_MBChecking9DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "P2P");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "1000");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Zelle transfer Conf# 9skjd827q; Miller");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3691.20");
        inputParam.put("createdDate", "-8");
        inputParam.put("transactionDate", "-8");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        inputParam.put("Person_Id", "person2");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking10DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4691.20");
        inputParam.put("createdDate", "-8");
        inputParam.put("transactionDate", "-8");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking11DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Starbucks Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4811.31");
        inputParam.put("createdDate", "-9");
        inputParam.put("transactionDate", "-9");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking12DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Best Buy 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4829.50");
        inputParam.put("createdDate", "-9");
        inputParam.put("transactionDate", "-9");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking13DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS HEB Round Rock #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "5024.53");
        inputParam.put("createdDate", "-9");
        inputParam.put("transactionDate", "-9");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking14DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "5100.44");
        inputParam.put("createdDate", "-9");
        inputParam.put("transactionDate", "-9");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking15DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "5137.63");
        inputParam.put("createdDate", "-9");
        inputParam.put("transactionDate", "-9");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking16DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "5175.67");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_MBChecking17DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Core Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "5209.88");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_MBChecking18DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Jan Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "5128.97");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        inputParam.put("Payee_id", "payee1");
        inputParam.put("Bill_id", "bill2");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking19DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "5256.95");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking20DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "5325.14");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking21DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Starbucks Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "5445.26");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking22DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Best Buy 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "5463.45");
        inputParam.put("createdDate", "-11");
        inputParam.put("transactionDate", "-11");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking23DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "5658.48");
        inputParam.put("createdDate", "-11");
        inputParam.put("transactionDate", "-11");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking24DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "5734.39");
        inputParam.put("createdDate", "-11");
        inputParam.put("transactionDate", "-11");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking25DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "5802.58");
        inputParam.put("createdDate", "-11");
        inputParam.put("transactionDate", "-11");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking26DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Starbucks Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "5922.71");
        inputParam.put("createdDate", "-11");
        inputParam.put("transactionDate", "-11");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking27DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Best Buy 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "5940.89");
        inputParam.put("createdDate", "-12");
        inputParam.put("transactionDate", "-12");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking28DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6135.92");
        inputParam.put("createdDate", "-12");
        inputParam.put("transactionDate", "-12");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking29DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6211.83");
        inputParam.put("createdDate", "-12");
        inputParam.put("transactionDate", "-12");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking30DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6249.02");
        inputParam.put("createdDate", "-12");
        inputParam.put("transactionDate", "-12");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking31DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6287.06");
        inputParam.put("createdDate", "-12");
        inputParam.put("transactionDate", "-12");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_MBChecking32DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Core Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6321.27");
        inputParam.put("createdDate", "-13");
        inputParam.put("transactionDate", "-13");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_MBChecking33DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Jan Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6240.36");
        inputParam.put("createdDate", "-13");
        inputParam.put("transactionDate", "-13");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        inputParam.put("Payee_id", "payee1");
        inputParam.put("Bill_id", "bill2");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking34DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6368.34");
        inputParam.put("createdDate", "-13");
        inputParam.put("transactionDate", "-13");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking35DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6436.53");
        inputParam.put("createdDate", "-13");
        inputParam.put("transactionDate", "-13");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking36DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Starbucks Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6556.65");
        inputParam.put("createdDate", "-13");
        inputParam.put("transactionDate", "-13");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking37DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Best Buy 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6574.84");
        inputParam.put("createdDate", "-14");
        inputParam.put("transactionDate", "-14");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking38DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6769.87");
        inputParam.put("createdDate", "-14");
        inputParam.put("transactionDate", "-14");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking39DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6845.78");
        inputParam.put("createdDate", "-14");
        inputParam.put("transactionDate", "-14");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking40DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6882.97");
        inputParam.put("createdDate", "-14");
        inputParam.put("transactionDate", "-14");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking41DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6921.01");
        inputParam.put("createdDate", "-14");
        inputParam.put("transactionDate", "-14");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_MBChecking42DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Core Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6955.22");
        inputParam.put("createdDate", "-15");
        inputParam.put("transactionDate", "-15");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_MBChecking43DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Jan Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6874.31");
        inputParam.put("createdDate", "-15");
        inputParam.put("transactionDate", "-15");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        inputParam.put("Payee_id", "payee1");
        inputParam.put("Bill_id", "bill2");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking44DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7002.29");
        inputParam.put("createdDate", "-15");
        inputParam.put("transactionDate", "-15");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking45DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7070.48");
        inputParam.put("createdDate", "-15");
        inputParam.put("transactionDate", "-15");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking46DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Starbucks Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7190.61");
        inputParam.put("createdDate", "-15");
        inputParam.put("transactionDate", "-15");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking47DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Best Buy 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7208.79");
        inputParam.put("createdDate", "-16");
        inputParam.put("transactionDate", "-16");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking48DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7403.82");
        inputParam.put("createdDate", "-16");
        inputParam.put("transactionDate", "-16");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking49DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7479.73");
        inputParam.put("createdDate", "-16");
        inputParam.put("transactionDate", "-16");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking50DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7516.92");
        inputParam.put("createdDate", "-16");
        inputParam.put("transactionDate", "-16");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking51DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7554.96");
        inputParam.put("createdDate", "-16");
        inputParam.put("transactionDate", "-16");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_MBChecking52DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Core Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7589.17");
        inputParam.put("createdDate", "-16");
        inputParam.put("transactionDate", "-16");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_MBChecking53DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Jan Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7508.26");
        inputParam.put("createdDate", "-17");
        inputParam.put("transactionDate", "-17");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        inputParam.put("Payee_id", "payee1");
        inputParam.put("Bill_id", "bill2");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking54DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7636.24");
        inputParam.put("createdDate", "-17");
        inputParam.put("transactionDate", "-17");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking55DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7704.43");
        inputParam.put("createdDate", "-17");
        inputParam.put("transactionDate", "-17");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking56DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Starbucks Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7824.55");
        inputParam.put("createdDate", "-17");
        inputParam.put("transactionDate", "-17");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking57DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Best Buy 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7842.74");
        inputParam.put("createdDate", "-17");
        inputParam.put("transactionDate", "-17");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking58DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8037.77");
        inputParam.put("createdDate", "-17");
        inputParam.put("transactionDate", "-17");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking59DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8113.68");
        inputParam.put("createdDate", "-18");
        inputParam.put("transactionDate", "-18");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking60DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8150.87");
        inputParam.put("createdDate", "-18");
        inputParam.put("transactionDate", "-18");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking61DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8188.91");
        inputParam.put("createdDate", "-18");
        inputParam.put("transactionDate", "-18");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_MBChecking62DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Core Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8223.12");
        inputParam.put("createdDate", "-18");
        inputParam.put("transactionDate", "-18");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_MBChecking63DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Jan Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8142.21");
        inputParam.put("createdDate", "-18");
        inputParam.put("transactionDate", "-18");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        inputParam.put("Payee_id", "payee1");
        inputParam.put("Bill_id", "bill2");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking64DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8270.19");
        inputParam.put("createdDate", "-18");
        inputParam.put("transactionDate", "-18");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking65DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8338.38");
        inputParam.put("createdDate", "-19");
        inputParam.put("transactionDate", "-19");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_MBChecking66DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "2416.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Acme Inc. Payroll ID: 1921");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8458.51");
        inputParam.put("createdDate", "-19");
        inputParam.put("transactionDate", "-19");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking67DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Starbucks Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6041.59");
        inputParam.put("createdDate", "-19");
        inputParam.put("transactionDate", "-19");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking68DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Best Buy 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6059.78");
        inputParam.put("createdDate", "-19");
        inputParam.put("transactionDate", "-19");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking69DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6254.81");
        inputParam.put("createdDate", "-19");
        inputParam.put("transactionDate", "-19");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_MBChecking70DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Core Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6330.72");
        inputParam.put("createdDate", "-19");
        inputParam.put("transactionDate", "-19");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_MBChecking71DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Jan Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6249.81");
        inputParam.put("createdDate", "-20");
        inputParam.put("transactionDate", "-20");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        inputParam.put("Payee_id", "payee1");
        inputParam.put("Bill_id", "bill2");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking72DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6377.79");
        inputParam.put("createdDate", "-20");
        inputParam.put("transactionDate", "-20");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking73DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6445.98");
        inputParam.put("createdDate", "-20");
        inputParam.put("transactionDate", "-20");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking74DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Starbucks Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6566.10");
        inputParam.put("createdDate", "-20");
        inputParam.put("transactionDate", "-20");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking75DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Best Buy 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6584.29");
        inputParam.put("createdDate", "-20");
        inputParam.put("transactionDate", "-20");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking76DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6779.32");
        inputParam.put("createdDate", "-21");
        inputParam.put("transactionDate", "-21");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking77DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6855.23");
        inputParam.put("createdDate", "-21");
        inputParam.put("transactionDate", "-21");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking78DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6923.42");
        inputParam.put("createdDate", "-21");
        inputParam.put("transactionDate", "-21");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking79DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Starbucks Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7043.54");
        inputParam.put("createdDate", "-21");
        inputParam.put("transactionDate", "-21");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking80DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Best Buy 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7061.73");
        inputParam.put("createdDate", "-21");
        inputParam.put("transactionDate", "-21");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking81DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7256.76");
        inputParam.put("createdDate", "-21");
        inputParam.put("transactionDate", "-21");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking82DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7332.67");
        inputParam.put("createdDate", "-22");
        inputParam.put("transactionDate", "-22");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking83DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7369.86");
        inputParam.put("createdDate", "-22");
        inputParam.put("transactionDate", "-22");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking84DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7407.91");
        inputParam.put("createdDate", "-22");
        inputParam.put("transactionDate", "-22");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_MBChecking85DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Core Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7442.11");
        inputParam.put("createdDate", "-22");
        inputParam.put("transactionDate", "-22");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_MBChecking86DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Jan Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7361.20");
        inputParam.put("createdDate", "-22");
        inputParam.put("transactionDate", "-22");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        inputParam.put("Payee_id", "payee1");
        inputParam.put("Bill_id", "bill2");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking87DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7489.18");
        inputParam.put("createdDate", "-22");
        inputParam.put("transactionDate", "-22");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking88DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7557.37");
        inputParam.put("createdDate", "-23");
        inputParam.put("transactionDate", "-23");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking89DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Starbucks Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7677.49");
        inputParam.put("createdDate", "-23");
        inputParam.put("transactionDate", "-23");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking90DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Best Buy 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7695.68");
        inputParam.put("createdDate", "-23");
        inputParam.put("transactionDate", "-23");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking91DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7890.71");
        inputParam.put("createdDate", "-23");
        inputParam.put("transactionDate", "-23");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking92DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7966.62");
        inputParam.put("createdDate", "-23");
        inputParam.put("transactionDate", "-23");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking93DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8003.81");
        inputParam.put("createdDate", "-23");
        inputParam.put("transactionDate", "-23");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking94DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8041.85");
        inputParam.put("createdDate", "-24");
        inputParam.put("transactionDate", "-24");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_MBChecking95DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Core Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8076.06");
        inputParam.put("createdDate", "-24");
        inputParam.put("transactionDate", "-24");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_MBChecking96DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Jan Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7995.15");
        inputParam.put("createdDate", "-24");
        inputParam.put("transactionDate", "-24");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        inputParam.put("Payee_id", "payee1");
        inputParam.put("Bill_id", "bill2");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking97DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8123.13");
        inputParam.put("createdDate", "-24");
        inputParam.put("transactionDate", "-24");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking98DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8191.32");
        inputParam.put("createdDate", "-24");
        inputParam.put("transactionDate", "-24");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking99DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Starbucks Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8311.44");
        inputParam.put("createdDate", "-24");
        inputParam.put("transactionDate", "-24");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking100DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Best Buy 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8329.63");
        inputParam.put("createdDate", "-25");
        inputParam.put("transactionDate", "-25");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking101DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8524.66");
        inputParam.put("createdDate", "-25");
        inputParam.put("transactionDate", "-25");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking102DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8600.57");
        inputParam.put("createdDate", "-25");
        inputParam.put("transactionDate", "-25");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking103DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8637.76");
        inputParam.put("createdDate", "-25");
        inputParam.put("transactionDate", "-25");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking104DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8675.80");
        inputParam.put("createdDate", "-25");
        inputParam.put("transactionDate", "-25");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_MBChecking105DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Core Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8710.01");
        inputParam.put("createdDate", "-25");
        inputParam.put("transactionDate", "-25");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_MBChecking106DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Jan Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8629.10");
        inputParam.put("createdDate", "-26");
        inputParam.put("transactionDate", "-26");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        inputParam.put("Payee_id", "payee1");
        inputParam.put("Bill_id", "bill2");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking107DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8757.08");
        inputParam.put("createdDate", "-26");
        inputParam.put("transactionDate", "-26");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking108DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8825.27");
        inputParam.put("createdDate", "-26");
        inputParam.put("transactionDate", "-26");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking109DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Starbucks Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8945.39");
        inputParam.put("createdDate", "-26");
        inputParam.put("transactionDate", "-26");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking110DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Best Buy 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8963.58");
        inputParam.put("createdDate", "-26");
        inputParam.put("transactionDate", "-26");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking111DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "9158.61");
        inputParam.put("createdDate", "-26");
        inputParam.put("transactionDate", "-26");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking112DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "9234.52");
        inputParam.put("createdDate", "-27");
        inputParam.put("transactionDate", "-27");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking113DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "9271.71");
        inputParam.put("createdDate", "-27");
        inputParam.put("transactionDate", "-27");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking114DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "9309.75");
        inputParam.put("createdDate", "-27");
        inputParam.put("transactionDate", "-27");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_MBChecking115DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Core Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "9343.96");
        inputParam.put("createdDate", "-27");
        inputParam.put("transactionDate", "-27");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_MBChecking116DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Jan Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "9263.05");
        inputParam.put("createdDate", "-27");
        inputParam.put("transactionDate", "-27");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        inputParam.put("Payee_id", "payee1");
        inputParam.put("Bill_id", "bill2");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking117DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "9391.03");
        inputParam.put("createdDate", "-27");
        inputParam.put("transactionDate", "-27");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking118DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "9459.22");
        inputParam.put("createdDate", "-28");
        inputParam.put("transactionDate", "-28");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking119DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Starbucks Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "9579.34");
        inputParam.put("createdDate", "-28");
        inputParam.put("transactionDate", "-28");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_MBChecking120DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "2416.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Acme Inc. Payroll ID: 1921");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "9597.53");
        inputParam.put("createdDate", "-28");
        inputParam.put("transactionDate", "-28");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking121DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7180.62");
        inputParam.put("createdDate", "-28");
        inputParam.put("transactionDate", "-28");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking122DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7217.81");
        inputParam.put("createdDate", "-28");
        inputParam.put("transactionDate", "-28");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking123DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7255.85");
        inputParam.put("createdDate", "-28");
        inputParam.put("transactionDate", "-28");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_MBChecking124DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Core Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7290.06");
        inputParam.put("createdDate", "-28");
        inputParam.put("transactionDate", "-28");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_MBChecking125DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Jan Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7209.15");
        inputParam.put("createdDate", "-29");
        inputParam.put("transactionDate", "-29");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        inputParam.put("Payee_id", "payee1");
        inputParam.put("Bill_id", "bill2");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking126DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7337.13");
        inputParam.put("createdDate", "-29");
        inputParam.put("transactionDate", "-29");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking127DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7405.32");
        inputParam.put("createdDate", "-29");
        inputParam.put("transactionDate", "-29");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking128DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Starbucks Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7525.44");
        inputParam.put("createdDate", "-29");
        inputParam.put("transactionDate", "-29");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking129DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Best Buy 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7543.63");
        inputParam.put("createdDate", "-29");
        inputParam.put("transactionDate", "-29");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking130DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7738.66");
        inputParam.put("createdDate", "-29");
        inputParam.put("transactionDate", "-29");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking131DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7814.57");
        inputParam.put("createdDate", "-30");
        inputParam.put("transactionDate", "-30");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking132DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7851.76");
        inputParam.put("createdDate", "-30");
        inputParam.put("transactionDate", "-30");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking133DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7889.80");
        inputParam.put("createdDate", "-30");
        inputParam.put("transactionDate", "-30");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_MBChecking134DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Core Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7924.01");
        inputParam.put("createdDate", "-30");
        inputParam.put("transactionDate", "-30");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_MBChecking135DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Jan Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7843.10");
        inputParam.put("createdDate", "-30");
        inputParam.put("transactionDate", "-30");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        inputParam.put("Payee_id", "payee1");
        inputParam.put("Bill_id", "bill2");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking136DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7971.08");
        inputParam.put("createdDate", "-30");
        inputParam.put("transactionDate", "-30");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking137DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8039.27");
        inputParam.put("createdDate", "-31");
        inputParam.put("transactionDate", "-31");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking138DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Starbucks Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8159.39");
        inputParam.put("createdDate", "-31");
        inputParam.put("transactionDate", "-31");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking139DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Best Buy 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8177.58");
        inputParam.put("createdDate", "-31");
        inputParam.put("transactionDate", "-31");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking140DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8372.61");
        inputParam.put("createdDate", "-31");
        inputParam.put("transactionDate", "-31");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking141DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8448.52");
        inputParam.put("createdDate", "-31");
        inputParam.put("transactionDate", "-31");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking142DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8485.71");
        inputParam.put("createdDate", "-31");
        inputParam.put("transactionDate", "-31");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking143DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8523.75");
        inputParam.put("createdDate", "-32");
        inputParam.put("transactionDate", "-32");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_MBChecking144DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Core Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8557.96");
        inputParam.put("createdDate", "-32");
        inputParam.put("transactionDate", "-32");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_MBChecking145DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Jan Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8477.05");
        inputParam.put("createdDate", "-32");
        inputParam.put("transactionDate", "-32");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        inputParam.put("Payee_id", "payee1");
        inputParam.put("Bill_id", "bill2");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking146DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8605.03");
        inputParam.put("createdDate", "-32");
        inputParam.put("transactionDate", "-32");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking147DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8673.22");
        inputParam.put("createdDate", "-32");
        inputParam.put("transactionDate", "-32");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking148DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Starbucks Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8793.34");
        inputParam.put("createdDate", "-32");
        inputParam.put("transactionDate", "-32");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking149DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Best Buy 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8811.53");
        inputParam.put("createdDate", "-33");
        inputParam.put("transactionDate", "-33");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking150DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "9006.56");
        inputParam.put("createdDate", "-33");
        inputParam.put("transactionDate", "-33");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking151DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "9082.47");
        inputParam.put("createdDate", "-33");
        inputParam.put("transactionDate", "-33");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking152DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "9119.66");
        inputParam.put("createdDate", "-33");
        inputParam.put("transactionDate", "-33");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking153DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "9157.71");
        inputParam.put("createdDate", "-33");
        inputParam.put("transactionDate", "-33");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_MBChecking154DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Core Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "9191.91");
        inputParam.put("createdDate", "-33");
        inputParam.put("transactionDate", "-33");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_MBChecking155DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Jan Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "9110.99");
        inputParam.put("createdDate", "-34");
        inputParam.put("transactionDate", "-34");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        inputParam.put("Payee_id", "payee1");
        inputParam.put("Bill_id", "bill2");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking156DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "9238.98");
        inputParam.put("createdDate", "-34");
        inputParam.put("transactionDate", "-34");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking157DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "9307.17");
        inputParam.put("createdDate", "-34");
        inputParam.put("transactionDate", "-34");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking158DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Starbucks Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "9427.29");
        inputParam.put("createdDate", "-34");
        inputParam.put("transactionDate", "-34");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking159DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Best Buy 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "9445.48");
        inputParam.put("createdDate", "-34");
        inputParam.put("transactionDate", "-34");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking160DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "9640.51");
        inputParam.put("createdDate", "-34");
        inputParam.put("transactionDate", "-34");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking161DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "9716.42");
        inputParam.put("createdDate", "-34");
        inputParam.put("transactionDate", "-34");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_MBChecking162DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "2416.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Acme Inc. Payroll ID: 1921");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "9753.61");
        inputParam.put("createdDate", "-35");
        inputParam.put("transactionDate", "-35");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking163DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7336.71");
        inputParam.put("createdDate", "-35");
        inputParam.put("transactionDate", "-35");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking164DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7374.74");
        inputParam.put("createdDate", "-35");
        inputParam.put("transactionDate", "-35");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_MBChecking165DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Core Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7408.95");
        inputParam.put("createdDate", "-35");
        inputParam.put("transactionDate", "-35");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_MBChecking166DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Jan Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7328.04");
        inputParam.put("createdDate", "-35");
        inputParam.put("transactionDate", "-35");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        inputParam.put("Payee_id", "payee1");
        inputParam.put("Bill_id", "bill2");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking167DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7456.02");
        inputParam.put("createdDate", "-35");
        inputParam.put("transactionDate", "-35");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking168DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7524.21");
        inputParam.put("createdDate", "-36");
        inputParam.put("transactionDate", "-36");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking169DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Starbucks Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7644.33");
        inputParam.put("createdDate", "-36");
        inputParam.put("transactionDate", "-36");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking170DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Best Buy 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7662.52");
        inputParam.put("createdDate", "-36");
        inputParam.put("transactionDate", "-36");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking171DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7857.55");
        inputParam.put("createdDate", "-36");
        inputParam.put("transactionDate", "-36");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking172DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7933.46");
        inputParam.put("createdDate", "-36");
        inputParam.put("transactionDate", "-36");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking173DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7970.65");
        inputParam.put("createdDate", "-36");
        inputParam.put("transactionDate", "-36");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking174DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "Core Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8008.69");
        inputParam.put("createdDate", "-37");
        inputParam.put("transactionDate", "-37");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_MBChecking175DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8042.91");
        inputParam.put("createdDate", "-37");
        inputParam.put("transactionDate", "-37");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_MBChecking176DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Jan Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7961.99");
        inputParam.put("createdDate", "-37");
        inputParam.put("transactionDate", "-37");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        inputParam.put("Payee_id", "payee1");
        inputParam.put("Bill_id", "bill2");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking177DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8089.97");
        inputParam.put("createdDate", "-37");
        inputParam.put("transactionDate", "-37");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking178DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8158.16");
        inputParam.put("createdDate", "-37");
        inputParam.put("transactionDate", "-37");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking179DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Starbucks Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8278.28");
        inputParam.put("createdDate", "-37");
        inputParam.put("transactionDate", "-37");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking180DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Best Buy 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8296.47");
        inputParam.put("createdDate", "-38");
        inputParam.put("transactionDate", "-38");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking181DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8491.50");
        inputParam.put("createdDate", "-38");
        inputParam.put("transactionDate", "-38");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking182DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8567.41");
        inputParam.put("createdDate", "-38");
        inputParam.put("transactionDate", "-38");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking183DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Starbucks Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8687.53");
        inputParam.put("createdDate", "-38");
        inputParam.put("transactionDate", "-38");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking184DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Best Buy 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8705.72");
        inputParam.put("createdDate", "-38");
        inputParam.put("transactionDate", "-38");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking185DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8900.75");
        inputParam.put("createdDate", "-38");
        inputParam.put("transactionDate", "-38");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking186DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "8976.66");
        inputParam.put("createdDate", "-39");
        inputParam.put("transactionDate", "-39");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking187DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "9013.85");
        inputParam.put("createdDate", "-39");
        inputParam.put("transactionDate", "-39");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking188DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "9051.89");
        inputParam.put("createdDate", "-39");
        inputParam.put("transactionDate", "-39");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_MBChecking189DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Core Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "9086.12");
        inputParam.put("createdDate", "-39");
        inputParam.put("transactionDate", "-39");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_MBChecking190DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Jan Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "9005.19");
        inputParam.put("createdDate", "-39");
        inputParam.put("transactionDate", "-39");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        inputParam.put("Payee_id", "payee1");
        inputParam.put("Bill_id", "bill2");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking191DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "9133.17");
        inputParam.put("createdDate", "-39");
        inputParam.put("transactionDate", "-39");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking192DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "9201.36");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking193DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Starbucks Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "9321.48");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking194DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Best Buy 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "9339.67");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking195DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "9534.71");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking196DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "9610.61");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBChecking197DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "9647.81");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCardPayment_MBChecking198DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "CardPayment");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Core Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "3019.26");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment - Thank You");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "9685.84");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_MBChecking199DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "869.14");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Infinity credit card bill payment");
        inputParam.put("notes", "Citi Credit Card Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6666.58");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        inputParam.put("Payee_id", "payee3");
        inputParam.put("Bill_id", "bill2");
        return inputParam;
    }

    public Map<String, String> getCredit_MBChecking200DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Core Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "20");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Temporary credit adjustment Card XX0203 Claim 180111112749");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7535.72");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getExternalTransfer_MBChecking201DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "ExternalTransfer");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online External Fund Transfer to Tom Burnett");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7515.72");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_MBChecking202DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "2416.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Acme Inc. Payroll ID: 1921");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7710.72");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBChecking203DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "206.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Best Buy 5th Avenue");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "5293.81");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCheckDeposit_MBChecking204DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Deposit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "2200");
        inputParam.put("checkNumber", "19806");
        inputParam.put("description", "Check 19806");
        inputParam.put("notes", "From Dan");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "5500");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getExternalTransfer_MBChecking205DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "ExternalTransfer");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "500");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "WireTransfer to JennaHastings");
        inputParam.put("notes", "For Euro Trip");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3300");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_MBChecking206DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "250");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "CitiBank Credit Card Bill Pay");
        inputParam.put("notes", "Citi Credit Card Bill");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "1");
        inputParam.put("scheduledDate", "+14");
        inputParam.put("Payee_id", "payee1");
        inputParam.put("Bill_id", "bill2");
        return inputParam;
    }

    public Map<String, String> getBillPay_MBChecking207DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "40.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Checkcard 2378 Vonage *Price+Taxes 982-221-8387 Recurring");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("frequencyType", "Monthly");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "");
        inputParam.put("createdDate", "-140");
        inputParam.put("transactionDate", "-140");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "1");
        inputParam.put("scheduledDate", "7");
        inputParam.put("frequencyStartDate", "-140");
        inputParam.put("frequencyEndDate", "+140");
        inputParam.put("Payee_id", "payee3");
        inputParam.put("Bill_id", "bill2");
        return inputParam;
    }

    public Map<String, String> getInternalTransfer_MBChecking208DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "ExternalTransfer");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "John Bailey");
        inputParam.put("amount", "100");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online External Fund Transfer to JohnBailey");
        inputParam.put("notes", "Transfer to my BoA Account for school fee");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("frequencyType", "Monthly");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "1");
        inputParam.put("scheduledDate", "7");
        inputParam.put("frequencyStartDate", "+7");
        inputParam.put("frequencyEndDate", "+372");
        return inputParam;
    }

    public Map<String, String> getP2P_MBChecking209DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "P2P");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "35");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Zelle transfer Conf# 2fqjh376b; Francis");
        inputParam.put("notes", "Coffee at Star Bucks");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "1");
        inputParam.put("scheduledDate", "+21");
        inputParam.put("Person_Id", "person3");
        return inputParam;
    }

    public Map<String, String> getP2P_MBChecking210DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "P2P");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "1000");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Zelle transfer Conf# 9skjd827q; Miller");
        inputParam.put("notes", "To Arthur for his personal use");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "1");
        inputParam.put("scheduledDate", "+28");
        inputParam.put("Person_Id", "person2");
        return inputParam;
    }

    public Map<String, String> getCardLessWithDrawl_MBChecking1DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Cardless");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "50");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Cardless withdrawal to Henry");
        inputParam.put("notes", "To plumber");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3520.78");
        inputParam.put("createdDate", "-5");
        inputParam.put("transactionDate", "-5");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        inputParam.put("cashlessOTP", "572839");
        inputParam.put("cashlessMode", "Others");
        inputParam.put("cashWithdrawalTransactionStatus", "Pending");
        return inputParam;
    }

    public Map<String, String> getCardLessWithDrawl_MBChecking2DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Cardless");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "100");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Cardless withdrawal to Jane Doe");
        inputParam.put("notes", "Misc expenses");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3570.78");
        inputParam.put("createdDate", "-6");
        inputParam.put("transactionDate", "-6");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        inputParam.put("cashlessOTP", "324692");
        inputParam.put("cashlessMode", "Others");
        inputParam.put("cashWithdrawalTransactionStatus", "Pending");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC1DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "48.97");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Shell Oil 2798739008");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3119.61");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternalTransfer_MBCC2DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternalTransfer");
        inputParam.put("fromAccountNumber", "Rewards Savings");
        inputParam.put("toAccountNumber", "Freedom Credit Card");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "2500");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online payment from SAV 18");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "570.639999999989");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC3DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3070.64");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC4DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Freedom Credit Card");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3036.42999999999");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_MBCC5DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Jan Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3117.33999999999");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC6DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2989.35999999999");
        inputParam.put("createdDate", "-8");
        inputParam.put("transactionDate", "-8");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCardPayment_MBCC7DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "CardPayment");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Freedom Credit Card");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "5000");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment - Thank You");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2921.16999999999");
        inputParam.put("createdDate", "-8");
        inputParam.put("transactionDate", "-8");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC8DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Starbucks Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7921.16999999999");
        inputParam.put("createdDate", "-8");
        inputParam.put("transactionDate", "-8");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC9DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Best Buy 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7902.97999999999");
        inputParam.put("createdDate", "-8");
        inputParam.put("transactionDate", "-8");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC10DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS HEB Round Rock #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7707.94999999999");
        inputParam.put("createdDate", "-8");
        inputParam.put("transactionDate", "-8");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC11DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7632.03999999999");
        inputParam.put("createdDate", "-9");
        inputParam.put("transactionDate", "-9");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC12DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7594.84999999999");
        inputParam.put("createdDate", "-9");
        inputParam.put("transactionDate", "-9");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC13DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7556.80999999999");
        inputParam.put("createdDate", "-9");
        inputParam.put("transactionDate", "-9");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_MBCC14DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Freedom Credit Card");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7522.59999999999");
        inputParam.put("createdDate", "-9");
        inputParam.put("transactionDate", "-9");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_MBCC15DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Jan Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7603.50999999999");
        inputParam.put("createdDate", "-9");
        inputParam.put("transactionDate", "-9");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC16DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7475.52999999999");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC17DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7407.33999999999");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC18DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Starbucks Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7287.21999999999");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC19DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Best Buy 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7269.02999999999");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC20DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "7073.99999999999");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC21DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6998.08999999999");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC22DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6960.89999999999");
        inputParam.put("createdDate", "-11");
        inputParam.put("transactionDate", "-11");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC23DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6922.85999999999");
        inputParam.put("createdDate", "-11");
        inputParam.put("transactionDate", "-11");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_MBCC24DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Freedom Credit Card");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6888.64999999999");
        inputParam.put("createdDate", "-11");
        inputParam.put("transactionDate", "-11");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_MBCC25DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Jan Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6969.55999999999");
        inputParam.put("createdDate", "-11");
        inputParam.put("transactionDate", "-11");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC26DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6841.57999999999");
        inputParam.put("createdDate", "-11");
        inputParam.put("transactionDate", "-11");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC27DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6773.38999999999");
        inputParam.put("createdDate", "-12");
        inputParam.put("transactionDate", "-12");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC28DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Starbucks Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6653.26999999999");
        inputParam.put("createdDate", "-12");
        inputParam.put("transactionDate", "-12");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC29DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Best Buy 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6635.07999999999");
        inputParam.put("createdDate", "-12");
        inputParam.put("transactionDate", "-12");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC30DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6440.04999999999");
        inputParam.put("createdDate", "-12");
        inputParam.put("transactionDate", "-12");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC31DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6364.13999999999");
        inputParam.put("createdDate", "-12");
        inputParam.put("transactionDate", "-12");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC32DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6295.94999999999");
        inputParam.put("createdDate", "-13");
        inputParam.put("transactionDate", "-13");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC33DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Starbucks Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6175.82999999999");
        inputParam.put("createdDate", "-13");
        inputParam.put("transactionDate", "-13");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC34DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Best Buy 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "6157.63999999999");
        inputParam.put("createdDate", "-13");
        inputParam.put("transactionDate", "-13");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC35DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "5962.60999999999");
        inputParam.put("createdDate", "-13");
        inputParam.put("transactionDate", "-13");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC36DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "5886.69999999999");
        inputParam.put("createdDate", "-13");
        inputParam.put("transactionDate", "-13");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC37DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "5849.50999999999");
        inputParam.put("createdDate", "-14");
        inputParam.put("transactionDate", "-14");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC38DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "5811.46999999999");
        inputParam.put("createdDate", "-14");
        inputParam.put("transactionDate", "-14");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_MBCC39DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Freedom Credit Card");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "5777.25999999999");
        inputParam.put("createdDate", "-14");
        inputParam.put("transactionDate", "-14");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_MBCC40DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Jan Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "5858.16999999999");
        inputParam.put("createdDate", "-14");
        inputParam.put("transactionDate", "-14");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC41DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "5730.19");
        inputParam.put("createdDate", "-14");
        inputParam.put("transactionDate", "-14");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC42DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "5662");
        inputParam.put("createdDate", "-15");
        inputParam.put("transactionDate", "-15");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC43DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Starbucks Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "5541.88");
        inputParam.put("createdDate", "-15");
        inputParam.put("transactionDate", "-15");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC44DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Best Buy 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "5523.69");
        inputParam.put("createdDate", "-15");
        inputParam.put("transactionDate", "-15");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC45DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "5328.66");
        inputParam.put("createdDate", "-15");
        inputParam.put("transactionDate", "-15");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC46DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "5252.75");
        inputParam.put("createdDate", "-15");
        inputParam.put("transactionDate", "-15");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC47DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "5215.56");
        inputParam.put("createdDate", "-16");
        inputParam.put("transactionDate", "-16");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC48DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "5177.52");
        inputParam.put("createdDate", "-16");
        inputParam.put("transactionDate", "-16");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_MBCC49DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Freedom Credit Card");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "5143.31");
        inputParam.put("createdDate", "-16");
        inputParam.put("transactionDate", "-16");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_MBCC50DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Jan Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "5224.22");
        inputParam.put("createdDate", "-16");
        inputParam.put("transactionDate", "-16");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC51DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "5096.24");
        inputParam.put("createdDate", "-16");
        inputParam.put("transactionDate", "-16");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC52DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "5028.05");
        inputParam.put("createdDate", "-16");
        inputParam.put("transactionDate", "-16");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC53DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Starbucks Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4907.93");
        inputParam.put("createdDate", "-17");
        inputParam.put("transactionDate", "-17");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC54DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Best Buy 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4889.74");
        inputParam.put("createdDate", "-17");
        inputParam.put("transactionDate", "-17");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC55DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4694.71");
        inputParam.put("createdDate", "-17");
        inputParam.put("transactionDate", "-17");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC56DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4618.8");
        inputParam.put("createdDate", "-17");
        inputParam.put("transactionDate", "-17");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC57DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4581.61");
        inputParam.put("createdDate", "-17");
        inputParam.put("transactionDate", "-17");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC58DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4543.57");
        inputParam.put("createdDate", "-17");
        inputParam.put("transactionDate", "-17");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_MBCC59DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Freedom Credit Card");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4509.36");
        inputParam.put("createdDate", "-18");
        inputParam.put("transactionDate", "-18");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_MBCC60DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Jan Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4590.27");
        inputParam.put("createdDate", "-18");
        inputParam.put("transactionDate", "-18");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC61DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4462.29");
        inputParam.put("createdDate", "-18");
        inputParam.put("transactionDate", "-18");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC62DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4394.1");
        inputParam.put("createdDate", "-18");
        inputParam.put("transactionDate", "-18");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC63DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Starbucks Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4273.98");
        inputParam.put("createdDate", "-18");
        inputParam.put("transactionDate", "-18");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC64DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Best Buy 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4255.79");
        inputParam.put("createdDate", "-18");
        inputParam.put("transactionDate", "-18");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC65DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4060.76");
        inputParam.put("createdDate", "-19");
        inputParam.put("transactionDate", "-19");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC66DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3984.85");
        inputParam.put("createdDate", "-19");
        inputParam.put("transactionDate", "-19");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC67DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3947.66");
        inputParam.put("createdDate", "-19");
        inputParam.put("transactionDate", "-19");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC68DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3909.62");
        inputParam.put("createdDate", "-19");
        inputParam.put("transactionDate", "-19");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_MBCC69DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Freedom Credit Card");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3875.41");
        inputParam.put("createdDate", "-19");
        inputParam.put("transactionDate", "-19");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_MBCC70DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Jan Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3956.32");
        inputParam.put("createdDate", "-19");
        inputParam.put("transactionDate", "-19");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC71DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3828.34");
        inputParam.put("createdDate", "-20");
        inputParam.put("transactionDate", "-20");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC72DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3760.15");
        inputParam.put("createdDate", "-20");
        inputParam.put("transactionDate", "-20");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC73DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Starbucks Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3640.03");
        inputParam.put("createdDate", "-20");
        inputParam.put("transactionDate", "-20");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC74DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Best Buy 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3621.84");
        inputParam.put("createdDate", "-20");
        inputParam.put("transactionDate", "-20");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC75DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3426.81");
        inputParam.put("createdDate", "-20");
        inputParam.put("transactionDate", "-20");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_MBCC76DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Freedom Credit Card");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3350.9");
        inputParam.put("createdDate", "-21");
        inputParam.put("transactionDate", "-21");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_MBCC77DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Jan Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3431.81");
        inputParam.put("createdDate", "-21");
        inputParam.put("transactionDate", "-21");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC78DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3303.83");
        inputParam.put("createdDate", "-21");
        inputParam.put("transactionDate", "-21");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC79DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3235.64");
        inputParam.put("createdDate", "-21");
        inputParam.put("transactionDate", "-21");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC80DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Starbucks Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3115.52");
        inputParam.put("createdDate", "-21");
        inputParam.put("transactionDate", "-21");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC81DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Best Buy 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3097.33");
        inputParam.put("createdDate", "-21");
        inputParam.put("transactionDate", "-21");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC82DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2902.3");
        inputParam.put("createdDate", "-22");
        inputParam.put("transactionDate", "-22");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC83DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2826.39");
        inputParam.put("createdDate", "-22");
        inputParam.put("transactionDate", "-22");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC84DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2758.2");
        inputParam.put("createdDate", "-22");
        inputParam.put("transactionDate", "-22");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC85DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Starbucks Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2638.08");
        inputParam.put("createdDate", "-22");
        inputParam.put("transactionDate", "-22");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC86DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Best Buy 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2619.89");
        inputParam.put("createdDate", "-22");
        inputParam.put("transactionDate", "-22");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC87DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2424.86");
        inputParam.put("createdDate", "-22");
        inputParam.put("transactionDate", "-22");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC88DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2348.95");
        inputParam.put("createdDate", "-23");
        inputParam.put("transactionDate", "-23");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC89DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2311.76");
        inputParam.put("createdDate", "-23");
        inputParam.put("transactionDate", "-23");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC90DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2273.72");
        inputParam.put("createdDate", "-23");
        inputParam.put("transactionDate", "-23");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_MBCC91DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Freedom Credit Card");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2239.51");
        inputParam.put("createdDate", "-23");
        inputParam.put("transactionDate", "-23");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_MBCC92DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Jan Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2320.42");
        inputParam.put("createdDate", "-23");
        inputParam.put("transactionDate", "-23");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC93DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2192.44");
        inputParam.put("createdDate", "-23");
        inputParam.put("transactionDate", "-23");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC94DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2124.25");
        inputParam.put("createdDate", "-24");
        inputParam.put("transactionDate", "-24");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC95DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Starbucks Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2004.13");
        inputParam.put("createdDate", "-24");
        inputParam.put("transactionDate", "-24");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC96DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Best Buy 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1985.94");
        inputParam.put("createdDate", "-24");
        inputParam.put("transactionDate", "-24");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC97DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1790.91");
        inputParam.put("createdDate", "-24");
        inputParam.put("transactionDate", "-24");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC98DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1715");
        inputParam.put("createdDate", "-24");
        inputParam.put("transactionDate", "-24");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC99DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1677.81");
        inputParam.put("createdDate", "-24");
        inputParam.put("transactionDate", "-24");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC100DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1639.77");
        inputParam.put("createdDate", "-25");
        inputParam.put("transactionDate", "-25");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_MBCC101DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Freedom Credit Card");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1605.56");
        inputParam.put("createdDate", "-25");
        inputParam.put("transactionDate", "-25");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_MBCC102DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Jan Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1686.47");
        inputParam.put("createdDate", "-25");
        inputParam.put("transactionDate", "-25");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC103DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1558.49");
        inputParam.put("createdDate", "-25");
        inputParam.put("transactionDate", "-25");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC104DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1490.3");
        inputParam.put("createdDate", "-25");
        inputParam.put("transactionDate", "-25");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC105DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Starbucks Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1370.18");
        inputParam.put("createdDate", "-25");
        inputParam.put("transactionDate", "-25");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC106DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Best Buy 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1351.99");
        inputParam.put("createdDate", "-26");
        inputParam.put("transactionDate", "-26");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC107DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1156.96");
        inputParam.put("createdDate", "-26");
        inputParam.put("transactionDate", "-26");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC108DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1081.05");
        inputParam.put("createdDate", "-26");
        inputParam.put("transactionDate", "-26");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC109DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1043.86");
        inputParam.put("createdDate", "-26");
        inputParam.put("transactionDate", "-26");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC110DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1005.82");
        inputParam.put("createdDate", "-26");
        inputParam.put("transactionDate", "-26");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_MBCC111DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Freedom Credit Card");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "971.609999999999");
        inputParam.put("createdDate", "-26");
        inputParam.put("transactionDate", "-26");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_MBCC112DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Jan Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1052.52");
        inputParam.put("createdDate", "-27");
        inputParam.put("transactionDate", "-27");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC113DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "924.539999999999");
        inputParam.put("createdDate", "-27");
        inputParam.put("transactionDate", "-27");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC114DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "856.349999999999");
        inputParam.put("createdDate", "-27");
        inputParam.put("transactionDate", "-27");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC115DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Starbucks Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "736.229999999999");
        inputParam.put("createdDate", "-27");
        inputParam.put("transactionDate", "-27");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC116DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Best Buy 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "718.039999999999");
        inputParam.put("createdDate", "-27");
        inputParam.put("transactionDate", "-27");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC117DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "523.009999999999");
        inputParam.put("createdDate", "-27");
        inputParam.put("transactionDate", "-27");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC118DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "447.099999999999");
        inputParam.put("createdDate", "-28");
        inputParam.put("transactionDate", "-28");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC119DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "409.909999999999");
        inputParam.put("createdDate", "-28");
        inputParam.put("transactionDate", "-28");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC120DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "371.869999999999");
        inputParam.put("createdDate", "-28");
        inputParam.put("transactionDate", "-28");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_MBCC121DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Freedom Credit Card");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "337.659999999999");
        inputParam.put("createdDate", "-28");
        inputParam.put("transactionDate", "-28");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_MBCC122DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Jan Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "418.569999999999");
        inputParam.put("createdDate", "-28");
        inputParam.put("transactionDate", "-28");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC123DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "290.589999999999");
        inputParam.put("createdDate", "-28");
        inputParam.put("transactionDate", "-28");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC124DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "222.399999999999");
        inputParam.put("createdDate", "-28");
        inputParam.put("transactionDate", "-28");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC125DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Starbucks Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "102.279999999999");
        inputParam.put("createdDate", "-29");
        inputParam.put("transactionDate", "-29");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC126DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "84.0899999999987");
        inputParam.put("createdDate", "-29");
        inputParam.put("transactionDate", "-29");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCardPayment_MBCC127DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "CardPayment");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Freedom Credit Card");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "4800");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment - Thank You");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "46.8999999999987");
        inputParam.put("createdDate", "-29");
        inputParam.put("transactionDate", "-29");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC128DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4846.9");
        inputParam.put("createdDate", "-29");
        inputParam.put("transactionDate", "-29");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_MBCC129DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Freedom Credit Card");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4812.69");
        inputParam.put("createdDate", "-29");
        inputParam.put("transactionDate", "-29");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_MBCC130DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Jan Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4893.6");
        inputParam.put("createdDate", "-29");
        inputParam.put("transactionDate", "-29");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC131DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4765.62");
        inputParam.put("createdDate", "-30");
        inputParam.put("transactionDate", "-30");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC132DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4697.43");
        inputParam.put("createdDate", "-30");
        inputParam.put("transactionDate", "-30");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC133DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Starbucks Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4577.31");
        inputParam.put("createdDate", "-30");
        inputParam.put("transactionDate", "-30");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC134DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Best Buy 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4559.12");
        inputParam.put("createdDate", "-30");
        inputParam.put("transactionDate", "-30");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC135DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4364.09");
        inputParam.put("createdDate", "-30");
        inputParam.put("transactionDate", "-30");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC136DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4288.18");
        inputParam.put("createdDate", "-30");
        inputParam.put("transactionDate", "-30");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC137DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4250.99");
        inputParam.put("createdDate", "-31");
        inputParam.put("transactionDate", "-31");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC138DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4212.95");
        inputParam.put("createdDate", "-31");
        inputParam.put("transactionDate", "-31");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_MBCC139DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Freedom Credit Card");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4178.74");
        inputParam.put("createdDate", "-31");
        inputParam.put("transactionDate", "-31");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_MBCC140DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Jan Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4259.65");
        inputParam.put("createdDate", "-31");
        inputParam.put("transactionDate", "-31");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC141DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4131.67");
        inputParam.put("createdDate", "-31");
        inputParam.put("transactionDate", "-31");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC142DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4063.48");
        inputParam.put("createdDate", "-31");
        inputParam.put("transactionDate", "-31");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC143DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Starbucks Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3943.36");
        inputParam.put("createdDate", "-32");
        inputParam.put("transactionDate", "-32");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC144DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Best Buy 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3925.17");
        inputParam.put("createdDate", "-32");
        inputParam.put("transactionDate", "-32");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC145DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3730.14");
        inputParam.put("createdDate", "-32");
        inputParam.put("transactionDate", "-32");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC146DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3654.23");
        inputParam.put("createdDate", "-32");
        inputParam.put("transactionDate", "-32");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC147DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3617.04");
        inputParam.put("createdDate", "-32");
        inputParam.put("transactionDate", "-32");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC148DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3579");
        inputParam.put("createdDate", "-32");
        inputParam.put("transactionDate", "-32");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_MBCC149DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Freedom Credit Card");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3544.79");
        inputParam.put("createdDate", "-33");
        inputParam.put("transactionDate", "-33");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_MBCC150DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Jan Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3625.7");
        inputParam.put("createdDate", "-33");
        inputParam.put("transactionDate", "-33");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC151DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3497.72");
        inputParam.put("createdDate", "-33");
        inputParam.put("transactionDate", "-33");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC152DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3429.53");
        inputParam.put("createdDate", "-33");
        inputParam.put("transactionDate", "-33");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC153DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Starbucks Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3309.41");
        inputParam.put("createdDate", "-33");
        inputParam.put("transactionDate", "-33");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC154DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Best Buy 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3291.22");
        inputParam.put("createdDate", "-33");
        inputParam.put("transactionDate", "-33");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC155DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3096.19");
        inputParam.put("createdDate", "-34");
        inputParam.put("transactionDate", "-34");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC156DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3020.28");
        inputParam.put("createdDate", "-34");
        inputParam.put("transactionDate", "-34");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC157DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2983.09");
        inputParam.put("createdDate", "-34");
        inputParam.put("transactionDate", "-34");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC158DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2945.05");
        inputParam.put("createdDate", "-34");
        inputParam.put("transactionDate", "-34");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_MBCC159DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Freedom Credit Card");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2910.84");
        inputParam.put("createdDate", "-34");
        inputParam.put("transactionDate", "-34");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_MBCC160DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Jan Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2991.75");
        inputParam.put("createdDate", "-34");
        inputParam.put("transactionDate", "-34");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC161DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2863.77");
        inputParam.put("createdDate", "-34");
        inputParam.put("transactionDate", "-34");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC162DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2795.58");
        inputParam.put("createdDate", "-35");
        inputParam.put("transactionDate", "-35");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC163DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Starbucks Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2675.46");
        inputParam.put("createdDate", "-35");
        inputParam.put("transactionDate", "-35");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC164DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Best Buy 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2657.27");
        inputParam.put("createdDate", "-35");
        inputParam.put("transactionDate", "-35");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC165DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2462.24");
        inputParam.put("createdDate", "-35");
        inputParam.put("transactionDate", "-35");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC166DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2386.33");
        inputParam.put("createdDate", "-35");
        inputParam.put("transactionDate", "-35");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC167DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2349.14");
        inputParam.put("createdDate", "-35");
        inputParam.put("transactionDate", "-35");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC168DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2311.1");
        inputParam.put("createdDate", "-36");
        inputParam.put("transactionDate", "-36");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_MBCC169DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Freedom Credit Card");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2276.89");
        inputParam.put("createdDate", "-36");
        inputParam.put("transactionDate", "-36");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_MBCC170DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Jan Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2357.8");
        inputParam.put("createdDate", "-36");
        inputParam.put("transactionDate", "-36");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC171DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2229.82");
        inputParam.put("createdDate", "-36");
        inputParam.put("transactionDate", "-36");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC172DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2161.63");
        inputParam.put("createdDate", "-36");
        inputParam.put("transactionDate", "-36");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC173DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Starbucks Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2041.51");
        inputParam.put("createdDate", "-36");
        inputParam.put("transactionDate", "-36");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC174DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Best Buy 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2023.32");
        inputParam.put("createdDate", "-37");
        inputParam.put("transactionDate", "-37");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC175DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1828.29");
        inputParam.put("createdDate", "-37");
        inputParam.put("transactionDate", "-37");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC176DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1752.38");
        inputParam.put("createdDate", "-37");
        inputParam.put("transactionDate", "-37");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC177DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1715.19");
        inputParam.put("createdDate", "-37");
        inputParam.put("transactionDate", "-37");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC178DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1677.15");
        inputParam.put("createdDate", "-37");
        inputParam.put("transactionDate", "-37");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_MBCC179DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Freedom Credit Card");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1642.94");
        inputParam.put("createdDate", "-37");
        inputParam.put("transactionDate", "-37");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_MBCC180DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Jan Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1723.85");
        inputParam.put("createdDate", "-38");
        inputParam.put("transactionDate", "-38");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC181DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1595.87");
        inputParam.put("createdDate", "-38");
        inputParam.put("transactionDate", "-38");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC182DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1527.68");
        inputParam.put("createdDate", "-38");
        inputParam.put("transactionDate", "-38");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC183DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Starbucks Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1407.56");
        inputParam.put("createdDate", "-38");
        inputParam.put("transactionDate", "-38");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC184DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Best Buy 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1389.37");
        inputParam.put("createdDate", "-38");
        inputParam.put("transactionDate", "-38");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC185DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1194.34");
        inputParam.put("createdDate", "-38");
        inputParam.put("transactionDate", "-38");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC186DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1118.43");
        inputParam.put("createdDate", "-39");
        inputParam.put("transactionDate", "-39");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getGeneratedAccounts() {
		return generatedAccounts;
	}

	public void setGeneratedAccounts(Map<String, String> generatedAccounts) {
		this.generatedAccounts = generatedAccounts;
	}

	public Map<String, String> getPOS_MBCC187DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Starbucks Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "998.31");
        inputParam.put("createdDate", "-39");
        inputParam.put("transactionDate", "-39");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC188DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Best Buy 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "980.12");
        inputParam.put("createdDate", "-39");
        inputParam.put("transactionDate", "-39");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC189DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "785.09");
        inputParam.put("createdDate", "-39");
        inputParam.put("transactionDate", "-39");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC190DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "709.18");
        inputParam.put("createdDate", "-39");
        inputParam.put("transactionDate", "-39");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC191DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "671.99");
        inputParam.put("createdDate", "-39");
        inputParam.put("transactionDate", "-39");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC192DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "633.95");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_MBCC193DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Freedom Credit Card");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "599.74");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_MBCC194DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Jan Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "680.65");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC195DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "552.67");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC196DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "484.48");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC197DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Starbucks Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "364.36");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC198DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Best Buy 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "346.17");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_MBCC199DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "151.14");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC200DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "75.23");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_MBCC201DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "38.04");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCardPayment_MBCC202DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "CardPayment");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Freedom Credit Card");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "3019.26");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment - Thank You");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "0");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_MBCC203DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Freedom Credit Card");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "162.51");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "April Electricity Bill");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "1");
        inputParam.put("scheduledDate", "+14");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getLoanPayment_MBLoan1DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "LoanPayment");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Turbo Auto Loan");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "368");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "16024.2288358465");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getLoanPayment_MBLoan2DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "LoanPayment");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Turbo Auto Loan");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "368");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "16361.5509278567");
        inputParam.put("createdDate", "-37");
        inputParam.put("transactionDate", "-37");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getLoanPayment_MBLoan3DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "LoanPayment");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Turbo Auto Loan");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "368");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "16698.2417246231");
        inputParam.put("createdDate", "-67");
        inputParam.put("transactionDate", "-67");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getTax_MBLoan4DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Tax");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Turbo Auto Loan");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "10");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "County tax");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "17034.3024076088");
        inputParam.put("createdDate", "-97");
        inputParam.put("transactionDate", "-97");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getLoanPayment_MBLoan5DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "LoanPayment");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Turbo Auto Loan");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "368");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "17034.3024076088");
        inputParam.put("createdDate", "-97");
        inputParam.put("transactionDate", "-97");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getLoanPayment_MBLoan6DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "LoanPayment");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Turbo Auto Loan");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "368");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "17369.7341560662");
        inputParam.put("createdDate", "-127");
        inputParam.put("transactionDate", "-127");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getLoanPayment_MBLoan7DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "LoanPayment");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Turbo Auto Loan");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "368");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "17704.5381470405");
        inputParam.put("createdDate", "-157");
        inputParam.put("transactionDate", "-157");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getLoanPayment_MBLoan8DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "LoanPayment");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Turbo Auto Loan");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "368");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "18038.7155553741");
        inputParam.put("createdDate", "-187");
        inputParam.put("transactionDate", "-187");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getLoanPayment_MBLoan9DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "LoanPayment");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Turbo Auto Loan");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "368");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "18372.2675537109");
        inputParam.put("createdDate", "-217");
        inputParam.put("transactionDate", "-217");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getLoanPayment_MBLoan10DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "LoanPayment");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Turbo Auto Loan");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "368");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "18705.1953125");
        inputParam.put("createdDate", "-247");
        inputParam.put("transactionDate", "-247");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getLoanPayment_MBLoan11DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "LoanPayment");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Turbo Auto Loan");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "1000");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Principal Payment");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "19037.5");
        inputParam.put("createdDate", "-277");
        inputParam.put("transactionDate", "-277");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_MBLoan12DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Turbo Auto Loan");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "20000");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "New loan setup");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "20000");
        inputParam.put("createdDate", "-317");
        inputParam.put("transactionDate", "-317");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInterestCredit_MBDeposit1DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InterestCredit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "12 Months Term Deposit");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "0.23");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Interest on Term Deposit");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInterestCredit_MBDeposit2DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InterestCredit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "12 Months Term Deposit");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "0.18");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Interest on Term Deposit");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "");
        inputParam.put("createdDate", "-14");
        inputParam.put("transactionDate", "-14");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInterestCredit_MBDeposit3DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InterestCredit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "12 Months Term Deposit");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "0.17");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Interest on Term Deposit");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "");
        inputParam.put("createdDate", "-21");
        inputParam.put("transactionDate", "-21");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCardTransaction1DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "5314000000004360");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43959");
        inputParam.put("transactionTime", "11:30:23");
        inputParam.put("transactionDescription", "Sunny Bee, Clark Ave");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "SUNNY BEE");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Grocery");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "B");
        inputParam.put("transactionDetailDescription", "Sunny Bee, Clark Ave");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-3");
        inputParam.put("transactionReferenceNumber", "92315215");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS1422");
        return inputParam;
    }
    public Map getCardTransaction2DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "5314000000004360");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43959");
        inputParam.put("transactionTime", "11:30:25");
        inputParam.put("transactionDescription", "SRV CHGS");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "SUNNY BEE");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Grocery");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "F");
        inputParam.put("transactionCategory", "B");
        inputParam.put("transactionDetailDescription", "Sunny Bee, Clark Ave");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-0.015");
        inputParam.put("transactionReferenceNumber", "92315324");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "Y");
        inputParam.put("taxPercentage", "0.5");
        inputParam.put("transactionTaxAmount", "0.01");
        inputParam.put("transactionTerminalID", "POS1422");
        return inputParam;
    }
    public Map getCardTransaction3DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "5314000000004360");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43959");
        inputParam.put("transactionTime", "13:03:48");
        inputParam.put("transactionDescription", "Amazon US");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "AMAZON");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Online Shopping");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "B");
        inputParam.put("transactionDetailDescription", "Amazon Online");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-25");
        inputParam.put("transactionReferenceNumber", "AM0242555");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "OL94242");
        return inputParam;
    }
    public Map getCardTransaction4DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "5314000000004360");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43961");
        inputParam.put("transactionTime", "08:05:48");
        inputParam.put("transactionDescription", "Total Gas");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "TOTAL GAS");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Fuel");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "B");
        inputParam.put("transactionDetailDescription", "Total Gas, Mark St");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-8.25");
        inputParam.put("transactionReferenceNumber", "53536396363");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS225252");
        return inputParam;
    }
    public Map getCardTransaction5DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "5314000000004360");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43963");
        inputParam.put("transactionTime", "10:00:35");
        inputParam.put("transactionDescription", "Starbucks");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "STARTBUCKS");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "B");
        inputParam.put("transactionDetailDescription", "Startbucks, Tower plaza");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-1.25");
        inputParam.put("transactionReferenceNumber", "242647758");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "QR235673888");
        return inputParam;
    }
    public Map getCardTransaction6DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "5314000000004360");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43964");
        inputParam.put("transactionTime", "15:03:24");
        inputParam.put("transactionDescription", "Payment Received");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "ACE BANK");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Finance");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "B");
        inputParam.put("transactionDetailDescription", "ACE BANK");
        inputParam.put("transactionIndicator", "C");
        inputParam.put("transactionAmount", "350");
        inputParam.put("transactionReferenceNumber", "352362623");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "OL253536262");
        return inputParam;
    }
    public Map getCardTransaction7DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "5314000000004360");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43964");
        inputParam.put("transactionTime", "20:15:54");
        inputParam.put("transactionDescription", "Ramsay's");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "RAMSAY RESTO");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "B");
        inputParam.put("transactionDetailDescription", "Ramsay's Resto, Beverly Ave");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-125");
        inputParam.put("transactionReferenceNumber", "2532626266");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS367575886");
        return inputParam;
    }
    public Map getCardTransaction8DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "5314000000004360");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43964");
        inputParam.put("transactionTime", "20:15:54");
        inputParam.put("transactionDescription", "SRV CHGS");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "RAMSAY RESTO");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "F");
        inputParam.put("transactionCategory", "B");
        inputParam.put("transactionDetailDescription", "Ramsay's Resto, Beverly Ave");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-3.125");
        inputParam.put("transactionReferenceNumber", "2532626268");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "Y");
        inputParam.put("taxPercentage", "2.5");
        inputParam.put("transactionTaxAmount", "1.25");
        inputParam.put("transactionTerminalID", "POS367575886");
        return inputParam;
    }
    public Map getCardTransaction9DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "5314000000004360");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43965");
        inputParam.put("transactionTime", "06:00:10");
        inputParam.put("transactionDescription", "Netflix");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "NETFLIX");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Entertainment");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "B");
        inputParam.put("transactionDetailDescription", "Netflix");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-10");
        inputParam.put("transactionReferenceNumber", "NF2215262677");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "Y");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "OL21511666");
        return inputParam;
    }
    public Map getCardTransaction10DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "5314000000004360");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43966");
        inputParam.put("transactionTime", "10:03:24");
        inputParam.put("transactionDescription", "Sunny Bee, Clark Ave");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "SUNNY BEE");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Grocery");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "B");
        inputParam.put("transactionDetailDescription", "Sunny Bee, Clark Ave");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-18");
        inputParam.put("transactionReferenceNumber", "92315215");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS1422");
        return inputParam;
    }
    public Map getCardTransaction11DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "5314000000004360");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43966");
        inputParam.put("transactionTime", "10:03:25");
        inputParam.put("transactionDescription", "SRVC CHGS");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "SUNNY BEE");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Grocery");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "F");
        inputParam.put("transactionCategory", "B");
        inputParam.put("transactionDetailDescription", "Sunny Bee, Clark Ave");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-0.09");
        inputParam.put("transactionReferenceNumber", "92315324");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "Y");
        inputParam.put("taxPercentage", "0.5");
        inputParam.put("transactionTaxAmount", "0.1");
        inputParam.put("transactionTerminalID", "POS1422");
        return inputParam;
    }
    public Map getCardTransaction12DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "5314000000004360");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43967");
        inputParam.put("transactionTime", "07:00:10");
        inputParam.put("transactionDescription", "Uber");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "UBER");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Travel");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "U");
        inputParam.put("transactionDetailDescription", "Uber Transportation");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-10");
        inputParam.put("transactionReferenceNumber", "2141564637");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "235232637378");
        return inputParam;
    }
    public Map getCardTransaction13DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "5314000000004360");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43967");
        inputParam.put("transactionTime", "07:30:02");
        inputParam.put("transactionDescription", "Burger King, Airport");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "BURGER KING");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "U");
        inputParam.put("transactionDetailDescription", "Burger King, Airport");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-3.25");
        inputParam.put("transactionReferenceNumber", "12415178");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS99255636");
        return inputParam;
    }
    public Map getCardTransaction14DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "5314000000004360");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43968");
        inputParam.put("transactionTime", "20:18:32");
        inputParam.put("transactionDescription", "Uber");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "UBER");
        inputParam.put("transactionMerchantCity", "Manchester");
        inputParam.put("merchantCategory", "Travel");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "U");
        inputParam.put("transactionDetailDescription", "Uber UK");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-15");
        inputParam.put("transactionReferenceNumber", "79264485");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "0.91");
        inputParam.put("exchangeCurrency", "EUR");
        inputParam.put("exchangeAmount", "13.65");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "324678959");
        return inputParam;
    }
    public Map getCardTransaction15DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "5314000000004360");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43969");
        inputParam.put("transactionTime", "07:00:10");
        inputParam.put("transactionDescription", "Uber");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "UBER");
        inputParam.put("transactionMerchantCity", "Manchester");
        inputParam.put("merchantCategory", "Travel");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "U");
        inputParam.put("transactionDetailDescription", "Uber UK");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-5");
        inputParam.put("transactionReferenceNumber", "3534643747");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "0.91");
        inputParam.put("exchangeCurrency", "EUR");
        inputParam.put("exchangeAmount", "4.55");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "6357458488");
        return inputParam;
    }
    public Map getCardTransaction16DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "5314000000004360");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43969");
        inputParam.put("transactionTime", "12:30:10");
        inputParam.put("transactionDescription", "Ormer Mayfair Restaurant");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "ORMER GROUPS");
        inputParam.put("transactionMerchantCity", "Manchester");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "P");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "U");
        inputParam.put("transactionDetailDescription", "Ormer Mayfair, Piccadilly");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-380");
        inputParam.put("transactionReferenceNumber", "OM124151626");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "0.91");
        inputParam.put("exchangeCurrency", "EUR");
        inputParam.put("exchangeAmount", "345.72");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS9215151");
        return inputParam;
    }
    public Map getCardTransaction17DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "5314000000004360");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43969");
        inputParam.put("transactionTime", "17:30:10");
        inputParam.put("transactionDescription", "Holiday Inn, London");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "Holiday INN GROUPS");
        inputParam.put("transactionMerchantCity", "Manchester");
        inputParam.put("merchantCategory", "Accomodation");
        inputParam.put("transactionStatus", "P");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "U");
        inputParam.put("transactionDetailDescription", "Holiday Inn, UK");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-100");
        inputParam.put("transactionReferenceNumber", "3444853894253785");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "0.91");
        inputParam.put("exchangeCurrency", "EUR");
        inputParam.put("exchangeAmount", "90.98");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS163789232");
        return inputParam;
    }
    public Map getCardTransaction18DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "5314000000004360");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43969");
        inputParam.put("transactionTime", "18:00:10");
        inputParam.put("transactionDescription", "Uber");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "UBER");
        inputParam.put("transactionMerchantCity", "Manchester");
        inputParam.put("merchantCategory", "Travel");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "U");
        inputParam.put("transactionDetailDescription", "Uber UK");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-5");
        inputParam.put("transactionReferenceNumber", "3534643747");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "0.91");
        inputParam.put("exchangeCurrency", "EUR");
        inputParam.put("exchangeAmount", "4.55");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "6357458488");
        return inputParam;
    }
    public Map getCardTransaction19DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "5314000000004360");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43970");
        inputParam.put("transactionTime", "07:00:10");
        inputParam.put("transactionDescription", "Uber");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "UBER");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Travel");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "U");
        inputParam.put("transactionDetailDescription", "Uber Transportation");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-15");
        inputParam.put("transactionReferenceNumber", "354654648");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "991241521515");
        return inputParam;
    }
    public Map getCardTransaction20DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "5314000000004360");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43971");
        inputParam.put("transactionTime", "10:03:24");
        inputParam.put("transactionDescription", "Cosco");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "COSCO MART");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Shopping");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "U");
        inputParam.put("transactionDetailDescription", "COSCO, US");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-40");
        inputParam.put("transactionReferenceNumber", "241512516361");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS2515167888");
        return inputParam;
    }
    public Map getCardTransaction21DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43959");
        inputParam.put("transactionTime", "11:30:23");
        inputParam.put("transactionDescription", "Cash Withdrawal ATM");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "ACE BANK ATM");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Finance");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Cash WDWL ATM");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-200");
        inputParam.put("transactionReferenceNumber", "223262786");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "ATM2527454848");
        return inputParam;
    }
    public Map getCardTransaction22DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43959");
        inputParam.put("transactionTime", "11:30:25");
        inputParam.put("transactionDescription", "SRVC CHGS ATM 1%");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "ACE BANK ATM");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Finance");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Cash WDWL ATM");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-2");
        inputParam.put("transactionReferenceNumber", "223262790");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "ATM2527454848");
        return inputParam;
    }
    public Map getCardTransaction23DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43959");
        inputParam.put("transactionTime", "13:03:48");
        inputParam.put("transactionDescription", "Taco Bell");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "TACO BELL");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Taco Bell, Clar Ave");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-3.5");
        inputParam.put("transactionReferenceNumber", "78346262788");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS166437373");
        return inputParam;
    }
    public Map getCardTransaction24DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43961");
        inputParam.put("transactionTime", "08:05:48");
        inputParam.put("transactionDescription", "Toys R Us");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "TOYS R US");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Shopping");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Toysrus, Total Mall");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-15.45");
        inputParam.put("transactionReferenceNumber", "3252262622");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS532362622");
        return inputParam;
    }
    public Map getCardTransaction25DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43963");
        inputParam.put("transactionTime", "10:00:35");
        inputParam.put("transactionDescription", "Old Mac Bakery");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "OLD MAC BAKERY");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Old Mac Bakery");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-2.6");
        inputParam.put("transactionReferenceNumber", "353262623623");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS125162472");
        return inputParam;
    }
    public Map getCardTransaction26DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43964");
        inputParam.put("transactionTime", "20:15:54");
        inputParam.put("transactionDescription", "Pizza Hut");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "PIZZA HUT");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Pizza Hut, Clive St");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-5.42");
        inputParam.put("transactionReferenceNumber", "5326262623626");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS2423262");
        return inputParam;
    }
    public Map getCardTransaction27DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43964");
        inputParam.put("transactionTime", "20:45:54");
        inputParam.put("transactionDescription", "Coldstone Creamery");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "COLDSTONE CREAMERY");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "ColdStone Creamery, Clive St");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-8.3");
        inputParam.put("transactionReferenceNumber", "315125125215");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS186859542");
        return inputParam;
    }
    public Map getCardTransaction28DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43965");
        inputParam.put("transactionTime", "06:00:10");
        inputParam.put("transactionDescription", "Max Life Insurance");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "MAX LIFE INSURANCE");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Investment");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Maxlife Insurance 242515156");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-550");
        inputParam.put("transactionReferenceNumber", "241241251331613");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "OL32363623623");
        return inputParam;
    }
    public Map getCardTransaction29DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43965");
        inputParam.put("transactionTime", "15:03:24");
        inputParam.put("transactionDescription", "Total Gas");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "TOTAL GAS");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Fuel");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Total Gas, Mark St");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-4");
        inputParam.put("transactionReferenceNumber", "15645844848");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS225252");
        return inputParam;
    }
    public Map getCardTransaction30DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43966");
        inputParam.put("transactionTime", "10:03:24");
        inputParam.put("transactionDescription", "Sunny Bee, Clark Ave");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "SUNNY BEE");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Grocery");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Sunny Bee, Clark Ave");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-3");
        inputParam.put("transactionReferenceNumber", "92315215");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS1422");
        return inputParam;
    }
    public Map getCardTransaction31DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43966");
        inputParam.put("transactionTime", "10:03:25");
        inputParam.put("transactionDescription", "Sunny Bee SRV CHG");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "SUNNY BEE");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Grocery");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "F");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Sunny Bee, Clark Ave");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-0.015");
        inputParam.put("transactionReferenceNumber", "92315324");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS1422");
        return inputParam;
    }
    public Map getCardTransaction32DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43967");
        inputParam.put("transactionTime", "07:00:10");
        inputParam.put("transactionDescription", "Cash Withdrawal ATM");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "ACE BANK ATM");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Finance");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Cash WDWL ATM");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-45");
        inputParam.put("transactionReferenceNumber", "223246446");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "ATM2527454848");
        return inputParam;
    }
    public Map getCardTransaction33DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43967");
        inputParam.put("transactionTime", "07:30:02");
        inputParam.put("transactionDescription", "SRVC CHGS ATM 1%");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "ACE BANK ATM");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Finance");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Cash WDWL ATM");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-0.8");
        inputParam.put("transactionReferenceNumber", "225252790");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "ATM2527454848");
        return inputParam;
    }
    public Map getCardTransaction34DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43968");
        inputParam.put("transactionTime", "20:18:32");
        inputParam.put("transactionDescription", "Uber");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "UBER");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Travel");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Uber Transportation");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-4.5");
        inputParam.put("transactionReferenceNumber", "114141564");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS5315");
        return inputParam;
    }
    public Map getCardTransaction35DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43969");
        inputParam.put("transactionTime", "07:00:10");
        inputParam.put("transactionDescription", "Apple Music");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "APPLE MUSIC");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Entertainment");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Apple Music Bill 01114");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-12");
        inputParam.put("transactionReferenceNumber", "323623632636326");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "OL1515125125");
        return inputParam;
    }
    public Map getCardTransaction36DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43969");
        inputParam.put("transactionTime", "12:30:10");
        inputParam.put("transactionDescription", "Starbucks");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "STARTBUCKS");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Startbucks, Tower plaza");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-1.25");
        inputParam.put("transactionReferenceNumber", "241252323");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "QR23524225");
        return inputParam;
    }
    public Map getCardTransaction37DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43969");
        inputParam.put("transactionTime", "17:30:10");
        inputParam.put("transactionDescription", "HardRock Café");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "HARDROCK CAFÉ");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Hardrock Café, Marine Drive");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-14.5");
        inputParam.put("transactionReferenceNumber", "125151252156");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS1423623678");
        return inputParam;
    }
    public Map getCardTransaction38DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43970");
        inputParam.put("transactionTime", "07:00:10");
        inputParam.put("transactionDescription", "Amazon US");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "AMAZON");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Online");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Amazon Online");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-25");
        inputParam.put("transactionReferenceNumber", "AM02425514");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "OL94242");
        return inputParam;
    }
    public Map getCardTransaction39DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43970");
        inputParam.put("transactionTime", "18:00:10");
        inputParam.put("transactionDescription", "Mark Hospital");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "MARK HOSPITAL");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Health");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Mark Hospital, Sacramento");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-800");
        inputParam.put("transactionReferenceNumber", "6474262623");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS014166348");
        return inputParam;
    }
    public Map getCardTransaction40DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43971");
        inputParam.put("transactionTime", "10:03:24");
        inputParam.put("transactionDescription", "Cash Withdrawal ATM");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "ACE BANK ATM");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Finance");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Cash WDWL ATM");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-200");
        inputParam.put("transactionReferenceNumber", "353734232");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "ATM2527454848");
        return inputParam;
    }
    public Map getCardTransaction41DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43971");
        inputParam.put("transactionTime", "10:03:24");
        inputParam.put("transactionDescription", "SRVC CHGS ATM 1%");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "ACE BANK ATM");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Finance");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Cash WDWL ATM");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-1.2");
        inputParam.put("transactionReferenceNumber", "353734233");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "ATM2527454848");
        return inputParam;
    }
    public Map getCardTransaction42DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43974");
        inputParam.put("transactionTime", "12:30:10");
        inputParam.put("transactionDescription", "Starbucks");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "STARTBUCKS");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "P");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Startbucks, Tower plaza");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-3.45");
        inputParam.put("transactionReferenceNumber", "252626271");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "QR23521551");
        return inputParam;
    }
    public Map getCardTransaction43DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43974");
        inputParam.put("transactionTime", "17:30:10");
        inputParam.put("transactionDescription", "Total Gas");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "TOTAL GAS");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Fuel");
        inputParam.put("transactionStatus", "P");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Total Gas, Mark St");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-6.2");
        inputParam.put("transactionReferenceNumber", "65745845832");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS222362");
        return inputParam;
    }
    public Map getCardTransaction44DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43974");
        inputParam.put("transactionTime", "07:00:10");
        inputParam.put("transactionDescription", "Amazon US");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "AMAZON");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Online");
        inputParam.put("transactionStatus", "P");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Amazon Online");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-125");
        inputParam.put("transactionReferenceNumber", "AM02422142");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "OL943161");
        return inputParam;
    }
    public Map getCardTransaction45DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43959");
        inputParam.put("transactionTime", "11:30:23");
        inputParam.put("transactionDescription", "Sunny Bee, Clark Ave");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "SUNNY BEE");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Grocery");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "B");
        inputParam.put("transactionDetailDescription", "Sunny Bee, Clark Ave");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-3");
        inputParam.put("transactionReferenceNumber", "92315215");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS1422");
        return inputParam;
    }
    public Map getCardTransaction46DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43959");
        inputParam.put("transactionTime", "11:30:25");
        inputParam.put("transactionDescription", "SRV CHGS");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "SUNNY BEE");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Grocery");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "F");
        inputParam.put("transactionCategory", "B");
        inputParam.put("transactionDetailDescription", "Sunny Bee, Clark Ave");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-0.015");
        inputParam.put("transactionReferenceNumber", "92315324");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "Y");
        inputParam.put("taxPercentage", "0.5");
        inputParam.put("transactionTaxAmount", "0.01");
        inputParam.put("transactionTerminalID", "POS1422");
        return inputParam;
    }
    public Map getCardTransaction47DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43959");
        inputParam.put("transactionTime", "13:03:48");
        inputParam.put("transactionDescription", "Amazon US");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "AMAZON");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Online Shopping");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "B");
        inputParam.put("transactionDetailDescription", "Amazon Online");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-25");
        inputParam.put("transactionReferenceNumber", "AM0242555");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "OL94242");
        return inputParam;
    }
    public Map getCardTransaction48DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43961");
        inputParam.put("transactionTime", "08:05:48");
        inputParam.put("transactionDescription", "Total Gas");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "TOTAL GAS");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Fuel");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "B");
        inputParam.put("transactionDetailDescription", "Total Gas, Mark St");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-8.25");
        inputParam.put("transactionReferenceNumber", "53536396363");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS225252");
        return inputParam;
    }
    public Map getCardTransaction49DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43963");
        inputParam.put("transactionTime", "10:00:35");
        inputParam.put("transactionDescription", "Starbucks");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "STARTBUCKS");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "B");
        inputParam.put("transactionDetailDescription", "Startbucks, Tower plaza");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-1.25");
        inputParam.put("transactionReferenceNumber", "242647758");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "QR235673888");
        return inputParam;
    }
    public Map getCardTransaction50DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43964");
        inputParam.put("transactionTime", "15:03:24");
        inputParam.put("transactionDescription", "Payment Received");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "ACE BANK");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Finance");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "B");
        inputParam.put("transactionDetailDescription", "ACE BANK");
        inputParam.put("transactionIndicator", "C");
        inputParam.put("transactionAmount", "350");
        inputParam.put("transactionReferenceNumber", "352362623");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "OL253536262");
        return inputParam;
    }
    public Map getCardTransaction51DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43964");
        inputParam.put("transactionTime", "20:15:54");
        inputParam.put("transactionDescription", "Ramsay's");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "RAMSAY RESTO");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "B");
        inputParam.put("transactionDetailDescription", "Ramsay's Resto, Beverly Ave");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-125");
        inputParam.put("transactionReferenceNumber", "2532626266");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS367575886");
        return inputParam;
    }
    public Map getCardTransaction52DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43964");
        inputParam.put("transactionTime", "20:15:54");
        inputParam.put("transactionDescription", "SRV CHGS");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "RAMSAY RESTO");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "F");
        inputParam.put("transactionCategory", "B");
        inputParam.put("transactionDetailDescription", "Ramsay's Resto, Beverly Ave");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-3.125");
        inputParam.put("transactionReferenceNumber", "2532626268");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "Y");
        inputParam.put("taxPercentage", "2.5");
        inputParam.put("transactionTaxAmount", "1.25");
        inputParam.put("transactionTerminalID", "POS367575886");
        return inputParam;
    }
    public Map getCardTransaction53DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43965");
        inputParam.put("transactionTime", "06:00:10");
        inputParam.put("transactionDescription", "Netflix");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "NETFLIX");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Entertainment");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "B");
        inputParam.put("transactionDetailDescription", "Netflix");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-10");
        inputParam.put("transactionReferenceNumber", "NF2215262677");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "Y");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "OL21511666");
        return inputParam;
    }
    public Map getCardTransaction54DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43966");
        inputParam.put("transactionTime", "10:03:24");
        inputParam.put("transactionDescription", "Sunny Bee, Clark Ave");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "SUNNY BEE");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Grocery");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "B");
        inputParam.put("transactionDetailDescription", "Sunny Bee, Clark Ave");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-18");
        inputParam.put("transactionReferenceNumber", "92315215");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS1422");
        return inputParam;
    }
    public Map getCardTransaction55DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43966");
        inputParam.put("transactionTime", "10:03:25");
        inputParam.put("transactionDescription", "SRVC CHGS");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "SUNNY BEE");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Grocery");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "F");
        inputParam.put("transactionCategory", "B");
        inputParam.put("transactionDetailDescription", "Sunny Bee, Clark Ave");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-0.09");
        inputParam.put("transactionReferenceNumber", "92315324");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "Y");
        inputParam.put("taxPercentage", "0.5");
        inputParam.put("transactionTaxAmount", "0.1");
        inputParam.put("transactionTerminalID", "POS1422");
        return inputParam;
    }
    public Map getCardTransaction56DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43967");
        inputParam.put("transactionTime", "07:00:10");
        inputParam.put("transactionDescription", "Uber");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "UBER");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Travel");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "U");
        inputParam.put("transactionDetailDescription", "Uber Transportation");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-10");
        inputParam.put("transactionReferenceNumber", "2141564637");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "235232637378");
        return inputParam;
    }
    public Map getCardTransaction57DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43967");
        inputParam.put("transactionTime", "07:30:02");
        inputParam.put("transactionDescription", "Burger King, Airport");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "BURGER KING");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "U");
        inputParam.put("transactionDetailDescription", "Burger King, Airport");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-3.25");
        inputParam.put("transactionReferenceNumber", "12415178");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS99255636");
        return inputParam;
    }
    public Map getCardTransaction58DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43968");
        inputParam.put("transactionTime", "20:18:32");
        inputParam.put("transactionDescription", "Uber");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "UBER");
        inputParam.put("transactionMerchantCity", "Manchester");
        inputParam.put("merchantCategory", "Travel");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "U");
        inputParam.put("transactionDetailDescription", "Uber UK");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-15");
        inputParam.put("transactionReferenceNumber", "79264485");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "0.91");
        inputParam.put("exchangeCurrency", "EUR");
        inputParam.put("exchangeAmount", "13.65");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "324678959");
        return inputParam;
    }
    public Map getCardTransaction59DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43969");
        inputParam.put("transactionTime", "07:00:10");
        inputParam.put("transactionDescription", "Uber");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "UBER");
        inputParam.put("transactionMerchantCity", "Manchester");
        inputParam.put("merchantCategory", "Travel");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "U");
        inputParam.put("transactionDetailDescription", "Uber UK");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-5");
        inputParam.put("transactionReferenceNumber", "3534643747");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "0.91");
        inputParam.put("exchangeCurrency", "EUR");
        inputParam.put("exchangeAmount", "4.55");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "6357458488");
        return inputParam;
    }
    public Map getCardTransaction60DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43969");
        inputParam.put("transactionTime", "12:30:10");
        inputParam.put("transactionDescription", "Ormer Mayfair Restaurant");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "ORMER GROUPS");
        inputParam.put("transactionMerchantCity", "Manchester");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "P");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "U");
        inputParam.put("transactionDetailDescription", "Ormer Mayfair, Piccadilly");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-580");
        inputParam.put("transactionReferenceNumber", "OM124151626");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "0.91");
        inputParam.put("exchangeCurrency", "EUR");
        inputParam.put("exchangeAmount", "345.72");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS9215151");
        return inputParam;
    }
    public Map getCardTransaction61DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43969");
        inputParam.put("transactionTime", "17:30:10");
        inputParam.put("transactionDescription", "Holiday Inn, London");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "Holiday INN GROUPS");
        inputParam.put("transactionMerchantCity", "Manchester");
        inputParam.put("merchantCategory", "Accomodation");
        inputParam.put("transactionStatus", "P");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "U");
        inputParam.put("transactionDetailDescription", "Holiday Inn, UK");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-390");
        inputParam.put("transactionReferenceNumber", "3444853894253785");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "0.91");
        inputParam.put("exchangeCurrency", "EUR");
        inputParam.put("exchangeAmount", "90.98");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS163789232");
        return inputParam;
    }
    public Map getCardTransaction62DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43969");
        inputParam.put("transactionTime", "18:00:10");
        inputParam.put("transactionDescription", "Uber");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "UBER");
        inputParam.put("transactionMerchantCity", "Manchester");
        inputParam.put("merchantCategory", "Travel");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "U");
        inputParam.put("transactionDetailDescription", "Uber UK");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-5");
        inputParam.put("transactionReferenceNumber", "3534643747");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "0.91");
        inputParam.put("exchangeCurrency", "EUR");
        inputParam.put("exchangeAmount", "4.55");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "6357458488");
        return inputParam;
    }
    public Map getCardTransaction63DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43970");
        inputParam.put("transactionTime", "07:00:10");
        inputParam.put("transactionDescription", "Uber");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "UBER");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Travel");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "U");
        inputParam.put("transactionDetailDescription", "Uber Transportation");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-15");
        inputParam.put("transactionReferenceNumber", "354654648");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "991241521515");
        return inputParam;
    }
    public Map getCardTransaction64DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43971");
        inputParam.put("transactionTime", "10:03:24");
        inputParam.put("transactionDescription", "Cosco");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "COSCO MART");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Shopping");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "U");
        inputParam.put("transactionDetailDescription", "COSCO, US");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-40");
        inputParam.put("transactionReferenceNumber", "241512516361");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS2515167888");
        return inputParam;
    }
    public Map getCardTransaction65DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43959");
        inputParam.put("transactionTime", "11:30:23");
        inputParam.put("transactionDescription", "Cash Withdrawal ATM");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "ACE BANK ATM");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Finance");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Cash WDWL ATM");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-200");
        inputParam.put("transactionReferenceNumber", "223262786");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "ATM2527454848");
        return inputParam;
    }
    public Map getCardTransaction66DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43959");
        inputParam.put("transactionTime", "11:30:25");
        inputParam.put("transactionDescription", "SRVC CHGS ATM 1%");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "ACE BANK ATM");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Finance");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Cash WDWL ATM");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-2");
        inputParam.put("transactionReferenceNumber", "223262790");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "ATM2527454848");
        return inputParam;
    }
    public Map getCardTransaction67DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43959");
        inputParam.put("transactionTime", "13:03:48");
        inputParam.put("transactionDescription", "Taco Bell");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "TACO BELL");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Taco Bell, Clar Ave");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-3.5");
        inputParam.put("transactionReferenceNumber", "78346262788");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS166437373");
        return inputParam;
    }
    public Map getCardTransaction68DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43961");
        inputParam.put("transactionTime", "08:05:48");
        inputParam.put("transactionDescription", "Toys R Us");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "TOYS R US");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Shopping");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Toysrus, Total Mall");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-15.45");
        inputParam.put("transactionReferenceNumber", "3252262622");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS532362622");
        return inputParam;
    }
    public Map getCardTransaction69DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43963");
        inputParam.put("transactionTime", "10:00:35");
        inputParam.put("transactionDescription", "Old Mac Bakery");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "OLD MAC BAKERY");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Old Mac Bakery");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-2.6");
        inputParam.put("transactionReferenceNumber", "353262623623");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS125162472");
        return inputParam;
    }
    public Map getCardTransaction70DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43964");
        inputParam.put("transactionTime", "20:15:54");
        inputParam.put("transactionDescription", "Pizza Hut");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "PIZZA HUT");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Pizza Hut, Clive St");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-5.42");
        inputParam.put("transactionReferenceNumber", "5326262623626");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS2423262");
        return inputParam;
    }
    public Map getCardTransaction71DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43964");
        inputParam.put("transactionTime", "20:45:54");
        inputParam.put("transactionDescription", "Coldstone Creamery");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "COLDSTONE CREAMERY");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "ColdStone Creamery, Clive St");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-8.3");
        inputParam.put("transactionReferenceNumber", "315125125215");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS186859542");
        return inputParam;
    }
    public Map getCardTransaction72DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43965");
        inputParam.put("transactionTime", "06:00:10");
        inputParam.put("transactionDescription", "Max Life Insurance");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "MAX LIFE INSURANCE");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Investment");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Maxlife Insurance 242515156");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-550");
        inputParam.put("transactionReferenceNumber", "241241251331613");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "OL32363623623");
        return inputParam;
    }
    public Map getCardTransaction73DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43965");
        inputParam.put("transactionTime", "15:03:24");
        inputParam.put("transactionDescription", "Total Gas");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "TOTAL GAS");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Fuel");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Total Gas, Mark St");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-4");
        inputParam.put("transactionReferenceNumber", "15645844848");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS225252");
        return inputParam;
    }
    public Map getCardTransaction74DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43966");
        inputParam.put("transactionTime", "10:03:24");
        inputParam.put("transactionDescription", "Sunny Bee, Clark Ave");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "SUNNY BEE");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Grocery");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Sunny Bee, Clark Ave");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-3");
        inputParam.put("transactionReferenceNumber", "92315215");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS1422");
        return inputParam;
    }
    public Map getCardTransaction75DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43966");
        inputParam.put("transactionTime", "10:03:25");
        inputParam.put("transactionDescription", "Sunny Bee SRV CHG");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "SUNNY BEE");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Grocery");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "F");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Sunny Bee, Clark Ave");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-0.015");
        inputParam.put("transactionReferenceNumber", "92315324");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS1422");
        return inputParam;
    }
    public Map getCardTransaction76DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43967");
        inputParam.put("transactionTime", "07:00:10");
        inputParam.put("transactionDescription", "Cash Withdrawal ATM");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "ACE BANK ATM");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Finance");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Cash WDWL ATM");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-45");
        inputParam.put("transactionReferenceNumber", "223246446");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "ATM2527454848");
        return inputParam;
    }
    public Map getCardTransaction77DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43967");
        inputParam.put("transactionTime", "07:30:02");
        inputParam.put("transactionDescription", "SRVC CHGS ATM 1%");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "ACE BANK ATM");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Finance");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Cash WDWL ATM");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-0.8");
        inputParam.put("transactionReferenceNumber", "225252790");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "ATM2527454848");
        return inputParam;
    }
    public Map getCardTransaction78DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43968");
        inputParam.put("transactionTime", "20:18:32");
        inputParam.put("transactionDescription", "Uber");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "UBER");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Travel");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Uber Transportation");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-4.5");
        inputParam.put("transactionReferenceNumber", "114141564");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS5315");
        return inputParam;
    }
    public Map getCardTransaction79DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43969");
        inputParam.put("transactionTime", "07:00:10");
        inputParam.put("transactionDescription", "Apple Music");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "APPLE MUSIC");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Entertainment");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Apple Music Bill 01114");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-12");
        inputParam.put("transactionReferenceNumber", "323623632636326");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "OL1515125125");
        return inputParam;
    }
    public Map getCardTransaction80DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43969");
        inputParam.put("transactionTime", "12:30:10");
        inputParam.put("transactionDescription", "Starbucks");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "STARTBUCKS");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Startbucks, Tower plaza");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-1.25");
        inputParam.put("transactionReferenceNumber", "241252323");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "QR23524225");
        return inputParam;
    }
    public Map getCardTransaction81DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43969");
        inputParam.put("transactionTime", "17:30:10");
        inputParam.put("transactionDescription", "HardRock Café");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "HARDROCK CAFÉ");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Hardrock Café, Marine Drive");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-14.5");
        inputParam.put("transactionReferenceNumber", "125151252156");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS1423623678");
        return inputParam;
    }
    public Map getCardTransaction82DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43970");
        inputParam.put("transactionTime", "07:00:10");
        inputParam.put("transactionDescription", "Amazon US");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "AMAZON");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Online");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Amazon Online");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-25");
        inputParam.put("transactionReferenceNumber", "AM02425514");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "OL94242");
        return inputParam;
    }
    public Map getCardTransaction83DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43970");
        inputParam.put("transactionTime", "18:00:10");
        inputParam.put("transactionDescription", "Mark Hospital");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "MARK HOSPITAL");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Health");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Mark Hospital, Sacramento");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-800");
        inputParam.put("transactionReferenceNumber", "6474262623");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS014166348");
        return inputParam;
    }
    public Map getCardTransaction84DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43971");
        inputParam.put("transactionTime", "10:03:24");
        inputParam.put("transactionDescription", "Cash Withdrawal ATM");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "ACE BANK ATM");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Finance");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Cash WDWL ATM");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-200");
        inputParam.put("transactionReferenceNumber", "353734232");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "ATM2527454848");
        return inputParam;
    }
    public Map getCardTransaction85DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43971");
        inputParam.put("transactionTime", "10:03:24");
        inputParam.put("transactionDescription", "SRVC CHGS ATM 1%");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "ACE BANK ATM");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Finance");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Cash WDWL ATM");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-1.2");
        inputParam.put("transactionReferenceNumber", "353734233");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "ATM2527454848");
        return inputParam;
    }
    public Map getCardTransaction86DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43974");
        inputParam.put("transactionTime", "07:00:10");
        inputParam.put("transactionDescription", "Uber");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "UBER");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Travel");
        inputParam.put("transactionStatus", "P");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Uber Transportation");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-4");
        inputParam.put("transactionReferenceNumber", "5474583232");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "3252362623626");
        return inputParam;
    }
    public Map getCardTransaction87DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43974");
        inputParam.put("transactionTime", "10:03:24");
        inputParam.put("transactionDescription", "Sunny Bee, Clark Ave");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "SUNNY BEE");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Grocery");
        inputParam.put("transactionStatus", "P");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Sunny Bee, Clark Ave");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-11");
        inputParam.put("transactionReferenceNumber", "22362722");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS1453");
        return inputParam;
    }
    public Map getCardTransaction88DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43974");
        inputParam.put("transactionTime", "10:03:25");
        inputParam.put("transactionDescription", "SRVC CHGS");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "SUNNY BEE");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Grocery");
        inputParam.put("transactionStatus", "P");
        inputParam.put("transactionType", "F");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Sunny Bee, Clark Ave");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-0.055");
        inputParam.put("transactionReferenceNumber", "22362724");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "Y");
        inputParam.put("taxPercentage", "0.5");
        inputParam.put("transactionTaxAmount", "0.1");
        inputParam.put("transactionTerminalID", "POS1453");
        return inputParam;
    }
    public Map getCardTransaction89DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43974");
        inputParam.put("transactionTime", "20:15:54");
        inputParam.put("transactionDescription", "Pizza Hut");
        inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "PIZZA HUT");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "P");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Pizza Hut, Clive St");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-8.35");
        inputParam.put("transactionReferenceNumber", "5769742523");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        inputParam.put("taxPercentage", "");
        inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS24221512");
        return inputParam;
    }

    public Map<String, String> getDeposit_MBDeposit4DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Deposit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "12 Months Term Deposit");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "5000");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Principal Deposit");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "");
        inputParam.put("createdDate", "-28");
        inputParam.put("transactionDate", "-28");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

}
