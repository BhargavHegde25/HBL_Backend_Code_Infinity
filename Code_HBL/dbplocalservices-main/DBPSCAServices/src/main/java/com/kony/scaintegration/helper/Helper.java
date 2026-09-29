package com.kony.scaintegration.helper;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;

public class Helper {
	private Helper() {
	}

	public static String getConfigProperty(String key) throws Exception {
		return EnvironmentConfigurationsHandler.getServerAppProperty(key);
	}

	public static String getCurrentTimeStamp() {
		return getFormattedTimeStamp(new Date(), null);
	}

	public static String getFormattedTimeStamp(Date dt, String format) {
		String dtFormat = "yyyy-MM-dd'T'HH:mm:ss";
		if (StringUtils.isNotBlank(format)) {
			dtFormat = format;
		}
		SimpleDateFormat formatter = new SimpleDateFormat(dtFormat);
		return formatter.format(dt);
	}

	public static String replaceSchemaName(String operationid, String schemaname) {
		if (operationid == null)
			return operationid;
		if (operationid.contains("{schema_name}"))
			operationid = operationid.replace("{schema_name}", schemaname);
		return operationid;

	}

	public static boolean isScaVerify(JsonObject requestPayload) {
		JsonElement mfaElement = requestPayload.get("MFAAttributes");
		if (!isJsonEleNull(mfaElement) && !mfaElement.isJsonNull()) {
			JsonObject mfaAttributes = null;
			if (mfaElement.isJsonObject()) {
				mfaAttributes = mfaElement.getAsJsonObject();
			} else {
				JsonParser parser = new JsonParser();
				mfaAttributes = parser.parse(mfaElement.getAsString()).getAsJsonObject();
			}
			if (mfaAttributes.has(Constants.SERVICEKEY)) {
				String serviceKey = mfaAttributes.get(Constants.SERVICEKEY).getAsString();
				return StringUtils.isNotBlank(serviceKey);
			}
		}
		return false;
	}

	public static boolean isJsonEleNull(JsonElement ele) {
		return null == ele || ele.isJsonNull();
	}

}
