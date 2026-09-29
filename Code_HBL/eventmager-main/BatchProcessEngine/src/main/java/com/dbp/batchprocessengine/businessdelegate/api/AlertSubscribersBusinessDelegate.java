package com.dbp.batchprocessengine.businessdelegate.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface AlertSubscribersBusinessDelegate {

	JsonArray getSubscribers(String alerttypes, String corecustids);
}
