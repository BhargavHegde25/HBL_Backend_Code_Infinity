package com.kony.dbputilities.customersecurityservices;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;


public class ActivateDbxP2PForUser implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.CUSTOMER_UPDATE);
		}
		if (!HelperMethods.hasError(result)) {
			result = new Result();
			Param success = new Param(DBPUtilitiesConstants.RESULT_MSG, "Successful",
					DBPConstants.FABRIC_STRING_CONSTANT_KEY);
			result.addParam(success);
		}
		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
		boolean status = true;
		String userId = HelperMethods.getCustomerIdFromSession(dcRequest);
		status = isP2pActivated(dcRequest, userId);
		inputParams.remove("UserName");
		inputParams.put(DBPUtilitiesConstants.C_ID, userId);
		inputParams.put(DBPUtilitiesConstants.IS_P2P_ACTIVE, String.valueOf(!status));
		return true;
	}

	/*
	 * private boolean isP2pActivated(String userId) { String query = "Id" +
	 * DBPUtilitiesConstants.EQUAL + userId; JsonObject response =
	 * LocalServiceValidations.callGetApi( URLConstants.USER_GET, query); JsonArray
	 * ele = (JsonArray) response.get(DBPUtilitiesConstants.DS_USER); if (null !=
	 * ele && 0 != ele.size()) { JsonObject jobj = (JsonObject) ele.get(0); return
	 * null != jobj.get("isP2PActivated") &&
	 * jobj.get("isP2PActivated").getAsBoolean(); } return false; }
	 */

	private boolean isP2pActivated(DataControllerRequest dcRequest, String userId) {
		String query = "id" + DBPUtilitiesConstants.EQUAL + userId;
		Result user = new Result();
		String isP2PActivated = "";
		try {
			user = HelperMethods.callGetApi(dcRequest, query, HelperMethods.getHeaders(dcRequest),
					URLConstants.CUSTOMER_GET);
			isP2PActivated = HelperMethods.getFieldValue(user, "isP2PActivated");
		} catch (HttpCallException e) {
			isP2PActivated = "";
			alert.prepareError(e.getMessage()).log();
		}

		return StringUtils.isBlank(isP2PActivated) ? false : Boolean.parseBoolean(isP2PActivated);
	}
}