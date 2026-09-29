/**
 * 
 */
package com.temenos.infinity.wealth.t24.processor.pre;

import java.util.Arrays;
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
public class GetHoldingsListOrchPreProcessor implements DataPreProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "rawtypes", "unused" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			diagnostic.prepareDebug("==========> GetHoldingsListOrchPreProcessor T24 - Entered ").log();
			Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
			if (!userPermissions.contains("WEALTH_PORTFOLIO_DETAILS_HOLDINGS_VIEW")) {
				result.addParam("opstatus", "1582");
				result.addParam("status", TemenosConstants.FAILURE);
				result.addParam("error", "Logged in user not authorized to perform this action");
				diagnostic.prepareDebug("==========> GetHoldingsListOrchPreProcessor T24 - No User permission").log();
				return false;
			} else {
				diagnostic.prepareDebug("==========> GetHoldingsListOrchPreProcessor T24 - User has permission ").log();
				String portfolioId = null, sortBy = null;
				List sortValues = Arrays.asList("", "description", "marketPrice", "quantity", "marketValue",
						"costPrice", "unrealPLMkt", "weightPercentage", "assetClass", "region", "sector", "secCCy",
						"exchangeRate", "marketValPOS", "costValue", "costExchangeRate", "unRealizedPLPercentage",
						"dailyPL", "dailyPLPercentage", "costValueSecCcy", "unrealizedProfitLossPercentageSecCcy",
						"unrealizedProfitLossSecCcy", "subAssetClass","status");
				Object sortByObj = inputMap.get(TemenosConstants.SORTBY);

				if (!sortValues.contains(sortByObj)) {
					return validateData(result, TemenosConstants.SORTBY);
				}
				if (inputMap.get(TemenosConstants.PORTFOLIOID) != null
						&& inputMap.get(TemenosConstants.PORTFOLIOID).toString().trim().length() > 0) {
					portfolioId = inputMap.get(TemenosConstants.PORTFOLIOID).toString();
				} else {
					return PortfolioServiceUtils.unauthAccess(result, TemenosConstants.PORTFOLIOID);
				}
				if (sortByObj != null) {
					sortBy = inputMap.get(TemenosConstants.SORTBY).toString();
				} else {
					return PortfolioServiceUtils.unauthAccess(result, TemenosConstants.SORTBY);
				}
				Object navPageObj = inputMap.get("navPage");
				if (navPageObj == null || navPageObj.toString().trim().equals("")) {
					return PortfolioServiceUtils.unauthAccess(result, TemenosConstants.NAVPAGE);
				}
				
				Object includeOrdersObj = inputMap.get(TemenosConstants.ISINCLUEORDERS);
				if (includeOrdersObj == null || includeOrdersObj.toString().trim().equals("")) {
					inputMap.put(TemenosConstants.ISINCLUEORDERS, "false");
					request.addRequestParam_(TemenosConstants.ISINCLUEORDERS,"false");
					// return PortfolioServiceUtils.unauthAccess(result,
					// TemenosConstants.ISINCLUEORDERS);
				} else {
					String isIncludeOrders = includeOrdersObj.toString();
					if (!isIncludeOrders.equalsIgnoreCase("true")) {
						inputMap.put(TemenosConstants.ISINCLUEORDERS, "false");
						request.addRequestParam_(TemenosConstants.ISINCLUEORDERS,"false");
					}
				}
				
				List<String> allportfoliosList = PortfolioWealthUtils.getAllPortfoliosFromCache(request);
				if (allportfoliosList.contains(portfolioId)) {
					diagnostic.prepareDebug("==========> GetHoldingsListOrchPreProcessor T24 - Portfolio exists for the customer").log();
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
			alert.prepareError("==========> GetHoldingsListOrchPreProcessor T24 - Error: " + e.getMessage()).log();
			e.getMessage();
			return false;
		}
	}

	

	public static boolean validateData(Result result, String inputParam) {
		alert.prepareError("Error:Invalid input. Format is not valid").log();
		result.addParam("status", "Failure");
		result.addParam("error", TemenosConstants.SORTBY + " value is not valid");
		return false;
	}

}
