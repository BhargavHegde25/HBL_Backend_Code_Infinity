package com.temenos.infinity.api.chequemanagement.utils;

import java.util.HashMap;

import com.konylabs.middleware.dataobject.ResultToJSON;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbx.util.CommonUtils;
import com.kony.http.HttpCallException;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;

public class ChequeDetailsMockOperation implements ObjectProcessorTask {

	public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager) throws Exception {
        try {
            String CHEQUE_BACKEND = EnvironmentConfigurationsHandler.getServerProperty("CHEQUE_BACKEND");
            if("STUB".equalsIgnoreCase(CHEQUE_BACKEND)||"SRMS_MOCK".equalsIgnoreCase(CHEQUE_BACKEND)) {
                HashMap<String, Object> headerParams = new HashMap<String, Object>();
        		HashMap<String, Object> inputParams = new HashMap<String, Object>();
        		JsonObject requestPayload = new JsonObject();
        		JsonObject ChequeSupplement = new JsonObject();
        		JsonArray result= new JsonArray();
           		requestPayload.addProperty("checkNumber","OCB2225050O0O");
                requestPayload.addProperty("chequeStatus","Issued");
                requestPayload.addProperty("payeeName","JohnAqua");
                requestPayload.addProperty("issueDate","10/10/2022");
                requestPayload.addProperty("amount","1.00");
                requestPayload.addProperty("opstatus", Integer.parseInt("0"));
                requestPayload.addProperty("httpStatusCode", Integer.parseInt("200"));
                 result.add(requestPayload);
                ChequeSupplement.add("ChequeSupplements",result);
                fabricResponseManager.getHeadersHandler().addHeader("Content-Type", "application/json");
               fabricResponseManager.getPayloadHandler().updatePayloadAsJson(ChequeSupplement);
                return false;
            }
        } catch (Exception e){
            return false;
        }
        return true;
    }
}
