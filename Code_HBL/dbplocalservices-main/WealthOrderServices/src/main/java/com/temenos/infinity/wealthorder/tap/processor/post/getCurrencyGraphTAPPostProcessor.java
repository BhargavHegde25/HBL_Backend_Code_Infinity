package com.temenos.infinity.wealthorder.tap.processor.post;

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
import com.temenos.infinity.api.wealthOrder.config.WealthAPIServices;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 *
 * @author padmasris
 *
 */
public class getCurrencyGraphTAPPostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		diagnostic.prepareDebug("==========> getCurrencyGraphTAPPostProcessor TAP - Entered ").log();
		try {
			// TODO Auto-generated method stub
			String flagHis = "false";
			JSONArray dataSet = new JSONArray();
			JSONArray dataArr = new JSONArray();
			JSONObject historicalDataJSON = new JSONObject();
			Dataset resultSet = result.getDatasetById("historicalData");
			if (resultSet != null) {
				dataArr = ResultToJSON.convertDataset(resultSet);
				for (int i = dataArr.length() - 1; i >= 0; i--) {
					dataSet.put(dataArr.get(i));
				}
				flagHis = "true";
			} else {
				dataSet = new JSONArray();
				flagHis = "false";
			}
			historicalDataJSON.put("historicalData", dataSet.toString());
			Result historicalDataResult = Utilities.constructResultFromJSONObject(historicalDataJSON);
			historicalDataResult.addOpstatusParam("0");
			historicalDataResult.addHttpStatusCodeParam("200");
			historicalDataResult.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			historicalDataResult.addParam("flagHis", flagHis);
			diagnostic.prepareDebug("==========> getCurrencyGraphTAPPostProcessor TAP -  Exiting with success ").log();
			return historicalDataResult;
		} catch (Exception e) {

			alert.prepareError("==========> getCurrencyGraphTAPPostProcessor TAP - Error: " + e.getMessage()).log();
		}
		return null;
	}
}
