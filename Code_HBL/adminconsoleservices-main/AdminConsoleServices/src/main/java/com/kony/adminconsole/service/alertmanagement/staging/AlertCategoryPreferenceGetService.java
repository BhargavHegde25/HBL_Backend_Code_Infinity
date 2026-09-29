package com.kony.adminconsole.service.alertmanagement.staging;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.core.security.UserDetailsBean;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.handler.AlertManagementHandler;
import com.kony.adminconsole.handler.CustomerHandler;
import com.kony.adminconsole.service.authmodule.APICustomIdentityService;
import com.kony.adminconsole.utilities.ACConstants;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

/**
 * Service to retrieve the Alert Category preference of customers
 * 
 * @author Aditya Mankal
 */
public class AlertCategoryPreferenceGetService implements JavaService2 {

    private static final String DEFAULT_LOCALE = AlertManagementHandler.DEFAULT_LOCALE;
    private static final String ACCOUNT_ID_PARAM = "AccountId";
    private static final String ACCOUNT_TYPE_PARAM = "AccountTypeId";
    private static final String CUSTOMER_ID_PARAM = "CustomerId";
    private static final String LEGALENTITYID_PARAM = "legalEntityId";

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {

        Result processedResult = new Result();

        try {
            // Read Inputs
            String customerId = requestInstance.getParameter(CUSTOMER_ID_PARAM);
            diagnostic.prepareDebug("Is Customer ID Null?" + StringUtils.isBlank(customerId)).log();
            String accountId = requestInstance.getParameter(ACCOUNT_ID_PARAM);
            diagnostic.prepareDebug("Is Account ID Null?" + StringUtils.isBlank(accountId)).log();
            String accountTypeId = requestInstance.getParameter(ACCOUNT_TYPE_PARAM);
            diagnostic.prepareDebug("Is Account Type Null?" + StringUtils.isBlank(accountTypeId)).log();
            String acceptLanguage = requestInstance.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
            diagnostic.prepareDebug("Received Accept-Language Header:" + acceptLanguage).log();
            
            String legalEntityId = requestInstance.getParameter(LEGALENTITYID_PARAM);
            
            diagnostic.prepareDebug("Received legalEntityId:" + legalEntityId).log();

            // Validate Inputs
            if (StringUtils.isBlank(customerId)) {
                // Missing Alert Customer ID
                alert.prepareError("Missing Customer ID").log();
                ErrorCodeEnum.ERR_20688.setErrorCode(processedResult);
                return processedResult;
            }
            if (StringUtils.isBlank(legalEntityId)) {
                // Missing Alert Customer ID
                alert.prepareError("Missing  legalEntityId").log();
                ErrorCodeEnum.ERR_22232.setErrorCode(processedResult);
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
            if (StringUtils.isNotBlank(accountTypeId)
                    && !AlertManagementHandler.getAccountTypeIdsMap(requestInstance,legalEntityId).containsKey(accountTypeId)) {
                alert.prepareError("Invalid account type").log();
                ErrorCodeEnum.ERR_20549.setErrorCode(processedResult);
                return processedResult;
            }

            // Fetch Logged In User Info
            String loggedInUser = StringUtils.EMPTY;
            UserDetailsBean userDetailsBeanInstance = LoggedInUserHandler.getUserDetails(requestInstance);
            if (userDetailsBeanInstance != null) {
                loggedInUser = userDetailsBeanInstance.getId();
            }

            // Format Accept Language Identifier
            if (StringUtils.isBlank(acceptLanguage)) {
                // Consider Default Locale if Accept-Language Header is Blank
                acceptLanguage = DEFAULT_LOCALE;
                diagnostic.prepareDebug(
                        "Received Accept-Language Header is empty. Returning Data of Default Locale." + DEFAULT_LOCALE).log();
            }
            acceptLanguage = CommonUtilities.formatLanguageIdentifier(acceptLanguage);
            if (StringUtils.equalsIgnoreCase(loggedInUser, APICustomIdentityService.API_USER_ID)) {
                acceptLanguage = AlertManagementHandler.formatLocaleAsPerKonyMobileSDK(acceptLanguage);
            }
            
            Result alertPreferences = AlertManagementHandler.getAlertPreferences(requestInstance,legalEntityId);
        	Dataset alertConfigDs = getAlertConfigRecord(processedResult, alertPreferences);
            if(processedResult.getParamValueByName(ACConstants.DBP_ERROR_CODE) != null) {
            	return processedResult;
            }
            processedResult.addDataset(alertConfigDs);

            // Fetch Customer Alert Category Subscription
            Map<String, JSONObject> alertCategorySubscriptionMap =
                    AlertManagementHandler.getCustomerCategorySubscription(customerId, accountId, accountTypeId,
                            acceptLanguage, requestInstance,legalEntityId);

            // Sort Customer Alert Category Subscription
            alertCategorySubscriptionMap = CommonUtilities.sortJSONObjectMapByValue(alertCategorySubscriptionMap,
                    "alertcategory_DisplaySequence", true, true);

            // Construct Result Dataset
            Dataset resultDataset = getResultDataset(alertCategorySubscriptionMap);
            processedResult.addDataset(resultDataset);

            
            
        } catch (ApplicationException e) {
            Result errorResult = new Result();
            alert.prepareError("Application Exception. Checked Involved Operations. Exception Trace:", e).log();
            e.getErrorCodeEnum().setErrorCode(errorResult);
            return errorResult;
        } catch (Exception e) {
            Result errorResult = new Result();
            diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
            ErrorCodeEnum.ERR_20921.setErrorCode(errorResult);
            return errorResult;
        }

        diagnostic.prepareDebug("Returning Success Response").log();
        return processedResult;
    }

    public static Dataset getAlertConfigRecord(Result processedResult, Result alertPreferences) {
    	Dataset alertConfigDs = new Dataset("alertConfiguration");  
    	Record clientRecord = null;
    	if(!alertPreferences.getDatasetById(ACConstants.CUSTOMERVIEWALERTCONFIGURATION_TN).getAllRecords().isEmpty()) {
    		Record rec = alertPreferences.getDatasetById(ACConstants.CUSTOMERVIEWALERTCONFIGURATION_TN).getRecord(0);
    		clientRecord = new Record();
    		String alertPreference = rec.getParamValueByName("alertPreferenceView");
    		if(StringUtils.isBlank(alertPreference) ||
    				!EnumUtils.isValidEnum(ACConstants.ALERTPREFERNCES.class, alertPreference)){
    			alert.prepareError("No valid alertPreferenc is set").log();
    			ErrorCodeEnum.ERR_20970.setErrorCode(processedResult);
    			return alertConfigDs;
    		}
    		clientRecord.addParam(new Param("alertPreferenceView",alertPreference,FabricConstants.STRING));
    		clientRecord.addParam(new Param("enableFrequency",rec.getParamValueByName("enableFrequency"),FabricConstants.STRING));
    		clientRecord.addParam(new Param("enableSeparateContact",rec.getParamValueByName("enableSeparateContact"),FabricConstants.STRING));

    	}
    	alertConfigDs.addRecord(clientRecord);
    	return alertConfigDs;
    }

    /**
     * Method to construct Response Dataset
     * 
     * @param alertCategorySubscriptionMap
     * @return Dataset
     */
    private Dataset getResultDataset(Map<String, JSONObject> alertCategorySubscriptionMap) {

        Dataset resultDataset = new Dataset();
        resultDataset.setId("records");

        JSONObject currSubscriptionJSON = null;

        // Traverse Alert Categories
        for (Entry<String, JSONObject> entry : alertCategorySubscriptionMap.entrySet()) {

            Record currAlertCategoryRecord = new Record();

            // Traverse Current Alert Category
            for (String currKey : entry.getValue().keySet()) {

                if (!StringUtils.equals(currKey, AlertManagementHandler.CATEGORY_SUBSCRIPTION_PARAM)) {
                    currAlertCategoryRecord
                            .addParam(new Param(currKey, entry.getValue().optString(currKey), FabricConstants.STRING));
                } else {
                    // Alert Category Subscription Record
                    Record categorySubscriptionRecord = new Record();
                    categorySubscriptionRecord.setId(AlertManagementHandler.CATEGORY_SUBSCRIPTION_PARAM);
                    currAlertCategoryRecord.addRecord(categorySubscriptionRecord);

                    currSubscriptionJSON = entry.getValue().optJSONObject(currKey);
                    if (currSubscriptionJSON != null) {
                        categorySubscriptionRecord.addParam(new Param(AlertManagementHandler.IS_SUBSCRIBED_PARAM,
                                currSubscriptionJSON.optString(AlertManagementHandler.IS_SUBSCRIBED_PARAM),
                                FabricConstants.STRING));
                        categorySubscriptionRecord.addParam(new Param(AlertManagementHandler.IS_INITIAL_LOAD_PARAM,
                                currSubscriptionJSON.optString(AlertManagementHandler.IS_INITIAL_LOAD_PARAM),
                                FabricConstants.STRING));
                    } else {
                        categorySubscriptionRecord.addParam(new Param(AlertManagementHandler.IS_SUBSCRIBED_PARAM,
                                String.valueOf(false), FabricConstants.STRING));
                        categorySubscriptionRecord.addParam(new Param(AlertManagementHandler.IS_INITIAL_LOAD_PARAM,
                                String.valueOf(true), FabricConstants.STRING));
                    }

                }

            }
            resultDataset.addRecord(currAlertCategoryRecord);
        }
        return resultDataset;
    }

}
