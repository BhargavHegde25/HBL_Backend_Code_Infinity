/**
 * 
 */
package com.temenos.infinity.wealth.tap.processor.post;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealth.util.ModelConstraintDBUtil;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * @author himaja.sridhar
 *
 */
public class DeleteModelConstraintIDPostProcessor implements DataPostProcessor2 {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		String isDeleted = ModelConstraintDBUtil.deleteModelConstraint(request);
		diagnostic.prepareDebug("==========> DeleteModelConstraintIDPreProcessor TAP - Exited ").log();
		result.addOpstatusParam("0");
		result.addHttpStatusCodeParam("200");
		result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		return true;
	}

}
