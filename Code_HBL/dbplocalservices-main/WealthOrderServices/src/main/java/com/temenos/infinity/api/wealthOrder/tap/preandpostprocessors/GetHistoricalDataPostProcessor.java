package com.temenos.infinity.api.wealthOrder.tap.preandpostprocessors;

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
import com.temenos.infinity.api.wealthOrder.config.WealthAPIServices;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 *
 * @author padmasris
 *
 */
public class GetHistoricalDataPostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {
			// TODO Auto-generated method stub
			JSONArray dataSet = new JSONArray();
			JSONArray dataArr = new JSONArray();
			JSONObject historicalDataJSON = new JSONObject();
			Dataset resultSet = result.getDatasetById("historicalData");
			if (resultSet != null) {
				dataArr = ResultToJSON.convertDataset(resultSet);
				for(int i=dataArr.length()-1;i>=0;i--)
				{
					dataSet.put(dataArr.get(i));
				}
			} else {
				dataSet = new JSONArray();
			}
			historicalDataJSON.put("historicalData", dataSet);
			
			Result historicalDataResult = Utilities.constructResultFromJSONObject(historicalDataJSON);
			historicalDataResult.addOpstatusParam("0");
			historicalDataResult.addHttpStatusCodeParam("200");
			historicalDataResult.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			return historicalDataResult;
		} catch (Exception e) {

			alert.prepareError("Error while invoking GetHistoricalDataPostProcessor - "
					+ WealthAPIServices.WEALTHSERVICESORCHESTRATION.getOperationName() + "  : " + e).log();
		}
		return null;
	}
}
