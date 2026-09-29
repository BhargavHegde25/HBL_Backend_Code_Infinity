package com.kony.dbputilities.customersecurityservices;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class CreateAddress implements JavaService2 {
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String customerId = HelperMethods.getCustomerIdFromSession(dcRequest);
		String addressId = UUID.randomUUID().toString();
		if (preProcess(inputParams, dcRequest, customerId)) {
			result = createAddress(dcRequest, addressId, inputParams);
			createCustomerAddress(dcRequest, addressId, customerId, inputParams);
		}
		return result;
	}

	private void createCustomerAddress(DataControllerRequest dcRequest, String addressId, String customerId,
			Map<String, String> address) throws HttpCallException {
		Map<String, String> customerAddress = new HashMap<>();
		String isPrimary = address.get("isPreferredAddress");
		if ("1".equals(isPrimary) || "true".equals(isPrimary)) {
			customerAddress.put("isPrimary", "1");
		}
		customerAddress.put("Customer_id", customerId);
		customerAddress.put("Address_id", addressId);
		customerAddress.put("Type_id", address.get("addressType"));
		HelperMethods.callApi(dcRequest, customerAddress, HelperMethods.getHeaders(dcRequest),
				URLConstants.CUSTOMER_ADDRESS_CREATE);
	}

	private Result createAddress(DataControllerRequest dcRequest, String addressId, Map<String, String> address)
			throws HttpCallException {
		address.put("id", addressId);
		address.put("cityName", address.get("city"));
		address.put("zipCode", address.get("zipcode"));
		return HelperMethods.callApi(dcRequest, address, HelperMethods.getHeaders(dcRequest),
				URLConstants.ADDRESS_CREATE);
	}

	private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, String customerId)
			throws HttpCallException {
		String isPrimary = inputParams.get("isPreferredAddress");
		if ("1".equals(isPrimary) || "true".equals(isPrimary)) {
			modifyOldPrefferedAddress(dcRequest, customerId);
		}

		return true;
	}

	private void modifyOldPrefferedAddress(DataControllerRequest dcRequest, String userId) throws HttpCallException {
		String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + userId + DBPUtilitiesConstants.AND + "isPrimary"
				+ DBPUtilitiesConstants.EQUAL + "1";
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
		HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.ADDRESS_UPDATE);
	}
}