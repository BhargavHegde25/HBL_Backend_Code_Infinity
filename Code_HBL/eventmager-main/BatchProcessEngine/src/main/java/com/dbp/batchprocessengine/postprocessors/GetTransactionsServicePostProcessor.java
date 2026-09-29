package com.dbp.batchprocessengine.postprocessors;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.dbp.batchprocessengine.utils.BatchProcessEngineConstants;
import com.dbp.batchprocessengine.utils.BatchProcessEngineHelperMethods;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.objectserviceutils.EventsDispatcher;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetTransactionsServicePostProcessor implements DataPostProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static int BATCH_SIZE = 50;

	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		JsonArray transactionsArray = new JsonArray();
		JsonObject customParams = new JsonObject();
		String eventSubtype = request.getParameter("objectType");
		if (eventSubtype == null || eventSubtype.equals("") || eventSubtype.equals("null")) {
			eventSubtype = BatchProcessEngineConstants.EVENT_SUBTYPE_TRANSACTIONS;
		}
		try {
			String responsestr = com.konylabs.middleware.dataobject.ResultToJSON.convert(result);
			JsonParser parser = new JsonParser();
			customParams = parser.parse(responsestr).getAsJsonObject();
			transactionsArray = customParams.getAsJsonArray("transactions");
		} catch (Exception e) {
			alert.prepareError("exception occurred", e).log();
		}

		try {
			if (transactionsArray != null && transactionsArray.size() != 0) {
				String batchsizestr = BatchProcessEngineHelperMethods
						.getConfigProperty("BATCHPROCESSENGINE_BATCH_SIZE");
				if (batchsizestr != null && !batchsizestr.equals("")) {
					BATCH_SIZE = Integer.parseInt(batchsizestr);
				}
				String statusId = "SID_EVENT_SUCCESS";
				int count = 0;
				JsonObject transactions = new JsonObject();
				JsonArray eventDataArray = new JsonArray();
				for (int index = 0; index < transactionsArray.size(); index++) {
					eventDataArray.add(transactionsArray.get(index));
					count++;
					if (count == BATCH_SIZE) {
						transactions = new JsonObject();
						transactions.add("transactions", eventDataArray);

						EventsDispatcher.dispatch(request, response, BatchProcessEngineConstants.EVENT_TYPE,
								eventSubtype, "producer", statusId, "", "", transactions);

						eventDataArray = new JsonArray();
						count = 0;
					}
				}
				if (count > 0) {
					transactions = new JsonObject();
					transactions.add("transactions", eventDataArray);

					EventsDispatcher.dispatch(request, response, BatchProcessEngineConstants.EVENT_TYPE, eventSubtype,
							"producer", statusId, "", "", transactions);

				}
			}
		} catch (Exception e) {
			alert.prepareError("excption occurred in transactions post processor", e).log();
		}
		return result;

	}

}
