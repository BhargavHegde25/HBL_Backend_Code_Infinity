/**
 * 
 */
package com.temenos.infinity.api.wealthOrder.tap.preandpostprocessors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthOrder.backenddelegate.impl.HoldingsListBackendDelegateImpl;
import com.temenos.infinity.api.wealthOrder.config.WealthAPIServices;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * (INFO) Builds the result in desired format for the TAP service
 * 
 * @author himaja.sridhar
 *
 */
public class GetSearchInstrumentListPostProcessor implements DataPostProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {
			JSONObject searchJSON = new JSONObject();
			JSONArray searchArr = new JSONArray();
			JSONArray sortedJSON = new JSONArray();
			String idVal = "";

			String[] searchVal = request.getParameterValues(TemenosConstants.SEARCHBYINSTRUMENTNAME);
			String search = "";
			if (searchVal != null && searchVal.length > 0) {
				search = searchVal[0].trim();
			}
			/*
			 * Record headerRec = result.getRecordById("header"); Record paginationRec =
			 * headerRec.getRecordById("pagination"); String
			 * recCount=paginationRec.getParamValueByName("total").toString();
			 * if(Integer.parseInt(recCount) == 0) { Result errorRes = new Result();
			 * errorRes.addParam("errormessage", "No matching records returned in search");
			 * return errorRes; }
			 */
			Dataset resultSet = result.getDatasetById("body");
			if (resultSet != null) {

				JSONArray tapArr = Utilities
						.convertStringToJSONArray(ResultToJSON.convertDataset(resultSet).toString());
				String[] colArray = new String[] { TemenosConstants.DESCRIPTION, "holdingsType", "ISIN",
						TemenosConstants.RICCODE, TemenosConstants.INSTRUMENTID, "idCode", "natureE" };

				for (int i = 0; i < tapArr.length(); i++) {
					JSONObject searchObj = tapArr.getJSONObject(i);
					if (searchObj.getString("natureE").equalsIgnoreCase("Cash Account")) {
					} else {
						for (String key : colArray) {
							if (searchObj.has(key)) {
								if (key.equals("natureE")) {
									searchObj.put(TemenosConstants.APPLICATION,
											(searchObj.get("natureE").toString().equalsIgnoreCase("Stock")
													|| searchObj.get("natureE").toString()
															.equalsIgnoreCase("Fixed Income")
													|| searchObj.get("natureE").toString()
															.equalsIgnoreCase("Fund Share")) ? "SC" : "DX");
								} else {
								}
							} else {
								searchObj.put(key, "");
							}
						}
						searchObj.remove("natureE");
						searchArr.put(searchObj);
					}
				}
				List<JSONObject> jsonValues = new ArrayList<JSONObject>();
				for (int i = 0; i < searchArr.length(); i++) {
					jsonValues.add(searchArr.getJSONObject(i));
				}
				Collections.sort(jsonValues, new Comparator<JSONObject>() {
					private final String KEY_NAME = TemenosConstants.DESCRIPTION;

					@Override
					public int compare(JSONObject a, JSONObject b) {
						String str1 = new String();
						String str2 = new String();
						str1 = (String) a.get(KEY_NAME);
						str2 = (String) b.get(KEY_NAME);
						return str1.compareToIgnoreCase(str2);
					}

				});
				for (int i = 0; i < searchArr.length(); i++) {
					sortedJSON.put(jsonValues.get(i));
				}
				if (search.equals("")) {
				} else {

					HoldingsListBackendDelegateImpl holdingsListBackendDelegate = new HoldingsListBackendDelegateImpl();
					sortedJSON = holdingsListBackendDelegate.returnSearch(sortedJSON, search, "");

				}
				for (int k = 0; k < sortedJSON.length(); k++) {
					String id = sortedJSON.getJSONObject(k).getString("idCode");
					idVal = idVal.equals("") ? id : idVal.concat(",").concat(id);
				}
				searchJSON.put("idCnt", sortedJSON.length());
			} else {
				searchJSON.put("idCnt", "0");
			}
			searchJSON.put("instrumentList", sortedJSON);

			searchJSON.put("id", idVal);

			Result searchRes = Utilities.constructResultFromJSONObject(searchJSON);
			searchRes.addOpstatusParam("0");
			searchRes.addHttpStatusCodeParam("200");
			searchRes.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			return searchRes;
		} catch (Exception e) {

			alert.prepareError("Error while invoking GetInstrumentSearchPostProcessor - "
					+ WealthAPIServices.WEALTHSERVICESORCHESTRATION.getOperationName() + "  : " + e).log();
		}
		return null;
	}

}
