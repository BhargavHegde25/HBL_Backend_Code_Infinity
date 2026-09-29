package com.temenos.infinity.wealth.tap.processor.post;

/**
 * (INFO) If status is set as a part of the request , the operation is exited
 * else operation is executed.
 * 
 * @author shreya.singh
 *
 */

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
import com.temenos.infinity.api.wealth.config.PortfolioWealthAPIServices;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

public class GetRecommendedInstrIPPostProcessor implements DataPostProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {		
			diagnostic.prepareDebug("==========> GetRecommendedInstrIPPostProcessor TAP - Entered ").log();
			JSONObject recommendJSON = new JSONObject();
			JSONObject recommendResN = new JSONObject();
			Dataset bodyData = result.getDatasetById("body");
			JSONArray recommendArr = new JSONArray();
			String healthStatus = "", healthMessage = "";
			
			if(bodyData != null) {
				JSONArray recommended = ResultToJSON.convertDataset(bodyData);
				diagnostic.prepareDebug("==========> GetRecommendedInstrIPPostProcessor TAP - No. of records returned initially: " + recommended.length() ).log();
				for (int i = 0; i < recommended.length(); i++) {
					JSONObject recomJSON = new JSONObject();
					JSONObject recommendedJSON = recommended.getJSONObject(i);
					if(recommendedJSON.has("recomNatE")) {
						healthStatus = "1" ;
						healthMessage = "Some issues with your portfolio health";
						if(recommendedJSON.has("valueN")) {
							String recommendation =  recommendedJSON.getString("recomNatL");
							String instrumentName =  recommendedJSON.getString("instrumentName");
							String value = recommendedJSON.getString("valueN").concat("%");
							recomJSON.put(TemenosConstants.INSTRUMENTNAME,instrumentName);
							String instDet = recommendation.concat(" recommendation for ").concat(instrumentName).concat(" for a max weight of ").concat(value);
							recomJSON.put(TemenosConstants.INSTRUMENTDETAILS,instDet);
						}
						else {
							String recommendation =  recommendedJSON.getString("recomNatL");
							String instrumentName =  recommendedJSON.getString("instrumentName");
							recomJSON.put(TemenosConstants.INSTRUMENTNAME,instrumentName);
							String instDet = recommendation.concat(" recommendation for ").concat(instrumentName);
							recomJSON.put(TemenosConstants.INSTRUMENTDETAILS,instDet);
						}
					} 
					else {
						if(healthStatus.equals("1")) {
						}
						else {
							healthStatus = "0";	
							healthMessage = "No issues";
						}
						continue;
					}
					recommendArr.put(recomJSON);
					
				}
				recommendJSON.put("recommendedInstrumentStatus", healthStatus);
				recommendJSON.put("recommendedInstrumentComment", healthMessage);
				recommendJSON.put("recommendedInstrumentDetails", recommendArr);
				recommendResN.put("recommendedDetails", recommendJSON);
				
			} 
			else {
				recommendResN.put("recommendId", "");
				diagnostic.prepareDebug("==========> GetRecommendedInstrIPPostProcessor TAP - No records ").log();
			}
			Result recommendRes = Utilities.constructResultFromJSONObject(recommendResN);
			recommendRes.addOpstatusParam("0");
			recommendRes.addHttpStatusCodeParam("200");
			recommendRes.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			recommendRes.addParam("portfolioID",request.getParameter("portfolioId"));
			diagnostic.prepareDebug("==========> GetRecommendedInstrIPPostProcessor TAP - Exited ").log();
			return recommendRes;
			
		} catch (Exception e) {
			alert.prepareError("==========> GetRecommendedInstrIPPostProcessor TAP - Error: " + e.getMessage()).log();
		}
		return response;
	}


}
