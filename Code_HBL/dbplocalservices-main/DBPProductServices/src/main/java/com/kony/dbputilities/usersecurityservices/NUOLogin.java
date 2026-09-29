package com.kony.dbputilities.usersecurityservices;

import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.util.DBPInputConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class NUOLogin implements JavaService2 {

	@SuppressWarnings("rawtypes")
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map inputParams = HelperMethods.getInputParamMap(inputArray);

		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.NEW_USER_GET);
			result = postProcess(dcRequest, result);
		}

		return result;
	}

	private Result postProcess(DataControllerRequest dcRequest, Result result) {
		Result retVal = new Result();
		if (HelperMethods.hasRecords(result)) {
			retVal = sessionAttributes(result);
		} else {
			Param p1 = new Param("message", "Incorrect username or password", DBPConstants.FABRIC_STRING_CONSTANT_KEY);
			retVal.addParam(p1);
			Param p2 = new Param("errmsg", "Incorrect username or password", DBPConstants.FABRIC_STRING_CONSTANT_KEY);
			retVal.addParam(p2);
		}
		return retVal;
	}

	private Result sessionAttributes(Result result) {
		Result retVal = new Result();
		Record sessionAttr = new Record();
		String token = UUID.randomUUID().toString();
		sessionAttr.setId("security_attributes");
		Param sessionId = new Param("session_token", token, DBPConstants.FABRIC_STRING_CONSTANT_KEY);
		Param sessionTtl = new Param("session_ttl", null, DBPConstants.FABRIC_STRING_CONSTANT_KEY);
		sessionAttr.addParam(sessionId);
		sessionAttr.addParam(sessionTtl);

		Record usrAttr = result.getAllDatasets().get(0).getRecord(0);
		usrAttr.setId(DBPUtilitiesConstants.USR_ATTR);
		usrAttr.addParam(new Param("user_id", usrAttr.getParam("id").getValue(), "String"));
		retVal.addRecord(usrAttr);
		retVal.addRecord(sessionAttr);

		return retVal;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
		boolean status = true;
		String username = (String) inputParams.get(DBPInputConstants.USR_NAME);
		String password = (String) inputParams.get(DBPUtilitiesConstants.PWD_FIELD);
		StringBuilder sb = new StringBuilder();
		if (StringUtils.isNotBlank(password) && StringUtils.isNotBlank(username)) {
			sb.append(DBPUtilitiesConstants.USER_NAME).append(DBPUtilitiesConstants.EQUAL).append(username)
					.append(DBPUtilitiesConstants.AND).append("passWord").append(DBPUtilitiesConstants.EQUAL)
					.append("'" + password + "'");
		} else {
			HelperMethods.setValidationMsg("Incorrect details.", dcRequest, result);
			status = false;
		}
		inputParams.put(DBPUtilitiesConstants.FILTER, sb.toString());
		return status;
	}
}
