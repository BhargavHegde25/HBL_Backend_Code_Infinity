/**
 * 
 */
package com.temenos.infinity.wealth.tap.processor.pre;

import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * @author himaja.sridhar
 *
 */
public class RevertStrategyPreProcessor implements DataPreProcessor2 {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		diagnostic.prepareDebug("==========> Delete Constraint Element TAP - Entered ").log();
		if(request.getParameter("idVal") != null && request.getParameter("idVal")!="" && !request.getParameter("idVal").equals("")) {
			diagnostic.prepareDebug("==========>  Delete Constraint Element TAP - Constraint available ").log();
			inputMap.put("constraintId", request.getParameter("idVal"));
			result.addOpstatusParam("0");
			result.addHttpStatusCodeParam("200");
			result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		}
		else {
			diagnostic.prepareDebug("==========> Delete Constraint Element TAP - Asset personlized for the first time ").log();
			result.addOpstatusParam("0");
			result.addHttpStatusCodeParam("200");
			result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			return false;
		}
		diagnostic.prepareDebug("==========> R Delete Constraint Element TAP - Exited ").log();
		return true;
	}

}
