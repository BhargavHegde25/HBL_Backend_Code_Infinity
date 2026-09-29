package com.temenos.infinity.wealth.tap.processor.pre;

import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.tap.preandpostprocessors.TAPTokenGenPreProcessor;


public class ConfirmOrdersIPUpdatePreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("rawtypes")
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			diagnostic.prepareDebug("==========>  ConfirmOrdersIPUpdatePreProcessor TAP - Entered").log();
			if (request.getParameter(TemenosConstants.WEALTH_CORE) != null
					&& (request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("TAP,Refinitiv")
							|| request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("TAP"))) {
				
				TAPTokenGenPreProcessor obj = new TAPTokenGenPreProcessor();
				obj.execute(inputMap, request, response, result);
				diagnostic.prepareDebug("==========>  ConfirmOrdersIPUpdatePreProcessor TAP - Token Generation Succeeded").log();
				inputMap.put("funcResultCode", request.getParameter("funcResultCode"));
				inputMap.put(TemenosConstants.PORTFOLIODIMENSIONE,TemenosConstants.PORTFOLIOVAL);
				request.addRequestParam_(TemenosConstants.PORTFOLIODIMENSIONE,TemenosConstants.PORTFOLIOVAL);
				inputMap.put(TemenosConstants.SESSIONSTATUSE,TemenosConstants.TRADINGVAL);
				request.addRequestParam_(TemenosConstants.SESSIONSTATUSE,TemenosConstants.TRADINGVAL);
				diagnostic.prepareDebug("==========>  ConfirmOrdersIPUpdatePreProcessor TAP - Input Parameters set").log();
				return true;
				
			} else {
				result.addOpstatusParam("0");
				result.addHttpStatusCodeParam("200");
				result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				diagnostic.prepareDebug("==========>  ConfirmOrdersIPUpdatePreProcessor TAP - Exiting without Token Generation").log();
				return false;
			}
		} catch (Exception e) {
			alert.prepareError("==========>  ConfirmOrdersIPUpdatePreProcessor TAP - Error: "+ e.getMessage()).log();
			e.getMessage();
		}
		return false;
	}

}
