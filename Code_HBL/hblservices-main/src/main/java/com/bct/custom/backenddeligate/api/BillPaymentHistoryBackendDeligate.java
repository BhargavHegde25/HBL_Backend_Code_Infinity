package com.bct.custom.backenddeligate.api;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.BackendDelegate;
import com.google.gson.JsonObject;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public interface BillPaymentHistoryBackendDeligate  extends BackendDelegate{
	public JSONArray getBillPaymentHistory(String biilerId, Map<String, Object> inputmap, DataControllerRequest dcRequest) throws ApplicationException;

	public JsonObject getBillTransactionById(Map<String, Object> inputParams, DataControllerRequest dcRequest)throws ApplicationException;;
}
