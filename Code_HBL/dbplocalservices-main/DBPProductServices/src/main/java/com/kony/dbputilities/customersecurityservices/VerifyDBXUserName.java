package com.kony.dbputilities.customersecurityservices;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class VerifyDBXUserName implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String isOtpRequired = inputParams.get("doNotSendOTP");
		Result result1 = new Result();
		if (preProcess(inputParams, dcRequest, result)) {
			if (StringUtils.isBlank(isOtpRequired) || ("false").equalsIgnoreCase(isOtpRequired)) {
				Map<String, String> headersMap = HelperMethods.getHeaders(dcRequest);
				headersMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
				result1 = (Result) new RequestOTP().invoke(methodID, inputArray, dcRequest, dcResponse);
			}
		} else {
			return result;
		}

		result = postProcess(inputParams, result, result1);
		return result;
	}

	private Result postProcess(Map<String, String> inputParams, Result result, Result result1) {

		Result retResult = new Result();

		Record usrAttr = new Record();
		usrAttr.setId(DBPUtilitiesConstants.USR_ATTR);
		if (!HelperMethods.hasRecords(result)) {
			result1.removeParamByName("Otp");
			for (Param param : result1.getAllParams()) {
				usrAttr.addParam(param);
			}
			usrAttr.addParam(new Param(DBPUtilitiesConstants.IS_USERNAME_EXISTS, "true", "String"));
		} else {
			ErrorCodeEnum.ERR_10003.setErrorCode(usrAttr);
		}
		retResult.addRecord(usrAttr);
		return retResult;
	}

	private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
			throws HttpCallException {

		boolean status = true;

		String userName = inputParams.get("UserName");
		String phone = inputParams.get("Phone");
		String email = inputParams.get("Email");

		String filterKey = "";
		String filtervalue = "";

		StringBuilder sb = new StringBuilder();

		if (StringUtils.isNotBlank(userName)) {
			filterKey = "UserName";
			filtervalue = userName;
		} else if (StringUtils.isNotBlank(phone)) {
			filterKey = "Phone";
			filtervalue = phone;
		} else if (StringUtils.isNotBlank(email)) {
			filterKey = "Email";
			filtervalue = email;
		} else {
			Record record = new Record();
			record.setId(DBPUtilitiesConstants.USR_ATTR);
			ErrorCodeEnum.ERR_10001.setErrorCode(record);
			result.addRecord(record);
			status = false;
		}

		if (status) {
			sb.append(filterKey).append(DBPUtilitiesConstants.EQUAL).append("'" + filtervalue + "'");
			Result rs = HelperMethods.callGetApi(dcRequest, sb.toString(), HelperMethods.getHeaders(dcRequest),
					URLConstants.CUSTOMERCOMMUNICATIONVIEW_GET);

			String filterQuery = "userName" + DBPUtilitiesConstants.EQUAL + userName;
			Result newUser = HelperMethods.callGetApi(dcRequest, filterQuery, HelperMethods.getHeaders(dcRequest),
					URLConstants.NEW_USER_GET);

			if (HelperMethods.hasRecords(newUser)) {
				status = true;
				return status;
			}

			if (!HelperMethods.hasRecords(rs) && !HelperMethods.hasError(rs)) {
				Record usrAttr = new Record();
				usrAttr.setId(DBPUtilitiesConstants.USR_ATTR);
				usrAttr.addParam(new Param(DBPUtilitiesConstants.IS_USERNAME_EXISTS, "false", "String"));
				status = false;
				result.addRecord(usrAttr);
			}
		}
		return status;
	}
}
