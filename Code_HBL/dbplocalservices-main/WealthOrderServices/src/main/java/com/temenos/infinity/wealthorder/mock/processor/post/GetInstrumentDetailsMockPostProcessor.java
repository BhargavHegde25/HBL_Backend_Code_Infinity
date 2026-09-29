/**
 * 
 */
package com.temenos.infinity.wealthorder.mock.processor.post;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * @author himaja.sridhar
 *
 */
public class GetInstrumentDetailsMockPostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		diagnostic.prepareDebug("==========> GetInstrumentDetailsMockPostProcessor Mock - Entered").log();
		try {
			String isinCode = request.getParameter(TemenosConstants.RICCODE);
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMM YYYY");
			Date date = new Date();
			JSONObject instrumentDetails = new JSONObject();

			if (isinCode.equalsIgnoreCase("AMZN.OQ")) {

				instrumentDetails.put("instrumentName", "Amazon.com Inc");
				instrumentDetails.put("ISINCode", "US0231351067");
				instrumentDetails.put("stockExchange", "NYSE");
				instrumentDetails.put(TemenosConstants.INSTRUMENTID, "100021-000");
				instrumentDetails.put("referenceCurrency", "USD");
				instrumentDetails.put("marketPrice", "3203.53");
				instrumentDetails.put("closeRate", "3203.53");
				instrumentDetails.put("netchange", "16.55");
				instrumentDetails.put("percentageChange", "0.51");
				instrumentDetails.put("timeReceived", "12:20:00");
				instrumentDetails.put("dateReceived", sdf.format(date));

			} else if (isinCode.equalsIgnoreCase("GOOGL.OQ")) {

				instrumentDetails.put("instrumentName", "Alphabet");
				instrumentDetails.put("ISINCode", "US02079K1079");
				instrumentDetails.put("stockExchange", "NYSE");
				instrumentDetails.put(TemenosConstants.INSTRUMENTID, "100077-000");
				instrumentDetails.put("referenceCurrency", "USD");
				instrumentDetails.put("marketPrice", "1824.97");
				instrumentDetails.put("closeRate", "1824.97");
				instrumentDetails.put("netchange", "29.61");
				instrumentDetails.put("percentageChange", "1.65");
				instrumentDetails.put("timeReceived", "12:20:00");
				instrumentDetails.put("dateReceived", sdf.format(date));

			} else if (isinCode.equalsIgnoreCase("AAPL.OQ")) {

				instrumentDetails.put("instrumentName", "Apple");
				instrumentDetails.put("ISINCode", "US0378331005");
				instrumentDetails.put("stockExchange", "NYSE");
				instrumentDetails.put(TemenosConstants.INSTRUMENTID, "100050-000");

				instrumentDetails.put("referenceCurrency", "USD");
				instrumentDetails.put("marketPrice", "123.08");
				instrumentDetails.put("closeRate", "123.08");
				instrumentDetails.put("netchange", "0.36");
				instrumentDetails.put("percentageChange", "0.29");
				instrumentDetails.put("timeReceived", "21:00:00");
				instrumentDetails.put("dateReceived", sdf.format(date));

			} else if (isinCode.equalsIgnoreCase("TSLA.OQ")) {

				instrumentDetails.put("instrumentName", "TSLA 5.300% 15Aug2025 Corp (USD)");
				instrumentDetails.put("ISINCode", "USU8810LAA18");
				instrumentDetails.put("stockExchange", "XNYS");
				instrumentDetails.put(TemenosConstants.INSTRUMENTID, "100056-000");
				instrumentDetails.put("referenceCurrency", "USD");
				instrumentDetails.put("marketPrice", "593.38");
				instrumentDetails.put("closeRate", "593.38");
				instrumentDetails.put("netchange", "24.56");
				instrumentDetails.put("percentageChange", "4.31");
				instrumentDetails.put("timeReceived", "06:22:50");
				instrumentDetails.put("dateReceived", sdf.format(date));

			} else if (isinCode.equalsIgnoreCase("GM.N")) {

				instrumentDetails.put("instrumentName", "General Motors, 4% 1apr2025, USD");
				instrumentDetails.put("ISINCode", "US37045VAG59");
				instrumentDetails.put("stockExchange", "XFRA");
				instrumentDetails.put(TemenosConstants.INSTRUMENTID, "100156-000");
				instrumentDetails.put("referenceCurrency", "USD");
				instrumentDetails.put("marketPrice", "52.04");
				instrumentDetails.put("closeRate", "52.04");
				instrumentDetails.put("netchange", "-1.35");
				instrumentDetails.put("percentageChange", "-2.53");
				instrumentDetails.put("timeReceived", "06:22:50");
				instrumentDetails.put("dateReceived", sdf.format(date));
				instrumentDetails.put("isSecurityAsset", true);

			} else if (isinCode.equalsIgnoreCase("F.N")) {

				instrumentDetails.put("instrumentName", "Ford, 8.875% 15jan2022, USD");
				instrumentDetails.put("ISINCode", "US345370BJ82");
				instrumentDetails.put("stockExchange", "XNYS");

				instrumentDetails.put(TemenosConstants.INSTRUMENTID, "100091-000");

				instrumentDetails.put("referenceCurrency", "USD");
				instrumentDetails.put("marketPrice", "12.86");
				instrumentDetails.put("closeRate", "12.86");
				instrumentDetails.put("netchange", "1.66");
				instrumentDetails.put("percentageChange", "0.55");
				instrumentDetails.put("timeReceived", "06:22:50");
				instrumentDetails.put("dateReceived", sdf.format(date));

			} else if (isinCode.equalsIgnoreCase("JPM.N")) {

				instrumentDetails.put("instrumentName", "JPMorgan Funds - US Growth Fund A (acc)");
				instrumentDetails.put("ISINCode", "LU0210536198");
				instrumentDetails.put("stockExchange", "MFAU");
				instrumentDetails.put(TemenosConstants.INSTRUMENTID, "100092-000");
				instrumentDetails.put("referenceCurrency", "USD");
				instrumentDetails.put("marketPrice", "3203.53");
				instrumentDetails.put("closeRate", "3203.53");
				instrumentDetails.put("netchange", "16.55");
				instrumentDetails.put("percentageChange", "0.51");
				instrumentDetails.put("timeReceived", "12:20:00");
				instrumentDetails.put("dateReceived", sdf.format(date));

			} else if (isinCode.equalsIgnoreCase("BLK")) {

				instrumentDetails.put("instrumentName", "iShares Core S&P 500 UCITS ETF");
				instrumentDetails.put("ISINCode", "IE00B5BMR087");
				instrumentDetails.put("stockExchange", "GER");

				instrumentDetails.put(TemenosConstants.INSTRUMENTID, "100093-000");

				instrumentDetails.put("referenceCurrency", "USD");
				instrumentDetails.put("marketPrice", "308.75");
				instrumentDetails.put("closeRate", "308.75");
				instrumentDetails.put("netchange", "0.13");
				instrumentDetails.put("percentageChange", "1.30");
				instrumentDetails.put("timeReceived", "12:20:00");
				instrumentDetails.put("dateReceived", sdf.format(date));

			} else if (isinCode.equalsIgnoreCase("FIDELIT.LG")) {

				instrumentDetails.put("instrumentName", "Fidelity S&P 500 Index USD P Acc");
				instrumentDetails.put("ISINCode", "IE00BYX5MS15");
				instrumentDetails.put("stockExchange", "NYSE");

				instrumentDetails.put(TemenosConstants.INSTRUMENTID, "100094-000");

				instrumentDetails.put("referenceCurrency", "USD");
				instrumentDetails.put("marketPrice", "3203.53");
				instrumentDetails.put("closeRate", "3203.53");
				instrumentDetails.put("netchange", "16.55");
				instrumentDetails.put("percentageChange", "0.51");
				instrumentDetails.put("timeReceived", "12:20:00");
				instrumentDetails.put("dateReceived", sdf.format(date));

			} else if (isinCode.equalsIgnoreCase("2YTD")) {

			} else if (isinCode.equalsIgnoreCase("5YTD")) {

			} else if (isinCode.equalsIgnoreCase("10YTD")) {

			} else if (isinCode.equalsIgnoreCase("BA.N")) {

				instrumentDetails.put("instrumentName", "The Boeing Company (BA)");
				instrumentDetails.put("ISINCode", "US0378331005");
				instrumentDetails.put("stockExchange", "NYSE");

				instrumentDetails.put(TemenosConstants.INSTRUMENTID, "100096-000");

				instrumentDetails.put("referenceCurrency", "USD");
				instrumentDetails.put("marketPrice", "120.3");
				instrumentDetails.put("closeRate", "120.3");
				instrumentDetails.put("netchange", "-1.04");
				instrumentDetails.put("percentageChange", "-0.87");
				instrumentDetails.put("timeReceived", "21:00:00");
				instrumentDetails.put("dateReceived", sdf.format(date));

			} else if (isinCode.equalsIgnoreCase("BA.NQ")) {

				instrumentDetails.put("instrumentName", "Boeing Co");
				instrumentDetails.put("ISINCode", "US0970231058");
				instrumentDetails.put("stockExchange", "NYSE");
				instrumentDetails.put("referenceCurrency", "USD");
				instrumentDetails.put("marketPrice", "202.06");
				instrumentDetails.put("closeRate", "202.06");
				instrumentDetails.put("netchange", "-1.3");
				instrumentDetails.put("percentageChange", "-0.64");
				instrumentDetails.put("timeReceived", "21:00:00");
				instrumentDetails.put("dateReceived", sdf.format(date));
				instrumentDetails.put("isSecurityAsset", true);

			} else if (isinCode.equalsIgnoreCase("AXP.N")) {

				instrumentDetails.put("instrumentName", "American Express Company");
				instrumentDetails.put("ISINCode", "US0258161092");
				instrumentDetails.put("stockExchange", "NYSE");

				instrumentDetails.put(TemenosConstants.INSTRUMENTID, "100020-000");

				instrumentDetails.put("referenceCurrency", "USD");
				instrumentDetails.put("marketPrice", "306.67");
				instrumentDetails.put("closeRate", "116.15");
				instrumentDetails.put("netchange", "1.66");
				instrumentDetails.put("percentageChange", "0.55");
				instrumentDetails.put("timeReceived", "21:00:00");
				instrumentDetails.put("dateReceived", sdf.format(date));

				instrumentDetails.put("isSecurityAsset", true);

			} else if (isinCode.equalsIgnoreCase("PNC.N")) {

				instrumentDetails.put("instrumentName", "AXP American Express Co.");
				instrumentDetails.put("ISINCode", "US0258161092");
				instrumentDetails.put("stockExchange", "NYSE");

				instrumentDetails.put(TemenosConstants.INSTRUMENTID, "100097-000");

				instrumentDetails.put("referenceCurrency", "USD");
				instrumentDetails.put("marketPrice", "116.15");
				instrumentDetails.put("closeRate", "116.15");
				instrumentDetails.put("netchange", "-5.00");
				instrumentDetails.put("percentageChange", "-4.13");
				instrumentDetails.put("timeReceived", "21:00:00");
				instrumentDetails.put("dateReceived", sdf.format(date));
				instrumentDetails.put("isSecurityAsset", true);

			} else if (isinCode.equalsIgnoreCase("CSCO.OQ")) {

				instrumentDetails.put("instrumentName", "Cisco Systems, Inc.");
				instrumentDetails.put("ISINCode", "US17275R1023");
				instrumentDetails.put("stockExchange", "NYSE");
				instrumentDetails.put(TemenosConstants.INSTRUMENTID, "100098-000");

				instrumentDetails.put("referenceCurrency", "USD");
				instrumentDetails.put("marketPrice", "120.3");
				instrumentDetails.put("closeRate", "120.3");
				instrumentDetails.put("netchange", "-1.04");
				instrumentDetails.put("percentageChange", "-0.87");
				instrumentDetails.put("timeReceived", "21:00:00");
				instrumentDetails.put("dateReceived", sdf.format(date));

			} else if (isinCode.equalsIgnoreCase("AAPL.N")) {

				instrumentDetails.put("instrumentName", "APPLE-CALL-115-16JUL");
				instrumentDetails.put("ISINCode", "US0378331005");
				instrumentDetails.put("stockExchange", "NYSE");
				instrumentDetails.put(TemenosConstants.INSTRUMENTID, "100099-000");

				instrumentDetails.put("referenceCurrency", "USD");
				instrumentDetails.put("marketPrice", "120.3");
				instrumentDetails.put("closeRate", "120.3");
				instrumentDetails.put("netchange", "-1.04");
				instrumentDetails.put("percentageChange", "-0.87");
				instrumentDetails.put("timeReceived", "21:00:00");
				instrumentDetails.put("dateReceived", sdf.format(date));

			} else if (isinCode.equalsIgnoreCase("GOOGL.N")) {

				instrumentDetails.put("instrumentName", "GOOGLE-PUT-2300-16JUL");
				instrumentDetails.put("ISINCode", "US02079K1079");
				instrumentDetails.put("stockExchange", "NYSE");
				instrumentDetails.put(TemenosConstants.INSTRUMENTID, "100028-000");

				instrumentDetails.put("referenceCurrency", "USD");
				instrumentDetails.put("marketPrice", "120.3");
				instrumentDetails.put("closeRate", "120.3");
				instrumentDetails.put("netchange", "-1.04");
				instrumentDetails.put("percentageChange", "-0.87");
				instrumentDetails.put("timeReceived", "21:00:00");
				instrumentDetails.put("dateReceived", sdf.format(date));

			} else if (isinCode.equalsIgnoreCase("TXG.OQ")) {

				instrumentDetails.put("instrumentName", "CITI-CALL-70-16JUL");
				instrumentDetails.put("ISINCode", "US1729671016");
				instrumentDetails.put("stockExchange", "NYSE");
				instrumentDetails.put(TemenosConstants.INSTRUMENTID, "100029-000");

				instrumentDetails.put("referenceCurrency", "USD");
				instrumentDetails.put("marketPrice", "60.91");
				instrumentDetails.put("closeRate", "60.91");
				instrumentDetails.put("netchange", "0.35");
				instrumentDetails.put("percentageChange", "0.58");
				instrumentDetails.put("timeReceived", "21:00:00");
				instrumentDetails.put("dateReceived", sdf.format(date));
				instrumentDetails.put("isSecurityAsset", true);

			} else if (isinCode.equalsIgnoreCase("FEURUSD")) {

			} else if (isinCode.equalsIgnoreCase("FGBPUSD")) {

			} else if (isinCode.equalsIgnoreCase("FCHFUSD")) {

			} else if (isinCode.equalsIgnoreCase("AMZN.O")) {

				instrumentDetails.put("ISINCode", "US0231351067");
				instrumentDetails.put("instrumentName", "Amazon.com Inc");
				instrumentDetails.put("stockExchange", "NYSE");
				instrumentDetails.put("referenceCurrency", "USD");
				instrumentDetails.put("marketPrice", "3203.53");
				instrumentDetails.put("closeRate", "3203.53");
				instrumentDetails.put("netchange", "16.55");
				instrumentDetails.put("percentageChange", "0.51");
				instrumentDetails.put("timeReceived", "12:20:00");
				instrumentDetails.put("dateReceived", sdf.format(date));
				instrumentDetails.put("isSecurityAsset", true);

			} else if (isinCode.equalsIgnoreCase("GOOGL.O")) {

				instrumentDetails.put("ISINCode", "US02079K1079");
				instrumentDetails.put("instrumentName", "Google LLC");
				instrumentDetails.put("stockExchange", "NYSE");
				instrumentDetails.put("referenceCurrency", "USD");
				instrumentDetails.put("marketPrice", "1776.84");
				instrumentDetails.put("closeRate", "1776.84");
				instrumentDetails.put("netchange", "4.67");
				instrumentDetails.put("percentageChange", "0.26");
				instrumentDetails.put("timeReceived", "20:59:00");
				instrumentDetails.put("dateReceived", sdf.format(date));
				instrumentDetails.put("isSecurityAsset", true);

			} else if (isinCode.equalsIgnoreCase("AAPL.O")) {

				instrumentDetails.put("ISINCode", "US0378331005");
				instrumentDetails.put("instrumentName", "Apple Inc");
				instrumentDetails.put("stockExchange", "NYSE");
				instrumentDetails.put("referenceCurrency", "USD");
				instrumentDetails.put("marketPrice", "123.08");
				instrumentDetails.put("closeRate", "123.08");
				instrumentDetails.put("netchange", "0.36");
				instrumentDetails.put("percentageChange", "0.29");
				instrumentDetails.put("timeReceived", "21:00:00");
				instrumentDetails.put("dateReceived", sdf.format(date));
				instrumentDetails.put("isSecurityAsset", true);

			} else if (isinCode.equalsIgnoreCase("LVMH.PA")) {

				instrumentDetails.put("ISINCode", "FR0000121014");
				instrumentDetails.put("instrumentName", "LVMH");
				instrumentDetails.put("stockExchange", "Euronext Paris");
				instrumentDetails.put("referenceCurrency", "EUR");
				instrumentDetails.put("marketPrice", "497.95");
				instrumentDetails.put("closeRate", "497.95");
				instrumentDetails.put("netchange", "0.15");
				instrumentDetails.put("percentageChange", "0.03");
				instrumentDetails.put("timeReceived", "06:22:50");
				instrumentDetails.put("dateReceived", sdf.format(date));
				instrumentDetails.put("isSecurityAsset", true);

			} else if (isinCode.equalsIgnoreCase("C.N")) {

				instrumentDetails.put("instrumentName", "Citigroup Inc");
				instrumentDetails.put("ISINCode", "US1729671016");
				instrumentDetails.put(TemenosConstants.INSTRUMENTID, "100018-000");

				instrumentDetails.put("referenceCurrency", "USD");
				instrumentDetails.put("marketPrice", "8.86");
				instrumentDetails.put("closeRate", "60.91");
				instrumentDetails.put("netchange", "0.35");
				instrumentDetails.put("percentageChange", "0.58");
				instrumentDetails.put("timeReceived", "21:00:00");
				instrumentDetails.put("dateReceived", sdf.format(date));
				instrumentDetails.put("isSecurityAsset", true);

			} else if (isinCode.equalsIgnoreCase("AMAG.OQ")) {

				instrumentDetails.put("ISINCode", "US02266311111");
				instrumentDetails.put("instrumentName", "AMAG PHARMACEUTICALS INC");
				instrumentDetails.put("stockExchange", "NYSE");
				instrumentDetails.put("referenceCurrency", "USD");
				instrumentDetails.put("marketPrice", "13.75");
				instrumentDetails.put("closeRate", "13.75");
				instrumentDetails.put("netchange", "0.02");
				instrumentDetails.put("percentageChange", "0.15");
				instrumentDetails.put("timeReceived", "06:22:50");
				instrumentDetails.put("dateReceived", sdf.format(date));
				instrumentDetails.put("isSecurityAsset", true);

			} else if (isinCode.equalsIgnoreCase("AMAL.OQ")) {

				instrumentDetails.put("ISINCode", "US0226631085");
				instrumentDetails.put("instrumentName", "AMALGAMTD BANK A");
				instrumentDetails.put("stockExchange", "NYSE");
				instrumentDetails.put("referenceCurrency", "USD");
				instrumentDetails.put("marketPrice", "13.15");
				instrumentDetails.put("closeRate", "13.15");
				instrumentDetails.put("netchange", "0.14");
				instrumentDetails.put("percentageChange", "-1.05");
				instrumentDetails.put("timeReceived", "06:22:50");
				instrumentDetails.put("dateReceived", sdf.format(date));
				instrumentDetails.put("isSecurityAsset", true);

			} else if (isinCode.equalsIgnoreCase("AMRN.OQ")) {

				instrumentDetails.put("ISINCode", "US0231112063");
				instrumentDetails.put("instrumentName", "AMARIN CORP");
				instrumentDetails.put("stockExchange", "NYSE");
				instrumentDetails.put("referenceCurrency", "USD");
				instrumentDetails.put("marketPrice", "4.93");
				instrumentDetails.put("closeRate", "4.93");
				instrumentDetails.put("netchange", "0.09");
				instrumentDetails.put("percentageChange", "1.85");
				instrumentDetails.put("timeReceived", "06:22:50");
				instrumentDetails.put("dateReceived", sdf.format(date));
				instrumentDetails.put("isSecurityAsset", true);

			} else if (isinCode.equalsIgnoreCase("AMBA.OQ")) {

				instrumentDetails.put("ISINCode", "US00001210000");
				instrumentDetails.put("instrumentName", "AMBARELLA INC");
				instrumentDetails.put("stockExchange", "NYSE");
				instrumentDetails.put("referenceCurrency", "USD");
				instrumentDetails.put("marketPrice", "60.72");
				instrumentDetails.put("closeRate", "60.72");
				instrumentDetails.put("netchange", "-0.0050");
				instrumentDetails.put("percentageChange", "-0.09");
				instrumentDetails.put("timeReceived", "06:22:50");
				instrumentDetails.put("dateReceived", sdf.format(date));
				instrumentDetails.put("isSecurityAsset", true);

			} else if (isinCode.equalsIgnoreCase("AMCX.O")) {

				instrumentDetails.put("ISINCode", "US00164V1035");
				instrumentDetails.put("instrumentName", "AMC NTWK CL A");
				instrumentDetails.put("stockExchange", "NYSE");
				instrumentDetails.put("referenceCurrency", "USD");
				instrumentDetails.put("marketPrice", "28.745");
				instrumentDetails.put("closeRate", "28.745");
				instrumentDetails.put("netchange", "-0.355");
				instrumentDetails.put("percentageChange", "-1.25");
				instrumentDetails.put("timeReceived", "06:22:50");
				instrumentDetails.put("dateReceived", sdf.format(date));
				instrumentDetails.put("isSecurityAsset", true);

			} else if (isinCode.equalsIgnoreCase("MSFT.N")) {

				instrumentDetails.put("ISINCode", "US5949181045");
				instrumentDetails.put("instrumentName", "MICROSOFT CP");
				instrumentDetails.put("stockExchange", "NYSE");
				instrumentDetails.put("referenceCurrency", "USD");
				instrumentDetails.put("marketPrice", "214.19");
				instrumentDetails.put("closeRate", "214.19");
				instrumentDetails.put("netchange", "-1.13");
				instrumentDetails.put("percentageChange", "-0.52");
				instrumentDetails.put("timeReceived", "06:22:50");
				instrumentDetails.put("dateReceived", sdf.format(date));
				instrumentDetails.put("isSecurityAsset", true);

			} else if (isinCode.equalsIgnoreCase("WMT.N")) {

				instrumentDetails.put("ISINCode", "US9311421039");
				instrumentDetails.put("instrumentName", "Walmart Inc");
				instrumentDetails.put("stockExchange", "NYSE");
				instrumentDetails.put("referenceCurrency", "USD");
				instrumentDetails.put("marketPrice", "148.91");
				instrumentDetails.put("closeRate", "148.91");
				instrumentDetails.put("netchange", "0.15");
				instrumentDetails.put("percentageChange", "0.03");
				instrumentDetails.put("timeReceived", "06:22:50");
				instrumentDetails.put("dateReceived", sdf.format(date));
				instrumentDetails.put("isSecurityAsset", true);

			} else if (isinCode.equalsIgnoreCase("IXM0461.DE")) {

				instrumentDetails.put("ISINCode", "IE00B5BMR087");
				instrumentDetails.put("instrumentName", "iShares Core S&P 500");
				instrumentDetails.put("stockExchange", "GER");
				instrumentDetails.put("referenceCurrency", "USD");
				instrumentDetails.put("marketPrice", "308.75");
				instrumentDetails.put("closeRate", "308.75");
				instrumentDetails.put("netchange", "0.13");
				instrumentDetails.put("percentageChange", "1.30");
				instrumentDetails.put("timeReceived", "06:22:50");
				instrumentDetails.put("dateReceived", sdf.format(date));
				instrumentDetails.put("isSecurityAsset", true);

			} else if (isinCode.equalsIgnoreCase("BAC.N")) {

				instrumentDetails.put("ISINCode", "US0605051046");
				instrumentDetails.put("instrumentName", "Bank of America Corp");
				instrumentDetails.put("stockExchange", "NYSE");
				instrumentDetails.put("referenceCurrency", "USD");
				instrumentDetails.put("marketPrice", "30.94");
				instrumentDetails.put("closeRate", "30.94");
				instrumentDetails.put("netchange", "-0.22");
				instrumentDetails.put("percentageChange", "-0.71");
				instrumentDetails.put("timeReceived", "06:22:50");
				instrumentDetails.put("dateReceived", sdf.format(date));
				instrumentDetails.put("isSecurityAsset", true);

			} else if (isinCode.equalsIgnoreCase("KO.N")) {

				instrumentDetails.put("ISINCode", "US1912161007");
				instrumentDetails.put("instrumentName", "Coca-Cola Co");
				instrumentDetails.put("stockExchange", "NYSE");
				instrumentDetails.put("referenceCurrency", "USD");
				instrumentDetails.put("marketPrice", "49.29");
				instrumentDetails.put("closeRate", "49.29");
				instrumentDetails.put("netchange", "0.51");
				instrumentDetails.put("percentageChange", "1.05");
				instrumentDetails.put("timeReceived", "06:22:50");
				instrumentDetails.put("dateReceived", sdf.format(date));
				instrumentDetails.put("isSecurityAsset", true);

			} else if (isinCode.equalsIgnoreCase("PFE.N")) {

				instrumentDetails.put("ISINCode", "INE182A01018");
				instrumentDetails.put("instrumentName", "Pfizer Inc");
				instrumentDetails.put("stockExchange", "NYSE");
				instrumentDetails.put("referenceCurrency", "USD");
				instrumentDetails.put("marketPrice", "37.31");
				instrumentDetails.put("closeRate", "37.31");
				instrumentDetails.put("netchange", "0.03");
				instrumentDetails.put("percentageChange", "0.08");
				instrumentDetails.put("timeReceived", "06:22:50");
				instrumentDetails.put("dateReceived", sdf.format(date));
				instrumentDetails.put("isSecurityAsset", true);

			} else if (isinCode.equalsIgnoreCase("NESN.S")) {

				instrumentDetails.put("ISINCode", "CH0012005267");
				instrumentDetails.put("instrumentName", "Nestle");
				instrumentDetails.put("stockExchange", "SWX");
				instrumentDetails.put("referenceCurrency", "EUR");
				instrumentDetails.put("marketPrice", "145.00");
				instrumentDetails.put("closeRate", "145.00");
				instrumentDetails.put("netchange", "-0.22");
				instrumentDetails.put("percentageChange", "-0.71");
				instrumentDetails.put("timeReceived", "06:22:50");
				instrumentDetails.put("dateReceived", sdf.format(date));
				instrumentDetails.put("isSecurityAsset", true);

			} else if (isinCode.equalsIgnoreCase("NOVN.S")) {

				instrumentDetails.put("ISINCode", "CH0012005267");
				instrumentDetails.put("instrumentName", "Nestle");
				instrumentDetails.put("stockExchange", "SWX");
				instrumentDetails.put("referenceCurrency", "EUR");
				instrumentDetails.put("marketPrice", "86.96");
				instrumentDetails.put("closeRate", "86.96");
				instrumentDetails.put("netchange", "0.35");
				instrumentDetails.put("percentageChange", "0.58");
				instrumentDetails.put("timeReceived", "06:22:50");
				instrumentDetails.put("dateReceived", sdf.format(date));
				instrumentDetails.put("isSecurityAsset", true);

			} else {
				instrumentDetails.put("ISINCode", "FRACARRE2354");
				instrumentDetails.put("instrumentName", "CARREFOUR SA");
				instrumentDetails.put("stockExchange", "Euronext Paris");
				instrumentDetails.put("referenceCurrency", "USD");
				instrumentDetails.put("marketPrice", "14.14");
				instrumentDetails.put("closeRate", "14.14");
				instrumentDetails.put("netchange", "36.61");
				instrumentDetails.put("percentageChange", "1.55");
				instrumentDetails.put("timeReceived", "22:00:00");
				instrumentDetails.put("dateReceived", sdf.format(date));
				instrumentDetails.put("isSecurityAsset", true);
			}
			JSONObject response1 = new JSONObject();
			response1.put("instrumentDetails", instrumentDetails.toString());
			if (instrumentDetails != null) {
				response1.put("opstatus", "0");
				response1.put("httpStatusCode", "200");
			}
			diagnostic.prepareDebug("==========> GetInstrumentDetailsMockPostProcessor Mock - Exiting with success").log();
			return Utilities.constructResultFromJSONObject(response1);
		} catch (Exception e) {
			alert.prepareError("==========> GetInstrumentDetailsMockPostProcessor Mock - Error: " + e.getMessage()).log();
			return result;
		}

	}

}
