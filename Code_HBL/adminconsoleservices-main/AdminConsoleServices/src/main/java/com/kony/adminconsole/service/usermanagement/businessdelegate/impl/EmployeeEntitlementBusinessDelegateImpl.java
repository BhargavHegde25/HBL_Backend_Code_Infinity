package com.kony.adminconsole.service.usermanagement.businessdelegate.impl;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.utilities.OperationName;
import com.kony.adminconsole.utilities.ServiceId;
import com.kony.adminconsole.service.usermanagement.businessdelegate.api.EmployeeEntitlementBusinessDelegate ;

public class EmployeeEntitlementBusinessDelegateImpl implements EmployeeEntitlementBusinessDelegate {

	@Override
	public JSONObject createEntitlementByUserId(Map<String, Object> postParametersMap, String backendToken ) throws DBPApplicationException {
		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", backendToken);
		
		String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.ENTITLEMENT_MS)
                        .withOperationId(OperationName.OP_CREATE_ENTITLEMENT_BY_USERID)
                        .withRequestParameters(postParametersMap)
                        .withRequestHeaders(headerMap)
                        .build()
                        .getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}

	@Override
	public JSONObject getEntitlementByUserId(Map<String, Object> postParametersMap, String backendToken) throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", backendToken);
		
		String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.ENTITLEMENT_MS)
                        .withOperationId(OperationName.OP_GET_ENTITLEMENT_BY_USERID)
                        .withRequestParameters(postParametersMap)
                        .withRequestHeaders(headerMap)
                        .build()
                        .getResponse();
		if(null !=serviceResponse && serviceResponse.contains("entitlements")) {
			JSONObject respJson = new JSONObject(serviceResponse);
			JSONArray respArr = respJson.getJSONArray("entitlements");
			for(int i=0; i< respArr.length(); i++) {
				JSONObject jsonObj = respArr.getJSONObject(i);
				JSONArray rolesArr = jsonObj.getJSONArray("roles");
				JSONArray newRolesArr = new JSONArray();
				for(int j=0; j<rolesArr.length(); j++) {
					JSONObject newObj = new JSONObject();
					newObj.put("id", rolesArr.get(i).toString());
					newRolesArr.put(newObj);
				}
				jsonObj.put("roles", newRolesArr);
			}
			return respJson;
		}
		
        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}

	@Override
	public JSONObject updateEntitlementByUserId(Map<String, Object> postParametersMap, String backendToken) throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", backendToken);
		
		String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.ENTITLEMENT_MS)
                        .withOperationId(OperationName.OP_UPDATE_ENTITLEMENT_BY_USERID)
                        .withRequestParameters(postParametersMap)
                        .withRequestHeaders(headerMap)
                        .build()
                        .getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}

	@Override
	public JSONObject getEntitlement(Map<String, Object> postParametersMap, String backendToken) throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", backendToken);
		
		String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.ENTITLEMENT_MS)
                        .withOperationId(OperationName.OP_GET_ENTITLEMENT)
                        .withRequestParameters(postParametersMap)
                        .withRequestHeaders(headerMap)
                        .build()
                        .getResponse();

		if(null !=serviceResponse && serviceResponse.contains("entitlements")) {
			JSONObject respJson = new JSONObject(serviceResponse);
			JSONArray respArr = respJson.getJSONArray("entitlements");
			for(int i=0; i< respArr.length(); i++) {
				JSONObject jsonObj = respArr.getJSONObject(i);
				JSONArray rolesArr = jsonObj.getJSONArray("roles");
				JSONArray newRolesArr = new JSONArray();
				for(int j=0; j<rolesArr.length(); j++) {
					JSONObject newObj = new JSONObject();
					newObj.put("id", rolesArr.get(i).toString());
					newRolesArr.put(newObj);
				}
				jsonObj.put("roles", newRolesArr);
			}
			return respJson;
		}
		
        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}

	@Override
	public JSONObject updateEntitlement(Map<String, Object> postParametersMap, String backendToken) throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", backendToken);
		
		String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.ENTITLEMENT_MS)
                        .withOperationId(OperationName.OP_UPDATE_ENTITLEMENT)
                        .withRequestParameters(postParametersMap)
                        .withRequestHeaders(headerMap)
                        .build()
                        .getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}

	@Override
	public JSONObject deleteEntitlement(Map<String, Object> postParametersMap, String backendToken) throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", backendToken);
		
		String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.ENTITLEMENT_MS)
                        .withOperationId(OperationName.OP_DELETE_ENTITLEMENT)
                        .withRequestParameters(postParametersMap)
                        .withRequestHeaders(headerMap)
                        .build()
                        .getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}

}
