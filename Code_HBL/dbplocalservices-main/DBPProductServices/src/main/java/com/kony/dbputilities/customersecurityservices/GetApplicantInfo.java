package com.kony.dbputilities.customersecurityservices;

import java.util.Map;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetApplicantInfo implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		Result applicant = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
				URLConstants.CUSTOMER_APPLICANT_INFO);
		Result addresses = getApplicantAddress(dcRequest, inputParams);
		postProcess(applicant, addresses);
		return applicant;
	}

	private void postProcess(Result applicant, Result addresses) {
		if (HelperMethods.hasRecords(applicant)) {
			applicant.getAllDatasets().get(0).setId("applicantinfo");
			if (HelperMethods.hasRecords(addresses)) {
				addresses.getAllDatasets().get(0).setId("addresses");
				applicant.addDataset(addresses.getAllDatasets().get(0));
			}
			HelperMethods.setSuccessMsg("Applicant found.", applicant);
		} else {
			ErrorCodeEnum.ERR_10136.setErrorCode(applicant);
		}
	}

	private Result getApplicantAddress(DataControllerRequest dcRequest, Map<String, String> inputParams)
			throws HttpCallException {
		String userId = inputParams.get("Customer_id");
		StringBuilder filter = new StringBuilder();
		filter.append("CustomerId").append(DBPUtilitiesConstants.EQUAL).append(userId);
		filter.append(DBPUtilitiesConstants.AND).append("Type_id").append(DBPUtilitiesConstants.EQUAL)
				.append("ADR_TYPE_HOME");
		return HelperMethods.callGetApi(dcRequest, filter.toString(), HelperMethods.getHeaders(dcRequest),
				URLConstants.CUSTOMER_ADDRESS_MB_VIEW_GET);
	}
}
