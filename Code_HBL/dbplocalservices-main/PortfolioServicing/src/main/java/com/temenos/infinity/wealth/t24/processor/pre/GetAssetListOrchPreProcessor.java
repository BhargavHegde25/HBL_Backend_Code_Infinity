package com.temenos.infinity.wealth.t24.processor.pre;

import java.util.HashMap;
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
 * @author muthukumarv
 *
 */

public class GetAssetListOrchPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			diagnostic.prepareDebug("==========> GetAssetListOrchPreProcessor T24 - Entered ").log();
			Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
            if (!userPermissions.contains("WEALTH_INVESTMENT_DETAILS_TOTAL_ASSETS_VIEW")) {
            	result.addParam("opstatus", "1582");
            	result.addParam("status", TemenosConstants.FAILURE);
				result.addParam("error", "Logged in user not authorized to perform this action");
				diagnostic.prepareDebug("==========> GetAssetListOrchPreProcessor T24 - No User permission").log();
                return false;
            }
            else
			{
            	/* Checking whether coreCustomerId is present in request or not */
    			String coreCustomerId = null;
    			if (inputMap.get(TemenosConstants.CORECUSTOMERID) != null
    					&& inputMap.get(TemenosConstants.CORECUSTOMERID).toString().trim().length() > 0) {
    				/*CorecustomerId is present in request so entering Multi customer flow*/
    				coreCustomerId=inputMap.get(TemenosConstants.CORECUSTOMERID).toString();
    				inputMap.put(TemenosConstants.CUSTOMERID, coreCustomerId);
    				request.addRequestParam_(TemenosConstants.CUSTOMERID, coreCustomerId);
    			} else {
    				/*CorecustomerId is not present in request so entering Single customer flow*/
    				coreCustomerId = PortfolioWealthUtils.getCustomerFromCache(request);
    				inputMap.put(TemenosConstants.CUSTOMERID, coreCustomerId);
    				request.addRequestParam_(TemenosConstants.CUSTOMERID, coreCustomerId);
    			}
            	diagnostic.prepareDebug("==========> GetAssetListOrchPreProcessor T24 - User has permission ").log();
				return true;
			}

		} catch (Exception e) {
			alert.prepareError("==========> GetAssetListOrchPreProcessor T24 - Error: " + e.getMessage()).log();
			e.getMessage();
			return false;
		}


	}


}
