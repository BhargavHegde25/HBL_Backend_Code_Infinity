package com.kony.kmsinvoke.businessdelegate.api;

import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.google.gson.JsonObject;

public interface LogNotificationStatusBusinessDelegate extends BusinessDelegate {
	Map<String, Object> insertToAlertHistory(String channel,JsonObject inputParamsObject, JsonObject kmsResponse, JsonObject logparams);

}
