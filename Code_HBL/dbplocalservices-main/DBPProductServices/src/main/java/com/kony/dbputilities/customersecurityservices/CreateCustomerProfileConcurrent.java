package com.kony.dbputilities.customersecurityservices;

import java.util.Map;

import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class CreateCustomerProfileConcurrent implements JavaService2 {
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		String personalInformation = dcRequest.getParameter("personalInformation");
		String contactInformation = dcRequest.getParameter("contactInformation");
		String addressInformation = dcRequest.getParameter("addressInformation");
		String backendIdentifierInfo = dcRequest.getParameter("backendIdentifierInfo");

		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		inputParams.put("personalInformation", personalInformation);
		inputParams.put("contactInformation", contactInformation);
		inputParams.put("addressInformation", addressInformation);
		inputParams.put("backendIdentifierInfo", backendIdentifierInfo);

		result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
				URLConstants.CREATE_CUTOMERPROFILE_CONCURRENT);
		return result;
	}

}
