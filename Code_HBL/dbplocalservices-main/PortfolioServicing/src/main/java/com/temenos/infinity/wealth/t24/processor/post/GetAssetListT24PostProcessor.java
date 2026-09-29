package com.temenos.infinity.wealth.t24.processor.post;


import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
/**
 *
 * 
 * @author muthukumarv
 *
 */

public class GetAssetListT24PostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("opstatus", "0");
				jsonObj.put("httpStatusCode", "200");
				diagnostic.prepareDebug("==========> GetAssetListT24PostProcessor T24 - Executed ").log();
				return Utilities.constructResultFromJSONObject(jsonObj);
			
		} catch (Exception e) {
			alert.prepareError("==========> GetAssetListT24PostProcessor T24 - Error: " + e.getMessage()).log();
			e.getMessage();
		}
		return result;
	}

}
