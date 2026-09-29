/**
 * 
 */
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

/**
 * @author himaja.sridhar
 *
 */
public class GetInvestmentConstraintsHCPostProcessor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		Dataset bodySet = result.getDatasetById("body");
		JSONArray constraint = new JSONArray();
		if (bodySet != null) {
		JSONArray bodyArr = ResultToJSON.convertDataset(bodySet);
		String portfolioCode = "";
		boolean flag = false;
		for (int i = 0; i < bodyArr.length(); i++) {
			JSONObject bodyObj = bodyArr.getJSONObject(i);
			portfolioCode = bodyObj.getString("PORTFOLIO_CODE");
			String modelConst = bodyObj.getString("MODELL_CONSTR_FILTER");
			String allocConst = bodyObj.getString("ALLOC_CONSTR_FILTER");
			String minVal = bodyObj.has("MIN_OBJ") ? bodyObj.getString("MIN_OBJ") : "0";
			String maxVal = bodyObj.has("MAX_OBJ") ? bodyObj.getString("MAX_OBJ") : "0";
			String instrumentName = bodyObj.has("INSTR_DENOM") ? bodyObj.getString("INSTR_DENOM") : "";
			String actualPos = bodyObj.has("CONSTR_ACTUAL_POSITION") ? bodyObj.getString("CONSTR_ACTUAL_POSITION")
					: "0";
			if (modelConst == "true") {
				if (allocConst == "true") {
					JSONObject constraints = new JSONObject();
					String marketSeg = bodyObj.getString("MARKET_SEGMENT");
					String details = "Market Segment ".concat(marketSeg).concat(" has a current weight of ")
							.concat(actualPos).concat("%").concat(" having constraint of minimum ").concat(minVal).concat("%");
					if (maxVal == "0") {
						details = details.concat(".");
					} else {
						details = details.concat(" and maximum ").concat(maxVal).concat("%").concat(".");
					}
					constraints.put("investmentConstraintDetails", details);
					constraint.put(constraints);
				}
				String secInConst = bodyObj.getString("SECU_IN_CONSTR_FILTER");
				if (secInConst == "true") {
					JSONObject constraints = new JSONObject();
					String valType = bodyObj.getString("POSITION_TYPE");
					String details = "Instrument ".concat(instrumentName).concat(" has a current value of ")
							.concat(actualPos).concat(valType).concat(" having constraint of minimum ").concat(minVal)
							.concat(valType);
					if (maxVal == "0") {
						details = details.concat(".");
					} else {
						details = details.concat(" and maximum ").concat(maxVal).concat(valType).concat(".");
					}
					constraints.put("investmentConstraintDetails", details);
					constraint.put(constraints);
				}
			}
				String secOutConst = bodyObj.getString("SECU_OUT_CONSTR_FILTER");
				if (secOutConst == "true") {
					JSONObject constraints = new JSONObject();
					String minValSecOut = bodyObj.has("MIN_OBJ_SEC_OUT") ? bodyObj.getString("MIN_OBJ_SEC_OUT") : "0";
					String remPos = bodyObj.getString("REMAINING_POSITION");
					String details = "Instrument ".concat(instrumentName).concat(" has a current position of ")
							.concat(actualPos).concat(" qty and has a minimum ").concat(minValSecOut)
							.concat(" qty excluded from position for rebalancing with ").concat(remPos)
							.concat("  qty remaining .");
					constraints.put("investmentConstraintDetails", details);
					constraint.put(constraints);
				}
				String holdConst = bodyObj.getString("CONSTR_SET_FILTER");
				if (holdConst == "true") {
					JSONObject constraints = new JSONObject();
					String holdConstSeg = bodyObj.getString("CONSTR_LABEL_PART_1");
					String details = "Position ".concat(holdConstSeg);
					constraints.put("investmentConstraintDetails", details);
					constraint.put(constraints);
				}
			}
	
		JSONObject constraintObj = new JSONObject();
		constraintObj.put("investmentConstraintDetails", constraint);
		Result performanceRes = Utilities.constructResultFromJSONObject(constraintObj);
		performanceRes.addOpstatusParam("0");
		performanceRes.addHttpStatusCodeParam("200");
		performanceRes.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		performanceRes.addParam("portfolioID", portfolioCode);
		
		
		return performanceRes;
		}
		JSONObject constraintObj = new JSONObject();
		constraintObj.put("investmentConstraintDetails", constraint);
		Result performanceRes = Utilities.constructResultFromJSONObject(constraintObj);
		performanceRes.addOpstatusParam("0");
		performanceRes.addHttpStatusCodeParam("200");
		performanceRes.addParam("portfolioId", TemenosConstants.PORTFOLIOID);
		performanceRes.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		//performanceRes.addParam("investmentConstraintComment", "No issues");
		//performanceRes.addParam("investmentConstraintStatus", "0");
		return performanceRes;
	}
	
}
