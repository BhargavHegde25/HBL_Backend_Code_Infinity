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
import com.konylabs.middleware.dataobject.Result;

/**
 * @author himaja.sridhar
 *
 */
public class GetAllStrategiesOrchPostProcessor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		if(result.getDatasetById("LoopDataset")!=null){
		JSONArray loopArr = ResultToJSON.convertDataset(result.getDatasetById("LoopDataset"));
		JSONObject allStrategyObj = new JSONObject();
		JSONArray alterStrategyArr = new JSONArray();
		for(int i=0;i<loopArr.length();i++) {
			JSONObject loopObj = loopArr.getJSONObject(i);
			if(i==0) {
				allStrategyObj.put("recStrategy",loopObj.getJSONArray("recStrategy"));
				
			}
			else {
				alterStrategyArr.put(loopObj.getJSONArray("recStrategy").getJSONObject(0));
			}
		}
		allStrategyObj.put("alternateStrategy", alterStrategyArr);
		Result allStrategies = Utilities.constructResultFromJSONObject(allStrategyObj);
		allStrategies.addOpstatusParam("0");
		allStrategies.addHttpStatusCodeParam("200");
		allStrategies.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		return allStrategies;
		}
		else {
			result.addOpstatusParam("0");
			result.addHttpStatusCodeParam("200");
			result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			return result;
		}
	}

}
