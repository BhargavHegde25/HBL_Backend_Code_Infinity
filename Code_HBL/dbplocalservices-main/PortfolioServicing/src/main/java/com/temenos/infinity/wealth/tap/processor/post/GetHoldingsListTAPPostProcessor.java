/**
 * 
 */
package com.temenos.infinity.wealth.tap.processor.post;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;
import com.temenos.infinity.wealth.common.util.PortfolioServiceUtils;

/**
 * @author vanathi.nirupama
 *
 */
public class GetHoldingsListTAPPostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

		try {
			diagnostic.prepareDebug("==========> GetHoldingsListTAPPostProcessor TAP - Entered ").log();
			String refCcy = "";
			JSONObject holdingsJSON = new JSONObject();
			JSONArray sortedJSON = new JSONArray();
			String sortBy = request.getParameter("sortBy");
			int totalCount = 0;
			String[] colArray = new String[] { TemenosConstants.HOLDINGSID, "holdingsType",
					TemenosConstants.MARKETPRICE, "ISIN", TemenosConstants.MARKETVALPOS,
					TemenosConstants.WEIGHTPERCENTAGE, TemenosConstants.UNREALPLMKT, TemenosConstants.REGION,
					TemenosConstants.ASSETCLASS, TemenosConstants.SECTOR, TemenosConstants.SECCCY,
					TemenosConstants.MARKETVALUE, TemenosConstants.COSTVALUE, TemenosConstants.UNREALIZEDPLPERCENTAGE,
					TemenosConstants.QUANTITY, TemenosConstants.COSTPRICE, TemenosConstants.DESCRIPTION,
					TemenosConstants.COSTVALUESECCCY, TemenosConstants.UNREALPLMKTSECCCY,
					TemenosConstants.UNREALIZEDPLPERCENTAGESECCCY, "exchangeRate", TemenosConstants.SUBASSETCLASS,
					"accruedInterest", TemenosConstants.ISINEXCHANGE, TemenosConstants.MARKETVALUEINSECCCY, TemenosConstants.STATUS };
			String fieldValue = Arrays.toString(colArray).replace("[", "").replace("]", "");
			fieldValue = fieldValue.replace("accruedInterest", "");
			Dataset ds = result.getDatasetById("body");
			if (ds != null) {

				List<Record> drecords = ds.getAllRecords();
				JSONArray bodyArray = new JSONArray();
				for (int j = 0; j < drecords.size(); j++) {
					JSONObject holdingsObj = new JSONObject();
					Record drecord = drecords.get(j);
					holdingsObj = CommonUtils.convertRecordToJSONObject(drecord);
					bodyArray.put(holdingsObj);
				}

				/*
				 * JSONObject resultObj = new JSONObject(); resultObj.put("Field",
				 * Utilities.convertStringToJSONArray(ResultToJSON.convertDataset(ds).toString()
				 * )); JSONArray bodyArray = resultObj.getJSONArray("Field");
				 */
				diagnostic.prepareDebug("==========> GetHoldingsListTAPPostProcessor TAP - No. of records returned initially: " + bodyArray.length() ).log();
				String[] fieldsArray = new String[] {};

				JSONArray holdingsArr = new JSONArray();
				for (int i = 0; i < bodyArray.length(); i++) {

					JSONObject instrumentObj = bodyArray.getJSONObject(i);
					if (instrumentObj.has("nature")
							&& !instrumentObj.get("nature").toString().equalsIgnoreCase("Cash Account")
							&& !instrumentObj.get("quantity").equals("0")) {
						for (String key : colArray) {
							if (!instrumentObj.has(key)) {
								instrumentObj.put(key, "");
							}
							if (!instrumentObj.get("nature").toString().equalsIgnoreCase("Money Market")
									&& !instrumentObj.get("nature").toString().equalsIgnoreCase("Fixed Income")
									&& key.equalsIgnoreCase("accruedInterest")) {
								instrumentObj.remove(key);
							}
							if (key.equalsIgnoreCase(TemenosConstants.ISINEXCHANGE)) {
								instrumentObj
										.put(key,
												((!instrumentObj.get("ISIN").toString().equalsIgnoreCase(""))
														? ((!instrumentObj.get(TemenosConstants.HOLDINGS_TYPE)
																.toString().equalsIgnoreCase(""))
																		? (instrumentObj.get("ISIN").toString() + " | "
																				+ instrumentObj.get(
																						TemenosConstants.HOLDINGS_TYPE)
																						.toString())
																		: instrumentObj.get("ISIN").toString())
														: ((!instrumentObj.get(TemenosConstants.HOLDINGS_TYPE)
																.toString().equalsIgnoreCase(""))
																		? instrumentObj.get(
																				TemenosConstants.HOLDINGS_TYPE)
																		: "")));
							}

							if (key.equalsIgnoreCase(TemenosConstants.MARKETPRICE)) {
								instrumentObj.put(key, (instrumentObj.get(TemenosConstants.MARKETPRICE) != null
										&& instrumentObj.get(TemenosConstants.MARKETPRICE).toString().length() > 0
												? instrumentObj.get(TemenosConstants.MARKETPRICE).toString()
												: "0"));
							}

						}
						List<String> openStatusList = new ArrayList<>();
						String INF_WLTH_ORDER_STATUS = EnvironmentConfigurationsHandler
								.getValue(TemenosConstants.INF_WLTH_ORDER_STATUS, request);
						if (INF_WLTH_ORDER_STATUS != null && INF_WLTH_ORDER_STATUS.length() > 0) {
							JSONObject json = new JSONObject(INF_WLTH_ORDER_STATUS);
							JSONArray openArray = json.getJSONArray(TemenosConstants.ORDERS_TYPE_OPEN);
							if (openArray != null && openArray.length() > 0) {
								for (int j = 0; j < openArray.length(); j++) {
									if (openArray.get(j) != null && !openArray.isEmpty())
										openStatusList.add(openArray.get(j).toString());
								}
							}
						}
						if (request.getParameter(TemenosConstants.ISINCLUEORDERS).equalsIgnoreCase(TemenosConstants.TRUE)) {
							if (instrumentObj.has(TemenosConstants.STATUS)
									&& instrumentObj.get(TemenosConstants.STATUS) != null
									&& instrumentObj.get(TemenosConstants.STATUS).toString().length() > 0) {
								String status = instrumentObj.get(TemenosConstants.STATUS).toString();
								if (openStatusList.contains(status)) {
									instrumentObj.put(TemenosConstants.STATUS, TemenosConstants.OPEN);

								} else if (status.equals(TemenosConstants.ACCOUNTED_STATUS)) {
									instrumentObj.put(TemenosConstants.STATUS, "");
								} else {
									continue;
								}

							}
						} else {
							instrumentObj.remove(TemenosConstants.STATUS);
						}
						if (instrumentObj.has(TemenosConstants.REFERENCECURRENCY)
								&& instrumentObj.get(TemenosConstants.REFERENCECURRENCY) != null
								&& instrumentObj.get(TemenosConstants.REFERENCECURRENCY).toString().length() > 0) {
							refCcy = instrumentObj.get(TemenosConstants.REFERENCECURRENCY).toString();
						}

						instrumentObj.put("application",
								(instrumentObj.get("nature").toString().equalsIgnoreCase("Stock")
										|| instrumentObj.get("nature").toString().equalsIgnoreCase("Fixed Income")
										|| instrumentObj.get("nature").toString().equalsIgnoreCase("Fund Share"))
												? "SC"
												: "DX");
						instrumentObj.put("isSecurityAsset",
								(instrumentObj.get("nature").toString().equalsIgnoreCase("Stock")
										|| instrumentObj.get("nature").toString().equalsIgnoreCase("Fixed Income")
										|| instrumentObj.get("nature").toString().equalsIgnoreCase("Fund Share"))
												? true
												: false);

						instrumentObj.put(TemenosConstants.UNREALPLMKTSECCCY,
								(instrumentObj.get(TemenosConstants.UNREALPLMKTSECCCY).toString().equalsIgnoreCase(""))
										? "-"
										: instrumentObj.get(TemenosConstants.UNREALPLMKTSECCCY).toString());
						instrumentObj.put(TemenosConstants.UNREALIZEDPLPERCENTAGESECCCY,
								(instrumentObj.get(TemenosConstants.UNREALIZEDPLPERCENTAGESECCCY).toString()
										.equalsIgnoreCase("")) ? "-"
												: instrumentObj.get(TemenosConstants.UNREALIZEDPLPERCENTAGESECCCY)
														.toString());
						instrumentObj.put(TemenosConstants.UNREALIZEDPLPERCENTAGE,
								instrumentObj.get(TemenosConstants.UNREALIZEDPLPERCENTAGE).toString()
										.equalsIgnoreCase("") ? "0.00"
												: String.format("%.2f", Double.parseDouble(instrumentObj
														.get(TemenosConstants.UNREALIZEDPLPERCENTAGE).toString())));
						instrumentObj.put(TemenosConstants.UNREALPLMKT,
								(instrumentObj.get(TemenosConstants.UNREALPLMKT).toString().equalsIgnoreCase(""))
										? "0.00"
										: instrumentObj.get(TemenosConstants.UNREALPLMKT).toString());

						instrumentObj.put(TemenosConstants.WEIGHTPERCENTAGE,
								instrumentObj.get(TemenosConstants.WEIGHTPERCENTAGE).toString().equalsIgnoreCase("")
										? "0.00"
										: String.format("%.2f", Double.parseDouble(
												instrumentObj.get(TemenosConstants.WEIGHTPERCENTAGE).toString())));

						instrumentObj.put(TemenosConstants.MARKETVALUE,
								instrumentObj.get(TemenosConstants.MARKETVALUE).toString().equalsIgnoreCase("") ? "0.00"
										: String.format("%.2f", Double
												.parseDouble(instrumentObj.get(TemenosConstants.MARKETVALUE).toString())
												* (instrumentObj.get("exchangeRate").toString().equalsIgnoreCase("")
														? Double.parseDouble(
																instrumentObj.get("exchangeRate").toString())
														: 1)));

					} else {
						continue;
					}

					for (String s : fieldsArray) {
						instrumentObj.remove(s);
					}
					holdingsArr.put(instrumentObj);
				}

				String sortType = request.getParameter(TemenosConstants.SORTORDER);
				diagnostic.prepareDebug("==========> GetHoldingsListTAPPostProcessor TAP - No. of holdings returned: " + holdingsArr.length() ).log();
				sortedJSON = holdingsArr;
				if (sortBy != null) {
					sortedJSON = PortfolioServiceUtils.sortHoldingsArray(sortedJSON, sortBy, sortType);
					diagnostic.prepareDebug("==========> GetHoldingsListTAPPostProcessor TAP - No. of holdings returned after sort: " + sortedJSON.length() ).log();
				} else {
				}

				String searchVal = request.getParameter("searchByInstrumentName");
				String search = (searchVal != null && searchVal.trim().length() > 0) ? searchVal : "";
				if (search.equals("")) {
					diagnostic.prepareDebug("==========> GetHoldingsListTAPPostProcessor TAP - No search implemented ").log();
				} else {
					String instrumentId = request.getParameter("instrumentId") != null
							? request.getParameter("instrumentId")
							: "";
					
					sortedJSON = PortfolioServiceUtils.returnHoldingsSearch(sortedJSON, search, instrumentId);
					diagnostic.prepareDebug("==========> GetHoldingsListTAPPostProcessor TAP - No. of holdings returned after search: " + sortedJSON.length() ).log();
				}
				if (request.getParameter("instrumentId") != null
						&& request.getParameter("instrumentId").toString() != null) {
					String instrumentid = request.getParameter("instrumentId").toString();
					
					sortedJSON = PortfolioServiceUtils.returnHoldingsInstrumentID(sortedJSON, instrumentid);
					diagnostic.prepareDebug("==========> GetHoldingsListTAPPostProcessor TAP - No. of holdings returned after search by ID: " + sortedJSON.length() ).log();
				} else {
					diagnostic.prepareDebug("==========> GetHoldingsListTAPPostProcessor TAP - No search by id implemented ").log();
				}

				String limitVal = request.getParameter(TemenosConstants.PAGESIZE);
				String offsetVal = request.getParameter(TemenosConstants.PAGEOFFSET);
				int limit = (limitVal != null && limitVal.trim().length() > 0) ? Integer.parseInt(limitVal) : 0;
				int offset = (offsetVal != null && offsetVal.trim().length() > 0) ? Integer.parseInt(offsetVal) : 0;

				totalCount = sortedJSON.length();
				if (limit > 0 && offset >= 0) {
					sortedJSON = PortfolioWealthUtils.pagination(sortedJSON, limit, offset);
					diagnostic.prepareDebug("==========> GetHoldingsListTAPPostProcessor TAP - No. of holdings returned after pagination: " + sortedJSON.length() ).log();
				}

			} else {
				sortedJSON = new JSONArray();
			}
			holdingsJSON.put("portfolioHoldings", sortedJSON);
			holdingsJSON.put("fieldstoDisplay", fieldValue);
			holdingsJSON.put("portfolioID", request.getParameter("portfolioId"));
			holdingsJSON.put("accountNumber", request.getParameter("portfolioId"));
			holdingsJSON.put(TemenosConstants.SORTBY, sortBy);
			holdingsJSON.put("totalCount", totalCount);
			holdingsJSON.put("referenceCurrency", refCcy);

			Result holdingsRes = Utilities.constructResultFromJSONObject(holdingsJSON);
			holdingsRes.addOpstatusParam("0");
			holdingsRes.addHttpStatusCodeParam("200");
			holdingsRes.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			diagnostic.prepareDebug("==========> GetHoldingsListTAPPostProcessor TAP - Exited ").log();
			return holdingsRes;

		} catch (Exception e) {
			alert.prepareError("==========> GetHoldingsListTAPPostProcessor TAP - Error: " + e.getMessage()).log();
			e.getMessage();
		}

		return result;
	}
}
