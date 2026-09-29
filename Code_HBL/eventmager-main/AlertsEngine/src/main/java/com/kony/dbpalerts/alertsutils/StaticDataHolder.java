package com.kony.dbpalerts.alertsutils;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import com.google.gson.JsonObject;
import com.kony.dbpalerts.alertsprocess.ProcessEvents;
import com.kony.dbpalerts.dbconnectionutils.InitialDbProcess;

public class StaticDataHolder {

	private StaticDataHolder() {
	}

	private static Integer isalertaccountLevel = null;
	private static Map<String, String> appleveldata = null;
	private static Map<String, String> mfinfoappleveldata = null;
	private static Map<String, String> customertypedata = null;
	private static Map<String, Map<String, String>> recipientTypes = null;

	private static String schemaname = null;

	private static ConcurrentMap<String, Map<String, JsonObject>> globalcommunicationdata = null;
	private static ConcurrentMap<String, String> globalcustswitchdata = null;
	private static String externalAlerts = null;

	public static synchronized void verifyAndInitializePreprocessData() {
		appleveldata = ProcessEvents.fetchAppLevelData(appleveldata);
		if (mfinfoappleveldata == null)
			mfinfoappleveldata = ProcessEvents.mfInfoFetchAppLevelData(mfinfoappleveldata);
		customertypedata = ProcessEvents.fetchCustomerTypeData(customertypedata);
		if (recipientTypes == null) {
			recipientTypes = InitialDbProcess.fetchRecipientTypes();
		}
	}

	public static Integer getIsalertaccountLevel() {
		return isalertaccountLevel;
	}

	public static void setIsalertaccountLevel(Integer isalertaccountLevel) {
		StaticDataHolder.isalertaccountLevel = isalertaccountLevel;
	}

	public static Map<String, String> getAppleveldata() {
		return appleveldata;
	}

	public static void setAppleveldata(Map<String, String> appleveldata) {
		StaticDataHolder.appleveldata = appleveldata;
	}

	public static Map<String, String> getMfinfoappleveldata() {
		return mfinfoappleveldata;
	}

	public static void setMfinfoappleveldata(Map<String, String> mfinfoappleveldata) {
		StaticDataHolder.mfinfoappleveldata = mfinfoappleveldata;
	}

	public static Map<String, String> getCustomertypedata() {
		return customertypedata;
	}

	public static void setCustomertypedata(Map<String, String> customertypedata) {
		StaticDataHolder.customertypedata = customertypedata;
	}

	public static String getSchemaname() {
		return schemaname;
	}

	public static void setSchemaname(String schemaname) {
		StaticDataHolder.schemaname = schemaname;
	}

	public static ConcurrentMap<String, Map<String, JsonObject>> getGlobalcommunicationdata() {
		return globalcommunicationdata;
	}

	public static void setGlobalcommunicationdata(
			ConcurrentMap<String, Map<String, JsonObject>> globalcommunicationdata) {
		StaticDataHolder.globalcommunicationdata = globalcommunicationdata;
	}

	public static ConcurrentMap<String, String> getGlobalcustswitchdata() {
		return globalcustswitchdata;
	}

	public static void setGlobalcustswitchdata(ConcurrentMap<String, String> globalcustswitchdata) {
		StaticDataHolder.globalcustswitchdata = globalcustswitchdata;
	}

	public static Map<String, Map<String, String>> getRecipientTypes() {
		return recipientTypes;
	}

	public static String getExternalAlerts() {
		return externalAlerts;
	}

	public static void setExternalAlerts(String externalAlerts) {
		StaticDataHolder.externalAlerts = externalAlerts;

	}

}
