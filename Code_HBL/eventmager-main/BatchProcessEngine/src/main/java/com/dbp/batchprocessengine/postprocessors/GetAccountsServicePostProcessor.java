package com.dbp.batchprocessengine.postprocessors;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.dbp.batchprocessengine.utils.BatchProcessEngineConstants;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.objectserviceutils.EventsDispatcher;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetAccountsServicePostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

		JsonObject customParams = new JsonObject();
		JsonArray accounts = new JsonArray();
		String eventSubtype = request.getParameter("objectType");
		if (eventSubtype == null || eventSubtype.equals("") || eventSubtype.equals("null")) {
			eventSubtype = BatchProcessEngineConstants.EVENT_SUBTYPE_ACCOUNTS;
		}
		try {
			String statusId = "SID_EVENT_SUCCESS";
			String responsestr = com.konylabs.middleware.dataobject.ResultToJSON.convert(result);
			JsonParser parser = new JsonParser();
			customParams = parser.parse(responsestr).getAsJsonObject();
			accounts = customParams.getAsJsonArray("accounts");
			if (accounts != null && accounts.size() != 0)
				EventsDispatcher.dispatch(request, response, BatchProcessEngineConstants.EVENT_TYPE, eventSubtype,
						"producer", statusId, "", "", customParams);
		} catch (Exception e) {
			alert.prepareError("exception occurred", e).log();
		}
		return result;
	}
}
