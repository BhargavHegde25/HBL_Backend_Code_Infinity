package com.temenos.infinity.api.wealth.tap.preandpostprocessors;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

import java.security.SecureRandom;

/**
 * (INFO) If status is set as a part of the request , the operation is exited
 * else operation is executed.
 * 
 * @author himaja.sridhar
 *
 */

public class ConfirmChangeStratTAPPostProcessor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		if (result.getRecordById("body") != null) {
			String id = result.getRecordById("body").getParamValueByName("id").toString();
			result.addParam("id", id);
			result.addOpstatusParam("0");
			result.addHttpStatusCodeParam("200");
			result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		} else{
			//result.removeParamByName("errmsg");
		}
		result.removeParamByName("errmsg");
		return result;

	}
}