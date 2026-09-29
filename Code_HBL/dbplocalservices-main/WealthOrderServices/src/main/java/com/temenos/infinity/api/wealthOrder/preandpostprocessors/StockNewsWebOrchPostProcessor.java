package com.temenos.infinity.api.wealthOrder.preandpostprocessors;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 *  (INFO) Sets the status parameters to the Result.
 * @author himaja.sridhar
 *
 */
public class StockNewsWebOrchPostProcessor implements DataPostProcessor2{

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		result.removeParamByName("StoryId");
		result.removeDatasetById("storyIDs");
		result.addHttpStatusCodeParam("200");
		result.addOpstatusParam("0");
		result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		result.removeParamByName("errmsg");
		
		return result;
	}

}
