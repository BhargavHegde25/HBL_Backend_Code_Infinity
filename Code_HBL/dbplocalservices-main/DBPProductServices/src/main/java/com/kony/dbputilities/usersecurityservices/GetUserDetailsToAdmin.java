package com.kony.dbputilities.usersecurityservices;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

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

public class GetUserDetailsToAdmin implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.USER_GET);
		}
		if (HelperMethods.hasRecords(result)) {
			postProcess(dcRequest, result);
		}
		return result;
	}

	private void postProcess(DataControllerRequest dcRequest, Result result) throws HttpCallException {
		Record user = result.getAllDatasets().get(0).getRecord(0);
		String currencyCode = HelperMethods.getCurrencyCode(HelperMethods.getFieldValue(user, "country"));
		user.addParam(new Param("currencyCode", currencyCode, "String"));
		updateDateFormat(user);
	}

	private void updateDateFormat(Record user) {
		String dateOfBirth = HelperMethods.getFieldValue(user, "dateOfBirth");
		try {
			if (StringUtils.isNotBlank(dateOfBirth)) {
				user.addParam(new Param("dateOfBirth",
						HelperMethods.convertDateFormat(dateOfBirth, "yyyy-MM-dd'T'hh:mm:ss'Z'"), "String"));
			}
		} catch (Exception e) {

		}
	}

	private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
			throws HttpCallException {
		String adminId = HelperMethods.getAPIUserIdFromSession(dcRequest);
		if (HelperMethods.isAdmin(dcRequest, adminId)) {
			String filter = "Id" + DBPUtilitiesConstants.EQUAL + getUserId(dcRequest, inputParams.get("accountNumber"));
			inputParams.put(DBPUtilitiesConstants.FILTER, filter);
			return true;
		} else {
			HelperMethods.setValidationMsg("logged in user is not admin", dcRequest, result);
			return false;
		}
	}

	private String getUserId(DataControllerRequest dcRequest, String acctNum) throws HttpCallException {
		String filter = "Account_id" + DBPUtilitiesConstants.EQUAL + acctNum;
		Result account = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
				URLConstants.ACCOUNTS_GET);
		return HelperMethods.getFieldValue(account, "User_id");
	}

}
