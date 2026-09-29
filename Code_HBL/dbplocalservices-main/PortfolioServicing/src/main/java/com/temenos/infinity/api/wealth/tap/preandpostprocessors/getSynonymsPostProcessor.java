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
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;

/**
 * (INFO) Builds the result in desired format for the TAP service
 * 
 * @author himaja.sridhar
 *
 */
public class getSynonymsPostProcessor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
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
		return synoRes;
	}

}
