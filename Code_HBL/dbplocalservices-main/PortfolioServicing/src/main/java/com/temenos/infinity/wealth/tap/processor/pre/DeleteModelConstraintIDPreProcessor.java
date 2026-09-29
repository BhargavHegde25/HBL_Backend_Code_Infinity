/**
 * 
 */
package com.temenos.infinity.wealth.tap.processor.pre;

import java.util.HashMap;

import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealth.util.ModelConstraintDBUtil;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

/**
 * @author himaja.sridhar
 *
 */
public class DeleteModelConstraintIDPreProcessor implements DataPreProcessor2 {

	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		diagnostic.prepareDebug("==========> DeleteModelConstraintIDPreProcessor TAP - Entered ").log();
		inputMap.put("portfolioCode", request.getParameter("portfolioCode"));
		String constraintId = ModelConstraintDBUtil.getModelConstraint(inputMap, request);
		diagnostic.prepareDebug("==========> DeleteModelConstraintIDPreProcessor TAP - Constraint from db fetched ").log();
		inputMap.put("modelConstraintId", constraintId);
		inputMap.put("portfolioId", request.getParameter("portfolioId"));
		return true;
	}
}
