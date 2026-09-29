package com.temenos.infinity.wealth.mock.processor.pre;

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

public class GetPersonalizedStrategyPreProcessor implements DataPreProcessor2 {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

/**
 * @author vinayranjan.sharma
 *
 */
 
	@SuppressWarnings("rawtypes")
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			diagnostic.prepareDebug("==========> GetPersonalizedStrategyPreProcessor Mock - Entered ").log();
			String portfolioId = null, portfolioservicetype = "";
			if (inputMap.get(TemenosConstants.PORTFOLIOID) != null
					&& inputMap.get(TemenosConstants.PORTFOLIOID).toString().trim().length() > 0) {
				portfolioId = inputMap.get(TemenosConstants.PORTFOLIOID).toString();
			} else {
				unauthAccess(result,TemenosConstants.PORTFOLIOID);
			}

			if (inputMap.get(TemenosConstants.PORTFOLIOSERVICETYPE) != null
					&& inputMap.get(TemenosConstants.PORTFOLIOSERVICETYPE).toString().trim().length() > 0) {
				portfolioservicetype = inputMap.get(TemenosConstants.PORTFOLIOSERVICETYPE).toString();
			} else {
				unauthAccess(result,TemenosConstants.PORTFOLIOSERVICETYPE);
			}

			List<String> allportfoliosList = PortfolioWealthUtils.getAllPortfoliosFromCache(request);

			if (allportfoliosList.contains(portfolioId) && portfolioservicetype.equalsIgnoreCase("Advisory")) {
				String wealthCore = PortfolioWealthUtils.getWealthCoreFromCache(request);
				request.addRequestParam_(TemenosConstants.WEALTH_CORE, wealthCore);
				if (wealthCore.equals("Mock")) {
					diagnostic.prepareDebug("==========> GetPersonalizedStrategyPreProcessor Mock - Core check done").log();
					return true;
				} else {
					result.addOpstatusParam("0");
					result.addHttpStatusCodeParam("200");
					result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
					diagnostic.prepareDebug("==========> GetPersonalizedStrategyPreProcessor Mock - Exiting for token generation").log();
					result.addParam("wealthCoreFlag", "false");
					return false;
				}
			} else {
				diagnostic.prepareDebug("==========> GetPersonalizedStrategyPreProcessor Mock - Not an Advisory Portfolio/Valid portfolio for the customer").log();
				result.addParam("status", "Failure");
				result.addParam("error", "Unauthorized Access");
				return false;
			}
		} catch (Exception e) {
			alert.prepareError("==========> GetPersonalizedStrategyPreProcessor Mock - Error: " + e.getMessage()).log();
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
