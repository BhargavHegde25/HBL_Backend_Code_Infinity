/**
 * 
 */
package com.temenos.infinity.api.wealth.tap.preandpostprocessors;

import org.json.JSONArray;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * @author himaja.sridhar
 *
 */
public class GetStrategyListPostProcessor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		JSONObject bodyObj = ResultToJSON.convertRecord(result.getRecordById("body"));
		JSONArray permArr =bodyObj.getJSONArray("permValues");
		JSONObject permValue = permArr.getJSONObject(0);
		String strategyCode = permValue.get("code").toString();
		Result listResult = new Result();
		listResult.addOpstatusParam("0");
		listResult.addHttpStatusCodeParam("200");
		listResult.addParam("strategyCode", strategyCode);
		listResult.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		return listResult;
	}

}
