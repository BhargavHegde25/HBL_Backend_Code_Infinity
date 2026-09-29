/**
 * 
 */
package com.temenos.infinity.wealth.tap.processor.post;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;
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
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		diagnostic.prepareDebug("==========> GetAllStrategiesOrchPostProcessor TAP - Entered ").log();
		if(result.getDatasetById("LoopDataset")!=null){
		JSONArray loopArr = ResultToJSON.convertDataset(result.getDatasetById("LoopDataset"));
		JSONObject allStrategyObj = new JSONObject();
		JSONArray alterStrategyArr = new JSONArray();
		diagnostic.prepareDebug("==========> GetAllStrategiesOrchPostProcessor TAP - No. of records returned initially:  " + loopArr.length()).log();
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
		diagnostic.prepareDebug("==========> GetAllStrategiesOrchPostProcessor TAP - No. of records returned alternateStrategy:  " + alterStrategyArr.length()).log();
		Result allStrategies = Utilities.constructResultFromJSONObject(allStrategyObj);
		allStrategies.addOpstatusParam("0");
		allStrategies.addHttpStatusCodeParam("200");
		allStrategies.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		diagnostic.prepareDebug("==========> GetAllStrategiesOrchPostProcessor TAP - Exited ").log();
		return allStrategies;
		}
		else {
			result.addOpstatusParam("0");
			result.addHttpStatusCodeParam("200");
			result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			diagnostic.prepareDebug("==========> GetAllStrategiesOrchPostProcessor TAP - Exited ").log();
			return result;
		}
	}

}
