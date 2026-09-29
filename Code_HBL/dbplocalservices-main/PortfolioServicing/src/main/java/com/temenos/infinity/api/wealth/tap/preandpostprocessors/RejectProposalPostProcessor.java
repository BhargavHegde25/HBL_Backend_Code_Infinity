package com.temenos.infinity.api.wealth.tap.preandpostprocessors;

import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * (INFO) If status is set as a part of the request , the operation is exited
 * else operation is executed.
 * 
 * @author padmasris
 *
 */

public class RejectProposalPostProcessor implements DataPostProcessor2{

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		JSONObject bodyObj = ResultToJSON.convertRecord(result.getRecordById("body"));
		String message=bodyObj.getString("message");
		result.addParam("message", message);
		result.addOpstatusParam("0");
		result.addHttpStatusCodeParam("200");
		result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		return result;
	}
}
