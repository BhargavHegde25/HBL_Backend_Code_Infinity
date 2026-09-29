/**
 * 
 */
package com.temenos.infinity.wealth.mock.processor.pre;

import java.util.HashMap;
import java.util.List;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
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
public class GetPerformanceMockPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "unused", "rawtypes", "null" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			diagnostic.prepareDebug("==========> GetPerformanceMockPreProcessor Mock - Entered ").log();
			String wealthCore = EnvironmentConfigurationsHandler.getValue(TemenosConstants.WEALTH_CORE, request);
			result.addParam(TemenosConstants.WEALTH_CORE, wealthCore);
			String dateformat = "YYYYMMdd";

			String dateFrom = null, dateTo = null, portfolioId = null, benchMark = null, duration = null, sortBy = null,
					sortType = null, limit = null, offset = null;
			if (inputMap.get(TemenosConstants.PORTFOLIOID) != null
					&& inputMap.get(TemenosConstants.PORTFOLIOID).toString().trim().length() > 0) {
				portfolioId = inputMap.get(TemenosConstants.PORTFOLIOID).toString();
			} else {
				return unauthAccess(result, TemenosConstants.PORTFOLIOID);
			}
			Object dateFromObj = inputMap.get(TemenosConstants.DATEFROM);
			if (dateFromObj != null || !dateFromObj.equals("")) {
				dateFrom = inputMap.get(TemenosConstants.DATEFROM).toString();
				boolean isTrue = PortfolioWealthUtils.validateDateFormat(dateformat, dateFrom);
				if (!isTrue) {
					return validateDateFormat(result, TemenosConstants.DATEFROM);
				}
			} else {
				return unauthAccess(result, TemenosConstants.DATEFROM);
			}
			Object dateToObj = inputMap.get(TemenosConstants.DATETO);
			if (dateToObj != null || !dateToObj.equals("")) {
				dateTo = inputMap.get(TemenosConstants.DATETO).toString();
				boolean isTrue = PortfolioWealthUtils.validateDateFormat(dateformat, dateTo);
				if (!isTrue) {
					return validateDateFormat(result, TemenosConstants.DATETO);
				}
			} else {
				return unauthAccess(result, TemenosConstants.DATETO);
			}
			Object durationObj = inputMap.get(TemenosConstants.DURATION);
			if (durationObj != null || !durationObj.equals("")) {
			} else {
				return unauthAccess(result, TemenosConstants.DURATION);
			}
			List<String> allportfoliosList = PortfolioWealthUtils.getAllPortfoliosFromCache(request);

			if (allportfoliosList.contains(portfolioId)) {
				diagnostic.prepareDebug("==========> GetPerformanceMockPreProcessor Mock - Portfolio exists for the customer").log();
				if (wealthCore != null
						&& (wealthCore.equalsIgnoreCase("Mock"))) {
					diagnostic.prepareDebug("==========> GetPerformanceMockPreProcessor Mock - Core check done").log();
					return true;
				} else {
					result.addOpstatusParam("0");
					result.addHttpStatusCodeParam("200");
					result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
					diagnostic.prepareDebug("==========> GetPerformanceMockPreProcessor Mock - Exiting for token generation").log();
					return false;
				}
			} else {
				alert.prepareError("Portfolio ID " + portfolioId + " does not exist for the Customer").log();
				alert.prepareError("Invalid request").log();
				result.addParam("status", "Failure");
				result.addParam("error", "Unauthorized Access");
				return false;
			}

		} catch (Exception e) {
			alert.prepareError("==========> GetPerformanceMockPreProcessor Mock - Error: " + e.getMessage()).log();
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

	public static boolean validateDateFormat(Result result, String inputParam) {
		alert.prepareError("Error:Invalid input. Format is not valid").log();
		result.addParam("status", "Failure");
		result.addParam("error", inputParam + " is not in a valid format.");
		return false;
	}
}
