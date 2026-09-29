package com.kony.eum.dbputilities.usersecurityservices;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.metrics.DBPMetricData;
import com.kony.dbputilities.metrics.DBPMetricsProcessorHelper;
import com.kony.eum.dbputilities.usersecurityservices.CreateUser;
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
import com.konylabs.middleware.exceptions.MetricsException;
import com.konylabs.middleware.registry.AppRegistryException;

public class CreateUser implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@SuppressWarnings("rawtypes")
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map inputParams = HelperMethods.getInputParamMap(inputArray);

		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.USER_CREATE);
		}
		if (!HelperMethods.hasError(result)) {
			result = postProcess(dcRequest, inputParams, result);
		}

		return result;
	}

	@SuppressWarnings("rawtypes")
	private Result postProcess(DataControllerRequest dcRequest, Map inputParams, Result result)
			throws HttpCallException, MetricsException, ParseException {
		String userId = getUserId(result);
		String accountId = (String) inputParams.get(DBPInputConstants.ACCT_NUM);
		String accountType = (String) inputParams.get(DBPInputConstants.ACCT_TYPE);
		Result rs = updateAccount(dcRequest, accountType, accountId, userId);
		if (!HelperMethods.hasError(rs)) {
			rs = getSuccessResult();
			pushCustomMetricData(dcRequest);
		}
		return rs;
	}

	private String getUserId(Result result) {
		String id = "";
		Dataset ds = result.getAllDatasets().get(0);
		if (null != ds && !ds.getAllRecords().isEmpty()) {
			id = ds.getRecord(0).getParam(DBPUtilitiesConstants.U_ID).getValue();
		}
		return id;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Result updateAccount(DataControllerRequest dcRequest, String accountType, String accountId, String userId)
			throws HttpCallException {
		Map input = new HashMap();
		input.put(DBPUtilitiesConstants.ACCOUNT_ID, accountId);
		input.put(DBPUtilitiesConstants.UA_USR_ID, userId);
		input.put(DBPUtilitiesConstants.TYPE_ID, accountType);
		return HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
				URLConstants.ACCOUNTS_UPDATE);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
		boolean status = true;
		String username = (String) inputParams.get(DBPUtilitiesConstants.USER_NAME);
		String password = (String) inputParams.get(DBPUtilitiesConstants.PWD_FIELD);
		if (!StringUtils.isNotBlank(username) || !StringUtils.isNotBlank(password)) {
			HelperMethods.setValidationMsg("Please provide username and password.", dcRequest, result);
			status = false;
		}
		if (status) {
			String role = (String) inputParams.get(DBPUtilitiesConstants.ROLE);
			if (!StringUtils.isNotBlank(role)) {
				inputParams.put(DBPUtilitiesConstants.ROLE, "BASIC");
			}
			String dob = (String) inputParams.get(DBPUtilitiesConstants.DOB);
			if (!StringUtils.isNotBlank(dob)) {
				inputParams.remove(dob);
			}
			inputParams.put(DBPUtilitiesConstants.IS_ENROLLED, "true");
			inputParams.put("passWord", password);
		}
		return status;
	}

	private Result getSuccessResult() {
		Result result = new Result();
		Param p = new Param(DBPUtilitiesConstants.RESULT_MSG, "success", DBPConstants.FABRIC_STRING_CONSTANT_KEY);
		result.addParam(p);
		return result;
	}

	private void pushCustomMetricData(DataControllerRequest dcRequest) throws MetricsException, ParseException {
		DBPMetricsProcessorHelper helper = new DBPMetricsProcessorHelper();
		List<DBPMetricData> custMetrics = new ArrayList<>();
		custMetrics.add(new DBPMetricData("enrolment_status", "new", DBPMetricData.STRING));
		try {
			helper.addCustomMetrics(dcRequest, custMetrics, false);
		} catch (AppRegistryException e) {

			alert.prepareError(e.getMessage()).log();
		}
	}
}