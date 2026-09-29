/**
 * 
 */
package com.temenos.infinity.api.wealth.tap.preandpostprocessors;

import org.json.JSONArray;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * @author himaja.sridhar
 *
 */
public class GetInstrumentsOrchPostProcessor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		Dataset body = result.getDatasetById("LoopDataset");
		JSONArray loopArr = ResultToJSON.convertDataset(body);
		JSONArray minArr = new JSONArray();
		JSONObject minObj = new JSONObject();
		for(int i = 0 ; i <loopArr.length();i++) {
			JSONObject loopObj = loopArr.getJSONObject(i);
			JSONArray jsonArr = Utilities.convertStringToJSONArray(loopObj.get("instrumentMinimal").toString());
			loopObj = jsonArr.getJSONObject(0);
			minArr.put(loopObj);
		}
		minObj.put("instrumentMinimal", minArr);
		JSONObject insObj = new JSONObject();
		insObj.put("isSecurityAsset", "");
		minObj.put("instrumentAssets", insObj);
		Result minRes = Utilities.constructResultFromJSONObject(minObj);
		minRes.addOpstatusParam("0");
		minRes.addHttpStatusCodeParam("200");
		minRes.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		return minRes;
	}

}
