/**
 * 
 */
package com.temenos.infinity.wealth.tap.processor.post;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
/**
 * 
 * 
 * 
 * @author GAAYATHRI R
 *
 */
public class GetRiskAnalysisIPOrchPostProcessor implements DataPostProcessor2 {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		if (request.getParameter(TemenosConstants.WEALTH_CORE) != null
				&& (request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("TAP,Refinitiv")
						|| request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("TAP"))) {
			diagnostic.prepareDebug("==========> GetRiskAnalysisIPOrchPostProcessor TAP - Entered ").log();
			Dataset riskSet =result.getDatasetById("riskAnalysis");
			JSONArray riskStatus = ResultToJSON.convertDataset(result.getDatasetById("portfolioHealth"));
			String riskVal = "";
			for(int k=0;k<riskStatus.length();k++) {
				if(riskStatus.getJSONObject(k).get("healthParameter").toString().equalsIgnoreCase("Risk Analysis"))
				{
					riskVal = riskStatus.getJSONObject(k).get("healthStatus").toString();
				}
			}
			result.removeDatasetById("riskAnalysis");
			Dataset RiskAnalysis = new Dataset();
			RiskAnalysis.setId("riskAnalysis");
			diagnostic.prepareDebug("==========> GetRiskAnalysisIPOrchPostProcessor TAP - No. of records returned initially: " + riskSet.getAllRecords().size() ).log();
			for(int i=0;i<riskSet.getAllRecords().size();i++) {
				if(!riskSet.getRecord(i).getParamValueByName("portfolioLevel").equalsIgnoreCase("false"))
				{
					RiskAnalysis.addRecord(riskSet.getRecord(i));
				}
			}
			result.addDataset(RiskAnalysis);
			Record riskCode = result.getDatasetById("riskCode").getRecord(0);
			result.addParam("wealthRiskFlag", "true");
			riskCode.addParam("riskStatus", riskVal);
			result.getDatasetById("riskAnalysis").getRecord(0).addAllParams(riskCode.getAllParams());
			
			
		}else {
			result.addOpstatusParam("0");
			result.addHttpStatusCodeParam("200");
			result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		
		}
		diagnostic.prepareDebug("==========> GetRiskAnalysisIPOrchPostProcessor TAP - Exited ").log();
		return result;
	}

}
