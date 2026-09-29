package com.dbp.reminderengine.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface ProcessAlertsBusinessDelegate extends BusinessDelegate {
	
	JsonArray getCustomerDetails(String customerDetails);

}
