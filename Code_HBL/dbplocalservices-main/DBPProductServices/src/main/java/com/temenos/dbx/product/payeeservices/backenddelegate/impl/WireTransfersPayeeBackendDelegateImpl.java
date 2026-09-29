package com.temenos.dbx.product.payeeservices.backenddelegate.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.temenos.dbx.product.commonsutils.CommonUtils;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.payeeservices.backenddelegate.api.WireTransfersPayeeBackendDelegate;
import com.temenos.dbx.product.payeeservices.dto.WireTransfersPayeeBackendDTO;

/**
 * @author Subrahmanyam Yadav
 * @version 1.0
 * implements {@link WireTransfersPayeeBackendDelegate}
 */
public class WireTransfersPayeeBackendDelegateImpl implements WireTransfersPayeeBackendDelegate {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	@Override
	public List<WireTransfersPayeeBackendDTO> fetchPayees(Set<String> payeeIds,
			Map<String, Object> headerParams, DataControllerRequest dcRequest) {
		List<WireTransfersPayeeBackendDTO> backendPayeeDTOs = new ArrayList<WireTransfersPayeeBackendDTO>();

		String serviceName = ServiceId.BACKENDPAYEESERVICESORCH;
		String operationName = OperationName.BACKENDWIRETRANSFERSPAYEEGETORCH;
        
		Map<String, Object> requestParameters = new HashMap<String, Object>();
        requestParameters.put("id", String.join(",", payeeIds));
        requestParameters.put("loop_count", String.valueOf(payeeIds.size()));
		
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
			JSONArray records = responseObj.getJSONArray("LoopDataset");
			for(int i = 0; i < records.length(); i++) {
				List<WireTransfersPayeeBackendDTO> payeeDTOs = new ArrayList<WireTransfersPayeeBackendDTO>();
				responseObj = records.getJSONObject(i);
				if(responseObj.has("errmsg") || responseObj.has("dbpErrMsg") || responseObj.has("errorMessage")) {
					WireTransfersPayeeBackendDTO payee = new WireTransfersPayeeBackendDTO();
					payee = JSONUtils.parse(responseObj.toString(), WireTransfersPayeeBackendDTO.class);
	                backendPayeeDTOs.add(payee);
	                return backendPayeeDTOs;
	            }
				WireTransfersPayeeBackendDTO payee = JSONUtils.parse(responseObj.toString(), WireTransfersPayeeBackendDTO.class);
				payeeDTOs.add(payee);
				JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
				if(jsonArray != null)
					payeeDTOs = JSONUtils.parseAsList(jsonArray.toString(), WireTransfersPayeeBackendDTO.class);
				backendPayeeDTOs.addAll(payeeDTOs);
			}
			
        }
        catch(JSONException e) {
            alert.prepareError("Failed to fetch billpay payees from payee table: " + e).log();
            return null;
        }
        catch(Exception e) {
            alert.prepareError("Caught exception at fetchPayees: " + e).log();
            return null;
        }

        return backendPayeeDTOs;
	}

	@Override
	public WireTransfersPayeeBackendDTO createPayee(WireTransfersPayeeBackendDTO wireTransfersPayeeBackendDTO,
			Map<String, Object> headerParams, DataControllerRequest dcRequest) {
		String serviceName = ServiceId.WIRE_TRANSFERS_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.WIRETRANSFERSPAYEE_BACKEND_CREATE;
	        
        Map<String, Object> requestParameters;
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(wireTransfersPayeeBackendDTO).toString(), String.class, Object.class);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the request params: " + e).log();
			return null;
		}
			
		String createResponse = null;
		try {
			createResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					withRequestHeaders(headerParams).
					withDataControllerRequest(dcRequest).
					build().getResponse();
			
			JSONObject response = new JSONObject(createResponse);
			wireTransfersPayeeBackendDTO = JSONUtils.parse(response.toString(), WireTransfersPayeeBackendDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to create payee at payee table: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at createPayeeAtBackend: " + e).log();
			return null;
		}
		
		return wireTransfersPayeeBackendDTO;
	}
	
	@Override
	public WireTransfersPayeeBackendDTO deletePayee(WireTransfersPayeeBackendDTO wireTransfersPayeeBackendDTO,
			Map<String, Object> headerParams, DataControllerRequest dcRequest) {
		String serviceName = ServiceId.WIRE_TRANSFERS_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.WIRETRANSFERSPAYEE_BACKEND_DELETE;
	        
        Map<String, Object> requestParameters;
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(wireTransfersPayeeBackendDTO).toString(), String.class, Object.class);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the request params: " + e).log();
			return null;
		}
		
		requestParameters.put("Id", requestParameters.get("id"));
		
		String deleteResponse = null;
		try {
			deleteResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					withRequestHeaders(headerParams).
					withDataControllerRequest(dcRequest).
					build().getResponse();
			
			JSONObject response = new JSONObject(deleteResponse);
			wireTransfersPayeeBackendDTO = JSONUtils.parse(response.toString(), WireTransfersPayeeBackendDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to delete payee from payee table: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at deletePayeeAtBackend: " + e).log();
			return null;
		}
		
		return wireTransfersPayeeBackendDTO;

	}
	
	@Override
	public WireTransfersPayeeBackendDTO editPayee(WireTransfersPayeeBackendDTO wireTransfersPayeeBackendDTO,
			Map<String, Object> headerParams, DataControllerRequest dcRequest) {
		String serviceName = ServiceId.WIRE_TRANSFERS_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.WIRETRANSFERSPAYEE_BACKEND_EDIT;
	        
        Map<String, Object> requestParameters;
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(wireTransfersPayeeBackendDTO).toString(), String.class, Object.class);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the request params: " + e).log();
			return null;
		}
			
		requestParameters.put("Id", requestParameters.get("id"));
		
		String editResponse = null;
		try {
			editResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					withRequestHeaders(headerParams).
					withDataControllerRequest(dcRequest).
					build().getResponse();
			
			JSONObject response = new JSONObject(editResponse);
			wireTransfersPayeeBackendDTO = JSONUtils.parse(response.toString(), WireTransfersPayeeBackendDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to edit payee at payee table: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at editPayeeAtBackend: " + e).log();
			return null;
		}
		
		return wireTransfersPayeeBackendDTO;

	}

}
