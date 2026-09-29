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
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * @author himaja.sridhar
 *
 */
public class GetActivePortfolioStrategyPostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {
		diagnostic.prepareDebug("==========> GetActivePortfolioStrategyPostProcessor TAP - Entered ").log();
		Dataset bodySet = result.getDatasetById("body");
		JSONObject linkJSON = new JSONObject();
		int idCnt=0;
		if(bodySet != null) {
		JSONArray bodyArr = ResultToJSON.convertDataset(bodySet);	
		diagnostic.prepareDebug("==========> GetActivePortfolioStrategyPostProcessor TAP - No. of records returned initially: " + bodyArr.length() ).log();
		String idVal = "";
		for(int i =0 ;i<bodyArr.length();i++) {
			JSONObject bodyObj = bodyArr.getJSONObject(i);
			if(!bodyObj.get("strategyNatureE").toString().equalsIgnoreCase("Investment profile"))
			{
				bodyArr.remove(i);
			}
			else {
				idCnt++;
				String id = bodyObj.getString("id");
				idVal = idVal.equals("") ? id : idVal.concat(",").concat(id);
			}
		}
		diagnostic.prepareDebug("==========> GetActivePortfolioStrategyPostProcessor TAP - No. of records after filter: " + idCnt).log();
		linkJSON.put("idCnt", idCnt);
		linkJSON.put("idVal", idVal);
		Result linkRes = Utilities.constructResultFromJSONObject(linkJSON);
		linkRes.addOpstatusParam("0");
		linkRes.addHttpStatusCodeParam("200");
		linkRes.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		diagnostic.prepareDebug("==========> GetActivePortfolioStrategyPostProcessor TAP - Exited ").log();
		return linkRes;
		}
		else {
			linkJSON.put("idCnt", idCnt);
			Result linkRes = Utilities.constructResultFromJSONObject(linkJSON);
			linkRes.addOpstatusParam("0");
			linkRes.addHttpStatusCodeParam("200");
			linkRes.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			diagnostic.prepareDebug("==========> GetActivePortfolioStrategyPostProcessor TAP - Exited ").log();
			return linkRes;
		}
		}
		catch (Exception e) {
			alert.prepareError("==========> GetActivePortfolioStrategyPostProcessor TAP - Error: " + e.getMessage()).log();
		}
		return null;
	}

}
