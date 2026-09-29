package com.temenos.infinity.api.wealthOrder.tap.preandpostprocessors;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
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
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;
import com.temenos.infinity.api.wealthOrder.tap.preandpostprocessors.CancelOrdersPostProcessor;

/**
 * (INFO) If status is set as a part of the request , the operation is exited
 * else operation is executed.
 * 
 * @author balaji.krishnan
 *
 */

public class CancelOrdersPostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {
			String orderId = "";
			boolean success = false;
			JSONArray errResponseArr = new JSONArray();
			JSONArray msgResponseArr = new JSONArray();
			JSONObject finresponse = new JSONObject();
			if (StringUtils.isNotBlank(request.getParameterValues(TemenosConstants.ORDER_ID)[0])) {
				orderId = request.getParameterValues(TemenosConstants.ORDER_ID)[0];
			}
			Dataset ds = result.getDatasetById("messages");
			if (ds != null) {
				JSONObject resultObj = new JSONObject();
				resultObj.put("Field", Utilities.convertStringToJSONArray(ResultToJSON.convertDataset(ds).toString()));
				JSONArray msgArray = resultObj.getJSONArray("Field");
				if (msgArray != null && msgArray.length() > 0) {
					for (int i = 0; i < msgArray.length(); i++) {
						JSONObject errJSONObj = new JSONObject();
						JSONObject msgJSONObj = new JSONObject();
						JSONObject msgObj = msgArray.getJSONObject(i);
						if (StringUtils.isNotBlank(msgObj.get("message").toString())
								&& msgObj.get("message").toString().equalsIgnoreCase("Update succeeded.")) {
							success = true;
							String id = msgObj.has("paramName") && msgObj.get("paramName") != null
									&& msgObj.get("paramName").toString().length() > 0
											? msgObj.get("paramName").toString()
											: orderId;
							msgJSONObj.put("id", id);
							msgJSONObj.put("message", msgObj.get("message").toString());
							msgResponseArr.put(msgJSONObj);
							break;
						} else {
							String id = msgObj.has("paramName") && msgObj.get("paramName") != null
									&& msgObj.get("paramName").toString().length() > 0
											? msgObj.get("paramName").toString()
											: orderId;
							errJSONObj.put("id", id);
							errJSONObj.put("message", msgObj.get("message").toString());
							errResponseArr.put(errJSONObj);
						}
					}
					if (success) {
						finresponse.put(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
						finresponse.put(TemenosConstants.ORDER_ID, orderId);
						finresponse.put("message", msgResponseArr.toString());
						result.addParam("id", orderId);
						finresponse.put("opstatus", "0");
						finresponse.put("httpStatusCode", "200");

						return Utilities.constructResultFromJSONObject(finresponse);
					} else {
						finresponse.put(TemenosConstants.ORDER_ID, orderId);
						JSONArray additionalParamArr = new JSONArray();
						additionalParamArr.put(finresponse);
						return PortfolioWealthUtils.return_ErrorResponse(result, errResponseArr, additionalParamArr);
					}
				}
			} else {
				JSONObject errJSONObj = new JSONObject();
				JSONArray additionalParamArr = new JSONArray();
				errJSONObj.put("id", "ERR");
				errJSONObj.put("message", "Messages cannot be empty");
				errResponseArr.put(errJSONObj);
				return PortfolioWealthUtils.return_ErrorResponse(result, errResponseArr, additionalParamArr);
			}

		} catch (Exception e) {
			alert.prepareError("Error in CancelOrdersPostProcessor" + e).log();
		}
		return result;
	}
}
