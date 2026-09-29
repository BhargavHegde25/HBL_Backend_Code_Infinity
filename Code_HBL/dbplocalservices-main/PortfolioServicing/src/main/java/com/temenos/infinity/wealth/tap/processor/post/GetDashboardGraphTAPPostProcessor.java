package com.temenos.infinity.wealth.tap.processor.post;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealth.util.WealthMockUtil;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.wealth.common.util.PerformanceDisplayUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

/**
 * @author muthukumarv & Sarah 
 *
 */
public class GetDashboardGraphTAPPostProcessor implements DataPostProcessor2 {

	WealthMockUtil wealthMockUtil = new WealthMockUtil();
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {
			diagnostic.prepareDebug("==========> GetDashboardGraphTAPPostProcessor TAP - Entered ").log();
			boolean isAlreadyLoaded = true;
			JSONArray graphArr = new JSONArray();
			JSONObject investmentJSON = new JSONObject();
			Dataset body = result.getDatasetById("body");

			if (null == request.getParameter(TemenosConstants.GRAPHDURATION)
					|| request.getParameter(TemenosConstants.GRAPHDURATION).isEmpty()) {
				isAlreadyLoaded = false;
			}

			Double PERIOD_FINAL_MKT_VAL = null, PERIOD_INITIAL_MKT_VAL = null, PERIOD_GAIN_LOSS = null;
			String PERIOD_FINAL_DATE = null, PERIOD_INTIAL_DATE = null;
			Double DIFF_PERF = null;

			Set<Object> sets = new HashSet<>();
			int count = 0;

			if (body != null) {
				JSONArray bodyArr = ResultToJSON.convertDataset(body);
				diagnostic.prepareDebug(
						"==========> GetDashboardGraphTAPPostProcessor TAP - No. of records returned initially: "
								+ bodyArr.length())
						.log();
				for (int i = 0; i <= bodyArr.length() - 1; i++) {
					if (!isAlreadyLoaded) {
						String perfMetric = (EnvironmentConfigurationsHandler
								.getValue(TemenosConstants.INF_WLTH_PRFM_METRIC, request));
						investmentJSON = PerformanceDisplayUtils.getPerformanceForDashboard(bodyArr, perfMetric, Calendar.getInstance().get(Calendar.YEAR));
						isAlreadyLoaded = true;
					}

					JSONObject bodyJSON = bodyArr.getJSONObject(i);
					String periodVal = bodyJSON.getString("PERIOD_DISPLAY");

					if (sets.add(periodVal)) {
						count++;
						if (count == 1) {
							PERIOD_INITIAL_MKT_VAL = Double.parseDouble(bodyJSON.getString("PERIOD_INITIAL_MKT_VAL"));
							PERIOD_INTIAL_DATE = bodyJSON.getString("PERIOD_INITIAL_DATE");
							DIFF_PERF = Double.parseDouble(bodyJSON.getString("DIFF_PERF").toString());
							PERIOD_GAIN_LOSS = Double.parseDouble(bodyJSON.getString("PERIOD_GAIN_LOSS").toString());
						} else {
							count++;
						}

						if (count == 3) {
							graphArr.put(graphInitialPoint(PERIOD_INTIAL_DATE, PERIOD_INITIAL_MKT_VAL, DIFF_PERF));
							graphArr.put(graphFinalPoint(PERIOD_FINAL_DATE, PERIOD_FINAL_MKT_VAL, DIFF_PERF));

						} else if (count > 3) {
							// For last record of the array
							if (i == bodyArr.length() - 1) {
								PERIOD_FINAL_MKT_VAL = Double.parseDouble(bodyJSON.getString("PERIOD_FINAL_MKT_VAL"));
								DIFF_PERF = Double.parseDouble(bodyJSON.getString("DIFF_PERF").toString());
								PERIOD_GAIN_LOSS = Double
										.parseDouble(bodyJSON.getString("PERIOD_GAIN_LOSS").toString());
								PERIOD_FINAL_DATE = bodyJSON.getString("PERIOD_FINAL_DATE");
								graphArr.put(
										graphFinalValue(bodyJSON, PERIOD_FINAL_MKT_VAL, DIFF_PERF, PERIOD_GAIN_LOSS));
							} else {
								graphArr.put(graphFinalPoint(PERIOD_FINAL_DATE, PERIOD_FINAL_MKT_VAL, DIFF_PERF));
							}
						}
						PERIOD_FINAL_MKT_VAL = Double.parseDouble(bodyJSON.getString("PERIOD_FINAL_MKT_VAL"));
						DIFF_PERF = Double.parseDouble(bodyJSON.getString("DIFF_PERF").toString());
						PERIOD_GAIN_LOSS = Double.parseDouble(bodyJSON.getString("PERIOD_GAIN_LOSS").toString());
						PERIOD_FINAL_DATE = bodyJSON.getString("PERIOD_FINAL_DATE");

						// If only one record is returned
						if (count == 1 && i == bodyArr.length() - 1) {
							graphArr.put(graphInitialPoint(PERIOD_INTIAL_DATE, PERIOD_INITIAL_MKT_VAL, DIFF_PERF));
							graphArr.put(graphFinalValue(bodyJSON, PERIOD_FINAL_MKT_VAL, DIFF_PERF, PERIOD_GAIN_LOSS));
						}

					} else {

						PERIOD_FINAL_MKT_VAL = PERIOD_FINAL_MKT_VAL
								+ Double.parseDouble(bodyJSON.getString("PERIOD_FINAL_MKT_VAL"));
						DIFF_PERF = DIFF_PERF + Double.parseDouble(bodyJSON.getString("DIFF_PERF").toString());
						PERIOD_GAIN_LOSS = PERIOD_GAIN_LOSS
								+ Double.parseDouble(bodyJSON.getString("PERIOD_GAIN_LOSS").toString());
						PERIOD_FINAL_DATE = bodyJSON.getString("PERIOD_FINAL_DATE");

						if (count == 1) {
							PERIOD_INITIAL_MKT_VAL = PERIOD_INITIAL_MKT_VAL
									+ Double.parseDouble(bodyJSON.getString("PERIOD_INITIAL_MKT_VAL"));
						}

						// For last record of the array
						if (i == bodyArr.length() - 1 && count > 1) {
							graphArr.put(graphFinalValue(bodyJSON, PERIOD_FINAL_MKT_VAL, DIFF_PERF, PERIOD_GAIN_LOSS));
						}

						// For last record of the array returning only one month
						if (count == 1 && i == bodyArr.length() - 1) {
							graphArr.put(graphInitialPoint(PERIOD_INTIAL_DATE, PERIOD_INITIAL_MKT_VAL, DIFF_PERF));
							graphArr.put(graphFinalValue(bodyJSON, PERIOD_FINAL_MKT_VAL, DIFF_PERF, PERIOD_GAIN_LOSS));

						}
					}
				}
				diagnostic.prepareDebug("==========> GetDashboardGraphTAPPostProcessor TAP - No. of records for graph: "
						+ graphArr.length()).log();

				JSONObject lastObj = graphArr.getJSONObject(graphArr.length() - 1);

				String unRealizedPLAmt = lastObj.get("PERIOD_GAIN_LOSS").toString();
				String unRealizedPL = Double.parseDouble(unRealizedPLAmt) >= 0 ? "P" : "L";

				investmentJSON.put(TemenosConstants.REFERENCECURRENCY, lastObj.get("REF_CURRENCY").toString());
				investmentJSON.put(TemenosConstants.MARKETVALUE, lastObj.get("AMOUNT").toString());
				investmentJSON.put(TemenosConstants.UNREALIZEDPLAMOUNT,
						Math.abs(Double.parseDouble(lastObj.get("PERIOD_GAIN_LOSS").toString())));
				investmentJSON.put(TemenosConstants.UNREALIZEDPL, unRealizedPL);
				investmentJSON.put(TemenosConstants.UNREALIZEDPLPERCENTAGE,
						Math.abs(Double.parseDouble(lastObj.get("PERCENTAGE").toString())));
				investmentJSON.put("todayPLAmount", "");
				investmentJSON.put("todayPLPercentage", "");
				investmentJSON.put("todayPL", "");
				diagnostic.prepareDebug("==========> GetDashboardGraphTAPPostProcessor TAP - Response JSON Built")
						.log();
				if (graphArr.length() == 2) {
					diagnostic.prepareDebug(
							"==========> GetDashboardGraphTAPPostProcessor TAP - Array Length: " + graphArr.length())
							.log();
					graphArr.getJSONObject(graphArr.length() - 1).remove("REF_CURRENCY");
					graphArr.getJSONObject(graphArr.length() - 1).remove("PERIOD_GAIN_LOSS");
				} else {
					// graphArr.remove(graphArr.length() - 1);
				}
			} else {
				graphArr = new JSONArray();
				investmentJSON.put(TemenosConstants.REFERENCECURRENCY, "");
				investmentJSON.put(TemenosConstants.MARKETVALUE, "");
				investmentJSON.put(TemenosConstants.UNREALIZEDPLAMOUNT, "");
				investmentJSON.put(TemenosConstants.UNREALIZEDPL, "");
				investmentJSON.put(TemenosConstants.UNREALIZEDPLPERCENTAGE, "");
				investmentJSON.put("todayPLAmount", "");
				investmentJSON.put("todayPLPercentage", "");
				investmentJSON.put("todayPL", "");
				investmentJSON.put(TemenosConstants.PERFORMANCEPERCENTAGE, "");
				investmentJSON.put(TemenosConstants.PERFORMANCEPL, "");
			}
			investmentJSON.put(TemenosConstants.GRAPHDURATION, graphArr);

			Result performanceRes = Utilities.constructResultFromJSONObject(investmentJSON);
			performanceRes.addOpstatusParam("0");
			performanceRes.addHttpStatusCodeParam("200");
			performanceRes.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			diagnostic.prepareDebug("==========> GetDashboardGraphTAPPostProcessor TAP - Exited ").log();
			return performanceRes;

		} catch (Exception e) {
			alert.prepareError("==========> GetDashboardGraphTAPPostProcessor TAP - Error: " + e.getMessage()).log();
			return null;
		}

	}

	public JSONObject graphFinalValue(JSONObject bodyJSON, Double pERIOD_FINAL_MKT_VAL, Double dIFF_PERF,
			Double pERIOD_GAIN_LOSS) {
		JSONObject graphJSON = new JSONObject();
		graphJSON.put("TIMESTAMP", bodyJSON.getString("PERIOD_FINAL_DATE"));
		graphJSON.put("AMOUNT", String.format("%.2f", pERIOD_FINAL_MKT_VAL));
		graphJSON.put("PERCENTAGE", String.format("%.2f", dIFF_PERF));
		graphJSON.put("REF_CURRENCY", bodyJSON.getString("REF_CURRENCY"));
		graphJSON.put("PERIOD_GAIN_LOSS", String.format("%.2f", pERIOD_GAIN_LOSS));
		if(graphJSON.getString("PERCENTAGE").equalsIgnoreCase("-0.00")){
			graphJSON.put("PERCENTAGE", "0.00");
		}
		return graphJSON;
	}

	public JSONObject graphInitialPoint(String pERIOD_INTIAL_DATE, Double pERIOD_INITIAL_MKT_VAL, Double dIFF_PERF) {
		JSONObject graphJSON = new JSONObject();

		graphJSON.put("TIMESTAMP", pERIOD_INTIAL_DATE);
		graphJSON.put("AMOUNT", String.format("%.2f", pERIOD_INITIAL_MKT_VAL));
		graphJSON.put("PERCENTAGE", String.format("%.2f", dIFF_PERF));
		if(graphJSON.getString("PERCENTAGE").equalsIgnoreCase("-0.00")){
			graphJSON.put("PERCENTAGE", "0.00");
		}
		return graphJSON;

	}

	public JSONObject graphFinalPoint(String pERIOD_FINAL_DATE, Double pERIOD_FINAL_MKT_VAL, Double dIFF_PERF) {
		JSONObject graphJSON = new JSONObject();

		graphJSON.put("TIMESTAMP", pERIOD_FINAL_DATE);
		graphJSON.put("AMOUNT", String.format("%.2f", pERIOD_FINAL_MKT_VAL));
		graphJSON.put("PERCENTAGE", String.format("%.2f", dIFF_PERF));
		if(graphJSON.getString("PERCENTAGE").equalsIgnoreCase("-0.00")){
			graphJSON.put("PERCENTAGE", "0.00");
		}
		return graphJSON;

	}
}
