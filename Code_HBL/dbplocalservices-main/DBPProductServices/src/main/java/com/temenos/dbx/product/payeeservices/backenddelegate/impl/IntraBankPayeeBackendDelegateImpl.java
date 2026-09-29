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
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.commonsutils.CommonUtils;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.dbx.product.payeeservices.backenddelegate.api.IntraBankPayeeBackendDelegate;
import com.temenos.dbx.product.payeeservices.dto.IntraBankPayeeBackendDTO;

/**
 * @author Subrahmanyam Yadav
 * @version 1.0
 * implements {@link IntraBankPayeeBackendDelegate}
 */
public class IntraBankPayeeBackendDelegateImpl implements IntraBankPayeeBackendDelegate {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public List<IntraBankPayeeBackendDTO> fetchPayees(Set<String> payeeIds, 
			Map<String, Object> headerParams, DataControllerRequest dcRequest) {
		List<IntraBankPayeeBackendDTO> backendPayeeDTOs = new ArrayList<IntraBankPayeeBackendDTO>();

		String serviceName = ServiceId.BACKENDPAYEESERVICESORCH;
		String operationName = OperationName.BACKENDINTRABANKPAYEEGETORCH;
        
		Map<String, Object> requestParameters = new HashMap<String, Object>();
        requestParameters.put("id", String.join(",", payeeIds));
        requestParameters.put("loop_count", String.valueOf(payeeIds.size()));
        
        Map<String, Object> customer = CustomerSession.getCustomerMap(dcRequest);
		if (CustomerSession.IsBusinessUser(customer)) {
			serviceName = "BackendPayeeServicesOrch";
			operationName = "backendIntraBankPayeeGetOrch";
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
			JSONArray records = responseObj.getJSONArray("LoopDataset");
			for(int i = 0; i < records.length(); i++) {
				List<IntraBankPayeeBackendDTO> payeeDTOs = new ArrayList<IntraBankPayeeBackendDTO>();
				responseObj = records.getJSONObject(i);
	            if(responseObj.has("errmsg") || responseObj.has("dbpErrMsg") || responseObj.has("errorMessage")) {
	            	IntraBankPayeeBackendDTO payee = new IntraBankPayeeBackendDTO();
	                payee = JSONUtils.parse(responseObj.toString(), IntraBankPayeeBackendDTO.class);
	                backendPayeeDTOs.add(payee);
	                return backendPayeeDTOs;
	            }
	            JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
	            if(jsonArray != null)
					payeeDTOs = JSONUtils.parseAsList(jsonArray.toString(), IntraBankPayeeBackendDTO.class);
				backendPayeeDTOs.addAll(payeeDTOs);
			}
        }
        catch(JSONException e) {
            alert.prepareError("Failed to fetch intra bank payees: " + e).log();
            return null;
        }
        catch(Exception e) {
            alert.prepareError("Caught exception at fetchPayeesFromBackend: " + e).log();
            return null;
        }
        return backendPayeeDTOs;
	}

	@Override
	public String validateForApprovals( DataControllerRequest request, Map<String, Object> requestParameters) {
		String serviceName = ServiceId.DBP_APPROVAL_REQUEST_SERVICES;
		String operationName = OperationName.VALIDATE_FOR_APPROVALS;
		try {
			String response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					withRequestHeaders(request.getHeaderMap()).
					withDataControllerRequest(request).
					build().getResponse();
			return response;
		} catch ( Exception exception ) {
			return null;
		}
	}

	@Override
	public String checkIfPayeeStatusInPending( DataControllerRequest request, HashMap<String, Object> requestParameters ) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BBREQUEST_GET;
		try {
			String response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					withRequestHeaders(request.getHeaderMap()).
					withDataControllerRequest(request).
					build().getResponse();
			return response;
		} catch ( Exception exception ) {
			return null;
		}
	}

	@Override
	public IntraBankPayeeBackendDTO createPayee(IntraBankPayeeBackendDTO intraBankPayeeBackendDTO, Map<String, Object> headerParams, DataControllerRequest dcRequest) {
		String serviceName = ServiceId.INTRA_BANK_PAYEE_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.INTRA_BANK_PAYEE_BACKEND_CREATE;
		
		Map<String, Object> customer = CustomerSession.getCustomerMap(dcRequest);
		if (CustomerSession.IsBusinessUser(customer)) {
			serviceName = "IntraBankPayeeLOB";
			operationName = "IntraBankPayeeCreate";
		}
		
        Map<String, Object> requestParameters;
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(intraBankPayeeBackendDTO).toString(), String.class, Object.class);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the request params: " + e).log();
			return null;
		}
		
		requestParameters.put("IBAN", requestParameters.get("iban"));
			
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
			intraBankPayeeBackendDTO = JSONUtils.parse(response.toString(), IntraBankPayeeBackendDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to create payee at payee table: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at createPayeeAtBackend: " + e).log();
			return null;
		}
		
		return intraBankPayeeBackendDTO;
	}

	@Override
	public IntraBankPayeeBackendDTO deletePayee(IntraBankPayeeBackendDTO intraBankPayeeBackendDTO,
														 Map<String, Object> headerParams, DataControllerRequest dcRequest) {
		String serviceName = ServiceId.INTRA_BANK_PAYEE_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.INTRA_BANK_PAYEE_BACKEND_DELETE;
	        
        Map<String, Object> requestParameters;
        
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(intraBankPayeeBackendDTO).toString(), String.class, Object.class);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the request params: " + e).log();
			return null;
		}
		
		requestParameters.put("IBAN", requestParameters.get("iban"));
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
			intraBankPayeeBackendDTO = JSONUtils.parse(response.toString(), IntraBankPayeeBackendDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to delete payee from payee table: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at deletePayeeAtBackend: " + e).log();
			return null;
		}
		
		return intraBankPayeeBackendDTO;
	}
	
	@Override
	public IntraBankPayeeBackendDTO editPayee(IntraBankPayeeBackendDTO intraBankPayeeBackendDTO,
			Map<String, Object> headerParams, DataControllerRequest dcRequest) {
		String serviceName = ServiceId.INTRA_BANK_PAYEE_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.INTRA_BANK_PAYEE_BACKEND_EDIT;
	        
        Map<String, Object> requestParameters;
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(intraBankPayeeBackendDTO).toString(), String.class, Object.class);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the request params: " + e).log();
			return null;
		}
		
		requestParameters.put("IBAN", requestParameters.get("iban"));
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
			intraBankPayeeBackendDTO = JSONUtils.parse(response.toString(), IntraBankPayeeBackendDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to edit payee at payee table: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at editPayeeAtBackend: " + e).log();
			return null;
		}
		
		return intraBankPayeeBackendDTO;
	}
	
	@Override
	public boolean validateBeneficiaryNameFromAccountId(String accountNumber, String beneficiaryName, DataControllerRequest dcr) {
		return true;
	}
}
