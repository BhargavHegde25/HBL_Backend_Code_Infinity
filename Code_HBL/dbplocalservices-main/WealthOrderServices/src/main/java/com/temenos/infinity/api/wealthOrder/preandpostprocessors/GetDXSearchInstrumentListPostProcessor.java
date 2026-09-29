package com.temenos.infinity.api.wealthOrder.preandpostprocessors;

import java.util.Arrays;
import java.util.List;

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
import com.temenos.infinity.api.wealthOrder.config.WealthAPIServices;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * (INFO) If status is set as a part of the request , the operation is exited
 * else operation is executed.
 * 
 * @author balaji.krishnan
 *
 */

public class GetDXSearchInstrumentListPostProcessor implements DataPostProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

		try {
			JSONObject responseJSON = new JSONObject();
			JSONObject instSearchJSON = new JSONObject();
			JSONObject fabricresponseObj = new JSONObject();
			Record headerRec = result.getRecordById("header");
			String statusVal = headerRec.getParamValueByName("status");
			JSONArray instrumentArray = new JSONArray();
			if (statusVal != null && statusVal.trim().equalsIgnoreCase("success")) {
				Dataset ds = result.getDatasetById("body");
				if (ds != null) {
					JSONObject resultObj = new JSONObject();
					resultObj.put("Field",
							Utilities.convertStringToJSONArray(ResultToJSON.convertDataset(ds).toString()));
					JSONArray refnitivArray = resultObj.getJSONArray("Field");

					if (refnitivArray != null && refnitivArray.length() > 0) {

						List<String> dxsearchcolArray = Arrays.asList("holdingsType", "instrumentId", "ISIN",
								"underlying", TemenosConstants.RICCODE);

						for (int i = 0; i < refnitivArray.length(); i++) {
							fabricresponseObj = refnitivArray.getJSONObject(i);
							instSearchJSON = new JSONObject();
							for (String key : dxsearchcolArray) {
								if (!fabricresponseObj.has(key)) {
									fabricresponseObj.put(key, "");
								}
								if (key.equalsIgnoreCase("underlying")) {
									instSearchJSON.put("description", fabricresponseObj.get(key));
								} else {
									instSearchJSON.put(key, fabricresponseObj.get(key));
								}
							}
							instSearchJSON.put("application", "DXMaster");
							instrumentArray.put(instSearchJSON);
						}

					}

				}
				responseJSON.put("dxinstrumentList", instrumentArray);
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

			alert.prepareError("Error while invoking GetDXSearchInstrumentListPostProcessor - "
					+ WealthAPIServices.WEALTHSERVICESORCHESTRATION.getOperationName() + "  : " + e).log();
		}

		return result;
	}

}
