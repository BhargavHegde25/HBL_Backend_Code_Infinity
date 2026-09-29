package com.kony.kmsinvoke.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.google.gson.JsonObject;

public interface SendPushNotificationBusinessDelegate extends BusinessDelegate {

	JsonObject sendPushNotification(JsonObject inputparams);

}
