package com.temenos.infinity.wealth.t24.processor.post;


import java.util.List;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.dbputilities.util.CommonUtils;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
/**
 *
 * 
 * @author muthukumarv
 *
 */

public class GetRecentActivityT24PostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {
			diagnostic.prepareDebug("==========> GetRecentActivityT24PostProcessor T24 - Entered ").log();
			Record headerRec = result.getRecordById("header");
			String statusVal = headerRec.getParamValueByName("status");
			if (!statusVal.equals("success")) {

				alert.prepareError("==========> GetRecentActivityT24PostProcessor T24 - Error from API").log();
				JSONObject response1 = new JSONObject();
				Record errorRec = result.getRecordById("error");
				response1.put("errormessage", errorRec.getParamValueByName("message"));
				Result errorRes = Utilities.constructResultFromJSONObject(response1);
				return errorRes;
			}
			
			Dataset bodyDataset = result.getDatasetById("body");
			String customerPortfolio = null;
			int portfoliosNb = 0;
			
			List<Record> drecords = bodyDataset.getAllRecords();
			JSONArray bodyArray = new JSONArray();
			diagnostic.prepareDebug("==========> GetRecentActivityT24PostProcessor T24 - No. of Records returned initially: "+ drecords.size()).log();
			for (int j = 0; j < drecords.size(); j++) {
				JSONObject portfolioObj = new JSONObject();
				Record drecord = drecords.get(j);
				portfolioObj = CommonUtils.convertRecordToJSONObject(drecord); 
				if (portfolioObj.has("portfolio")) {
					bodyArray.put(portfolioObj);
					String portfolioId = portfolioObj.getString("portfolio");
					if (customerPortfolio == null) {
						customerPortfolio = portfolioId;
					} else {
						customerPortfolio = customerPortfolio + "," + portfolioId;
					}
				}
			}
			portfoliosNb = bodyArray.length();
			
			JSONObject portfolioJSON = new JSONObject(); 
			
			portfolioJSON.put("portfolioId", customerPortfolio);
			portfolioJSON.put("loop_count", portfoliosNb);
			diagnostic.prepareDebug("==========> GetRecentActivityT24PostProcessor T24 - LoopCount: "+ portfoliosNb).log();

			Result portfolioRes = Utilities.constructResultFromJSONObject(portfolioJSON);
			portfolioRes.addOpstatusParam("0");
			portfolioRes.addHttpStatusCodeParam("200");
			portfolioRes.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);

			return portfolioRes;
				
			
		} catch (Exception e) {
			alert.prepareError("==========> GetRecentActivityT24PostProcessor T24 - Error: " + e.getMessage()).log();
			e.getMessage();
		}
		return result;
	}

}
