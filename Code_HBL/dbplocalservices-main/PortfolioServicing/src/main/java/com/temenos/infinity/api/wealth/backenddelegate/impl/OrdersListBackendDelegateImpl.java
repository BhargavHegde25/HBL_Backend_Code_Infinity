/**
 * 
 */
package com.temenos.infinity.api.wealth.backenddelegate.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealth.backenddelegate.api.OrdersListBackendDelegate;
import com.temenos.infinity.api.wealth.config.PortfolioWealthAPIServices;
import com.temenos.infinity.api.wealth.constants.OperationName;
import com.temenos.infinity.api.wealth.constants.ServiceId;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealth.util.WealthMockUtil;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;

/**
 * @author balaji.kk
 *
 */
public class OrdersListBackendDelegateImpl implements OrdersListBackendDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	WealthMockUtil wealthMockUtil = new WealthMockUtil();

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Result getOrdersDetails(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		Map<String, Object> inputMap = new HashMap<>();
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		Object sortByObj = inputParams.get(TemenosConstants.SORTBY);

		int indexOfSort = 0;
		String portfolioId = null, startDate = null, endDate = null, sortBy = null, search = null, orderviewType = null,
				cancelorderID = null, sortType = null, limit = null, offset = null, instrumentId = null,
				dateformat = "YYYY-MM-dd";

		List sortValues = Arrays.asList("description", "orderType", "quantity", "limitPrice", "tradeDate", "price",
				"status", "orderReference", "stopPrice", "orderExecutionPrice");

		String[] fieldName = new String[] { "tradeDate", "quantity", "limitPrice", "description", "orderReference",
				"stopPrice" };
		String[] columnName = new String[] { "ORDER_ENTRY_DATE", "QUANTITY", "LIMIT", "INSTR_NAME", "ORDER_CODE",
				"STOP" };

		if (!sortValues.contains(sortByObj)) {
			alert.prepareError("Invalid request").log();
			JSONObject result = new JSONObject();
			result.put("status", "Failure");
			result.put("error", TemenosConstants.SORTBY + " value is not valid");
			return Utilities.constructResultFromJSONObject(result);
		}

		if (inputParams.get(TemenosConstants.PORTFOLIOID) != null
				&& inputParams.get(TemenosConstants.PORTFOLIOID).toString().trim().length() > 0) {
			portfolioId = inputParams.get(TemenosConstants.PORTFOLIOID).toString();
			inputMap.put(TemenosConstants.PORTFOLIOID, portfolioId);
		} else {
			return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.PORTFOLIOID);
		}

		if (inputParams.get(TemenosConstants.ORDERSVIEW_TYPE) != null
				&& (TemenosConstants.ORDERSVIEW_TYPE.length() > 0)) {
			orderviewType = inputParams.get(TemenosConstants.ORDERSVIEW_TYPE).toString();
			inputMap.put(TemenosConstants.ORDERSVIEW_TYPE, orderviewType);
			if (orderviewType.trim().equalsIgnoreCase("OPEN") || orderviewType.trim().equalsIgnoreCase("HISTORY")) {

				if (inputParams.get(TemenosConstants.STARTDATE) != null) {
					startDate = inputParams.get(TemenosConstants.STARTDATE).toString();
					inputMap.put(TemenosConstants.STARTDATE, startDate);
					inputMap.put("fromDate", startDate);
					boolean isTrue = PortfolioWealthUtils.validateDateFormat(dateformat, startDate);
					if (!isTrue) {
						return PortfolioWealthUtils.validateFormat(TemenosConstants.STARTDATE);
					}
				} else if (orderviewType.trim().equalsIgnoreCase("HISTORY")) {
					return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.STARTDATE);
				}

				if (inputParams.get(TemenosConstants.ENDDATE) != null) {
					endDate = inputParams.get(TemenosConstants.ENDDATE).toString();
					inputMap.put(TemenosConstants.ENDDATE, endDate);
					inputMap.put("toDate", endDate);
					boolean isTrue = PortfolioWealthUtils.validateDateFormat(dateformat, endDate);
					if (!isTrue) {
						return PortfolioWealthUtils.validateFormat(TemenosConstants.ENDDATE);
					}
				} else if (orderviewType.trim().equalsIgnoreCase("HISTORY")) {
					return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.ENDDATE);
				}

			} else {
				return PortfolioWealthUtils.validateMandatoryFields("Type should be Open/History");
			}
		} else {
			return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.ORDERSVIEW_TYPE);
		}

		if (inputParams.get(TemenosConstants.SORTBY) != null
				&& sortValues.contains(inputParams.get(TemenosConstants.SORTBY))) {
			sortBy = inputParams.get(TemenosConstants.SORTBY).toString();
			inputMap.put(TemenosConstants.SORTBY, sortBy);
			indexOfSort = ArrayUtils.indexOf(fieldName, sortBy);
			if (indexOfSort == -1) {
				indexOfSort = 0;
			}
		} else {
			inputMap.put(TemenosConstants.SORTBY, "description");
			indexOfSort = ArrayUtils.indexOf(fieldName, "description");
			if (indexOfSort == -1) {
				indexOfSort = 0;
			}
		}

		if (inputParams.get(TemenosConstants.SEARCHBYINSTRUMENTNAME) != null) {
			search = inputParams.get(TemenosConstants.SEARCHBYINSTRUMENTNAME).toString();
			inputMap.put(TemenosConstants.SEARCHBYINSTRUMENTNAME, search);
		}

		if (inputParams.get(TemenosConstants.ORDER_ID) != null) {
			cancelorderID = inputParams.get(TemenosConstants.ORDER_ID).toString();
			inputMap.put(TemenosConstants.ORDER_ID, cancelorderID);
		}

		if (inputParams.get(TemenosConstants.SORTORDER) != null) {
			sortType = inputParams.get(TemenosConstants.SORTORDER).toString();
			inputMap.put(TemenosConstants.SORTORDER, sortType);
			inputMap.put(TemenosConstants.ORDERBY, columnName[indexOfSort]);
			inputMap.put(TemenosConstants.SORTTYPE, sortType);
		} else {
			if (sortBy.equalsIgnoreCase("tradeDate")) {
				inputMap.put(TemenosConstants.ORDERBY, columnName[indexOfSort]);
				inputMap.put(TemenosConstants.SORTTYPE, "desc");
			} else {
				inputMap.put(TemenosConstants.ORDERBY, columnName[indexOfSort]);
				inputMap.put(TemenosConstants.SORTTYPE, "asc");
			}
		}

		if (inputParams.get(TemenosConstants.PAGESIZE) != null) {
			limit = inputParams.get(TemenosConstants.PAGESIZE).toString();
			inputMap.put(TemenosConstants.PAGESIZE, limit);
		}

		if (inputParams.get(TemenosConstants.PAGEOFFSET) != null) {
			offset = inputParams.get(TemenosConstants.PAGEOFFSET).toString();
			inputMap.put(TemenosConstants.PAGEOFFSET, offset);
		}

		if (inputParams.get(TemenosConstants.INSTRUMENTID) != null) {
			instrumentId = inputParams.get(TemenosConstants.INSTRUMENTID).toString();
			inputMap.put("instrumentId", instrumentId);
		}
		List<String> allportfoliosList = PortfolioWealthUtils.getAllPortfoliosFromCache(request);

		if (allportfoliosList.contains(portfolioId)) {
			try {
				String serviceName = ServiceId.WEALTHORCHESTRATION;
				String operationName = OperationName.GETORDERDETAILS;

				return PortfolioWealthUtils.backendResponse(serviceName, operationName, inputMap, headersMap, request);

			} catch (Exception e) {
				alert.prepareError("Error while invoking Transact - "
						+ PortfolioWealthAPIServices.WEALTH_GETORDERSDETAILS.getOperationName() + "  : " + e).log();
				return null;

			}
		} else {

			return PortfolioWealthUtils.UnauthorizedAccess();

		}

	}

	public JSONArray sortArray(JSONArray array, String sortBy, String sortType) {
		JSONArray sortedJSON = new JSONArray();
		JSONArray sortedJSONArray = new JSONArray();
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
		List<JSONObject> jsonValues = new ArrayList<JSONObject>();
		for (int i = 0; i < array.length(); i++) {
			jsonValues.add(array.getJSONObject(i));
		}
		if (sortBy.equals("") || sortBy.equalsIgnoreCase(TemenosConstants.DESCRIPTION)) {// Instrument Alphabetically
			Collections.sort(jsonValues, new Comparator<JSONObject>() {
				private final String KEY_NAME = TemenosConstants.DESCRIPTION;

				@Override
				public int compare(JSONObject a, JSONObject b) {
					String str1 = new String();
					String str2 = new String();
					str1 = a.has(KEY_NAME) ? (String) a.get(KEY_NAME) : "";
					str2 = b.has(KEY_NAME) ? (String) b.get(KEY_NAME) : "";
					return str1.compareToIgnoreCase(str2);
				}

			});
			for (int i = 0; i < array.length(); i++) {
				sortedJSON.put(jsonValues.get(i));
			}

			if (sortType != null && sortType.equalsIgnoreCase(TemenosConstants.DESCENDING)) {
				for (int i = sortedJSON.length() - 1; i >= 0; i--) {
					sortedJSONArray.put(jsonValues.get(i));
				}

			} else {
				for (int i = 0; i < sortedJSON.length(); i++) {
					sortedJSONArray.put(jsonValues.get(i));
				}
			}

		} else if (sortBy.equalsIgnoreCase(TemenosConstants.ORDERTYPE)// TYPE, STATUS
				|| sortBy.equalsIgnoreCase(TemenosConstants.ORDERS_STATUS)
				|| sortBy.equalsIgnoreCase(TemenosConstants.ORDER_REFERENCE)) {
			Collections.sort(jsonValues, new Comparator<JSONObject>() {

				private final String KEY_NAME = sortBy;

				@Override
				public int compare(JSONObject a, JSONObject b) {
					String str1 = new String();
					String str2 = new String();
					str1 = a.has(KEY_NAME) ? (String) a.get(KEY_NAME) : "";
					str2 = b.has(KEY_NAME) ? (String) b.get(KEY_NAME) : "";
					return str1.compareToIgnoreCase(str2);
				}

			});
			for (int i = 0; i < array.length(); i++) {
				sortedJSON.put(jsonValues.get(i));
			}

			if (sortType != null && sortType.equalsIgnoreCase(TemenosConstants.DESCENDING)) {
				for (int i = sortedJSON.length() - 1; i >= 0; i--) {
					sortedJSONArray.put(jsonValues.get(i));
				}

			} else {
				for (int i = 0; i < sortedJSON.length(); i++) {
					sortedJSONArray.put(jsonValues.get(i));
				}
			}

		} else if (sortBy.equalsIgnoreCase(TemenosConstants.TRADEDATE)) {// DATE
			Collections.sort(jsonValues, new Comparator<JSONObject>() {
				private final String KEY_NAME = TemenosConstants.TRADEDATE;

				@Override
				public int compare(JSONObject a, JSONObject b) {
					String str1 = new String();
					String str2 = new String();
					str1 = a.has(KEY_NAME) ? (String) a.get(KEY_NAME) : "";
					str2 = b.has(KEY_NAME) ? (String) b.get(KEY_NAME) : "";
					Date d1 = new Date();
					Date d2 = new Date();
					try {
						if (str1 != null && str1.trim().length() > 0) {
							d1 = sdformat.parse(str1);
							d2 = sdformat.parse(str2);
						}
					} catch (ParseException e) {
						e.getMessage();
					}
					return d1.compareTo(d2);
				}

			});
			for (int i = array.length() - 1; i >= 0; i--) {
				sortedJSON.put(jsonValues.get(i));
			}

			if (sortType != null && sortType.equalsIgnoreCase(TemenosConstants.ASCENDING)) {
				for (int i = sortedJSON.length() - 1; i >= 0; i--) {
					sortedJSONArray.put(sortedJSON.get(i));
				}

			} else {
				for (int i = 0; i < sortedJSON.length(); i++) {
					sortedJSONArray.put(sortedJSON.get(i));
				}
			}
		} else {// QUANTITY , PRICE, LIMIT PRICE
			Collections.sort(jsonValues, new Comparator<JSONObject>() {
				private final String KEY_NAME = sortBy;

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
					dbl1 = (a.has(KEY_NAME) && a.get(KEY_NAME).toString().length() > 0)
							? Double.parseDouble((a.get(KEY_NAME)).toString())
							: 0;
					dbl2 = (b.has(KEY_NAME) && b.get(KEY_NAME).toString().length() > 0)
							? Double.parseDouble((b.get(KEY_NAME)).toString())
							: 0;
					return dbl1.compareTo(dbl2);
				}
			});

			for (int i = 0; i < array.length(); i++) {
				sortedJSON.put(jsonValues.get(i));
			}

			if (sortType != null && sortType.equalsIgnoreCase(TemenosConstants.DESCENDING)) {
				for (int i = sortedJSON.length() - 1; i >= 0; i--) {
					sortedJSONArray.put(jsonValues.get(i));
				}

			} else {
				for (int i = 0; i < sortedJSON.length(); i++) {
					sortedJSONArray.put(jsonValues.get(i));
				}
			}
		}
		return sortedJSONArray;

	}

	public JSONArray filterDate(JSONArray array, String startDate, String endDate) {
		JSONArray filteredArray = new JSONArray();
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < array.length(); ++i) {
			JSONObject obj = array.getJSONObject(i);
			try {
				Date sdt = sdformat.parse(startDate);
				Date edt = sdformat.parse(endDate);
				Date tdt = sdformat.parse(obj.getString(TemenosConstants.TRADEDATE));
				if (sdt.compareTo(tdt) * tdt.compareTo(edt) >= 0) {
					filteredArray.put(obj);
				}
			} catch (Exception e) {
				alert.prepareError("Error while invoking ORDERS - "
						+ PortfolioWealthAPIServices.WEALTH_GETORDERSDETAILS.getOperationName() + "  : " + e).log();
				return null;
			}
		}
		return filteredArray;

	}

	public JSONArray returnSearch(JSONArray array, String searchValue) {
		JSONArray filteredArray = new JSONArray();
		JSONArray sortedJSON = new JSONArray();
		for (int i = 0; i < array.length(); i++) {
			JSONObject obj = null;
			try {
				obj = array.getJSONObject(i);
				String search = searchValue.toLowerCase();

				String desc = obj.getString("description").toLowerCase();
				String isin = obj.getString("ISIN").toLowerCase();
				String orderType = obj.getString("orderType").toLowerCase();

				if (desc.contains(search)) {
					filteredArray.put(obj);
				}
				if (isin.contains(search)) {
					filteredArray.put(obj);
				}
				if (orderType.contains(search)) {
					filteredArray.put(obj);
				}

			} catch (JSONException e) {
				alert.prepareError("Error while invoking ORDERSDETAILS - "
						+ PortfolioWealthAPIServices.WEALTH_GETORDERSDETAILS.getOperationName() + "  : " + e).log();
				return null;
			}
		}
		try {
			Set<String> stationCodes = new HashSet<String>();
			for (int j = 0; j < filteredArray.length(); j++) {
				String stationCode = filteredArray.getJSONObject(j).getString(TemenosConstants.ORDER_REFERENCE);
				if (stationCodes.contains(stationCode)) {
					continue;
				} else {
					stationCodes.add(stationCode);
					sortedJSON.put(filteredArray.getJSONObject(j));
				}

			}
			filteredArray = sortedJSON;
		} catch (JSONException e) {
			alert.prepareError("Error while invoking ORDERS - "
					+ PortfolioWealthAPIServices.WEALTH_GETORDERSDETAILS.getOperationName() + "  : " + e).log();
			return null;
		}
		return filteredArray;
	}

	public String[] mockTradeDate(int openRecordCount, int arrayLength) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		String[] tradeDateArr = new String[arrayLength];
		for (int i = 0; i < arrayLength; i++) {
			if (i > 0) {
				cal.add(Calendar.MONTH, -1);
				cal.add(Calendar.DATE, 1);
			} else {
				cal.add(Calendar.MONTH, 0);
				cal.add(Calendar.DATE, 0);
			}
			tradeDateArr[i] = sdf.format(cal.getTime());
		}
		Calendar calToday = Calendar.getInstance();
		int k = 0;
		for (int j = openRecordCount; j < arrayLength; j++) {
			if (k > 0) {
				calToday.add(Calendar.MONTH, -1);
				calToday.add(Calendar.DATE, 1);
			} else {
				calToday.add(Calendar.MONTH, 0);
				calToday.add(Calendar.DATE, 0);
			}
			k++;
			tradeDateArr[j] = sdf.format(calToday.getTime());
		}
		return tradeDateArr;
	}

	public JSONArray pagination(JSONArray sortedJSON, int limit, int offset) {

		JSONArray paginationJSON = new JSONArray();

		int j = 0;
		for (int i = offset; i < sortedJSON.length(); i++) {
			if (j == limit) {
				break;
			} else {
				paginationJSON.put(sortedJSON.get(i));
			}
			j++;
		}

		return paginationJSON;

	}

}
