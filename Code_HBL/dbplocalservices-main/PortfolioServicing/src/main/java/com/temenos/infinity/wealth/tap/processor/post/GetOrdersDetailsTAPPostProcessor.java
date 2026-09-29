/**
 * 
 */
package com.temenos.infinity.wealth.tap.processor.post;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;
import com.temenos.infinity.wealth.common.util.PortfolioServiceUtils;

/**
 * @author himaja.sridhar
 *
 */
public class GetOrdersDetailsTAPPostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private LinkedHashSet<Integer> partialcompleted_index = new LinkedHashSet<Integer>();
	private HashSet<String> orderIds = new HashSet<String>();

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {
			orderIds = new HashSet<String>();
			diagnostic.prepareDebug("==========> GetOrdersDetailsTAPPostProcessor TAP - Entered ").log();
			boolean cancel_OR_modify_order = false;
			String[] typeArr = null, openStatusArr = null, historyStatusArr = null, portfolioIdArr = null,
					searchArr = null, sortByArr = null, sortOrderArr = null, pageSizeArr = null, pageOffsetArr = null,
					startDateArr = null, endDateArr = null;
			String type = "", responseStatus = "", portfolioId = "", startDate = "", endDate = "", search = "",
					sortBy = "", sortOrder = "", pageSize = "", pageOffset = "", orderId = "";
			int totalCount = 0, pageSizeVal = 0, pageOffsetVal = 0;

			portfolioIdArr = request.getParameterValues(TemenosConstants.PORTFOLIOID);
			typeArr = request.getParameterValues(TemenosConstants.ORDERSVIEW_TYPE);
			startDateArr = request.getParameterValues(TemenosConstants.STARTDATE);
			endDateArr = request.getParameterValues(TemenosConstants.ENDDATE);
			searchArr = request.getParameterValues(TemenosConstants.SEARCHBYINSTRUMENTNAME);
			sortByArr = request.getParameterValues(TemenosConstants.SORTBY);
			sortOrderArr = request.getParameterValues(TemenosConstants.SORTORDER);
			pageSizeArr = request.getParameterValues(TemenosConstants.PAGESIZE);
			pageOffsetArr = request.getParameterValues(TemenosConstants.PAGEOFFSET);

			if (request.getParameterValues("cancelormodify_order") != null
					&& request.getParameterValues("cancelormodify_order")[0].toString().equalsIgnoreCase("true")) {
				cancel_OR_modify_order = true;
				if (StringUtils.isNotBlank(request.getParameterValues(TemenosConstants.ORDER_ID)[0])) {
					orderId = request.getParameterValues(TemenosConstants.ORDER_ID)[0];
				}
			}

			JSONArray sortedJSON = new JSONArray();
			JSONArray finalArr = new JSONArray();

			if (typeArr != null) {
				type = typeArr[0].toString().trim();
			}

			//openStatusArr = new String[] { "To Send", "Sent", "Placed", "Partially Executed" };
			//List<String> openStatusList = Arrays.asList(openStatusArr);
			List<String> openStatusList = new ArrayList<>();
			//historyStatusArr = new String[] { "Cancelled", "Rejected", "Expired", "Partially Executed" };
			//List<String> historyStatusList = Arrays.asList(historyStatusArr);
			List<String> historyStatusList = new ArrayList<>();
			
			String INF_WLTH_ORDER_STATUS = EnvironmentConfigurationsHandler
					.getValue(TemenosConstants.INF_WLTH_ORDER_STATUS, request);
			if (INF_WLTH_ORDER_STATUS != null && INF_WLTH_ORDER_STATUS.length() > 0) {
				JSONObject json = new JSONObject(INF_WLTH_ORDER_STATUS);
				JSONArray openArray = json.getJSONArray(TemenosConstants.ORDERS_TYPE_OPEN);
				if (openArray != null && openArray.length() > 0) {
					for (int i = 0; i < openArray.length(); i++) {
						if (openArray.get(i) != null && !openArray.isEmpty())
							openStatusList.add(openArray.get(i).toString());
					}
				}
				JSONArray historyArray = json.getJSONArray(TemenosConstants.ORDERS_TYPE_HISTORY);
				if (historyArray != null && historyArray.length() > 0) {
					for (int i = 0; i < historyArray.length(); i++) {
						if (historyArray.get(i) != null && !historyArray.isEmpty())
							historyStatusList.add(historyArray.get(i).toString());
					}
				}

			}
			if (type.equalsIgnoreCase("OPEN")) {
				diagnostic.prepareDebug("==========> GetOrdersDetailsTAPPostProcessor TAP - Open ").log();
				if (startDateArr != null && startDateArr.length > 0) {
					startDate = startDateArr[0].trim();
				}
				if (endDateArr != null && endDateArr.length > 0) {
					endDate = endDateArr[0].trim();
				}
				if (searchArr != null && searchArr.length > 0) {
					search = searchArr[0].trim();
				}
				if (sortByArr != null && sortByArr.length > 0) {
					sortBy = sortByArr[0].trim();
				}
				if (sortOrderArr != null && sortOrderArr.length > 0) {
					sortOrder = sortOrderArr[0].trim();
				}
				if (pageSizeArr != null && pageSizeArr.length > 0) {
					pageSize = pageSizeArr[0].trim();
				}
				if (pageOffsetArr != null && pageOffsetArr.length > 0) {
					pageOffset = pageOffsetArr[0].trim();
				}
				if (portfolioIdArr != null && portfolioIdArr.length > 0) {
					portfolioId = portfolioIdArr[0].trim();
				}

				pageSizeVal = (pageSize != null && pageSize.trim().length() > 0) ? Integer.parseInt(pageSize) : 0;
				pageOffsetVal = (pageOffset != null && pageOffset.trim().length() > 0) ? Integer.parseInt(pageOffset)
						: 0;
			}
			JSONObject responseJSON = new JSONObject();

			Dataset ds = result.getDatasetById("body");
			if (ds != null) {
				diagnostic.prepareDebug("==========> GetOrdersDetailsTAPPostProcessor TAP - No. of records returned initially: " + ds.getAllRecords().size() ).log();
				JSONObject resultObj = new JSONObject();
				resultObj.put("Field", Utilities.convertStringToJSONArray(ResultToJSON.convertDataset(ds).toString()));
				JSONArray refnitivArray = resultObj.getJSONArray("Field");

				if (refnitivArray != null && refnitivArray.length() > 0) {

					String[] colArray = new String[] { "isCancelEnable", "isModifyEnable", TemenosConstants.DESCRIPTION,
							"ISIN", "orderedAt", "instrumentId", "holdingsType", TemenosConstants.LIMITPRICE,
							TemenosConstants.STOPPRICE, TemenosConstants.TRADEDATE, "orderMode", "orderModeType",
							TemenosConstants.ORDERTYPE, TemenosConstants.ORDER_REFERENCE, "orderStatus", "validity",
							"instrumentCurrency", "assetType", TemenosConstants.QUANTITY, TemenosConstants.ISINEXCHANGE,
							TemenosConstants.SWIPEACTIONENABLE, };

					int uniqueid = 0;

					for (int i = 0; i < refnitivArray.length(); i++) {
						JSONObject ordersObj = refnitivArray.getJSONObject(i);
						responseStatus = ordersObj.has("orderStatus") ? ordersObj.get("orderStatus").toString() : "";
						if (((type.equalsIgnoreCase("OPEN") && openStatusList.contains(responseStatus))
								|| (type.equalsIgnoreCase("HISTORY") && historyStatusList.contains(responseStatus)))) {
							ordersObj = show_Orders(colArray, ordersObj, i, type, false, uniqueid);
							ordersObj.remove("orderStatus");
							ordersObj.remove("ExecQuantity");
							ordersObj.remove("UnExecQuantity");
							finalArr.put(ordersObj);
							uniqueid++;
						}
						if (cancel_OR_modify_order) {
							savePendingOrderIds(ordersObj);
						}
					}

					if (partialcompleted_index != null && partialcompleted_index.size() > 0) {
						Dataset body = result.getDatasetById("body");
						if (body != null) {

							JSONObject resultJSONObj = new JSONObject();
							resultJSONObj.put("Field",
									Utilities.convertStringToJSONArray(ResultToJSON.convertDataset(body).toString()));
							JSONArray fabricresponseArray = resultJSONObj.getJSONArray("Field");

							if (fabricresponseArray != null && fabricresponseArray.length() > 0) {

								for (int i = 0; i < fabricresponseArray.length(); i++) {
									if (partialcompleted_index.contains(i)) {
										JSONObject jsonObj = fabricresponseArray.getJSONObject(i);
										responseStatus = jsonObj.has("orderStatus")
												? jsonObj.get("orderStatus").toString()
												: "";
										if (((type.equalsIgnoreCase("OPEN") && openStatusList.contains(responseStatus))
												|| (type.equalsIgnoreCase("HISTORY")
														&& historyStatusList.contains(responseStatus)))) {

											jsonObj = show_Orders(colArray, jsonObj, i, type, true, uniqueid);
											jsonObj.remove("orderStatus");
											jsonObj.remove("ExecQuantity");
											jsonObj.remove("UnExecQuantity");
											finalArr.put(jsonObj);
											uniqueid++;
										}
										if (cancel_OR_modify_order) {
											savePendingOrderIds(jsonObj);
										}
									}

								}
							}
						}
					}
					uniqueid = 0;
					partialcompleted_index = new LinkedHashSet<Integer>();
					if (type.equalsIgnoreCase("OPEN")) {
						if (startDate == null || endDate == null || startDate.equals("") || endDate.equals("")) {
							sortedJSON = finalArr;
						} else {
							sortedJSON = PortfolioServiceUtils.filterOrdersDate(finalArr, startDate, endDate);
							diagnostic.prepareDebug("==========> GetOrdersDetailsTAPPostProcessor TAP - No. of holdings returned after date filter: " + sortedJSON.length() ).log();
						}

						if (search != null && search.length() > 0) {
							sortedJSON = returnSearch(sortedJSON, search);
							diagnostic.prepareDebug("==========> GetOrdersDetailsTAPPostProcessor TAP - No. of holdings returned after search: " + sortedJSON.length() ).log();
						}

						// other sorts are performed in backendImpl itself
						if (sortBy != null && sortBy.equalsIgnoreCase("tradeDate")) {
							sortedJSON = sortdatetimeArray(sortedJSON, sortBy, sortOrder);
							diagnostic.prepareDebug("==========> GetOrdersDetailsTAPPostProcessor TAP - No. of holdings returned after sort by date: " + sortedJSON.length() ).log();
						} else if (sortBy != null) {
							sortedJSON = PortfolioServiceUtils.sortOrdersArray(sortedJSON, sortBy, sortOrder);
							diagnostic.prepareDebug("==========> GetOrdersDetailsTAPPostProcessor TAP - No. of holdings returned after sort: " + sortedJSON.length() ).log();
						}

						totalCount = sortedJSON.length();

						if (pageSizeVal > 0 && pageOffsetVal >= 0) {
							sortedJSON = PortfolioWealthUtils.pagination(sortedJSON, pageSizeVal, pageOffsetVal);
							diagnostic.prepareDebug("==========> GetOrdersDetailsTAPPostProcessor TAP - No. of holdings returned after pagination: " + sortedJSON.length() ).log();
						}
					}
				}
			} else {
				sortedJSON = new JSONArray();
			}

			if (cancel_OR_modify_order) {
				result.clearDatasets();
				result.clearRecords();
				result.clearParams();
				responseJSON.put("pendingorderIds", orderIds.toString());
				responseJSON.put("pendingorderIdsCount", orderIds.size());
				responseJSON.put("pendingOrderID_Authentication", orderIds.contains(orderId));

			} else {
				orderIds = new HashSet<String>();
				if (type.equalsIgnoreCase("OPEN")) {
					responseJSON.put("portfolioID", portfolioId);
					responseJSON.put("ordersDetails", sortedJSON);
					responseJSON.put(TemenosConstants.STARTDATE, startDate);
					responseJSON.put(TemenosConstants.ENDDATE, endDate);
					responseJSON.put(TemenosConstants.SORTBY, sortBy);
					responseJSON.put(TemenosConstants.SORTORDER, sortOrder);
					responseJSON.put("count", totalCount);
				} else {
					responseJSON.put("pendingordersDetails", finalArr);
					responseJSON.put("pendingorderstotalCount", finalArr.length());
				}
			}

			responseJSON.put("opstatus", "0");
			responseJSON.put("httpStatusCode", "200");
			responseJSON.put(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			diagnostic.prepareDebug("==========> GetOrdersDetailsTAPPostProcessor TAP - Exited ").log();
			return Utilities.constructResultFromJSONObject(responseJSON);
		} catch (Exception e) {

			alert.prepareError("==========> GetOrdersDetailsTAPPostProcessor TAP - Error: " + e.getMessage()).log();
		}

		return result;
	}

	public JSONArray sortdatetimeArray(JSONArray array, String sortBy, String sortType) {
		JSONArray sortedJSON = new JSONArray();
		JSONArray sortedJSONArray = new JSONArray();
		SimpleDateFormat sdformat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
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
		}
		return sortedJSONArray;

	}

	private void partialcompletedmethod(int i) {
		partialcompleted_index.add(i);
	}

	private String orderType(String orderType) {
		diagnostic.prepareDebug("==========> GetOrdersDetailsTAPPostProcessor TAP - Enter:orderType ").log();
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
		diagnostic.prepareDebug("==========> GetOrdersDetailsTAPPostProcessor TAP - Enter:orderStatusName ").log();
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

	private JSONObject show_Orders(String[] colArray, JSONObject ordersObj, int i, String type, boolean flag,
			int uniqueid) {
		diagnostic.prepareDebug("==========> GetOrdersDetailsTAPPostProcessor TAP - Enter:show_Orders ").log();
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
			// For Cancel and Modify Orders - Security
			/*
			 * if (key.trim().equalsIgnoreCase(TemenosConstants.ORDER_REFERENCE)) {
			 * orderIds.add(ordersObj.get(key).toString()); }
			 */

			if (key.trim().equalsIgnoreCase(TemenosConstants.QUANTITY)) {
				if (ordersObj.get("orderStatus").toString().trim().equalsIgnoreCase(TemenosConstants.PARTIALLY_EXECUTED)
						&& type.trim().equalsIgnoreCase("OPEN")) {

					ordersObj.put(key,
							(ordersObj.has("UnExecQuantity") && ordersObj.get("UnExecQuantity") != null
									&& ordersObj.get("UnExecQuantity").toString().length() > 0)
											? ordersObj.get("UnExecQuantity").toString()
											: "");

				} else if (ordersObj.get("orderStatus").toString().trim().equalsIgnoreCase(
						TemenosConstants.PARTIALLY_EXECUTED) && type.trim().equalsIgnoreCase("HISTORY")) {

					ordersObj.put(key,
							(ordersObj.has("ExecQuantity") && ordersObj.get("ExecQuantity") != null
									&& ordersObj.get("ExecQuantity").toString().length() > 0)
											? ordersObj.get("ExecQuantity").toString()
											: "");

				} else if (ordersObj.get("orderStatus").toString().trim().equalsIgnoreCase("Cancelled")
						&& type.trim().equalsIgnoreCase("HISTORY")) {

					if (ordersObj.has("ExecQuantity") && ordersObj.get("ExecQuantity") != null
							&& ordersObj.get("ExecQuantity").toString().length() > 0) {

						partialcompletedmethod(i);
						if (flag) {
							ordersObj.put(TemenosConstants.QUANTITY, ordersObj.get("ExecQuantity").toString());
							ordersObj.put(TemenosConstants.ORDERS_STATUS, "Completed");
						}
					}

					if (ordersObj.has("UnExecQuantity") && ordersObj.get("UnExecQuantity") != null
							&& ordersObj.get("UnExecQuantity").toString().length() > 0) {
						if (!flag) {
							ordersObj.put(TemenosConstants.QUANTITY, ordersObj.get("UnExecQuantity").toString());
						}
					}

				} else {
					ordersObj.put(key,
							(ordersObj.get(key).toString().length() > 0 ? ordersObj.get(key).toString() : ""));

				}
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
		diagnostic.prepareDebug("==========> GetOrdersDetailsTAPPostProcessor TAP - Exit:show_Orders ").log();
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
				alert.prepareError("Error while invoking GetOrdersDetailsTAPPostProcessor - " + e.getMessage()).log();
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
			alert.prepareError("Error while invoking GetOrdersDetailsTAPPostProcessor - " + e.getMessage()).log();
			return null;
		}
		return filteredArray;
	}

	private void savePendingOrderIds(JSONObject ordersObj) {
		if (ordersObj.has(TemenosConstants.ORDER_REFERENCE)
				&& ordersObj.get(TemenosConstants.ORDER_REFERENCE) != null) {
			orderIds.add(ordersObj.get(TemenosConstants.ORDER_REFERENCE).toString());
		}
	}
}
