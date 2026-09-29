package com.kony.adminconsole.service.usermanagement.javaservices;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.kony.adminconsole.commons.handler.EnvironmentConfigurationsHandler;
import com.kony.adminconsole.commons.utils.MemoryManager;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.core.security.UserDetailsBean;
import com.kony.adminconsole.handler.PermissionHandler;
import com.kony.adminconsole.service.authmodule.APICustomIdentityService;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetPermissionsLoggedInUser implements JavaService2 {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	private final String CACHE_CONSTANT = "SPOTLIGHT_PERMISSIONS_";

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		
		Result result = new Result();
		UserDetailsBean userBean = LoggedInUserHandler.getUserDetails(request.getServicesManager());
		String userId = userBean.getUserId();
		String roles = userBean.getRoleId();
		String permissions = "";
		
		if(APICustomIdentityService.API_USER_ID.
				equalsIgnoreCase(userBean.getUserId())) {
			diagnostic.prepareDebug("... loggedin user is API user...").log();
			JSONArray permissionsArray = new JSONArray();
			permissionsArray.put("API_ACCESS");
			permissions = permissionsArray.toString();
		} else {
			diagnostic.prepareDebug("... loggedin user is an internal user...").log();
			if(StringUtils.isNotBlank(userId)) {
				permissions = (String) MemoryManager.getFromCache(CACHE_CONSTANT+userId);
			}
			
			if(StringUtils.isBlank(permissions)) {
				diagnostic.prepareDebug("... permissions not found in cache...").log();
				JSONObject responseObject = PermissionHandler
						.getRolesGrantedPermissionsWithLEInfo(roles, request);
				permissions = responseObject.getString("permissions");
				
				int cacheTime = 0;
				try {
					cacheTime = Integer.parseInt(EnvironmentConfigurationsHandler
							.getServerAppPropertyValue("PERMISSION_CACHE_EXPIRE_TIME", request));
				} catch (Exception e) {
					cacheTime = 20 * 60;
				}
				MemoryManager.saveIntoCache(CACHE_CONSTANT+userId, permissions, cacheTime);
				diagnostic.prepareDebug("... permissions saved in cache...").log();
			}
		}
		diagnostic.prepareDebug("... permissions returned..."+permissions).log();
		result.addStringParam("permissions", permissions);
		return result;
	}

}
