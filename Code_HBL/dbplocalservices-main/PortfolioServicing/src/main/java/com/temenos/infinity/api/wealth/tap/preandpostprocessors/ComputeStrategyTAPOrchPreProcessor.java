/**
 * 
 */
package com.temenos.infinity.api.wealth.tap.preandpostprocessors;

import java.util.HashMap;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealth.util.ModelConstraintDBUtil;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * @author himaja.sridhar
 *
 */
public class ComputeStrategyTAPOrchPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			if (request.getParameter(TemenosConstants.WEALTH_CORE) != null
					&& (request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("TAP,Refinitiv")
							|| request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("TAP"))) {
				if (inputMap.get("portfolioCode") != null
						&& inputMap.get("portfolioCode").toString().trim().length() > 0) {
					String constraintId = ModelConstraintDBUtil.getModelConstraint(inputMap, request);
					if (constraintId == null) {
						return true;
					} else {
						request.addRequestParam_("modelConstraintId", constraintId);
						return true;
					}
				} else {
					return unauthAccess(result, "portfolioCode");
				}
			} else {
				result.addOpstatusParam("0");
				result.addHttpStatusCodeParam("200");
				result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				return false;
			}

		} catch (Exception e) {
			alert.prepareError("Error in ConfirmRecomStratOrchPreProcessor" + e).log();
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
