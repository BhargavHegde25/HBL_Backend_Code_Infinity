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
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * @author muthukumarv
 *
 */
public class GetDashboardGraphTAPOrchPostProcessor implements DataPostProcessor2 {
	
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		diagnostic.prepareDebug("==========> GetDashboardGraphTAPOrchPostProcessor TAP - Entered ").log();
		Record portfolioListRec = result.getRecordById("PortfolioList");
		//Dataset graphDurationSet = result.getDatasetById("graphDuration");
		if(portfolioListRec!= null)
		{
			JSONObject portObj = ResultToJSON.convertRecord(portfolioListRec);
			JSONArray portArr = portObj.getJSONArray("portfolioList");
			diagnostic.prepareDebug("==========> GetDashboardGraphTAPOrchPostProcessor TAP - No. of records returned : " + portArr.length() ).log();
			Double totalVal = 0.0;
			for(int i =0;i<portArr.length();i++)
			{
				JSONObject portJSON = portArr.getJSONObject(i);
				totalVal = totalVal + Double.parseDouble(portJSON.get("marketValue").toString());
			}
			 result.addParam(TemenosConstants.MARKETVALUE, String.format("%.2f", totalVal));
			 result.removeRecordById("PortfolioList");
			 result.removeParamByName("isTapIntegration");
		}
		diagnostic.prepareDebug("==========> GetDashboardGraphTAPOrchPostProcessor TAP - Exited ").log();
		return result;
			

	}
	
}
