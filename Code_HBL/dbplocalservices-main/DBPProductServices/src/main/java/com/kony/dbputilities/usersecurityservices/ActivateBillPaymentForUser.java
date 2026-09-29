package com.kony.dbputilities.usersecurityservices;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

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

public class ActivateBillPaymentForUser implements JavaService2 {
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.USER_UPDATE);
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

	private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
			throws HttpCallException {
		boolean status = true;
		String username = inputParams.get(DBPUtilitiesConstants.USER_NAME);
		if (StringUtils.isBlank(username)) {
			HelperMethods.setValidationMsg("Please provide username.", dcRequest, result);
			status = false;
		}
		if (status) {
			String uid = getId(dcRequest, username);
			inputParams.put(DBPUtilitiesConstants.U_ID, uid);
			inputParams.put(DBPUtilitiesConstants.IS_BILL_PAY_ACTIVE, "true");
			status = StringUtils.isNotBlank(uid);
		}
		return status;
	}

	private String getId(DataControllerRequest dcRequest, String username) throws HttpCallException {
		String query = DBPUtilitiesConstants.USER_NAME + DBPUtilitiesConstants.EQUAL + username;
		Result user = HelperMethods.callGetApi(dcRequest, query, HelperMethods.getHeaders(dcRequest),
				URLConstants.USER_GET);
		return HelperMethods.getFieldValue(user, "Id");
	}
}