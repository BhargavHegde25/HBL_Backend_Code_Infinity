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

public class GetEmpDetails implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String userId = inputParams.get("Customer_id");
		StringBuilder filter = new StringBuilder();
		filter.append("Customer_id").append(DBPUtilitiesConstants.EQUAL).append(userId);
		Result empDetails = HelperMethods.callGetApi(dcRequest, filter.toString(), HelperMethods.getHeaders(dcRequest),
				URLConstants.CUSTOMER_EMP_DETAILS_GET);
		Result addresses = getEmployeeAddress(dcRequest, inputParams);
		Result otherSourceIncomes = getOtherSourceOfIncomes(dcRequest, inputParams);
		Result expenses = getExpenses(dcRequest, inputParams);
		postProcess(empDetails, addresses, otherSourceIncomes, expenses);
		return empDetails;
	}

	private void postProcess(Result empDetails, Result addresses, Result otherSourceIncomes, Result expenses) {
		if (HelperMethods.hasRecords(empDetails)) {
			empDetails.getAllDatasets().get(0).setId("EmployeeDetails");
			if (HelperMethods.hasRecords(addresses)) {
				addresses.getAllDatasets().get(0).setId("Addresses");
				empDetails.addDataset(addresses.getAllDatasets().get(0));
			}
			if (HelperMethods.hasRecords(otherSourceIncomes)) {
				otherSourceIncomes.getAllDatasets().get(0).setId("OtherSourceOfIncomes");
				empDetails.addDataset(otherSourceIncomes.getAllDatasets().get(0));
			}
			if (HelperMethods.hasRecords(expenses)) {
				expenses.getAllDatasets().get(0).setId("Expenses");
				empDetails.addDataset(expenses.getAllDatasets().get(0));
			}
			HelperMethods.setSuccessMsg("Employee found.", empDetails);
		} else {
			ErrorCodeEnum.ERR_10137.setErrorCode(empDetails);
		}
	}

	private Result getExpenses(DataControllerRequest dcRequest, Map<String, String> inputParams)
			throws HttpCallException {
		String userId = inputParams.get("Customer_id");
		StringBuilder filter = new StringBuilder();
		filter.append("Customer_id").append(DBPUtilitiesConstants.EQUAL).append(userId);
		return HelperMethods.callGetApi(dcRequest, filter.toString(), HelperMethods.getHeaders(dcRequest),
				URLConstants.CUSTOMER_EXPENSES_GET);
	}

	private Result getOtherSourceOfIncomes(DataControllerRequest dcRequest, Map<String, String> inputParams)
			throws HttpCallException {
		String userId = inputParams.get("Customer_id");
		StringBuilder filter = new StringBuilder();
		filter.append("Customer_id").append(DBPUtilitiesConstants.EQUAL).append(userId);
		return HelperMethods.callGetApi(dcRequest, filter.toString(), HelperMethods.getHeaders(dcRequest),
				URLConstants.CUSTOMER_INCOME_DETAILS_GET);
	}

	private Result getEmployeeAddress(DataControllerRequest dcRequest, Map<String, String> inputParams)
			throws HttpCallException {
		String userId = inputParams.get("Customer_id");
		StringBuilder filter = new StringBuilder();
		filter.append("CustomerId").append(DBPUtilitiesConstants.EQUAL).append(userId);
		filter.append(DBPUtilitiesConstants.AND).append("Type_id").append(DBPUtilitiesConstants.EQUAL)
				.append("ADR_TYPE_WORK");
		return HelperMethods.callGetApi(dcRequest, filter.toString(), HelperMethods.getHeaders(dcRequest),
				URLConstants.CUSTOMER_ADDRESS_MB_VIEW_GET);
	}
}