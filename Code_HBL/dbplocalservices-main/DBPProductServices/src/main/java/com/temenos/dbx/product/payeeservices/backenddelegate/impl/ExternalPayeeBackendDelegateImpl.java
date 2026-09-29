package com.temenos.dbx.product.payeeservices.backenddelegate.impl;

import java.io.IOException;
import java.util.ArrayList;
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
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.commonsutils.CommonUtils;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.dbx.product.payeeservices.backenddelegate.api.ExternalPayeeBackendDelegate;
import com.temenos.dbx.product.payeeservices.dto.ExternalPayeeBackendDTO;
import com.temenos.dbx.product.payeeservices.dto.PayeesFilterDTO;

/**
 * @author Subrahmanyam Yadav
 * @version 1.0
 * implements {@link ExternalPayeeBackendDelegate}
 */
public class ExternalPayeeBackendDelegateImpl implements ExternalPayeeBackendDelegate{
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public List<ExternalPayeeBackendDTO> fetchPayees(PayeesFilterDTO payeesFilterDTO, Map<String, Object> headerParams,
			DataControllerRequest dcRequest) {
		List<ExternalPayeeBackendDTO> payeeDTOs = new ArrayList<ExternalPayeeBackendDTO>();
				
		String serviceName = ServiceId.EXTERNAL_PAYEE_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.EXTERNALPAYEEPAYEE_BACKEND_GET;
		
		Map<String, Object> customer = CustomerSession.getCustomerMap(dcRequest);
		if (CustomerSession.IsBusinessUser(customer)) {
			serviceName = "ExternalPayeeLOB";
			operationName = "ExternalPayeeGet";
		} 
        
		Map<String, Object> requestParameters;
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(payeesFilterDTO).toString(), String.class, Object.class);
		} catch (IOException e) {
			alert.prepareError("Error occurred while fetching the input params: " + e).log();
			return null;
		}
		
        String payeeResponse = null;
        try {
        	payeeResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					withRequestHeaders(headerParams).
					withDataControllerRequest(dcRequest).
					build().getResponse();
        	
			JSONObject responseObj = new JSONObject(payeeResponse);
			if(responseObj.has("errmsg") || responseObj.has("dbpErrMsg") || responseObj.has("errorMessage")) {
				ExternalPayeeBackendDTO payee = new ExternalPayeeBackendDTO();
				payee = JSONUtils.parse(responseObj.toString(), ExternalPayeeBackendDTO.class);
				payeeDTOs.add(payee);
				return payeeDTOs;
			}
			JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
			payeeDTOs = JSONUtils.parseAsList(jsonArray.toString(), ExternalPayeeBackendDTO.class);
        }
        catch(JSONException e) {
            alert.prepareError("Failed to fetch external payees from externalaccount table: " + e).log();
            return null;
        }
        catch(Exception e) {
            alert.prepareError("Caught exception at fetchPayeesFromBackend: " + e).log();
            return null;
        }

        return payeeDTOs;
	}

	@Override
	public Map<String, String> fetchUserNameByCoreCustomerId(Map<String, Object> serviceReqMap,
			Map<String, Object> headerParams, DataControllerRequest dcRequest) {
		Map<String, String> custIdUserNameMap = new HashMap<>();
		
		String serviceName = ServiceId.FETCHUSERNAMESERVICESORCH;
		String operationName = OperationName.GETUSERNAMEBYCORECUSTID;
		
        String backendResponse = null;
        try {
        	backendResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(serviceReqMap).
					withRequestHeaders(headerParams).
					withDataControllerRequest(dcRequest).
					build().getResponse();
        	
			JSONObject responseObj = new JSONObject(backendResponse);
			JSONArray records = responseObj.getJSONArray("LoopDataset");
			for(int i = 0; i < records.length(); i++) {
				responseObj = records.getJSONObject(i);
				if(responseObj.has("errmsg") || responseObj.has("dbpErrMsg") || responseObj.has("errorMessage")) {
					custIdUserNameMap = null;
	                return custIdUserNameMap;
	            }
				JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
				if(jsonArray != null) {
					JSONObject respObj = jsonArray.getJSONObject(0);
					custIdUserNameMap.put(respObj.getString("BackendId"), respObj.getString("UserName"));
				}
			}
			
        }
        catch(JSONException e) {
            alert.prepareError("Failed to fetch details from payee table: " + e).log();
            return null;
        }
        catch(Exception e) {
            alert.prepareError("Caught exception at fetchUserNameByCoreCustomerId: " + e).log();
            return null;
        }
        return custIdUserNameMap;
	}
}
