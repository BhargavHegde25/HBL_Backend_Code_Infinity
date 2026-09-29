package com.kony.adminconsole.service.productmanagement.backenddelegate.impl;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.service.productmanagement.backenddelegate.api.FacilityBackendDelegate;
import com.kony.adminconsole.utilities.OperationName;
import com.kony.adminconsole.utilities.ServiceId;

public class FacilityBackendDelegateImpl implements FacilityBackendDelegate {
	
	
	@Override
	public JSONObject createFacility(Map<String, Object> postParametersMap, String backendToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", backendToken);
        
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.PRODUCTMS)
                        .withOperationId(OperationName.OP_CREATE_FACILITY)
                        .withRequestParameters(postParametersMap)
                        .withRequestHeaders(headerMap)
                        .build()
                        .getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}
	
	@Override
	public JSONObject editFacility(Map<String, Object> postParametersMap, String backendToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", backendToken);
		
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.PRODUCTMS)
                        .withOperationId(OperationName.OP_UPDATE_FACILITY)
                        .withRequestParameters(postParametersMap)
                        .withRequestHeaders(headerMap)
                        .build()
                        .getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}
	
	@Override
	public JSONObject editFacilityFeatures(Map<String, Object> postParametersMap, String backendToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", backendToken);
		
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.PRODUCTMS)
                        .withOperationId(OperationName.OP_UPDATE_FACILITY_FEATURES)
                        .withRequestParameters(postParametersMap)
                        .withRequestHeaders(headerMap)
                        .build()
                        .getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}
	
	@Override
	public JSONObject deleteFacilityFeatures(Map<String, Object> postParametersMap, String backendToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", backendToken);
		
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.PRODUCTMS)
                        .withOperationId(OperationName.OP_DELETE_FACILITY_FEATURES)
                        .withRequestParameters(postParametersMap)
                        .withRequestHeaders(headerMap)
                        .build()
                        .getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}
	
	@Override
	public JSONObject getFacility(Map<String, Object> postParametersMap, String backendToken)
			throws DBPApplicationException {

		
		if(postParametersMap.get("marketingCatalogBackend").equals("MS")){
			Map<String, Object> headerMap = new HashMap<>();
	        headerMap.put("backendToken", backendToken);
	        
	        String operationName = OperationName.OP_GET_FACILITY;
	        if(postParametersMap.isEmpty()) {
	        	operationName = OperationName.OP_GET_FACILITIES;
	        }
	        
	        String serviceResponse =
	                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.PRODUCTMS)
	                        .withOperationId(operationName)
	                        .withRequestParameters(postParametersMap)
	                        .withRequestHeaders(headerMap)
	                        .build()
	                        .getResponse();

	        return CommonUtilities.getStringAsJSONObject(serviceResponse);
		}else {
			
		
			String facilityId = (String) postParametersMap.get("facilityId");
			if(facilityId!=null && !"".equals(facilityId)){
			//	String filter="facilityId eq "+facilityId;
				postParametersMap.put("_facilityId", facilityId);
			}else {
				postParametersMap.put("_facilityId", "");
			}
            String response = DBPServiceExecutorBuilder.builder().
	                    withServiceId(ServiceId.CRUDLAYER).
	                    withOperationId(OperationName.DB_GET_FACILITIES).
	                    withRequestParameters(postParametersMap).
	                    build().getResponse();
	            JSONObject responseJSON = CommonUtilities.getStringAsJSONObject(response);
	            return responseJSON;
		}
				
		

	}

	@Override
	public JSONObject createFacilityFeatures(Map<String, Object> postParametersMap, String backendToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", backendToken);
		
		String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.PRODUCTMS)
                        .withOperationId(OperationName.OP_CREATE_FACILITY_FEATURES)
                        .withRequestParameters(postParametersMap)
                        .withRequestHeaders(headerMap)
                        .build()
                        .getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}
	
	
	@Override
	public JSONObject getFacilityFeatures(Map<String, Object> postParametersMap, String backendToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", backendToken);
		
		String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.PRODUCTMS)
                        .withOperationId(OperationName.OP_GET_FACILITY_FEATURES)
                        .withRequestParameters(postParametersMap)
                        .withRequestHeaders(headerMap)
                        .build()
                        .getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}
}
