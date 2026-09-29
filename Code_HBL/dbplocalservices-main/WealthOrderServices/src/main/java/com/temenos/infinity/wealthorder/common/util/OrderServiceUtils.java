/**
 * 
 */
package com.temenos.infinity.wealthorder.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthOrder.config.WealthAPIServices;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;

/**
 * @author himaja.sridhar
 *
 */
public class OrderServiceUtils {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	
	public static String[] netAmountCalculation(String[] price, String[] quantity) throws ParseException {
		/** netAmountCalculation  for Instrument Transactions **/
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

	public static String[] instrumentAmountCalculation(String[] netAmount, String[] exchangeRate) throws ParseException {
		/** instrumentAmountCalculation for Instrument Transactions **/
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
		/** totalAmountCalculation  for Instrument Transactions **/
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
	
	public static JSONArray sortInstrumentTransactionsArray(JSONArray array, String sortBy, String sortType) {
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

	public static JSONArray filterInstrumentTranscationsDate(JSONArray array, String startDate, String endDate) {
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
				alert.prepareError("Error while invoking filterInstrumentTranscationsDate - " + e.getMessage()).log();
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
				alert.prepareError("Error while invoking  searchViewInstrumentTransactions - " + e.getMessage()).log();
				return null;
			}
		}

		return filteredArray;
	}

	public static String[] mockTradeDateViewInstrumentTransaction(int arrayLength) {
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
	
	public static JSONArray returnInstrumentTransactionsSearch(JSONArray array, String searchValue) {
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
				alert.prepareError("Error while invoking returnInstrumentTransactionsSearch - " + e.getMessage()).log();
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
			alert.prepareError("Error while invoking returnInstrumentTransactionsSearch - "+ e.getMessage()).log();
			return null;
		}
		return filteredArray;
	}

	public static JSONObject searchManipulation(JSONArray assettypeArray, List<String> colArray, Result result, Dataset bodyDataset,
			String type) {
		JSONObject assetTypeObj = new JSONObject();
		try {

			JSONObject fabricresponseObj = assettypeArray.getJSONObject(0);
			for (String key : colArray) {
				if (!fabricresponseObj.has(key)) {
					fabricresponseObj.put(key, "");
				}
				assetTypeObj.put(key, fabricresponseObj.get(key));
			}
			assetTypeObj.put("assetType", type);

		} catch (Exception e) {
			e.getMessage();
			alert.prepareError("Exception Occured at dx assettype manipulation", e).log();
		}
		return assetTypeObj;
	}

	public static JSONArray favouritemanipulation(JSONArray assettypeArray, List<String> colArray, Result result,
			Dataset bodyDataset, String type) {
		JSONArray instrumentArray = new JSONArray();
		String[] favcolArray = new String[] { TemenosConstants.INSTRUMENTID, TemenosConstants.INSTRUMENTNAME,
				TemenosConstants.STOCKEXCHANGE, TemenosConstants.ISIN, TemenosConstants.RICCODE, "marketPrice",
				"dateReceived", "referenceCurrency", "instrumentCurrencyId", TemenosConstants.QUANTITY };

		for (int i = 0; i < assettypeArray.length(); i++) {
			JSONObject instrObj = assettypeArray.getJSONObject(i);
			for (String key : favcolArray) {
				if (instrObj.has(key)) {
					if (key.equalsIgnoreCase("dateReceived")) {
						try {
							SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
							Date date = formatter.parse(instrObj.get(key).toString());
							formatter = new SimpleDateFormat("dd MMM yyyy");
							String formattedDate = formatter.format(date);
							instrObj.put(key, formattedDate);
						} catch (Exception e) {
							instrObj.put(key, instrObj.get(key));
						}
					} else {
						instrObj.put(key, instrObj.get(key));
					}
				} else if (key.equalsIgnoreCase(TemenosConstants.INSTRUMENTNAME)) {
					instrObj.put(key, instrObj.get("underlying"));
				} else {
					instrObj.put(key, "");
				}
			}
			instrumentArray.put(instrObj);
		}
		return instrumentArray;
	}
	public static JSONArray returnSearch(JSONArray array, String searchValue, String inp_instrumentId) {
		JSONArray filtedArray = new JSONArray();
		String search = searchValue.toLowerCase();
		for (int i = 0; i < array.length(); i++) {
			JSONObject obj = null;
			try {
				obj = array.getJSONObject(i);
				String desc = obj.getString("description").toLowerCase();
				String instrumentId = (obj.has("holdingsId") ? obj.getString("holdingsId"):
						(obj.has("instrumentId") ? obj.getString("instrumentId") : ""));
				if (inp_instrumentId.length() > 0 && instrumentId != null && instrumentId.length() > 0) {
					if (desc.contains(search) && inp_instrumentId.equals((instrumentId).toString())) {
						filtedArray.put(obj);
					}
				} else {
					if (desc.contains(search)) {
						filtedArray.put(obj);
					}
				}
			} catch (Exception e) {

				alert.prepareError("Error while returnSearch - " + e.getMessage()).log();
				return null;
			}
		}
		return filtedArray;
	}
	
	public static JSONArray returnSearchInstrumentID(JSONArray array, String instrumentidInp) {
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

				alert.prepareError("Error while invoking returnInstrumentID - " + e.getMessage()).log();
				return null;
			}
		}
		return filtedArray;
	}
	public static JSONObject newsPagination(JSONObject jsonResult, int limit, int offset) {
		JSONArray jsonArray = jsonResult.getJSONArray("stockNews");
		JSONObject response = new JSONObject();
		JSONArray paginationJSON = new JSONArray();

		int j = 0;
		for (int i = offset; i < jsonArray.length(); i++) {
			if (j == limit) {
				break;
			} else {
				paginationJSON.put(jsonArray.get(i));
			}
			j++;
		}
		response.put("stockNews", paginationJSON);
		int totalCount = jsonArray.length();
		response.put("totalCount", totalCount);
		response.put("opstatus", "0");
		response.put("httpStatusCode", "200");
		return response;
	}
	
	public static boolean validateData(Result result, String inputParam) {
		alert.prepareError("Error:Invalid input. Format is not valid").log();
		result.addParam("status", "Failure");
		result.addParam("error", TemenosConstants.SORTBY + " value is not valid");
		return false;
	}

	/** Mandatory Check **/
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
	
	public static boolean validateMandatoryFields(Result result, String inputParam) {
		alert.prepareError("Error:Invalid input. Mandatory fields not given").log();
		result.addParam("status", "Failure");
		result.addParam("error", "Invalid Input! " + inputParam + " is mandatory.");
		return false;
	}
		public static Result getFavouriteInstruments(String userId, DataControllerRequest dcRequest) throws ApplicationException {
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

	public static Result splitfavInstrumentIds(Result result) {

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
	
	
	public static Result createFavouriteInstruments(DataControllerRequest request,
			DataControllerResponse response) throws ApplicationException {
		Result result = new Result();
		Result result1 = new Result();
		try {
			Map<String, Object> inputMap = new HashMap<>();
			//Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
			Object ricObj = request.getParameter(TemenosConstants.RICCODE);
			Object instrumentObj = request.getParameter(TemenosConstants.INSTRUMENTID);
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
					ricCode = request.getParameter(TemenosConstants.RICCODE).toString();
				} else {
					ricCode = "";
				}
				if (instrumentObj != null) {
					instrumentId = request.getParameter(TemenosConstants.INSTRUMENTID).toString();
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
}
