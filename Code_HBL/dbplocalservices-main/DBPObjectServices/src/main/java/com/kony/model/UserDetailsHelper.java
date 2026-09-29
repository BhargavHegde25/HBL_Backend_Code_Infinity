package com.kony.model;

import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.util.AESEncyptor;
import com.kony.memorymgmt.UserDetailsManager;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;

public class UserDetailsHelper {

    private UserDetailsHelper() {
    }

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    public static void saveUserDetailsIntoSession(JsonObject response, FabricRequestManager fabricRequestManager) {
        String userDetailsObject = "records";
        if (null != response && !response.isJsonNull() && response.has(userDetailsObject)) {
        	boolean privacyEnabled = isE2EPrivacyEnabled();
        	if(privacyEnabled) {
        		try {
					response = AESEncyptor.decryptJsonObject(response);
				} catch (Exception e) {}
        	}
            JsonArray userDetails = response.getAsJsonArray(userDetailsObject);
            String userDetailsMap = getUserDetailsMap(userDetails);
            UserDetailsManager detailsManager = new UserDetailsManager(fabricRequestManager);
            detailsManager.saveUserDetailsIntoSession(userDetailsMap);
        }
    }
    
    private static boolean isE2EPrivacyEnabled() {
    	String privacyFlag = "";
    	try {
    		privacyFlag = EnvironmentConfigurationsHandler.getClientAppProperty("E2E_ENCRYPTION_ENABLED");
		} catch (Exception e) {
			privacyFlag = "false";
		}
    	return Boolean.parseBoolean(privacyFlag);
    }

    public static String reloadUserDetailsIntoSession(FabricRequestManager fabricRequestManager) {
        String responseString = null;
        try {
            String res = DBPServiceExecutorBuilder.builder().withObjectId("Login")
                    .withServiceId("Users")
                    .withOperationId("get").build().getResponse();
            JsonObject response = new JsonParser().parse(res).getAsJsonObject();
            String userDetailsObject = "records";
            if (null != response && !response.isJsonNull() && response.has(userDetailsObject)) {
            	boolean privacyEnabled = isE2EPrivacyEnabled();
            	if(privacyEnabled) {
            		try {
    					response = AESEncyptor.decryptJsonObject(response);
    				} catch (Exception e) {}
            	}
                JsonArray userDetails = response.getAsJsonArray(userDetailsObject);
                responseString = getUserDetailsMap(userDetails);
                UserDetailsManager detailsManager = new UserDetailsManager(fabricRequestManager);
                detailsManager.saveUserDetailsIntoSession(responseString);
            }
        } catch (Exception e) {
            alert.prepareError("Error while reloading external accounts:", e).log();
        }
        return responseString;
    }

    private static String getUserDetailsMap(JsonArray userDetails) {
        JsonObject resMap = new JsonObject();
        if (null != userDetails && !userDetails.isJsonNull() && userDetails.size() > 0) {
            JsonElement userDetailsObjEle = userDetails.get(0);
            if (userDetailsObjEle != null && userDetailsObjEle.isJsonObject()) {
                JsonObject userDetailsObj = userDetailsObjEle.getAsJsonObject();
                JsonElement contactDetails = userDetailsObj.get("ContactNumbers");
                JsonElement emailIds = userDetailsObj.get("EmailIds");
                JsonElement addresses = userDetailsObj.get("Addresses");
                JsonElement userName = userDetailsObj.get("userName");
                if (contactDetails != null && !contactDetails.isJsonNull() && contactDetails.isJsonArray()
                        && contactDetails.getAsJsonArray().size() > 0) {
                    resMap.add("ContactNumbers", contactDetails);
                }
                if (emailIds != null && !emailIds.isJsonNull() && emailIds.isJsonArray()
                        && emailIds.getAsJsonArray().size() > 0) {
                    resMap.add("EmailIds", emailIds);
                }
                if (addresses != null && !addresses.isJsonNull() && addresses.isJsonArray()
                        && addresses.getAsJsonArray().size() > 0) {
                    resMap.add("Addresses", addresses);
                }
                if(userName != null && !userName.isJsonNull()) {
                	resMap.add("userName", userName);
                }
            }
        }
        return resMap.toString();
    }

}
