package com.temenos.infinity.api.wealth.preandpostprocessors;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

public class GetOrderProposalPostProcessor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		result.addOpstatusParam("0");
		result.addHttpStatusCodeParam("200");
		result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		return result;
	}

}
