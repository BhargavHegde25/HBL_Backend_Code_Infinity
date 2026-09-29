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
import com.temenos.dbx.product.payeeservices.backenddelegate.api.BillPayPayeeBackendDelegate;
import com.temenos.dbx.product.payeeservices.businessdelegate.impl.BillPayPayeeBusinessDelegateImpl;
import com.temenos.dbx.product.payeeservices.dto.BillPayPayeeBackendDTO;

/**
 * 
 * @author KH2624
 * @version 1.0
 * implements {@link BillPayPayeeBackendDelegate}
 */

public class BillPayPayeeBackendDelegateImpl implements BillPayPayeeBackendDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	@Override
	public BillPayPayeeBackendDTO createPayee(BillPayPayeeBackendDTO billPayPayeeBackendDTO,
			Map<String, Object> headerParams, DataControllerRequest dcRequest) {
		
		String serviceName = ServiceId.BILL_PAY_PAYEE_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.BILLPAYPAYEE_BACKEND_CREATE;
	        
        Map<String, Object> requestParameters;
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(billPayPayeeBackendDTO).toString(), String.class, Object.class);
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
			billPayPayeeBackendDTO = JSONUtils.parse(response.toString(), BillPayPayeeBackendDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to create payee at payee table: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at createPayee: " + e).log();
			return null;
		}
		
		return billPayPayeeBackendDTO;
	}

	@Override
	public List<BillPayPayeeBackendDTO> fetchPayees(Set<String> payeeIds,
			Map<String, Object> headerParams, DataControllerRequest dcRequest) {
		List<BillPayPayeeBackendDTO> backendPayeeDTOs = new ArrayList<BillPayPayeeBackendDTO>();

		String serviceName = ServiceId.BACKENDPAYEESERVICESORCH;
		String operationName = OperationName.BACKENDBILLPAYPAYEEGETORCH;
        
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
				List<BillPayPayeeBackendDTO> payeeDTOs = new ArrayList<BillPayPayeeBackendDTO>();
				responseObj = records.getJSONObject(i);
				if(responseObj.has("errmsg") || responseObj.has("dbpErrMsg") || responseObj.has("errorMessage")) {
					BillPayPayeeBackendDTO payee = new BillPayPayeeBackendDTO();
					payee = JSONUtils.parse(responseObj.toString(), BillPayPayeeBackendDTO.class);
	                backendPayeeDTOs.add(payee);
	                return backendPayeeDTOs;
	            }
				BillPayPayeeBackendDTO payee = JSONUtils.parse(responseObj.toString(), BillPayPayeeBackendDTO.class);
				payeeDTOs.add(payee);
				JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
				if(jsonArray != null)
					payeeDTOs = JSONUtils.parseAsList(jsonArray.toString(), BillPayPayeeBackendDTO.class);
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
	public BillPayPayeeBackendDTO editPayee(BillPayPayeeBackendDTO billPayPayeeBackendDTO,
			Map<String, Object> headerParams, DataControllerRequest dcRequest) {
		
		String serviceName = ServiceId.BILL_PAY_PAYEE_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.BILLPAYPAYEE_BACKEND_EDIT;
	        
        Map<String, Object> requestParameters;
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(billPayPayeeBackendDTO).toString(), String.class, Object.class);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the request params: " + e).log();
			return null;
		}
			
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
			billPayPayeeBackendDTO = JSONUtils.parse(response.toString(), BillPayPayeeBackendDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to edit payee at payee table: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at editPayee: " + e).log();
			return null;
		}
		
		return billPayPayeeBackendDTO;

	}

	@Override
	public BillPayPayeeBackendDTO deletePayee(BillPayPayeeBackendDTO billPayPayeeBackendDTO,
			Map<String, Object> headerParams, DataControllerRequest dcRequest) {
		
		String serviceName = ServiceId.BILL_PAY_PAYEE_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.BILLPAYPAYEE_BACKEND_DELETE;
	        
        Map<String, Object> requestParameters;
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(billPayPayeeBackendDTO).toString(), String.class, Object.class);
		} catch (IOException e) {
			alert.prepareError("Error occurred while fetching the request params: " + e).log();
			return null;
		}
			
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
			billPayPayeeBackendDTO = JSONUtils.parse(response.toString(), BillPayPayeeBackendDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to delete payee from payee table: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at deletePayee: " + e).log();
			return null;
		}
		
		return billPayPayeeBackendDTO;
	}
	
}