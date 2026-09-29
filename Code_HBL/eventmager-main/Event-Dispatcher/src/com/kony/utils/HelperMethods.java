package com.kony.utils;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.MemoryManager;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.konylabs.middleware.api.ServicesManagerHelper;
import com.konylabs.middleware.api.processor.IdentityHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.ehcache.ResultCache;

public class HelperMethods {

	private HelperMethods() {

	}

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");


	public static String getCustomerSessionUrl(FabricRequestManager requestManager) {
		return EnvironmentConfigurationsHandler.getValue(URLConstants.CUSTOMER_SESSION_URL, requestManager);

	}

	public static String getCustomerIdFromSession(FabricRequestManager requestManager) {
		return requestManager.getServicesManager().getIdentityHandler().getUserId();
	}

	public static String getStringFromJsonObject(JsonObject object, String key, boolean required) {
		JsonElement element = getElementFromJsonObject(object, key, required);
		return element == null ? null : element.getAsString();
	}

	public static JsonElement getElementFromJsonObject(JsonObject object, String key, boolean required) {
		JsonElement element = object.get(key);
		if ((element == null) && (required)) {
			throw new IllegalArgumentException("Required attribute '" + key + "' was not present");
		}
		return element;
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

	public static JsonObject convertDcRequestToJson(DataControllerRequest requestManager) {

		JsonObject requestData = new JsonObject();
		try {
			if (requestManager != null) {
				Iterator<String> parameterNames = requestManager.getParameterNames();
				while (parameterNames.hasNext()) {
					String parameterName = parameterNames.next();
					requestData.addProperty(parameterName, requestManager.getParameter(parameterName));
				}
			}
		} catch (Exception e) {
			alert.prepareError(URLConstants.EXCEPTION, e).log();
		}
		return requestData;
	}

	public static String callInternalService(Map<String, Object> inputparams, String serviceid, String operationid,
			String objectid) {
		if (serviceid == null || operationid == null)
			return null;
		try {
			DBPServiceExecutorBuilder db = DBPServiceExecutorBuilder.builder().withServiceId(serviceid)
					.withOperationId(operationid);
			if (objectid != null)
				db = db.withObjectId(objectid);
			return db.withRequestParameters(inputparams).build().getResponse();
		} catch (Exception e) {
			alert.prepareError(URLConstants.EXCEPTION, e).log();

		}
		return null;

	}

	public static Result returnSuccess(Result res) {
		res.addParam(new Param(URLConstants.SUCCESS, URLConstants.TRUE, URLConstants.STRING));
		return res;
	}

	public static Result result(Result res, ErrorCodeEnum errorEnum) {
		res.addParam(new Param(URLConstants.DBPERRCODE, errorEnum.getErrCode(), URLConstants.STRING));
		res.addParam(new Param(URLConstants.DBPERRMSG, errorEnum.getErrMsg(), URLConstants.STRING));
		return res;

	}

	public static String getParamFromIToken(String authkey, String providerUserId) {
		TokenUtils tokenobj = new TokenUtils(authkey);
		return tokenobj.getValue(providerUserId);
	}
	public static String getCompanyId(DataControllerRequest request) {
        try {
            IdentityHandler identityHandler = request.getServicesManager().getIdentityHandler();
            Map<String, Object> userAttributes = identityHandler.getUserAttributes();
            String companyId = null;
            if(userAttributes != null && userAttributes.size() >0) {
                companyId = (String)userAttributes.get("legalEntityId");
            }else {
                companyId = (String)userAttributes.get("companyId");
            }
            return companyId;
        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
        }
        return "";
    }
	
	public static String getLoginEntityId(DataControllerRequest request) {
        try {
            IdentityHandler identityHandler = request.getServicesManager().getIdentityHandler();
            Map<String, Object> userAttributes = identityHandler.getUserAttributes();
            String companyId = null;
            if(userAttributes != null && userAttributes.size() >0) {
                companyId = (String)userAttributes.get("defaultLegalEntity");
                if(companyId==null || "".equals(companyId)) {
                	companyId = (String)userAttributes.get("homeLegalEntity");
                }
            }
            return companyId;
        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
        }
        return "";
    }
	public static String getLoginEntityId(FabricRequestManager requestManager) {
		 try {
	            IdentityHandler identityHandler = requestManager.getServicesManager().getIdentityHandler();
	            Map<String, Object> userAttributes = identityHandler.getUserAttributes();
	            String companyId = null;
	            if(userAttributes != null && userAttributes.size() >0) {
	                companyId = (String)userAttributes.get("defaultLegalEntity");
	                if(companyId==null || "".equals(companyId)) {
	                	companyId = (String)userAttributes.get("homeLegalEntity");
	                }
	            }
	            return companyId;
	        } catch (Exception e) {
	            alert.prepareError(e.toString()).log();
	        }
	        return "";
	    }
	
	public static String getCompanyId(FabricRequestManager requestManager) {
        try {
            IdentityHandler identityHandler = requestManager.getServicesManager().getIdentityHandler();
            Map<String, Object> userAttributes = identityHandler.getUserAttributes();
            String companyId = null;
            if(userAttributes != null && userAttributes.size() >0) {
                companyId = (String)userAttributes.get("legalEntityId");
            }else {
                companyId = (String)userAttributes.get("companyId");
            }
            return companyId;
        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
        }
        return "";
    
	}
	public static Object getFromCache(String key) {
        ResultCache resultCache = null;
        try {
            resultCache = ServicesManagerHelper.getServicesManager().getResultCache();
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching ResultCache instance from Services Manager API", e).log();
        }
        if (resultCache != null && StringUtils.isNotBlank(key)) {
            return resultCache.retrieveFromCache(key);
        }
 
        return new Object();
 
    }
	
	public static String getDecyptedClientIpAddressFromCache(DataControllerRequest request) throws Exception {
		try {
			String sessionToken = request.getServicesManager().getIdentityHandler().getSecurityAttributes().get("session_token")
					.toString();
			String key = sessionToken + "_" + URLConstants.CLIENT_IP;
			String ipAddress = ""+(String)MemoryManager.getFromCache(key);
			return ipAddress;
		}
		catch(Exception e) {
			return "";
		}
	}
	public static String getDecyptedClientIpAddressFromCache(FabricRequestManager requestManager) throws Exception {
		String sessionToken = requestManager.getServicesManager().getIdentityHandler().getSecurityAttributes().get("session_token")
				.toString();
		String key = sessionToken + "_" + URLConstants.CLIENT_IP;
		String ipAddress = MemoryManager.getFromCache(key).toString();
		return ipAddress;
	}
	public static String decryptClientIp(String encryptedIp) {
		StringBuilder decryptedId = new StringBuilder();
		String key = "";
		try {
			key = com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler.getClientAppProperty("CLIENT_IP_KEY");	
		}  catch (Exception e) {}
		if(StringUtils.isBlank(key)) {
			key = "255.0.255.0";
		}
		String[] encryptedOctets = encryptedIp.split("\\.");
		String encryptedBinary = "";
		for(String octet : encryptedOctets) {
			encryptedBinary += Integer.toBinaryString(Integer.parseInt(octet)) + " ";
		}
		
		String[] keyOctets = key.split("\\.");
		String keyBinary = "";
		for(String octet : keyOctets) {
			keyBinary += Integer.toBinaryString(Integer.parseInt(octet)) + " ";
		}
		String[] encryptedBits = encryptedBinary.split(" ");
		String[] keyBits = keyBinary.split(" ");
		
		for(int i = 0; i <  encryptedBits.length; i++) {
			decryptedId.append(Integer.parseInt(encryptedBits[i], 2) ^ Integer.parseInt(keyBits[i], 2));
			decryptedId.append(".");
		}
		
		return decryptedId.substring(0, decryptedId.length() - 1);
	}
}
