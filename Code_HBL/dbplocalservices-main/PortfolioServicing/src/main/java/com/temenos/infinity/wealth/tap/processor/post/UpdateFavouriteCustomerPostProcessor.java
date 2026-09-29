package com.temenos.infinity.wealth.tap.processor.post;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.wealth.common.util.CustomerUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;

/**
 * @author muthukumarv
 *
 */

public class UpdateFavouriteCustomerPostProcessor implements DataPostProcessor2 {

	private static final Logger LOG = LogManager.getLogger(UpdateFavouriteCustomerPostProcessor.class);

	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {
			Map<String, Object> inputMap = new HashMap<>();
			Result result1 = new Result();
			String customerId = HelperMethods.getCustomerIdFromSession(request);
			JSONObject actualRes = new JSONObject();
			String newBackendid, operation = null;
			if (request.getParameter("backendId") != null) {
				newBackendid = request.getParameter("backendId").toString();
			} else {
				newBackendid = "";
			}
			if (request.getParameter("operation") != null) {
				operation = request.getParameter("operation").toString();
			} else {
				operation = "";
			}
			Result finalResult = null;

			result = getFavouriteCustomers(customerId, request);
			if (HelperMethods.hasRecords(result)) {

				Record existingRecord = result.getAllDatasets().get(0).getAllRecords().get(0);

				String id = HelperMethods.getFieldValue(existingRecord, "id");

				String existingBackendIds = HelperMethods.getFieldValue(existingRecord, "favoriteCustomerId");
				String existingBackendIdsArr[] = existingBackendIds.trim().split(",");
				if (operation.equalsIgnoreCase("Add")) {
					//actualRes = CustomerUtils.updateCustomerDetailsFromTAP(existingBackendIds, existingBackendIdsArr, newBackendid, operation);

					if (!newBackendid.equals("") && (!Arrays.asList(existingBackendIdsArr).contains(newBackendid))) {
						actualRes = CustomerUtils.updateCustomerDetailsFromTAP(existingBackendIds, existingBackendIdsArr, newBackendid, operation);
					} else {
						actualRes = CustomerUtils.updateCustomerDetailsFromTAP(existingBackendIds, existingBackendIdsArr, newBackendid, operation);
						finalResult = CustomerUtils.finalResult(actualRes, TemenosConstants.FAILURE);
						return finalResult;
					}

				}
				List<String> existingBackendList = Arrays.asList(existingBackendIdsArr);
				if (operation.equalsIgnoreCase("Remove")) {
					if (!newBackendid.equals("") && (existingBackendList.contains(newBackendid))) {
						actualRes = CustomerUtils.updateCustomerDetailsFromTAP(existingBackendIds, existingBackendIdsArr, newBackendid, operation);
					} else {
						actualRes = CustomerUtils.updateCustomerDetailsFromTAP(existingBackendIds, existingBackendIdsArr, newBackendid, operation);
						finalResult = CustomerUtils.finalResult(actualRes, TemenosConstants.FAILURE);
						return finalResult;
					}
				}
				inputMap.put("id", id);
				inputMap.put("customerId", customerId);
				inputMap.put("favoriteCustomerId", actualRes.getString("existingBackendIds"));
				result1 = HelperMethods.callApi(request, inputMap, HelperMethods.getHeaders(request),
						URLConstants.INF_WLTH_FAVORITE_CUSTOMER_UPDATE);
				List<Dataset> dataset = result1.getAllDatasets();
				List<Record> drecords = dataset.get(0).getAllRecords();
				JSONObject resultJson = CommonUtils.convertRecordToJSONObject(drecords.get(0));
				resultJson.put("msg", actualRes.getString("msg"));
				finalResult = CustomerUtils.finalResult(resultJson, "Success");
				return finalResult;

			} else {
				if (operation.equalsIgnoreCase("Add")) {
					//inputMap.put("id", "1");
					inputMap.put("customerId", customerId);
					inputMap.put("favoriteCustomerId", newBackendid);
					result1 = HelperMethods.callApi(request, inputMap, HelperMethods.getHeaders(request),
							URLConstants.INF_WLTH_FAVORITE_CUSTOMER_CREATE);
					List<Dataset> dataset = result1.getAllDatasets();
					List<Record> drecords = dataset.get(0).getAllRecords();
					JSONObject resultJson = CommonUtils.convertRecordToJSONObject(drecords.get(0));
					resultJson.put("opstatus", result.getOpstatusParamValue());
					resultJson.put("httpStatusCode", "200");
					finalResult = Utilities.constructResultFromJSONObject(resultJson);
					// finalResult = splitfavInstrumentIds(finalResult);
					finalResult.addParam("status", "Success");
					finalResult.addParam("msg", "Favourite customers updated successfully");
					return finalResult;
				} else {
					JSONObject resultJson = new JSONObject();
					finalResult = Utilities.constructResultFromJSONObject(resultJson);
					finalResult.addParam("status", "Success");
					finalResult.addParam("msg", "Invalid operation");
					return finalResult;
				}
			}
		} catch (Exception e) {
			LOG.error("Exception occured while updating the field order from backend delegate :" + e.getMessage());
			throw new ApplicationException(ErrorCodeEnum.ERR_21130);
		}

	}

	private Result getFavouriteCustomers(String customerId, DataControllerRequest dcRequest)
			throws ApplicationException {
		Map<String, String> inputParams = new HashMap<>();
		String filter = "customerId" + DBPUtilitiesConstants.EQUAL + customerId;
		inputParams.put(DBPUtilitiesConstants.FILTER, filter);
		try {
			return HelperMethods.callGetApi(dcRequest, inputParams.get(DBPUtilitiesConstants.FILTER),
					HelperMethods.getHeaders(dcRequest), URLConstants.INF_WLTH_FAVORITE_CUSTOMER_GET);
		} catch (Exception e) {
			LOG.error("Exception occured while fetching the field order from backend delegate :" + e.getMessage());
			throw new ApplicationException(ErrorCodeEnum.ERR_21129);
		}
	}

}
