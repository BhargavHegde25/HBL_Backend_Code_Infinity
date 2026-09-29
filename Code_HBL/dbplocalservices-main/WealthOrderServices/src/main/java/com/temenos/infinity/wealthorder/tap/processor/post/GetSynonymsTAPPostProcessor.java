/**
 * 
 */
package com.temenos.infinity.wealthorder.tap.processor.post;

import org.json.JSONArray;
import org.json.JSONObject;
import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;
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
public class GetSynonymsTAPPostProcessor implements DataPostProcessor2 {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		diagnostic.prepareDebug("==========> GetSynonymsTAPPostProcessor TAP - Entered ").log();
		JSONObject resultObj = new JSONObject();
		Dataset body = result.getDatasetById("body");
		String code = "";
		String identifier = "";
		if (body == null || request.getParameter("id") == null) {
			code = "";
		} else {
			JSONArray bodyArr = ResultToJSON.convertDataset(body);
			for (int i = 0; i < bodyArr.length(); i++) {
				JSONObject bodyJSON = bodyArr.getJSONObject(i);
				if (bodyJSON.getString("codificationCode").equalsIgnoreCase("RICCODE")) {
					code = bodyJSON.getString("code");
					break;
				}
			}
			identifier = request.getParameter("id");
			
		}
		resultObj.put("RICCode", code);
		resultObj.put("id", identifier);
		Result synoRes = Utilities.constructResultFromJSONObject(resultObj);
		synoRes.addOpstatusParam("0");
		synoRes.addHttpStatusCodeParam("200");
		synoRes.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		diagnostic.prepareDebug("==========> GetSynonymsTAPPostProcessor TAP - Exited ").log();
		return synoRes;
	}

}
