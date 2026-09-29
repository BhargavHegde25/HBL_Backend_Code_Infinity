/**
 * 
 */
package com.temenos.infinity.wealth.mock.processor.post;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
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
public class GetHoldingsListMockPostProcessor implements DataPostProcessor2 {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@SuppressWarnings("unused")
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		diagnostic.prepareDebug("==========> GetHoldingsListMockPostProcessor Mock - Entered ").log();
		String portfolioId = (String)  request.getParameter(TemenosConstants.PORTFOLIOID);
		String sortBy = (String)  request.getParameter(TemenosConstants.SORTBY);
		String searchVal = (String) request.getParameter(TemenosConstants.SEARCHBYINSTRUMENTNAME);
		String instrumentidVal = (String)  request.getParameter(TemenosConstants.INSTRUMENTID);
		String[] holdingsId = null, description = null, ISIN = null, exchange = null, secCCy = null, quantity = null,
				marketPrice = null, costPrice = null, marketValue = null, unrealPLMkt = null, RICCode = null,
				weightPercentage = null, assetClass = null, region = null, sector = null, exchangeRate = null,
				marketValPOS = null, costValue = null, costExchangeRate = null, unRealizedPLPercentage = null,
				dailyPL = null, dailyPLPercentage = null, instrumentId = null, unrealPLMktSec = null,
				unRealizedPLPercentageSec = null, costValueSec = null, amountBought = null, accruedInterest = null,
				balance = null, amountSold = null, quote = null, costQuote = null, counterpartAmount = null,
				subAssetClass = null, application = null, status = null;
		;
		int averageCostIndex = 0, nominalIndex = 0, accruedInterestIndex = 0, amountBoughtIndex = 0, balanceIndex = 0,
				amountSoldIndex = 0, quoteIndex = 0, costQuoteIndex = 0, counterpartAmountIndex = 0;

		boolean[] isSecurityAsset = null, isAdvisory = null;
		String sortType = (String)  request.getParameter(TemenosConstants.SORTORDER);
		int totalCount = 0;
		String limitVal = (String)  request.getParameter(TemenosConstants.PAGESIZE);
		String offsetVal = (String)  request.getParameter(TemenosConstants.PAGEOFFSET);
		String isIncludeOrders = (String)  request.getParameter(TemenosConstants.ISINCLUEORDERS);
		//String isIncludeOrders = EnvironmentConfigurationsHandler.getValue("INF_WLTH_IS_INCLUDE_ORDERS", request);
		if(isIncludeOrders==null || isIncludeOrders.isEmpty())
		{
			isIncludeOrders= "false";	
		}
		int limit = (limitVal != null && limitVal.trim().length() > 0) ? Integer.parseInt(limitVal) : 0;
		int offset = (offsetVal != null && offsetVal.trim().length() > 0) ? Integer.parseInt(offsetVal) : 0;
		String search = (searchVal != null && searchVal.trim().length() > 0) ? searchVal : "";
		String instrumentid = (instrumentidVal != null && instrumentidVal.trim().length() > 0) ? instrumentidVal : "";
		JSONObject responseVal = new JSONObject();
		JSONArray holdingsArr = new JSONArray();

		if (portfolioId.equalsIgnoreCase("100777-1")) {
			if(isIncludeOrders.equalsIgnoreCase("true")) {
			holdingsId = new String[] { "100051-000", "100093-000", "100016-000", "100020-000", "100086-000",
					"100017-000", "100018-000", "100019-000", "100022-000", "100021-000", "100077-000", "100050-000", "100077-000", "100021-000", "100050-000", "100017-000", "100014-000", "100086-000", "100016-000", "100022-000" };
			instrumentId = new String[] { "100051-000", "100093-000", "100016-000", "100020-000", "100086-000",
					"100017-000", "100018-000", "100019-000", "100022-000", "100021-000", "100077-000", "100050-000", "100077-000", "100021-000", "100050-000", "100017-000", "100014-000", "100086-000", "100016-000", "100022-000" };
			description = new String[] { "LVMH", "iShares Core S&P 500 UCITS ETF", "Coca-Cola Co",
					"American Express Company", "Boeing Co", "Bank of America Corp", "Citigroup Inc",
					"General Motors Company", "Pfizer Inc", "Amazon.com Inc", "Alphabet", "Apple", "Alphabet", "Amazon.com Inc", "Apple", "Bank of America Corp", "Walmart Inc", "Boeing Co", "Coca-Cola Co", "Pfizer Inc"};
			status = new String[] { "", "", "", "", "", "", "", "", "", "", "", "", "Open", "Open", "Open", "Open", "Open", "Open", "Open", "Open" };
			ISIN = new String[] { "FR0000121014", "IE00B5BMR087", "US1912161007", "US0258161092", "US0970231058",
					"US0605051046", "US1729671016", "US37045V1008", "INE182A01018", "US0231351067", "US02079K1079",
					"US0378331005", "US02079K1079", "US0231351067", "US0378331005", "US0605051046", "US9311421039", "US0970231058", "US1912161007", "INE182A01018" };
			exchange = new String[] { "Euronext Paris", "GER", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE",
					"NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE" };
			secCCy = new String[] { "EUR", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD",
					"USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD" };
			quantity = new String[] { "6", "16", "10", "15", "10", "12", "15", "8", "10", "4", "2", "23", "2", "2", "7", "12", "6", "11", "6", "2" };
			marketPrice = new String[] { "497.95", "308.75", "49.29", "116.15", "202.06", "30.94", "60.91", "52.04",
					"37.31", "3203.53", " 1824.97", "123.08", "1824.97", "3203.53", "123.08", "30.94", "148.91", "202.06", "49.29", "37.31" };
			costPrice = new String[] { "372.29", "300.00", "45.00", "105.00", "190.00", "33.00", "55.00", "49.00",
					"34.00", "2652.13", "1622.11", "98.08", "1622.11", "2652.13", "98.08", "33.00", "132.51", "190.00", "45.00", "34.00" };
			marketValue = new String[] { "2987.70", "5631.12", "492.90", "1742.25", "2020.60", "371.28", "913.65",
					"416.32", "373.10", "12814.12", "3649.94", "2830.84", "3649.94", "6407.06", "861.56", "371.28", "893.46", "2222.66", "295.74", "74.62" };
			unrealPLMkt = new String[] { "+753.96", "+631.12", "+42.90", "+167.25", "+120.60", "-24.72", "+88.65",
					"+24.32", "+33.10", "+2205.60", "+405.72", "575.00", "+405.72", "+1102.80", "+175.00", "-24.72", "+98.40", "+132.66", "+25.74", "+6.62" };
			unrealPLMktSec = new String[] {"+753.96", "+831.12", "+42.90", "+167.25", "+120.60", "-24.72", "+88.65",
					"+24.32", "+33.10", "+2205.60", "+405.72", "575.00", "+405.72", "+1102.80", "+175.00", "-24.72", "+98.40", "+132.66", "+25.74", "+6.62" };
			RICCode = new String[] { "LVMH.PA", "IXM0461.DE", "KO.N", "AXP.N", "BA.NQ", "BAC.N", "C.N", "GM.N", "PFE.N",
					"AMZN.OQ", "GOOGL.OQ", "AAPL.OQ", "GOOGL.OQ", "AMZN.OQ", "AAPL.OQ", "BAC.N", "", "BA.NQ", "KO.N", "PFE.N" };

			weightPercentage = new String[] { "6.03", "11.36", "0.99", "3.51", "4.08", "0.75", "1.84", "0.84", "0.75",
					"25.85", "7.36", "5.71", "7.36", "12.92", "1.74", "0.75", "1.80", "4.48", "0.60", "0.15" };
			assetClass = new String[] { "Share", "Fund", "Share", "Share", "Share", "Share", "Share", "Share", "Share",
					"Share", "Share", "Share", "Share", "Share", "Share", "Share", "Share", "Share", "Share", "Share" };
			subAssetClass = new String[] { "Ordinary Shares", "Exchange Traded Funds", "Ordinary Shares",
					"Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares",
					"Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares" };

			region = new String[] { "EU", "US", "US", "US", "US", "US", "US", "US", "US", "US",
					"US", "US", "US", "US", "US", "US", "US", "US", "US", "US" };
			sector = new String[] { "Consumer Cyclical", "Fund -Open Ended Investment Company", "Consumer Defensive",
					"Financial Services", "Industrials", "Financial Services", "Financial Services",
					"Consumer Cyclical", "Healthcare", "Consumer Cyclical", "Communication Services", "Technology", "Communication Services", "Consumer Cyclical", "Technology", "Financial Services", "Consumer Defensive", "Industrials", "Consumer Defensive", "Healthcare" };
			exchangeRate = new String[] { "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1" };
			marketValPOS = new String[] { "2987.70", "5631.12", "492.90", "1742.25", "2020.60", "371.28", "913.65",
					"416.32", "373.10", "12814.12", "3649.94", "2830.84", "3649.94", "6407.06", "861.56", "371.28", "893.46", "2222.66", "295.74", "74.62" };
			costValue = new String[] { "2233.74", "5000.00", "450.00", "1575.00", "1900.00", "396.00", "825.00",
					"392.00", "340.00", "10608.52", "3244.22", "2255.84", "3244.22", "5304.26", "686.56", "396.00", "795.06", "2090.00", "270.00", "68.00" };
			costValueSec = new String[] { "335.06", "2500.00", "270.00", "1102.50", "1520.00", "356.40", "82.50",
					"43.12", "40.80", "1379.11", "454.19", "338.38", "454.19", "689.55", "102.98", "356.40", "79.51", "1672.00", "162.00", "8.16" };
			costExchangeRate = new String[] { "0.15", "0.5", "0.6", "0.7", "0.8", "0.9", "0.10", "0.11", "0.12", "0.13",
					"0.14", "0.15", "0.14", "0.13", "0.15", "0.90", "0.10", "0.80", "0.60", "0.12" };
			unRealizedPLPercentage = new String[] { "34", "13", "10", "11", "6", "-6", "11", "6", "10", "21", "13",
					"25", "13", "21", "25", "-6", "12", "6", "10", "10" };
			unRealizedPLPercentageSec = new String[] { "", "", "", "", "", "", "", "", "", "", "",
					"", "", "", "", "", "", "", "", "" };
			dailyPL = new String[] { "85.9", "0.13", "44.5", "-30.2", "-20.4", "-10.45", "23.6", "-10.65", "90.5",
					"145.5", "29.61", "0.36", "29.61", "145.5", "0.36", "-10.45", "2.46", "-20.4", "44.5", "-10.65"};
			dailyPLPercentage = new String[] { "0.11", "1.3", "0.14", "-0.92", "0.60", "-1.20", "0.56", "0.45", "0.83",
					"0.03", "1.65", "0.29", "1.65", "0.03", "0.29", "-1.20", "0.13", "0.60", "0.14", "0.83"};
			isSecurityAsset = new boolean[] { true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true };
			isAdvisory = new boolean[] { false, false, false, false, false, false, false, false, false, false, false,
					false, false, false, false, false, false, false, false, false };
			application = new String[] { "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC" };
			accruedInterest = new String[] { "121.56", "55.23", "356.76" };
		}else
		{
			holdingsId = new String[] { "100051-000", "100093-000", "100016-000", "100020-000", "100086-000",
					"100017-000", "100018-000", "100019-000", "100022-000", "100021-000", "100077-000", "100050-000"};
			instrumentId = new String[] { "100051-000", "100093-000", "100016-000", "100020-000", "100086-000",
					"100017-000", "100018-000", "100019-000", "100022-000", "100021-000", "100077-000", "100050-000", "100077-000", "100021-000", "100050-000"};
			description = new String[] { "LVMH", "iShares Core S&P 500 UCITS ETF", "Coca-Cola Co",
					"American Express Company", "Boeing Co", "Bank of America Corp", "Citigroup Inc",
					"General Motors Company", "Pfizer Inc", "Amazon.com Inc", "Alphabet", "Apple"};
			ISIN = new String[] { "FR0000121014", "IE00B5BMR087", "US1912161007", "US0258161092", "US0970231058",
					"US0605051046", "US1729671016", "US37045V1008", "INE182A01018", "US0231351067", "US02079K1079",
					"US0378331005"};
			exchange = new String[] { "Euronext Paris", "GER", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE",
					"NYSE", "NYSE", "NYSE"};
			secCCy = new String[] { "EUR", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD",
					"USD"};
			quantity = new String[] { "6", "16", "10", "15", "10", "12", "15", "8", "10", "4", "2", "23"};
			marketPrice = new String[] { "497.95", "308.75", "49.29", "116.15", "202.06", "30.94", "60.91", "52.04",
					"37.31", "3203.53", " 1824.97", "123.08"};
			costPrice = new String[] {"372.29", "300.00", "45.00", "105.00", "190.00", "33.00", "55.00", "49.00",
					"34.00", "2652.13", "1622.11", "98.08"};
			marketValue = new String[] {  "2987.70", "5631.12", "492.90", "1742.25", "2020.60", "371.28", "913.65",
					"416.32", "373.10", "12814.12", "3649.94", "2830.84"};
			unrealPLMkt = new String[] { "+753.96", "+631.12", "+42.90", "+167.25", "+120.60", "-24.72", "+88.65",
					"+24.32", "+33.10", "+2205.60", "+405.72", "575.00"};
			unrealPLMktSec = new String[] { "+753.96", "+631.12", "+42.90", "+167.25", "+120.60", "-24.72", "+88.65",
					"+24.32", "+33.10", "+2205.60", "+405.72", "575.00" };
			RICCode = new String[] { "LVMH.PA", "IXM0461.DE", "KO.N", "AXP.N", "BA.NQ", "BAC.N", "C.N", "GM.N", "PFE.N",
					"AMZN.OQ", "GOOGL.OQ", "AAPL.OQ"};

			weightPercentage = new String[] {"6.03", "11.36", "0.99", "3.51", "4.08", "0.75", "1.84", "0.84", "0.75",
					"25.85", "7.36", "5.71"};
			assetClass = new String[] { "Share", "Fund", "Share", "Share", "Share", "Share", "Share", "Share", "Share",
					"Share", "Share", "Share" };
			subAssetClass = new String[] { "Ordinary Shares", "Exchange Traded Funds", "Ordinary Shares",
					"Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares",
					"Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares" };

			region = new String[] { "EU", "US", "US", "US", "US", "US", "US", "US", "US", "US",
					"US", "US"};
			sector = new String[] { "Consumer Cyclical", "Fund -Open Ended Investment Company", "Consumer Defensive",
					"Financial Services", "Industrials", "Financial Services", "Financial Services",
					"Consumer Cyclical", "Healthcare", "Consumer Cyclical", "Communication Services", "Technology" };
			exchangeRate = new String[] { "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1" };
			marketValPOS = new String[] {  "2987.70", "4940.00", "492.90", "1742.25", "2020.60", "371.28", "913.65",
					"416.32", "373.10", "12814.12", "3649.94", "2830.84" };
			costValue = new String[] { "2233.74", "5000.00", "450.00", "1575.00", "1900.00", "396.00", "825.00",
					"392.00", "340.00", "10608.52", "3244.22", "2255.84" };
			costValueSec = new String[] {  "335.06", "2500.00", "270.00", "1102.50", "1520.00", "356.40", "82.50",
					"43.12", "40.80", "1379.11", "454.19", "338.38"};
			costExchangeRate = new String[] { "0.15", "0.5", "0.6", "0.7", "0.8", "0.9", "0.10", "0.11", "0.12", "0.13",
					"0.14", "0.15"};
			unRealizedPLPercentage = new String[] {"34", "13", "10", "11", "6", "-6", "11", "6", "10", "21", "13",
					"25" };
			unRealizedPLPercentageSec = new String[] { "", "", "", "", "", "", "", "", "", "", "",
					"" };
			dailyPL = new String[] { "85.9", "0.13", "44.5", "-30.2", "-20.4", "-10.45", "23.6", "-10.65", "90.5",
					"145.5", "29.61", "0.36"};
			dailyPLPercentage = new String[] { "0.11", "1.3", "0.14", "-0.92", "0.60", "-1.20", "0.56", "0.45", "0.83",
					"0.03", "1.65", "0.29"};
			isSecurityAsset = new boolean[] { true, true, true, true, true, true, true, true, true, true, true, true };
			isAdvisory = new boolean[] { false, false, false, false, false, false, false, false, false, false, false,
					false};
			application = new String[] { "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC"};
			accruedInterest = new String[] { "121.56", "55.23", "356.76" };
			
		}
		} else if (portfolioId.equalsIgnoreCase("100777-2")) {
			if(isIncludeOrders.equalsIgnoreCase("true")) {
			holdingsId = new String[] { "100077-000", "100014-000", "100016-000", "100020-000", "100086-000",
					"100017-000", "100018-000", "100019-000", "100022-000", "100021-000", "100050-000", "100020-000", "100017-000", "100086-000", "100018-000", "100016-000", "100019-000", "100022-000", "100014-000" };
			instrumentId = new String[] { "100077-000", "100014-000", "100016-000", "100020-000", "100086-000",
					"100017-000", "100018-000", "100019-000", "100022-000", "100021-000", "100050-000", "100020-000", "100017-000", "100086-000", "100018-000", "100016-000", "100019-000", "100022-000", "100014-000" };
			description = new String[] { "Alphabet", "Walmart Inc", "Coca-Cola Co", "American Express Company",
					"Boeing Co", "Bank of America Corp", "Citigroup Inc", "General Motors Company", "Pfizer Inc",
					"Amazon.com Inc", "Apple", "American Express Company", "Bank of America Corp", "Boeing Co", "Citigroup Inc", "Coca-Cola Co", "General Motors Company", "Pfizer Inc", "Walmart Inc" };
			status = new String[] { "", "", "", "","", "", "", "", "", "", "", "Open", "Open", "Open", "Open", "Open", "Open", "Open", "Open" };
			ISIN = new String[] { "US02079K1079", "US9311421039", "US1912161007", "US0258161092", "US0970231058",
					"US0605051046", "US1729671016", "US37045V1008", "INE182A01018", "US0231351067", "US0378331005", "US0258161092", "US0605051046", "US0970231058", "US1729671016", "US1912161007", "US37045V1008", "INE182A01018", "US9311421039" };
			exchange = new String[] { "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE",
					"NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE" };
			secCCy = new String[] { "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD" };
			quantity = new String[] { "2", "6", "6", "10", "8", "9", "12", "6", "9", "2", "7", "13", "12", "11", "15", "11", "9", "9", "12" };
			marketPrice = new String[] { "1824.97", "148.91", "49.29", "116.15", "202.06", "30.94", "60.91", "52.04",
					"37.31", "3203.53", "123.08", "116.15", "30.94", "202.06", "60.91", "49.29", "52.04", "37.31", "148.91" };
			costPrice = new String[] { "1622.11", "132.51", "42.00", "106.00", "184.00", "27.00", "61.00", "50.00",
					"32.00", "2652.13", "98.08", "105.00", "27.00", "184.00", "61.00", "42.00", "50.00", "32.00", "132.51" };
			marketValue = new String[] { "3649.94", "893.46", "295.74", "1161.50", "1616.48", "278.46", "730.92",
					"312.24", "335.79", "6407.06", "861.56", "1509.95", "371.28", "2222.66", "913.65", "542.19", "468.36", "335.79", "1786.92" };
			unrealPLMkt = new String[] { "+405.72", "+98.40", "+43.74", "+105.50", "+144.48", "+35.46", "-1.08",
					"+12.24", "+52.29", "+1102.80", "+175.00", "+144.95", "+47.28", "+198.66", "-1.35", "+80.19", "+18.36", "52.29", "+196.80"};
			unrealPLMktSec = new String[] { "+405.72", "+98.40", "+43.74", "+105.50", "+144.48", "+35.46", "-1.08",
					"+12.24", "+52.29", "+1102.80", "175.00", "+144.95", "+47.28", "+198.66", "-1.35", "+80.19", "+18.36", "+5.29", "+196.80" };
			RICCode = new String[] { "GOOGL.OQ", "WMT.N", "KO.N", "AXP.N", "BA.NQ", "BAC.N", "C.N", "GM.N", "PFE.N",
					"AMZN.OQ", "AAPL.OQ", "AXP.N", "BAC.N", "BA.NQ", "C.N", "KO.N", "GM.N", "PFE.N", "WMT.N" };
			weightPercentage = new String[] { "21.66", "5.30", "1.75", "6.89", "9.59", "1.65", "4.34", "1.85", "1.99",
					"38.02", "5.11", "8.96", "2.20", "13.19", "5.42", "3.22", "2.78", "1.99", "10.60" };
			assetClass = new String[] { "Share", "Share", "Share", "Share", "Share", "Share", "Share", "Share",
					"Share", "Share", "Share", "Share", "Share", "Share", "Share", "Share", "Share", "Share", "Share" };
			subAssetClass = new String[] { "Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares",
					"Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares",
					"Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares" };

			region = new String[] { "US", "US", "US", "US", "US", "US", "US", "US", "US", "US", "US", "US", "US", "US", "US", "US", "US", "US", "US" };
			sector = new String[] { "Communication Services", "Consumer Defensive", "Consumer Defensive",
					"Financial Services", "Industrials", "Financial Services", "Financial Services",
					"Consumer Cyclical", "Healthcare", "Consumer Cyclical", "Technology", "Financial Services", "Financial Services", "Industrials", "Financial Services", "Consumer Defensive", "Consumer Cyclical", "Healthcare", "Consumer Defensive" };
			exchangeRate = new String[] { "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1" };
			marketValPOS = new String[] { "3649.94", "893.46", "295.74", "1161.50", "1616.48", "278.46", "730.92",
					"312.24", "335.79", "6407.06", "861.56", "1509.95", "371.28", "2222.66", "913.65", "542.19", "468.36", "335.79", "1786.92" };
			costValue = new String[] { "3244.22", "795.06", "252.00", "1060.00", "1472.00", "243.00", "732.00",
					"300.00", "288.00", "5304.26", "686.56", "1365.00", "324.00", "2024.00", "915.00", "462.00", "450.00", "288.00", "1590.12" };
			costValueSec = new String[] { "324.42", "318.02", "126.00", "636.00", "1030.40", "194.40", "658.80",
					"30.00", "31.68", "742.60", "109.85", "819.00", "259.20", "1416.80", "823.50", "231.00", "45.00", "31.68", "636.05" };
			costExchangeRate = new String[] { "0.1", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9", "0.1", "0.11", "0.14",
					"0.16", "0.6", "0.8", "0.7", "0.9", "0.5", "0.1", "0.11", "0.4" };
			unRealizedPLPercentage = new String[] { "13", "12", "17", "10", "10", "15", "0", "4", "18", "21", "25", "11", "15", "10", "0", "17", "4", "18", "12" };
			unRealizedPLPercentageSec = new String[] { "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "" };
			dailyPL = new String[] { "5.34", "2.46", "4.42", "-3.56", "4.44", "-4.89", "6.9", "2.24", "1.56", "145.5",
					"0.36", "-3.56", "-4.89", "4.44", "6.9", "4.42", "2.24", "1.56", "2.46" };
			dailyPLPercentage = new String[] { "0.10", "0.13", "0.10", "0.23", "0.10", "-0.34", "0.32", "0.12", "0.14",
					"0.11", "0.03", "0.29", "0.23", "-0.34", "0.10", "0.32", "0.10", "0.12", "0.14", "0.13" };
			isSecurityAsset = new boolean[] { true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true };
			isAdvisory = new boolean[] { false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false };
			application = new String[] { "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC" };

			accruedInterest = new String[] { "132.74", "185.63", "127.52" };
			balance = new String[] { "25132.74", "12785.6325", "35127.518" };
			} else {
				holdingsId = new String[] { "100077-000", "100014-000", "100016-000", "100020-000", "100086-000",
						"100017-000", "100018-000", "100019-000", "100022-000", "100021-000", "100050-000" };
				instrumentId = new String[] { "100077-000", "100014-000", "100016-000", "100020-000", "100086-000",
						"100017-000", "100018-000", "100019-000", "100022-000", "100021-000", "100050-000" };
				description = new String[] { "Alphabet", "Walmart Inc", "Coca-Cola Co", "American Express Company",
						"Boeing Co", "Bank of America Corp", "Citigroup Inc", "General Motors Company", "Pfizer Inc",
						"Amazon.com Inc", "Apple" };
				ISIN = new String[] { "US02079K1079", "US9311421039", "US1912161007", "US0258161092", "US0970231058",
						"US0605051046", "US1729671016", "US37045V1008", "INE182A01018", "US0231351067", "US0378331005" };
				exchange = new String[] { "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE",
						"NYSE"};
				secCCy = new String[] { "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD" };
				quantity = new String[] { "2", "6", "6", "10", "8", "9", "12", "6", "9", "2", "7"};
				marketPrice = new String[] { "1824.97", "148.91", "49.29", "116.15", "202.06", "30.94", "60.91", "52.04",
						"37.31", "3203.53", "123.08"};
				costPrice = new String[] { "1622.11", "132.51", "42.00", "106.00", "184.00", "27.00", "61.00", "50.00",
						"32.00", "2652.13", "98.08" };
				marketValue = new String[] { "3649.94", "893.46", "295.74", "1161.50", "1616.48", "278.46", "730.92",
						"312.24", "335.79", "6407.06", "861.56" };
				unrealPLMkt = new String[] { "+405.72", "+98.40", "+43.74", "+105.50", "+144.48", "+35.46", "-1.08",
						"+12.24", "+52.29", "+1102.80", "+175.00"};
				unrealPLMktSec = new String[] { "+405.72", "+98.40", "+43.74", "+105.50", "+144.48", "+35.46", "-1.08",
						"+12.24", "+52.29", "+1102.80", "175.00" };
				RICCode = new String[] { "GOOGL.OQ", "WMT.N", "KO.N", "AXP.N", "BA.NQ", "BAC.N", "C.N", "GM.N", "PFE.N",
						"AMZN.OQ", "AAPL.OQ" };
				weightPercentage = new String[] { "21.66", "5.30", "1.75", "6.89", "9.59", "1.65", "4.34", "1.85", "1.99",
						"38.02", "5.11"};
				assetClass = new String[] { "Share", "Share", "Share", "Share", "Share", "Share", "Share", "Share",
						"Share", "Share", "Share" };
				subAssetClass = new String[] { "Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares",
						"Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares",
						"Ordinary Shares", "Ordinary Shares" };

				region = new String[] { "US", "US", "US", "US", "US", "US", "US", "US", "US", "US", "US" };
				sector = new String[] { "Communication Services", "Consumer Defensive", "Consumer Defensive",
						"Financial Services", "Industrials", "Financial Services", "Financial Services",
						"Consumer Cyclical", "Healthcare", "Consumer Cyclical", "Technology" };
				exchangeRate = new String[] { "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1" };
				marketValPOS = new String[] { "3649.94", "893.46", "295.74", "1161.50", "1616.48", "278.46", "730.92",
						"312.94", "335.79", "6407.06", "861.56" };
				costValue = new String[] { "3244.22", "795.06", "252.00", "1060.00", "1472.00", "243.00", "732.00",
						"300.00", "288.00", "5304.26", "686.56"};
				costValueSec = new String[] { "324.42", "318.02", "126.00", "636.00", "1030.40", "194.40", "658.80",
						"30.00", "31.68", "742.60", "109.85"};
				costExchangeRate = new String[] { "0.1", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9", "0.1", "0.11", "0.14",
						"0.16"};
				unRealizedPLPercentage = new String[] { "13", "12", "17", "10", "10", "15", "0", "4", "18", "21", "25"};
				unRealizedPLPercentageSec = new String[] { "", "", "", "", "", "", "", "", "", "", "" };
				dailyPL = new String[] { "5.34", "2.46", "4.42", "-3.56", "4.44", "-4.89", "6.9", "2.24", "1.56", "145.5",
						"0.36" };
				dailyPLPercentage = new String[] { "0.10", "0.13", "0.10", "0.23", "0.10", "-0.34", "0.32", "0.12", "0.14",
						"0.11", "0.03" };
				isSecurityAsset = new boolean[] { true, true, true, true, true, true, true, true, true, true, true };
				isAdvisory = new boolean[] { false, false, false, false, false, false, false, false, false, false, false };
				application = new String[] { "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC" };

				accruedInterest = new String[] { "132.74", "185.63", "127.52" };
				balance = new String[] { "25132.74", "12785.6325", "35127.518" };

			}

		} else if (portfolioId.equalsIgnoreCase("100777-3")) {
			if(isIncludeOrders.equalsIgnoreCase("true")) {
			holdingsId = new String[] { "100027-000", "100051-000", "100016-000", "100020-000", "100086-000",
					"100098-000", "100099-000", "100028-000", "100029-000", "100030-000", "100130-000", "100230-000", "100050-000", "100077-000", "100021-000", "100051-000" };
			instrumentId = new String[] { "100027-000", "100051-000", "100016-000", "100020-000", "100086-000",
					"100098-000", "100099-000", "100028-000", "100029-000", "100030-000", "100130-000", "100230-000", "100050-000", "100077-000", "100021-000", "100051-000" };
			description = new String[] { "Google LLC", "LVMH", "Coca-Cola Co", "American Express Company", "Boeing Co",
					"Cisco Systems, Inc.", "APPLE-CALL-115-16JUL", "GOOGLE-PUT-2300-16JUL", "CITI-CALL-70-16JUL",
					"Forward EURUSD 2901 2022", "Forward GBPUSD 2901 2022", "Forward CHFUSD 2901 2022", "Apple", "Alphabet", "Amazon.com Inc", "LVMH" };
			status = new String[] { "", "", "", "", "","", "", "", "", "", "", "", "Open", "Open", "Open", "Open" };
			ISIN = new String[] { "US02079K1079", "FR0000121014", "US1912161007", "US0258161092", "US0970231058",
					"US17275R1023", "US0378331005", "US02079K1079", "US1729671016", "", "", "", "US0378331005", "US02079K1079", "US0231351067", "FR0000121014" };
			exchange = new String[] { "NYSE", "Euronext Paris", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE",
					"", "", "", "NYSE", "NYSE", "NYSE", "Euronext Paris" };
			secCCy = new String[] { "USD", "USD", "EUR", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD",
					"USD", "USD", "USD", "USD", "USD" };
			quantity = new String[] { "1", "5", "7", "8", "9", "14", "101", "24", "22", "", "", "", "7", "2", "2", "6" };
			marketPrice = new String[] { "1824.97", "497.95", "51.53", "116.15", "202.06", "51.53", "123.08",
					"124.83", "49.29", "116.15", "202.06", "3503.24", "162.03", "106.95", "102.17", "499.76" };
			costPrice = new String[] { "1622.11", "484.42", "45.00", "105.00", "190.00", "47.23", "121.24", "119.54",
					"47.68", "", "", "", "146.20", "97.6", "94.2", "489.42" };
			marketValue = new String[] { "1824.97", "2489.75", "360.71", "929.20", "1818.54", "721.42", "12431.08",
					"2995.92", "1084.38", "106400", "140000", "89600", "1134.21", "213.90", "204.34", "2998.56" };
			unrealPLMkt = new String[] { "+202.86", "+67.65", "+45.71", "+89.20", "+108.54", "+60.20", "+185.84",
					"+126.96", "+35.42", "+1191.68", "+1568.00", "+1003.52", "+110.81", "+18.70", "+15.94", "+62.04" };
			unrealPLMktSec = new String[] { "+212.86", "+638.30", "+40.03", "+99.20", "+118.54", "+71414.00",
					"+5733.00", "-720.00", "+440.00", "+1191.68", "+1568.00", "+1003.52", "+110.81", "+18.70", "+15.94", "" };
			RICCode = new String[] { "GOOGL.O", "LVMH.PA", "KO.N", "AXP.N", "BA.NQ", "CSCO.OQ", "AAPL.N", "GOOGL.N",
					"TXG.OQ", "FEURUSD", "FGBPUSD", "FCHFUSD", "AAPL.OQ", "GOOGL.OQ", "AMZN.OQ", "LVMH.PA" };
			weightPercentage = new String[] { "5.33", "7.28", "1.05", "2.72", "5.31", "2.11", "36.33", "8.76", "3.17",
					"8.33", "10.96", "7.01", "3.31", "0.63", "0.60", "8.76"};
			assetClass = new String[] { "Share", "Share", "Share", "Share", "Share", "Future", "Option", "Option",
					"Option", "Forward", "Forward", "Forward", "Share", "Share", "Share", "Share" };
			subAssetClass = new String[] { "Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares",
					"Ordinary Shares", "Equity Futures", "Share Options", "Share Options", "Share Options", "Forward",
					"Forward", "Forward", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares" };
			region = new String[] { "US", "US", "EU", "US", "US", "US", "US", "US", "US", "US", "US", "US", "US", "US", "US", "US", "EU" };
			sector = new String[] { "Computer Services", "Retail", "Beverages (Nonalcoholic)",
					"Consumer Financial Services", "Aerospace & Defense", "Retail", "Communications Equipment",
					"Computer Services", "Consumer Financial Services", "Consumer Financial Services", "Aerospace & Defense",
					"Retail", "Technology", "Communication Services", "Consumer Cyclical", "Retail" };
			exchangeRate = new String[] { "1", "0.82", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "0.82" };
			marketValPOS = new String[] { "1824.97", "2041.60", "360.71", "929.20", "1818.54", "721.42", "12431.08",
					"2995.92", "1084.38", "106400.00", "140000.00", "89600.00", "1134.21", "213.90", "204.34", "2458.82" };
			costValue = new String[] { "1622.11", "2422.10", "315.00", "840.00", "1710.00", "661.22", "12245.24",
					"2868.96", "1048.96", "", "", "", "1023.40", "195.20", "188.40", "2936.52" };
			costValueSec = new String[] { "162.22", "968.84", "157.50", "84.00", "1197.00", "528.98", "11020.72",
					"2868.96", "1153.86", "", "", "", "921.06", "19.52", "26.38", "1174.61" };
			costExchangeRate = new String[] { "0.1", "0.4", "0.5", "0.1", "0.7", "0.8", "0.9", "1.0", "1.1", "1.2",
					"1.3", "1.4", "0.9", "0.1", "0.14", "0.4" };
			unRealizedPLPercentage = new String[] { "13", "3", "15", "11", "6", "9", "2", "4", "3", "", "",
					"", "11", "10", "8", "2" };
			unRealizedPLPercentageSec = new String[] { "", "", "", "", "", "", "", "", "", "", "",
					"", "", "", "", "" };
			dailyPL = new String[] { "145.5", "85.9", "44.5", "-30.2", "-20.4", "60.5", "75.6", "95.9", "54.5", "-20.2",
					"-40.4", "-54.5", "", "", "", "" };
			dailyPLPercentage = new String[] { "145.05", "85.9", "44.5", "-30.02", "-20.4", "70.5", "85.6", "75.9",
					"64.5", "-10.2", "-50.4", "-64.5", "", "", "", "" };
			isSecurityAsset = new boolean[] { true, true, true, true, true, false, false, false, false, false, false,
					false, true, true, true, true };
			isAdvisory = new boolean[] { false, false, false, false, false, false, false, false, false, false, false,
					false, false, false, false, false };
			application = new String[] { "SC", "SC", "SC", "SC", "SC", "DX", "DX", "DX", "DX", "DX", "DX", "DX", "SC", "SC", "SC", "SC" };

			amountBought = new String[] { "95000.00", "125000.00", "80000.00" };
			amountSold = new String[] { "35000.00", "24000.00", "25000.00" };
			quote = new String[] { "0.75", "0.73", "0.65" };
			costQuote = new String[] { "0.86", "0.83", "0.71" };
			counterpartAmount = new String[] { "35000.00", "24000.00", "25000.00" };
			}else
			{
				holdingsId = new String[] { "100027-000", "100051-000", "100016-000", "100020-000", "100086-000",
						"100098-000", "100099-000", "100028-000", "100029-000", "100030-000", "100130-000", "100230-000"};
				instrumentId = new String[] { "100027-000", "100051-000", "100016-000", "100020-000", "100086-000",
						"100098-000", "100099-000", "100028-000", "100029-000", "100030-000", "100130-000", "100230-000" };
				description = new String[] { "Google LLC", "LVMH", "Coca-Cola Co", "American Express Company", "Boeing Co",
						"Cisco Systems, Inc.", "APPLE-CALL-115-16JUL", "GOOGLE-PUT-2300-16JUL", "CITI-CALL-70-16JUL",
						"Forward EURUSD 2901 2022", "Forward GBPUSD 2901 2022", "Forward CHFUSD 2901 2022"};
				ISIN = new String[] { "US02079K1079", "FR0000121014", "US1912161007", "US0258161092", "US0970231058",
						"US17275R1023", "US0378331005", "US02079K1079", "US1729671016", "", "", "" };
				exchange = new String[] { "NYSE", "Euronext Paris", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE",
						"", "", "" };
				secCCy = new String[] { "USD", "USD", "EUR", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD",
						"USD" };
				quantity = new String[] { "1", "5", "7", "8", "9", "14", "101", "24", "22", "", "", "" };
				marketPrice = new String[] { "1824.97", "497.95", "51.53", "116.15", "202.06", "51.53", "123.08",
						"124.83", "49.29", "116.15", "202.06", "3503.24"};
				costPrice = new String[] { "1622.11", "484.42", "45.00", "105.00", "190.00", "47.23", "121.24", "119.54",
						"47.68", "", "", "" };
				marketValue = new String[] { "1824.97", "2489.75", "360.71", "929.20", "1818.54", "721.42", "12431.08",
						"2995.92", "1084.38",  "106400", "140000", "89600" };
				unrealPLMkt = new String[] { "+202.86", "+67.65", "+45.71", "+89.20", "+108.54", "+60.20", "+185.84",
						"126.96", "+35.42", "+1191.68", "+1568.00", "+1003.52" };
				unrealPLMktSec = new String[] { "+212.86", "+638.30", "+40.03", "+99.20", "+118.54", "+71414.00",
						"+5733.00", "-720.00", "+440.00", "+1191.68", "+1568.00", "+1003.52" };
				RICCode = new String[] { "GOOGL.O", "LVMH.PA", "KO.N", "AXP.N", "BA.NQ", "CSCO.OQ", "AAPL.N", "GOOGL.N",
						"TXG.OQ", "FEURUSD", "FGBPUSD", "FCHFUSD"};
				weightPercentage = new String[] { "5.33", "7.28", "1.05", "2.72", "5.31", "2.11", "36.33", "8.76", "3.17",
						"8.33", "10.96", "7.01"};
				assetClass = new String[] { "Share", "Share", "Share", "Share", "Share", "Future", "Option", "Option",
						"Option", "Forward", "Forward", "Forward" };
				subAssetClass = new String[] { "Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares",
						"Ordinary Shares", "Equity Futures", "Share Options", "Share Options", "Share Options", "Forward",
						"Forward", "Forward" };
				region = new String[] { "US", "US", "EU", "US", "US", "US", "US", "US", "US", "US", "US", "US", "US" };
				sector = new String[] { "Computer Services", "Retail", "Beverages (Nonalcoholic)",
						"Consumer Financial Services", "Aerospace & Defense", "Retail", "Communications Equipment",
						"Computer services", "Consumer Financial Services", "Consumer Financial Services", "Aerospace & Defense",
						"Retail" };
				exchangeRate = new String[] { "1", "0.82", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1" };
				marketValPOS = new String[] { "1824.97", "2041.60", "360.71", "929.20", "1818.54", "721.42", "12431.08",
						"2995.92", "1084.38", "106400.00", "140000.00", "89600.00"};
				costValue = new String[] { "1622.11", "2422.1", "315.00", "840.00", "1710.00", "661.22", "12245.24",
						"2868.96", "1084.38", "", "", ""};
				costValueSec = new String[] { "162.211", "968.84", "157.50", "84.00", "1197.00", "528.976", "11020.716",
						"2868.96", "1048.96", "", "", "" };
				costExchangeRate = new String[] { "0.1", "0.4", "0.5", "0.1", "0.7", "0.8", "0.9", "1.0", "1.1", "1.2",
						"1.3", "1.4" };
				unRealizedPLPercentage = new String[] { "13", "3", "15", "11", "6", "9", "2", "4", "3", "", "",
						"" };
				unRealizedPLPercentageSec = new String[] { "", "", "", "", "", "", "", "", "", "", "",
						"" };
				dailyPL = new String[] { "145.5", "85.9", "44.5", "-30.2", "-20.4", "60.5", "75.6", "95.9", "54.5", "-20.2",
						"-40.4", "-54.5" };
				dailyPLPercentage = new String[] { "145.05", "85.9", "44.5", "-30.02", "-20.4", "70.5", "85.6", "75.9",
						"64.5", "-10.2", "-50.4", "-64.5" };
				isSecurityAsset = new boolean[] { true, true, true, true, true, false, false, false, false, false, false,
						false };
				isAdvisory = new boolean[] { false, false, false, false, false, false, false, false, false, false, false,
						false };
				application = new String[] { "SC", "SC", "SC", "SC", "SC", "DX", "DX", "DX", "DX", "DX", "DX", "DX" };

				amountBought = new String[] { "95000.00", "125000.00", "80000.00" };
				amountSold = new String[] { "35000.00", "24000.00", "25000.00" };
				quote = new String[] { "0.75", "0.73", "0.65" };
				costQuote = new String[] { "0.86", "0.83", "0.71" };
				counterpartAmount = new String[] { "35000.00", "24000.00", "25000.00" };
			}

		} else if (portfolioId.equalsIgnoreCase("100777-4")) {
			if(isIncludeOrders.equalsIgnoreCase("true")) {
			holdingsId = new String[] { "100077-000", "100021-000", "100050-000", "100051-000", "100016-000",
					"100020-000", "100086-000", "100017-000", "100118-000", "100019-000", "100122-000", "100093-000", "100077-000", "100021-000", "100050-000", "100017-000", "100086-000", "100016-000", "100022-000", "100014-000" };
			instrumentId = new String[] { "100077-000", "100021-000", "100050-000", "100051-000", "100016-000",
					"100020-000", "100086-000", "100017-000", "100118-000", "100019-000", "100122-000", "100093-000", "100077-000", "100021-000", "100050-000", "100017-000", "100086-000", "100016-000", "100022-000", "100014-000" };
			description = new String[] { "Alphabet", "Amazon.com Inc", "Apple", "LVMH", "Coca-Cola Co",
					"American Express Company", "BP Inc", "Barclays Inc", "HSBC Inc",
					"General Motors Company", "Hang seng Bank", "iShares Core S&P 500 UCITS ETF", "Alphabet", "Amazon.com Inc", "Apple", "Bank of America Corp", "Boeing Co", "Coca-Cola Co", "Pfizer Inc", "Walmart Inc"};
			status = new String[] { "", "", "", "", "", "", "", "", "", "", "", "", "Open", "Open", "Open", "Open", "Open", "Open", "Open", "Open"};
			ISIN = new String[] { "US02079K1079", "US0231351067", "US0378331005", "FR0000121014", "US1912161007",
					"US0258161092", "GB0007980591", "GB0031348658", "HK0005405286", "US37045V1008", "HK0000004322",
					"IE00B5BMR087", "US02079K1079", "US0231351067", "US0378331005", "US0605051046", "US0970231058", "US1912161007", "INE182A01018", "US9311421039" };
			exchange = new String[] { "NYSE", "NYSE", "NYSE", "Euronext Paris", "NYSE", "NYSE", "LSE", "LSE", "HSI",
					"NYSE", "HSI", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE" };
			secCCy = new String[] { "USD", "USD", "USD", "EUR", "USD", "USD", "GBP", "GBP", "HKD", "USD", "HKD",
					"USD", "USD", "USD", "USD", "USD", "USD", "USD", "EUR", "USD" };
			quantity = new String[] { "20", "122", "50", "45", "65", "34", "40", "65", "250", "26", "100", "120", "2", "2", "7", "12", "11", "6", "2", "6" };
			marketPrice = new String[] { "1824.97", "3203.53", "123.08", "497.95", "49.29", "116.15", "202.06", "465.80",
					"47.50", "52.04", "124.70", "309", "1824.97", "3203.53", "123.08", "30.94", "202.06", "49.29", "37.31", "148.91" };
			costPrice = new String[] { "1622.11", "2652.13", "98.08", "372.29", "45.00", "105.00", "190.00", "400.00",
					"45.00", "49.00", "119.00", "300.00", "1622.11", "2652.13", "98.08", "33.00", "190.00", "45.00", "34.00", "132.51" };
			marketValue = new String[] { "36499.40", "390830.66", "6154.00", "22407.75", "3203.85", "3949.10", "8082.40",
					"30277.00", "11875.00", "1353.04", "12470.00", "37050.00", "3649.94", "6407.06", "861.56", "371.28", "2222.66", "295.74", "74.62", "893.46" };
			unrealPLMkt = new String[] { "+4057.20", "+67270.80", "+1250.00", "+5654.70", "+278.85", "+379.10", "+482.40",
					"+4277.00", "+625.00", "+79.04", "+570.00", "+1050.00", "+405.72", "+1102.80", "+175.00", "-24.72", "+132.66", "+25.74", "+6.62", "+98.40" };
			unrealPLMktSec = new String[] { "+4057.20", "+67270.80", "+1250.00", "+5654.70", "+278.85", "+379.10", "+482.40",
					"+4277.00", "+625.00", "+79.04", "+9070.00", "+1050.00", "+405.72", "+1102.80", "+175.00", "-24.72", "+132.66", "+25.74", "+6.62", "+98.40" };
			RICCode = new String[] { "GOOGL.OQ", "AMZN.OQ", "AAPL.OQ", "LVMH.PA", "KO.N", "AXP.N", "BA.NQ", "BAC.N",
					"HI.N", "GM.N", "HSB.N", "IXMO461.DE", "GOOGL.OQ", "AMZN.OQ", "AAPL.OQ", "BAC.N", "BA.NQ", "KO.N", "PFE.N", "WMT.N" };
			weightPercentage = new String[] { "6.30", "67.44", "1.06", "3.87", "0.55", "0.68", "1.39", "5.22", "0.04",
					"0.23", "2.15", "6.39", "0.63", "1.11", "0.15", "0.06", "0.38", "0.05", "0.01", "0.15" };
			assetClass = new String[] { "Share", "Share", "Share", "Share", "Share", "Share", "Share", "Share",
					"Share", "Share", "Share", "Fund", "Share", "Share", "Share", "Share", "Share", "Share", "Share", "Share" };
			subAssetClass = new String[] { "Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares",
					"Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares",
					"Ordinary Shares", "Exchange Traded Funds", "Exchange Traded Funds", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares" };

			region = new String[] { "US", "US", "US", "EU", "US", "US", "UK", "UK", "Asia", "US", "Asia", "US", "US", "US", "US", "US", "US", "US", "EU", "US" };
			sector = new String[] { "Computer Services", "Consumer Cyclical", "Communications Equipment", "Retail",
					"Beverages (Nonalcoholic)", "Consumer Financial Services", "Energy Sector", "Regional Banks",
					"Regional Banks", "Auto & Truck Manufacturers", "Regional Banks", "Financial Services", "Computer Services", "Consumer Cyclical", "Communications Equipment", "Financial Services", "Industrials", "Beverages (Nonalcoholic)", "Healthcare", "Consumer Defensive" };
			exchangeRate = new String[] { "1", "1", "1", "0.82", "1", "1", "0.82", "0.82", "7.79", "1", "7.79", "1", "1", "1", "1", "1", "1", "1", "1", "1" };
			marketValPOS = new String[] { "36499.40", "390830.66", "6154.00", "18374.36", "3203.85", "3949.10", "6627.57",
					"24827.14", "92506.25", "1353.04", "97141.30", "37050.00", "3649.94", "6407.06", "861.56", "371.28", "2222.66", "295.74", "74.62", "893.46" };
			costValue = new String[] { "32442.20", "323559.86", "4904.00", "16753.05", "2925.00", "3570.00", "7600.00",
					"26000.00", "11250.00", "1274.00", "11900.00", "36000.00", "3244.22", "5304.26", "686.56", "396", "2090", "270", "68", "795.06" };
			costValueSec = new String[] { "32442.20", "323559.86", "4904.00", "13737.50", "2925.00", "3570.00", "6232.00",
					"21320.00", "87637.50", "1274.00", "92701", "36000.00", "3244.22", "5304.26", "686.56", "396", "2090", "270", "68", "795.06" };
			costExchangeRate = new String[] { "1", "1", "1", "0.82", "1", "1", "0.82", "0.82", "7.79", "1", "7.79", "1", "1", "1", "1", "1", "1", "1", "1", "1" };
			unRealizedPLPercentage = new String[] { "13", "21", "25", "34", "10", "11", "6", "16", "6", "6", "5",
					"3", "13", "21", "25", "-6", "6", "10", "10", "12" };
			unRealizedPLPercentageSec = new String[] { "", "", "", "", "", "", "", "", "", "", "",
			"", "", "", "", "", "", "", "", "" };
			dailyPL = new String[] { "145.5", "50.5", "65.6", "85.9", "44.5", "-30.2", "-20.4", "-10.45", "23.6",
					"-10.65", "90.5", "90.5", "145.5", "50.5", "65.6 ", "-4.89", "-20.4", "4.42", "1.56", "2.46" };
			dailyPLPercentage = new String[] { "0.03", "0.15", "0.12", "0.11", "0.14", "-0.92", "0.60", "-1.20", "0.56",
					"0.45", "0.83", "0.83", "0.03", "0.15", "0.12", "-0.34", "-20.4", "0.10", "0.14", "0.13" };
			isSecurityAsset = new boolean[] { false, false, false, false, false, false, false, false, false, false,
					false, false, false, false, false, false, false, false, false, false };
			isAdvisory = new boolean[] { true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true };
			application = new String[] { "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC" };
			}else {

				holdingsId = new String[] { "100077-000", "100021-000", "100050-000", "100051-000", "100016-000",
						"100020-000", "100086-000", "100017-000", "100118-000", "100019-000", "100122-000", "100093-000" };
				instrumentId = new String[] { "100077-000", "100021-000", "100050-000", "100051-000", "100016-000",
						"100020-000", "100086-000", "100017-000", "100118-000", "100019-000", "100122-000", "100093-000" };
				description = new String[] { "Alphabet", "Amazon.com Inc", "Apple", "LVMH", "Coca-Cola Co",
						"American Express Company", "BP Inc", "Barclays Inc", "HSBC Inc",
						"General Motors Company", "Hang seng Bank", "iShares Core S&P 500 UCITS ETF"};
				ISIN = new String[] { "US02079K1079", "US0231351067", "US0378331005", "FR0000121014", "US1912161007",
						"US0258161092", "GB0007980591", "GB0031348658", "HK0005405286", "US37045V1008", "HK0000004322",
						"IE00B5BMR087" };
				exchange = new String[] { "NYSE", "NYSE", "NYSE", "Euronext Paris", "NYSE", "NYSE", "LSE", "LSE", "HSI",
						"NYSE", "HSI", "NYSE"};
				secCCy = new String[] { "USD", "USD", "USD", "EUR", "USD", "USD", "GBP", "GBP", "HKD", "USD", "HKD",
						"USD" };
				quantity = new String[] { "20", "122", "50", "45", "65", "34", "40", "65", "250", "26", "100", "120" };
				marketPrice = new String[] { "1824.97", "3203.53", "123.08", "497.95", "49.29", "116.15", "202.06", "465.80",
						"47.50", "52.04", "124.70", "309" };
				costPrice = new String[] { "1622.11", "2652.13", "98.08", "372.29", "45.00", "105.00", "190.00", "400.00",
						"45.00", "49.00", "119.00", "300.00"};
				marketValue = new String[] { "36499.40", "390830.66", "6154.00", "22407.75", "3203.85", "3949.10", "8082.40",
						"30277.00", "11875.00", "1353.04", "12470.00", "37050.00"};
				unrealPLMkt = new String[] { "+4057.20", "+67270.80", "+1250.00", "+5654.70", "+278.85", "+379.10", "+482.40",
						"+4277.00", "+625.00", "+79.04", "+570.00", "+1050.00"};
				unrealPLMktSec = new String[] { "+4057.20", "+67270.80", "+1250.00", "+5654.70", "+278.85", "+379.10", "+482.40",
						"+4277.00", "+625.00", "+79.04", "+9070.00", "+1050.00" };
				RICCode = new String[] { "GOOGL.OQ", "AMZN.OQ", "AAPL.OQ", "LVMH.PA", "KO.N", "AXP.N", "BA.NQ", "BAC.N",
						"HI.N", "GM.N", "HSB.N", "IXMO461.DE" };
				weightPercentage = new String[] { "6.30", "67.44", "1.06", "3.87", "0.55", "0.68", "1.39", "5.22", "0.04",
						"0.23", "2.15", "6.39"};
				assetClass = new String[] { "Share", "Share", "Share", "Share", "Share", "Share", "Share", "Share",
						"Share", "Share", "Share", "Fund" };
				subAssetClass = new String[] { "Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares",
						"Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares",
						"Ordinary Shares", "Exchange Traded Funds", "Exchange Traded Funds"};

				region = new String[] { "US", "US", "US", "EU", "US", "US", "UK", "UK", "Asia", "US", "Asia", "US" };
				sector = new String[] { "Computer Services", "Consumer Cyclical", "Communications Equipment", "Retail",
						"Beverages (Nonalcoholic)", "Consumer Financial Services", "Energy Sector", "Regional Banks",
						"Regional Banks", "Auto & Truck Manufacturers", "Regional Banks", "Financial Services"};
				exchangeRate = new String[] { "1", "1", "1", "0.82", "1", "1", "0.82", "0.82", "7.79", "1", "7.79", "1" };
				marketValPOS = new String[] { "36499.40", "390830.66", "6154.00", "18374.36", "3203.85", "3949.10", "6627.57",
						"24827.14", "92506.25", "1353.04", "97141.30", "37050.00" };
				costValue = new String[] { "32442.20", "323559.86", "4904.00", "16753.05", "2925.00", "3570.00", "7600.00",
						"26000.00", "11250.00", "1274.00", "11900.00", "36000.00"};
				costValueSec = new String[] { "32442.20", "323559.86", "4904.00", "13737.50", "2925.00", "3570.00", "6232.00",
						"21320.00", "87637.50", "1274.00", "92701", "36000.00"};
				costExchangeRate = new String[] { "1", "1", "1", "0.82", "1", "1", "0.82", "0.82", "7.79", "1", "7.79", "1" };
				unRealizedPLPercentage = new String[] { "13", "21", "25", "34", "10", "11", "6", "16", "6", "6", "5",
						"3" };
				unRealizedPLPercentageSec = new String[] { "", "", "", "", "", "", "", "", "", "", "",
				"" };
				dailyPL = new String[] { "145.5", "50.5", "65.6", "85.9", "44.5", "-30.2", "-20.4", "-10.45", "23.6",
						"-10.65", "90.5", "90.5" };
				dailyPLPercentage = new String[] { "0.03", "0.15", "0.12", "0.11", "0.14", "-0.92", "0.60", "-1.20", "0.56",
						"0.45", "0.83", "0.83" };
				isSecurityAsset = new boolean[] { false, false, false, false, false, false, false, false, false, false,
						false, false };
				isAdvisory = new boolean[] { true, true, true, true, true, true, true, true, true, true, true, true };
				application = new String[] { "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC"};
				
				
			}
		} else if (portfolioId.equalsIgnoreCase("100777-5")) {
			if(isIncludeOrders.equalsIgnoreCase("true")) {
			holdingsId = new String[] { "100077-000", "100021-000", "100050-000", "100014-000", "100016-000",
					"100020-000", "100086-000", "100158-000", "100095-000", "100118-000", "100122-000", "100020-000", "100017-000", "100086-000", "100018-000", "100016-000", "100019-000", "100022-000", "100014-000" };
			instrumentId = new String[] { "100077-000", "100021-000", "100050-000", "100014-000", "100016-000",
					"100020-000", "100086-000", "100158-000", "100095-000", "100118-000", "100122-000", "100020-000", "100017-000", "100086-000", "100018-000", "100016-000", "100019-000", "100022-000", "100014-000" };
			description = new String[] { "Alphabet", "Amazon.com Inc", "Apple", "Walmart Inc", "Coca-Cola Co",
					"BP Inc", "Barclays Inc", "Nestle", "Novartis", "HSBC Inc",
					"Hang Seng Bank", "American Express Company", "Bank of America Corp", "Boeing Co", "Citigroup Inc", "Coca-Cola Co", "General Motors Company", "Pfizer Inc", "Walmart Inc" };
			status = new String[] { "", "", "", "", "", "", "", "", "", "", "", "Open", "Open", "Open", "Open", "Open", "Open", "Open", "Open" };
			ISIN = new String[] { "US02079K1079", "US0231351067", "US0378331005", "US9311421039", "US1912161007",
					"GB0007980591", "GB0031348658", "CH0038863350", "CH0012005267", "HK0005405286", "HK0000004322", "US0258161092", "US0605051046", "US0970231058", "US1729671016", "US1912161007", "US37045V1008", "INE182A01018", "US9311421039" };
			exchange = new String[] { "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "LSE", "LSE", "SWX", "SWX", "HSI",
					"HSI", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE" };
			secCCy = new String[] { "USD", "USD", "USD", "USD", "USD", "GBP", "GBP", "EUR", "EUR", "HKD", "HKD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD" };
			quantity = new String[] { "2", "65", "27", "24", "6", "25", "46", "44", "25", "250", "100", "13", "12", "11", "15", "11", "9", "9", "12" };
			marketPrice = new String[] { "1824.97", "3203.53", "123.08", "148.91", "49.29", "202.06", "465.80",
					"145.00", "86.96", "47.50", "124.70", "306.67", "30.94", "202.06", "60.91", "49.29", "52.04", "37.31", "150.51" };
			costPrice = new String[] { "1622.11", "2652.13", "98.08", "132.51", "42.00", "190.00", "400.00", "131.00",
					"82.00", "45.00", "119.00", "105.00", "33.00", "190.00", "55.00", "42.00", "50.00", "32.00", "120.20" };
			marketValue = new String[] { "3649.94", "208229.45", "3323.16", "3573.84", "295.74", "5051.50", "21426.80",
					"6380.00", "2174.00", "11875.00", "12470.00", "3986.71", "371.28", "2222.66", "913.65", "542.19", "468.36", "335.79", "1806.12" };
			unrealPLMkt = new String[] { "405.72", "35841.00", "675.00", "393.60", "43.74", "301.50", "3026.80", "616.00",
					"136.50", "625.00", "570.00", "+364.00", "-24.72", "+364.00", "+88.65", "+80.19", "18.36", "47.79", "+364.00" };
			unrealPLMktSec = new String[] { "405.72", "35841.00", "675.00", "393.60", "43.74", "301.50", "3026.80", "616.00",
					"136.50", "625.00", "570.00", "+364.00", "-24.72", "+364.00", "+88.65", "+80.19", "18.36", "47.79", "+364.00"  };
			RICCode = new String[] { "GOOGL.OQ", "AMZN.OQ", "AAPL.OQ", "WMT.N", "KO.N", "AXP.N", "BA.NQ", "NESN.S",
					"NOVN.S", "HI.N", "HSB.N", "AXP.N", "BAC.N", "BA.NQ", "C.N", "KO.N", "GM.N", "PFE.N", "WMT.N" };
			weightPercentage = new String[] { "1.31", "74.70", "1.19", "1.28", "0.11", "1.81", "7.69", "2.29", "0.78",
					"4.26", "4.47", "1.43", "0.06", "0.80", "0.16", "0.19", "0.08", "0.06", "0.65" };
			assetClass = new String[] { "Share", "Share", "Share", "Share", "Share", "Share", "Share", "Share",
					"Share", "Share", "Share", "Share", "Share", "Share", "Share", "Share", "Share", "Share", "Share" };
			subAssetClass = new String[] { "Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares",
					"Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares",
					"Ordinary Shares", "Exchange Traded Funds", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares" };

			region = new String[] { "US", "US", "US", "US", "US", "UK", "UK", "EU", "EU", "Asia", "Asia", "US", "US", "UK", "US", "US", "US", "US", "US" };
			sector = new String[] { "Computer Services", "Consumer Cyclical", "Communications Equipment", "Retail",
					"Beverages (Nonalcoholic)","Energy Sector", "Regional Banks", "Food processing",
					"Pharmaceuticals", "Regional Banks", "Regional Banks", "Consumer Cyclical", "Financial Services", "Industrials", "Financial Services", "Beverages (Nonalcoholic)", "Consumer Cyclical", "Healthcare", "Retail" };
			exchangeRate = new String[] { "1", "1", "1", "1", "1", "0.82", "0.82", "0.93", "0.93", "7.79", "7.79", "1", "1", "1", "1", "1", "1", "1", "1" };
			marketValPOS = new String[] { "3649.94", "208229.45", "3323.16", "3573.84", "295.74", "4142.23", "17569.98",
					"5933.40", "2021.82", "92506.25", "97141.30", "3986.71", "371.28", "2222.66", "913.65", "542.19", "468.36", "335.79", "1806.12" };
			costValue = new String[] { "3244.22", "172388.45", "2648.16", "3180.24", "252.00", "4750.00", "18400.00",
					"5764.00", "2050.00", "11250.00", "11900.00", "1365.00", "396.00", "2090.00", "825.00", "462.00", "450.00", "288.00", "1442.40" };
			costValueSec = new String[] { "3244.22", "172388.45", "2648.16", "3180.24", "252.00", "3895.00", "15088.00",
					"5360.52", "1906.50", "87637.50", "92701.00", "1365.00", "396.00", "2090.00", "825.00", "462.00", "450.00", "288.00", "1442.40" };
			costExchangeRate = new String[] { "1", "1", "1", "1", "1", "0.82", "0.82", "0.93", "0.93", "7.79", "7.79", "1", "1", "1", "1", "1", "1", "1", "1" };
			unRealizedPLPercentage = new String[] { "13", "21", "25", "12", "17", "6", "16", "11", "7", "6", "5", "27", "-6", "17", "11", "17", "4", "17", "25" };
			unRealizedPLPercentageSec = new String[] { "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "" };
			dailyPL = new String[] { "5.34", "4.56", "-1.45", "2.46", "4.42", "-3.56", "4.44", "-4.89", "6.9", "2.24",
					"1.56", "-30.2", "-4.89", "-20.4", "6.9", "4.42", "2.24", "1.56", "2.46" };
			dailyPLPercentage = new String[] { "0.10", "0.11", "-0.34", "0.13", "0.10", "-0.23", "0.10", "-0.34",
					"0.32", "0.12", "0.14", "-0.92", "-0.34", "-20.4", "0.32", "0.10", "0.12", "0.14", "0.13" };
			isSecurityAsset = new boolean[] { false, false, false, false, false, false, false, false, false, false,
					false, false, false, false, false, false, false, false, false };
			isAdvisory = new boolean[] { true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true };
			application = new String[] { "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC" };
			} else {

				holdingsId = new String[] { "100077-000", "100021-000", "100050-000", "100014-000", "100016-000",
						"100020-000", "100086-000", "100158-000", "100095-000", "100118-000", "100122-000" };
				instrumentId = new String[] { "100077-000", "100021-000", "100050-000", "100014-000", "100016-000",
						"100020-000", "100086-000", "100158-000", "100095-000", "100019-000", "100022-000" };
				description = new String[] { "Alphabet", "Amazon.com Inc", "Apple", "Walmart Inc", "Coca-Cola Co",
						"BP Inc", "Barclays Inc", "Nestle", "Novartis", "HSBC Inc",
						"Hang Seng Bank" };
				ISIN = new String[] { "US02079K1079", "US0231351067", "US0378331005", "US9311421039", "US1912161007",
						"GB0007980591", "GB0031348658", "CH0038863350", "CH0012005267", "HK0005405286", "HK0000004322" };
				exchange = new String[] { "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "LSE", "LSE", "SWX", "SWX", "HSI",
						"HSI" };
				secCCy = new String[] { "USD", "USD", "USD", "USD", "USD", "GBP", "GBP", "EUR", "EUR", "HKD", "HKD" };
				quantity = new String[] { "2", "65", "27", "24", "6", "25", "46", "44", "25", "250", "100"};
				marketPrice = new String[] { "1824.97", "3203.53", "123.08", "148.91", "49.29", "202.06", "465.80",
						"145.00", "86.96", "47.50", "124.70" };
				costPrice = new String[] { "1622.11", "2652.13", "98.08", "132.51", "42.00", "190.00", "400.00", "131.00",
						"82.00", "45.00", "119.00" };
				marketValue = new String[] { "3649.94", "208229.45", "3323.16", "3573.84", "295.74", "5051.50", "21426.80",
						"6380.00", "2174.00", "11875.00", "12470.00" };
				unrealPLMkt = new String[] { "405.72", "35841.00", "675.00", "393.60", "43.74", "301.50", "3026.80", "616.00",
						"136.50", "625.00", "570.00" };
				unrealPLMktSec = new String[] { "405.72", "35841.00", "675.00", "393.60", "43.74", "301.50", "3026.80", "616.00",
						"136.50", "625.00", "570.00" };
				RICCode = new String[] { "GOOGL.OQ", "AMZN.OQ", "AAPL.OQ", "WMT.N", "KO.N", "AXP.N", "BA.NQ", "NESN.S",
						"NOVN.S", "HI.N", "HSB.N"};
				weightPercentage = new String[] { "1.31", "74.70", "1.19", "1.28", "0.11", "1.81", "7.69", "2.29", "0.78",
						"4.26", "4.47" };
				assetClass = new String[] { "Share", "Share", "Share", "Share", "Share", "Share", "Share", "Share",
						"Share", "Share", "Share" };
				subAssetClass = new String[] { "Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares",
						"Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares",
						"Ordinary Shares", "Exchange Traded Funds" };

				region = new String[] { "US", "US", "US", "US", "US", "UK", "UK", "EU", "EU", "Asia", "Asia" };
				sector = new String[] { "Computer Services", "Consumer Cyclical", "Communications Equipment", "Retail",
						"Beverages (Nonalcoholic)","Energy Sector", "Regional Banks", "Food processing",
						"Pharmaceuticals", "Regional Banks", "Regional Banks" };
				exchangeRate = new String[] { "1", "1", "1", "1", "1", "0.82", "0.82", "0.93", "0.93", "7.79", "7.79" };
				marketValPOS = new String[] { "3649.94", "208229.45", "3323.16", "3573.84", "295.74", "4142.23", "17569.98",
						"5933.40", "2021.82", "92506.25", "97141.30" };
				costValue = new String[] { "3244.22", "172388.45", "2648.16", "3180.24", "252.00", "4750.00", "18400.00",
						"5764.00","2050.00", "11250.00", "11900.00", };
				costValueSec = new String[] { "3244.22", "172388.45", "2648.16", "3180.24", "252.00", "3895.00", "15088.00",
						"5764.00", "2037.50", "87637.50", "92701.00"};
				costExchangeRate = new String[] { "1", "1", "1", "1", "1", "0.82", "0.82", "0.93", "0.93", "7.79", "7.79" };
				unRealizedPLPercentage = new String[] { "13", "21", "25", "12", "17", "6", "16", "11", "7", "6", "5" };
				unRealizedPLPercentageSec = new String[] { "13", "21", "25", "12", "17", "6", "16", "11", "7", "6", "267" };
				dailyPL = new String[] { "5.34", "4.56", "-1.45", "2.46", "4.42", "-3.56", "4.44", "-4.89", "6.9", "2.24",
						"1.56" };
				dailyPLPercentage = new String[] { "0.10", "0.11", "-0.34", "0.13", "0.10", "-0.23", "0.10", "-0.34",
						"0.32", "0.12", "0.14" };
				isSecurityAsset = new boolean[] { false, false, false, false, false, false, false, false, false, false,
						false };
				isAdvisory = new boolean[] { true, true, true, true, true, true, true, true, true, true, true };
				application = new String[] { "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC" };
				
			}
		}

		for (int i = 0; i < description.length; i++) {
			JSONObject holdingsObj = new JSONObject();
			holdingsObj.put(TemenosConstants.HOLDINGSID, holdingsId[i]);
			holdingsObj.put(TemenosConstants.INSTRUMENTID, instrumentId[i]);
			holdingsObj.put(TemenosConstants.DESCRIPTION, description[i]);
			holdingsObj.put("ISIN", ISIN[i]);
			holdingsObj.put("holdingsType", exchange[i]);
			holdingsObj.put(TemenosConstants.QUANTITY, quantity[i]);
			holdingsObj.put(TemenosConstants.MARKETPRICE, marketPrice[i]);
			holdingsObj.put(TemenosConstants.COSTPRICE, costPrice[i]);
			holdingsObj.put(TemenosConstants.MARKETVALUE, marketValue[i]);
			holdingsObj.put(TemenosConstants.UNREALPLMKT, unrealPLMkt[i]);
			holdingsObj.put("secCCy", secCCy[i]);
			holdingsObj.put(TemenosConstants.RICCODE, RICCode[i]);
			holdingsObj.put(TemenosConstants.UNREALPLMKTSECCCY, unrealPLMktSec[i]);
			holdingsObj.put(TemenosConstants.UNREALIZEDPLPERCENTAGESECCCY, unRealizedPLPercentageSec[i]);
			holdingsObj.put(TemenosConstants.COSTVALUESECCCY, costValueSec[i]);
			holdingsObj.put(TemenosConstants.WEIGHTPERCENTAGE, weightPercentage[i]);
			holdingsObj.put(TemenosConstants.ASSETCLASS, assetClass[i]);
			holdingsObj.put(TemenosConstants.SUBASSETCLASS, subAssetClass[i]);
			holdingsObj.put(TemenosConstants.REGION, region[i]);
			holdingsObj.put(TemenosConstants.SECTOR, sector[i]);
			holdingsObj.put(TemenosConstants.EXCHANGERATE, exchangeRate[i]);
			holdingsObj.put(TemenosConstants.MARKETVALPOS, marketValPOS[i]);
			holdingsObj.put(TemenosConstants.COSTVALUE, costValue[i]);
			holdingsObj.put(TemenosConstants.COSTEXCHANGERATE, costExchangeRate[i]);
			holdingsObj.put(TemenosConstants.UNREALIZEDPLPERCENTAGE, unRealizedPLPercentage[i]);
			holdingsObj.put(TemenosConstants.DAILYPL, dailyPL[i]);
			holdingsObj.put(TemenosConstants.DAILYPLPERCENTAGE, dailyPLPercentage[i]);
			holdingsObj.put(TemenosConstants.ISSECURITYASSET, isSecurityAsset[i]);
			holdingsObj.put(TemenosConstants.APPLICATION, application[i]);
			holdingsObj.put(TemenosConstants.ISADVISORY, isAdvisory[i]);
			if(isIncludeOrders.equalsIgnoreCase("true")) {
			holdingsObj.put(TemenosConstants.STATUS, status[i]);
			}

			if (portfolioId.equalsIgnoreCase("100777-1")) {
				/*
				 * if (i >= 12) { holdingsObj.put("averageCost", averageCost[averageCostIndex]);
				 * averageCostIndex++; } if (i >= 15 && i <= 17) { holdingsObj.put("nominal",
				 * nominal[nominalIndex]); nominalIndex++; }
				 */
				if (i >= 13 && i <= 15) {
					holdingsObj.put("accruedInterest", accruedInterest[accruedInterestIndex]);
					accruedInterestIndex++;
				}
			} else if (portfolioId.equalsIgnoreCase("100777-2")) {
				/*
				 * if (i >= 11) { holdingsObj.put("averageCost", averageCost[averageCostIndex]);
				 * averageCostIndex++; } if (i >= 13) { holdingsObj.put("nominal",
				 * nominal[nominalIndex]); nominalIndex++; }
				 */
				if (i >= 11 && i <= 13) {
					holdingsObj.put("accruedInterest", accruedInterest[accruedInterestIndex]);
					accruedInterestIndex++;
				}
				if (i >= 11 && i <= 13) {
					holdingsObj.put("balance", balance[balanceIndex]);
					balanceIndex++;
				}
				/*
				 * if(i>=13 && i<=15) { holdingsObj.remove(TemenosConstants.MARKETVALPOS);
				 * holdingsObj.remove(TemenosConstants.UNREALPLMKTSECCCY);
				 * holdingsObj.remove(TemenosConstants.UNREALIZEDPLPERCENTAGESECCCY);
				 * holdingsObj.remove(TemenosConstants.UNREALIZEDPLPERCENTAGE);
				 * holdingsObj.remove(TemenosConstants.UNREALPLMKT);
				 * holdingsObj.remove(TemenosConstants.COSTPRICE); }
				 */
			} else if (portfolioId.equalsIgnoreCase("100777-3")) {
				/*
				 * if (i >= 7 && i <= 10) { holdingsObj.put("averageCost",
				 * averageCost[averageCostIndex]); averageCostIndex++;
				 * holdingsObj.put("nominal", nominal[nominalIndex]); nominalIndex++; }
				 */
				if (i >= 13) {
					holdingsObj.put("amountBought", amountBought[accruedInterestIndex]);
					accruedInterestIndex++;
					holdingsObj.put("amountSold", amountSold[amountSoldIndex]);
					amountSoldIndex++;
					holdingsObj.put("quote", quote[quoteIndex]);
					quoteIndex++;
					holdingsObj.put("costQuote", costQuote[costQuoteIndex]);
					costQuoteIndex++;
					holdingsObj.put("counterpartAmount", counterpartAmount[counterpartAmountIndex]);
					counterpartAmountIndex++;
					// holdingsObj.remove(TemenosConstants.QUANTITY);
					// holdingsObj.remove(TemenosConstants.COSTPRICE);
				}

			}

			holdingsArr.put(holdingsObj);
		}
		
		diagnostic.prepareDebug("==========> GetHoldingsListMockPostProcessor Mock -  No. of records returned: "+holdingsArr.length()).log();
		// holdingsArr.put(holdingsObj);
		JSONArray sortedJSON = new JSONArray();
		if (sortBy.equals("") || sortBy.equalsIgnoreCase(TemenosConstants.DESCRIPTION)) {
			List<JSONObject> jsonValues = new ArrayList<JSONObject>();
			for (int i = 0; i < holdingsArr.length(); i++) {
				jsonValues.add(holdingsArr.getJSONObject(i));
			}
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

			if (sortType != null && sortType.equalsIgnoreCase(TemenosConstants.DESCENDING)) {
				for (int i = holdingsArr.length() - 1; i >= 0; i--) {
					sortedJSON.put(jsonValues.get(i));
				}
			} else {
				for (int i = 0; i < holdingsArr.length(); i++) {
					sortedJSON.put(jsonValues.get(i));
				}
			}

		} else if (sortBy.equalsIgnoreCase(TemenosConstants.ASSETCLASS)
				|| sortBy.equalsIgnoreCase(TemenosConstants.REGION) || sortBy.equalsIgnoreCase(TemenosConstants.SECTOR)
				|| sortBy.equalsIgnoreCase(TemenosConstants.SECCCY)
				|| sortBy.equalsIgnoreCase(TemenosConstants.STATUS)) {
			List<JSONObject> jsonValues = new ArrayList<JSONObject>();
			for (int i = 0; i < holdingsArr.length(); i++) {
				jsonValues.add(holdingsArr.getJSONObject(i));
			}
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

			if (sortType != null && sortType.equalsIgnoreCase(TemenosConstants.DESCENDING)) {
				for (int i = holdingsArr.length() - 1; i >= 0; i--) {
					sortedJSON.put(jsonValues.get(i));
				}
			} else {
				for (int i = 0; i < holdingsArr.length(); i++) {
					sortedJSON.put(jsonValues.get(i));
				}
			}

		} else {
			List<JSONObject> jsonValues = new ArrayList<JSONObject>();
			for (int i = 0; i < holdingsArr.length(); i++) {
				jsonValues.add(holdingsArr.getJSONObject(i));
			}
			Collections.sort(jsonValues, new Comparator<JSONObject>() {
				private final String KEY_NAME = sortBy;

				@Override
				public int compare(JSONObject a, JSONObject b) {
					Double dbl1 = null;
					Double dbl2 = null;
					dbl1 = (a.has(KEY_NAME) && a.get(KEY_NAME).toString().length() > 0)
							? Double.parseDouble((a.get(KEY_NAME)).toString())
							: 0;
					dbl2 = (b.has(KEY_NAME) && b.get(KEY_NAME).toString().length() > 0)
							? Double.parseDouble((b.get(KEY_NAME)).toString())
							: 0;
					// str1 = (Double) a.getDouble(KEY_NAME);
					// str2 = (Double) b.getDouble(KEY_NAME);
					return dbl1.compareTo(dbl2);
				}
			});
			if (sortType != null && sortType.equalsIgnoreCase(TemenosConstants.DESCENDING)) {
				for (int i = holdingsArr.length() - 1; i >= 0; i--) {
					sortedJSON.put(jsonValues.get(i));
				}

			} else {
				for (int i = 0; i < holdingsArr.length(); i++) {
					sortedJSON.put(jsonValues.get(i));
				}
			}
		}
		diagnostic.prepareDebug("==========> GetHoldingsListMockPostProcessor Mock -  No. of records returned after sort: "+sortedJSON.length()).log();
		if (search.equals("")) {
			diagnostic.prepareDebug("==========> GetHoldingsListMockPostProcessor Mock -  No search" ).log();
		} else {
			sortedJSON = PortfolioServiceUtils.returnHoldingsSearch(sortedJSON, search, "");
			diagnostic.prepareDebug("==========> GetHoldingsListMockPostProcessor Mock -  No. of records returned after search: "+sortedJSON.length()).log();
		}

		if (instrumentid.equals("")) {
			diagnostic.prepareDebug("==========> GetHoldingsListMockPostProcessor Mock -  No instrument search").log();
		} else {
			sortedJSON = PortfolioServiceUtils.returnHoldingsInstrumentID(sortedJSON, instrumentid);
			diagnostic.prepareDebug("==========> GetHoldingsListMockPostProcessor Mock -  No. of records returned after instrument search: "+sortedJSON.length()).log();
		}

		totalCount = sortedJSON.length();
		if (limit > 0 && offset >= 0) {
			sortedJSON = PortfolioWealthUtils.pagination(sortedJSON, limit, offset);
			diagnostic.prepareDebug("==========> GetHoldingsListMockPostProcessor Mock -  No. of records returned after pagination: "+sortedJSON.length()).log();
		}
		if (portfolioId.equalsIgnoreCase("100777-1")) {
			responseVal.put("portfolioID", portfolioId);
			responseVal.put("referenceCurrency", "USD");
			responseVal.put("marketValue", "34243.82");
			responseVal.put("unRealizedPL", "P");
			responseVal.put("unRealizedPLAmount", "5023.50");
			responseVal.put("unRealizedPLPercentage", "21");
			responseVal.put("todayPL", "P");
			responseVal.put("todayPLAmount", "498.23");
			responseVal.put("accountName", "John Bailey Portfolio 1.");
			responseVal.put("accountNumber", "100777-1");
			responseVal.put("todayPLPercentage", "2.12");
		} else if (portfolioId.equalsIgnoreCase("100777-2")) {
			responseVal.put("referenceCurrency", "USD");
			responseVal.put("marketValue", "16543.15");
			responseVal.put("unRealizedPL", "P");
			responseVal.put("unRealizedPLAmount", "2174.55");
			responseVal.put("unRealizedPLPercentage", "15");
			responseVal.put("todayPL", "P");
			responseVal.put("todayPLAmount", "1593.21");
			responseVal.put("todayPLPercentage", "11.09");
			responseVal.put("accountName", "John Bailey Portfolio 2.");
			responseVal.put("accountNumber", "100777-2");
			responseVal.put("portfolioID", portfolioId);
		} else if (portfolioId.equalsIgnoreCase("100777-3")) {
			responseVal.put("referenceCurrency", "USD");
			responseVal.put("marketValue", "24655.94");
			responseVal.put("unRealizedPL", "P");
			responseVal.put("unRealizedPLAmount", "4065.93");
			responseVal.put("unRealizedPLPercentage", "20");
			responseVal.put("todayPL", "P");
			responseVal.put("todayPLAmount", "46.00");
			responseVal.put("todayPLPercentage", "1.15");
			responseVal.put("accountName", "John Bailey Portfolio 3.");
			responseVal.put("accountNumber", "100777-3");
			responseVal.put("portfolioID", portfolioId);
		} else if (portfolioId.equalsIgnoreCase("100777-4")) {
			responseVal.put("referenceCurrency", "USD");
			responseVal.put("marketValue", "48881.31");
			responseVal.put("unRealizedPL", "P");
			responseVal.put("unRealizedPLAmount", "4532.38");
			responseVal.put("unRealizedPLPercentage", "9.22");
			responseVal.put("todayPL", "P");
			responseVal.put("todayPLAmount", "510.12");
			responseVal.put("todayPLPercentage", "1.04");
			responseVal.put("accountName", "John Bailey Portfolio 4.");
			responseVal.put("accountNumber", "100777-4");
			responseVal.put("portfolioID", portfolioId);
		} else if (portfolioId.equalsIgnoreCase("100777-5")) {
			responseVal.put("referenceCurrency", "USD");
			responseVal.put("marketValue", "18191.27");
			responseVal.put("unRealizedPL", "P");
			responseVal.put("unRealizedPLAmount", "2331.69");
			responseVal.put("unRealizedPLPercentage", "14.99");
			responseVal.put("todayPL", "P");
			responseVal.put("todayPLAmount", "22.11");
			responseVal.put("todayPLPercentage", "0.14");
			responseVal.put("accountName", "John Bailey Portfolio 5.");
			responseVal.put("accountNumber", "100777-5");
			responseVal.put("portfolioID", portfolioId);
		} else {
			responseVal.put("portfolioID", portfolioId);
			responseVal.put("referenceCurrency", "USD");
			responseVal.put("marketValue", "34243.82");
			responseVal.put("unRealizedPL", "P");
			responseVal.put("unRealizedPLAmount", "5023.50");
			responseVal.put("unRealizedPLPercentage", "21");
			responseVal.put("todayPL", "P");
			responseVal.put("todayPLAmount", "498.23");
			responseVal.put("accountName", "John Bailey Portfolio 1.");
			responseVal.put("accountNumber", "100777-1");
			responseVal.put("todayPLPercentage", "2.12");
		}
		String[] colArray = new String[] { TemenosConstants.HOLDINGSID, "holdingsType", TemenosConstants.MARKETPRICE,
				"ISIN", TemenosConstants.MARKETVALPOS, TemenosConstants.WEIGHTPERCENTAGE, TemenosConstants.UNREALPLMKT,
				TemenosConstants.REGION, TemenosConstants.ASSETCLASS, TemenosConstants.SECTOR, TemenosConstants.SECCCY,
				TemenosConstants.MARKETVALUE, TemenosConstants.COSTVALUE, TemenosConstants.UNREALIZEDPLPERCENTAGE,
				TemenosConstants.QUANTITY, TemenosConstants.COSTPRICE, TemenosConstants.DESCRIPTION,
				TemenosConstants.RICCODE, TemenosConstants.EXCHANGERATE, TemenosConstants.COSTEXCHANGERATE,
				TemenosConstants.DAILYPL, TemenosConstants.DAILYPLPERCENTAGE,
				TemenosConstants.UNREALIZEDPLPERCENTAGESECCCY, TemenosConstants.UNREALPLMKTSECCCY,
				TemenosConstants.COSTVALUESECCCY, TemenosConstants.STATUS };

		String fieldValue = Arrays.toString(colArray).replace("[", "").replace("]", "");
		responseVal.put("portfolioID", portfolioId);
		responseVal.put("portfolioHoldings", sortedJSON);
		responseVal.put("fieldstoDisplay", fieldValue);
		responseVal.put(TemenosConstants.SORTBY, sortBy);
		responseVal.put("totalCount", totalCount);
		responseVal.put("opstatus", "0");
		responseVal.put("httpStatusCode", "200");
		Result final_result = Utilities.constructResultFromJSONObject(responseVal);
		final_result.addOpstatusParam("0");
		final_result.addHttpStatusCodeParam("200");
		final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		diagnostic.prepareDebug("==========> GetHoldingsListMockPostProcessor Mock -  Exited").log();
		return final_result;
	}


}