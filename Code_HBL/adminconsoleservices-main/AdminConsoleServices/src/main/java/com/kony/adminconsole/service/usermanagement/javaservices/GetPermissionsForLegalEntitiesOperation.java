package com.kony.adminconsole.service.usermanagement.javaservices;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.kony.adminconsole.commons.handler.EnvironmentConfigurationsHandler;
import com.kony.adminconsole.commons.utils.MemoryManager;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.handler.PermissionHandler;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class GetPermissionsForLegalEntitiesOperation implements JavaService2{
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		
		Result result = new Result();
		String roles = LoggedInUserHandler.getUserDetails(request.getServicesManager()).getRoleId();		
		JSONObject responseObject = PermissionHandler.getRolesGrantedPermissionsWithLEInfo(roles, request);
		
		try {
			String userid= null;
			if (request.getServicesManager() != null && request.getServicesManager().getIdentityHandler() != null) {
				userid = LoggedInUserHandler.getUserDetails(request).getId();
            }
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
    	 catch(Exception e) {
    		 alert.prepareError("Exception occured while caching the legalEntityToRoleMapping details").log();
    	 }
		
		result.addParam(new Param("legalEntityToRoleMapping", responseObject.getString("legalEntityToRoleMapping")));
		result.addParam(new Param("permissions", responseObject.getString("permissions")));
		return result;
	}
	
	
	
}
