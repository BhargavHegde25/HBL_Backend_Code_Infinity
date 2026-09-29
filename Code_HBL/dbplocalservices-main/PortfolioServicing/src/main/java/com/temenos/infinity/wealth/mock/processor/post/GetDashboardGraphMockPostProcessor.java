/**
 * 
 */
package com.temenos.infinity.wealth.mock.processor.post;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * Mock data construction for getDashboardGraph service
 * 
 * @author muthukumarv
 */
public class GetDashboardGraphMockPostProcessor implements DataPostProcessor2 {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		diagnostic.prepareDebug("==========> GetDashboardGraphMockPostProcessor Mock - Entered").log();
		try {

			JSONObject jsonObj = new JSONObject();
			String perfMetric = EnvironmentConfigurationsHandler.getValue(TemenosConstants.INF_WLTH_PRFM_METRIC,
					request);
			String customerId = (String) request.getParameter(TemenosConstants.CUSTOMERID);
			String graphDuration = (String) request.getParameter(TemenosConstants.GRAPHDURATION);
			boolean isFirstTime = false;
			if(graphDuration==null || graphDuration.isEmpty())
			{
				if (perfMetric.equalsIgnoreCase(TemenosConstants.OneY)) {
					graphDuration = "OneY";
				}else
				{
					graphDuration=perfMetric;
				}
				isFirstTime = true;	
			}
			String[] XaxisArray = new String[30];
			Double[] XaxisArrayPerc;
//			String[] XaxisArrayVal = new String[] { "51500", "51800", "52200", "52500", "52600", "52400", "52700", "52500",
//					"53700", "53100", "53800", "54100", "54300", "53300", "54600", "54800", "55500", "55400", "55600",
//					"55800", "54800", "56100", "56500", "56500", "56400", "56600", "56500", "57500", "58500", "58600" };

			String[] XaxisArrayVal = new String[] { "130278.45", "121848.92", "120837.92", "116492.00", "101010.10",
					"100090.48", "99563.08", "99153.11", "98326.48", "98635.35", "99759.22", "101890.12", "102911.56",
					"103829.49", "104278.32", "105673.58", "106342.23", "107234.46", "108728.72", "109289.60", "110289.19",
					"117893.23", "119647.24", "120415.34", "104563.67", "99849.24", "121212.12", "125464.34", "130000.00",
					"133497.14" };
			JSONArray assetArray = new JSONArray();
			if (graphDuration.equalsIgnoreCase("OneM")) {
				Date today = new Date();
				Calendar dates = Calendar.getInstance();
				dates.add(Calendar.DAY_OF_MONTH, -30);
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				for (int i = 0; i < 29; i++) {
					dates.add(Calendar.DATE, 1);
					XaxisArray[i] = df.format(dates.getTime());

				}
				XaxisArray[29] = df.format(today.getTime());
				XaxisArrayPerc = new Double[] {};
				for (int i = 0; i < XaxisArray.length; i++) {
					JSONObject resp = new JSONObject();
					resp.put("TIMESTAMP", XaxisArray[i].concat("+05:30"));
					resp.put("AMOUNT", XaxisArrayVal[i]);
					resp.put("PERCENTAGE", i + 0.25);
					assetArray.put(resp);
				}
			}
			if (graphDuration.equalsIgnoreCase("OneY")) {
//				XaxisArrayVal = new String[] { "51500", "51800", "52200", "52500", "52600", "52400", "52700", "52500",
//						"53700", "53100", "53800", "54100", "54300", "53500", "53300", "54600", "54800", "55600", "55500",
//						"55400", "55600", "55800", "54800", "56100", "56500", "56500", "56400", "56600", "56500", "57500",
//						"58500", "58600", "57300", "56750", "56000", "54560", "56100", "56950", "55000", "54900", "57500",
//						"57800", "58000", "55600", "55100", "54700" };

				XaxisArrayVal = new String[] { "130278.45", "121848.92", "120837.92", "116492.00", "101010.10", "100090.48",
						"99563.08", "99153.11", "98326.48", "98635.35", "99759.22", "101890.12", "102911.56", "103829.49",
						"104278.32", "100090.48", "99563.08", "99153.11", "98326.48", "98635.35", "105673.58", "106342.23",
						"107234.46", "108728.72", "109289.60", "110289.19", "117893.23", "119647.24", "120415.34",
						"104563.67", "110837.89", "117380.23", "119807.09", "120415.34", "104563.67", "103678.28",
						"101010.10", "100013.99", "99563.08", "99153.11", "99849.24", "120820.78", "120964.90", "125464.34",
						"130000.00", "133497.14" };

				String[] months = nextMonths();
				XaxisArray = getMonths(months);
				for (int i = 0; i < XaxisArray.length; i++) {
					JSONObject resp = new JSONObject();
					resp.put("TIMESTAMP", XaxisArray[i].concat("+05:30"));
					resp.put("AMOUNT", XaxisArrayVal[i]);
					resp.put("PERCENTAGE", i + 0.25);
					assetArray.put(resp);
				}
			}
			if (graphDuration.equalsIgnoreCase("FiveY")) {
				Calendar c = Calendar.getInstance();
				Calendar backDate = Calendar.getInstance();
				int firstYear = c.getInstance().get(Calendar.YEAR) - 4;
				backDate.set(firstYear, Calendar.JANUARY, 06, 00, 00, 00);

				DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				XaxisArray[0] = df.format(backDate.getTime());

				int index_count = 1;
				while (backDate.before(Calendar.getInstance())) {
					backDate.add(Calendar.MONTH, 3);
					XaxisArray[index_count] = df.format(backDate.getTime());
					index_count++;
				}
				XaxisArray[index_count - 1] = df.format(c.getTime());
				for (int i = 0; i < index_count; i++) {
					JSONObject resp = new JSONObject();
					resp.put("TIMESTAMP", XaxisArray[i].concat("+05:30"));
					resp.put("AMOUNT", XaxisArrayVal[i]);
					resp.put("PERCENTAGE", i + 0.25);
					assetArray.put(resp);
				}
			}
			if (graphDuration.equalsIgnoreCase("YTD")) {
//				XaxisArrayVal = new String[] { "51500", "51800", "52200", "52500", "52600", "52400", "52700", "52500",
//						"53700", "53100", "53800", "54100", "54300", "53500", "53300", "54600", "54800", "55600", "55500",
//						"55400", "55600", "55800", "54800", "56100", "56500", "56500", "56400", "56600", "56500", "57500",
//						"58500", "58600", "57300", "56750", "56000", "54560", "56100", "56950", "55000", "54900", "57500",
//						"57800", "58000", "57900", "55600", "55100", "55900", "54700" };

				XaxisArrayVal = new String[] { "130278.45", "121848.92", "120837.92", "116492.00", "101010.10", "100090.48",
						"99563.08", "99153.11", "98326.48", "98635.35", "99759.22", "101890.12", "102911.56", "103829.49",
						"104278.32", "100090.48", "99563.08", "99153.11", "98326.48", "98635.35", "105673.58", "106342.23",
						"107234.46", "108728.72", "109289.60", "110289.19", "117893.23", "119647.24", "120415.34",
						"104563.67", "110837.89", "117380.23", "119807.09", "120415.34", "104563.67", "103678.28",
						"101010.10", "100013.99", "99563.08", "99153.11", "99849.24", "120820.78", "120964.90", "121212.12",
						"120415.34", "125464.34", "130000.00", "133497.14" };
				String[] months = prevMonths();
				XaxisArray = getPrevMonths(months);
				for (int i = 0; i < XaxisArray.length; i++) {
					JSONObject resp = new JSONObject();
					resp.put("TIMESTAMP", XaxisArray[i].concat("+05:30"));
					resp.put("AMOUNT", XaxisArrayVal[i]);
					resp.put("PERCENTAGE", i + 0.25);
					assetArray.put(resp);
				}
			}

			jsonObj.put("referenceCurrency", "USD");
			jsonObj.put("marketValue", "924663.78");
			jsonObj.put("unRealizedPL", "P");
			jsonObj.put("unRealizedPLAmount", "152807.00");
			jsonObj.put("unRealizedPLPercentage", "68.25");
			jsonObj.put("todayPL", "P");
			jsonObj.put("todayPLAmount", "1064.46");
			// jsonObj.put("accountName", "John Bailey Portfolio 1.");
			// jsonObj.put("accountNumber", "100777-1");
			jsonObj.put("todayPLPercentage", "1.42");
			jsonObj.put("graphDuration", assetArray);
			jsonObj.put("customerId", customerId);
			jsonObj.put("opstatus", "0");
			jsonObj.put("httpStatusCode", "200");
			if (isFirstTime && (perfMetric != null && !perfMetric.isEmpty() && perfMetric.equalsIgnoreCase(TemenosConstants.YTD))) {
				jsonObj.put("performanceValue", "+33.41");
				jsonObj.put("performance", "YTD");
			} else if (isFirstTime && (perfMetric != null && !perfMetric.isEmpty() && perfMetric.equalsIgnoreCase(TemenosConstants.OneY))) {
				jsonObj.put("performanceValue", "+32.56");
				jsonObj.put("performance", "1Y");
			}
			Result final_result = Utilities.constructResultFromJSONObject(jsonObj);
			final_result.addOpstatusParam("0");
			final_result.addHttpStatusCodeParam("200");
			final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			return final_result;


		} catch (Exception e) {
			e.getMessage();
			alert.prepareError("==========> GetDashboardGraphMockPostProcessor Mock - Error: " + e.getMessage()).log();
		}
		diagnostic.prepareDebug("==========> GetDashboardGraphMockPostProcessor Mock - Exited").log();
		return result;
	}
	
	public String[] nextMonths() {
		diagnostic.prepareDebug("==========> nextMonths Mock - Entered ").log();
		int currentMonth = Calendar.getInstance().get(Calendar.MONTH);

		String[] finalArr = null;

		String a[] = new String[] { "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV",
				"DEC" };
		List<String> al = Arrays.asList(a);

		List<String> finalList = new ArrayList<String>();
		finalList.addAll(al.subList(currentMonth + 1, al.size()));
		finalList.addAll(al.subList(0, currentMonth + 1));

		finalArr = finalList.toArray(new String[0]);
		diagnostic.prepareDebug("==========> nextMonths Mock - Executed ").log();
		return finalArr;

	}

	public String[] prevMonths() {
		diagnostic.prepareDebug("==========> prevMonths Mock - Entered ").log();
		int currentMonth = Calendar.getInstance().get(Calendar.MONTH);

		String[] finalArr = null;

		String a[] = new String[] { "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV",
				"DEC" };
		List<String> al = Arrays.asList(a);

		List<String> finalList = new ArrayList<String>();
		if (currentMonth == 0) {
			finalList.addAll(al.subList(0, 1));
		} else {
			finalList.addAll(al.subList(0, currentMonth));
		}

		finalArr = finalList.toArray(new String[0]);
		diagnostic.prepareDebug("==========> prevMonths Mock - Executed ").log();
		return finalArr;

	}

	public String[] getMonths(String[] arr) {
		diagnostic.prepareDebug("==========> getMonths Mock - Entered ").log();
		String[] resultArray = new String[46];
		Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();

//		if (arr[0].equalsIgnoreCase("Jan")) {
//			startDate.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - 1);
//			//startDate.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH));
//		} else {
//			startDate.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - 1);
//			startDate.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH) + 1);
//		}
		startDate.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - 1);
		startDate.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH));
		// startDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
	//	startDate.set(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		for (int i = 0; i < 3; i++) {
			startDate.add(Calendar.DATE, 4);
			resultArray[i] = df.format(startDate.getTime());
		}
		for (int i = 3; i < 45; i++) {
			startDate.add(Calendar.DATE, 8);
			resultArray[i] = df.format(startDate.getTime());
		}
		endDate.add(Calendar.DATE, -1);
		resultArray[45] = df.format(endDate.getTime());
		diagnostic.prepareDebug("==========> getMonths Mock - Executed ").log();
		return resultArray;
	}

	public String[] getPrevMonths(String[] arr) {
		diagnostic.prepareDebug("==========> getPrevMonths Mock - Entered ").log();
		String[] resultArray = new String[48];
		Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();

		if (arr[0].equalsIgnoreCase("Jan")) {
			startDate.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
			startDate.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH));
		} else {
			startDate.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
			startDate.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH));
		}
		startDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		for (int i = 47; i >= 0; i--) {
			startDate.add(Calendar.DATE, -8);
			resultArray[i] = df.format(startDate.getTime());
		}
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		endDate.add(Calendar.DATE, -1);
		resultArray[47] = df.format(endDate.getTime());
		resultArray[0] = df.format(cal.getTime());
		diagnostic.prepareDebug("==========> getPrevMonths Mock - Executed ").log();
		return resultArray;
	}
}