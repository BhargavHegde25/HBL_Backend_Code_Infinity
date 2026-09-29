/**
 * 
 */
package com.temenos.infinity.api.wealthOrder.preandpostprocessors;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * (INFO) Returns the Result object constructed.
 * @author himaja.sridhar
 *
 */
public class GetHistoricalDataPostProcessor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		String errMsg = result.getParamValueByName("errmsg");
		if (errMsg != null && !errMsg.equals("")) {
			result.addOpstatusParam("0");
			result.addHttpStatusCodeParam("200");
			result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			result.removeParamByName("errmsg");
			return result;
		} else {
		return result;
		}
	}

}
