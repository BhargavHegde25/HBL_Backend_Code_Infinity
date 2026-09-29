package com.kony.dbx.util;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.registry.AppRegistryException;

public class EnvironmentConfigurationsHandler {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	  
	  public static String getValue(String key, DataControllerRequest requestInstance)
	  {
	    try
	    {
	      ServicesManager serviceManager = requestInstance.getServicesManager();
	      ConfigurableParametersHelper configurableParametersHelper = serviceManager.getConfigurableParametersHelper();
	      return configurableParametersHelper.getServerProperty(key);
	    }
	    catch (AppRegistryException are)
	    {
	    }
	    return null;
	  }
	  
	  public static String getValue(String key, FabricRequestManager requestManager)
	  {
	    ServicesManager serviceManager = requestManager.getServicesManager();
	    ConfigurableParametersHelper configurableParametersHelper = serviceManager.getConfigurableParametersHelper();
	    String requiredURL = configurableParametersHelper.getServerProperty(key);
	    return requiredURL;
	  }
}
