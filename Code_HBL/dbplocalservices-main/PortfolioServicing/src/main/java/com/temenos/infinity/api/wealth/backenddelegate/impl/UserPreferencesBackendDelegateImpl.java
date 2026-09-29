package com.temenos.infinity.api.wealth.backenddelegate.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealth.backenddelegate.api.UserPreferenceBackendDelegate;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;

public class UserPreferencesBackendDelegateImpl implements UserPreferenceBackendDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("unchecked")
	@Override
	public Result getHoldingsOrder(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) throws ApplicationException {
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		Object portfolioIdObj = inputParams.get(TemenosConstants.PORTFOLIOID);
		Object userIdObj = inputParams.get(TemenosConstants.USERID);

		String portfolioId = null, userId = null;
		if (userIdObj == null || userIdObj.equals("") || portfolioIdObj == null || portfolioIdObj.equals("")) {
			alert.prepareError("Invalid request").log();
			JSONObject result = new JSONObject();
			result.put("status", "Failure");
			result.put("error", "Invalid Input Params!");
			return Utilities.constructResultFromJSONObject(result);
		} else {
			if (portfolioIdObj != null) {
				portfolioId = inputParams.get(TemenosConstants.PORTFOLIOID).toString();
			}
			if (userIdObj != null) {
				userId = inputParams.get(TemenosConstants.USERID).toString();
			}
		}
		List<String> allportfoliosList = PortfolioWealthUtils.getAllPortfoliosFromCache(request);

		if (allportfoliosList.contains(portfolioId)) {
			Map<String, String> input = new HashMap<>();
			String filter = "userId" + DBPUtilitiesConstants.EQUAL + userId + DBPUtilitiesConstants.AND + "portfolioId"
					+ DBPUtilitiesConstants.EQUAL + portfolioId;
			input.put(DBPUtilitiesConstants.FILTER, filter);
			try {
				return HelperMethods.callGetApi(request, input.get(DBPUtilitiesConstants.FILTER),
						HelperMethods.getHeaders(request), URLConstants.WEALTH_USER_PREFERENCES_GET);
			} catch (Exception e) {
				alert.prepareError("Exception occured while fetching the field order from backend delegate :" + e.getMessage()).log();
				throw new ApplicationException(ErrorCodeEnum.ERR_21001);
			}
		} else {
			return PortfolioWealthUtils.UnauthorizedAccess();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Result modifyHoldingsOrder(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) throws ApplicationException {
		Result result = new Result();
		Result result1 = new Result();
		try {
			Map<String, Object> inputMap = new HashMap<>();
			Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
			Object portfolioIdObj = inputParams.get(TemenosConstants.PORTFOLIOID);
			Object userIdObj = inputParams.get(TemenosConstants.USERID);
			Object fieldOrderObj = inputParams.get(TemenosConstants.FIELDORDER);
			String portfolioId = null, userId = null, fieldOrder = null;
			if (userIdObj == null || userIdObj.equals("") || portfolioIdObj == null || portfolioIdObj.equals("")
					|| fieldOrderObj == null || fieldOrderObj.equals("")) {
				alert.prepareError("Invalid request").log();
				result.addParam("status", "Failure");
				result.addParam("error", "Invalid Input Params!");
				return result;
			} else {
				if (portfolioIdObj != null) {
					portfolioId = inputParams.get(TemenosConstants.PORTFOLIOID).toString();
					inputMap.put(TemenosConstants.PORTFOLIOID, portfolioId);
				}
				if (userIdObj != null) {
					userId = inputParams.get(TemenosConstants.USERID).toString();
					inputMap.put(TemenosConstants.USERID, userId);
				}
				if (fieldOrderObj != null) {
					fieldOrder = inputParams.get(TemenosConstants.FIELDORDER).toString();
					inputMap.put(TemenosConstants.FIELDORDER, fieldOrder);
				}
			}
			List<String> allportfoliosList = PortfolioWealthUtils.getAllPortfoliosFromCache(request);

			if (allportfoliosList.contains(portfolioId)) {
				// Getting the user preference details of BusinessUser
				result = getPreferences(portfolioId, userId, request);
				if (HelperMethods.hasRecords(result)) {

					List<Record> existingRecords = result.getAllDatasets().get(0).getAllRecords();
					for (Record record : existingRecords) {
						String id = HelperMethods.getFieldValue(record, "id");
						inputMap.put("id", id);
						HelperMethods.removeNullValues(inputMap);
						HelperMethods.callApi(request, inputMap, HelperMethods.getHeaders(request),
								URLConstants.WEALTH_USER_PREFERENCES_UPDATE);
						result1.addParam("status", "Success");
						result1.addParam("msg", "User preferences updated successfully");
						result1.addParam("opstatus", "0");
						result1.addParam("httpStatusCode", "200");
						return result1;
					}
				} else {
					inputMap.put("id", HelperMethods.getNumericId() + "");
					HelperMethods.removeNullValues(inputMap);
					HelperMethods.callApi(request, inputMap, HelperMethods.getHeaders(request),
							URLConstants.WEALTH_USER_PREFERENCES_CREATE);
					result1.addParam("status", "Success");
					result1.addParam("msg", "User preferences created successfully");
					result1.addParam("opstatus", "0");
					result1.addParam("httpStatusCode", "200");
					return result1;
				}
			} else {
				return PortfolioWealthUtils.UnauthorizedAccess();
			}

		} catch (Exception e) {
			alert.prepareError("Exception occured while updating the field order from backend delegate :" + e.getMessage()).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_21130);
		}
		return null;
	}

	private Result getPreferences(String portfolioId, String userId, DataControllerRequest dcRequest)
			throws ApplicationException {
		Map<String, String> inputParams = new HashMap<>();
		String filter = "userId" + DBPUtilitiesConstants.EQUAL + userId + DBPUtilitiesConstants.AND + "portfolioId"
				+ DBPUtilitiesConstants.EQUAL + portfolioId;
		inputParams.put(DBPUtilitiesConstants.FILTER, filter);
		try {
			return HelperMethods.callGetApi(dcRequest, inputParams.get(DBPUtilitiesConstants.FILTER),
					HelperMethods.getHeaders(dcRequest), URLConstants.WEALTH_USER_PREFERENCES_GET);
		} catch (Exception e) {
			alert.prepareError("Exception occured while fetching the field order from backend delegate :" + e.getMessage()).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_21129);
		}
	}

}
