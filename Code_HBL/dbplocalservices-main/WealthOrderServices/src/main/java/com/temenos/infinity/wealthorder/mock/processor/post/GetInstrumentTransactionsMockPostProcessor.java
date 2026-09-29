/**
 * 
 */
package com.temenos.infinity.wealthorder.mock.processor.post;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;
import com.temenos.infinity.wealthorder.common.util.OrderServiceUtils;

/**
 * @author himaja.sridhar
 *
 */
public class GetInstrumentTransactionsMockPostProcessor implements DataPostProcessor2 {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		diagnostic.prepareDebug("==========> GetInstrumentTransactionsMockPostProcessor Mock - Entered").log();
		String portfolioId = (String) request.getParameter(TemenosConstants.PORTFOLIOID);
		String startDate = (String) request.getParameter(TemenosConstants.STARTDATE);
		String sortBy = (String) request.getParameter(TemenosConstants.SORTBY);
		String searchVal = (String) request.getParameter(TemenosConstants.INSTRUMENTID);
		String endDate = (String) request.getParameter(TemenosConstants.ENDDATE);
		JSONObject responseVal = new JSONObject();
		JSONArray transactionsArr = new JSONArray();
		String[] transactionId = null, description = null, ISIN = null, exchange = null, quantity = null,
				exchangeRate = null, orderType = null, RICCode = null, limitPrice = null, tradeDate = null, fees = null,
				valueDate = null, instrumentCurrency = null, netAmount = null, instrumentAmount = null, total = null,
				instrumentId = null, referenceCurrency = null, feesCurrency = null;
		String sortType = (String) request.getParameter(TemenosConstants.SORTORDER);
		int totalCount = 0;
		String limitVal = (String) request.getParameter(TemenosConstants.PAGESIZE);
		String offsetVal = (String) request.getParameter(TemenosConstants.PAGEOFFSET);
		int limit = (limitVal != null && limitVal.trim().length() > 0) ? Integer.parseInt(limitVal) : 0;
		int offset = (offsetVal != null && offsetVal.trim().length() > 0) ? Integer.parseInt(offsetVal) : 0;
		String search = (searchVal != null && searchVal.trim().length() > 0) ? searchVal : "";
		if (portfolioId.equalsIgnoreCase("100777-1")) {
			transactionId = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13" };

			description = new String[] { "Alphabet", "Amazon.com Inc", "Apple", "LVMH", "Alphabet", "Coca-Cola Co",
					"American Express Company", "Boeing Co", "Bank of America Corp", "Citigroup Inc",
					"General Motors Company", "Pfizer Inc", "iShares Core S&P 500 UCITS ETF" };

			quantity = new String[] { "5", "4", "23", "5", "3", "10", "15", "10", "12", "15", "8", "10", "15" };

			/*
			 * instrumentId = new String[] { "100027-000", "100021-000", "100050-000",
			 * "100051-000", "100027-000", "100016-000", "100020-000", "100086-000",
			 * "100017-000", "100018-000", "100019-000", "100022-000", "100093-000" };
			 */

			fees = new String[] { "8.11", "10.39", "2.57", "2.46", "5.10", "0.45", "1.58", "1.90", "0.40", "0.83",
					"0.39", "0.34", "4.54" };

			exchangeRate = new String[] { "1", "1", "1", "1.21", "1", "1", "1", "1", "1", "1", "1", "1", "1.21" };

			limitPrice = new String[] { "1621.59", "2598.44", "111.59", "492.38", "1700.50", "45", "105", "190", "33",
					"55", "49", "34", "302.72" };

			orderType = new String[] { "Buy Limit", "Buy Limit", "Buy Limit", "Buy Limit", "Sell Limit", "Buy Limit",
					"Buy Limit", "Buy Limit", "Buy Limit", "Buy Limit", "Buy Limit", "Buy Limit", "Buy Limit" };

			ISIN = new String[] { "US02079K1079", "US0231351067", "US0378331005", "FR0000121014", "US02079K1079",
					"US1912161007", "US0258161092", "US0970231058", "US0605051046", "US1729671016", "US37045V1008",
					"INE182A01018", "IE00B5BMR087" };

			exchange = new String[] { "NYSE", "NYSE", "NYSE", "Euronext Paris", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE",
					"NYSE", "NYSE", "NYSE", "GER" };

			RICCode = new String[] { "GOOGL.OQ", "AMZN.OQ", "AAPL.OQ", "LVMH.PA", "GOOGL.OQ", "KO.N", "AXP.N", "BA.NQ",
					"BAC.N", "C.O", "GM.N", "PFE.N", "IXMO461.DE" };

			instrumentCurrency = new String[] { "USD", "USD", "USD", "EUR", "USD", "USD", "USD", "USD", "USD", "USD",
					"USD", "USD", "USD" };

			referenceCurrency = new String[] { "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD",
					"USD", "USD", "USD" };
			instrumentId = new String[] { "100077-000", "100021-000", "100050-000", "100051-000", "100077-000",
					"100016-000", "100020-000", "100086-000", "100017-000", "100018-000", "100019-000", "100022-000",
					"100093-000" };
			
			feesCurrency = new String[] { "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD",
					"USD", "USD", "USD" };

			instrumentAmount = OrderServiceUtils.netAmountCalculation(limitPrice, quantity);
			total = OrderServiceUtils.totalAmountCalculation(instrumentAmount, fees, orderType);
			netAmount = OrderServiceUtils.instrumentAmountCalculation(total, exchangeRate);

			tradeDate = OrderServiceUtils
					.mockTradeDateViewInstrumentTransaction(transactionId.length);
			valueDate = OrderServiceUtils
					.mockTradeDateViewInstrumentTransaction(transactionId.length);
		} else if (portfolioId.equalsIgnoreCase("100777-2")) {
			transactionId = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13" };

			description = new String[] { "Alphabet", "Amazon.com Inc", "Apple", "Walmart", "Alphabet", "Walmart",
					"Coca-Cola Co", "American Express Company", "Boeing Co", "Bank of America Corp", "Citigroup Inc",
					"General Motors Company", "Pfizer Inc" };

			quantity = new String[] { "5", "2", "7", "8", "3", "2", "6", "10", "8", "9", "12", "6", "9" };

			fees = new String[] { "8.11", "5.20", "0.78", "1.12", "5.10", "0.29", "0.25", "1.06", "1.47", "0.24",
					"0.73", "0.30", "0.28" };

			exchangeRate = new String[] { "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1" };

			limitPrice = new String[] { "1621.59", "2598.44", "111.59", "140.00", "1700.50", "146", "42", "106", "184",
					"27", "61", "50", "32" };

			orderType = new String[] { "Buy Limit", "Buy Limit", "Buy Limit", "Buy Limit", "Sell Limit", "Sell Limit",
					"Buy Limit", "Buy Limit", "Buy Limit", "Buy Limit", "Buy Limit", "Buy Limit", "Buy Limit" };

			ISIN = new String[] { "US02079K1079", "US0231351067", "US0378331005", "US0378331005", "US02079K1079",
					"US0378331005", "US1912161007", "US0258161092", "US0970231058", "US0605051046", "US1729671016",
					"US37045V1008", "INE182A01018" };

			exchange = new String[] { "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE",
					"NYSE", "NYSE", "NYSE" };

			RICCode = new String[] { "GOOGL.OQ", "AMZN.OQ", "AAPL.OQ", "WMT.N", "GOOGL.OQ", "WMT.N", "KO.N", "BA.NQ",
					"BAC.N", "C.O", "GM.N", "PFE.N", "IXMO461.DE" };

			instrumentCurrency = new String[] { "USD", "USD", "USD", "EUR", "USD", "USD", "USD", "USD", "USD", "USD",
					"USD", "USD", "USD" };

			referenceCurrency = new String[] { "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD",
					"USD", "USD", "USD" };
			instrumentId = new String[] { "100077-000", "100021-000", "100050-000", "100014-000", "100077-000",
					"100014-000", "100016-000", "100020-000", "100086-000", "100017-000", "100018-000", "100019-000",
					"100022-000" };
			
			feesCurrency = new String[] { "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD",
					"USD", "USD", "USD" };

			instrumentAmount = OrderServiceUtils.netAmountCalculation(limitPrice, quantity);
			total = OrderServiceUtils.totalAmountCalculation(instrumentAmount, fees, orderType);
			netAmount = OrderServiceUtils.instrumentAmountCalculation(total, exchangeRate);

			tradeDate = OrderServiceUtils
					.mockTradeDateViewInstrumentTransaction(transactionId.length);
			valueDate = OrderServiceUtils
					.mockTradeDateViewInstrumentTransaction(transactionId.length);
		} else if (portfolioId.equalsIgnoreCase("100777-3")) {
			transactionId = new String[] { "1", "2", "3", "4", "5", "6", "7", "8" };
			description = new String[] { "Google LLC", "Amazon.com Inc", "Apple", "LVMH", "Google LLC", "Coca-Cola Co",
					"American Express Company", "Boeing Co" };
			ISIN = new String[] { "US02079K1079", "US0231351067", "US0378331005", "FR0000121014", "US02079K1079",
					"US1912161007", "US0258161092", "US0970231058" };
			exchange = new String[] { "NYSE", "NYSE", "NYSE", "PAR", "NYSE", "NYSE", "NYSE", "NYSE" };
			orderType = new String[] { "Buy Limit", "Buy Limit", "Buy Limit", "Buy Limit", "Buy Limit", "Buy Limit",
					"Buy Limit", "Buy Limit" };
			RICCode = new String[] { "GOOGL.O", "AMZN.O", "AAPL.OQ", "LVMH.PA", "GOOGL.O", "KO.N", "AXP.N", "BA.NQ" };
			instrumentCurrency = new String[] { "USD", "USD", "USD", "EUR", "USD", "USD", "USD", "USD" };
			quantity = new String[] { "8", "5", "10", "5", "7", "8", "9", "10" };
			limitPrice = new String[] { "1621.59", "2598.44", "111.59", "492.38", "1700.50", "45.00", "105.00",
					"190.00" };

			referenceCurrency = new String[] { "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD" };
			feesCurrency = new String[] { "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD" };
			/*
			 * instrumentAmount = new String[] { "12972.72", "12992.20", "1155.90",
			 * "2461.90", "11930.50", "360.00", "945.00", "1900.00" };
			 */
			fees = new String[] { "12.97", "12.99", "1.12", "2.46", "11.90", "0.36", "0.95", "1.90" };
			/*
			 * total = new String[] { "12985.69", "13005.19", "1117.02", "2464.36",
			 * "11915.40", "360.36", "945.95", "1901.90" };
			 */
			exchangeRate = new String[] { "1", "1", "1", "1.21", "1", "1", "1", "1" };
			/*
			 * netAmount = new String[] { "12985.69", "13005.19", "117.02", "2981.88",
			 * "11915.40", "360.36", "945.95", "1901.90" };
			 */
			instrumentId = new String[] { "100077-000", "100021-000", "100050-000", "100051-000", "100077-000",
					"100016-000", "100020-000", "100086-000", "100017-000", "100018-000", "100019-000", "100022-000",
					"100093-000" };
			netAmount = OrderServiceUtils.netAmountCalculation(limitPrice, quantity);
			instrumentAmount = OrderServiceUtils.instrumentAmountCalculation(netAmount, exchangeRate);
			total = OrderServiceUtils.totalAmountCalculation(instrumentAmount, fees, orderType);
			tradeDate = OrderServiceUtils
					.mockTradeDateViewInstrumentTransaction(transactionId.length);
			valueDate = OrderServiceUtils
					.mockTradeDateViewInstrumentTransaction(transactionId.length);
		} else if (portfolioId.equalsIgnoreCase("100777-4")) {
			transactionId = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13" };

			description = new String[] { "Alphabet", "Amazon.com Inc", "Apple", "LVMH", "Alphabet", "Coca-Cola Co",
					"American Express Company", "Boeing Co", "Bank of America Corp", "Citigroup Inc",
					"General Motors Company", "Pfizer Inc", "iShares Core S&P 500 UCITS ETF" };

			quantity = new String[] { "5", "4", "23", "5", "3", "10", "15", "10", "12", "15", "8", "10", "15" };

			fees = new String[] { "8.11", "10.39", "2.57", "2.46", "5.10", "0.45", "1.58", "1.90", "0.40", "0.83",
					"0.39", "0.34", "4.54" };

			exchangeRate = new String[] { "1", "1", "1", "1.21", "1", "1", "1", "1", "1", "1", "1", "1", "1.21" };

			limitPrice = new String[] { "1621.59", "2598.44", "111.59", "492.38", "1700.50", "45", "105", "190", "33",
					"55", "49", "34", "302.72" };

			orderType = new String[] { "Buy Limit", "Buy Limit", "Buy Limit", "Buy Limit", "Sell Limit", "Buy Limit",
					"Buy Limit", "Buy Limit", "Buy Limit", "Buy Limit", "Buy Limit", "Buy Limit", "Buy Limit" };

			ISIN = new String[] { "US02079K1079", "US0231351067", "US0378331005", "FR0000121014", "US02079K1079",
					"US1912161007", "US0258161092", "US0970231058", "US0605051046", "US1729671016", "US37045V1008",
					"INE182A01018", "IE00B5BMR087" };

			exchange = new String[] { "NYSE", "NYSE", "NYSE", "Euronext Paris", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE",
					"NYSE", "NYSE", "NYSE", "NYSE" };

			RICCode = new String[] { "GOOGL.OQ", "AMZN.OQ", "AAPL.OQ", "LVMH.PA", "GOOGL.OQ", "KO.N", "AXP.N", "BA.NQ",
					"BAC.N", "C.O", "GM.N", "PFE.N", "IXMO461.DE" };

			instrumentCurrency = new String[] { "USD", "USD", "USD", "EUR", "USD", "USD", "USD", "USD", "USD", "USD",
					"USD", "USD", "USD" };

			referenceCurrency = new String[] { "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD",
					"USD", "USD", "USD" };
			instrumentId = new String[] { "100077-000", "100021-000", "100050-000", "100051-000", "100077-000",
					"100016-000", "100020-000", "100086-000", "100017-000", "100018-000", "100019-000", "100022-000",
					"100093-000" };
			feesCurrency = new String[] { "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD",
					"USD", "USD", "USD" };
			instrumentAmount = OrderServiceUtils.netAmountCalculation(limitPrice, quantity);
			total = OrderServiceUtils.totalAmountCalculation(instrumentAmount, fees, orderType);
			netAmount = OrderServiceUtils.instrumentAmountCalculation(total, exchangeRate);
			tradeDate = OrderServiceUtils
					.mockTradeDateViewInstrumentTransaction(transactionId.length);
			valueDate = OrderServiceUtils
					.mockTradeDateViewInstrumentTransaction(transactionId.length);
		} else if (portfolioId.equalsIgnoreCase("100777-5")) {
			transactionId = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13" };

			description = new String[] { "Alphabet", "Amazon.com Inc", "Apple", "Walmart", "Alphabet", "Walmart",
					"Coca-Cola Co", "American Express Company", "Boeing Co", "Bank of America Corp", "Citigroup Inc",
					"General Motors Company", "Pfizer Inc" };

			quantity = new String[] { "5", "2", "7", "8", "3", "2", "6", "10", "8", "9", "12", "6", "9" };

			fees = new String[] { "8.11", "5.20", "0.78", "1.12", "5.10", "0.29", "0.25", "1.06", "1.47", "0.24",
					"0.73", "0.30", "0.28" };

			exchangeRate = new String[] { "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1" };

			limitPrice = new String[] { "1621.59", "2598.44", "111.59", "140.00", "1700.50", "146", "42", "106", "184",
					"27", "61", "50", "32" };

			orderType = new String[] { "Buy Limit", "Buy Limit", "Buy Limit", "Buy Limit", "Sell Limit", "Sell Limit",
					"Buy Limit", "Buy Limit", "Buy Limit", "Buy Limit", "Buy Limit", "Buy Limit", "Buy Limit" };

			ISIN = new String[] { "US02079K1079", "US0231351067", "US0378331005", "US0378331005", "US02079K1079",
					"US0378331005", "US1912161007", "US0258161092", "US0970231058", "US0605051046", "US1729671016",
					"US37045V1008", "INE182A01018" };

			exchange = new String[] { "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE",
					"NYSE", "NYSE", "NYSE" };

			RICCode = new String[] { "GOOGL.OQ", "AMZN.OQ", "AAPL.OQ", "LVMH.PA", "GOOGL.OQ", "KO.N", "AXP.N", "BA.NQ",
					"", "BAC.N", "C.O", "GM.N", "PFE.N" };

			referenceCurrency = new String[] { "USD", "USD", "USD", "EUR", "USD", "USD", "USD", "USD", "USD", "USD",
					"USD", "USD", "USD" };
			
			feesCurrency = new String[] { "USD", "USD", "USD", "EUR", "USD", "USD", "USD", "USD", "USD", "USD",
					"USD", "USD", "USD" };
			instrumentCurrency = new String[] { "USD", "USD", "USD", "EUR", "USD", "USD", "USD", "USD", "USD", "USD",
					"USD", "USD", "USD" };

			instrumentId = new String[] { "100077-000", "100021-000", "100050-000", "100014-000", "100077-000",
					"100014-000", "100016-000", "100020-000", "100086-000", "100017-000", "100018-000", "100019-000",
					"100022-000" };

			instrumentAmount = OrderServiceUtils.netAmountCalculation(limitPrice, quantity);
			total = OrderServiceUtils.totalAmountCalculation(instrumentAmount, fees, orderType);
			netAmount = OrderServiceUtils.instrumentAmountCalculation(total, exchangeRate);
			tradeDate = OrderServiceUtils
					.mockTradeDateViewInstrumentTransaction(transactionId.length);
			valueDate = OrderServiceUtils
					.mockTradeDateViewInstrumentTransaction(transactionId.length);
		}
		for (int i = 0; i < description.length; i++) {
			JSONObject transObj = new JSONObject();
			transObj.put(TemenosConstants.TRANSACTIONID, transactionId[i]);
			transObj.put(TemenosConstants.DESCRIPTION, description[i]);
			transObj.put("ISIN", ISIN[i]);
			transObj.put("holdingsType", exchange[i]);
			transObj.put(TemenosConstants.ORDERTYPE, orderType[i]);
			transObj.put(TemenosConstants.RICCODE, RICCode[i]);
			transObj.put(TemenosConstants.QUANTITY, quantity[i]);
			transObj.put(TemenosConstants.LIMITPRICE, limitPrice[i]);
			transObj.put(TemenosConstants.INSTRUMENTAMOUNT, instrumentAmount[i]);
			transObj.put(TemenosConstants.FEES, fees[i]);
			transObj.put("total", total[i]);
			transObj.put(TemenosConstants.EXCHANGERATE, exchangeRate[i]);
			transObj.put(TemenosConstants.NETAMOUNT, netAmount[i]);
			transObj.put(TemenosConstants.TRADEDATE, tradeDate[i]);
			transObj.put(TemenosConstants.VALUEDATE, valueDate[i]);
			transObj.put(TemenosConstants.INSTRUMENTCURRENCY, instrumentCurrency[i]);
			transObj.put(TemenosConstants.REFERENCECURRENCY, referenceCurrency[i]);
			transObj.put("feesCurrency", feesCurrency[i]);
			transObj.put(TemenosConstants.INSTRUMENTID, instrumentId[i]);
			transactionsArr.put(transObj);
		}
		JSONArray sortedJSON = new JSONArray();
		if (startDate.equals("") || endDate.equals("")) {
			sortedJSON = transactionsArr;
		} else {

			sortedJSON = OrderServiceUtils.filterInstrumentTranscationsDate(transactionsArr, startDate, endDate);
		}
		if (sortBy != null) {
			sortedJSON = OrderServiceUtils.sortInstrumentTransactionsArray(sortedJSON, sortBy, sortType);
		} else {
		}
		if (search.equals("")) {

		} else {
			sortedJSON = OrderServiceUtils.searchViewInstrumentTransactions(sortedJSON, search);
		}
		totalCount = sortedJSON.length();

		if (limit > 0 && offset >= 0) {
			sortedJSON = PortfolioWealthUtils.pagination(sortedJSON, limit, offset);
		}
		responseVal.put("portfolioID", portfolioId);
		responseVal.put("portfolioTransactions", sortedJSON);
		responseVal.put(TemenosConstants.STARTDATE, startDate);
		responseVal.put(TemenosConstants.ENDDATE, endDate);
		responseVal.put(TemenosConstants.SORTBY, sortBy);
		responseVal.put(TemenosConstants.REFERENCECURRENCY, "USD");
		responseVal.put("totalCount", totalCount);
		responseVal.put("opstatus", "0");
		responseVal.put("httpStatusCode", "200");
		
		Result final_result = Utilities.constructResultFromJSONObject(responseVal);
		final_result.addOpstatusParam("0");
		final_result.addHttpStatusCodeParam("200");
		final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		diagnostic.prepareDebug("==========> GetInstrumentTransactionsMockPostProcessor Mock - Exiting with success").log();
		return final_result;
	}

}
