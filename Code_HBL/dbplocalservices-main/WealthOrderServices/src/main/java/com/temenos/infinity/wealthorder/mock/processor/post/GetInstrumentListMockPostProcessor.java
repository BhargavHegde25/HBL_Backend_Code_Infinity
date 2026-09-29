/**
 * 
 */
package com.temenos.infinity.wealthorder.mock.processor.post;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * (INFO) Searches the array of objects containing the holdings (of the chosen
 * portfolio) and instruments containing the keyword. The holdings Array is
 * obtained by calling the method "mockGetHoldingsList". Each JSON object is
 * formed by performing the for loop on all the string arrays. A new JSON Object
 * can be created by adding values to all the string arrays.
 * 
 * @author himaja.sridhar
 *
 */
public class GetInstrumentListMockPostProcessor implements DataPostProcessor2 {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		diagnostic.prepareDebug("==========> GetInstrumentListMockPostProcessor Mock - Entered").log();
		String search = (String) request.getParameter(TemenosConstants.SEARCHBYINSTRUMENTNAME);
		String portfolioId = (String) request.getParameter(TemenosConstants.PORTFOLIOID);
		String sortBy = (String) request.getParameter(TemenosConstants.SORTBY);
		String[] description = null, instrumentId = null, holdingsId = null, ISIN = null, exchange = null,
				RICCode = null, application = null, secCCy = null, marketPrice = null, percentageChange = null;
		boolean[] isSecurityAsset = null;
		JSONObject responseVal = new JSONObject();
		JSONObject holdingsObj = new JSONObject();
		JSONArray instrumentArr = new JSONArray();
		Map<String, Object> inputMap = new HashMap<>();
		inputMap.put(TemenosConstants.PORTFOLIOID, portfolioId);
		inputMap.put(TemenosConstants.SEARCHBYINSTRUMENTNAME, search);
		inputMap.put(TemenosConstants.SORTBY, sortBy);
		if (portfolioId != null) {
			holdingsObj = mockGetHoldingsList(inputMap);
			instrumentArr = holdingsObj.getJSONArray("portfolioHoldings");
		}

		JSONArray sortedJSON = new JSONArray();

		if (portfolioId != null) {

			if (portfolioId.equalsIgnoreCase("100777-1")) {
				instrumentId = new String[] { "100014-000", "100044-000", "100052-000", "100053-000", "100054-000",
						"100055-000", "100059-000", "100012-000", "100013-000", "100015-000", "100070-000",
						"100056-000", "100091-000", "100092-000", "100094-000", "100101-000", "100195-000",
						"100295-000", "100096-000", "100097-000", "100158-000", "100095-000", "100027-000",
						"100098-000", "100099-000", "100028-000", "100029-000", "100030-000", "100130-000",
						"100230-000" };
				holdingsId = new String[] { "100014-000", "100044-000", "100052-000", "100053-000", "100054-000",
						"100055-000", "100059-000", "100012-000", "100013-000", "100015-000", "100070-000",
						"100056-000", "100091-000", "100092-000", "100094-000", "100101-000", "100195-000",
						"100295-000", "100096-000", "100097-000", "100158-000", "100095-000", "100027-000",
						"100098-000", "100099-000", "100028-000", "100029-000", "100030-000", "100130-000",
						"100230-000" };
				description = new String[] { "Walmart Inc", "Honeywell International Inc", "AMAG PHARMACEUTICALS INC",
						"AMALGAMTD BANK A", "AMARIN CORP", "AMBARELLA INC", "AMC NTWK CL A", "MICROSOFT CP",
						"TESLA INC", "iShares Core S&P 500", "Apple Inc", "TSLA 5.300% 15Aug2025 Corp (USD)",
						"Ford, 8.875% 15jan2022, USD", "JPMorgan Funds - US Growth Fund A (acc)",
						"Fidelity S&P 500 Index USD P Acc", "2 Year Term Deposit 1.5%, ", "5 Year Term Deposit 3.25%",
						"10 Year Term Deposit 4.75%", "The Boeing Company (BA)", "AXP American Express Co.", "Nestle",
						"Novartis", "Google LLC", "Cisco Systems, Inc.", "APPLE-CALL-115-16JUL",
						"GOOGLE-PUT-2300-16JUL", "CITI-CALL-70-16JUL", "Forward EURUSD 2901 2022",
						"Forward GBPUSD 2901 2022", "Forward CHFUSD 2901 2022", };
				ISIN = new String[] { "US9311421039", "US4385161066", "US02266311111", "US0226631085", "US0231112063",
						"US00001210000", "US00164V1035", "US5949181045", "US88160R1014", "IE00B5BMR087", "US0378331005",
						"USU8810LAA18", "US345370BJ82", "LU0210536198", "IE00BYX5MS15", "", "", "", "US0378331005",
						"US0258161092", "CH0038863350", "CH0012005267", "US02079K1079", "US17275R1023", "US0378331005",
						"US02079K1079", "US1729671016", "", "", "", };
				exchange = new String[] { "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "GER",
						"NYSE", "XNYS", "XNYS", "MFAU", "NYSE", "", "", "", "NYSE", "NYSE", "SWX", "SWX", "NYSE",
						"NYSE", "NYSE", "NYSE", "NYSE", "", "", "" };
				RICCode = new String[] { "WMT.N", "HON.N", "AMAG.OQ", "AMAL.OQ", "AMRN.OQ", "AMBA.OQ", "AMCX.O",
						"MSFT.N", "TSLA.OQ", "IXM0461.DE", "AAPL.O", "TSLA.OQ", "F.N", "JPM.N", "FIDELIT.LG", "2YTD",
						"5YTD", "10YTD", "BA.N", "PNC.N", "NESN.S", "NOVN.S", "GOOGL.O", "CSCO.OQ", "AAPL.N", "GOOGL.N",
						"TXG.OQ", "FEURUSD", "FGBPUSD", "FCHFUSD" };
				isSecurityAsset = new boolean[] { true, true, true, true, true, true, true, true, true, true, true,
						true, true, true, true, false, false, false, true, true, false, false, false, false, false,
						false, false, false, false, false, };
				application = new String[] { "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC",
						"SC", "SC", "SC", "DX", "DX", "DX", "SC", "SC", "SC", "SC", "SC", "DX", "DX", "DX", "DX", "DX",
						"DX", "DX" };
				secCCy = new String[] { "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "EUR", "USD",
						"USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "EUR", "EUR", "USD", "USD",
						"USD", "USD", "USD", "USD", "USD", "USD" };
				marketPrice = new String[] { "148.91", "3203.53", "13.75", "13.15", "4.93", "60.72", "28.745", "214.19",
						"593.38", "306.67", "123.08", "306.67", "306.67", "306.67", "306.67", "306.67", "306.67",
						"306.67", "306.67", "306.67", "", "", "1776.84", "306.67", "306.67", "306.67", "", "306.67",
						"306.67", "306.67" };
				percentageChange = new String[] { "0.03", "0.51", "0.15", "-1.05", "1.85", "-0.09", "-1.25", "-0.52",
						"4.31", "0.55", "0.29", "0.55", "0.55", "0.55", "0.55", "0.55", "0.55", "0.55", "0.55", "0.55",
						"", "", "0.26", "0.55", "0.55", "0.55", "", "0.55", "0.55", "0.55" };
			} else if (portfolioId.equalsIgnoreCase("100777-2")) {
				instrumentId = new String[] { "100051-000", "100044-000", "100052-000", "100053-000", "100054-000",
						"100055-000", "100059-000", "100012-000", "100013-000", "100015-000", "100070-000",
						"100056-000", "100091-000", "100092-000", "100094-000", "100101-000", "100195-000",
						"100295-000", "100096-000", "100097-000", "100158-000", "100095-000", "100027-000",
						"100098-000", "100099-000", "100028-000", "100029-000", "100030-000", "100130-000",
						"100230-000", "100093-000" };
				holdingsId = new String[] { "100051-000", "100044-000", "100052-000", "100053-000", "100054-000",
						"100055-000", "100059-000", "100012-000", "100013-000", "100015-000", "100070-000",
						"100056-000", "100091-000", "100092-000", "100094-000", "100101-000", "100195-000",
						"100295-000", "100096-000", "100097-000", "100158-000", "100095-000", "100027-000",
						"100098-000", "100099-000", "100028-000", "100029-000", "100030-000", "100130-000",
						"100230-000", "100093-000" };
				description = new String[] { "LVMH", "Honeywell International Inc", "AMAG PHARMACEUTICALS INC",
						"AMALGAMTD BANK A", "AMARIN CORP", "AMBARELLA INC", "AMC NTWK CL A", "MICROSOFT CP",
						"TESLA INC", "iShares Core S&P 500", "Apple Inc", "TSLA 5.300% 15Aug2025 Corp (USD)",
						"Ford, 8.875% 15jan2022, USD", "JPMorgan Funds - US Growth Fund A (acc)",
						"Fidelity S&P 500 Index USD P Acc", "2 Year Term Deposit 1.5%, ", "5 Year Term Deposit 3.25%",
						"10 Year Term Deposit 4.75%", "The Boeing Company (BA)", "AXP American Express Co.", "Nestle",
						"Novartis", "Google LLC", "Cisco Systems, Inc.", "APPLE-CALL-115-16JUL",
						"GOOGLE-PUT-2300-16JUL", "CITI-CALL-70-16JUL", "Forward EURUSD 2901 2022",
						"Forward GBPUSD 2901 2022", "Forward CHFUSD 2901 2022", "iShares Core S&P 500 UCITS ETF" };
				ISIN = new String[] { "FR0000121014", "US4385161066", "US02266311111", "US0226631085", "US0231112063",
						"US00001210000", "US00164V1035", "US5949181045", "US88160R1014", "IE00B5BMR087", "US0378331005",
						"USU8810LAA18", "US345370BJ82", "LU0210536198", "IE00BYX5MS15", "", "", "", "US0378331005",
						"US0258161092", "CH0038863350", "CH0012005267", "US02079K1079", "US17275R1023", "US0378331005",
						"US02079K1079", "US1729671016", "", "", "", "IE00B5BMR087" };
				exchange = new String[] { "Euronext Paris", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE",
						"NYSE", "GER", "NYSE", "XNYS", "XNYS", "MFAU", "NYSE", "", "", "", "NYSE", "NYSE", "SWX", "SWX",
						"NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "", "", "", "GER" };
				RICCode = new String[] { "LVMH.PA", "HON.N", "AMAG.OQ", "AMAL.OQ", "AMRN.OQ", "AMBA.OQ", "AMCX.O",
						"MSFT.N", "TSLA.OQ", "IXM0461.DE", "AAPL.O", "TSLA.OQ", "F.N", "JPM.N", "FIDELIT.LG", "2YTD",
						"5YTD", "10YTD", "BA.N", "PNC.N", "NESN.S", "NOVN.S", "GOOGL.O", "CSCO.OQ", "AAPL.N", "GOOGL.N",
						"TXG.OQ", "FEURUSD", "FGBPUSD", "FCHFUSD", "BLK" };
				isSecurityAsset = new boolean[] { true, true, true, true, true, true, true, true, true, true, true,
						true, true, true, true, false, false, false, true, true, false, false, false, false, false,
						false, false, false, false, false, true };
				application = new String[] { "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC",
						"SC", "SC", "SC", "DX", "DX", "DX", "SC", "SC", "SC", "SC", "SC", "DX", "DX", "DX", "DX", "DX",
						"DX", "DX", "DX" };
				secCCy = new String[] { "EUR", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "EUR", "USD",
						"USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "EUR", "EUR", "USD", "USD",
						"USD", "USD", "USD", "USD", "USD", "USD", "USD" };
				marketPrice = new String[] { "497.95", "3203.53", "13.75", "13.15", "4.93", "60.72", "28.745", "214.19",
						"593.38", "306.67", "123.08", "306.67", "306.67", "306.67", "306.67", "306.67", "306.67",
						"306.67", "306.67", "306.67", "", "", "1776.84", "306.67", "306.67", "306.67", "", "306.67",
						"306.67", "306.67", "308.75" };
				percentageChange = new String[] { "0.03", "0.51", "0.15", "-1.05", "1.85", "-0.09", "-1.25", "-0.52",
						"4.31", "0.55", "0.29", "0.55", "0.55", "0.55", "0.55", "0.55", "0.55", "0.55", "0.55", "0.55",
						"", "", "0.26", "0.55", "0.55", "0.55", "", "0.55", "0.55", "0.55", "0.08" };
			} else if (portfolioId.equalsIgnoreCase("100777-3")) {
				instrumentId = new String[] { "100017-000", "100018-000", "100019-000", "100093-000", "100022-000",
						"100014-000", "100044-000", "100052-000", "100053-000", "100054-000", "100055-000",
						"100059-000", "100012-000", "100013-000", "100015-000", "100070-000", "100056-000",
						"100091-000", "100092-000", "100094-000", "100101-000", "100195-000", "100295-000",
						"100096-000", "100097-000", "100158-000", "100095-000", "100021-000", "100077-000",
						"100050-000" };
				holdingsId = new String[] { "100017-000", "100018-000", "100019-000", "100093-000", "100022-000",
						"100014-000", "100044-000", "100052-000", "100053-000", "100054-000", "100055-000",
						"100059-000", "100012-000", "100013-000", "100015-000", "100070-000", "100056-000",
						"100091-000", "100092-000", "100094-000", "100101-000", "100195-000", "100295-000",
						"100096-000", "100097-000", "100158-000", "100095-000", "100021-000", "100077-000",
						"100050-000" };
				description = new String[] { "Bank of America Corp", "Citigroup Inc", "General Motors Company",
						"iShares Core S&P 500 UCITS ETF", "Pfizer Inc", "Walmart Inc", "Honeywell International Inc",
						"AMAG PHARMACEUTICALS INC", "AMALGAMTD BANK A", "AMARIN CORP", "AMBARELLA INC", "AMC NTWK CL A",
						"MICROSOFT CP", "TESLA INC", "iShares Core S&P 500", "Apple Inc",
						"TSLA 5.300% 15Aug2025 Corp (USD)", "Ford, 8.875% 15jan2022, USD",
						"JPMorgan Funds - US Growth Fund A (acc)", "Fidelity S&P 500 Index USD P Acc",
						"2 Year Term Deposit 1.5%, ", "5 Year Term Deposit 3.25%", "10 Year Term Deposit 4.75%",
						"The Boeing Company (BA)", "AXP American Express Co.", "Nestle", "Novartis", "Amazon.com Inc",
						"Alphabet", "Apple" };
				ISIN = new String[] { "US0605051046", "US1729671016", "US37045V1008", "IE00B5BMR087", "INE182A01018",
						"US9311421039", "US4385161066", "US02266311111", "US0226631085", "US0231112063",
						"US00001210000", "US00164V1035", "US5949181045", "US88160R1014", "IE00B5BMR087", "US0378331005",
						"USU8810LAA18", "US345370BJ82", "LU0210536198", "IE00BYX5MS15", "", "", "", "US0378331005",
						"US0258161092", "CH0038863350", "CH0012005267", "US0231351067", "US02079K1079",
						"US0378331005" };
				exchange = new String[] { "NYSE", "NYSE", "NYSE", "GER", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE",
						"NYSE", "NYSE", "NYSE", "NYSE", "GER", "NYSE", "XNYS", "XNYS", "MFAU", "NYSE", "", "", "",
						"NYSE", "NYSE", "SWX", "SWX", "NYSE", "NYSE", "NYSE" };
				RICCode = new String[] { "BAC.N", "C.N", "GM.N", "BLK", "PFE.N", "WMT.N", "HON.N", "AMAG.OQ", "AMAL.OQ",
						"AMRN.OQ", "AMBA.OQ", "AMCX.O", "MSFT.N", "TSLA.OQ", "IXM0461.DE", "AAPL.O", "TSLA.OQ", "F.N",
						"JPM.N", "FIDELIT.LG", "2YTD", "5YTD", "10YTD", "BA.N", "PNC.N", "NESN.S", "NOVN.S", "AMZN.OQ",
						"GOOGL.OQ", "AAPL.OQ" };
				isSecurityAsset = new boolean[] { true, true, true, true, true, true, true, true, true, true, true,
						true, true, true, true, true, true, true, true, true, false, false, false, true, true, false,
						false, true, true, true };
				application = new String[] { "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC",
						"SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "DX", "DX", "DX", "SC", "SC", "SC", "SC", "SC",
						"SC", "SC" };
				secCCy = new String[] { "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD",
						"USD", "USD", "USD", "EUR", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD",
						"USD", "EUR", "EUR", "USD", "USD", "USD" };
				marketPrice = new String[] { "30.94", "60.91", "52.04", "308.75", "37.31", "148.91", "3203.53", "13.75",
						"13.15", "4.93", "60.72", "28.745", "214.19", "593.38", "306.67", "123.08", "306.67", "306.67",
						"306.67", "306.67", "306.67", "306.67", "306.67", "306.67", "306.67", "", "", "3203.53",
						"1824.97", "123.08" };
				percentageChange = new String[] { "-0.71", "0.58", "-2.53", "0.08", "0.08", "0.03", "0.51", "0.15",
						"-1.05", "1.85", "-0.09", "-1.25", "-0.52", "4.31", "0.55", "0.29", "0.55", "0.55", "0.55",
						"0.55", "0.55", "0.55", "0.55", "0.55", "0.55", "", "", "0.51", "1.65", "0.29" };
			}
		} else {
			instrumentId = new String[] { "100051-000", "100093-000", "100016-000", "100020-000", "100086-000",
					"100017-000", "100018-000", "100019-000", "100022-000", "100021-000", "100077-000", "100050-000",
					"100014-000", "100027-000", "100098-000", "100099-000", "100028-000", "100029-000", "100030-000",
					"100130-000", "100230-000", "100158-000", "100095-000", "100044-000", "100052-000", "100053-000",
					"100054-000", "100055-000", "100059-000", "100012-000", "100013-000", "100015-000", "100070-000",
					"100056-000", "100091-000", "100092-000", "100094-000", "100101-000", "100195-000", "100295-000",
					"100096-000", "100097-000" };
			holdingsId = new String[] { "100051-000", "100093-000", "100016-000", "100020-000", "100086-000",
					"100017-000", "100018-000", "100019-000", "100022-000", "100021-000", "100077-000", "100050-000",
					"100014-000", "100027-000", "100098-000", "100099-000", "100028-000", "100029-000", "100030-000",
					"100130-000", "100230-000", "100158-000", "100095-000", "100044-000", "100052-000", "100053-000",
					"100054-000", "100055-000", "100059-000", "100012-000", "100013-000", "100015-000", "100070-000",
					"100056-000", "100091-000", "100092-000", "100094-000", "100101-000", "100195-000", "100295-000",
					"100096-000", "100097-000" };
			description = new String[] { "LVMH", "iShares Core S&P 500 UCITS ETF", "Coca-Cola Co",
					"American Express Company", "Boeing Co", "Bank of America Corp", "Citigroup Inc",
					"General Motors Company", "Pfizer Inc", "Amazon.com Inc", "Alphabet", "Apple", "Walmart Inc",
					"Google LLC", "Cisco Systems, Inc.", "APPLE-CALL-115-16JUL", "GOOGLE-PUT-2300-16JUL",
					"CITI-CALL-70-16JUL", "Forward EURUSD 2901 2022", "Forward GBPUSD 2901 2022",
					"Forward CHFUSD 2901 2022", "Nestle", "Novartis", "Honeywell International Inc",
					"AMAG PHARMACEUTICALS INC", "AMALGAMTD BANK A", "AMARIN CORP", "AMBARELLA INC", "AMC NTWK CL A",
					"MICROSOFT CP", "TESLA INC", "iShares Core S&P 500", "Apple Inc",
					"TSLA 5.300% 15Aug2025 Corp (USD)", "Ford, 8.875% 15jan2022, USD",
					"JPMorgan Funds - US Growth Fund A (acc)", "Fidelity S&P 500 Index USD P Acc",
					"2 Year Term Deposit 1.5%, ", "5 Year Term Deposit 3.25%", "10 Year Term Deposit 4.75%",
					"The Boeing Company (BA)", "AXP American Express Co." };
			ISIN = new String[] { "FR0000121014", "IE00B5BMR087", "US1912161007", "US0258161092", "US0970231058",
					"US0605051046", "US1729671016", "US37045V1008", "INE182A01018", "US0231351067", "US02079K1079",
					"US0378331005", "US9311421039", "US02079K1079", "US17275R1023", "US0378331005", "US02079K1079",
					"US1729671016", "", "", "", "CH0038863350", "CH0012005267", "US4385161066", "US02266311111",
					"US0226631085", "US0231112063", "US00001210000", "US00164V1035", "US5949181045", "US88160R1014",
					"IE00B5BMR087", "US0378331005", "USU8810LAA18", "US345370BJ82", "LU0210536198", "IE00BYX5MS15", "",
					"", "", "US0378331005", "US0258161092" };
			exchange = new String[] { "Euronext Paris", "GER", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE",
					"NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "", "", "", "SWX", "SWX",
					"NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "GER", "NYSE", "XNYS", "XNYS",
					"MFAU", "NYSE", "", "", "", "NYSE", "NYSE" };
			RICCode = new String[] { "LVMH.PA", "BLK", "KO.N", "AXP.N", "BA.NQ", "BAC.N", "C.N", "GM.N", "PFE.N",
					"AMZN.OQ", "GOOGL.OQ", "AAPL.OQ", "WMT.N", "GOOGL.O", "CSCO.OQ", "AAPL.N", "GOOGL.N", "TXG.OQ",
					"FEURUSD", "FGBPUSD", "FCHFUSD", "NESN.S", "NOVN.S", "HON.N", "AMAG.OQ", "AMAL.OQ", "AMRN.OQ",
					"AMBA.OQ", "AMCX.O", "MSFT.N", "TSLA.OQ", "IXM0461.DE", "AAPL.O", "TSLA.OQ", "F.N", "JPM.N",
					"FIDELIT.LG", "2YTD", "5YTD", "10YTD", "BA.N", "PNC.N" };
			isSecurityAsset = new boolean[] { true, true, true, true, true, true, true, true, true, true, true, true,
					true, true, false, false, false, false, false, false, false, false, false, true, true, true, true,
					true, true, true, true, true, true, true, true, true, true, false, false, false, true, true };
			application = new String[] { "SC", "DX", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC",
					"SC", "DX", "DX", "DX", "DX", "DX", "DX", "DX", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC",
					"SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "DX", "DX", "DX", "SC", "SC" };
			secCCy = new String[] { "EUR", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD",
					"USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "EUR", "EUR", "USD", "USD", "USD",
					"USD", "USD", "USD", "USD", "USD", "EUR", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD",
					"USD", "USD" };
			marketPrice = new String[] { "497.95", "308.75", "49.29", "306.67", "202.06", "30.94", "60.91", "52.04",
					"37.31", "3203.53", "1824.97", "123.08", "148.91", "1776.84", "306.67", "306.67", "306.67", "",
					"306.67", "306.67", "306.67", "", "", "3203.53", "13.75", "13.15", "4.93", "60.72", "28.745",
					"214.19", "593.38", "306.67", "123.08", "306.67", "306.67", "306.67", "306.67", "306.67", "306.67",
					"306.67", "306.67", "306.67" };
			percentageChange = new String[] { "0.03", "0.08", "1.05", "0.55", "-0.64", "-0.71", "0.58", "-2.53", "0.08",
					"0.51", "1.65", "0.29", "0.03", "0.26", "0.55", "0.55", "0.55", "", "0.55", "0.55", "0.55", "", "",
					"0.51", "0.15", "-1.05", "1.85", "-0.09", "-1.25", "-0.52", "4.31", "0.55", "0.29", "0.55", "0.55",
					"0.55", "0.55", "0.55", "0.55", "0.55", "0.55", "0.55" };

		}
		for (int i = 0; i < description.length; i++) {
			JSONObject instrumentObj = new JSONObject();
			instrumentObj.put(TemenosConstants.INSTRUMENTID, instrumentId[i]);
			instrumentObj.put(TemenosConstants.HOLDINGSID, holdingsId[i]);
			instrumentObj.put(TemenosConstants.DESCRIPTION, description[i]);
			instrumentObj.put("ISIN", ISIN[i]);
			instrumentObj.put("holdingsType", exchange[i]);
			instrumentObj.put(TemenosConstants.RICCODE, RICCode[i]);
			instrumentObj.put(TemenosConstants.ISSECURITYASSET, isSecurityAsset[i]);
			instrumentObj.put(TemenosConstants.APPLICATION, application[i]);
			instrumentObj.put(TemenosConstants.SECCCY, secCCy[i]);
			instrumentObj.put(TemenosConstants.MARKETPRICE, marketPrice[i]);
			instrumentObj.put(TemenosConstants.PERCENTAGECHANGE, percentageChange[i]);

			instrumentArr.put(instrumentObj);
		}
		List<JSONObject> jsonValues = new ArrayList<JSONObject>();
		for (int i = 0; i < instrumentArr.length(); i++) {
			jsonValues.add(instrumentArr.getJSONObject(i));
		}
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
		for (int i = 0; i < instrumentArr.length(); i++) {
			sortedJSON.put(jsonValues.get(i));
		}
		if (search.equals("")) {
		} else {
			sortedJSON = OrderServiceUtils.returnSearch(sortedJSON, search, "");

		}
		responseVal.put("instrumentList", sortedJSON);
		responseVal.put("opstatus", "0");
		responseVal.put("httpStatusCode", "200");

		Result final_result = Utilities.constructResultFromJSONObject(responseVal);
		final_result.addOpstatusParam("0");
		final_result.addHttpStatusCodeParam("200");
		final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		diagnostic.prepareDebug("==========> GetInstrumentListMockPostProcessor Mock - Exiting with success").log();
		return final_result;
	}

	@SuppressWarnings("unused")
	public JSONObject mockGetHoldingsList(Map<String, Object> inputMap) {
		diagnostic.prepareDebug("==========> GetInstrumentListMockPostProcessor Mock - mockGetHoldingsList Begin").log();
		String portfolioId = (String) inputMap.get(TemenosConstants.PORTFOLIOID);
		String sortBy = (String) inputMap.get(TemenosConstants.SORTBY);
		String searchVal = (String) inputMap.get(TemenosConstants.SEARCHBYINSTRUMENTNAME);
		String instrumentidVal = (String) inputMap.get(TemenosConstants.INSTRUMENTID);
		String[] holdingsId = null, description = null, ISIN = null, exchange = null, secCCy = null, quantity = null,
				marketPrice = null, costPrice = null, marketValue = null, unrealPLMkt = null, RICCode = null,
				weightPercentage = null, assestClass = null, region = null, sector = null, exchangeRate = null,
				marketValPOS = null, costValue = null, costExchangeRate = null, unRealizedPLPercentage = null,
				dailyPL = null, dailyPLPercentage = null, instrumentId = null, unrealPLMktSec = null,
				unRealizedPLPercentageSec = null, costValueSec = null, amountBought = null, accruedInterest = null,
				balance = null, amountSold = null, quote = null, costQuote = null, counterpartAmount = null,
				subAssetClass = null, application = null;
		;
		int averageCostIndex = 0, nominalIndex = 0, accruedInterestIndex = 0, amountBoughtIndex = 0, balanceIndex = 0,
				amountSoldIndex = 0, quoteIndex = 0, costQuoteIndex = 0, counterpartAmountIndex = 0;

		boolean[] isSecurityAsset = null, isAdvisory = null;
		String sortType = (String) inputMap.get(TemenosConstants.SORTORDER);
		int totalCount = 0;
		String limitVal = (String) inputMap.get(TemenosConstants.PAGESIZE);
		String offsetVal = (String) inputMap.get(TemenosConstants.PAGEOFFSET);
		int limit = (limitVal != null && limitVal.trim().length() > 0) ? Integer.parseInt(limitVal) : 0;
		int offset = (offsetVal != null && offsetVal.trim().length() > 0) ? Integer.parseInt(offsetVal) : 0;
		String search = (searchVal != null && searchVal.trim().length() > 0) ? searchVal : "";
		String instrumentid = (instrumentidVal != null && instrumentidVal.trim().length() > 0) ? instrumentidVal : "";
		JSONObject response = new JSONObject();
		JSONArray holdingsArr = new JSONArray();

		if (portfolioId.equalsIgnoreCase("100777-1")) {
			holdingsId = new String[] { "100051-000", "100093-000", "100016-000", "100020-000", "100086-000",
					"100017-000", "100018-000", "100019-000", "100022-000", "100021-000", "100077-000", "100050-000",
					"100056-000", "100156-000", "100091-000", "100092-000" };
			instrumentId = new String[] { "100051-000", "100093-000", "100016-000", "100020-000", "100086-000",
					"100017-000", "100018-000", "100019-000", "100022-000", "100021-000", "100077-000", "100050-000",
					"100056-000", "100156-000", "100091-000", "100092-000" };
			description = new String[] { "LVMH", "iShares Core S&P 500 UCITS ETF", "Coca-Cola Co",
					"American Express Company", "Boeing Co", "Bank of America Corp", "Citigroup Inc",
					"General Motors Company", "Pfizer Inc", "Amazon.com Inc", "Alphabet", "Apple" };
			ISIN = new String[] { "FR0000121014", "IE00B5BMR087", "US1912161007", "US0258161092", "US0970231058",
					"US0605051046", "US1729671016", "US37045V1008", "INE182A01018", "US0231351067", "US02079K1079",
					"US0378331005" };
			exchange = new String[] { "Euronext Paris", "GER", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE",
					"NYSE", "NYSE", "NYSE" };
			secCCy = new String[] { "EUR", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD",
					"USD" };
			quantity = new String[] { "6", "16", "10", "15", "10", "12", "15", "8", "10", "4", "2", "23" };
			marketPrice = new String[] { "497.95", "308.75", "49.29", "116.15", "202.06", "30.94", "60.91", "52.04",
					"37.31", "3203.53", " 1824.97", "123.08" };
			costPrice = new String[] { "372.29", "300.00", "45.00", "105.00", "190.00", "33.00", "55.00", "49.00",
					"34.00", "2652.13", "1622.11", "98.08" };
			marketValue = new String[] { "2987.70", "5631.12", "492.90", "1742.25", "2020.60", "371.28", "913.65",
					"416.32", "373.10", "12814.12", "3649.94", "2830.84" };
			unrealPLMkt = new String[] { "+753.96", "+631.12", "+42.90", "+167.25", "+120.60", "-24.72", "+88.65",
					"+24.32", "+33.10", "+2205.60", "+405.72", "575.00" };
			unrealPLMktSec = new String[] { "+753.96", "+631.12", "+42.90", "+167.25", "+120.60", "-24.72", "+88.65",
					"+24.32", "+33.10", "+2205.60", "+405.72", "575.00" };
			RICCode = new String[] { "LVMH.PA", "BLK", "KO.N", "AXP.N", "BA.NQ", "BAC.N", "C.N", "GM.N", "PFE.N",
					"AMZN.OQ", "GOOGL.OQ", "AAPL.OQ" };

			weightPercentage = new String[] { "6.03", "11.36", "0.99", "3.51", "4.08", "0.75", "1.84", "0.84", "0.75",
					"25.85", "7.36", "5.71" };
			assestClass = new String[] { "Share", "Fund", "Share", "Share", "Share", "Share", "Share", "Share", "Share",
					"Share", "Share", "Share" };
			subAssetClass = new String[] { "Ordinary Shares", "Exchange Traded Funds", "Ordinary Shares",
					"Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares",
					"Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares" };

			region = new String[] { "EU", "US", "US", "US", "US", "US", "US", "US", "US", "North America",
					"North America", "North America" };
			sector = new String[] { "Consumer Cyclical", "Fund -Open Ended Investment Company", "Consumer Defensive",
					"Financial Services", "Industrials", "Financial Services", "Financial Services",
					"Consumer Cyclical", "Healthcare", "Consumer Cyclical", "Communication Services", "Technology" };
			exchangeRate = new String[] { "0.82", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1" };
			marketValPOS = new String[] { "2987.70", "5631.12", "492.90", "1742.25", "2020.60", "371.28", "913.65",
					"416.32", "373.10", "12814.12", "3649.94", "2830.84" };
			costValue = new String[] { "2233.74", "5000.00", "450.00", "1575.00", "1900.00", "396.00", "825.00",
					"392.00", "340.00", "10608.52", "3244.22", "2255.84" };
			costValueSec = new String[] { "2233.74", "5000.00", "450.00", "1575.00", "1900.00", "396.00", "825.00",
					"392.00", "340.00", "10608.52", "3244.22", "2255.84" };
			costExchangeRate = new String[] { "0.15", "0.5", "0.6", "0.7", "0.8", "0.9", "0.10", "0.11", "0.12", "0.13",
					"0.14", "0.15", "0.16" };
			unRealizedPLPercentage = new String[] { "34", "13", "10", "11", "6", "-6", "11", "6", "10", "21", "13",
					"25" };
			unRealizedPLPercentageSec = new String[] { "34", "13", "10", "11", "6", "-6", "11", "6", "10", "21", "13",
					"25" };
			dailyPL = new String[] { "85.9", "0.13", "44.5", "-30.2", "-20.4", "-10.45", "23.6", "-10.65", "90.5",
					"145.5", "29.61", "0.36" };
			dailyPLPercentage = new String[] { "0.11", "1.3", "0.14", "-0.92", "0.60", "-1.20", "0.56", "0.45", "0.83",
					"0.03", "1.65", "0.29" };
			isSecurityAsset = new boolean[] { true, true, true, true, true, true, true, true, true, true, true, true };
			isAdvisory = new boolean[] { false, false, false, false, false, false, false, false, false, false, false,
					false };
			application = new String[] { "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC" };
			accruedInterest = new String[] { "121.56", "55.23", "356.76" };
		} else if (portfolioId.equalsIgnoreCase("100777-2")) {
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
					"NYSE" };
			secCCy = new String[] { "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD" };
			quantity = new String[] { "2", "6", "6", "10", "8", "9", "12", "6", "9", "2", "7" };
			marketPrice = new String[] { "1824.97", "148.91", "49.29", "116.15", "202.06", "30.94", "60.91", "52.04",
					"37.31", "3203.53", "123.08" };
			costPrice = new String[] { "1622.11", "132.51", "42.00", "106.00", "184.00", "27.00", "61.00", "50.00",
					"32.00", "2652.13", "98.08" };
			marketValue = new String[] { "3649.94", "893.46", "295.74", "1161.50", "1616.48", "278.46", "730.92",
					"312.24", "335.79", "6407.06", "861.56" };
			unrealPLMkt = new String[] { "+405.72", "+98.40", "+43.74", "+105.50", "+144.48", "+35.46", "-1.08",
					"+12.24", "+52.29", "+1102.80", "175.00" };
			unrealPLMktSec = new String[] { "+405.72", "+98.40", "+43.74", "+105.50", "+144.48", "+35.46", "-1.08",
					"+12.24", "+52.29", "+1102.80", "175.00" };
			RICCode = new String[] { "GOOGL.OQ", "WMT.N", "KO.N", "AXP.N", "BA.NQ", "BAC.N", "C.N", "GM.N", "PFE.N",
					"AMZN.OQ", "AAPL.OQ" };
			weightPercentage = new String[] { "21.66", "5.30", "1.75", "6.89", "9.59", "1.65", "4.34", "1.85", "1.99",
					"38.02", "5.11" };
			assestClass = new String[] { "Share", "Share", "Share", "Share", "Share", "Share", "Share", "Share",
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
			costValue = new String[] { "3244.22", "795.06", "252.00", "1056.00", "1472.00", "243.00", "732.00",
					"300.00", "283.50", "5304.26", "686.56" };
			costValueSec = new String[] { "3244.22", "795.06", "252.00", "1056.00", "1472.00", "243.00", "732.00",
					"300.00", "283.50", "5304.26", "686.56" };
			costExchangeRate = new String[] { "0.1", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9", "0.10", "0.11", "0.14",
					"0.16" };
			unRealizedPLPercentage = new String[] { "13", "12", "17", "10", "10", "15", "0", "4", "18", "21", "25" };
			unRealizedPLPercentageSec = new String[] { "13", "12", "17", "10", "10", "15", "0", "4", "18", "21", "25" };
			dailyPL = new String[] { "5.34", "2.46", "4.42", "-3.56", "4.44", "-4.89", "6.9", "2.24", "1.56", "145.5",
					"0.36" };
			dailyPLPercentage = new String[] { "0.10", "0.13", "0.10", "0.23", "0.10", "-0.34", "0.32", "0.12", "0.14",
					"0.11", "0.03", "0.29" };
			isSecurityAsset = new boolean[] { true, true, true, true, true, true, true, true, true, true, true };
			isAdvisory = new boolean[] { false, false, false, false, false, false, false, false, false, false, false };
			application = new String[] { "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC" };

			accruedInterest = new String[] { "132.74", "185.63", "127.52" };
			balance = new String[] { "25132.74", "12785.6325", "35127.518" };

		} else if (portfolioId.equalsIgnoreCase("100777-3")) {
			holdingsId = new String[] { "100027-000", "100051-000", "100016-000", "100020-000", "100086-000",
					"100098-000", "100099-000", "100028-000", "100029-000", "100030-000", "100130-000", "100230-000" };
			instrumentId = new String[] { "100027-000", "100051-000", "100016-000", "100020-000", "100086-000",
					"100098-000", "100099-000", "100028-000", "100029-000", "100030-000", "100130-000", "100230-000" };
			description = new String[] { "Google LLC", "LVMH", "Coca-Cola Co", "American Express Company", "Boeing Co",
					"Cisco Systems, Inc.", "APPLE-CALL-115-16JUL", "GOOGLE-PUT-2300-16JUL", "CITI-CALL-70-16JUL",
					"Forward EURUSD 2901 2022", "Forward GBPUSD 2901 2022", "Forward CHFUSD 2901 2022" };
			ISIN = new String[] { "US02079K1079", "FR0000121014", "US1912161007", "US0258161092", "US0970231058",
					"US17275R1023", "US0378331005", "US02079K1079", "US1729671016", "", "", "" };
			exchange = new String[] { "NYSE", "Euronext Paris", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE",
					"", "", "" };
			secCCy = new String[] { "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD",
					"USD" };
			quantity = new String[] { "1", "5", "7", "8", "9", "14", "13", "24", "22", "", "", "" };
			marketPrice = new String[] { "1824.97", "497.95", "49.29", "116.15", "202.06", "3203.53", "123.08",
					"497.95", "49.29", "116.15", "202.06", "3503.24" };
			costPrice = new String[] { "1622.11", "372.29", "45.00", "105.00", "190.00", "2.15", "12.24", "13.5",
					"2.14", "", "", "" };
			marketValue = new String[] { "1824.97", "2489.75", "345.03", "929.20", "1818.54", "74424.00", "21645.00",
					"31680.00", "5148.00", "106400.00", "140000.00", "89600.00" };
			unrealPLMkt = new String[] { "+202.86", "+628.30", "+30.03", "+89.20", "+108.54", "+71414.00", "+5733.00",
					"-720.00", "+440.00", "+1191.68", "+1568.00", "+1003.52" };
			unrealPLMktSec = new String[] { "+212.86", "+638.30", "+40.03", "+99.20", "+118.54", "+71414.00",
					"+5733.00", "-720.00", "+440.00", "+1191.68", "+1568.00", "+1003.52" };
			RICCode = new String[] { "GOOGL.O", "LVMH.PA", "KO.N", "AXP.N", "BA.NQ", "CSCO.OQ", "AAPL.N", "GOOGL.N",
					"TXG.OQ", "FEURUSD", "FGBPUSD", "FCHFUSD" };
			weightPercentage = new String[] { "5.33", "7.28", "1.01", "2.72", "5.31", "5.82", "1.69", "2.48", "0.40",
					"8.33", "10.96", "7.01" };
			assestClass = new String[] { "Share", "Share", "Share", "Share", "Share", "Future", "Option", "Option",
					"Option", "Forward", "Forward", "Forward" };
			subAssetClass = new String[] { "Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares",
					"Ordinary Shares", "Equity Futures", "Share Options", "Share Options", "Share Options", "Forward",
					"Forward", "Forward" };
			region = new String[] { "US", "US", "EU", "US", "US", "US", "US", "US", "US", "US", "US", "US", "US" };
			sector = new String[] { "Computer Services", "Retail", "Beverages (Nonalcoholic)",
					"Consumer Financial Services", "Aerospace & Defense", "Retail", "Communications Equipment",
					"Retail", "Beverages (Nonalcoholic)", "Consumer Financial Services", "Aerospace & Defense",
					"Retail" };
			exchangeRate = new String[] { "1", "0.82", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1" };
			marketValPOS = new String[] { "1824.97", "2041.60", "345.03", "929.20", "1818.54", "74424.00", "21645.00",
					"31680.00", "5148.00", "106400.00", "140000.00", "89600.00" };
			costValue = new String[] { "1622.11", "1861.45", "315.00", "840.00", "1710.00", "13260.65", "980.80",
					"1861.45", "315.00", "840.00", "1710.00", "355.00" };
			costValueSec = new String[] { "1632.11", "1871.45", "325.00", "850.00", "1720.00", "13360.65", "990.80",
					"1871.45", "325.00", "860.00", "1730.00", "365.00" };
			costExchangeRate = new String[] { "0.1", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9", "1.0", "1.1", "1.2",
					"1.3", "1.4" };
			unRealizedPLPercentage = new String[] { "13", "34", "10", "11", "6", "14", "31", "15", "51", "12", "22",
					"17" };
			unRealizedPLPercentageSec = new String[] { "14", "35", "11", "12", "7", "6", "14", "31", "15", "51", "12",
					"22" };
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

		} else if (portfolioId.equalsIgnoreCase("100777-4")) {
			holdingsId = new String[] { "100077-000", "100021-000", "100050-000", "100051-000", "100016-000",
					"100020-000", "100086-000", "100017-000", "100018-000", "100019-000", "100022-000", "100093-000" };
			instrumentId = new String[] { "100077-000", "100021-000", "100050-000", "100051-000", "100016-000",
					"100020-000", "100086-000", "100017-000", "100018-000", "100019-000", "100022-000", "100093-000" };
			description = new String[] { "Alphabet", "Amazon.com Inc", "Apple", "LVMH", "Coca-Cola Co",
					"American Express Company", "Boeing Co", "Bank of America Corp", "Citigroup Inc",
					"General Motors Company", "Pfizer Inc", "iShares Core S&P 500 UCITS ETF" };
			ISIN = new String[] { "US02079K1079", "US0231351067", "US0378331005", "FR0000121014", "US1912161007",
					"US0258161092", "US0970231058", "US0605051046", "US1729671016", "US37045V1008", "INE182A01018",
					"IE00B5BMR087" };
			exchange = new String[] { "NYSE", "NYSE", "NYSE", "Euronext Paris", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE",
					"NYSE", "NYSE", "NYSE", };
			secCCy = new String[] { "USD", "USD", "USD", "EUR", "USD", "USD", "USD", "USD", "USD", "USD", "USD",
					"USD", };
			quantity = new String[] { "2", "4", "23", "6", "10", "15", "10", "12", "15", "8", "10", "16" };
			marketPrice = new String[] { "1824.97", "3203.53", "123.08", "497.95", "49.29", "116.15", "202.06", "30.94",
					"60.91", "52.04", "37.31", "308.75" };
			costPrice = new String[] { "1622.11", "2652.13", "98.08", "372.29", "45.00", "105.00", "190.00", "33.00",
					"55.00", "49.00", "34.00", "300.00" };
			marketValue = new String[] { "3649.94", "12814.12", "2830.84", "2987.70", "492.90", "1742.25", "2020.60",
					"371.28", "913.65", "416.32", "373.10", "4940.00" };
			unrealPLMkt = new String[] { "+405.72", "+2205.60", "+575.00", "+753.96", "+42.90", "+167.25", "+120.60",
					"-24.72", "+88.65", "+24.32", "+33.10", "+140.00" };
			unrealPLMktSec = new String[] { "+405.72", "+2205.60", "+575.00", "+753.96", "+42.90", "+167.25", "+120.60",
					"-24.72", "+88.65", "+24.32", "+33.10", "+140.00" };
			RICCode = new String[] { "GOOGL.OQ", "AMZN.OQ", "AAPL.OQ", "LVMH.PA", "KO.N", "AXP.N", "BA.NQ", "BAC.N",
					"C.N", "GM.N", "PFE.N", "IXMO461.DE" };
			weightPercentage = new String[] { "7.47", "26.21", "5.79", "6.11", "1.01", "3.56", "4.13", "0.76", "1.87",
					"0.85", "0.76", "10.11" };
			assestClass = new String[] { "Share", "Share", "Share", "Share", "Share", "Share", "Share", "Share",
					"Share", "Share", "Share", "Fund" };
			subAssetClass = new String[] { "Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares",
					"Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares",
					"Ordinary Shares", "Exchange Traded Funds", "Exchange Traded Funds" };

			region = new String[] { "US", "US", "US", "EU", "US", "US", "US", "US", "US", "US", "US", "US" };
			sector = new String[] { "Computer Services", "Retail", "Communications Equipment", "Retail",
					"Beverages (Nonalcoholic)", "Consumer Financial Services", "Aerospace & Defense", "Regional Banks",
					"Regional Banks", "Auto & Truck Manufacturers", "Biotechnology & Drugs", "Exchange Traded Funds" };
			exchangeRate = new String[] { "1", "1", "1", "0.82", "1", "1", "1", "1", "1", "1", "1", "1" };
			marketValPOS = new String[] { "3649.94", "12814.12", "2830.84", "2449.91", "492.90", "1742.25", "2020.60",
					"371.28", "913.65", "416.32", "373.10", "4940.00" };
			costValue = new String[] { "3244.22", "10608.52", "2255.84", "2233.74", "450.00", "1575.00", "1900.00",
					"396.00", "825.00", "392.00", "340.00", "4800.00" };
			costValueSec = new String[] { "3244.22", "10608.52", "2255.84", "2233.74", "450.00", "1575.00", "1900.00",
					"396.00", "825.00", "392.00", "340.00", "4800.00" };
			costExchangeRate = new String[] { "1", "1", "1", "0.82", "1", "1", "1", "1", "1", "1", "1", "1" };
			unRealizedPLPercentage = new String[] { "13", "21", "25", "34", "10", "11", "6", "-6", "11", "6", "10",
					"3" };
			unRealizedPLPercentageSec = new String[] { "13", "21", "25", "34", "10", "11", "6", "-6", "11", "6", "10",
					"3" };
			dailyPL = new String[] { "145.5", "50.5", "65.6", "85.9", "44.5", "-30.2", "-20.4", "-10.45", "23.6",
					"-10.65", "90.5", "90.5" };
			dailyPLPercentage = new String[] { "0.03", "0.15", "0.12", "0.11", "0.14", "-0.92", "0.60", "-1.20", "0.56",
					"0.45", "0.83", "0.83" };
			isSecurityAsset = new boolean[] { false, false, false, false, false, false, false, false, false, false,
					false, false };
			isAdvisory = new boolean[] { true, true, true, true, true, true, true, true, true, true, true, true };
			application = new String[] { "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "DX" };

		} else if (portfolioId.equalsIgnoreCase("100777-5")) {
			holdingsId = new String[] { "100077-000", "100021-000", "100050-000", "100014-000", "100016-000",
					"100020-000", "100086-000", "100158-000", "100095-000", "100019-000", "100022-000" };
			instrumentId = new String[] { "100077-000", "100021-000", "100050-000", "100014-000", "100016-000",
					"100020-000", "100086-000", "100158-000", "100095-000", "100019-000", "100022-000" };
			description = new String[] { "Alphabet", "Amazon.com Inc", "Apple", "Walmart Inc", "Coca-Cola Co",
					"American Express Company", "Boeing Co", "Nestle", "Novartis", "General Motors Company",
					"Pfizer Inc" };
			ISIN = new String[] { "US02079K1079", "US0231351067", "US0378331005", "US9311421039", "US1912161007",
					"US0258161092", "US0970231058", "CH0038863350", "CH0012005267", "US37045V1008", "INE182A01018" };
			exchange = new String[] { "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "NYSE", "SWX", "SWX", "NYSE",
					"NYSE" };
			secCCy = new String[] { "USD", "USD", "USD", "USD", "USD", "USD", "USD", "EUR", "EUR", "USD", "USD" };
			quantity = new String[] { "2", "2", "7", "6", "6", "10", "8", "9", "12", "6", "9" };
			marketPrice = new String[] { "1824.97", "3203.53", "123.08", "148.91", "49.29", "116.15", "202.06",
					"145.00", "86.96", "52.04", "37.31" };
			costPrice = new String[] { "1622.11", "2652.13", "98.08", "132.51", "42.00", "106.00", "184.00", "131.00",
					"82.00", "50.00", "32.00" };
			marketValue = new String[] { "3649.94", "6407.06", "861.56", "893.46", "295.74", "1161.50", "1616.48",
					"1305.00", "1043.52", "312.24", "335.79" };
			unrealPLMkt = new String[] { "405.72", "1102.80", "175.00", "98.40", "43.74", "105.50", "144.48", "126.00",
					"65.52", "12.24", "52.29" };
			unrealPLMktSec = new String[] { "405.72", "1102.80", "175.00", "98.40", "43.74", "105.50", "144.48",
					"126.00", "65.52", "12.24", "52.29" };
			RICCode = new String[] { "GOOGL.OQ", "AMZN.OQ", "AAPL.OQ", "WMT.N", "KO.N", "AXP.N", "BA.NQ", "NESN.S",
					"NOVN.S", "GM.N", "PFE.N" };
			weightPercentage = new String[] { "20.06", "35.22", "4.74", "4.91", "1.63", "6.38", "8.89", "7.17", "5.74",
					"1.72", "1.85" };
			assestClass = new String[] { "Share", "Share", "Share", "Share", "Share", "Share", "Share", "Share",
					"Share", "Share", "Share" };
			subAssetClass = new String[] { "Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares",
					"Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares", "Ordinary Shares",
					"Ordinary Shares", "Exchange Traded Funds" };

			region = new String[] { "US", "US", "US", "US", "US", "US", "US", "EU", "EU", "US", "US" };
			sector = new String[] { "Computer Services", "Retail", "Communications Equipment", "Retail",
					"Beverages (Nonalcoholic)", "Consumer Financial Services", "Aerospace & Defense", "Food processing",
					"Pharmaceuticals", "Auto & Truck Manufacturers", "Biotechnology & Drugs" };
			exchangeRate = new String[] { "1", "1", "1", "1", "1", "1", "1", "0.93", "0.93", "1", "1" };
			marketValPOS = new String[] { "3649.94", "6407.06", "861.56", "893.46", "295.74", "1161.50", "1616.48",
					"1213.65", "970.47", "312.24", "335.79" };
			costValue = new String[] { "3244.22", "5304.26", "686.56", "795.06", "252.00", "1056.00", "1472.00",
					"1179.00", "978.00", "300.00", "283.50" };
			costValueSec = new String[] { "3244.22", "5304.26", "686.56", "795.06", "252.00", "1056.00", "1472.00",
					"1179.00", "978.00", "300.00", "283.50" };
			costExchangeRate = new String[] { "1", "1", "1", "1", "1", "1", "1", "0.93", "0.93", "1", "1" };
			unRealizedPLPercentage = new String[] { "13", "21", "25", "12", "17", "10", "10", "11", "7", "4", "18" };
			unRealizedPLPercentageSec = new String[] { "13", "21", "25", "12", "17", "10", "10", "11", "7", "4", "18" };
			dailyPL = new String[] { "5.34", "4.56", "-1.45", "2.46", "4.42", "-3.56", "4.44", "-4.89", "6.9", "2.24",
					"1.56" };
			dailyPLPercentage = new String[] { "0.10", "0.11", "-0.34", "0.13", "0.10", "-0.23", "0.10", "-0.34",
					"0.32", "0.12", "0.14" };
			isSecurityAsset = new boolean[] { false, false, false, false, false, false, false, false, false, false,
					false };
			isAdvisory = new boolean[] { true, true, true, true, true, true, true, true, true, true, true };
			application = new String[] { "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC", "SC" };
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
			holdingsObj.put(TemenosConstants.ASSESTCLASS, assestClass[i]);
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
				if (i >= 9) {
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

		} else if (sortBy.equalsIgnoreCase(TemenosConstants.ASSESTCLASS)
				|| sortBy.equalsIgnoreCase(TemenosConstants.REGION) || sortBy.equalsIgnoreCase(TemenosConstants.SECTOR)
				|| sortBy.equalsIgnoreCase(TemenosConstants.SECCCY)) {
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

		if (search.equals("")) {
		} else {
			sortedJSON = OrderServiceUtils.returnSearch(sortedJSON, search, "");
		}

		if (instrumentid.equals("")) {
		} else {
			sortedJSON = OrderServiceUtils.returnSearchInstrumentID(sortedJSON, instrumentid);
		}

		totalCount = sortedJSON.length();
		if (limit > 0 && offset >= 0) {
			sortedJSON = PortfolioWealthUtils.pagination(sortedJSON, limit, offset);
		}
		if (portfolioId.equalsIgnoreCase("100777-1")) {
			response.put("portfolioID", portfolioId);
			response.put("referenceCurrency", "USD");
			response.put("marketValue", "34243.82");
			response.put("unRealizedPL", "P");
			response.put("unRealizedPLAmount", "5023.50");
			response.put("unRealizedPLPercentage", "21");
			response.put("todayPL", "P");
			response.put("todayPLAmount", "498.23");
			response.put("accountName", "John Bailey Portfolio 1.");
			response.put("accountNumber", "100777-1");
			response.put("todayPLPercentage", "2.12");
		} else if (portfolioId.equalsIgnoreCase("100777-2")) {
			response.put("referenceCurrency", "USD");
			response.put("marketValue", "16543.15");
			response.put("unRealizedPL", "P");
			response.put("unRealizedPLAmount", "2174.55");
			response.put("unRealizedPLPercentage", "15");
			response.put("todayPL", "P");
			response.put("todayPLAmount", "1593.21");
			response.put("todayPLPercentage", "11.09");
			response.put("accountName", "John Bailey Portfolio 2.");
			response.put("accountNumber", "100777-2");
			response.put("portfolioID", portfolioId);
		} else if (portfolioId.equalsIgnoreCase("100777-3")) {
			response.put("referenceCurrency", "USD");
			response.put("marketValue", "24655.94");
			response.put("unRealizedPL", "P");
			response.put("unRealizedPLAmount", "4065.93");
			response.put("unRealizedPLPercentage", "20");
			response.put("todayPL", "P");
			response.put("todayPLAmount", "46.00");
			response.put("todayPLPercentage", "1.15");
			response.put("accountName", "John Bailey Portfolio 3.");
			response.put("accountNumber", "100777-3");
			response.put("portfolioID", portfolioId);
		} else if (portfolioId.equalsIgnoreCase("100777-4")) {
			response.put("referenceCurrency", "USD");
			response.put("marketValue", "48881.31");
			response.put("unRealizedPL", "P");
			response.put("unRealizedPLAmount", "4532.38");
			response.put("unRealizedPLPercentage", "9.22");
			response.put("todayPL", "P");
			response.put("todayPLAmount", "510.12");
			response.put("todayPLPercentage", "1.04");
			response.put("accountName", "John Bailey Portfolio 4.");
			response.put("accountNumber", "100777-4");
			response.put("portfolioID", portfolioId);
		} else if (portfolioId.equalsIgnoreCase("100777-5")) {
			response.put("referenceCurrency", "USD");
			response.put("marketValue", "18191.27");
			response.put("unRealizedPL", "P");
			response.put("unRealizedPLAmount", "2331.69");
			response.put("unRealizedPLPercentage", "14.99");
			response.put("todayPL", "P");
			response.put("todayPLAmount", "22.11");
			response.put("todayPLPercentage", "0.14");
			response.put("accountName", "John Bailey Portfolio 5.");
			response.put("accountNumber", "100777-5");
			response.put("portfolioID", portfolioId);
		} else {
			response.put("portfolioID", portfolioId);
			response.put("referenceCurrency", "USD");
			response.put("marketValue", "34243.82");
			response.put("unRealizedPL", "P");
			response.put("unRealizedPLAmount", "5023.50");
			response.put("unRealizedPLPercentage", "21");
			response.put("todayPL", "P");
			response.put("todayPLAmount", "498.23");
			response.put("accountName", "John Bailey Portfolio 1.");
			response.put("accountNumber", "100777-1");
			response.put("todayPLPercentage", "2.12");
		}
		String[] colArray = new String[] { TemenosConstants.HOLDINGSID, "holdingsType", TemenosConstants.MARKETPRICE,
				"ISIN", TemenosConstants.MARKETVALPOS, TemenosConstants.WEIGHTPERCENTAGE, TemenosConstants.UNREALPLMKT,
				TemenosConstants.REGION, TemenosConstants.ASSESTCLASS, TemenosConstants.SECTOR, TemenosConstants.SECCCY,
				TemenosConstants.MARKETVALUE, TemenosConstants.COSTVALUE, TemenosConstants.UNREALIZEDPLPERCENTAGE,
				TemenosConstants.QUANTITY, TemenosConstants.COSTPRICE, TemenosConstants.DESCRIPTION,
				TemenosConstants.RICCODE, TemenosConstants.EXCHANGERATE, TemenosConstants.COSTEXCHANGERATE,
				TemenosConstants.DAILYPL, TemenosConstants.DAILYPLPERCENTAGE,
				TemenosConstants.UNREALIZEDPLPERCENTAGESECCCY, TemenosConstants.UNREALPLMKTSECCCY,
				TemenosConstants.COSTVALUESECCCY };

		String fieldValue = Arrays.toString(colArray).replace("[", "").replace("]", "");
		response.put("portfolioID", portfolioId);
		response.put("portfolioHoldings", sortedJSON);
		response.put("fieldstoDisplay", fieldValue);
		response.put(TemenosConstants.SORTBY, sortBy);
		response.put("totalCount", totalCount);
		response.put("opstatus", "0");
		response.put("httpStatusCode", "200");
		diagnostic.prepareDebug("==========> GetInstrumentListMockPostProcessor Mock - mockGetHoldingsList End").log();
		return response;
	}
}
