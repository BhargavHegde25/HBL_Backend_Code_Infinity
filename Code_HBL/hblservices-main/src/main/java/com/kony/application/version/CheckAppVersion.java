package com.kony.application.version;

import java.util.HashMap;

import org.apache.log4j.Logger;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.Param;

public class CheckAppVersion implements JavaService2 {
	private static Logger logger = Logger.getLogger(CheckAppVersion.class);

	@Override
	public Object invoke(String methodId, Object[] svcRequest, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		logger.debug("Start of the service call at the mobilefabric: " + System.currentTimeMillis());
		Result result = new Result();
		if ("AppVersion".equals(methodId)) {
			Param param_opstatus = null;
			try {
				@SuppressWarnings("unchecked")
				HashMap<String, String> clientInputData = (HashMap<String, String>) svcRequest[1];
				String platform = clientInputData.get("platform");
				String clientApVersion = clientInputData.get("appVersion");
				logger.debug("input parameters are" + platform+clientApVersion);
				ServicesManager manager = request.getServicesManager();
    			ConfigurableParametersHelper configurableParametersHelper = manager.getConfigurableParametersHelper(); 
    			String storeURL = "";
    			String minversion="";
    			String latestversion="";
    			String message = "";
    			if (platform != null && platform.equalsIgnoreCase("android"))
    			{
    				storeURL = configurableParametersHelper.getServerProperty("ANDROID_STORE_URL");
    				minversion = configurableParametersHelper.getServerProperty("ANDROID_MIN");
    				latestversion=configurableParametersHelper.getServerProperty("ANDROID_LATEST");
    				
    				String upgradeRes = getUpgradeType(platform, clientApVersion, minversion, latestversion).toString();
    				result.addParam(new Param("upgrade", upgradeRes));
    				result.addParam(new Param("storeURL", storeURL));
    				result.addParam(new Param("latestVersion", latestversion));
    				if(upgradeRes.equalsIgnoreCase("MANDATORY")) {
    					message = "Update of the application is available. Please download the new version by clicking the Upgrade button.";
    				}else if(upgradeRes.equalsIgnoreCase("OPTIONAL")){
    					message = "Update of the application is available. Please download the new version by clicking the Upgrade button. Do you want to proceed?";
    				}
    				result.addParam(new Param("message", message));
    			}
    			else
    			{
    				
    				storeURL = configurableParametersHelper.getServerProperty("IPHONE_STORE_URL");
    				minversion = configurableParametersHelper.getServerProperty("IOS_MIN");
    				latestversion=configurableParametersHelper.getServerProperty("IOS_LATEST");
    				
    				String upgradeRes = getUpgradeType(platform, clientApVersion, minversion, latestversion).toString();
    				result.addParam(new Param("upgrade", upgradeRes));
    				result.addParam(new Param("storeURL", storeURL));
    				result.addParam(new Param("latestVersion", latestversion));
    				if(upgradeRes.equalsIgnoreCase("MANDATORY")) {
    					message = "Update of the application is available. Please download the new version by clicking the Upgrade button.";
    				}else if(upgradeRes.equalsIgnoreCase("OPTIONAL")){
    					message = "Update of the application is available. Please download the new version by clicking the Upgrade button. Do you want to proceed?";
    				}
    				result.addParam(new Param("message", message));
    			}    			
				
				param_opstatus = new Param("opstatus", "0", "int");
				result.addParam(param_opstatus);
			} catch (Exception e) {
				result.addParam(new Param("opstatus", "0", "int"));
				result.addParam(new Param(AppConstants.ERROR_TITLE, "System Error", AppConstants.DATATYPE_STRING));
				result.addParam(new Param(AppConstants.ERROR_MESSAGE,
						"Due to Technical reasons, System is unavailable. Please try again later.",
						AppConstants.DATATYPE_STRING));
			} 
		}
		logger.debug("End of the service call at the middleware: " + System.currentTimeMillis());
		return result;
	}
	
	public static UpgradeType getUpgradeType(String platform, String currentVersion, String MIN, String LATEST) {
		
           return UpgradeChecker.checkUpgrade(platform, currentVersion, MIN, LATEST);
   }
}
