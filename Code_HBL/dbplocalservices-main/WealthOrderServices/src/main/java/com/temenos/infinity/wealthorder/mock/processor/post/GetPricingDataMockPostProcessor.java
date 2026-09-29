/**
 * 
 */
package com.temenos.infinity.wealthorder.mock.processor.post;

import com.temenos.logger.Logger;
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
public class GetPricingDataMockPostProcessor implements DataPostProcessor2 {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		diagnostic.prepareDebug("==========> GetPricingDataMockPostProcessor Mock - Entered ").log();
		String isinCode = request.getParameter(TemenosConstants.RICCODE).toString();
		JSONObject responseVal = new JSONObject();
		if (isinCode.equalsIgnoreCase("AMZN.O")) {
			responseVal.put("ISINCode", "US0231351067");
			responseVal.put("referenceCurrency", "USD");
			responseVal.put("bidRate", "3201.12");
			responseVal.put("bidVolume", "9160");
			responseVal.put("askRate", "3204.32");
			responseVal.put("askVolume", "12560");
			responseVal.put("openRate", "3221.65");
			responseVal.put("closeRate", "3220.08");
			responseVal.put("volume", "132120");
			responseVal.put("high52W", "3552.25");
			responseVal.put("low52W", "1626.03");
			responseVal.put("latestRate", "3203.53");
		} else if (isinCode.equalsIgnoreCase("GOOGL.O")) {
			responseVal.put("ISINCode", "US02079K1079");
			responseVal.put("referenceCurrency", "USD");
			responseVal.put("bidRate", "1769.77");
			responseVal.put("bidVolume", "10");
			responseVal.put("askRate", "1787.6");
			responseVal.put("askVolume", "12");
			responseVal.put("openRate", "1767.875");
			responseVal.put("closeRate", "1772.17");
			responseVal.put("volume", "14419");
			responseVal.put("high52W", "1816.09");
			responseVal.put("low52W", "1009.0");
			responseVal.put("latestRate", "1776.84");
		} else if (isinCode.equalsIgnoreCase("AAPL.O")) {
			responseVal.put("ISINCode", "US0378331005");
			responseVal.put("referenceCurrency", "USD");
			responseVal.put("bidRate", "122.08");
			responseVal.put("bidVolume", "6");
			responseVal.put("askRate", "124.08");
			responseVal.put("askVolume", "1");
			responseVal.put("openRate", "118.88");
			responseVal.put("closeRate", "120.3");
			responseVal.put("volume", "20769880");
			responseVal.put("high52W", "137.98");
			responseVal.put("low52W", "53.1575");
			responseVal.put("latestRate", "120.3");
		} else if (isinCode.equalsIgnoreCase("LVMH.PA")) {
			responseVal.put("ISINCode", "FR0000121014");
			responseVal.put("referenceCurrency", "EUR");
			responseVal.put("bidRate", "478.45");
			responseVal.put("bidVolume", "713");
			responseVal.put("askRate", "476.65");
			responseVal.put("askVolume", "288");
			responseVal.put("openRate", "479.00");
			responseVal.put("closeRate", "477.3");
			responseVal.put("volume", "752");
			responseVal.put("high52W", "489.5");
			responseVal.put("low52W", "278.7");
			responseVal.put("latestRate", "477.3");
		} else if (isinCode.equalsIgnoreCase("AMAG.OQ")) {

			responseVal.put("ISINCode", "US02266311111");
			responseVal.put("referenceCurrency", "USD");
			responseVal.put("bidRate", "13.74");
			responseVal.put("bidVolume", "5");
			responseVal.put("askRate", "13.70");
			responseVal.put("askVolume", "3");
			responseVal.put("openRate", "13.75");
			responseVal.put("closeRate", "13.73");
			responseVal.put("volume", "742572");
			responseVal.put("high52W", "13.80");
			responseVal.put("low52W", "4.41");
			responseVal.put("latestRate", "120.3");
		} else if (isinCode.equalsIgnoreCase("AMAL.O")) {

			responseVal.put("ISINCode", "US0226631085");
			responseVal.put("referenceCurrency", "USD");
			responseVal.put("bidRate", "13.3");
			responseVal.put("bidVolume", "1");
			responseVal.put("askRate", "13.43");
			responseVal.put("askVolume", "1");
			responseVal.put("openRate", "13.11");
			responseVal.put("closeRate", "13.29");
			responseVal.put("volume", "2108");
			responseVal.put("high52W", "19.98");
			responseVal.put("low52W", "7.96");
			responseVal.put("latestRate", "13.31");
		} else if (isinCode.equalsIgnoreCase("AMRN.O")) {

			responseVal.put("ISINCode", "US0231112063");
			responseVal.put("referenceCurrency", "USD");
			responseVal.put("bidRate", "4.89");
			responseVal.put("bidVolume", "36");
			responseVal.put("askRate", "5.04");
			responseVal.put("askVolume", "4");
			responseVal.put("openRate", "4.84");
			responseVal.put("closeRate", "4.93");
			responseVal.put("volume", "1175064");
			responseVal.put("high52W", "26.11");
			responseVal.put("low52W", "3.36");
			responseVal.put("latestRate", "4.93");
		} else if (isinCode.equalsIgnoreCase("AMBA.O")) {

			responseVal.put("ISINCode", "US00001210000");
			responseVal.put("referenceCurrency", "USD");
			responseVal.put("bidRate", "60.87");
			responseVal.put("bidVolume", "1");
			responseVal.put("askRate", "60.98");
			responseVal.put("askVolume", "1");
			responseVal.put("openRate", "60.68");
			responseVal.put("closeRate", "60.725");
			responseVal.put("volume", "15445");
			responseVal.put("high52W", "73.59");
			responseVal.put("low52W", "36.02");
			responseVal.put("latestRate", "60.92");
		} else if (isinCode.equalsIgnoreCase("AMCX.O")) {

			responseVal.put("ISINCode", "US00164V1035");
			responseVal.put("referenceCurrency", "USD");
			responseVal.put("bidRate", "28.83");
			responseVal.put("bidVolume", "1");
			responseVal.put("askRate", "28.85");
			responseVal.put("askVolume", "1");
			responseVal.put("openRate", "28.07");
			responseVal.put("closeRate", "28.39");
			responseVal.put("volume", "245551");
			responseVal.put("high52W", "42.63");
			responseVal.put("low52W", "19.62");
			responseVal.put("latestRate", "28.83");
		} else if (isinCode.equalsIgnoreCase("MSFT.O")) {

			responseVal.put("ISINCode", "US5949181045");
			responseVal.put("referenceCurrency", "USD");
			responseVal.put("bidRate", "214.70");
			responseVal.put("bidVolume", "23");
			responseVal.put("askRate", "214.85");
			responseVal.put("askVolume", "7");
			responseVal.put("openRate", "214.75");
			responseVal.put("closeRate", "215.32");
			responseVal.put("volume", "804970");
			responseVal.put("high52W", "232.86");
			responseVal.put("low52W", "132.52");
			responseVal.put("latestRate", "214.19");
		} else if (isinCode.equalsIgnoreCase("TSLA.O")) {

			responseVal.put("ISINCode", "US88160R1014");
			responseVal.put("referenceCurrency", "USD");
			responseVal.put("bidRate", "593.0");
			responseVal.put("bidVolume", "5");
			responseVal.put("askRate", "593.48");
			responseVal.put("askVolume", "1");
			responseVal.put("openRate", "590.88");
			responseVal.put("closeRate", "593.38");
			responseVal.put("volume", "8435006");
			responseVal.put("high52W", "607.77");
			responseVal.put("low52W", "65.45");
			responseVal.put("latestRate", "593.38");
		} else if (isinCode.equalsIgnoreCase("WMT.N")) {

			responseVal.put("ISINCode", "US9311421039");
			responseVal.put("referenceCurrency", "USD");
			responseVal.put("bidRate", "149.25");
			responseVal.put("bidVolume", "19");
			responseVal.put("askRate", "149.80");
			responseVal.put("askVolume", "3");
			responseVal.put("openRate", "150.0");
			responseVal.put("closeRate", "149.3");
			responseVal.put("volume", "2087843");
			responseVal.put("high52W", "153.4");
			responseVal.put("low52W", "102.0");
			responseVal.put("latestRate", "149.3");
		} else if (isinCode.equalsIgnoreCase("IXM0461.DE")) {

			responseVal.put("ISINCode", "IE00B5BMR087");
			responseVal.put("referenceCurrency", "EUR");
			responseVal.put("bidRate", "306.65");
			responseVal.put("bidVolume", "19");
			responseVal.put("askRate", "307.5");
			responseVal.put("askVolume", "3");
			responseVal.put("openRate", "304.90");
			responseVal.put("closeRate", "305.01");
			responseVal.put("volume", "2087843");
			responseVal.put("high52W", "312.90");
			responseVal.put("low52W", "203.30");
			responseVal.put("latestRate", "306.67");
		} else if (isinCode.equalsIgnoreCase("AMZN.OQ")) {
			responseVal.put("ISINCode", "US0231351067");
			responseVal.put("referenceCurrency", "USD");
			responseVal.put("bidRate", "1769.77");
			responseVal.put("bidVolume", "10");
			responseVal.put("askRate", "1787.6");
			responseVal.put("askVolume", "12");
			responseVal.put("openRate", "1767.875");
			responseVal.put("closeRate", "1772.17");
			responseVal.put("volume", "14419");
			responseVal.put("high52W", "1816.09");
			responseVal.put("low52W", "1009.0");
			responseVal.put("latestRate", "3203.53");
		} else if (isinCode.equalsIgnoreCase("GOOGL.OQ")) {
			responseVal.put("ISINCode", "US02079K1079");
			responseVal.put("referenceCurrency", "USD");
			responseVal.put("bidRate", "1769.77");
			responseVal.put("bidVolume", "10");
			responseVal.put("askRate", "1787.6");
			responseVal.put("askVolume", "12");
			responseVal.put("openRate", "1767.875");
			responseVal.put("closeRate", "1772.17");
			responseVal.put("volume", "14419");
			responseVal.put("high52W", "1816.09");
			responseVal.put("low52W", "1009.0");
			responseVal.put("latestRate", "1824.97");
		} else if (isinCode.equalsIgnoreCase("AAPL.OQ")) {
			responseVal.put("ISINCode", "US0378331005");
			responseVal.put("referenceCurrency", "USD");
			responseVal.put("bidRate", "1769.77");
			responseVal.put("bidVolume", "10");
			responseVal.put("askRate", "1787.6");
			responseVal.put("askVolume", "12");
			responseVal.put("openRate", "1767.875");
			responseVal.put("closeRate", "1772.17");
			responseVal.put("volume", "14419");
			responseVal.put("high52W", "1816.09");
			responseVal.put("low52W", "1009.0");
			responseVal.put("latestRate", "123.08");
		} else if (isinCode.equalsIgnoreCase("TSLA.OQ")) {
			responseVal.put("ISINCode", "USU8810LAA18");
			responseVal.put("referenceCurrency", "USD");
			responseVal.put("bidRate", "1769.77");
			responseVal.put("bidVolume", "10");
			responseVal.put("askRate", "1787.6");
			responseVal.put("askVolume", "12");
			responseVal.put("openRate", "1767.875");
			responseVal.put("closeRate", "1772.17");
			responseVal.put("volume", "14419");
			responseVal.put("high52W", "1816.09");
			responseVal.put("low52W", "1009.0");
			responseVal.put("latestRate", "593.38");
		} else if (isinCode.equalsIgnoreCase("GM.N")) {
			responseVal.put("ISINCode", "US37045VAG59");
			responseVal.put("referenceCurrency", "USD");
			responseVal.put("bidRate", "1769.77");
			responseVal.put("bidVolume", "10");
			responseVal.put("askRate", "1787.6");
			responseVal.put("askVolume", "12");
			responseVal.put("openRate", "1767.875");
			responseVal.put("closeRate", "1772.17");
			responseVal.put("volume", "14419");
			responseVal.put("high52W", "1816.09");
			responseVal.put("low52W", "1009.0");
			responseVal.put("latestRate", "52.04");
		} else if (isinCode.equalsIgnoreCase("F.N")) {
			responseVal.put("ISINCode", "US345370BJ82");
			responseVal.put("referenceCurrency", "USD");
			responseVal.put("bidRate", "1769.77");
			responseVal.put("bidVolume", "10");
			responseVal.put("askRate", "1787.6");
			responseVal.put("askVolume", "12");
			responseVal.put("openRate", "1767.875");
			responseVal.put("closeRate", "1772.17");
			responseVal.put("volume", "14419");
			responseVal.put("high52W", "1816.09");
			responseVal.put("low52W", "1009.0");
			responseVal.put("latestRate", "12.86");
		} else if (isinCode.equalsIgnoreCase("JPM.N")) {
			responseVal.put("ISINCode", "LU0210536198");
			responseVal.put("referenceCurrency", "USD");
			responseVal.put("bidRate", "1769.77");
			responseVal.put("bidVolume", "10");
			responseVal.put("askRate", "1787.6");
			responseVal.put("askVolume", "12");
			responseVal.put("openRate", "1767.875");
			responseVal.put("closeRate", "1772.17");
			responseVal.put("volume", "14419");
			responseVal.put("high52W", "1816.09");
			responseVal.put("low52W", "1009.0");
			responseVal.put("latestRate", "3203.53");
		} else if (isinCode.equalsIgnoreCase("BLK")) {
			responseVal.put("ISINCode", "IE0031442068");
			responseVal.put("referenceCurrency", "USD");
			responseVal.put("bidRate", "1769.77");
			responseVal.put("bidVolume", "10");
			responseVal.put("askRate", "1787.6");
			responseVal.put("askVolume", "12");
			responseVal.put("openRate", "1767.875");
			responseVal.put("closeRate", "1772.17");
			responseVal.put("volume", "14419");
			responseVal.put("high52W", "1816.09");
			responseVal.put("low52W", "1009.0");
			responseVal.put("latestRate", "308.75");
		} else if (isinCode.equalsIgnoreCase("FIDELIT.LG")) {
			responseVal.put("ISINCode", "IE00BYX5MS15");
			responseVal.put("referenceCurrency", "USD");
			responseVal.put("bidRate", "1769.77");
			responseVal.put("bidVolume", "10");
			responseVal.put("askRate", "1787.6");
			responseVal.put("askVolume", "12");
			responseVal.put("openRate", "1767.875");
			responseVal.put("closeRate", "1772.17");
			responseVal.put("volume", "14419");
			responseVal.put("high52W", "1816.09");
			responseVal.put("low52W", "1009.0");
			responseVal.put("latestRate", "3203.53");
		} else if (isinCode.equalsIgnoreCase("BA.N")) {
			responseVal.put("ISINCode", "US0378331005");
			responseVal.put("referenceCurrency", "USD");
			responseVal.put("bidRate", "1769.77");
			responseVal.put("bidVolume", "10");
			responseVal.put("askRate", "1787.6");
			responseVal.put("askVolume", "12");
			responseVal.put("openRate", "1767.875");
			responseVal.put("closeRate", "1772.17");
			responseVal.put("volume", "14419");
			responseVal.put("high52W", "1816.09");
			responseVal.put("low52W", "1009.0");
			responseVal.put("latestRate", "120.3");
		} else if (isinCode.equalsIgnoreCase("BA.NQ")) {
			responseVal.put("ISINCode", "US0970231058");
			responseVal.put("referenceCurrency", "USD");
			responseVal.put("bidRate", "1769.77");
			responseVal.put("bidVolume", "10");
			responseVal.put("askRate", "1787.6");
			responseVal.put("askVolume", "12");
			responseVal.put("openRate", "1767.875");
			responseVal.put("closeRate", "1772.17");
			responseVal.put("volume", "14419");
			responseVal.put("high52W", "1816.09");
			responseVal.put("low52W", "1009.0");
			responseVal.put("latestRate", "202.06");
		} else if (isinCode.equalsIgnoreCase("AXP.N")) {
			responseVal.put("ISINCode", "US0258161092");
			responseVal.put("referenceCurrency", "USD");
			responseVal.put("bidRate", "1769.77");
			responseVal.put("bidVolume", "10");
			responseVal.put("askRate", "1787.6");
			responseVal.put("askVolume", "12");
			responseVal.put("openRate", "1767.875");
			responseVal.put("closeRate", "1772.17");
			responseVal.put("volume", "14419");
			responseVal.put("high52W", "1816.09");
			responseVal.put("low52W", "1009.0");
			responseVal.put("latestRate", "306.67");
		} else if (isinCode.equalsIgnoreCase("PNC.N")) {
			responseVal.put("ISINCode", "US0258161092");
			responseVal.put("referenceCurrency", "USD");
			responseVal.put("bidRate", "1769.77");
			responseVal.put("bidVolume", "10");
			responseVal.put("askRate", "1787.6");
			responseVal.put("askVolume", "12");
			responseVal.put("openRate", "1767.875");
			responseVal.put("closeRate", "1772.17");
			responseVal.put("volume", "14419");
			responseVal.put("high52W", "1816.09");
			responseVal.put("low52W", "1009.0");
			responseVal.put("latestRate", "116.15");
		} else if (isinCode.equalsIgnoreCase("CSCO.OQ")) {
			responseVal.put("ISINCode", "US17275R1023");
			responseVal.put("referenceCurrency", "USD");
			responseVal.put("bidRate", "1769.77");
			responseVal.put("bidVolume", "10");
			responseVal.put("askRate", "1787.6");
			responseVal.put("askVolume", "12");
			responseVal.put("openRate", "1767.875");
			responseVal.put("closeRate", "1772.17");
			responseVal.put("volume", "14419");
			responseVal.put("high52W", "1816.09");
			responseVal.put("low52W", "1009.0");
			responseVal.put("latestRate", "120.3");
		} else if (isinCode.equalsIgnoreCase("AAPL.N")) {
			responseVal.put("ISINCode", "US0378331005");
			responseVal.put("referenceCurrency", "USD");
			responseVal.put("bidRate", "1769.77");
			responseVal.put("bidVolume", "10");
			responseVal.put("askRate", "1787.6");
			responseVal.put("askVolume", "12");
			responseVal.put("openRate", "1767.875");
			responseVal.put("closeRate", "1772.17");
			responseVal.put("volume", "14419");
			responseVal.put("high52W", "1816.09");
			responseVal.put("low52W", "1009.0");
			responseVal.put("latestRate", "120.3");
		} else if (isinCode.equalsIgnoreCase("GOOGL.N")) {
			responseVal.put("ISINCode", "US02079K1079");
			responseVal.put("referenceCurrency", "USD");
			responseVal.put("bidRate", "1769.77");
			responseVal.put("bidVolume", "10");
			responseVal.put("askRate", "1787.6");
			responseVal.put("askVolume", "12");
			responseVal.put("openRate", "1767.875");
			responseVal.put("closeRate", "1772.17");
			responseVal.put("volume", "14419");
			responseVal.put("high52W", "1816.09");
			responseVal.put("low52W", "1009.0");
			responseVal.put("latestRate", "120.3");
		} else if (isinCode.equalsIgnoreCase("C.N")) {
			responseVal.put("ISINCode", "US1729671016");
			responseVal.put("referenceCurrency", "USD");
			responseVal.put("bidRate", "1769.77");
			responseVal.put("bidVolume", "10");
			responseVal.put("askRate", "1787.6");
			responseVal.put("askVolume", "12");
			responseVal.put("openRate", "1767.875");
			responseVal.put("closeRate", "1772.17");
			responseVal.put("volume", "14419");
			responseVal.put("high52W", "1816.09");
			responseVal.put("low52W", "1009.0");
			responseVal.put("latestRate", "8.86");
		} else if (isinCode.equalsIgnoreCase("BAC.N")) {
			responseVal.put("ISINCode", "US0605051046");
			responseVal.put("referenceCurrency", "USD");
			responseVal.put("bidRate", "1719.77");
			responseVal.put("bidVolume", "10");
			responseVal.put("askRate", "1717.6");
			responseVal.put("askVolume", "13");
			responseVal.put("openRate", "1717.875");
			responseVal.put("closeRate", "1742.17");
			responseVal.put("volume", "14409");
			responseVal.put("high52W", "1810.09");
			responseVal.put("low52W", "1090.0");
			responseVal.put("latestRate", "30.94");
		} else if (isinCode.equalsIgnoreCase("KO.N")) {
			responseVal.put("ISINCode", "US1912161007");
			responseVal.put("referenceCurrency", "USD");
			responseVal.put("bidRate", "1739.77");
			responseVal.put("bidVolume", "11");
			responseVal.put("askRate", "1785.6");
			responseVal.put("askVolume", "10");
			responseVal.put("openRate", "1967.875");
			responseVal.put("closeRate", "1272.17");
			responseVal.put("volume", "13019");
			responseVal.put("high52W", "1716.09");
			responseVal.put("low52W", "1089.0");
			responseVal.put("latestRate", "49.29");
		}
		JSONObject pricingDetails = new JSONObject();
		if (responseVal != null && responseVal.length() > 0) {
			pricingDetails.put("pricingDetails", responseVal.toString());
			pricingDetails.put("opstatus", "0");
			pricingDetails.put("httpStatusCode", "200");
		}
		Result final_result = Utilities.constructResultFromJSONObject(pricingDetails);
		final_result.addOpstatusParam("0");
		final_result.addHttpStatusCodeParam("200");
		final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		diagnostic.prepareDebug("==========> GetPricingDataMockPostProcessor Mock - Exiting with success").log();
		return final_result;
	}

}
