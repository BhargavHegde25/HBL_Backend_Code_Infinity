/**
 * 
 */
package com.temenos.infinity.api.wealthOrder.preandpostprocessors;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthOrder.config.WealthAPIServices;

/**
 * (INFO) Builds the Result in the desired format for the Refinitiv service.
 * 
 * @author himaja.sridhar
 *
 */
public class GetStockNewsStoryPostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {
			JSONObject resObj = new JSONObject();
			resObj.put("ID", result.getParamByName("ID").getValue());
			resObj.put("HT", result.getParamByName("HT").getValue());
			resObj.put("RT", result.getParamByName("RT").getValue());
			resObj.put("TE", result.getParamByName("TE").getValue().replaceAll("<pre>|</pre>", ""));
			resObj.put("PR", result.getParamByName("PR").getValue());
			JSONObject resultObj = new JSONObject();
			resultObj.put("stockNewsStory", resObj);
			resultObj.put("opstatus", "0");
			resultObj.put("httpStatusCode", "200");
			return Utilities.constructResultFromJSONObject(resultObj);
		} catch (Exception e) {
			alert.prepareError("Error while invoking Refinitiv - "
					+ WealthAPIServices.WEALTH_GETREFINITIVSTOCKNEWSSTORY.getOperationName() + "  : " + e).log();
		}

		return null;
	}
}
