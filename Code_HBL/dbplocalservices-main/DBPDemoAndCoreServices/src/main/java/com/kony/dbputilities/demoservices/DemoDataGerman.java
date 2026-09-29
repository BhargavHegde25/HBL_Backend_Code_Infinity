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

public class DemoDataGerman {

    private Map<String, String> generatedAccounts;
    private Map<String, String> generatedPayees;
    private Map<String, String> generatedPersons;
    private Map<String, String> generatedBills;
    private Map<String, String> accountTypes;
    private Map<String, String> transactionTypes;
    private Map<String, String> generatedExternalAccounts;

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

    public Map<String, String> getUser1DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("userfirstname", "Rudolf");
        inputParam.put("userlastname", "Schenker");
        inputParam.put("gender", "Male");
        inputParam.put("dateOfBirth", "1983-08-31");
        inputParam.put("PersonalIDType", "BSN (Burgerservicenummer)");
        inputParam.put("ssn", "T22000129");
        inputParam.put("email", "user.rschenker@gmail.com");
        inputParam.put("phone", "+91-1752006714");
        inputParam.put("phoneExtension", "1752006714");
        inputParam.put("phoneCountryCode", "+49");
        inputParam.put("secondaryemail", "");
        inputParam.put("secondaryphone", "");
        inputParam.put("secondaryemail2", "");
        inputParam.put("secondaryphone2", "");
        inputParam.put("addressLine1", "Rellinger Str. 25");
        inputParam.put("addressLine2", "");
        inputParam.put("city", "Hamburg");
        inputParam.put("state", "Hamburg");
        inputParam.put("countryCode", "DE");
        inputParam.put("country", "Deutschland");
        inputParam.put("zipcode", "20257");
        // inputParam.put("userImageURL", "https://retailbanking1.konycloud.com/dbimages/displayPicture.png");
        inputParam.put("lastlogintime", "2018-11-23");
        inputParam.put("areUserAlertsTurnedOn", "0");
        inputParam.put("areDepositTermsAccepted", "0");
        inputParam.put("areAccountStatementTermsAccepted", "0");
        inputParam.put("default_account_desposit", "FR52 3000 6000 0112 3400 7890 900");
        inputParam.put("default_account_transfers", "FR52 3000 6000 0112 3400 7890 900");
        inputParam.put("default_account_payments", "FR52 3000 6000 0112 3400 7890 900");
        inputParam.put("default_account_cardless", "FR52 3000 6000 0112 3400 7890 900");
        inputParam.put("isPhoneEnabled", "0");
        inputParam.put("isBillPaySupported", "1");
        inputParam.put("isP2PSupported", "1");
        inputParam.put("isBillPayActivated", "1");
        inputParam.put("isP2PActivated", "1");
        inputParam.put("isWireTransferEligible", "1");
        inputParam.put("isEmailEnabled", "1");
        inputParam.put("showBillPayFromAccPopup", "1");

        return inputParam;

    }

    public Map<String, String> getCustomerDemoData() {
        Map<String, String> inputParam = new HashMap<>();

        inputParam.put("FirstName", "Rudolf");
        inputParam.put("LastName", "Schenker");
        // inputParam.put("UserImageURL", "https://retailbanking1.konycloud.com/dbimages/displayPicture.png");

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

        inputParam.put("Lastlogintime", "2018-11-23");
        inputParam.put("DateOfBirth", "1983-08-31");
        return inputParam;
    }

    public Map<String, String> getCustomerCommunicationDemoData() {
        Map<String, String> inputParam = new HashMap<>();

        inputParam.put("Email", "user.rschenker@gmail.com");
        inputParam.put("Phone", "+91-1752006714");
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
        inputParam.put("Region_id", "Deutschland");
        inputParam.put("isPreferredAddress", "1");
        inputParam.put("City_id", "C4");
        inputParam.put("cityName", "Hamburg");
        inputParam.put("addressLine1", "Rellinger Str. 25");
        inputParam.put("addressLine2", "");
        inputParam.put("zipCode", "20257");
        inputParam.put("country", "Deutschland");
        inputParam.put("type", "home");
        return inputParam;
    }

    public Map<String, String> getPhone1DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("receivePromotions", "1");
        inputParam.put("isPrimary", "1");
        inputParam.put("phoneNumber", "+91-1752006714");
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

    public Map<String, String> getUser2DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("userfirstname", "Diane");
        inputParam.put("userlastname", "Kruger");
        inputParam.put("gender", "Female");
        inputParam.put("dateOfBirth", "1980-10-03");
        inputParam.put("PersonalIDType", "BSN (Burgerservicenummer)");
        inputParam.put("ssn", "T43012345");
        inputParam.put("email", "user.dkruger@gmail.com");
        inputParam.put("phone", "+91-1752006714");
        inputParam.put("phoneExtension", "1752006714");
        inputParam.put("phoneCountryCode", "+49");
        inputParam.put("secondaryemail", "");
        inputParam.put("secondaryphone", "");
        inputParam.put("secondaryemail2", "");
        inputParam.put("secondaryphone2", "");
        inputParam.put("addressLine1", "Rellinger Str. 25");
        inputParam.put("addressLine2", "");
        inputParam.put("city", "Hamburg");
        inputParam.put("state", "Hamburg");
        inputParam.put("countryCode", "DE");
        inputParam.put("country", "Deutschland");
        inputParam.put("zipcode", "69100");
        // inputParam.put("userImageURL", "https://retailbanking1.konycloud.com/dbimages/displayPicture.png");
        inputParam.put("lastlogintime", "2018-11-23");
        inputParam.put("areUserAlertsTurnedOn", "0");
        inputParam.put("areDepositTermsAccepted", "0");
        inputParam.put("areAccountStatementTermsAccepted", "0");
        inputParam.put("default_account_desposit", "GB65 MIDL 0700 9312 3456 90");
        inputParam.put("default_account_transfers", "GB65 MIDL 0700 9312 3456 90");
        inputParam.put("default_account_payments", "GB65 MIDL 0700 9312 3456 90");
        inputParam.put("default_account_cardless", "GB65 MIDL 0700 9312 3456 90");
        inputParam.put("isPhoneEnabled", "0");
        inputParam.put("isBillPaySupported", "1");
        inputParam.put("isP2PSupported", "1");
        inputParam.put("isBillPayActivated", "1");
        inputParam.put("isP2PActivated", "1");
        inputParam.put("isWireTransferEligible", "1");
        inputParam.put("isEmailEnabled", "1");
        inputParam.put("showBillPayFromAccPopup", "1");

        return inputParam;

    }

    public Map<String, String> getAccount1DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("Type_id", "Savings");
        inputParam.put("Name", "Savings");
        inputParam.put("AccountName", "Sparkonto");
        inputParam.put("NickName", "Meine Ersparnisse");
        inputParam.put("OpeningDate", "2016-11-23");
        inputParam.put("closingDate", "");
        inputParam.put("StatusDesc", "Active");
        inputParam.put("FavouriteStatus", "");
        inputParam.put("AvailableBalance", "57300");
        inputParam.put("CurrentBalance", "57300");
        inputParam.put("PendingDeposit", "500");
        inputParam.put("PendingWithdrawal", "0");
        inputParam.put("lastStatementBalance", "");
        inputParam.put("ShowTransactions", "1");
        inputParam.put("SupportBillPay", "1");
        inputParam.put("SupportDeposit", "1");
        inputParam.put("SupportTransferFrom", "1");
        inputParam.put("SupportTransferTo", "1");
        inputParam.put("SupportCardlessCash", "1");
        inputParam.put("TransactionLimit", "500");
        inputParam.put("TransferLimit", "");
        inputParam.put("IsPFM", "");
        inputParam.put("EStatementEnable", "");
        inputParam.put("AccountPreference", "");
        inputParam.put("CurrencyCode", "EUR");
        inputParam.put("AccountHolder", "Rudolf Schenker");
        inputParam.put("RoutingNumber", "");
        inputParam.put("SwiftCode", "");
        inputParam.put("BsbNum", "");
        inputParam.put("JointHolders", "Diane Kruger");
        inputParam.put("DividendRate", "0.01");
        inputParam.put("DividendYTD", "4.5");
        inputParam.put("LastDividendPaidAmount", "3.5");
        inputParam.put("lastDividendPaidDate", "2018-07-01");
        inputParam.put("PreviousYearDividend", "3");
        inputParam.put("DividendPaidYTD", "0");
        inputParam.put("DividendLastPaidAmount", "3.5");
        inputParam.put("DividendLastPaidDate", "2018-07-01");
        inputParam.put("PreviousYearsDividends", "3.2");
        inputParam.put("AvailableCredit", "");
        inputParam.put("dueDate", "");
        inputParam.put("minimumDue", "");
        inputParam.put("outstandingBalance", "");
        inputParam.put("creditCardNumber", "");
        inputParam.put("availablePoints", "500");
        inputParam.put("currentAmountDue", "");
        inputParam.put("lastPaymentAmount", "");
        inputParam.put("lateFeesDue", "0");
        inputParam.put("creditLimit", "");
        inputParam.put("interestRate", "");
        inputParam.put("maturityDate", "");
        inputParam.put("maturityOption", "");
        inputParam.put("bondInterest", "");
        inputParam.put("bondInterestLastYear", "");
        inputParam.put("TotalCreditMonths", "3000");
        inputParam.put("TotalDebitsMonth", "2800");
        inputParam.put("interestEarned", "2");
        inputParam.put("maturityAmount", "");
        inputParam.put("paymentTerm", "");
        inputParam.put("principalValue", "");
        inputParam.put("paymentDue", "");
        inputParam.put("interestPaidYTD", "");
        inputParam.put("interestPaidPreviousYTD", "");
        inputParam.put("unpaidInterest", "");
        inputParam.put("regularPaymentAmount", "");
        inputParam.put("principalBalance", "");
        inputParam.put("originalAmount", "");
        inputParam.put("payoffAmount", "");
        inputParam.put("payOffCharge", "");
        inputParam.put("interestPaidLastYear", "");
        inputParam.put("bankName", "Infinity Europe");
        inputParam.put("IBAN", "DE22 1000 0000 0123 4567 70");
        inputParam.put("Sort Code", "N/A");
        inputParam.put("adminProductId", "");
        inputParam.put("AccountHolder2", "John Doe");
        return inputParam;
    }

    public Map<String, String> getAccount2DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("Type_id", "Checking");
        inputParam.put("Name", "Checking");
        inputParam.put("AccountName", "Girokonto");
        inputParam.put("NickName", "Meine Girokonto");
        inputParam.put("OpeningDate", "2016-11-23");
        inputParam.put("closingDate", "");
        inputParam.put("StatusDesc", "Active");
        inputParam.put("FavouriteStatus", "");
        inputParam.put("AvailableBalance", "4500");
        inputParam.put("CurrentBalance", "4612");
        inputParam.put("PendingDeposit", "200");
        inputParam.put("PendingWithdrawal", "112");
        inputParam.put("lastStatementBalance", "");
        inputParam.put("ShowTransactions", "1");
        inputParam.put("SupportBillPay", "1");
        inputParam.put("SupportDeposit", "1");
        inputParam.put("SupportTransferFrom", "1");
        inputParam.put("SupportTransferTo", "1");
        inputParam.put("SupportCardlessCash", "1");
        inputParam.put("TransactionLimit", "500");
        inputParam.put("TransferLimit", "");
        inputParam.put("IsPFM", "");
        inputParam.put("EStatementEnable", "");
        inputParam.put("AccountPreference", "");
        inputParam.put("CurrencyCode", "EUR");
        inputParam.put("AccountHolder", "Rudolf Schenker");
        inputParam.put("RoutingNumber", "");
        inputParam.put("SwiftCode", "");
        inputParam.put("BsbNum", "");
        inputParam.put("JointHolders", "Diane Kruger");
        inputParam.put("DividendRate", "0.01");
        inputParam.put("DividendYTD", "4.5");
        inputParam.put("LastDividendPaidAmount", "3.5");
        inputParam.put("lastDividendPaidDate", "2018-07-01");
        inputParam.put("PreviousYearDividend", "3");
        inputParam.put("DividendPaidYTD", "0");
        inputParam.put("DividendLastPaidAmount", "3.5");
        inputParam.put("dividendLastPaidDate", "2018-07-01");
        inputParam.put("PreviousYearsDividends", "3.2");
        inputParam.put("availableCredit", "");
        inputParam.put("dueDate", "");
        inputParam.put("minimumDue", "");
        inputParam.put("outstandingBalance", "");
        inputParam.put("creditCardNumber", "");
        inputParam.put("availablePoints", "500");
        inputParam.put("currentAmountDue", "");
        inputParam.put("lastPaymentAmount", "");
        inputParam.put("lateFeesDue", "0");
        inputParam.put("creditLimit", "");
        inputParam.put("interestRate", "");
        inputParam.put("maturityDate", "");
        inputParam.put("maturityOption", "");
        inputParam.put("bondInterest", "");
        inputParam.put("bondInterestLastYear", "");
        inputParam.put("TotalCreditMonths", "3000");
        inputParam.put("TotalDebitsMonth", "2800");
        inputParam.put("interestEarned", "2");
        inputParam.put("maturityAmount", "");
        inputParam.put("paymentTerm", "");
        inputParam.put("principalValue", "");
        inputParam.put("paymentDue", "");
        inputParam.put("interestPaidYTD", "");
        inputParam.put("interestPaidPreviousYTD", "");
        inputParam.put("unpaidInterest", "");
        inputParam.put("regularPaymentAmount", "");
        inputParam.put("principalBalance", "");
        inputParam.put("originalAmount", "");
        inputParam.put("payoffAmount", "");
        inputParam.put("payOffCharge", "");
        inputParam.put("interestPaidLastYear", "");
        inputParam.put("bankName", "Infinity Europe");
        inputParam.put("IBAN", "DE64 1000 0000 0123 4567 90");
        inputParam.put("Sort Code", "N/A");
        inputParam.put("adminProductId", "");
        inputParam.put("AccountHolder2", "John Doe");
        return inputParam;
    }

    public Map<String, String> getUserBill1DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("billDueDate", "2019-05-18");
        inputParam.put("paidDate", "2017-12-20");
        inputParam.put("description", "AT&T_Mobile Phone ");
        inputParam.put("dueAmount", "1200.00");
        inputParam.put("paidAmount", "1080.00");
        inputParam.put("balanceAmount", "120.00");
        inputParam.put("minimumDue", "10.00");
        inputParam.put("ebillURL", "http://retailbanking1.konycloud.com/dbimages/billpay_ebill.png");
        inputParam.put("statusDesc", "TRANSACTION_STATUS_SUCCESSFUL");
        inputParam.put("billGeneratedDate", "2016-12-21");
        inputParam.put("currencyCode", "EUR");
        return inputParam;
    }

    public Map<String, String> getUserBill2DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("billDueDate", "2019-09-21");
        inputParam.put("paidDate", "2017-12-20");
        inputParam.put("description", "Electricity - ABC");
        inputParam.put("dueAmount", "1570.00");
        inputParam.put("paidAmount", "1000.00");
        inputParam.put("balanceAmount", "570.00");
        inputParam.put("minimumDue", "10.00");
        inputParam.put("ebillURL", "http://retailbanking1.konycloud.com/dbimages/billpay_ebill.png");
        inputParam.put("statusDesc", "TRANSACTION_STATUS_SUCCESSFUL");
        inputParam.put("billGeneratedDate", "2017-09-18");
        inputParam.put("currencyCode", "EUR");
        return inputParam;
    }

    public Map<String, String> getInternalTransfer_SAV1DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternalTransfer");
        inputParam.put("fromAccountNumber", "DE22 1000 0000 0123 4567 70");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("transactionCurrency", "EUR");
        inputParam.put("amount", "300.00");
        inputParam.put("baseCurrency", "EUR");
        inputParam.put("convertedAmount", "300.00");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Overdracht naar spaargeld");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "57300.00");
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

    public Map<String, String> getInterest_SAV5DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Interest");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "DE22 1000 0000 0123 4567 70");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("transactionCurrency", "EUR");
        inputParam.put("amount", "300.00");
        inputParam.put("baseCurrency", "EUR");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Overdracht naar spaargeld");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1719.00");
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

    public Map<String, String> getPayee1DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("accountNumber", "7790396982957");
        inputParam.put("addressLine1", "ABC Energy LLC");
        inputParam.put("addressLine2", "Merrion St");
        inputParam.put("state", "West Yorkshire");
        inputParam.put("cityName", "Leeds");
        inputParam.put("nickName", "Electricity - ABC");
        inputParam.put("companyName", "ABC Energy");
        inputParam.put("email", "electricity@gmail.com");
        inputParam.put("firstName", "Electricity");
        inputParam.put("lastName", "Bill");
        inputParam.put("name", "ABC Energy");
        inputParam.put("phone", "+91-4351348905");
        inputParam.put("zipCode", "759634");
        inputParam.put("eBillEnable", "1");
        inputParam.put("isAutoPayEnabled", "0");
        inputParam.put("nameOnBill", "John Bailey");
        inputParam.put("billerId", "");

        return inputParam;
    }

    public Map<String, String> getPayee2DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("accountNumber", "158930572850356");
        inputParam.put("addressLine1", "Max Life Family Insurance");
        inputParam.put("addressLine2", "Merrion St");
        inputParam.put("state", "West Yorkshire");
        inputParam.put("cityName", "Leeds");
        inputParam.put("nickName", "Family Insurance");
        inputParam.put("companyName", "Max Life Family Insurance");
        inputParam.put("email", "insurance@gmail.com");
        inputParam.put("firstName", "Family");
        inputParam.put("lastName", "Insurance");
        inputParam.put("name", "Max Life Family Insurance");
        inputParam.put("phone", "+91-1501273981272");
        inputParam.put("zipCode", "459667");
        inputParam.put("eBillEnable", "0");
        inputParam.put("isAutoPayEnabled", "0");
        inputParam.put("nameOnBill", "John Bailey");
        inputParam.put("billerId", "");

        return inputParam;
    }

    public Map<String, String> getPayee3DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("accountNumber", "6890396982929");
        inputParam.put("addressLine1", "CITI Business");
        inputParam.put("addressLine2", "Merrion St");
        inputParam.put("state", "West Yorkshire");
        inputParam.put("cityName", "Leeds");
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
        inputParam.put("billerId", "");

        return inputParam;
    }

    public Map<String, String> getPayee4DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("accountNumber", "6890396982929");
        inputParam.put("addressLine1", "AT&T");
        inputParam.put("addressLine2", "Merrion St");
        inputParam.put("state", "West Yorkshire");
        inputParam.put("cityName", "Leeds");
        inputParam.put("nickName", "AT&T_Mobile Phone");
        inputParam.put("companyName", "AT&T Inc");
        inputParam.put("email", "cableBill@gmail.com");
        inputParam.put("firstName", "My Mobile");
        inputParam.put("lastName", "Bill");
        inputParam.put("name", "AT&T Mobile");
        inputParam.put("phone", "+91-938899898");
        inputParam.put("zipCode", "42110");
        inputParam.put("eBillEnable", "1");
        inputParam.put("isAutoPayEnabled", "0");
        inputParam.put("nameOnBill", "John Bailey");
        inputParam.put("billerId", "");

        return inputParam;
    }

    public Map<String, String> getPayee5DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("accountNumber", "11433468761745");
        inputParam.put("addressLine1", "514S,");
        inputParam.put("addressLine2", "Merrion St");
        inputParam.put("state", "West Yorkshire");
        inputParam.put("cityName", "Leeds");
        inputParam.put("nickName", "City Water Works");
        inputParam.put("companyName", "West Yorkshire");
        inputParam.put("email", "rent@gmail.com");
        inputParam.put("firstName", "Water");
        inputParam.put("lastName", "Works");
        inputParam.put("name", "Water Works");
        inputParam.put("phone", "+91-65817436791");
        inputParam.put("zipCode", "42110");
        inputParam.put("eBillEnable", "1");
        inputParam.put("isAutoPayEnabled", "0");
        inputParam.put("nameOnBill", "John Bailey");
        inputParam.put("billerId", "");

        return inputParam;
    }

    public Map<String, String> getPayee6DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("accountNumber", "9611932872351");
        inputParam.put("addressLine1", "44 Shirley Ave");
        inputParam.put("addressLine2", "Merrion St");
        inputParam.put("cityName", "Leeds");
        inputParam.put("state", "West Yorkshire");
        inputParam.put("nickName", "City Utilities Company");
        inputParam.put("companyName", "Beam Teleservices");
        inputParam.put("email", "wifi@gmail.com");
        inputParam.put("firstName", "City");
        inputParam.put("lastName", "Utilities");
        inputParam.put("name", "Beam Teleservices");
        inputParam.put("phone", "+91-4523746832");
        inputParam.put("zipCode", "50098");
        inputParam.put("eBillEnable", "0");
        inputParam.put("isAutoPayEnabled", "0");
        inputParam.put("nameOnBill", "John Bailey");
        inputParam.put("billerId", "");

        return inputParam;
    }

    public Map<String, String> getPerson1DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("email", "judy.blume@gmail.com");
        inputParam.put("name", "Judy Blume");
        inputParam.put("nickName", "Judy");
        inputParam.put("phone", "+91-8790146356");
        inputParam.put("secondaryEmail", "jblume@gmail.com");
        inputParam.put("secondoryPhoneNumber", "+91-4567483920");
        inputParam.put("primaryContactForSending", "8790146356");

        return inputParam;
    }

    public Map<String, String> getPerson2DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("email", "");
        inputParam.put("name", "Arthur Miller");
        inputParam.put("nickName", "Arthur");
        inputParam.put("phone", "+91-6565178276");
        inputParam.put("secondaryEmail", "");
        inputParam.put("secondoryPhoneNumber", "");
        inputParam.put("primaryContactForSending", "6565178276");

        return inputParam;
    }

    public Map<String, String> getPerson3DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("email", "francis@gmail.com");
        inputParam.put("name", "Francis Ford");
        inputParam.put("nickName", "Francis F");
        inputParam.put("phone", "");
        inputParam.put("secondaryEmail", "");
        inputParam.put("secondoryPhoneNumber", "");
        inputParam.put("primaryContactForSending", "Fordfrancis@gmail.com");

        return inputParam;
    }

    public Map<String, String> getPerson4DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("email", "peter@gmail.com");
        inputParam.put("name", "Peter Jackson");
        inputParam.put("nickName", "Peter J");
        inputParam.put("phone", "");
        inputParam.put("secondaryEmail", "JacksonP@yahoo.com");
        inputParam.put("secondoryPhoneNumber", "");
        inputParam.put("primaryContactForSending", "peter@gmail.com");

        return inputParam;
    }

       public Map<String, String> getCard1DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("action", "Activate");
        inputParam.put("card_Status", "Active");
        inputParam.put("availableCredit", "87161.00");
        inputParam.put("serviceProvider", "Visa");
        inputParam.put("billingAddress", "Merrion St, Leeds, West Yorkshire, United Kingdom");
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
        inputParam.put("billingAddress", "Merrion St, Leeds, West Yorkshire, United Kingdom");
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
        inputParam.put("billingAddress", "Merrion St, Leeds, West Yorkshire, United Kingdom");
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
        inputParam.put("billingAddress", "Merrion St, Leeds, West Yorkshire, United Kingdom");
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
        inputParam.put("billingAddress", "Merrion St, Leeds, West Yorkshire, United Kingdom");
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
        inputParam.put("billingAddress", "Merrion St, Leeds, West Yorkshire, United Kingdom");
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
        inputParam.put("billingAddress", "Merrion St, Leeds, West Yorkshire, United Kingdom");
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
        inputParam.put("billingAddress", "Merrion St, Leeds, West Yorkshire, United Kingdom");
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

    public Map<String, String> getRecentCardLessWithdrawal1DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("amount", "500.00");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Cardless withdrawal to Self");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("toAccountBalance", "37000.00");
        inputParam.put("fromAccountBalance", "5792.00");
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
        inputParam.put("fromAccountNumber", "");
        return inputParam;
    }

    public Map<String, String> getRecentCardLessWithdrawal2DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("amount", "750.00");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Cardless withdrawal to Henry");
        inputParam.put("notes", "To plumber");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("toAccountBalance", "62782.00");
        inputParam.put("fromAccountBalance", "5020.00");
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
        inputParam.put("fromAccountNumber", "");
        return inputParam;
    }

    public Map<String, String> getRecentCardLessWithdrawal3DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("amount", "200.00");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Cardless withdrawal to Jane Doe");
        inputParam.put("notes", "Misc expenses");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("toAccountBalance", "25649.00");
        inputParam.put("fromAccountBalance", "9834.00");
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
        inputParam.put("fromAccountNumber", "");
        return inputParam;
    }

    public Map<String, String> getExtAccount1DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("nickName", "Alex Sion");
        inputParam.put("beneficiaryName", "Alex Sion");
        inputParam.put("accountType", "Savings");
        inputParam.put("countryName", "United Kingdom");
        inputParam.put("firstName", "Alex");
        inputParam.put("lastName", "Sion");
        inputParam.put("bankName", "ING Bank");
        inputParam.put("notes", " ");
        inputParam.put("routingNumber", "765678987");
        inputParam.put("swiftCode", "0");
        inputParam.put("isInternationalAccount", "0");
        inputParam.put("isSameBankAccount", "0");
        inputParam.put("isVerified", "1");
        inputParam.put("createdOn", "2017-10-10");
        inputParam.put("accountNumber", "GB86 UBKL 1234 5698 7654 32");
        return inputParam;
    }

    public Map<String, String> getExtAccount2DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("nickName", "Tom Purnell");
        inputParam.put("beneficiaryName", "Tom Purnell");
        inputParam.put("accountType", "Current");
        inputParam.put("countryName", "Spain");
        inputParam.put("firstName", "Tom");
        inputParam.put("lastName", "Purnell");
        inputParam.put("bankName", "BNP Paribas");
        inputParam.put("notes", " ");
        inputParam.put("routingNumber", "765678987");
        inputParam.put("swiftCode", "0");
        inputParam.put("isInternationalAccount", "0");
        inputParam.put("isSameBankAccount", "0");
        inputParam.put("isVerified", "1");
        inputParam.put("createdOn", "2016-11-07");
        inputParam.put("accountNumber", "ES26 4141 5000 3000 1210 2699");
        return inputParam;
    }

    public Map<String, String> getExtAccount3DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("nickName", "John");
        inputParam.put("beneficiaryName", "John Bailey");
        inputParam.put("accountType", "Current");
        inputParam.put("countryName", "Germany");
        inputParam.put("firstName", "John");
        inputParam.put("lastName", "Bailey");
        inputParam.put("bankName", "ING Bank");
        inputParam.put("notes", " ");
        inputParam.put("routingNumber", "765678922");
        inputParam.put("swiftCode", "0");
        inputParam.put("isInternationalAccount", "0");
        inputParam.put("isSameBankAccount", "0");
        inputParam.put("isVerified", "1");
        inputParam.put("createdOn", "2018-01-07");
        inputParam.put("accountNumber", "DE39 1564 6228 2897 4602 07");
        return inputParam;
    }

    public Map<String, String> getExtAccount4DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("nickName", "Dan Markus");
        inputParam.put("beneficiaryName", "Dan Markus");
        inputParam.put("accountType", "Current");
        inputParam.put("countryName", "France");
        inputParam.put("firstName", "Dan");
        inputParam.put("lastName", "Markus");
        inputParam.put("bankName", "CitiBank");
        inputParam.put("notes", " ");
        inputParam.put("routingNumber", "0");
        inputParam.put("swiftCode", "0");
        inputParam.put("isInternationalAccount", "0");
        inputParam.put("isSameBankAccount", "1");
        inputParam.put("isVerified", "1");
        inputParam.put("createdOn", "2015-10-13");
        inputParam.put("accountNumber", "FR94 8744 9990 8215 8677 7376 534");
        return inputParam;
    }

    public Map<String, String> getExtAccount5DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("nickName", "Henry");
        inputParam.put("beneficiaryName", "Henry James");
        inputParam.put("accountType", "Savings");
        inputParam.put("countryName", "United States of America (USA)");
        inputParam.put("firstName", "Henry");
        inputParam.put("lastName", "James");
        inputParam.put("bankName", "Bank of America");
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

    public Map<String, String> getWithdrawal_Current1DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Withdrawal");
        inputParam.put("fromAccountNumber", "DE64 1000 0000 0123 4567 90");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("transactionCurrency", "EUR");
        inputParam.put("Amount", "120");
        inputParam.put("baseCurrency", "EUR");
        inputParam.put("convertedAmount", "120");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Auszahlung am Geldautomaten");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1322");
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

    public Map<String, String> getExternalTransfer_Current2DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "ExternalTransfer");
        inputParam.put("fromAccountNumber", "DE64 1000 0000 0123 4567 90");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "GB86 UBKL 1234 5698 7654 32");
        inputParam.put("transactionCurrency", "EUR");
        inputParam.put("Amount", "112");
        inputParam.put("baseCurrency", "GBP");
        inputParam.put("convertedAmount", "100");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "externe Übertragung");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1442");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        inputParam.put("beneficiaryName", "Alex Sion");
        return inputParam;
    }

    public Map<String, String> getPOS_Current3DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "DE64 1000 0000 0123 4567 90");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("transactionCurrency", "EUR");
        inputParam.put("Amount", "50");
        inputParam.put("baseCurrency", "EUR");
        inputParam.put("convertedAmount", "50");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Vapiano Hamburg");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1554");
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

    public Map<String, String> getCardless_Current4DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Cardless");
        inputParam.put("fromAccountNumber", "DE64 1000 0000 0123 4567 90");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("transactionCurrency", "EUR");
        inputParam.put("Amount", "70");
        inputParam.put("baseCurrency", "EUR");
        inputParam.put("convertedAmount", "40");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Kino und Snacks");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1604");
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

    public Map<String, String> getBillPay_Current5DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "DE64 1000 0000 0123 4567 90");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("transactionCurrency", "EUR");
        inputParam.put("Amount", "100");
        inputParam.put("baseCurrency", "EUR");
        inputParam.put("convertedAmount", "100");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Telekommunikation");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1674");
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

    public Map<String, String> getInternetTransaction_Current6DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "DE64 1000 0000 0123 4567 90");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("transactionCurrency", "EUR");
        inputParam.put("Amount", "45");
        inputParam.put("baseCurrency", "EUR");
        inputParam.put("convertedAmount", "45");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "amazon.de");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1774");
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

    public Map<String, String> getReceivedP2P_Current7DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "ReceivedP2P");
        inputParam.put("fromAccountNumber", "DE64 1000 0000 0123 4567 90");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("transactionCurrency", "EUR");
        inputParam.put("amount", "110");
        inputParam.put("baseCurrency", "EUR");
        inputParam.put("convertedAmount", "100");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Konzertkarten");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1819");
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

    public Map<String, String> getCredit_Current8DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "DE64 1000 0000 0123 4567 90");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("transactionCurrency", "EUR");
        inputParam.put("amount", "25");
        inputParam.put("baseCurrency", "EUR");
        inputParam.put("convertedAmount", "25");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Rückerstattung amazon.de");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1709");
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

    public Map<String, String> getExternalTransfer_Current9DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "ExternalTransfer");
        inputParam.put("fromAccountNumber", "DE64 1000 0000 0123 4567 90");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "ES26 4141 5000 3000 1210 2699");
        inputParam.put("transactionCurrency", "EUR");
        inputParam.put("Amount", "300");
        inputParam.put("baseCurrency", "EUR");
        inputParam.put("convertedAmount", "100");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "externe Übertragung");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1684");
        inputParam.put("createdDate", "-8");
        inputParam.put("transactionDate", "-8");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        inputParam.put("beneficiaryName", "Tom Purnell");
        return inputParam;
    }

    public Map<String, String> getP2P_Current10DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "P2P");
        inputParam.put("fromAccountNumber", "DE64 1000 0000 0123 4567 90");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("transactionCurrency", "EUR");
        inputParam.put("Amount", "26");
        inputParam.put("baseCurrency", "EUR");
        inputParam.put("convertedAmount", "26");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bier mit meinen Freunden");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1984");
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

    public Map<String, String> getInterest_Current11DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Interest");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "DE64 1000 0000 0123 4567 90");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("transactionCurrency", "EUR");
        inputParam.put("amount", "10");
        inputParam.put("baseCurrency", "EUR");
        inputParam.put("convertedAmount", "10");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Zinserträge");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2010");
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

    public Map<String, String> getLoan_Current12DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Loan");
        inputParam.put("fromAccountNumber", "DE64 1000 0000 0123 4567 90");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("transactionCurrency", "EUR");
        inputParam.put("Amount", "700");
        inputParam.put("baseCurrency", "EUR");
        inputParam.put("convertedAmount", "700");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Autokredit");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2000");
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

    public Map<String, String> getExternalTransfer_Current13DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "ExternalTransfer");
        inputParam.put("fromAccountNumber", "DE64 1000 0000 0123 4567 90");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "DE39 1564 6228 2897 4602 07");
        inputParam.put("transactionCurrency", "EUR");
        inputParam.put("Amount", "2000.00");
        inputParam.put("baseCurrency", "EUR");
        inputParam.put("convertedAmount", "2000.00");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Miete für November");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2700.00");
        inputParam.put("createdDate", "-9");
        inputParam.put("transactionDate", "-9");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "1");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        inputParam.put("beneficiaryName", "John");
        return inputParam;
    }

    public Map<String, String> getInternalTransfer_Current14DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InternalTransfer");
        inputParam.put("fromAccountNumber", "DE64 1000 0000 0123 4567 90");
        inputParam.put("toAccountNumber", "DE22 1000 0000 0123 4567 70");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("transactionCurrency", "EUR");
        inputParam.put("Amount", "300");
        inputParam.put("baseCurrency", "EUR");
        inputParam.put("convertedAmount", "300");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Overdracht naar spaargeld");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4700");
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

    public Map<String, String> getPayroll_Current15DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Payroll");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "DE64 1000 0000 0123 4567 90");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("transactionCurrency", "EUR");
        inputParam.put("amount", "5000");
        inputParam.put("baseCurrency", "EUR");
        inputParam.put("convertedAmount", "4600");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Lohneinkommen");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "5000");
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
