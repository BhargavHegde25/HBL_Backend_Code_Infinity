package com.temenos.infinity.wealthorder.mock.processor.post;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.temenos.infinity.api.wealthOrder.backenddelegate.impl.HistoricalDataBackendDelegateImpl;
import com.temenos.infinity.api.wealthOrder.config.WealthAPIServices;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

public class getCurrencyGraphMockPostProcessor implements DataPostProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		diagnostic.prepareDebug("==========> getCurrencyGraphMockPostProcessor Mock - Entered").log();
		JSONObject response1 = new JSONObject();
		JSONArray objGraph = new JSONArray();
		// Map<String, Object> inputParams = (HashMap<String, Object>)
		// request.getOriginalRequest();
		String graphDuration = request.getParameter(TemenosConstants.DATEORPERIOD);
		String currencyPair = request.getParameter(TemenosConstants.CURRENCYPAIRS);
		try {
			if (currencyPair != null)
				objGraph = getCurrencyGraphData(request);
			else
				objGraph = getInstrumentGraphData(request);
			if (graphDuration != null)
				response1.put("historicalData", objGraph.toString());
			Result final_result = Utilities.constructResultFromJSONObject(response1);
			final_result.addOpstatusParam("0");
			final_result.addHttpStatusCodeParam("200");
			final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			result.appendResult(final_result);
		} catch (Exception e) {
			e.getMessage();
			alert.prepareError("==========> getCurrencyGraphMockPostProcessor Mock - Error: " + e.getMessage()).log();
		}
		return result;
	}

	public JSONArray getInstrumentGraphData(DataControllerRequest request) {
		diagnostic.prepareDebug("==========> getCurrencyGraphMockPostProcessor Mock - getInstrumentGraphData::Begin").log();
		HistoricalDataBackendDelegateImpl historicalData = new HistoricalDataBackendDelegateImpl();

		String graphDuration = request.getParameter(TemenosConstants.DATEORPERIOD);
		List<String> XaxisArray = new ArrayList<String>();
		List<Double> XaxisArrayValues = getMockDataPoints(request);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		JSONArray assetArray = new JSONArray();
		if (graphDuration.equalsIgnoreCase("1D")) {
			ZonedDateTime zonedTime = ZonedDateTime.now(ZoneId.of("America/New_York"));

			if (zonedTime.getDayOfWeek() == DayOfWeek.SATURDAY) {
				zonedTime = zonedTime.minusDays(1);
			} else if (zonedTime.getDayOfWeek() == DayOfWeek.SATURDAY) {
				zonedTime = zonedTime.minusDays(2);
			} else {
				if (zonedTime.getHour() < 9 || (zonedTime.getHour() == 9 && zonedTime.getMinute() < 30)) {

					zonedTime = zonedTime.minusDays(1);

					if (zonedTime.getDayOfWeek() == DayOfWeek.SATURDAY) {
						zonedTime = zonedTime.minusDays(1);
					} else if (zonedTime.getDayOfWeek() == DayOfWeek.SATURDAY) {
						zonedTime = zonedTime.minusDays(2);
					}
				}
			}

			zonedTime = zonedTime.withHour(9);
			zonedTime = zonedTime.withMinute(30);

			Calendar backDate = GregorianCalendar.from(zonedTime);

			Calendar endTime = GregorianCalendar.from(zonedTime);
			endTime.set(Calendar.HOUR_OF_DAY, 16);
			endTime.set(Calendar.MINUTE, 00);

			XaxisArray.add(df.format(backDate.getTime()));
			while (backDate.before(endTime)) {
				backDate.add(Calendar.MINUTE, 30);
				XaxisArray.add(df.format(backDate.getTime()));
			}

			for (int i = 0; i < XaxisArray.size() - 1; i++) {
				JSONObject responseObj = new JSONObject();
				if (XaxisArrayValues != null) {
					responseObj.put("CLOSE", XaxisArrayValues.get(i));
					responseObj.put("TIMESTAMP", XaxisArray.get(i).concat("+05:30"));
					assetArray.put(responseObj);
				}
			}
			JSONObject responseObj = new JSONObject();
			if (XaxisArrayValues != null) {
				responseObj.put("CLOSE", XaxisArrayValues.get(XaxisArrayValues.size() - 1));
				responseObj.put("TIMESTAMP", XaxisArray.get(XaxisArray.size() - 1).concat("+05:30"));
				assetArray.put(responseObj);
			}
		} else if (graphDuration == null || graphDuration.equalsIgnoreCase("1Y")) {
			String[] months = historicalData.nextMonths();

			XaxisArray = historicalData.getMonths(months);

			for (int i = 0; i < XaxisArray.size() - 1; i++) {
				JSONObject responseObj = new JSONObject();
				if (XaxisArrayValues != null) {
					responseObj.put("CLOSE", XaxisArrayValues.get(i));
					responseObj.put("TIMESTAMP", XaxisArray.get(i).concat("+05:30"));
					assetArray.put(responseObj);
				}
			}
			JSONObject responseObj = new JSONObject();
			if (XaxisArrayValues != null) {
				responseObj.put("CLOSE", XaxisArrayValues.get(XaxisArrayValues.size() - 1));
				responseObj.put("TIMESTAMP", XaxisArray.get(XaxisArray.size() - 1).concat("+05:30"));
				assetArray.put(responseObj);
			}
		} else if (graphDuration.equalsIgnoreCase("1M")) {
			Calendar c = Calendar.getInstance();
			Calendar backDate = Calendar.getInstance();
			backDate.add(Calendar.DAY_OF_MONTH, -30); // to get the date exactly
														// one month before
			// backDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); // set the
			// Monday of the week

			while (backDate.before(Calendar.getInstance())) {
				if (backDate.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
						|| Calendar.DAY_OF_WEEK == Calendar.SUNDAY) {
					backDate.add(Calendar.DATE, 1);
					continue;
				}
				XaxisArray.add(df.format(backDate.getTime()));
				backDate.add(Calendar.DATE, 1);
			}
			XaxisArray.add(df.format(c.getTime()));

			for (int i = 0; i < XaxisArray.size() - 1; i++) {
				JSONObject responseObj = new JSONObject();
				if (XaxisArrayValues != null) {
					responseObj.put("CLOSE", XaxisArrayValues.get(i));
					responseObj.put("TIMESTAMP", XaxisArray.get(i).concat("+05:30"));
					assetArray.put(responseObj);
				}
			}
			JSONObject responseObj = new JSONObject();
			if (XaxisArrayValues != null) {
				responseObj.put("CLOSE", XaxisArrayValues.get(XaxisArrayValues.size() - 1));
				responseObj.put("TIMESTAMP", XaxisArray.get(XaxisArray.size() - 1).concat("+05:30"));
				assetArray.put(responseObj);
			}
		}

		else if (graphDuration.equalsIgnoreCase("5Y")) {
			Calendar c = Calendar.getInstance();
			Calendar backDate = Calendar.getInstance();
			int firstYear = c.getInstance().get(Calendar.YEAR) - 4;
			backDate.set(firstYear, Calendar.JANUARY, 01, 00, 00, 00);
			if (backDate.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
				backDate.add(Calendar.DATE, 2);
			} else if (backDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				backDate.add(Calendar.DATE, 1);
			}

			while (backDate.before(Calendar.getInstance())) {
				XaxisArray.add(df.format(backDate.getTime()));
				backDate.add(Calendar.MONTH, 3);
			}

			XaxisArray.add(df.format(c.getTime()));
			for (int i = 0; i < XaxisArray.size() - 1; i++) {
				JSONObject responseObj = new JSONObject();
				if (XaxisArrayValues != null) {
					responseObj.put("CLOSE", XaxisArrayValues.get(i));
					responseObj.put("TIMESTAMP", XaxisArray.get(i).concat("+05:30"));
					assetArray.put(responseObj);
				}
			}
			JSONObject responseObj = new JSONObject();
			if (XaxisArrayValues != null) {
				responseObj.put("CLOSE", XaxisArrayValues.get(XaxisArrayValues.size() - 1));
				responseObj.put("TIMESTAMP", XaxisArray.get(XaxisArray.size() - 1).concat("+05:30"));
				assetArray.put(responseObj);
			}
		} else if (graphDuration.equalsIgnoreCase("YTD")) {

			// int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
			Map<Integer, Integer> daysPerMonth = new HashMap<>();

			Calendar backDate = Calendar.getInstance();
			int year = backDate.getInstance().get(Calendar.YEAR);
			backDate.set(year, Calendar.JANUARY, 01, 00, 00, 00);
			if (backDate.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
				backDate.add(Calendar.DATE, -1);
			} else if (backDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				backDate.add(Calendar.DATE, -2);
			}

			while (backDate.before(Calendar.getInstance())) {
				if (daysPerMonth.containsKey(backDate.get(Calendar.MONTH))) {
					daysPerMonth.put(backDate.get(Calendar.MONTH), daysPerMonth.get(backDate.get(Calendar.MONTH)) + 1);
				} else {
					daysPerMonth.put(backDate.get(Calendar.MONTH), 1);
				}
				if (daysPerMonth.get(backDate.get(Calendar.MONTH)) <= 4)
					XaxisArray.add(df.format(backDate.getTime()));
				backDate.add(Calendar.DATE, 7);

			}

			for (int i = 0; i < XaxisArray.size() - 1; i++) {
				JSONObject responseObj = new JSONObject();
				if (XaxisArrayValues != null) {
					responseObj.put("CLOSE", XaxisArrayValues.get(i));
					responseObj.put("TIMESTAMP", XaxisArray.get(i).concat("+05:30"));
					assetArray.put(responseObj);
				}
			}
			JSONObject responseObj = new JSONObject();
			if (XaxisArrayValues != null) {
				responseObj.put("CLOSE", XaxisArrayValues.get(XaxisArrayValues.size() - 1));
				responseObj.put("TIMESTAMP", XaxisArray.get(XaxisArray.size() - 1).concat("+05:30"));
				assetArray.put(responseObj);
			}
		}
		// assetArray.put(response);
		diagnostic.prepareDebug("==========> getCurrencyGraphMockPostProcessor Mock - getInstrumentGraphData::End").log();
		return assetArray;
	}

	public List<String> getMonths(String[] arr) {
		ArrayList<String> resultList = new ArrayList<>();
		Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();
		Map<Integer, Integer> daysPerMonth = new HashMap<Integer, Integer>();

//			if (arr[0].equalsIgnoreCase("Jan")) {
//				startDate.set(Calendar.MONTH, 0);
		//
//			} else {
//				startDate.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - 1);
//				startDate.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH) + 1);
//			}
		startDate.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - 1);
		startDate.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH));
//			startDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
//			startDate.set(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		while (startDate.before(Calendar.getInstance())) {
			if (daysPerMonth.containsKey(startDate.get(Calendar.MONTH))) {
				daysPerMonth.put(startDate.get(Calendar.MONTH), daysPerMonth.get(startDate.get(Calendar.MONTH)) + 1);
			} else {
				daysPerMonth.put(startDate.get(Calendar.MONTH), 1);
			}
			if (daysPerMonth.get(startDate.get(Calendar.MONTH)) <= 4)
				resultList.add(df.format(startDate.getTime()));
			startDate.add(Calendar.DATE, 7);
		}
		resultList.add(df.format(endDate.getTime()));

		return resultList;
	}

	public String[] nextMonths() {
		int currentMonth = Calendar.getInstance().get(Calendar.MONTH);

		String[] finalArr = null;

		String a[] = new String[] { "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV",
				"DEC" };
		List<String> al = Arrays.asList(a);

		List<String> finalList = new ArrayList<String>();
		finalList.addAll(al.subList(currentMonth + 1, al.size()));
		finalList.addAll(al.subList(0, currentMonth + 1));

		finalArr = finalList.toArray(new String[0]);

		return finalArr;

	}

	private JSONArray getCurrencyGraphData(DataControllerRequest request) {
		diagnostic.prepareDebug("==========> getCurrencyGraphMockPostProcessor Mock - getCurrencyGraphData::Begin").log();
		String graphDuration = request.getParameter(TemenosConstants.DATEORPERIOD);
		List<String> XaxisArray = new ArrayList<String>();
		List<Double> XaxisArrayValues = getMockDataPoints(request);
		// DecimalFormat format = new DecimalFormat("##.00");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		JSONArray assetArray = new JSONArray();
		if (graphDuration == null || graphDuration.equalsIgnoreCase("1Y")) {
			String[] months = nextMonths();

			XaxisArray = getMonths(months);

			for (int i = 0; i < XaxisArray.size() - 1; i++) {
				JSONObject responseObj = new JSONObject();
				responseObj.put("CLOSE", XaxisArrayValues.get(i));
				responseObj.put("TIMESTAMP", XaxisArray.get(i).concat("+05:30"));
				assetArray.put(responseObj);
			}
			JSONObject responseObj = new JSONObject();
			responseObj.put("CLOSE", XaxisArrayValues.get(XaxisArrayValues.size() - 1));
			responseObj.put("TIMESTAMP", XaxisArray.get(XaxisArray.size() - 1).concat("+05:30"));
			assetArray.put(responseObj);
		} else if (graphDuration.equalsIgnoreCase("1M")) {
			Calendar c = Calendar.getInstance();
			Calendar backDate = Calendar.getInstance();
			backDate.add(Calendar.DAY_OF_MONTH, -30); // to get the date exactly
														// one month before
			// backDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); // set the
			// Monday of the week

			while (backDate.before(Calendar.getInstance())) {
				if (backDate.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
						|| Calendar.DAY_OF_WEEK == Calendar.SUNDAY) {
					backDate.add(Calendar.DATE, 1);
					continue;
				}
				XaxisArray.add(df.format(backDate.getTime()));
				backDate.add(Calendar.DATE, 1);
			}
			XaxisArray.add(df.format(c.getTime()));

			for (int i = 0; i < XaxisArray.size() - 1; i++) {
				JSONObject responseObj = new JSONObject();
				responseObj.put("CLOSE", XaxisArrayValues.get(i));
				responseObj.put("TIMESTAMP", XaxisArray.get(i).concat("+05:30"));
				assetArray.put(responseObj);
			}
			JSONObject responseObj = new JSONObject();
			responseObj.put("CLOSE", XaxisArrayValues.get(XaxisArrayValues.size() - 1));
			responseObj.put("TIMESTAMP", XaxisArray.get(XaxisArray.size() - 1).concat("+05:30"));
			assetArray.put(responseObj);
		}

		else if (graphDuration.equalsIgnoreCase("5Y")) {
			Calendar c = Calendar.getInstance();
			Calendar backDate = Calendar.getInstance();
			int firstYear = c.getInstance().get(Calendar.YEAR) - 4;
			backDate.set(firstYear, Calendar.JANUARY, 01, 00, 00, 00);
			if (backDate.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
				backDate.add(Calendar.DATE, 2);
			} else if (backDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				backDate.add(Calendar.DATE, 1);
			}

			while (backDate.before(Calendar.getInstance())) {
				XaxisArray.add(df.format(backDate.getTime()));
				backDate.add(Calendar.MONTH, 3);
			}

			XaxisArray.add(df.format(c.getTime()));
			for (int i = 0; i < XaxisArray.size() - 1; i++) {
				JSONObject responseObj = new JSONObject();
				responseObj.put("CLOSE", XaxisArrayValues.get(i));
				responseObj.put("TIMESTAMP", XaxisArray.get(i).concat("+05:30"));
				assetArray.put(responseObj);
			}
			JSONObject responseObj = new JSONObject();
			responseObj.put("CLOSE", XaxisArrayValues.get(XaxisArrayValues.size() - 1));
			responseObj.put("TIMESTAMP", XaxisArray.get(XaxisArray.size() - 1).concat("+05:30"));
			assetArray.put(responseObj);
		} else if (graphDuration.equalsIgnoreCase("YTD")) {

			// int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
			Map<Integer, Integer> daysPerMonth = new HashMap<>();

			Calendar backDate = Calendar.getInstance();
			int year = backDate.getInstance().get(Calendar.YEAR);
			backDate.set(year, Calendar.JANUARY, 01, 00, 00, 00);
			if (backDate.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
				backDate.add(Calendar.DATE, -1);
			} else if (backDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				backDate.add(Calendar.DATE, -2);
			}

			while (backDate.before(Calendar.getInstance())) {
				if (daysPerMonth.containsKey(backDate.get(Calendar.MONTH))) {
					daysPerMonth.put(backDate.get(Calendar.MONTH), daysPerMonth.get(backDate.get(Calendar.MONTH)) + 1);
				} else {
					daysPerMonth.put(backDate.get(Calendar.MONTH), 1);
				}
				if (daysPerMonth.get(backDate.get(Calendar.MONTH)) <= 4)
					XaxisArray.add(df.format(backDate.getTime()));
				backDate.add(Calendar.DATE, 7);

			}

			for (int i = 0; i < XaxisArray.size() - 1; i++) {
				JSONObject responseObj = new JSONObject();
				responseObj.put("CLOSE", XaxisArrayValues.get(i));
				responseObj.put("TIMESTAMP", XaxisArray.get(i).concat("+05:30"));
				assetArray.put(responseObj);
			}
			JSONObject responseObj = new JSONObject();
			responseObj.put("CLOSE", XaxisArrayValues.get(XaxisArrayValues.size() - 1));
			responseObj.put("TIMESTAMP", XaxisArray.get(XaxisArray.size() - 1).concat("+05:30"));
			assetArray.put(responseObj);
		}
		// assetArray.put(response);
		diagnostic.prepareDebug("==========> getCurrencyGraphMockPostProcessor Mock - getCurrencyGraphData::End").log();
		return assetArray;
	}

	public List<Double> getMockDataPoints(DataControllerRequest request) {
		String currencyPair = request.getParameter(TemenosConstants.CURRENCYPAIRS);
		String ricCode = request.getParameter(TemenosConstants.RICCODE);
		if (currencyPair != null) {
			if (currencyPair.equalsIgnoreCase("EURUSD")) {
				List<Double> dataPoints = new ArrayList<Double>(Arrays.asList(1.1016, 1.105, 1.1022, 1.1015, 1.1057,
						1.1119, 1.1078, 1.1175, 1.1158, 1.112, 1.1088, 1.1023, 1.1093, 1.0943, 1.083, 1.0843, 1.1025,
						1.1285, 1.1105, 1.0694, 1.114, 1.0808, 1.0935, 1.0876, 1.082, 1.0983, 1.084, 1.0815, 1.09,
						1.1098, 1.1284, 1.1254, 1.1175, 1.1217, 1.1248, 1.1298, 1.1426, 1.1654, 1.1774, 1.1786, 1.1841,
						1.1795, 1.1903, 1.1838, 1.1845, 1.1837, 1.163, 1.1713, 1.1824, 1.1718, 1.1859, 1.1647, 1.1872,
						1.1832, 1.1873));
				return dataPoints;
			} else if (currencyPair.equalsIgnoreCase("USDEUR")) {
				List<Double> dataPoints = new ArrayList<Double>(Arrays.asList(0.9073, 0.9046, 0.9069, 0.9075, 0.9041,
						0.899, 0.9025, 0.8946, 0.8958, 0.899, 0.9016, 0.9069, 0.9013, 0.9135, 0.923, 0.9219, 0.9068,
						0.886, 0.9003, 0.9349, 0.8973, 0.9251, 0.9142, 0.9195, 0.9239, 0.9105, 0.9224, 0.9245, 0.9172,
						0.9009, 0.8859, 0.8883, 0.8945, 0.8912, 0.8889, 0.8848, 0.8749, 0.8578, 0.8488, 0.8482, 0.8443,
						0.8475, 0.8398, 0.8445, 0.844, 0.8445, 0.8595, 0.8534, 0.8455, 0.8532, 0.843, 0.8583, 0.842,
						0.8449, 0.842));
				return dataPoints;
			} else if (currencyPair.equalsIgnoreCase("USDGBP")) {
				List<Double> dataPoints = new ArrayList<Double>(Arrays.asList(0.7828, 0.775, 0.7792, 0.7731, 0.7611,
						0.7503, 0.7689, 0.7646, 0.764, 0.7655, 0.7685, 0.7646, 0.7574, 0.7756, 0.7663, 0.7708, 0.7799,
						0.7662, 0.8143, 0.8588, 0.8026, 0.8154, 0.8028, 0.7998, 0.8083, 0.7996, 0.8057, 0.8259, 0.8213,
						0.8098, 0.7895, 0.7973, 0.8091, 0.8106, 0.8008, 0.7921, 0.7955, 0.7816, 0.7641, 0.766, 0.7641,
						0.7638, 0.7488, 0.7528, 0.7814, 0.7741, 0.7843, 0.7727, 0.7663, 0.7741, 0.7666, 0.7725, 0.7599,
						0.7582, 0.7539));
				return dataPoints;
			} else if (currencyPair.equalsIgnoreCase("GBPUSD")) {
				List<Double> dataPoints = new ArrayList<Double>(Arrays.asList(1.277, 1.2899, 1.283, 1.2933, 1.3135,
						1.3325, 1.3002, 1.3076, 1.3086, 1.3059, 1.3008, 1.3076, 1.3199, 1.2891, 1.3046, 1.2969, 1.282,
						1.3047, 1.2276, 1.1641, 1.2456, 1.226, 1.2452, 1.2499, 1.2367, 1.2502, 1.2407, 1.2104, 1.2164,
						1.2344, 1.2663, 1.254, 1.2356, 1.2333, 1.2483, 1.262, 1.2565, 1.2789, 1.3088, 1.3051, 1.3084,
						1.3087, 1.3349, 1.3282, 1.2793, 1.2915, 1.2745, 1.2931, 1.3046, 1.2913, 1.304, 1.2941, 1.3156,
						1.3186, 1.3261));
				return dataPoints;
			} else {
				List<Double> dataPoints = new ArrayList<Double>(Arrays.asList(1.1586, 1.1669, 1.1636, 1.1737, 1.1875,
						1.198, 1.1735, 1.1698, 1.1723, 1.1739, 1.1727, 1.1858, 1.1896, 1.1776, 1.2042, 1.1956, 1.1625,
						1.1559, 1.1052, 1.0884, 1.1177, 1.1341, 1.1384, 1.1492, 1.1426, 1.1384, 1.1445, 1.119, 1.1163,
						1.1121, 1.1218, 1.1139, 1.1053, 1.0991, 1.1096, 1.1166, 1.0993, 1.0972, 1.1105, 1.107, 1.1047,
						1.1092, 1.1211, 1.1217, 1.0798, 1.0907, 1.0955, 1.1039, 1.103, 1.1018, 1.0992, 1.1107, 1.1078,
						1.1141, 1.1165));
				return dataPoints;
			}
		} else if (ricCode != null) {
			if (ricCode.equalsIgnoreCase("AMZN.O")) {
				List<Double> dataPoints = new ArrayList<Double>(Arrays.asList(1785.00, 1736.91, 1745.90, 1801.52,
						1751.99, 1762.15, 1784.75, 1869.28, 1874.08, 1883.03, 1862.94, 1861.77, 2008.26, 2077.99,
						2133.71, 2094.18, 1884.27, 1902.09, 1778.32, 1846.73, 1902.12, 1906.20, 2044.25, 2367.06,
						2409.79, 2288.24, 2377.14, 2409.04, 2433.06, 2437.34, 2480.73, 2549.27, 2672.94, 2693.91,
						2886.27, 3205.00, 2961.14, 3008.5, 3165.885, 3167.15, 3146.75, 3284.38, 3400.445, 3297.89,
						3117.73, 2954.72, 3095.97, 3126.585, 3287.70, 3216.10, 3204.98, 3037.32, 3311.86, 3128.79,
						3113.84));
				return dataPoints;
			} else if (ricCode.equalsIgnoreCase("GOOGL.O")) {
				List<Double> dataPoints = new ArrayList<Double>(
						Arrays.asList(1307.43, 1331.68, 1294.3, 1305.77, 1339.95, 1347.31, 1351.09, 1354.52, 1360.00,
								1429.36, 1479.43, 1466.73, 1434.45, 1478.63, 1518.34, 1482.39, 1339.96, 1295.65, 1213.7,
								1066.15, 1109.16, 1089.63, 1206.17, 1274.34, 1273.81, 1317.08, 1381.755, 1374.21,
								1414.65, 1434.52, 1437.29, 1414.97, 1427.34, 1362.73, 1471.79, 1540.58, 1514.23,
								1509.07, 1486.45, 1498.19, 1501.84, 1574.82, 1638.35, 1582.28, 1517.01, 1451.2, 1440.16,
								1457.07, 1510.45, 1566.99, 1632.88, 1615.39, 1759.2, 1772.17, 1758.85));
				return dataPoints;
			} else if (ricCode.equalsIgnoreCase("AAPL.O")) {
				List<Double> dataPoints = new ArrayList<Double>(Arrays.asList(65.035, 66.44, 65.445, 66.8125, 67.6775,
						68.7875, 69.86, 72.45, 74.3575, 77.5825, 79.6825, 79.5775, 77.3775, 80.0075, 81.2375, 78.2625,
						68.34, 72.2575, 69.4925, 57.31, 61.935, 60.3525, 66.9975, 70.7, 70.7425, 72.2675, 77.5325,
						76.9275, 79.7225, 79.485, 82.875, 84.7, 87.43, 88.4075, 91.0275, 95.92, 96.3275, 92.615, 106.26,
						111.1125, 114.9075, 124.37, 124.8075, 120.96, 112.00, 106.84, 112.28, 113.02, 116.97, 119.02,
						115.04, 108.86, 118.69, 119.26, 118.64));
				return dataPoints;
			} else if (ricCode.equalsIgnoreCase("LVMH.PA")) {
				List<Double> dataPoints = new ArrayList<Double>(
						Arrays.asList(403.0, 405.5, 396.25, 407.3, 401.35, 403.8, 410.5, 417.6, 419.1, 423.65, 439.05,
								416.3, 395.3, 413.95, 414.95, 404.5, 370.85, 360.35, 314.9, 311.0, 341.65, 325.05,
								347.4, 358.0, 340.85, 352.45, 352.25, 330.25, 355.3, 375.0, 404.25, 379.15, 380.05,
								387.7, 396.7, 400.7, 410.25, 399.1, 366.75, 374.0, 386.05, 385.45, 396.35, 402.85,
								416.6, 413.85, 400.2, 407.65, 411.9, 432.6, 428.0, 402.3, 435.5, 469.75, 488.65));
				return dataPoints;
			} else if (ricCode.equalsIgnoreCase("AMAG.OQ")) {
				List<Double> dataPoints = new ArrayList<Double>(Arrays.asList(17.45, 18.03, 18.0, 18.12, 18.65, 18.75,
						18.94, 18.59, 18.18, 18.04, 17.95, 17.22, 16.24, 17.48, 17.14, 17.2, 17.01, 14.64, 10.77, 9.08,
						9.22, 8.84, 9.7, 9.31, 10.27, 10.85, 10.61, 9.54, 10.57, 11.23, 13.22, 10.97, 11.25, 12.57,
						11.62, 11.05, 11.24, 11.17, 11.55, 12.05, 12.37, 11.97, 12.25, 11.7, 12.46, 11.83, 10.53, 10.6,
						10.97, 11.86, 11.88, 11.1, 12.05, 12.5, 13.24));
				return dataPoints;
			} else if (ricCode.equalsIgnoreCase("AMAL.OQ")) {
				List<Double> dataPoints = new ArrayList<Double>(Arrays.asList(18.45, 19.03, 19.0, 19.12, 19.65, 19.75,
						19.94, 19.59, 19.18, 19.04, 18.95, 18.22, 17.24, 18.48, 18.14, 18.2, 16.01, 15.64, 11.77, 9.08,
						9.22, 8.84, 9.7, 9.31, 10.27, 10.85, 10.61, 9.54, 10.57, 11.23, 13.22, 10.97, 11.25, 12.57,
						11.62, 11.05, 11.24, 11.17, 11.55, 12.05, 12.37, 11.97, 12.25, 11.7, 12.46, 11.83, 10.53, 11.6,
						11.97, 11.86, 11.88, 11.1, 11.05, 12.5, 13.24));
				return dataPoints;
			} else if (ricCode.equalsIgnoreCase("AMRN.A")) {
				List<Double> dataPoints = new ArrayList<Double>(Arrays.asList(17.49, 24.06, 20.92, 21.25, 22.45, 24.12,
						21.04, 21.55, 20.97, 18.96, 19.91, 20.23, 18.56, 17.66, 17.64, 17.93, 14.65, 14.92, 12.43,
						10.63, 13.22, 4.78, 6.04, 6.5, 7.52, 7.35, 7.71, 7.71, 7.23, 6.87, 6.67, 6.59, 6.93, 6.72, 6.93,
						6.7, 6.72, 6.52, 6.49, 6.99, 6.83, 6.92, 7.36, 4.315, 4.06, 4.17, 3.81, 4.45, 5.25, 5.14, 4.99,
						4.85, 4.28, 4.27, 4.54));
				return dataPoints;
			} else if (ricCode.equalsIgnoreCase("AMBA.OQ")) {
				List<Double> dataPoints = new ArrayList<Double>(Arrays.asList(56.1, 58.39, 54.72, 54.74, 53.72, 54.97,
						57.97, 59.91, 62.82, 62.42, 62.69, 60.49, 59.14, 62.31, 71.52, 67.45, 59.45, 50.64, 45.19,
						40.31, 47.88, 45.43, 47.72, 49.25, 52.05, 49.0, 54.75, 52.2, 57.29, 56.74, 53.68, 50.55, 48.9,
						44.86, 45.21, 48.21, 47.37, 44.58, 45.28, 46.34, 45.83, 45.97, 52.01, 52.18, 50.37, 53.03,
						49.61, 51.92, 58.15, 57.06, 57.15, 54.67, 60.15, 58.71, 65.345));
				return dataPoints;
			} else if (ricCode.equalsIgnoreCase("AMCX.O")) {
				List<Double> dataPoints = new ArrayList<Double>(Arrays.asList(41.41, 39.65, 39.82, 38.43, 38.23, 37.39,
						39.47, 39.54, 39.36, 39.69, 42.25, 38.25, 36.59, 37.69, 37.79, 35.05, 31.0, 27.33, 27.94, 24.73,
						24.83, 20.58, 24.5, 24.37, 21.19, 24.04, 26.03, 27.58, 29.16, 28.27, 33.31, 27.89, 26.62, 22.64,
						23.16, 24.35, 24.92, 24.87, 23.1, 23.595, 25.2, 24.51, 25.39, 23.21, 20.86, 24.98, 24.19, 24.05,
						23.75, 23.275, 23.36, 21.25, 23.89, 26.36, 29.0));
				return dataPoints;
			} else if (ricCode.equalsIgnoreCase("MSFT.N")) {
				List<Double> dataPoints = new ArrayList<Double>(
						Arrays.asList(151.69, 154.5, 157.335, 158.97, 158.67, 161.21, 167.03, 165.03, 170.19, 183.9,
								185.35, 178.46, 161.96, 161.36, 158.93, 136.77, 149.58, 153.92, 164.95, 178.67, 174.48,
								174.63, 184.63, 183.31, 183.46, 183.13, 187.24, 187.8, 195.14, 196.24, 206.25, 213.75,
								202.9, 201.3, 204.99, 212.52, 208.8, 213.3, 228.95, 214.17, 204.08, 200.45, 207.79,
								206.12, 215.78, 219.725, 216.13, 202.61, 223.44, 216.5, 210.39, 215.23, 214.19));
				return dataPoints;
			} else if (ricCode.equalsIgnoreCase("TSLA.OQ")) {
				List<Double> dataPoints = new ArrayList<Double>(Arrays.asList(67.178, 71.678, 81.118, 86.076, 88.602,
						95.63, 102.1, 112.964, 130.114, 149.614, 160.006, 180.2, 133.598, 140.696, 109.324, 85.506,
						102.872, 96.002, 114.6, 150.778, 145.03, 140.264, 163.884, 159.834, 163.376, 167.00, 177.132,
						187.056, 200.18, 191.948, 241.732, 308.93, 300.168, 283.4, 286.152, 290.542, 330.142, 409.996,
						442.68, 418.32, 372.72, 442.15, 407.34, 415.09, 434.00, 439.67, 420.63, 388.04, 429.95, 408.5,
						489.61, 585.76, 593.38));
				return dataPoints;
			} else if (ricCode.equalsIgnoreCase("WMT.N")) {
				List<Double> dataPoints = new ArrayList<Double>(Arrays.asList(119.78, 120.29, 120.29, 119.59, 117.89,
						116.38, 114.96, 114.37, 114.49, 116.45, 117.89, 118.58, 107.68, 117.23, 114.1, 113.97, 109.58,
						119.48, 121.8, 132.12, 129.44, 122.92, 122.94, 125.94, 124.33, 124.06, 121.56, 117.74, 119.85,
						118.32, 119.21, 130.68, 131.74, 131.24, 129.4, 129.97, 132.6, 131.63, 140.3, 142.83, 136.7,
						135.29, 137.27, 140.5, 142.78, 144.71, 143.85, 138.75, 145.77, 150.54, 150.24, 151.6, 149.3));
				return dataPoints;
			} else if (ricCode.equalsIgnoreCase("IXM0461.DE")) {
				List<Double> dataPoints = new ArrayList<Double>(Arrays.asList(286.67, 287.7138, 287.4623, 287.6988,
						285.9225, 286.4889, 285.2566, 287.0173, 286.0793, 289.1433, 290.9961, 291.8677, 294.5241,
						295.6687, 295.5725, 294.9065, 295.8449, 301.7169, 293.4718, 289.2953, 291.4473, 286.9141,
						290.6052, 286.8046, 282.9884, 287.637, 289.7462, 289.7178, 284.3093, 281.3128, 277.9103,
						282.1898, 283.8088, 281.279, 282.7912, 289.2102, 285.9447, 290.1855, 289.7221, 288.9341,
						289.8574, 291.4802, 291.075, 294.6975, 295.7867, 301.371, 301.8388, 298.8635, 298.2935,
						300.3985, 296.4671, 294.3168, 291.2942, 292.8192, 292.6368, 288.0287, 289.7738, 282.0492,
						287.1513, 280.9408, 285.5923, 291.2942, 292.8192, 292.6368, 301.23, 306.65));
				return dataPoints;
			} else if (ricCode.equalsIgnoreCase("2YTD") || ricCode.equalsIgnoreCase("5YTD")
					|| ricCode.equalsIgnoreCase("10YTD") || ricCode.equalsIgnoreCase("FEURUSD")
					|| ricCode.equalsIgnoreCase("FGBPUSD") || ricCode.equalsIgnoreCase("FCHFUSD")) {
				return null;
			} else {
				List<Double> dataPoints = new ArrayList<Double>(
						Arrays.asList(15.55, 15.415, 15.44, 14.98, 15.06, 14.915, 15.13, 15.245, 15.03, 15.015, 14.75,
								15.29, 15.305, 15.525, 15.855, 16.13, 15.645, 15.605, 13.03, 13.715, 14.245, 15.09,
								14.22, 13.995, 13.32, 13.495, 13.295, 12.76, 13.205, 13.655, 14.865, 14.04, 14.105,
								13.945, 13.56, 13.47, 13.81, 13.55, 13.5, 13.39, 13.75, 13.225, 13.445, 14.315, 14.605,
								13.43, 13.695, 13.49, 13.875, 14.055, 13.85, 13.36, 13.41, 14.34, 14.135));
				return dataPoints;
			}
		}
		return null;
	}
}
