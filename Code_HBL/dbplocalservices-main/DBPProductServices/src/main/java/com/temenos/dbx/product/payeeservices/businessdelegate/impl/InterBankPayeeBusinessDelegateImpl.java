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
import com.temenos.dbx.product.payeeservices.businessdelegate.api.InterBankPayeeBusinessDelegate;
import com.temenos.dbx.product.payeeservices.backenddelegate.api.InterBankPayeeBackendDelegate;
import com.temenos.dbx.product.payeeservices.dto.InterBankPayeeDTO;

public class InterBankPayeeBusinessDelegateImpl implements InterBankPayeeBusinessDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    
    @Override
    public List<InterBankPayeeDTO> fetchPayeesFromDBX(Set<String> associatedCifs,String legalEntityId) {
        String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
        String operationName = OperationName.DB_INTERBANKPAYEE_GET;
        Map<String, Object> requestParameters = new HashMap<>();
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

        List<InterBankPayeeDTO> interBankPayPayeeDTOsList = null;
        String payeeResponse = null;
        try {
            payeeResponse = DBPServiceExecutorBuilder.builder().
                    withServiceId(serviceName).
                    withObjectId(null).
                    withOperationId(operationName).
                    withRequestParameters(requestParameters).
                    build().getResponse();
            JSONObject responseObj = new JSONObject(payeeResponse);
            JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
            interBankPayPayeeDTOsList = JSONUtils.parseAsList(jsonArray.toString(), InterBankPayeeDTO.class);
        }
        catch (JSONException e) {
            alert.prepareError("Failed to fetch interbank payee Ids: " + e).log();
            return null;
        }
        catch (Exception e) {
            alert.prepareError("Caught exception at fetch interbank payee Ids: " + e).log();
            return null;
        }

        return interBankPayPayeeDTOsList;
    }

    @Override
    public InterBankPayeeDTO createPayeeAtDBX(InterBankPayeeDTO interBankPayeeDTO) {

        String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
        String operationName = OperationName.DB_INTERBANKPAYEE_CREATE;

        Map<String, Object> requestParameters;
        try {
            requestParameters = JSONUtils.parseAsMap(new JSONObject(interBankPayeeDTO).toString(), String.class, Object.class);
        } catch (IOException e) {
            alert.prepareError("Error occurred while fetching the request params: " + e).log();
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
            interBankPayeeDTO = JSONUtils.parse(responseArray.getJSONObject(0).toString(), InterBankPayeeDTO.class);
        }
        catch (JSONException e) {
            alert.prepareError("Failed to create payee at interbankpayee table: " + e).log();
            return null;
        }
        catch (Exception e) {
            alert.prepareError("Caught exception at createPayeeAtDBX: " + e).log();
            return null;
        }

        return interBankPayeeDTO;
    }

    @Override
    public boolean deletePayeeAtDBX(InterBankPayeeDTO interBankPayeeDTO) {

        String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
        String operationName = OperationName.DB_INTERBANKPAYEE_DELETE;

        Map<String, Object> requestParams = new HashMap<String, Object>();
		String filter = "payeeId" + DBPUtilitiesConstants.EQUAL + interBankPayeeDTO.getPayeeId();
		
		if(interBankPayeeDTO.getContractId() != null) {
			filter += DBPUtilitiesConstants.AND +"contractId" + DBPUtilitiesConstants.EQUAL + interBankPayeeDTO.getContractId();
		}
		
		if(interBankPayeeDTO.getCif() != null) {
			filter += DBPUtilitiesConstants.AND +"cif" + DBPUtilitiesConstants.EQUAL + interBankPayeeDTO.getCif();
		}
		
		requestParams.put(DBPUtilitiesConstants.FILTER, filter);

        String deleteResponse = null;
        try {
            deleteResponse = DBPServiceExecutorBuilder.builder().
                    withServiceId(serviceName).
                    withObjectId(null).
                    withOperationId(operationName).
                    withRequestParameters(requestParams).
                    build().getResponse();

            JSONObject response = new JSONObject(deleteResponse);
            if(response.getInt("opstatus") == 0 && response.getInt("httpStatusCode") == 0 && response.getInt("deletedRecords") == 1) {
                return true;
            }
        }
        catch (JSONException e) {
            alert.prepareError("Failed to delete payee at interbankpayee table: " + e).log();
            return false;
        }
        catch (Exception e) {
            alert.prepareError("Caught exception at deletePayeeAtDBX: " + e).log();
            return false;
        }

        return false;
    }
    
    @Override
    public List<InterBankPayeeDTO> fetchPayeeByIdAtDBX(InterBankPayeeDTO interBankPayeeDTO) {
        String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
        String operationName = OperationName.DB_INTERBANKPAYEE_GET;

        Map<String, Object> requestParams = new HashMap<String, Object>();
		String filter = "payeeId" + DBPUtilitiesConstants.EQUAL + interBankPayeeDTO.getPayeeId();
		
		if(interBankPayeeDTO.getContractId() != null) {
			filter += DBPUtilitiesConstants.AND +"contractId" + DBPUtilitiesConstants.EQUAL + interBankPayeeDTO.getContractId();
		}
		
		if(interBankPayeeDTO.getCif() != null) {
			filter += DBPUtilitiesConstants.AND +"cif" + DBPUtilitiesConstants.EQUAL + interBankPayeeDTO.getCif();
		}
		
		if(StringUtils.isNotBlank(interBankPayeeDTO.getLegalEntityId())) {
			filter += DBPUtilitiesConstants.AND +"legalEntityId" + DBPUtilitiesConstants.EQUAL + interBankPayeeDTO.getLegalEntityId();
		}
		requestParams.put(DBPUtilitiesConstants.FILTER, filter);
        List<InterBankPayeeDTO> interBankPayeeDTOs = null;
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
			interBankPayeeDTOs = JSONUtils.parseAsList(jsonArray.toString(), InterBankPayeeDTO.class);
        }
        catch (JSONException e) {
            alert.prepareError("Failed to fetch intra bank payee by id: " + e).log();
            return null;
        }
        catch (Exception e) {
            alert.prepareError("Caught exception at fetchPayeeByIdAtDBX: " + e).log();
            return null;
        }

        return interBankPayeeDTOs;
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
        InterBankPayeeBackendDelegate interBankPayeeBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(InterBankPayeeBackendDelegate.class);
        String jsonObject = interBankPayeeBackendDelegate.validateForApprovals(request, payload);
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
    public boolean checkIfPayeeStatusInPending(DataControllerRequest request, String payeeId ) throws ApplicationException {
        InterBankPayeeBackendDelegate interBankPayeeBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(InterBankPayeeBackendDelegate.class);
        String filterQuery = "transactionId" + DBPUtilitiesConstants.EQUAL + payeeId + DBPUtilitiesConstants.AND + "status" + DBPUtilitiesConstants.EQUAL + "Pending";
        HashMap<String, Object> requestParameters = new HashMap<>();
        requestParameters.put("$filter", filterQuery);
        String jsonString = interBankPayeeBackendDelegate.checkIfPayeeStatusInPending(request, requestParameters);
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
