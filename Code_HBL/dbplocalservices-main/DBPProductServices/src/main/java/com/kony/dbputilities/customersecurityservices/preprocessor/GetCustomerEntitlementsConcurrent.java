package com.kony.dbputilities.customersecurityservices.preprocessor;

import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;

public class GetCustomerEntitlementsConcurrent implements JavaService2 {

	@Override
	public Object invoke(String arg0, Object[] arg1, DataControllerRequest dcRequest, DataControllerResponse arg3)
			throws Exception {
		Log4j2Configurator.getInstance();

		Map<String, String> input = new HashMap<>();

		input.put("id", HelperMethods.getCustomerIdFromSession(dcRequest));

		return HelperMethods.callApi(dcRequest, input, HelperMethods.getHeadersWithReportingParams(dcRequest),
				URLConstants.CUSTOMER_ENTITLEMENTS_CONCURRENT);
	}

}
