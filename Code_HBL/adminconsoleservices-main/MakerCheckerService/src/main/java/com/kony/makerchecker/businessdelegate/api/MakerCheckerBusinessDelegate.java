package com.kony.makerchecker.businessdelegate.api;


import java.util.List;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;

public interface MakerCheckerBusinessDelegate extends BusinessDelegate {

	public Map<String,String> isMakerCheckerEnabled(DataControllerRequest request, String expApiOperationName, String legalEntityId) throws Exception;
	
	public JSONObject storePayloadForRequest(DataControllerRequest request, Map<String, String> inputParam);
	
	public JSONObject getDashboardCounts(DataControllerRequest request, Map<String, String> inputParam) throws Exception;
	
	public JSONObject getMakerCheckerPendingRequests(DataControllerRequest request,Map<String, String> inputParams) throws Exception;

	
	public JSONObject approvalRequestViewDetails(DataControllerRequest request, Map<String, String> inputParam) throws Exception;

	public JSONObject getRequestsHistory(DataControllerRequest request, Map<String, Object> inputParams) throws Exception;
	public JSONObject approveRejectRequest(DataControllerRequest dataControllerRequest,
			String requestId, String action, String comments, HashMap<String, String> approvalRequestDetails);

	public JSONObject getMakerCheckerConfig(DataControllerRequest request, Map<String, String> inputParams);
	
	public String getApprovalRequests(DataControllerRequest request, String reqId, List<String> excludedParamsList) throws Exception;

	public JSONObject getCheckerApprovalRequests(DataControllerRequest request, Map<String, String> inputParams) throws Exception;

	public JSONObject getAllMakerPendingRequests(DataControllerRequest request, Map<String, String> inputParams)
			throws Exception;
			
	public JSONObject getMCModuleActionOperation(DataControllerRequest request, Map<String, String> inputParams) throws Exception;

	public JSONObject updateMakerCheckerConfig(DataControllerRequest request, Map<String, Object> inputParams);

	public JSONObject withdrawRequest(DataControllerRequest request, Map<String, Object> inputParams);
	
	public JSONObject viewCustomerDetails(DataControllerRequest request, DataControllerResponse response, Map<String, Object> inputParams);

	public JSONObject enrollCustomerViewDetails(DataControllerRequest request, Map<String, String> inputParams);
	
	public JSONObject viewEditContractDetails(DataControllerRequest request, DataControllerResponse response, String requestId);
	
	public JSONObject createSignatoryGroupViewDetails(DataControllerRequest request, DataControllerResponse response, String requestId);
	
	public JSONObject deleteSignatoryGroupViewDetails(DataControllerRequest request, DataControllerResponse response, String requestId);
	
	public JSONObject editSignatoryGroupViewDetails(DataControllerRequest request, DataControllerResponse response, String requestId);
	
	public JSONObject createApprovalRuleBySignatoryGroupViewDetails(DataControllerRequest request, DataControllerResponse response, String requestId);

	public JSONObject createContractViewDetails(DataControllerRequest request, Map<String, String> inputParams);

}
