package com.kony.dbputilities.customersecurityservices;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commonsutils.CustomerSession;


public class ActivateBillPaymentForUser implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		Map<String, Object> customer = CustomerSession.getCustomerMap(dcRequest);
		if (!(customer.get("UserName").equals(inputParams.get("userName"))))
			return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.CUSTOMER_UPDATE);
		} else {
			result.addParam(new Param("errmsg", "Not a valid user to activate/deactivate", "String"));
		}
		if (!HelperMethods.hasError(result)) {
			result = postProcess(result);
		}
		return result;
	}

	private Result postProcess(Result result) {
		Param p = new Param("result", "Successful", DBPConstants.FABRIC_STRING_CONSTANT_KEY);
		result.addParam(p);
		return result;
	}

	private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result) {
		boolean status = true;
		String username = inputParams.get(DBPUtilitiesConstants.USER_NAME);
		if (StringUtils.isBlank(username)) {
			HelperMethods.setValidationMsg("Please provide username.", dcRequest, result);
			status = false;
		}
		if (status && checkRecordExists(inputParams, dcRequest, result)) {
			if (status) {
				String uid = getId(dcRequest, username);
				inputParams.put("id", uid);
				inputParams.put(DBPUtilitiesConstants.IS_BILL_PAY_ACTIVE, "true");
				status = StringUtils.isNotBlank(uid);
			}
		} else {
			status = false;
		}
		return status;
	}

	private boolean checkRecordExists(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result) {

		boolean status = true;

		String id = HelperMethods.getCustomerIdFromSession(dcRequest);
		String filter = "id" + DBPUtilitiesConstants.EQUAL + id + DBPUtilitiesConstants.AND + "UserName"
				+ DBPUtilitiesConstants.EQUAL + "'" + inputParams.get(DBPUtilitiesConstants.USER_NAME) +"'";
		Result chkresult = new Result();
		try {
			chkresult = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
					URLConstants.CUSTOMER_GET);
		} catch (HttpCallException e) {
			chkresult = new Result();
			alert.prepareError(e.getMessage()).log();
		}
		if (HelperMethods.hasRecords(chkresult)) {
			status = true;
		} else {
			status = false;
		}
		return status;
	}

	private String getId(DataControllerRequest dcRequest, String username) {
		String query = "UserName" + DBPUtilitiesConstants.EQUAL + username;
		Result user = new Result();
		try {
			user = HelperMethods.callGetApi(dcRequest, query, HelperMethods.getHeaders(dcRequest),
					URLConstants.CUSTOMER_GET);
		} catch (HttpCallException e) {
			alert.prepareError(e.getMessage()).log();
			return new String("");
		}
		return HelperMethods.getFieldValue(user, "id");
	}
}