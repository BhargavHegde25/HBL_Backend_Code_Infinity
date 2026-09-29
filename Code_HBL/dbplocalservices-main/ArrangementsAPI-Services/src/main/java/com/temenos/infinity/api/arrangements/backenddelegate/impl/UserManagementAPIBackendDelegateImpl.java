package com.temenos.infinity.api.arrangements.backenddelegate.impl;

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

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.HelperMethods;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.api.arrangements.backenddelegate.api.UserManagementAPIBackendDelegate;
import com.temenos.infinity.api.arrangements.config.ArrangementsAPIServices;
import com.temenos.infinity.api.arrangements.config.UserManagementAPIServices;
import com.temenos.infinity.api.arrangements.constants.ErrorCodeEnum;
import com.temenos.infinity.api.arrangements.constants.UserAccountSettingConstants;
import com.temenos.infinity.api.arrangements.dto.CustomerDetailsDTO;
import com.temenos.infinity.api.arrangements.dto.UserAccountSettingsDTO;
import com.temenos.infinity.api.arrangements.utils.ArrangementsUtils;

public class UserManagementAPIBackendDelegateImpl implements UserManagementAPIBackendDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public UserAccountSettingsDTO updateUserAccountSettings(UserAccountSettingsDTO userAccount,
			HashMap<String, Object> headerMap) throws ApplicationException {

		// Load Check Book Request properties
		Properties props = ArrangementsUtils.loadProps(UserAccountSettingConstants.PARAM_PROPERTY);

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
		alert.prepareError("OMS Request" + inputMap.toString()).log();
		
		String updateAccountResponse = null;
		String profileBackend = EnvironmentConfigurationsHandler.getServerProperty("PAYMENT_BACKEND");
		
		if("t24".equalsIgnoreCase(profileBackend)) {
			try {
			inputMap.clear();
			inputMap.put("accountID", userAccount.getAccountID());
			inputMap.put("nickName", userAccount.getNickName());
			inputMap.put("favouriteStatus", userAccount.getFavouriteStatus());
			inputMap.put("eStatementEnable", userAccount.geteStatementEnable());
			inputMap.put("email", userAccount.getEmail());
			updateAccountResponse = DBPServiceExecutorBuilder.builder()
					.withServiceId(ArrangementsAPIServices.ARRANGEMENTSORCHSERVICESJSON_UPDATEACCOUNTDETAILS.getServiceName())
					.withOperationId(ArrangementsAPIServices.ARRANGEMENTSORCHSERVICESJSON_UPDATEACCOUNTDETAILS.getOperationName())
					.withRequestParameters(inputMap).withRequestHeaders(headerMap)
					.withFabricAuthToken(headerMap.get("X-Kony-Authorization").toString()).build().getResponse();
			alert.prepareError("updateAccountResponse:: " + updateAccountResponse).log();
			userAccount.setId(HelperMethods.getNewId());
			userAccount.setStatus("Success");
			return userAccount;
			} catch (Exception e) {
				alert.prepareError("Unable to create user account settings order " + e).log();
				throw new ApplicationException(ErrorCodeEnum.ERR_20053);
			}
		} else if ("stub".equalsIgnoreCase(profileBackend) || "mock".equalsIgnoreCase(profileBackend)) {
			userAccount.setId(HelperMethods.getNewId());
			userAccount.setStatus("Success");
			return userAccount;
		}

		// Making a call to order request API
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
		Properties props = ArrangementsUtils.loadProps(UserAccountSettingConstants.PARAM_PROPERTY);
		
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

		inputMap.put("requestBody", requestBody.toString());
		alert.prepareError("OMS Request" + inputMap.toString()).log();

		// Making a call to order request API
		String updateCustomerDetailsResponse = null;
		JSONObject Response = new JSONObject();
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
		
		return responseDTO;
	}
	
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
    			if(result.has("User"))
    				user = result.getJSONArray("User");
    			else
    				user = result.getJSONArray("ExternalUsers");
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
}