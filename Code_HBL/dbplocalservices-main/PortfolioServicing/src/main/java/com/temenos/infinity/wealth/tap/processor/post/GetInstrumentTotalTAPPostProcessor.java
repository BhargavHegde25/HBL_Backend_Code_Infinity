/**
 * 
 */
package com.temenos.infinity.wealth.tap.processor.post;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * @author himaja.sridhar
 *
 */
public class GetInstrumentTotalTAPPostProcessor implements DataPostProcessor2 {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		diagnostic.prepareDebug("==========> GetInstrumentTotalTAPPostProcessor TAP - Entered ").log();
		String graphDuration = "";
		if (request.getParameter(TemenosConstants.GRAPHDURATION) == null) {
			graphDuration = "OneM";
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
			diagnostic.prepareDebug("==========> GetInstrumentTotalTAPPostProcessor TAP - No. of records returned initially: " + bodyArr.length() ).log();
			if (bodyArr.length() == 1) {
				JSONObject bodyJSON = bodyArr.getJSONObject(0);
				graphArr.put(graphPoint(bodyJSON, "1"));
				graphArr.put(graphPoint(bodyJSON, "2"));

			} else {
				for (int i = 0; i < bodyArr.length() - 1; i++) {
					JSONObject bodyJSON = bodyArr.getJSONObject(i);
					if (i == 0) {
						graphArr.put(graphPoint(bodyJSON, "1"));
						graphArr.put(graphPoint(bodyJSON, "2"));

					} else {
						graphArr.put(graphPoint(bodyJSON, "2"));
					}

				}
			}
			portfolioJSON.put(graphDuration, graphArr);
			diagnostic.prepareDebug("==========> GetInstrumentTotalTAPPostProcessor TAP - No. of graph points returned: " + graphArr.length() ).log();
			JSONObject lastObj = bodyArr.getJSONObject(bodyArr.length() - 1);

			String unRealizedPLAmt = lastObj.get("PERIOD_GAIN_LOSS").toString();
			
			portfolioJSON.put(TemenosConstants.REFERENCECURRENCY, lastObj.get("REF_CURRENCY").toString());
			portfolioJSON.put(TemenosConstants.ACCOUNTNAME, lastObj.get("PORTFOLIO_NAME").toString());
			portfolioJSON.put(TemenosConstants.ACCOUNTID, lastObj.get("PORTFOLIO_CODE").toString());
			portfolioJSON.put("portfolioID", lastObj.get("PORTFOLIO_CODE").toString());
			portfolioJSON.put(TemenosConstants.MARKETVALUE, lastObj.get("PERIOD_FINAL_MKT_VAL").toString());
			portfolioJSON.put(TemenosConstants.UNREALIZEDPLAMOUNT, String.valueOf(Math.abs(Double.parseDouble(unRealizedPLAmt))));
			portfolioJSON.put(TemenosConstants.GRAPHDURATION, graphDuration);

			/*
			Double diffPer = Double.parseDouble(lastObj.get("DIFF_PERF").toString()) ;
			if(diffPer == 0.0)
			{
				portfolioJSON.put(TemenosConstants.UNREALIZEDPLPERCENTAGE,"");
			}
			else {
			
			portfolioJSON.put(TemenosConstants.UNREALIZEDPLPERCENTAGE, String.format("%.2f", Math.abs(diffPer)));
			//portfolioJSON.put(TemenosConstants.UNREALIZEDPLPERCENTAGE, String.format("%.2f", diffPer));
			}*/
			portfolioJSON.put(TemenosConstants.UNREALIZEDPLPERCENTAGE,"");
			

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

		}
		portfolioJSON.put("todayPLAmount", "");
		portfolioJSON.put("todayPLPercentage", "");
		portfolioJSON.put("todayPL", "");

		totalArr.put(portfolioJSON);
		totalJSON.put("instrumentTotal", totalArr);
		diagnostic.prepareDebug("==========> GetInstrumentTotalTAPPostProcessor TAP - Total records: " + totalArr.length() ).log();
		Result portfolioRes = Utilities.constructResultFromJSONObject(totalJSON);
		portfolioRes.addOpstatusParam("0");
		portfolioRes.addHttpStatusCodeParam("200");
		portfolioRes.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		diagnostic.prepareDebug("==========> GetInstrumentTotalTAPPostProcessor TAP - Exited ").log();
		return portfolioRes;
	}
	public JSONObject graphPoint(JSONObject bodyJSON, String dataCount) {
		JSONObject graphJSON = new JSONObject();
		if (dataCount.equals("1")) {
			graphJSON.put("AMOUNT", bodyJSON.getString("PERIOD_INITIAL_MKT_VAL"));
			graphJSON.put("TIMESTAMP", bodyJSON.getString("PERIOD_INITIAL_DATE"));
			Double percentage = Double.parseDouble(bodyJSON.getString("DIFF_PERF").toString()) ;
			graphJSON.put("PERCENTAGE", String.format("%.2f", percentage));
		} else {
			graphJSON.put("AMOUNT", bodyJSON.getString("PERIOD_FINAL_MKT_VAL"));
			graphJSON.put("TIMESTAMP", bodyJSON.getString("PERIOD_FINAL_DATE"));
			Double percentage = Double.parseDouble(bodyJSON.getString("DIFF_PERF").toString()) ;
			graphJSON.put("PERCENTAGE", String.format("%.2f", percentage));
		}

		return graphJSON;
	}

}
