package com.kony.dbputilities.customersecurityservices;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.dto.PasswordLockoutSettingsDTO;

public class GetCustomerStatus implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.CUSTOMERVERIFY_GET);
			result = postProcess(inputParams, dcRequest, result);
		}

		return result;
	}

	private Result postProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
			throws HttpCallException {
		Result ret = new Result();
		if (HelperMethods.hasRecords(result)) {
			String isLocked = "false";
			if(StringUtils.isNotBlank(HelperMethods.getFieldValue(result, "lockCount"))) {
				int lockCount = Integer.parseInt(HelperMethods.getFieldValue(result, "lockCount"));
				
				PasswordLockoutSettingsDTO dto = (PasswordLockoutSettingsDTO) new PasswordLockoutSettingsDTO().loadDTO();
				if (lockCount > dto.getAccountLockoutThreshold()) {
					isLocked = "true";
				}
			}
			Dataset set = new Dataset("user");
			Record user = new Record();
			user.addParam(new Param("isLocked", isLocked, "String"));
			user.addParam(new Param("isEnrolled", HelperMethods.getFieldValue(result, "isEnrolled"), "String"));
			user.addParam(new Param("id", HelperMethods.getFieldValue(result, "id"), "String"));
			set.addRecord(user);
			ret.addDataset(set);
		} else {
			HelperMethods.setValidationMsg("User not found", dcRequest, ret);
		}
		return ret;
	}

	private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result) {
		StringBuilder filter = new StringBuilder();
		String userName = "";

		userName = inputParams.get("userName");

		if(StringUtils.isNotBlank(userName)) {
			userName = inputParams.get("UserName");
		}

		if (StringUtils.isNotBlank(userName)) {
			filter.append("UserName").append(DBPUtilitiesConstants.EQUAL)
			.append("\'" + userName + "\'");
		} else {
			filter.append("id").append(DBPUtilitiesConstants.EQUAL)
			.append(HelperMethods.getCustomerIdFromSession(dcRequest));
		}
		inputParams.put(DBPUtilitiesConstants.FILTER, filter.toString());
		return true;
	}

}
