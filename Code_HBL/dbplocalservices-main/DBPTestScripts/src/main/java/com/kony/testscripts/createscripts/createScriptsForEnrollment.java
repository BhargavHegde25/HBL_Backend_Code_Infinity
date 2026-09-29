package com.kony.testscripts.createscripts;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.security.SecureRandom;

import org.apache.commons.lang3.StringUtils;

import com.kony.eum.dbputilities.customersecurityservices.CreateCustomerCommunication;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * 
 * @author sowmya.vandanapu
 *
 */
public class createScriptsForEnrollment implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse)
            throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        String firstName = StringUtils.isNotBlank(inputParams.get("firstName")) ? inputParams.get("firstName")
                : dcRequest.getParameter("firstName");
        String lastName = StringUtils.isNotBlank(inputParams.get("lastName")) ? inputParams.get("lastName")
                : dcRequest.getParameter("lastName");
        String dateOfBirth = StringUtils.isNotBlank(inputParams.get("dateOfBirth")) ? inputParams.get("dateOfBirth")
                : dcRequest.getParameter("dateOfBirth");
        String phone = StringUtils.isNotBlank(inputParams.get("phone")) ? inputParams.get("phone")
                : dcRequest.getParameter("phone");
        String email = StringUtils.isNotBlank(inputParams.get("email")) ? inputParams.get("email")
                : dcRequest.getParameter("email");
        String phoneCountryCode =
                StringUtils.isNotBlank(inputParams.get("phoneCountryCode")) ? inputParams.get("phoneCountryCode")
                        : dcRequest.getParameter("phoneCountryCode");

        // customer create

        Calendar usercal = Calendar.getInstance();
        Map<String, String> inputParam = new HashMap<>();
        String id = HelperMethods.generateUniqueCustomerId(dcRequest);
        inputParam.put("id", id);
        inputParam.put("UserName", id);
        inputParam.put("Status_id", DBPUtilitiesConstants.CUSTOMER_STATUS_NEW);
        inputParam.put("Bank_id", "1");
        inputParam.put("isEnrolled", "false");
        inputParam.put("FirstName", firstName);
        inputParam.put("LastName", lastName);
        inputParam.put("Ssn", String.valueOf(getSSN()));
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
        inputParam.put("Lastlogintime", HelperMethods.getFormattedTimeStamp(new Date(), null));
        inputParam.put("CurrentLoginTime", HelperMethods.getFormattedTimeStamp(new Date(), null));
        inputParam.put("DateOfBirth", dateOfBirth);
        inputParam.put("isEnrolledFromSpotlight", "1");

        result = HelperMethods.callApi(dcRequest, inputParam, HelperMethods.getHeaders(dcRequest),
                URLConstants.USER_CREATE);

        // customer communication create

        inputParam.clear();
        inputParam.put("Email", email);
        inputParam.put("Phone", ("+" + phoneCountryCode + "-" + phone));
        inputParam.put("id", id);
        CreateCustomerCommunication.invoke(inputParam, dcRequest);

        createAccounts(dcRequest, id, id);
        return result;
    }

    public int getSSN() {
        int n = 1000000000 + new SecureRandom().nextInt(900000000);
        return n;
    }

    private void createAccounts(DataControllerRequest dcRequest, String userId, String userName)
            throws HttpCallException {
        String accountNum = null;
        Calendar cal = Calendar.getInstance();
        Map<String, String> input = getAccount1DemoData();
        accountNum = getDummyAccountId();
        cal.add(Calendar.DATE, -1);
        input.put("Account_id", accountNum);
        // input.put("AccountName",input.get("AccountName") + "-X" +
        // accountNum.substring(accountNum.length()-4));
        input.put("User_id", userId);
        input.put("Product_id", "8");
        input.put("Bank_id", "1");
        input.put("LastPaymentDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        String accHolderName = "{\"username\": \"" + userName + "\", \"fullname\": \"" + input.get("AccountHolder")
                + "\"}";
        input.put("AccountHolder", accHolderName);
        String jointHolderNames = null;
        String accHolder2 = input.get("AccountHolder2");
        jointHolderNames = "[" + "{\"username\": \"" + "john.Doe" + "\", \"fullname\": \"" + accHolder2 + "\"}" + "]";
        input.put("JointHolders", jointHolderNames);
        input.put("DividendLastPaidDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        input.put("EStatementmentEnable", "false");
        input.put("ownership", "Single");

        HelperMethods.removeNullValues(input);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_ACCOUNTS_CREATE);

        input = getAccount2DemoData();
        accountNum = getDummyAccountId();
        cal.add(Calendar.DATE, -1);
        input.put("Account_id", accountNum);
        // input.put("AccountName",input.get("AccountName") + "-X" +
        // accountNum.substring(accountNum.length()-4));
        input.put("User_id", userId);
        input.put("Product_id", "11");
        input.put("Bank_id", "1");
        input.put("LastPaymentDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        accHolderName = "{\"username\": \"" + userName + "\", \"fullname\": \"" + input.get("AccountHolder") + "\"}";
        input.put("AccountHolder", accHolderName);
        accHolder2 = input.get("AccountHolder2");
        jointHolderNames = "[" + "{\"username\": \"" + "john.Doe" + "\", \"fullname\": \"" + accHolder2 + "\"}" + "]";
        input.put("JointHolders", jointHolderNames);
        input.put("DividendLastPaidDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        input.put("EStatementmentEnable", "false");
        input.put("ownership", "Single");

        HelperMethods.removeNullValues(input);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_ACCOUNTS_CREATE);

        accountNum = getDummyAccountId();
        cal.add(Calendar.DATE, -1);
        input.put("Account_id", accountNum);
        // input.put("AccountName",input.get("AccountName") + "-X" +
        // accountNum.substring(accountNum.length()-4));
        input.put("User_id", userId);
        input.put("Product_id", "2");
        input.put("Bank_id", "1");
        input.put("LastPaymentDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        accHolderName = "{\"username\": \"" + userName + "\", \"fullname\": \"" + input.get("AccountHolder") + "\"}";
        input.put("AccountHolder", accHolderName);
        accHolder2 = input.get("AccountHolder2");
        jointHolderNames = "[" + "{\"username\": \"" + "john.Doe" + "\", \"fullname\": \"" + accHolder2 + "\"}" + "]";
        input.put("JointHolders", jointHolderNames);
        input.put("DividendLastPaidDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        input.put("EStatementmentEnable", "false");
        input.put("ownership", "Single");

        HelperMethods.removeNullValues(input);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_ACCOUNTS_CREATE);

        accountNum = getDummyAccountId();
        cal.add(Calendar.DATE, -1);
        input.put("Account_id", accountNum);
        // input.put("AccountName",input.get("AccountName") + "-X" +
        // accountNum.substring(accountNum.length()-4));
        input.put("User_id", userId);
        input.put("Product_id", "5");
        input.put("Bank_id", "1");
        input.put("LastPaymentDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        accHolderName = "{\"username\": \"" + userName + "\", \"fullname\": \"" + input.get("AccountHolder") + "\"}";
        input.put("AccountHolder", accHolderName);
        accHolder2 = input.get("AccountHolder2");
        jointHolderNames = "[" + "{\"username\": \"" + "john.Doe" + "\", \"fullname\": \"" + accHolder2 + "\"}" + "]";
        input.put("JointHolders", jointHolderNames);
        input.put("DividendLastPaidDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        input.put("EStatementmentEnable", "false");
        input.put("ownership", "Single");

        HelperMethods.removeNullValues(input);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_ACCOUNTS_CREATE);

        accountNum = getDummyAccountId();
        cal.add(Calendar.DATE, -1);
        input.put("Account_id", accountNum);
        // input.put("AccountName",input.get("AccountName") + "-X" +
        // accountNum.substring(accountNum.length()-4));
        input.put("User_id", userId);
        input.put("Product_id", "4");
        input.put("Bank_id", "1");
        input.put("LastPaymentDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        accHolderName = "{\"username\": \"" + userName + "\", \"fullname\": \"" + input.get("AccountHolder") + "\"}";
        input.put("AccountHolder", accHolderName);
        accHolder2 = input.get("AccountHolder2");
        jointHolderNames = "[" + "{\"username\": \"" + "john.Doe" + "\", \"fullname\": \"" + accHolder2 + "\"}" + "]";
        input.put("JointHolders", jointHolderNames);
        input.put("DividendLastPaidDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        input.put("EStatementmentEnable", "false");
        input.put("ownership", "Single");

        HelperMethods.removeNullValues(input);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_ACCOUNTS_CREATE);
    }

    private String getDummyAccountId() {
        SimpleDateFormat idformatter = new SimpleDateFormat("yyMMddHHmmssSSS");
        return idformatter.format(new Date());
    }

    public Map<String, String> getAccount1DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("Type_id", "2");
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
        inputParam.put("Type_id", "1");
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
        inputParam.put("Type_id", "3");
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
        inputParam.put("Type_id", "6");
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
}
