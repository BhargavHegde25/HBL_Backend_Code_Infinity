package com.kony.dbputilities.usersecurityservices;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class DeleteNewUserPerosnalInfo implements JavaService2 {
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		process(dcRequest, result);
		return result;
	}

	private void process(DataControllerRequest dcRequest, Result result) throws HttpCallException {
		String userId = HelperMethods.getUserIdFromNUOSession(dcRequest);
		deleteUserProducts(dcRequest, userId);
		deleteUserPersonalInfo(dcRequest, userId);
		deleteUserCreditCheck(dcRequest, userId);
	}

	private void deleteUserCreditCheck(DataControllerRequest dcRequest, String userId) throws HttpCallException {
		List<Record> creditChecks = getCreditChecks(dcRequest, userId);
		if (null != creditChecks) {
			Map<String, String> input = new HashMap<>();
			for (Record cc : creditChecks) {
				input.put("id", HelperMethods.getFieldValue(cc, "id"));
				HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
						URLConstants.CREDIT_CHECK_DELETE);
			}
		}
	}

	private List<Record> getCreditChecks(DataControllerRequest dcRequest, String userId) throws HttpCallException {
		String filter = "newuser_id" + DBPUtilitiesConstants.EQUAL + userId;
		Result cchecks = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
				URLConstants.CREDIT_CHECK_GET);
		if (HelperMethods.hasRecords(cchecks)) {
			return cchecks.getAllDatasets().get(0).getAllRecords();
		}
		return null;
	}

	private void deleteUserPersonalInfo(DataControllerRequest dcRequest, String userId) throws HttpCallException {
		List<Record> personalInfos = getUserPersonalInfo(dcRequest, userId);
		if (null != personalInfos) {
			Map<String, String> input = new HashMap<>();
			for (Record cc : personalInfos) {
				input.put("id", HelperMethods.getFieldValue(cc, "id"));
				HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
						URLConstants.PERSONAL_INFO_DELETE);
			}
		}
	}

	private List<Record> getUserPersonalInfo(DataControllerRequest dcRequest, String userId) throws HttpCallException {
		String filter = "newuser_id" + DBPUtilitiesConstants.EQUAL + userId;
		Result infos = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
				URLConstants.PERSONAL_INFO_GET);
		if (HelperMethods.hasRecords(infos)) {
			return infos.getAllDatasets().get(0).getAllRecords();
		}
		return null;
	}

	private void deleteUserProducts(DataControllerRequest dcRequest, String userId) throws HttpCallException {
		List<Record> userProducts = getUserProducts(dcRequest, userId);
		if (null != userProducts) {
			Map<String, String> input = new HashMap<>();
			for (Record cc : userProducts) {
				input.put("id", HelperMethods.getFieldValue(cc, "id"));
				HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
						URLConstants.USER_PRODUCTS_DELETE);
			}
		}
	}

	private List<Record> getUserProducts(DataControllerRequest dcRequest, String userId) throws HttpCallException {
		String filter = "newuser_id" + DBPUtilitiesConstants.EQUAL + userId;
		Result products = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
				URLConstants.USER_PRODUCTS_GET);
		if (HelperMethods.hasRecords(products)) {
			return products.getAllDatasets().get(0).getAllRecords();
		}
		return null;
	}
}