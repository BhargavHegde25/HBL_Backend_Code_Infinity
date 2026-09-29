package com.temenos.infinity.api.wealth.mockdata;

import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.ErrorCodeEnum;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealth.util.WealthMockUtil;

/**
 * (INFO) Prepares Result object from the mock data returned by the Util.
 *
 */
public class GetInstrumentMinimalMock implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("unchecked")
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		WealthMockUtil mockUtil = new WealthMockUtil();
		Map<String, Object> inputMap = new HashMap<>();
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		Object instrumentIdObj = inputParams.get(TemenosConstants.INSTRUMENTID);
		String instrumentId = null;
		if (instrumentIdObj != null) {
			instrumentId = inputParams.get(TemenosConstants.INSTRUMENTID).toString();
			inputMap.put(TemenosConstants.INSTRUMENTID, instrumentId);
		}
		try {
			JSONObject response1 = new JSONObject();
			JSONArray instrumentMinimal = mockUtil.getInstrumentMinimal(instrumentId);
			JSONObject instrumentAssets = mockUtil.getInstrumentAssets(instrumentId);
			response1.put("instrumentMinimal", instrumentMinimal);
			response1.put("instrumentAssets", instrumentAssets);
			if (instrumentMinimal != null) {
			response1.put("opstatus", "0");
			response1.put("httpStatusCode", "200");
			}
			return Utilities.constructResultFromJSONObject(response1);
		} catch (Exception e) {
			alert.prepareError("Caught exception at invoke of getInstrumentMinimal: ", e).log();
			return ErrorCodeEnum.ERR_20040.setErrorCode(new Result());
		}
	}

}
