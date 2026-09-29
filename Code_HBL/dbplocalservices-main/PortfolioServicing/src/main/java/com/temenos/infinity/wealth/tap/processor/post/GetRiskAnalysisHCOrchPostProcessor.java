/**
 * 
 */
package com.temenos.infinity.wealth.tap.processor.post;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;

/**
 * @author himaja.sridhar
 *
 */
public class GetRiskAnalysisHCOrchPostProcessor implements DataPostProcessor2 {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		if (request.getParameter(TemenosConstants.WEALTH_CORE) != null
				&& (request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("TAP,Refinitiv")
						|| request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("TAP"))) {
			diagnostic.prepareDebug("==========> GetRiskAnalysisHCOrchPostProcessor TAP - Entered ").log();
			String riskStatus = result.getDatasetById("portfolioHealth").getRecord(2).getParamValueByName("healthStatus").toString();
			Record statusRecord = new Record();
			Param statusParam = new Param();
			statusParam.setName(TemenosConstants.RISKSTATSUS);
			statusParam.setValue(riskStatus);
			statusRecord.addParam(statusParam);
			Dataset riskSet =result.getDatasetById("riskAnalysis");
			result.removeDatasetById("riskAnalysis");
			Dataset riskAnalysis = new Dataset();
			riskAnalysis.setId("riskAnalysis");
			diagnostic.prepareDebug("==========> GetRiskAnalysisHCOrchPostProcessor TAP - No. of records returned initially: " + riskSet.getAllRecords().size() ).log();
			for(int i=0;i<riskSet.getAllRecords().size();i++) {
				if(!riskSet.getRecord(i).getParamValueByName("portfolioLevel").equalsIgnoreCase("false"))
				{
					riskAnalysis.addRecord(riskSet.getRecord(i));
				}
			}
			result.addDataset(riskAnalysis);
			Record riskCode = result.getDatasetById("riskCode").getRecord(0);
			result.addParam("wealthRiskFlag", "true");
			result.getDatasetById("riskAnalysis").getRecord(0).addAllParams(riskCode.getAllParams());
			result.getDatasetById("riskAnalysis").getRecord(0).addAllParams(statusRecord.getAllParams());
			
		}else {
			result.addOpstatusParam("0");
			result.addHttpStatusCodeParam("200");
			result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		
		}
		diagnostic.prepareDebug("==========> GetRiskAnalysisHCOrchPostProcessor TAP - Exited ").log();
		return result;
	}

}
