package com.temenos.dbx.product.integration;

import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.URLConstants;

public class IntegrationMappings {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	private Map<String, IntegrationMappings> integrationMap = new HashMap<>();

	private static IntegrationMappings integrationMappings;

	public static IntegrationMappings getInstance() {
		if (integrationMappings == null) {
			integrationMappings = new IntegrationMappings();
		}

		return integrationMappings;
	}

	public String getIntegrationName() {
		try {
			return EnvironmentConfigurationsHandler.getServerProperty(URLConstants.INTEGRATION_NAME);
		} catch (Exception e) {
			alert.prepareError("Exception while fetching INTEGRATION_NAME from server property", e).log();
		}
		return "t24";
	}

	public void addIntegration(String integartion, IntegrationMappings integrationClass) {
		integrationMap.put(integartion, integrationClass);
	}

	public IntegrationMappings getIntegration(String integartion) {
		return integrationMap.get(integartion.toLowerCase());
	}

	public IntegrationMappings getIntegrations() {
		return getIntegration(getIntegrationName());
	}

	public boolean containsIntegration(String integartion) {
		return integrationMap.containsKey(integartion.toLowerCase());
	}

}