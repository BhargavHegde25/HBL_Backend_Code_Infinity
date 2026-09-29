package com.kony.adminconsole.service.approvalmatrix.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface ApprovalMatrixResource extends Resource {

	public Result getApprovalMatrix(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
	public Result createApprovalRuleSGLevel(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
	public Result createApprovalRuleUserLevel(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
	public Result getApprovalRules(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
	public Result updateApprovalRuleUserLevel(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
	public Result updateApprovalRuleSGLevel(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
	public Result getApproversInSignatoryGroup(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
	public Result getApprovalMatrixByContractId(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
	public Result getAccountActionCustomerApproverList(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
	public Result updateApprovalMatrixStatus(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
	public Result isApprovalMatrixDisabled(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
}
