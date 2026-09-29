/**
 * 
 */
package com.temenos.infinity.api.wealth.tap.preandpostprocessors;

import org.json.JSONArray;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * @author himaja.sridhar
 *
 */
public class GetAllocationHCOrchPostProcessor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		
		if (request.getParameter("wealthCore") != null
				&& (request.getParameter("wealthCore").equalsIgnoreCase("TAP,Refinitiv")
						|| request.getParameter("wealthCore").equalsIgnoreCase("TAP"))) {
			Dataset allocatSet = result.getDatasetById(TemenosConstants.PORTFOLIOHEALTH);
			JSONArray allocatArray = ResultToJSON.convertDataset(allocatSet);
			String allocationStatus = "";
			for(int i=0;i<allocatArray.length();i++) {
				String healthParam = allocatArray.getJSONObject(i).getString(TemenosConstants.HEALTHPARAMETER);
				if(healthParam.equalsIgnoreCase("Asset Allocation")) {
					allocationStatus= allocatArray.getJSONObject(i).getString(TemenosConstants.HEALTHSTATUS);
				}
			}
			result.addParam("allocationStatus", allocationStatus);
			result.removeDatasetById(TemenosConstants.PORTFOLIOHEALTH);
			result.addParam("wealthAllocationFlag", "true");
			result.removeDatasetById("recommendedInstrumentDetails");
			
		}
		else {
			
			result.addOpstatusParam("0");
			result.addHttpStatusCodeParam("200");
			result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
	
		}
		return result;
	}

}
