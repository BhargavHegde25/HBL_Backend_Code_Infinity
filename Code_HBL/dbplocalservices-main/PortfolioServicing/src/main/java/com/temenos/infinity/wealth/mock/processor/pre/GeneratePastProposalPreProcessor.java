package com.temenos.infinity.wealth.mock.processor.pre;

import java.util.HashMap;
import java.util.List;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;

public class GeneratePastProposalPreProcessor implements DataPreProcessor2 {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("rawtypes")
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			diagnostic.prepareDebug("==========> GeneratePastProposalPreProcessor Mock - Entered ").log();
			String wealthCore = EnvironmentConfigurationsHandler.getValue(TemenosConstants.WEALTH_CORE, request);
			result.addParam("wealthCore", wealthCore);
			
			String portfolioId = null, portfolioservicetype = "", navPage = "";
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
			
			if (inputMap.get(TemenosConstants.NAVPAGE) != null) {
				navPage = inputMap.get(TemenosConstants.NAVPAGE).toString();
			} else {
				unauthAccess(result,TemenosConstants.NAVPAGE);
			}

			List<String> allportfoliosList = PortfolioWealthUtils.getAllPortfoliosFromCache(request);

			if (allportfoliosList.contains(portfolioId) && portfolioservicetype.equalsIgnoreCase("Advisory")) {
				diagnostic.prepareDebug("==========> GeneratePastProposalPreProcessor Mock - Advisory Portfolio ").log();
				if (wealthCore.equals("Mock")) {
					diagnostic.prepareDebug("==========> GeneratePastProposalPreProcessor Mock - Core check done").log();
					return true;
				} else {
					result.addOpstatusParam("0");
					result.addHttpStatusCodeParam("200");
					result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
					diagnostic.prepareDebug("==========> GeneratePastProposalPreProcessor Mock - Exiting for token generation").log();
					return false;
				}
			} else {
				diagnostic.prepareDebug("==========> GeneratePastProposalPreProcessor Mock - Not an Advisory Portfolio/Valid portfolio for the customer").log();
				result.addParam("status", "Failure");
				result.addParam("error", "Unauthorized Access");
				return false;
			}
			
		}
		catch (Exception e) {
			alert.prepareError("==========> GeneratePastProposalPreProcessor Mock - Error: " + e.getMessage()).log();
			e.getMessage();
			return false;
		}
	}
	private boolean unauthAccess(Result result, String param) {
		alert.prepareError("Error:Invalid input. Mandatory fields not given").log();
		result.addParam("status", "Failure");
		result.addParam("error", "Invalid Input! " + param + " is mandatory.");
		result.addHttpStatusCodeParam("0");
		return false;
		
	}
}
