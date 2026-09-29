package com.kony.dbp.batchprocessingalerts;

import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.kony.dbp.batchprocessingengine.helper.HelperMethods;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class CallQueueMaster implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		HashMap<String, Object> inputmap = new HashMap<>();
		HashMap<String, Object> headermap = new HashMap<>();
		try {
			String events = request.getParameter(Constants.EVENTS);
			JsonParser parser = new JsonParser();
			JsonArray eventsarray = parser.parse(events).getAsJsonArray();
			alert.prepareError("eventsarray " + eventsarray).log();
			inputmap.put(Constants.EVENTS, eventsarray);
			ServicesManager servicesmanager = request.getServicesManager();
			inputmap.put(Constants.TOKEN, HelperMethods.deriveToken(servicesmanager, eventsarray.toString()));
			String producer = Constants.RETAIL_AND_BUSINESS_BANKING;
			try {
				producer = HelperMethods.getConfigProperty(Constants.BATCH_ALERT_APP_ID);
			} catch (Exception e) {
			}
			inputmap.put(Constants.PRODUCER, producer);
			return DispatchEvents.callQueueMaster(servicesmanager, inputmap, headermap);
		} catch (Exception e) {
			alert.prepareError("exception occurred ", e).log();
		}
		result.addParam(new Param(Constants.SUCCESS, Constants.FALSE, Constants.STRING));
		return result;

	}

}
