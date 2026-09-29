package com.kony.testscripts.createscripts;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.kony.eum.dbputilities.customersecurityservices.CreateCustomerCommunication;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class CreateScriptsForContractCreation implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse)
            throws Exception {
        Result result = new Result();

        String address = dcRequest.getParameter("address");
        JsonArray jsonarray = new JsonParser().parse(address).getAsJsonArray();
        for (JsonElement jsonelement : jsonarray) {
            Map<String, String> inputParams = new HashMap<>();
            inputParams.put("id", JSONUtil.getString(jsonelement.getAsJsonObject(), "id"));
            inputParams.put("Region_id", "US-NY");
            inputParams.put("addressLine1", JSONUtil.getString(jsonelement.getAsJsonObject(), "addressLine1"));
            inputParams.put("addressLine2", JSONUtil.getString(jsonelement.getAsJsonObject(), "addressLine2"));
            inputParams.put("cityName", JSONUtil.getString(jsonelement.getAsJsonObject(), "cityName"));
            inputParams.put("state", JSONUtil.getString(jsonelement.getAsJsonObject(), "state"));
            inputParams.put("country", JSONUtil.getString(jsonelement.getAsJsonObject(), "country"));
            inputParams.put("zipCode", JSONUtil.getString(jsonelement.getAsJsonObject(), "zipCode"));
            inputParams.put("Region_id", "US-NY");
            Result internal = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ADDRESS_CREATE);
            if (HelperMethods.hasError(internal)) {
                result.addStringParam("addressStatus", "address cration failed");
                return result;
            }

        }

        String membership = dcRequest.getParameter("membership");
        jsonarray = new JsonParser().parse(membership).getAsJsonArray();
        for (JsonElement jsonelement : jsonarray) {
            Map<String, String> inputParams = new HashMap<>();
            inputParams.put("id", JSONUtil.getString(jsonelement.getAsJsonObject(), "id"));
            inputParams.put("name", JSONUtil.getString(jsonelement.getAsJsonObject(), "name"));
            inputParams.put("firstName", JSONUtil.getString(jsonelement.getAsJsonObject(), "firstName"));
            inputParams.put("lastName", JSONUtil.getString(jsonelement.getAsJsonObject(), "lastName"));
            inputParams.put("dateOfBirth", JSONUtil.getString(jsonelement.getAsJsonObject(), "dateOfBirth"));
            inputParams.put("ssn", JSONUtil.getString(jsonelement.getAsJsonObject(), "ssn"));
            inputParams.put("taxId", JSONUtil.getString(jsonelement.getAsJsonObject(), "taxId"));
            inputParams.put("phone", "+" + JSONUtil.getString(jsonelement.getAsJsonObject(), "phone").trim());
            inputParams.put("email", JSONUtil.getString(jsonelement.getAsJsonObject(), "email"));
            inputParams.put("addressId", JSONUtil.getString(jsonelement.getAsJsonObject(), "addressId"));
            inputParams.put("status", JSONUtil.getString(jsonelement.getAsJsonObject(), "status"));
            inputParams.put("industry", JSONUtil.getString(jsonelement.getAsJsonObject(), "industry"));
            inputParams.put("isBusinessType", JSONUtil.getString(jsonelement.getAsJsonObject(), "isBusinessType"));
            inputParams.put("faxId", JSONUtil.getString(jsonelement.getAsJsonObject(), "faxId"));
            Result internal = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.MEMBERSHIP_CREATE);

            inputParams.clear();
            inputParams.put("id", JSONUtil.getString(jsonelement.getAsJsonObject(), "id"));
            inputParams.put("UserName", JSONUtil.getString(jsonelement.getAsJsonObject(), "id"));
            inputParams.put("Status_id", "SID_CUS_NEW");
            if ("1".equalsIgnoreCase(JSONUtil.getString(jsonelement.getAsJsonObject(), "isBusinessType")))
                inputParams.put("CustomerType_id", "TYPE_ID_RETAIL");
            else
                inputParams.put("CustomerType_id", "TYPE_ID_BUSINESS");
            inputParams.put("FirstName", JSONUtil.getString(jsonelement.getAsJsonObject(), "firstName"));
            inputParams.put("LastName", JSONUtil.getString(jsonelement.getAsJsonObject(), "lastName"));
            inputParams.put("DateOfBirth", JSONUtil.getString(jsonelement.getAsJsonObject(), "dateOfBirth"));
            inputParams.put("Ssn", JSONUtil.getString(jsonelement.getAsJsonObject(), "ssn"));
            inputParams.put("isEnrolledFromSpotlight", "1");
            HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.CUSTOMER_CREATE);

            inputParams.clear();
            inputParams.put("id", JSONUtil.getString(jsonelement.getAsJsonObject(), "id"));
            inputParams.put("Phone", "+" + JSONUtil.getString(jsonelement.getAsJsonObject(), "phone").trim());
            inputParams.put("Email", JSONUtil.getString(jsonelement.getAsJsonObject(), "email"));
            CreateCustomerCommunication object = new CreateCustomerCommunication();
            object.invoke(inputParams, dcRequest);

            Map<String, String> input = new HashMap<>();
            input.put("Customer_id", JSONUtil.getString(jsonelement.getAsJsonObject(), "id"));
            input.put("Address_id", JSONUtil.getString(jsonelement.getAsJsonObject(), "addressId"));
            input.put("Type_id", "ADR_TYPE_HOME");
            input.put("isPrimary", "1");
            HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                    URLConstants.CUSTOMER_ADDRESS_CREATE);

            inputParams.clear();

            if (HelperMethods.hasError(internal)) {
                result.addStringParam("membershipStatus", "membership cration failed");
                return result;
            }
        }
        String membershipRelation = dcRequest.getParameter("membershipRelation");
        jsonarray = new JsonParser().parse(membershipRelation).getAsJsonArray();
        for (JsonElement jsonelement : jsonarray) {
            Map<String, String> inputParams = new HashMap<>();
            inputParams.put("id", HelperMethods.getNewId());
            inputParams.put("membershipId", JSONUtil.getString(jsonelement.getAsJsonObject(), "membershipId"));
            inputParams.put("relatedMebershipId",
                    JSONUtil.getString(jsonelement.getAsJsonObject(), "relatedMebershipId"));
            inputParams.put("relationshipId", JSONUtil.getString(jsonelement.getAsJsonObject(), "relationshipId"));
            inputParams.put("relationshipName", JSONUtil.getString(jsonelement.getAsJsonObject(), "relationshipName"));
            Result internal = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.MEMBERSHIPRELATION_CREATE);
            if (HelperMethods.hasError(internal)) {
                result.addStringParam("membershipRelationStatus", "membership relation cration failed");
                return result;
            }
        }
        String accounts = dcRequest.getParameter("accounts");
        jsonarray = new JsonParser().parse(accounts).getAsJsonArray();
        for (JsonElement jsonelement : jsonarray) {
            Map<String, String> inputParams = new HashMap<>();
            inputParams = getAccount1DemoData();
            inputParams.put("Account_id", JSONUtil.getString(jsonelement.getAsJsonObject(), "Account_id"));
            inputParams.put("AccountName",
                    JSONUtil.getString(jsonelement.getAsJsonObject(), "AccountName"));
            inputParams.put("AccountHolder",
                    "{\"username\": \"" + JSONUtil.getString(jsonelement.getAsJsonObject(), "AccountHolder")
                            + "\", \"fullname\": \""
                            + JSONUtil.getString(jsonelement.getAsJsonObject(), "AccountHolder") + "\"}");
            inputParams.put("ownership", JSONUtil.getString(jsonelement.getAsJsonObject(), "ownership"));
            inputParams.put("Type_id", JSONUtil.getString(jsonelement.getAsJsonObject(), "Type_id"));
            inputParams.put("StatusDesc", "Active");
            inputParams.put("AvailableBalance", "10000");
            inputParams.put("CurrentBalance", "10000");
            if (StringUtils.isNotBlank(JSONUtil.getString(jsonelement.getAsJsonObject(), "NickName")))
                inputParams.put("NickName", JSONUtil.getString(jsonelement.getAsJsonObject(), "NickName"));
            Result internal = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.DMS_ACCOUNTS_CREATE);

            if (HelperMethods.hasError(internal)) {
                result.addStringParam("accountsCreateStatus", "accounts cration failed");
                return result;
            }
        }
        String membershipAccounts = dcRequest.getParameter("membershipAccounts");
        jsonarray = new JsonParser().parse(membershipAccounts).getAsJsonArray();
        for (JsonElement jsonelement : jsonarray) {
            Map<String, String> inputParams = new HashMap<>();
            inputParams.put("id", HelperMethods.getNewId());
            inputParams.put("membershipId",
                    JSONUtil.getString(jsonelement.getAsJsonObject(), "membershipId"));
            inputParams.put("accountId", JSONUtil.getString(jsonelement.getAsJsonObject(), "accountId"));
            inputParams.put("ownership", JSONUtil.getString(jsonelement.getAsJsonObject(), "ownership"));
            inputParams.put("Type_id", JSONUtil.getString(jsonelement.getAsJsonObject(), "Type_id"));
            Result internal = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.MEMBERSHIP_ACCOUNTS_CREATE);
            if (HelperMethods.hasError(internal)) {
                result.addStringParam("membershipAccounts", "membership account cration failed");
                return result;
            }
        }
        result.addStringParam("status", "success");
        return result;
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