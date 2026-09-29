package com.dbp.reminderengine.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.google.gson.JsonObject;



public interface CustomerDetailsBusinessDelegate extends BusinessDelegate {

	JsonObject getCustomerDetails();

}
