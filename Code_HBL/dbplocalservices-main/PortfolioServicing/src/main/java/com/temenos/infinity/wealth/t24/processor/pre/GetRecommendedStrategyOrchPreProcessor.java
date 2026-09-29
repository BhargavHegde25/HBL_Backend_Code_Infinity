package com.temenos.infinity.wealth.t24.processor.pre;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.sessionmanager.SessionScope;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;

/**
 * (INFO) If status is set as a part of the request , the operation is exited
 * else operation is executed.
 * 
 * @author balaji.krishnan
 *
 */

public class GetRecommendedStrategyOrchPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("rawtypes")
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			diagnostic.prepareDebug("==========> GetRecommendedStrategyOrchPreProcessor T24 - Entered ").log();
			Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
			 if (!userPermissions.contains(TemenosConstants.RECOMMENDED_STRATEGY_VIEW)) {
	            	result.addParam("opstatus", "1582");
	            	result.addParam("status", TemenosConstants.FAILURE);
					result.addParam("error", "Logged in user not authorized to perform this action");
					diagnostic.prepareDebug("==========> GetRecommendedStrategyOrchPreProcessor T24 - No User permission").log();
	                return false;
	            }
			 diagnostic.prepareDebug("==========> GetRecommendedStrategyOrchPreProcessor T24 - User has permission ").log();
			 String portfolioId = null, portfolioservicetype = "";
				if (inputMap.get(TemenosConstants.PORTFOLIOID) != null
						&& inputMap.get(TemenosConstants.PORTFOLIOID).toString().trim().length() > 0) {
					portfolioId = inputMap.get(TemenosConstants.PORTFOLIOID).toString();
				} else {
					return unauthAccess(result,TemenosConstants.PORTFOLIOID);
				}

				if (inputMap.get(TemenosConstants.PORTFOLIOSERVICETYPE) != null
						&& inputMap.get(TemenosConstants.PORTFOLIOSERVICETYPE).toString().trim().length() > 0) {
					portfolioservicetype = inputMap.get(TemenosConstants.PORTFOLIOSERVICETYPE).toString();
				} else {
					return unauthAccess(result,TemenosConstants.PORTFOLIOSERVICETYPE);
				}

				List<String> allportfoliosList = PortfolioWealthUtils.getAllPortfoliosFromCache(request);
				String wealthCore = "";

				if (allportfoliosList.contains(portfolioId) && portfolioservicetype.equalsIgnoreCase("Advisory")) {
					diagnostic.prepareDebug("==========> GetRecommendedStrategyOrchPreProcessor T24 - Advisory Portfolio ").log();
					wealthCore = PortfolioWealthUtils.getWealthCoreFromCache(request);
					request.addRequestParam_(TemenosConstants.WEALTH_CORE, wealthCore);
					return true;
				} else {
					diagnostic.prepareDebug("==========> GetRecommendedStrategyOrchPreProcessor T24 - Not an Advisory Portfolio/Valid portfolio for the customer").log();
					result.addParam("status", "Failure");
					result.addParam("error", "Unauthorized Access");
					return false;
				}
			} catch (Exception e) {
				alert.prepareError("==========> GetRecommendedStrategyOrchPreProcessor T24 - Error: " + e.getMessage()).log();
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
