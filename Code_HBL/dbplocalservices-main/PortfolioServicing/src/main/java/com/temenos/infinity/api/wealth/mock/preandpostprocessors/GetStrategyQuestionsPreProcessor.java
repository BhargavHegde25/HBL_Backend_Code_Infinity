package com.temenos.infinity.api.wealth.mock.preandpostprocessors;

import java.util.HashMap;
import java.util.List;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;

public class GetStrategyQuestionsPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("rawtypes")
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
            
			String portfolioId = null, portfolioservicetype = "";
			if (inputMap.get(TemenosConstants.PORTFOLIOID) != null
					&& inputMap.get(TemenosConstants.PORTFOLIOID).toString().trim().length() > 0) {
				portfolioId = inputMap.get(TemenosConstants.PORTFOLIOID).toString();
			} else {
				unauthAccess(result,TemenosConstants.PORTFOLIOID);
			}

			if (inputMap.get(TemenosConstants.PORTFOLIOSERVICETYPE) != null) {
				portfolioservicetype = inputMap.get(TemenosConstants.PORTFOLIOSERVICETYPE).toString();
			} else {
				unauthAccess(result,TemenosConstants.PORTFOLIOSERVICETYPE);
			}

			List<String> allportfoliosList = PortfolioWealthUtils.getAllPortfoliosFromCache(request);

			if (allportfoliosList.contains(portfolioId) && portfolioservicetype.equalsIgnoreCase("Advisory")) {
				String wealthCore = PortfolioWealthUtils.getWealthCoreFromCache(request);
				if (wealthCore.equals("Mock")) {
					return true;
				} else {
					result.addOpstatusParam("0");
					result.addHttpStatusCodeParam("200");
					result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
					result.addParam("getStrategy_flag", "false");
					return false;
				}
			} else {
				alert.prepareError("Invalid request").log();
				result.addParam("status", "Failure");
				result.addParam("error", "Unauthorized Access");
				return false;
			}
		} catch (Exception e) {
			alert.prepareError("Error in GetStrategyQuestionsPreProcessor" + e).log();
			e.getMessage();
			return false;
		}
	}

	private boolean unauthAccess(Result result, String param) {
		alert.prepareError("Error:Invalid input. Mandatory fields not given").log();
		result.addParam("status", "Failure");
		result.addParam("error", "Invalid Input! " + param + " is mandatory.");
		return false;
		
	}
}
