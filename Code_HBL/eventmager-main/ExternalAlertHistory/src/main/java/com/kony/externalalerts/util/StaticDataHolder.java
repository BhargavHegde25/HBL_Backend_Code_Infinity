package com.kony.externalalerts.util;

import org.apache.commons.lang3.StringUtils;

public class StaticDataHolder {

	private static String schemaname = null;
	private static String coreType = null;

	public static void initializestaticdata() {
		if (schemaname == null) {
			schemaname = HelperMethods.getConfigProperty(ExternalAlertsConstants.SCHEMANAME);
			if (StringUtils.isBlank(schemaname))
				schemaname = ExternalAlertsConstants.DEFAULTSCHEMANAME;
			setschemaname(schemaname);
		}
		if (coreType == null) {
			coreType = HelperMethods.getConfigProperty(ExternalAlertsConstants.CORETYPE);
			if (StringUtils.isAllBlank(coreType))
				coreType = ExternalAlertsConstants.DEFAULTCORETYPE;
			setCoreType(coreType);
		}

	}

	public static void setschemaname(String schemaname) {
		StaticDataHolder.schemaname = schemaname;

	}

	public static String getSchemaName() {
		return StaticDataHolder.schemaname;
	}

	private static void setCoreType(String coreType) {
		StaticDataHolder.coreType = coreType;

	}

	public static String getCoreType() {
		return StaticDataHolder.coreType;
	}
}
