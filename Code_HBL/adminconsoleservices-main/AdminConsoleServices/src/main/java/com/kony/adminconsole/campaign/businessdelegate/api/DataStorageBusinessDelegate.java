package com.kony.adminconsole.campaign.businessdelegate.api;

import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.error.DBPApplicationException;
import com.kony.adminconsole.exception.ApplicationException;
import com.konylabs.middleware.exceptions.MiddlewareException;

public interface DataStorageBusinessDelegate extends BusinessDelegate {
	
	public JSONObject getCustomerApplications(JSONObject payload, Boolean isSME, String deploymentPlatform) throws ApplicationException, JSONException, DBPApplicationException, MiddlewareException;

	public JSONObject getEntityItemEntry(String entityItemId) throws ApplicationException, JSONException, DBPApplicationException, MiddlewareException;

	public JSONObject getEntityItemByKeyNameVersionType(String entityDefinitionCode, String key,  String name,  String version, String type) throws ApplicationException, JSONException, DBPApplicationException, MiddlewareException, Exception;
	
}
