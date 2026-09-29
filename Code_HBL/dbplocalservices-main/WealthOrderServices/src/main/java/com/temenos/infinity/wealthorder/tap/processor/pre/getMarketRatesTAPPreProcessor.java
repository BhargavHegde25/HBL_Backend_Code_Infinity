package com.temenos.infinity.wealthorder.tap.processor.pre;

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
import com.temenos.infinity.api.wealthservices.tap.preandpostprocessors.TAPTokenGenPreProcessor;

public class getMarketRatesTAPPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		diagnostic.prepareDebug("==========>  getMarketRatesTAPPreProcessor TAP - Entered").log();
		try {
			String wealthCore = EnvironmentConfigurationsHandler.getValue(TemenosConstants.WEALTH_CORE, request);
			if (wealthCore != null
					&& (wealthCore.equalsIgnoreCase("TAP,Refinitiv") || wealthCore.equalsIgnoreCase("TAP"))) {
				TAPTokenGenPreProcessor obj = new TAPTokenGenPreProcessor();
				obj.execute(inputMap, request, response, result);
				inputMap.put("natureE", "Sell");
				inputMap.put("completeOnly", "true");
				inputMap.put("orderTypeCode", "PCK_TCIB_FXSPOT_CON");
				inputMap.put("fxRateDirectionF", "false");
				inputMap.put("portfolioCode", request.getParameter(TemenosConstants.PORTFOLIOID));
				inputMap.put(TemenosConstants.SELLCURRENCY,
						request.getParameter(TemenosConstants.CURRENCYPAIRS).substring(0, 3));
				inputMap.put(TemenosConstants.BUYCURRENCY,
						request.getParameter(TemenosConstants.CURRENCYPAIRS).substring(3));
				diagnostic.prepareDebug("==========>  getMarketRatesTAPPreProcessor TAP - Token Generation Succeeded").log();
				return true;
			} else {
				result.addOpstatusParam("0");
				result.addHttpStatusCodeParam("200");
				result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				diagnostic.prepareDebug("==========>  getMarketRatesTAPPreProcessor TAP - Token Generation Succeeded").log();
				return false;
			}
		} catch (Exception e) {
			alert.prepareError("==========>  getMarketRatesTAPPreProcessor TAP - Error: " + e.getMessage()).log();
			e.getMessage();
		}
		return false;
	}

}
