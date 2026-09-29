package com.kony.adminconsole.service.customerdatamanagement.businessdelegate.api;

import java.util.Map;

import org.json.JSONObject;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.error.DBPApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;

public interface CustomerDataManagementBusinessDelegate  extends BusinessDelegate {
	public JSONObject getSDPReport(Map<String, Object> postParametersMap,DataControllerRequest request)
            throws DBPApplicationException;

	public JSONObject triggerErasure(Map<String, Object> postParametersMap,DataControllerRequest request)
			throws DBPApplicationException;

	public JSONObject updateCustomerErasureStatus(Map<String, Object> postParametersMap,DataControllerRequest request)
			throws DBPApplicationException;

	public JSONObject applicationPurge(Map<String, Object> postParametersMap,DataControllerRequest request)
			throws DBPApplicationException;
}
