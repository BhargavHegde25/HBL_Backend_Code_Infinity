package com.temenos.infinity.api.wealthOrder.tap.preandpostprocessors;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthOrder.config.WealthAPIServices;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthOrder.tap.preandpostprocessors.GetHistoryOrderDetailsPostProcessor;

/**
 * (INFO) If status is set as a part of the request , the operation is exited
 * else operation is executed.
 * 
 * @author balaji.krishnan
 *
 */

public class GetHistoryOrderDetailsPostProcessor implements DataPostProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private HashSet<String> historyorderIds = new HashSet<String>();

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

		try {
			historyorderIds = new HashSet<String>();
			boolean cancel_OR_modify_order = false;
			String[] typeArr = null, historyStatusArr = null;
			String type = "", responseStatus = "";

			typeArr = request.getParameterValues(TemenosConstants.ORDERSVIEW_TYPE);

			if (request.getParameterValues("cancelormodify_order") != null
					&& request.getParameterValues("cancelormodify_order")[0].toString().equalsIgnoreCase("true")) {
				cancel_OR_modify_order = true;
			}

			JSONArray finalArr = new JSONArray();

			if (typeArr != null) {
				type = typeArr[0].toString().trim();
			}

			historyStatusArr = new String[] { "Executed" };
			List<String> historyStatusList = Arrays.asList(historyStatusArr);

			JSONObject responseJSON = new JSONObject();

			Dataset ds = result.getDatasetById("body");
			if (ds != null) {

				JSONObject resultObj = new JSONObject();
				resultObj.put("Field", Utilities.convertStringToJSONArray(ResultToJSON.convertDataset(ds).toString()));
				JSONArray refnitivArray = resultObj.getJSONArray("Field");

				if (refnitivArray != null && refnitivArray.length() > 0) {

					String[] colArray = new String[] { "isCancelEnable", "isModifyEnable", TemenosConstants.DESCRIPTION,
							"ISIN", "orderedAt", "instrumentId", "holdingsType", TemenosConstants.LIMITPRICE,
							TemenosConstants.STOPPRICE, TemenosConstants.TRADEDATE, "orderMode", "orderModeType",
							TemenosConstants.ORDERTYPE, TemenosConstants.ORDER_REFERENCE, "orderStatus", "validity",
							"instrumentCurrency", "assetType", TemenosConstants.QUANTITY, TemenosConstants.ISINEXCHANGE,
							TemenosConstants.SWIPEACTIONENABLE };

					int uniqueid = 0;

					for (int i = 0; i < refnitivArray.length(); i++) {
						JSONObject ordersObj = refnitivArray.getJSONObject(i);
						responseStatus = ordersObj.has("orderStatus") ? ordersObj.get("orderStatus").toString() : "";
						if ((type.equalsIgnoreCase("HISTORY") && historyStatusList.contains(responseStatus))) {
							ordersObj = show_Orders(colArray, ordersObj, i, type, false, uniqueid);
							ordersObj.remove("orderStatus");
							ordersObj.remove("ExecQuantity");
							ordersObj.remove("UnExecQuantity");
							finalArr.put(ordersObj);
							uniqueid++;
						}
						if (cancel_OR_modify_order) {
							saveRecentOperationOrderIds(ordersObj);
						}
					}

					uniqueid = 0;

				}
			} else {
				finalArr = new JSONArray();
			}
			if (cancel_OR_modify_order) {
				result.clearDatasets();
				result.clearRecords();
				result.clearParams();
				responseJSON.put("recentoperationorderIds", historyorderIds.toString());
				responseJSON.put("recentoperationIdsCount", historyorderIds.size());

			} else {
				historyorderIds = new HashSet<String>();
				responseJSON.put("recentoperationDetails", finalArr);
				responseJSON.put("recentoperationtotalCount", finalArr.length());
				responseJSON.put("opstatus", "0");
				responseJSON.put("httpStatusCode", "200");
				responseJSON.put(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				return Utilities.constructResultFromJSONObject(responseJSON);
			}
		} catch (Exception e) {

			alert.prepareError("Error while invoking GetOrderDetailsPostProcessor - "
					+ WealthAPIServices.WEALTHSERVICESORCHESTRATION.getOperationName() + "  : " + e).log();
		}

		return result;
	}

	public JSONArray sortdatetimeArray(JSONArray array, String sortBy, String sortType) {
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

	private String orderType(String orderType) {
		if (orderType != null && orderType.trim().equalsIgnoreCase("LIMIT")) {
			return "Limit";
		} else if (orderType != null && orderType.trim().equalsIgnoreCase("MARKET")) {
			return "Market";
		} else if (orderType != null && orderType.trim().equalsIgnoreCase("STOP")) {
			return "Stop Loss";
		} else if (orderType != null && orderType.trim().equalsIgnoreCase("STOP-LIMIT")) {
			return "Stop Limit";
		}
		return "";
	}

	private String orderStatusName(String orderStatus, String inpType) {
		if (inpType != null && inpType.trim().equalsIgnoreCase("OPEN")) {
			if (orderStatus != null && orderStatus.trim().equalsIgnoreCase("To Send")
					|| orderStatus.trim().equalsIgnoreCase("Sent") || orderStatus.trim().equalsIgnoreCase("Placed")
					|| orderStatus.trim().equalsIgnoreCase("Partially Executed")) {

				return "Open";
			}

		} else {
			if (orderStatus != null && ((orderStatus.trim().equalsIgnoreCase("EXECUTED"))
					|| (orderStatus.trim().equalsIgnoreCase("Partially Executed")))) {
				return "Completed";
			} else if (orderStatus != null && (orderStatus.trim().equalsIgnoreCase("CANCELLED"))) {
				return "Cancelled";
			} else if (orderStatus != null && (orderStatus.trim().equalsIgnoreCase("REJECTED"))) {
				return "Rejected";
			} else if (orderStatus != null && (orderStatus.trim().equalsIgnoreCase("EXPIRED"))) {
				return "Expired";
			} else {
				return orderStatus;
			}
		}
		return orderStatus;
	}

	private JSONObject show_Orders(String[] colArray, JSONObject ordersObj, int i, String type, boolean flagq,
			int uniqueid) {
		String status = "", orderMode = "", ordertransactType = "", validity = "";
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
		for (String key : colArray) {

			if (!ordersObj.has(key)) {
				ordersObj.put(key, "");
			}
			if (key.trim().equalsIgnoreCase("isCancelEnable")) {

				ordersObj.put(key,
						ordersObj.get(key).toString().trim().length() > 0 ? ordersObj.get(key).toString() : "false");
			}
			if (key.trim().equalsIgnoreCase("isModifyEnable")) {

				ordersObj.put(key,
						ordersObj.get(key).toString().trim().length() > 0 ? ordersObj.get(key).toString() : "false");
			}
			if (key.trim().equalsIgnoreCase("orderMode")) {

				orderMode = ordersObj.get("orderMode").toString();
				if (orderMode.equalsIgnoreCase("SEL")) {
					orderMode = "Sell";
				}
				orderMode = orderMode.trim().length() > 0
						? (orderMode.substring(0, 1).toUpperCase()
								+ orderMode.substring(1, orderMode.length()).toLowerCase())
						: "";
				ordersObj.put(key, orderMode);
			}

			if (key.trim().equalsIgnoreCase("orderModeType")) {

				ordertransactType = (orderType(ordersObj.get("orderModeType").toString()));
				ordersObj.put(key, ordertransactType);
			}

			if (key.trim().equalsIgnoreCase(TemenosConstants.ORDERTYPE)) {
				orderMode = orderMode.trim().length() > 0
						? (orderMode.substring(0, 1).toUpperCase()
								+ orderMode.substring(1, orderMode.length()).toLowerCase())
						: "";
				ordersObj.put(key, (orderMode + " " + ordertransactType));
			}

			if (key.trim().equalsIgnoreCase("orderStatus")) {
				status = orderStatusName(ordersObj.get("orderStatus").toString(), type);
				ordersObj.put(TemenosConstants.ORDERS_STATUS, status);

				if (status.trim().equalsIgnoreCase(TemenosConstants.STATUS_COMPLETED)) {
					if (ordertransactType.trim().equalsIgnoreCase(TemenosConstants.LIMIT_TYPE)
							|| ordertransactType.trim().equalsIgnoreCase(TemenosConstants.STOPLIMIT_TYPE)) {

						ordersObj.put(TemenosConstants.ORDER_EXECUTION_PRICE,
								(ordersObj.get(TemenosConstants.LIMITPRICE).toString().length() > 0
										? ordersObj.get(TemenosConstants.LIMITPRICE).toString()
										: ""));

					} else if (ordertransactType.trim().equalsIgnoreCase(TemenosConstants.STOPLOSS_TYPE)) {
						ordersObj.put(TemenosConstants.ORDER_EXECUTION_PRICE,
								(ordersObj.get(TemenosConstants.STOPPRICE).toString().length() > 0
										? ordersObj.get(TemenosConstants.STOPPRICE).toString()
										: ""));
					} else {
						ordersObj.put(TemenosConstants.ORDER_EXECUTION_PRICE, "");
					}
				} else {
					ordersObj.put(TemenosConstants.ORDER_EXECUTION_PRICE, "");
				}

			}

			if (key.trim().equalsIgnoreCase(TemenosConstants.STOPPRICE)
					|| key.trim().equalsIgnoreCase(TemenosConstants.LIMITPRICE)) {
				ordersObj.put(key, (ordersObj.get(key).toString().length() > 0 ? ordersObj.get(key).toString() : ""));
			}

			if (key.trim().equalsIgnoreCase("validity")) {

				validity = ordersObj.get(key).toString().trim().length() > 0
						? (ordersObj.get(key).toString().equalsIgnoreCase("Day Order") ? "Day Order"
								: "Good Till Canceled")
						: "Good Till Canceled";
				ordersObj.put(key, validity);
			}

			if (key.trim().equalsIgnoreCase("tradeDate")) {
				if (ordersObj.get(key).toString().trim().length() > 0) {
					try {
						Date sdt = sdformat.parse(ordersObj.get(key).toString());
						String tradeDate = sdformat.format(sdt);
						ordersObj.put(key, tradeDate);
					} catch (Exception e) {
						e.getMessage();
					}
				}
			}

			/*
			 * if (key.trim().equalsIgnoreCase(TemenosConstants.ORDER_REFERENCE)) {
			 * historyorderIds.add(ordersObj.get(key).toString()); }
			 */

			if (key.trim().equalsIgnoreCase(TemenosConstants.QUANTITY)) {
				ordersObj.put(key, (ordersObj.get(key).toString().length() > 0 ? ordersObj.get(key).toString() : ""));
			}

			if (key.trim().equalsIgnoreCase(TemenosConstants.ISINEXCHANGE)) {
				ordersObj.put(key,
						((!ordersObj.get("ISIN").toString().equalsIgnoreCase(""))
								? ((!ordersObj.get(TemenosConstants.HOLDINGS_TYPE).toString().equalsIgnoreCase(""))
										? (ordersObj.get("ISIN").toString() + " | "
												+ ordersObj.get(TemenosConstants.HOLDINGS_TYPE).toString())
										: ordersObj.get("ISIN").toString())
								: ((!ordersObj.get(TemenosConstants.HOLDINGS_TYPE).toString().equalsIgnoreCase(""))
										? ordersObj.get(TemenosConstants.HOLDINGS_TYPE)
										: "")));
			}
			if (key.trim().equalsIgnoreCase(TemenosConstants.SWIPEACTIONENABLE)) {
				if (orderMode.equalsIgnoreCase("BUY") || orderMode.equalsIgnoreCase("SELL")) {
					if (ordertransactType.equalsIgnoreCase("Market") || ordertransactType.equalsIgnoreCase("Limit")
							|| ordertransactType.equalsIgnoreCase("Stop Loss")
							|| ordertransactType.equalsIgnoreCase("Stop Limit")) {
						if (validity.equalsIgnoreCase("Day Order") || validity.equalsIgnoreCase("Good Till Canceled")) {
							if (ordersObj.get("isCancelEnable").toString().equalsIgnoreCase("true")
									&& ordersObj.get("isModifyEnable").toString().equalsIgnoreCase("false")) {
								ordersObj.put(key, "Cancel");
							} else if (ordersObj.get("isModifyEnable").toString().equalsIgnoreCase("true")
									&& ordersObj.get("isCancelEnable").toString().equalsIgnoreCase("false")) {
								ordersObj.put(key, "Modify");
							} else if (ordersObj.get("isCancelEnable").toString().equalsIgnoreCase("true")
									&& ordersObj.get("isModifyEnable").toString().equalsIgnoreCase("true")) {
								ordersObj.put(key, "true");
							} else if (ordersObj.get("isCancelEnable").toString().equalsIgnoreCase("false")
									&& ordersObj.get("isModifyEnable").toString().equalsIgnoreCase("false")) {
								ordersObj.put(key, "false");
							} else {
								ordersObj.put(key, "false");
							}
						} else {
							ordersObj.put(key, "false");
						}
					} else {
						ordersObj.put(key, "false");
					}
				} else {
					ordersObj.put(key, "false");
				}
			}

			ordersObj.put(TemenosConstants.ISTAPINTEGRATION, "true");
			ordersObj.put("uniqueid", uniqueid);
		}
		return ordersObj;
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
						+ WealthAPIServices.WEALTH_GETORDERSDETAILS.getOperationName() + "  : " + e).log();
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
			alert.prepareError("Error while invoking ORDERS - " + WealthAPIServices.WEALTH_GETORDERSDETAILS.getOperationName()
					+ "  : " + e).log();
			return null;
		}
		return filteredArray;
	}

	private void saveRecentOperationOrderIds(JSONObject ordersObj) {
		if (ordersObj.has(TemenosConstants.ORDER_REFERENCE)
				&& ordersObj.get(TemenosConstants.ORDER_REFERENCE) != null) {
			historyorderIds.add(ordersObj.get(TemenosConstants.ORDER_REFERENCE).toString());
		}
	}

}
