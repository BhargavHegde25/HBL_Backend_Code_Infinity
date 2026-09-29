package com.kony.adminconsole.service.alertmanagement.staging;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.core.security.UserDetailsBean;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.handler.AlertManagementHandler;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.handler.CustomerHandler;
import com.kony.adminconsole.service.alertmanagement.staging.util.AlertSubscription;
import com.kony.adminconsole.service.authmodule.APICustomIdentityService;
import com.kony.adminconsole.utilities.ACConstants;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.kony.adminconsole.utilities.StatusEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

/**
 * Service to set Customer Alert Category Preference and Alert Type Preference
 * 
 * @author Aditya Mankal
 *
 */
public class AlertPreferenceManageService implements JavaService2 {

	private static final String ACCOUNT_ID_PARAM = "accountId";
	private static final String ACCOUNT_TYPE_PARAM = "accountTypeId";
	private static final String CUSTOMER_ID_PARAM = "customerId";
	private static final String IS_SUBSCRIBED_PARAM = "isSubscribed";
	private static final String ALERT_CATEGORY_ID_PARAM = "alertCategoryId";
	private static final String ALERT_SUBSCRIPTION = "alertSubscription";
	private static final String ACCOUNTS = "accounts";
	private static final String CUSTOMER_TYPE_STR = "customerTypeStr";
	private static final String LEGALENTITYID = "legalEntityId";


	private static final CustomerChannelSubscriptionProcessor channelsubscripProcessor = new CustomerChannelSubscriptionProcessor();
	private static final CustomerFrequencySubscriptionProcessor frequencyProcessor = new CustomerFrequencySubscriptionProcessor();
	private static final CustomerAlertSubscriptionProcessor alertProcessor = new CustomerAlertSubscriptionProcessor() ;


	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) {

		Result processedResult = new Result();
		String customerId = StringUtils.EMPTY;
		String loggedInUser = StringUtils.EMPTY;
		try {
			// Read Inputs
			String alertCategoryId = requestInstance.getParameter(ALERT_CATEGORY_ID_PARAM);
			diagnostic.prepareDebug("Received Alert Category ID:" + alertCategoryId).log();
			String isSubscribedStr = requestInstance.getParameter(IS_SUBSCRIBED_PARAM);
			diagnostic.prepareDebug("Received is Subscribed:" + isSubscribedStr).log();
			customerId = requestInstance.getParameter(CUSTOMER_ID_PARAM);
			diagnostic.prepareDebug("Is Customer ID Null?" + StringUtils.isBlank(customerId)).log();
			String accountId = requestInstance.getParameter(ACCOUNT_ID_PARAM);
			diagnostic.prepareDebug("Is Account ID Null?" + StringUtils.isBlank(accountId)).log();
			String accountTypeId = requestInstance.getParameter(ACCOUNT_TYPE_PARAM);
			diagnostic.prepareDebug("Is account Type Name Null?" + StringUtils.isBlank(accountTypeId)).log();
			String accounts = requestInstance.getParameter(ACCOUNTS);
			diagnostic.prepareDebug("Is Accounts Null?" + StringUtils.isBlank(accounts)).log();
			String customerTypeStr = requestInstance.getParameter(CUSTOMER_TYPE_STR);
			diagnostic.prepareDebug("Is customer type string Null?" + StringUtils.isBlank(customerTypeStr)).log();			
			String alertSubscription = requestInstance.getParameter(ALERT_SUBSCRIPTION);
			diagnostic.prepareDebug("Received alertSubsription " + alertSubscription).log();
			String backendId = requestInstance.getParameter("backendId");
			diagnostic.prepareDebug("backendId : " + backendId).log();
			
			String legalEntityId = requestInstance.getParameter(LEGALENTITYID);
			diagnostic.prepareDebug("Received is Subscribed:" + isSubscribedStr).log();

			// Validate Inputs
			
			if (StringUtils.isBlank(legalEntityId)) {
				// Missing Alert Category ID
				alert.prepareError("Missing legalEntityId").log();
				ErrorCodeEnum.ERR_22232.setErrorCode(processedResult);
				return processedResult;
			}
			if (StringUtils.isBlank(alertCategoryId)) {
				// Missing Alert Category ID
				alert.prepareError("Missing Alert Category ID").log();
				ErrorCodeEnum.ERR_20920.setErrorCode(processedResult);
				return processedResult;
			}
			if (StringUtils.isBlank(customerId)) {
				// Missing Alert Customer ID
				alert.prepareError("Missing Customer ID").log();
				ErrorCodeEnum.ERR_20688.setErrorCode(processedResult);
				return processedResult;
			}
			
			// Validate Customer Id by resolving username
            String customerUsername = CustomerHandler.getCustomerUsername(customerId, requestInstance);
            if (StringUtils.isBlank(customerUsername)) {
                // Unrecognized Customer. Customer Type Could not be resolved
                alert.prepareError("Unrecognized Customer").log();
                ErrorCodeEnum.ERR_20539.setErrorCode(processedResult);
                return processedResult;
            }
			
			if (StringUtils.isBlank(customerTypeStr)) {
				// Unrecognized Customer type. Customer Type Could not be resolved
				alert.prepareError("Unrecognized Customer Type str. Customer Type  Could not be resolved").log();
				ErrorCodeEnum.ERR_20792.setErrorCode(processedResult);
				return processedResult;
			}			
						
			if (StringUtils.isNotBlank(accountTypeId)
					&& !AlertManagementHandler.getAccountTypeIdsMap(requestInstance,legalEntityId).containsKey(accountTypeId)) {
				alert.prepareError("Invalid account type").log();
				ErrorCodeEnum.ERR_20549.setErrorCode(processedResult);
				return processedResult;
			}			
			
			if(StringUtils.isNotBlank(accountId) && StringUtils.isBlank(accountTypeId)) {
				String accountTypeDesc = AlertTypePreferenceGetService.
							getAccountTypeIdFromAccounts(requestInstance, accountId, accountTypeId, accounts,legalEntityId);
				if(StringUtils.isBlank(accountTypeDesc)) {
					alert.prepareError("Invalid account type").log();
					ErrorCodeEnum.ERR_20549.setErrorCode(processedResult);
					return processedResult;
				} 
			}

			if(StringUtils.isBlank(alertSubscription)) {
				alert.prepareError("Malformed alertSubscription JSON").log();
				ErrorCodeEnum.ERR_20928.setErrorCode(processedResult);
				return processedResult;           
			}
			AlertSubscription alertSubscriptionObj;

			try {
				alertSubscriptionObj = new Gson().fromJson(alertSubscription, AlertSubscription.class);
			}catch(Exception e) {
				alert.prepareError("Malformed alertSubscription JSON").log();
				ErrorCodeEnum.ERR_20928.setErrorCode(processedResult);
				return processedResult;           
			}

			if(alertSubscriptionObj == null || StringUtils.isBlank(alertSubscriptionObj.getPreferenceLevel()) ||
					!EnumUtils.isValidEnum(ACConstants.ALERTPREFERNCES.class, alertSubscriptionObj.getPreferenceLevel()))
			{
				alert.prepareError("No valid alertPreferenc is set").log();
				ErrorCodeEnum.ERR_20969.setErrorCode(processedResult);
				return processedResult;
			}
			
			//Get AlertPreferences
			Result alertPreferencesReadResult = AlertManagementHandler.getAlertPreferences(requestInstance,legalEntityId);
			if(alertPreferencesReadResult.getDatasetById(ACConstants.CUSTOMERVIEWALERTCONFIGURATION_TN).getAllRecords().isEmpty()) {
				alert.prepareError("No valid alertPreferenc is set").log();
				ErrorCodeEnum.ERR_20970.setErrorCode(processedResult);
				return processedResult;
			}
			
			Record rec = alertPreferencesReadResult.getDatasetById(ACConstants.CUSTOMERVIEWALERTCONFIGURATION_TN).getRecord(0);
	    	String alertPreferenceFetched = rec.getParamValueByName("alertPreferenceView");
	    	if(!alertPreferenceFetched.equalsIgnoreCase(alertSubscriptionObj.getPreferenceLevel())) {
	    		alert.prepareError("Input alertPreferenc is not matched to backend preference").log();
				ErrorCodeEnum.ERR_20971.setErrorCode(processedResult);
				return processedResult;
		    } 		   

			alertSubscriptionObj.setCatId(alertCategoryId);
			alertSubscriptionObj.setAccountID(accountId);
			alertSubscriptionObj.setAccountType(accountTypeId);
			alertSubscriptionObj.setCustomerId(customerId);
			alertSubscriptionObj.setBackendId(backendId);

			// Fetch Logged In User Info
			UserDetailsBean userDetailsBeanInstance = LoggedInUserHandler.getUserDetails(requestInstance);
			if (userDetailsBeanInstance != null) {
				loggedInUser = userDetailsBeanInstance.getId();
			}

			// Set Alert Category Preference
			if (StringUtils.equalsIgnoreCase(isSubscribedStr, "TRUE")
					|| StringUtils.equalsIgnoreCase(isSubscribedStr, "FALSE")) {
				boolean isSubscribed = StringUtils.equalsIgnoreCase(isSubscribedStr, "TRUE");
				Record alertCategoryPreferenceRecord = setCustomerAlertEnablement(customerId, alertCategoryId,
						accountId, accountTypeId, isSubscribed, loggedInUser, requestInstance,legalEntityId);
				processedResult.addRecord(alertCategoryPreferenceRecord);
			}

			if( alertSubscription != null ) {
				Dataset channelDataset = channelsubscripProcessor.setChannelSubscriptions(
						alertSubscriptionObj, requestInstance, loggedInUser,legalEntityId);
				processedResult.addDataset(channelDataset);
				if(isFrequencyEditable(rec)) {
						processedResult.addDataset(frequencyProcessor.setFrequenctSubscriptions(
						alertSubscriptionObj, requestInstance, loggedInUser,legalEntityId));
				}
				if( channelDataset.getAllRecords() != null && !channelDataset.getAllRecords().isEmpty()){  
					processedResult.addRecord(alertProcessor.setAlertSubscriptions(
						alertSubscriptionObj, requestInstance, loggedInUser, customerTypeStr,legalEntityId));
				}
			}

			auditActivity(customerId, loggedInUser, true, requestInstance);

		} catch (ApplicationException e) {
			auditActivity(customerId, loggedInUser, false, requestInstance);
			Result errorResult = new Result();
			errorResult.addParam("FailureReason", e.getMessage());
			alert.prepareError("Application Exception. Checked Involved Operations. Exception Trace:", e).log();
			e.getErrorCodeEnum().setErrorCode(errorResult);
			return errorResult;
		} catch (Exception e) {
			auditActivity(customerId, loggedInUser, false, requestInstance);
			Result errorResult = new Result();
			errorResult.addParam("FailureReason", e.getMessage());
			diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
			ErrorCodeEnum.ERR_20927.setErrorCode(errorResult);
			return errorResult;
		}

		return processedResult;
	}

	/**
	 * Method to audit the Activity Information
	 * 
	 * @param customerId
	 * @param loggedInUser
	 * @param status
	 * @param requestInstance
	 */
	private void auditActivity(String customerId, String loggedInUser, boolean status,
			DataControllerRequest requestInstance) {

		if (!StringUtils.equalsIgnoreCase(loggedInUser, APICustomIdentityService.API_USER_ID)) {
			if (status == true) {
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.ALERTS, EventEnum.UPDATE,
						ActivityStatusEnum.SUCCESSFUL, "Alert Preferences Updated. Customer id:" + customerId);
			} else {
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.ALERTS, EventEnum.UPDATE,
						ActivityStatusEnum.FAILED, "Failed to update Alert Preferences. Customer id:" + customerId);
			}
		}
	}

	/**
	 * Method to set the Customer Alert Category Preference of a customer
	 * 
	 * @param customerId
	 * @param alertCategoryId
	 * @param accountId
	 * @param isSubscribed
	 * @param loggedInUserId
	 * @param requestInstance
	 * @return Record containing status information
	 * @throws ApplicationException
	 */
	private Record setCustomerAlertEnablement(String customerId, String alertCategoryId, String accountId,
			String accountTypeId, boolean isSubscribed, String loggedInUserId, DataControllerRequest requestInstance, String legalEntityId)
					throws ApplicationException {

		Record operationRecord = new Record();
		operationRecord.setId("alertCategoryEnablement");

		if (StringUtils.isBlank(customerId) || StringUtils.isBlank(alertCategoryId) || StringUtils.isBlank(legalEntityId) ) {
			// Missing Mandatory Inputs. Returning Empty Record
			alert.prepareError("Missing Mandatory Inputs. Returning Empty Record.").log();
			return operationRecord;
		}

		// Fetch Customer Alert Category Association
		boolean isInitialRegistration = readCustomerChannelSubscription(customerId, alertCategoryId, accountId,
				accountTypeId, requestInstance,legalEntityId);
		diagnostic.prepareDebug("isInitialRegistration" + String.valueOf(isInitialRegistration)).log();

		// Create/Update Alert Category Subscription based on current association
		
		Map<String, String> parameterMap = new HashMap<>();
		parameterMap.put("Customer_id", customerId);
		parameterMap.put("AlertCategoryId", alertCategoryId);

		if (StringUtils.isNotBlank(accountId)) {
			parameterMap.put("AccountID", accountId);
		} else {
			parameterMap.put("AccountID", ACConstants.STAR_VALUE);
		}

		if (StringUtils.isNotBlank(accountTypeId)) {
			parameterMap.put("AccountType", accountTypeId);
		} else {
			parameterMap.put("AccountType", ACConstants.STAR_VALUE);
		}

		parameterMap.put("Status_id",
				isSubscribed ? StatusEnum.SID_SUBSCRIBED.name() : StatusEnum.SID_UNSUBSCRIBED.name());
		parameterMap.put("companyLegalUnit", legalEntityId);
		String currOperation = StringUtils.EMPTY;
		String currentTime = CommonUtilities.getISOFormattedLocalTimestamp();
		String setCustomerAlertSwitchResponse = StringUtils.EMPTY;
		JSONObject setCustomerAlertSwitchResponseJSON = null;

		if (isInitialRegistration) {
			// Non-Existing Alert Category Association. Create Alert Category Preference
			currOperation = "createPreference";
			parameterMap.put("createdby", loggedInUserId);
			parameterMap.put("createdts", currentTime);
			diagnostic.prepareDebug("Creating Customer Alert Category Switch Record").log();
			setCustomerAlertSwitchResponse = Executor.invokeService(ServiceURLEnum.CUSTOMERALERTSWITCH_CREATE,
					parameterMap, null, requestInstance);
		} else {
			// Existing Alert Category Association. Update Alert Category Preference
			currOperation = "updatePreference";
			parameterMap.put("modifiedby", loggedInUserId);
			parameterMap.put("lastmodifiedts", currentTime);
			diagnostic.prepareDebug("Updating Customer Alert Category Switch Record").log();
			setCustomerAlertSwitchResponse = Executor.invokeService(ServiceURLEnum.CUSTOMERALERTSWITCH_UPDATE,
					parameterMap, null, requestInstance);
		}

		// Add Operation Meta
		operationRecord.addParam(new Param("operation", currOperation, FabricConstants.STRING));
		operationRecord.addParam(new Param("customerId", customerId, FabricConstants.STRING));
		operationRecord.addParam(new Param("alertCategoryId", alertCategoryId, FabricConstants.STRING));
		if (StringUtils.isNotBlank(accountId)) {
			operationRecord.addParam(new Param("accountId", accountId, FabricConstants.STRING));
		}
		if (StringUtils.isNotBlank(accountTypeId)) {
			operationRecord.addParam(new Param("accountTypeId", accountTypeId, FabricConstants.STRING));
		}

		// Check Operation Response
		setCustomerAlertSwitchResponseJSON = CommonUtilities.getStringAsJSONObject(setCustomerAlertSwitchResponse);
		if (setCustomerAlertSwitchResponseJSON == null
				|| !setCustomerAlertSwitchResponseJSON.has(FabricConstants.OPSTATUS)
				|| setCustomerAlertSwitchResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
			diagnostic.prepareDebug("Failed CRUD Operation").log();
			operationRecord.addParam(new Param("status", "Fail", FabricConstants.STRING));
			operationRecord
			.addParam(new Param("serviceResponse", setCustomerAlertSwitchResponse, FabricConstants.STRING));
		} else {
			diagnostic.prepareDebug("Successful CRUD Operation").log();
			operationRecord.addParam(new Param("status", "Success", FabricConstants.STRING));
		}

		return operationRecord;
	}
	
	public static boolean isFrequencyEditable(Record rec) {
		boolean isEditFrequencyEnabled = false;
		String alertFrequencyPreferenceStr = rec.getParamValueByName("enableFrequency");
		if (StringUtils.equalsIgnoreCase(alertFrequencyPreferenceStr, "TRUE")
				|| StringUtils.equalsIgnoreCase(alertFrequencyPreferenceStr, "1")) {
			isEditFrequencyEnabled = true;			
		}
		return isEditFrequencyEnabled;
	}

	public boolean readCustomerChannelSubscription(String customerId, String alertCategoryId, String accountId,
			String accountTypeId, DataControllerRequest requestInstance, String legalEntityId) throws ApplicationException {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder
		.append("Customer_id eq '" + customerId + "' and AlertCategoryId eq '" + alertCategoryId + "' and companyLegalUnit eq '" + legalEntityId + "'");

		if (StringUtils.isNotBlank(accountId)) {
			queryBuilder.append(" and AccountID eq '" + accountId + "'");
		}
		else {			
			queryBuilder.append(" and AccountID eq '").append(ACConstants.STAR_VALUE).append("'");
		}
		if (StringUtils.isNotBlank(accountTypeId)) {
			queryBuilder.append(" and AccountType eq '" + accountTypeId + "'");
		}
		else {			
			queryBuilder.append(" and AccountType eq '").append(ACConstants.STAR_VALUE).append("'");
		}

		queryBuilder.trimToSize();
		String filterQuery = queryBuilder.toString();

		Map<String, String> parameterMap1 = new HashMap<>();
		parameterMap1.put(ODataQueryConstants.FILTER, filterQuery);

		String readCustomerAlertSwitchResponse =
				Executor.invokeService(ServiceURLEnum.CUSTOMERALERTSWITCH_READ, parameterMap1, null, requestInstance);
		JSONObject readCustomerAlertSwitchResponseJSON =
				CommonUtilities.getStringAsJSONObject(readCustomerAlertSwitchResponse);
		if (readCustomerAlertSwitchResponseJSON == null
				|| !readCustomerAlertSwitchResponseJSON.has(FabricConstants.OPSTATUS)
				|| readCustomerAlertSwitchResponseJSON.getInt(FabricConstants.OPSTATUS) != 0
				|| !readCustomerAlertSwitchResponseJSON.has("customeralertswitch")) {
			// Failed to Read Customer Alert Switch
			diagnostic.prepareDebug("Failed CRUD Operation").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20927);
		}

		// Successfully Read Customer Alert Switch
		diagnostic.prepareDebug("Successful CRUD Operation").log();

		JSONArray customerAlertSwitchRecordsArray =
				readCustomerAlertSwitchResponseJSON.optJSONArray("customeralertswitch");
		boolean isInitialRegistration;
		if (customerAlertSwitchRecordsArray != null && customerAlertSwitchRecordsArray.optJSONObject(0) != null) {
			// Existing Alert Category Association
			isInitialRegistration = false;
		} else {
			// Non-Existing Alert Category Association
			isInitialRegistration = true;
		}
		return isInitialRegistration;
	}
}
