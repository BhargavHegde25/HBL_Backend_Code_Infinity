package com.temenos.infinity.api.wealthOrder.preandpostprocessors;

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
 * (INFO) If status is set as a part of the request , the operation is exited
 * else operation is executed.
 * 
 * @author balaji.krishnan
 *
 */

public class GetSearchOrchInstrumentListPostProcessor implements DataPostProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

		String search = request.getParameterValues(TemenosConstants.SEARCHBYINSTRUMENTNAME)[0];

		JSONArray instrumentArray = new JSONArray();
		JSONArray sortedJSON = new JSONArray();
		JSONObject responseJSON = new JSONObject();
		try {

			Dataset scsearch_ds = result.getDatasetById("scinstrumentList");
			if (scsearch_ds != null) {
				JSONObject scsearchObj = new JSONObject();
				scsearchObj.put("Field",
						Utilities.convertStringToJSONArray(ResultToJSON.convertDataset(scsearch_ds).toString()));
				JSONArray scsearchArray = scsearchObj.getJSONArray("Field");
				if (scsearchArray != null && scsearchArray.length() > 0) {
					for (int i = 0; i < scsearchArray.length(); i++) {
						JSONObject obj = scsearchArray.getJSONObject(i);
						instrumentArray.put(obj);
					}
				}
			}

			Dataset dxsearch_ds = result.getDatasetById("dxinstrumentList");
			if (dxsearch_ds != null) {
				JSONObject dxsearchObj = new JSONObject();
				dxsearchObj.put("Field",
						Utilities.convertStringToJSONArray(ResultToJSON.convertDataset(dxsearch_ds).toString()));
				JSONArray dxsearchArray = dxsearchObj.getJSONArray("Field");
				if (dxsearchArray != null && dxsearchArray.length() > 0) {
					for (int i = 0; i < dxsearchArray.length(); i++) {
						JSONObject obj = dxsearchArray.getJSONObject(i);
						instrumentArray.put(obj);
					}
				}
			}

			try {
				List<JSONObject> jsonValues = new ArrayList<JSONObject>();
				for (int i = 0; i < instrumentArray.length(); i++) {
					jsonValues.add(instrumentArray.getJSONObject(i));
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
				for (int i = 0; i < instrumentArray.length(); i++) {
					sortedJSON.put(jsonValues.get(i));
				}
/*
				if (search.equals("")) {
				} else {
					HoldingsListBackendDelegateImpl holdingsListBackendDelegate = new HoldingsListBackendDelegateImpl();
					sortedJSON = holdingsListBackendDelegate.returnSearch(sortedJSON, search);
				}*/
			} catch (Exception e) {
				e.getMessage();
				alert.prepareError("Error while invoking GetSearchOrchInstrumentListPostProcessor - "
						+ WealthAPIServices.WEALTHSERVICESORCHESTRATION.getOperationName() + "  : " + e).log();
			}

			responseJSON.put("instrumentList", sortedJSON);
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
