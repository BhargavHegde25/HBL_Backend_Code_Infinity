package com.kony.adminconsole.utilities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONObject;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.adminconsole.commons.handler.EnvironmentConfigurationsHandler;
import com.kony.adminconsole.commons.utils.MemoryManager;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.handler.PermissionHandler;
import com.konylabs.middleware.controller.DataControllerRequest;

public class CacheUtil {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");

	public static Set<String> getLoggedInUserLegalEntities(DataControllerRequest request) {
		Set<String> spotlightUserEntities = new HashSet();
		try {
			String userid = null;
			if (request.getServicesManager() != null && request.getServicesManager().getIdentityHandler() != null) {
				userid = LoggedInUserHandler.getUserDetails(request).getId();
			}
			String legalEntityData = "";
			if (StringUtils.isNotBlank(userid)) {
				legalEntityData = (String) MemoryManager.getFromCache("legalEntityToRoleMapping_" + userid);
			}
			if (StringUtils.isBlank(legalEntityData)) {
				String roles = LoggedInUserHandler.getUserDetails(request.getServicesManager()).getRoleId();
				JSONObject responseObject = PermissionHandler.getRolesGrantedPermissionsWithLEInfo(roles, request);
				legalEntityData = responseObject.getString("legalEntityToRoleMapping");
				if (StringUtils.isNotBlank(userid)) {
					int cacheTime = 0;
					try {
						cacheTime = Integer.parseInt(EnvironmentConfigurationsHandler
								.getServerAppPropertyValue("DBP_CACHE_EXPIRE_TIME", request));
					} catch (Exception e) {
						cacheTime = 20 * 60;
					}
					MemoryManager.saveIntoCache("legalEntityToRoleMapping_" + userid,
							responseObject.getString("legalEntityToRoleMapping"), cacheTime);
				}
			}
			if (StringUtils.isNotBlank(legalEntityData)) {
				JsonElement legalEntityDataJsonEle = new JsonParser().parse(legalEntityData);
				if (legalEntityDataJsonEle != null && legalEntityDataJsonEle.isJsonObject()) {
					JsonObject legalEntityDataJsonObj = legalEntityDataJsonEle.getAsJsonObject();
					Set<Map.Entry<String, JsonElement>> elements = legalEntityDataJsonObj.entrySet();
					for (Map.Entry<String, JsonElement> legalEntity : elements) {
						spotlightUserEntities.add(legalEntity.getKey());
					}
				}
			}
		}

		catch (Exception e) {
			alert.prepareError("Error in getting legal entity from cache", e).log();
		}
		return spotlightUserEntities;
	}
}
