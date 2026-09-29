package com.kony.dbputilities.usersecurityservices;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.metrics.DBPMetricData;
import com.kony.dbputilities.metrics.DBPMetricsProcessorHelper;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.exceptions.MetricsException;
import com.konylabs.middleware.registry.AppRegistryException;

public class EnrollUser implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	@SuppressWarnings("rawtypes")
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map inputParams = HelperMethods.getInputParamMap(inputArray);

		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.NEW_USER_CREATE);
		}
		if (!HelperMethods.hasError(result)) {
			Param p = new Param("success", "User Successfully Added", DBPConstants.FABRIC_STRING_CONSTANT_KEY);
			result.addParam(p);
			pushCustomMetricData(dcRequest);
		}

		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
		boolean status = true;
		String username = (String) inputParams.get(DBPUtilitiesConstants.USER_NAME);
		String password = (String) inputParams.get(DBPUtilitiesConstants.PWD_FIELD);
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			HelperMethods.setValidationMsg("Please provide username and password.", dcRequest, result);
			status = false;
		}
		if (status) {
			String role = (String) inputParams.get(DBPUtilitiesConstants.ROLE);
			if (StringUtils.isBlank(role)) {
				inputParams.put(DBPUtilitiesConstants.ROLE, "BASIC");
			}
			inputParams.put("passWord", password);
			inputParams.put("lastlogintime", HelperMethods.getCurrentTimeStamp());
		}
		return status;
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