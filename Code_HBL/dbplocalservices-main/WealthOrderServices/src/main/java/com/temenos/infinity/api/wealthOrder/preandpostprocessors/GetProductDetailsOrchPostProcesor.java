package com.temenos.infinity.api.wealthOrder.preandpostprocessors;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * (INFO) Builds the Result in the desired format for the Refinitiv and
 * Instruments T24/TAP service.
 * 
 * @author balaji.krishnan
 **/

public class GetProductDetailsOrchPostProcesor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

		try {

			String errMsg = result.getParamValueByName("errmsg");
			if (errMsg != null && !errMsg.equals("")) {
				JSONObject favouriteObj = new JSONObject();
				Result res = Utilities.constructResultFromJSONObject(favouriteObj);
				res.addOpstatusParam("0");
				res.addHttpStatusCodeParam("200");
				res.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				return res;
			}

			String application = request.getParameterValues(TemenosConstants.APPLICATION)[0];
			Dataset instrumentMinimal_ds = result.getDatasetById("instrumentMinimal");
			Record instrumentDetails = result.getRecordById("instrumentDetails");
			Record instrumentAssets = result.getRecordById("instrumentAssets");

			HashMap<String, String> instrument_map = new HashMap<String, String>();

			if (instrumentMinimal_ds != null) {
				List<Record> instrument_list = instrumentMinimal_ds.getAllRecords();
				Record instrumentMinimal = instrument_list.get(0);

				List<String> fields = Arrays.asList("marketPrice", "stockExchange", "instrumentName",
						"referenceCurrency", "quantity", "ISINCode", "instrumentId", "dateReceived", "RICCode",
						"instrumentCurrencyId", "netchange", "percentageChange", "timeReceived");

				fields.forEach(key -> {
					String value = "";
					if (instrumentDetails == null || instrumentDetails.isEmpty()) {
						value = instrumentMinimal.hasParamByName(key) ? instrumentMinimal.getParamValueByName(key) : "";
					} else if (instrumentDetails != null && !instrumentDetails.isEmpty()) {

						if (key.equalsIgnoreCase("marketPrice")) {

							if (instrumentDetails.getParamValueByName(key).equals("")
									|| Double.parseDouble(instrumentDetails.getParamValueByName(key).toString()) == 0) {

								if (instrumentDetails.getParamValueByName("closeRate").equals("") || Double.parseDouble(
										instrumentDetails.getParamValueByName("closeRate").toString()) == 0) {
									value = instrumentMinimal.hasParamByName(key)
											? instrumentMinimal.getParamValueByName(key)
											: "";
								} else {
									value = instrumentDetails.getParamValueByName("closeRate");
								}

							} else {
								value = instrumentDetails.getParamValueByName(key);
							}

						} else if (key.equalsIgnoreCase("instrumentName")) {
							value = instrumentMinimal.hasParamByName(key) ? instrumentMinimal.getParamValueByName(key)
									: "";
						} else {
							value = instrumentDetails.hasParamByName(key) ? instrumentDetails.getParamValueByName(key)
									: instrumentMinimal.hasParamByName(key) ? instrumentMinimal.getParamValueByName(key)
											: "";
						}

					}

					instrument_map.put(key, value);
				});

				if (application != null && application.equalsIgnoreCase("SC")) {
					instrument_map.put("isSecurityAsset", "true");
				} else {
					instrument_map.put("isSecurityAsset", "false");
				}
				if (instrumentAssets != null) {
					instrument_map.put("isSecurityAsset", instrumentAssets.getParamValueByName("isSecurityAsset"));
					Record assetTypes = instrumentAssets.getRecordById("assetTypes");
					if (assetTypes != null && assetTypes.getParamValueByName("assetType") != null) {
						instrument_map.put("assetType", assetTypes.getParamValueByName("assetType"));
					} else {
						instrument_map.put("assetType", "");
					}
				} else {
					instrument_map.put("assetType", "");
				}

				instrument_map.put(TemenosConstants.ISINEXCHANGE,
						(!instrument_map.get("ISINCode").toString().equalsIgnoreCase(""))
								? ((!instrument_map.get("stockExchange").toString().equalsIgnoreCase(""))
										? (instrument_map.get("ISINCode").toString() + " | "
												+ instrument_map.get("stockExchange").toString())
										: instrument_map.get("ISINCode").toString())
								: ((!instrument_map.get("stockExchange").toString().equalsIgnoreCase(""))
										? instrument_map.get("stockExchange")
										: ""));

				instrument_map.put("formatted_netperChange", formattedNetPerChange(instrument_map, "netchange") + " "
						+ formattedNetPerChange(instrument_map, "percentageChange"));

				instrument_map.put(TemenosConstants.APPLICATION, request.getParameter(TemenosConstants.APPLICATION));

				JSONObject obj = new JSONObject();
				obj.put("productDetails", instrument_map);
				result.appendResult(Utilities.constructResultFromJSONObject(obj));
			}
			return result;

		} catch (Exception e) {
			e.getMessage();
			result.addHttpStatusCodeParam(result.getHttpStatusCodeParamValue());
			result.addParam("errMsg", e.getMessage());
			return result;
		}

	}

	private String formattedNetPerChange(HashMap<String, String> instrument_map, String key) {
		String formatted_Val = "";
		if (key.equalsIgnoreCase("netchange")) {
			if (instrument_map.containsKey(key) && instrument_map.get(key).toString() != "") {
				if (Double.parseDouble(instrument_map.get(key).toString()) == 0) {
					formatted_Val = "+0.00";
				} else {
					formatted_Val = Double.parseDouble(instrument_map.get(key).toString()) > 0
							? ("+" + instrument_map.get(key))
							: (instrument_map.get(key));
				}
			}
		} else {
			if (instrument_map.containsKey(key) && instrument_map.get(key).toString() != "") {
				if (Double.parseDouble(instrument_map.get(key).toString()) == 0) {
					formatted_Val = "(+0.00%)";
				} else {
					formatted_Val = Double.parseDouble(instrument_map.get(key).toString()) > 0
							? ("(+" + instrument_map.get(key) + "%)")
							: ("(" + instrument_map.get(key) + "%)");
				}
			}
		}
		return formatted_Val;
	}

}