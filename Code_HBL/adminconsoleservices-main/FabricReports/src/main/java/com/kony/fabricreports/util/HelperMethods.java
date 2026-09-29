package com.kony.fabricreports.util;

import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;

public class HelperMethods {
	private HelperMethods() {}

	public static String getEnvConfigValue(String key) {
		try {
			return EnvironmentConfigurationsHandler.getServerAppProperty(key);
		} catch (Exception e) {
		}
		return null;
	}

}
