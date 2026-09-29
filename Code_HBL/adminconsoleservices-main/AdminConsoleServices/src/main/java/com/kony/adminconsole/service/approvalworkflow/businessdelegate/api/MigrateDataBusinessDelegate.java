package com.kony.adminconsole.service.approvalworkflow.businessdelegate.api;

import java.util.Map;

import org.json.JSONObject;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.error.DBPApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;

public interface MigrateDataBusinessDelegate extends BusinessDelegate{

    public JSONObject moveDataRecords(Map<String, Object> postParametersMap,DataControllerRequest request)
            throws DBPApplicationException;
}