package com.temenos.infinity.api.wealthOrder.backenddelegate.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.HistoricalDataBackendDelegate;
import com.temenos.infinity.api.wealthOrder.config.WealthAPIServices;
import com.temenos.infinity.api.wealthOrder.constants.OperationName;
import com.temenos.infinity.api.wealthOrder.constants.ServiceId;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;


public class HistoricalDataBackendDelegateImpl implements HistoricalDataBackendDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("unchecked")
	@Override
	public Result getHistoricalData(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		Map<String, Object> inputMap = new HashMap<>();
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		Object instrumentIdObj = inputParams.get(TemenosConstants.INSTRUMENTID);
		String currencyPairs = null;
		String ricCode = null;
		String instrumentId = null;
		String dateOrPeriod = null;
		if (inputParams.get(TemenosConstants.CURRENCYPAIRS) != null
				&& inputParams.get(TemenosConstants.CURRENCYPAIRS).toString().trim().length() > 0) {
			currencyPairs = inputParams.get(TemenosConstants.CURRENCYPAIRS).toString();
			inputMap.put(TemenosConstants.CURRENCYPAIRS, currencyPairs);
			if (currencyPairs.substring(0, 3).equals(currencyPairs.substring(3))) {
				return PortfolioWealthUtils.validateMandatoryFields("FROM and TO Currency cannot be the same.");
			}
		} else if (inputParams.get(TemenosConstants.RICCODE) != null
				&& inputParams.get(TemenosConstants.RICCODE).toString().trim().length() > 0) {
			ricCode = inputParams.get(TemenosConstants.RICCODE).toString();
			inputMap.put(TemenosConstants.RICCODE, ricCode);
			// instrumentCode in TAP
			inputMap.put(TemenosConstants.INSTRUMENTCODE, ricCode);
		} else {
			return PortfolioWealthUtils.validateMandatoryFields("RICCode/Currency");
		}

		if (inputParams.get(TemenosConstants.DATEORPERIOD) != null
				&& inputParams.get(TemenosConstants.DATEORPERIOD).toString().trim().length() > 0) {
			dateOrPeriod = (String) inputParams.get(TemenosConstants.DATEORPERIOD);
			inputMap.put(TemenosConstants.DATEORPERIOD, dateOrPeriod);
		} else {
			return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.DATEORPERIOD);
		}

		if (instrumentIdObj != null) {
			instrumentId = inputParams.get(TemenosConstants.INSTRUMENTID).toString();
			inputMap.put(TemenosConstants.INSTRUMENTCODE, instrumentId);
		}
		String createResponse = null;
		String serviceName = null;

		if (isTAPOrRefinitiv(request)) {
			serviceName = ServiceId.WEALTHMKTORCHESTRATION;
		} else {
			serviceName = ServiceId.WEALTHORCHESTRATION;
		}
		String operationName = OperationName.GETHISTORICALDATA;

		try {
			createResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(inputMap).withRequestHeaders(headersMap)
					.withDataControllerRequest(request).build().getResponse();
			JSONObject resultJSON = new JSONObject(createResponse);
			return Utilities.constructResultFromJSONObject(resultJSON);
		} catch (Exception e) {
			alert.prepareError("Error while invoking Service - " + WealthAPIServices.WEALTH_GETHISTORICALDATA.getOperationName()
					+ "  : " + e).log();
		}

		return null;

	}

	public String[] nextMonths() {
		int currentMonth = Calendar.getInstance().get(Calendar.MONTH);

		String[] finalArr = null;

		String a[] = new String[] { "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV",
				"DEC" };
		List<String> al = Arrays.asList(a);

		List<String> finalList = new ArrayList<String>();
		finalList.addAll(al.subList(currentMonth + 1, al.size()));
		finalList.addAll(al.subList(0, currentMonth + 1));

		finalArr = finalList.toArray(new String[0]);

		return finalArr;

	}

	public List<String> getMonths(String[] arr) {
		ArrayList<String> resultList = new ArrayList<>();
		Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();
		Map<Integer, Integer> daysPerMonth = new HashMap<Integer, Integer>();

//		if (arr[0].equalsIgnoreCase("Jan")) {
//			startDate.set(Calendar.MONTH, 0);
//
//		} else {
//			startDate.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - 1);
//			startDate.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH) + 1);
//		}
		startDate.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - 1);
		startDate.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH));
//		startDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
//		startDate.set(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		while (startDate.before(Calendar.getInstance())) {
			if (daysPerMonth.containsKey(startDate.get(Calendar.MONTH))) {
				daysPerMonth.put(startDate.get(Calendar.MONTH), daysPerMonth.get(startDate.get(Calendar.MONTH)) + 1);
			} else {
				daysPerMonth.put(startDate.get(Calendar.MONTH), 1);
			}
			if (daysPerMonth.get(startDate.get(Calendar.MONTH)) <= 4)
				resultList.add(df.format(startDate.getTime()));
			startDate.add(Calendar.DATE, 7);
		}
		resultList.add(df.format(endDate.getTime()));

		return resultList;
	}

	private boolean isTAPOrRefinitiv(DataControllerRequest request) {
		String mkt_applicationID = (EnvironmentConfigurationsHandler.getValue(TemenosConstants.INF_WLTH_MKT_APPID,
				request) != null
						? EnvironmentConfigurationsHandler.getValue(TemenosConstants.INF_WLTH_MKT_APPID, request)
								.toString().trim()
						: "");
		String mkt_username = (EnvironmentConfigurationsHandler.getValue(TemenosConstants.INF_WLTH_MKT_USER,
				request) != null
						? EnvironmentConfigurationsHandler.getValue(TemenosConstants.INF_WLTH_MKT_USER, request)
								.toString().trim()
						: "");
		String mkt_password = (EnvironmentConfigurationsHandler.getValue(TemenosConstants.INF_WLTH_MKT_PWD,
				request) != null
						? EnvironmentConfigurationsHandler.getValue(TemenosConstants.INF_WLTH_MKT_PWD, request)
								.toString().trim()
						: "");
		if (mkt_applicationID.equals("") || mkt_username.equals("") || mkt_password.equals("")) {
			return false;

		} else {
			return true;
		}

	}
}
