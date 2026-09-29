package com.temenos.infinity.api.wealth.backenddelegate.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;
import org.json.JSONArray;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealth.backenddelegate.api.FavoriteInstrumentsBackendDelegate;

import com.temenos.infinity.api.wealth.config.PortfolioWealthAPIServices;
import com.temenos.infinity.api.wealth.constants.OperationName;
import com.temenos.infinity.api.wealth.constants.ServiceId;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealth.util.WealthMockUtil;

public class FavoriteInstrumentsBackendDelegateImpl implements FavoriteInstrumentsBackendDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("unchecked")
	@Override
	public Result getFavoriteInstruments(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {

		Result favouritesResult = new Result();
		String sortBy = null, sortOrder = null, pageSize = null, pageOffset = null, search = null;
		Map<String, Object> inputMap = new HashMap<>();
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		String userId = HelperMethods.getUserIdFromSession(request);
		//Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		//String customerId = CustomerSession.getCustomerId(customer);
		String customerId = HelperMethods.getCustomerIdFromSession(request);
		String ricCodes = null, instrumentIds =null;
		DecimalFormat df = new DecimalFormat("0.00");
		Object pageSizeObj = inputParams.get(TemenosConstants.PAGESIZE);
		Object pageOffsetObj = inputParams.get(TemenosConstants.PAGEOFFSET);
		Object sortByObj = inputParams.get(TemenosConstants.SORTBY);
		Object sortOrderObj = inputParams.get(TemenosConstants.SORTORDER);
		Object searchObj = inputParams.get(TemenosConstants.SEARCHBYINSTRUMENTNAME);
		int totalCount = 0;

		List sortValues = Arrays.asList("", "instrumentName", "referenceCurrency", "lastRate", "percentageChange",
				"volume", "bidRate", "askRate", "dateTime");

		if (sortByObj != null && !sortValues.contains(sortByObj)) {
			alert.prepareError("Invalid request").log();
			JSONObject result = new JSONObject();
			result.put("status", "Failure");
			result.put("error", "Invalid Input Params!");
			return Utilities.constructResultFromJSONObject(result);
		}

		if (sortByObj != null) {
			sortBy = sortByObj.toString();
			inputMap.put("sortBy", sortBy);
		}

		if (sortOrderObj != null) {
			sortOrder = sortOrderObj.toString();
			inputMap.put("sortOrder", sortOrder);
		}
		if (pageSizeObj != null) {
			pageSize = pageSizeObj.toString();
			inputMap.put("pageSize", pageSize);
		}
		if (pageOffsetObj != null) {
			pageOffset = pageOffsetObj.toString();
			inputMap.put("pageOffset", pageOffset);
		}
		if (searchObj != null) {
			search = inputParams.get(TemenosConstants.SEARCHBYINSTRUMENTNAME).toString();
			inputMap.put("searchByInstrumentName", search);
		}

		int pageSizeVal = (pageSize != null && pageSize.trim().length() > 0) ? Integer.parseInt(pageSize) : 0;
		int pageOffsetVal = (pageOffset != null && pageOffset.trim().length() > 0) ? Integer.parseInt(pageOffset) : 0;

		try {
			UserFavouriteInstrumentsBackendDelegateImpl userFavourites = new UserFavouriteInstrumentsBackendDelegateImpl();
			favouritesResult = userFavourites.getFavouriteInstruments(methodID, inputArray, request, response,
					headersMap);
			ricCodes = favouritesResult.getParamValueByName(TemenosConstants.USERFAVORITESCODES);
			instrumentIds = favouritesResult.getParamValueByName(TemenosConstants.USERFAVORITESIDS);
			if (ricCodes == null) {
				ricCodes = "";
			}
			if (instrumentIds == null) {
				instrumentIds = "";
			}

		} catch (Exception e) {
			alert.prepareError("Error while invoking Favourites Instruments - "
					+ PortfolioWealthAPIServices.WEALTH_GETFAVORITEINSTRUMENTS.getOperationName() + "  : " + e).log();
			return null;
		}

//		ricCodes = "AMZN.O:TSLA.OQ:GOOGL.O";
		if (ricCodes.equals("") && instrumentIds.equals("")) {
			JSONObject responseJSON = new JSONObject();
			responseJSON.put("totalCount", totalCount);
			return Utilities.constructResultFromJSONObject(responseJSON);
		}
		inputMap.put("Fields",
				"CF_CURRENCY:CF_EXCHNG:ISIN_CODE:TRADE_DATE:CF_NAME:PRCTCK_1:BID:ASK:TRDPRC_1:PCTCHNG:CF_CLOSE:CF_NETCHNG:CF_DATE:CF_TIME:CF_BID:BIDSIZE:CF_ASK:ASKSIZE:CF_VOLUME:CF_LAST");
		inputMap.put("RequestKey", getKeyArray(ricCodes).toString());
		inputMap.put(TemenosConstants.RICCODES, ricCodes);//For Mockdata

		String returnResponse = null;
		String serviceName = ServiceId.WEALTHORCHESTRATION;
		String operationName = OperationName.GET_FAVOURITE_INSTRUMENT;

		try {
			returnResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(inputMap).withRequestHeaders(headersMap)
					.withDataControllerRequest(request).build().getResponse();
			JSONObject resultJSON = new JSONObject(returnResponse);
			return Utilities.constructResultFromJSONObject(resultJSON);

		} catch (Exception e) {
			alert.prepareError("Error while invoking Transact - " + PortfolioWealthAPIServices.GET_FAVOURITE_INSTRUMENT.getOperationName()
					+ "  : " + e).log();
			return null;
		}

	}

	private JSONArray mockGetFavoriteInstruments(String codesList) {
		WealthMockUtil wealthmockUtil = new WealthMockUtil();
		String[] codesArr = codesList.split(":");
		ArrayList<String> codesArrList = new ArrayList<String>(Arrays.asList(codesArr));
		JSONArray favoriteInstruments = wealthmockUtil.mockGetFavoriteInstruments(codesArrList);

		return favoriteInstruments;

	}

	private JSONArray getKeyArray(String codesList) {

		JSONArray requestKey = new JSONArray();
		String[] codesArr = codesList.split(":");
		for (int i = 0; i < codesArr.length; i++) {
			JSONObject key = new JSONObject();
			key.put("Name", codesArr[i]);
			key.put("NameType", "RIC");
			requestKey.put(key);
		}
		return requestKey;

	}

	private JSONArray getSortedJsonArray(JSONArray jsonArr, String sortBy, String sortOrder, int size, int offset) {

		JSONArray sortedJsonArr = new JSONArray();

		if (sortBy == null || sortBy.isEmpty()) {
			sortBy = TemenosConstants.INSTRUMENTNAME;
		}
		String sortValue = sortBy;

		if (sortValue.equalsIgnoreCase(TemenosConstants.INSTRUMENTNAME)
				|| sortValue.equalsIgnoreCase(TemenosConstants.REFERENCECURRENCY)) {
			List<JSONObject> jsonValues = new ArrayList<JSONObject>();
			for (int i = 0; i < jsonArr.length(); i++) {
				jsonValues.add(jsonArr.getJSONObject(i));
			}
			Collections.sort(jsonValues, new Comparator<JSONObject>() {

				private final String KEY_NAME = sortValue;

				@Override
				public int compare(JSONObject a, JSONObject b) {
					String str1 = new String();
					String str2 = new String();
					str1 = (String) a.get(KEY_NAME);
					str2 = (String) b.get(KEY_NAME);
					return str1.compareToIgnoreCase(str2);
				}

			});
			if (sortOrder != null && sortOrder.equalsIgnoreCase(TemenosConstants.DESCENDING)) {
				for (int i = jsonArr.length() - 1; i >= 0; i--) {
					sortedJsonArr.put(jsonValues.get(i));
				}

			} else {
				for (int i = 0; i < jsonArr.length(); i++) {
					sortedJsonArr.put(jsonValues.get(i));
				}
			}

		} else if (sortValue.equalsIgnoreCase("bidRate") || sortValue.equalsIgnoreCase("askRate")
				|| sortValue.equalsIgnoreCase("lastRate") || sortBy.equalsIgnoreCase("percentageChange")) {
			List<JSONObject> jsonValues = new ArrayList<JSONObject>();
			for (int i = 0; i < jsonArr.length(); i++) {
				jsonValues.add(jsonArr.getJSONObject(i));
			}
			Collections.sort(jsonValues, new Comparator<JSONObject>() {

				private final String KEY_NAME = sortValue;

				@Override
				public int compare(JSONObject a, JSONObject b) {
					if (a.has(KEY_NAME) && b.has(KEY_NAME) && a.get(KEY_NAME).toString().equalsIgnoreCase("")) {
						return (b.get(KEY_NAME).toString().equalsIgnoreCase("")) ? 0 : -1;
					}
					if (b.has(KEY_NAME) && b.get(KEY_NAME).toString().equalsIgnoreCase("")) {
						return 1;
					}
					Double dbl1 = null;
					Double dbl2 = null;
					dbl1 = Double.parseDouble(a.get(KEY_NAME).toString());
					dbl2 = Double.parseDouble(b.get(KEY_NAME).toString());
					return dbl1.compareTo(dbl2);
				}

			});
			if (sortOrder != null && sortOrder.equalsIgnoreCase(TemenosConstants.DESCENDING)) {
				for (int i = jsonArr.length() - 1; i >= 0; i--) {
					sortedJsonArr.put(jsonValues.get(i));
				}

			} else {
				for (int i = 0; i < jsonArr.length(); i++) {
					sortedJsonArr.put(jsonValues.get(i));
				}
			}
		} else if (sortValue.equalsIgnoreCase("volume")) {
			List<JSONObject> jsonValues = new ArrayList<JSONObject>();
			for (int i = 0; i < jsonArr.length(); i++) {
				jsonValues.add(jsonArr.getJSONObject(i));
			}
			Collections.sort(jsonValues, new Comparator<JSONObject>() {

				private final String KEY_NAME = sortValue;

				@Override
				public int compare(JSONObject a, JSONObject b) {
					if (a.has(KEY_NAME) && b.has(KEY_NAME) && a.get(KEY_NAME).toString().equalsIgnoreCase("")) {
						return (b.get(KEY_NAME).toString().equalsIgnoreCase("")) ? 0 : -1;
					}
					if (b.has(KEY_NAME) && b.get(KEY_NAME).toString().equalsIgnoreCase("")) {
						return 1;
					}
					Integer l1 = null;
					Integer l2 = null;
					l1 = (Integer) a.get(KEY_NAME);
					l2 = (Integer) b.get(KEY_NAME);
					return l1.compareTo(l2);
				}

			});
			if (sortOrder != null && sortOrder.equalsIgnoreCase(TemenosConstants.DESCENDING)) {
				for (int i = jsonArr.length() - 1; i >= 0; i--) {
					sortedJsonArr.put(jsonValues.get(i));
				}

			} else {
				for (int i = 0; i < jsonArr.length(); i++) {
					sortedJsonArr.put(jsonValues.get(i));
				}
			}
		}

		if (size > 0 && offset >= 0) {
			sortedJsonArr = pagination(sortedJsonArr, size, offset);
		}
		return sortedJsonArr;
	}

	public JSONArray pagination(JSONArray sortedJSON, int size, int offset) {

		JSONArray paginationJSON = new JSONArray();

		int j = 0;
		for (int i = offset; i < sortedJSON.length(); i++) {
			if (j == size) {
				break;
			} else {
				paginationJSON.put(sortedJSON.get(i));
			}
			j++;
		}

		return paginationJSON;

	}

	public JSONArray returnSearch(JSONArray array, String searchValue) {
		JSONArray filtedArray = new JSONArray();
		for (int i = 0; i < array.length(); i++) {
			JSONObject obj = null;
			try {
				obj = array.getJSONObject(i);
				String desc = obj.getString("instrumentName").toLowerCase();
				String search = searchValue.toLowerCase();
				if (desc.contains(search)) {
					filtedArray.put(obj);
				}
			} catch (Exception e) {

				alert.prepareError("Error while searching Instruments - "
						+ PortfolioWealthAPIServices.WEALTH_GETFAVORITEINSTRUMENTS.getOperationName() + "  : " + e).log();
				return null;
			}
		}
		return filtedArray;
	}
}
