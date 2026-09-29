/**
 * 
 */
package com.temenos.infinity.wealth.tap.processor.post;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * @author himaja.sridhar
 *
 */
public class GetPerformanceTAPPostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@SuppressWarnings("unused")
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {
		diagnostic.prepareDebug("==========> GetPerformanceTAPPostProcessor TAP - Entered ").log();
		JSONObject portfolioPerf = new JSONObject();
		JSONObject performanceList = new JSONObject();
		JSONArray portfolioPerfArr = new JSONArray();
		JSONArray sortedJSON = new JSONArray();
		String performanceStr = "";

		Record header = result.getRecordById("header");
		Dataset body = result.getDatasetById("body");
		if(body != null) {
		
		JSONArray bodyArr = ResultToJSON.convertDataset(body);
		diagnostic.prepareDebug("==========> GetPerformanceTAPPostProcessor TAP - No. of records returned initially: " + bodyArr.length() ).log();
		if (bodyArr.length() == 1) {
			diagnostic.prepareDebug("==========> GetPerformanceTAPPostProcessor TAP - Only one record returned").log();
			JSONObject graphJSON = new JSONObject();
			JSONObject bodyJSON = bodyArr.getJSONObject(0);
			portfolioPerfArr.put(graphPoint(bodyJSON));
		}
		for (int i = 0; i < bodyArr.length() - 1; i++) {
			JSONObject bodyJSON = bodyArr.getJSONObject(i);
			portfolioPerfArr.put(graphPoint(bodyJSON));
		}

		JSONObject lastObj = bodyArr.getJSONObject(bodyArr.length() - 1);

		performanceList.put(TemenosConstants.NET_DEPOSIT, Double.parseDouble(lastObj.get("PERIOD_INVEST_WITHDRAWAL").toString()));
		performanceList.put(TemenosConstants.INITIAL_VALUE, lastObj.get("PERIOD_INITIAL_MKT_VAL").toString());
		performanceList.put(TemenosConstants.CURRENT_VAL, lastObj.get("PERIOD_FINAL_MKT_VAL").toString());
		performanceList.put(TemenosConstants.FEES_TAX, lastObj.get("PERIOD_FEE_TAX").toString());
		performanceList.put(TemenosConstants.PL, lastObj.get("PERIOD_GAIN_LOSS").toString());
		performanceList.put(TemenosConstants.MONEY_WEIGHTED, lastObj.get("PERIOD_RET_MWR").toString());
		performanceList.put(TemenosConstants.TIME_WEIGHTED, lastObj.get("PERIOD_RET_TWR").toString());
		
		performanceStr= performanceList.toString();
		//portfolioPerf.put("performanceList", performanceList);
		portfolioPerf.put("performanceList", performanceStr);
		
		
		portfolioPerf.put(TemenosConstants.REFERENCECURRENCY, lastObj.get("REF_CURRENCY").toString());
		portfolioPerf.put("portfolioID", lastObj.get("PORTFOLIO_CODE").toString());
		portfolioPerf.put("monthlyOverview", portfolioPerfArr);
		}
		else {
			portfolioPerf.put("performanceList", performanceStr);
			//portfolioPerf.put("performanceList", performanceList);
			portfolioPerf.put("monthlyOverview", portfolioPerfArr);
		}
		Result performanceRes = Utilities.constructResultFromJSONObject(portfolioPerf);
		performanceRes.addOpstatusParam("0");
		performanceRes.addHttpStatusCodeParam("200");
		performanceRes.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		diagnostic.prepareDebug("==========> GetPerformanceTAPPostProcessor TAP - Exited ").log();
		return performanceRes;
		

	} catch (Exception e) {
		alert.prepareError("==========> GetPerformanceTAPPostProcessor TAP - Error: " + e.getMessage()).log();
		return null;
	}
}

public JSONObject graphPoint(JSONObject bodyJSON) {
	JSONObject graphJSON = new JSONObject();
	Double portPer = Double.parseDouble(bodyJSON.getString("PTF_PERF_CUMUL"));
	graphJSON.put(TemenosConstants.PORTFOLIORETURN, bodyJSON.getString("PERIOD_FINAL_MKT_VAL"));
	graphJSON.put(TemenosConstants.DATE_TIME, bodyJSON.getString("PERIOD_FINAL_DATE"));
	graphJSON.put("PERIOD_DISPLAY", bodyJSON.getString("PERIOD_DISPLAY"));
	graphJSON.put("PERIOD_INITIAL_DATE", bodyJSON.getString("PERIOD_INITIAL_DATE"));
	graphJSON.put(TemenosConstants.PERCENTAGECHANGE, String.format("%.2f", portPer));
	return graphJSON;

}

}
