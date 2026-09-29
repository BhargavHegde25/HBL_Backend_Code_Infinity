/**
 * 
 */
package com.temenos.infinity.api.wealthOrder.preandpostprocessors;

import org.json.JSONArray;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;
import com.temenos.infinity.api.wealthservices.util.T24ErrorAndOverrideHandling;

/**
 * (INFO) Builds the Result in the desired format for the create Order services
 * 
 * @author balaji.krishnan
 *
 */
public class CreateCurrencyConvertionPostProcessor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

		T24ErrorAndOverrideHandling errorHandlingutil = T24ErrorAndOverrideHandling.getInstance();
		if (errorHandlingutil.isErrorResult(result)) {
			return result;
		}
		Record headerRec = result.getRecordById("header");
		String statusVal = headerRec.getParamValueByName("status");
		JSONObject response1 = new JSONObject();
		JSONObject finalResponse = new JSONObject();
		if (statusVal.equals("success")) {
			String id = headerRec.getParamValueByName("id");
			Record body = result.getRecordById("body");
			Double indicativeRate = body.getParamValueByName("indicativeRate") != null
					? Double.parseDouble(body.getParamValueByName("indicativeRate").toString())
					: 0;
			response1.put("id", id);
			response1.put("indicativeRate", indicativeRate);
			response1.put("status", statusVal);
			response1.put("opstatus", "0");
			response1.put("httpStatusCode", "200");
			finalResponse.put("CreateCurrencyConvertion", response1);
			Result result1 = Utilities.constructResultFromJSONObject(finalResponse);
			result1.addOpstatusParam("0");
			result1.addHttpStatusCodeParam("200");
			result1.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			return result1;
		} else {
			Record errorRec = result.getRecordById("error");
			Object obj = errorRec.getParam("errorDetails").getObjectValue();
			JSONArray errArr = PortfolioWealthUtils.objectToJSONArray(obj);
			JSONObject errObj = errArr.getJSONObject(0);
			response1.put("errormessage", errObj.get("message"));
			Result errorRes = Utilities.constructResultFromJSONObject(response1);
			return errorRes;
		}

	}

}
