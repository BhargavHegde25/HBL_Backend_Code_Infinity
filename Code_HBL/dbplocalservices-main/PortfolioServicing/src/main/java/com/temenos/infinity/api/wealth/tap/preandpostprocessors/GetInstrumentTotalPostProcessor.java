/**
 * 
 */
package com.temenos.infinity.api.wealth.tap.preandpostprocessors;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import java.util.Calendar;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.wealth.common.util.PerformanceDisplayUtils;

/**
 * (INFO) Builds the result in desired format for the TAP service
 * 
 * @author himaja.sridhar & Sarah
 *
 */
public class GetInstrumentTotalPostProcessor implements DataPostProcessor2 {
	private static final Logger LOG = LogManager.getLogger(GetInstrumentTotalPostProcessor.class);

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

		try {
			String graphDuration = "";
			boolean isAlreadyLoaded = true;
			String perfMetric = (EnvironmentConfigurationsHandler.getValue(TemenosConstants.INF_WLTH_PRFM_METRIC,
					request));
			if (null == request.getParameter(TemenosConstants.GRAPHDURATION)
					|| request.getParameter(TemenosConstants.GRAPHDURATION).isEmpty()) {
				if (TemenosConstants.OneY.equalsIgnoreCase(perfMetric)) {
					graphDuration = TemenosConstants.ONEYEAR;
				} else {
					graphDuration = perfMetric;
				}
				isAlreadyLoaded = false;
			} else {
				graphDuration = request.getParameter(TemenosConstants.GRAPHDURATION).toString();
			}
			JSONObject portfolioJSON = new JSONObject();
			JSONObject totalJSON = new JSONObject();
			JSONArray totalArr = new JSONArray();
			JSONArray graphArr = new JSONArray();

			Dataset body = result.getDatasetById("body");

			if (body != null) {
				JSONArray bodyArr = ResultToJSON.convertDataset(body);

				if (bodyArr.length() == 1) {

					JSONObject bodyJSON = bodyArr.getJSONObject(0);
					graphArr.put(graphPoint(bodyJSON, "1"));
					graphArr.put(graphPoint(bodyJSON, "2"));

				} else {
					for (int i = 0; i < bodyArr.length(); i++) {
						JSONObject bodyJSON = bodyArr.getJSONObject(i);
						if (i == 0) {
							graphArr.put(graphPoint(bodyJSON, "1"));
							graphArr.put(graphPoint(bodyJSON, "2"));

						} else {
							graphArr.put(graphPoint(bodyJSON, "2"));
						}
					}
				}
				
				SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
				String currDate = sdformat.format(Calendar.getInstance().getTime());

				if (!isAlreadyLoaded) {
					portfolioJSON = PerformanceDisplayUtils.getPerformanceForPortfolio(bodyArr, perfMetric, Calendar.getInstance().get(Calendar.YEAR), currDate);
				}
				
				int index = -1;

				for (int ndx = 0; ndx < graphArr.length(); ndx++) {
					JSONObject jsonObject = (JSONObject) graphArr.get(ndx);
					if (currDate.equalsIgnoreCase(jsonObject.getString("TIMESTAMP"))) {
						if (index > -1) {
							// Remove the object positioned at the index
							graphArr.remove(index);
						}

						index = ndx;
					}
				}
				portfolioJSON.put(graphDuration, graphArr);

				JSONObject lastObj = bodyArr.getJSONObject(bodyArr.length() - 1);

				String unRealizedPLAmt = lastObj.get("PERIOD_GAIN_LOSS").toString();

				portfolioJSON.put(TemenosConstants.REFERENCECURRENCY, lastObj.get("REF_CURRENCY").toString());
				portfolioJSON.put(TemenosConstants.ACCOUNTNAME, lastObj.get("PORTFOLIO_NAME").toString());
				portfolioJSON.put(TemenosConstants.ACCOUNTID, lastObj.get("PORTFOLIO_CODE").toString());
				portfolioJSON.put("portfolioID", lastObj.get("PORTFOLIO_CODE").toString());
				portfolioJSON.put(TemenosConstants.MARKETVALUE, lastObj.get("PERIOD_FINAL_MKT_VAL").toString());
				portfolioJSON.put(TemenosConstants.UNREALIZEDPLAMOUNT,
						String.valueOf(Math.abs(Double.parseDouble(unRealizedPLAmt))));
				portfolioJSON.put(TemenosConstants.GRAPHDURATION, graphDuration);

				portfolioJSON.put(TemenosConstants.UNREALIZEDPLPERCENTAGE, "");

				String unRealizedPL = Double.parseDouble(unRealizedPLAmt) >= 0 ? "P" : "L";
				portfolioJSON.put(TemenosConstants.UNREALIZEDPL, unRealizedPL);

			} else {
				portfolioJSON.put(graphDuration, graphArr);
				portfolioJSON.put(TemenosConstants.REFERENCECURRENCY, "");
				portfolioJSON.put(TemenosConstants.ACCOUNTNAME, "");
				portfolioJSON.put(TemenosConstants.ACCOUNTID, "");
				portfolioJSON.put("portfolioID", "");
				portfolioJSON.put(TemenosConstants.MARKETVALUE, "");
				portfolioJSON.put(TemenosConstants.UNREALIZEDPLAMOUNT, "");
				portfolioJSON.put(TemenosConstants.GRAPHDURATION, "");
				portfolioJSON.put(TemenosConstants.UNREALIZEDPLPERCENTAGE, "");
				portfolioJSON.put(TemenosConstants.UNREALIZEDPL, "");
				portfolioJSON.put(TemenosConstants.PERFORMANCEPERCENTAGE, "");
				portfolioJSON.put(TemenosConstants.PERFORMANCEPL, "");
			}
			portfolioJSON.put("todayPLAmount", "");
			portfolioJSON.put("todayPLPercentage", "");
			portfolioJSON.put("todayPL", "");

			totalArr.put(portfolioJSON);
			totalJSON.put("instrumentTotal", totalArr);
			Result portfolioRes = Utilities.constructResultFromJSONObject(totalJSON);
			portfolioRes.addOpstatusParam("0");
			portfolioRes.addHttpStatusCodeParam("200");
			portfolioRes.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			return portfolioRes;
		} catch (Exception e) {
			LOG.error("==========> GetInstrumentTotalPostProcessor TAP - Error: " + e.getMessage());
			e.getMessage();
		}
		return null;
	}

	public JSONObject graphPoint(JSONObject bodyJSON, String dataCount) {
		JSONObject graphJSON = new JSONObject();
		if (dataCount.equals("1")) {
			graphJSON.put("AMOUNT", bodyJSON.getString("PERIOD_INITIAL_MKT_VAL"));
			graphJSON.put("TIMESTAMP", bodyJSON.getString("PERIOD_INITIAL_DATE"));
			Double percentage = Double.parseDouble(bodyJSON.getString("DIFF_PERF").toString()) ;
//			if(bodyJSON.getString("DIFF_PERF").toString().substring(0,1) == "-" || percentage < 0.00) {
//				percentage = -Double.parseDouble(bodyJSON.getString("DIFF_PERF").toString().substring(1));
//			} else {
//				percentage = Double.parseDouble(bodyJSON.getString("DIFF_PERF").toString()) ;
//			}
			graphJSON.put("PERCENTAGE", String.format("%.2f", percentage));
		} else {
			graphJSON.put("AMOUNT", bodyJSON.getString("PERIOD_FINAL_MKT_VAL"));
			graphJSON.put("TIMESTAMP", bodyJSON.getString("PERIOD_FINAL_DATE"));
			Double percentage = Double.parseDouble(bodyJSON.getString("DIFF_PERF").toString()) ;
//			if(bodyJSON.getString("DIFF_PERF").toString().substring(0,1) == "-" || percentage < 0.00) {
//				percentage = -Double.parseDouble(bodyJSON.getString("DIFF_PERF").toString().substring(1));
//			} else {
//				percentage = Double.parseDouble(bodyJSON.getString("DIFF_PERF").toString()) ;
//			}
			graphJSON.put("PERCENTAGE", String.format("%.2f", percentage));
		}

		return graphJSON;
	}
}
