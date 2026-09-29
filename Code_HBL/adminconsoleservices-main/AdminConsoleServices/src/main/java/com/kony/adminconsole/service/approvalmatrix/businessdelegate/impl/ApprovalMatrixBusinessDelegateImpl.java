package com.kony.adminconsole.service.approvalmatrix.businessdelegate.impl;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.service.approvalmatrix.businessdelegate.api.ApprovalMatrixBusinessDelegate;
import com.kony.adminconsole.utilities.OperationName;
import com.kony.adminconsole.utilities.ServiceId;

public class ApprovalMatrixBusinessDelegateImpl implements ApprovalMatrixBusinessDelegate {

	@Override
	public JSONObject getApprovalMatrix(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_GET_APPROVAL_MATRIX).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build()
                        .getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}
	
	@Override
	public JSONObject createApprovalRuleSGLevel(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_CREATE_APPROVAL_RULE_SG_LEVEL).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build()
                        .getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}
	
	@Override
	public JSONObject createApprovalRuleUserLevel(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_CREATE_APPROVAL_RULE_USER_LEVEL).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build()
                        .getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}
	
	@Override
	public JSONObject getApprovalRules(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_GET_APPROVAL_RULE).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build()
                        .getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}
	
	@Override
	public JSONObject updateApprovalRuleUserLevel(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_UPDATE_APPROVAL_RULE_USER_LEVEL).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build()
                        .getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}
	
	@Override
	public JSONObject updateApprovalRuleSGLevel(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_UPDATE_APPROVAL_RULE_SG_LEVEL).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build()
                        .getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}
	
	@Override
	public JSONObject getApproversInSignatoryGroup(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_GET_APPROVERS_IN_SIGNATORY_GROUP).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build()
                        .getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}
	
	@Override
	public JSONObject getApprovalMatrixByContractId(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_GET_APPROVAL_MATRIX_BY_CONTRACT_ID).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build()
                        .getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}
	
	@Override
	public JSONObject getAccountActionCustomerApproverList(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_GET_APPROVERS_LIST).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build()
                        .getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}
	
	@Override
	public JSONObject updateApprovalMatrixStatus(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
			throws DBPApplicationException {

		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_UPDATEAPPROVALMATRIXSTATUS).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build().getResponse();
        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}
	
	@Override
	public JSONObject isApprovalMatrixDisabled(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_ISAPPROVALMATRIXDISABLED).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build().getResponse();
        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}

}
