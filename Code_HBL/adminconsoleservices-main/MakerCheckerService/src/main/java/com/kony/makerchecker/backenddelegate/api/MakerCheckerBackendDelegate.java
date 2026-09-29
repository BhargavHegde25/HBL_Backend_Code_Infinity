package com.kony.makerchecker.backenddelegate.api;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import com.dbp.core.api.BackendDelegate;
import com.kony.adminconsole.exception.DBPAuthenticationException;
import com.konylabs.middleware.controller.DataControllerRequest;

public interface MakerCheckerBackendDelegate extends BackendDelegate {
	
	public Map<String,String> isMakerCheckerEnabled(DataControllerRequest request, String expApiOperationName, String legalEntityId);
	public JSONObject storePayloadForRequest(DataControllerRequest request, Map<String, String> inputParams);
	public JSONObject getDashboardCounts(DataControllerRequest request, Map<String, String> inputParams);
	public JSONObject getMakerCheckerPendingRequests(DataControllerRequest request, Map<String, String> inputParams);

	public JSONObject approvalRequestViewDetails(DataControllerRequest request, Map<String, String> inputParams);
	
	public JSONObject getRequestsHistory(Map<String, Object> inputParams);
	
	public void callExpAPIAsyncAndGetResult(DataControllerRequest request, String service, String object, String operation,
			String requestId, HashMap<String, Object> payload, String username);
	public void updateApprovalRequestStatus(DataControllerRequest request, String requestId, String string,
			String comments, String currentStatus, String username);

	
	public JSONObject getMakerCheckerConfig(DataControllerRequest request, Map<String, String> inputParams);
	public JSONObject getApprovalRequests(DataControllerRequest request, String reqId);
	public JSONObject getAllMakerPendingRequests(DataControllerRequest request, Map<String, Object> inputParams);
	public JSONObject getCheckerApprovalRequests(DataControllerRequest request, Map<String, String> inputParams);
	public JSONObject getMCModuleActionOperation(DataControllerRequest request, Map<String, String> inputParams);
	
	public JSONObject updateMakerCheckerConfig(DataControllerRequest request, Map<String, Object> inputParams);
	
	public JSONObject getEnrollCustomerViewDetails(DataControllerRequest request, Map<String, String> inputParams);
	
	public JSONObject getEditCustomerViewDetails(DataControllerRequest request, Map<String, String> inputParams);
	
	public JSONObject getCreateApprovalRuleBySGViewDetails(DataControllerRequest request, Map<String, String> inputParams);
	
	public JSONObject updateApprovalRequests(DataControllerRequest request, Map<String, Object> inputParams);
	
	public JSONObject getContractDetails(DataControllerRequest request, Map<String, Object> inputParams) throws DBPAuthenticationException;
	
	public JSONObject getContractFeatureActionLimits(DataControllerRequest request, Map<String, Object> inputParams) throws DBPAuthenticationException;
	
	public JSONObject getSignatoryViewDetails(DataControllerRequest request, Map<String, Object> inputParams);
	

}
