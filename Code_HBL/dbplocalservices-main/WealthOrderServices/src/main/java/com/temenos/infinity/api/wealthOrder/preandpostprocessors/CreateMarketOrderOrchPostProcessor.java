package com.temenos.infinity.api.wealthOrder.preandpostprocessors;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * (INFO) Sets the status parameters to the Result.
 * 
 * @author himaja.sridhar
 *
 */
public class CreateMarketOrderOrchPostProcessor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {
			if (result.getParamValueByName(TemenosConstants.STATUS).equalsIgnoreCase(TemenosConstants.SUCCESS)
					&& !((result.getParamValueByName("errmsg") != null
							&& result.getParamValueByName("errmsg").length() > 0)
							|| (result.getParamValueByName("errorDetails") != null
									&& result.getParamValueByName("errorDetails").length() > 0))) {
				result.addHttpStatusCodeParam("200");
				result.addOpstatusParam("0");
				result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				result.removeParamByName("errmsg");
			} else {
				result.addOpstatusParam("1582");
				result.addHttpStatusCodeParam("0");
				result.addParam(TemenosConstants.STATUS, "Failure");
			}
		} catch (Exception e) {
			result.addOpstatusParam("1582");
			result.addHttpStatusCodeParam("0");
			result.addParam(TemenosConstants.STATUS, "Failure");
		}
		return result;
	}

}
