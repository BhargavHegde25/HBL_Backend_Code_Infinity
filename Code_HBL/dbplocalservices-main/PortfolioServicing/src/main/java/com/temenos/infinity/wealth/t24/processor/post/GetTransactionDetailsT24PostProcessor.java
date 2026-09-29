/**
 * 
 */
package com.temenos.infinity.wealth.t24.processor.post;

import java.util.List;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.dbputilities.util.CommonUtils;
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
 * @author himaja.sridhar
 *
 */
public class GetTransactionDetailsT24PostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response) throws Exception {
		try {
			
			diagnostic.prepareDebug("==========> GetTransactionDetailsT24PostProcessor T24 - Entered ").log();
			JSONObject responseJSON = new JSONObject();
			JSONArray emptyArr = new JSONArray();
			String portfolioId = "", startDate = "", endDate = "", search = "", sortBy = "", sortOrder = "",
					pageSize = "", pageOffset = "", navPage = "";
			int totalCount = 0, pageSizeValue = 0, pageOffsetValue = 0;

			Record headerRecord = result.getRecordById("header");
			String status = headerRecord.getParamValueByName("status");

			if (status != null && status.trim().equalsIgnoreCase("success")) {

				diagnostic.prepareDebug("==========> GetTransactionDetailsT24PostProcessor T24 - Status Success ").log();
				Dataset bodyDataset = result.getDatasetById("body");

				List<Record> records = bodyDataset.getAllRecords();
				JSONArray transactionListJSONArray = new JSONArray();
				diagnostic.prepareDebug("==========> GetTransactionDetailsT24PostProcessor T24 - No. of records returned initially: "+ records.size()).log();
				for (int j = 0; j < records.size(); j++) {
					JSONObject transactionListJSONObject = new JSONObject();
					Record record = records.get(j);
					transactionListJSONObject = CommonUtils.convertRecordToJSONObject(record);
					transactionListJSONArray.put(transactionListJSONObject);
				}

				String[] responseFields = new String[] { TemenosConstants.TRANSACTIONID, TemenosConstants.DESCRIPTION,
						"ISIN", TemenosConstants.INSTRUMENTID, TemenosConstants.HOLDINGS_TYPE,
						TemenosConstants.QUANTITY, TemenosConstants.TRADEDATE, TemenosConstants.VALUEDATE,
						TemenosConstants.ORDERTYPE, TemenosConstants.LIMITPRICE, TemenosConstants.NETAMOUNT,
						TemenosConstants.EXCHANGERATE, TemenosConstants.INSTRUMENTAMOUNT, TemenosConstants.FEES,
						TemenosConstants.TOTAL, TemenosConstants.TRADECURRENCY, TemenosConstants.INSTRUMENTCURRENCY,
						TemenosConstants.CUSTOMERID, TemenosConstants.REFERENCECURRENCY, TemenosConstants.ISINEXCHANGE,
						TemenosConstants.PRICE, TemenosConstants.STOCKEXCHANGE };

				JSONArray responseArray = new JSONArray();
				for (int i = 0; i < transactionListJSONArray.length(); i++) {
					JSONObject responseObject = transactionListJSONArray.getJSONObject(i);
					if (responseObject.has(TemenosConstants.TRANSACTIONID)
							&& responseObject.get(TemenosConstants.TRANSACTIONID).toString() != null
							&& responseObject.get(TemenosConstants.TRANSACTIONID).toString().length() > 0) {
						for (String field : responseFields) {
							if (!(responseObject.has(field))) {
								responseObject.put(field, "");
							}
							if (field.equalsIgnoreCase(TemenosConstants.ORDERTYPE)) {
								if (responseObject.has(TemenosConstants.APPLICATION)
										&& responseObject.get(TemenosConstants.APPLICATION) != null
										&& responseObject.get(TemenosConstants.APPLICATION).toString()
												.equalsIgnoreCase("DX.TRADE")) {
									responseObject.put(field,
											(responseObject.has(TemenosConstants.TRANSACTIONTYPE)
													&& responseObject.get(TemenosConstants.TRANSACTIONTYPE) != null)
															? responseObject.get(TemenosConstants.TRANSACTIONTYPE)
															: "");
								} else {
									responseObject.put(field, responseObject.get(TemenosConstants.ORDERTYPE));
								}
							}
							if (field.equalsIgnoreCase(TemenosConstants.LIMITPRICE)) {
								responseObject.put(field, (responseObject.get(TemenosConstants.LIMITPRICE) != null
										&& responseObject.get(TemenosConstants.LIMITPRICE).toString().length() > 0)
												? responseObject.get(TemenosConstants.LIMITPRICE).toString()
														.replace(",", "").trim()
												: "0");
							}
							if (field.equalsIgnoreCase(TemenosConstants.PRICE)) {
								responseObject.put(field,
										(responseObject.get(TemenosConstants.PRICE) != null
												&& responseObject.get(TemenosConstants.PRICE).toString().length() > 0)
														? responseObject.get(TemenosConstants.PRICE).toString()
																.replace(",", "").trim()
														: "0");
							}
							if (field.equalsIgnoreCase(TemenosConstants.FEES)) {
								responseObject.put(field, Math
										.abs(Double.parseDouble(responseObject.get(TemenosConstants.FEES).toString())));
							}
							if (field.equalsIgnoreCase(TemenosConstants.ISINEXCHANGE)) {
								responseObject
										.put(field,
												((!responseObject.get("ISIN").toString().equalsIgnoreCase(""))
														? ((!responseObject.get(TemenosConstants.HOLDINGS_TYPE)
																.toString().equalsIgnoreCase(""))
																		? (responseObject.get("ISIN").toString() + " | "
																				+ responseObject.get(
																						TemenosConstants.HOLDINGS_TYPE)
																						.toString())
																		: responseObject.get("ISIN").toString())
														: ((!responseObject.get(TemenosConstants.HOLDINGS_TYPE)
																.toString().equalsIgnoreCase(""))
																		? responseObject.get(
																				TemenosConstants.HOLDINGS_TYPE)
																		: "")));
							}
							responseObject.remove(TemenosConstants.APPLICATION);
						}
						responseObject.put("quantityWithoutComma",
								(responseObject.get(TemenosConstants.QUANTITY).toString()).replace(",", ""));
						responseArray.put(responseObject);
					}
				}

				JSONArray sortedJSON = new JSONArray();
				diagnostic.prepareDebug("==========> GetTransactionDetailsT24PostProcessor T24 - No. of Transactions returned: " + responseArray.length() ).log();
				portfolioId = request.getParameter(TemenosConstants.PORTFOLIOID);
				navPage = request.getParameter(TemenosConstants.NAVPAGE);

				if ((request.getParameter(TemenosConstants.STARTDATE) != null
						&& request.getParameter(TemenosConstants.STARTDATE).length() > 0)
						&& (request.getParameter(TemenosConstants.ENDDATE) != null
								&& request.getParameter(TemenosConstants.ENDDATE).length() > 0)) {
					startDate = request.getParameter(TemenosConstants.STARTDATE);
					endDate = request.getParameter(TemenosConstants.ENDDATE);
					sortedJSON = PortfolioServiceUtils.filterTransactionsDate(responseArray, startDate, endDate);
					diagnostic.prepareDebug("==========> GetTransactionDetailsT24PostProcessor T24 - No. of Transactions returned after filter: " + sortedJSON.length() ).log();
				} else {
					// For recent activities we need the last five regardless the period
					sortedJSON = responseArray;
					diagnostic.prepareDebug("==========> GetTransactionDetailsT24PostProcessor T24 - No filter").log();
				}

				if (request.getParameter(TemenosConstants.SORTBY) != null
						&& request.getParameter(TemenosConstants.SORTBY).length() > 0) {
					sortBy = request.getParameter(TemenosConstants.SORTBY);
					if (request.getParameter(TemenosConstants.SORTORDER) != null
							&& request.getParameter(TemenosConstants.SORTORDER).length() > 0) {
						sortOrder = request.getParameter(TemenosConstants.SORTORDER);
					}
					sortedJSON = PortfolioServiceUtils.sortTransactionsArray(sortedJSON, sortBy, sortOrder);
					diagnostic.prepareDebug("==========> GetTransactionDetailsT24PostProcessor T24 - No. of Transactions returned after sort: " + sortedJSON.length() ).log();
				}

				if (request.getParameter(TemenosConstants.SEARCHBYINSTRUMENTNAME) != null
						&& request.getParameter(TemenosConstants.SEARCHBYINSTRUMENTNAME).length() > 0) {
					search = request.getParameter(TemenosConstants.SEARCHBYINSTRUMENTNAME);
					sortedJSON = PortfolioServiceUtils.returnTransactionsSearch(sortedJSON, search);
					diagnostic.prepareDebug("==========> GetTransactionDetailsT24PostProcessor T24 - No. of Transactions returned after search: " + sortedJSON.length() ).log();
				}

				totalCount = sortedJSON.length();

				if (request.getParameter(TemenosConstants.PAGESIZE) != null
						&& request.getParameter(TemenosConstants.PAGESIZE).length() > 0) {
					pageSize = request.getParameter(TemenosConstants.PAGESIZE);
					pageSizeValue = (pageSize != null && pageSize.trim().length() > 0) ? Integer.parseInt(pageSize) : 0;
				}
				if (request.getParameter(TemenosConstants.PAGEOFFSET) != null
						&& request.getParameter(TemenosConstants.PAGEOFFSET).length() > 0) {
					pageOffset = request.getParameter(TemenosConstants.PAGEOFFSET);
					pageOffsetValue = (pageOffset != null && pageOffset.trim().length() > 0)
							? Integer.parseInt(pageOffset)
							: 0;
				}

				if (pageSizeValue > 0 && pageOffsetValue >= 0) {
					sortedJSON = PortfolioWealthUtils.pagination(sortedJSON, pageSizeValue, pageOffsetValue);
					diagnostic.prepareDebug("==========> GetTransactionDetailsT24PostProcessor T24 - No. of Transactions returned after pagination: " + sortedJSON.length() ).log();
				}

				responseJSON.put("portfolioTransactions", sortedJSON);
				responseJSON.put("portfolioID", portfolioId);
				responseJSON.put(TemenosConstants.STARTDATE, startDate);
				responseJSON.put(TemenosConstants.ENDDATE, endDate);
				responseJSON.put(TemenosConstants.SORTBY, sortBy);
				responseJSON.put(TemenosConstants.SORTORDER, sortOrder);
				responseJSON.put(TemenosConstants.SEARCHBYINSTRUMENTNAME, search);
				responseJSON.put(TemenosConstants.PAGESIZE, pageSizeValue);
				responseJSON.put(TemenosConstants.PAGEOFFSET, pageOffsetValue);
				responseJSON.put(TemenosConstants.NAVPAGE, navPage);
				responseJSON.put(TemenosConstants.TOTAL_COUNT, totalCount);

				result = Utilities.constructResultFromJSONObject(responseJSON);
				result.addOpstatusParam("0");
				result.addHttpStatusCodeParam("200");
				result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);

				return result;

			} else {
				Record errorRecord = result.getRecordById("error");
				alert.prepareError("==========> GetTransactionDetailsT24PostProcessor T24 - Error from API").log();
				//responseJSON.put("errormessage", errorRecord.getParamValueByName("message"));
				//responseJSON.put("errorcode", errorRecord.getParamValueByName("code"));
				String code = errorRecord.getParamValueByName("code");
				if (code.equalsIgnoreCase("TGVCP-007")) {			
					responseJSON.put("portfolioTransactions", emptyArr);
					Result portfolioRes = Utilities.constructResultFromJSONObject(responseJSON);
					portfolioRes.addOpstatusParam("0");
					portfolioRes.addHttpStatusCodeParam("200");
					portfolioRes.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
					return portfolioRes;
				} else {
					responseJSON.put("errormessage", errorRecord.getParamValueByName("message"));
					responseJSON.put("errorcode", errorRecord.getParamValueByName("code"));
					Result errorResponse = Utilities.constructResultFromJSONObject(responseJSON);
					return errorResponse;
				}
				
			}

		} catch (Exception e) {
			alert.prepareError("==========> GetTransactionDetailsT24PostProcessor T24 - Error: " + e.getMessage()).log();
		}
		return response;
	}

}
