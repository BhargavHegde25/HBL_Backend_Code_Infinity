/**
 * 
 */
package com.temenos.infinity.wealth.mock.processor.post;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
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
import com.temenos.infinity.wealth.common.util.PortfolioServiceUtils;

/**
 * @author himaja.sridhar
 *
 */
public class GetPerformanceMockPostProcessor implements DataPostProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {
			diagnostic.prepareDebug("==========> GetPerformanceMockPostProcessor Mock - Entered ").log();
		String portfolioId = (String) request.getParameter(TemenosConstants.PORTFOLIOID);
		String dateFrom = (String) request.getParameter(TemenosConstants.DATEFROM);
		String dateTo = (String) request.getParameter(TemenosConstants.DATETO);
		String duration = (String) request.getParameter(TemenosConstants.DURATION);
		String benchMarkIndex = (String) request.getParameter(TemenosConstants.BENCHMARK);
		String sortBy = (String) request.getParameter(TemenosConstants.SORTBY);
		String sortType = (String) request.getParameter(TemenosConstants.SORTORDER);
		String limitVal = (String) request.getParameter(TemenosConstants.PAGESIZE);
		String offsetVal = (String) request.getParameter(TemenosConstants.PAGEOFFSET);
		int totalCount = 0;
		int limit = (limitVal != null && limitVal.trim().length() > 0) ? Integer.parseInt(limitVal) : 0;
		int offset = (offsetVal != null && offsetVal.trim().length() > 0) ? Integer.parseInt(offsetVal) : 0;
		JSONObject responseVal = new JSONObject();
		JSONObject performanceObj = new JSONObject();
		JSONArray performanceArray = new JSONArray();
		JSONArray filteredJson = new JSONArray();
		new JSONArray();
		String[] portfolioReturn = null, percentageChange = null, benchMark = null, dateTime = null;
		JSONArray benchMarkArray = new JSONArray();
		new JSONObject();
		JSONObject jsonobj = new JSONObject();
		JSONArray sortedJSON = new JSONArray();
		benchMarkArray = getBenchMarkArray();
		String initialValue = "";
		if ((benchMarkIndex.trim()).equals("")) {
			benchMarkIndex = benchMarkArray.getJSONObject(0).getString("benchMark").toString();
			benchMark =getBenchMarkReturn(benchMarkIndex);
		} else {
			benchMark = getBenchMarkReturn(benchMarkIndex);
		}
		diagnostic.prepareDebug("==========> GetPerformanceMockPostProcessor Mock - benchmark fetched ").log();
		if (portfolioId.equalsIgnoreCase("100777-1")) {
			portfolioReturn = new String[] { "43000.50", "44100.45", "47100.50", "45150.45", "42519.80", "44000.60",
					"44040.50", "46100.35", "43550.45", "43550.00", "44670.45", "46245.75", "48010.00", "46720.20",
					"46230.30", "43550.55", "43690.80", "41450.45", "43010.35" };
			percentageChange = new String[] { "4.33", "2.56", "9.53", "5.00", "-1.12", "2.33", "2.42", "7.21", "1.28",
					"1.28", "3.88", "7.55", "11.65", "8.65", "7.51", "1.28", "1.61", "-3.60", "0.02" };
			dateTime = PortfolioServiceUtils.mockPerformanceDateTime(portfolioReturn.length);
			diagnostic.prepareDebug("==========> GetPerformanceMockPostProcessor Mock - dateTime Fetched ").log();
			for (int i = 0; i < portfolioReturn.length; i++) {
				JSONObject monthlyOverview = new JSONObject();
				monthlyOverview.put(TemenosConstants.PORTFOLIORETURN, portfolioReturn[i]);
				monthlyOverview.put(TemenosConstants.PERCENTAGECHANGE, percentageChange[i]);
				monthlyOverview.put(TemenosConstants.BENCHMARK, benchMark[i]);
				monthlyOverview.put(TemenosConstants.DATE_TIME, dateTime[i]);
				performanceArray.put(monthlyOverview);
			}
			if (duration.equalsIgnoreCase("Inception")) {
				filteredJson = performanceArray;
				diagnostic.prepareDebug("==========> GetPerformanceMockPostProcessor Mock -  No. of records returned(Inception): "+filteredJson.length()).log();
			} else if (duration.equalsIgnoreCase("Free")) {
				filteredJson = PortfolioServiceUtils.filterPerformanceCustomDate(performanceArray, dateFrom,
						dateTo);
				diagnostic.prepareDebug("==========> GetPerformanceMockPostProcessor Mock -  No. of records returned(Free): "+filteredJson.length()).log();
			} else {
				filteredJson = PortfolioServiceUtils.filterPerformanceDate(performanceArray, dateFrom, dateTo);
				diagnostic.prepareDebug("==========> GetPerformanceMockPostProcessor Mock -  No. of records returned after date filter: "+filteredJson.length()).log();
			}
			if (sortBy != null) {
				sortedJSON = PortfolioServiceUtils.sortPerformanceArray(filteredJson, sortBy, sortType);
				diagnostic.prepareDebug("==========> GetPerformanceMockPostProcessor Mock -  No. of records returned after sort: "+sortedJSON.length()).log();
			} else {
			}
			totalCount = sortedJSON.length();
			if (limit > 0 && offset >= 0) {
				sortedJSON = PortfolioWealthUtils.pagination(sortedJSON, limit, offset);
				diagnostic.prepareDebug("==========> GetPerformanceMockPostProcessor Mock -  No. of records returned after Pagination: "+sortedJSON.length()).log();
			}
			if (duration.equalsIgnoreCase("OneY")) {
				performanceObj.put(TemenosConstants.INITIAL_VALUE, "44040.50");
				performanceObj.put(TemenosConstants.NET_DEPOSIT, "15000.00");
				performanceObj.put(TemenosConstants.PL, "510.15");
				performanceObj.put(TemenosConstants.FEES_TAX, "-74.45");
				performanceObj.put(TemenosConstants.TIME_WEIGHTED, "6");
				performanceObj.put(TemenosConstants.MONEY_WEIGHTED, "4");
			} else if (duration.equalsIgnoreCase("YTD")) {
				performanceObj.put(TemenosConstants.INITIAL_VALUE, "43010.35");
				performanceObj.put(TemenosConstants.NET_DEPOSIT, "10000.00");
				performanceObj.put(TemenosConstants.PL, "200.00");
				performanceObj.put(TemenosConstants.FEES_TAX, "-25.00");
				performanceObj.put(TemenosConstants.TIME_WEIGHTED, "3");
				performanceObj.put(TemenosConstants.MONEY_WEIGHTED, "2");
			} else if (duration.equalsIgnoreCase("Inception")) {
				performanceObj.put(TemenosConstants.INITIAL_VALUE, "43000.50");
				performanceObj.put(TemenosConstants.NET_DEPOSIT, "5000.00");
				performanceObj.put(TemenosConstants.PL, "850.85");
				performanceObj.put(TemenosConstants.FEES_TAX, "-124.40");
				performanceObj.put(TemenosConstants.TIME_WEIGHTED, "8");
				performanceObj.put(TemenosConstants.MONEY_WEIGHTED, "5");
			} else if (duration.equalsIgnoreCase("Free")) {
				String startDate = PortfolioServiceUtils.getPerformancePreviousMonth(dateFrom);
				diagnostic.prepareDebug("==========> GetPerformanceMockPostProcessor Mock -  dateTime Fetched").log();
				int i = 0;
				for (i = 0; i < performanceArray.length(); i++) {
					jsonobj = performanceArray.getJSONObject(i);
					String startdateVal = jsonobj.getString("dateTime");
					if (startdateVal.equals(startDate)) {
						initialValue = jsonobj.getString("portfolioReturn");
					}
				}

				if (initialValue.equals("")) {
					jsonobj = performanceArray.getJSONObject(0);
					initialValue = jsonobj.getString("portfolioReturn");
				}
				performanceObj.put(TemenosConstants.INITIAL_VALUE, initialValue);
				performanceObj.put(TemenosConstants.NET_DEPOSIT, "12250.00");
				performanceObj.put(TemenosConstants.PL, "670.50");
				performanceObj.put(TemenosConstants.FEES_TAX, "-88.50");
				performanceObj.put(TemenosConstants.TIME_WEIGHTED, "5");
				performanceObj.put(TemenosConstants.MONEY_WEIGHTED, "4");
			} else {
				performanceObj.put(TemenosConstants.INITIAL_VALUE, "44040.50");
				performanceObj.put(TemenosConstants.NET_DEPOSIT, "15000.00");
				performanceObj.put(TemenosConstants.PL, "510.15");
				performanceObj.put(TemenosConstants.FEES_TAX, "-74.45");
				performanceObj.put(TemenosConstants.TIME_WEIGHTED, "6");
				performanceObj.put(TemenosConstants.MONEY_WEIGHTED, "4");
			}
			String currentVal = String
					.valueOf(PortfolioServiceUtils.calculatePerformanceCurrentValue(performanceObj));
				performanceObj.put(TemenosConstants.CURRENT_VAL, currentVal);
				diagnostic.prepareDebug("==========> GetPerformanceMockPostProcessor Mock -  Current value").log();
		} else if (portfolioId.equalsIgnoreCase("100777-2")) {
			portfolioReturn = new String[] { "12010.45", "12510.70", "13010.20", "12750.45", "11910.50", "12365.80",
					"12150.50", "12999.45", "12109.45", "11860.50", "12222.45", "12999.90", "13010.10", "12860.90",
					"12310.40", "11888.90", "11980.80", "11670.70", "11799.80" };
			percentageChange = new String[] { "1.20", "4.17", "8.32", "6.16", "-0.83", "2.96", "1.17", "8.23", "0.82",
					"-1.25", "1.77", "8.24", "8.32", "7.08", "2.50", "-1.01", "-0.25", "-2.83", "-1.75" };
			dateTime = PortfolioServiceUtils.mockPerformanceDateTime(portfolioReturn.length);
			for (int i = 0; i < portfolioReturn.length; i++) {
				JSONObject monthlyOverview = new JSONObject();
				monthlyOverview.put(TemenosConstants.PORTFOLIORETURN, portfolioReturn[i]);
				monthlyOverview.put(TemenosConstants.PERCENTAGECHANGE, percentageChange[i]);
				monthlyOverview.put(TemenosConstants.BENCHMARK, benchMark[i]);
				monthlyOverview.put(TemenosConstants.DATE_TIME, dateTime[i]);
				performanceArray.put(monthlyOverview);
			}

			if (duration.equalsIgnoreCase("Inception")) {
				filteredJson = performanceArray;
				diagnostic.prepareDebug("==========> GetPerformanceMockPostProcessor Mock -  No. of records returned(Inception): "+filteredJson.length()).log();
			} else if (duration.equalsIgnoreCase("Free")) {
				filteredJson = PortfolioServiceUtils.filterPerformanceCustomDate(performanceArray, dateFrom,
						dateTo);
				diagnostic.prepareDebug("==========> GetPerformanceMockPostProcessor Mock -  No. of records returned(Free): "+filteredJson.length()).log();
			} else {
				filteredJson = PortfolioServiceUtils.filterPerformanceDate(performanceArray, dateFrom, dateTo);
				diagnostic.prepareDebug("==========> GetPerformanceMockPostProcessor Mock -  No. of records returned after date filter: "+filteredJson.length()).log();
			}
			if (sortBy != null) {
				sortedJSON = PortfolioServiceUtils.sortPerformanceArray(filteredJson, sortBy, sortType);
				diagnostic.prepareDebug("==========> GetPerformanceMockPostProcessor Mock -  No. of records returned after sort: "+sortedJSON.length()).log();
			} else {
			}
			totalCount = sortedJSON.length();
			if (limit > 0 && offset >= 0) {
				sortedJSON = PortfolioWealthUtils.pagination(sortedJSON, limit, offset);
				diagnostic.prepareDebug("==========> GetPerformanceMockPostProcessor Mock -  No. of records returned after pagination: "+sortedJSON.length()).log();
			}
			if (duration.equalsIgnoreCase("OneY")) {
				performanceObj.put(TemenosConstants.INITIAL_VALUE, "12150.50");
				performanceObj.put(TemenosConstants.NET_DEPOSIT, "15000.00");
				performanceObj.put(TemenosConstants.PL, "450.45");
				performanceObj.put(TemenosConstants.FEES_TAX, "-65.10");
				performanceObj.put(TemenosConstants.TIME_WEIGHTED, "6");
				performanceObj.put(TemenosConstants.MONEY_WEIGHTED, "4");

			} else if (duration.equalsIgnoreCase("YTD")) {
				performanceObj.put(TemenosConstants.INITIAL_VALUE, "11799.80");
				performanceObj.put(TemenosConstants.NET_DEPOSIT, "5000.00");
				performanceObj.put(TemenosConstants.PL, "150.00");
				performanceObj.put(TemenosConstants.FEES_TAX, "-10.00");
				performanceObj.put(TemenosConstants.TIME_WEIGHTED, "2");
				performanceObj.put(TemenosConstants.MONEY_WEIGHTED, "2");
			} else if (duration.equalsIgnoreCase("Inception")) {
				performanceObj.put(TemenosConstants.INITIAL_VALUE, "12010.45");
				performanceObj.put(TemenosConstants.NET_DEPOSIT, "5000.00");
				performanceObj.put(TemenosConstants.PL, "813.35");
				performanceObj.put(TemenosConstants.FEES_TAX, "-94.24");
				performanceObj.put(TemenosConstants.TIME_WEIGHTED, "8");
				performanceObj.put(TemenosConstants.MONEY_WEIGHTED, "5");
			} else if (duration.equalsIgnoreCase("Free")) {
				String startDate = PortfolioServiceUtils.getPerformancePreviousMonth(dateFrom);
				diagnostic.prepareDebug("==========> GetPerformanceMockPostProcessor Mock -  dateTime Fetched").log();
				int i = 0;
				for (i = 0; i < performanceArray.length(); i++) {
					jsonobj = performanceArray.getJSONObject(i);
					String startdateVal = jsonobj.getString("dateTime");
					if (startdateVal.equals(startDate)) {
						initialValue = jsonobj.getString("portfolioReturn");
					}
				}

				if (initialValue.equals("")) {
					jsonobj = performanceArray.getJSONObject(0);
					initialValue = jsonobj.getString("portfolioReturn");
				}
				performanceObj.put(TemenosConstants.INITIAL_VALUE, initialValue);
				performanceObj.put(TemenosConstants.NET_DEPOSIT, "4500.00");
				performanceObj.put(TemenosConstants.PL, "135.50");
				performanceObj.put(TemenosConstants.FEES_TAX, "-13.50");
				performanceObj.put(TemenosConstants.TIME_WEIGHTED, "3");
				performanceObj.put(TemenosConstants.MONEY_WEIGHTED, "2");

			} else {
				performanceObj.put(TemenosConstants.INITIAL_VALUE, "12150.50");
				performanceObj.put(TemenosConstants.NET_DEPOSIT, "15000.00");
				performanceObj.put(TemenosConstants.PL, "450.45");
				performanceObj.put(TemenosConstants.FEES_TAX, "-65.10");
				performanceObj.put(TemenosConstants.TIME_WEIGHTED, "6");
				performanceObj.put(TemenosConstants.MONEY_WEIGHTED, "4");

			}
		String currentVal = String
			.valueOf(PortfolioServiceUtils.calculatePerformanceCurrentValue(performanceObj));
		performanceObj.put(TemenosConstants.CURRENT_VAL, currentVal);
		diagnostic.prepareDebug("==========> GetPerformanceMockPostProcessor Mock - Current value").log();
		} else if (portfolioId.equalsIgnoreCase("100777-3")) {
			portfolioReturn = new String[] { "28023.24", "29045.45", "30444.34", "28222.45", "27600.34", "29987.39",
					"30087.12", "28500.12", "27700.12", "27655.67", "28429.90", "29078.12", "30098.34", "28098.67",
					"32098.56", "33098.12", "29907.40", "30098.00", "31098.34", "34215.94" };
			percentageChange = new String[] { "4.00", "3.65", "8.64", "0.71", "-1.51", "7.01", "7.36", "1.70", "-1.15",
					"-1.31", "1.45", "3.76", "7.40", "0.27", "14.54", "18.11", "6.72", "7.40", "10.97", "22.10" };
			dateTime = PortfolioServiceUtils.mockPerformanceDateTime(portfolioReturn.length);
			for (int i = 0; i < portfolioReturn.length; i++) {
				JSONObject monthlyOverview = new JSONObject();
				monthlyOverview.put(TemenosConstants.PORTFOLIORETURN, portfolioReturn[i]);
				monthlyOverview.put(TemenosConstants.PERCENTAGECHANGE, percentageChange[i]);
				monthlyOverview.put(TemenosConstants.BENCHMARK, benchMark[i]);
				monthlyOverview.put(TemenosConstants.DATE_TIME, dateTime[i]);
				performanceArray.put(monthlyOverview);
			}

			if (duration.equalsIgnoreCase("Inception")) {
				filteredJson = performanceArray;
				diagnostic.prepareDebug("==========> GetPerformanceMockPostProcessor Mock -  No. of records returned(Inception): "+filteredJson.length()).log();
			} else if (duration.equalsIgnoreCase("Free")) {
				filteredJson = PortfolioServiceUtils.filterPerformanceCustomDate(performanceArray, dateFrom,
						dateTo);
				diagnostic.prepareDebug("==========> GetPerformanceMockPostProcessor Mock -  No. of records returned(Free): "+filteredJson.length()).log();
			} else {
				filteredJson = PortfolioServiceUtils.filterPerformanceDate(performanceArray, dateFrom, dateTo);
				diagnostic.prepareDebug("==========> GetPerformanceMockPostProcessor Mock -  No. of records returned after date filter: "+filteredJson.length()).log();
			}
			if (sortBy != null) {
				sortedJSON = PortfolioServiceUtils.sortPerformanceArray(filteredJson, sortBy, sortType);
				diagnostic.prepareDebug("==========> GetPerformanceMockPostProcessor Mock -  No. of records returned afer sort: "+sortedJSON.length()).log();
			} else {
			}
			totalCount = sortedJSON.length();
			if (limit > 0 && offset >= 0) {
				sortedJSON = PortfolioWealthUtils.pagination(sortedJSON, limit, offset);
				diagnostic.prepareDebug("==========> GetPerformanceMockPostProcessor Mock -  No. of records returned after pagination: "+sortedJSON.length()).log();
			}
			if (duration.equalsIgnoreCase("OneY")) {
				performanceObj.put(TemenosConstants.INITIAL_VALUE, "30087.12");
				performanceObj.put(TemenosConstants.NET_DEPOSIT, "15000.00");
				performanceObj.put(TemenosConstants.PL, "250.60");
				performanceObj.put(TemenosConstants.FEES_TAX, "-50.75");
				performanceObj.put(TemenosConstants.TIME_WEIGHTED, "5.5");
				performanceObj.put(TemenosConstants.MONEY_WEIGHTED, "3.5");

			} else if (duration.equalsIgnoreCase("YTD")) {
				performanceObj.put(TemenosConstants.INITIAL_VALUE, "31098.34");
				performanceObj.put(TemenosConstants.NET_DEPOSIT, "9650.20");
				performanceObj.put(TemenosConstants.PL, "120.50");
				performanceObj.put(TemenosConstants.FEES_TAX, "-20.70");
				performanceObj.put(TemenosConstants.TIME_WEIGHTED, "2.9");
				performanceObj.put(TemenosConstants.MONEY_WEIGHTED, "1.9");
			} else if (duration.equalsIgnoreCase("Inception")) {
				performanceObj.put(TemenosConstants.INITIAL_VALUE, "28023.24");
				performanceObj.put(TemenosConstants.NET_DEPOSIT, "1267.90");
				performanceObj.put(TemenosConstants.PL, "469.55");
				performanceObj.put(TemenosConstants.FEES_TAX, "-76.10");
				performanceObj.put(TemenosConstants.TIME_WEIGHTED, "7.0");
				performanceObj.put(TemenosConstants.MONEY_WEIGHTED, "4.5");
			} else if (duration.equalsIgnoreCase("Free")) {
				String startDate = PortfolioServiceUtils.getPerformancePreviousMonth(dateFrom);
				diagnostic.prepareDebug("==========> GetPerformanceMockPostProcessor Mock -  dateTime Fetched").log();
				int i = 0;
				for (i = 0; i < performanceArray.length(); i++) {
					jsonobj = performanceArray.getJSONObject(i);
					String startdateVal = jsonobj.getString("dateTime");
					if (startdateVal.equals(startDate)) {
						initialValue = jsonobj.getString("portfolioReturn");
					}
				}

				if (initialValue.equals("")) {
					jsonobj = performanceArray.getJSONObject(0);
					initialValue = jsonobj.getString("portfolioReturn");
				}
				performanceObj.put(TemenosConstants.INITIAL_VALUE, initialValue);
				performanceObj.put(TemenosConstants.NET_DEPOSIT, "11560.00");
				performanceObj.put(TemenosConstants.PL, "330.50");
				performanceObj.put(TemenosConstants.FEES_TAX, "-45.60");
				performanceObj.put(TemenosConstants.TIME_WEIGHTED, "4.5");
				performanceObj.put(TemenosConstants.MONEY_WEIGHTED, "3.5");

			} else {
				performanceObj.put(TemenosConstants.INITIAL_VALUE, "30087.12");
				performanceObj.put(TemenosConstants.NET_DEPOSIT, "15000.00");
				performanceObj.put(TemenosConstants.PL, "250.60");
				performanceObj.put(TemenosConstants.FEES_TAX, "-50.75");
				performanceObj.put(TemenosConstants.TIME_WEIGHTED, "5.5");
				performanceObj.put(TemenosConstants.MONEY_WEIGHTED, "3.5");

			}
			String currentVal = String
					.valueOf(PortfolioServiceUtils.calculatePerformanceCurrentValue(performanceObj));
				performanceObj.put(TemenosConstants.CURRENT_VAL, currentVal);
				diagnostic.prepareDebug("==========> GetPerformanceMockPostProcessor Mock -  current value").log();
		} else if (portfolioId.equalsIgnoreCase("100777-4")) {
			
			if(benchMarkIndex.equalsIgnoreCase("S&P500")) {
			portfolioReturn = new String[] { "46908.0", "47017.5", "48170.5", "46289.3", "46789.0", "286670.85",
					"309780.47", "315137.81", "311554.93", "307526.34", "312051.08", "323101.14", "348921.31", "349865.95",
					"409391.47", "500539.76", "536599.23", "579480.81" };
			} else if(benchMarkIndex.equalsIgnoreCase("NASDAQ")){
				portfolioReturn = new String[] { "46908.0", "47017.5", "48170.5", "46289.3", "46789.0", "286319.98",
						"309067.34", "314412.35", "310837.71", "306818.39", "311332.72", "323496.17", "349347.92", "350293.71",
						"409892.01", "500539.76", "536599.23", "579480.81" };
			} else if(benchMarkIndex.equalsIgnoreCase("DOWJ")){
				portfolioReturn = new String[] { "46908.0", "47017.5", "48170.5", "46289.3", "46789.0", "287290.44",
						"309113.88", "333672.15", "339615.42", "335754.25", "331151.25", "336023.59", "349006.63", "376897.01",
						"377917.39", "442215.53", "540539.7", "579480.81" };
			} else {
				portfolioReturn = new String[] { "46908.0", "47017.5", "48170.5", "46289.3", "46789.0", "286670.85",
						"309780.47", "315137.81", "311554.93", "307526.34", "312051.08", "323101.14", "348921.31", "349865.95",
						"409391.47", "500539.76", "536599.23", "579480.81" };
			}
			
			if(benchMarkIndex.equalsIgnoreCase("S&P500")) {
				percentageChange = new String[] { "4.00", "3.65", "8.64", "0.71", "-1.51", "7.01", "7.46", "1.70", "-1.15",
						"-1.31", "1.45", "3.42", "7.40", "0.27", "14.54", "18.21", "6.72", "7.40", "10.97", "22.10" };
				} else if(benchMarkIndex.equalsIgnoreCase("NASDAQ")){
					percentageChange = new String[] { "4.00", "3.65", "8.64", "0.71", "-1.51", "7.01", "7.36", "1.70", "-1.15",
							"-1.31", "1.45", "3.76", "7.40", "0.27", "14.54", "18.11", "6.72", "7.40", "10.97", "22.10" };
				} else if(benchMarkIndex.equalsIgnoreCase("DOWJ")){
					percentageChange = new String[] { "4.00", "3.65", "8.64", "0.71", "-1.51", "7.06", "7.36", "1.75", "-1.15",
							"-1.39", "1.45", "3.72", "7.40", "0.27", "14.54", "18.19", "6.72", "7.40", "10.97", "22.10" };
				} else {
					percentageChange = new String[] { "4.00", "3.65", "8.64", "0.71", "-1.51", "7.01", "7.46", "1.70", "-1.15",
							"-1.31", "1.45", "3.42", "7.40", "0.27", "14.54", "18.21", "6.72", "7.40", "10.97", "22.10" };
				}
			
			dateTime = PortfolioServiceUtils.mockPerformanceDateTime(portfolioReturn.length);
			diagnostic.prepareDebug("==========> GetPerformanceMockPostProcessor Mock -  dateTime fetched").log();
			for (int i = 0; i < portfolioReturn.length; i++) {
				JSONObject monthlyOverview = new JSONObject();
				monthlyOverview.put(TemenosConstants.PORTFOLIORETURN, portfolioReturn[i]);
				monthlyOverview.put(TemenosConstants.PERCENTAGECHANGE, percentageChange[i]);
				monthlyOverview.put(TemenosConstants.BENCHMARK, benchMark[i]);
				monthlyOverview.put(TemenosConstants.DATE_TIME, dateTime[i]);
				performanceArray.put(monthlyOverview);
			}

			if (duration.equalsIgnoreCase("Inception")) {
				filteredJson = performanceArray;
				diagnostic.prepareDebug("==========> GetPerformanceMockPostProcessor Mock -  No. of records returned(Inception): "+filteredJson.length()).log();
			} else if (duration.equalsIgnoreCase("Free")) {
				filteredJson = PortfolioServiceUtils.filterPerformanceCustomDate(performanceArray, dateFrom,
						dateTo);
				diagnostic.prepareDebug("==========> GetPerformanceMockPostProcessor Mock -  No. of records returned(Free): "+filteredJson.length()).log();
			} else {
				filteredJson = PortfolioServiceUtils.filterPerformanceDate(performanceArray, dateFrom, dateTo);
				diagnostic.prepareDebug("==========> GetPerformanceMockPostProcessor Mock -  No. of records returned after date filter: "+filteredJson.length()).log();
			}
			if (sortBy != null) {
				sortedJSON = PortfolioServiceUtils.sortPerformanceArray(filteredJson, sortBy, sortType);
				diagnostic.prepareDebug("==========> GetPerformanceMockPostProcessor Mock -  No. of records returned after sort: "+filteredJson.length()).log();
			} else {
			}
			totalCount = sortedJSON.length();
			if (limit > 0 && offset >= 0) {
				sortedJSON = PortfolioWealthUtils.pagination(sortedJSON, limit, offset);
				diagnostic.prepareDebug("==========> GetPerformanceMockPostProcessor Mock -  No. of records returned after pagination: "+sortedJSON.length()).log();
			}
			if (duration.equalsIgnoreCase("OneY")) {
				performanceObj.put(TemenosConstants.CURRENT_VAL, "579481.00");
				performanceObj.put(TemenosConstants.INITIAL_VALUE, "286319.98");
				performanceObj.put(TemenosConstants.NET_DEPOSIT, "250000.00");
				performanceObj.put(TemenosConstants.PL, "293162.00");
				performanceObj.put(TemenosConstants.FEES_TAX, "-9600.00");
				performanceObj.put(TemenosConstants.TIME_WEIGHTED, "9.6");
				performanceObj.put(TemenosConstants.MONEY_WEIGHTED, "7.4");

			} else if (duration.equalsIgnoreCase("YTD")) {
				performanceObj.put(TemenosConstants.CURRENT_VAL, "579481.00");
				performanceObj.put(TemenosConstants.INITIAL_VALUE, "254529.98");
				performanceObj.put(TemenosConstants.NET_DEPOSIT, "221450.00");
				performanceObj.put(TemenosConstants.PL, "243521.00");
				performanceObj.put(TemenosConstants.FEES_TAX, "-7850.00");
				performanceObj.put(TemenosConstants.TIME_WEIGHTED, "8.52");
				performanceObj.put(TemenosConstants.MONEY_WEIGHTED, "6.95");
			} else if (duration.equalsIgnoreCase("Inception")) {
				performanceObj.put(TemenosConstants.CURRENT_VAL, "579481.00");
				performanceObj.put(TemenosConstants.INITIAL_VALUE, "261375.98");
				performanceObj.put(TemenosConstants.NET_DEPOSIT, "230000.00");
				performanceObj.put(TemenosConstants.PL, "273140.00");
				performanceObj.put(TemenosConstants.FEES_TAX, "-8120.00");
				performanceObj.put(TemenosConstants.TIME_WEIGHTED, "8.6");
				performanceObj.put(TemenosConstants.MONEY_WEIGHTED, "5.4");
			} else if (duration.equalsIgnoreCase("Free")) {
				String startDate = PortfolioServiceUtils.getPerformancePreviousMonth(dateFrom);
				diagnostic.prepareDebug("==========> GetPerformanceMockPostProcessor Mock -  dateTime fetched").log();
				int i = 0;
				for (i = 0; i < performanceArray.length(); i++) {
					jsonobj = performanceArray.getJSONObject(i);
					String startdateVal = jsonobj.getString("dateTime");
					if (startdateVal.equals(startDate)) {
						initialValue = jsonobj.getString("portfolioReturn");
					}
				}

				if (initialValue.equals("")) {
					jsonobj = performanceArray.getJSONObject(0);
					initialValue = jsonobj.getString("portfolioReturn");
				}
				performanceObj.put(TemenosConstants.CURRENT_VAL, "50879.00");
				performanceObj.put(TemenosConstants.INITIAL_VALUE, initialValue);
				performanceObj.put(TemenosConstants.NET_DEPOSIT, "2900.00");
				performanceObj.put(TemenosConstants.PL, "1250.00");
				performanceObj.put(TemenosConstants.FEES_TAX, "-60.00");
				performanceObj.put(TemenosConstants.TIME_WEIGHTED, "4.5");
				performanceObj.put(TemenosConstants.MONEY_WEIGHTED, "3.5");

			} else {
				performanceObj.put(TemenosConstants.CURRENT_VAL, "48762.00");
				performanceObj.put(TemenosConstants.INITIAL_VALUE, "44672");
				performanceObj.put(TemenosConstants.NET_DEPOSIT, "2900.00");
				performanceObj.put(TemenosConstants.PL, "1250.00");
				performanceObj.put(TemenosConstants.FEES_TAX, "-60.00");
				performanceObj.put(TemenosConstants.TIME_WEIGHTED, "5.5");
				performanceObj.put(TemenosConstants.MONEY_WEIGHTED, "3.5");

			}
		} else if (portfolioId.equalsIgnoreCase("100777-5")) {
			if(benchMarkIndex.equalsIgnoreCase("S&P500")) {
				portfolioReturn = new String[] { "16546.12", "17590.22", "16103.82", "17898.33", "17030.22", "18111.88",
						"16987.68", "104797.43", "103565", "102225.84", "103729.93", "107726.59", "116398.26", "116713.38",
						"136570.77", "167181.75", "179225.72", "193548.29", "217152.8", "278758.41" };
				} else if(benchMarkIndex.equalsIgnoreCase("NASDAQ")){
				portfolioReturn = new String[] { "16546.12", "17590.22", "16103.82", "17898.33", "17030.22", "18111.88",
					"16987.68", "104896.83", "103704.23", "102363.27", "103869.37", "107927.45", "116552.32", "116867.86",
					"136751.53", "166994.18", "179024.63", "193331.14", "217152.8", "278758.41" };
				} else if(benchMarkIndex.equalsIgnoreCase("DOWJ")){
					portfolioReturn = new String[] { "16546.12", "17590.22", "16103.82", "17898.33", "17030.22", "18111.88",
							"16987.68", "104683.14", "103492.97", "102154.75", "103699.87", "107751.32", "116362.12", "116794.26",
							"136505.68", "166693.96", "178817.8", "193107.78", "216901.92", "278758.41" };
				} else {
					portfolioReturn = new String[] { "16546.12", "17590.22", "16103.82", "17898.33", "17030.22", "18111.88",
							"16987.68", "104797.43", "103565", "102225.84", "103729.93", "107726.59", "116398.26", "116713.38",
							"136570.77", "167181.75", "179225.72", "193548.29", "217152.8", "278758.41" };
				}
			
			if(benchMarkIndex.equalsIgnoreCase("S&P500")) {
				percentageChange = new String[] { "4.00", "3.65", "8.64", "0.71", "-1.51", "7.01", "7.36", "1.70", "-1.19",
						"-1.31", "1.45", "3.71", "7.45", "0.27", "14.54", "18.31", "6.72", "7.40", "10.87", "22.10" };
				} else if(benchMarkIndex.equalsIgnoreCase("NASDAQ")){
					percentageChange = new String[] { "4.00", "3.65", "8.64", "0.71", "-1.51", "7.01", "7.36", "1.70", "-1.15",
							"-1.31", "1.45", "3.76", "7.40", "0.27", "14.54", "18.11", "6.72", "7.40", "10.97", "22.10" };
				} else if(benchMarkIndex.equalsIgnoreCase("DOWJ")){
					percentageChange = new String[] { "4.00", "3.65", "8.64", "0.71", "-1.51", "7.01", "7.36", "1.70", "-1.15",
							"-1.31", "1.49", "3.76", "7.40", "0.37", "14.44", "18.11", "6.78", "7.40", "10.97", "22.19" };
				} else {
					percentageChange = new String[] { "4.00", "3.65", "8.64", "0.71", "-1.51", "7.01", "7.36", "1.70", "-1.19",
							"-1.31", "1.45", "3.71", "7.45", "0.27", "14.54", "18.31", "6.72", "7.40", "10.87", "22.10" };
				}
			
			dateTime = PortfolioServiceUtils.mockPerformanceDateTime(portfolioReturn.length);
			diagnostic.prepareDebug("==========> GetPerformanceMockPostProcessor Mock -  dateTime fetched").log();
			for (int i = 0; i < portfolioReturn.length; i++) {
				JSONObject monthlyOverview = new JSONObject();
				monthlyOverview.put(TemenosConstants.PORTFOLIORETURN, portfolioReturn[i]);
				monthlyOverview.put(TemenosConstants.PERCENTAGECHANGE, percentageChange[i]);
				monthlyOverview.put(TemenosConstants.BENCHMARK, benchMark[i]);
				monthlyOverview.put(TemenosConstants.DATE_TIME, dateTime[i]);
				performanceArray.put(monthlyOverview);
			}

			if (duration.equalsIgnoreCase("Inception")) {
				filteredJson = performanceArray;
				diagnostic.prepareDebug("==========> GetPerformanceMockPostProcessor Mock -  No. of records returned(Inception): "+filteredJson.length()).log();
			} else if (duration.equalsIgnoreCase("Free")) {
				filteredJson = PortfolioServiceUtils.filterPerformanceCustomDate(performanceArray, dateFrom,
						dateTo);
				diagnostic.prepareDebug("==========> GetPerformanceMockPostProcessor Mock -  No. of records returned(Free): "+filteredJson.length()).log();
			} else {
				filteredJson = PortfolioServiceUtils.filterPerformanceDate(performanceArray, dateFrom, dateTo);
				diagnostic.prepareDebug("==========> GetPerformanceMockPostProcessor Mock -  No. of records returned after date filter: "+filteredJson.length()).log();
			}
			if (sortBy != null) {
				sortedJSON = PortfolioServiceUtils.sortPerformanceArray(filteredJson, sortBy, sortType);
				diagnostic.prepareDebug("==========> GetPerformanceMockPostProcessor Mock -  No. of records returned after sort: "+sortedJSON.length()).log();
			} else {
			}
			totalCount = sortedJSON.length();
			if (limit > 0 && offset >= 0) {
				sortedJSON = PortfolioWealthUtils.pagination(sortedJSON, limit, offset);
				diagnostic.prepareDebug("==========> GetPerformanceMockPostProcessor Mock -  No. of records returned after pagination: "+sortedJSON.length()).log();
			}
			if (duration.equalsIgnoreCase("OneY")) {
				if(benchMarkIndex.equalsIgnoreCase("S&P500")) {
					performanceObj.put(TemenosConstants.CURRENT_VAL, "278758.41");
					performanceObj.put(TemenosConstants.INITIAL_VALUE, "104797.43");
					performanceObj.put(TemenosConstants.NET_DEPOSIT, "40000.00");
					performanceObj.put(TemenosConstants.PL, "173960.98");
					performanceObj.put(TemenosConstants.FEES_TAX, "-8300.00");
					performanceObj.put(TemenosConstants.TIME_WEIGHTED, "8.4");
					performanceObj.put(TemenosConstants.MONEY_WEIGHTED, "6.5");
				}
				else if(benchMarkIndex.equalsIgnoreCase("NASDAQ")) {
					performanceObj.put(TemenosConstants.CURRENT_VAL, "278758.41");
					performanceObj.put(TemenosConstants.INITIAL_VALUE, "104896.83");
					performanceObj.put(TemenosConstants.NET_DEPOSIT, "40000.00");
					performanceObj.put(TemenosConstants.PL, "173861.58");
					performanceObj.put(TemenosConstants.FEES_TAX, "-8300.00");
					performanceObj.put(TemenosConstants.TIME_WEIGHTED, "8.4");
					performanceObj.put(TemenosConstants.MONEY_WEIGHTED, "6.5");
				}
				else if(benchMarkIndex.equalsIgnoreCase("DOWJ")) {
					performanceObj.put(TemenosConstants.CURRENT_VAL, "278758.41");
					performanceObj.put(TemenosConstants.INITIAL_VALUE, "104683.14");
					performanceObj.put(TemenosConstants.NET_DEPOSIT, "40000.00");
					performanceObj.put(TemenosConstants.PL, "174075.27");
					performanceObj.put(TemenosConstants.FEES_TAX, "-8300.00");
					performanceObj.put(TemenosConstants.TIME_WEIGHTED, "8.4");
					performanceObj.put(TemenosConstants.MONEY_WEIGHTED, "6.5");
				} else {
					performanceObj.put(TemenosConstants.CURRENT_VAL, "278758.41");
					performanceObj.put(TemenosConstants.INITIAL_VALUE, "104797.43");
					performanceObj.put(TemenosConstants.NET_DEPOSIT, "40000.00");
					performanceObj.put(TemenosConstants.PL, "173960.98");
					performanceObj.put(TemenosConstants.FEES_TAX, "-8300.00");
					performanceObj.put(TemenosConstants.TIME_WEIGHTED, "8.4");
					performanceObj.put(TemenosConstants.MONEY_WEIGHTED, "6.5");
				}

			} else if (duration.equalsIgnoreCase("YTD")) {
				performanceObj.put(TemenosConstants.CURRENT_VAL, "278758.41");
				performanceObj.put(TemenosConstants.INITIAL_VALUE, "109797.43");
				performanceObj.put(TemenosConstants.NET_DEPOSIT, "65000.00");
				performanceObj.put(TemenosConstants.PL, "143740.98");
				performanceObj.put(TemenosConstants.FEES_TAX, "-7600.00");
				performanceObj.put(TemenosConstants.TIME_WEIGHTED, "8.4");
				performanceObj.put(TemenosConstants.MONEY_WEIGHTED, "6.5");
			} else if (duration.equalsIgnoreCase("Inception")) {
				performanceObj.put(TemenosConstants.CURRENT_VAL, "278758.41");
				performanceObj.put(TemenosConstants.INITIAL_VALUE, "126797.43");
				performanceObj.put(TemenosConstants.NET_DEPOSIT, "76000.00");
				performanceObj.put(TemenosConstants.PL, "132960.98");
				performanceObj.put(TemenosConstants.FEES_TAX, "-6900.00");
				performanceObj.put(TemenosConstants.TIME_WEIGHTED, "8.4");
				performanceObj.put(TemenosConstants.MONEY_WEIGHTED, "6.5");
			} else if (duration.equalsIgnoreCase("Free")) {
				String startDate = PortfolioServiceUtils.getPerformancePreviousMonth(dateFrom);
				diagnostic.prepareDebug("==========> GetPerformanceMockPostProcessor Mock -  dateTime Fetched").log();
				int i = 0;
				for (i = 0; i < performanceArray.length(); i++) {
					jsonobj = performanceArray.getJSONObject(i);
					String startdateVal = jsonobj.getString("dateTime");
					if (startdateVal.equals(startDate)) {
						initialValue = jsonobj.getString("portfolioReturn");
					}
				}

				if (initialValue.equals("")) {
					jsonobj = performanceArray.getJSONObject(0);
					initialValue = jsonobj.getString("portfolioReturn");
				}
				performanceObj.put(TemenosConstants.CURRENT_VAL, "20937.28");
				performanceObj.put(TemenosConstants.INITIAL_VALUE, initialValue);
				performanceObj.put(TemenosConstants.NET_DEPOSIT, "2500.00");
				performanceObj.put(TemenosConstants.PL, "1788.09");
				performanceObj.put(TemenosConstants.FEES_TAX, "-338.49");
				performanceObj.put(TemenosConstants.TIME_WEIGHTED, "4.5");
				performanceObj.put(TemenosConstants.MONEY_WEIGHTED, "3.5");

			} else {
				performanceObj.put(TemenosConstants.CURRENT_VAL, "18191.27");
				performanceObj.put(TemenosConstants.INITIAL_VALUE, "14241.67");
				performanceObj.put(TemenosConstants.NET_DEPOSIT, "2500.00");
				performanceObj.put(TemenosConstants.PL, "1788.09");
				performanceObj.put(TemenosConstants.FEES_TAX, "-338.49");
				performanceObj.put(TemenosConstants.TIME_WEIGHTED, "5.5");
				performanceObj.put(TemenosConstants.MONEY_WEIGHTED, "3.5");

			}
		}
//		String currentVal = String
//				.valueOf(portfolioPerformanceBackendDelegateImpl.calculatePerformanceCurrentValue(performanceObj));
//		performanceObj.put(TemenosConstants.CURRENT_VAL, currentVal);
		String performanceStr = performanceObj.toString();
		responseVal.put("performanceList", performanceStr);
		responseVal.put("monthlyOverview", filteredJson);
		responseVal.put("sortedMonthlyOverview", sortedJSON);
		responseVal.put("benchMarkList", benchMarkArray);
		responseVal.put("selectedBenchMark", benchMarkIndex);
		responseVal.put(TemenosConstants.REFERENCECURRENCY, "USD");
		responseVal.put(TemenosConstants.SORTBY, sortBy);
		responseVal.put(TemenosConstants.SORTORDER, sortType);
		responseVal.put(TemenosConstants.PAGESIZE, limitVal);
		responseVal.put(TemenosConstants.PAGEOFFSET, offsetVal);
		responseVal.put("totalCount", totalCount);
		responseVal.put("portfolioID", portfolioId);
		responseVal.put("opstatus", "0");
		responseVal.put("httpStatusCode", "200");
		Result final_result = Utilities.constructResultFromJSONObject(responseVal);
		final_result.addOpstatusParam("0");
		final_result.addHttpStatusCodeParam("200");
		final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		diagnostic.prepareDebug("==========> GetPerformanceMockPostProcessor Mock - Exited ").log();
		return final_result;
		}
		catch (Exception e) {
			alert.prepareError("==========> GetPerformanceMockPostProcessor Mock - Error: " + e.getMessage()).log();
			throw new Exception("Error while invoking GetPerformanceMockPostProcessor ");
		}
	}
	@SuppressWarnings("unused")
	public JSONArray getBenchMarkArray() {
		JSONObject response = new JSONObject();
		String[] benchMark = null, id = null;
		benchMark = new String[] { "S&P500", "NASDAQ", "DOWJ" };
		id = new String[] { "1", "2", "3" };
		JSONArray benchMarkArray = new JSONArray();
		for (int i = 0; i < benchMark.length; i++) {
			JSONObject benchMarkObj = new JSONObject();
			benchMarkObj.put("benchMark", benchMark[i]);
			benchMarkObj.put("benchMarkId", id[i]);
			benchMarkArray.put(benchMarkObj);
		}
		
		return benchMarkArray;
	}
	
	public String[] getBenchMarkReturn(String selectedBenchMark) {
		String[] response=null;
		String[] sp=null,nasdaq=null,dowJ=null;
		sp=new String[] {"2","3","11","8","-1","6","2","8","-3","1","6","8","17",
				"11","3","-2","1","-2","4","3"};
		nasdaq=new String[] {"4","5","9","7","-1","7","1","9","-2","1","6","8","10",
				"12","2","-2","3","-2","3","-1"};
		dowJ=new String[] {"3","1","8","2","-1","7","1","10","-2","1","4","5","8",
				"6","2","-2","3","3","-1","2"};
		if(selectedBenchMark.equals("S&P500")) {
			response=sp;
		}else if(selectedBenchMark.equals("NASDAQ")) {
			response=nasdaq;
		}else {
			response=dowJ;
		}
		return response;
	}
	
}
