package com.temenos.infinity.wealth.refinitiv.processor.pre;

import java.util.HashMap;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

public class GetDailyMarketRefinitivPreProcessor implements DataPreProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("rawtypes")
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			diagnostic.prepareDebug("==========> GetDailyMarketRefinitivPreProcessor Refinitiv - Entered ").log();
			String wealthCore = EnvironmentConfigurationsHandler.getValue(TemenosConstants.WEALTH_CORE, request);

			if (wealthCore != null
					&& (wealthCore.equalsIgnoreCase("TAP,Refinitiv") || wealthCore.equalsIgnoreCase("T24,Refinitiv"))) {

				Object marketIndexObj = TemenosConstants.MARKETINDEX;
				String marketIndex = null;
				String inputValue = "";

				if (marketIndexObj != null && marketIndexObj.toString().trim().length() > 0) {
					marketIndex = (TemenosConstants.MARKETINDEX).toString();
					String marketsArr[] = marketIndex.toUpperCase().trim().split("\\s*,\\s*");
					for (String marketsVal : marketsArr) {
						String forQuotes = "\"";
						inputValue = inputValue.concat(forQuotes.concat(marketsVal.concat("\",")));
					}
					inputValue = inputValue.substring(0, inputValue.length() - 1);
					inputMap.put(TemenosConstants.MARKETS, inputValue);
					request.addRequestParam_(TemenosConstants.MARKETS, inputValue);
				}
				diagnostic.prepareDebug("==========> GetDailyMarketRefinitivPreProcessor Refinitiv - Exited ").log();
				return true;

			} else {
				diagnostic.prepareDebug("==========> GetDailyMarketRefinitivPreProcessor Refinitiv - Exiting without Token Generation ").log();
				result.addOpstatusParam("0");
				result.addHttpStatusCodeParam("200");
				result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				return false;
			}

		} catch (Exception e) {
			alert.prepareError("==========> GetDailyMarketRefinitivPreProcessor Refinitiv - Error: " + e.getMessage()).log();
			e.getMessage();
			return false;
		}
	}
}
