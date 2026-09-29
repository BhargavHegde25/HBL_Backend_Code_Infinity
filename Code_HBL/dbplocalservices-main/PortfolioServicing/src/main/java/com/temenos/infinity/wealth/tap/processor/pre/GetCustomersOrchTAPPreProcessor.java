package com.temenos.infinity.wealth.tap.processor.pre;

import java.util.HashMap;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * @author sarah
 *
 */
public class GetCustomersOrchTAPPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "rawtypes"})
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			diagnostic.prepareDebug("==========> GetCustomersOrchTAPPreProcessor TAP - Entered ").log();
			if (request.getParameter(TemenosConstants.WEALTH_CORE) != null
					&& (request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("TAP,Refinitiv")
							|| request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("TAP"))) {
				diagnostic.prepareDebug("==========> GetCustomersOrchTAPPreProcessor TAP - No Constraint in DB for the portfolio ").log();
				Object coreCustId = request.getParameter("coreCustomerId");
				if (coreCustId == null || coreCustId.equals("")) {
					//diagnostic.prepareDebug("==========> GetWatchlistDBPreProcessor Mock -Error:: invalid operation").log();
					return unauthAccess(result, TemenosConstants.CORECUSTOMERID);
				}
				return true;
			} else {
				result.addOpstatusParam("0");
				result.addHttpStatusCodeParam("200");
				result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				return false;
			}

		} catch (Exception e) {
			alert.prepareError("==========> GetCustomersOrchTAPPreProcessor TAP - Error: " + e.getMessage()).log();
			e.getMessage();
		}
		return false;
	}
public static boolean unauthAccess (Result result, String param) {
		alert.prepareError("Error:Invalid input. Mandatory fields not given").log();
		result.addParam("status", "Failure");
		result.addParam("error", "Invalid Input! " + param + " is mandatory.");
		result.addHttpStatusCodeParam("200");
		return false;

	}
}
