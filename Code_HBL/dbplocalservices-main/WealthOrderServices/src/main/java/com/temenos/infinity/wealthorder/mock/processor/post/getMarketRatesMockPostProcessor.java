package com.temenos.infinity.wealthorder.mock.processor.post;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

public class getMarketRatesMockPostProcessor implements DataPostProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		diagnostic.prepareDebug("==========> getMarketRatesMockPostProcessor Mock - Entered").log();
		try {
			JSONObject responseOne = new JSONObject();
			String currencyPair = request.getParameter(TemenosConstants.CURRENCYPAIRS);
			if (currencyPair.equalsIgnoreCase("EURUSD")) {
				responseOne.put("marketRate", "1.16");
			} else if (currencyPair.equalsIgnoreCase("USDEUR")) {
				responseOne.put("marketRate", "0.85");
			} else if (currencyPair.equalsIgnoreCase("USDGBP")) {
				responseOne.put("marketRate", "0.78");
			} else if (currencyPair.equalsIgnoreCase("GBPUSD")) {
				responseOne.put("marketRate", "1.30");
			} else {
				responseOne.put("marketRate", "1.12");
			}

			// responseOne.put("status", "success");
			Result final_result = Utilities.constructResultFromJSONObject(responseOne);
			final_result.addOpstatusParam("0");
			final_result.addHttpStatusCodeParam("200");
			final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			result.appendResult(final_result);
			// return responseOne;

		} catch (Exception e) {
			e.getMessage();
			alert.prepareError("==========> getMarketRatesMockPostProcessor Mock - Error: " + e.getMessage()).log();
		}
		diagnostic.prepareDebug("==========> getMarketRatesMockPostProcessor Mock - Exiting with success").log();
		return result;
	}
}
