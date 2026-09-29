package com.temenos.infinity.wealth.t24.processor.pre;

import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.preandpostprocessors.TransactTokenGenPreProcessor;


public class GetStrategyQuestionsPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("rawtypes")
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			diagnostic.prepareDebug("==========> GetStrategyQuestionsPreProcessor T24 - Entered ").log();
			if (request.getParameter(TemenosConstants.WEALTH_CORE) != null
					&& (request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("T24,Refinitiv")
							|| request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("T24"))) {
				TransactTokenGenPreProcessor obj = new TransactTokenGenPreProcessor();
				obj.execute(inputMap, request, response, result);
				diagnostic.prepareDebug("==========> GetStrategyQuestionsPreProcessor T24 - Token Generation").log();
				return true;
			} else {
				result.addOpstatusParam("0");
				result.addHttpStatusCodeParam("200");
				result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				diagnostic.prepareDebug("==========> GetStrategyQuestionsPreProcessor T24 - Exiting without Token Generation").log();
				return false;
			}
		} catch (Exception e) {
			alert.prepareError("==========> GetStrategyQuestionsPreProcessor T24 - Error: " + e.getMessage()).log();
			e.getMessage();
		}
		return false;
	}

}
