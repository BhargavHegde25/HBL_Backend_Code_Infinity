package com.temenos.infinity.wealth.tap.processor.post;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

public class GetInvestmentConstraintsHCOrchPostProcessor implements DataPostProcessor2 {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		diagnostic.prepareDebug("==========> GetInvestmentConstraintsHCOrchPostProcessor TAP - Entered ").log();
		if (request.getParameter(TemenosConstants.WEALTH_CORE) != null
				&& (request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("TAP,Refinitiv")
						|| request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("TAP"))) {
			Dataset allocatSet = result.getDatasetById(TemenosConstants.PORTFOLIOHEALTH);
			JSONArray allocatArray = ResultToJSON.convertDataset(allocatSet);
			String allocationStatus = "";
			String allocationComment = "";
			diagnostic.prepareDebug("==========> GetInvestmentConstraintsHCOrchPostProcessor TAP - No. of records returned initially: " + allocatArray.length() ).log();
			for(int i=0;i<allocatArray.length();i++) {
				String healthParam = allocatArray.getJSONObject(i).getString(TemenosConstants.HEALTHPARAMETER);
				if(healthParam.equalsIgnoreCase("Investment Constraints")) {
					allocationStatus= allocatArray.getJSONObject(i).getString(TemenosConstants.HEALTHSTATUS);
				}
			}
			if(allocationStatus.equals("1")) {
				allocationComment = "Some issues with your portfolio health";
				diagnostic.prepareDebug("==========> GetInvestmentConstraintsHCOrchPostProcessor TAP - Issues ").log();
			}else {
				allocationComment = "No issues";
				diagnostic.prepareDebug("==========> GetInvestmentConstraintsHCOrchPostProcessor TAP - No Issues ").log();
			}
			result.addParam("investmentConstraintStatus", allocationStatus);
			result.addParam("investmentConstraintComment", allocationComment);
			result.removeDatasetById(TemenosConstants.PORTFOLIOHEALTH);
			//result.addParam("wealthAllocationFlag", "true");
			result.removeDatasetById("recommendedInstrumentDetails");
		}
		else {
			result.addOpstatusParam("0");
			result.addHttpStatusCodeParam("200");
			result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		}
		diagnostic.prepareDebug("==========> GetInvestmentConstraintsHCOrchPostProcessor TAP - Exited ").log();
		return result;
	}

}
