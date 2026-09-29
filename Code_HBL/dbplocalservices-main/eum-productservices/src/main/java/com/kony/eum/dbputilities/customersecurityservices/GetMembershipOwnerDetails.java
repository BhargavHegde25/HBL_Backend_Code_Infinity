package com.kony.eum.dbputilities.customersecurityservices;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetMembershipOwnerDetails implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.MEMBERSHIPOWNER_GET);
		}
		return result;
	}

	private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result) {
		String customerID = inputParams.get("Customer_id");
		String membershipID = inputParams.get("Membership_id");
		if (StringUtils.isBlank(membershipID)) {
			membershipID = inputParams.get("membershipID");
		}
		StringBuilder sb = new StringBuilder();
		if (StringUtils.isNotBlank(customerID)) {
			sb.append("Customer_id").append(DBPUtilitiesConstants.EQUAL).append(customerID);
		} else if (StringUtils.isNotBlank(membershipID)) {
			sb.append("Membership_id").append(DBPUtilitiesConstants.EQUAL).append(membershipID);
		}
		inputParams.put(DBPUtilitiesConstants.FILTER, sb.toString());
		return true;
	}
}