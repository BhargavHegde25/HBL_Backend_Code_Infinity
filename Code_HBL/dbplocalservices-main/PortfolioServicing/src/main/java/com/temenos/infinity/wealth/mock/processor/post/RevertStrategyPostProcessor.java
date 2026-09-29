/**
 * 
 */
package com.temenos.infinity.wealth.mock.processor.post;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

/**
 * @author himaja.sridhar
 *
 */
public class RevertStrategyPostProcessor implements DataPostProcessor2 {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		Result revertStrategy = new Result();
		revertStrategy.addParam("message","Delete succeeded");
		revertStrategy.addOpstatusParam("0");
		revertStrategy.addHttpStatusCodeParam("200");
		revertStrategy.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		diagnostic.prepareDebug("==========> RevertStrategyPostProcessor Mock - Executed").log();
		return revertStrategy;
	}

}
