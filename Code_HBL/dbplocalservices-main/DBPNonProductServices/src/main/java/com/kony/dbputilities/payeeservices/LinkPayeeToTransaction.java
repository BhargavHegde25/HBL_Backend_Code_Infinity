package com.kony.dbputilities.payeeservices;

import java.text.ParseException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class LinkPayeeToTransaction implements JavaService2 {
    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);

        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ACCOUNT_TRANSACTION_UPDATE);
        }

        return postProcess(result);
    }

    private Object postProcess(Result result) {
        // TODO Auto-generated method stub
        if (result == null || HelperMethods.hasError(result)) {
            result = new Result();
            result.addParam("success", "false");
        } else {
            result.addParam("success", "true");
        }

        return result;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result)
            throws ParseException, HttpCallException {

        String customerId = HelperMethods.getCustomerIdFromSession(dcRequest);
        if (StringUtils.isBlank(customerId)) {
            return false;
        }

        boolean status = true;
        String id = (String) inputParams.get("transactionId");
        String personId = (String) inputParams.get("PayPersonId");
        String payeeId = (String) inputParams.get("payeeId");

        inputParams.put(DBPUtilitiesConstants.T_TRANS_ID, id);

        if (!StringUtils.isBlank(payeeId)) {
            inputParams.put("Payee_id", payeeId);
        }
        if (!StringUtils.isBlank(personId)) {
            inputParams.put("Person_Id", personId);
        }
        if (isUpdateAllowed(dcRequest, inputParams)) {
            return true;
        }

        return status;
    }

    private boolean isUpdateAllowed(DataControllerRequest dcRequest, Map<String, String> inputParams)
            throws HttpCallException {
        String id = (String) inputParams.get("transactionId");
        String customerId = HelperMethods.getCustomerIdFromSession(dcRequest);

        String query = DBPUtilitiesConstants.T_TRANS_ID + DBPUtilitiesConstants.EQUAL + id;
        Result result = HelperMethods.callGetApi(dcRequest, query, HelperMethods.getHeaders(dcRequest),
                URLConstants.TRANSACTION_GET);

        String fromAccountNumber = HelperMethods.getFieldValue(result, "fromAccountNumber");

        String filter = "Account_id" + DBPUtilitiesConstants.EQUAL + fromAccountNumber + DBPUtilitiesConstants.AND +
                "User_id" + DBPUtilitiesConstants.EQUAL + customerId;

        Result accounts = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.ACCOUNT_GET);
        if (!HelperMethods.hasRecords(accounts)) {
            return false;
        }

        if (inputParams.containsKey("Payee_id")) {
            filter = "Id" + DBPUtilitiesConstants.EQUAL + inputParams.get("Payee_id") + DBPUtilitiesConstants.AND +
                    "User_Id" + DBPUtilitiesConstants.EQUAL + customerId;
            Result payees = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.PAYEE_GET);
            if (!HelperMethods.hasRecords(payees)) {
                return false;
            }
        }

        if (inputParams.containsKey("Person_Id")) {
            filter = "id" + DBPUtilitiesConstants.EQUAL + inputParams.get("Person_Id") + DBPUtilitiesConstants.AND +
                    "User_id" + DBPUtilitiesConstants.EQUAL + customerId;

            Result paypersons = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.PAYPERSON_GET);
            if (!HelperMethods.hasRecords(paypersons)) {
                return false;
            }
        }

        return true;
    }
}
