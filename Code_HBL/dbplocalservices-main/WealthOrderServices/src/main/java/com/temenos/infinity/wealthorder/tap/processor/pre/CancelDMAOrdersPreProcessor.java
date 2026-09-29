package com.temenos.infinity.wealthorder.tap.processor.pre;

import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.tap.preandpostprocessors.TAPTokenGenPreProcessor;

/**
 * 
 * @author muthukumarv
 *
 */

public class CancelDMAOrdersPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("rawtypes")
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		diagnostic.prepareDebug("==========> CancelDMAOrdersPreProcessor TAP - Entered ").log();
		try {
			if (request.getParameter("OrderID_Authentication") != null
					&& request.getParameter("OrderID_Authentication").equalsIgnoreCase("true")) {
				if (request.getParameter(TemenosConstants.OPSTATUS).equalsIgnoreCase("0")) {
					if ((request.getParameter("message") != null && request.getParameter("message").length() > 0)
							|| (request.getParameter("errorDetails") != null
									&& request.getParameter("errorDetails").length() > 0)) {
						result.addOpstatusParam("0");
						result.addHttpStatusCodeParam("200");
						result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
						diagnostic.prepareDebug("==========>  CancelDMAOrdersPreProcessor TAP - Exiting with error").log();
						return false;
					} else {
						TAPTokenGenPreProcessor obj = new TAPTokenGenPreProcessor();
						obj.execute(inputMap, request, response, result);
						diagnostic.prepareDebug("==========>  CancelDMAOrdersPreProcessor TAP - Token Generation Succeeded").log();
						return true;
					}
				}
			} else {
				result.addOpstatusParam("0");
				result.addHttpStatusCodeParam("200");
				result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				diagnostic.prepareDebug("==========>  CancelDMAOrdersPreProcessor TAP - Exiting with OrderID_Authentication false").log();
				return false;
			}
		} catch (Exception e) {
			alert.prepareError("==========>  CancelDMAOrdersPreProcessor TAP - Error: " + e.getMessage()).log();
			e.getMessage();
			return false;
		}
		return false;
	}

}
