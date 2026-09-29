package com.temenos.infinity.wealth.t24.processor.pre;

import java.util.HashMap;
import java.util.Set;
import java.util.List;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;


import com.kony.dbputilities.sessionmanager.SessionScope;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
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
 * @author r.gaayathri
 *
 */

public class RejectProposalOrchPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("rawtypes")
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			diagnostic.prepareDebug("==========> RejectProposalOrchPreProcessor T24 - Entered ").log();
//			Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
//			 if (!userPermissions.contains(TemenosConstants.SUITABILITY_PROFILE_VIEW)) {
//	            	result.addParam("opstatus", "1582");
//	            	result.addParam("status", TemenosConstants.FAILURE);
//					result.addParam("error", "Logged in user not authorized to perform this action");
//	                return false;
//	            }
			
             String portfolioId = null, portfolioservicetype = "";
             if (inputMap.get(TemenosConstants.PORTFOLIOID) != null
                     && inputMap.get(TemenosConstants.PORTFOLIOID).toString().trim().length() > 0) {
                 portfolioId = inputMap.get(TemenosConstants.PORTFOLIOID).toString();
             } else {
                 return unauthAccess(result,TemenosConstants.PORTFOLIOID);
         }
             List<String> allportfoliosList = PortfolioWealthUtils.getAllPortfoliosFromCache(request);
             String wealthCore = "";
             if (allportfoliosList.contains(portfolioId) )
             {   diagnostic.prepareDebug("==========> RejectProposalOrchPreProcessor T24 - Portfolio exists for the customer").log();       
            	 wealthCore = PortfolioWealthUtils.getWealthCoreFromCache(request);
                 request.addRequestParam_(TemenosConstants.WEALTH_CORE, wealthCore);
                     return true;
                 } 
             else {
            	 alert.prepareError("Portfolio ID " + portfolioId + " does not exist for the Customer").log();
                 result.addParam("status", "Failure");
                 result.addParam("error", "Unauthorized Access");
                 return false;
             }
			
		} catch (Exception e) {
			alert.prepareError("==========> RejectProposalOrchPreProcessor T24 - Error: " + e.getMessage()).log();
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
