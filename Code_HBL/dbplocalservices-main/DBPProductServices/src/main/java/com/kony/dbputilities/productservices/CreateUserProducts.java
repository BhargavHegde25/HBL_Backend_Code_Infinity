package com.kony.dbputilities.productservices;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dbp.core.constants.DBPConstants;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class CreateUserProducts implements JavaService2 {

	@SuppressWarnings("rawtypes")
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Map inputParams = HelperMethods.getInputParamMap(inputArray);
		processHelper(dcRequest, inputParams);
		Result result = new Result();
		Param success = new Param(DBPUtilitiesConstants.RESULT_MSG, "Successful",
				DBPConstants.FABRIC_STRING_CONSTANT_KEY);
		result.addParam(success);
		return result;
	}

	@SuppressWarnings("rawtypes")
	private void processHelper(DataControllerRequest dcRequest, Map inputParams) throws HttpCallException {
		String userid = HelperMethods.getUserIdFromNUOSession(dcRequest);
		deleteExistingRecords(dcRequest, userid);
		String productLi = (String) inputParams.get(DBPUtilitiesConstants.PRODUCT_LIST);
		JsonArray jArray = getJsonArray(productLi);
		String product = null;
		if (jArray.isJsonArray()) {
			for (int i = 0; i < jArray.size(); i++) {
				product = ((JsonObject) jArray.get(i)).get("product").getAsString();
				insertUserProducts(dcRequest, userid, product);
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void deleteExistingRecords(DataControllerRequest dcRequest, String userid) {
		try {
			Map inputParams = new HashMap();
			inputParams.put(DBPUtilitiesConstants.FILTER, "newuser_id" + DBPUtilitiesConstants.EQUAL + userid);
			Result result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.USER_PRODUCTS_GET);
			if (null != result && null != result.getAllDatasets() && !result.getAllDatasets().isEmpty()) {
				List<Record> records = result.getAllDatasets().get(0).getAllRecords();
				for (Record r : records) {
					String id = r.getParam(DBPUtilitiesConstants.P_ID).getValue();
					inputParams.put(DBPUtilitiesConstants.P_ID, id);
					HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
							URLConstants.USER_PRODUCTS_DELETE);
				}
			}
		} catch (Exception e) {
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void insertUserProducts(DataControllerRequest dcRequest, String userid, String product)
			throws HttpCallException {
		Map inputParams = new HashMap();
		inputParams.put("newuser_id", userid);
		inputParams.put("product", product);
		HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
				URLConstants.USER_PRODUCTS_CREATE);
	}

	private JsonArray getJsonArray(String jsonString) {
		JsonParser jsonParser = new JsonParser();
		return (JsonArray) jsonParser.parse(jsonString);
	}
}
