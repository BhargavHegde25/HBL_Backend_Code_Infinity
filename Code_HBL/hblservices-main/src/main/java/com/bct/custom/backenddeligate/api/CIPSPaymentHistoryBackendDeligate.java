package com.bct.custom.backenddeligate.api;

import java.util.Map;

import org.json.JSONArray;

import com.dbp.core.api.BackendDelegate;
import com.google.gson.JsonObject;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public interface CIPSPaymentHistoryBackendDeligate extends BackendDelegate{
	public JSONArray getCIPSPaymentHistory(String biilerId, Map<String, Object> inputmap, DataControllerRequest dcRequest) throws ApplicationException;
	public JsonObject getCIPSTransactionById(Map<String, Object> inputParams, DataControllerRequest dcRequest)throws ApplicationException;;
}

