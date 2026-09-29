package com.temenos.infinity.wealth.tap.processor.post;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

public class CreateQuesSessionPostProcessor implements DataPostProcessor2 {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

		try {
			diagnostic.prepareDebug("==========> CreateQuesSessionPostProcessor TAP - Entered ").log();
			Record body_Record = result.getRecordById("body");
			if (body_Record != null) {

				if (body_Record.hasParamByName("code") && body_Record.getParamValueByName("code") != null) {
					diagnostic.prepareDebug("==========> CreateQuesSessionPostProcessor TAP - Question Code Generated ").log();
					JSONObject final_response = new JSONObject();
					final_response.put("questionnaireHistoCode", body_Record.getParamValueByName("code"));
					Result final_result = Utilities.constructResultFromJSONObject(final_response);
					result.addParam("questionnaireHistoCode", body_Record.getParamValueByName("code"));
					final_result.addOpstatusParam("0");
					final_result.addHttpStatusCodeParam("200");
					final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
					diagnostic.prepareDebug("==========> CreateQuesSessionPostProcessor TAP - Exited ").log();
					return final_result;
				}
			}

		} catch (Exception e) {
			e.getMessage();
		}
		return result;

	}

}
