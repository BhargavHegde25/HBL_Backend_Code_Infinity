/**
 * 
 */
package com.temenos.infinity.api.wealth.preandpostprocessors;

import java.util.List;

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
 * @author himaja.sridhar
 *
 */
public class GetCustomerPortofolioPostProcessor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

		Record headerRec = result.getRecordById("header");
		String statusVal = headerRec.getParamValueByName("status");
		if (!statusVal.equals("success")) {
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

		Result portfolioRes = Utilities.constructResultFromJSONObject(portfolioJSON);
		portfolioRes.addOpstatusParam("0");
		portfolioRes.addHttpStatusCodeParam("200");
		portfolioRes.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);

		return portfolioRes;
	}

}
