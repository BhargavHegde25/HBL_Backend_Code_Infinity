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

public class DemoDataUSNew {

    private Map<String, String> generatedAccounts;
    private Map<String, String> generatedPayees;
    private Map<String, String> generatedPersons;
    private Map<String, String> generatedBills;
    private Map<String, String> accountTypes;
    private Map<String, String> transactionTypes;
    private Map<String, String> generatedExternalAccounts;
    private String primaryCoreCustomerId;
    private String secondaryCoreCustomerId;
    private String primaryCoreCustomerName;
    private String secondaryCoreCustomerName;
    private String contractId;

    public String getPrimaryCoreCustomerName() {
        return primaryCoreCustomerName;
    }

    public void setPrimaryCoreCustomerName(String primaryCoreCustomerName) {
        this.primaryCoreCustomerName = primaryCoreCustomerName;
    }

    public String getSecondaryCoreCustomerName() {
        return secondaryCoreCustomerName;
    }

    public void setSecondaryCoreCustomerName(String secondaryCoreCustomerName) {
        this.secondaryCoreCustomerName = secondaryCoreCustomerName;
    }

    private String userName;

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

    public Map<String, String> getGeneratedAccounts() {
        return generatedAccounts;
    }

    public void setGeneratedAccounts(Map<String, String> generatedAccounts) {
        this.generatedAccounts = generatedAccounts;
    }

    public Map<String, String> getGeneratedPayees() {
        return generatedPayees;
    }

    public void setGeneratedPayees(Map<String, String> generatedPayees) {
        this.generatedPayees = generatedPayees;
    }

    public Map<String, String> getGeneratedPersons() {
        return generatedPersons;
    }

    public void setGeneratedPersons(Map<String, String> generatedPersons) {
        this.generatedPersons = generatedPersons;
    }

    public Map<String, String> getGeneratedBills() {
        return generatedBills;
    }

    public void setGeneratedBills(Map<String, String> generatedBills) {
        this.generatedBills = generatedBills;
    }

    public Map<String, String> getAccountTypes() {
        return accountTypes;
    }

    public void setAccountTypes(Map<String, String> accountTypes) {
        this.accountTypes = accountTypes;
    }

    public Map<String, String> getTransactionTypes() {
        return transactionTypes;
    }

    public void setTransactionTypes(Map<String, String> transactionTypes) {
        this.transactionTypes = transactionTypes;
    }

    public Map<String, String> getGeneratedExternalAccounts() {
        return generatedExternalAccounts;
    }

    public void setGeneratedExternalAccounts(Map<String, String> generatedExternalAccounts) {
        this.generatedExternalAccounts = generatedExternalAccounts;
    }

    public String getPrimaryCoreCustomerId() {
        return primaryCoreCustomerId;
    }

    public void setPrimaryCoreCustomerId(String primaryCoreCustomerId) {
        this.primaryCoreCustomerId = primaryCoreCustomerId;
    }

    public String getSecondaryCoreCustomerId() {
        return secondaryCoreCustomerId;
    }

    public void setSecondaryCoreCustomerId(String secondaryCoreCustomerId) {
        this.secondaryCoreCustomerId = secondaryCoreCustomerId;
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
        // String filter = "countryCode" + DBPUtilitiesConstants.EQUAL + countryCode;
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

    public Map<String, String> getUserDemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("addressLine1", "7380,");
        inputParam.put("addressLine2", "West Sand Lake Road,");
        inputParam.put("city", "Orlando");
        inputParam.put("state", "Florida");
        inputParam.put("country", "United States of America");
        inputParam.put("zipcode", "32819");
        inputParam.put("userFirstName", "John");
        inputParam.put("userLastName", "Bailey");
        // inputParam.put("userImageURL", "https://retailbanking1.konycloud.com/dbimages/displayPicture.png");
        inputParam.put("secondaryphone", "+91-8729899218");
        inputParam.put("secondaryphone2", "+91-8729899218");
        inputParam.put("secondaryemail2", "johnb@yahoo.com");
        inputParam.put("phone", "+91-4258303691");
        inputParam.put("email", "john.bailey@gmail.com");
        inputParam.put("ssn", "065452564");
        inputParam.put("areUserAlertsTurnedOn", "0");
        inputParam.put("areDepositTermsAccepted", "0");
        inputParam.put("areAccountStatementTermsAccepted", "0");
        inputParam.put("isPhoneEnabled", "0");
        inputParam.put("isBillPaySupported", "1");
        inputParam.put("isP2PSupported", "1");
        inputParam.put("isBillPayActivated", "1");
        inputParam.put("isP2PActivated", "1");
        inputParam.put("isWireTransferEligible", "1");
        inputParam.put("isWireTransferActivated", "1");
        inputParam.put("isEmailEnabled", "1");
        inputParam.put("showBillPayFromAccPopup", "1");
        inputParam.put("lastlogintime", "2018-08-19");
        inputParam.put("dateOfBirth", "1985-05-05");
        return inputParam;
    }

    public Map<String, String> getCustomerDemoData() {
        Map<String, String> inputParam = new HashMap<>();

        inputParam.put("FirstName", "Linda");
        inputParam.put("LastName", "Jones");
        inputParam.put("Ssn", "906545256");
        inputParam.put("areUserAlertsTurnedOn", "0");
        inputParam.put("areDepositTermsAccepted", "0");
        inputParam.put("areAccountStatementTermsAccepted", "0");
        inputParam.put("IsPhoneEnabled", "0");
        inputParam.put("isBillPaySupported", "1");
        inputParam.put("isP2PSupported", "1");
        inputParam.put("isBillPayActivated", "1");
        inputParam.put("isP2PActivated", "1");
        inputParam.put("isWireTransferEligible", "1");
        inputParam.put("isWireTransferActivated", "1");
        inputParam.put("IsEmailEnabled", "1");
        inputParam.put("isEagreementSigned", "false");

        inputParam.put("Lastlogintime", "2018-08-19");
        inputParam.put("DateOfBirth", "1985-05-05");
        return inputParam;
    }

    public Map<String, String> getCustomerCommunicationDemoData() {
        Map<String, String> inputParam = new HashMap<>();

        inputParam.put("Email", "john.bailey@gmail.com");
        inputParam.put("Phone", "+91-4258303691");
        return inputParam;
    }

    public Map<String, String> getCustomerPreferenceDemoData() {
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
        inputParam.put("Region_id", "US-NY");
        inputParam.put("isPreferredAddress", "1");
        inputParam.put("City_id", "CITY1437");
        inputParam.put("cityName", "Santa Clara");
        inputParam.put("addressLine1", "210,Cowper Street");
        inputParam.put("addressLine2", "Palo Alto");
        inputParam.put("zipCode", "94301");
        inputParam.put("country", "Con2");
        inputParam.put("type", "home");
        inputParam.put("state", "state2");
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

    public Map<String, String> getMembershipData() {
        Map<String, String> input = new HashMap<>();
        input.put("id", HelperMethods.getNumericId() + "");
        input.put("Status_id", "SID_CUS_NEW");
        return input;
    }

    public Map<String, String> getMBOrganisationCommunication() {
        Map<String, String> inputParam = new HashMap<>();

        inputParam.put("Email", "michael.davis@gmail.com");
        inputParam.put("Phone", "+91-4258303691");
        return inputParam;
    }

    public Map<String, String> getAdminCustomerDemoData() {
        Map<String, String> inputParam = new HashMap<>();

        inputParam.put("FirstName", "Michael");
        inputParam.put("LastName", "Davis");
        // inputParam.put("UserImageURL", "");

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
        inputParam.put("isEagreementSigned", "0");
        return inputParam;
    }

    public Map<String, String> getAdminCustomerCommunicationDemoData() {
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

    public Map<String, String> getAccount1DemoData() {
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
        inputParam.put("AccountHolder2", "John Doe");
        inputParam.put("adminProductId", "PRODUCT3");
        return inputParam;
    }

    public Map<String, String> getAccount2DemoData() {
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
        inputParam.put("AccountHolder2", "John Doe");
        inputParam.put("adminProductId", "PRODUCT4");
        return inputParam;
    }

    public Map<String, String> getAccount3DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("Type_id", "CreditCard");
        inputParam.put("Name", "Freedom Credit Card");
        inputParam.put("AccountName", "Freedom Credit Card");
        inputParam.put("NickName", "My Credit Card");
        inputParam.put("OpeningDate", "2016-10-10");
        inputParam.put("StatusDesc", "Active");
        inputParam.put("FavouriteStatus", "0");
        inputParam.put("AvailableBalance", "0");
        inputParam.put("CurrentBalance", "3119.61");
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
        inputParam.put("AccountHolder", "John");
        inputParam.put("DueDate", "2019-08-12");
        inputParam.put("RoutingNumber", "648721691");
        inputParam.put("SwiftCode", "CTBAUS124");
        inputParam.put("BsbNum", "123456789");
        inputParam.put("JointHolders", "Jane Bailey");
        inputParam.put("AvailableCredit", "4880.39");
        inputParam.put("MinimumDue", "276");
        inputParam.put("OutstandingBalance", "619.61");
        inputParam.put("CreditCardNumber", "4541982333084990");
        inputParam.put("AvailablePoints", "35");
        inputParam.put("CurrentAmountDue", "3070.64");
        inputParam.put("LastPaymentAmount", "3019.26");
        inputParam.put("LateFeesDue", "10");
        inputParam.put("CreditLimit", "8000");
        inputParam.put("InterestRate", "14.62");
        inputParam.put("PaymentDue", "28");
        inputParam.put("AccountHolder2", "John Doe");
        inputParam.put("adminProductId", "PRODUCT7");
        return inputParam;
    }

    public Map<String, String> getAccount4DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("Type_id", "Deposit");
        inputParam.put("Name", "12 Months Term Deposit");
        inputParam.put("AccountName", "12 Months Term Deposit");
        inputParam.put("NickName", "12 Months Term Deposit");
        inputParam.put("OpeningDate", "2017-11-08");
        inputParam.put("StatusDesc", "Active");
        inputParam.put("FavouriteStatus", "0");
        inputParam.put("AvailableBalance", "0");
        inputParam.put("CurrentBalance", "5018");
        inputParam.put("ShowTransactions", "1");
        inputParam.put("SupportBillPay", "0");
        inputParam.put("SupportDeposit", "0");
        inputParam.put("SupportTransferFrom", "0");
        inputParam.put("SupportTransferTo", "0");
        inputParam.put("SupportCardlessCash", "0");
        inputParam.put("IsPFM", "0");
        inputParam.put("EStatementmentEnable", "0");
        inputParam.put("AccountPreference", "4");
        inputParam.put("CurrencyCode", "USD");
        inputParam.put("AccountHolder", "John");
        inputParam.put("RoutingNumber", "648721691");
        inputParam.put("SwiftCode", "CTBAUS124");
        inputParam.put("BsbNum", "123456789");
        inputParam.put("JointHolders", "Jane Bailey");
        inputParam.put("DividendRate", "1.25");
        inputParam.put("DividendYTD", "18");
        inputParam.put("LastDividendPaidAmount", "6.95");
        inputParam.put("PreviousYearDividend", "70");
        inputParam.put("DividendPaidYTD", "18");
        inputParam.put("DividendLastPaidAmount", "6.95");
        inputParam.put("PreviousYearsDividends", "70");
        inputParam.put("LastDividendPaidDate", "2018-01-08");
        inputParam.put("DividendLastPaidDate", "2018-01-08");
        inputParam.put("AvailablePoints", "0");
        inputParam.put("LateFeesDue", "23");
        inputParam.put("InterestRate", "1.25");
        inputParam.put("MaturityOption", "Renew");
        inputParam.put("InterestEarned", "18");
        inputParam.put("maturityAmount", "5600");
        inputParam.put("MaturityDate", "2018-11-08");
        inputParam.put("PaymentTerm", "4");
        inputParam.put("AccountHolder2", "John Doe");
        inputParam.put("adminProductId", "PRODUCT10");
        return inputParam;
    }

    public Map<String, String> getAccount5DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("Type_id", "Loan");
        inputParam.put("Name", "Turbo Auto Loan");
        inputParam.put("AccountName", "Turbo Auto Loan");
        inputParam.put("NickName", "Turbo Auto Loan");
        inputParam.put("OpeningDate", "2016-08-08");
        inputParam.put("ClosingDate", "2019-08-08");
        inputParam.put("StatusDesc", "Active");
        inputParam.put("FavouriteStatus", "0");
        inputParam.put("AvailableBalance", "0");
        inputParam.put("CurrentBalance", "0");
        inputParam.put("ShowTransactions", "1");
        inputParam.put("SupportBillPay", "0");
        inputParam.put("SupportDeposit", "0");
        inputParam.put("SupportTransferFrom", "0");
        inputParam.put("SupportTransferTo", "1");
        inputParam.put("SupportCardlessCash", "0");
        inputParam.put("IsPFM", "0");
        inputParam.put("EStatementmentEnable", "0");
        inputParam.put("AccountPreference", "3");
        inputParam.put("CurrencyCode", "USD");
        inputParam.put("AccountHolder", "John");
        inputParam.put("RoutingNumber", "648721691");
        inputParam.put("SwiftCode", "CTBAUS124");
        inputParam.put("BsbNum", "123456789");
        inputParam.put("JointHolders", "Jane Bailey");
        inputParam.put("AvailableCredit", "0");
        inputParam.put("DueDate", "2018-08-12");
        inputParam.put("OutstandingBalance", "16760.23");
        inputParam.put("CurrentAmountDue", "736");
        inputParam.put("LastPaymentAmount", "368");
        inputParam.put("LateFeesDue", "11");
        inputParam.put("InterestRate", "2.25");
        inputParam.put("PrincipalValue", "7120");
        inputParam.put("PaymentDue", "13");
        inputParam.put("InterestPaidYTD", "242.30");
        inputParam.put("InterestPaidPreviousYTD", "289");
        inputParam.put("principalBalance", "16024.23");
        inputParam.put("OriginalAmount", "20000");
        inputParam.put("payoffAmount", "16.23");
        inputParam.put("PayOffCharge", "749");
        inputParam.put("InterestPaidLastYear", "289");
        inputParam.put("AccountHolder2", "John Doe");
        inputParam.put("adminProductId", "PRODUCT14");
        return inputParam;
    }

    public Map<String, String> getInvestmentSavingsAccount() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("Type_id", "Savings");
        inputParam.put("AccountType", "Savings");
        inputParam.put("Name", "Investment Savings Account");
        inputParam.put("AccountName", "Investment Savings Account");
        inputParam.put("NickName", "Investment Savings Account");
        inputParam.put("OpeningDate", "2020-10-10");
        inputParam.put("ClosingDate", "");
        inputParam.put("StatusDesc", "Active");
        inputParam.put("FavouriteStatus", "1");
        inputParam.put("AvailableBalance", "30000");
        inputParam.put("CurrentBalance", "25000");
        inputParam.put("PendingDeposit", "0");
        inputParam.put("PendingWithdrawal", "0");
        inputParam.put("LastStatementBalance", "");
        inputParam.put("ShowTransactions", "1");
        inputParam.put("SupportBillPay", "1");
        inputParam.put("SupportDeposit", "1");
        inputParam.put("SupportTransferFrom", "1");
        inputParam.put("SupportTransferTo", "1");
        inputParam.put("SupportCardlessCash", "1");
        inputParam.put("TransactionLimit", "2500");
        inputParam.put("TransferLimit", "5000");
        inputParam.put("IsPFM", "1");
        inputParam.put("EStatementEnable", "0");
        inputParam.put("AccountPreference", "0");
        inputParam.put("CurrencyCode", "USD");
        inputParam.put("AccountHolder", "John");
        inputParam.put("RoutingNumber", "");
        inputParam.put("SwiftCode", "");
        inputParam.put("BsbNum", "");
        inputParam.put("JointHolders", "");
        inputParam.put("DividendRate", "0");
        inputParam.put("DividendYTD", "0");
        inputParam.put("LastDividendPaidAmount", "0");
        inputParam.put("LastDividendPaidDate", "");
        inputParam.put("PreviousYearDividend", "0");
        inputParam.put("DividendPaidYTD", "0");
        inputParam.put("DividendLastPaidAmount", "0");
        inputParam.put("DividendLastPaidDate", "");
        inputParam.put("PreviousYearsDividends", "0");
        inputParam.put("AvailableCredit", "");
        inputParam.put("DueDate", "");
        inputParam.put("MinimumDue", "");
        inputParam.put("OutstandingBalance", "");
        inputParam.put("CreditCardNumber", "");
        inputParam.put("AvailablePoints", "");
        inputParam.put("CurrentAmountDue", "");
        inputParam.put("LastPaymentAmount", "");
        inputParam.put("LateFeesDue", "");
        inputParam.put("CreditLimit", "");
        inputParam.put("InterestRate", "");
        inputParam.put("MaturityDate", "");
        inputParam.put("MaturityOption", "");
        inputParam.put("BondInterest", "");
        inputParam.put("BondInterestLastYear", "");
        inputParam.put("TotalCreditMonths", "");
        inputParam.put("TotalDebitsMonth", "");
        inputParam.put("InterestEarned", "");
        inputParam.put("MaturityAmount", "");
        inputParam.put("PaymentTerm", "");
        inputParam.put("PrincipalValue", "");
        inputParam.put("PaymentDue", "");
        inputParam.put("InterestPaidYTD", "");
        inputParam.put("InterestPaidPreviousYTD", "");
        inputParam.put("UnpaidInterest", "");
        inputParam.put("RegularPaymentAmount", "");
        inputParam.put("PrincipalBalance", "");
        inputParam.put("OriginalAmount", "");
        inputParam.put("PayoffAmount", "");
        inputParam.put("PayOffCharge", "");
        inputParam.put("InterestPaidLastYear", "");
        inputParam.put("BankName", "");
        return inputParam;
    }

    public Map<String, String> getInvestmentCashAccount1() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("Type_id", "Investment");
        inputParam.put("AccountType", "Investment");
        inputParam.put("Name", "Investment Cash Account1");
        inputParam.put("AccountName", "Investment Cash Account1");
        inputParam.put("NickName", "Investment Cash Account1");
        inputParam.put("OpeningDate", "2020-10-10");
        inputParam.put("ClosingDate", "");
        inputParam.put("StatusDesc", "Active");
        inputParam.put("FavouriteStatus", "1");
        inputParam.put("AvailableBalance", "150560.5");
        inputParam.put("CurrentBalance", "150560.45");
        inputParam.put("PendingDeposit", "0");
        inputParam.put("PendingWithdrawal", "0");
        inputParam.put("LastStatementBalance", "");
        inputParam.put("ShowTransactions", "1");
        inputParam.put("SupportBillPay", "1");
        inputParam.put("SupportDeposit", "1");
        inputParam.put("SupportTransferFrom", "1");
        inputParam.put("SupportTransferTo", "1");
        inputParam.put("SupportCardlessCash", "1");
        inputParam.put("TransactionLimit", "50000");
        inputParam.put("TransferLimit", "50000");
        inputParam.put("IsPFM", "1");
        inputParam.put("EStatementEnable", "0");
        inputParam.put("AccountPreference", "0");
        inputParam.put("CurrencyCode", "USD");
        inputParam.put("AccountHolder", "John");
        inputParam.put("RoutingNumber", "");
        inputParam.put("SwiftCode", "");
        inputParam.put("BsbNum", "");
        inputParam.put("JointHolders", "");
        inputParam.put("DividendRate", "0");
        inputParam.put("DividendYTD", "0");
        inputParam.put("LastDividendPaidAmount", "0");
        inputParam.put("LastDividendPaidDate", "");
        inputParam.put("PreviousYearDividend", "0");
        inputParam.put("DividendPaidYTD", "0");
        inputParam.put("DividendLastPaidAmount", "0");
        inputParam.put("DividendLastPaidDate", "");
        inputParam.put("PreviousYearsDividends", "0");
        inputParam.put("AvailableCredit", "");
        inputParam.put("DueDate", "");
        inputParam.put("MinimumDue", "");
        inputParam.put("OutstandingBalance", "");
        inputParam.put("CreditCardNumber", "");
        inputParam.put("AvailablePoints", "");
        inputParam.put("CurrentAmountDue", "");
        inputParam.put("LastPaymentAmount", "");
        inputParam.put("LateFeesDue", "");
        inputParam.put("CreditLimit", "");
        inputParam.put("InterestRate", "");
        inputParam.put("MaturityDate", "");
        inputParam.put("MaturityOption", "");
        inputParam.put("BondInterest", "");
        inputParam.put("BondInterestLastYear", "");
        inputParam.put("TotalCreditMonths", "");
        inputParam.put("TotalDebitsMonth", "");
        inputParam.put("InterestEarned", "");
        inputParam.put("MaturityAmount", "");
        inputParam.put("PaymentTerm", "");
        inputParam.put("PrincipalValue", "");
        inputParam.put("PaymentDue", "");
        inputParam.put("InterestPaidYTD", "");
        inputParam.put("InterestPaidPreviousYTD", "");
        inputParam.put("UnpaidInterest", "");
        inputParam.put("RegularPaymentAmount", "");
        inputParam.put("PrincipalBalance", "");
        inputParam.put("OriginalAmount", "");
        inputParam.put("PayoffAmount", "");
        inputParam.put("PayOffCharge", "");
        inputParam.put("InterestPaidLastYear", "");
        inputParam.put("BankName", "");
        return inputParam;
    }

    public Map<String, String> getInvestmentCashAccount2() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("Type_id", "Investment");
        inputParam.put("AccountType", "Investment");
        inputParam.put("Name", "Investment Cash Account2");
        inputParam.put("AccountName", "Investment Cash Account2");
        inputParam.put("NickName", "Investment Cash Account2");
        inputParam.put("OpeningDate", "2020-10-10");
        inputParam.put("ClosingDate", "");
        inputParam.put("StatusDesc", "Active");
        inputParam.put("FavouriteStatus", "1");
        inputParam.put("AvailableBalance", "140000.6");
        inputParam.put("CurrentBalance", "120560.75");
        inputParam.put("PendingDeposit", "0");
        inputParam.put("PendingWithdrawal", "0");
        inputParam.put("LastStatementBalance", "");
        inputParam.put("ShowTransactions", "1");
        inputParam.put("SupportBillPay", "1");
        inputParam.put("SupportDeposit", "1");
        inputParam.put("SupportTransferFrom", "1");
        inputParam.put("SupportTransferTo", "1");
        inputParam.put("SupportCardlessCash", "1");
        inputParam.put("TransactionLimit", "50000");
        inputParam.put("TransferLimit", "50000");
        inputParam.put("IsPFM", "1");
        inputParam.put("EStatementEnable", "0");
        inputParam.put("AccountPreference", "0");
        inputParam.put("CurrencyCode", "USD");
        inputParam.put("AccountHolder", "John");
        inputParam.put("RoutingNumber", "");
        inputParam.put("SwiftCode", "");
        inputParam.put("BsbNum", "");
        inputParam.put("JointHolders", "");
        inputParam.put("DividendRate", "0");
        inputParam.put("DividendYTD", "0");
        inputParam.put("LastDividendPaidAmount", "0");
        inputParam.put("LastDividendPaidDate", "");
        inputParam.put("PreviousYearDividend", "0");
        inputParam.put("DividendPaidYTD", "0");
        inputParam.put("DividendLastPaidAmount", "0");
        inputParam.put("DividendLastPaidDate", "");
        inputParam.put("PreviousYearsDividends", "0");
        inputParam.put("AvailableCredit", "");
        inputParam.put("DueDate", "");
        inputParam.put("MinimumDue", "");
        inputParam.put("OutstandingBalance", "");
        inputParam.put("CreditCardNumber", "");
        inputParam.put("AvailablePoints", "");
        inputParam.put("CurrentAmountDue", "");
        inputParam.put("LastPaymentAmount", "");
        inputParam.put("LateFeesDue", "");
        inputParam.put("CreditLimit", "");
        inputParam.put("InterestRate", "");
        inputParam.put("MaturityDate", "");
        inputParam.put("MaturityOption", "");
        inputParam.put("BondInterest", "");
        inputParam.put("BondInterestLastYear", "");
        inputParam.put("TotalCreditMonths", "");
        inputParam.put("TotalDebitsMonth", "");
        inputParam.put("InterestEarned", "");
        inputParam.put("MaturityAmount", "");
        inputParam.put("PaymentTerm", "");
        inputParam.put("PrincipalValue", "");
        inputParam.put("PaymentDue", "");
        inputParam.put("InterestPaidYTD", "");
        inputParam.put("InterestPaidPreviousYTD", "");
        inputParam.put("UnpaidInterest", "");
        inputParam.put("RegularPaymentAmount", "");
        inputParam.put("PrincipalBalance", "");
        inputParam.put("OriginalAmount", "");
        inputParam.put("PayoffAmount", "");
        inputParam.put("PayOffCharge", "");
        inputParam.put("InterestPaidLastYear", "");
        inputParam.put("BankName", "");
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
        inputParam.put("phone", "+91-4523746832");
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
        inputParam.put("expirationDate", "2025-06-12");
        inputParam.put("pinNumber", "1578");
        inputParam.put("withdrawlLimit", "214");
        inputParam.put("reason", "");
        inputParam.put("cardHolderName", "John Bailey");
        inputParam.put("secondaryCardHolder", "John Dane");
        inputParam.put("isInternational", "1");
        inputParam.put("currentBalance", "1000");
        inputParam.put("availableBalance", "850");
        inputParam.put("rewardsPoint", "250");
        inputParam.put("cvv", "450");
        return inputParam;
    }

    public Map<String, String> getCard2DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("action", "Activate");
        inputParam.put("card_Status", "Issued");
        inputParam.put("availableCredit", "12380");
        inputParam.put("serviceProvider", "Master Card");
        inputParam.put("billingAddress", "2076, Burnett Ln, Austin, Texas, 78791");
        inputParam.put("cardProductName", "Eazee Food Card");
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("expirationDate", "2025-06-11");
        inputParam.put("pinNumber", "1579");
        inputParam.put("withdrawlLimit", "524");
        inputParam.put("reason", "");
        inputParam.put("cardHolderName", "John Bailey");
        inputParam.put("secondaryCardHolder", "John Doe");
        inputParam.put("currentBalance", "3400");
        inputParam.put("availableBalance", "3150");
        inputParam.put("rewardsPoint", "250");
        inputParam.put("cvv", "800");
        return inputParam;
    }

    public Map<String, String> getCard3DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("action", "Activate");
        inputParam.put("card_Status", "Active");
        inputParam.put("availableCredit", "8513");
        inputParam.put("serviceProvider", "Master Card");
        inputParam.put("billingAddress", "2076, Burnett Ln, Austin, Texas, 78791");
        inputParam.put("cardProductName", "My Platinum Credit Card");
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("expirationDate", "2025-06-15");
        inputParam.put("pinNumber", "1580");
        inputParam.put("creditLimit", "20000");
        inputParam.put("reason", "");
        inputParam.put("cardHolderName", "John Bailey");
        inputParam.put("secondaryCardHolder", "John Doe");
        inputParam.put("rewardsPoint", "550");
        inputParam.put("cvv", "990");
        return inputParam;
    }

    public Map<String, String> getCard4DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("action", "Activate");
        inputParam.put("card_Status", "Active");
        inputParam.put("availableCredit", "8513");
        inputParam.put("serviceProvider", "Master Card");
        inputParam.put("billingAddress", "2076, Burnett Ln, Austin, Texas, 78792");
        inputParam.put("cardProductName", "My Platinum Credit Card");
        inputParam.put("cardNumber", "5314000000004360");
        inputParam.put("cardType", "Credit");
        inputParam.put("expirationDate", "2020-06-15");
        inputParam.put("pinNumber", "1580");
        inputParam.put("creditLimit", "20000");
        inputParam.put("reason", "");
        inputParam.put("cardHolderName", "John Bailey");
        inputParam.put("secondaryCardHolder", "John Doe");
        inputParam.put("rewardsPoint", "550");
        inputParam.put("cvv", "360");
        return inputParam;
    }

    public Map<String, String> getCard5DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("action", "Activate");
        inputParam.put("card_Status", "Issued");
        inputParam.put("availableCredit", "8513");
        inputParam.put("serviceProvider", "Master Card");
        inputParam.put("billingAddress", "2076, Burnett Ln, Austin, Texas, 78792");
        inputParam.put("cardProductName", "My Platinum Credit Card");
        inputParam.put("cardNumber", "5314000000004360");
        inputParam.put("cardType", "Credit");
        inputParam.put("expirationDate", "2025-06-15");
        inputParam.put("pinNumber", "1580");
        inputParam.put("creditLimit", "20000");
        inputParam.put("reason", "");
        inputParam.put("cardHolderName", "John Bailey");
        inputParam.put("secondaryCardHolder", "John Doe");
        inputParam.put("rewardsPoint", "550");
        inputParam.put("cvv", "360");
        return inputParam;
    }

    public Map<String, String> getCard6DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("action", "Activate");
        inputParam.put("card_Status", "Issued");
        inputParam.put("availableCredit", "87161.00");
        inputParam.put("serviceProvider", "Master Card");
        inputParam.put("billingAddress", "2076, Burnett Ln, Austin, Texas, 78790");
        inputParam.put("cardProductName", "Gold Debit Card");
        inputParam.put("cardNumber", "5314000000004380");
        inputParam.put("cardType", "Debit");
        inputParam.put("expirationDate", "2025-08-12");
        inputParam.put("pinNumber", "1572");
        inputParam.put("withdrawlLimit", "1000");
        inputParam.put("reason", "");
        inputParam.put("cardHolderName", "John Bailey");
        inputParam.put("secondaryCardHolder", "John Dane");
        inputParam.put("isInternational", "1");
        inputParam.put("currentBalance", "");
        inputParam.put("availableBalance", "");
        inputParam.put("cvv", "380");
        inputParam.put("rewardsPoint", "0");
        return inputParam;
    }

    public Map<String, String> getCard7DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("action", "Activate");
        inputParam.put("card_Status", "Issued");
        inputParam.put("availableCredit", "3200");
        inputParam.put("serviceProvider", "Visa");
        inputParam.put("billingAddress", "2076, Burnett Ln, Austin, Texas, 78792");
        inputParam.put("cardProductName", "Freedom Credit Card");
        inputParam.put("cardNumber", "4259000000003210");
        inputParam.put("cardType", "Credit");
        inputParam.put("expirationDate", "2025-06-15");
        inputParam.put("pinNumber", "1480");
        inputParam.put("creditLimit", "3200");
        inputParam.put("reason", "");
        inputParam.put("cardHolderName", "John Bailey");
        inputParam.put("secondaryCardHolder", "John Doe");
        inputParam.put("cvv", "210");
        inputParam.put("rewardsPoint", "0");
        return inputParam;
    }

    public Map<String, String> getCard8DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("action", "Activate");
        inputParam.put("card_Status", "Active");
        inputParam.put("availableCredit", "12380");
        inputParam.put("serviceProvider", "Master Card");
        inputParam.put("billingAddress", "2076, Burnett Ln, Austin, Texas, 78791");
        inputParam.put("cardProductName", "Eazee Food Card");
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("expirationDate", "2020-06-15");
        inputParam.put("pinNumber", "1579");
        inputParam.put("withdrawlLimit", "524");
        inputParam.put("reason", "");
        inputParam.put("cardHolderName", "John Bailey");
        inputParam.put("secondaryCardHolder", "John Doe");
        inputParam.put("currentBalance", "3400");
        inputParam.put("availableBalance", "3150");
        inputParam.put("rewardsPoint", "250");
        inputParam.put("cvv", "800");
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

    public Map<String, String> getUserBill1DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("billDueDate", "2025-05-18");
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
        inputParam.put("billDueDate", "2025-09-21");
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

    public Map<String, String> getInternalTransfer_SAV1DemoData() {
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

    public Map<String, String> getCheckDeposit_SAV2DemoData() {
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

    public Map<String, String> getExternalTransfer_SAV3DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "ExternalTransfer");
        inputParam.put("fromAccountNumber", "Rewards Savings");
        inputParam.put("toAccountNumber", "Alex Sion");
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

    public Map<String, String> getRemoteCheckDeposit_SAV4DemoData() {
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

    public Map<String, String> getInternalTransfer_SAV5DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternalTransfer");
        inputParam.put("fromAccountNumber", "Rewards Savings");
        inputParam.put("toAccountNumber", "");
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

    public Map<String, String> getInterest_SAV6DemoData() {
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

    public Map<String, String> getRemoteCheckDeposit_SAV7DemoData() {
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

    public Map<String, String> getCredit_SAV8DemoData() {
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

    public Map<String, String> getFee_SAV9DemoData() {
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

    public Map<String, String> getInternalTransfer_SAV10DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternalTransfer");
        inputParam.put("fromAccountNumber", "Rewards Savings");
        inputParam.put("toAccountNumber", "");
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

    public Map<String, String> getExternalTransfer_SAV11DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "ExternalTransfer");
        inputParam.put("fromAccountNumber", "Rewards Savings");
        inputParam.put("toAccountNumber", "John Bailey");
        inputParam.put("toExternalAccountNumber", "John Bailey");
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

    public Map<String, String> getLoan_SAV12DemoData() {
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

    public Map<String, String> getCardlessWithdrawl_SAVDemoData() {
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

    public Map<String, String> getInterestCredit_Deposit1DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Interest");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "12 Months Term Deposit");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "6.95");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Interest on Term Deposit");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "5018.00");
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

    public Map<String, String> getInterestCredit_Deposit2DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Interest");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "12 Months Term Deposit");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "5.80");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Interest on Term Deposit");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "5011.05");
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

    public Map<String, String> getInterestCredit_Deposit3DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Interest");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "12 Months Term Deposit");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "5.25");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Interest on Term Deposit");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "5005.25");
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

    public Map<String, String> getDeposit_Deposit4DemoData() {
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
        inputParam.put("fromAccountBalance", "5000");
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

    public Map<String, String> getLoanPayment_Loan1DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Loan");
        inputParam.put("toAccountNumber", "Turbo Auto Loan");
        inputParam.put("amount", "368");
        inputParam.put("description", "Payment");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("fromAccountBalance", "16024.23");

        return inputParam;
    }

    public Map<String, String> getLoanPayment_Loan2DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Loan");
        inputParam.put("toAccountNumber", "Turbo Auto Loan");
        inputParam.put("amount", "368");
        inputParam.put("description", "Payment");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("createdDate", "-37");
        inputParam.put("transactionDate", "-37");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("fromAccountBalance", "16361.55");
        return inputParam;
    }

    public Map<String, String> getLoanPayment_Loan3DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Loan");
        inputParam.put("toAccountNumber", "Turbo Auto Loan");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "368");
        inputParam.put("description", "Payment");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("createdDate", "-67");
        inputParam.put("transactionDate", "-67");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("fromAccountBalance", "16698.24");
        return inputParam;
    }

    public Map<String, String> getTax_Loan4DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Tax");
        inputParam.put("toAccountNumber", "Turbo Auto Loan");
        inputParam.put("description", "County tax");
        inputParam.put("amount", "10");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("createdDate", "-97");
        inputParam.put("transactionDate", "-97");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("fromAccountBalance", "17034.30");
        return inputParam;
    }

    public Map<String, String> getLoanPayment_Loan5DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Loan");
        inputParam.put("toAccountNumber", "Turbo Auto Loan");
        inputParam.put("amount", "368");
        inputParam.put("description", "Payment");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("createdDate", "-97");
        inputParam.put("transactionDate", "-97");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("fromAccountBalance", "17034.30");
        return inputParam;
    }

    public Map<String, String> getLoanPayment_Loan6DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Loan");
        inputParam.put("toAccountNumber", "Turbo Auto Loan");
        inputParam.put("amount", "368");
        inputParam.put("description", "Payment");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("createdDate", "-127");
        inputParam.put("transactionDate", "-127");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("fromAccountBalance", "17369.73");
        return inputParam;
    }

    public Map<String, String> getLoanPayment_Loan7DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Loan");
        inputParam.put("toAccountNumber", "Turbo Auto Loan");
        inputParam.put("amount", "368");
        inputParam.put("description", "Payment");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("createdDate", "-157");
        inputParam.put("transactionDate", "-157");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("fromAccountBalance", "17704.54");
        return inputParam;
    }

    public Map<String, String> getLoanPayment_Loan8DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Loan");
        inputParam.put("toAccountNumber", "Turbo Auto Loan");
        inputParam.put("amount", "368");
        inputParam.put("description", "Payment");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("createdDate", "-187");
        inputParam.put("transactionDate", "-187");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("fromAccountBalance", "18038.72");
        return inputParam;
    }

    public Map<String, String> getInvestmentTransaction1() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternalTransfer");
        inputParam.put("fromAccountNumber", "Investment Savings Account");
        inputParam.put("toAccountNumber", "Investment Cash Account1");
        inputParam.put("amount", "10800");
        inputParam.put("description", "Online transfer from Savings account to Investment cash account");
        inputParam.put("notes", "Funding for Investment");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    public Map<String, String> getInvestmentTransaction2() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternalTransfer");
        inputParam.put("fromAccountNumber", "Investment Cash Account2");
        inputParam.put("toAccountNumber", "Investment Savings Account");
        inputParam.put("amount", "25000.45");
        inputParam.put("description", "Online transfer from Investment cash account to savings account");
        inputParam.put("notes", "Withdrawal of Funds from Investment cash account");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-8");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    public Map<String, String> getLoanPayment_Loan9DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Loan");
        inputParam.put("toAccountNumber", "Turbo Auto Loan");
        inputParam.put("amount", "368");
        inputParam.put("description", "Payment");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("createdDate", "-217");
        inputParam.put("transactionDate", "-217");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("fromAccountBalance", "18372.27");
        return inputParam;
    }

    public Map<String, String> getLoanPayment_Loan10DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Loan");
        inputParam.put("toAccountNumber", "Turbo Auto Loan");
        inputParam.put("amount", "368");
        inputParam.put("description", "Payment");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("createdDate", "-247");
        inputParam.put("transactionDate", "-247");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("fromAccountBalance", "18705.20");
        return inputParam;
    }

    public Map<String, String> getLoanPayment_Loan11DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Loan");
        inputParam.put("toAccountNumber", "Turbo Auto Loan");
        inputParam.put("amount", "1000");
        inputParam.put("description", "Principal Payment");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("createdDate", "-277");
        inputParam.put("transactionDate", "-277");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("fromAccountBalance", "19037.50");
        return inputParam;
    }

    public Map<String, String> getCredit_Loan12DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("toAccountNumber", "Turbo Auto Loan");
        inputParam.put("amount", "20000");
        inputParam.put("description", "New loan setup");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("createdDate", "-317");
        inputParam.put("transactionDate", "-317");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("fromAccountBalance", "20000");
        return inputParam;
    }

    public Map<String, String> getPOS_CC1DemoData() {
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

    public Map<String, String> getInternalTransfer_CC2DemoData() {
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
        inputParam.put("fromAccountBalance", "570.64");
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

    public Map<String, String> getPOS_CC3DemoData() {
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

    public Map<String, String> getInternetTransaction_CC4DemoData() {
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
        inputParam.put("fromAccountBalance", "3036.43");
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

    public Map<String, String> getBillPay_CC5DemoData() {
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
        inputParam.put("fromAccountBalance", "3117.34");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
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

    public Map<String, String> getPOS_CC6DemoData() {
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
        inputParam.put("fromAccountBalance", "2989.36");
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

    public Map<String, String> getCardPayment_CC7DemoData() {
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
        inputParam.put("fromAccountBalance", "2921.17");
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

    public Map<String, String> getPOS_CC8DemoData() {
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
        inputParam.put("fromAccountBalance", "7921.17");
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

    public Map<String, String> getPOS_CC9DemoData() {
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
        inputParam.put("fromAccountBalance", "7902.98");
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

    public Map<String, String> getPOS_CC10DemoData() {
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
        inputParam.put("fromAccountBalance", "7707.95");
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

    public Map<String, String> getInternetTransaction_CC11DemoData() {
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
        inputParam.put("fromAccountBalance", "7632.04");
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

    public Map<String, String> getInternetTransaction_CC12DemoData() {
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
        inputParam.put("fromAccountBalance", "7594.85");
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

    public Map<String, String> getPOS_CC13DemoData() {
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
        inputParam.put("fromAccountBalance", "7556.81");
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

    public Map<String, String> getCredit_CC14DemoData() {
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
        inputParam.put("fromAccountBalance", "7522.61");
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

    public Map<String, String> getBillPay_CC15DemoData() {
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
        inputParam.put("fromAccountBalance", "7603.51");
        inputParam.put("createdDate", "-9");
        inputParam.put("transactionDate", "-9");
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

    public Map<String, String> getPOS_CC16DemoData() {
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
        inputParam.put("fromAccountBalance", "7475.53");
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

    public Map<String, String> getInternetTransaction_CC17DemoData() {
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
        inputParam.put("fromAccountBalance", "7407.34");
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

    public Map<String, String> getPOS_CC18DemoData() {
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
        inputParam.put("fromAccountBalance", "7287.22");
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

    public Map<String, String> getPOS_CC19DemoData() {
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
        inputParam.put("fromAccountBalance", "7269.03");
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

    public Map<String, String> getPOS_CC20DemoData() {
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
        inputParam.put("fromAccountBalance", "7074.11");
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

    public Map<String, String> getInternetTransaction_CC21DemoData() {
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
        inputParam.put("fromAccountBalance", "6998.09");
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

    public Map<String, String> getInternetTransaction_CC22DemoData() {
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
        inputParam.put("fromAccountBalance", "6960.91");
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

    public Map<String, String> getPOS_CC23DemoData() {
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
        inputParam.put("fromAccountBalance", "6922.86");
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

    public Map<String, String> getCredit_CC24DemoData() {
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
        inputParam.put("fromAccountBalance", "6888.65");
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

    public Map<String, String> getBillPay_CC25DemoData() {
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
        inputParam.put("fromAccountBalance", "6969.56");
        inputParam.put("createdDate", "-11");
        inputParam.put("transactionDate", "-11");
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

    public Map<String, String> getPOS_CC26DemoData() {
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
        inputParam.put("fromAccountBalance", "6841.58");
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

    public Map<String, String> getInternetTransaction_CC27DemoData() {
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
        inputParam.put("fromAccountBalance", "6773.39");
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

    public Map<String, String> getPOS_CC28DemoData() {
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
        inputParam.put("fromAccountBalance", "6653.27");
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

    public Map<String, String> getPOS_CC29DemoData() {
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
        inputParam.put("fromAccountBalance", "6635.08");
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

    public Map<String, String> getPOS_CC30DemoData() {
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
        inputParam.put("fromAccountBalance", "6440.05");
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

    public Map<String, String> getPOS_CC31DemoData() {
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
        inputParam.put("fromAccountBalance", "6364.14");
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

    public Map<String, String> getInternetTransaction_CC32DemoData() {
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
        inputParam.put("fromAccountBalance", "6295.95");
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

    public Map<String, String> getPOS_CC33DemoData() {
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
        inputParam.put("fromAccountBalance", "6175.83");
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

    public Map<String, String> getPOS_CC34DemoData() {
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
        inputParam.put("fromAccountBalance", "6157.64");
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

    public Map<String, String> getPOS_CC35DemoData() {
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
        inputParam.put("fromAccountBalance", "5962.61");
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

    public Map<String, String> getInternetTransaction_CC36DemoData() {
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
        inputParam.put("fromAccountBalance", "5886.70");
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

    public Map<String, String> getInternetTransaction_CC37DemoData() {
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
        inputParam.put("fromAccountBalance", "5849.51");
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

    public Map<String, String> getPOS_CC38DemoData() {
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
        inputParam.put("fromAccountBalance", "5811.47");
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

    public Map<String, String> getCredit_CC39DemoData() {
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
        inputParam.put("fromAccountBalance", "5777.26");
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

    public Map<String, String> getBillPay_CC40DemoData() {
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
        inputParam.put("fromAccountBalance", "5858.17");
        inputParam.put("createdDate", "-14");
        inputParam.put("transactionDate", "-14");
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

    public Map<String, String> getPOS_CC41DemoData() {
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

    public Map<String, String> getInternetTransaction_CC42DemoData() {
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

    public Map<String, String> getPOS_CC43DemoData() {
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

    public Map<String, String> getPOS_CC44DemoData() {
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

    public Map<String, String> getPOS_CC45DemoData() {
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

    public Map<String, String> getInternetTransaction_CC46DemoData() {
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

    public Map<String, String> getInternetTransaction_CC47DemoData() {
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

    public Map<String, String> getPOS_CC48DemoData() {
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

    public Map<String, String> getCredit_CC49DemoData() {
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

    public Map<String, String> getBillPay_CC50DemoData() {
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
        inputParam.put("Payee_id", "payee1");
        inputParam.put("Bill_id", "bill2");
        return inputParam;
    }

    public Map<String, String> getPOS_CC51DemoData() {
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

    public Map<String, String> getInternetTransaction_CC52DemoData() {
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

    public Map<String, String> getPOS_CC53DemoData() {
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

    public Map<String, String> getPOS_CC54DemoData() {
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

    public Map<String, String> getPOS_CC55DemoData() {
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

    public Map<String, String> getInternetTransaction_CC56DemoData() {
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
        inputParam.put("fromAccountBalance", "4618.81");
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

    public Map<String, String> getInternetTransaction_CC57DemoData() {
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

    public Map<String, String> getPOS_CC58DemoData() {
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

    public Map<String, String> getCredit_CC59DemoData() {
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

    public Map<String, String> getBillPay_CC60DemoData() {
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
        inputParam.put("Payee_id", "payee1");
        inputParam.put("Bill_id", "bill2");
        return inputParam;
    }

    public Map<String, String> getPOS_CC61DemoData() {
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

    public Map<String, String> getInternetTransaction_CC62DemoData() {
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
        inputParam.put("fromAccountBalance", "4394.12");
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

    public Map<String, String> getPOS_CC63DemoData() {
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

    public Map<String, String> getPOS_CC64DemoData() {
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

    public Map<String, String> getPOS_CC65DemoData() {
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

    public Map<String, String> getInternetTransaction_CC66DemoData() {
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

    public Map<String, String> getInternetTransaction_CC67DemoData() {
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

    public Map<String, String> getPOS_CC68DemoData() {
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

    public Map<String, String> getCredit_CC69DemoData() {
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

    public Map<String, String> getBillPay_CC70DemoData() {
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
        inputParam.put("Payee_id", "payee1");
        inputParam.put("Bill_id", "bill2");
        return inputParam;
    }

    public Map<String, String> getPOS_CC71DemoData() {
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

    public Map<String, String> getInternetTransaction_CC72DemoData() {
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

    public Map<String, String> getPOS_CC73DemoData() {
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

    public Map<String, String> getPOS_CC74DemoData() {
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

    public Map<String, String> getPOS_CC75DemoData() {
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

    public Map<String, String> getCredit_CC76DemoData() {
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
        inputParam.put("fromAccountBalance", "3350.91");
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

    public Map<String, String> getBillPay_CC77DemoData() {
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
        inputParam.put("Payee_id", "payee1");
        inputParam.put("Bill_id", "bill2");
        return inputParam;
    }

    public Map<String, String> getPOS_CC78DemoData() {
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

    public Map<String, String> getInternetTransaction_CC79DemoData() {
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

    public Map<String, String> getPOS_CC80DemoData() {
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

    public Map<String, String> getPOS_CC81DemoData() {
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

    public Map<String, String> getPOS_CC82DemoData() {
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
        inputParam.put("fromAccountBalance", "2902.31");
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

    public Map<String, String> getPOS_CC83DemoData() {
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

    public Map<String, String> getInternetTransaction_CC84DemoData() {
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
        inputParam.put("fromAccountBalance", "2758.21");
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

    public Map<String, String> getPOS_CC85DemoData() {
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

    public Map<String, String> getPOS_CC86DemoData() {
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

    public Map<String, String> getPOS_CC87DemoData() {
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

    public Map<String, String> getInternetTransaction_CC88DemoData() {
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

    public Map<String, String> getInternetTransaction_CC89DemoData() {
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

    public Map<String, String> getPOS_CC90DemoData() {
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

    public Map<String, String> getCredit_CC91DemoData() {
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

    public Map<String, String> getBillPay_CC92DemoData() {
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
        inputParam.put("Payee_id", "payee1");
        inputParam.put("Bill_id", "bill2");
        return inputParam;
    }

    public Map<String, String> getPOS_CC93DemoData() {
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

    public Map<String, String> getInternetTransaction_CC94DemoData() {
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

    public Map<String, String> getPOS_CC95DemoData() {
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

    public Map<String, String> getPOS_CC96DemoData() {
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

    public Map<String, String> getPOS_CC97DemoData() {
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

    public Map<String, String> getInternetTransaction_CC98DemoData() {
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

    public Map<String, String> getInternetTransaction_CC99DemoData() {
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

    public Map<String, String> getPOS_CC100DemoData() {
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

    public Map<String, String> getCredit_CC101DemoData() {
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

    public Map<String, String> getBillPay_CC102DemoData() {
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
        inputParam.put("Payee_id", "payee1");
        inputParam.put("Bill_id", "bill2");
        return inputParam;
    }

    public Map<String, String> getPOS_CC103DemoData() {
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

    public Map<String, String> getInternetTransaction_CC104DemoData() {
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
        inputParam.put("fromAccountBalance", "1490.31");
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

    public Map<String, String> getPOS_CC105DemoData() {
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

    public Map<String, String> getPOS_CC106DemoData() {
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

    public Map<String, String> getPOS_CC107DemoData() {
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

    public Map<String, String> getInternetTransaction_CC108DemoData() {
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

    public Map<String, String> getInternetTransaction_CC109DemoData() {
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

    public Map<String, String> getPOS_CC110DemoData() {
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

    public Map<String, String> getCredit_CC111DemoData() {
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
        inputParam.put("fromAccountBalance", "971.61");
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

    public Map<String, String> getBillPay_CC112DemoData() {
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
        inputParam.put("Payee_id", "payee1");
        inputParam.put("Bill_id", "bill2");
        return inputParam;
    }

    public Map<String, String> getPOS_CC113DemoData() {
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
        inputParam.put("fromAccountBalance", "924.54");
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

    public Map<String, String> getInternetTransaction_CC114DemoData() {
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
        inputParam.put("fromAccountBalance", "856.35");
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

    public Map<String, String> getPOS_CC115DemoData() {
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
        inputParam.put("fromAccountBalance", "736.23");
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

    public Map<String, String> getPOS_CC116DemoData() {
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
        inputParam.put("fromAccountBalance", "718.04");
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

    public Map<String, String> getPOS_CC117DemoData() {
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
        inputParam.put("fromAccountBalance", "523.01");
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

    public Map<String, String> getInternetTransaction_CC118DemoData() {
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
        inputParam.put("fromAccountBalance", "447.01");
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

    public Map<String, String> getInternetTransaction_CC119DemoData() {
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
        inputParam.put("fromAccountBalance", "409.91");
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

    public Map<String, String> getPOS_CC120DemoData() {
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
        inputParam.put("fromAccountBalance", "371.87");
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

    public Map<String, String> getCredit_CC121DemoData() {
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
        inputParam.put("fromAccountBalance", "337.66");
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

    public Map<String, String> getBillPay_CC122DemoData() {
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
        inputParam.put("fromAccountBalance", "418.57");
        inputParam.put("createdDate", "-28");
        inputParam.put("transactionDate", "-28");
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

    public Map<String, String> getPOS_CC123DemoData() {
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
        inputParam.put("fromAccountBalance", "290.59");
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

    public Map<String, String> getInternetTransaction_CC124DemoData() {
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
        inputParam.put("fromAccountBalance", "222.40");
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

    public Map<String, String> getPOS_CC125DemoData() {
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
        inputParam.put("fromAccountBalance", "102.28");
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

    public Map<String, String> getInternetTransaction_CC126DemoData() {
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
        inputParam.put("fromAccountBalance", "84.09");
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

    public Map<String, String> getCardPayment_CC127DemoData() {
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
        inputParam.put("fromAccountBalance", "46.91");
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

    public Map<String, String> getPOS_CC128DemoData() {
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
        inputParam.put("fromAccountBalance", "4846.91");
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

    public Map<String, String> getCredit_CC129DemoData() {
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

    public Map<String, String> getBillPay_CC130DemoData() {
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
        inputParam.put("fromAccountBalance", "4893.61");
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

    public Map<String, String> getPOS_CC131DemoData() {
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

    public Map<String, String> getInternetTransaction_CC132DemoData() {
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

    public Map<String, String> getPOS_CC133DemoData() {
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

    public Map<String, String> getPOS_CC134DemoData() {
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

    public Map<String, String> getPOS_CC135DemoData() {
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

    public Map<String, String> getInternetTransaction_CC136DemoData() {
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

    public Map<String, String> getInternetTransaction_CC137DemoData() {
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

    public Map<String, String> getPOS_CC138DemoData() {
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

    public Map<String, String> getCredit_CC139DemoData() {
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

    public Map<String, String> getBillPay_CC140DemoData() {
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
        inputParam.put("Payee_id", "payee1");
        inputParam.put("Bill_id", "bill2");
        return inputParam;
    }

    public Map<String, String> getPOS_CC141DemoData() {
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

    public Map<String, String> getInternetTransaction_CC142DemoData() {
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

    public Map<String, String> getPOS_CC143DemoData() {
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

    public Map<String, String> getPOS_CC144DemoData() {
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

    public Map<String, String> getPOS_CC145DemoData() {
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

    public Map<String, String> getInternetTransaction_CC146DemoData() {
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

    public Map<String, String> getInternetTransaction_CC147DemoData() {
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

    public Map<String, String> getPOS_CC148DemoData() {
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

    public Map<String, String> getCredit_CC149DemoData() {
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

    public Map<String, String> getBillPay_CC150DemoData() {
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
        inputParam.put("fromAccountBalance", "3625.71");
        inputParam.put("createdDate", "-33");
        inputParam.put("transactionDate", "-33");
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

    public Map<String, String> getPOS_CC151DemoData() {
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

    public Map<String, String> getInternetTransaction_CC152DemoData() {
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

    public Map<String, String> getPOS_CC153DemoData() {
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

    public Map<String, String> getPOS_CC154DemoData() {
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

    public Map<String, String> getPOS_CC155DemoData() {
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

    public Map<String, String> getInternetTransaction_CC156DemoData() {
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

    public Map<String, String> getInternetTransaction_CC157DemoData() {
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

    public Map<String, String> getPOS_CC158DemoData() {
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

    public Map<String, String> getCredit_CC159DemoData() {
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

    public Map<String, String> getBillPay_CC160DemoData() {
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
        inputParam.put("Payee_id", "payee1");
        inputParam.put("Bill_id", "bill2");
        return inputParam;
    }

    public Map<String, String> getPOS_CC161DemoData() {
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

    public Map<String, String> getInternetTransaction_CC162DemoData() {
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

    public Map<String, String> getPOS_CC163DemoData() {
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

    public Map<String, String> getPOS_CC164DemoData() {
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

    public Map<String, String> getPOS_CC165DemoData() {
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

    public Map<String, String> getInternetTransaction_CC166DemoData() {
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

    public Map<String, String> getInternetTransaction_CC167DemoData() {
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

    public Map<String, String> getPOS_CC168DemoData() {
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
        inputParam.put("fromAccountBalance", "2311.12");
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

    public Map<String, String> getCredit_CC169DemoData() {
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

    public Map<String, String> getBillPay_CC170DemoData() {
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
        inputParam.put("fromAccountBalance", "2357.81");
        inputParam.put("createdDate", "-36");
        inputParam.put("transactionDate", "-36");
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

    public Map<String, String> getPOS_CC171DemoData() {
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

    public Map<String, String> getInternetTransaction_CC172DemoData() {
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

    public Map<String, String> getPOS_CC173DemoData() {
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

    public Map<String, String> getPOS_CC174DemoData() {
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

    public Map<String, String> getPOS_CC175DemoData() {
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

    public Map<String, String> getInternetTransaction_CC176DemoData() {
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

    public Map<String, String> getInternetTransaction_CC177DemoData() {
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

    public Map<String, String> getPOS_CC178DemoData() {
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

    public Map<String, String> getCredit_CC179DemoData() {
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

    public Map<String, String> getBillPay_CC180DemoData() {
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
        inputParam.put("Payee_id", "payee1");
        inputParam.put("Bill_id", "bill2");
        return inputParam;
    }

    public Map<String, String> getPOS_CC181DemoData() {
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

    public Map<String, String> getInternetTransaction_CC182DemoData() {
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

    public Map<String, String> getPOS_CC183DemoData() {
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

    public Map<String, String> getPOS_CC184DemoData() {
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

    public Map<String, String> getPOS_CC185DemoData() {
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

    public Map<String, String> getInternetTransaction_CC186DemoData() {
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

    public Map<String, String> getPOS_CC187DemoData() {
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

    public Map<String, String> getPOS_CC188DemoData() {
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

    public Map<String, String> getPOS_CC189DemoData() {
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

    public Map<String, String> getInternetTransaction_CC190DemoData() {
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

    public Map<String, String> getInternetTransaction_CC191DemoData() {
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

    public Map<String, String> getPOS_CC192DemoData() {
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

    public Map<String, String> getCredit_CC193DemoData() {
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

    public Map<String, String> getBillPay_CC194DemoData() {
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
        inputParam.put("Payee_id", "payee1");
        inputParam.put("Bill_id", "bill2");
        return inputParam;
    }

    public Map<String, String> getPOS_CC195DemoData() {
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

    public Map<String, String> getInternetTransaction_CC196DemoData() {
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

    public Map<String, String> getPOS_CC197DemoData() {
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

    public Map<String, String> getPOS_CC198DemoData() {
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

    public Map<String, String> getPOS_CC199DemoData() {
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

    public Map<String, String> getInternetTransaction_CC200DemoData() {
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

    public Map<String, String> getInternetTransaction_CC201DemoData() {
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

    public Map<String, String> getCardPayment_CC202DemoData() {
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

    public Map<String, String> getBillPay_CC203DemoData() {
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
        inputParam.put("Payee_id", "payee1");
        inputParam.put("Bill_id", "bill2");
        return inputParam;
    }

    public Map<String, String> getCredit_Checking1DemoData() {
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

    public Map<String, String> getInternalTransfer_Checking2DemoData() {
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

    public Map<String, String> getInternalTransfer_Checking3DemoData() {
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

    public Map<String, String> getInternalTransfer_Checking4DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternalTransfer");
        inputParam.put("fromAccountNumber", "Rewards Savings");
        inputParam.put("toAccountNumber", "");
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

    public Map<String, String> getCheckDeposit_Checking5DemoData() {
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
        inputParam.put("frontImage1", "http://retailbanking1.konycloud.com/dbimages/check_front.png");
        inputParam.put("frontImage2", "http://retailbanking1.konycloud.com/dbimages/check_front.png");
        inputParam.put("backImage1", "http://retailbanking1.konycloud.com/dbimages/check_back.png");
        inputParam.put("backImage2", "http://retailbanking1.konycloud.com/dbimages/check_back.png");
        inputParam.put("checkImage", "http://retailbanking1.konycloud.com/dbimages/check_front.png");
        inputParam.put("checkImageBack", "http://retailbanking1.konycloud.com/dbimages/check_back.png");
        inputParam.put("hasDepositImage", "1");

        return inputParam;
    }

    public Map<String, String> getP2P_Checking6DemoData() {
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

    public Map<String, String> getInterest_Checking7DemoData() {
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

    public Map<String, String> getWithdrawal_Checking8DemoData() {
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
        inputParam.put("fromAccountBalance", "3491.19");
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

    public Map<String, String> getP2P_Checking9DemoData() {
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
        inputParam.put("fromAccountBalance", "3691.19");
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

    public Map<String, String> getInternetTransaction_Checking10DemoData() {
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
        inputParam.put("fromAccountBalance", "4691.19");
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

    public Map<String, String> getPOS_Checking11DemoData() {
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

    public Map<String, String> getPOS_Checking12DemoData() {
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

    public Map<String, String> getPOS_Checking13DemoData() {
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

    public Map<String, String> getInternetTransaction_Checking14DemoData() {
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

    public Map<String, String> getInternetTransaction_Checking15DemoData() {
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

    public Map<String, String> getPOS_Checking16DemoData() {
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

    public Map<String, String> getCredit_Checking17DemoData() {
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

    public Map<String, String> getBillPay_Checking18DemoData() {
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

    public Map<String, String> getPOS_Checking19DemoData() {
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

    public Map<String, String> getInternetTransaction_Checking20DemoData() {
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

    public Map<String, String> getPOS_Checking21DemoData() {
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

    public Map<String, String> getPOS_Checking22DemoData() {
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

    public Map<String, String> getPOS_Checking23DemoData() {
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

    public Map<String, String> getPOS_Checking24DemoData() {
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

    public Map<String, String> getInternetTransaction_Checking25DemoData() {
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

    public Map<String, String> getPOS_Checking26DemoData() {
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

    public Map<String, String> getPOS_Checking27DemoData() {
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

    public Map<String, String> getPOS_Checking28DemoData() {
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

    public Map<String, String> getInternetTransaction_Checking29DemoData() {
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

    public Map<String, String> getInternetTransaction_Checking30DemoData() {
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

    public Map<String, String> getPOS_Checking31DemoData() {
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

    public Map<String, String> getCredit_Checking32DemoData() {
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

    public Map<String, String> getBillPay_Checking33DemoData() {
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

    public Map<String, String> getPOS_Checking34DemoData() {
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

    public Map<String, String> getInternetTransaction_Checking35DemoData() {
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

    public Map<String, String> getPOS_Checking36DemoData() {
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

    public Map<String, String> getPOS_Checking37DemoData() {
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

    public Map<String, String> getPOS_Checking38DemoData() {
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

    public Map<String, String> getInternetTransaction_Checking39DemoData() {
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

    public Map<String, String> getInternetTransaction_Checking40DemoData() {
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

    public Map<String, String> getPOS_Checking41DemoData() {
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

    public Map<String, String> getCredit_Checking42DemoData() {
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

    public Map<String, String> getBillPay_Checking43DemoData() {
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

    public Map<String, String> getPOS_Checking44DemoData() {
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

    public Map<String, String> getInternetTransaction_Checking45DemoData() {
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

    public Map<String, String> getPOS_Checking46DemoData() {
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

    public Map<String, String> getPOS_Checking47DemoData() {
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

    public Map<String, String> getPOS_Checking48DemoData() {
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

    public Map<String, String> getInternetTransaction_Checking49DemoData() {
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

    public Map<String, String> getInternetTransaction_Checking50DemoData() {
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

    public Map<String, String> getPOS_Checking51DemoData() {
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

    public Map<String, String> getCredit_Checking52DemoData() {
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

    public Map<String, String> getBillPay_Checking53DemoData() {
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

    public Map<String, String> getPOS_Checking54DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-68.19");
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

    public Map<String, String> getInternetTransaction_Checking55DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-120.12");
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

    public Map<String, String> getPOS_Checking56DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-18.19");
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

    public Map<String, String> getPOS_Checking57DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-195.03");
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

    public Map<String, String> getPOS_Checking58DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-75.91");
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

    public Map<String, String> getInternetTransaction_Checking59DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-37.19");
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

    public Map<String, String> getInternetTransaction_Checking60DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-38.04");
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

    public Map<String, String> getPOS_Checking61DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-34.21");
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

    public Map<String, String> getCredit_Checking62DemoData() {
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

    public Map<String, String> getBillPay_Checking63DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-127.98");
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

    public Map<String, String> getPOS_Checking64DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-68.19");
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

    public Map<String, String> getInternetTransaction_Checking65DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-120.12");
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

    public Map<String, String> getCredit_Checking66DemoData() {
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

    public Map<String, String> getPOS_Checking67DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-18.19");
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

    public Map<String, String> getPOS_Checking68DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-195.03");
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

    public Map<String, String> getPOS_Checking69DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-75.91");
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

    public Map<String, String> getCredit_Checking70DemoData() {
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

    public Map<String, String> getBillPay_Checking71DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-127.98");
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

    public Map<String, String> getPOS_Checking72DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-68.19");
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

    public Map<String, String> getInternetTransaction_Checking73DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-120.12");
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

    public Map<String, String> getPOS_Checking74DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-18.19");
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

    public Map<String, String> getPOS_Checking75DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-195.03");
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

    public Map<String, String> getPOS_Checking76DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-75.91");
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

    public Map<String, String> getPOS_Checking77DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-68.19");
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

    public Map<String, String> getInternetTransaction_Checking78DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-120.12");
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

    public Map<String, String> getPOS_Checking79DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-18.19");
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

    public Map<String, String> getPOS_Checking80DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-195.03");
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

    public Map<String, String> getPOS_Checking81DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-75.91");
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

    public Map<String, String> getInternetTransaction_Checking82DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-37.19");
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

    public Map<String, String> getInternetTransaction_Checking83DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-38.04");
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

    public Map<String, String> getPOS_Checking84DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-34.21");
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

    public Map<String, String> getCredit_Checking85DemoData() {
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

    public Map<String, String> getBillPay_Checking86DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-127.98");
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

    public Map<String, String> getPOS_Checking87DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-68.19");
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

    public Map<String, String> getInternetTransaction_Checking88DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-120.12");
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

    public Map<String, String> getPOS_Checking89DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-18.19");
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

    public Map<String, String> getPOS_Checking90DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-195.03");
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

    public Map<String, String> getPOS_Checking91DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-75.91");
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

    public Map<String, String> getInternetTransaction_Checking92DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-37.19");
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

    public Map<String, String> getInternetTransaction_Checking93DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-38.04");
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

    public Map<String, String> getPOS_Checking94DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-34.21");
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

    public Map<String, String> getCredit_Checking95DemoData() {
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

    public Map<String, String> getBillPay_Checking96DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-127.98");
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

    public Map<String, String> getPOS_Checking97DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-68.19");
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

    public Map<String, String> getInternetTransaction_Checking98DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-120.12");
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

    public Map<String, String> getPOS_Checking99DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-18.19");
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

    public Map<String, String> getPOS_Checking100DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-195.03");
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

    public Map<String, String> getPOS_Checking101DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-75.91");
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

    public Map<String, String> getInternetTransaction_Checking102DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-37.19");
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

    public Map<String, String> getInternetTransaction_Checking103DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-38.04");
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

    public Map<String, String> getPOS_Checking104DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-34.21");
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

    public Map<String, String> getCredit_Checking105DemoData() {
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

    public Map<String, String> getBillPay_Checking106DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-127.98");
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

    public Map<String, String> getPOS_Checking107DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-68.19");
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

    public Map<String, String> getInternetTransaction_Checking108DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-120.12");
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

    public Map<String, String> getPOS_Checking109DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-18.19");
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

    public Map<String, String> getPOS_Checking110DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-195.03");
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

    public Map<String, String> getPOS_Checking111DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-75.91");
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

    public Map<String, String> getInternetTransaction_Checking112DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-37.19");
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

    public Map<String, String> getInternetTransaction_Checking113DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-38.04");
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

    public Map<String, String> getPOS_Checking114DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-34.21");
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

    public Map<String, String> getCredit_Checking115DemoData() {
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

    public Map<String, String> getBillPay_Checking116DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-127.98");
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

    public Map<String, String> getPOS_Checking117DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-68.19");
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

    public Map<String, String> getInternetTransaction_Checking118DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-120.12");
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

    public Map<String, String> getPOS_Checking119DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-18.19");
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

    public Map<String, String> getCredit_Checking120DemoData() {
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

    public Map<String, String> getInternetTransaction_Checking121DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-37.19");
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

    public Map<String, String> getInternetTransaction_Checking122DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-38.04");
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

    public Map<String, String> getPOS_Checking123DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-34.21");
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

    public Map<String, String> getCredit_Checking124DemoData() {
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

    public Map<String, String> getBillPay_Checking125DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-127.98");
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

    public Map<String, String> getPOS_Checking126DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-68.19");
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

    public Map<String, String> getInternetTransaction_Checking127DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-120.12");
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

    public Map<String, String> getPOS_Checking128DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-18.19");
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

    public Map<String, String> getPOS_Checking129DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-195.03");
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

    public Map<String, String> getPOS_Checking130DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-75.91");
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

    public Map<String, String> getInternetTransaction_Checking131DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-37.19");
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

    public Map<String, String> getInternetTransaction_Checking132DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-38.04");
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

    public Map<String, String> getPOS_Checking133DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-34.21");
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

    public Map<String, String> getCredit_Checking134DemoData() {
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

    public Map<String, String> getBillPay_Checking135DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-127.98");
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

    public Map<String, String> getPOS_Checking136DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-68.19");
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

    public Map<String, String> getInternetTransaction_Checking137DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-120.12");
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

    public Map<String, String> getPOS_Checking138DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-18.19");
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

    public Map<String, String> getPOS_Checking139DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-195.03");
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

    public Map<String, String> getPOS_Checking140DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-75.91");
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

    public Map<String, String> getInternetTransaction_Checking141DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-37.19");
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

    public Map<String, String> getInternetTransaction_Checking142DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-38.04");
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

    public Map<String, String> getPOS_Checking143DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-34.21");
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

    public Map<String, String> getCredit_Checking144DemoData() {
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

    public Map<String, String> getBillPay_Checking145DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-127.98");
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

    public Map<String, String> getPOS_Checking146DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-68.19");
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

    public Map<String, String> getInternetTransaction_Checking147DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-120.12");
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

    public Map<String, String> getPOS_Checking148DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-18.19");
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

    public Map<String, String> getPOS_Checking149DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-195.03");
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

    public Map<String, String> getPOS_Checking150DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-75.91");
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

    public Map<String, String> getInternetTransaction_Checking151DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-37.19");
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

    public Map<String, String> getInternetTransaction_Checking152DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-38.04");
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

    public Map<String, String> getPOS_Checking153DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-34.21");
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

    public Map<String, String> getCredit_Checking154DemoData() {
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

    public Map<String, String> getBillPay_Checking155DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-127.98");
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

    public Map<String, String> getPOS_Checking156DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-68.19");
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

    public Map<String, String> getInternetTransaction_Checking157DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-120.12");
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

    public Map<String, String> getPOS_Checking158DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-18.19");
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

    public Map<String, String> getPOS_Checking159DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-195.03");
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

    public Map<String, String> getPOS_Checking160DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-75.91");
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

    public Map<String, String> getInternetTransaction_Checking161DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-37.19");
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

    public Map<String, String> getCredit_Checking162DemoData() {
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

    public Map<String, String> getInternetTransaction_Checking163DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-38.04");
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

    public Map<String, String> getPOS_Checking164DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-34.21");
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

    public Map<String, String> getCredit_Checking165DemoData() {
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

    public Map<String, String> getBillPay_Checking166DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-127.98");
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

    public Map<String, String> getPOS_Checking167DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-68.19");
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

    public Map<String, String> getInternetTransaction_Checking168DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-120.12");
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

    public Map<String, String> getPOS_Checking169DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-18.19");
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

    public Map<String, String> getPOS_Checking170DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-195.03");
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

    public Map<String, String> getPOS_Checking171DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-75.91");
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

    public Map<String, String> getInternetTransaction_Checking172DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-37.19");
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

    public Map<String, String> getInternetTransaction_Checking173DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-38.04");
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

    public Map<String, String> getPOS_Checking174DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "Core Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-34.21");
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

    public Map<String, String> getCredit_Checking175DemoData() {
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

    public Map<String, String> getBillPay_Checking176DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-127.98");
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

    public Map<String, String> getPOS_Checking177DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-68.19");
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

    public Map<String, String> getInternetTransaction_Checking178DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-120.12");
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

    public Map<String, String> getPOS_Checking179DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-18.19");
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

    public Map<String, String> getPOS_Checking180DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-195.03");
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

    public Map<String, String> getPOS_Checking181DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-75.91");
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

    public Map<String, String> getInternetTransaction_Checking182DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-120.12");
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

    public Map<String, String> getPOS_Checking183DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-18.19");
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

    public Map<String, String> getPOS_Checking184DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-195.03");
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

    public Map<String, String> getPOS_Checking185DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-75.91");
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

    public Map<String, String> getInternetTransaction_Checking186DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-37.19");
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

    public Map<String, String> getInternetTransaction_Checking187DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-38.04");
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

    public Map<String, String> getPOS_Checking188DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-34.21");
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

    public Map<String, String> getCredit_Checking189DemoData() {
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

    public Map<String, String> getBillPay_Checking190DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-127.98");
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

    public Map<String, String> getPOS_Checking191DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-68.19");
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

    public Map<String, String> getInternetTransaction_Checking192DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-120.12");
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

    public Map<String, String> getPOS_Checking193DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-18.19");
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

    public Map<String, String> getPOS_Checking194DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-195.03");
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

    public Map<String, String> getPOS_Checking195DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-75.91");
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

    public Map<String, String> getInternetTransaction_Checking196DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-37.19");
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

    public Map<String, String> getInternetTransaction_Checking197DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-38.04");
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

    public Map<String, String> getCardPayment_Checking198DemoData() {
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

    public Map<String, String> getBillPay_Checking199DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-869.14");
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

    public Map<String, String> getCredit_Checking200DemoData() {
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

    public Map<String, String> getExternalTransfer_Checking201DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "ExternalTransfer");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "Alex Sion");
        inputParam.put("amount", "-195");
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
        inputParam.put("beneficiaryName", "Alex Sion");
        return inputParam;
    }

    public Map<String, String> getCredit_Checking202DemoData() {
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

    public Map<String, String> getPOS_Checking203DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-206.19");
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

    public Map<String, String> getCheckDeposit_Checking204DemoData() {
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

    public Map<String, String> getExternalTransfer_Checking205DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "ExternalTransfer");
        inputParam.put("fromAccountNumber", "Core Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "John bailey");
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
        inputParam.put("frequencyEndDate", "");
        inputParam.put("beneficiaryName", "John Bailey");
        return inputParam;
    }

    public Map<String, String> getBillPay_Checking206DemoData() {
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

    public Map<String, String> getBillPay_Checking207DemoData() {
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

    public Map<String, String> getInternalTransfer_Checking208DemoData() {
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

    public Map<String, String> getP2P_Checking209DemoData() {
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

    public Map<String, String> getP2P_Checking210DemoData() {
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

    public Map<String, String> getCardLessWithDrawl_Checking1DemoData() {
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

    public Map<String, String> getCardLessWithDrawl_Checking2DemoData() {
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

    public Map getCardTransaction1DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "5314000000004360");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43959");
        inputParam.put("transactionTime", "11:30:23");
        inputParam.put("transactionDescription", "Sunny Bee, Clark Ave");
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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
        inputParam.put("transactionBalance", "0.0");
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

}