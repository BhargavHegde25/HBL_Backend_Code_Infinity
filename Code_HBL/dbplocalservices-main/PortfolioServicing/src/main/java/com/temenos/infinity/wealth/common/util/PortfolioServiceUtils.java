/**
 * 
 */
package com.temenos.infinity.wealth.common.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
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

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealth.config.PortfolioWealthAPIServices;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * @author himaja.sridhar
 *
 */
public class PortfolioServiceUtils {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	public static JSONArray returnHoldingsSearch(JSONArray array, String searchValue, String inp_instrumentId) {
		JSONArray filtedArray = new JSONArray();
		String search = searchValue.toLowerCase();
		for (int i = 0; i < array.length(); i++) {
			JSONObject obj = null;
			try {
				obj = array.getJSONObject(i);
				String desc = obj.getString("description").toLowerCase();
				String instrumentId = obj.getString("holdingsId");
				if (inp_instrumentId.length() > 0 && instrumentId != null && instrumentId.length() > 0) {
					if (desc.contains(search) && inp_instrumentId.equals(instrumentId)) {
						filtedArray.put(obj);
					}
				} else {
					if (desc.contains(search)) {
						filtedArray.put(obj);
					}
				}

			} catch (Exception e) {

				alert.prepareError("Error while invoking Sorting in holdings  : " + e).log();
				return null;
			}
		}
		return filtedArray;
	}

	public static JSONArray returnHoldingsInstrumentID(JSONArray array, String instrumentidInp) {
		JSONArray filtedArray = new JSONArray();
		for (int i = 0; i < array.length(); i++) {
			JSONObject obj = null;
			try {
				obj = array.getJSONObject(i);
				String instId = obj.getString(TemenosConstants.HOLDINGSID).toLowerCase();
				if (instId.contains(instrumentidInp) || instId.equalsIgnoreCase(instrumentidInp)) {
					// equalsIgnoreCase added for instrument IDs like EUR_GBP
					filtedArray.put(obj);
				}
			} catch (Exception e) {

				alert.prepareError("Error while invoking  returnHoldingsInstrumentID" + e.getMessage()).log();
				return null;
			}
		}
		return filtedArray;
	}

	public static JSONArray sortHoldingsArray(JSONArray holdingsArr, String sortBy, String sortType) {
		JSONArray sortedJSON = new JSONArray();
		if (sortBy.equals("")) {
			sortBy = TemenosConstants.DESCRIPTION;
		}
		final String finalSortBy = sortBy;
		if (finalSortBy.equalsIgnoreCase(TemenosConstants.DESCRIPTION)
				|| finalSortBy.equalsIgnoreCase(TemenosConstants.ASSETCLASS)
				|| finalSortBy.equalsIgnoreCase(TemenosConstants.REGION)
				|| finalSortBy.equalsIgnoreCase(TemenosConstants.SECTOR)
				|| finalSortBy.equalsIgnoreCase(TemenosConstants.SECCCY)
				|| finalSortBy.equalsIgnoreCase(TemenosConstants.SUBASSETCLASS)
				|| finalSortBy.equalsIgnoreCase(TemenosConstants.STATUS)) {
			List<JSONObject> jsonValues = new ArrayList<JSONObject>();

			for (int i = 0; i < holdingsArr.length(); i++) {
				jsonValues.add(holdingsArr.getJSONObject(i));
			}
			Collections.sort(jsonValues, new Comparator<JSONObject>() {

				private final String KEY_NAME = finalSortBy;

				@Override
				public int compare(JSONObject a, JSONObject b) {
					String str1 = new String();
					String str2 = new String();
					str1 = a.has(KEY_NAME) ? (String) a.get(KEY_NAME) : "";
					str2 = b.has(KEY_NAME) ? (String) b.get(KEY_NAME) : "";
					return str1.compareToIgnoreCase(str2);
				}

			});

			if (sortType != null && sortType.equalsIgnoreCase(TemenosConstants.DESCENDING)) {
				Collections.reverse(jsonValues);
			}
			sortedJSON = new JSONArray(jsonValues);
		} else if (finalSortBy.equalsIgnoreCase(TemenosConstants.MARKETVALPOS)
				|| finalSortBy.equalsIgnoreCase(TemenosConstants.UNREALIZEDPLPERCENTAGE)) {
			List<JSONObject> jsonValues = new ArrayList<JSONObject>();
			for (int i = 0; i < holdingsArr.length(); i++) {
				jsonValues.add(holdingsArr.getJSONObject(i));
			}
			Collections.sort(jsonValues, new Comparator<JSONObject>() {
				private final String KEY_NAME = finalSortBy;

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
					str1 = Double.parseDouble(a.getString(KEY_NAME).replace(",", ""));
					str2 = Double.parseDouble(b.getString(KEY_NAME).replace(",", ""));
					return str1.compareTo(str2);
				}
			});
			if (sortType != null && sortType.equalsIgnoreCase(TemenosConstants.DESCENDING)) {
				Collections.reverse(jsonValues);
			}
			sortedJSON = new JSONArray(jsonValues);

		} else {
			List<JSONObject> jsonValues = new ArrayList<JSONObject>();
			for (int i = 0; i < holdingsArr.length(); i++) {
				jsonValues.add(holdingsArr.getJSONObject(i));
			}
			Collections.sort(jsonValues, new Comparator<JSONObject>() {
				private final String KEY_NAME = finalSortBy;

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
			if (sortType != null && sortType.equalsIgnoreCase(TemenosConstants.DESCENDING)) {
				Collections.reverse(jsonValues);
			}
			sortedJSON = new JSONArray(jsonValues);
		}
		return sortedJSON;
	}

	public static JSONArray sortTransactionsArray(JSONArray array, String sortBy, String sortType) {
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

	public static JSONArray filterTransactionsDate(JSONArray array, String startDate, String endDate) {
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
				alert.prepareError("Error while invoking filterTransactionsDate - " + e.getMessage()).log();
				return null;
			}
		}
		return filteredArray;

	}

	public static JSONArray searchViewInstrumentTransactions(JSONArray array, String searchValue) {
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
				alert.prepareError("Error while invoking searchViewInstrumentTransactions - " + e.getMessage()).log();
				return null;
			}
		}

		return filteredArray;
	}

	public static JSONArray returnTransactionsSearch(JSONArray array, String searchValue) {
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
				alert.prepareError("Error while invoking returnTransactionsSearch - " + e.getMessage()).log();
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
			alert.prepareError("Error while invoking returnTransactionsSearch - " + e.getMessage()).log();
			return null;
		}
		return filteredArray;
	}

	public static String[] mockTransactionsTradeDate(int arrayLength) {
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

	public static String[] netAmountCalculation(String[] price, String[] quantity) throws ParseException {

		String[] netAmount = new String[price.length];
		double unitPrice = 0, amount = 0;
		int qty = 0;
		for (int i = 0; i < price.length; i++) {
			unitPrice = Double.parseDouble(price[i]);
			if (price[i] == "0") {
				unitPrice = 1;
			}
			qty = Integer.parseInt(quantity[i]);
			amount = unitPrice * qty;
			netAmount[i] = Double.toString(amount);
		}
		return netAmount;
	}

	public static String[] instrumentAmountCalculation(String[] netAmount, String[] exchangeRate)
			throws ParseException {

		String[] instrumentAmount = new String[netAmount.length];
		double instrAmount = 0, rate = 0, amount = 0;
		for (int i = 0; i < netAmount.length; i++) {
			amount = Double.parseDouble(netAmount[i]);
			rate = Double.parseDouble(exchangeRate[i]);
			instrAmount = amount * rate;
			instrumentAmount[i] = Double.toString(instrAmount);
		}
		return instrumentAmount;
	}

	public static String[] totalAmountCalculation(String[] instrumentAmount, String[] fees, String[] type)
			throws ParseException {

		String[] totalAmount = new String[instrumentAmount.length];
		double total = 0, fee = 0, amount = 0;
		for (int i = 0; i < instrumentAmount.length; i++) {
			amount = Double.parseDouble(instrumentAmount[i]);
			fee = Double.parseDouble(fees[i]);
			if (type[i].equalsIgnoreCase("Buy Limit")) {
				total = amount + fee;
			} else {
				total = amount - fee;
			}

			totalAmount[i] = Double.toString(total);
			;
		}
		return totalAmount;
	}

	public static String[] mockOrdersTradeDate(int openRecordCount, int arrayLength) {

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

	public static JSONArray filterOrdersDate(JSONArray array, String startDate, String endDate) {
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
				alert.prepareError("Error while invoking Order Details - " + e.getMessage()).log();
				return null;
			}
		}
		return filteredArray;

	}

	public static JSONArray sortOrdersArray(JSONArray array, String sortBy, String sortType) {
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

	public static JSONArray returnOrdersSearch(JSONArray array, String searchValue) {
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
				alert.prepareError("Error while invoking Order Details - " + e.getMessage()).log();
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
			alert.prepareError("Error while invoking Order Details - " + e.getMessage()).log();
			return null;
		}
		return filteredArray;
	}

	public static JSONArray sortdatetimeArrayOrders(JSONArray array, String sortBy, String sortType) {
		JSONArray sortedJSON = new JSONArray();
		JSONArray sortedJSONArray = new JSONArray();
		SimpleDateFormat sdformat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		SimpleDateFormat sdformaT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		List<JSONObject> jsonValues = new ArrayList<JSONObject>();
		for (int i = 0; i < array.length(); i++) {
			jsonValues.add(array.getJSONObject(i));
		}
		if (sortBy.equalsIgnoreCase(TemenosConstants.TRADEDATE)) {// DATE
			Collections.sort(jsonValues, new Comparator<JSONObject>() {
				private final String KEY_NAME = "orderedAt";

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
							if (str1.contains("-")) {
								d1 = sdformaT.parse(str1);
							} else {
								d1 = sdformat.parse(str1);
							}
							if (str2.contains("-")) {
								d2 = sdformaT.parse(str2);
							} else {
								d2 = sdformat.parse(str2);
							}
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
		}
		return sortedJSONArray;

	}

	public static JSONArray returnHistoryOrdersSearch(JSONArray array, String searchValue) {
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
				alert.prepareError("Error while invoking returnHistoryOrdersSearch -  " + e.getMessage()).log();
				return null;
			}
		}
		try {
			Set<Integer> stationCodes = new HashSet<Integer>();
			for (int j = 0; j < filteredArray.length(); j++) {
				int stationCode = filteredArray.getJSONObject(j).getInt("uniqueid");
				if (stationCodes.contains(stationCode)) {
					continue;
				} else {
					stationCodes.add(stationCode);
					sortedJSON.put(filteredArray.getJSONObject(j));
				}

			}
			filteredArray = sortedJSON;
		} catch (JSONException e) {
			alert.prepareError("Error while invoking returnHistoryOrdersSearch -  " + e.getMessage()).log();
			return null;
		}
		return filteredArray;
	}

	public static String[] nextMonths() {
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

	public static String[] getMonths(String[] arr) {
		String[] resultArray = new String[46];
		Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();
		startDate.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - 1);
		startDate.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH));
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		for (int i = 0; i < 3; i++) {
			startDate.add(Calendar.DATE, 4);
			resultArray[i] = df.format(startDate.getTime());
		}
		for (int i = 3; i < 45; i++) {
			startDate.add(Calendar.DATE, 8);
			resultArray[i] = df.format(startDate.getTime());
		}
		endDate.add(Calendar.DATE, -1);
		resultArray[45] = df.format(endDate.getTime());

		return resultArray;
	}

	public static String[] prevMonths() {
		int currentMonth = Calendar.getInstance().get(Calendar.MONTH);

		String[] finalArr = null;

		String a[] = new String[] { "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV",
				"DEC" };
		List<String> al = Arrays.asList(a);

		List<String> finalList = new ArrayList<String>();
		if (currentMonth == 0) {
			finalList.addAll(al.subList(0, 1));
		} else {
			finalList.addAll(al.subList(0, currentMonth));
		}

		finalArr = finalList.toArray(new String[0]);

		return finalArr;

	}

	public static String[] getPrevMonths(String[] arr) {
		String[] resultArray = new String[48];
		Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();

		if (arr[0].equalsIgnoreCase("Jan")) {
			startDate.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
			startDate.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH));
		} else {
			startDate.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
			startDate.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH));
		}
		startDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		for (int i = 47; i >= 0; i--) {
			startDate.add(Calendar.DATE, -8);
			resultArray[i] = df.format(startDate.getTime());
		}
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		endDate.add(Calendar.DATE, -1);
		resultArray[47] = df.format(endDate.getTime());
		resultArray[0] = df.format(cal.getTime());

		return resultArray;
	}

	public static Result getPreferences(String portfolioId, String userId, DataControllerRequest dcRequest)
			throws ApplicationException {
		Map<String, String> inputParams = new HashMap<>();
		String filter = "userId" + DBPUtilitiesConstants.EQUAL + userId + DBPUtilitiesConstants.AND + "portfolioId"
				+ DBPUtilitiesConstants.EQUAL + portfolioId;
		inputParams.put(DBPUtilitiesConstants.FILTER, filter);
		try {
			return HelperMethods.callGetApi(dcRequest, inputParams.get(DBPUtilitiesConstants.FILTER),
					HelperMethods.getHeaders(dcRequest), URLConstants.WEALTH_USER_PREFERENCES_GET);
		} catch (Exception e) {
			alert.prepareError("Exception occured while fetching the field order from Table :" + e.getMessage()).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_21129);
		}
	}
	
	public static String[] mockPerformanceDateTime(int arrayLength) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String[] tradeDateArr = new String[arrayLength];
		String[] tradeDateArrRev = new String[arrayLength];
		Calendar cal = Calendar.getInstance();
		Calendar calToday = Calendar.getInstance();
		for (int i = 1; i < arrayLength; i++) {
			cal.set(Calendar.DAY_OF_MONTH, 1);
			 cal.add(Calendar.DATE, -1);
			tradeDateArr[i] = sdf.format(cal.getTime());
		}
		tradeDateArr[0] = sdf.format(calToday.getTime());
		int k = 0;
		for (int j = arrayLength - 1; j >= 0; j--) {
			tradeDateArrRev[k] = tradeDateArr[j];
			k++;
		}
		return tradeDateArrRev;

	}
	public static JSONArray filterPerformanceDate(JSONArray array, String startDate, String endDate) {
		JSONArray filteredArray = new JSONArray();
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyyMMdd");
		for (int i = 0; i < array.length(); ++i) {
			JSONObject obj = array.getJSONObject(i);
			try {
				Date sdt = sdformat.parse(startDate);
				Date edt = sdformat.parse(endDate);
				Date tdt = sdformat.parse(obj.getString(TemenosConstants.DATE_TIME));
				if (sdt.compareTo(tdt) * tdt.compareTo(edt) >= 0) {
					filteredArray.put(obj);
				}
			} catch (Exception e) {
				alert.prepareError("Error while invoking Performance  : " + e.getMessage()).log();
				return null;
			}
		}
		return filteredArray;

	}
	public static JSONArray filterPerformanceCustomDate(JSONArray array, String startDate, String endDate) throws ParseException {
		JSONArray filteredArray = new JSONArray();
		JSONArray filteredNewArray = new JSONArray();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date date1=new SimpleDateFormat("yyyyMMdd").parse(startDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		cal.get(Calendar.DAY_OF_MONTH);
		String lastDateOfPreviousMonth="";
		
		
		if((cal.get(Calendar.DAY_OF_MONTH))>=15 && (cal.get(Calendar.DAY_OF_MONTH))<=20) {
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.add(Calendar.DATE, -1);
			lastDateOfPreviousMonth = sdf.format(cal.getTime());
		}
		filteredNewArray=filterPerformanceDate(array,lastDateOfPreviousMonth,lastDateOfPreviousMonth);
		//if(filteredNewArray != null) {
			//JSONObject object = filteredNewArray.getJSONObject(0);
			//filteredArray.put(object);
		//}
		filteredArray=filterPerformanceDate(array,startDate,endDate);
		if(lastDateOfPreviousMonth != "") {
			return concatPerformanceArray(filteredNewArray,filteredArray);
		}else {
			return filteredArray;
		}

	}
	@SuppressWarnings("unused")
	public static JSONArray sortPerformanceArray(JSONArray array,String sortBy, String sortType) {
		JSONArray sortedJSON = new JSONArray();
		JSONArray sortedJSONArray = new JSONArray();
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
		List<JSONObject> jsonValues = new ArrayList<JSONObject>();
		ArrayList<String> datesList = new ArrayList<>();
		for (int i = 0; i < array.length(); i++) {
			jsonValues.add(array.getJSONObject(i));
			datesList.add(array.getJSONObject(i).getString("dateTime"));
		}
		if (sortBy.equals("") || sortBy.equalsIgnoreCase(TemenosConstants.DATE_TIME)) {
			Collections.sort(datesList);
			for (String dates : datesList) {
				for (int i = 0; i < array.length(); i++) {
					if (jsonValues.get(i).get("dateTime").equals(dates)) {
						sortedJSON.put(jsonValues.get(i));
					}
				}
			}
			if (sortType != null && sortType.equalsIgnoreCase(TemenosConstants.DESCENDING)) {
				for (int i = sortedJSON.length()-1; i >= 0; i--) {
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
					str1 = (Double) a.getDouble(KEY_NAME);
					str2 = (Double) b.getDouble(KEY_NAME);
					return str1.compareTo(str2);
				}
			});

			for (int i = 0; i < array.length(); i++) {
				sortedJSON.put(jsonValues.get(i));
			}
			if (sortType != null && sortType.equalsIgnoreCase(TemenosConstants.DESCENDING)) {
				for (int i = sortedJSON.length()-1; i >= 0; i--) {
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
	public static String getPerformancePreviousMonth(String fromDate) throws ParseException{
		DateFormat  sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar cal = Calendar.getInstance();
		Date convertedDate = new Date();
		convertedDate=sdf.parse(fromDate);
		cal.setTime(convertedDate);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.DATE, -1);
	    String startDate = sdf.format(cal.getTime());
	    return startDate;
	}
	public static double calculatePerformanceCurrentValue(JSONObject performanceObj) {
		DecimalFormat df2 = new DecimalFormat("#.##");
		double initialValue = Double.parseDouble(performanceObj.getString("initialValue"));
		double netDeposit = Double.parseDouble(performanceObj.getString("netDeposit"));
		double pl =Double.parseDouble(performanceObj.getString("pl"));
		double feesAndTax = Double.parseDouble(performanceObj.getString("feesAndTax"));
		double currentValue =initialValue + netDeposit + pl + feesAndTax;
		return Double.parseDouble(df2.format(currentValue));
		}
	public static JSONArray concatPerformanceArray(JSONArray arr1, JSONArray arr2) {
	    JSONArray result = new JSONArray();
	    for (int i = 0; i < arr1.length(); i++) {
	        result.put(arr1.get(i));
	    }
	    for (int i = 0; i < arr2.length(); i++) {
	        result.put(arr2.get(i));
	    }
	    return result;
	}
	
	public static String[] mockAccountActivityBookingDate(int arrayLength) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
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

	public static JSONArray filterAccountActivityDate(JSONArray array, String startDate, String endDate) {
		JSONArray filteredArray = new JSONArray();
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyyMMdd");
		for (int i = 0; i < array.length(); ++i) {
			JSONObject obj = array.getJSONObject(i);
			try {
				Date sdt = sdformat.parse(startDate);
				Date edt = sdformat.parse(endDate);
				Date tdt;
				String dateHolder = obj.getString(TemenosConstants.BOOKINGDATE);
				if(dateHolder.contains("T")) {
					String[] arrOfDateStr = dateHolder.split("T",2);
					String a = arrOfDateStr[0];
					a = a.replace("-","");
					tdt = sdformat.parse(a);
				}
				else {
					tdt = sdformat.parse(dateHolder);
				}
				//if (tdt.after(sdt) && tdt.before(edt)) {
				if (sdt.compareTo(tdt) * tdt.compareTo(edt) >= 0) {
					filteredArray.put(obj);
				}
			} catch (Exception e) {
				alert.prepareError("Error while invoking Service - "
						+ PortfolioWealthAPIServices.WEALTH_GETACCOUNTACTIVITY.getOperationName() + "  : " + e).log();
				return null;
			}
		}
		return filteredArray;

	}
	public static JSONArray returnAccountsBasedOnId(JSONArray array, String accountId) {
		JSONArray filteredArray = new JSONArray();
		for (int i = 0; i < array.length(); i++) {
			JSONObject obj = null;
			try {
				obj = array.getJSONObject(i);
				String accId = obj.getString("accountId");
				if (accId.contentEquals(accountId)) {
					filteredArray.put(obj);
				}

			} catch (JSONException e) {
				alert.prepareError("Error while invoking Service - "
						+ PortfolioWealthAPIServices.WEALTH_GETACCOUNTACTIVITYOPERATIONS.getOperationName() + "  : " + e).log();
				return null;
			}
		}
		return filteredArray;
	}
	
	
	  public static JSONArray sortJSONArray(JSONArray jsonArray) {
	        // Create a list to hold JSONObjects
	        List<JSONObject> jsonObjectList = new ArrayList<>();

	        // Populate the list with JSONObjects from the JSONArray
	        for (int i = 0; i < jsonArray.length(); i++) {
	            jsonObjectList.add(jsonArray.getJSONObject(i));
	        }

	        // Sort the list of JSONObjects by customer names
	        Collections.sort(jsonObjectList, new Comparator<JSONObject>() {
	            
	        	@Override
	            public int compare(JSONObject o1, JSONObject o2) {
	        		String str1 = o1.optString("customerName");
	                String str2 = o2.optString("customerName");
	                
	                if (str1 == null || str2 == null) {
	                    return 0; 
	                }
	                String str1LastName = str1.substring(str1.indexOf(" ") +1);
	                String str2LastName = str2.substring(str2.indexOf(" ") +1);
	                int lastNameComparison = str1LastName.compareTo(str2LastName);
                    if (lastNameComparison == 0) {
                      String str1FirstName = str1.substring(0, str1.indexOf(" "));
                      String str2FirstName = str2.substring(0, str2.indexOf(" "));
                      return str1FirstName.compareTo(str2FirstName);
                 }
                return lastNameComparison;
	            }
	        });

	        // Clear the original JSONArray
	        jsonArray = new JSONArray();

	        // Add sorted JSONObjects back to the JSONArray
	        for (JSONObject jsonObject : jsonObjectList) {
	            jsonArray.put(jsonObject);
	        }
	        return jsonArray;
	        
	    }

	
	public static JSONArray sortAccountActivityArray(JSONArray array, String sortBy, String sortType) {
		JSONArray sortedJSON = new JSONArray();
		JSONArray sortedJSONArray = new JSONArray();
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyyMMdd");
		List<JSONObject> jsonValues = new ArrayList<JSONObject>();
		for (int i = 0; i < array.length(); i++) {
			jsonValues.add(array.getJSONObject(i));
		}
		if (sortBy.equals("") || sortBy.equalsIgnoreCase(TemenosConstants.BOOKINGDATE)
				|| sortBy.equalsIgnoreCase(TemenosConstants.VALUEDATE)) {
			Collections.sort(jsonValues, new Comparator<JSONObject>() {
				private final String KEY_NAME = (sortBy.equalsIgnoreCase(TemenosConstants.VALUEDATE)) ? "valueDate"
						: sortBy;

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
				for (int i = sortedJSON.length()-1; i >= 0; i--) {
					sortedJSONArray.put(sortedJSON.get(i));
				}

			} else {
				for (int i = 0; i < sortedJSON.length(); i++) {
					sortedJSONArray.put(sortedJSON.get(i));
				}
			}

		} else if (sortBy.equalsIgnoreCase(TemenosConstants.QUANTITY)) {
			Collections.sort(jsonValues, new Comparator<JSONObject>() {
				private final String KEY_NAME = sortBy;

				@Override
				public int compare(JSONObject a, JSONObject b) {
					String str1 = null;
					String str2 = null;
					Integer val1 = null, val2;
					if (a.has(sortBy)) {
						str1 = (String) a.get(KEY_NAME);
						if (str1 != null && !str1.trim().isEmpty()) {
							val1 = Integer.parseInt(str1);
						} else {
							val1 = 0;
						}
					} else {
						val1 = 0;
					}
					if (b.has(sortBy)) {
						str2 = (String) b.get(KEY_NAME);
						if (str2 != null && !str2.trim().isEmpty()) {
							val2 = Integer.parseInt(str2);
						} else {
							val2 = 0;
						}
					} else {
						val2 = 0;
					}
					return val1.compareTo(val2);
				}

			});
			for (int i = 0; i < array.length(); i++) {
				sortedJSON.put(jsonValues.get(i));
			}
			
			if (sortType != null && sortType.equalsIgnoreCase(TemenosConstants.DESCENDING)) {
				for (int i = sortedJSON.length()-1; i >= 0; i--) {
					sortedJSONArray.put(jsonValues.get(i));
				}

			} else {
				for (int i = 0; i < sortedJSON.length(); i++) {
					sortedJSONArray.put(jsonValues.get(i));
				}
			}

		} else if (sortBy.equalsIgnoreCase(TemenosConstants.DISPLAYNAME)
				|| sortBy.equalsIgnoreCase(TemenosConstants.SHORTNAME)) {
			Collections.sort(jsonValues, new Comparator<JSONObject>() {
				private final String KEY_NAME = sortBy;

				@Override
				public int compare(JSONObject a, JSONObject b) {
					String str1 = new String();
					String str2 = new String();
					if (a.has(sortBy)) {
						str1 = (String) a.get(KEY_NAME);
						if (str1 != null && !str1.trim().isEmpty()) {
						} else {
							str1 = "";
						}
					} else {
						str1 = "";
					}
					if (b.has(sortBy)) {
						str2 = (String) b.get(KEY_NAME);
						if (str2 != null && !str2.trim().isEmpty()) {
						} else {
							str2 = "";
						}
					} else {
						str2 = "";
					}
					return str1.compareToIgnoreCase(str2);
				}

			});
			for (int i = 0; i < array.length(); i++) {
				sortedJSON.put(jsonValues.get(i));
			}
			
			if (sortType != null && sortType.equalsIgnoreCase(TemenosConstants.DESCENDING)) {
				for (int i = sortedJSON.length()-1; i >= 0; i--) {
					sortedJSONArray.put(jsonValues.get(i));
				}

			} else {
				for (int i = 0; i < sortedJSON.length(); i++) {
					sortedJSONArray.put(jsonValues.get(i));
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
					str1 = (Double) a.getDouble(KEY_NAME);
					str2 = (Double) b.getDouble(KEY_NAME);
					return str1.compareTo(str2);
				}
			});

			for (int i = 0; i < array.length(); i++) {
				sortedJSON.put(jsonValues.get(i));
			}
			
			if (sortType != null && sortType.equalsIgnoreCase(TemenosConstants.DESCENDING)) {
				for (int i = sortedJSON.length()-1; i >= 0; i--) {
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
	public static JSONArray returnAccountActivitySearch(JSONArray array, String searchValue) {
		JSONArray filteredArray = new JSONArray();
		for (int i = 0; i < array.length(); i++) {
			JSONObject obj = null;
			try {
				obj = array.getJSONObject(i);
				String search = searchValue.toLowerCase();

				String desc = obj.getString("displayName").toLowerCase();

				if (desc.contains(search)) {
					filteredArray.put(obj);
				}

			} catch (JSONException e) {
				alert.prepareError("Error while invoking Service - "
						+ PortfolioWealthAPIServices.WEALTH_GETACCOUNTACTIVITYOPERATIONS.getOperationName() + "  : " + e).log();
				return null;
			}
		}
		return filteredArray;
	}
	
	public static String[] reverseArray(String[] arrayValue) {
		List<String> list = new ArrayList<>();
		if (arrayValue != null && arrayValue.length > 0) {
			list = Arrays.asList(arrayValue);
			Collections.reverse(list);
		}
		return list.toArray(arrayValue);
	}

	public static boolean validateData(Result result, String inputParam) {
		alert.prepareError("Error:Invalid input. Format is not valid").log();
		result.addParam("status", "Failure");
		result.addParam("error", TemenosConstants.SORTBY + " value is not valid");
		return false;
	}

	public static boolean unauthAccess(Result result, String param) {
		alert.prepareError("Error:Invalid input. Mandatory fields not given").log();
		result.addParam("status", "Failure");
		result.addParam("error", "Invalid Input! " + param + " is mandatory.");
		return false;
	}

	public static boolean validateFormat(Result result, String inputParam) {
		alert.prepareError("Error:Invalid input. Format inValid").log();
		result.addParam("status", "Failure");
		result.addParam("error", inputParam + " is not in a valid format.");
		return false;

	}
}
