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
public class GetPerformanceOrchPreProcessor implements DataPreProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@SuppressWarnings({ "rawtypes", "null" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			diagnostic.prepareDebug("==========> GetPerformanceOrchPreProcessor T24 - Entered ").log();
			Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
			if (!userPermissions.contains("WEALTH_PORTFOLIO_DETAILS_PERFORMANCE_VIEW")) {
				result.addParam("opstatus", "1582");
				result.addParam("status", TemenosConstants.FAILURE);
				result.addParam("error", "Logged in user not authorized to perform this action");
				diagnostic.prepareDebug("==========> GetPerformanceOrchPreProcessor T24 - No User permission").log();
				return false;
			} else {
				diagnostic.prepareDebug("==========> GetPerformanceOrchPreProcessor T24 - User has permission ").log();
				String portfolioId = null;
				String dateformat = "YYYYMMdd";
				String dateFrom = null, dateTo = null;
				String dateFromObj = inputMap.get(TemenosConstants.DATEFROM).toString();
				String dateToObj = inputMap.get(TemenosConstants.DATETO).toString();
				String durationObj=inputMap.get(TemenosConstants.DURATION).toString();
				if (inputMap.get(TemenosConstants.PORTFOLIOID) != null
						&& inputMap.get(TemenosConstants.PORTFOLIOID).toString().trim().length() > 0) {
					portfolioId = inputMap.get(TemenosConstants.PORTFOLIOID).toString();
				} else {
					return PortfolioServiceUtils.unauthAccess(result, TemenosConstants.PORTFOLIOID);
				}
				if (dateFromObj != null || !dateFromObj.equals("")) {
					dateFrom = inputMap.get(TemenosConstants.DATEFROM).toString();
					boolean isTrue = PortfolioWealthUtils.validateDateFormat(dateformat,dateFrom);
					if(!isTrue) {
						return PortfolioServiceUtils.validateFormat(result,TemenosConstants.DATEFROM);
					}
				}
				else {
					return PortfolioServiceUtils.unauthAccess(result,TemenosConstants.DATEFROM);
				}
				
				if (dateToObj != null || ! dateToObj.equals("")) {
					dateTo = inputMap.get(TemenosConstants.DATETO).toString();
					boolean isTrue = PortfolioWealthUtils.validateDateFormat(dateformat,dateTo);
					if(!isTrue) {
						return PortfolioServiceUtils.validateFormat(result,TemenosConstants.DATETO);
					}
				}
				else {
					return PortfolioServiceUtils.unauthAccess(result,TemenosConstants.DATETO);
				}
				if (durationObj != null || ! durationObj.equals("")) {
				}
				else {
					return PortfolioServiceUtils.unauthAccess(result,TemenosConstants.DURATION);
				}
				List<String> allportfoliosList = PortfolioWealthUtils.getAllPortfoliosFromCache(request);
				if (allportfoliosList.contains(portfolioId)) {
					diagnostic.prepareDebug("==========> GetPerformanceOrchPreProcessor T24 - Portfolio exists for the customer").log();
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
			alert.prepareError("==========> GetPerformanceOrchPreProcessor T24 - Error: " + e.getMessage()).log();
			e.getMessage();
			return false;
		}
	}

}
