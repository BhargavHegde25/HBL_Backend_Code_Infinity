package com.kony.makerchecker.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface MakerCheckerResource extends Resource {
	
	public Result isMakerCheckerEnabled(String method, Object[] inputArray,
			DataControllerRequest dataControllerRequest, DataControllerResponse dataControllerResponse);
	
	public Result storePayloadForRequest(String method, Object[] inputArray,
			DataControllerRequest dataControllerRequest, DataControllerResponse dataControllerResponse);
	
	public Result getDashboardCounts(String method, Object[] inputArray,
			DataControllerRequest dataControllerRequest, DataControllerResponse dataControllerResponse);
	
	public Result getMakerCheckerPendingRequests(String method, Object[] inputArray,
	DataControllerRequest dataControllerRequest, DataControllerResponse dataControllerResponse);

	public Result approvalRequestViewDetails(String method, Object[] inputArray,
	DataControllerRequest dataControllerRequest, DataControllerResponse dataControllerResponse);

	public Result approveRejectRequest(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

	public Result requestsHistory(String method, Object[] inputArray,
			DataControllerRequest dataControllerRequest, DataControllerResponse dataControllerResponse);

	public Result getMakerCheckerConfig(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	public Result parseApprovalRequestDetails(String method, Object[] inputArray,
			DataControllerRequest dataControllerRequest, DataControllerResponse dataControllerResponse);

	public Result getAllMakerPendingRequests(String method, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse dataControllerResponse);
	
	public Result getCheckerApprovalRequests(String method, Object[] inputArray, 
			DataControllerRequest dataControllerRequest, DataControllerResponse dataControllerResponse);
	
	public Result getMCModuleActionOperation(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

	public Result updateMakerCheckerConfig(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	public Result withdrawRequest(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	public Result viewCustomerDetails(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

	public Result enrollCustomerViewDetails(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

	public Result createContractViewDetails(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	public Result editContractViewDetails(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	public Result createSignatoryGroupViewDetails(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	public Result deleteSignatoryGroupViewDetails(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	public Result editSignatoryGroupViewDetails(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	public Result createApprovalRuleBySignatoryGroupViewDetails(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
}
