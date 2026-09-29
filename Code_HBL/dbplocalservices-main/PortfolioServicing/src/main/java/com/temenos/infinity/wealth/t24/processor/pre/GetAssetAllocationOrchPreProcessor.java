/**
 * 
 */
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
import com.temenos.infinity.wealth.common.util.PortfolioServiceUtils;

/**
 * @author himaja.sridhar
 *
 */
public class GetAssetAllocationOrchPreProcessor implements DataPreProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("rawtypes")
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			diagnostic.prepareDebug("==========> GetAssetAllocationOrchPreProcessor T24 - Entered ").log();
			Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
			if (!userPermissions.contains("WEALTH_PORTFOLIO_DETAILS_ASSET_ALLOCATION_VIEW")) {
				result.addParam("opstatus", "1582");
				result.addParam("status", TemenosConstants.FAILURE);
				result.addParam("error", "Logged in user not authorized to perform this action");
				diagnostic.prepareDebug("==========> GetAssetAllocationOrchPreProcessor T24 - No User permission").log();
				return false;
			} else {
				diagnostic.prepareDebug("==========> GetAssetAllocationOrchPreProcessor T24 - User has permission ").log();
				String portfolioId = null;
				if (inputMap.get(TemenosConstants.PORTFOLIOID) != null
						&& inputMap.get(TemenosConstants.PORTFOLIOID).toString().trim().length() > 0) {
					portfolioId = inputMap.get(TemenosConstants.PORTFOLIOID).toString();
				} else {
					return PortfolioServiceUtils.unauthAccess(result, TemenosConstants.PORTFOLIOID);
				}
				List<String> allportfoliosList = PortfolioWealthUtils.getAllPortfoliosFromCache(request);
				if (allportfoliosList.contains(portfolioId)) {
					diagnostic.prepareDebug("==========> GetAssetAllocationOrchPreProcessor T24 - Portfolio exists for the customer").log();
				} else {
					alert.prepareError("Portfolio ID " + portfolioId + " does not exist for the Customer").log();
					alert.prepareError("Invalid request").log();
					result.addParam("status", "Failure");
					result.addParam("error", "Unauthorized Access");
					return false;

				}
				return true;
			}
		} catch (Exception e) {
			alert.prepareError("==========> GetAssetAllocationOrchPreProcessor T24 - Error: " + e.getMessage()).log();
			e.getMessage();
			return false;
		}
	}

}
