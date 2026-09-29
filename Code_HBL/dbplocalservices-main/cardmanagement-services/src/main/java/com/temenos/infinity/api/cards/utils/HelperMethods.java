package com.temenos.infinity.api.cards.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.mfa.MFAConstants;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.ServicesManagerHelper;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;

public class HelperMethods {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private static String cardType=null;
	
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
	
	public static boolean isJsonEleNull(JsonElement ele) {
		return null == ele || ele.isJsonNull();
	}
	
    public static String getCardType()
    {
    	return cardType;
    }
    
	public static String getCustomerIdFromSession(FabricRequestManager requestManager) {
		return getCustomerIdFromIdentityService(requestManager);
	}

	public static void setCardType(String cardTypeVal)
    {
    	cardType=cardTypeVal;
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
	
	public static JsonElement getElementFromJsonObject(JsonObject object, String key, boolean required) {
		JsonElement element = object.get(key);
		if (isJsonEleNull(element) && required) {
			throw new IllegalArgumentException("Required attribute '" + key + "' was not present");
		}
		return element;
	}
	
	public static String getStringFromJsonObject(JsonObject object, String key) {
		JsonElement element = getElementFromJsonObject(object, key, false);
		return isJsonEleNull(element) ? null : element.getAsString();
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
	
	public static Map<String, Object> getInputMapFromInputArray(Object[] inputArray) {
        @SuppressWarnings("unchecked")
        Map<String, Object> inputMap = (HashMap<String, Object>) inputArray[1];
        return inputMap;
    }
	
    public static JSONObject getStringAsJSONObject(String jsonString) {
        JSONObject generatedJSONObject = new JSONObject();
        if (HelperMethods.isEmptyString(jsonString)) {
            return null;
        }
        try {
            generatedJSONObject = new JSONObject(jsonString);
            return generatedJSONObject;
        } catch (JSONException e) {
            alert.prepareError(e.getMessage()).log();
            return null;
        }
    }
    
    public static boolean isEmptyString(String testString) {
        if (testString == null || testString.trim().length() == 0) {
            return true;
        }
        return false;
    }

}
