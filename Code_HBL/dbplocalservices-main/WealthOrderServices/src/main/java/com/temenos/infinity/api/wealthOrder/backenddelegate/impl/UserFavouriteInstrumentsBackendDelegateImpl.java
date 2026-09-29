package com.temenos.infinity.api.wealthOrder.backenddelegate.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONObject;

import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.UserFavouriteInstrumentsBackendDelegate;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

public class UserFavouriteInstrumentsBackendDelegateImpl implements UserFavouriteInstrumentsBackendDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Result getFavouriteInstruments(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) throws ApplicationException {
		// Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		String userId = HelperMethods.getUserIdFromSession(request);
		// Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		// String customerId = CustomerSession.getCustomerId(customer);
		// String customerId = HelperMethods.getCustomerIdFromSession(request);
		JSONObject resultJson = new JSONObject();
		String filter = null;
		Result result = new Result();
		Result finalResult = new Result();

		if (userId == null || userId.equals("")) {
			alert.prepareError("Invalid request").log();
			JSONObject errResult = new JSONObject();
			errResult.put("status", "Failure");
			errResult.put("error", "Invalid User!");
			return Utilities.constructResultFromJSONObject(errResult);
		}

		Map<String, String> input = new HashMap<>();

		filter = "userId" + DBPUtilitiesConstants.EQUAL + userId;

		input.put(DBPUtilitiesConstants.FILTER, filter);

		try {
			result = HelperMethods.callGetApi(request, input.get(DBPUtilitiesConstants.FILTER),
					HelperMethods.getHeaders(request), URLConstants.WEALTH_USER_FAVORITES_GET);
			if (HelperMethods.hasRecords(result)) {
				List<Dataset> dataset = result.getAllDatasets();
				List<Record> drecords = dataset.get(0).getAllRecords();
				resultJson = CommonUtils.convertRecordToJSONObject(drecords.get(0));
				//Splitted to instrumentids
				String favInstrumentIds = resultJson.get("favInstrumentIds").toString();
				if (favInstrumentIds != null && favInstrumentIds.length() > 0) {
					String favInstrumentIdsArr[] = favInstrumentIds.trim().split("@");
					String instrumentId = "";
					for (String s : favInstrumentIdsArr) {
						instrumentId =  instrumentId + s.trim().split("~")[0].trim() + " " ;
					}
					resultJson.put("favInstrumentIds", instrumentId.trim().replace(" ", "@"));
					resultJson.put("instr_app", favInstrumentIds);
				}//
			}

			resultJson.put("opstatus", result.getOpstatusParamValue());
			resultJson.put("httpStatusCode", result.getHttpStatusCodeParamValue());
			finalResult = Utilities.constructResultFromJSONObject(resultJson);
		} catch (Exception e) {
			alert.prepareError("Exception occured while fetching the field order from backend delegate :" + e.getMessage()).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_21001);
		}

		return finalResult;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Result updateFavouriteInstruments(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) throws ApplicationException {
		Result result = new Result();
		Result result1 = new Result();
		Result finalResult = null;
		try {
			Map<String, Object> inputMap = new HashMap<>();
			Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
			Object ricObj = inputParams.get(TemenosConstants.RICCODE);
			Object instrumentObj = inputParams.get(TemenosConstants.INSTRUMENTID);
			Object operationObj = inputParams.get(TemenosConstants.OPERATION);
			String ricCode = null, instrumentId = null, operation = null, application = null;
			String userId = HelperMethods.getUserIdFromSession(request);
			// Map<String, Object> customer = CustomerSession.getCustomerMap(request);
			// String customerId = CustomerSession.getCustomerId(customer);
			// String customerId = HelperMethods.getCustomerIdFromSession(request);
			if (userId == null || userId.equals("")) {
				alert.prepareError("Invalid request").log();
				JSONObject resultJSON = new JSONObject();
				resultJSON.put("status", "Failure");
				resultJSON.put("error", "Invalid User!");
				return Utilities.constructResultFromJSONObject(resultJSON);
			}
			if (operationObj == null || operationObj.equals("")
					|| !(inputParams.get(TemenosConstants.OPERATION).toString().equalsIgnoreCase("ADD")
							|| inputParams.get(TemenosConstants.OPERATION).toString().equalsIgnoreCase("REMOVE"))) {
				alert.prepareError("Invalid request").log();
				JSONObject resultJSON = new JSONObject();
				resultJSON.put("status", "Failure");
				resultJSON.put("error", "Invalid Input operation!");
				return Utilities.constructResultFromJSONObject(resultJSON);
			}
			if (ricObj != null) {
				ricCode = inputParams.get(TemenosConstants.RICCODE).toString();
			} else {
				ricCode = "";
			}
			if (operationObj != null) {
				operation = inputParams.get(TemenosConstants.OPERATION).toString();
			}
			if (instrumentObj != null) {
				instrumentId = inputParams.get(TemenosConstants.INSTRUMENTID).toString();
			} else {
				instrumentId = "";
			}

			if (inputParams.get(TemenosConstants.APPLICATION) != null
					&& inputParams.get(TemenosConstants.APPLICATION).toString().length() > 0) {
				application = inputParams.get(TemenosConstants.APPLICATION).toString();
				instrumentId = (instrumentId + "~" + application).trim();
			}

			result = getFavouriteInstruments(userId, request);
			if (HelperMethods.hasRecords(result)) {

				Record existingRecord = result.getAllDatasets().get(0).getAllRecords().get(0);

				String id = HelperMethods.getFieldValue(existingRecord, "id");

				String favInstrumentCodes = HelperMethods.getFieldValue(existingRecord,
						TemenosConstants.USERFAVORITESCODES);
				String favInstrumentCodesArr[] = favInstrumentCodes.trim().split("@");
				String favInstrumentIds = HelperMethods.getFieldValue(existingRecord,
						TemenosConstants.USERFAVORITESIDS);
				if (operation.equalsIgnoreCase("Add")) {

					if (!ricCode.equals("")
							&& (!Arrays.asList(favInstrumentCodesArr).contains(ricCode) || ricCode.equalsIgnoreCase("RICCode"))) {
						if (favInstrumentCodes.equals("")) {
							favInstrumentCodes = ricCode;

						} else {
							favInstrumentCodes = favInstrumentCodes + "@" + ricCode;
						}
					}
					if (!instrumentId.equals("") && !favInstrumentIds.contains(instrumentId)) {
						if (favInstrumentIds.equals("")) {
							favInstrumentIds = instrumentId;
						} else {
							favInstrumentIds = favInstrumentIds + "@" + instrumentId;
						}
					}
				}
				List<String> ricList = Arrays.asList(favInstrumentCodesArr);
				if (operation.equalsIgnoreCase("Remove") && (!ricCode.equals(""))) {
					if(ricList.contains(ricCode)) {
						int index = ricList.indexOf(ricCode);
						favInstrumentCodesArr = ArrayUtils.remove(favInstrumentCodesArr,index);
						String removedList = StringUtils.join(favInstrumentCodesArr, "@");
						favInstrumentCodes = removedList;
					}
					
				}
				if (operation.equalsIgnoreCase("Remove") && (!instrumentId.equals(""))) {
					String strToRemove1 = "@" + instrumentId;
					String strToRemove2 = instrumentId + "@";
					favInstrumentIds = favInstrumentIds.replace(strToRemove1, "");
					favInstrumentIds = favInstrumentIds.replace(strToRemove2, "");
					favInstrumentIds = favInstrumentIds.replace(instrumentId, "");
				}
				inputMap.put("id", id);
				inputMap.put(TemenosConstants.USERFAVORITESCODES, favInstrumentCodes);
				inputMap.put(TemenosConstants.USERFAVORITESIDS, favInstrumentIds);
				result1 = HelperMethods.callApi(request, inputMap, HelperMethods.getHeaders(request),
						URLConstants.WEALTH_USER_FAVORITES_UPDATE);
				List<Dataset> dataset = result1.getAllDatasets();
				List<Record> drecords = dataset.get(0).getAllRecords();
				JSONObject resultJson = CommonUtils.convertRecordToJSONObject(drecords.get(0));
				resultJson.put("opstatus", result.getOpstatusParamValue());
				resultJson.put("httpStatusCode", result.getHttpStatusCodeParamValue());
				finalResult = Utilities.constructResultFromJSONObject(resultJson);
				finalResult = splitfavInstrumentIds(finalResult);
				finalResult.addParam("status", "Success");
				finalResult.addParam("msg", "User favourites updated successfully");
				return finalResult;

			} else {
				if (operation.equalsIgnoreCase("Add")) {
					inputParams.put(TemenosConstants.USERFAVORITESCODES, ricCode);
					inputParams.put(TemenosConstants.USERFAVORITESIDS, instrumentId);
					result1 = createFavouriteInstruments(methodID, inputArray, request, response, headersMap);
					List<Dataset> dataset = result1.getAllDatasets();
					List<Record> drecords = dataset.get(0).getAllRecords();
					JSONObject resultJson = CommonUtils.convertRecordToJSONObject(drecords.get(0));
					resultJson.put("opstatus", result.getOpstatusParamValue());
					resultJson.put("httpStatusCode", result.getHttpStatusCodeParamValue());
					finalResult = Utilities.constructResultFromJSONObject(resultJson);
					finalResult = splitfavInstrumentIds(finalResult);
					finalResult.addParam("status", "Success");
					finalResult.addParam("msg", "User favourites updated successfully");
					return finalResult;
				} else {
					JSONObject resultJson = new JSONObject();
					finalResult = Utilities.constructResultFromJSONObject(resultJson);
					finalResult.addParam("status", "Success");
					finalResult.addParam("msg", "Instrument was not in Favourites");
					return finalResult;
				}
			}

		} catch (Exception e) {
			alert.prepareError("Exception occured while updating the field order from backend delegate :" + e.getMessage()).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_21130);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Result createFavouriteInstruments(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) throws ApplicationException {
		Result result = new Result();
		Result result1 = new Result();
		try {
			Map<String, Object> inputMap = new HashMap<>();
			Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
			Object ricObj = inputParams.get(TemenosConstants.RICCODE);
			Object instrumentObj = inputParams.get(TemenosConstants.INSTRUMENTID);
			String ricCode = null, instrumentId = null;
			String userId = HelperMethods.getUserIdFromSession(request);
			// Map<String, Object> customer = CustomerSession.getCustomerMap(request);
			// String customerId = CustomerSession.getCustomerId(customer);
			String customerId = HelperMethods.getCustomerIdFromSession(request);
			if (userId == null || userId.equals("")) {
				alert.prepareError("Invalid request").log();
				result.addParam("status", "Failure");
				result.addParam("error", "Invalid User!");
				return result;
			} else {
				if (ricObj != null) {
					ricCode = inputParams.get(TemenosConstants.RICCODE).toString();
				} else {
					ricCode = "";
				}
				if (instrumentObj != null) {
					instrumentId = inputParams.get(TemenosConstants.INSTRUMENTID).toString();
				} else {
					instrumentId = "";
				}
				inputMap.put(TemenosConstants.USERFAVORITESCODES, ricCode);
				inputMap.put(TemenosConstants.USERFAVORITESIDS, instrumentId);
				inputMap.put(TemenosConstants.USERID, userId);
				inputMap.put(TemenosConstants.CUSTOMERID, customerId);
			}

			result1 = HelperMethods.callApi(request, inputMap, HelperMethods.getHeaders(request),
					URLConstants.WEALTH_USER_FAVORITES_CREATE);
			return result1;

		} catch (Exception e) {
			alert.prepareError("Exception occured while updating the field order from backend delegate :" + e.getMessage()).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_21130);
		}
	}

	private Result getFavouriteInstruments(String userId, DataControllerRequest dcRequest) throws ApplicationException {
		Map<String, String> inputParams = new HashMap<>();
		String filter = "userId" + DBPUtilitiesConstants.EQUAL + userId;
		inputParams.put(DBPUtilitiesConstants.FILTER, filter);
		try {
			return HelperMethods.callGetApi(dcRequest, inputParams.get(DBPUtilitiesConstants.FILTER),
					HelperMethods.getHeaders(dcRequest), URLConstants.WEALTH_USER_FAVORITES_GET);
		} catch (Exception e) {
			alert.prepareError("Exception occured while fetching the field order from backend delegate :" + e.getMessage()).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_21129);
		}
	}

	private Result splitfavInstrumentIds(Result result) {

		try {
			String favInstrumentIds = result.getParamValueByName("favInstrumentIds").toString();
			if (favInstrumentIds != null && favInstrumentIds.length() > 0) {
				String favInstrumentIdsArr[] = favInstrumentIds.trim().split("@");
				String instrumentId = "";
				for (String s : favInstrumentIdsArr) {
					instrumentId =  instrumentId + s.trim().split("~")[0].trim() + " " ;
				}
				result.addParam(TemenosConstants.INSTRUMENTID, instrumentId.trim());
				result.addParam("favInstrumentIds", instrumentId.trim().replace(" ", "@"));
				result.addParam("instr_app", favInstrumentIds);
			}
		} catch (Exception e) {
			alert.prepareError("splitfavInstrumentIds").log();
		}
		return result;
	}

}
