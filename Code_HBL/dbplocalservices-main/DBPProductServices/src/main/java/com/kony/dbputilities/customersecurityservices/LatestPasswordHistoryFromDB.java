package com.kony.dbputilities.customersecurityservices;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;

public class LatestPasswordHistoryFromDB implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {

		Map<String, String> inputmap = HelperMethods.getInputParamMap(inputArray);
		String customerId = inputmap.get("customer_id");
		if (StringUtils.isBlank(customerId)) {
			Map<String, String> userdetails = HelperMethods.getUserFromIdentityService(dcRequest);
			customerId = userdetails.get("customer_id");
		}

		if (StringUtils.isBlank(customerId)) {
			return null;
		}

		HashMap<String, String> hashMap = new HashMap<>();
		hashMap.put("$filter", "Customer_id eq " + customerId);
		hashMap.put("$orderby", "createdts desc");
		hashMap.put("$top", "1");
		hashMap.put("$skip", "0");
		Result result = new Result();
		try {
			result = HelperMethods.callApi(dcRequest, hashMap, HelperMethods.getHeaders(dcRequest),
					URLConstants.PASSWORDHISTORY_GET);
			return result;
		} catch (HttpCallException e) {
			alert.prepareError(e.getMessage()).log();
			Dataset ds = new Dataset();
			ds.setId("passwordhistory");
			Result retResult = new Result();
			retResult.addDataset(ds);
			return retResult;
		}
	}
}
