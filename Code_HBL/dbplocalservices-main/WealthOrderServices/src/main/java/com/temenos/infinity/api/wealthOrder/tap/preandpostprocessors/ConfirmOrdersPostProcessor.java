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
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;
import com.temenos.infinity.api.wealthOrder.tap.preandpostprocessors.ConfirmOrdersPostProcessor;

/**
 * (INFO) If status is set as a part of the request , the operation is exited
 * else operation is executed.
 * 
 * @author balaji.krishnan
 *
 */

public class ConfirmOrdersPostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {
			Record bodyRecord = result.getRecordById("body");
			if (bodyRecord != null) {
				Dataset ds = bodyRecord.getDatasetById("publish");
				if (ds != null) {
					JSONObject resultObj = new JSONObject();
					JSONObject finresponse = new JSONObject();
					String orderRef = null;
					resultObj.put("Field",
							Utilities.convertStringToJSONArray(ResultToJSON.convertDataset(ds).toString()));
					JSONArray msgArray = resultObj.getJSONArray("Field");
					if (msgArray != null && msgArray.length() > 0) {
						for (int i = 0; i < msgArray.length(); i++) {
							JSONObject msgObj = msgArray.getJSONObject(i);
							if (StringUtils.isNotBlank(msgObj.get("message").toString())) {
								if (msgObj.get("message").toString()
										.equalsIgnoreCase("The session has been fully published.")) {
									Dataset orderds = bodyRecord.getDatasetById("orders");
									if (orderds != null) {
										JSONObject orderresultObj = new JSONObject();
										orderresultObj.put("Field", Utilities.convertStringToJSONArray(
												ResultToJSON.convertDataset(orderds).toString()));
										JSONArray orderidArray = orderresultObj.getJSONArray("Field");
										if (orderidArray != null && orderidArray.length() > 0) {
											JSONObject orderidObj = orderidArray.getJSONObject(0);
											if (StringUtils.isNotBlank(orderidObj.get("ORDER_CODE").toString())) {
												orderRef = orderidObj.get("ORDER_CODE").toString();
											}
											finresponse.put(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
											finresponse.put("opstatus", "0");
											finresponse.put("httpStatusCode", "200");
											finresponse.put("fees", 0);
											finresponse.put("id", orderRef);
										} else {//RS
											result.removeParamByName("errmsg");
											finresponse.put("errormessage", msgObj.get("message").toString());
											finresponse.put(TemenosConstants.STATUS, "failure");
										}
										return Utilities.constructResultFromJSONObject(finresponse);
									}
								}
							}
						}
					}
				}
			} else {
				JSONArray errResponseArr = new JSONArray();
				JSONObject errJSONObj = new JSONObject();
				JSONArray additionalParamArr = new JSONArray();
				errJSONObj.put("id", result.getParamValueByName("code"));
				errJSONObj.put("message", result.getParamValueByName("message"));
				errResponseArr.put(errJSONObj);
				return PortfolioWealthUtils.return_ErrorResponse(result, errResponseArr, additionalParamArr);
			}
		}

		catch (Exception e) {
			alert.prepareError("Error in CancelOrdersPostProcessor" + e).log();
		}
		return result;
	}

}
