
package com.temenos.infinity.api.wealth.tap.preandpostprocessors;

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
import com.temenos.infinity.api.wealth.backenddelegate.impl.PortfolioPerformanceBackendDelegateImpl;
import com.temenos.infinity.api.wealth.config.PortfolioWealthAPIServices;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealth.util.WealthMockUtil;

/**
 * (INFO) Builds the result in desired format for the TAP service
 * 
 * @author himaja.sridhar
 *
 */
public class GetPortfolioPerformancePostProcessor implements DataPostProcessor2 {

	WealthMockUtil wealthMockUtil = new WealthMockUtil();
	PortfolioPerformanceBackendDelegateImpl portfolioPerformanceBackendDelegateImpl = new PortfolioPerformanceBackendDelegateImpl();
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("unused")
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {
			JSONObject portfolioPerf = new JSONObject();
			JSONObject performanceList = new JSONObject();
			JSONArray portfolioPerfArr = new JSONArray();
			JSONArray sortedJSON = new JSONArray();

			Record header = result.getRecordById("header");
			Dataset body = result.getDatasetById("body");
			if(body != null) {
			JSONArray bodyArr = ResultToJSON.convertDataset(body);

			if (bodyArr.length() == 1) {
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
			portfolioPerf.put("performanceList", performanceList);

			portfolioPerf.put(TemenosConstants.REFERENCECURRENCY, lastObj.get("REF_CURRENCY").toString());
			portfolioPerf.put("portfolioID", lastObj.get("PORTFOLIO_CODE").toString());
			portfolioPerf.put("monthlyOverview", portfolioPerfArr);
			}
			else {
				portfolioPerf.put("performanceList", performanceList);
				portfolioPerf.put("monthlyOverview", portfolioPerfArr);
			}
			Result performanceRes = Utilities.constructResultFromJSONObject(portfolioPerf);
			performanceRes.addOpstatusParam("0");
			performanceRes.addHttpStatusCodeParam("200");
			performanceRes.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			return performanceRes;
			

		} catch (Exception e) {
			alert.prepareError("Error while invoking GetPortfolioPerformancePostProcessor - "
					+ PortfolioWealthAPIServices.WEALTHSERVICESORCHESTRATION.getOperationName() + "  : " + e).log();
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
