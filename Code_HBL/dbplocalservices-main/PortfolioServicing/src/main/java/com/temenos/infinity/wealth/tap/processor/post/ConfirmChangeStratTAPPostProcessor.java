package com.temenos.infinity.wealth.tap.processor.post;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

import java.security.SecureRandom;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

/**
 * (INFO) If status is set as a part of the request , the operation is exited
 * else operation is executed.
 * 
 * @author himaja.sridhar
 *
 */

public class ConfirmChangeStratTAPPostProcessor implements DataPostProcessor2 {


	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

		diagnostic.prepareDebug("==========> ConfirmChangeStratTAPPostProcessor TAP - Entered ").log();
		if (result.getRecordById("body") != null) {
			String id = result.getRecordById("body").getParamValueByName("id").toString();
			diagnostic.prepareDebug("==========> ConfirmChangeStratTAPPostProcessor TAP - ID Fetched "+ id).log();
			result.addParam("id", id);
			result.addOpstatusParam("0");
			result.addHttpStatusCodeParam("200");
			result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		} else{
			//result.removeParamByName("errmsg");
		}
		result.removeParamByName("errmsg");
		diagnostic.prepareDebug("==========> ConfirmChangeStratTAPPostProcessor TAP - Exited ").log();
		return result;

	}
}