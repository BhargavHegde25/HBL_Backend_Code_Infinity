package com.temenos.infinity.api.wealthOrder.preandpostprocessors;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.dbputilities.util.CommonUtils;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * (INFO) Builds the Result in the desired format for the Refinitiv service.
 *
 */

public class GetInstrumentMinimalPostProcessor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		Record headerRec = result.getDatasetById("header").getRecord(0);
		String statusVal = headerRec.getParamValueByName("status");
		if (statusVal.equals("success")) {
			JSONObject instrumentJSON = new JSONObject();
			Dataset bodyDataset = result.getDatasetById("body");
			JSONArray instrumentArray = new JSONArray();
			if (bodyDataset != null) {
				List<Record> drecords = bodyDataset.getAllRecords();
				JSONArray bodyArray = new JSONArray();
				for (int j = 0; j < drecords.size(); j++) {
					JSONObject instrumentObject = new JSONObject();
					Record drecord = drecords.get(j);
					instrumentObject = CommonUtils.convertRecordToJSONObject(drecord);
					String RICCode = "";

					Dataset altInstrDs = drecord.getDatasetById("alternateInstruments");
					List<Record> drecordsAltInstr = altInstrDs.getAllRecords();
					for (int altIndex = 0; altIndex < drecordsAltInstr.size(); altIndex++) {
						JSONObject altInstrObj = new JSONObject();
						Record altRecord = drecordsAltInstr.get(altIndex);
						altInstrObj = CommonUtils.convertRecordToJSONObject(altRecord);
						if (altInstrObj.has("alternateInstrument")) {
							if (altInstrObj.get("alternateInstrument").toString().contains("RICCODE")) {
								RICCode = altInstrObj.has("alternateInstrumentId")
										? altInstrObj.getString("alternateInstrumentId")
										: "";
								break;
							}
						}
					}
					instrumentObject.put("RICCode", RICCode);
					bodyArray.put(instrumentObject);
				}

				String[] colArray = new String[] { TemenosConstants.INSTRUMENTID, TemenosConstants.INSTRUMENTNAME,
						TemenosConstants.STOCKEXCHANGE, TemenosConstants.ISIN, TemenosConstants.RICCODE, "marketPrice",
						"dateReceived", "referenceCurrency", "instrumentCurrencyId", TemenosConstants.QUANTITY };

				for (int i = 0; i < bodyArray.length(); i++) {
					JSONObject instrObj = bodyArray.getJSONObject(i);
					for (String key : colArray) {
						if (instrObj.has(key)) {
							if (key.equalsIgnoreCase("dateReceived")) {
								try {
									SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
									Date date = formatter.parse(instrObj.get(key).toString());
									formatter = new SimpleDateFormat("dd MMM yyyy");
									String formattedDate = formatter.format(date);
									instrObj.put(key, formattedDate);
								} catch (Exception e) {
									instrObj.put(key, instrObj.get(key));
								}
							} else {
								instrObj.put(key, instrObj.get(key));
							}
						} else {
							instrObj.put(key, "");
						}
					}
					instrumentArray.put(instrObj);
				}
			}
			instrumentJSON.put("instrumentMinimal", instrumentArray);

			Result instrRes = Utilities.constructResultFromJSONObject(instrumentJSON);
			instrRes.addOpstatusParam("0");
			instrRes.addHttpStatusCodeParam("200");
			instrRes.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			return instrRes;
		} else {
			JSONObject response1 = new JSONObject();
			Record errorRec = result.getRecordById("error");
			response1.put("errormessage", errorRec.getParamValueByName("message"));
			Result errorRes = Utilities.constructResultFromJSONObject(response1);
			return errorRes;
		}
	}
}
