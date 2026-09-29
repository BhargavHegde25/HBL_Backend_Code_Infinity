/**
 * 
 */
package com.temenos.infinity.api.wealthOrder.tap.preandpostprocessors;

import org.json.JSONArray;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;

/**
 * @author himaja.sridhar
 *
 */
public class GetPayInstructionsPostProcessor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		Dataset bodySet = result.getDatasetById("body");
		JSONObject payObj = new JSONObject();
		if(bodySet!=null)
		{
			JSONArray bodyArr =ResultToJSON.convertDataset(bodySet);
			payObj.put("payInstructions", bodyArr);
		}
		else {
			payObj.put("payInstructions", "");
		}
		Result payResult = Utilities.constructResultFromJSONObject(payObj);
		payResult.addOpstatusParam("0");
		payResult.addHttpStatusCodeParam("200");
		payResult.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		return payResult;
	}

}
