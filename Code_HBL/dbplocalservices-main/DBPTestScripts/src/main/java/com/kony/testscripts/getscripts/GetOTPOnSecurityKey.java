package com.kony.testscripts.getscripts;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;

public class GetOTPOnSecurityKey implements JavaService2 {

	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {

		String securityKey = dcRequest.getParameter("securityKey");

		String filterQuery = "securityKey" + DBPUtilitiesConstants.EQUAL + securityKey;

		return HelperMethods.callGetApi(dcRequest, filterQuery, HelperMethods.getHeaders(dcRequest),
				URLConstants.OTP_GET);
	}
}
