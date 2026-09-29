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
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * @author himaja.sridhar
 *
 */
public class GetPayInstructionsPostProcessor implements DataPostProcessor2 {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		diagnostic.prepareDebug("==========> GetPayInstructionsPostProcessor TAP - Entered ").log();
		Dataset bodySet = result.getDatasetById("body");
		JSONObject payObj = new JSONObject();
		if(bodySet!=null)
		{
			JSONArray bodyArr =ResultToJSON.convertDataset(bodySet);
			diagnostic.prepareDebug("==========> GetPayInstructionsPostProcessor TAP - No. of records returned for accounts: "+bodyArr.length()).log();
			payObj.put("payInstructions", bodyArr);
		}
		else {
			payObj.put("payInstructions", "");
			diagnostic.prepareDebug("==========> GetPayInstructionsPostProcessor TAP - No records returned").log();
		}
		Result payResult = Utilities.constructResultFromJSONObject(payObj);
		payResult.addOpstatusParam("0");
		payResult.addHttpStatusCodeParam("200");
		payResult.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		diagnostic.prepareDebug("==========> GetPayInstructionsPostProcessor TAP - Exited ").log();
		return payResult;
	}

}
