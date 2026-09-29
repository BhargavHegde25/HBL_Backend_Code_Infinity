/**
 * 
 */
package com.temenos.infinity.wealth.tap.processor.post;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealth.tap.preandpostprocessors.GetDashboardGraphDataPostProcessor;
import com.temenos.infinity.api.wealth.util.WealthMockUtil;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * @author muthukumarv
 *
 */
public class GetAssetListTAPPostProcessor implements DataPostProcessor2 {
	
	WealthMockUtil wealthMockUtil = new WealthMockUtil();
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {
			diagnostic.prepareDebug("==========> GetAssetListTAPPostProcessor TAP - Entered ").log();
			JSONObject AssetListJSON = new JSONObject();
			Dataset bodyData = result.getDatasetById("body");

			JSONArray assetListArr = new JSONArray();
			String referenceCurr = null, unRealizedPL = "";
			Double marketVal = 0.0, totalMarketVal = 0.0, unRealizedPLAmount = 0.0;
			if (bodyData != null) {
				JSONArray assetLis = ResultToJSON.convertDataset(bodyData);
				diagnostic.prepareDebug("==========> GetAssetListTAPPostProcessor TAP - No. of records returned initially: " + assetLis.length() ).log();
				for (int i = 0; i < assetLis.length(); i++) {
					JSONObject assetJSON = assetLis.getJSONObject(i);
					unRealizedPLAmount = assetJSON.get("Total_PL").toString() != null
							? Double.parseDouble(assetJSON.get("Total_PL").toString())
							: 0;
					if (!assetJSON.getString("assetDesc").equalsIgnoreCase("True")) {
					} else {
						assetJSON.remove("Total_PL");
						assetJSON.remove("assetDesc");
						referenceCurr = assetJSON.getString("referenceCurrency");
						if (assetJSON.get("marketValue").toString().equals("0")
								|| assetJSON.get("marketValue") == null) {

						} else {
							marketVal = marketVal
									+ Double.parseDouble(assetJSON.get("marketValue").toString().trim().length() > 0
											? assetJSON.get("marketValue").toString()
											: "0");
							assetJSON.remove("referenceCurrency");
							if(!assetJSON.has("assetGroup")) {
								assetJSON.put("assetGroup", "Others");
							}
							assetListArr.put(assetJSON);
						}

					}
				}
				totalMarketVal = totalMarketVal + marketVal;
				if (unRealizedPLAmount >= 0) {
					unRealizedPL = "P";
				} else {
					unRealizedPL = "L";
				}
				diagnostic.prepareDebug("==========> GetAssetListTAPPostProcessor TAP - No. of records returned after filter: " + assetListArr.length() ).log();
			} else {
				assetListArr = new JSONArray();
			}
			assetListArr = sortArray(assetListArr);
			diagnostic.prepareDebug("==========> GetAssetListTAPPostProcessor TAP - No. of records returned after sort: " + assetListArr.length() ).log();
			AssetListJSON.put("assets", assetListArr);
			AssetListJSON.put("referenceCurrency", referenceCurr);
			//AssetListJSON.put("customerId", request.getParameter("customerId"));
			AssetListJSON.put("totalAssetValue", String.format("%.2f", totalMarketVal));
			AssetListJSON.put("unRealizedPLAmount", unRealizedPLAmount);
			AssetListJSON.put("unRealizedPL", unRealizedPL);
			AssetListJSON.put("unRealizedPLPercentage", "");
			//JSONObject AssetList = new JSONObject();
			//AssetList.put("AssetList", AssetListJSON);
			//Result AssetRes = Utilities.constructResultFromJSONObject(AssetList);
			String assetlist = AssetListJSON.toString();
			Result AssetRes = new Result();
			AssetRes.addOpstatusParam("0");
			AssetRes.addHttpStatusCodeParam("200");
			AssetRes.addParam("AssetList", assetlist);
			AssetRes.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			diagnostic.prepareDebug("==========> GetAssetListTAPPostProcessor TAP - Exited ").log();
			return AssetRes;

		} catch (Exception e) {
			alert.prepareError("==========> GetAssetListTAPPostProcessor TAP - Error: " + e.getMessage()).log();
			return null;
		}

	}
	public JSONArray sortArray(JSONArray array) {
		JSONArray sortedJSONArray = new JSONArray();
		List<JSONObject> jsonValues = new ArrayList<JSONObject>();
		for (int i = 0; i < array.length(); i++) {
			jsonValues.add(array.getJSONObject(i));
		}

		Collections.sort(jsonValues, new Comparator<JSONObject>() {
			private final String KEY_NAME = "marketValue";

			@Override
			public int compare(JSONObject a, JSONObject b) {
				if (a.has(KEY_NAME) && b.has(KEY_NAME) && a.get(KEY_NAME).toString().equalsIgnoreCase("")) {
					return (b.get(KEY_NAME).toString().equalsIgnoreCase("")) ? 0 : -1;
				}
				if (b.has(KEY_NAME) && b.get(KEY_NAME).toString().equalsIgnoreCase("")) {
					return 1;
				}
				Double str1 = null;
				Double str2 = null;
				str1 = Math.abs(Double.parseDouble(a.get(KEY_NAME).toString()));
				str2 = Math.abs(Double.parseDouble(b.get(KEY_NAME).toString()));
				return str1.compareTo(str2);
			}
		});

		for (int i = array.length() - 1; i >= 0; i--) {
			sortedJSONArray.put(jsonValues.get(i));
		}

		return sortedJSONArray;

	}
}
