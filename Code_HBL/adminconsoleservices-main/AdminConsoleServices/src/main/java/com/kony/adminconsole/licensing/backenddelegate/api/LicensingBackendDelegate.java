package com.kony.adminconsole.licensing.backenddelegate.api;

import java.util.ArrayList;
import org.json.JSONObject;

import com.dbp.core.api.BackendDelegate;


public interface LicensingBackendDelegate extends BackendDelegate{
	
	public ArrayList<String> getUserActions(String customerId, String legalEntityId);

	public JSONObject getCustomerIds();

	public JSONObject getRoleIds();

	public JSONObject getServiceDefinitions(ArrayList<String> roles);

	public JSONObject getFeaturesforServiceDefinitions(ArrayList<String> servicedefinition);

	public JSONObject getTypeidForFeature(ArrayList<String> featureIds);


}
