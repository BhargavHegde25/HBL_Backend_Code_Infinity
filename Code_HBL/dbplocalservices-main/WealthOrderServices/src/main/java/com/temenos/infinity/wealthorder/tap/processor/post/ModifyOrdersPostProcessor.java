package com.temenos.infinity.wealthorder.tap.processor.post;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
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


/**
 * 
 * @author muthukumarv
 *
 */

public class ModifyOrdersPostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		diagnostic.prepareDebug("==========> ModifyOrdersPostProcessor TAP - Entered ").log();
		try {

			String orderId = "";
			if (StringUtils.isNotBlank(request.getParameterValues(TemenosConstants.ORDER_ID)[0])) {
				orderId = request.getParameterValues(TemenosConstants.ORDER_ID)[0];
			}

			if (StringUtils.isBlank(request.getParameterValues(TemenosConstants.VALIDATEONLY)[0])) {
				Record bodyRecord = result.getRecordById("header");
				if (bodyRecord != null) {
					Dataset ds = bodyRecord.getDatasetById("messages");
					if (ds != null) {
						JSONObject resultObj = new JSONObject();
						resultObj.put("Field",
								Utilities.convertStringToJSONArray(ResultToJSON.convertDataset(ds).toString()));
						JSONArray msgArray = resultObj.getJSONArray("Field");
						if (msgArray != null && msgArray.length() > 0) {
							for (int i = 0; i < msgArray.length(); i++) {
								JSONObject msgObj = msgArray.getJSONObject(i);
								if (StringUtils.isNotBlank(msgObj.get("message").toString())) {
									if (msgObj.get("message").toString().equalsIgnoreCase("Update succeeded.")) {
										return return_SuccessResponse(result, orderId);
									}
								}
							}
						}
					}
				} else {
					diagnostic.prepareDebug("==========> ModifyOrdersPostProcessor TAP - Exiting with error ").log();
					return return_ErrResponse(result, orderId);
				}

			} else {
				diagnostic.prepareDebug("==========> ModifyOrdersPostProcessor TAP - Exiting with success ").log();
				return return_SuccessResponse(result, orderId);
			}
		}

		catch (Exception e) {
			alert.prepareError("==========> ModifyOrdersPostProcessor TAP - Error: " + e.getMessage()).log();
		}
		diagnostic.prepareDebug("==========> ModifyOrdersPostProcessor TAP - Exited ").log();
		return result;
	}

	private Result return_SuccessResponse(Result result, String orderId) {
		diagnostic.prepareDebug("==========> ModifyOrdersPostProcessor TAP - Entered return_SuccessResponse").log();
		JSONObject finresponse = new JSONObject();
		String errmsg = result.getParamValueByName("errmsg");
		if (errmsg != null && errmsg.length() > 0) {
			return return_ErrResponse(result, orderId);
		}
		String orderRef = null;
		double totalFees = 0;
		Record orderds = result.getRecordById("body");
		if (orderds != null) {
			orderRef = orderds.getParamValueByName("code");
			if (StringUtils.isNotBlank(orderRef)) {
				try {
					String feesArr[] = { "bp1AmountM", "bp2AmountM", "bp3AmountM", "bp4AmountM", "bp5AmountM",
							"bp6AmountM", "bp7AmountM", "bp8AmountM", "bp9AmountM" };
					for (String fees : feesArr) {
						totalFees = totalFees + (StringUtils.isNotBlank(orderds.getParamValueByName(fees))
								? Double.parseDouble(orderds.getParamValueByName(fees))
								: 0);
					}
				} catch (Exception e) {
					alert.prepareError("==========> ModifyOrdersPostProcessor TAP - Error: " + e.getMessage()).log();
					e.getMessage();
				} finally {
					finresponse.put(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
					finresponse.put("opstatus", "0");
					finresponse.put("httpStatusCode", "200");
					finresponse.put("fees", totalFees);
					finresponse.put("id", orderRef);
				}
			}
		} else {// RS
			finresponse.put("errormessage", result.getParamValueByName("message"));
			finresponse.put(TemenosConstants.STATUS, "failure");
		}
		diagnostic.prepareDebug("==========> ModifyOrdersPostProcessor TAP - Exiting return_SuccessResponse").log();
		return Utilities.constructResultFromJSONObject(finresponse);

	}

	private Result return_ErrResponse(Result result, String orderId) {
		diagnostic.prepareDebug("==========> ModifyOrdersPostProcessor TAP - Entered return_ErrResponse").log();
		JSONArray errResponseArr = new JSONArray();
		JSONObject finresponse = new JSONObject();
		Dataset ds = result.getDatasetById("messagesList");
		if (ds != null) {
			JSONObject resultObj = new JSONObject();
			resultObj.put("Field", Utilities.convertStringToJSONArray(ResultToJSON.convertDataset(ds).toString()));
			JSONArray msgArray = resultObj.getJSONArray("Field");
			if (msgArray != null && msgArray.length() > 0) {
				for (int i = 0; i < msgArray.length(); i++) {
					JSONObject errJSONObj = new JSONObject();
					JSONObject msgObj = msgArray.getJSONObject(i);
					if (StringUtils.isNotBlank(msgObj.get("message").toString())) {
						errJSONObj.put("id",
								msgObj.has("code") && msgObj.get("code") != null
										&& msgObj.get("code").toString().length() > 0 ? msgObj.get("code").toString()
												: orderId);
						errJSONObj.put("message", msgObj.get("message").toString());
						errResponseArr.put(errJSONObj);
					}
				}
				finresponse.put(TemenosConstants.ORDER_ID, orderId);
				JSONArray additionalParamArr = new JSONArray();
				additionalParamArr.put(finresponse);
				return PortfolioWealthUtils.return_ErrorResponse(result, errResponseArr, additionalParamArr);
			}
		} else {
			JSONObject errJSONObj = new JSONObject();
			JSONArray additionalParamArr = new JSONArray();
			errJSONObj.put("id", result.getParamValueByName("code"));
			errJSONObj.put("message", result.getParamValueByName("message"));
			errResponseArr.put(errJSONObj);
			return PortfolioWealthUtils.return_ErrorResponse(result, errResponseArr, additionalParamArr);
		}
		diagnostic.prepareDebug("==========> ModifyOrdersPostProcessor TAP - Exiting return_ErrResponse").log();
		return result;
	}

}
