package com.temenos.infinity.api.wealth.tap.preandpostprocessors;

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

public class GetPortfolioHealthPostProcessor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		Dataset bodySet = result.getDatasetById("body");
		JSONObject healthObj = new JSONObject();
		JSONArray healthArray = new JSONArray();
		JSONObject portfolioHealth = new JSONObject();
		Record bodyRec = bodySet.getRecord(0);
		// String Portfolio = (String) bodyObj.get(TemenosConstants.PORTFOLIOID);
		// String portfolioId = request.getParameterValues(TemenosConstants.PORTFOLIOID)[0];
		if (bodyRec != null) {

			JSONObject bodyObj = ResultToJSON.convertRecord(bodyRec);
			String assetVal = bodyObj.get("AllObjectiveComplianceE").toString();
			String investmentVal = bodyObj.get("AllConstraintComplianceE").toString();
			String riskVal = bodyObj.get("MainRiskComplianceE").toString();
			
			int assetInt = Integer.parseInt(assetVal);
			int investmentInt = Integer.parseInt(investmentVal);
			int riskInt = Integer.parseInt(riskVal);
			
			if(assetInt < 3) {
			healthObj.put(TemenosConstants.HEALTHSTATUS, "0");
			healthObj.put(TemenosConstants.HEALTHPARAMETER, "Asset Allocation");
			healthArray.put(healthObj);
			} else {
				healthObj.put(TemenosConstants.HEALTHSTATUS, "1");
				healthObj.put(TemenosConstants.HEALTHPARAMETER, "Asset Allocation");
				healthArray.put(healthObj);
			}
			
			if (investmentInt < 3 ) {
				healthObj = new JSONObject();
				healthObj.put(TemenosConstants.HEALTHSTATUS, "0");
				healthObj.put(TemenosConstants.HEALTHPARAMETER, "Investment Constraints");
				healthArray.put(healthObj);
				} else {
					healthObj = new JSONObject();
					healthObj.put(TemenosConstants.HEALTHSTATUS, "1");
					healthObj.put(TemenosConstants.HEALTHPARAMETER, "Investment Constraints");
					healthArray.put(healthObj);
				}
			
			if (riskInt < 3 ) {
			healthObj = new JSONObject();
			healthObj.put(TemenosConstants.HEALTHSTATUS, "0");
			healthObj.put(TemenosConstants.HEALTHPARAMETER, "Risk Analysis");
			healthArray.put(healthObj);
			} else {
				healthObj = new JSONObject();
				healthObj.put(TemenosConstants.HEALTHSTATUS, "1");
				healthObj.put(TemenosConstants.HEALTHPARAMETER, "Risk Analysis");
				healthArray.put(healthObj);
			}
			portfolioHealth.put(TemenosConstants.PORTFOLIOHEALTH, healthArray);

			// portfolioHealth.put("PortfolioID", bodyObj.get("Portfolio"));
			portfolioHealth.put(TemenosConstants.STATUS, TemenosConstants.SUCCESS);	
			portfolioHealth.put("portfolioID", request.getParameter("portfolioId"));

		}

		Result healthResult = Utilities.constructResultFromJSONObject(portfolioHealth);
		healthResult.addOpstatusParam("0");
		healthResult.addHttpStatusCodeParam("200");
		healthResult.addParam("portfolioId", TemenosConstants.PORTFOLIOID);
		healthResult.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		return healthResult;
	}

}
