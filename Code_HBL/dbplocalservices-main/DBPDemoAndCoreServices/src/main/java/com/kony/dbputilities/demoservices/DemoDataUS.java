package com.kony.dbputilities.demoservices;

import java.util.HashMap;
import java.util.Map;

public class DemoDataUS {

    private Map<String, String> generatedAccounts;
    private Map<String, String> generatedPayees;
    private Map<String, String> generatedPersons;
    private Map<String, String> generatedBills;

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

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getUserDemoData() {
        Map inputParam = new HashMap();
        inputParam.put("addressLine1", "7380,");
        inputParam.put("addressLine2", "West Sand Lake Road,");
        inputParam.put("city", "Orlando");
        inputParam.put("state", "Florida");
        inputParam.put("country", "USA");
        inputParam.put("zipcode", "32819");
        inputParam.put("userFirstName", "John");
        inputParam.put("userLastName", "Bailey");
        inputParam.put("secondaryphone", "8729899218");
        inputParam.put("secondaryphone2", "8729899218");
        inputParam.put("secondaryemail2", "john.bailey@yahoo.com");
        inputParam.put("phone", "4258303691");
        inputParam.put("email", "john.bailey@gmail.com");
        inputParam.put("ssn", "065452564");
        inputParam.put("areAccountStatementTermsAccepted", "0");
        inputParam.put("areDepositTermsAccepted", "0");
        inputParam.put("areUserAlertsTurnedOn", "0");
        inputParam.put("isPhoneEnabled", "0");
        inputParam.put("isBillPaySupported", "1");
        inputParam.put("isP2PSupported", "1");
        inputParam.put("isBillPayActivated", "1");
        inputParam.put("isP2PActivated", "1");
        inputParam.put("isWireTransferEligible", "1");
        inputParam.put("isWireTransferActivated", "1");
        inputParam.put("isEmailEnabled", "1");
        inputParam.put("showBillPayFromAccPopup", "1");
        // inputParam.put("userImageURL", "https://retailbanking1.konycloud.com/dbimages/displayPicture.png");
        inputParam.put("lastlogintime", "2016-01-01");
        inputParam.put("dateOfBirth", "1985-05-05");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getAccount1DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("Type_id", "Savings");
        inputParam.put("Name", "Rewards Savings");
        inputParam.put("AccountName", "Rewards Savings");
        inputParam.put("NickName", "My Savings");
        inputParam.put("OpeningDate", "2016-10-10");
        inputParam.put("StatusDesc", "Active");
        inputParam.put("FavouriteStatus", "1");
        inputParam.put("AvailableBalance", "6949.78");
        inputParam.put("CurrentBalance", "5296.78");
        inputParam.put("PendingDeposit", "1653");
        inputParam.put("PendingWithdrawal", "0");
        inputParam.put("PaymentTerm", "3700.23");
        inputParam.put("PrincipalValue", "3700.23");
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
        inputParam.put("DividendRate", "0.0");
        inputParam.put("DividendYTD", "0");
        inputParam.put("CurrentAmountDue", "478");
        inputParam.put("LastDividendPaidAmount", "0");
        inputParam.put("PreviousYearDividend", "0");
        inputParam.put("DividendPaidYTD", "0");
        inputParam.put("DividendLastPaidAmount", "0");
        inputParam.put("PreviousYearsDividends", "0");
        inputParam.put("TotalCreditMonths", "370");
        inputParam.put("TotalDebitsMonth", "734");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getAccount2DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("Type_id", "Checking");
        inputParam.put("Name", "Core Checking");
        inputParam.put("AccountName", "Core Checking");
        inputParam.put("NickName", "My Checking");
        inputParam.put("OpeningDate", "2017-09-10");
        inputParam.put("StatusDesc", "Active");
        inputParam.put("FavouriteStatus", "1");
        inputParam.put("AvailableBalance", "2487.69");
        inputParam.put("CurrentBalance", "4170.78");
        inputParam.put("PendingDeposit", "2416.91");
        inputParam.put("PendingWithdrawal", "-4100");
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
        inputParam.put("CurrentAmountDue", "38");
        inputParam.put("DividendRate", "0");
        inputParam.put("DividendYTD", "0");
        inputParam.put("LastDividendPaidAmount", "0");
        inputParam.put("PreviousYearDividend", "0");
        inputParam.put("DividendPaidYTD", "0");
        inputParam.put("DividendLastPaidAmount", "0");
        inputParam.put("PreviousYearsDividends", "0");
        inputParam.put("LateFeesDue", "43");
        inputParam.put("BondInterest", "0");
        inputParam.put("BondInterestLastYear", "0");
        inputParam.put("TotalCreditMonths", "370");
        inputParam.put("TotalDebitsMonth", "734");
        inputParam.put("InterestEarned", "23");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getAccount3DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("AvailableBalance", "0");
        inputParam.put("Type_id", "CreditCard");
        inputParam.put("Name", "Freedom Credit Card");
        inputParam.put("AvailableCredit", "8513");
        inputParam.put("CurrentBalance", "20000.09");
        inputParam.put("DueDate", "2018-08-12");
        inputParam.put("InterestRate", "14.62");
        inputParam.put("MaturityDate", "2016-10-10");
        inputParam.put("MinimumDue", "276");
        inputParam.put("AccountName", "Freedom Credit Card");
        inputParam.put("NickName", "Freedom Credit card");
        inputParam.put("OpeningDate", "2016-10-10");
        inputParam.put("LastStatementBalance", "3578.14");
        inputParam.put("OutstandingBalance", "4513");
        inputParam.put("CreditCardNumber", "4541982333084990");
        inputParam.put("AvailablePoints", "724");
        inputParam.put("ShowTransactions", "1");
        inputParam.put("SupportBillPay", "1");
        inputParam.put("SupportDeposit", "1");
        inputParam.put("SupportTransferFrom", "1");
        inputParam.put("SupportTransferTo", "1");
        inputParam.put("SupportCardlessCash", "0");
        inputParam.put("TransactionLimit", "3700.23");
        inputParam.put("TransferLimit", "3700.23");
        inputParam.put("IsPFM", "1");
        inputParam.put("StatusDesc", "Active");
        inputParam.put("FavouriteStatus", "0");
        inputParam.put("RoutingNumber", "648721691");
        inputParam.put("SwiftCode", "CTBAUS124");
        inputParam.put("JointHolders", "Jane Bailey");
        inputParam.put("LastDividendPaidAmount", "14");
        inputParam.put("LastDividendPaidDate", "2018-03-08");
        inputParam.put("DividendLastPaidAmount", "14");
        inputParam.put("DividendLastPaidDate", "23-03-2018");
        inputParam.put("CurrentAmountDue", "3478");
        inputParam.put("PaymentDue", "28");
        inputParam.put("LastPaymentAmount", "1345");
        inputParam.put("TotalCreditMonths", "379");
        inputParam.put("TotalDebitsMonth", "274");
        inputParam.put("LateFeesDue", "10");
        inputParam.put("CreditLimit", "5000");
        inputParam.put("AccountHolder", "John");
        inputParam.put("BsbNum", "123456789");
        inputParam.put("EStatementmentEnable", "0");
        inputParam.put("AccountPreference", "2");
        inputParam.put("BankName", "Infinity Bank");
        inputParam.put("CurrencyCode", "USD");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getAccount4DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("AvailableBalance", "0");
        inputParam.put("Type_id", "Loan");
        inputParam.put("AvailableCredit", "0");
        inputParam.put("ClosingDate", "2019-08-08");
        inputParam.put("CurrentBalance", "0");
        inputParam.put("DueDate", "2018-08-12");
        inputParam.put("InterestRate", "2.25");
        inputParam.put("AccountName", "Turbo Auto Loan");
        inputParam.put("Name", "Turbo Auto Loan");
        inputParam.put("NickName", "Turbo Auto Loan");
        inputParam.put("OpeningDate", "2016-08-08");
        inputParam.put("PrincipalValue", "7120");
        inputParam.put("OutstandingBalance", "18990");
        inputParam.put("ShowTransactions", "1");
        inputParam.put("SupportBillPay", "0");
        inputParam.put("SupportDeposit", "0");
        inputParam.put("SupportTransferFrom", "0");
        inputParam.put("SupportTransferTo", "1");
        inputParam.put("SupportCardlessCash", "0");
        inputParam.put("IsPFM", "0");
        inputParam.put("StatusDesc", "Active");
        inputParam.put("FavouriteStatus", "0");
        inputParam.put("RoutingNumber", "648721691");
        inputParam.put("SwiftCode", "CTBAUS124");
        inputParam.put("JointHolders", "Jane Bailey");
        inputParam.put("CurrentAmountDue", "734");
        inputParam.put("PaymentDue", "13");
        inputParam.put("LastPaymentAmount", "734");
        inputParam.put("TotalCreditMonths", "770");
        inputParam.put("TotalDebitsMonth", "893");
        inputParam.put("LateFeesDue", "11");
        inputParam.put("CreditLimit", "0");
        inputParam.put("InterestPaidYTD", "128");
        inputParam.put("InterestPaidPreviousYTD", "189");
        inputParam.put("AccountHolder", "John");
        inputParam.put("LastDividendPaidAmount", "44");
        inputParam.put("LastDividendPaidDate", "2018-04-08");
        inputParam.put("DividendLastPaidAmount", "23");
        inputParam.put("DividendLastPaidDate", "23-04-2018");
        inputParam.put("principalBalance", "16000");
        inputParam.put("OriginalAmount", "20000");
        inputParam.put("payoffAmount", "16749");
        inputParam.put("BsbNum", "123456789");
        inputParam.put("PayOffCharge", "749");
        inputParam.put("InterestPaidLastYear", "189");
        inputParam.put("EStatementmentEnable", "0");
        inputParam.put("AccountPreference", "3");
        inputParam.put("BankName", "Infinity Bank");
        inputParam.put("CurrencyCode", "USD");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getAccount5DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("AvailableBalance", "5000");
        inputParam.put("Type_id", "Deposit");
        inputParam.put("CurrentBalance", "5000");
        inputParam.put("InterestRate", "0.45");
        inputParam.put("MaturityDate", "2018-11-08");
        inputParam.put("AccountName", "12 Months Term Deposit");
        inputParam.put("Name", "12 Months Term Deposit");
        inputParam.put("NickName", "12 Months Term Deposit");
        inputParam.put("OpeningDate", "2017-11-08");
        inputParam.put("PaymentTerm", "4");
        inputParam.put("AvailablePoints", "0");
        inputParam.put("ShowTransactions", "1");
        inputParam.put("SupportBillPay", "0");
        inputParam.put("SupportDeposit", "0");
        inputParam.put("SupportTransferFrom", "0");
        inputParam.put("SupportTransferTo", "0");
        inputParam.put("SupportCardlessCash", "0");
        inputParam.put("IsPFM", "0");
        inputParam.put("StatusDesc", "Active");
        inputParam.put("FavouriteStatus", "0");
        inputParam.put("MaturityOption", "Renew");
        inputParam.put("RoutingNumber", "648721691");
        inputParam.put("SwiftCode", "CTBAUS124");
        inputParam.put("JointHolders", "Jane Bailey");
        inputParam.put("DividendRate", "0.45");
        inputParam.put("DividendYTD", "34");
        inputParam.put("LastDividendPaidAmount", "34");
        inputParam.put("LastDividendPaidDate", "2018-01-08");
        inputParam.put("PreviousYearDividend", "70");
        inputParam.put("CurrentAmountDue", "3478");
        inputParam.put("LateFeesDue", "23");
        inputParam.put("DividendPaidYTD", "34");
        inputParam.put("DividendLastPaidAmount", "34");
        inputParam.put("DividendLastPaidDate", "2018-01-08");
        inputParam.put("TotalCreditMonths", "570");
        inputParam.put("TotalDebitsMonth", "653");
        inputParam.put("PreviousYearsDividends", "70");
        inputParam.put("AccountHolder", "John");
        inputParam.put("InterestEarned", "45");
        inputParam.put("maturityAmount", "5600");
        inputParam.put("BsbNum", "123456789");
        inputParam.put("EStatementmentEnable", "0");
        inputParam.put("AccountPreference", "4");
        inputParam.put("BankName", "Infinity Bank");
        inputParam.put("CurrencyCode", "USD");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getAccount6DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("AvailableBalance", "4275.82");
        inputParam.put("Type_id", "Savings");
        inputParam.put("StatusDesc", "Active");
        inputParam.put("AvailableCredit", "4275.82");
        inputParam.put("ClosingDate", "2016-10-10");
        inputParam.put("CurrentBalance", "4300.27");
        inputParam.put("DueDate", "2016-10-10");
        inputParam.put("InterestRate", "3700.23");
        inputParam.put("MaturityDate", "2016-10-10");
        inputParam.put("MinimumDue", "3700.23");
        inputParam.put("Name", "Savings Max");
        inputParam.put("NickName", "Savings Max");
        inputParam.put("OpeningDate", "2016-10-10");
        inputParam.put("PaymentTerm", "3700.23");
        inputParam.put("PrincipalValue", "3700.23");
        inputParam.put("LastStatementBalance", "3700.23");
        inputParam.put("OutstandingBalance", "3700.23");
        inputParam.put("CreditCardNumber", "978913571");
        inputParam.put("AvailablePoints", "3700");
        inputParam.put("ShowTransactions", "1");
        inputParam.put("SupportBillPay", "1");
        inputParam.put("SupportDeposit", "1");
        inputParam.put("SupportTransferFrom", "1");
        inputParam.put("SupportTransferTo", "1");
        inputParam.put("SupportCardlessCash", "0");
        inputParam.put("TransactionLimit", "3700.23");
        inputParam.put("TransferLimit", "3700.23");
        inputParam.put("IsPFM", "1");
        inputParam.put("CurrencyCode", "USD");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getAccount7DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("Type_id", "Checking");
        inputParam.put("AvailableBalance", "1876.28");
        inputParam.put("AvailableCredit", "2010.12");
        inputParam.put("ClosingDate", "2016-10-10");
        inputParam.put("CurrentBalance", "2010.12");
        inputParam.put("DueDate", "2016-10-10");
        inputParam.put("InterestRate", "0.5");
        inputParam.put("MaturityDate", "2016-10-10");
        inputParam.put("MinimumDue", "3700.23");
        inputParam.put("Name", "Checking Account");
        inputParam.put("NickName", "Checking Account");
        inputParam.put("OpeningDate", "2016-10-10");
        inputParam.put("PaymentTerm", "3700.23");
        inputParam.put("PrincipalValue", "3700.23");
        inputParam.put("LastStatementBalance", "3700.23");
        inputParam.put("OutstandingBalance", "0");
        inputParam.put("CreditCardNumber", "978913571");
        inputParam.put("AvailablePoints", "3700");
        inputParam.put("ShowTransactions", "1");
        inputParam.put("SupportBillPay", "1");
        inputParam.put("SupportDeposit", "1");
        inputParam.put("SupportTransferFrom", "1");
        inputParam.put("SupportTransferTo", "1");
        inputParam.put("SupportCardlessCash", "1");
        inputParam.put("TransactionLimit", "2010.12");
        inputParam.put("TransferLimit", "2010.12");
        inputParam.put("IsPFM", "1");
        inputParam.put("CurrencyCode", "USD");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getAccount8DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("AvailableBalance", "20000");
        inputParam.put("Type_id", "Savings");
        inputParam.put("StatusDesc", "Active");
        inputParam.put("AvailableCredit", "20000");
        inputParam.put("ClosingDate", "2016-10-10");
        inputParam.put("CurrentBalance", "20000");
        inputParam.put("DueDate", "2016-10-10");
        inputParam.put("InterestRate", "2.25");
        inputParam.put("MaturityDate", "2016-10-10");
        inputParam.put("MinimumDue", "100");
        inputParam.put("Name", "Savings Plus");
        inputParam.put("NickName", "Savings Plus");
        inputParam.put("OpeningDate", "2016-10-10");
        inputParam.put("PaymentTerm", "3700.23");
        inputParam.put("PrincipalValue", "20000");
        inputParam.put("LastStatementBalance", "3700.23");
        inputParam.put("OutstandingBalance", "20000");
        inputParam.put("CreditCardNumber", "4541982333084990");
        inputParam.put("AvailablePoints", "3700");
        inputParam.put("ShowTransactions", "0");
        inputParam.put("SupportBillPay", "0");
        inputParam.put("SupportDeposit", "1");
        inputParam.put("SupportTransferFrom", "0");
        inputParam.put("SupportTransferTo", "1");
        inputParam.put("SupportCardlessCash", "0");
        inputParam.put("TransactionLimit", "3700.23");
        inputParam.put("TransferLimit", "3700.23");
        inputParam.put("IsPFM", "1");
        inputParam.put("CurrencyCode", "USD");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getPayee1DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("accountNumber", "7790396982957");
        inputParam.put("addressLine1", "ABC Energy LLC");
        inputParam.put("addressLine2", "200 East Post Road");
        inputParam.put("state", "Florida");
        inputParam.put("cityName", "Orlando ");
        inputParam.put("nickName", "Electricity - ABC");
        inputParam.put("companyName", "ABC Energy");
        inputParam.put("email", "electricity@gmail.com");
        inputParam.put("firstName", "Electricity");
        inputParam.put("lastName", "Bill");
        inputParam.put("name", "ABC Energy");
        inputParam.put("phone", "4351348905");
        inputParam.put("zipCode", "759634");
        inputParam.put("eBillEnable", "1");
        inputParam.put("isAutoPayEnabled", "0");
        inputParam.put("nameOnBill", "John Bailey");
        inputParam.put("billerId", "3");
        inputParam.put("softDelete", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getPayee2DemoData() {
        Map inputParam = new HashMap();
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
        inputParam.put("phone", "1501273981272");
        inputParam.put("zipCode", "459667");
        inputParam.put("eBillEnable", "0");
        inputParam.put("isAutoPayEnabled", "0");
        inputParam.put("nameOnBill", "John Bailey");
        inputParam.put("billerId", "4");
        inputParam.put("softDelete", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getPayee3DemoData() {
        Map inputParam = new HashMap();
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
        inputParam.put("name", "Citibank Credit Cards");
        inputParam.put("phone", " ");
        inputParam.put("zipCode", "759634");
        inputParam.put("eBillEnable", "1");
        inputParam.put("isAutoPayEnabled", "0");
        inputParam.put("nameOnBill", "John Bailey");
        inputParam.put("billerId", "1");
        inputParam.put("softDelete", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getPayee4DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("accountNumber", "6890396982929");
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
        inputParam.put("phone", "938899898");
        inputParam.put("zipCode", "42110");
        inputParam.put("eBillEnable", "1");
        inputParam.put("isAutoPayEnabled", "0");
        inputParam.put("nameOnBill", "John Bailey");
        inputParam.put("billerId", "2");
        inputParam.put("softDelete", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getPayee5DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("accountNumber", "11433468761745");
        inputParam.put("addressLine1", "514S,");
        inputParam.put("addressLine2", "Magnolia St.");
        inputParam.put("state", "New York");
        inputParam.put("cityName", "Orlando");
        inputParam.put("nickName", "City Water Works");
        inputParam.put("companyName", "Virginia Home Rentals");
        inputParam.put("email", "rent@gmail.com");
        inputParam.put("firstName", "Water");
        inputParam.put("lastName", "Works");
        inputParam.put("name", "Water Works");
        inputParam.put("phone", "65817436791");
        inputParam.put("zipCode", "42110");
        inputParam.put("eBillEnable", "0");
        inputParam.put("isAutoPayEnabled", "0");
        inputParam.put("nameOnBill", "Water Works");
        inputParam.put("billerId", "3");
        inputParam.put("softDelete", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getPayee6DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("accountNumber", "9611932872351");
        inputParam.put("addressLine1", "44 Shirley Ave");
        inputParam.put("addressLine2", "West Chicago");
        inputParam.put("cityName", "San Antonio");
        inputParam.put("state", "Texas");
        inputParam.put("nickName", "City Utilities Company");
        inputParam.put("companyName", "Beam Teleservices");
        inputParam.put("email", "wifi@gmail.com");
        inputParam.put("firstName", "City");
        inputParam.put("lastName", "Utilities");
        inputParam.put("name", "Beam Teleservices");
        inputParam.put("phone", "4523746832");
        inputParam.put("zipCode", "50098");
        inputParam.put("eBillEnable", "0");
        inputParam.put("isAutoPayEnabled", "0");
        inputParam.put("nameOnBill", "Citi Utilities");
        inputParam.put("billerId", "3");
        inputParam.put("softDelete", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getPerson1DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("email", "judy.blume@gmail.com");
        inputParam.put("name", "Judy Blume");
        inputParam.put("nickName", "Judy");
        inputParam.put("phone", "8790146356");
        inputParam.put("secondaryEmail", "jblume@gmail.com");
        inputParam.put("secondoryPhoneNumber", "4567483920");
        inputParam.put("primaryContactForSending", "8790146356");
        inputParam.put("isSoftDelete", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getPerson2DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("name", "Arthur Miller");
        inputParam.put("nickName", "Arthur");
        inputParam.put("phone", "6565178276");
        inputParam.put("primaryContactForSending", "6565178276");
        inputParam.put("isSoftDelete", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getPerson3DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("email", "frank@yahoo.com");
        inputParam.put("name", "Frank Underwoord");
        inputParam.put("nickName", "Frank U");
        inputParam.put("primaryContactForSending", "frank@yahoo.com");
        inputParam.put("phone", "5638294057");
        inputParam.put("isSoftDelete", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getPerson4DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("email", "francis@gmail.com");
        inputParam.put("name", "Francis Ford");
        inputParam.put("nickName", "Francis F");
        inputParam.put("primaryContactForSending", "Fordfrancis@gmail.com");
        inputParam.put("isSoftDelete", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getPerson5DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("email", "peter@gmail.com");
        inputParam.put("name", "Peter Jackson");
        inputParam.put("secondaryEmail", "JacksonP@yahoo.com");
        inputParam.put("nickName", "Peter J");
        inputParam.put("primaryContactForSending", "peter@gmail.com");
        inputParam.put("isSoftDelete", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getExtAccount1DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("nickName", "Alex Sion");
        inputParam.put("beneficiaryName", "Alex Sion");
        inputParam.put("accountType", "Savings");
        inputParam.put("countryName", "United States of America (USA)");
        inputParam.put("firstName", "Alex");
        inputParam.put("lastName", "Sion");
        inputParam.put("notes", " ");
        inputParam.put("bankName", "National Bank");
        inputParam.put("isSameBankAccount", "0");
        inputParam.put("isVerified", "1");
        inputParam.put("createdOn", "2017-10-10");
        inputParam.put("routingNumber", "765678987");
        inputParam.put("swiftCode", "0");
        inputParam.put("isInternationalAccount", "0");
        inputParam.put("accountNumber", "98287890273");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getExtAccount2DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("nickName", "Tom B");
        inputParam.put("beneficiaryName", "Tom Brummett");
        inputParam.put("accountType", "Checking");
        inputParam.put("countryName", "United States of America (USA)");
        inputParam.put("firstName", "Tom");
        inputParam.put("lastName", "Brummett");
        inputParam.put("notes", " ");
        inputParam.put("bankName", "CitiBank");
        inputParam.put("isSameBankAccount", "0");
        inputParam.put("isVerified", "1");
        inputParam.put("createdOn", "2016-11-07");
        inputParam.put("routingNumber", "765678987");
        inputParam.put("swiftCode", "0");
        inputParam.put("isInternationalAccount", "0");
        inputParam.put("accountNumber", "5528789466");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getExtAccount3DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("nickName", "My BoA Account");
        inputParam.put("beneficiaryName", "John Bailey");
        inputParam.put("accountType", "Checking");
        inputParam.put("countryName", "United States of America (USA)");
        inputParam.put("firstName", "John");
        inputParam.put("lastName", "Bailey");
        inputParam.put("notes", " ");
        inputParam.put("bankName", "Bank of America");
        inputParam.put("isSameBankAccount", "0");
        inputParam.put("isVerified", "0");
        inputParam.put("createdOn", "2018-01-07");
        inputParam.put("routingNumber", "765678922");
        inputParam.put("swiftCode", "0");
        inputParam.put("isInternationalAccount", "0");
        inputParam.put("accountNumber", "29475930375");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getExtAccount4DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("nickName", "Dan Marous");
        inputParam.put("beneficiaryName", "Dan Marous");
        inputParam.put("accountType", "Checking");
        inputParam.put("countryName", "United States of America (USA)");
        inputParam.put("firstName", "Dan");
        inputParam.put("lastName", "Marous");
        inputParam.put("notes", " ");
        inputParam.put("bankName", "Infinity Bank");
        inputParam.put("isSameBankAccount", "1");
        inputParam.put("isVerified", "1");
        inputParam.put("createdOn", "2015-10-13");
        inputParam.put("routingNumber", "0");
        inputParam.put("swiftCode", "0");
        inputParam.put("isInternationalAccount", "0");
        inputParam.put("accountNumber", "47834984272");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getExtAccount5DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("nickName", "Henry");
        inputParam.put("beneficiaryName", "Henry James");
        inputParam.put("accountType", "Savings");
        inputParam.put("countryName", "India (Ind)");
        inputParam.put("firstName", "Henry");
        inputParam.put("lastName", "James");
        inputParam.put("notes", " ");
        inputParam.put("bankName", "ICICI bank");
        inputParam.put("isSameBankAccount", "0");
        inputParam.put("isVerified", "1");
        inputParam.put("createdOn", "2017-12-28");
        inputParam.put("routingNumber", "0");
        inputParam.put("swiftCode", "CTBAUS12");
        inputParam.put("isInternationalAccount", "1");
        inputParam.put("accountNumber", "64314698173");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map geCard1DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("action", "Activate");
        inputParam.put("card_Status", "Active");
        inputParam.put("availableCredit", "87161.00");
        inputParam.put("serviceProvider", "Visa");
        inputParam.put("cardProductName", "Gold Debit Card");
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("expirationDate", "2019-12-12");
        inputParam.put("pinNumber", "1578");
        inputParam.put("withdrawlLimit", "214");
        inputParam.put("reason", " ");
        inputParam.put("cardHolderName", "John Brown");
        inputParam.put("secondaryCardHolder", "John");
        inputParam.put("isInternational", "1");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map geCard2DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("action", "Activate");
        inputParam.put("card_Status", "Active");
        inputParam.put("availableCredit", "12380.00");
        inputParam.put("serviceProvider", "Master Card");
        inputParam.put("cardProductName", "Eazee Food Card");
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("expirationDate", "2018-11-11");
        inputParam.put("pinNumber", "1579");
        inputParam.put("withdrawlLimit", "524");
        inputParam.put("reason", " ");
        inputParam.put("cardHolderName", "Hugo Brown");
        inputParam.put("secondaryCardHolder", "Dane");
        inputParam.put("isInternational", "1");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map geCard3DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("action", "Activate");
        inputParam.put("card_Status", "Active");
        inputParam.put("availableCredit", "8513.00");
        inputParam.put("serviceProvider", "Master Card");
        inputParam.put("cardProductName", "Premium Club Credit Card");
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("expirationDate", "2020-09-09");
        inputParam.put("pinNumber", "1580");
        inputParam.put("creditLimit", "20000");
        inputParam.put("billingAddress", "#2076, Road #3, Downtown, Austin, Texas, 560002");
        inputParam.put("reason", " ");
        inputParam.put("cardHolderName", "Leo Brown");
        inputParam.put("secondaryCardHolder", "Doe");
        inputParam.put("isInternational", "1");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map geUserAlertDemoData() {
        Map inputParam = new HashMap();
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

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map geAccountAlert1DemoData() {
        Map inputParam = new HashMap();
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

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map geAccountAlert2DemoData() {
        Map inputParam = new HashMap();
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

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map geAccountAlert3DemoData() {
        Map inputParam = new HashMap();
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

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map geAccountAlert4DemoData() {
        Map inputParam = new HashMap();
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

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getRecent1DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "1600");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Internal Transfer from Pro Checking");
        inputParam.put("notes", "Saving for a car");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("toAccountBalance", "3145");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "511");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getRecent2DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "1.78");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Interest Credit for Jan");
        inputParam.put("notes", "Card bill payment");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("toAccountBalance", "3041.78");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "3041.78");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getRecent3DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "500");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Internal Transfer from Pro Checking");
        inputParam.put("notes", "Taking some money to cover expenses");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("toAccountBalance", "6232");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "9741.78");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getRecent4DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "599");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Internal Transfer to Pro Checking");
        inputParam.put("notes", "Judy's College Savings");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("toAccountBalance", "2512");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "2515");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getRecent5DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "10");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Charge Low Ac Balance");
        inputParam.put("notes", "Card bill payment");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("toAccountBalance", "3626");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "4626");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getRecent6DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "1500");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Internal Transfer  to Pro Checking");
        inputParam.put("notes", "Saving for a car");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("toAccountBalance", "6387");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "2646");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getRecent7DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "2500");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Internal Transfer Freedom Credit Card");
        inputParam.put("notes", "Jan Credit Card Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("toAccountBalance", "52000");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "5161");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getRecent8DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "1600");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Internal Transfer Savings Plus");
        inputParam.put("notes", "Saving for a car");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("toAccountBalance", "0");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "0");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getRecent9DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "250");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "ATM-KonyBank,Austin,70123");
        inputParam.put("notes", "Taking some money to cover expenses");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("toAccountBalance", "6387");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "1161");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getRecent10DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "300");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "ATM-KonyBank,Orlando,34672");
        inputParam.put("notes", "For personal use");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("toAccountBalance", "341");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "5151");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getRecent11DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "2500");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Online payment from SAV 18");
        inputParam.put("notes", "To avoid Overdraft");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("toAccountBalance", "49000");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "4141");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getRecent12DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "1000");
        inputParam.put("checkNumber", "19804");
        inputParam.put("description", "Check 19804");
        inputParam.put("notes", "FTaking some money to cover expenses");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("toAccountBalance", "3040");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "3040");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getRecent13DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "2750");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Check 19802");
        inputParam.put("notes", "Received from Jane for Rent");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("toAccountBalance", "6294");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "7518");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("backImage1", "http://pmqa.konylabs.net/KonyWebBanking/check_back.png");
        inputParam.put("backImage2", "http://pmqa.konylabs.net/KonyWebBanking/check_back.png");
        inputParam.put("bankName1", "HDFC Bank");
        inputParam.put("bankName2", "City Bank");
        inputParam.put("cashAmount", "143");
        inputParam.put("checkDesc", "EMI Option");
        inputParam.put("checkNumber1", "Check #19802");
        inputParam.put("checkNumber2", "Check #876555");
        inputParam.put("frontImage1", "http://pmqa.konylabs.net/KonyWebBanking/check_front.png");
        inputParam.put("frontImage2", "http://pmqa.konylabs.net/KonyWebBanking/check_front.png");
        inputParam.put("withdrawlAmount1", "2750");
        inputParam.put("withdrawlAmount2", "750");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getRecent14DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "700");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Check 32174");
        inputParam.put("notes", "Tuition Fee");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("toAccountBalance", "1924");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "6522");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("backImage1", "http://pmqa.konylabs.net/KonyWebBanking/check_back.png");
        inputParam.put("backImage2", "http://pmqa.konylabs.net/KonyWebBanking/check_back.png");
        inputParam.put("bankName1", "HDFC Bank");
        inputParam.put("bankName2", "City Bank");
        inputParam.put("cashAmount", "24");
        inputParam.put("checkDesc", "EMI Option");
        inputParam.put("checkNumber1", "Check #32174");
        inputParam.put("checkNumber2", "Check #79834");
        inputParam.put("frontImage1", "http://pmqa.konylabs.net/KonyWebBanking/check_front.png");
        inputParam.put("frontImage2", "http://pmqa.konylabs.net/KonyWebBanking/check_front.png");
        inputParam.put("withdrawlAmount1", "700");
        inputParam.put("withdrawlAmount2", "300");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getRecent15DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "2200");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Check 19806");
        inputParam.put("notes", "Deposit to my account please");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("toAccountBalance", "1233");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "9712");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("backImage1", "http://pmqa.konylabs.net/KonyWebBanking/check_back.png");
        inputParam.put("backImage2", "http://pmqa.konylabs.net/KonyWebBanking/check_back.png");
        inputParam.put("bankName1", "HDFC Bank");
        inputParam.put("bankName2", "City Bank");
        inputParam.put("cashAmount", "24");
        inputParam.put("checkDesc", "EMI Option");
        inputParam.put("checkNumber1", "Check #32174");
        inputParam.put("checkNumber2", "Check #79834");
        inputParam.put("frontImage1", "http://pmqa.konylabs.net/KonyWebBanking/check_front.png");
        inputParam.put("frontImage2", "http://pmqa.konylabs.net/KonyWebBanking/check_front.png");
        inputParam.put("withdrawlAmount1", "2200");
        inputParam.put("withdrawlAmount2", "2700");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getRecent16DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "120");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Jan Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("toAccountBalance", "49000");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "4880");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getRecent17DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "120");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Jan Electricity Bill");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("toAccountBalance", "49000");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "4880");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "1");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getRecent18DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "200");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Online Payment on Amazon.com");
        inputParam.put("notes", "Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("toAccountBalance", "49000");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "4880");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "1");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getRecent19DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "2500");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Cardless withdrawal to Self");
        inputParam.put("cashWithdrawalTransactionStatus", "pending");
        inputParam.put("notes", "Cash for me");
        inputParam.put("cashlessOTP", "236723");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("toAccountBalance", "51500");
        inputParam.put("cashlessMode", "Self");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "2380");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getRecent20DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "1000");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Cardless withdrawal to Self");
        inputParam.put("cashWithdrawalTransactionStatus", "pending");
        inputParam.put("notes", "Cardless withdrawal to myself");
        inputParam.put("cashlessOTP", "789429");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("toAccountBalance", "37000");
        inputParam.put("cashlessMode", "Self");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "5792");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getRecent21DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "100");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Cardless withdrawal to John Bailey");
        inputParam.put("cashWithdrawalTransactionStatus", "pending");
        inputParam.put("notes", "Cash to John Bailey");
        inputParam.put("cashlessOTP", "324692");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("toAccountBalance", "25649");
        inputParam.put("cashlessMode", "others");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "9834");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getRecent22DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "200");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Cardless withdrawal to John Doe");
        inputParam.put("cashWithdrawalTransactionStatus", "pending");
        inputParam.put("notes", "Cash to John Doe");
        inputParam.put("cashlessOTP", "572839");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("toAccountBalance", "62782");
        inputParam.put("cashlessMode", "others");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "5020");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getRecent23DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "900");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Cardless withdrawal to Self");
        inputParam.put("cashWithdrawalTransactionStatus", "pending");
        inputParam.put("notes", "Cash for my expenses");
        inputParam.put("cashlessOTP", "537614");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("toAccountBalance", "14613");
        inputParam.put("cashlessMode", "Self");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "5409");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getExt1DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "645");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Online External Fund Transfer to Alex Sion");
        inputParam.put("notes", "Online External");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("toAccountBalance", "0");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "4503.81");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getExt2DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "995");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Online External Fund Transfer to Tom Brummett");
        inputParam.put("notes", "To Tom for his Wedding");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("toAccountBalance", "0");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "1631");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getExt3DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "100");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "OnlineExtFundTransfer to JohnBailey");
        inputParam.put("notes", "Transfer to my BoA Account for school fee");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("toAccountBalance", "0");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "8945");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "12");
        inputParam.put("frequencyStartDate", "2018-12-31");
        inputParam.put("frequencyEndDate", "2018-12-31");
        inputParam.put("isScheduled", "1");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getExt4DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "0.20");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "For Verification");
        inputParam.put("notes", "To Dan for his personal use");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("toAccountBalance", "0");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "0");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "12");
        inputParam.put("isScheduled", "1");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getExt5DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "0.41");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "For Verification");
        inputParam.put("notes", "To Tom for his wedding");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("toAccountBalance", "10240.59");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "5148.81");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    /*
     * @SuppressWarnings({ "rawtypes", "unchecked" }) public Map getExt6DemoData() { Map inputParam = new HashMap();
     * inputParam.put("amount", "504"); inputParam.put("checkNumber", "0"); inputParam.put("description",
     * "To Dan for his personal use"); inputParam.put("notes", "To Dan for his personal use");
     * inputParam.put("statusDesc", "0"); inputParam.put("toAccountBalance", "0");
     * inputParam.put("toExternalAccountNumber", "0"); inputParam.put("frequencyType", "Once");
     * inputParam.put("isScheduled", "0"); return inputParam; }
     * 
     * @SuppressWarnings({ "rawtypes", "unchecked" }) public Map getExt7DemoData() { Map inputParam = new HashMap();
     * inputParam.put("amount", "1205"); inputParam.put("checkNumber", "0"); inputParam.put("description",
     * "To Tom for his wedding"); inputParam.put("notes", "To Tom for his wedding"); inputParam.put("statusDesc", "0");
     * inputParam.put("toAccountBalance", "0"); inputParam.put("toExternalAccountNumber", "0");
     * inputParam.put("frequencyType", "Once"); inputParam.put("isScheduled", "0"); return inputParam; }
     * 
     * @SuppressWarnings({ "rawtypes", "unchecked" }) public Map getExt8DemoData() { Map inputParam = new HashMap();
     * inputParam.put("amount", "455"); inputParam.put("checkNumber", "0"); inputParam.put("description",
     * "To Dan for his personal use"); inputParam.put("notes", "To Dan for his personal use");
     * inputParam.put("statusDesc", "0"); inputParam.put("toAccountBalance", "0");
     * inputParam.put("toExternalAccountNumber", "0"); inputParam.put("frequencyType", "Once");
     * inputParam.put("isScheduled", "0"); return inputParam; }
     */

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getRecievedP2P1DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "314");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "For Wedding");
        inputParam.put("notes", "For Wedding");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("p2pContact", "arthur@gmail.com");
        inputParam.put("toAccountBalance", "1511");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "4275");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getBill1DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "75");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Bill Pay to AT&T");
        inputParam.put("notes", "Mobile Bill for Dec 17");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("toAccountBalance", "0");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "9479");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("frequencyStartDate", "2018-01-10");
        inputParam.put("isScheduled", "1");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getBill2DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "800");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Bill Pay to CitiBank Credit Card");
        inputParam.put("notes", "Citi Credit Card Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("toAccountBalance", "0");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "8790");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("frequencyStartDate", "2018-01-10");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    /*
     * @SuppressWarnings({ "rawtypes", "unchecked" }) public Map getBill3DemoData() { Map inputParam = new HashMap();
     * inputParam.put("amount", "30"); inputParam.put("checkNumber", "0"); inputParam.put("description",
     * "Home wifi bill payment"); inputParam.put("notes", "Home wifi bill payment"); inputParam.put("statusDesc", "0");
     * inputParam.put("toAccountBalance", "0"); inputParam.put("toExternalAccountNumber", "0");
     * inputParam.put("frequencyType", "Once"); inputParam.put("fromAccountBalance", "3700.23");
     * inputParam.put("isScheduled", "0"); return inputParam; }
     * 
     * @SuppressWarnings({ "rawtypes", "unchecked" }) public Map getBill4DemoData() { Map inputParam = new HashMap();
     * inputParam.put("amount", "110"); inputParam.put("checkNumber", "0"); inputParam.put("description",
     * "Electricity bill payment"); inputParam.put("notes", "Electricity bill payment"); inputParam.put("statusDesc",
     * "0"); inputParam.put("toAccountBalance", "0"); inputParam.put("toExternalAccountNumber", "0");
     * inputParam.put("frequencyType", "Once"); inputParam.put("fromAccountBalance", "3700.23");
     * inputParam.put("isScheduled", "0"); return inputParam; }
     * 
     * @SuppressWarnings({ "rawtypes", "unchecked" }) public Map getBill5DemoData() { Map inputParam = new HashMap();
     * inputParam.put("amount", "79"); inputParam.put("checkNumber", "0"); inputParam.put("description",
     * "Home wifi bill payment"); inputParam.put("notes", "Home wifi bill payment"); inputParam.put("statusDesc", "0");
     * inputParam.put("toAccountBalance", "0"); inputParam.put("toExternalAccountNumber", "0");
     * inputParam.put("frequencyType", "Once"); inputParam.put("fromAccountBalance", "3700.23");
     * inputParam.put("isScheduled", "0"); return inputParam; }
     * 
     * @SuppressWarnings({ "rawtypes", "unchecked" }) public Map getBill6DemoData() { Map inputParam = new HashMap();
     * inputParam.put("amount", "103"); inputParam.put("checkNumber", "0"); inputParam.put("description",
     * "Electricity bill payment"); inputParam.put("notes", "Electricity bill payment"); inputParam.put("statusDesc",
     * "0"); inputParam.put("toAccountBalance", "0"); inputParam.put("toExternalAccountNumber", "0");
     * inputParam.put("frequencyType", "Once"); inputParam.put("fromAccountBalance", "3700.23");
     * inputParam.put("isScheduled", "0"); return inputParam; }
     * 
     * @SuppressWarnings({ "rawtypes", "unchecked" }) public Map getBill7DemoData() { Map inputParam = new HashMap();
     * inputParam.put("amount", "30"); inputParam.put("checkNumber", "0"); inputParam.put("description",
     * "Home wifi bill payment"); inputParam.put("notes", "Home wifi bill payment"); inputParam.put("statusDesc", "0");
     * inputParam.put("toAccountBalance", "0"); inputParam.put("toExternalAccountNumber", "0");
     * inputParam.put("frequencyType", "Once"); inputParam.put("fromAccountBalance", "3700.23");
     * inputParam.put("isScheduled", "0"); return inputParam; }
     * 
     * @SuppressWarnings({ "rawtypes", "unchecked" }) public Map getBill8DemoData() { Map inputParam = new HashMap();
     * inputParam.put("amount", "58"); inputParam.put("checkNumber", "0"); inputParam.put("description",
     * "Electricity bill payment"); inputParam.put("notes", "Electricity bill payment"); inputParam.put("statusDesc",
     * "0"); inputParam.put("toAccountBalance", "0"); inputParam.put("toExternalAccountNumber", "0");
     * inputParam.put("frequencyType", "Once"); inputParam.put("fromAccountBalance", "3700.23");
     * inputParam.put("isScheduled", "0"); return inputParam; }
     */

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getP2p1DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "30");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Person2PersonTransfer to Arthur");
        inputParam.put("notes", "For golf bet");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("toAccountBalance", "0");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "2616");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getP2p2DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "15");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Person2PersonTransfer to Frank");
        inputParam.put("notes", "Books");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("toAccountBalance", "0");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "8956");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getP2p3DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "40");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "PersonToPerson Transfer");
        inputParam.put("notes", "For golf bet");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("toAccountBalance", "0");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "7460");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getP2p4DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "30");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "PersonToPerson to Arthur");
        inputParam.put("notes", "For Golf Bet");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("toAccountBalance", "0");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "1152");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getP2p5DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "15");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "PersonToPerson to Frank");
        inputParam.put("notes", "Book");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("toAccountBalance", "0");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "1146");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    /*
     * @SuppressWarnings({ "rawtypes", "unchecked" }) public Map getP2p6DemoData() { Map inputParam = new HashMap();
     * inputParam.put("amount", "100"); inputParam.put("checkNumber", "0"); inputParam.put("description",
     * "To Arthur for his personal use"); inputParam.put("notes", "To Arthur for his personal use");
     * inputParam.put("statusDesc", "0"); inputParam.put("toAccountBalance", "0");
     * inputParam.put("toExternalAccountNumber", "0"); inputParam.put("frequencyType", "Once");
     * inputParam.put("isScheduled", "0"); return inputParam; }
     * 
     * @SuppressWarnings({ "rawtypes", "unchecked" }) public Map getP2p7DemoData() { Map inputParam = new HashMap();
     * inputParam.put("amount", "186"); inputParam.put("checkNumber", "0"); inputParam.put("description",
     * "For golf bet"); inputParam.put("notes", "For golf bet"); inputParam.put("statusDesc", "0");
     * inputParam.put("toAccountBalance", "0"); inputParam.put("toExternalAccountNumber", "0");
     * inputParam.put("frequencyType", "Once"); inputParam.put("isScheduled", "0"); return inputParam; }
     * 
     * @SuppressWarnings({ "rawtypes", "unchecked" }) public Map getP2p8DemoData() { Map inputParam = new HashMap();
     * inputParam.put("amount", "51"); inputParam.put("checkNumber", "0"); inputParam.put("description",
     * "To Arthur for his personal use"); inputParam.put("notes", "To Arthur for his personal use");
     * inputParam.put("statusDesc", "0"); inputParam.put("toAccountBalance", "0");
     * inputParam.put("toExternalAccountNumber", "0"); inputParam.put("frequencyType", "Once");
     * inputParam.put("isScheduled", "0"); return inputParam; }
     */

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getRdc1DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "2750");
        inputParam.put("checkNumber", "19802");
        inputParam.put("description", "Check 19802");
        inputParam.put("notes", "Received from Jane for Rent");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("toAccountBalance", "2041");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "0");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getRdc2DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "700");
        inputParam.put("checkNumber", "32174");
        inputParam.put("description", "Check 32174");
        inputParam.put("notes", "Tuition Fee");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("toAccountBalance", "0");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "6342.14");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getRdc3DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "5000");
        inputParam.put("checkNumber", "32174");
        inputParam.put("description", "Deposit");
        inputParam.put("notes", "Principal Deposit");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("toAccountBalance", "12329.23");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "12329.23");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getLoan1DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "317.94");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Payment");
        inputParam.put("notes", "Payment");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "3141");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getLoan2DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "316.72");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Payment");
        inputParam.put("notes", "Payment");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "4123");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getLoan3DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "315.48");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Payment");
        inputParam.put("notes", "Payment");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "531");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getLoan4DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "314.25");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Tax");
        inputParam.put("notes", "County Tax");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "613");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getLoan5DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "311.90");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Payment");
        inputParam.put("notes", "Payment");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "1781");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getLoan6DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "308.23");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Payment");
        inputParam.put("notes", "Payment");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "4112");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getLoan7DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "307.18");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Payment");
        inputParam.put("notes", "Payment");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "1445");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getLoan8DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "20000");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Setup");
        inputParam.put("notes", "New Loan Setup");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "1512");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getDeposit1DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "0.23");
        inputParam.put("description", "Interest On Term Deposit");
        inputParam.put("notes", "Interest On Term Deposit");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "7329.23");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getDeposit2DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "0.18");
        inputParam.put("description", "Interest On Term Deposit");
        inputParam.put("notes", "Interest On Term Deposit");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "7329.41");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getDeposit3DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "0.17");
        inputParam.put("description", "Interest On Term Deposit");
        inputParam.put("notes", "Interest On Term Deposit");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "7329.58");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getSInternal1DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "500");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Internal Transfer Pro Checking");
        inputParam.put("notes", "Taking some money to cover expenses");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("toAccountBalance", "8563");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "2419");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "1");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getSInternal2DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "1600");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Internal Transfer Savings Plus");
        inputParam.put("notes", "Saving for a car");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("toAccountBalance", "3123");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "7364");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "1");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getSInternal3DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "3500");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Internal Transfer Freedom Credit Card");
        inputParam.put("notes", "Jan Credit Card Bill");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("toAccountBalance", "3123");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "7364");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "1");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getSInternal4DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "2000");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Internal Transfer to Freedom Credit Card");
        inputParam.put("notes", "For May Bill");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("toAccountBalance", "3123");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "7364");
        inputParam.put("viewReportLink", "http://http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "1");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getSExt1DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "100");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Online External Fund Transfer to JohnBailey");
        inputParam.put("notes", "For Investments");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("toAccountBalance", "3123");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "7364");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "1");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getSExt2DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "511");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "To Dan for his personal use");
        inputParam.put("notes", "To Dan for his personal use");
        inputParam.put("statusDesc", "0");
        inputParam.put("toAccountBalance", "0");
        inputParam.put("frequencyType", "Once");
        inputParam.put("isScheduled", "1");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getSExt3DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "1254");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "To Tom for his wedding");
        inputParam.put("notes", "To Tom for his wedding");
        inputParam.put("statusDesc", "0");
        inputParam.put("toAccountBalance", "0");
        inputParam.put("frequencyType", "Once");
        inputParam.put("isScheduled", "1");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getSExt4DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "455");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "To Dan for his personal use");
        inputParam.put("notes", "To Dan for his personal use");
        inputParam.put("statusDesc", "0");
        inputParam.put("toAccountBalance", "0");
        inputParam.put("frequencyType", "Once");
        inputParam.put("isScheduled", "1");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getSBill1DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "250");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Bill Pay to CitiBank Credit Card");
        inputParam.put("notes", "Citi Credit Card Bill");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("toAccountBalance", "0");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "9479");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("frequencyStartDate", "2018-01-10");
        inputParam.put("isScheduled", "1");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getSBill2DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "149");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Home wifi bill payment");
        inputParam.put("notes", "Home wifi bill payment");
        inputParam.put("statusDesc", "0");
        inputParam.put("toAccountBalance", "0");
        inputParam.put("frequencyType", "Once");
        inputParam.put("isScheduled", "1");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getSBill3DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "110");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Electricity bill payment");
        inputParam.put("notes", "Electricity bill payment");
        inputParam.put("statusDesc", "0");
        inputParam.put("toAccountBalance", "0");
        inputParam.put("frequencyType", "Once");
        inputParam.put("isScheduled", "1");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getSP2p1DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "107");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "To Arthur for his personal use");
        inputParam.put("notes", "To Arthur for his personal use");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("toAccountBalance", "0");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "9479");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "1");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getSP2p2DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "186");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "For golf bet");
        inputParam.put("notes", "For golf bet");
        inputParam.put("statusDesc", "0");
        inputParam.put("toAccountBalance", "0");
        inputParam.put("frequencyType", "Once");
        inputParam.put("fromAccountBalance", "9479");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "1");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getSP2p3DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("amount", "51");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "To Arthur for his personal use");
        inputParam.put("notes", "To Arthur for his personal use");
        inputParam.put("statusDesc", "0");
        inputParam.put("toAccountBalance", "0");
        inputParam.put("frequencyType", "Once");
        inputParam.put("isScheduled", "1");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getAddress1DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("Region_id", "0");
        inputParam.put("isPreferredAddress", "1");
        inputParam.put("City_id", "0");
        inputParam.put("cityName", "Santa Clara");
        inputParam.put("addressLine1", "210,Cowper Street");
        inputParam.put("addressLine2", "Palo Alto");
        inputParam.put("zipCode", "94301");
        inputParam.put("country", "United States");
        inputParam.put("type", "home");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getPhone1DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("receivePromotions", "1");
        inputParam.put("isPrimary", "1");
        inputParam.put("phoneNumber", "4258303691");
        inputParam.put("extension", "Mobile");
        inputParam.put("countryType", "Domestic");
        inputParam.put("type", "Mobile");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getUserBill1DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("billDueDate", "2019-05-18");
        inputParam.put("paidDate", "2017-12-20");
        inputParam.put("description", "Mobile T");
        inputParam.put("dueAmount", "1200.00");
        inputParam.put("paidAmount", "1080.00");
        inputParam.put("balanceAmount", "120.00");
        inputParam.put("minimumDue", "10.00");
        inputParam.put("ebillURL", "http://pmqa.konylabs.net:8080/KonyWebBanking/billpay_ebill.png");
        inputParam.put("statusDesc", "TRANSACTION_STATUS_SUCCESSFUL");
        inputParam.put("billGeneratedDate", "2016-12-21");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getNotification1DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("isRead", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getNotification2DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("isRead", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getNotification3DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("isRead", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getNotification4DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("isRead", "0");
        return inputParam;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getNotification5DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("isRead", "0");
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
}