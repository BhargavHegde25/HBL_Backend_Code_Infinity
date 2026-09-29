package com.kony.kmsinvoke.businessdelegate.api;

import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.google.gson.JsonObject;

public interface NotificationBusinessDelegate extends BusinessDelegate {
	
	JsonObject sendNotification(Map<String,Object> inputparams);

}
