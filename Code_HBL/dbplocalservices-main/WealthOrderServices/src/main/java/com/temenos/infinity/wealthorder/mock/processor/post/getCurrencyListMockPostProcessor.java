package com.temenos.infinity.wealthorder.mock.processor.post;

import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthOrder.config.WealthAPIServices;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;

public class getCurrencyListMockPostProcessor implements DataPostProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		diagnostic.prepareDebug("==========> getCurrencyListMockPostProcessor Mock - Entered").log();
		try {
			JSONObject resultJson = new JSONObject();
			String currencyCode[] = { "AED", "BHD", "BRL", "CAD", "CHF", "CNY", "HKD", "KWD", "SGD", "YEN" };
			String currencyVal[] = { "United Arab Emirates Dirham", "Bahrain Dinar", "Brazil Real", "Canada Dollar",
					"Switzerland Franc", "Chinese Yuan", "Hong Kong Dollar", "Kuwait Dinar", "Singapore Dollar",
					"Japanese Yen" };
			JSONArray addCurrency = new JSONArray();

			for (int i = 0; i < currencyCode.length; i++) {
				JSONObject responseObj = new JSONObject();
				responseObj.put("CurrencyCode", currencyCode[i]);
				responseObj.put("CurrencyValue", currencyVal[i]);
				addCurrency.put(responseObj);
			}
			resultJson.put("AddCurrency", addCurrency);
			Result final_result = Utilities.constructResultFromJSONObject(resultJson);
			final_result.addOpstatusParam("0");
			final_result.addHttpStatusCodeParam("200");
			final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			result.appendResult(final_result);
		} catch (Exception e) {
			e.getMessage();
			alert.prepareError("==========> getCurrencyListMockPostProcessor Mock - Error: " + e.getMessage()).log();
		}
		diagnostic.prepareDebug("==========> getCurrencyListMockPostProcessor Mock -Exiting with success").log();
		return result;
	}
}
