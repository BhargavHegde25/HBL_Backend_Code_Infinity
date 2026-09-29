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
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * 
 * @author balaji k.k
 *
 */

public class getSCAssetTypesPostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

		Record headerRec = result.getRecordById("header");
		String statusVal = headerRec.getParamValueByName("status");
		JSONObject instrumentAssetsObj = new JSONObject();
		JSONObject instrumentJSON = new JSONObject();
		JSONObject manipulationObj = new JSONObject();
		String type = "", interestRate = "", rating = "";
		if (statusVal.equals("success")) {

			Dataset bodyDataset = result.getDatasetById("body");

			if (bodyDataset != null) {
				JSONObject resultObj = new JSONObject();
				resultObj.put("Field",
						Utilities.convertStringToJSONArray(ResultToJSON.convertDataset(bodyDataset).toString()));
				JSONArray assettypeArray = resultObj.getJSONArray("Field");
				if (assettypeArray != null && assettypeArray.length() > 0) {

					List<String> stockscolArray = Arrays.asList(TemenosConstants.SECTOR, TemenosConstants.REGION,
							TemenosConstants.COUNTRY);

					List<String> bondscolArray = Arrays.asList(TemenosConstants.INTERESTRATE,
							TemenosConstants.NXTCOUPNDATE, TemenosConstants.MATURITYDATE, TemenosConstants.DURATION,
							TemenosConstants.MODIFIEDDURATION, TemenosConstants.RATING);

					List<String> fundscolArray = Arrays.asList(TemenosConstants.SECTOR, TemenosConstants.REGION,
							TemenosConstants.COUNTRY, TemenosConstants.ISSUER, "type");

					JSONObject assetObj = assettypeArray.getJSONObject(0);
					type = assetObj.has("type") ? assetObj.get("type").toString() : "";

					try {
						if (type.equalsIgnoreCase("Shares")) {
							manipulationObj = manipulation(assettypeArray, stockscolArray, result, bodyDataset,
									interestRate, rating, "Stock");
						} else if (type.equalsIgnoreCase("Bonds")) {

							Dataset interstRateDs = bodyDataset.getAllRecords().get(0).getDatasetById("interstRate");
							if (interstRateDs != null) {
								JSONObject interstRateObj = new JSONObject();
								interstRateObj.put("Field", Utilities.convertStringToJSONArray(
										ResultToJSON.convertDataset(interstRateDs).toString()));
								JSONArray interstRateArray = interstRateObj.getJSONArray("Field");
								if (interstRateArray != null) {
									JSONObject interestRateobj = interstRateArray.getJSONObject(0);
									interestRate = (interestRateobj != null && interestRateobj.has("interestRate"))
											? interestRateobj.get("interestRate").toString()
											: "";
								}
							}

							Dataset ratingDs = bodyDataset.getAllRecords().get(0).getDatasetById("ratingAgencies");
							if (ratingDs != null) {
								JSONObject ratingObj = new JSONObject();
								ratingObj.put("Field", Utilities
										.convertStringToJSONArray(ResultToJSON.convertDataset(ratingDs).toString()));
								JSONArray ratingArray = ratingObj.getJSONArray("Field");
								if (ratingArray != null) {
									JSONObject ratingobj = ratingArray.getJSONObject(0);
									rating = (ratingobj != null && ratingobj.has("rating"))
											? ratingobj.get("rating").toString()
											: "";
								}
							}

							manipulationObj = manipulation(assettypeArray, bondscolArray, result, bodyDataset,
									interestRate, rating, "Bond");
						} else if (type.toLowerCase().contains("fund")) {
							manipulationObj = manipulation(assettypeArray, fundscolArray, result, bodyDataset,
									interestRate, rating, "Fund");
						}
					} catch (Exception e) {
						alert.prepareError(e.toString()).log();
						e.getMessage();
						return null;
					}

				}
				instrumentAssetsObj.put("assetTypes", manipulationObj);
				instrumentAssetsObj.put("isSecurityAsset", true);
				instrumentJSON.put("instrumentAssets", instrumentAssetsObj);
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

	private JSONObject manipulation(JSONArray assettypeArray, List<String> colArray, Result result, Dataset bodyDataset,
			String interestRate, String rating, String type) {
		JSONObject assetTypeObj = new JSONObject();
		try {
			JSONObject fabricresponseObj = assettypeArray.getJSONObject(0);
			for (String key : colArray) {
				if (!fabricresponseObj.has(key)) {
					fabricresponseObj.put(key, "");
				}
				assetTypeObj.put(key, fabricresponseObj.get(key));
				if (key.equalsIgnoreCase("interestRate")) {
					assetTypeObj.put(key, interestRate);
				}
				if (key.equalsIgnoreCase("rating")) {
					assetTypeObj.put(key, rating);
				}
			}
			assetTypeObj.put("assetType", type);

		} catch (Exception e) {
			e.getMessage();
			alert.prepareError("Exception Occured at sc assettype manipulation", e).log();
		}
		return assetTypeObj;
	}
}
