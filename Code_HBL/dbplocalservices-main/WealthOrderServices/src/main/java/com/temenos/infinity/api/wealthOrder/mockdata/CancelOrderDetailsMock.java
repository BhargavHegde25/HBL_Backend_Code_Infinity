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
import com.temenos.infinity.api.wealthOrder.util.WealthMockUtil;

/**
 * (INFO) Prepares Result object from the mock data returned by the Util.
 *
 */

public class CancelOrderDetailsMock implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		WealthMockUtil mockUtil = new WealthMockUtil();
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];

		try {
			JSONObject resultJSON = mockUtil.cancelOrder(inputParams);
			return Utilities.constructResultFromJSONObject(resultJSON);
		}
		catch(Exception e) {
			alert.prepareError("Caught exception at invoke of cancelOrder: ", e).log();
			return ErrorCodeEnum.ERR_20040.setErrorCode(new Result());
		}
	}

}
