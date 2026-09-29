/**
 * 
 */
package com.temenos.infinity.wealth.mock.processor.post;

import org.json.JSONArray;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.wealth.common.util.PortfolioServiceUtils;
import com.temenos.infinity.api.commons.utils.Utilities;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

/**
 * @author himaja.sridhar
 *
 */
public class GetAccountActivityMockPostProcessor implements DataPostProcessor2 {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@SuppressWarnings("unused")
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		diagnostic.prepareDebug("==========> GetAccountActivityMockPostProcessor Mock - Entered ").log();
		String portfolioId = (String) request.getParameter(TemenosConstants.PORTFOLIOID);
		String accountId = (String) request.getParameter(TemenosConstants.ACCID);
		String listType = (String) request.getParameter(TemenosConstants.LISTTYPE);
		String dateFrom = (String) request.getParameter(TemenosConstants.DATEFROM);
		String dateTo = (String) request.getParameter(TemenosConstants.DATETO);
		String search = (String) request.getParameter(TemenosConstants.SEARCHBYINSTRUMENTNAME);
		String sortBy = (String) request.getParameter(TemenosConstants.SORTBY);

		String[] balance = null, displayName = null, accId = null, quantity = null, amount = null, currencyId = null,
				shortName = null, bookingDate = null, valueDate = null, generatedBookingDate = null,
				generatedValueDate = null;
		JSONArray accListArr = new JSONArray();
		JSONObject responseVal = new JSONObject();
		JSONObject status = new JSONObject();
		JSONObject accountActivity = new JSONObject();
		if (portfolioId.equalsIgnoreCase("100777-1")) {
			if (accountId.equalsIgnoreCase("11098")) {
				balance = new String[] { "35020.60", "34628.21", "33802.38", "33405.98", "31504.08", "29927.51",
						"29477.06", "24931.72", "30038.32", "29865.82", "33865.82", "31401.46", "28832.32", "18428.17",
						"10312.11" };
				displayName = new String[] { "Securities Purchase", "Securities Purchase", "Securities Purchase",
						"Securities Purchase", "Securities Purchase", "Securities Purchase", "Securities Purchase",
						"Securities Purchase", "Securities Sale", "Securities Transfer In", "Internal Transfer",
						"Securities Purchase", "Securities Purchase", "Securities Purchase", "Securities Purchase" };
				accId = new String[] { "11098", "11098", "11098", "11098", "11098", "11098", "11098", "11098", "11098",
						"11098", "11098", "11098", "11098", "11098", "11098" };
				quantity = new String[] { "10", "8", "15", "12", "10", "15", "10", "15", "3", "", "", "5", "23", "4",
						"5" };
				amount = new String[] { "-340.34", "-392.39", "-825.83", "-396.40", "-1901.90", "-1576.58", "-450.45",
						"-4545.34", "+5106.60", "-172.50", "+4000.00", "-2464.36", "-2569.14", "-10404.15",
						"-8116.06" };
				currencyId = new String[] { "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD",
						"USD", "USD", "USD", "USD" };
				shortName = new String[] { "Pfizer Inc", "General Motors Company", "Citigroup Inc",
						"Bank of America Corp", "Boeing Co", "American Express Company", "Coca-Cola Co",
						"iShares Core S&P 500 UCITS ETF USD", "Google LLC", "Uk Govt Tres 6 Pct 10-01-2029", "",
						"LVMH Moet Hennessy Louis Vuitton SE", "Apple Computer Inc Com Stk (Us)", "Amazon.com",
						"Google LLC" };
				generatedBookingDate = PortfolioServiceUtils.mockAccountActivityBookingDate(displayName.length);
				bookingDate = PortfolioServiceUtils.reverseArray(generatedBookingDate);
				generatedValueDate = PortfolioServiceUtils.mockAccountActivityBookingDate(displayName.length);
				valueDate = PortfolioServiceUtils.reverseArray(generatedValueDate);
			}
			if (accountId.equalsIgnoreCase("13465")) {
				balance = new String[] { "2183.45", "2083.45", "4142.79" };
				displayName = new String[] { "Internal Transfer", "Charge Capitalise", "Internal Transfer" };
				accId = new String[] { "13465", "13465", "13465" };
				quantity = new String[] { "", "", "" };
				amount = new String[] { "+415.78", "-100.00", "+2059.34" };
				currencyId = new String[] { "EUR", "EUR", "EUR" };
				shortName = new String[] { "", "", "" };
				generatedBookingDate = PortfolioServiceUtils.mockAccountActivityBookingDate(displayName.length);
				bookingDate = PortfolioServiceUtils.reverseArray(generatedBookingDate);
				generatedValueDate = PortfolioServiceUtils.mockAccountActivityBookingDate(displayName.length);
				valueDate = PortfolioServiceUtils.reverseArray(generatedValueDate);
			}
		} else if (portfolioId.equalsIgnoreCase("100777-2")) {
			if (accountId.equalsIgnoreCase("11156")) {
				balance = new String[] { "32850.85", "32550.55", "31817.82", "31574.58", "30101.11", "29044.05",
						"28791.80", "36791.80", "37083.43", "42190.03", "41068.91", "40287.00", "35084.92", "8288.46",
						"172.40" };
				displayName = new String[] { "Securities Purchase", "Securities Purchase", "Securities Purchase",
						"Securities Purchase", "Securities Purchase", "Securities Purchase", "Securities Purchase",
						"Internal Transfer", "Securities Sale", "Securities Sale", "Securities Purchase",
						"Securities Purchase", "Securities Purchase", "Internal Transfer Out", "Securities Purchase" };
				accId = new String[] { "11156", "11156", "11156", "11156", "11156", "11156", "", "11156", "11156",
						"11156", "11156", "11156", "11156", "11156", "11156" };
				quantity = new String[] { "9", "6", "12", "9", "8", "10", "6", "", "2", "3", "8", "7", "2", "", "5" };
				amount = new String[] { "-283.78", "-300.30", "-732.73", "-243.24", "-1473.47", "-1057.06", "-252.25",
						"+8000.00", "+291.63", "+5106.60", "-1121.12", "-781.91", "-5202.08", "+26796.47", "-8116.06" };
				currencyId = new String[] { "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD",
						"USD", "USD", "", "USD" };
				shortName = new String[] { "Pfizer Inc", "General Motors Company", "Citigroup Inc",
						"Bank of America Corp", "Boeing Co", "American Express Company", "Coca-Cola Co", "",
						"Walmart Inc", "Google LLC", "Walmart Inc", "Apple Computer Inc Com Stk (Us)", "Amazon.com", "",
						"Google LLC" };
				generatedBookingDate = PortfolioServiceUtils.mockAccountActivityBookingDate(displayName.length);
				bookingDate = PortfolioServiceUtils.reverseArray(generatedBookingDate);
				generatedValueDate = PortfolioServiceUtils.mockAccountActivityBookingDate(displayName.length);
				valueDate = PortfolioServiceUtils.reverseArray(generatedValueDate);
			}
			if (accountId.equalsIgnoreCase("16325")) {
				balance = new String[] { "133.35", "112.79" };
				displayName = new String[] { "Internal Transfer", "Charge Capitalise" };
				accId = new String[] { "16325", "16325" };
				quantity = new String[] { "", "" };
				amount = new String[] { "100", "-20.56" };
				currencyId = new String[] { "EUR", "EUR" };
				shortName = new String[] { "", "" };
				generatedBookingDate = PortfolioServiceUtils.mockAccountActivityBookingDate(displayName.length);
				bookingDate = PortfolioServiceUtils.reverseArray(generatedBookingDate);
				generatedValueDate = PortfolioServiceUtils.mockAccountActivityBookingDate(displayName.length);
				valueDate = PortfolioServiceUtils.reverseArray(generatedValueDate);
			}
		} else if (portfolioId.equalsIgnoreCase("100777-3")) {
			if (accountId.equalsIgnoreCase("12573")) {
				balance = new String[] { "48380.50", "48434.56", "48074.20", "36158.79", "33176.91", "32059.90",
						"22545.70", "9560.00", };
				displayName = new String[] { "Securities Purchase", "Securities Purchase", "Securities Purchase",
						"Securities Purchase", "Securities Purchase", "Securities Purchase", "Internal Transfer Out",
						"Securities Purchase" };
				accId = new String[] { "12573", "12573", "12573", "12573", "12573", "12573", "12573", "12573" };
				quantity = new String[] { "10", "9", "8", "7", "5", "10", "5", "8" };
				amount = new String[] { "-1901.90", "-945.95", "-360.36", "-11915.40", "-2981.88", "-1117.02",
						"-9514.20", "-12985.69", };
				currencyId = new String[] { "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD" };
				shortName = new String[] { "Boeing Co", "American Express Company", "Coca-Cola Co", "Google LLC",
						"LVMH Moet Hennessy Louis Vuitton SE", "Apple Computer Inc Com Stk (Us)", "", "Google LLC" };
				generatedBookingDate = PortfolioServiceUtils.mockAccountActivityBookingDate(displayName.length);
				bookingDate = PortfolioServiceUtils.reverseArray(generatedBookingDate);
				generatedValueDate = PortfolioServiceUtils.mockAccountActivityBookingDate(displayName.length);
				valueDate = PortfolioServiceUtils.reverseArray(generatedValueDate);
			}
		}
		if (portfolioId.equalsIgnoreCase("100777-4")) {
			if (accountId.equalsIgnoreCase("120057")) {
				balance = new String[] { "35020.60", "34628.21", "33802.38", "33405.98", "31504.08", "29927.51",
						"29477.06", "24931.72", "30038.32", "29865.82", "33865.82", "31401.46", "28832.32", "18428.17",
						"10312.11" };
				displayName = new String[] { "Securities Purchase", "Securities Purchase", "Securities Purchase",
						"Securities Purchase", "Securities Purchase", "Securities Purchase", "Securities Purchase",
						"Securities Purchase", "Securities Sale", "Securities Transfer In", "Internal Transfer",
						"Securities Purchase", "Securities Purchase", "Securities Purchase", "Securities Purchase" };
				accId = new String[] { "120057", "120057", "120057", "120057", "120057", "120057", "120057", "120057",
						"120057", "120057", "120057", "120057", "120057", "120057", "120057" };
				quantity = new String[] { "10", "8", "15", "12", "10", "15", "10", "15", "3", "", "", "5", "23", "4",
						"5" };
				amount = new String[] { "-340.34", "-392.39", "-825.83", "-396.40", "-1901.90", "-1576.58", "-450.45",
						"-4545.34", "+5106.60", "-172.50", "+4000.00", "-2464.36", "-2569.14", "-10404.15",
						"-8116.06" };
				currencyId = new String[] { "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD",
						"USD", "USD", "USD", "USD" };
				shortName = new String[] { "Pfizer Inc", "General Motors Company", "Citigroup Inc",
						"Bank of America Corp", "Boeing Co", "American Express Company", "Coca-Cola Co",
						"iShares Core S&P 500 UCITS ETF USD", "Google LLC", "Uk Govt Tres 6 Pct 10-01-2029", "",
						"LVMH Moet Hennessy Louis Vuitton SE", "Apple Computer Inc Com Stk (Us)", "Amazon.com",
						"Google LLC" };
				generatedBookingDate = PortfolioServiceUtils.mockAccountActivityBookingDate(displayName.length);
				bookingDate = PortfolioServiceUtils.reverseArray(generatedBookingDate);
				generatedValueDate = PortfolioServiceUtils.mockAccountActivityBookingDate(displayName.length);
				valueDate = PortfolioServiceUtils.reverseArray(generatedValueDate);
			}
			if (accountId.equalsIgnoreCase("120065")) {
				balance = new String[] { "4458.57", "4042.79", "4142.79" };
				displayName = new String[] { "Internal Transfer", "Charge Capitalise", "Internal Transfer" };
				accId = new String[] { "120065", "120065", "120065" };
				quantity = new String[] { "", "", "" };
				amount = new String[] { "415.78", "-100", "2059.34" };
				currencyId = new String[] { "EUR", "EUR", "EUR" };
				shortName = new String[] { "", "", "" };
				generatedBookingDate = PortfolioServiceUtils.mockAccountActivityBookingDate(displayName.length);
				bookingDate = PortfolioServiceUtils.reverseArray(generatedBookingDate);
				generatedValueDate = PortfolioServiceUtils.mockAccountActivityBookingDate(displayName.length);
				valueDate = PortfolioServiceUtils.reverseArray(generatedValueDate);
			}
		}
		if (portfolioId.equalsIgnoreCase("100777-5")) {
			if (accountId.equalsIgnoreCase("11992")) {
				balance = new String[] { "32850.85", "32550.55", "31817.82", "31574.58", "30101.11", "29044.05",
						"28791.80", "36791.80", "37083.43", "42190.03", "41068.91", "40287.00", "35084.92", "8288.46",
						"172.40" };
				displayName = new String[] { "Securities Purchase", "Securities Purchase", "Securities Purchase",
						"Securities Purchase", "Securities Purchase", "Securities Purchase", "Securities Purchase",
						"Internal Transfer", "Securities Sale", "Securities Sale", "Securities Purchase",
						"Securities Purchase", "Securities Purchase", "Internal Transfer Out", "Securities Purchase" };
				accId = new String[] { "11992", "11992", "11992", "11992", "11992", "11992", "11992", "11992", "11992",
						"11992", "11992", "11992", "11992", "", "11992" };
				quantity = new String[] { "9", "6", "12", "9", "8", "10", "6", "", "2", "3", "8", "7", "2", "", "5" };
				amount = new String[] { "-283.78", "-300.30", "-732.73", "-243.24", "-1473.47", "-1057.06", "-252.25",
						"+8000.00", "+291.63", "+5106.60", "-1121.12", "-781.91", "-5202.08", "+26796.47", "-8116.06" };
				currencyId = new String[] { "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD",
						"USD", "USD", "", "USD" };
				shortName = new String[] { "Pfizer Inc", "General Motors Company", "Citigroup Inc",
						"Bank of America Corp", "Boeing Co", "American Express Company", "Coca-Cola Co", "",
						"Walmart Inc", "Google LLC", "Walmart Inc", "Apple Computer Inc Com Stk (Us)", "Amazon.com", "",
						"Google LLC" };
				generatedBookingDate = PortfolioServiceUtils.mockAccountActivityBookingDate(displayName.length);
				bookingDate = PortfolioServiceUtils.reverseArray(generatedBookingDate);
				generatedValueDate = PortfolioServiceUtils.mockAccountActivityBookingDate(displayName.length);
				valueDate = PortfolioServiceUtils.reverseArray(generatedValueDate);
			}
			if (accountId.equalsIgnoreCase("11982")) {
				balance = new String[] { "133.35", "112.79" };
				displayName = new String[] { "Internal Transfer", "Charge Capitalise" };
				accId = new String[] { "11982", "11982" };
				quantity = new String[] { "", "" };
				amount = new String[] { "100", "-20.56" };
				currencyId = new String[] { "EUR", "EUR" };
				shortName = new String[] { "", "" };
				generatedBookingDate = PortfolioServiceUtils.mockAccountActivityBookingDate(displayName.length);
				bookingDate = PortfolioServiceUtils.reverseArray(generatedBookingDate);
				generatedValueDate = PortfolioServiceUtils.mockAccountActivityBookingDate(displayName.length);
				valueDate = PortfolioServiceUtils.reverseArray(generatedValueDate);
			}
		}
		for (int i = 0; i < displayName.length; i++) {
			JSONObject actListObj = new JSONObject();
			actListObj.put(TemenosConstants.ACCID, accId[i]);
			actListObj.put(TemenosConstants.DISPLAYNAME, displayName[i]);
			actListObj.put(TemenosConstants.SHORTNAME, shortName[i]);
			actListObj.put(TemenosConstants.QUANTITY, quantity[i]);
			actListObj.put(TemenosConstants.CURRENCYID, currencyId[i]);
			actListObj.put(TemenosConstants.AMOUNT, amount[i]);
			actListObj.put(TemenosConstants.BALANCE, balance[i]);
			actListObj.put(TemenosConstants.BOOKINGDATE, bookingDate[i]);
			actListObj.put(TemenosConstants.VALUEDATE, valueDate[i]);
			accListArr.put(actListObj);
		}
		JSONArray sortedJSON = new JSONArray();
		if (dateFrom.equals("") || dateTo.equals("")) {
			sortedJSON = accListArr;
			diagnostic.prepareDebug("==========> GetAccountActivityMockPostProcessor Mock - No date filter ").log();
		} else {

			sortedJSON = PortfolioServiceUtils.filterAccountActivityDate(accListArr, dateFrom, dateTo);
			diagnostic.prepareDebug("==========> GetAccountActivityMockPostProcessor T24 - No. of records returned after filter: " + sortedJSON.length() ).log();
		}

		status.put(TemenosConstants.STATUS, "success");
		responseVal.put("body", sortedJSON);
		responseVal.put("header", status);
		accountActivity.put("accountActivityList", responseVal);
		Result res = new Result();
		res = Utilities.constructResultFromJSONObject(accountActivity);
		res.addOpstatusParam("0");
		res.addHttpStatusCodeParam("200");
		res.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		diagnostic.prepareDebug("==========> GetAccountActivityMockPostProcessor Mock - Exited").log();
		return res;
	}

}
