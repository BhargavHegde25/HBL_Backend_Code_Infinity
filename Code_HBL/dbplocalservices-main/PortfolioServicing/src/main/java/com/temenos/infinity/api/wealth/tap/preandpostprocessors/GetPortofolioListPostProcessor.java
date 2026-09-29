/**
 * 
 */
package com.temenos.infinity.api.wealth.tap.preandpostprocessors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
 * @author himaja.sridhar
 *
 */
public class GetPortofolioListPostProcessor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

		JSONObject portfolioJSON = new JSONObject();
		JSONArray portfolioArr = new JSONArray();
		JSONObject portfolioListObj = new JSONObject();

		try {
			Dataset bodyDataset = result.getDatasetById("body");
			if (bodyDataset == null) {
				portfolioListObj.put("portfolioList", portfolioArr);
				portfolioJSON.put("PortfolioList", portfolioListObj);
				Result portfolioRes = Utilities.constructResultFromJSONObject(portfolioJSON);
				portfolioRes.addOpstatusParam("0");
				portfolioRes.addHttpStatusCodeParam("200");
				portfolioRes.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				portfolioRes.addParam(TemenosConstants.ISTAPINTEGRATION, "true");
				return portfolioRes;
			}
			List<Record> drecords = bodyDataset.getAllRecords();
			JSONArray bodyArray = new JSONArray();
			for (int j = 0; j < drecords.size(); j++) {
				JSONObject portfolioObj = new JSONObject();
				Record drecord = drecords.get(j);
				portfolioObj = CommonUtils.convertRecordToJSONObject(drecord);
				if (portfolioObj.has(TemenosConstants.PORTFOLIOID)
						&& (portfolioObj.has("statusE") && portfolioObj.get("statusE") != null
								&& !portfolioObj.get("statusE").toString().equalsIgnoreCase("Closed"))) {
					bodyArray.put(portfolioObj);
				}
			}
			String unRealizedPLAmount = "", unRealizedPL = "";

			String[] colArray = new String[] { TemenosConstants.PORTFOLIOID, TemenosConstants.CUSTOMERID,
					TemenosConstants.REFERENCECURRENCY, TemenosConstants.ACCOUNTNAME, TemenosConstants.MARKETVALUE,
					TemenosConstants.PRIMARYHOLDER, TemenosConstants.JOINTHOLDERS, TemenosConstants.INVESTMENTTYPE,
					TemenosConstants.JOINTHOLDERID, "portfolioManagedSince", "unRealizedPLAmount", "portfolioCCY","portfolioCode" };

			for (int i = 0; i < bodyArray.length(); i++) {
				JSONObject portfolioObj = bodyArray.getJSONObject(i);
				for (String key : colArray) {
					if (portfolioObj.has(key)) {
						portfolioObj.put(key, portfolioObj.get(key));
						if (key.equalsIgnoreCase(TemenosConstants.PORTFOLIOID)) {
							portfolioObj.put(TemenosConstants.ACCOUNTID, portfolioObj.get(key));
						}
						if (key.trim().equalsIgnoreCase("unRealizedPLAmount")) {

							unRealizedPLAmount = portfolioObj.get(key).toString().trim().length() > 0
									? (portfolioObj.get(key).toString())
									: "0.00";

							unRealizedPL = Double.parseDouble(unRealizedPLAmount) >= 0 ? "P" : "L";
							portfolioObj.put("unRealizedPL", unRealizedPL);
							portfolioObj.put(key, String.valueOf(Math.abs(Double.parseDouble(unRealizedPLAmount))));
						}

					} else {
						portfolioObj.put(key, "");
					}
					portfolioObj.put("isJointAccount", "false");
					portfolioObj.put("unRealizedPLPercentage", "");
				}
				portfolioArr.put(portfolioObj);

			}
			if (portfolioArr != null && portfolioArr.length() > 0) {
				portfolioArr = sortArray(portfolioArr);
			}
			portfolioListObj.put("portfolioList", portfolioArr);
			portfolioJSON.put("PortfolioList", portfolioListObj);
			Result portfolioRes = Utilities.constructResultFromJSONObject(portfolioJSON);
			portfolioRes.addOpstatusParam("0");
			portfolioRes.addHttpStatusCodeParam("200");
			portfolioRes.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			portfolioRes.addParam(TemenosConstants.ISTAPINTEGRATION, "true");
			return portfolioRes;
		} catch (Exception e) {
			e.getMessage();
		}
		return portfolioListObj;
	}

	public JSONArray sortArray(JSONArray array) {
		JSONArray sortedJSON = new JSONArray();
		List<JSONObject> jsonValues = new ArrayList<JSONObject>();
		for (int i = 0; i < array.length(); i++) {
			jsonValues.add(array.getJSONObject(i));
		}
		Collections.sort(jsonValues, new Comparator<JSONObject>() {
			private final String KEY_NAME = TemenosConstants.PORTFOLIOID;

			@Override
			public int compare(JSONObject a, JSONObject b) {
				String str1 = new String();
				String str2 = new String();
				str1 = a.has(KEY_NAME) ? (String) a.get(KEY_NAME) : "";
				str2 = b.has(KEY_NAME) ? (String) b.get(KEY_NAME) : "";
				return str1.compareToIgnoreCase(str2);
			}

		});
		for (int i = 0; i < array.length(); i++) {
			sortedJSON.put(jsonValues.get(i));
		}
		return sortedJSON;

	}

}
