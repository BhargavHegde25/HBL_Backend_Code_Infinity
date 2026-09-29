package com.temenos.infinity.wealth.mock.processor.pre;

import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.preandpostprocessors.TransactTokenGenPreProcessor;

/**
 * (INFO) If status is set as a part of the request , the operation is exited
 * else operation is executed.
 * 
 * @author R.Lakshminarayanan
 *
 */

public class ConfirmChangeStratFromQuestionPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("rawtypes")
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			diagnostic.prepareDebug("==========> ConfirmChangeStratFromQuestionPreProcessor Mock - Entered ").log();
			if (request.getParameter(TemenosConstants.WEALTH_CORE) != null
					&& (request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("Mock"))) {
				diagnostic.prepareDebug("==========> ConfirmChangeStratFromQuestionPreProcessor Mock - Core check done").log();
				return true;

			} else {
				result.addOpstatusParam("0");
				result.addHttpStatusCodeParam("200");
				result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				diagnostic.prepareDebug("==========> ConfirmChangeStratFromQuestionPreProcessor Mock - Exiting for token generation").log();
				return false;
			}

		} catch (Exception e) {
			alert.prepareError("==========> ConfirmChangeStratFromQuestionPreProcessor Mock - Error: " + e.getMessage()).log();
			e.getMessage();
		}
		return false;
	}

}
