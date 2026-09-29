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
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.StockNewsBackendDelegate;
import com.temenos.infinity.api.wealthOrder.config.WealthAPIServices;
import com.temenos.infinity.api.wealthOrder.constants.OperationName;
import com.temenos.infinity.api.wealthOrder.constants.ServiceId;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;

public class StockNewsBackendDelegateImpl implements StockNewsBackendDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("unchecked")
	@Override
	public Result getStockNews(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {

		Map<String, Object> inputMap = new HashMap<>();
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		Object pageSizeObj = inputParams.get(TemenosConstants.PAGESIZE);
		Object offsetObj = inputParams.get(TemenosConstants.PAGEOFFSET);

		String ricCode = null;
		String pageSizeVal = null;
		String offsetVal = null;
		if (inputParams.get(TemenosConstants.RICCODE) != null) {
			ricCode = inputParams.get(TemenosConstants.RICCODE).toString();
			inputMap.put("instrumentCode", ricCode);
			inputMap.put("languageCode", "en");

			if (pageSizeObj != null) {
				pageSizeVal = inputParams.get(TemenosConstants.PAGESIZE).toString();
			} else {
				pageSizeVal = "0";
			}
			inputMap.put(TemenosConstants.PAGESIZE, pageSizeVal);

			if (offsetObj != null) {
				offsetVal = inputParams.get(TemenosConstants.PAGEOFFSET).toString();
			} else {
				offsetVal = "0";
			}
			inputMap.put(TemenosConstants.PAGEOFFSET, offsetVal);

		} else {
			return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.RICCODE);
		}

		String createResponse = null;
		String serviceName = ServiceId.WEALTHMKTORCHESTRATION;
		String operationName = OperationName.GETSTOCKNEWS;

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
