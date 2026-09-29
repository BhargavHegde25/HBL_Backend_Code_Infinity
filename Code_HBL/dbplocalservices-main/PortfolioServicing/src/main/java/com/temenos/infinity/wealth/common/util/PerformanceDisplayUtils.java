package com.temenos.infinity.wealth.common.util;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * @author Sarah
 *
 */

public class PerformanceDisplayUtils {

	public static JSONObject getPerformanceForDashboard(JSONArray pmdArray, String perfMetric, int year) {
		JSONObject investmentJSON = new JSONObject();
		String percentageValue = "";
		for (int i = 0; i <= pmdArray.length() - 1; i++) {
			JSONObject bodyJSON = pmdArray.getJSONObject(i);
			String periodVal = bodyJSON.getString("PERIOD_DISPLAY");
			String percentVal = bodyJSON.getString("PERIOD_RET_TWR");
			if (periodVal.equalsIgnoreCase(String.valueOf(year))) {
				if(percentVal.substring(0,1) == "-" || Double.parseDouble(percentVal) < 0.00) {
					percentageValue = String.format("%.2f", Double.parseDouble(percentVal.substring(0)));
				} else {
					percentageValue = ("+" + String.format("%.2f", Double.parseDouble(percentVal)));
				}
				investmentJSON.put(TemenosConstants.PERFORMANCEPERCENTAGE, percentageValue);
				if (TemenosConstants.YTD.equalsIgnoreCase(perfMetric)) {
					investmentJSON.put(TemenosConstants.PERFORMANCEPL, TemenosConstants.YTD);
				} else {
					investmentJSON.put(TemenosConstants.PERFORMANCEPL, TemenosConstants.OneY);
				}
			}
		}

		return investmentJSON;
	}

	public static JSONObject getPerformanceForPortfolio(JSONArray pmdArray, String perfMetric, int year, String currDate) {
		JSONObject portfolioJSON = new JSONObject();
		ArrayList<HashMap<String, String>> al = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> hMap = new HashMap<String, String>();
		int count = 0;
		double totalpercent = 0;
		String percentageValue = "";
		for (int i = 0; i <= pmdArray.length() - 1; i++) {
			JSONObject bodyJSON = pmdArray.getJSONObject(i);
			String periodVal = bodyJSON.getString("PERIOD_DISPLAY");
			String percentVal = bodyJSON.getString("PERIOD_RET_TWR");
			String timeVal = bodyJSON.getString("PERIOD_FINAL_DATE");
			if (periodVal.equalsIgnoreCase(String.valueOf(year))) {
				hMap.put("PERIOD_DISPLAY", periodVal);
				hMap.put("PERIOD_RET_TWR", percentVal);
				hMap.put("PERIOD_FINAL_DATE", timeVal);
				al.add(hMap);
			}
		}
		if (perfMetric != null && (perfMetric.equalsIgnoreCase(TemenosConstants.YTD)
				|| perfMetric.equalsIgnoreCase(TemenosConstants.OneY))) {
			for (int i = 0; i <= pmdArray.length() - 1; i++) {
				JSONObject bodyJSONPerf = pmdArray.getJSONObject(i);
				String periodVal = bodyJSONPerf.getString("PERIOD_DISPLAY");
				String percentVal = bodyJSONPerf.getString("PERIOD_RET_TWR");
				String timeVal = bodyJSONPerf.getString("PERIOD_FINAL_DATE");
				if (pmdArray.length() == 1) {
					if (periodVal.equalsIgnoreCase(String.valueOf(year))) {
						if(percentVal.substring(0,1) == "-" || Double.parseDouble(percentVal) < 0.00) {
							percentageValue = String.format("%.2f", Double.parseDouble(percentVal));
						} else {
							percentageValue = ("+" + String.format("%.2f", Double.parseDouble(percentVal)));
						}
						portfolioJSON.put(TemenosConstants.PERFORMANCEPERCENTAGE, percentageValue);
						if (perfMetric.equalsIgnoreCase(TemenosConstants.YTD)) {
							portfolioJSON.put(TemenosConstants.PERFORMANCEPL, TemenosConstants.YTD);
						} else {
							portfolioJSON.put(TemenosConstants.PERFORMANCEPL, TemenosConstants.OneY);
						}
					}}else {
						if (periodVal.equalsIgnoreCase(String.valueOf(year))) {
							if(timeVal.equalsIgnoreCase(currDate) && (percentVal.equalsIgnoreCase("0") || percentVal.equalsIgnoreCase("0.00") || percentVal.equalsIgnoreCase("-0.00"))) {
								portfolioJSON.put(TemenosConstants.PERFORMANCEPERCENTAGE, "+0.00");
								if(perfMetric.equalsIgnoreCase(TemenosConstants.YTD)) {
									portfolioJSON.put(TemenosConstants.PERFORMANCEPL, TemenosConstants.YTD);
								}else {
									portfolioJSON.put(TemenosConstants.PERFORMANCEPL, TemenosConstants.OneY);
								}
							}else if(timeVal.equalsIgnoreCase(currDate) && (!percentVal.equalsIgnoreCase("0") || !percentVal.equalsIgnoreCase("0.00") || percentVal.equalsIgnoreCase("-0.00"))) {
								for (int j = 0; j < al.size(); j++) {
									if(al.get(j).get("PERIOD_DISPLAY").equalsIgnoreCase(String.valueOf(year)) && (!al.get(j).get("PERIOD_RET_TWR").equals("0") || !al.get(j).get("PERIOD_RET_TWR").equals("0.00") || !al.get(j).get("PERIOD_RET_TWR").equals("-0.00"))) {
										totalpercent = totalpercent + ((Double.parseDouble(al.get(j).get("PERIOD_RET_TWR")) != 0 || Double.parseDouble(al.get(j).get("PERIOD_RET_TWR")) != 0.00 || Double.parseDouble(al.get(j).get("PERIOD_RET_TWR")) != -0.00) ? Double.parseDouble(al.get(j).get("PERIOD_RET_TWR")) : 0.00);
										count++;
									}
								}
								percentageValue = totalpercent >=0 ? ("+" + String.format("%.2f", (totalpercent/count))) : (String.format("%.2f", (totalpercent/count)));
								portfolioJSON.put(TemenosConstants.PERFORMANCEPERCENTAGE, percentageValue);
								if(perfMetric.equalsIgnoreCase(TemenosConstants.YTD)) {
									portfolioJSON.put(TemenosConstants.PERFORMANCEPL, TemenosConstants.YTD);
								}else {
									portfolioJSON.put(TemenosConstants.PERFORMANCEPL, TemenosConstants.OneY);
								}}}}
		}}
		return portfolioJSON;

	}
}
