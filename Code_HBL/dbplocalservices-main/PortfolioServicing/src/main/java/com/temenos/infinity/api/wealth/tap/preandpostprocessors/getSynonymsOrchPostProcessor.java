/**
 * 
 */
package com.temenos.infinity.api.wealth.tap.preandpostprocessors;

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
 * (INFO) Builds the result in desired format for the TAP service
 * 
 * @author himaja.sridhar
 *
 */
public class getSynonymsOrchPostProcessor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		Dataset body = result.getDatasetById("LoopDataset");
		JSONArray loopArr = ResultToJSON.convertDataset(body);
		JSONObject loopObj = new JSONObject();
		loopObj.put("RICSet",loopArr);
		Result synoRes = Utilities.constructResultFromJSONObject(loopObj);
		synoRes.addOpstatusParam("0");
		synoRes.addHttpStatusCodeParam("200");
		synoRes.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		return synoRes;
	}

}
