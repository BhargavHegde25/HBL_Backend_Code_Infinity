package com.kony.dbputilities.usersecurityservices;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.exceptions.HttpCallException;
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

public class CreateUserPersonalInfo implements JavaService2 {

	@SuppressWarnings({ "rawtypes" })
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {

		Result result = new Result();
		Map inputParams = HelperMethods.getInputParamMap(inputArray);

		if (preProcess(inputParams, dcRequest, result)) {
			String id = (String) inputParams.get(DBPUtilitiesConstants.P_ID);
			if (StringUtils.isNotBlank(id)) {
				result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
						URLConstants.PERSONAL_INFO_UPDATE);
			} else {
				result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
						URLConstants.PERSONAL_INFO_CREATE);
			}
		}
		if (!HelperMethods.hasError(result)) {
			Param p = new Param("success", "You have successfully " + "added User personal info",
					DBPConstants.FABRIC_STRING_CONSTANT_KEY);
			result.addParam(p);
		}

		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result)
			throws HttpCallException {
		boolean status = true;
		fetchNProcessExistingRecord(dcRequest, inputParams);
		String infoType = (String) inputParams.get(DBPInputConstants.INFO_TYPE);
		if (StringUtils.isNotBlank(infoType)) {
			if ("PersonalInfo".equalsIgnoreCase(infoType)) {
				inputParams.put(DBPUtilitiesConstants.PERSONL_INFO, String.valueOf(true));
			} else if ("EmploymentInfo".equalsIgnoreCase(infoType)) {
				inputParams.put(DBPUtilitiesConstants.EMPLOYMENT_INFO, String.valueOf(true));
			} else if ("FinancialInfo".equalsIgnoreCase(infoType)) {
				inputParams.put(DBPUtilitiesConstants.FINANCIAL_INFO, String.valueOf(true));
			} else if ("SecurityQuestions".equalsIgnoreCase(infoType)) {
				inputParams.put(DBPUtilitiesConstants.SECURITY_QUEST, String.valueOf(true));
			}
		}
		return status;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void fetchNProcessExistingRecord(DataControllerRequest dcRequest, Map inputParams)
			throws HttpCallException {
		String userId = HelperMethods.getUserIdFromNUOSession(dcRequest);
		Result result = HelperMethods.callGetApi(dcRequest, "newuser_id" + DBPUtilitiesConstants.EQUAL + userId,
				HelperMethods.getHeaders(dcRequest), URLConstants.PERSONAL_INFO_GET);
		List<Record> pInfo = result.getAllDatasets().get(0).getAllRecords();
		if (!pInfo.isEmpty()) {
			Record exitInfo = pInfo.get(0);
			inputParams.put(DBPUtilitiesConstants.P_ID, exitInfo.getParam(DBPUtilitiesConstants.P_ID).getValue());
			setValue(inputParams, exitInfo, DBPUtilitiesConstants.PI_DOB);
			setValue(inputParams, exitInfo, DBPUtilitiesConstants.GENDER);
			setValue(inputParams, exitInfo, DBPUtilitiesConstants.PI_FIRST_NAME);
			setValue(inputParams, exitInfo, DBPUtilitiesConstants.PI_LAST_NAME);
			setValue(inputParams, exitInfo, DBPUtilitiesConstants.MARITAL_STATUS);
			setValue(inputParams, exitInfo, DBPUtilitiesConstants.STATE);
			setValue(inputParams, exitInfo, DBPUtilitiesConstants.SPOUSE_F_NAME);
			setValue(inputParams, exitInfo, DBPUtilitiesConstants.SPOUSE_L_NAME);
			setValue(inputParams, exitInfo, DBPUtilitiesConstants.ADDRESS1);
			setValue(inputParams, exitInfo, DBPUtilitiesConstants.ADDRESS2);
			setValue(inputParams, exitInfo, DBPUtilitiesConstants.COUNTRY);
			setValue(inputParams, exitInfo, DBPUtilitiesConstants.ZIP_CODE);
			setValue(inputParams, exitInfo, DBPUtilitiesConstants.COMAPNY);
			setValue(inputParams, exitInfo, DBPUtilitiesConstants.SSN);
			setValue(inputParams, exitInfo, DBPUtilitiesConstants.MONTHLY_EXP);
			setValue(inputParams, exitInfo, DBPUtilitiesConstants.JOB_PROF);
			setValue(inputParams, exitInfo, DBPUtilitiesConstants.EXPERIENCE);
			setValue(inputParams, exitInfo, DBPUtilitiesConstants.ANNUAL_INCOME);
			setValue(inputParams, exitInfo, DBPUtilitiesConstants.SPOUSE_NAME);
			setValue(inputParams, exitInfo, DBPUtilitiesConstants.CITY);
			setValue(inputParams, exitInfo, DBPUtilitiesConstants.DEPENDENT_NOS);
			setValue(inputParams, exitInfo, DBPUtilitiesConstants.ASSETS);
			setValue(inputParams, exitInfo, DBPUtilitiesConstants.EMP_INFO);
		} else {
			inputParams.put("newuser_id", userId);
			inputParams.put(DBPUtilitiesConstants.MARITAL_STATUS, inputParams.get("maritalStatus"));
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void setValue(Map inputParams, Record exitInfo, String fieldName) {
		String value = (String) inputParams.get(fieldName);
		if (DBPUtilitiesConstants.MARITAL_STATUS.equals(fieldName)) {
			value = (String) inputParams.get("maritalStatus");
			inputParams.put(DBPUtilitiesConstants.MARITAL_STATUS, value);
		}
		if (StringUtils.isBlank(value)) {
			inputParams.put(fieldName, HelperMethods.getParamValue(exitInfo.getParam(fieldName)));
		}
	}
}