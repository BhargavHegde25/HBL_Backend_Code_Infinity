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
import com.temenos.dbx.product.payeeservices.backenddelegate.api.InternationalPayeeBackendDelegate;
import com.temenos.dbx.product.payeeservices.dto.InternationalPayeeBackendDTO;

/**
 * @author Subrahmanyam Yadav
 * @version 1.0
 * implements {@link InternationalPayeeBackendDelegate}
 */
public class InternationalPayeeBackendDelegateImpl implements InternationalPayeeBackendDelegate{
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	@Override
	public List<InternationalPayeeBackendDTO> fetchPayees(Set<String> payeeIds, 
			Map<String, Object> headerParams, DataControllerRequest dcRequest) {
		List<InternationalPayeeBackendDTO> backendPayeeDTOs = new ArrayList<InternationalPayeeBackendDTO>();

		String serviceName = ServiceId.BACKENDPAYEESERVICESORCH;
		String operationName = OperationName.BACKENDINTERNATIONALPAYEEGETORCH;
        
		Map<String, Object> requestParameters = new HashMap<String, Object>();
        requestParameters.put("id", String.join(",", payeeIds));
        requestParameters.put("loop_count", String.valueOf(payeeIds.size()));
        
        Map<String, Object> customer = CustomerSession.getCustomerMap(dcRequest);
		if (CustomerSession.IsBusinessUser(customer)) {
			serviceName = "BackendPayeeServicesOrch";
			operationName = "backendInternationalPayeeGetOrch";
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
				List<InternationalPayeeBackendDTO> payeeDTOs = new ArrayList<InternationalPayeeBackendDTO>();
				responseObj = records.getJSONObject(i);
	            if(responseObj.has("errmsg") || responseObj.has("dbpErrMsg") || responseObj.has("errorMessage")) {
	            	InternationalPayeeBackendDTO payee = new InternationalPayeeBackendDTO();
	                payee = JSONUtils.parse(responseObj.toString(), InternationalPayeeBackendDTO.class);
	                backendPayeeDTOs.add(payee);
	                return backendPayeeDTOs;
	            }
	            JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
	            if(jsonArray != null)
					payeeDTOs = JSONUtils.parseAsList(jsonArray.toString(), InternationalPayeeBackendDTO.class);
				backendPayeeDTOs.addAll(payeeDTOs);
			}
			
        }
        catch(JSONException e) {
            alert.prepareError("Failed to fetch international payees: " + e).log();
            return null;
        }
        catch(Exception e) {
            alert.prepareError("Caught exception at fetchPayeesFromBackend: " + e).log();
            return null;
        }
        return backendPayeeDTOs;
	}
	
	@Override
	public InternationalPayeeBackendDTO createPayee(InternationalPayeeBackendDTO InternationalPayeeBackendDTO, Map<String, Object> headerParams, DataControllerRequest dcRequest) {
		String serviceName = ServiceId.INTERNATIONAL_PAYEE_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.INTERNATIONAL_PAYEE_BACKEND_CREATE;
	        
		Map<String, Object> customer = CustomerSession.getCustomerMap(dcRequest);
		if (CustomerSession.IsBusinessUser(customer)) {
			serviceName = "InternationalPayeeLOB";
			operationName = "InternationalPayeeCreate";
		}
		
        Map<String, Object> requestParameters;
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(InternationalPayeeBackendDTO).toString(), String.class, Object.class);
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
			InternationalPayeeBackendDTO = JSONUtils.parse(response.toString(), InternationalPayeeBackendDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to create payee at payee table: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at createPayeeAtBackend: " + e).log();
			return null;
		}
		
		return InternationalPayeeBackendDTO;
	}

	@Override
	public InternationalPayeeBackendDTO deletePayee(InternationalPayeeBackendDTO InternationalPayeeBackendDTO,
			Map<String, Object> headerParams, DataControllerRequest dcRequest) {
		String serviceName = ServiceId.INTERNATIONAL_PAYEE_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.INTERNATIONAL_PAYEE_BACKEND_DELETE;
	        
        Map<String, Object> requestParameters;
        
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(InternationalPayeeBackendDTO).toString(), String.class, Object.class);
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
			InternationalPayeeBackendDTO = JSONUtils.parse(response.toString(), InternationalPayeeBackendDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to delete payee from payee table: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at deletePayeeAtBackend: " + e).log();
			return null;
		}
		
		return InternationalPayeeBackendDTO;
	}
	
	@Override
	public InternationalPayeeBackendDTO editPayee(InternationalPayeeBackendDTO InternationalPayeeBackendDTO,
			Map<String, Object> headerParams, DataControllerRequest dcRequest) {
		String serviceName = ServiceId.INTERNATIONAL_PAYEE_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.INTERNATIONAL_PAYEE_BACKEND_EDIT;
	        
        Map<String, Object> requestParameters;
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(InternationalPayeeBackendDTO).toString(), String.class, Object.class);
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
			InternationalPayeeBackendDTO = JSONUtils.parse(response.toString(), InternationalPayeeBackendDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to edit payee at payee table: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at editPayeeAtBackend: " + e).log();
			return null;
		}
		
		return InternationalPayeeBackendDTO;
	}
	
	@Override
	public boolean isValidIbanAndSwiftCode(String iban, String swiftcode, DataControllerRequest dcr) {
		return true;
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
	public String checkIfPayeeStatusInPending(DataControllerRequest request, Map<String, Object> requestParameters ) {
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
}
