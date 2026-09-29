/**
 * 
 */
package com.temenos.infinity.wealthorder.t24.processor.post;

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

/**
 * @author himaja.sridhar
 *
 */
public class GetInstrumentListT24PostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {
			String RICCode = "";
			JSONArray instrumentArray = new JSONArray();
			JSONObject responseJSON = new JSONObject();
			JSONObject instSearchJSON = new JSONObject();
			JSONArray altInsArray = new JSONArray();
			JSONObject altInsJSON = new JSONObject();

			Record headerRec = result.getRecordById("header");
			String statusVal = headerRec.getParamValueByName("status");
			if (statusVal != null && statusVal.trim().equalsIgnoreCase("success")) {
				Dataset ds = result.getDatasetById("body");
				if (ds != null) {
					JSONObject resultObj = new JSONObject();
					resultObj.put("Field",
							Utilities.convertStringToJSONArray(ResultToJSON.convertDataset(ds).toString()));
					JSONArray refnitivArray = resultObj.getJSONArray("Field");

					if (refnitivArray != null && refnitivArray.length() > 0) {

						String[] colArray = new String[] { "holdingsType", "instrumentId", "ISIN", "description" };

						for (int i = 0; i < refnitivArray.length(); i++) {
							instSearchJSON = refnitivArray.getJSONObject(i);

							for (String key : colArray) {
								if (!instSearchJSON.has(key)) {
									instSearchJSON.put(key, "");
								}
							}

							if (instSearchJSON.has("alternateInstruments")) {
								altInsArray = instSearchJSON.getJSONArray("alternateInstruments");
								if (altInsArray != null && altInsArray.length() > 0) {
									for (int k = 0; k < altInsArray.length(); k++) {
										altInsJSON = altInsArray.getJSONObject(k);

										RICCode = (altInsJSON.has("alternateInstrument")
												&& altInsJSON.get("alternateInstrument").toString() != null)
														? (altInsJSON.get("alternateInstrument").toString().trim()
																.equalsIgnoreCase("RICCODE")
																		? (altInsJSON.has("RICCode")
																				? altInsJSON.get("RICCode").toString()
																				: "")
																		: "")
														: "";
										if (RICCode.length() > 0) {
											break;
										}
									}
								}
								instSearchJSON.remove("alternateInstruments");
							}

							instSearchJSON.put(TemenosConstants.RICCODE, RICCode);
							instSearchJSON.put("application", "SC");
							instrumentArray.put(instSearchJSON);
						}

					}

				}
				responseJSON.put("scinstrumentList", instrumentArray);
				responseJSON.put("opstatus", "0");
				responseJSON.put("httpStatusCode", "200");
				responseJSON.put("status", statusVal);
			} else {
				Record errorRec = result.getRecordById("error");
				if (errorRec != null) {
					Record error = errorRec.getAllRecords().get(0);
					responseJSON.put("errormessage", error.getParamValueByName("message"));
				} else {
					responseJSON.put("errormessage", "");
				}
				responseJSON.put("status", statusVal);
			}
			return Utilities.constructResultFromJSONObject(responseJSON);
		} catch (Exception e) {

			alert.prepareError("Error while invoking GetInstrumentListT24PostProcessor - " + e.getMessage()).log();
		}

		return result;
	}

}
