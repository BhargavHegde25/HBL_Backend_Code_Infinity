package com.temenos.infinity.api.wealth.backenddelegate.impl;

import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealth.backenddelegate.api.InstrumentDetailsBackendDelegate;
import com.temenos.infinity.api.wealth.constants.OperationName;
import com.temenos.infinity.api.wealth.constants.ServiceId;
import com.temenos.infinity.api.wealth.config.PortfolioWealthAPIServices;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;

public class InstrumentDetailsBackendDelegateImpl implements InstrumentDetailsBackendDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("unchecked")
	@Override
	public Result getInstrumentDetails(String methodID, Object[] inputArray, DataControllerRequest request,
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
		inputMap.put("objName", "instrumentDetails");
		inputMap.put("fields",
				"CF_CURRENCY:CF_EXCHNG:ISIN_CODE:TRADE_DATE:CF_NAME:PRCTCK_1:BID:ASK:TRDPRC_1:PCTCHNG:CF_CLOSE:CF_NETCHNG:CF_DATE:CF_TIME");

		String createResponse = null;
		String serviceName = ServiceId.WEALTHMKTORCHESTRATION;
		String operationName = OperationName.GETINSTRUMENTDETAILS;

		try {
			createResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(inputMap).withRequestHeaders(headerMap)
					.withDataControllerRequest(request).build().getResponse();
			JSONObject resultJSON = new JSONObject(createResponse);
			return Utilities.constructResultFromJSONObject(resultJSON);
		} catch (Exception e) {
			alert.prepareError("Error while invoking Service - "
					+ PortfolioWealthAPIServices.WEALTH_GETINSTRUMENTREFEDETAILS.getOperationName() + "  : " + e).log();
		}

		return null;

	}

}
