package com.kony.adminconsole.service.signatorygroup.businessdelegate.impl;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.service.signatorygroup.businessdelegate.api.SignatoryGroupBusinessDelegate;
import com.kony.adminconsole.utilities.OperationName;
import com.kony.adminconsole.utilities.ServiceId;

public class SignatoryGroupBusinessDelegateImpl implements SignatoryGroupBusinessDelegate {

	@Override
	public JSONObject createSignatoryGroup(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_CREATE_SG).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build()
                        .getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}

	@Override
	public JSONObject updateSignatoryGroups(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_UPDATE_SG).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build()
                        .getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}

	@Override
	public JSONObject deleteSignatoryGroup(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_DELETE_SG).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build()
                        .getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}

	@Override
	public JSONObject getNoGroupUsers(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_GET_NOGROUP_USERS).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build()
                        .getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}

	@Override
	public JSONObject getApprovalPermissionsForUser(Map<String, Object> postParametersMap,
			String dbpServicesClaimsToken) throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_GET_APPROVALPERMISSION_FOR_USER).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build()
                        .getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}

	@Override
	public JSONObject getAllSignatoryGroupsbyCoreCustomerIds(Map<String, Object> postParametersMap,
			String dbpServicesClaimsToken) throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_GET_ALL_SG_BY_CORECUSTOMER_ID).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build()
                        .getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}

	@Override
	public JSONObject getAllSignatoryGroups(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_GET_ALL_SG).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build()
                        .getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}
	
	@Override
	public JSONObject getSignatoryGroupDetails(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_GET_SG_DETAILS).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build()
                        .getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}

	@Override
	public JSONObject fetchApprovalMode(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_FETCH_APPROVAL_MODE).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build()
                        .getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}

	@Override
	public JSONObject updateApprovalMode(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_UPDATE_APPROVAL_MODE).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build()
                        .getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}

	@Override
	public JSONObject deleteApprovalMode(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_DELETE_APPROVAL_MODE).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build()
                        .getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}
	
	@Override
	public JSONObject isSignatoryGroupEligibleForDelete(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_IF_ELIGIBLE_DELETE_SG).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build()
                        .getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}

}
