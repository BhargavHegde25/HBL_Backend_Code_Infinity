package com.temenos.infinity.api.wealthOrder.mockdata;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthOrder.config.WealthAPIServices;
import com.temenos.infinity.api.wealthservices.constants.ErrorCodeEnum;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthOrder.util.WealthMockUtil;

/**
 * (INFO) Prepares Result object from the mock data returned by the Util.
 *
 */
public class GetFavoriteInstrumentsMock implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {

		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
			Object ricCodesObj = inputParams.get(TemenosConstants.RICCODES);

			Object searchArr = null, sortByArr = null, sortOrderArr = null, pageSizeArr = null, pageOffsetArr = null;
			String search = "", sortBy = "", sortOrder = "", pageSize = "", pageOffset = "",ricCodes="";
			int totalCount = 0, pageSizeVal = 0, pageOffsetVal = 0;

			searchArr = inputParams.get("searchByInstrumentName");
			sortByArr = inputParams.get("sortBy");
			sortOrderArr = inputParams.get("sortOrder");
			pageSizeArr = inputParams.get("pageSize");
			pageOffsetArr = inputParams.get("pageOffset");

			if (searchArr != null) {
				search = searchArr.toString().trim();
			}
			if (sortByArr != null) {
				sortBy = sortByArr.toString().trim();
			}
			if (sortOrderArr != null) {
				sortOrder = sortOrderArr.toString().trim();
			}
			if (pageSizeArr != null) {
				pageSize = pageSizeArr.toString().trim();
			}
			if (pageOffsetArr != null) {
				pageOffset = pageOffsetArr.toString().trim();
			}
			if (ricCodesObj != null) {
				ricCodes = ricCodesObj.toString().trim();
			}

			pageSizeVal = (pageSize != null && pageSize.trim().length() > 0) ? Integer.parseInt(pageSize) : 0;
			pageOffsetVal = (pageOffset != null && pageOffset.trim().length() > 0) ? Integer.parseInt(pageOffset) : 0;

			JSONObject responseJSON = new JSONObject();
			JSONArray favoriteInstruments = mockGetFavoriteInstruments(ricCodes);
			if (search != null && !search.equals("")) {
				favoriteInstruments = returnSearch(favoriteInstruments, search);

			}
			totalCount = favoriteInstruments.length();
			JSONArray sortedFavoriteInstruments = getSortedJsonArray(favoriteInstruments, sortBy, sortOrder,
					pageSizeVal, pageOffsetVal);
			responseJSON.put("favoriteInstruments", sortedFavoriteInstruments);
			responseJSON.put("totalCount", totalCount);
			responseJSON.put("opstatus", "0");
			responseJSON.put("httpStatusCode", "200");
			return Utilities.constructResultFromJSONObject(responseJSON);

		} catch (Exception e) {
			alert.prepareError("Caught exception at invoke of getFavMock: ", e).log();
			return ErrorCodeEnum.ERR_20040.setErrorCode(new Result());
		}
	}

	private JSONArray mockGetFavoriteInstruments(String codesList) {
		WealthMockUtil wealthmockUtil = new WealthMockUtil();
		String[] codesArr = codesList.split("@");
		ArrayList<String> codesArrList = new ArrayList<String>(Arrays.asList(codesArr));
		JSONArray favoriteInstruments = wealthmockUtil.mockGetFavoriteInstruments(codesArrList);

		return favoriteInstruments;

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
						+ WealthAPIServices.WEALTH_GETFAVORITEINSTRUMENTS.getOperationName() + "  : " + e).log();
				return null;
			}
		}
		return filtedArray;
	}

	private JSONArray getSortedJsonArray(JSONArray jsonArr, String sortBy, String sortOrder, int size, int offset) {

		JSONArray sortedJsonArr = new JSONArray();
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dtf = new SimpleDateFormat("dd MMM yyyy 'at' HH:mm:ss");
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		
		if (sortBy == null || sortBy.isEmpty()) {
			sortBy = TemenosConstants.INSTRUMENTNAME;
		}
		String sortValue = sortBy;

		if (sortValue.equalsIgnoreCase(TemenosConstants.INSTRUMENTNAME)
				|| sortValue.equalsIgnoreCase(TemenosConstants.REFERENCECURRENCY)) {
			List<JSONObject> jsonValues = new ArrayList<JSONObject>();
			for (int i = 0; i < jsonArr.length(); i++) {
				JSONObject instrumentObj = jsonArr.getJSONObject(i);			
				Date date = sdf.parse(instrumentObj.get("dateReceived").toString(), new ParsePosition(0));
				String instrumentDate = df.format(date);
				String instrumentDateTime = instrumentDate + "T" + instrumentObj.get("timeReceived");
				String formatDateTime = dtf.format(dt.parse(instrumentDateTime, new ParsePosition(0)));
				instrumentObj.put("dateTime", instrumentDateTime);
				instrumentObj.put("formatDateTime", formatDateTime);
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
				JSONObject instrumentObj = jsonArr.getJSONObject(i);			
				Date date = sdf.parse(instrumentObj.get("dateReceived").toString(), new ParsePosition(0));
				String instrumentDate = df.format(date);
				String instrumentDateTime = instrumentDate + "T" + instrumentObj.get("timeReceived");
				String formatDateTime = dtf.format(dt.parse(instrumentDateTime, new ParsePosition(0)));
				instrumentObj.put("dateTime", instrumentDateTime);
				instrumentObj.put("formatDateTime", formatDateTime);
				jsonValues.add(jsonArr.getJSONObject(i));
			}
			Collections.sort(jsonValues, new Comparator<JSONObject>() {

				private final String KEY_NAME = sortValue;

				@Override
				public int compare(JSONObject a, JSONObject b) {
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
				JSONObject instrumentObj = jsonArr.getJSONObject(i);			
				Date date = sdf.parse(instrumentObj.get("dateReceived").toString(), new ParsePosition(0));
				String instrumentDate = df.format(date);
				String instrumentDateTime = instrumentDate + "T" + instrumentObj.get("timeReceived");
				String formatDateTime = dtf.format(dt.parse(instrumentDateTime, new ParsePosition(0)));
				instrumentObj.put("dateTime", instrumentDateTime);
				instrumentObj.put("formatDateTime", formatDateTime);
				jsonValues.add(jsonArr.getJSONObject(i));
			}
			Collections.sort(jsonValues, new Comparator<JSONObject>() {

				private final String KEY_NAME = sortValue;

				@Override
				public int compare(JSONObject a, JSONObject b) {
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
		} else if (sortValue.equalsIgnoreCase(TemenosConstants.DATE_TIME)) {
			List<JSONObject> jsonValues = new ArrayList<JSONObject>();
			for (int i = 0; i < jsonArr.length(); i++) {
				JSONObject instrumentObj = jsonArr.getJSONObject(i);			
				Date date = sdf.parse(instrumentObj.get("dateReceived").toString(), new ParsePosition(0));
				String instrumentDate = df.format(date);
				String instrumentDateTime = instrumentDate + "T" + instrumentObj.get("timeReceived");
				String formatDateTime = dtf.format(dt.parse(instrumentDateTime, new ParsePosition(0)));
				instrumentObj.put("dateTime", instrumentDateTime);
				instrumentObj.put("formatDateTime", formatDateTime);
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

}
