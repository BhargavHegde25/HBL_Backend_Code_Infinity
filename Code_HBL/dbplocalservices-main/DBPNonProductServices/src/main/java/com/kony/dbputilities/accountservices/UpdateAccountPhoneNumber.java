package com.kony.dbputilities.accountservices;

import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class UpdateAccountPhoneNumber implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Map inputParams = HelperMethods.getInputParamMap(inputArray);
        if (inputParams.isEmpty()) {
            return new Result();
        }
        process(inputParams, dcRequest);
        Result result = postProcess();
        return result;
    }

    private Result postProcess() {
        Result result = new Result();
        result.addParam(new Param("result", "Success", DBPUtilitiesConstants.STRING_TYPE));
        return result;
    }

    @SuppressWarnings("rawtypes")
    private void process(Map inputParams, DataControllerRequest dcRequest) throws HttpCallException {
        String accountLi = (String) inputParams.get("accountli");
        Map<String, String> map = getAccountPhoneMap(accountLi);
        for (String account : map.keySet()) {
            updateAccount(dcRequest, account, map.get(account));
        }
    }

    private Map<String, String> getAccountPhoneMap(String accountLi) {
        Map<String, String> map = new HashMap<>();
        JsonArray jArray = getJsonArray(accountLi);
        if (jArray.isJsonArray()) {
            for (int i = 0; i < jArray.size(); i++) {
                JsonObject account = (JsonObject) jArray.get(i);
                String accountNumber = getAccountNumber(account);
                String selected = getIsSelected(account);
                String phone = account.get("phone").getAsString();
                if ("false".equalsIgnoreCase(selected)) {
                    phone = null;
                }
                map.put(accountNumber, phone);
            }
        }
        return map;
    }

    private String getAccountNumber(JsonObject account) {
        JsonElement accountNumber = account.get("accountNumber");
        if (accountNumber.isJsonObject()) {
            return accountNumber.getAsJsonObject().get("id").getAsString();
        }
        return accountNumber.getAsString();
    }

    private String getIsSelected(JsonObject account) {
        JsonElement accountNumber = account.get("accountNumber");
        if (accountNumber.isJsonObject()) {
            return accountNumber.getAsJsonObject().get("selected").getAsString();
        }
        return "true";
    }

    private void updateAccount(DataControllerRequest dcRequest, String accountId, String phone) {
        Map<String, String> input = new HashMap<>();
        input.put("Account_id", accountId);
        input.put("phone", phone);
        try {
            HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.ACCOUNTS_UPDATE);
        } catch (HttpCallException e) {

            alert.prepareError(e.getMessage()).log();
        }
    }

    private JsonArray getJsonArray(String jsonString) {
        JsonParser jsonParser = new JsonParser();
        return (JsonArray) jsonParser.parse(jsonString);
    }
}