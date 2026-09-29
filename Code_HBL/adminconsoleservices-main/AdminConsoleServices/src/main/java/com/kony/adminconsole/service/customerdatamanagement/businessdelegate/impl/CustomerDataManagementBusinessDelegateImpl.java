package com.kony.adminconsole.service.customerdatamanagement.businessdelegate.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.service.customerdatamanagement.businessdelegate.api.CustomerDataManagementBusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;

public class CustomerDataManagementBusinessDelegateImpl implements CustomerDataManagementBusinessDelegate{
	public JSONObject getSDPReport(Map<String, Object> postParametersMap, DataControllerRequest request) throws DBPApplicationException {
        String operationName = "dbxdb_SDP_Report_get";
		String response = DBPServiceExecutorBuilder.builder().
				withServiceId("CRUDLayer").
				withOperationId(operationName).
				withRequestParameters(postParametersMap).
				build().getResponse();
        JSONObject responseJSON = CommonUtilities.getStringAsJSONObject(response);
        JSONObject inputData = (JSONObject) postParametersMap.get("_input");
        JSONObject params = (JSONObject) postParametersMap.get("params");
        JSONObject finalResponseJSON = new JSONObject();

        if(responseJSON.optInt("httpStatusCode") != -1) {

            Iterator keys = responseJSON.keys();
            JSONArray personalDataArray = new JSONArray();
            while(keys.hasNext()) {
                String responseKey = (String)keys.next();
                if(!responseKey.equals("opstatus") && !responseKey.equals("httpStatusCode")) {
                    JSONArray record = responseJSON.optJSONArray(responseKey);
                    if(record.length() > 0) {
                        Iterator iteratorObj = record.iterator();
                        while (iteratorObj.hasNext()) {
                            JSONObject data = (JSONObject) iteratorObj.next();
                            JSONObject entityObject = new JSONObject();
                            Iterator columnKeys = data.keys();
                            JSONArray dataEntityFields = new JSONArray();
                            while(columnKeys.hasNext()) {
                                JSONObject attributeNameAndValue = new JSONObject();
                                String currKey = (String)columnKeys.next();
                                if(!currKey.equals("entityId") && !currKey.equals("tableName")) {
                                    attributeNameAndValue.put("attributeName",currKey);
                                    attributeNameAndValue.put("attributeValue",data.get(currKey).toString());
                                    dataEntityFields.put(attributeNameAndValue);
                                }
                            }
                            entityObject.put("dataEntityFields",dataEntityFields);
                            entityObject.put("dataEntityName",data.get("tableName").toString());
                            entityObject.put("entityId", data.get("entityId").toString());
                            personalDataArray.put(entityObject);
                        }
                    }
                }
            }
            finalResponseJSON.put("personalData",personalDataArray);
        } else {
            finalResponseJSON = responseJSON;
        }

        finalResponseJSON.put("partyId",inputData.optString("partyId"));
        finalResponseJSON.put("customerId",inputData.optString("customerId"));
        finalResponseJSON.put("serviceId",inputData.optString("serviceId"));
        finalResponseJSON.put("reportType",params.getJSONArray("reportType").getString(0));
        finalResponseJSON.put("requestId",params.getJSONArray("requestId").getString(0));

        return finalResponseJSON;
    }

    public JSONObject triggerErasure(Map<String, Object> postParametersMap, DataControllerRequest request)
            throws DBPApplicationException {
        String authToken = CommonUtilities.getAuthToken(request);
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("X-Kony-Authorization", authToken);

        String response = DBPServiceExecutorBuilder.builder().
                withServiceId("CRUDLayer").
                withOperationId("dbxdb_erasure_proc").
                withRequestParameters(postParametersMap).
                build().getResponse();
        JSONObject responseJSON = CommonUtilities.getStringAsJSONObject(response);
        return responseJSON;
    }

    public JSONObject updateCustomerErasureStatus(Map<String, Object> postParametersMap, DataControllerRequest request)
            throws DBPApplicationException {
        String operationName = "dbxdb_update_customer_erasure_status_proc";
        String response = DBPServiceExecutorBuilder.builder().
                withServiceId("CRUDLayer").
                withOperationId(operationName).
                withRequestParameters(postParametersMap).
                build().getResponse();
        JSONObject responseJSON = CommonUtilities.getStringAsJSONObject(response);
        return responseJSON;
    }

    public JSONObject applicationPurge(Map<String, Object> postParametersMap,DataControllerRequest request)
            throws DBPApplicationException {
        String operationName = "dbxdb_delete_prospect_data_proc";
        String response = DBPServiceExecutorBuilder.builder().
                withServiceId("CRUDLayer").
                withOperationId(operationName).
                withRequestParameters(postParametersMap).
                build().getResponse();
        JSONObject responseJSON = CommonUtilities.getStringAsJSONObject(response);
        return responseJSON;
    };
}
