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

public class SubmitStrategyQuesOrchPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("rawtypes")
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			diagnostic.prepareDebug("==========> SubmitStrategyQuesOrchPreProcessor T24 - Entered ").log();
//			Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
//			 if (userPermissions.contains(TemenosConstants.CHANGE_STRATEGY_CONFIRMATION) &&
//	            		userPermissions.contains(TemenosConstants.CHOOSE_STRATEGY_ACKNOWLEDGEMENT) &&
//	            		userPermissions.contains(TemenosConstants.CHOOSE_STRATEGY_ACKNOWLEDGEMENT_CHART)) {
//	            	result.addParam("opstatus", "1582");
//	            	result.addParam("status", TemenosConstants.FAILURE);
//					result.addParam("error", "Logged in user not authorized to perform this action");
//	                return false;
//	            }
//			
			
			String wealthCore = PortfolioWealthUtils.getWealthCoreFromCache(request);
			String portfolioId = null, portfolioservicetype = "";
			if(wealthCore.equals("Mock")){
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
			
			}
			List<String> allportfoliosList = PortfolioWealthUtils.getAllPortfoliosFromCache(request);
			

			if ((allportfoliosList.contains(portfolioId) && portfolioservicetype.equalsIgnoreCase("Advisory")) || (wealthCore.equals("TAP")) || (wealthCore.equals("TAP,Refinitiv"))) {
				request.addRequestParam_(TemenosConstants.WEALTH_CORE, wealthCore);
				diagnostic.prepareDebug("==========> SubmitStrategyQuesOrchPreProcessor T24 - Advisory Portfolio ").log();
				return true;
			} else {
				diagnostic.prepareDebug("==========> SubmitStrategyQuesOrchPreProcessor T24 - Not an Advisory Portfolio/Valid portfolio for the customer").log();
				alert.prepareError("Invalid request").log();
				result.addParam("status", "Failure");
				result.addParam("error", "Unauthorized Access");
				return false;
			}
		} catch (Exception e) {
			alert.prepareError("==========> SubmitStrategyQuesOrchPreProcessor T24 - Error: " + e.getMessage()).log();
			e.getMessage();
			return false;
		}
	}

	private boolean unauthAccess(Result result, String param) {
		alert.prepareError("Error:Invalid input. Mandatory fields not given").log();
		result.addParam("status", "Failure");
		result.addParam("error", "Invalid Input! " + param + " is mandatory.");
		result.addParam("opstatus", "0");
		return false;
		
	}

}
