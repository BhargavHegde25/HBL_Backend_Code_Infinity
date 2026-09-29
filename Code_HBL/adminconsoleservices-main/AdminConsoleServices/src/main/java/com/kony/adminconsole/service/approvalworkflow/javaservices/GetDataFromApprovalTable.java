package com.kony.adminconsole.service.approvalworkflow.javaservices;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

public class GetDataFromApprovalTable implements JavaService2 {
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
                         DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();

    	Result result = new Result();
        JSONObject responseObj = new JSONObject();

        Map<String, Object> requestInputMap = new HashMap<String, Object>();
        requestInputMap = (Map<String, Object>) inputArray[1];
        String operationName = requestInputMap.get("operationName").toString();
        String authToken = CommonUtilities.getAuthToken(request);

        String filterQuery = "";
        
        if(operationName.equalsIgnoreCase("dbxdb_permissionapprovals_get") || operationName.equalsIgnoreCase("dbxdb_permissionapprovalconfig_get")) {
        	String expAPIOperationName = requestInputMap.get("expAPIOperationName").toString();
        	filterQuery = "expAPIOperationName eq '" + expAPIOperationName+"'";
        }
        else if(operationName.equalsIgnoreCase("dbxdb_approvalrequests_get")) {
        	String requestId = requestInputMap.get("requestId").toString();
        	filterQuery = "requestId eq '" + requestId+"'";
        }
        Map<String, Object> requestParameters = new HashMap<String, Object>();
        requestParameters.put("$filter", filterQuery);

        String approvalsResponse = "";

        try {
            approvalsResponse = DBPServiceExecutorBuilder.builder().withServiceId("ApprovalCRUDLayer")
                    .withOperationId(operationName)
                    .withRequestParameters(requestParameters).withFabricAuthToken(authToken).build().getResponse();
            responseObj = CommonUtilities.getStringAsJSONObject(approvalsResponse);
            result = CommonUtilities.getResultObjectFromJSONObject(responseObj);
            String[] lengthparam = operationName.split("_");
            result.addParam(lengthparam[1]+"Length",Integer.toString(responseObj.getJSONArray(lengthparam[1]).length()));
        } catch (Exception e) {
            result.addParam("Exception Message", e.getMessage());
        }
        return result;
    }
}