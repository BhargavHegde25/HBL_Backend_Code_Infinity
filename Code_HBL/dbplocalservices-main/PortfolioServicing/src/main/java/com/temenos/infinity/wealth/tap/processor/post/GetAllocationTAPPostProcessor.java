/**
 * 
 */
package com.temenos.infinity.wealth.tap.processor.post;

import com.temenos.logger.Logger;
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
public class GetAllocationTAPPostProcessor implements DataPostProcessor2 {

	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response) throws Exception {
		diagnostic.prepareDebug("==========> GetAllocationTAPPostProcessor TAP - Entered ").log();
		JSONArray assetArray = new JSONArray();
		JSONArray currencyArray = new JSONArray();
		JSONArray regionArray = new JSONArray();
		Dataset bodyDataset = result.getDatasetById("body");
		JSONArray assetList = ResultToJSON.convertDataset(bodyDataset);
		diagnostic.prepareDebug("==========> GetAllocationTAPPostProcessor TAP - No. of records returned initially: "+ assetList.length()).log();
		Double value;
		for(int i=0; i<assetList.length();i++) {
			JSONObject assetObj = assetList.getJSONObject(i);
			value=Double.parseDouble(assetObj.getString("totalValue"));
			String val=String.format("%.2f",value);
			assetObj.put("totalValue",val);	
			if(assetObj.get("uniqueAssetClass").toString().equalsIgnoreCase("true")) {
				Double assetvalue=Double.parseDouble(assetObj.getString("valueByAssetClass"));
				String assetval=String.format("%.2f",assetvalue);
				assetObj.put("valueByAssetClass",assetval);	
				
				Double assetwgtvalue=Double.parseDouble(assetObj.getString("wieghtByAssetClass"));
				String assetwgtval=String.format("%.2f",assetwgtvalue);
				assetObj.put("wieghtByAssetClass",assetwgtval);	
				assetArray.put(assetObj);
						}
			if(assetObj.get("uniqueCcyClass").toString().equalsIgnoreCase("true")
					&& !assetObj.get("valueByCurrency").toString().equalsIgnoreCase("0")
					&& !assetObj.get("weightByCurrency").toString().equalsIgnoreCase("0")) {
				Double currencyvalue=Double.parseDouble(assetObj.getString("valueByCurrency"));
				String currencyval=String.format("%.2f",currencyvalue);
				assetObj.put("valueByCurrency",currencyval);	
				
				Double currencywgtvalue=Double.parseDouble(assetObj.getString("weightByCurrency"));
				String currencywgtval=String.format("%.2f",currencywgtvalue);
				assetObj.put("weightByCurrency",currencywgtval);	
				currencyArray.put(assetObj);
			}
			if(assetObj.get("uniqueRegionClass").toString().equalsIgnoreCase("true")) {
				Double currencyvalue=Double.parseDouble(assetObj.getString("valueByRegion"));
				String currencyval=String.format("%.2f",currencyvalue);
				assetObj.put("valueByRegion",currencyval);	
				
				Double currencywgtvalue=Double.parseDouble(assetObj.getString("wieghtByRegion"));
				String currencywgtval=String.format("%.2f",currencywgtvalue);
				assetObj.put("wieghtByRegion",currencywgtval);
				regionArray.put(assetObj);
			}
		}
		JSONObject data =new JSONObject();
		data.put("portfolioID", request.getParameter("portfolioId"));
		data.put("asset", assetArray);
		data.put("currency", currencyArray);
		data.put("region", regionArray);
		diagnostic.prepareDebug("==========> GetAllocationTAPPostProcessor TAP - No. of records returned in assets: "+ assetArray.length() +
				" region: "+ regionArray.length() + " currency: " + currencyArray.length()).log();
		Result AssetRes = Utilities.constructResultFromJSONObject(data);
		AssetRes.addOpstatusParam("0");
		AssetRes.addHttpStatusCodeParam("200");
		AssetRes.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		diagnostic.prepareDebug("==========> GetAllocationTAPPostProcessor TAP - Exited ").log();
		return AssetRes;
	}

}
