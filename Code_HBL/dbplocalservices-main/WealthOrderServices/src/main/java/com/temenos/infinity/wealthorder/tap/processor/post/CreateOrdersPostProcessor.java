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
import com.temenos.infinity.api.wealthOrder.constants.OperationName;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;

/**
 * (INFO) If status is set as a part of the request , the operation is exited
 * else operation is executed.
 * 
 * @author balaji.krishnan
 *
 */

public class CreateOrdersPostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		diagnostic.prepareDebug("==========> CreateOrdersPostProcessor TAP - Entered ").log();
		try {
			JSONArray errResponseArr = new JSONArray();
			JSONArray msgResponseArr = new JSONArray();
			JSONObject responseJSON = new JSONObject();
			boolean error_flag = false;
			double totalFees = 0;
			Record bodyRecord = result.getRecordById("body");
			if (bodyRecord != null) {
				try {
					String feesArr[] = { "bp_1_amount_m", "bp_2_amount_m", "bp_3_amount_m", "bp_4_amount_m",
							"bp_5_amount_m", "bp_6_amount_m", "bp_7_amount_m", "bp_8_amount_m", "bp_9_amount_m" };
					Dataset ds = bodyRecord.getDatasetById("fees");
					if (ds != null) {
						JSONObject feesObj = new JSONObject();
						feesObj.put("Field",
								Utilities.convertStringToJSONArray(ResultToJSON.convertDataset(ds).toString()));
						JSONArray feesArray = feesObj.getJSONArray("Field");
						JSONObject feeObj = feesArray.getJSONObject(0);
						for (String fees : feesArr) {
							totalFees = totalFees + (StringUtils.isNotBlank(feeObj.get(fees).toString())
									? Double.parseDouble(feeObj.get(fees).toString())
									: 0);
						}
					}
				} catch (Exception e) {
					e.getMessage();
				} finally {
					String functionResultCode = null;
					if (StringUtils.isNotBlank(bodyRecord.getParamByName("funcResultCode").toString())) {
						functionResultCode = bodyRecord.getParamValueByName("funcResultCode").toString();
						// }
						Dataset ds = bodyRecord.getDatasetById("cases");
						if (ds != null) {
							JSONObject resultObj = new JSONObject();
							resultObj.put("Field",
									Utilities.convertStringToJSONArray(ResultToJSON.convertDataset(ds).toString()));
							JSONArray caseArray = resultObj.getJSONArray("Field");
							JSONObject msgJSONObj = new JSONObject();
							JSONObject errJSONObj = new JSONObject();
							if (caseArray != null && caseArray.length() > 0) {
								String[] colArray = new String[] { "caseCode", "DESCRIPTION", "portfolioCode",
										"CLARIFICATION_STATUS", "SEVERITY_LABEL" };
								for (int i = 0; i < caseArray.length(); i++) {
									JSONObject caseObj = caseArray.getJSONObject(i);
									msgJSONObj = new JSONObject();
									errJSONObj = new JSONObject();
									for (String key : colArray) {
										if (!caseObj.has(key)) {
											caseObj.put(key, "");
										}
										if (key.trim().equalsIgnoreCase("SEVERITY_LABEL")) {
											if (StringUtils.isNotBlank(caseObj.get(key).toString()) // Warning//Medium
													&& ((caseObj.get(key).toString().equalsIgnoreCase("High")
															|| (caseObj.get(key).toString()
																	.equalsIgnoreCase("Medium"))))) {
												error_flag = true;
												errJSONObj.put("id", caseObj.get("caseCode").toString());
												errJSONObj.put("message", caseObj.get("DESCRIPTION").toString());
												errResponseArr.put(errJSONObj);
											} else if (StringUtils.isNotBlank(caseObj.get(key).toString()) && ((caseObj
													.get(key).toString().equalsIgnoreCase("Low")
													|| (caseObj.get(key).toString().equalsIgnoreCase("Warning"))))) {
												msgJSONObj.put("id", caseObj.get("caseCode").toString());
												msgJSONObj.put("message", caseObj.get("DESCRIPTION").toString());
												msgResponseArr.put(msgJSONObj);
											}
										}
									}
								}

							}
						}
						if (error_flag) {
							JSONArray additionalParamArr = new JSONArray();
							diagnostic.prepareDebug("==========> CreateOrdersPostProcessor TAP - Exiting with error ").log();
							return PortfolioWealthUtils.return_ErrorResponse(result, errResponseArr,
									additionalParamArr);
						} else {
							responseJSON.put("opstatus", "0");
							responseJSON.put("httpStatusCode", "200");
							responseJSON.put("status", "success");
							if (msgResponseArr != null && msgResponseArr.length() > 0) {
								responseJSON.put("messageDetails", msgResponseArr.toString());
							}
							responseJSON.put("funcResultCode", functionResultCode);
							responseJSON.put("fees", totalFees);
							diagnostic.prepareDebug("==========> CreateOrdersPostProcessor TAP - Exiting with success ").log();
							return Utilities.constructResultFromJSONObject(responseJSON);
						}

					}
				}
			} else {
				Dataset ds = result.getDatasetById("errmessages");
				if (ds != null) {
					JSONObject resultObj = new JSONObject();
					resultObj.put("Field",
							Utilities.convertStringToJSONArray(ResultToJSON.convertDataset(ds).toString()));
					JSONArray msgsArray = resultObj.getJSONArray("Field");
					JSONObject errJSONObj = new JSONObject();
					if (msgsArray != null && msgsArray.length() > 0) {
						for (int i = 0; i < msgsArray.length(); i++) {
							JSONObject msgObj = msgsArray.getJSONObject(i);
							errJSONObj = new JSONObject();
							errJSONObj.put("id", msgObj.get("errparamName"));
							errJSONObj.put("message", msgObj.get("errmessagedisp"));
							errResponseArr.put(errJSONObj);
						}
						diagnostic.prepareDebug("==========> CreateOrdersPostProcessor TAP - Exiting with error ").log();
						return PortfolioWealthUtils.return_ErrorResponse(result, errResponseArr, new JSONArray());
					}
				} else {
					JSONArray additionalParamArr = new JSONArray();
					JSONObject errJSONObj = new JSONObject();
					errJSONObj.put("id", result.getParamValueByName("code"));
					errJSONObj.put("message", result.getParamValueByName("message"));
					errResponseArr.put(errJSONObj);
					diagnostic.prepareDebug("==========> CreateOrdersPostProcessor TAP - Exiting with error ").log();
					return PortfolioWealthUtils.return_ErrorResponse(result, errResponseArr, additionalParamArr);
				}
			}
		} catch (Exception e) {
			alert.prepareError("==========> CreateOrdersPostProcessor TAP - Error: " + e.getMessage()).log();
			e.getMessage();
		}
		return result;
	}

}
