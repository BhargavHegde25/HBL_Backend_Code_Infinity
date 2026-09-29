package com.kony.dbputilities.accountservices;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.usermanagement.resource.api.InfinityUserManagementResource;

public class NewAccountOpening implements JavaService2 {

    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Map inputParams = HelperMethods.getInputParamMap(inputArray);
        Result result = process(methodId, inputArray, inputParams, dcRequest, dcResponse);
        postProcess(result);
        return result;
    }

    private Result postProcess(Result result) {
        result.addParam(
                new Param("success", "Account Application Successfully Registered", DBPUtilitiesConstants.STRING_TYPE));
        return result;
    }

    @SuppressWarnings("rawtypes")
    private Result process(String methodId, Object[] inputArray, Map inputParams, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse)
            throws HttpCallException {
        Result applicationResult = HelperMethods.callGetApi(dcRequest, null, HelperMethods.getHeaders(dcRequest),
                URLConstants.APPLICATION_GET);
        String currencyCode = HelperMethods.getFieldValue(applicationResult, "currencyCode");
        Map<String, String> user = HelperMethods.getUserFromIdentityService(dcRequest);
        String productLi = (String) inputParams.get("productLi");
        JsonArray jArray = getJsonArray(productLi);
        String product = null;
        // Map<String, String> accountTypes = getAccountTypes(dcRequest);
        int accountPreference = getCurrAccountPreference(dcRequest, user.get("user_id"));
        JsonArray customerAccountJsonArray = new JsonArray();
        if (jArray.isJsonArray()) {
            for (int i = 0; i < jArray.size(); i++) {
                product = jArray.get(i).getAsJsonObject().get("product").getAsString();
                Map<String, String> input = getAccountDetails(convertToJson(product));
                String accountId =
                        createAccount(dcRequest, input, null, null, ++accountPreference, currencyCode);
                JsonObject json = new JsonParser().parse(jArray.get(i).getAsJsonObject().get("product").getAsString())
                        .getAsJsonObject();
                json.addProperty("accountId", accountId);
                customerAccountJsonArray.add(json);
            }
        }
        Map<String, Object> request = new HashMap<>();
        request.put("productLi", customerAccountJsonArray.toString());
        dcRequest.addRequestParam_("productLi", customerAccountJsonArray.toString());
        dcRequest.addRequestParam_("customerAccounts", customerAccountJsonArray.toString());
        dcRequest.updateOriginalRequest(request);
        Result result = new Result();
        result.addStringParam("customerAccounts", customerAccountJsonArray.toString());
        return result;
    }

    private void createCustomerAccounts(String methodId, Object[] inputArray, Map inputParams,
            DataControllerRequest dcRequest, DataControllerResponse dcResponse, JsonArray productsJsonArray) {
        JsonObject requestPayload = new JsonObject();
        Map<String, String> retailContractMap = new HashMap<>();
        retailContractMap.put("customerId", HelperMethods.getCustomerIdFromSession(dcRequest));
        JsonArray accounts = new JsonArray();
        for (int i = 0; i < productsJsonArray.size(); i++) {
            JsonObject temp = new JsonObject();
            temp = productsJsonArray.get(i).getAsJsonObject();
            JsonObject account = new JsonObject();
            account.addProperty("accountName",
                    JSONUtil.getString(temp, "productName"));
            account.addProperty("accountId",
                    JSONUtil.getString(temp, "accountId"));
            account.addProperty("isEnabled", "true");
            account.addProperty("typeId", JSONUtil.getString(temp, "productTypeId"));
            account.addProperty("ownerType",
                    JSONUtil.getString(temp, "Owner"));
            account.addProperty("accountStatus",
                    JSONUtil.getString(temp, "Active"));
            accounts.add(account);
        }
        retailContractMap.put("accounts", accounts.toString());
        InfinityUserManagementResource resource =
                DBPAPIAbstractFactoryImpl.getResource(InfinityUserManagementResource.class);
        inputArray[1] = retailContractMap;
        try {
            resource.createRetailContract(methodId, inputArray, dcRequest, dcResponse);
        } catch (ApplicationException e) {
            e.getMessage();
        }
    }

    private int getCurrAccountPreference(DataControllerRequest dcRequest, String userId) throws HttpCallException {
        int accntPref = -1;
        Map<String, String> input = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        sb.append("User_id").append(DBPUtilitiesConstants.EQUAL).append(userId);
        input.put(DBPUtilitiesConstants.FILTER, sb.toString());
        input.put(DBPUtilitiesConstants.SELECT, "AccountPreference");
        input.put(DBPUtilitiesConstants.ORDERBY, "AccountPreference desc");
        input.put(DBPUtilitiesConstants.TOP, "1");

        Result result = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.ACCOUNT_GET);
        if (HelperMethods.hasRecords(result)
                && StringUtils.isNotBlank((HelperMethods.getFieldValue(result, "AccountPreference")))) {
            accntPref = Integer.parseInt(HelperMethods.getFieldValue(result, "AccountPreference"));
        }
        return accntPref;
    }

    private Map<String, String> getAccountDetails(JsonObject product) {
        Map<String, String> input = null;
        String accountType = getAccoutType(product);
        if (null != accountType) {
            input = new HashMap<>();
            input.put("Type_id", accountType);
            input.put("adminProductId", product.get("productId").getAsString());
            input.put("AccountName", product.get("productName").getAsString());
            input.put("NickName", product.get("productName").getAsString());
            input.put("Name", product.get("productName").getAsString());
            if ("3".equals(product.get("productTypeId").getAsString())) {
                input.put("CreditCardNumber", "978913571");
                input.put("AvailablePoints", "0");
                input.put("LastStatementBalance", "0");
            }
        }
        return input;
    }

    private String getAccoutType(JsonObject product) {
        String accountType = product.get("productType").getAsString().toLowerCase();
        if (StringUtils.isNotBlank(accountType)) {
            if (accountType.contains("checking") || accountType.contains("current account")) {
                return "1";
            } else if (accountType.contains("saving")) {
                return "2";
            } else if (accountType.contains("creditcard") || accountType.contains("credit card")) {
                return "3";
            } else if (accountType.contains("deposit")) {
                return "4";
            } else if (accountType.contains("mortgage")) {
                return "5";
            } else if (accountType.contains("loan")) {
                return "6";
            } else if (accountType.contains("current")) {
                return "7";
            }
        }

        return "2";
    }

    /*
     * private Map<String, String> getAccountTypes(DataControllerRequest dcRequest) throws HttpCallException { Result
     * accountTypes = HelperMethods.callApi(null,HelperMethods.getHeaders(dcRequest), URLConstants.ACCOUNTTYPE_GET);
     * Map<String, String> accountTypeMap = new HashMap<>(); List<Record> types =
     * accountTypes.getAllDatasets().get(0).getAllRecords(); for(Record type: types){
     * accountTypeMap.put(HelperMethods.getFieldValue(type, "TypeID"), HelperMethods.getFieldValue(type,
     * "TypeDescription")); } return accountTypeMap; }
     */

    private String createAccount(DataControllerRequest dcRequest, Map<String, String> input, String userId,
            String userName, int count, String currencyCode) throws HttpCallException {
        SimpleDateFormat idformatter = new SimpleDateFormat("yyMMddHHmmssSSS");
        String accountId = idformatter.format(new Date());
        if (null != input) {
            input.put("Account_id", accountId);
            input.put("User_id", userId);
            input.put("Bank_id", "1");
            input.put("StatusDesc", "active");
            input.put("SwiftCode", "123456789");
            input.put("AccountPreference", String.valueOf(count));
            input.put("IsPFM", "1");
            input.put("CurrencyCode", currencyCode);
            input.put("PrincipalValue", "0");
            input.put("AvailableBalance", "0");
            input.put("AvailableCredit", "0");
            input.put("CurrentBalance", "0");
            input.put("MinimumDue", "0");
            input.put("SupportCardlessCash", "1");
            input.put("SupportDeposit", "1");
            input.put("SupportBillPay", "1");
            input.put("SupportTransferFrom", "1");
            input.put("SupportTransferTo", "1");
            input.put("OutstandingBalance", "0");
            input.put("ClosingDate", HelperMethods.getCurrentDate());
            input.put("DueDate", HelperMethods.getCurrentDate());
            input.put("MaturityDate", HelperMethods.getCurrentDate());
            input.put("PaymentTerm", "");
            input.put("AccountHolder",
                    "{\"fullname\":\"" + input.get("Name") + "\",\"username\":\"" + userName + "\"}");
            input.put("InterestRate", "0");
            input.put("OpeningDate", HelperMethods.getCurrentDate());
            input.put("ShowTransactions", "1");
            input.put("TransactionLimit", "0");
            input.put("TransferLimit", "0");
            input.put("FavouriteStatus", "0");
            input.put("MaturityOption", "0");
            input.put("RoutingNumber", "0");
            input.put("JointHolders", "Jane Bailey");
            input.put("DividendRate", "0");
            input.put("DividendYTD", "0");
            input.put("LastDividendPaidAmount", "0");
            input.put("LastDividendPaidDate", HelperMethods.getCurrentDate());
            input.put("PreviousYearDividend", "0");
            input.put("BondInterest", "0");
            input.put("BondInterestLastYear", "0");
            input.put("TotalCreditMonths", "0");
            input.put("TotalDebitsMonth", "0");
            input.put("CurrentAmountDue", "0");
            input.put("PaymentDue", "0");
            input.put("LastPaymentAmount", "0");
            input.put("LateFeesDue", "0");
            input.put("CreditLimit", "0");
            input.put("InterestPaidYTD", "0");
            input.put("InterestPaidPreviousYTD", "0");
            input.put("UnpaidInterest", "0");
            input.put("RegularPaymentAmount", "0");
            input.put("DividendPaidYTD", "0");
            input.put("DividendLastPaidAmount", "0");
            input.put("DividendLastPaidDate", HelperMethods.getCurrentDate());
            input.put("PreviousYearsDividends", "0");
            input.put("PendingDeposit", "0");
            input.put("PendingWithdrawal", "0");
            input.put("InterestEarned", "0");
            input.put("maturityAmount", "0");
            input.put("principalBalance", "0");
            input.put("OriginalAmount", "0");
            input.put("payoffAmount", "0");
            input.put("BsbNum", "000000000");
            input.put("PayOffCharge", "0");
            input.put("InterestPaidLastYear", "0");
            input.put("EStatementmentEnable", "0");

            HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ACCOUNTS_CREATE);
            // Flow For Retail Customer Ends here.
            // Map<String, String> user = HelperMethods.getUserFromIdentityService(dcRequest);
            // if (HelperMethods.getBusinessUserTypes().contains(user.get("customerType"))
            // || Boolean.parseBoolean(user.get("isCombinedUser"))) {
            // // Map Org Account to Customer
            // Map inputParams = new HashMap<>();
            // inputParams.put("id", UUID.randomUUID().toString());
            // inputParams.put("Account_id", HelperMethods.getFieldValue(result, "Account_id"));
            // inputParams.put("Customer_id", userId);
            // inputParams.put("AccountName", HelperMethods.getFieldValue(result, "AccountName"));
            // inputParams.put("IsViewAllowed", "1");
            // inputParams.put("IsDepositAllowed", "1");
            // inputParams.put("IsWithdrawAllowed", "1");
            //
            // if (Boolean.parseBoolean(user.get("isCombinedUser"))) {
            // inputParams.put("IsOrganizationAccount", "0");
            // } else {
            // inputParams.put("IsOrganizationAccount", "1");
            // }
            //
            // HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
            // URLConstants.CUSTOMERACCOUNTS_CREATE);
            // }
            return accountId;
        }
        return null;
    }

    private JsonArray getJsonArray(String jsonString) {
        JsonParser jsonParser = new JsonParser();
        return (JsonArray) jsonParser.parse(jsonString);
    }

    private JsonObject convertToJson(String jsonString) {
        JsonParser jsonParser = new JsonParser();
        return (JsonObject) jsonParser.parse(jsonString);
    }
}
