package com.kony.dbputilities.usersecurityservices;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

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

public class GetUserState implements JavaService2 {
	@SuppressWarnings("rawtypes")
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Result personalInfo = new Result();
		Result userProducts = new Result();
		Result creditCheck = new Result();
		Map inputParams = HelperMethods.getInputParamMap(inputArray);

		if (preProcess(inputParams, dcRequest, result)) {
			personalInfo = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.PERSONAL_INFO_GET);
			userProducts = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.USER_PRODUCTS_GET);
			creditCheck = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.CREDIT_CHECK_GET);
			postProcess(result, personalInfo, userProducts, creditCheck);
		}

		return result;
	}

	private void postProcess(Result result, Result personalInfo, Result userProducts, Result creditCheck) {
		Dataset ds = personalInfo.getAllDatasets().get(0);
		Record reqRec = new Record();
		if (!ds.getAllRecords().isEmpty()) {
			reqRec = ds.getAllRecords().get(0);
		}
		postProcessPersonalInfo(personalInfo, reqRec);
		postProcessUserProducts(userProducts, reqRec);
		postProcessCreditCheck(creditCheck, reqRec);
		if (ds.getAllRecords().isEmpty()) {
			ds.addRecord(reqRec);
		}
		result.addDataset(ds);
	}

	private void postProcessCreditCheck(Result creditCheck, Record reqRec) {
		Dataset ds = creditCheck.getAllDatasets().get(0);
		if (null == ds || null == ds.getAllRecords() || ds.getAllRecords().isEmpty()) {
			reqRec.addParam(new Param(DBPUtilitiesConstants.CREDIT_CHECK, "false", "String"));
		} else {
			Record temp = ds.getAllRecords().get(0);
			reqRec.addParam(new Param(DBPUtilitiesConstants.CREDIT_CHECK,
					temp.getParam(DBPUtilitiesConstants.IS_SIG_UPLOAD).getValue(), "String"));
		}
	}

	private void postProcessUserProducts(Result userProducts, Record reqRec) {
		Dataset ds = userProducts.getAllDatasets().get(0);
		if (null == ds || null == ds.getAllRecords() || ds.getAllRecords().isEmpty()) {
			reqRec.addParam(new Param(DBPUtilitiesConstants.USR_PRODUCTS, "false", "String"));
		} else {
			reqRec.addParam(new Param(DBPUtilitiesConstants.USR_PRODUCTS, "true", "String"));
		}
	}

	private void postProcessPersonalInfo(Result personalInfo, Record reqRec) {
		Dataset ds = personalInfo.getAllDatasets().get(0);
		if (null == ds || null == ds.getAllRecords() || ds.getAllRecords().isEmpty()) {
			reqRec.addParam(new Param(DBPUtilitiesConstants.PERSONL_INFO, "false", "String"));
			reqRec.addParam(new Param(DBPUtilitiesConstants.FINANCIAL_INFO, "false", "String"));
			reqRec.addParam(new Param(DBPUtilitiesConstants.EMPLOYMENT_INFO, "false", "String"));
			reqRec.addParam(new Param(DBPUtilitiesConstants.SECURITY_QUEST, "false", "String"));
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
		boolean status = true;
		String id = HelperMethods.getUserIdFromNUOSession(dcRequest);
		if (StringUtils.isNotBlank(id)) {
			String filter = "newuser_id" + DBPUtilitiesConstants.EQUAL + id;
			inputParams.put(DBPUtilitiesConstants.FILTER, filter);
		}
		return status;
	}
}