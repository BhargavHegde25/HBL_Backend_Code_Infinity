/**
 * 
 */
package com.temenos.infinity.wealthorder.refinitiv.processor.post;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * (INFO) Sets the status parameters to the result.
 * 
 * @author himaja.sridhar
 *
 */
public class GetMarketValuesPostProcessor implements DataPostProcessor2 {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		diagnostic.prepareDebug("==========> GetMarketValuesPostProcessor Refinitiv - Entered ").log();
		result.addOpstatusParam("0");
		result.addHttpStatusCodeParam("200");
		result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		result.removeParamByName("errmsg");
		diagnostic.prepareDebug("==========> GetMarketValuesPostProcessor Refinitiv - Exiting with success ").log();
		return result;
	}

}
