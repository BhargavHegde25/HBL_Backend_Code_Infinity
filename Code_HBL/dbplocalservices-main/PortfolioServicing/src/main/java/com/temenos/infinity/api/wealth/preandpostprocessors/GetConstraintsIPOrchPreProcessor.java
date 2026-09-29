package com.temenos.infinity.api.wealth.preandpostprocessors;

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
 * 
 * 
 * 
 * @author GAAYATHRI.R
 *
 */

public class GetConstraintsIPOrchPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("rawtypes")
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
            if (!userPermissions.contains(TemenosConstants.PORTFOLIO_HEALTH_INVESTMENT_CONSTRAINTS_VIEW)) {
            	result.addParam("opstatus", "1582");
            	result.addParam("status", TemenosConstants.FAILURE);
				result.addParam("error", "Logged in user not authorized to perform this action");
                return false;
            }
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
            if (allportfoliosList.contains(portfolioId) && portfolioservicetype.equalsIgnoreCase("Advisory"))
            {          
           	 wealthCore = PortfolioWealthUtils.getWealthCoreFromCache(request);
                request.addRequestParam_(TemenosConstants.WEALTH_CORE, wealthCore);
                    return true;
                } 
            else {
                alert.prepareError("Invalid request").log();
                result.addParam("status", "Failure");
                result.addParam("error", "Unauthorized Access");
                return false;
            }
			
		} catch (Exception e) {
			alert.prepareError("Error in GetConstraintsIPOrchPreProcessor" + e).log();
			e.getMessage();
			return false;
		}

	}
	private boolean unauthAccess(Result result, String param) {
	    alert.prepareError("Error:Invalid input! , Mandatory fields not given").log();
	    result.addParam("status", "Failure");
	    result.addParam("error", "Invalid Input!  " + param + " is mandatory.");
	    result.addHttpStatusCodeParam("0");
	    return false;
	}
}
