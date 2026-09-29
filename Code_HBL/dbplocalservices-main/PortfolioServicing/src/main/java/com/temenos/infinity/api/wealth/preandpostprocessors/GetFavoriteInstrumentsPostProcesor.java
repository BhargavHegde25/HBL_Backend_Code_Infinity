package com.temenos.infinity.api.wealth.preandpostprocessors;

import java.text.DecimalFormat;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * (INFO) Builds the Result in the desired format for the Refinitiv service.
 **/

public class GetFavoriteInstrumentsPostProcesor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

		try {

			DecimalFormat df = new DecimalFormat("0.00");

			String[] favRicCodesArr = request.getParameterValues("favInstrumentCodes");
			String[] favIdsArr = request.getParameterValues("favInstrumentIds");
			String favRicCodes = null, favIds = null;

			if (favRicCodesArr != null && favRicCodesArr.length > 0) {
				favRicCodes = favRicCodesArr[0].trim();
			}
			if (favIdsArr != null && favIdsArr.length > 0) {
				favIds = favIdsArr[0].trim();
			}

			List<Param> dParams = result.getAllParams();
			dParams.get(0).getObjectValue().toString();
			Utilities.convertStringToJSONArray(dParams.get(0).getObjectValue().toString());
			JSONObject resultObj = new JSONObject();
			resultObj.put("Field", Utilities.convertStringToJSONArray(dParams.get(0).getObjectValue().toString()));
			JSONArray refnitivArray = resultObj.getJSONArray("Field");
			if (refnitivArray != null && refnitivArray.length() > 0) {
				JSONObject responseJSON = new JSONObject();
				JSONArray arrInstruments = new JSONArray();

				for (int i = 0; i < refnitivArray.length(); i++) {
					JSONObject finalresponse = new JSONObject();
					JSONObject refInstrument = refnitivArray.getJSONObject(i);
					String RICCode = refInstrument.getJSONObject("RequestKey").getString("Name");
					finalresponse.put("RIC", RICCode);
					if (refInstrument.opt("Fields") != null && !refInstrument.opt("Fields").equals(null)) {
						JSONArray instrumentFields = refInstrument.getJSONObject("Fields").getJSONArray("Field");
						for (int j = 0; j < instrumentFields.length(); j++) {
							JSONObject refnitivObj = instrumentFields.getJSONObject(j);
							if (refnitivObj.get("Name").toString().equalsIgnoreCase("ISIN_CODE")) {

								finalresponse.put("ISINCode",
										refnitivObj.has(refnitivObj.get("DataType").toString())
												? refnitivObj.get(refnitivObj.get("DataType").toString())
												: "");

							} else if (refnitivObj.get("Name").toString().equalsIgnoreCase("CF_NAME")) {

								finalresponse.put("instrumentName",
										refnitivObj.has(refnitivObj.get("DataType").toString())
												? refnitivObj.get(refnitivObj.get("DataType").toString())
												: "");

							} else if (refnitivObj.get("Name").toString().equalsIgnoreCase("CF_EXCHNG")) {

								finalresponse.put("exchange",
										refnitivObj.has(refnitivObj.get("DataType").toString())
												? refnitivObj.get(refnitivObj.get("DataType").toString())
												: "");

							} else if (refnitivObj.get("Name").toString().equalsIgnoreCase("CF_CURRENCY")) {

								finalresponse.put("referenceCurrency",
										refnitivObj.has(refnitivObj.get("DataType").toString())
												? refnitivObj.get(refnitivObj.get("DataType").toString())
												: "");

							} else if (refnitivObj.get("Name").toString().equalsIgnoreCase("CF_LAST")) {

								finalresponse.put("lastRate",
										refnitivObj.has(refnitivObj.get("DataType").toString())
												? refnitivObj.get(refnitivObj.get("DataType").toString())
												: "0.00");

							} else if (refnitivObj.get("Name").toString().equalsIgnoreCase("CF_NETCHNG")) {

								finalresponse.put("netchange",
										refnitivObj.has(refnitivObj.get("DataType").toString())
												? refnitivObj.get(refnitivObj.get("DataType").toString())
												: "0.00");

							} else if (refnitivObj.get("Name").toString().equalsIgnoreCase("PCTCHNG")) {

								double perValue = Double
										.parseDouble(refnitivObj.has(refnitivObj.get("DataType").toString())
												? refnitivObj.get(refnitivObj.get("DataType").toString()).toString()
												: "0.00");

								finalresponse.put("percentageChange", df.format(perValue));

							} else if (refnitivObj.get("Name").toString().equalsIgnoreCase("CF_DATE")) {

								finalresponse.put("dateReceived",
										refnitivObj.has(refnitivObj.get("DataType").toString())
												? refnitivObj.get(refnitivObj.get("DataType").toString())
												: "00:00:00");

							} else if (refnitivObj.get("Name").toString().equalsIgnoreCase("CF_TIME")) {

								finalresponse.put("timeReceived",
										refnitivObj.has(refnitivObj.get("DataType").toString())
												? refnitivObj.get(refnitivObj.get("DataType").toString())
												: "00:00:00");

							} else if (refnitivObj.get("Name").toString().equalsIgnoreCase("CF_BID")) {

								double perValue = Double
										.parseDouble(refnitivObj.has(refnitivObj.get("DataType").toString())
												? refnitivObj.get(refnitivObj.get("DataType").toString()).toString()
												: "0.00");

								finalresponse.put("bidRate", df.format(perValue));
							} else if (refnitivObj.get("Name").toString().equalsIgnoreCase("CF_ASK")) {

								double perValue = Double
										.parseDouble(refnitivObj.has(refnitivObj.get("DataType").toString())
												? refnitivObj.get(refnitivObj.get("DataType").toString()).toString()
												: "0.00");

								finalresponse.put("askRate", df.format(perValue));
							} else if (refnitivObj.get("Name").toString().equalsIgnoreCase("CF_VOLUME")) {

								int intValue = Integer.parseInt(refnitivObj.has(refnitivObj.get("DataType").toString())
										? refnitivObj.get(refnitivObj.get("DataType").toString()).toString()
										: "0");

								finalresponse.put("volume", intValue);

							} else if (refnitivObj.get("Name").toString().equalsIgnoreCase("CF_CLOSE")) {

								double perValue = Double
										.parseDouble(refnitivObj.has(refnitivObj.get("DataType").toString())
												? refnitivObj.get(refnitivObj.get("DataType").toString()).toString()
												: "0.00");

								finalresponse.put("closeValue", df.format(perValue));
							}
						}

						arrInstruments.put(finalresponse);
					}
				}

				responseJSON.put("refinitivInstruments", arrInstruments);
				responseJSON.put("opstatus", "0");
				responseJSON.put("httpStatusCode", "200");
				return Utilities.constructResultFromJSONObject(responseJSON);
			}
		} catch (Exception e) {
			e.getMessage();
		}

		return null;
	}

}
