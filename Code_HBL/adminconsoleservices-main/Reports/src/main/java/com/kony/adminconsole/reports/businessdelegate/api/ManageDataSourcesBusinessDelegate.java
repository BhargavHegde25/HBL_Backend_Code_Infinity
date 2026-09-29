package com.kony.adminconsole.reports.businessdelegate.api;

import org.json.JSONObject;

import com.dbp.core.api.BusinessDelegate;

public interface ManageDataSourcesBusinessDelegate extends BusinessDelegate {
	JSONObject getDataSourcesList(String createdBy,String authToken) throws Exception;
	
}
