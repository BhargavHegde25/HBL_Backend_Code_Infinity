package com.temenos.infinity.api.arrangements.businessdelegate.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.temenos.infinity.api.arrangements.utils.CommonUtils;
import com.temenos.infinity.api.arrangements.constants.Constants;
import com.temenos.infinity.api.arrangements.constants.OperationName;
import com.temenos.infinity.api.arrangements.constants.ServiceId;
import com.temenos.infinity.api.arrangements.dto.CustomViewDTO;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.temenos.infinity.api.arrangements.businessdelegate.api.CustomViewBusinessDelegate;

public class CustomViewBusinessDelegateImpl implements CustomViewBusinessDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	@Override
	public List<CustomViewDTO> getCustomView(CustomViewDTO customViewDTO) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
        String operationName = OperationName.DB_CUSTOMVIEW_GET;
	        
        Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = "";
		String customerId = customViewDTO.getCustomerId();
		String id = customViewDTO.getId();
		String coreCustomerId = customViewDTO.getCoreCustomerId();
		if(customerId != null && !customerId.isEmpty()) {
			filter = "customerId" + DBPUtilitiesConstants.EQUAL + customerId;
		}
		if(id != null && !id.isEmpty()) {
			if(!filter.isEmpty()) {
				filter = filter + DBPUtilitiesConstants.AND;
			}
			filter = filter + "id" + DBPUtilitiesConstants.EQUAL + id;
		}
		
		if(coreCustomerId != null && !coreCustomerId.isEmpty()) {
			if(!filter.isEmpty()) {
				filter = filter + DBPUtilitiesConstants.AND;
			}
			filter = filter + "coreCustomerId" + DBPUtilitiesConstants.EQUAL + coreCustomerId;
		}
		
		diagnostic.prepareDebug("************Filter in getCustomView:"+filter).log();
		
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);
			
		List<CustomViewDTO> customViewDTOs = null;
		String customViewResponse = null;
		try {
			customViewResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(customViewResponse);
			JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
			customViewDTOs = JSONUtils.parseAsList(jsonArray.toString(), CustomViewDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch custom views from table: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at getCustomViews: " + e).log();
			return null;
		}
		
		return customViewDTOs;
	}

	@Override
	public CustomViewDTO createCustomView(CustomViewDTO customViewDTO) {

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_CUSTOMVIEW_CREATE;

		Map<String, Object> requestParameters =  new HashMap<String, Object>();
		
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(customViewDTO).toString(), String.class, Object.class);

		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " + e).log();
			return null;
		}
		
		diagnostic.prepareDebug("************Request parameters in createCustomView:"+requestParameters).log();
		String response = null;
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			JSONArray responseArray = responseObj.getJSONArray(Constants.CUSTOMVIEW);
			customViewDTO = JSONUtils.parse(responseArray.getJSONObject(0).toString(), CustomViewDTO.class);
		} catch (JSONException e) {
			alert.prepareError("Unable to Create Custom View: " + e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at createCustomView method: " + e).log();
			return null;
		}

		return customViewDTO;
	}

	@Override
	public boolean deleteCustomView(String id) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_CUSTOMVIEW_DELETE;
		
		Map<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put("id", id);
		
		try {
			String deleteResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					build().getResponse();
			JSONObject jsonRsponse = new JSONObject(deleteResponse);
			if(jsonRsponse.getInt("opstatus") == 0 && jsonRsponse.getInt("httpStatusCode") == 0 && jsonRsponse.getInt("deletedRecords") == 1) {
				return true;
			}
		}
		
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while deleting the custom view",jsonExp).log();
			return false;
		}
		catch(Exception exp) {
			alert.prepareError("Excpetion occured while deleting the custom view",exp).log();
			return false;
		}
		
		return false;	
	}

	@Override
	public CustomViewDTO editCustomView(CustomViewDTO customViewDTO) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_CUSTOMVIEW_EDIT;

		Map<String, Object> requestParameters =  new HashMap<String, Object>();
		
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(customViewDTO).toString(), String.class, Object.class);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " + e).log();
			return null;
		}
		
		String response = null;
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			JSONArray responseArray = responseObj.getJSONArray(Constants.CUSTOMVIEW);
			customViewDTO = JSONUtils.parse(responseArray.getJSONObject(0).toString(), CustomViewDTO.class);
		} catch (JSONException e) {
			alert.prepareError("Unable to edit Custom View: " + e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at editCustomView method: " + e).log();
			return null;
		}

		return customViewDTO;
	}

}
