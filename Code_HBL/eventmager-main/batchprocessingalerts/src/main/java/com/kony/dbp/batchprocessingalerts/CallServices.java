package com.kony.dbp.batchprocessingalerts;

import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.konylabs.middleware.api.OperationData;
import com.konylabs.middleware.api.ServiceRequest;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.registry.AppRegistryException;

public class CallServices {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");

	private CallServices() {

	}

	public static JsonArray callSubscriberService(DataControllerRequest request, Map<String, Object> inputmap,
			Map<String, Object> headermap) {
		String result2 = "";
		JsonArray response = new JsonArray();
		JsonObject errresponse = new JsonObject();
		try {

			OperationData operationData = request.getServicesManager().getOperationDataBuilder()
					.withServiceId(Constants.BATCHPROCESSINGOBJECTS).withVersion(Constants.BPOVERSION)
					.withObjectId(Constants.BPOOBJECTID).withOperationId(Constants.BPOOPERATIONID).build();
			ServiceRequest serviceRequest = request.getServicesManager().getRequestBuilder(operationData)
					.withInputs(inputmap).withHeaders(headermap).build();

			result2 = serviceRequest.invokeServiceAndGetJson();
			response = new JsonParser().parse(result2).getAsJsonObject().get(Constants.ALERTSUBTYPESPARAM).getAsJsonArray();
		} catch (AppRegistryException arex) {
			alert.prepareError("arex=", arex).log();
			errresponse.addProperty("AppRegistryException: ", arex.toString());
			response.add(errresponse);
		} catch (Exception ex) {
			errresponse.addProperty("Exception while fetching from subscriber service: ", ex.toString());
			response.add(errresponse);
		}

		return response;
	}
}
