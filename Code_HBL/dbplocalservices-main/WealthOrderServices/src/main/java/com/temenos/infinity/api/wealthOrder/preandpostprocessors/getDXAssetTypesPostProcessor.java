package com.temenos.infinity.api.wealthOrder.preandpostprocessors;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
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
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * 
 * @author balaji k.k
 *
 */

public class getDXAssetTypesPostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		if (request.getParameter("T24Favourite") == null
				&& request.getParameter(TemenosConstants.SEARCHBYINSTRUMENTNAME) != null) {
			GetDXSearchInstrumentListPostProcessor obj = new GetDXSearchInstrumentListPostProcessor();
			return obj.execute(result, request, response);
		} else {

			Record headerRec = result.getRecordById("header");
			String statusVal = headerRec.getParamValueByName("status");
			JSONObject instrumentAssetsObj = new JSONObject();
			JSONObject manipulationObj = new JSONObject();
			String type = "";
			if (statusVal.equals("success")) {
				JSONObject instrumentJSON = new JSONObject();
				Dataset bodyDataset = result.getDatasetById("body");

				if (bodyDataset != null) {
					JSONObject resultObj = new JSONObject();
					resultObj.put("Field",
							Utilities.convertStringToJSONArray(ResultToJSON.convertDataset(bodyDataset).toString()));
					JSONArray assettypeArray = resultObj.getJSONArray("Field");
					if (assettypeArray != null && assettypeArray.length() > 0) {

						List<String> futurescolArray = Arrays.asList(TemenosConstants.UNDERLYING,
								TemenosConstants.CONTRACTSIZE, TemenosConstants.EXPIRYDATE);

						List<String> optionscolArray = Arrays.asList(TemenosConstants.UNDERLYING,
								TemenosConstants.CONTRACTSIZE, TemenosConstants.EXPIRYDATE,
								TemenosConstants.STRIKEPRICE, TemenosConstants.OPTIONCLASS,
								TemenosConstants.OPTIONSTYLE);

						JSONObject assetObj = assettypeArray.getJSONObject(0);
						type = assetObj.has("type") ? assetObj.get("type").toString() : "";

						try {
							if (request.getParameter("T24Favourite") != null) {
								JSONArray instrumentArray = favouritemanipulation(assettypeArray, optionscolArray,
										result, bodyDataset, type);
								if (request.getParameter("T24DXMasterflag") != null) {
									instrumentJSON.put("instrumentMinimalDXMaster", instrumentArray);
								} else {
									instrumentJSON.put("instrumentMinimalDX", instrumentArray);
								}
								instrumentJSON.put("opstatus", "0");
								instrumentJSON.put("httpStatusCode", "200");
								instrumentJSON.put("status", statusVal);

								return Utilities.constructResultFromJSONObject(instrumentJSON);

							} else {
								if (type.equalsIgnoreCase("FUTURE")) {
									type = "Future";
									manipulationObj = manipulation(assettypeArray, futurescolArray, result, bodyDataset,
											type);
								} else if (type.equalsIgnoreCase("OPTION")) {
									type = "Option";
									manipulationObj = manipulation(assettypeArray, optionscolArray, result, bodyDataset,
											type);
								}
							}
						} catch (Exception e) {
							alert.prepareError(e.toString()).log();
							e.getMessage();
							return null;
						}

					}
					instrumentAssetsObj.put("assetTypes", manipulationObj);
					instrumentAssetsObj.put("isSecurityAsset", false);

					instrumentJSON.put("instrumentAssets", instrumentAssetsObj);

				} else {
					instrumentJSON.put("DXMaster", true);
				}

				instrumentJSON.put("opstatus", "0");
				instrumentJSON.put("httpStatusCode", "200");
				instrumentJSON.put("status", statusVal);

				return Utilities.constructResultFromJSONObject(instrumentJSON);

			} else {
				JSONObject response1 = new JSONObject();
				Record errorRec = result.getRecordById("error");
				response1.put("errormessage", errorRec.getParamValueByName("message"));
				Result errorRes = Utilities.constructResultFromJSONObject(response1);
				return errorRes;
			}

		}

	}

	private JSONObject manipulation(JSONArray assettypeArray, List<String> colArray, Result result, Dataset bodyDataset,
			String type) {
		JSONObject assetTypeObj = new JSONObject();
		try {

			JSONObject fabricresponseObj = assettypeArray.getJSONObject(0);
			for (String key : colArray) {
				if (!fabricresponseObj.has(key)) {
					fabricresponseObj.put(key, "");
				}
				assetTypeObj.put(key, fabricresponseObj.get(key));
			}
			assetTypeObj.put("assetType", type);

		} catch (Exception e) {
			e.getMessage();
			alert.prepareError("Exception Occured at dx assettype manipulation", e).log();
		}
		return assetTypeObj;
	}

	private JSONArray favouritemanipulation(JSONArray assettypeArray, List<String> colArray, Result result,
			Dataset bodyDataset, String type) {
		JSONArray instrumentArray = new JSONArray();
		String[] favcolArray = new String[] { TemenosConstants.INSTRUMENTID, TemenosConstants.INSTRUMENTNAME,
				TemenosConstants.STOCKEXCHANGE, TemenosConstants.ISIN, TemenosConstants.RICCODE, "marketPrice",
				"dateReceived", "referenceCurrency", "instrumentCurrencyId", TemenosConstants.QUANTITY };

		for (int i = 0; i < assettypeArray.length(); i++) {
			JSONObject instrObj = assettypeArray.getJSONObject(i);
			for (String key : favcolArray) {
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
				} else if (key.equalsIgnoreCase(TemenosConstants.INSTRUMENTNAME)) {
					instrObj.put(key, instrObj.get("underlying"));
				} else {
					instrObj.put(key, "");
				}
			}
			instrumentArray.put(instrObj);
		}
		return instrumentArray;
	}
}
