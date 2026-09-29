/**
 * 
 */
package com.temenos.infinity.wealthorder.tap.processor.post;

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
import com.temenos.infinity.wealthorder.common.util.OrderServiceUtils;

/**
 * @author himaja.sridhar
 *
 */
public class GetInstrumentTransactionsTAPPostProcessor implements DataPostProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@SuppressWarnings("unused")
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {

			diagnostic.prepareDebug("==========> GetInstrumentTransactionsTAPPostProcessor TAP -  Entered ").log();
			JSONObject responseJSON = new JSONObject();

			String portfolioId = "", startDate = "", endDate = "", search = "", sortBy = "", sortOrder = "",
					pageSize = "", pageOffset = "", navPage = "", instrumentId = "";
			int totalCount = 0, pageSizeValue = 0, pageOffsetValue = 0;

			String refCcy = "";
			String[] responseFields = new String[] { TemenosConstants.TRANSACTIONID, TemenosConstants.DESCRIPTION,
					TemenosConstants.INSTRUMENTID, TemenosConstants.HOLDINGS_TYPE, "ISIN", TemenosConstants.QUANTITY,
					TemenosConstants.TRADEDATE, TemenosConstants.VALUEDATE, TemenosConstants.ORDERTYPE,
					TemenosConstants.LIMITPRICE, TemenosConstants.NETAMOUNT, TemenosConstants.EXCHANGERATE,
					TemenosConstants.INSTRUMENTAMOUNT, TemenosConstants.FEES, TemenosConstants.TOTAL,
					TemenosConstants.TRADECURRENCY, TemenosConstants.INSTRUMENTCURRENCY, TemenosConstants.CUSTOMERID,
					TemenosConstants.REFERENCECURRENCY, "bp_1_pos_amount_m", "bp_2_pos_amount_m", "bp_3_pos_amount_m",
					"bp_4_pos_amount_m", "bp_5_pos_amount_m", "bp_6_pos_amount_m", "bp_7_pos_amount_m",
					"bp_8_pos_amount_m", "bp_9_pos_amount_m", TemenosConstants.ISINEXCHANGE };
			Dataset bodyDataset = result.getDatasetById("body");
			JSONArray sortedJSON = new JSONArray();

			portfolioId = request.getParameter(TemenosConstants.PORTFOLIOID);
			navPage = request.getParameter(TemenosConstants.NAVPAGE);
			instrumentId = request.getParameter(TemenosConstants.INSTRUMENTID);

			if (bodyDataset != null) {
				List<Record> records = bodyDataset.getAllRecords();
				JSONArray transactionListJSONArray = new JSONArray();
				for (int j = 0; j < records.size(); j++) {
					JSONObject transactionListJSONObject = new JSONObject();
					Record record = records.get(j);
					transactionListJSONObject = CommonUtils.convertRecordToJSONObject(record);
					transactionListJSONArray.put(transactionListJSONObject);
				}
				JSONArray responseArray = new JSONArray();
				for (int i = 0; i < transactionListJSONArray.length(); i++) {
					JSONObject responseObject = transactionListJSONArray.getJSONObject(i);
					Double fees = 0.0;
					for (String field : responseFields) {
						if (!(responseObject.has(field))) {
							responseObject.put(field, "");
						}
						if (field.equalsIgnoreCase(TemenosConstants.ISINEXCHANGE)) {
							responseObject.put(field, ((!responseObject.get("ISIN").toString().equalsIgnoreCase(""))
									? ((!responseObject.get(TemenosConstants.HOLDINGS_TYPE).toString()
											.equalsIgnoreCase("")) ? (responseObject.get("ISIN").toString() + " | "
													+ responseObject.get(TemenosConstants.HOLDINGS_TYPE).toString())
													: responseObject.get("ISIN").toString())
									: ((!responseObject.get(TemenosConstants.HOLDINGS_TYPE).toString()
											.equalsIgnoreCase("")) ? responseObject.get(TemenosConstants.HOLDINGS_TYPE)
													: "")));
						}
						if (field.equalsIgnoreCase(TemenosConstants.TRADEDATE)) {

							if (responseObject.get(TemenosConstants.TRADEDATE) != null
									&& responseObject.get(TemenosConstants.TRADEDATE).toString().length() > 0) {
								String respDate = responseObject.get(TemenosConstants.TRADEDATE).toString();
								String[] resultd = respDate.split("T");
								responseObject.put(field, resultd[0] != null ? resultd[0] : "");

							} else {
								responseObject.put(field, "");
							}

						}

						if (field.equalsIgnoreCase(TemenosConstants.VALUEDATE)) {

							if (responseObject.get(TemenosConstants.VALUEDATE) != null
									&& responseObject.get(TemenosConstants.VALUEDATE).toString().length() > 0) {
								String respDate = responseObject.get(TemenosConstants.VALUEDATE).toString();
								String[] resultd = respDate.split("T");
								responseObject.put(field, resultd[0] != null ? resultd[0] : "");

							} else {
								responseObject.put(field, "");
							}

						}	
					}
					refCcy = responseObject.get(TemenosConstants.REFERENCECURRENCY) != null
							? responseObject.get(TemenosConstants.REFERENCECURRENCY).toString()
							: "";
					fees = Double
							.parseDouble(responseObject.has("bp_1_pos_amount_m")
									? responseObject.get("bp_1_pos_amount_m").toString()
									: "0")
							+ Double.parseDouble(responseObject.has("bp_1_pos_amount_m")
									? responseObject.get("bp_1_pos_amount_m").toString()
									: "0")
							+ Double.parseDouble(responseObject.has("bp_3_pos_amount_m")
									? responseObject.get("bp_3_pos_amount_m").toString()
									: "0")
							+ Double.parseDouble(responseObject.has("bp_4_pos_amount_m")
									? responseObject.get("bp_4_pos_amount_m").toString()
									: "0")
							+ Double.parseDouble(responseObject.has("bp_5_pos_amount_m")
									? responseObject.get("bp_5_pos_amount_m").toString()
									: "0")
							+ Double.parseDouble(responseObject.has("bp_6_pos_amount_m")
									? responseObject.get("bp_6_pos_amount_m").toString()
									: "0")
							+ Double.parseDouble(responseObject.has("bp_7_pos_amount_m")
									? responseObject.get("bp_7_pos_amount_m").toString()
									: "0")
							+ Double.parseDouble(responseObject.has("bp_8_pos_amount_m")
									? responseObject.get("bp_8_pos_amount_m").toString()
									: "0")
							+ Double.parseDouble(responseObject.has("bp_9_pos_amount_m")
									? responseObject.get("bp_9_pos_amount_m").toString()
									: "0");
					responseObject.put(TemenosConstants.FEES, fees);
					responseObject.remove("bp_1_pos_amount_m");
					responseObject.remove("bp_2_pos_amount_m");
					responseObject.remove("bp_3_pos_amount_m");
					responseObject.remove("bp_4_pos_amount_m");
					responseObject.remove("bp_5_pos_amount_m");
					responseObject.remove("bp_6_pos_amount_m");
					responseObject.remove("bp_7_pos_amount_m");
					responseObject.remove("bp_8_pos_amount_m");
					responseObject.remove("bp_9_pos_amount_m");
					responseArray.put(responseObject);
				}

				if ((request.getParameter(TemenosConstants.STARTDATE) != null
						&& request.getParameter(TemenosConstants.STARTDATE).length() > 0)
						&& (request.getParameter(TemenosConstants.ENDDATE) != null
								&& request.getParameter(TemenosConstants.ENDDATE).length() > 0)) {
					startDate = request.getParameter(TemenosConstants.STARTDATE);
					endDate = request.getParameter(TemenosConstants.ENDDATE);
					sortedJSON = OrderServiceUtils.filterInstrumentTranscationsDate(responseArray, startDate, endDate);
				} else {
					sortedJSON = responseArray;
				}

				if (request.getParameter(TemenosConstants.SORTBY) != null
						&& request.getParameter(TemenosConstants.SORTBY).length() > 0
						&& request.getParameter(TemenosConstants.SORTBY).toString().equals("fees")) {
					sortBy = request.getParameter(TemenosConstants.SORTBY);
					if (request.getParameter(TemenosConstants.SORTORDER) != null
							&& request.getParameter(TemenosConstants.SORTORDER).length() > 0) {
						sortOrder = request.getParameter(TemenosConstants.SORTORDER);
					}
					sortedJSON = OrderServiceUtils.sortInstrumentTransactionsArray(sortedJSON, sortBy, sortOrder);
				}

				if (request.getParameter(TemenosConstants.INSTRUMENTID) != null
						&& request.getParameter(TemenosConstants.INSTRUMENTID).length() > 0) {
					sortedJSON = OrderServiceUtils.searchViewInstrumentTransactions(sortedJSON,
							instrumentId);
				}

				if (request.getParameter(TemenosConstants.SEARCHBYINSTRUMENTNAME) != null
						&& request.getParameter(TemenosConstants.SEARCHBYINSTRUMENTNAME).length() > 0) {
					search = request.getParameter(TemenosConstants.SEARCHBYINSTRUMENTNAME);
					sortedJSON = OrderServiceUtils.returnInstrumentTransactionsSearch(sortedJSON, search);
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
				}
			} else {
				sortedJSON = new JSONArray();
			}
			responseJSON.put("portfolioTransactions", sortedJSON);
			responseJSON.put("portfolioID", portfolioId);
			// responseJSON.put("referenceCurrency", refCcy);
			responseJSON.put(TemenosConstants.STARTDATE, startDate);
			responseJSON.put(TemenosConstants.ENDDATE, endDate);
			responseJSON.put(TemenosConstants.SORTBY, request.getParameter(TemenosConstants.SORTBY));
			responseJSON.put(TemenosConstants.SORTORDER, request.getParameter(TemenosConstants.SORTORDER));
			responseJSON.put(TemenosConstants.SEARCHBYINSTRUMENTNAME, search);
			responseJSON.put(TemenosConstants.PAGESIZE, pageSizeValue);
			responseJSON.put(TemenosConstants.PAGEOFFSET, pageOffsetValue);
			responseJSON.put(TemenosConstants.NAVPAGE, navPage);
			responseJSON.put(TemenosConstants.TOTAL_COUNT, totalCount);

			result = Utilities.constructResultFromJSONObject(responseJSON);
			result.addOpstatusParam("0");
			result.addHttpStatusCodeParam("200");
			result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			diagnostic.prepareDebug("==========> GetInstrumentTransactionsTAPPostProcessor TAP -  Exiting with success ").log();
			return result;
		} catch (Exception e) {
			alert.prepareError("==========> GetInstrumentTransactionsTAPPostProcessor TAP - Error: " + e.getMessage()).log();
			return result;
		}
	}

}
