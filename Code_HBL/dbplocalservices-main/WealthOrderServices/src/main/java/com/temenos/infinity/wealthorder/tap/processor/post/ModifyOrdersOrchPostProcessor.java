package com.temenos.infinity.wealthorder.tap.processor.post;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.wealthorder.tap.processor.pre.ModifyOrderTAPOrchPreProcessor;

/**
 * (INFO) Sets the status parameters to the Result.
 * 
 * @author himaja.sridhar
 *
 */
public class ModifyOrdersOrchPostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		diagnostic.prepareDebug("==========> ModifyOrdersOrchPostProcessor TAP - Entered ").log();
		try {
			if (request.getParameter(TemenosConstants.WEALTH_CORE) != null
					&& (request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("TAP,Refinitiv")
							|| request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("TAP"))) {
				if (result.getParamValueByName(TemenosConstants.STATUS).equalsIgnoreCase(TemenosConstants.SUCCESS)
						&& !((result.getParamValueByName("errmsg") != null
								&& result.getParamValueByName("errmsg").length() > 0)
								|| (result.getParamValueByName("errorDetails") != null
										&& result.getParamValueByName("errorDetails").length() > 0))) {
					result.addHttpStatusCodeParam("200");
					result.addOpstatusParam("0");
					result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
					result.removeParamByName("errmsg");
					diagnostic.prepareDebug("==========> ModifyOrdersOrchPostProcessor TAP - Exiting with success ").log();
				} else {
					result.addOpstatusParam("1582");
					result.addHttpStatusCodeParam("0");
					result.addParam(TemenosConstants.STATUS, "Failure");
					diagnostic.prepareDebug("==========> ModifyOrdersOrchPostProcessor TAP - Exiting with error ").log();
				}
				result.removeParamByName("OrderID_Authentication");
				result.removeParamByName("message");

				return result;
			} else {
				result.addOpstatusParam("0");
				result.addHttpStatusCodeParam("200");
				result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				diagnostic.prepareDebug("==========> ModifyOrdersOrchPostProcessor TAP - Exiting with success ").log();
				return result;
			}
		} catch (Exception e) {
			alert.prepareError("==========> ModifyOrdersOrchPostProcessor TAP - Error: " + e.getMessage()).log();
		}
		return result;
	}

}
