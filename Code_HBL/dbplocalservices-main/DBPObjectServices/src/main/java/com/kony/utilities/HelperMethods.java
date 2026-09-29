package com.kony.utilities;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.mfa.MFAConstants;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.kony.mfaconstant.ServiceNameConstants;
import com.kony.utils.EnvironmentConfigurationsHandler;
import com.kony.utils.TokenUtils;
import com.kony.utils.URLConstants;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.OperationData;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.ServicesManagerHelper;
import com.konylabs.middleware.api.processor.QueryParamsHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.exceptions.MiddlewareException;
import com.konylabs.middleware.registry.AppRegistryException;

public class HelperMethods {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private static String cardType=null;
	public static String getCustomerSessionUrl(FabricRequestManager requestManager) {
		return EnvironmentConfigurationsHandler.getValue(URLConstants.CUSTOMER_SESSION_URL, requestManager);
	}

	public static String getCustomerIdFromSession(FabricRequestManager requestManager) {
		return getCustomerIdFromIdentityService(requestManager);
	}

	private static String getCustomerIdFromIdentityService(FabricRequestManager requestManager) {
		String customerId = "";
		try {
			return requestManager.getServicesManager().getIdentityHandler().getUserId();

		} catch (Exception e) {
			alert.prepareError(e.getMessage()).log();
		}

		return customerId;
	}

	public static String getServiceId(JsonObject requestpayload) {
		String serviceId = getStringFromJsonObject(requestpayload, ServiceNameConstants.SERVICE_NAME);
		if (null == serviceId) {
			serviceId = getServiceId(requestpayload.get("MFAAttributes"));
		}
		return serviceId;
	}

	private static String getServiceId(JsonElement mfaElement) {
		if (!isJsonEleNull(mfaElement) && !mfaElement.isJsonNull()) {
			JsonObject mfaAttributes = null;
			if (mfaElement.isJsonObject()) {
				mfaAttributes = mfaElement.getAsJsonObject();
			} else {
				JsonParser parser = new JsonParser();
				mfaAttributes = parser.parse(mfaElement.getAsString()).getAsJsonObject();
			}
			if (mfaAttributes.has(MFAConstants.SERVICE_NAME)
					&& !mfaAttributes.get(MFAConstants.SERVICE_NAME).isJsonNull()) {
				return mfaAttributes.get(MFAConstants.SERVICE_NAME).getAsString();
			}
		}
		return null;
	}

	public static boolean isDACSkip(JsonObject requestpayload) {
		JsonElement mfaElement = requestpayload.get("skipDAC");
		diagnostic.prepareDebug("Inside DAC skip").log();
		if (!isJsonEleNull(mfaElement) && !mfaElement.isJsonNull()) {
			diagnostic.prepareDebug("Inside DAC skip :" + mfaElement.getAsString()).log();
			return true;
		}
		return false;
	}

	public static boolean isMFAVerify(JsonObject requestpayload) {
		JsonElement mfaElement = requestpayload.get("MFAAttributes");
		if (!isJsonEleNull(mfaElement) && !mfaElement.isJsonNull()) {
			JsonObject mfaAttributes = null;
			if (mfaElement.isJsonObject()) {
				mfaAttributes = mfaElement.getAsJsonObject();
			} else {
				JsonParser parser = new JsonParser();
				mfaAttributes = parser.parse(mfaElement.getAsString()).getAsJsonObject();
			}
			return ((mfaAttributes.has(MFAConstants.OTP) && !mfaAttributes.get(MFAConstants.OTP).isJsonNull())
					|| (mfaAttributes.has(MFAConstants.SECURITY_QUESTION)
							&& !mfaAttributes.get(MFAConstants.SECURITY_QUESTION).isJsonNull()));
		}
		return false;
	}

	public static boolean isDACEnabled() {
		ServicesManager serviceManager;
		try {
			serviceManager = ServicesManagerHelper.getServicesManager();
			ConfigurableParametersHelper configurableParametersHelper = serviceManager
					.getConfigurableParametersHelper();
			String isDacEnabled = configurableParametersHelper.getServerProperty("DAC_ENABLED");
			return StringUtils.isBlank(isDacEnabled)
					|| BooleanUtils.toBoolean(configurableParametersHelper.getServerProperty("DAC_ENABLED"));
		} catch (Exception e) {
			alert.prepareError(e.getMessage()).log();
		}
		return true;
	}

	public static String getStringFromJsonObject(JsonObject object, String key) {
		JsonElement element = getElementFromJsonObject(object, key, false);
		return isJsonEleNull(element) ? null : element.getAsString();
	}

	public static boolean isJsonEleNull(JsonElement ele) {
		return null == ele || ele.isJsonNull();
	}

	public static String getStringFromJsonObject(JsonObject object, String key, boolean required) {
		JsonElement element = getElementFromJsonObject(object, key, required);
		return isJsonEleNull(element) ? null : element.getAsString();
	}

	public static JsonElement getElementFromJsonObject(JsonObject object, String key, boolean required) {
		JsonElement element = object.get(key);
		if (isJsonEleNull(element) && required) {
			throw new IllegalArgumentException("Required attribute '" + key + "' was not present");
		}
		return element;
	}

	public static String getOperationString(ServicesManager sm) throws AppRegistryException {
		OperationData operationData = sm.getOperationData();
		String serviceId = operationData.getServiceId();
		String objectId = operationData.getObjectId();
		String operationId = operationData.getOperationId();
		String appendedString = String.join("_", serviceId, objectId, operationId);
		return appendedString;
	}

	public static JsonObject getJsonObjectFromJsonObject(JsonObject object, String key, boolean required) {
		JsonElement element = getElementFromJsonObject(object, key, required);
		if (element == null) {
			return null;
		}
		if (!element.isJsonObject()) {
			throw new IllegalArgumentException("Value for attribute '" + key + "' is not a JSON object");
		}
		return element.getAsJsonObject();
	}

	public static FabricRequestManager buildRequestPayloadForCRUDOperations(FabricRequestManager requestManager) {
		QueryParamsHandler queryParamsHandler = requestManager.getQueryParamsHandler();
		Set<String> parameterNames = queryParamsHandler.getParameterNames();
		JsonObject queryParamjsonObject = new JsonObject();
		for (String queryParamName : parameterNames) {
			queryParamjsonObject.addProperty(queryParamName, queryParamsHandler.getParameter(queryParamName));
		}
		requestManager.getPayloadHandler().updatePayloadAsJson(queryParamjsonObject);
		return requestManager;
	}

	public static String getSessionId(FabricRequestManager requestManager) {
		String sessionid = null;
		try {
			sessionid = requestManager.getServicesManager().getIdentityHandler().getSecurityAttributes()
					.get("session_token").toString();
		} catch (Exception e) {
		}
		if (sessionid == null) {
			try {
				String authkey = requestManager.getHeadersHandler().getHeader("X-Kony-Authorization");
				TokenUtils tokenobj = new TokenUtils(authkey);
				String customerid = tokenobj.getValue(URLConstants.PROVIDER_USER_ID);
				sessionid = tokenobj.getValue(URLConstants.SESSIONID);
			} catch (Exception e) {
			}
		}
		return sessionid;
	}
	
	public static String getPermittedUserActionIds(FabricRequestManager request, List<String> requiredActionIds) {

		try {
			JSONObject featuresPermissions = LegalEntityUtil.getUserCurrentLegalEntityFeaturePermissions(request);
			alert.prepareError("****************Permissions :"+featuresPermissions).log();
			Object permissionsObj = featuresPermissions.get("permissions");
			if(permissionsObj != null ) {
				String activeActions = "";
				String permissions = permissionsObj.toString();
				permissions = permissions.replaceAll("\"", "");
				permissions = permissions.substring(1, permissions.length()-1);
				
				List<String> permissionList = Arrays.asList(permissions.split(","));
				
				Set<String> result = permissionList.stream().distinct().filter(requiredActionIds::contains).collect(Collectors.toSet());
				
				if(result.size() > 0) {
					activeActions = result.toString();
					activeActions = activeActions.replaceAll("\\s","");
					activeActions = activeActions.substring(1, activeActions.length()-1);
					return activeActions;
				}
			}
			
		} 
		catch (Exception e) {
			alert.prepareError("Error while fetching customer attributes from the identity session", e).log();
		}
		
		 return null;
	}
	
	public static boolean isAdminUser(FabricRequestManager request) {
		try {
			Map<String, Object>  customerUserAttributes = request.getServicesManager()
					.getIdentityHandler().getUserAttributes();
			String customerTypeId = (String)customerUserAttributes.get("CustomerType_id");
			return StringUtils.isNoneBlank(customerTypeId) 
					&& "DBP_API_USER".equalsIgnoreCase(customerTypeId);
		}
		catch (Exception e) {
			alert.prepareError("Error while fetching customer attributes from the identity session", e).log();
		}
		return false;
	}


    public static void setCardType(String cardTypeVal)
    {
    	cardType=cardTypeVal;
    }
    
    public static String getCardType()
    {
    	return cardType;
    }
}