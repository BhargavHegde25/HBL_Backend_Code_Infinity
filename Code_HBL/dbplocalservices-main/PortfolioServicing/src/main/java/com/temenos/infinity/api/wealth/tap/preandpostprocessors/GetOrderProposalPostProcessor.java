package com.temenos.infinity.api.wealth.tap.preandpostprocessors;

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
 * 
 * @author s.subashini
 *
 */
public class GetOrderProposalPostProcessor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		
		Dataset bodyObj = result.getDatasetById("orderProposal");
		JSONArray objArray = ResultToJSON.convertDataset(bodyObj);
		Double buy = 0.0, sell = 0.0;
		Double totalOrderAmount = 0.0;
		String currency = "";
		String totalCount = "";
		JSONArray orderArray = new JSONArray();
		for(int i = 0; i < objArray.length(); i++) {
			JSONObject orderObj = new JSONObject();
			JSONObject obj = objArray.getJSONObject(i);
			
			String instrumentName = obj.getString("instrumentName");
			String orderAmount = obj.getString("orderAmount");
			String orderQty = obj.getString("orderQty");
			String order = obj.getString("order");
			currency = obj.getString("orderAmountCurrency");
			totalCount = String.valueOf(objArray.length()) ;
			
			if(obj.getString("order").equalsIgnoreCase("Buy")) {
				buy = buy +Double.parseDouble(obj.getString("orderAmount")) ;
			} else if(obj.getString("order").equalsIgnoreCase("Sell")) {
				sell = sell + Double.parseDouble(obj.getString("orderAmount"));
			}
			
			orderObj.put("instrumentName", instrumentName);
			orderObj.put("orderAmount", orderAmount);
			orderObj.put("orderQty", orderQty);
			orderObj.put("order", order);
			orderArray.put(orderObj);					
		}
		
		
		totalOrderAmount = buy - sell ; 
		
		JSONObject resultObj = new JSONObject();
		resultObj.put("orderAmountCurrency",currency);
		resultObj.put("totalCount", totalCount);
		resultObj.put("totalOrderAmount", totalOrderAmount);
		resultObj.put("portfolioID", request.getParameter("portfolioId"));
		resultObj.put("status", "success");
		resultObj.put("orderProposal", orderArray);
		Result linkRes = Utilities.constructResultFromJSONObject(resultObj);
		linkRes.addOpstatusParam("0");
		linkRes.addHttpStatusCodeParam("200");
		linkRes.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		return linkRes;
	}

}
