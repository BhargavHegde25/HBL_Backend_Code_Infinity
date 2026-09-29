/**
 * 
 */
package com.temenos.infinity.api.wealthOrder.mockdata;

import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.ErrorCodeEnum;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthOrder.util.WealthMockUtil;

/**
 * (INFO) Prepares Result object from the mock data returned by the Util.
 * @author himaja.sridhar
 *
 */
public class GetHistoricalDataMock implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("unchecked")
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		WealthMockUtil mockUtil = new WealthMockUtil();
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		JSONObject response1 = new JSONObject();
		JSONArray objGraph = new JSONArray();
		String graphDuration = (String) inputParams.get(TemenosConstants.DATEORPERIOD);
		String currencyPair = (String) inputParams.get(TemenosConstants.CURRENCYPAIRS);
		try {
			if (currencyPair != null)
				objGraph = mockUtil.getCurrencyGraphData(inputParams);
			else
				objGraph = mockUtil.getInstrumentGraphData(inputParams);
			if (graphDuration != null)
				response1.put("historicalData", objGraph);
			response1.put("opstatus", "0");
			response1.put("httpStatusCode", "200");
			return Utilities.constructResultFromJSONObject(response1);
		} catch (Exception e) {
			alert.prepareError("Caught exception at invoke of getHistoricalData: ", e).log();
			return ErrorCodeEnum.ERR_20040.setErrorCode(new Result());
		}
	}

}
