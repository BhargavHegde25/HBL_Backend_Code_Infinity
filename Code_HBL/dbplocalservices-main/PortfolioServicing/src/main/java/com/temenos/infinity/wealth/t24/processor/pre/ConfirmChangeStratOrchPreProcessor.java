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

public class ConfirmChangeStratOrchPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("rawtypes")
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			diagnostic.prepareDebug("==========> ConfirmChangeStratOrchPreProcessor T24 - Entered ").log();
			Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
            if (!userPermissions.contains(TemenosConstants.CHANGE_STRATEGY_CONFIRMATION)&&
                    !userPermissions.contains(TemenosConstants.CHOOSE_STRATEGY_ACKNOWLEDGEMENT)&&
                    !userPermissions.contains(TemenosConstants.CHOOSE_STRATEGY_ACKNOWLEDGEMENT_CHART)) {
            	result.addParam("opstatus", "1582");
            	result.addParam("status", TemenosConstants.FAILURE);
				result.addParam("error", "Logged in user not authorized to perform this action");
				diagnostic.prepareDebug("==========> ConfirmChangeStratOrchPreProcessor T24 - No User permission").log();
				return false;
			}
            diagnostic.prepareDebug("==========> ConfirmChangeStratOrchPreProcessor T24 - User has permission ").log();
            String portfolioId = null, portfolioservicetype = "", strategyId = null, strategyName = "";
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
			
			if (inputMap.get(TemenosConstants.STRATEGYID) != null
					&& inputMap.get(TemenosConstants.STRATEGYID).toString().trim().length() > 0) {
				strategyId = inputMap.get(TemenosConstants.STRATEGYID).toString();
			} else {
				return unauthAccess(result,TemenosConstants.STRATEGYID);
			}

			if (inputMap.get(TemenosConstants.STRATEGYNAME) != null
					&& inputMap.get(TemenosConstants.STRATEGYNAME).toString().trim().length() > 0) {
				strategyName = inputMap.get(TemenosConstants.STRATEGYNAME).toString();
			} else {
				return unauthAccess(result,TemenosConstants.STRATEGYNAME);
			}

			List<String> allportfoliosList = PortfolioWealthUtils.getAllPortfoliosFromCache(request);
			String wealthCore = "";
			if (allportfoliosList.contains(portfolioId) && portfolioservicetype.equalsIgnoreCase("Advisory")) {
				diagnostic.prepareDebug("==========> ConfirmChangeStratOrchPreProcessor T24 - Advisory Portfolio ").log();
				wealthCore = PortfolioWealthUtils.getWealthCoreFromCache(request);
				request.addRequestParam_(TemenosConstants.WEALTH_CORE, wealthCore);
				return true;
			}
		else {
				diagnostic.prepareDebug("==========> ConfirmChangeStratOrchPreProcessor T24 - Not an Advisory Portfolio/Valid portfolio for the customer ").log();
				result.addParam("status", "Failure");
				result.addParam("error", "Unauthorized Access");
				return false;
			}
			
		}
		catch (Exception e) {
			alert.prepareError("==========> ConfirmChangeStratOrchPreProcessor T24 - Error: " + e.getMessage()).log();
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
