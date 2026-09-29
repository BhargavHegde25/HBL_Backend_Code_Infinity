package com.kony.adminconsole.service.approvalrequests.backenddelegate.impl;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.adminconsole.service.approvalrequests.backenddelegate.api.ApprovalRequestsBackendDelegate;
import com.kony.adminconsole.utilities.ApprovalConstants;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Map;

public class ApprovalRequestsBackendDelegateImpl implements ApprovalRequestsBackendDelegate {

    public static final String APPROVALCRUD_SERVICE = "ApprovalCRUDLayer";
    public static final String APPROVALREQUESTS_GET = "dbxdb_approvalrequests_get";
    public static final String FETCH_APPROVALREQUESTS_COUNTS_PROC = "dbxdb_fetch_approvalrequests_counts_proc";
    public static final String FETCH_PENDING_APPROVALS_PROC = "dbxdb_fetch_pending_approvals_proc";
    public static final String FETCH_PENDING_APPROVALS_PROC_KC = "dbxdb_fetch_pending_approvals_proc_kc";
    public static final String FETCH_APPROVAL_HISTORY_PROC = "dbxdb_fetch_approval_history_proc";
    public static final String FETCH_APPROVAL_HISTORY_PROC_KC = "dbxdb_fetch_approval_history_proc_kc";
    public static final String APPROVAL_REQUEST_TABLE = "approvalrequests";
    public static final String PROC_RECORDS_KEY = "records";

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    @Override
    public JSONArray getApprovalRequestDBData(Map<String, Object> reqParams, Map<String, Object> reqHeaders) {
        JSONArray dbResponseArray = new JSONArray();
        try{
            String responseString = DBPServiceExecutorBuilder.builder()
                    .withServiceId(APPROVALCRUD_SERVICE)
                    .withObjectId(null)
                    .withOperationId(APPROVALREQUESTS_GET)
                    .withRequestHeaders(reqHeaders)
                    .withRequestParameters(reqParams)
                    .build()
                    .getResponse();
            JSONObject obj = new JSONObject(responseString);
            if(obj.has(APPROVAL_REQUEST_TABLE)){
                dbResponseArray = obj.getJSONArray(APPROVAL_REQUEST_TABLE);
            }
        } catch (Exception e){
            alert.prepareError("Error executing service- " + APPROVALCRUD_SERVICE + "/" + APPROVALREQUESTS_GET + " : " + e).log();
            return null;
        }
        return dbResponseArray;
    }

    @Override
    public JSONArray getApprovalRequestCountsDBData(Map<String, Object> reqParams, Map<String, Object> reqHeaders) {
        JSONArray dbResponseArray = getBackendDBData(APPROVALCRUD_SERVICE, FETCH_APPROVALREQUESTS_COUNTS_PROC, reqParams, reqHeaders);
        return dbResponseArray;
    }

    @Override
    public JSONArray getUserPendingApprovalsDBData(Map<String, Object> reqParams, Map<String, Object> reqHeaders){
        String isKeyCloakEnabled = reqParams.containsKey(ApprovalConstants.IS_KEYCLOAK_ENABLED) ? (String)reqParams.get(ApprovalConstants.IS_KEYCLOAK_ENABLED) : null;
        JSONArray dbResponseArray;
        if(isKeyCloakEnabled == null || isKeyCloakEnabled.equals("false")){
            dbResponseArray = getBackendDBData(APPROVALCRUD_SERVICE, FETCH_PENDING_APPROVALS_PROC, reqParams, reqHeaders);
        }
        else{
            dbResponseArray = getBackendDBData(APPROVALCRUD_SERVICE, FETCH_PENDING_APPROVALS_PROC_KC, reqParams, reqHeaders);
        }
        return dbResponseArray;
    }

    @Override
    public JSONArray getUserApprovalHistoryDBData(Map<String, Object> reqParams, Map<String, Object> reqHeaders) {
        String isKeyCloakEnabled = reqParams.containsKey(ApprovalConstants.IS_KEYCLOAK_ENABLED) ? (String)reqParams.get(ApprovalConstants.IS_KEYCLOAK_ENABLED) : null;
        JSONArray dbResponseArray;
        if(isKeyCloakEnabled == null || isKeyCloakEnabled.equals("false")){
            dbResponseArray = getBackendDBData(APPROVALCRUD_SERVICE, FETCH_APPROVAL_HISTORY_PROC, reqParams, reqHeaders);
        }
        else{
            dbResponseArray = getBackendDBData(APPROVALCRUD_SERVICE, FETCH_APPROVAL_HISTORY_PROC_KC, reqParams, reqHeaders);
        }
        return dbResponseArray;
    }

    private static JSONArray getBackendDBData(String serviceName, String operationName, Map<String, Object> reqParams, Map<String, Object> reqHeaders) {
        JSONArray dbResponseArray = new JSONArray();
        try{
            String responseString = DBPServiceExecutorBuilder.builder()
                    .withServiceId(serviceName)
                    .withObjectId(null)
                    .withOperationId(operationName)
                    .withRequestHeaders(reqHeaders)
                    .withRequestParameters(reqParams)
                    .build()
                    .getResponse();
            JSONObject obj = new JSONObject(responseString);
            if(obj.has(PROC_RECORDS_KEY)){
                dbResponseArray = obj.getJSONArray(PROC_RECORDS_KEY);
            }
        } catch (Exception e){
            alert.prepareError("Error executing service- " + serviceName + "/" + operationName + " : " + e).log();
            return null;
        }
        return dbResponseArray;
    }
}
