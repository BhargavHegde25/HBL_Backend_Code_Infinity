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
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.PricingDataBackendDelegate;
import com.temenos.infinity.api.wealthOrder.config.WealthAPIServices;
import com.temenos.infinity.api.wealthOrder.constants.OperationName;
import com.temenos.infinity.api.wealthOrder.constants.ServiceId;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;


public class PricingDataBackendDelegateImpl implements PricingDataBackendDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("unchecked")
	@Override
	public Result getPricingData(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		Map<String, Object> headerMap = new HashMap<>();
		Map<String, Object> inputMap = new HashMap<>();
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];

		String ricCode = null;
		if (inputParams.get(TemenosConstants.RICCODE) != null) {
			ricCode = inputParams.get(TemenosConstants.RICCODE).toString();
			inputMap.put("instrumentsCode", ricCode);
			inputMap.put(TemenosConstants.RICCODE, ricCode);

		} else {
			return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.RICCODE);
		}
		inputMap.put("fields",
				"CF_BID:BIDSIZE:CF_ASK:ASKSIZE:CF_VOLUME:CF_OPEN:CF_CLOSE:52WK_HIGH:52WK_LOW:CF_LAST:CF_CURRENCY");
		inputMap.put("objName", "pricingDetails");

		String createResponse = null;
		String serviceName = ServiceId.WEALTHMKTORCHESTRATION;
		String operationName = OperationName.GETPRICINGDATA;

		try {
			createResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(inputMap).withRequestHeaders(headerMap)
					.withDataControllerRequest(request).build().getResponse();
			JSONObject resultJSON = new JSONObject(createResponse);
			return Utilities.constructResultFromJSONObject(resultJSON);
		} catch (Exception e) {
			alert.prepareError("Error while invoking Service - " + WealthAPIServices.WEALTH_GETPRICINGDATA.getOperationName()
					+ "  : " + e).log();
		}

		return null;

	}

}
