package com.kony.dbputilities.usersecurityservices;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.customersecurityservices.PasswordHistoryManagement;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class ResetUserPassword implements JavaService2 {

	@SuppressWarnings("rawtypes")
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map inputParams = HelperMethods.getInputParamMap(inputArray);

		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.USER_UPDATE);
			PasswordHistoryManagement pm = new PasswordHistoryManagement(dcRequest, false);
			if (StringUtils.isNotBlank(getUserId(result))
					&& pm.makePasswordEntry(dcRequest, getUserId(result), (String) inputParams.get("passWord"))) {
				result = postProcess(result);
			} else if (StringUtils.isNotBlank(getUserId(result))) {
				Map<String, String> input = new HashMap<>();
				String id = getUserId(result);
				input.put("id", id);
				HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
						URLConstants.CUSTOMER_DELETE);
				result = new Result();
			}
		}

		return result;
	}

	private Result postProcess(Result result) {
		Result retVal = new Result();
		Param p = new Param(DBPUtilitiesConstants.RESULT_MSG, "updated", DBPConstants.FABRIC_STRING_CONSTANT_KEY);
		retVal.addParam(p);
		return retVal;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result)
			throws HttpCallException {
		boolean status = true;
		String userName = (String) inputParams.get(DBPUtilitiesConstants.USER_NAME);
		String password = (String) inputParams.get(DBPUtilitiesConstants.PWD_FIELD);
		inputParams.put("passWord", password);
		if (!StringUtils.isNotBlank(userName)) {
			HelperMethods.setValidationMsg("Please provide userName.", dcRequest, result);
			status = false;
		}
		if (status) {
			StringBuilder sb = new StringBuilder();
			sb.append(DBPUtilitiesConstants.USER_NAME).append(DBPUtilitiesConstants.EQUAL).append(userName);
			Result rs = HelperMethods.callGetApi(dcRequest, sb.toString(), HelperMethods.getHeaders(dcRequest),
					URLConstants.CUSTOMERVERIFY_GET);
			Dataset ds = rs.getDatasetById(DBPUtilitiesConstants.DS_USER);
			if (null != ds && null != ds.getAllRecords() && ds.getAllRecords().size() > 0) {
				String id = ds.getRecord(0).getParam(DBPUtilitiesConstants.U_ID).getValue();
				inputParams.put(DBPUtilitiesConstants.U_ID, id);
			} else {
				HelperMethods.setValidationMsg("incorrect userName.", dcRequest, result);
				status = false;
			}
		}
		return status;
	}

	private String getUserId(Result result) {
		String id = "";
		Dataset ds = result.getAllDatasets().get(0);
		if (null != ds && !ds.getAllRecords().isEmpty()) {
			id = ds.getRecord(0).getParam("id").getValue();
		}
		return id;
	}

}
