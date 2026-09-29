package com.temenos.infinity.wealth.tap.processor.post;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;
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

public class GetStrategyAllocationPostProcessor implements DataPostProcessor2 {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		diagnostic.prepareDebug("==========> GetStrategyAllocationPostProcessor TAP - Entered ").log();
		Dataset assetSet = result.getDatasetById("body");
		JSONArray assetArr = ResultToJSON.convertDataset(assetSet);
		JSONArray finalArr = new JSONArray();
		diagnostic.prepareDebug("==========> GetStrategyAllocationPostProcessor TAP - No. of records returned initially: "
				+ assetArr.length()).log();
		for (int i = 0; i < assetArr.length(); i++) {
				if (assetArr.getJSONObject(i).has("parentId")) {
					finalArr.put(assetArr.getJSONObject(i));
				}
		}

		for (int i = 0; i < finalArr.length(); i++) {
			JSONObject assetObj = finalArr.getJSONObject(i);
			String valType = assetObj.getString("strategyWeight");
			Double wght = Double.parseDouble(valType);
			Double wght1 = (double) Math.round(wght * 100) / 100;
			finalArr.getJSONObject(i).put("strategyWeight", wght1.toString());

		}

		JSONObject assetObj = new JSONObject();
		assetObj.put("strategyAlloc", finalArr);
		diagnostic.prepareDebug("==========> GetStrategyAllocationPostProcessor TAP - No. of records returned in allocation: "
				+ finalArr.length()).log();
		result.removeDatasetById("strategyAlloc");
		Result allocationHC = Utilities.constructResultFromJSONObject(assetObj);
		allocationHC.addOpstatusParam("0");
		allocationHC.addHttpStatusCodeParam("200");
		allocationHC.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		diagnostic.prepareDebug("==========> GetStrategyAllocationPostProcessor TAP - Exited ").log();
		return allocationHC;
	}
}