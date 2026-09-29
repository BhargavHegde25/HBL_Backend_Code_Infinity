package com.kony.dbputilities.customersecurityservices;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class UpdateAddress implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String addressId = inputParams.get("addressId");
		String customerId = HelperMethods.getCustomerIdFromSession(dcRequest);
		if (preProcess(inputParams, dcRequest, customerId)) {
			result = updateAddress(dcRequest, addressId, inputParams);
			updateCustomerAddress(dcRequest, addressId, customerId, inputParams);
		}
		return result;
	}

	private void updateCustomerAddress(DataControllerRequest dcRequest, String addressId, String customerId,
			Map<String, String> inputParams) throws HttpCallException {
		Map<String, String> input = new HashMap<>();
		String isPrimary = inputParams.get("isPreferredAddress");
		input.put("Customer_id", customerId);
		input.put("Address_id", addressId);
		if (StringUtils.isNotBlank(inputParams.get("addressType"))) {
			input.put("Type_id", inputParams.get("addressType"));
		}
		if ("1".equals(isPrimary) || "true".equals(isPrimary)) {
			input.put("isPrimary", "1");
		}
		HelperMethods.removeNullValues(input);
		HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
				URLConstants.CUSTOMER_ADDRESS_UPDATE);
	}

	private Result updateAddress(DataControllerRequest dcRequest, String addressId, Map<String, String> inputParams)
			throws HttpCallException {
		Map<String, String> input = new HashMap<>();
		input.put("id", inputParams.get("addressId"));
		input.put("cityName", inputParams.get("city"));
		input.put("zipCode", inputParams.get("zipcode"));
		input.put("addressLine1", inputParams.get("addressLine1"));
		input.put("addressLine2", inputParams.get("addressLine2"));
		input.put("country", inputParams.get("country"));
		input.put("state", inputParams.get("state"));
		HelperMethods.removeNullValues(input);
		return HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
				URLConstants.ADDRESS_UPDATE);
	}

	private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, String customerId)
			throws HttpCallException {
		String isPrimary = inputParams.get("isPreferredAddress");
		if ("1".equals(isPrimary) || "true".equals(isPrimary)) {
			modifyOldPreferredAddress(dcRequest, customerId);
		}
		return true;
	}

	private void modifyOldPreferredAddress(DataControllerRequest dcRequest, String customerId)
			throws HttpCallException {
		String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + customerId + DBPUtilitiesConstants.AND
				+ "isPrimary" + DBPUtilitiesConstants.EQUAL + "1";
		Result preferredAddresses = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
				URLConstants.CUSTOMER_ADDRESS_GET);
		if (HelperMethods.hasRecords(preferredAddresses)) {
			List<Record> customerAddres = preferredAddresses.getAllDatasets().get(0).getAllRecords();
			for (Record address : customerAddres) {
				updateAddress(dcRequest, address);
			}
		}
	}

	private void updateAddress(DataControllerRequest dcRequest, Record address) throws HttpCallException {
		Map<String, String> input = new HashMap<>();
		input.put("Customer_id", HelperMethods.getFieldValue(address, "Customer_id"));
		input.put("Address_id", HelperMethods.getFieldValue(address, "Address_id"));
		input.put("isPrimary", "0");
		HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
				URLConstants.CUSTOMER_ADDRESS_UPDATE);
	}
}