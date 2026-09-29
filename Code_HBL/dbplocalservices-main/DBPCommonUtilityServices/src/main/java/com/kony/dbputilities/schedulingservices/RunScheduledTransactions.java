package com.kony.dbputilities.schedulingservices;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;

import com.dbp.core.constants.DBPConstants;
import com.google.gson.JsonObject;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class RunScheduledTransactions implements JavaService2 {

    @Override
    public Object invoke(String paramString, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {

        ScheduledTransactionRead transactions = new ScheduledTransactionRead();
        Result result = (Result) transactions.invoke(paramString, inputArray, dcRequest, dcResponse);
        Dataset scheduledTransactions = new Dataset();
        if (result.getParamByName(DBPConstants.FABRIC_OPSTATUS_KEY).getValue().equals(DBPUtilitiesConstants.ZERO)) {
            scheduledTransactions = result.getDatasetById(DBPUtilitiesConstants.SCHEDULED_TRANS);
        }

        for (int reIndex = 0; reIndex < scheduledTransactions.getAllRecords().size(); reIndex++) {
            Record rec = scheduledTransactions.getRecord(reIndex);
            String id = rec.getParam(DBPUtilitiesConstants.ID).getValue();
            List<Param> paramsList = rec.getAllParams();
            Iterator<Param> params = paramsList.iterator();
            HashMap<String, Object> paramsMap = new HashMap<>();
            HashMap<String, String> headerParams = new HashMap<>();
            while (params.hasNext()) {
                Param p1 = params.next();
                paramsMap.put(p1.getName(), p1.getValue());
            }

            String authKey = dcRequest.getHeader(DBPUtilitiesConstants.X_KONY_AUTHORIZATION_VALUE);
            headerParams.put(DBPUtilitiesConstants.X_KONY_AUTHORIZATION, authKey);

            String userid = rec.getParam(DBPUtilitiesConstants.UN_USR_ID).getValue();
            paramsMap.put(DBPUtilitiesConstants.UN_ID, userid);

            rec = getUserDetails(dcRequest, paramsMap);

            if (null != rec.getParam(DBPUtilitiesConstants.TOKEN)
                    && !"".equals(rec.getParam(DBPUtilitiesConstants.TOKEN).getValue())) {
                String token = rec.getParam(DBPUtilitiesConstants.TOKEN).getValue();
                headerParams.put(DBPUtilitiesConstants.SESSION_TOKEN, token);
                result = makeTransfer(dcRequest, paramsMap, headerParams);
            } else {
                String username = rec.getParam(DBPUtilitiesConstants.USER_NAME).getValue();
                String password = rec.getParam("passWord").getValue();
                String token = login(username, password, dcRequest);
                headerParams.put(DBPUtilitiesConstants.SESSION_TOKEN, token);
                result = makeTransfer(dcRequest, paramsMap, headerParams);
            }

            if (null != result.getParamByName(DBPUtilitiesConstants.TRANSACTION_ID)) {
                updateScheduledTransactionTable(id, authKey, dcRequest);
            }
        }

        return result;
    }

    private Record getUserDetails(DataControllerRequest dcRequest, HashMap<String, Object> paramsMap)
            throws HttpCallException {
        Record rec;
        Result userResult = HelperMethods.callApi(dcRequest, paramsMap, HelperMethods.getHeaders(dcRequest),
                URLConstants.USER_GET);
        rec = userResult.getDatasetById("customer").getRecord(0);
        return rec;
    }

    private void updateScheduledTransactionTable(String id, String authKey, DataControllerRequest dcRequest)
            throws HttpCallException {
        HashMap<String, Object> inputParams = new HashMap<>();
        HashMap<String, String> headerParams = new HashMap<>();
        inputParams.put(DBPUtilitiesConstants.ID, id);
        inputParams.put(DBPUtilitiesConstants.STATUS_DESC, DBPUtilitiesConstants.TRANS_STATUS_DESC_COMPLETED);
        headerParams.put(DBPUtilitiesConstants.X_KONY_AUTHORIZATION, authKey);
        HelperMethods.callApi(dcRequest, inputParams, headerParams, URLConstants.SCHEDULEDTRANS_UPDATE);
    }

    private String login(String username, String password, DataControllerRequest dcRequest) throws HttpCallException {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("username", username);
        paramsMap.put(DBPUtilitiesConstants.PWD_FIELD, password);
        JsonObject response = HelperMethods.callApiJson(dcRequest, paramsMap, null,
                URLFinder.getPathUrl(URLConstants.CUSTOM_LOGIN, dcRequest));
        return response.getAsJsonObject(DBPUtilitiesConstants.PROVIDER_TOKEN).getAsJsonObject("params")
                .getAsJsonObject(DBPUtilitiesConstants.SECURITY_ATTRIBUTES).get(DBPUtilitiesConstants.SESSION_TOKEN)
                .toString();

    }

    private Result makeTransfer(DataControllerRequest dcRequest, HashMap<String, Object> paramsMap,
            HashMap<String, String> headerParams) throws HttpCallException {
        Result result = null;
        String typeId = (String) paramsMap.get(DBPUtilitiesConstants.T_TYPE_ID);
        String filter = DBPUtilitiesConstants.ID + DBPUtilitiesConstants.EQUAL + typeId;
        result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.TRANSACTION_TYPE_GET);

        String typeDesc = result.getDatasetById(DBPUtilitiesConstants.TRANS_TYPE_TABLE_NAME).getRecord(0)
                .getParam(DBPUtilitiesConstants.TRANS_TYPE_DESC).getValue();
        String scheduledDate = (String) paramsMap.get(DBPUtilitiesConstants.SCHEDULED_DATE);
        scheduledDate = scheduledDate.concat("T00:00:00.000Z");

        paramsMap.put(DBPUtilitiesConstants.TRANSACTION_TYPE, typeDesc);
        paramsMap.put(DBPUtilitiesConstants.SCHEDULED_DATE, scheduledDate);
        headerParams.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        Result transactionResult = HelperMethods.callApi(dcRequest, paramsMap, headerParams,
                URLConstants.TRANSACTIONS_POST);
        return transactionResult;
    }
}
