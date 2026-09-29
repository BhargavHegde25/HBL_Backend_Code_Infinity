/**
 * 
 */
package com.temenos.infinity.wealth.mock.processor.post;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * Mock data construction for GetAssetList service
 * 
 * @author muthukumarv
 */
public class GetAssetListMockPostProcessor implements DataPostProcessor2 {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		diagnostic.prepareInfo("==========> GetAssetListMockPostProcessor Mock - Entered").log();
		try {
			JSONObject resp = new JSONObject();
			JSONObject AssetList = new JSONObject();
			JSONArray assetArray = new JSONArray();
			String customerId = "100777";	
			String[] assetGroup = null, marketValue = null;
			assetGroup = new String[] { "Shares", "Cash", "Funds" };
			marketValue = new String[] { "850707.480", "31275.180", "42681.120" };

			for (int i = 0; i < assetGroup.length; i++) {
				JSONObject assetObj = new JSONObject();
				assetObj.put(TemenosConstants.ASSETGROUP, assetGroup[i]);
				assetObj.put(TemenosConstants.MARKETVALUE, marketValue[i]);
				assetArray.put(assetObj);
			}
			//resp.put("customerId", customerId);
			resp.put("referenceCurrency", "USD");
			resp.put("totalAssetValue", "924663.780");
			resp.put("assets", assetArray);
			resp.put("opstatus", "0");
			resp.put("httpStatusCode", "200");
			resp.put("unRealizedPL", "P");
			resp.put("unRealizedPLAmount", "152807.00");
			resp.put("unRealizedPLPercentage", "68.25");
			//AssetList.put("AssetList", resp);
			
			String assetlist = resp.toString();
			Result final_result = new Result();
			final_result.addOpstatusParam("0");
			final_result.addParam("AssetList", assetlist);
			diagnostic.prepareDebug("==========> GetAssetListMockPostProcessor Mock -  No. of records returned: "+assetlist.length()).log();
			final_result.addHttpStatusCodeParam("200");
			final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			return final_result;


		} catch (Exception e) {
			e.getMessage();
			alert.prepareError("==========> GetAssetListMockPostProcessor Mock - Error: " + e.getMessage()).log();
		}
		diagnostic.prepareInfo("==========> GetAssetListMockPostProcessor Mock - Exited").log();
		return result;
	}

}