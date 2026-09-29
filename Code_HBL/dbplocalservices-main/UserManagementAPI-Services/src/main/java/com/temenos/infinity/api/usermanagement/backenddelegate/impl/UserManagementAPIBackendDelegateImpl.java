package com.temenos.infinity.api.usermanagement.backenddelegate.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.constants.DBPConstants;
import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.api.usermanagement.backenddelegate.api.UserManagementAPIBackendDelegate;
import com.temenos.infinity.api.usermanagement.config.UserManagementAPIServices;
import com.temenos.infinity.api.usermanagement.constants.ErrorCodeEnum;
import com.temenos.infinity.api.usermanagement.constants.UserAccountSettingConstants;
import com.temenos.infinity.api.usermanagement.dto.CustomerDetailsDTO;
import com.temenos.infinity.api.usermanagement.dto.UserAccountSettingsDTO;
import com.temenos.infinity.api.usermanagement.utils.UserManagementUtils;

public class UserManagementAPIBackendDelegateImpl implements UserManagementAPIBackendDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public UserAccountSettingsDTO updateUserAccountSettings(UserAccountSettingsDTO userAccount,
			HashMap<String, Object> headerMap) throws ApplicationException {

		// Load Check Book Request properties
		Properties props = UserManagementUtils.loadProps(UserAccountSettingConstants.PARAM_PROPERTY);

		JSONObject requestBody = new JSONObject();
		requestBody.put("accountID", userAccount.getAccountID());
		requestBody.put("nickName", userAccount.getNickName());
		requestBody.put("favouriteStatus", userAccount.getFavouriteStatus());
		requestBody.put("eStatementEnable", userAccount.geteStatementEnable());
		requestBody.put("email", userAccount.getEmail());
		String escapedReqBody =  requestBody.toString().replace("'","\\'");
		escapedReqBody = escapedReqBody.replace("\"", "'");
		// Set Input Parameters for create Order service
		Map<String, Object> inputMap = new HashMap<>();
		inputMap.put("type", props.getProperty("accountType"));
		inputMap.put("subtype", props.getProperty("accountSubType"));
		inputMap.put("accountId", userAccount.getAccountID());
		inputMap.put("requestBody", escapedReqBody);
		

		// Making a call to order request API
		String updateAccountResponse = null;
		JSONObject Response = new JSONObject();
		try {
			updateAccountResponse = DBPServiceExecutorBuilder.builder()
					.withServiceId(UserManagementAPIServices.SERVICEREQUESTJAVA_CREATEORDER.getServiceName())
					.withOperationId(UserManagementAPIServices.SERVICEREQUESTJAVA_CREATEORDER.getOperationName())
					.withRequestParameters(inputMap).withRequestHeaders(headerMap)
					.withFabricAuthToken(headerMap.get("X-Kony-Authorization").toString()).build().getResponse();

		} catch (Exception e) {
			alert.prepareError("Unable to create user account settings order " + e).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20053);
		}

		if (StringUtils.isNotBlank(updateAccountResponse)) {
			alert.prepareError("OMS Response " + updateAccountResponse).log();
			Response = new JSONObject(updateAccountResponse);
		}

		if (Response.has(UserAccountSettingConstants.PARAM_ORDER_ID)
				&& StringUtils.isNotBlank(Response.getString(UserAccountSettingConstants.PARAM_ORDER_ID))) {
			userAccount.setId(Response.getString(UserAccountSettingConstants.PARAM_ORDER_ID));
			userAccount.setStatus(Response.getString(UserAccountSettingConstants.PARAM_ORDER_STATUS));

			if (Response.has(UserAccountSettingConstants.PARAM_ERROR_MESSAGE)) {
				if (StringUtils.isNotBlank(Response.getString(UserAccountSettingConstants.PARAM_ERROR_MESSAGE))) {
					userAccount
							.setErrorMessage(Response.getString(UserAccountSettingConstants.PARAM_ERROR_MESSAGE));
				}
			}
		}
		return userAccount;
	}

	@Override
	public CustomerDetailsDTO updateCustomerDetails(CustomerDetailsDTO customerDetailsDTO,
			HashMap<String, Object> headerMap) throws ApplicationException {

		// Load Check Book Request properties
		Properties props = UserManagementUtils.loadProps(UserAccountSettingConstants.PARAM_PROPERTY);
		
		CustomerDetailsDTO responseDTO = new CustomerDetailsDTO();
		
		Map<String, Object> inputMap = new HashMap<>();

		JSONObject requestBody = new JSONObject();
		if (customerDetailsDTO.getOperation().contentEquals("DeleteAddress")) {
			requestBody.put("userName", customerDetailsDTO.getUserName());
			requestBody.put("deleteAddressID", customerDetailsDTO.getDeleteAddressID());
			requestBody.put("operation", customerDetailsDTO.getOperation());
			requestBody.put("source", UserAccountSettingConstants.PARAM_SOURCE);
			
			inputMap.put("type", props.getProperty("addressType"));
			inputMap.put("subtype", props.getProperty("addressDeletionSubType"));

		} else if (customerDetailsDTO.getOperation().contentEquals("Delete")) {
			requestBody.put("userName", customerDetailsDTO.getUserName());
			requestBody.put("deleteCommunicationID", customerDetailsDTO.getDeleteCommunicationID());
			requestBody.put("operation", customerDetailsDTO.getOperation());
			requestBody.put("source", UserAccountSettingConstants.PARAM_SOURCE);
			
			if (customerDetailsDTO.getDetailToBeUpdated().contentEquals("phoneNumbers")) {
			inputMap.put("type", props.getProperty("phoneType"));
			inputMap.put("subtype", props.getProperty("phoneDeletionSubType"));
			}
			else if (customerDetailsDTO.getDetailToBeUpdated().contentEquals("EmailIds")) {
				inputMap.put("type", props.getProperty("emailType"));
				inputMap.put("subtype", props.getProperty("emailDeletionSubType"));
			}
				
			
		} else if (customerDetailsDTO.getOperation().contentEquals("Create")) {

			requestBody.put("isPrimary", customerDetailsDTO.getIsPrimary());
			if (StringUtils.isNotBlank(customerDetailsDTO.getIsAlertsRequired())) {
				requestBody.put("isAlertsRequired", customerDetailsDTO.getIsAlertsRequired());
			}
			requestBody.put("Extension", customerDetailsDTO.getExtension());
			requestBody.put("userName", customerDetailsDTO.getUserName());
			requestBody.put("modifiedByName", customerDetailsDTO.getModifiedByName());
			requestBody.put("operation", customerDetailsDTO.getOperation());
			requestBody.put("source", UserAccountSettingConstants.PARAM_SOURCE);

			if (customerDetailsDTO.getDetailToBeUpdated().contentEquals("phoneNumbers")) {
				requestBody.put("phoneNumber", customerDetailsDTO.getPhoneNumber());
				requestBody.put("phoneCountryCode", customerDetailsDTO.getPhoneCountryCode());
				requestBody.put("detailToBeUpdated", customerDetailsDTO.getDetailToBeUpdated());
				
				inputMap.put("type", props.getProperty("phoneType"));
				inputMap.put("subtype", props.getProperty("phoneCreationSubType"));
			}

			if (customerDetailsDTO.getDetailToBeUpdated().contentEquals("EmailIds")) {
				requestBody.put("value", customerDetailsDTO.getValue());
				requestBody.put("detailToBeUpdated", customerDetailsDTO.getDetailToBeUpdated());
				
				inputMap.put("type", props.getProperty("emailType"));
				inputMap.put("subtype", props.getProperty("emailCreationSubType"));
			}

		} else if (customerDetailsDTO.getOperation().contentEquals("Update")) {
			requestBody.put("communication_ID", customerDetailsDTO.getId());
			requestBody.put("isTypeBusiness", customerDetailsDTO.getIsTypeBusiness());
			requestBody.put("isPrimary", customerDetailsDTO.getIsPrimary());
			if (StringUtils.isNotBlank(customerDetailsDTO.getIsAlertsRequired())) {
				requestBody.put("isAlertsRequired", customerDetailsDTO.getIsAlertsRequired());
			}			
			requestBody.put("userName", customerDetailsDTO.getUserName());
			requestBody.put("modifiedByName", customerDetailsDTO.getModifiedByName());
			requestBody.put("operation", customerDetailsDTO.getOperation());
			requestBody.put("source", UserAccountSettingConstants.PARAM_SOURCE);

			if (customerDetailsDTO.getDetailToBeUpdated().contentEquals("phoneNumbers")) {
				requestBody.put("phoneNumber", customerDetailsDTO.getPhoneNumber());
				requestBody.put("phoneCountryCode", customerDetailsDTO.getPhoneCountryCode());
				requestBody.put("Extension", customerDetailsDTO.getExtension());
				requestBody.put("detailToBeUpdated", customerDetailsDTO.getDetailToBeUpdated());
				
				inputMap.put("type", props.getProperty("phoneType"));
				inputMap.put("subtype", props.getProperty("phoneUpdationSubType"));
			}

			if (customerDetailsDTO.getDetailToBeUpdated().contentEquals("EmailIds")) {
				requestBody.put("value", customerDetailsDTO.getValue());
				if (StringUtils.isNotBlank(customerDetailsDTO.getExtension())) {
					requestBody.put("Extension", customerDetailsDTO.getExtension());
				}
				requestBody.put("detailToBeUpdated", customerDetailsDTO.getDetailToBeUpdated());
				
				inputMap.put("type", props.getProperty("emailType"));
				inputMap.put("subtype", props.getProperty("emailUpdationSubType"));

			}
		} else if (customerDetailsDTO.getDetailToBeUpdated().contentEquals("Addresses")) {

			requestBody.put("Addr_type", customerDetailsDTO.getAddr_type());
			requestBody.put("addrLine1", customerDetailsDTO.getAddrLine1());
			requestBody.put("addrLine2", customerDetailsDTO.getAddrLine2());
			requestBody.put("City_id", customerDetailsDTO.getCity_id());
			requestBody.put("ZipCode", customerDetailsDTO.getZipCode());
			if (StringUtils.isNotBlank(customerDetailsDTO.getRegion_id())) {
				requestBody.put("Region_id", customerDetailsDTO.getRegion_id());
			}			
			requestBody.put("countryCode", customerDetailsDTO.getCountryCode());
			requestBody.put("isPrimary", customerDetailsDTO.getIsPrimary());
			requestBody.put("userName", customerDetailsDTO.getUserName());
			requestBody.put("modifiedByName", customerDetailsDTO.getModifiedByName());
			
			inputMap.put("type", props.getProperty("addressType"));
			

			if (customerDetailsDTO.getOperation().contentEquals("UpdateAddress")) {
				requestBody.put("Addr_id", customerDetailsDTO.getAddr_id());
				inputMap.put("subtype", props.getProperty("addressUpdationSubType"));
			}			
			else if (customerDetailsDTO.getOperation().contentEquals("CreateAddress")) {
				inputMap.put("subtype", props.getProperty("addressCreationSubType"));
			}
			requestBody.put("operation", customerDetailsDTO.getOperation());
			requestBody.put("source", UserAccountSettingConstants.PARAM_SOURCE);
			requestBody.put("detailToBeUpdated", customerDetailsDTO.getDetailToBeUpdated());
		}

		else if (customerDetailsDTO.getDetailToBeUpdated().contentEquals("UpdatePrimaryAddress")) {
			requestBody.put("customerName", customerDetailsDTO.getCustomerName());
			requestBody.put("customerId", customerDetailsDTO.getCustomerId());
			requestBody.put("addressId", customerDetailsDTO.getAddressId());
			requestBody.put("primaryFlag", customerDetailsDTO.getPrimaryFlag());
			requestBody.put("requestDetails", customerDetailsDTO.getRequestDetails());
			requestBody.put("addressType", customerDetailsDTO.getAddressType());
			requestBody.put("supportingDocumentData", customerDetailsDTO.getSupportingDocumentIds());
			inputMap.put("type", props.getProperty("addressType"));
			inputMap.put("subtype", "UpdatePrimaryAddress");
			/*
			 * if (customerDetailsDTO.getOperation().contentEquals("UpdatePrimaryAddress"))
			 * { requestBody.put("Addr_id", customerDetailsDTO.getAddr_id());
			 * inputMap.put("subtype", "UpdatePrimaryAddress"); } else if
			 * (customerDetailsDTO.getOperation().contentEquals("CreateAddress")) {
			 * inputMap.put("subtype", props.getProperty("addressCreationSubType")); }
			 */
			/*
			 * requestBody.put("operation", customerDetailsDTO.getOperation());
			 * requestBody.put("source", UserAccountSettingConstants.PARAM_SOURCE);
			 * requestBody.put("detailToBeUpdated",
			 * customerDetailsDTO.getDetailToBeUpdated());
			 */
		}
		
		else if (customerDetailsDTO.getDetailToBeUpdated().contentEquals("PrimaryAddress")) {

			requestBody.put("Addr_type", customerDetailsDTO.getAddr_type());
			requestBody.put("addrLine1", customerDetailsDTO.getAddrLine1());
			requestBody.put("addrLine2", customerDetailsDTO.getAddrLine2());
			requestBody.put("City_id", customerDetailsDTO.getCity_id());
			requestBody.put("ZipCode", customerDetailsDTO.getZipCode());
			if (StringUtils.isNotBlank(customerDetailsDTO.getRegion_id())) {
				requestBody.put("Region_id", customerDetailsDTO.getRegion_id());
			}			
			requestBody.put("countryCode", customerDetailsDTO.getCountryCode());
			requestBody.put("isPrimary", customerDetailsDTO.getIsPrimary());
			requestBody.put("userName", customerDetailsDTO.getUserName());
			requestBody.put("modifiedByName", customerDetailsDTO.getModifiedByName());
			inputMap.put("type", props.getProperty("addressType"));
			

			if (customerDetailsDTO.getOperation().contentEquals("UpdateAddress")) {
				requestBody.put("Addr_id", customerDetailsDTO.getAddr_id());
				inputMap.put("subtype", props.getProperty("addressUpdationSubType"));
			}			
			else if (customerDetailsDTO.getOperation().contentEquals("CreateAddress")) {
				inputMap.put("subtype", props.getProperty("addressCreationSubType"));
			}
			requestBody.put("operation", customerDetailsDTO.getOperation());
			requestBody.put("source", UserAccountSettingConstants.PARAM_SOURCE);
			requestBody.put("detailToBeUpdated", customerDetailsDTO.getDetailToBeUpdated());
		}
        if (StringUtils.equals(customerDetailsDTO.getDetailToBeUpdated(), "UpdatePrimaryAddress")) {
        	String formatRequestBody =  requestBody.toString().replace("'","\\'");
        	formatRequestBody = formatRequestBody.replace("\"", "'");
        	inputMap.put("requestBody", formatRequestBody);
		}
        else {
        	inputMap.put("requestBody",requestBody.toString());
        }

    	inputMap.put("requestBody",requestBody.toString());

		// Making a call to order request API
		String updateCustomerDetailsResponse = null;
		JSONObject Response = new JSONObject();

		String integration = EnvironmentConfigurationsHandler.getServerProperty("PROFILESETTINGS_BACKEND");

		if ("srms".equalsIgnoreCase(integration)) {

			try {
				updateCustomerDetailsResponse = DBPServiceExecutorBuilder.builder()
						.withServiceId(UserManagementAPIServices.SERVICEREQUESTJAVA_CREATEORDER.getServiceName())
						.withOperationId(UserManagementAPIServices.SERVICEREQUESTJAVA_CREATEORDER.getOperationName())
						.withRequestParameters(inputMap).withRequestHeaders(headerMap)
						.withFabricAuthToken(headerMap.get("X-Kony-Authorization").toString()).build().getResponse();

			} catch (Exception e) {
				alert.prepareError("Unable to update customer details order " + e).log();
				throw new ApplicationException(ErrorCodeEnum.ERR_20055);
			}

			if (StringUtils.isNotBlank(updateCustomerDetailsResponse)) {
				alert.prepareError("OMS Response " + updateCustomerDetailsResponse).log();
				Response = new JSONObject(updateCustomerDetailsResponse);
			}
			if (Response.has(UserAccountSettingConstants.PARAM_ORDER_ID)
					&& StringUtils.isNotBlank(Response.getString(UserAccountSettingConstants.PARAM_ORDER_ID))) {

				responseDTO.setOrderId(Response.getString(UserAccountSettingConstants.PARAM_ORDER_ID));
				responseDTO.setStatus(Response.getString(UserAccountSettingConstants.PARAM_ORDER_STATUS));

				if (Response.has(UserAccountSettingConstants.PARAM_ERROR_MESSAGE)) {
					if (StringUtils.isNotBlank(Response.getString(UserAccountSettingConstants.PARAM_ERROR_MESSAGE))) {
						responseDTO
								.setErrorMessage(Response.getString(UserAccountSettingConstants.PARAM_ERROR_MESSAGE));
					}
				}
			}
		} else {
			Map<String, Object> requestBodyMap = null;
			try {
				requestBody.put("legalEntityId", customerDetailsDTO.getLegalEntityId());
				requestBodyMap = new ObjectMapper().readValue(requestBody.toString(),
						new TypeReference<Map<String, Object>>() {
						});
			} catch (Exception e) {
				alert.prepareError("Error while parsing requestBody " + e).log();
			}
			try {
				updateCustomerDetailsResponse = DBPServiceExecutorBuilder.builder().withServiceId("eumProductServices")
						.withOperationId("UpdateCustomerDetailsOperation").withRequestParameters(requestBodyMap)
						.withRequestHeaders(headerMap)
						.withFabricAuthToken(headerMap.get("X-Kony-Authorization").toString()).build().getResponse();

			} catch (DBPApplicationException e) {
				alert.prepareError("Unable to update customer details order " + e).log();
			}

			if (StringUtils.isNotBlank(updateCustomerDetailsResponse)) {
				Response = new JSONObject(updateCustomerDetailsResponse);
			}

			if (Response.has(DBPConstants.DBP_ERROR_CODE_KEY)) {
				if (StringUtils.isNotBlank(Response.getString(DBPConstants.DBP_ERROR_MESSAGE_KEY))) {
					responseDTO.setErrorMessage(Response.getString(DBPConstants.DBP_ERROR_MESSAGE_KEY));
				}
			} else {
				responseDTO.setOrderId(HelperMethods.getNewId());
				responseDTO.setStatus(Integer.toString(Response.getInt(DBPConstants.FABRIC_OPSTATUS_KEY)));
			}

		}

		return responseDTO;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	    public Set<String> getUserEmailIds(String username,String customerId,String bankId, Map<String, Object> headerMap) throws JSONException, UnsupportedEncodingException {
		String response = null;
		JSONObject userResult = new JSONObject();
		JSONObject result = new JSONObject();
		JSONArray user = new JSONArray();
		
	        String deviceId1 = getDeviceId(headerMap);

	        Map<String, Object> inputParams = new HashMap<>();
	        inputParams.put("Username", username);
	        inputParams.put("Customer_id", customerId);
	        inputParams.put("Bank_id", bankId);
	        inputParams.put("deviceId", deviceId1);

	        Set<String> userEmailIDs = new HashSet<String>();
	        try {
	            response =  DBPServiceExecutorBuilder.builder().withObjectId("ExternalUsers").withServiceId("ExternalUserManagement")
						.withOperationId("get").withRequestParameters(inputParams).withRequestHeaders(headerMap)
						.withFabricAuthToken(headerMap.get("X-Kony-Authorization").toString()).build().getResponse();			
	        } catch (Exception e) {
	        	alert.prepareError("Unable to create user account settings order " + e).log(); 
	        }
	        if (StringUtils.isNotBlank(response)) {
    			alert.prepareError("OMS Response " + response).log();
    			result = new JSONObject(response);
    			user = result.getJSONArray("User");
    			userResult = (JSONObject) user.get(0);
    		}
            
            if (userResult.has("EmailIds")) {
              JSONArray emailIDsArr = (JSONArray) userResult.get("EmailIds");
               JSONObject emailIDJson = null;
               String emailID = null;
               for (int i=0;i<emailIDsArr.length();i++) {
            	   emailIDJson = emailIDsArr.getJSONObject(i);
            	   emailID = emailIDJson.get("Value").toString();
            	   userEmailIDs.add(emailID);
               }
            }

	        return userEmailIDs;
	    }
	
	public JSONArray getUserEmailDetails(String username,String customerId,String bankId, Map<String, Object> headerMap) throws JSONException, UnsupportedEncodingException {
		String response = null;
		JSONObject userResult = new JSONObject();
		JSONObject result = new JSONObject();
		JSONArray user = new JSONArray();
		
        String deviceId1 = getDeviceId(headerMap);

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("Username", username);
        inputParams.put("Customer_id", customerId);
        inputParams.put("Bank_id", bankId);
        inputParams.put("deviceId", deviceId1);

        JSONArray userEmailDetails = new JSONArray();
        try {
            response =  DBPServiceExecutorBuilder.builder().withObjectId("ExternalUsers").withServiceId("ExternalUserManagement")
					.withOperationId("get").withRequestParameters(inputParams).withRequestHeaders(headerMap)
					.withFabricAuthToken(headerMap.get("X-Kony-Authorization").toString()).build().getResponse();			
        } catch (Exception e) {
        	alert.prepareError("Unable to get external user details " + e).log(); 
        }
        if (StringUtils.isNotBlank(response)) {
			alert.prepareError("OMS Response " + response).log();
			result = new JSONObject(response);
			if(result.has("User"))
				user = result.getJSONArray("User");
			else if(result.has("ExternalUsers"))
				user = result.getJSONArray("ExternalUsers");
			else
				return null;
			userResult = (JSONObject) user.get(0);
		}
        
        if (userResult.has("EmailIds")) {
        	userEmailDetails = (JSONArray) userResult.get("EmailIds");
        }

        return userEmailDetails;
	 }
	 	  
	 private String getDeviceId(Map<String, Object> headers) throws JSONException, UnsupportedEncodingException {
	        String deviceId = null;
	        if (headers != null && headers.containsKey("X-Kony-ReportingParams")) {
	            String reportingParams = (String) headers.get("X-Kony-ReportingParams");
	            if (StringUtils.isNotBlank(reportingParams)) {
	                JSONObject reportingParamsJson = null;
	                    reportingParamsJson = 
	                            new JSONObject(URLDecoder.decode(reportingParams, StandardCharsets.UTF_8.name()));
	                if (null != reportingParamsJson) {
	                    deviceId = reportingParamsJson.optString("did");
	                }
	            }
	        }

	        return deviceId;
	    }
	 @Override
		public String uploadDocsForChangeRequest(HashMap<String, Object> dataMap,HashMap<String, Object> headerMap) throws ApplicationException {
			String result = null;
			try {
				result = DBPServiceExecutorBuilder.builder().withServiceId("Document").withObjectId("DocumentChecklist")
						.withOperationId("uploadMultipleDocuments").withRequestParameters(dataMap)
						.withRequestHeaders(headerMap).build().getResponse();
			} catch (Exception e) {
				alert.prepareError("Error in UserMAnagementAPIBackendDelegateImpl : uploadMultipleDocuments"+ e.getMessage()).log();

			}
			
			return result;
			
		}
}