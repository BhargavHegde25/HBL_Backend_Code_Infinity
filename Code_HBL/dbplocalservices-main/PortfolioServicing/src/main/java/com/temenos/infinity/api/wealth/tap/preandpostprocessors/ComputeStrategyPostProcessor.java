package com.temenos.infinity.api.wealth.tap.preandpostprocessors;

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

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		if (result.getRecordById("header").getDatasetById("messages").getRecord(0).hasParamByName("message")) {
			Result computeRes = new Result();
			computeRes.addOpstatusParam("0");
			computeRes.addHttpStatusCodeParam("200");
			computeRes.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			return computeRes;
		} else {
			result.addOpstatusParam("0");
			result.addHttpStatusCodeParam("200");
			result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			return result;

		}
	}
}
