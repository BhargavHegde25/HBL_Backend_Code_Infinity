package com.temenos.infinity.wealth.tap.processor.post;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * (INFO) If status is set as a part of the request , the operation is exited
 * else operation is executed.
 * 
 * @author himaja.sridhar
 *
 */

public class ComputeStrategyPostProcessor implements DataPostProcessor2 {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

		diagnostic.prepareDebug("==========> ComputeStrategyPostProcessor TAP - Entered ").log();
		if (result.getRecordById("header").getDatasetById("messages").getRecord(0).hasParamByName("message")) {
			String msg = result.getRecordById("header").getDatasetById("messages").getRecord(0).getParamValueByName("message").toString();
			diagnostic.prepareDebug("==========> ComputeStrategyPostProcessor TAP - "+ msg).log();
			Result computeRes = new Result();
			computeRes.addOpstatusParam("0");
			computeRes.addHttpStatusCodeParam("200");
			computeRes.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			return computeRes;
		} else {
			diagnostic.prepareDebug("==========> ComputeStrategyPostProcessor TAP - No msg returned in the expected tag").log();
			result.addOpstatusParam("0");
			result.addHttpStatusCodeParam("200");
			result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			return result;

		}
	}
}
