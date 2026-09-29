package com.kony.dbputilities.customersecurityservices;

import java.util.HashMap;

import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;

public class GetPasswordLockoutSettingFromDB implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {

		HashMap<String, String> hashMap = new HashMap<>();
		try {
			Result response = HelperMethods.callApi(dcRequest, hashMap, HelperMethods.getHeaders(dcRequest),
					URLConstants.PASSWORDLOCKOUTSETTINGS_GET);
			return response;
		} catch (Exception e) {
			Dataset ds = new Dataset();
			ds.setId("passwordlockoutsettings");
			Result result = new Result();
			result.addDataset(ds);
			return result;
		}

	}
}
