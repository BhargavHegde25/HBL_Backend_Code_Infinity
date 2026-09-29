/**
 * 
 */
package com.temenos.infinity.wealthorder.refinitiv.processor.post;

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
 * @author himaja.sridhar
 *
 */
public class GetNewsStoryRefinitivPostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		diagnostic.prepareDebug("==========> GetNewsStoryRefinitivPostProcessor Refinitiv - Entered ").log();
		try {
			JSONObject resObj = new JSONObject();
			resObj.put("ID", result.getParamByName("ID").getValue());
			resObj.put("HT", result.getParamByName("HT").getValue());
			resObj.put("RT", result.getParamByName("RT").getValue());
			resObj.put("TE", result.getParamByName("TE").getValue().replaceAll("<pre>|</pre>", ""));
			resObj.put("PR", result.getParamByName("PR").getValue());
			JSONObject resultObj = new JSONObject();
			resultObj.put("stockNewsStory", resObj.toString());
			resultObj.put("opstatus", "0");
			resultObj.put("httpStatusCode", "200");
			diagnostic.prepareDebug("==========> GetNewsStoryRefinitivPostProcessor Refinitiv - Exiting with success ").log();
			return Utilities.constructResultFromJSONObject(resultObj);
		} catch (Exception e) {
			alert.prepareError("==========> GetNewsStoryRefinitivPostProcessor Refinitiv - Error: " + e.getMessage()).log();
		}
		return result;
	}

}
