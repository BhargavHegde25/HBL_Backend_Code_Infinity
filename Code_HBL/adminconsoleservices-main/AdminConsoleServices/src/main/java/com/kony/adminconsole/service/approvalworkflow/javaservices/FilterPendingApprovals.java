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

public class FilterPendingApprovals implements JavaService2 {
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
                         DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();

        Result result = new Result();

        Map<String, Object> requestInputMap = new HashMap<String, Object>();
        requestInputMap = (Map<String, Object>) inputArray[1];

        String expAPIOperationName = requestInputMap.get("expAPIOperationName").toString();
        String status = requestInputMap.get("status").toString();
        String inputRecordId = requestInputMap.get("recordId").toString();

        String authToken = CommonUtilities.getAuthToken(request);

        String filterQuery = "expAPIOperationName eq " + expAPIOperationName + " and status eq '" + status + "'";

        Map<String, Object> requestParameters = new HashMap<String, Object>();
        requestParameters.put("$filter", filterQuery);

        String approvalRequestsResponse = "";

        try {
            approvalRequestsResponse = DBPServiceExecutorBuilder.builder().withServiceId("ApprovalCRUDLayer")
                    .withOperationId("dbxdb_approvalrequests_get")
                    .withRequestParameters(requestParameters).withFabricAuthToken(authToken).build().getResponse();
            if (approvalRequestsResponse == null) {
                result.addParam("pendingApprovalsExist", "false");
                result.addParam("requestId", "");
            } else {
                String pendingApprovalsExist = "false";
                String requestId = "";
                JSONObject inputRecord = new JSONObject(inputRecordId);
                JSONObject idRecord = new JSONObject();
                int idsCount = 0;

                for (int i = 0; i < inputRecord.length(); i++) {
                    String idIndex = "id" + (idsCount + 1);
                    if (inputRecord.has(idIndex)) {
                        String idValue = inputRecord.getString(idIndex);
                        idRecord.put(idIndex, idValue);
                        idsCount++;
                    } else {
                        break;
                    }
                }

                JSONObject responseObj = new JSONObject(approvalRequestsResponse);
                JSONArray approvalRequests = responseObj.optJSONArray("approvalrequests");

                for (int index = 0; index < approvalRequests.length(); index++) {
                    JSONObject recordObj = approvalRequests.getJSONObject(index);
                    String recordId = recordObj.opt("recordId").toString();
                    if (recordId != null && !("").equals(recordId)) {
                        JSONObject record = new JSONObject(recordId);
                        int matchedIdsCount = 0;
                        for (int idIndex = 1; idIndex <= idRecord.length(); idIndex++) {
                            String id = "id" + idIndex;
                            if (idRecord.getString(id).equals(record.optString(id))) {
                                matchedIdsCount++;
                            } else {
                                break;
                            }
                        }
                        if (matchedIdsCount == idsCount) {
                            pendingApprovalsExist = "true";
                            requestId = recordObj.opt("requestId").toString();
                            break;
                        }
                    }
                }
                result.addParam("pendingApprovalsExist", pendingApprovalsExist);
                result.addParam("requestId", requestId);
            }
            result.addParam("opstatus", "0");
            result.addParam("httpStatusCode", "200");
        } catch (Exception e) {
            result.addParam("Status", "FilterPendingApprovals Service Failed");
            result.addParam("Exception Message", e.getMessage());
        }
        return result;
    }
}