/**
 * 
 */
package com.temenos.infinity.api.wealthOrder.backenddelegate.impl;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.TransactionsListBackendDelegate;
import com.temenos.infinity.api.wealthOrder.config.WealthAPIServices;
import com.temenos.infinity.api.wealthOrder.constants.OperationName;
import com.temenos.infinity.api.wealthOrder.constants.ServiceId;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;
import com.temenos.infinity.api.wealthOrder.util.WealthMockUtil;


/**
 * @author himaja.sridhar
 *
 */
public class TransactionsListBackendDelegateImpl implements TransactionsListBackendDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	WealthMockUtil wealthMockUtil = new WealthMockUtil();

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Result getTransactionDetails(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		Map<String, Object> inputMap = new HashMap<>();
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		Object startDateObj = inputParams.get(TemenosConstants.STARTDATE);
		Object endDateObj = inputParams.get(TemenosConstants.ENDDATE);
		Object sortByObj = inputParams.get(TemenosConstants.SORTBY);
		Object searchObj = inputParams.get(TemenosConstants.SEARCHBYINSTRUMENTNAME);
		Object sortTypeObj = inputParams.get(TemenosConstants.SORTORDER);
		Object limitObj = inputParams.get(TemenosConstants.PAGESIZE);
		Object offsetObj = inputParams.get(TemenosConstants.PAGEOFFSET);
		String portfolioId = null;
		String startDate = null;
		String endDate = null;
		String sortBy = null;
		String search = null;
		int indexOfSort = 0;
		String dateformat = "YYYY-MM-dd";
		String sortType = null, limit = null, offset = null;
		List sortValues = Arrays.asList("description", "orderType", "quantity", "limitPrice", "netAmount", "tradeDate",
				"exchangeRate", "instrumentAmount", "valueDate", "fees", "total");

		String[] fieldName = new String[] { "tradeDate", "description", "orderType", "quantity", "limitPrice",
				"netAmount", "exchangeRate", "instrumentAmount", "valueDate", "total" };
		String[] columnName = new String[] { "OPERATION_DATE", "INSTR_NAME", "OPERATION_NATURE", "QUANTITY", "PRICE",
				"Gross_amount_instr_ccy", "EXCH_RATE", "GROSS_AMOUNT_OPER_CURR", "VALUE_DATE", "NET_AMOUNT_OPER_CURR" };

		if (!sortValues.contains(sortByObj)) {
			alert.prepareError("Invalid request").log();
			JSONObject result = new JSONObject();
			result.put("status", "Failure");
			result.put("error", TemenosConstants.SORTBY + " value is not valid");
			return Utilities.constructResultFromJSONObject(result);
		} else {
			if (inputParams.get(TemenosConstants.PORTFOLIOID) != null
					&& inputParams.get(TemenosConstants.PORTFOLIOID).toString().trim().length() > 0) {
				portfolioId = inputParams.get(TemenosConstants.PORTFOLIOID).toString();
				inputMap.put(TemenosConstants.PORTFOLIOID, portfolioId);
			} else {
				return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.PORTFOLIOID);
			}
			if (searchObj != null) {
				search = inputParams.get(TemenosConstants.SEARCHBYINSTRUMENTNAME).toString();
				inputMap.put(TemenosConstants.SEARCHBYINSTRUMENTNAME, search);
			}
			if (startDateObj != null) {
				startDate = inputParams.get(TemenosConstants.STARTDATE).toString();
				inputMap.put(TemenosConstants.STARTDATE, startDate);
				boolean isTrue = PortfolioWealthUtils.validateDateFormat(dateformat, startDate);
				if (!isTrue) {
					return PortfolioWealthUtils.validateFormat(TemenosConstants.STARTDATE);
				}
			} else {
				return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.STARTDATE);
			}
			if (endDateObj != null) {
				endDate = inputParams.get(TemenosConstants.ENDDATE).toString();
				inputMap.put(TemenosConstants.ENDDATE, endDate);
				boolean isTrue = PortfolioWealthUtils.validateDateFormat(dateformat, endDate);
				if (!isTrue) {
					return PortfolioWealthUtils.validateFormat(TemenosConstants.ENDDATE);
				}
			} else {
				return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.ENDDATE);
			}
			if (sortByObj != null) {
				sortBy = inputParams.get(TemenosConstants.SORTBY).toString();
				inputMap.put(TemenosConstants.SORTBY, sortBy);
				indexOfSort = ArrayUtils.indexOf(fieldName, sortBy);
				if (indexOfSort == -1) {
					indexOfSort = 0;
				}
			} else {
				return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.SORTBY);
			}
			if (sortTypeObj != null) {
				sortType = inputParams.get(TemenosConstants.SORTORDER).toString();
				inputMap.put(TemenosConstants.SORTORDER, sortType);
				inputMap.put(TemenosConstants.ORDERBY, columnName[indexOfSort]);
				inputMap.put(TemenosConstants.SORTTYPE, sortType);
			} else {
				if (sortBy.equalsIgnoreCase("tradeDate") || sortBy.equalsIgnoreCase("valueDate")) {
					inputMap.put(TemenosConstants.ORDERBY, columnName[indexOfSort]);
					inputMap.put(TemenosConstants.SORTTYPE, "desc");
				} else {
					inputMap.put(TemenosConstants.ORDERBY, columnName[indexOfSort]);
					inputMap.put(TemenosConstants.SORTTYPE, "asc");
				}

			}
			if (limitObj != null) {
				limit = inputParams.get(TemenosConstants.PAGESIZE).toString();
				inputMap.put(TemenosConstants.PAGESIZE, limit);
			}
			if (offsetObj != null) {
				offset = inputParams.get(TemenosConstants.PAGEOFFSET).toString();
				inputMap.put(TemenosConstants.PAGEOFFSET, offset);
			}
		}
		List<String> allportfoliosList = PortfolioWealthUtils.getAllPortfoliosFromCache(request);

		if (allportfoliosList.contains(portfolioId)) {
			try {
				String serviceName = ServiceId.WEALTHORCHESTRATION;
				String operationName = OperationName.GET_TRANSACTION_DETAILS;

				return PortfolioWealthUtils.backendResponse(serviceName, operationName, inputMap, headersMap, request);

			} catch (Exception e) {
				alert.prepareError("Error while invoking Transact - "
						+ WealthAPIServices.WEALTH_GETTRANSACTIONDETAILS.getOperationName() + "  : " + e).log();
				return null;

			}
		} else {

			return PortfolioWealthUtils.UnauthorizedAccess();

		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Result viewInstrumentTransactions(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		Map<String, Object> inputMap = new HashMap<>();
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		Object startDateObj = inputParams.get(TemenosConstants.STARTDATE);
		Object endDateObj = inputParams.get(TemenosConstants.ENDDATE);
		Object sortByObj = inputParams.get(TemenosConstants.SORTBY);
		Object instrumentidObj = inputParams.get(TemenosConstants.INSTRUMENTID);
		Object sortTypeObj = inputParams.get(TemenosConstants.SORTORDER);
		Object limitObj = inputParams.get(TemenosConstants.PAGESIZE);
		Object offsetObj = inputParams.get(TemenosConstants.PAGEOFFSET);
		String portfolioId = null;
		String startDate = null;
		String endDate = null;
		String sortBy = null;
		String instrumentid = null;
		String dateformat = "YYYY-MM-dd";
		int indexOfSort = 0;
		String sortType = null, limit = null, offset = null;
		List sortValues = Arrays.asList("description", "orderType", "quantity", "limitPrice", "netAmount", "tradeDate",
				"exchangeRate", "instrumentAmount", "valueDate", "fees", "total");

		String[] fieldName = new String[] { "tradeDate", "description", "orderType", "quantity", "limitPrice",
				"netAmount", "exchangeRate", "instrumentAmount", "valueDate", "total" };
		String[] columnName = new String[] { "OPERATION_DATE", "INSTR_NAME", "OPERATION_NATURE", "QUANTITY", "PRICE",
				"Gross_amount_instr_ccy", "EXCH_RATE", "GROSS_AMOUNT_OPER_CURR", "VALUE_DATE", "NET_AMOUNT_OPER_CURR" };
		if (!sortValues.contains(sortByObj)) {
			alert.prepareError("Invalid request").log();
			JSONObject result = new JSONObject();
			result.put("status", "Failure");
			result.put("error", TemenosConstants.SORTBY + " value is not valid");
			return Utilities.constructResultFromJSONObject(result);
		} else {
			if (inputParams.get(TemenosConstants.PORTFOLIOID) != null
					&& inputParams.get(TemenosConstants.PORTFOLIOID).toString().trim().length() > 0) {
				portfolioId = inputParams.get(TemenosConstants.PORTFOLIOID).toString();
				inputMap.put(TemenosConstants.PORTFOLIOID, portfolioId);
			} else {
				return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.PORTFOLIOID);
			}
			if (instrumentidObj != null) {
				instrumentid = inputParams.get(TemenosConstants.INSTRUMENTID).toString();
				inputMap.put(TemenosConstants.INSTRUMENTID, instrumentid);
			}
			if (startDateObj != null) {
				startDate = inputParams.get(TemenosConstants.STARTDATE).toString();
				inputMap.put(TemenosConstants.STARTDATE, startDate);
				boolean isTrue = PortfolioWealthUtils.validateDateFormat(dateformat, startDate);
				if (!isTrue) {
					return PortfolioWealthUtils.validateFormat(TemenosConstants.STARTDATE);
				}
			} else {
				return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.STARTDATE);
			}
			if (endDateObj != null) {
				endDate = inputParams.get(TemenosConstants.ENDDATE).toString();
				inputMap.put(TemenosConstants.ENDDATE, endDate);
				boolean isTrue = PortfolioWealthUtils.validateDateFormat(dateformat, endDate);
				if (!isTrue) {
					return PortfolioWealthUtils.validateFormat(TemenosConstants.ENDDATE);
				}
			} else {
				return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.ENDDATE);
			}
			if (sortByObj != null) {
				sortBy = inputParams.get(TemenosConstants.SORTBY).toString();
				inputMap.put(TemenosConstants.SORTBY, sortBy);
				indexOfSort = ArrayUtils.indexOf(fieldName, sortBy);
				if (indexOfSort == -1) {
					indexOfSort = 0;
				}
			} else {
				return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.SORTBY);
			}
			if (sortTypeObj != null) {
				sortType = inputParams.get(TemenosConstants.SORTORDER).toString();
				inputMap.put(TemenosConstants.SORTORDER, sortType);
				inputMap.put(TemenosConstants.ORDERBY, columnName[indexOfSort]);
				inputMap.put(TemenosConstants.SORTTYPE, sortType);
			} else {
				if (sortBy.equalsIgnoreCase("tradeDate") || sortBy.equalsIgnoreCase("valueDate")) {
					inputMap.put(TemenosConstants.ORDERBY, columnName[indexOfSort]);
					inputMap.put(TemenosConstants.SORTTYPE, "desc");
				} else {
					inputMap.put(TemenosConstants.ORDERBY, columnName[indexOfSort]);
					inputMap.put(TemenosConstants.SORTTYPE, "asc");
				}

			}
			if (limitObj != null) {
				limit = inputParams.get(TemenosConstants.PAGESIZE).toString();
				inputMap.put(TemenosConstants.PAGESIZE, limit);
			}
			if (offsetObj != null) {
				offset = inputParams.get(TemenosConstants.PAGEOFFSET).toString();
				inputMap.put(TemenosConstants.PAGEOFFSET, offset);
			}
		}

		List<String> allportfoliosList = PortfolioWealthUtils.getAllPortfoliosFromCache(request);

		if (allportfoliosList.contains(portfolioId)) {
			try {
				String serviceName = ServiceId.WEALTHORCHESTRATION;
				String operationName = OperationName.VIEWINSTRUMENTTRANSACTIONS;
				return PortfolioWealthUtils.backendResponse(serviceName, operationName, inputMap, headersMap, request);

			} catch (Exception e) {
				alert.prepareError("Error while invoking Transact - "
						+ WealthAPIServices.WEALTH_VIEWINSTRUMENTTRANSACTIONS.getOperationName() + "  : " + e).log();
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
		if (sortBy.equals("") || sortBy.equalsIgnoreCase(TemenosConstants.DESCRIPTION)) {
			Collections.sort(jsonValues, new Comparator<JSONObject>() {
				private final String KEY_NAME = TemenosConstants.DESCRIPTION;

				@Override
				public int compare(JSONObject a, JSONObject b) {
					String str1 = new String();
					String str2 = new String();
					str1 = (String) a.get(KEY_NAME);
					str2 = (String) b.get(KEY_NAME);
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
		} else if (sortBy.equalsIgnoreCase(TemenosConstants.ORDERTYPE)) {
			Collections.sort(jsonValues, new Comparator<JSONObject>() {
				private final String KEY_NAME = TemenosConstants.ORDERTYPE;

				@Override
				public int compare(JSONObject a, JSONObject b) {
					String str1 = new String();
					String str2 = new String();
					str1 = (String) a.get(KEY_NAME);
					str2 = (String) b.get(KEY_NAME);
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

		} else if (sortBy.equalsIgnoreCase(TemenosConstants.TRADEDATE)
				|| sortBy.equalsIgnoreCase(TemenosConstants.VALUEDATE)) {
			Collections.sort(jsonValues, new Comparator<JSONObject>() {
				private final String KEY_NAME = sortBy;

				@Override
				public int compare(JSONObject a, JSONObject b) {
					String str1 = new String();
					String str2 = new String();
					str1 = (String) a.get(KEY_NAME);
					str2 = (String) b.get(KEY_NAME);
					Date d1 = new Date();
					Date d2 = new Date();
					try {
						d1 = sdformat.parse(str1);
						d2 = sdformat.parse(str2);
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
		} else {
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
					Double str1 = null;
					Double str2 = null;
					Object object = a.get(KEY_NAME);
					str1 = Double.parseDouble(object.toString().replaceAll(",", ""));
					object = b.get(KEY_NAME);
					str2 = Double.parseDouble(object.toString().replaceAll(",", ""));

					/*
					 * str1 = (Double) a.getDouble(KEY_NAME); str2 = (Double) b.getDouble(KEY_NAME);
					 */
					return str1.compareTo(str2);
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
				// if (tdt.after(sdt) && tdt.before(edt)) {
				if (sdt.compareTo(tdt) * tdt.compareTo(edt) >= 0) {
					filteredArray.put(obj);
				}
			} catch (Exception e) {
				alert.prepareError("Error while invoking Transact - "
						+ WealthAPIServices.WEALTH_GETTRANSACTIONDETAILS.getOperationName() + "  : " + e).log();
				return null;
			}
		}
		return filteredArray;

	}

	public JSONArray searchViewInstrumentTransactions(JSONArray array, String searchValue) {
		JSONArray filteredArray = new JSONArray();

		for (int i = 0; i < array.length(); i++) {
			JSONObject obj = null;
			try {
				obj = array.getJSONObject(i);
				String search = searchValue.toLowerCase();

				String instrumentId = obj.getString("instrumentId").toLowerCase();

				if (instrumentId.contains(search)) {
					filteredArray.put(obj);
				}

			} catch (JSONException e) {
				alert.prepareError("Error while invoking Transact - "
						+ WealthAPIServices.WEALTH_VIEWINSTRUMENTTRANSACTIONS.getOperationName() + "  : " + e).log();
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
				alert.prepareError("Error while invoking Transact - "
						+ WealthAPIServices.WEALTH_GETTRANSACTIONDETAILS.getOperationName() + "  : " + e).log();
				return null;
			}
		}
		try {
			Set<String> stationCodes = new HashSet<String>();
			for (int j = 0; j < filteredArray.length(); j++) {
				String stationCode = filteredArray.getJSONObject(j).getString("transactionId");
				if (stationCodes.contains(stationCode)) {
					continue;
				} else {
					stationCodes.add(stationCode);
					sortedJSON.put(filteredArray.getJSONObject(j));
				}

			}
			filteredArray = sortedJSON;
		} catch (JSONException e) {
			alert.prepareError("Error while invoking Transact - "
					+ WealthAPIServices.WEALTH_GETTRANSACTIONDETAILS.getOperationName() + "  : " + e).log();
			return null;
		}
		return filteredArray;
	}

	public String[] mockTradeDate(int arrayLength) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String[] tradeDateArr = new String[arrayLength];
		Calendar cal = Calendar.getInstance();
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
		return tradeDateArr;
	}

	public String[] mockTradeDateViewInstrumentTransaction(int arrayLength) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String[] tradeDateArr = new String[arrayLength];
		Calendar cal = Calendar.getInstance();
		for (int i = 0; i < arrayLength; i++) {
			if (i > 0) {
				cal.add(Calendar.DATE, -2);
			} else {
				cal.add(Calendar.MONTH, 0);
				cal.add(Calendar.DATE, 0);
			}
			tradeDateArr[i] = sdf.format(cal.getTime());
		}
		return tradeDateArr;
	}
}
