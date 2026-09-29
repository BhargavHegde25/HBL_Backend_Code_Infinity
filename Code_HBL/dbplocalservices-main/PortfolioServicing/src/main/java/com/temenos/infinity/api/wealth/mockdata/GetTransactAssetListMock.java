package com.temenos.infinity.api.wealth.mockdata;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;


/**
 * (INFO) Prepares Result object from the mock data returned by the Util.
 *
 */
public class GetTransactAssetListMock implements JavaService2 {

	@SuppressWarnings("unchecked")
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Map<String, Object> inputMap = new HashMap<>();
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		Object customerIdobj = inputParams.get(TemenosConstants.CUSTOMERID);
		String customerId = null;
		if (customerIdobj != null) {
			customerId = inputParams.get(TemenosConstants.CUSTOMERID).toString();
			inputMap.put(TemenosConstants.CUSTOMERID, customerId);
		}

		JSONObject assetList = new JSONObject();
		assetList.put("opstatus", "0");
		assetList.put("httpStatusCode", "200");
		return Utilities.constructResultFromJSONObject(assetList);
	}

}
