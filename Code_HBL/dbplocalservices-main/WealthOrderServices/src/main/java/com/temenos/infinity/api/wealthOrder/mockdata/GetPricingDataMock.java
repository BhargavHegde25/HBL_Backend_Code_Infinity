/**
 * 
 */
package com.temenos.infinity.api.wealthOrder.mockdata;

import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
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
 * 
 * @author himaja.sridhar
 *
 */
public class GetPricingDataMock implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("unchecked")
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		WealthMockUtil mockUtil = new WealthMockUtil();
		Map<String, Object> inputMap = new HashMap<>();
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		Object ricCodeobj = inputParams.get(TemenosConstants.RICCODE);
		String ricCode = null;
		if (ricCodeobj != null) {
			ricCode = inputParams.get(TemenosConstants.RICCODE).toString();
			inputMap.put(TemenosConstants.RICCODE, ricCode);
		}
		try {
			JSONObject response1 = new JSONObject();
			JSONObject pricingDetails = mockUtil.getPricingDataDetails(ricCode);
			if (pricingDetails != null && pricingDetails.length() > 0) {
				response1.put("pricingDetails", pricingDetails);
				response1.put("opstatus", "0");
				response1.put("httpStatusCode", "200");
			}
			return Utilities.constructResultFromJSONObject(response1);
		} catch (Exception e) {
			alert.prepareError("Caught exception at invoke of getPricingData: ", e).log();
			return ErrorCodeEnum.ERR_20040.setErrorCode(new Result());
		}
	}

}
