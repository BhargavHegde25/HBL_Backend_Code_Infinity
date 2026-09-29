package com.kony.dbputilities.util;

import org.apache.commons.lang3.StringUtils;

public class FeatureConfiguration {
	
	public static boolean isProductSpecificFeatueEnabled() {
		String booleanString = EnvironmentConfigurationsHandler
				.getValue("IS_PRODUCT_FEATURE_ENABLED");
		if(StringUtils.isBlank(booleanString)) {
			booleanString = "false";
		}
		return Boolean.parseBoolean(booleanString) ;
	}
	
	public static boolean isAMSRoleFeatureEnabled() {
		String booleanString = EnvironmentConfigurationsHandler
				.getValue("IS_AMS_ROLE_ENABLED");
		if(StringUtils.isBlank(booleanString)) {
			booleanString = "false";
		}
		return Boolean.parseBoolean(booleanString) ;
	}

}
