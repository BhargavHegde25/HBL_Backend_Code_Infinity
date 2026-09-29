package com.temenos.infinity.wealth.mock.processor.post;



import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealth.config.PortfolioWealthAPIServices;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

public class SubmitStrategyQuesPostProcessor implements DataPostProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

		try {
			diagnostic.prepareDebug("==========> SubmitStrategyQuesPostProcessor Mock - Entered ").log();
			String portfolioId = request.getParameterValues(TemenosConstants.PORTFOLIOID)[0];
			
			if ((portfolioId.equalsIgnoreCase("100777-4")) ||(portfolioId.equalsIgnoreCase("100777-5"))) {
		    result.addParam("questionnaireHistoCode","question000-1");
			result.addParam("score", "28.0");
			result.addOpstatusParam("0");
			result.addHttpStatusCodeParam("200");
			result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			
			}
			
			else  {
				
				diagnostic.prepareDebug("==========> SubmitStrategyQuesPostProcessor Mock - Not an Advisory Portfolio").log();
				result.addParam("status", "Failure");
				result.addParam("error", "Unauthorized Access");
				return result;
			}
			
		} catch (Exception e) {
			e.getMessage();
			alert.prepareError("==========> SubmitStrategyQuesPostProcessor Mock - Error: " + e.getMessage()).log();
		}
		diagnostic.prepareDebug("==========> SubmitStrategyQuesPostProcessor Mock - Entered ").log();
		return result;
	}

}