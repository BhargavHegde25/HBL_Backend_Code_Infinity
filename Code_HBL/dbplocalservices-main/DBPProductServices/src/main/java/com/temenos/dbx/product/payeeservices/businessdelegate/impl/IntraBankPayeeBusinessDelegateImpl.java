package com.temenos.dbx.product.payeeservices.businessdelegate.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.temenos.dbx.product.commons.dto.TransactionStatusDTO;
import com.temenos.dbx.product.constants.TransactionStatusEnum;
import com.temenos.dbx.product.payeeservices.backenddelegate.api.IntraBankPayeeBackendDelegate;
import org.apache.commons.lang3.StringUtils;
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
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.payeeservices.businessdelegate.api.IntraBankPayeeBusinessDelegate;
import com.temenos.dbx.product.payeeservices.dto.IntraBankPayeeDTO;

/**
 * 
 * @author 
 * @version 1.0
 * implements {@link IntraBankPayeeBusinessDelegate}
 */

public class IntraBankPayeeBusinessDelegateImpl implements IntraBankPayeeBusinessDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public List<IntraBankPayeeDTO> fetchPayeesFromDBX(Set<String> associatedCifs, String legalEntityId) {
		
		final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
		final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_INTRABANKPAYEE_GET;
		
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = "";
        for (String cif : associatedCifs) {
			if(filter.isEmpty()) 
				filter = "cif" + DBPUtilitiesConstants.EQUAL + cif;
			else
				filter = filter +  DBPUtilitiesConstants.OR +"cif" + DBPUtilitiesConstants.EQUAL + cif;
		}
		if (StringUtils.isNotBlank(legalEntityId)) {
			filter = DBPUtilitiesConstants.OPEN_BRACE + filter + DBPUtilitiesConstants.CLOSE_BRACE
					+ DBPUtilitiesConstants.AND + "legalEntityId" + DBPUtilitiesConstants.EQUAL + legalEntityId;
		}
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);
		String payeeResponse = null;
		List<IntraBankPayeeDTO> intraBankPayeeDTOs = null;
		
		try {
			payeeResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(payeeResponse);
			JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
			intraBankPayeeDTOs = JSONUtils.parseAsList(jsonArray.toString(), IntraBankPayeeDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch intra bank payee Ids: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at fetch intra bank payee Ids: " + e).log();
			return null;
		}
		return intraBankPayeeDTOs;
	}

	@Override
	public JSONObject validateForApprovals( DataControllerRequest request, TransactionStatusDTO payloadForValidateForApprovals ) throws ApplicationException {
		Map<String, Object> payload;
		payload = new HashMap<>();
		payload.put("confirmationNumber", payloadForValidateForApprovals.getConfirmationNumber());
		payload.put("featureActionID", payloadForValidateForApprovals.getFeatureActionID());
		payload.put("status", new JSONObject().put("status", TransactionStatusEnum.NEW.getStatus()).put("message", ""));
		payload.put("additionalMetaInfo", payloadForValidateForApprovals.getAdditionalMetaInfo());
		payload.put("contractCifMap", payloadForValidateForApprovals.getContractCifMap());
		payload.put("customerId", payloadForValidateForApprovals.getCustomerId());
		IntraBankPayeeBackendDelegate intraBankPayeeBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(IntraBankPayeeBackendDelegate.class);
		String jsonObject = intraBankPayeeBackendDelegate.validateForApprovals(request, payload);
		if(StringUtils.isBlank(jsonObject)) {
			throw new ApplicationException(ErrorCodeEnum.ERR_29018);
		}
		JSONObject transactionResponse = new JSONObject(jsonObject);
		if(0 != transactionResponse.getInt("httpStatusCode") || 0 != transactionResponse.getInt("opstatus")) {
			throw new ApplicationException(ErrorCodeEnum.ERR_29018);
		}
		return transactionResponse;
	}

	@Override
	public IntraBankPayeeDTO createPayeeAtDBX(IntraBankPayeeDTO intraBankPayeeDTO) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
        String operationName = OperationName.DB_INTRABANKPAYEE_CREATE;
	        
        Map<String, Object> requestParameters;
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(intraBankPayeeDTO).toString(), String.class, Object.class);
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
					build().getResponse();
			
			JSONObject response = new JSONObject(createResponse);
			JSONArray responseArray = CommonUtils.getFirstOccuringArray(response);
			intraBankPayeeDTO = JSONUtils.parse(responseArray.getJSONObject(0).toString(), IntraBankPayeeDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to create payee at intrabankpayee table: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at createPayeeAtDBX: " + e).log();
			return null;
		}
		
		return intraBankPayeeDTO;
	}
	
	@Override
	public List<IntraBankPayeeDTO> fetchPayeeByIdAtDBX(IntraBankPayeeDTO intraBankPayeeDTO) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
        String operationName = OperationName.DB_INTRABANKPAYEE_GET;
	        
        Map<String, Object> requestParams = new HashMap<String, Object>();
		String filter = "payeeId" + DBPUtilitiesConstants.EQUAL + intraBankPayeeDTO.getPayeeId();
		
		if(intraBankPayeeDTO.getContractId() != null) {
			filter += DBPUtilitiesConstants.AND +"contractId" + DBPUtilitiesConstants.EQUAL + intraBankPayeeDTO.getContractId();
		}
		
		if(intraBankPayeeDTO.getCif() != null) {
			filter += DBPUtilitiesConstants.AND +"cif" + DBPUtilitiesConstants.EQUAL + intraBankPayeeDTO.getCif();
		}
		
		if(StringUtils.isNotBlank(intraBankPayeeDTO.getLegalEntityId())) {
			filter += DBPUtilitiesConstants.AND +"legalEntityId" + DBPUtilitiesConstants.EQUAL + intraBankPayeeDTO.getLegalEntityId();
		}
		
		requestParams.put(DBPUtilitiesConstants.FILTER, filter);
			
		List<IntraBankPayeeDTO> intraBankPayeeDTOs = null;
		String payeeResponse = null;
		try {
			payeeResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					build().getResponse();
			JSONObject responseObj = new JSONObject(payeeResponse);
			JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
			intraBankPayeeDTOs = JSONUtils.parseAsList(jsonArray.toString(), IntraBankPayeeDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch intra bank payee by id: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at fetchPayeeByIdAtDBX: " + e).log();
			return null;
		}
		
		return intraBankPayeeDTOs;
	}

	@Override
	public boolean deletePayeeAtDBX(IntraBankPayeeDTO intraBankPayeeDTO) {
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_INTRABANKPAYEE_DELETE;
	
		Map<String, Object> requestParams = new HashMap<String, Object>();
		String filter = "payeeId" + DBPUtilitiesConstants.EQUAL + intraBankPayeeDTO.getPayeeId();
		
		if(intraBankPayeeDTO.getContractId() != null) {
			filter += DBPUtilitiesConstants.AND +"contractId" + DBPUtilitiesConstants.EQUAL + intraBankPayeeDTO.getContractId();
		}
		
		if(intraBankPayeeDTO.getCif() != null) {
			filter += DBPUtilitiesConstants.AND +"cif" + DBPUtilitiesConstants.EQUAL + intraBankPayeeDTO.getCif();
		}
		
		requestParams.put(DBPUtilitiesConstants.FILTER, filter);
		
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
			alert.prepareError("JSONExcpetion occured while deleting the intra bank payee",jsonExp).log();
			return false;
		}
		catch(Exception e) {
			alert.prepareError("Excpetion occured while deleting the intra bank payee",e).log();
			return false;
		}
		
		return false;	
	}

	@Override
	public boolean checkIfPayeeStatusInPending(DataControllerRequest request, String payeeId ) throws ApplicationException {
		IntraBankPayeeBackendDelegate intraBankPayeeBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(IntraBankPayeeBackendDelegate.class);
		String filterQuery = "transactionId" + DBPUtilitiesConstants.EQUAL + payeeId + DBPUtilitiesConstants.AND + "status" + DBPUtilitiesConstants.EQUAL + "Pending";
		HashMap<String, Object> requestParameters = new HashMap<>();
		requestParameters.put("$filter", filterQuery);
		String jsonString = intraBankPayeeBackendDelegate.checkIfPayeeStatusInPending(request, requestParameters);
		if(jsonString == null) {
			throw new ApplicationException(ErrorCodeEnum.ERR_12612);
		}
		JSONObject obj = new JSONObject(jsonString);
		if(obj.has("bbrequest")) {
			return !obj.getJSONArray("bbrequest").isEmpty();
		}
		return false;
	}
	
}
