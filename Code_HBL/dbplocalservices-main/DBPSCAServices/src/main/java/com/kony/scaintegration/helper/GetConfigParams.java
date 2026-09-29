package com.kony.scaintegration.helper;

public class GetConfigParams {
	private GetConfigParams() {
	}

	private static String isScaEnabled = null;
	private static String schemaname = null;

	public static void setSchemaName(String schemaname) {
		GetConfigParams.schemaname = schemaname;
	}

	public static String getSchemaName() {
		return schemaname;
	}

	public static void setIsScaEnabled(String isScaEnabled) {

		GetConfigParams.isScaEnabled = isScaEnabled;
	}

	public static String getIsScaEnabled() {
		return isScaEnabled;
	}

}
