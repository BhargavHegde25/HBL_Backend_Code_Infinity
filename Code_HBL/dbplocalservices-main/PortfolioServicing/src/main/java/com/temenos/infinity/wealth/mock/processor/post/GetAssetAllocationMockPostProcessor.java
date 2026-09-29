/**
 * 
 */
package com.temenos.infinity.wealth.mock.processor.post;

import org.json.JSONArray;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

/**
 * @author himaja.sridhar
 *
 */
public class GetAssetAllocationMockPostProcessor implements DataPostProcessor2 {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response) throws Exception {
		diagnostic.prepareDebug("==========> GetAssetAllocationMockPostProcessor Mock - Entered ").log();
		JSONObject responseVal = new JSONObject();
		JSONArray assetArray = new JSONArray();
		String portfolioId = (String) request.getParameter(TemenosConstants.PORTFOLIOID);
		String[] assetGroup = null, marketValue = null;
		String totalMarketValue = "", referenceCurrency = "";
		if (portfolioId.equalsIgnoreCase("100777-1")) {
			assetGroup = new String[] { "Shares", "Cash", "Funds" };
			marketValue = new String[] { "28612.70", "15328.61", "5631.12" };
			totalMarketValue = "49572.43";
			referenceCurrency = "USD";
		} else if (portfolioId.equalsIgnoreCase("100777-2")) {
			assetGroup = new String[] { "Shares", "Cash" };
			marketValue = new String[] { "16543.15", "308.98" };
			totalMarketValue = "16852.13";
			referenceCurrency = "USD";
		} else if (portfolioId.equalsIgnoreCase("100777-3")) {
			assetGroup = new String[] { "Shares", "Cash" };
			marketValue = new String[] { "24655.94", "9560.00" };
			totalMarketValue = "34215.94";
			referenceCurrency = "USD";
		} else if (portfolioId.equalsIgnoreCase("100777-4")) {
			assetGroup = new String[] { "Shares", "Cash", "Funds" };
			marketValue = new String[] { "527102.20", "15328.61", "37050.00" };
			totalMarketValue = "48881.31";
			referenceCurrency = "USD";
		} else if (portfolioId.equalsIgnoreCase("100777-5")) {
			assetGroup = new String[] { "Shares", "Cash" };
			marketValue = new String[] { "278449.43", "308.98" };
			totalMarketValue = "18191.27";
			referenceCurrency = "USD";
		} else {
			assetGroup = new String[] { "Stocks", "Cash", "Funds" };
			marketValue = new String[] { "28612.70", "15328.61", "5631.12" };
			totalMarketValue = "49572.43";
			referenceCurrency = "USD";
		}
		for (int i = 0; i < assetGroup.length; i++) {
			JSONObject assetObj = new JSONObject();
			assetObj.put(TemenosConstants.ASSETGROUP, assetGroup[i]);
			assetObj.put(TemenosConstants.MARKETVALUE, marketValue[i]);
			assetArray.put(assetObj);
		}
		responseVal.put("portfolioID", portfolioId);
		responseVal.put(TemenosConstants.REFERENCECURRENCY, referenceCurrency);
		responseVal.put(TemenosConstants.TOTALMARKETVALUE, totalMarketValue);
		responseVal.put(TemenosConstants.ASSETS, assetArray);

		diagnostic.prepareDebug("==========> GetAssetAllocationMockPostProcessor Mock -  No. of records returned: "+assetArray.length()).log();
		responseVal.put("opstatus", "0");
		responseVal.put("httpStatusCode", "200");
		Result final_result = Utilities.constructResultFromJSONObject(responseVal);
		final_result.addOpstatusParam("0");
		final_result.addHttpStatusCodeParam("200");
		final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		diagnostic.prepareDebug("==========> GetAssetAllocationMockPostProcessor Mock - Exited").log();
		return final_result;
	}

}
