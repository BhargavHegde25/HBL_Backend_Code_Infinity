/**
 * 
 */
package com.temenos.infinity.api.wealth.preandpostprocessors;

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

/**
 * @author himaja.sridhar
 *
 */
public class ComputeStrategyOrchPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "rawtypes", "unused" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			String portfolioId = null, portfolioservicetype = "",marketSegmentId="",targetWeight="",isCustomized="";
			if (inputMap.get(TemenosConstants.PORTFOLIOID) != null
					&& inputMap.get(TemenosConstants.PORTFOLIOID).toString().trim().length() > 0) {
				portfolioId = inputMap.get(TemenosConstants.PORTFOLIOID).toString();
			} else {
				return unauthAccess(result, TemenosConstants.PORTFOLIOID);
			}
			if (inputMap.get(TemenosConstants.PORTFOLIOSERVICETYPE) != null
					&& inputMap.get(TemenosConstants.PORTFOLIOSERVICETYPE).toString().trim().length() > 0) {
				portfolioservicetype = inputMap.get(TemenosConstants.PORTFOLIOSERVICETYPE).toString();
			} else {
				return unauthAccess(result, TemenosConstants.PORTFOLIOSERVICETYPE);
			}
			if (inputMap.get("marketSegmentId") != null
					&& inputMap.get("marketSegmentId").toString().trim().length() > 0) {
				marketSegmentId = inputMap.get("marketSegmentId").toString();
			} else {
				return unauthAccess(result, "marketSegmentId");
			}
			if (inputMap.get("targetWeight") != null
					&& inputMap.get("targetWeight").toString().trim().length() > 0) {
				targetWeight = inputMap.get("targetWeight").toString();
			} else {
				return unauthAccess(result, "targetWeight");
			}
			if (inputMap.get("isCustomized") != null
					&& inputMap.get("isCustomized").toString().trim().length() > 0) {
				isCustomized = inputMap.get("isCustomized").toString();
			} else {
				return unauthAccess(result, "isCustomized");
			}	
			List<String> allportfoliosList = PortfolioWealthUtils.getAllPortfoliosFromCache(request);
			String wealthCore = "";
			if (allportfoliosList.contains(portfolioId) && portfolioservicetype.equalsIgnoreCase("Advisory")) {
				wealthCore = PortfolioWealthUtils.getWealthCoreFromCache(request);
				
				if(wealthCore.contains("TAP") && (!inputMap.containsKey("portfolioCode") || (inputMap.get("portfolioCode") != null && 
						inputMap.get("portfolioCode").toString().trim().length() == 0))) {
					return unauthAccess(result, "portfolioCode");
				}
				request.addRequestParam_(TemenosConstants.WEALTH_CORE, wealthCore);
				return true;
			} else {
				alert.prepareError("Invalid request").log();
				result.addParam("status", "Failure");
				result.addParam("error", "Unauthorized Access");
				return false;
			}

		} catch (Exception e) {
			alert.prepareError("Error in ConfirmRecomStratOrchPreProcessor" + e).log();
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
