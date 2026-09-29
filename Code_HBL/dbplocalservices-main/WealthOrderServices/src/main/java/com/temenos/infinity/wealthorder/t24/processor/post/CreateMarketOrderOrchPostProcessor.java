package com.temenos.infinity.wealthorder.t24.processor.post;

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
			if (request.getParameter(TemenosConstants.WEALTH_CORE) != null
					&& (request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("T24,Refinitiv")
							|| request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("T24"))) {
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
			} else {
				result.addOpstatusParam("0");
				result.addHttpStatusCodeParam("200");
				result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				return result;
			}
		} catch (Exception e) {
			result.addOpstatusParam("1582");
			result.addHttpStatusCodeParam("0");
			result.addParam(TemenosConstants.STATUS, "Failure");
		}
		return result;
	}

}
