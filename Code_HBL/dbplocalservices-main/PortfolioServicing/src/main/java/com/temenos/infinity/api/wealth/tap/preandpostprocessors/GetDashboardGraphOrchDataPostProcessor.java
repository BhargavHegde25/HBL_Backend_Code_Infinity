/**
 * 
 */
package com.temenos.infinity.api.wealth.tap.preandpostprocessors;

import org.json.JSONArray;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

/**
 * @author himaja.sridhar
 *
 */
public class GetDashboardGraphOrchDataPostProcessor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		Record portfolioListRec = result.getRecordById("PortfolioList");
		//Dataset graphDurationSet = result.getDatasetById("graphDuration");
		JSONObject portObj = ResultToJSON.convertRecord(portfolioListRec);
		JSONArray portArr = portObj.getJSONArray("portfolioList");
		Double totalVal = 0.0;
		for(int i =0;i<portArr.length();i++)
		{
			JSONObject portJSON = portArr.getJSONObject(i);
			totalVal = totalVal + Double.parseDouble(portJSON.get("marketValue").toString());
		}
		 result.addParam(TemenosConstants.MARKETVALUE, String.format("%.2f", totalVal));
		 result.removeRecordById("PortfolioList");
		 result.removeParamByName("isTapIntegration");
		
		return result;
	}

}
