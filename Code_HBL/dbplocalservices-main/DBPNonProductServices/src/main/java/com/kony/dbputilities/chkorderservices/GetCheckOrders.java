package com.kony.dbputilities.chkorderservices;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetCheckOrders implements JavaService2 {
    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);
        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.CHECK_ORDER_GET);
        }
        if (HelperMethods.hasRecords(result)) {
            postProcess(dcRequest, inputParams, result);
        }
        return result;
    }

    @SuppressWarnings("rawtypes")
    private void postProcess(DataControllerRequest dcRequest, Map inputParams, Result result) throws HttpCallException {
        List<Record> transactions = result.getAllDatasets().get(0).getAllRecords();
        for (Record transaction : transactions) {
            updateAccountNames(dcRequest, transaction);
        }
    }

    private void updateAccountNames(DataControllerRequest dcRequest, Record transaction) throws HttpCallException {
        String accountNum = HelperMethods.getFieldValue(transaction, "account_id");
        if (StringUtils.isNotBlank(accountNum)) {
            String filter = "Account_id" + DBPUtilitiesConstants.EQUAL + accountNum;
            Result account = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ACCOUNTS_GET);
            String accountName = HelperMethods.getFieldValue(account, "accountName");
            transaction.addParam(new Param("accountName", accountName, DBPUtilitiesConstants.STRING_TYPE));
            String nickName = HelperMethods.getFieldValue(account, "nickName");
            transaction.addParam(new Param("accountNickName", nickName, DBPUtilitiesConstants.STRING_TYPE));
        }
    }

    /*
     * view needs to be created.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
        String checkId = (String) inputParams.get("checkID");
        String filter = null;
        if (StringUtils.isNotBlank(checkId) && !"$".equals(checkId)) {
            filter = "id" + DBPUtilitiesConstants.EQUAL + checkId;
        } else {
            String userId = HelperMethods.getUserIdFromSession(dcRequest);
            filter = "User_id" + DBPUtilitiesConstants.EQUAL + userId;
        }
        inputParams.put(DBPUtilitiesConstants.FILTER, filter);
        return true;
    }
}
