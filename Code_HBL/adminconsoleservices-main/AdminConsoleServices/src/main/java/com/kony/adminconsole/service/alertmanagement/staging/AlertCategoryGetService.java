package com.kony.adminconsole.service.alertmanagement.staging;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.core.security.UserDetailsBean;
import com.kony.adminconsole.handler.AlertManagementHandler;
import com.kony.adminconsole.service.alertmanagement.utils.Constants;
import com.kony.adminconsole.service.authmodule.APICustomIdentityService;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.PermissionName;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

/**
 * Service to retrieve the Alert Categories
 * 
 * @author Aditya Mankal
 */
public class AlertCategoryGetService implements JavaService2 {

	private static final String DEFAULT_LOCALE = AlertManagementHandler.DEFAULT_LOCALE;
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) {

		Result processedResult = new Result();

		try {
			if (StringUtils.isBlank(requestInstance.getParameter(Constants.LEGAL_ENTITY_ID))) {
				alert.prepareError("legalEntityId cannot be empty").log();
				ErrorCodeEnum.ERR_22232.setErrorCode(processedResult);
				return processedResult;
			}
			String[] reqPermissions = {PermissionName.API_ACCESS,PermissionName.VIEW_ALERTS,PermissionName.VIEW_APP_CONTENT};
			if(!LoggedInUserHandler.hasAccessToLegalEntity(requestInstance,reqPermissions))
            {
				processedResult.addParam(new Param("Status", "Alert Category Get operation failed", FabricConstants.STRING));
                ErrorCodeEnum.ERR_22231.setErrorCode(processedResult);
                alert.prepareError("Logged in user do not have access to this legalEntity ").log();
                return processedResult;        
            }
			// Read Inputs
			String acceptLanguage = requestInstance.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
			diagnostic.prepareDebug("Received Accept-Language Header:" + acceptLanguage).log();
			
			String legalEntityId = requestInstance.getParameter(Constants.LEGAL_ENTITY_ID);
			
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
				diagnostic.prepareDebug("Received Accept-Language Header is empty. Returning Data of Default Locale." + DEFAULT_LOCALE).log();
			}
			acceptLanguage = CommonUtilities.formatLanguageIdentifier(acceptLanguage);
			if (StringUtils.equalsIgnoreCase(loggedInUser, APICustomIdentityService.API_USER_ID)) {
				acceptLanguage = AlertManagementHandler.formatLocaleAsPerKonyMobileSDK(acceptLanguage);
			}

			// Construct Input Map
			Map<String, String> inputMap = new HashMap<>();
			if (StringUtils.equals(acceptLanguage, DEFAULT_LOCALE)) {
				inputMap.put(ODataQueryConstants.FILTER, "(alertcategorytext_LanguageCode eq '" + acceptLanguage + "' and alertcategory_companyLegalunit eq '" + legalEntityId + "')");
			} else {
				inputMap.put(ODataQueryConstants.FILTER,
						"(alertcategorytext_LanguageCode eq '" + acceptLanguage + "' or alertcategorytext_LanguageCode eq '" + DEFAULT_LOCALE + "') and alertcategory_companyLegalunit eq '"+ legalEntityId + "'");
			}
			inputMap.put(ODataQueryConstants.ORDER_BY, "alertcategory_DisplaySequence asc");
			diagnostic.prepareDebug("filterQuery for alertcategory_view:" + inputMap.get(ODataQueryConstants.FILTER)).log();
			// Fetch Alert Categories
			String readAlertCategoryViewResponse = Executor.invokeService(ServiceURLEnum.ALERTCATEGORY_VIEW_READ, inputMap, null, requestInstance);
			JSONObject readAlertCategoryViewResponseJSON = CommonUtilities.getStringAsJSONObject(readAlertCategoryViewResponse);

			if (readAlertCategoryViewResponseJSON != null && readAlertCategoryViewResponseJSON.has(FabricConstants.OPSTATUS)
					&& readAlertCategoryViewResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readAlertCategoryViewResponseJSON.has("alertcategory_view")) {
				diagnostic.prepareDebug("Successful CRUD Operation").log();

				Dataset recordsDataset = new Dataset();
				recordsDataset.setId("records");

				// Construct Result dataset
				JSONArray alertCategoriesJSONArray = readAlertCategoryViewResponseJSON.optJSONArray("alertcategory_view");
				alertCategoriesJSONArray = CommonUtilities.filterRecordsByLocale(alertCategoriesJSONArray, "alertcategorytext_LanguageCode", "alertcategory_id", DEFAULT_LOCALE);
				alertCategoriesJSONArray = CommonUtilities.sortJSONArrayOfJSONObjects(alertCategoriesJSONArray, "alertcategory_DisplaySequence", true, true);

				// Filter Records by Locale
				if (alertCategoriesJSONArray != null && alertCategoriesJSONArray.length() > 0) {
					JSONObject currJSONObject;
					for (Object currObject : alertCategoriesJSONArray) {
						if (currObject instanceof JSONObject) {
							currJSONObject = (JSONObject) currObject;
							Record currRecord = new Record();
							for (String key : currJSONObject.keySet()) {
								currRecord.addParam(new Param(key, currJSONObject.optString(key), FabricConstants.STRING));
							}
							recordsDataset.addRecord(currRecord);
						}
					}
				}
				processedResult.addDataset(recordsDataset);

			} else {
				alert.prepareError("Failed CRUD Operation").log();
				ErrorCodeEnum.ERR_20915.setErrorCode(processedResult);
			}

		} catch (Exception e) {
			Result errorResult = new Result();
			diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
			ErrorCodeEnum.ERR_20915.setErrorCode(errorResult);
			return errorResult;
		}
		return processedResult;
	}
}
