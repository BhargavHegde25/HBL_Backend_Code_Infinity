package com.kony.kmsinvoke.util;

public class StaticDataHolder {

	private StaticDataHolder() {
	}

	private static String schemaname = null;
	public static String getSchemaname() {
		return schemaname;
	}
	public static void setSchemaname(String schemaname) {
		StaticDataHolder.schemaname = schemaname;
	}

}
