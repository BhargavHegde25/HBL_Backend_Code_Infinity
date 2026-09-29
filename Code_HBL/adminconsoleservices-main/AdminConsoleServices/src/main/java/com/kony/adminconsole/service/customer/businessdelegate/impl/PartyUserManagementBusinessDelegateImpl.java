package com.kony.adminconsole.service.customer.businessdelegate.impl;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.service.customer.businessdelegate.api.PartyUserManagementBusinessDelegate;
import com.kony.adminconsole.utilities.OperationName;
import com.kony.adminconsole.utilities.ServiceId;

public class PartyUserManagementBusinessDelegateImpl implements PartyUserManagementBusinessDelegate {

	@Override
	public JSONObject createPartyUser(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_CREATE_PARTY_USER).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build().getResponse();
        return CommonUtilities.getStringAsJSONObject(serviceResponse);
        
	}

	@Override
	public JSONObject updatePartyUser(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_UPDATE_PARTY_USER).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build().getResponse();
        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}

	@Override
	public JSONObject searchPartyUser(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_SEARCH_PARTY_USER).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build().getResponse();
        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}

}
