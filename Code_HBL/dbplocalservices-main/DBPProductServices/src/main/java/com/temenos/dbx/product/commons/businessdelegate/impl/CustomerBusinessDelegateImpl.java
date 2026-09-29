package com.temenos.dbx.product.commons.businessdelegate.impl;

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
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.temenos.dbx.product.commons.businessdelegate.api.CustomerBusinessDelegate;
import com.temenos.dbx.product.commons.dto.CustomerDTO;
import com.temenos.dbx.product.commons.dto.LimitsDTO;
import com.temenos.dbx.product.commons.dto.UserLimitsDTO;
import com.temenos.dbx.product.commonsutils.CommonUtils;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;

public class CustomerBusinessDelegateImpl implements CustomerBusinessDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	@Override
	public String getUserContractCustomerRole(String contractId, String coreCustomerId, String customerId) {
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		
		String serviceId = ServiceId.DBPRBLOCALSERVICEDB;
		String operationId = OperationName.DB_CUSTOMERGROUP_GET;
		
		String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + customerId + DBPUtilitiesConstants.AND +
				"contractId" + DBPUtilitiesConstants.EQUAL + contractId + DBPUtilitiesConstants.AND +
				"coreCustomerId" + DBPUtilitiesConstants.EQUAL + coreCustomerId;
		
		
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);
		
		try {
			String userRoleResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceId).
					withObjectId(null).
					withOperationId(operationId).
					withRequestParameters(requestParameters).
					build().getResponse();
			
			JSONObject userRoleResponseJSON = new JSONObject(userRoleResponse);
			JSONArray customerGroupArray = CommonUtils.getFirstOccuringArray(userRoleResponseJSON);
			
			if(customerGroupArray != null && customerGroupArray.length() > 0) {
				JSONObject customerGroup = (JSONObject) customerGroupArray.get(0);
				String roleId = customerGroup.optString("Group_id");
				return roleId;
			}
			
		} catch (Exception e) {
			alert.prepareError("Exception caught while fetching user role", e).log();
			return null;
		}
		return null;
	}
	
	@Override
	public UserLimitsDTO fetchCustomerLimits(String customerId, String featureActionID, String accountId) {
		
		UserLimitsDTO userLimitsDTO = null;
		String serviceId = ServiceId.DBP_PRODUCT_SERVICES;
		String operationId = OperationName.GET_CUSTOMER_ACCOUNT_ACTION_LIMITS;
		Map<String, Object> requestParams = new HashMap<String, Object>();
		
		requestParams.put("customerId", customerId);
		requestParams.put("action", featureActionID);
		requestParams.put("accountId", accountId);
		
		try {
			String response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceId).
					withObjectId(null).
					withOperationId(operationId).
					withRequestParameters(requestParams).
					build().getResponse();

			if(response == null) {
				userLimitsDTO = null;
			}
			JSONObject responseObj = new JSONObject(response).getJSONObject("limits");
			userLimitsDTO = JSONUtils.parse(responseObj.toString(), UserLimitsDTO.class);
			
		} catch (Exception e) {
			alert.prepareError("Exception caught while fetching user limits", e).log();
			return null;
		}
		return userLimitsDTO;
	}
	
	@Override
	public LimitsDTO fetchExhaustedLimits(String customerId, String featureActionID, String date) {
		LimitsDTO limitsDTO = null;
		Map<String, Object> requestParameters = new HashMap<String, Object>();

		String serviceId = ServiceId.TRANSACTIONSLIMIT;
		String operationId = OperationName.GET_TRANSACTIONS_AMOUNT;
		
		requestParameters.put("featureactionid", featureActionID);
		requestParameters.put("customerid", customerId);
		requestParameters.put("date", date);
		
		try {
			String response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceId).
					withObjectId(null).
					withOperationId(operationId).
					withRequestParameters(requestParameters).
					build().getResponse();
			
			if(response == null) {
				return null;
			}
			limitsDTO = JSONUtils.parse(response, LimitsDTO.class);
			
			if(limitsDTO.getDbpErrCode() == null 
					&& (limitsDTO.getDailyLimit() == null || limitsDTO.getWeeklyLimit() == null)) {
				return null;
			}
			
		} catch (Exception e) {
			alert.prepareError("Exception caught while fetching customer limits", e).log();
			return null;
		}
		return limitsDTO;
	}

	@Override
	public LimitsDTO fetchExhaustedLimits(String customerId, String featureActionID, String date, String accountId) {
		LimitsDTO limitsDTO = null;
		Map<String, Object> requestParameters = new HashMap<String, Object>();

		String serviceId = ServiceId.TRANSACTIONSLIMIT;
		String operationId = OperationName.GET_TRANSACTIONS_AMOUNT;
		
		requestParameters.put("featureactionid", featureActionID);
		requestParameters.put("customerid", customerId);
		requestParameters.put("accountid", accountId);
		requestParameters.put("date", date);
		
		try {
			String response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceId).
					withObjectId(null).
					withOperationId(operationId).
					withRequestParameters(requestParameters).
					build().getResponse();
			
			if(response == null) {
				return null;
			}
			limitsDTO = JSONUtils.parse(response, LimitsDTO.class);
			
			if(limitsDTO.getDbpErrCode() == null 
					&& (limitsDTO.getDailyLimit() == null || limitsDTO.getWeeklyLimit() == null)) {
				return null;
			}
			
		} catch (Exception e) {
			alert.prepareError("Exception caught while fetching customer limits", e).log();
			return null;
		}
		return limitsDTO;
	}
	
	@Override
	public JSONObject getUserDetails(String customerId) {
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		
		String response = null;
		String serviceId = ServiceId.DBPRBLOCALSERVICEDB;
		String operationId = OperationName.DB_CUSTOMER_GET;
		
		String filter = "id" + DBPUtilitiesConstants.EQUAL + customerId;
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);
		 try {
			  response = DBPServiceExecutorBuilder.builder().
						withServiceId(serviceId).
						withObjectId(null).
						withOperationId(operationId).
						withRequestParameters(requestParameters).
						build().getResponse();
	        } catch (Exception e) {
	            alert.prepareError("Error while invoking DB service - " + operationId + "  : " + e).log();
	            return null;
	        }
		 JSONObject jsonResponse = new JSONObject(response);
		 JSONArray custDetails = jsonResponse.getJSONArray(Constants.CUSTOMER);
		 return custDetails.getJSONObject(0);  
	}
	
	@Override
	public List<String> getCombinedUserIds(String customerId) {
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		List<String> combinedIds = new ArrayList<String>();
		JSONArray customers = null;
		
		String response = null;
		String serviceId = ServiceId.DBPRBLOCALSERVICEDB;
		String operationId = OperationName.DB_CUSTOMER_GET;
		
		String filter = "combinedUserId" + DBPUtilitiesConstants.EQUAL + customerId;
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceId).
					withObjectId(null).
					withOperationId(operationId).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject jsonResponse = new JSONObject(response);
			customers = jsonResponse.getJSONArray(Constants.CUSTOMER);
		} catch (Exception e) {
			alert.prepareError("Error while invoking DB service - " + operationId + "  : " + e).log();
			return combinedIds;
		}

		if(customers != null && customers.length() > 0) {
			for(Object customer : customers) {
				JSONObject cust = (JSONObject) customer;
				combinedIds.add(cust.optString("id"));
			}
		}
		 
		return combinedIds;  
	}
    
	@Override
    public List<CustomerDTO> getCustomerInfo(Set<String> customerIds) {
        String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
        String operationName = OperationName.DB_CUSTOMER_GET;
        
        Map<String, Object> requestParameters = new HashMap<String, Object>();

        String filter = "id" + DBPUtilitiesConstants.EQUAL + 
                String.join(DBPUtilitiesConstants.OR + "id" + DBPUtilitiesConstants.EQUAL, customerIds);
        requestParameters.put(DBPUtilitiesConstants.FILTER, filter);

        List<CustomerDTO> CustomerRecords = null;
        String response = null;
        try {
            response = DBPServiceExecutorBuilder.builder()
                    .withServiceId(serviceName)
                    .withObjectId(null)
                    .withOperationId(operationName)
                    .withRequestParameters(requestParameters)
                    .build().getResponse();
            JSONObject responseObj = new JSONObject(response);
            JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
            CustomerRecords = JSONUtils.parseAsList(jsonArray.toString(), CustomerDTO.class);
            
        } catch (JSONException je) {
            alert.prepareError("Failed to fetch customer record ", je).log();
            return null;
        } catch (Exception e) {
            alert.prepareError("Failed to fetch customer record: ", e).log();
            return null;
        }

        return CustomerRecords;
    }
	
}

