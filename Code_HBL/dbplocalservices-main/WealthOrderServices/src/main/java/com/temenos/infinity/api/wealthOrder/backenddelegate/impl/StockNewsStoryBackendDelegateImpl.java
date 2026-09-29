package com.temenos.infinity.api.wealthOrder.backenddelegate.impl;

import java.util.HashMap;
import java.util.Map;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONObject;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.StockNewsStoryBackendDelegate;
import com.temenos.infinity.api.wealthOrder.config.WealthAPIServices;
import com.temenos.infinity.api.wealthOrder.constants.OperationName;
import com.temenos.infinity.api.wealthOrder.constants.ServiceId;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

public class StockNewsStoryBackendDelegateImpl implements StockNewsStoryBackendDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("unchecked")
	@Override
	public Result getStockNewsStory(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		Map<String, Object> inputMap = new HashMap<>();
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		Object storyIdObj = inputParams.get(TemenosConstants.STORYID);

		String storyId = null;
		if (storyIdObj != null) {
			storyId = inputParams.get(TemenosConstants.STORYID).toString();
			inputMap.put(TemenosConstants.STORYID, storyId);
		} else {
			alert.prepareError("Error:Invalid input. Mandatory fields not given").log();
			JSONObject resultJSON = new JSONObject();
			resultJSON.put("status", "Failure");
			resultJSON.put("error", "Invalid Input! StoryID is mandatory.");
			return Utilities.constructResultFromJSONObject(resultJSON);
		}

		String createResponse = null;
		String serviceName = ServiceId.WEALTHMKTORCHESTRATION;
		String operationName = OperationName.GETSTOCKNEWSSTORY;

		try {
			createResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(inputMap).withRequestHeaders(headersMap)
					.withDataControllerRequest(request).build().getResponse();
			JSONObject resultJSON = new JSONObject(createResponse);
			return Utilities.constructResultFromJSONObject(resultJSON);
		} catch (Exception e) {
			alert.prepareError("Error while invoking Service - " + WealthAPIServices.WEALTH_GETSTOCKNEWS.getOperationName()
			+ "  : " + e).log();
		}

		return null;

	}

}
