package com.kony.testscripts.createscripts;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

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
public class CreateScriptsForIsAccountCentricCore implements JavaService2 {

    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();

        List<Integer> typeIdlist = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
        String ssn = String.valueOf(getSSN());

        StringBuilder sb = new StringBuilder();

        Map<String, String> accountNames = HelperMethods.getAccountsNames();
        for (int i = 0; i < 3; i++) {

            Map<String, String> input = new HashMap<>();
            String membershipId = UUID.randomUUID().toString();
            input.put("id", membershipId);

            HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                    URLConstants.MEMBERSHIP_CREATE);

            input.clear();

            String accountNum = getDummyAccountId();
            input.put("id", getRandomSuffix());
            input.put("membershipId", membershipId);
            input.put("firstName", dcRequest.getParameter("firstname"));
            input.put("lastName", dcRequest.getParameter("lastname"));
            input.put("userName", dcRequest.getParameter("firstname"));
            input.put("dateOfBirth", dcRequest.getParameter("dateOfBirth"));
            input.put("ssn", ssn);
            input.put("phone", processPhone(dcRequest.getParameter("phone")));
            input.put("email", dcRequest.getParameter("email"));
            input.put("memberType", DBPUtilitiesConstants.MEMBERTYPE_PRIMARY);
            input.put("memberTypeId", DBPUtilitiesConstants.MEMBERTYPE_PRIMARY);
            input.put("memberTypeName", DBPUtilitiesConstants.MEMBERTYPE_PRIMARY);

            HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                    URLConstants.MEMBERSHIPOWNER_CREATE);

            input.clear();
            input = getAccount1DemoData();
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            input.put("Account_id", accountNum);
            input.put("Type_id", String.valueOf(getRandomElement(typeIdlist)));
            input.put("AccountName", accountNames.get(input.get("Type_id")));
            input.put("NickName", "My " + input.get("AccountName"));
            input.put("Product_id", "8");
            input.put("Bank_id", "1");
            input.put("LastPaymentDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
            String accHolderName = "{\"username\": \"" + dcRequest.getParameter("firstname") + "\", \"fullname\": \""
                    + dcRequest.getParameter("firstname") + "\"}";
            input.put("AccountHolder", accHolderName);
            input.put("DividendLastPaidDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
            input.put("EStatementmentEnable", "false");
            input.put("ownership", "Single");

            HelperMethods.removeNullValues(input);
            HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                    URLConstants.DMS_ACCOUNTS_CREATE);

            sb.append(accountNum);
            if ((i + 1) != 3)
                sb.append(",");

            input.clear();

            input.put("id", UUID.randomUUID().toString());
            input.put("membershipId", membershipId);
            input.put("accountId", accountNum);

            HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                    URLConstants.MEMBERSHIP_ACCOUNTS_CREATE);

            input.clear();

        }
        result.addStringParam("Accounts", sb.toString());
        result.addStringParam("SSN", ssn);
        return result;
    }

    private String getDummyAccountId() {
        SimpleDateFormat idformatter = new SimpleDateFormat("yyMMddHHmmssSSS");
        return idformatter.format(new Date());
    }

    public int getRandomElement(List<Integer> list) {
        Random rand = new SecureRandom();
        return list.get(rand.nextInt(list.size()));
    }

    public int getSSN() {
        int n = 1000000000 + new SecureRandom().nextInt(900000000);
        return n;
    }

    private String processPhone(String phone) {
        if (!StringUtils.contains(phone, "-")) {
            phone = "+91-" + phone;

        }
        return phone;
    }

    public String getRandomSuffix() {
        int n = 1000000000 + new SecureRandom().nextInt(900000000);
        return String.valueOf(n);
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
}
