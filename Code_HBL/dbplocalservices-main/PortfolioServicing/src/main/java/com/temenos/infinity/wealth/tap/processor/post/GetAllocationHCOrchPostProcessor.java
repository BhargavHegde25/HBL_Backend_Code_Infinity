/**
 * 
 */
package com.temenos.infinity.wealth.tap.processor.post;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;
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

	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		diagnostic.prepareDebug("==========> GetAllocationHCOrchPostProcessor TAP - Entered ").log();
		if (request.getParameter("wealthCore") != null
				&& (request.getParameter("wealthCore").equalsIgnoreCase("TAP,Refinitiv")
						|| request.getParameter("wealthCore").equalsIgnoreCase("TAP"))) {
			Dataset allocatSet = result.getDatasetById(TemenosConstants.PORTFOLIOHEALTH);
			JSONArray allocatArray = ResultToJSON.convertDataset(allocatSet);
			diagnostic.prepareDebug("==========> GetAllocationHCOrchPostProcessor TAP - No. of records returned initially: " + allocatArray.length() ).log();
			String allocationStatus = "";
			for(int i=0;i<allocatArray.length();i++) {
				String healthParam = allocatArray.getJSONObject(i).getString(TemenosConstants.HEALTHPARAMETER);
				if(healthParam.equalsIgnoreCase("Asset Allocation")) {
					allocationStatus= allocatArray.getJSONObject(i).getString(TemenosConstants.HEALTHSTATUS);
					diagnostic.prepareDebug("==========> GetAllocationHCOrchPostProcessor TAP - Health parameter available" ).log();
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
		diagnostic.prepareDebug("==========> GetAllocationHCOrchPostProcessor TAP - Exited ").log();
		return result;
	}

}
