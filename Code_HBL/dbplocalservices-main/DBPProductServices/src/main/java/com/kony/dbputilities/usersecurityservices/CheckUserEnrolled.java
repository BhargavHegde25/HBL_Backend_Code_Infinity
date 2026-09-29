package com.kony.dbputilities.usersecurityservices;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.util.DBPInputConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class CheckUserEnrolled implements JavaService2 {

	@SuppressWarnings("rawtypes")
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map inputParams = HelperMethods.getInputParamMap(inputArray);

		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.CUSTOMERVERIFY_GET);
		}
		if (!HelperMethods.hasError(result)) {
			result = postProcess(result);
		}

		return result;
	}

	private Result postProcess(Result result) {
		Dataset ds = result.getDatasetById(DBPUtilitiesConstants.DS_USER);
		Result retVal = new Result();
		if (null != ds && null != ds.getAllRecords() && ds.getAllRecords().size() > 0) {
			Param p = null;
			Param isEnrolled = ds.getRecord(0).getParam(DBPUtilitiesConstants.IS_ENROLLED);
			if (null != isEnrolled && "true".equalsIgnoreCase(isEnrolled.getValue())) {
				p = new Param("result", "User Already Enrolled", DBPConstants.FABRIC_STRING_CONSTANT_KEY);
			} else {
				p = new Param("result", "User Not Enrolled", DBPConstants.FABRIC_STRING_CONSTANT_KEY);
			}
			retVal.addParam(p);
		} else {
			Param p = new Param(DBPUtilitiesConstants.VALIDATION_ERROR, "User Not Enrolled",
					DBPConstants.FABRIC_STRING_CONSTANT_KEY);
			retVal.addParam(p);
		}
		return retVal;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) throws ParseException {
		boolean status = true;
		String userlastname = (String) inputParams.get(DBPInputConstants.USR_LAST_NAME);
		String ssn = (String) inputParams.get(DBPUtilitiesConstants.SSN);
		String dob = (String) inputParams.get(DBPUtilitiesConstants.DOB);

		boolean isAdded = false;
		StringBuilder sb = new StringBuilder();
		if (StringUtils.isNotBlank(userlastname)) {
			userlastname = "'" + userlastname + "'";
			sb.append(DBPUtilitiesConstants.LAST_NAME).append(DBPUtilitiesConstants.EQUAL).append(userlastname);
			isAdded = true;
		}
		if (StringUtils.isNotBlank(ssn)) {
			if (isAdded) {
				sb.append(DBPUtilitiesConstants.AND);
			}
			sb.append(DBPUtilitiesConstants.SSN).append(DBPUtilitiesConstants.EQUAL).append(ssn);
			isAdded = true;
		}
		if (StringUtils.isNotBlank(dob)) {
			dob = HelperMethods.convertDateFormat(dob, null);
			if (isAdded) {
				sb.append(DBPUtilitiesConstants.AND);
			}
			sb.append(DBPUtilitiesConstants.DOB).append(DBPUtilitiesConstants.EQUAL).append(dob);
			isAdded = true;
			if (!isValidDOB(dob)) {
				Param p = new Param(DBPUtilitiesConstants.VALIDATION_ERROR, "Please provide valid DateOfBirth.",
						"String");
				result.addParam(p);
				status = false;
			}

		}
		if (isAdded) {
			inputParams.put(DBPUtilitiesConstants.FILTER, sb.toString());
		}
		return status;
	}

	private boolean isValidDOB(String dateOfBirth) {
		if (StringUtils.isNotBlank(dateOfBirth)) {
			Date createdts = HelperMethods.getFormattedTimeStamp(dateOfBirth);
			Calendar generatedCal = Calendar.getInstance();
			generatedCal.setTime(createdts);

			Date verifyDate = new Date();
			Calendar verifyingCal = Calendar.getInstance();
			verifyingCal.setTime(verifyDate);

			long GeneratedMilliSeconds = generatedCal.getTimeInMillis();
			long verifyingMilliSeconds = verifyingCal.getTimeInMillis();

			if (GeneratedMilliSeconds > verifyingMilliSeconds) {
				return false;
			}
		}
		return true;
	}
}