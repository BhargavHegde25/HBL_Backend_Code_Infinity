package com.temenos.infinity.wealth.tap.processor.post;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

public class ConfirmOrdersIPUpdatePostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {
			diagnostic.prepareDebug("==========> ConfirmOrdersIPUpdatePostProcessor TAP - Entered ").log();
			JSONObject assetObj = new JSONObject();
			assetObj.put("message", "Orders placed successfully.");
			Result final_result = Utilities.constructResultFromJSONObject(assetObj);
			final_result.addOpstatusParam("0");
			final_result.addHttpStatusCodeParam("200");
			final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			diagnostic.prepareDebug("==========> ConfirmOrdersIPUpdatePostProcessor TAP - Exited ").log();
			return final_result;
		} catch (Exception e) {
			alert.prepareError("==========> ConfirmOrdersIPUpdatePostProcessor TAP - Error: " + e.getMessage()).log();
			e.getMessage();

		}
		return result;
	}
}
