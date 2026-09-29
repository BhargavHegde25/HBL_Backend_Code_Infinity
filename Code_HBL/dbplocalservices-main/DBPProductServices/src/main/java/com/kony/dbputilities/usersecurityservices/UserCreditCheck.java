package com.kony.dbputilities.usersecurityservices;

import java.util.List;
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
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class UserCreditCheck implements JavaService2 {
	@SuppressWarnings("rawtypes")
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map inputParams = HelperMethods.getInputParamMap(inputArray);

		if (preProcess(inputParams, dcRequest, result)) {
			String id = (String) inputParams.get(DBPUtilitiesConstants.P_ID);
			if (StringUtils.isNotBlank(id)) {
				result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
						URLConstants.CREDIT_CHECK_UPDATE);
			} else {
				result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
						URLConstants.CREDIT_CHECK_CREATE);
			}
		}
		if (!HelperMethods.hasError(result)) {
			Param p = new Param("success", "Record inserted successfully", DBPConstants.FABRIC_STRING_CONSTANT_KEY);
			result.addParam(p);
		}

		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result)
			throws HttpCallException {
		boolean status = true;
		String userId = HelperMethods.getUserIdFromNUOSession(dcRequest);
		Result tempRes = HelperMethods.callGetApi(dcRequest, "newuser_id" + DBPUtilitiesConstants.EQUAL + userId,
				HelperMethods.getHeaders(dcRequest), URLConstants.CREDIT_CHECK_GET);
		List<Record> ccInfo = tempRes.getAllDatasets().get(0).getAllRecords();
		if (!ccInfo.isEmpty()) {
			Record exitInfo = ccInfo.get(0);
			inputParams.put(DBPUtilitiesConstants.P_ID, exitInfo.getParam(DBPUtilitiesConstants.P_ID).getValue());
		} else {
			inputParams.put("newuser_id", userId);
		}
		inputParams.put(DBPUtilitiesConstants.IS_CREDIT_CHK, String.valueOf(true));
		inputParams.put(DBPUtilitiesConstants.IS_SIG_UPLOAD, String.valueOf(false));
		return status;
	}
}