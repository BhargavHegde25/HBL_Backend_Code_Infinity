/**
 * 
 */
package com.temenos.infinity.api.wealthOrder.preandpostprocessors;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.konylabs.middleware.dataobject.Dataset;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * @author balaji k.k
 *
 */
public class GetOrchSearchFavouritesPostProcessor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

		JSONArray instrumentArray = new JSONArray();
		JSONObject responseJSON = new JSONObject();
		String description = "";
		try {
			if (result.hasParamByName("error") || (result.hasParamByName(TemenosConstants.STATUS)
					&& result.getParamValueByName(TemenosConstants.STATUS).equalsIgnoreCase("Failure"))) {
				result.removeDatasetById("favoriteInstruments");
				result.removeDatasetById("instrumentList");
				return result;
			}
			ArrayList<String> favouriteInstrumentsList = new ArrayList<String>();
			Dataset scsearch_ds = result.getDatasetById("favoriteInstruments");
			if (scsearch_ds != null) {
				JSONObject scsearchObj = new JSONObject();
				scsearchObj.put("Field",
						Utilities.convertStringToJSONArray(ResultToJSON.convertDataset(scsearch_ds).toString()));
				JSONArray scsearchArray = scsearchObj.getJSONArray("Field");
				if (scsearchArray != null && scsearchArray.length() > 0) {
					for (int i = 0; i < scsearchArray.length(); i++) {
						JSONObject obj = scsearchArray.getJSONObject(i);
						favouriteInstrumentsList.add(obj.get(TemenosConstants.INSTRUMENTID).toString());
					}
				}
			}

			Dataset dxsearch_ds = result.getDatasetById("instrumentList");
			if (dxsearch_ds != null) {
				JSONObject dxsearchObj = new JSONObject();
				dxsearchObj.put("Field",
						Utilities.convertStringToJSONArray(ResultToJSON.convertDataset(dxsearch_ds).toString()));
				JSONArray dxsearchArray = dxsearchObj.getJSONArray("Field");
				if (dxsearchArray != null && dxsearchArray.length() > 0) {
					for (int i = 0; i < dxsearchArray.length(); i++) {
						JSONObject obj = dxsearchArray.getJSONObject(i);
						description = obj.get(TemenosConstants.INSTRUMENTID).toString();
						if (favouriteInstrumentsList.contains(description)) {
							obj.put("isFavorite", true);
						} else {
							obj.put("isFavorite", false);
						}
						if(!obj.has(TemenosConstants.PERCENTAGECHANGE)) {
							obj.put(TemenosConstants.PERCENTAGECHANGE, "");
						}
						if(!obj.has(TemenosConstants.SECCCY)) {
							obj.put(TemenosConstants.SECCCY, "");
						}
						if(!obj.has(TemenosConstants.MARKETPRICE)) {
							obj.put(TemenosConstants.MARKETPRICE, "");
						}	
						instrumentArray.put(obj);
					}
				}
			}

			responseJSON.put("searchfavoriteInstruments", instrumentArray);
			responseJSON.put("opstatus", "0");
			responseJSON.put("httpStatusCode", "200");
			responseJSON.put("status", TemenosConstants.SUCCESS);

			return Utilities.constructResultFromJSONObject(responseJSON);

		} catch (Exception e) {
			e.getMessage();
			return response;
		}

	}

}
