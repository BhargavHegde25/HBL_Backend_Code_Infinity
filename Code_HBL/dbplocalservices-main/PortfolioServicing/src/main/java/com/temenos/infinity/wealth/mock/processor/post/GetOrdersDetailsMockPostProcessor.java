/**
 * 
 */
package com.temenos.infinity.wealth.mock.processor.post;

import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;
import com.temenos.infinity.wealth.common.util.PortfolioServiceUtils;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

/**
 * @author himaja.sridhar
 *
 */
public class GetOrdersDetailsMockPostProcessor implements DataPostProcessor2 {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		diagnostic.prepareDebug("==========> GetOrdersDetailsMockPostProcessor Mock - Entered ").log();
		String portfolioId = (String) request.getParameter(TemenosConstants.PORTFOLIOID);
		String startDate = (String) request.getParameter(TemenosConstants.STARTDATE);
		String search = (String) request.getParameter(TemenosConstants.SEARCHBYINSTRUMENTNAME);
		String endDate = (String) request.getParameter(TemenosConstants.ENDDATE);
		String type = (String) request.getParameter(TemenosConstants.ORDERSVIEW_TYPE);
		String cancelOrderID = (String) request.getParameter(TemenosConstants.ORDER_ID);
		String sortBy = (String) request.getParameter(TemenosConstants.SORTBY);
		String sortType = (String) request.getParameter(TemenosConstants.SORTORDER);
		String limitVal = (String) request.getParameter(TemenosConstants.PAGESIZE);
		String offsetVal = (String) request.getParameter(TemenosConstants.PAGEOFFSET);
		int totalCount = 0;

		int limit = (limitVal != null && limitVal.trim().length() > 0) ? Integer.parseInt(limitVal) : 0;
		int offset = (offsetVal != null && offsetVal.trim().length() > 0) ? Integer.parseInt(offsetVal) : 0;

		JSONObject responseVal = new JSONObject();
		JSONArray transactionsArr = new JSONArray();

		String[] orderId = null, description = null, ISIN = null, exchange = null, quantity = null, exchangeRate = null,
				orderType = null, status = null, total = null, RICCode = null, limitPrice = null, price = null,
				tradeDate = null, netAmount = null, fees = null, valueDate = null, instrumentCurrency = null,
				instrumentAmount = null, orderMode = null, orderModeType = null, stopPrice = null, validity = null,
				orderReference = null, instrumentId = null;

		if (portfolioId.equalsIgnoreCase("100777-1")) {
			orderId = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15",
					"16", "17", "18", "19" };

			description = new String[] { "Alphabet", "Amazon.com Inc", "Apple", "Walmart Inc", "Coca-Cola Co",
					"Pfizer Inc", "Boeing Co", "Bank of America Corp", "Alphabet", "Amazon.com Inc", "Apple",
					"Walmart Inc", "Coca-Cola Co", "American Express Company", "Boeing Co", "Bank of America Corp",
					"Citigroup Inc", "General Motors Company", "Pfizer Inc" };

			instrumentId = new String[] { "100077-000", "100021-000", "100050-000", "100014-000", "100016-000",
					"100022-000", "100086-000", "100017-000", "100077-000", "100021-000", "100050-000", "100014-000",
					"100016-000", "100020-000", "100086-000", "100017-000", "100018-000", "100019-000", "100022-000" };

			RICCode = new String[] { "GOOGL.O", "AMZN.O", "AAPL.OQ", "WALMART", "COKE.O", "PFE.O", "BA.O", "BAC.O",
					"GOOGL.O", "AMZN.O", "AAPL.OQ", "WALMART", "COKE.O", "AXP.O", "BA.O", "BAC.O", "C.O", "GM.N",
					"PFE.O" };

			ISIN = new String[] { "US02079K1079", "US0231351067", "US0378331005", "US9311421039", "US1912161007",
					"INE182A01018", "US0970231058", "US0605051046", "US02079K1079", "US0231351067", "US0378331005",
					"US9311421039", "US1912161007", "US0258161092", "US0970231058", "US0605051046", "US1729671016",
					"US37045V1008", "INE182A01018" };

			exchange = new String[] { "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE",
					"NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE" };

			orderType = new String[] { "Buy Limit", "Sell Limit", "Buy Market", "Sell Market", "Buy Stop Loss",
					"Sell Stop Loss", "Sell Stop Limit", "Buy Stop Limit", "Buy Limit", "Buy Market", "Sell Limit",
					"Sell Market", "Buy Stop Loss", "Buy Limit", "Sell Stop Limit", "Sell Market", "Buy Stop Limit",
					"Buy Limit", "Sell Stop Loss" };

			orderMode = new String[] { "Buy", "Sell", "Buy", "Sell", "Buy", "Sell", "Sell", "Buy", "Buy", "Buy", "Sell",
					"Sell", "Buy", "Buy", "Sell", "Sell", "Buy", "Buy", "Sell" };

			orderModeType = new String[] { "Limit", "Limit", "Market", "Market", "Stop Loss", "Stop Loss", "Stop Limit",
					"Stop Limit", "Limit", "Market", "Limit", "Market", "Stop Loss", "Limit", "Stop Limit", "Market",
					"Stop Limit", "Limit", "Stop Loss" };

			validity = new String[] { "Day Order", "Good Till Canceled", "Day Order", "Good Till Canceled", "Day Order",
					"Good Till Canceled", "Day Order", "Good Till Canceled", "Day Order", "Good Till Canceled",
					"Day Order", "Good Till Canceled", "Day Order", "Good Till Canceled", "Day Order",
					"Good Till Canceled", "Day Order", "Good Till Canceled", "Good Till Canceled" };

			instrumentCurrency = new String[] { "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD",
					"USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD" };

			quantity = new String[] { "2", "2", "7", "6", "6", "2", "11", "12", "5", "5", "10", "9", "9", "13", "11",
					"12", "15", "9", "12" };

			limitPrice = new String[] { "1775.45", "3240.60", "", "", "", "", "198.60", "33.10", "1733.75", "",
					"116.95", "", "", "105.00", "198.60", "", "64.70", "53.10", "" };

			stopPrice = new String[] { "", "", "", "", "50.50", "33.00", "199.65", "32.45", "", "", "", "", "50.50", "",
					"199.65", "", "62.40", "", "33.00" };

			price = new String[] { "1824.95", "3203.50", "123.10", "148.90", "49.30", "37.30", "202.05", "30.95",
					"1824.95", "3203.50", "123.10", "148.90", "49.30", "116.15", "202.05", "30.95", "60.90", "52.05",
					"37.30" };

			instrumentAmount = new String[] { "107.95", "1093.76", "266.57", "261.90", "440.80", "107.95", "1093.76",
					"266.57", "261.90", "440.80", "107.95", "1093.76", "266.57", "261.90", "440.80", "107.95",
					"1093.76", "266.57", "261.90" };

			fees = new String[] { "8.11", "10.39", "2.57", "2.46", "4.54", "8.11", "10.39", "2.57", "2.46", "4.54",
					"8.11", "10.39", "2.57", "2.46", "4.54", "8.11", "10.39", "2.57", "2.46" };

			total = new String[] { "8116.06", "10404.15", "2569.14", "2464.36", "5106.60", "4545.34", "816.06",
					"1004.15", "269.14", "264.36", "8116.06", "10404.15", "2569.14", "2464.36", "5106.60", "4545.34",
					"816.06", "1004.15", "269.14" };

			exchangeRate = new String[] { "1", "1", "1", "1", "1", "69", "1", "1", "1", "1", "1", "1", "1", "1", "1",
					"1", "1", "1", "69" };

			netAmount = new String[] { "816.06", "1404.15", "269.14", "281.88", "549.86", "8116.06", "10404.15",
					"2569.14", "2981.88", "5106.60", "1404.15", "269.14", "281.88", "549.86", "8116.06", "10404.15",
					"2569.14", "2981.88", "5106.60" };

			orderReference = new String[] { "OPODSC2010844602", "OPODSC2010844803", "OPODSC2010844452",
					"OPODSC2010887605", "OPODSC2010821825", "OPODSC2010843808", "OPODSC2010888705", "OPODSC2010888788",
					"OPODSC2010844620", "OPODSC2010844830", "OPODSC2010844425", "OPODSC2010887650", "OPODSC2010821852",
					"OPODSC2010843880", "OPODSC2010888750", "OPODSC2010888799", "OPODSC2010888325", "OPODSC2010888391",
					"OPODSC2010888481" };

			status = new String[] { "Open", "Open", "Open", "Open", "Open", "Open", "Open", "Open", "Completed",
					"Completed", "Completed", "Completed", "Completed", "Cancelled", "Completed", "Cancelled",
					"Completed", "Completed", "Rejected" };

			int openCount = 0;
			for (int i = 0; i < status.length; i++) {
				if (status[i].equalsIgnoreCase("open")) {
					openCount = openCount + 1;
				}
			}

			tradeDate = PortfolioServiceUtils.mockOrdersTradeDate(openCount, orderId.length);
			valueDate = PortfolioServiceUtils.mockOrdersTradeDate(openCount, orderId.length);

		} else if (portfolioId.equalsIgnoreCase("100777-2")) {
			orderId = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15",
					"16", "17" };

			description = new String[] { "American Express Company", "Boeing Co", "Bank of America Corp",
					"Citigroup Inc", "General Motors Company", "Pfizer Inc", "Coca-Cola Co", "Walmart Inc",
					"Coca-Cola Co", "American Express Company", "Boeing Co", "Bank of America Corp", "Citigroup Inc",
					"General Motors Company", "Pfizer Inc", "Coca-Cola Co", "Walmart Inc" };

			instrumentId = new String[] { "100020-000", "100086-000", "100017-000", "100018-000", "100019-000",
					"100022-000", "100016-000", "100014-000", "100016-000", "100020-000", "100086-000", "100017-000",
					"100018-000", "100019-000", "100022-000", "100016-000", "100014-000" };

			RICCode = new String[] { "AXP.O", "BA.O", "BAC.O", "C.O", "GM.N", "PFE.O", "COKE.O", "WALMART", "COKE.O",
					"AXP.O", "BA.O", "BAC.O", "C.O", "GM.N", "PFE.O", "COKE.O", "WALMART" };

			ISIN = new String[] { "US0258161092", "US0970231058", "US0605051046", "US1729671016", "US37045V1008",
					"INE182A01018", "US1912161007", "US9311421039", "US1912161007", "US0258161092", "US0970231058",
					"US0605051046", "US1729671016", "US37045V1008", "INE182A01018", "US1912161007", "US9311421039" };

			exchange = new String[] { "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE",
					"NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE" };

			orderType = new String[] { "Buy Limit", "Sell Limit", "Buy Market", "Sell Market", "Sell Stop Loss",
					"Buy Stop Loss", "Sell Stop Limit", "Buy Stop Limit", "Sell Market", "Buy Limit", "Buy Market",
					"Sell Market", "Buy Stop Loss", "Sell Limit", "Sell Stop Loss", "Sell Stop Limit",
					"Buy Stop Limit" };

			orderMode = new String[] { "Buy", "Sell", "Buy", "Sell", "Sell", "Buy", "Sell", "Buy", "Sell", "Buy", "Buy",
					"Sell", "Buy", "Sell", "Sell", "Sell", "Buy" };

			orderModeType = new String[] { "Limit", "Limit", "Market", "Market", "Stop Loss", "Stop Loss", "Stop Limit",
					"Stop Limit", "Market", "Limit", "Market", "Market", "Stop Loss", "Limit", "Stop Loss",
					"Stop Limit", "Stop Limit" };

			validity = new String[] { "Day Order", "Good Till Canceled", "Day Order", "Good Till Canceled", "Day Order",
					"Good Till Canceled", "Day Order", "Good Till Canceled", "Day Order", "Good Till Canceled",
					"Day Order", "Good Till Canceled", "Day Order", "Good Till Canceled", "Day Order", "Day Order",
					"Good Till Canceled" };

			instrumentCurrency = new String[] { "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD",
					"USD", "USD", "USD", "USD", "USD", "USD", "USD" };

			quantity = new String[] { "13", "11", "12", "15", "9", "9", "11", "12", "6", "10", "8", "9", "12", "6", "9",
					"11", "12" };

			limitPrice = new String[] { "105", "210", "", "", "", "", "198.60", "33.10", "", "105.00", "", "", "",
					"53.80", "", "198.60", "34.10" };

			stopPrice = new String[] { "", "", "", "", "32.30", "52.30", "202.05", "30.95", "", "", "", "", "62.45", "",
					"33.00", "198.65", "33.95" };

			price = new String[] { "116.15", "202.10", "30.95", "60.95", "33.00", "50.50", "199.65", "32.45", "49.30",
					"116.15", "202.60", "30.95", "60.90", "52.05", "37.30", "199.65", "32.45" };

			instrumentAmount = new String[] { "107.95", "1093.76", "266.57", "261.90", "440.80", "107.95", "1093.76",
					"266.57", "261.90", "440.80", "107.95", "1093.76", "266.57", "261.90", "440.80", "107.95",
					"1093.76" };

			fees = new String[] { "8.11", "10.39", "2.57", "2.46", "4.54", "8.11", "10.39", "2.57", "2.46", "4.54",
					"8.11", "10.39", "2.57", "2.46", "4.54", "8.11", "10.39" };

			total = new String[] { "8116.06", "10404.15", "2569.14", "2464.36", "5106.60", "4545.34", "816.06",
					"1004.15", "269.14", "264.36", "8116.06", "10404.15", "2569.14", "2464.36", "5106.60", "4545.34",
					"816.06" };

			exchangeRate = new String[] { "1", "1", "1", "1", "1", "69", "1", "1", "1", "1", "1", "1", "1", "1", "1",
					"1", "69", "1", "1" };

			netAmount = new String[] { "816.06", "1404.15", "269.14", "281.88", "549.86", "8116.06", "10404.15",
					"2569.14", "2981.88", "5106.60", "1404.15", "269.14", "281.88", "549.86", "8116.06", "10404.15",
					"2569.14" };

			orderReference = new String[] { "OPODSC2010854602", "OPODSC2010854803", "OPODSC2010854452",
					"OPODSC2010885605", "OPODSC2010831825", "OPODSC2010820025", "OPODSC2010888715", "OPODSC2010888778",
					"OPODSC2010834602", "OPODSC2010834803", "OPODSC2010834452", "OPODSC2010835605", "OPODSC2010841825",
					"OPODSC2010830025", "OPODSC2010832005", "OPODSC2010837007", "OPODSC2010832653" };

			status = new String[] { "Open", "Open", "Open", "Open", "Open", "Open", "Open", "Open", "Completed",
					"Completed", "Completed", "Cancelled", "Completed", "Completed", "Rejected", "Completed",
					"Completed" };

			int openCount = 0;
			for (int i = 0; i < status.length; i++) {
				if (status[i].equalsIgnoreCase("open")) {
					openCount = openCount + 1;
				}
			}

			tradeDate = PortfolioServiceUtils.mockOrdersTradeDate(openCount, orderId.length);
			valueDate = PortfolioServiceUtils.mockOrdersTradeDate(openCount, orderId.length);

		} else if (portfolioId.equalsIgnoreCase("100777-3")) {
			orderId = new String[] { "1", "2", "3", "4", "5", "6", "7", "8" };

			description = new String[] { "Alphabet", "Amazon.com Inc", "Apple", "LVMH", "Coca-Cola Co", "Boeing Co",
					"Alphabet", "American Express Company" };

			instrumentId = new String[] { "100077-000", "100021-000", "100050-000", "100051-000", "100016-000",
					"100086-000", "100077-000", "100020-000" };

			RICCode = new String[] { "GOOGL.O", "AMZN.O", "AAPL.OQ", "LVMH.PA", "COKE.O", "BA.NQ", "GOOGL.O", "AXP.N" };

			ISIN = new String[] { "US02079K1079", "US0231351067", "US0378331005", "FR0000121014", "US1912161007",
					"US0970231058", "US02079K1079", "US0258161092" };

			exchange = new String[] { "NYSE", "NYSE", "NYSE", "Euronext Paris", "NYSE", "NYSE", "NYSE", "NYSE" };

			orderType = new String[] { "Buy Limit", "Sell Limit", "Buy Market", "Sell Market", "Buy Stop Loss",
					"Sell Stop Limit", "Buy Limit", "Buy Limit" };

			orderMode = new String[] { "Buy", "Sell", "Buy", "Sell", "Buy", "Sell", "Buy", "Buy" };

			orderModeType = new String[] { "Limit", "Limit", "Market", "Market", "Stop Loss", "Stop Limit", "Limit",
					"Limit" };

			validity = new String[] { "Day Order", "Good Till Canceled", "Day Order", "Good Till Canceled", "Day Order",
					"Good Till Canceled", "Day Order", "Good Till Canceled" };

			instrumentCurrency = new String[] { "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD" };

			quantity = new String[] { "2", "2", "7", "6", "6", "11", "5", "13" };

			limitPrice = new String[] { "1775.45", "3240.60", "", "", "", "198.60", "1733.75", "105.00" };

			stopPrice = new String[] { "", "", "", "", "50.50", "199.65", "", "" };

			price = new String[] { "1824.95", "3203.50", "123.10", "148.90", "49.30", "202.05", "1824.95", "116.15" };

			instrumentAmount = new String[] { "107.95", "1093.76", "266.57", "261.90", "440.80", "1093.76", "261.90",
					"261.90" };

			fees = new String[] { "8.11", "10.39", "2.57", "2.46", "4.54", "10.39", "2.46", "2.46" };

			total = new String[] { "8116.06", "10404.15", "2569.14", "2464.36", "5106.60", "816.06", "269.14",
					"2464.36" };

			exchangeRate = new String[] { "1", "1", "1", "1", "1", "1", "1", "1" };

			netAmount = new String[] { "816.06", "1404.15", "269.14", "281.88", "549.86", "10404.15", "2981.88",
					"549.86" };

			orderReference = new String[] { "OPODSC2010844602", "OPODSC2010844803", "OPODSC2010844452",
					"OPODSC2010887605", "OPODSC2010821825", "OPODSC2010888705", "OPODSC2010844620",
					"OPODSC2010843880" };

			status = new String[] { "Open", "Open", "Open", "Open", "Completed", "Completed", "Completed", "Rejected" };

			int openCount = 0;
			for (int i = 0; i < status.length; i++) {
				if (status[i].equalsIgnoreCase("open")) {
					openCount = openCount + 1;
				}
			}

			tradeDate = PortfolioServiceUtils.mockOrdersTradeDate(openCount, orderId.length);
			valueDate = PortfolioServiceUtils.mockOrdersTradeDate(openCount, orderId.length);

		} else if (portfolioId.equalsIgnoreCase("100777-4")) {
			orderId = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15",
					"16", "17", "18", "19" };

			description = new String[] { "Alphabet", "Amazon.com Inc", "Apple", "Walmart Inc", "Coca-Cola Co",
					"Pfizer Inc", "Boeing Co", "Bank of America Corp", "Alphabet", "Amazon.com Inc", "Apple",
					"Walmart Inc", "Coca-Cola Co", "American Express Company", "Boeing Co", "Bank of America Corp",
					"Citigroup Inc", "General Motors Company", "Pfizer Inc" };

			instrumentId = new String[] { "100077-000", "100021-000", "100050-000", "100014-000", "100016-000",
					"100022-000", "100086-000", "100017-000", "100077-000", "100021-000", "100050-000", "100014-000",
					"100016-000", "100020-000", "100086-000", "100017-000", "100018-000", "100019-000", "100022-000" };

			RICCode = new String[] { "GOOGL.O", "AMZN.O", "AAPL.OQ", "WALMART", "COKE.O", "PFE.O", "BA.O", "BAC.O",
					"GOOGL.O", "AMZN.O", "AAPL.OQ", "WALMART", "COKE.O", "AXP.O", "BA.O", "BAC.O", "C.O", "GM.N",
					"PFE.O" };

			ISIN = new String[] { "US02079K1079", "US0231351067", "US0378331005", "US9311421039", "US1912161007",
					"INE182A01018", "US0970231058", "US0605051046", "US02079K1079", "US0231351067", "US0378331005",
					"US9311421039", "US1912161007", "US0258161092", "US0970231058", "US0605051046", "US1729671016",
					"US37045V1008", "INE182A01018" };

			exchange = new String[] { "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE",
					"NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE" };

			orderType = new String[] { "Buy Limit", "Sell Limit", "Buy Market", "Sell Market", "Buy Stop Loss",
					"Sell Stop Loss", "Sell Stop Limit", "Buy Stop Limit", "Buy Limit", "Buy Market", "Sell Limit",
					"Sell Market", "Buy Stop Loss", "Buy Limit", "Sell Stop Limit", "Sell Market", "Buy Stop Limit",
					"Buy Limit", "Sell Stop Loss" };

			orderMode = new String[] { "Buy", "Sell", "Buy", "Sell", "Buy", "Sell", "Sell", "Buy", "Buy", "Buy", "Sell",
					"Sell", "Buy", "Buy", "Sell", "Sell", "Buy", "Buy", "Sell" };

			orderModeType = new String[] { "Limit", "Limit", "Market", "Market", "Stop Loss", "Stop Loss", "Stop Limit",
					"Stop Limit", "Limit", "Market", "Limit", "Market", "Stop Loss", "Limit", "Stop Limit", "Market",
					"Stop Limit", "Limit", "Stop Loss" };

			validity = new String[] { "Day Order", "Good Till Canceled", "Day Order", "Good Till Canceled", "Day Order",
					"Good Till Canceled", "Day Order", "Good Till Canceled", "Day Order", "Good Till Canceled",
					"Day Order", "Good Till Canceled", "Day Order", "Good Till Canceled", "Day Order",
					"Good Till Canceled", "Day Order", "Good Till Canceled", "Good Till Canceled" };

			instrumentCurrency = new String[] { "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD",
					"USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD" };

			quantity = new String[] { "2", "2", "7", "6", "6", "2", "11", "12", "5", "5", "10", "9", "9", "13", "11",
					"12", "15", "9", "12" };

			limitPrice = new String[] { "1775.45", "3240.60", "", "", "", "", "198.60", "33.10", "1733.75", "",
					"116.95", "", "", "105.00", "198.60", "", "64.70", "53.10", "" };

			stopPrice = new String[] { "", "", "", "", "50.50", "33.00", "199.65", "32.45", "", "", "", "", "50.50", "",
					"199.65", "", "62.40", "", "33.00" };

			price = new String[] { "1824.95", "3203.50", "123.10", "148.90", "49.30", "37.30", "202.05", "30.95",
					"1824.95", "3203.50", "123.10", "148.90", "49.30", "116.15", "202.05", "30.95", "60.90", "52.05",
					"37.30" };

			instrumentAmount = new String[] { "107.95", "1093.76", "266.57", "261.90", "440.80", "107.95", "1093.76",
					"266.57", "261.90", "440.80", "107.95", "1093.76", "266.57", "261.90", "440.80", "107.95",
					"1093.76", "266.57", "261.90" };

			fees = new String[] { "8.11", "10.39", "2.57", "2.46", "4.54", "8.11", "10.39", "2.57", "2.46", "4.54",
					"8.11", "10.39", "2.57", "2.46", "4.54", "8.11", "10.39", "2.57", "2.46" };

			total = new String[] { "8116.06", "10404.15", "2569.14", "2464.36", "5106.60", "4545.34", "816.06",
					"1004.15", "269.14", "264.36", "8116.06", "10404.15", "2569.14", "2464.36", "5106.60", "4545.34",
					"816.06", "1004.15", "269.14" };

			exchangeRate = new String[] { "1", "1", "1", "1", "1", "69", "1", "1", "1", "1", "1", "1", "1", "1", "1",
					"1", "1", "1", "69" };

			netAmount = new String[] { "816.06", "1404.15", "269.14", "281.88", "549.86", "8116.06", "10404.15",
					"2569.14", "2981.88", "5106.60", "1404.15", "269.14", "281.88", "549.86", "8116.06", "10404.15",
					"2569.14", "2981.88", "5106.60" };

			orderReference = new String[] { "OPODSC2010844602", "OPODSC2010844803", "OPODSC2010844452",
					"OPODSC2010887605", "OPODSC2010821825", "OPODSC2010843808", "OPODSC2010888705", "OPODSC2010888788",
					"OPODSC2010844620", "OPODSC2010844830", "OPODSC2010844425", "OPODSC2010887650", "OPODSC2010821852",
					"OPODSC2010843880", "OPODSC2010888750", "OPODSC2010888799", "OPODSC2010888325", "OPODSC2010888391",
					"OPODSC2010888481" };

			status = new String[] { "Open", "Open", "Open", "Open", "Open", "Open", "Open", "Open", "Completed",
					"Completed", "Completed", "Completed", "Completed", "Cancelled", "Completed", "Cancelled",
					"Completed", "Completed", "Rejected" };

			int openCount = 0;
			for (int i = 0; i < status.length; i++) {
				if (status[i].equalsIgnoreCase("open")) {
					openCount = openCount + 1;
				}
			}

			tradeDate = PortfolioServiceUtils.mockOrdersTradeDate(openCount, orderId.length);
			valueDate = PortfolioServiceUtils.mockOrdersTradeDate(openCount, orderId.length);

		} else if (portfolioId.equalsIgnoreCase("100777-5")) {
			orderId = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15",
					"16", "17" };

			description = new String[] { "American Express Company", "Boeing Co", "Bank of America Corp",
					"Citigroup Inc", "General Motors Company", "Pfizer Inc", "Coca-Cola Co", "Walmart Inc",
					"Coca-Cola Co", "American Express Company", "Boeing Co", "Bank of America Corp", "Citigroup Inc",
					"General Motors Company", "Pfizer Inc", "Coca-Cola Co", "Walmart Inc" };

			instrumentId = new String[] { "100020-000", "100086-000", "100017-000", "100018-000", "100019-000",
					"100022-000", "100016-000", "100014-000", "100016-000", "100020-000", "100086-000", "100017-000",
					"100018-000", "100019-000", "100022-000", "100016-000", "100014-000" };

			RICCode = new String[] { "AXP.O", "BA.O", "BAC.O", "C.O", "GM.N", "PFE.O", "COKE.O", "WALMART", "COKE.O",
					"AXP.O", "BA.O", "BAC.O", "C.O", "GM.N", "PFE.O", "COKE.O", "WALMART" };

			ISIN = new String[] { "US0258161092", "US0970231058", "US0605051046", "US1729671016", "US37045V1008",
					"INE182A01018", "US1912161007", "US9311421039", "US1912161007", "US0258161092", "US0970231058",
					"US0605051046", "US1729671016", "US37045V1008", "INE182A01018", "US1912161007", "US9311421039" };

			exchange = new String[] { "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE",
					"NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE" };

			orderType = new String[] { "Buy Limit", "Sell Limit", "Buy Market", "Sell Market", "Sell Stop Loss",
					"Buy Stop Loss", "Sell Stop Limit", "Buy Stop Limit", "Sell Market", "Buy Limit", "Buy Market",
					"Sell Market", "Buy Stop Loss", "Sell Limit", "Sell Stop Loss", "Sell Stop Limit",
					"Buy Stop Limit" };

			orderMode = new String[] { "Buy", "Sell", "Buy", "Sell", "Sell", "Buy", "Sell", "Buy", "Sell", "Buy", "Buy",
					"Sell", "Buy", "Sell", "Sell", "Sell", "Buy" };

			orderModeType = new String[] { "Limit", "Limit", "Market", "Market", "Stop Loss", "Stop Loss", "Stop Limit",
					"Stop Limit", "Market", "Limit", "Market", "Market", "Stop Loss", "Limit", "Stop Loss",
					"Stop Limit", "Stop Limit" };

			validity = new String[] { "Day Order", "Good Till Canceled", "Day Order", "Good Till Canceled", "Day Order",
					"Good Till Canceled", "Day Order", "Good Till Canceled", "Day Order", "Good Till Canceled",
					"Day Order", "Good Till Canceled", "Day Order", "Good Till Canceled", "Day Order", "Day Order",
					"Good Till Canceled" };

			instrumentCurrency = new String[] { "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD",
					"USD", "USD", "USD", "USD", "USD", "USD", "USD" };

			quantity = new String[] { "13", "11", "12", "15", "9", "9", "11", "12", "6", "10", "8", "9", "12", "6", "9",
					"11", "12" };

			limitPrice = new String[] { "105", "210", "", "", "", "", "198.60", "33.10", "", "105.00", "", "", "",
					"53.80", "", "198.60", "34.10" };

			stopPrice = new String[] { "", "", "", "", "32.30", "52.30", "202.05", "30.95", "", "", "", "", "62.45", "",
					"33.00", "198.65", "33.95" };

			price = new String[] { "116.15", "202.10", "30.95", "60.95", "33.00", "50.50", "199.65", "32.45", "49.30",
					"116.15", "202.60", "30.95", "60.90", "52.05", "37.30", "199.65", "32.45" };

			instrumentAmount = new String[] { "107.95", "1093.76", "266.57", "261.90", "440.80", "107.95", "1093.76",
					"266.57", "261.90", "440.80", "107.95", "1093.76", "266.57", "261.90", "440.80", "107.95",
					"1093.76" };

			fees = new String[] { "8.11", "10.39", "2.57", "2.46", "4.54", "8.11", "10.39", "2.57", "2.46", "4.54",
					"8.11", "10.39", "2.57", "2.46", "4.54", "8.11", "10.39" };

			total = new String[] { "8116.06", "10404.15", "2569.14", "2464.36", "5106.60", "4545.34", "816.06",
					"1004.15", "269.14", "264.36", "8116.06", "10404.15", "2569.14", "2464.36", "5106.60", "4545.34",
					"816.06" };

			exchangeRate = new String[] { "1", "1", "1", "1", "1", "69", "1", "1", "1", "1", "1", "1", "1", "1", "1",
					"1", "69", "1", "1" };

			netAmount = new String[] { "816.06", "1404.15", "269.14", "281.88", "549.86", "8116.06", "10404.15",
					"2569.14", "2981.88", "5106.60", "1404.15", "269.14", "281.88", "549.86", "8116.06", "10404.15",
					"2569.14" };

			orderReference = new String[] { "OPODSC2010854602", "OPODSC2010854803", "OPODSC2010854452",
					"OPODSC2010885605", "OPODSC2010831825", "OPODSC2010820025", "OPODSC2010888715", "OPODSC2010888778",
					"OPODSC2010834602", "OPODSC2010834803", "OPODSC2010834452", "OPODSC2010835605", "OPODSC2010841825",
					"OPODSC2010830025", "OPODSC2010832005", "OPODSC2010837007", "OPODSC2010832653" };

			status = new String[] { "Open", "Open", "Open", "Open", "Open", "Open", "Open", "Open", "Completed",
					"Completed", "Completed", "Cancelled", "Completed", "Completed", "Rejected", "Completed",
					"Completed" };

			int openCount = 0;
			for (int i = 0; i < status.length; i++) {
				if (status[i].equalsIgnoreCase("open")) {
					openCount = openCount + 1;
				}
			}

			tradeDate = PortfolioServiceUtils.mockOrdersTradeDate(openCount, orderId.length);
			valueDate = PortfolioServiceUtils.mockOrdersTradeDate(openCount, orderId.length);

		}

		if (cancelOrderID != null && cancelOrderID.trim().length() > 0) {
			diagnostic.prepareDebug("==========> GetOrdersDetailsMockPostProcessor Mock - Cancel order Block ").log();
			sortBy = (sortBy != null && sortBy.length() > 0) ? sortBy : "description";
			type = (type != null && type.trim().length() > 0 && type.equalsIgnoreCase("HISTORY")) ? "HISTORY" : "OPEN";
			String splitOrderID[] = cancelOrderID.split(",");
			for (int i = 0; i < orderReference.length; i++) {
				if (Arrays.asList(splitOrderID).contains(orderReference[i])) {
					status[i] = "Cancelled";
				}
			}
		}

		for (int i = 0; i < orderId.length; i++) {

			JSONObject transObj = new JSONObject();
			if (type.trim().equalsIgnoreCase("OPEN") && status[i].toString().trim().equalsIgnoreCase("Open")) {

				transObj.put(TemenosConstants.TRANSACTIONID, orderId[i]);
				transObj.put(TemenosConstants.DESCRIPTION, description[i]);
				transObj.put("ISIN", ISIN[i]);
				transObj.put("holdingsType", exchange[i]);
				transObj.put(TemenosConstants.ORDERTYPE, orderType[i]);
				transObj.put(TemenosConstants.RICCODE, RICCode[i]);
				transObj.put(TemenosConstants.QUANTITY, quantity[i]);
				transObj.put(TemenosConstants.LIMITPRICE, limitPrice[i]);
				transObj.put(TemenosConstants.ORDERS_PRICE, price[i]);
				transObj.put(TemenosConstants.INSTRUMENTAMOUNT, instrumentAmount[i]);
				transObj.put(TemenosConstants.FEES, fees[i]);
				transObj.put("total", total[i]);
				transObj.put(TemenosConstants.EXCHANGERATE, exchangeRate[i]);
				transObj.put(TemenosConstants.NETAMOUNT, netAmount[i]);
				transObj.put(TemenosConstants.TRADEDATE, tradeDate[i]);
				transObj.put(TemenosConstants.VALUEDATE, valueDate[i]);
				transObj.put(TemenosConstants.INSTRUMENTCURRENCY, instrumentCurrency[i]);
				transObj.put(TemenosConstants.ORDERS_STATUS, status[i]);
				transObj.put("orderMode", orderMode[i]);
				transObj.put("orderModeType", orderModeType[i]);
				transObj.put(TemenosConstants.STOPPRICE, stopPrice[i]);
				transObj.put(TemenosConstants.VALIDITY, validity[i]);
				transObj.put(TemenosConstants.ORDER_REFERENCE, orderReference[i]);
				transObj.put(TemenosConstants.INSTRUMENTID, instrumentId[i]);
				transObj.put(TemenosConstants.ISINEXCHANGE, ISIN[i] + " | " + exchange[i]);
				transactionsArr.put(transObj);
			} else if (type.trim().equalsIgnoreCase("HISTORY")
					&& !(status[i].toString().trim().equalsIgnoreCase("Open"))) {

				transObj.put(TemenosConstants.TRANSACTIONID, orderId[i]);
				transObj.put(TemenosConstants.DESCRIPTION, description[i]);
				transObj.put("ISIN", ISIN[i]);
				transObj.put("holdingsType", exchange[i]);
				transObj.put(TemenosConstants.ORDERTYPE, orderType[i]);
				transObj.put(TemenosConstants.RICCODE, RICCode[i]);
				transObj.put(TemenosConstants.QUANTITY, quantity[i]);
				transObj.put(TemenosConstants.LIMITPRICE, limitPrice[i]);
				transObj.put(TemenosConstants.ORDERS_PRICE, price[i]);
				transObj.put(TemenosConstants.INSTRUMENTAMOUNT, instrumentAmount[i]);
				transObj.put(TemenosConstants.FEES, fees[i]);
				transObj.put("total", total[i]);
				transObj.put(TemenosConstants.EXCHANGERATE, exchangeRate[i]);
				transObj.put(TemenosConstants.NETAMOUNT, netAmount[i]);
				transObj.put(TemenosConstants.TRADEDATE, tradeDate[i]);
				transObj.put(TemenosConstants.VALUEDATE, valueDate[i]);
				transObj.put(TemenosConstants.INSTRUMENTCURRENCY, instrumentCurrency[i]);
				transObj.put(TemenosConstants.ORDERS_STATUS, status[i]);
				transObj.put("orderMode", orderMode[i]);
				transObj.put("orderModeType", orderModeType[i]);
				transObj.put(TemenosConstants.STOPPRICE, stopPrice[i]);
				transObj.put(TemenosConstants.VALIDITY, validity[i]);
				transObj.put(TemenosConstants.ORDER_REFERENCE, orderReference[i]);
				transObj.put(TemenosConstants.INSTRUMENTID, instrumentId[i]);
				transObj.put(TemenosConstants.ISINEXCHANGE, ISIN[i] + " | " + exchange[i]);
				if (status[i].equalsIgnoreCase(TemenosConstants.STATUS_COMPLETED)) {
					if (orderModeType[i].equalsIgnoreCase(TemenosConstants.LIMIT_TYPE)
							|| orderModeType[i].equalsIgnoreCase(TemenosConstants.STOPLIMIT_TYPE)) {
						transObj.put(TemenosConstants.ORDER_EXECUTION_PRICE, limitPrice[i]);
					} else if (orderModeType[i].equalsIgnoreCase(TemenosConstants.STOPLOSS_TYPE)) {
						transObj.put(TemenosConstants.ORDER_EXECUTION_PRICE, stopPrice[i]);
					} else {
						transObj.put(TemenosConstants.ORDER_EXECUTION_PRICE, "");
					}
				} else {
					transObj.put(TemenosConstants.ORDER_EXECUTION_PRICE, "");
				}

				transactionsArr.put(transObj);
			}

		}

		JSONArray sortedJSON = new JSONArray();
		if (startDate == null || endDate == null || startDate.equals("") || endDate.equals("")) {
			sortedJSON = transactionsArr;
			diagnostic.prepareDebug("==========> GetOrdersDetailsMockPostProcessor Mock -  No. of records returned: "+sortedJSON.length()).log();
		} else {
			sortedJSON = PortfolioServiceUtils.filterOrdersDate(transactionsArr, startDate, endDate);
			diagnostic.prepareDebug("==========> GetOrdersDetailsMockPostProcessor Mock -  No. of records returned after date filter: "+sortedJSON.length()).log();
		}

		if (sortBy != null) {
			sortedJSON = PortfolioServiceUtils.sortOrdersArray(sortedJSON, sortBy, sortType);
			diagnostic.prepareDebug("==========> GetOrdersDetailsMockPostProcessor Mock -  No. of records returned after sort: "+sortedJSON.length()).log();
		}

		if (search != null) {
			sortedJSON = PortfolioServiceUtils.returnOrdersSearch(sortedJSON, search);
			diagnostic.prepareDebug("==========> GetOrdersDetailsMockPostProcessor Mock -  No. of records returned after search: "+sortedJSON.length()).log();
		}

		totalCount = sortedJSON.length();

		if (limit > 0 && offset >= 0) {
			sortedJSON = PortfolioWealthUtils.pagination(sortedJSON, limit, offset);
			diagnostic.prepareDebug("==========> GetOrdersDetailsMockPostProcessor Mock -  No. of records returned after pagination: "+sortedJSON.length()).log();
		}

		responseVal.put("portfolioID", portfolioId);
		responseVal.put("ordersDetails", sortedJSON);
		responseVal.put(TemenosConstants.STARTDATE, startDate);
		responseVal.put(TemenosConstants.ENDDATE, endDate);
		responseVal.put(TemenosConstants.SORTBY, sortBy);
		responseVal.put(TemenosConstants.SORTORDER, sortType);
		responseVal.put("count", totalCount);
		responseVal.put("opstatus", "0");
		responseVal.put("httpStatusCode", "200");
		Result final_result = Utilities.constructResultFromJSONObject(responseVal);
		final_result.addOpstatusParam("0");
		final_result.addHttpStatusCodeParam("200");
		final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		diagnostic.prepareDebug("==========> GetOrdersDetailsMockPostProcessor Mock - Exited").log();
		return final_result;
	}

}
