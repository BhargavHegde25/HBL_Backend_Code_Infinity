package com.kony.eum.dbputilities.customersecurityservices;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.ErrorConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class UpdateCustomerProfile implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        try {
            Result result = null;
            Map<String, String> inputmap = HelperMethods.getInputParamMap(inputArray);

            if (StringUtils.isNotBlank(inputmap.get("CustomerAddress"))) {
                result = processAddress(dcRequest, inputmap);
            }
            if (StringUtils.isNotBlank(inputmap.get("EmployementDetails")) && !HelperMethods.hasError(result)) {
                result = processEmployementDetails(dcRequest, inputmap);
            }
            if (StringUtils.isNotBlank(inputmap.get("Expenses")) && !HelperMethods.hasError(result)) {
                result = processExpenses(dcRequest, inputmap);
            }
            if (StringUtils.isNotBlank(inputmap.get("OthersourceOfIncome")) && !HelperMethods.hasError(result)) {
                result = processOthersourceOfIncome(dcRequest, inputmap);
            }
            if (StringUtils.isNotBlank(inputmap.get("Phone")) && !HelperMethods.hasError(result)) {
                result = processPhone(dcRequest, inputmap);
            }
            if (StringUtils.isNotBlank(inputmap.get("Email")) && !HelperMethods.hasError(result)) {
                result = processEmail(dcRequest, inputmap);
            }
            if (!HelperMethods.hasError(result)) {
                HelperMethods.removeNullValues(inputmap);
                result = HelperMethods.callApi(dcRequest, inputmap, HelperMethods.getHeaders(dcRequest),
                        URLConstants.CUSTOMER_UPDATE);
            }
            if (HelperMethods.hasError(result)) {
                // log.error("Exception in UpdateUserProfile :" +
                // HelperMethods.getError(result));
                return ErrorCodeEnum.ERR_10056.setErrorCode(new Result(), ErrorConstants.ERROR_IN_CREATE);
            }
            return result;
        } catch (Exception e) {
            // log.error("Exception in UpdateUserProfile : ", e);
            Result result = new Result();
            ErrorCodeEnum.ERR_10056.setErrorCode(result, ErrorConstants.ERROR_IN_CREATE);
            return result;
        }
    }

    private Result processOthersourceOfIncome(DataControllerRequest dcRequest, Map<String, String> inputmap)
            throws HttpCallException {
        Result result = new Result();
        String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + inputmap.get("id");
        Result res = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMER_INCOME_DETAILS_GET);
        Map<String, Record> incomeSources = getAllRecordsWith(res, "SourceType");
        JsonArray othersourceOfIncomes = convertToJsonArray(inputmap.get("OthersourceOfIncome"));
        for (int i = 0; i < othersourceOfIncomes.size(); i++) {
            Map<String, String> othersourceOfIncome = jsonToMap(othersourceOfIncomes.get(i).getAsJsonObject());
            othersourceOfIncome.put("Customer_id", inputmap.get("id"));
            if (StringUtils.isNotBlank(othersourceOfIncome.get("SourceType"))
                    && incomeSources.containsKey(othersourceOfIncome.get("SourceType"))) {
                othersourceOfIncome.put("id",
                        HelperMethods.getFieldValue(incomeSources.get(othersourceOfIncome.get("SourceType")), "id"));
                result = HelperMethods.callApi(dcRequest, othersourceOfIncome, HelperMethods.getHeaders(dcRequest),
                        URLConstants.CUSTOMER_INCOME_DETAILS_UPDATE);
                if (HelperMethods.hasError(result)) {
                    return result;
                }
            } else {
                String id = "OSOI" + HelperMethods.getNewId();
                othersourceOfIncome.put("id", id);
                result = HelperMethods.callApi(dcRequest, othersourceOfIncome, HelperMethods.getHeaders(dcRequest),
                        URLConstants.CUSTOMER_INCOME_DETAILS_CREATE);
                if (HelperMethods.hasError(result)) {
                    return result;
                }
            }
        }
        return result;
    }

    private Map<String, Record> getAllRecordsWith(Result result, String keyName) {
        Map<String, Record> map = new HashMap<>();
        if (HelperMethods.hasRecords(result)) {
            List<Record> records = result.getAllDatasets().get(0).getAllRecords();
            for (Record rec : records) {
                if (null != HelperMethods.getFieldValue(rec, keyName)) {
                    map.put(HelperMethods.getFieldValue(rec, keyName), rec);
                }
            }
        }
        return map;
    }

    private Result processEmployementDetails(DataControllerRequest dcRequest, Map<String, String> inputmap)
            throws HttpCallException {
        Result result = null;
        String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + inputmap.get("id");
        Result res = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMER_EMP_DETAILS_GET);
        Map<String, String> employementDetails = getMap(inputmap.get("EmployementDetails"));
        employementDetails.put("Customer_id", inputmap.get("id"));
        if (HelperMethods.hasRecords(res)) {
            employementDetails.put("id", HelperMethods.getFieldValue(res, "id"));
            result = HelperMethods.callApi(dcRequest, employementDetails, HelperMethods.getHeaders(dcRequest),
                    URLConstants.CUSTOMER_EMP_DETAILS_UPDATE);
        } else {
            String id = "ED" + HelperMethods.getNewId();
            employementDetails.put("id", id);
            result = HelperMethods.callApi(dcRequest, employementDetails, HelperMethods.getHeaders(dcRequest),
                    URLConstants.CUSTOMER_EMP_DETAILS_CREATE);
        }
        return result;
    }

    private Result processExpenses(DataControllerRequest dcRequest, Map<String, String> inputmap)
            throws HttpCallException {
        Result result = new Result();
        JsonArray expenses = convertToJsonArray(inputmap.get("Expenses"));
        for (int i = 0; i < expenses.size(); i++) {
            Map<String, String> expense = jsonToMap(expenses.get(i).getAsJsonObject());
            expense.put("Customer_id", inputmap.get("id"));
            if (StringUtils.isNotBlank(expense.get("id"))) {
                result = HelperMethods.callApi(dcRequest, expense, HelperMethods.getHeaders(dcRequest),
                        URLConstants.CUSTOMER_EXPENSES_UPDATE);
                if (HelperMethods.hasError(result)) {
                    break;
                }
            } else {
                String id = "EX" + HelperMethods.getNewId();
                expense.put("id", id);
                result = HelperMethods.callApi(dcRequest, expense, HelperMethods.getHeaders(dcRequest),
                        URLConstants.CUSTOMER_EXPENSES_CREATE);
                if (HelperMethods.hasError(result)) {
                    break;
                }
            }
        }
        return result;
    }

    private Result processEmail(DataControllerRequest dcRequest, Map<String, String> inputmap)
            throws HttpCallException {
        String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + inputmap.get("id") + DBPUtilitiesConstants.AND
                + "softdeleteflag eq 0";
        Result res = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERCOMMUNICATION_GET);
        Map<String, String> emailInput = createInputForCustComm(inputmap.get("Email"), "COMM_TYPE_EMAIL",
                inputmap.get("id"));
        if (HelperMethods.hasRecords(res)) {
            List<Record> arr = res.getAllDatasets().get(0).getAllRecords();
            for (int i = 0; i < arr.size(); i++) {
                Record rec = arr.get(i);
                if ("COMM_TYPE_EMAIL".equalsIgnoreCase(HelperMethods.getFieldValue(rec, "Type_id"))) {
                    emailInput.put("isPrimary", "0");
                }
            }
        }
        res = HelperMethods.callApi(dcRequest, emailInput, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERCOMMUNICATION_CREATE);
        inputmap.remove("Email");
        return res;
    }

    private Result processPhone(DataControllerRequest dcRequest, Map<String, String> inputmap)
            throws HttpCallException {
        String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + inputmap.get("id") + DBPUtilitiesConstants.AND
                + "softdeleteflag eq 0";
        Result res = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERCOMMUNICATION_GET);
        Map<String, String> phoneInput = createInputForCustComm(inputmap.get("Phone"),
                DBPUtilitiesConstants.COMM_TYPE_PHONE, inputmap.get("id"));
        if (HelperMethods.hasRecords(res)) {
            List<Record> arr = res.getAllDatasets().get(0).getAllRecords();
            for (int i = 0; i < arr.size(); i++) {
                Record rec = arr.get(i);
                if (DBPUtilitiesConstants.COMM_TYPE_PHONE
                        .equalsIgnoreCase(HelperMethods.getFieldValue(rec, "Type_id"))) {
                    phoneInput.put("isPrimary", "0");
                }
            }
        }
        res = HelperMethods.callApi(dcRequest, phoneInput, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERCOMMUNICATION_CREATE);
        inputmap.remove("Phone");
        return res;
    }

    private Result processAddress(DataControllerRequest dcRequest, Map<String, String> inputmap)
            throws HttpCallException {
        Result result = new Result();
        String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + inputmap.get("id");
        Result res = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMER_ADDRESS_GET);
        Map<String, Record> customerAddresses = getAllRecordsWith(res, "Address_id");
        if (StringUtils.isNotBlank(inputmap.get("Address"))) {
            JsonArray addressArr = convertToJsonArray(inputmap.get("Address"));
            JsonArray customerAddressArr = convertToJsonArray(inputmap.get("CustomerAddress"));
            for (int i = 0; i < addressArr.size() && i < customerAddressArr.size(); i++) {
                String addressId = null;
                Map<String, String> address = jsonToMap(addressArr.get(i).getAsJsonObject());
                Map<String, String> customerAddress = jsonToMap(customerAddressArr.get(i).getAsJsonObject());
                customerAddress.put("Customer_id", inputmap.get("id"));
                if (!customerAddresses.isEmpty() && customerAddresses.containsKey(address.get("Address_id"))) {
                    addressId = address.get("Address_id");
                    result = updateCreateAddress(dcRequest, addressId, address, customerAddress);
                } else {
                    result = updateCreateAddress(dcRequest, addressId, address, customerAddress);
                }
                if (HelperMethods.hasError(result)) {
                    break;
                }
            }
            inputmap.remove("Address");
            inputmap.remove("CustomerAddress");
        }
        return result;
    }

    private Result updateCreateAddress(DataControllerRequest dcRequest, String addressId, Map<String, String> address,
            Map<String, String> customerAddress) throws HttpCallException {
        Result res = null;
        String isPrimary = "0";
        if (StringUtils.isNotBlank(customerAddress.get("isPrimary"))) {
            isPrimary = customerAddress.get("isPrimary");
        }
        if (StringUtils.isNotBlank(addressId)) {
            address.put("id", addressId);
            customerAddress.put("Address_id", addressId);
            res = HelperMethods.callApi(dcRequest, address, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ADDRESS_UPDATE);
            if (!HelperMethods.hasError(res)) {
                res = HelperMethods.callApi(dcRequest, customerAddress, HelperMethods.getHeaders(dcRequest),
                        URLConstants.CUSTOMER_ADDRESS_UPDATE);
            }
        } else {
            String genAddressId = "ADD" + HelperMethods.getNewId();
            address.put("id", genAddressId);
            customerAddress.put("isPrimary", isPrimary);
            customerAddress.put("Address_id", genAddressId);
            customerAddress.put("Type_id", address.get("Type_id"));
            res = HelperMethods.callApi(dcRequest, address, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ADDRESS_CREATE);
            if (!HelperMethods.hasError(res)) {
                res = HelperMethods.callApi(dcRequest, customerAddress, HelperMethods.getHeaders(dcRequest),
                        URLConstants.CUSTOMER_ADDRESS_CREATE);
            }
        }
        return res;
    }

    private Map<String, String> createInputForCustComm(String value, String type, String customerID) {
        Map<String, String> input = new HashMap<>();
        input.put("id", "CID" + HelperMethods.getNewId());
        input.put("isPrimary", "1");
        input.put("Value", value);
        input.put("Customer_id", customerID);
        input.put("Extension", DBPUtilitiesConstants.CUSTOMER_COMMUNICATION_PHONE_DEFAULT_EXTENSION);
        input.put("Type_id", type);
        return input;
    }

    private JsonArray convertToJsonArray(String jArray) {
        try {
            return (JsonArray) (new JsonParser().parse(jArray));
        } catch (Exception e) {
            return new JsonArray();
        }
    }

    private Map<String, String> getMap(String jArray) {
        JsonArray array = convertToJsonArray(jArray);
        JsonObject jobject = array.get(0).getAsJsonObject();
        return jsonToMap(jobject);
    }

    private Map<String, String> jsonToMap(JsonObject jobject) {
        Map<String, String> map = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : jobject.entrySet()) {
            if (HelperMethods.isJsonNotNull(entry.getValue())) {
                map.put(entry.getKey(), entry.getValue().getAsString());
            }
        }
        return map;
    }
}
