package com.temenos.infinity.wealth.tap.processor.post;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.Record;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

public class GetPortfolioHealthOrchPostProcessor implements DataPostProcessor2 {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		diagnostic.prepareDebug("==========> GetPortfolioHealthOrchPostProcessor TAP - Entered ").log();
		if (request.getParameter(TemenosConstants.WEALTH_CORE) != null
				&& (request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("TAP,Refinitiv")
						|| request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("TAP"))) {
			String recommendedSet  = result.getParamValueByName("recommendedInstrumentStatus");
			Record recRecord = new Record();
			recRecord.addParam(TemenosConstants.HEALTHPARAMETER, "Recommended Instruments");
			recRecord.addParam(TemenosConstants.HEALTHSTATUS, recommendedSet);
			result.getDatasetById(TemenosConstants.PORTFOLIOHEALTH).addRecord(recRecord);
		}
		else {
			result.addOpstatusParam("0");
			result.addHttpStatusCodeParam("200");
			result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		}
		diagnostic.prepareDebug("==========> GetPortfolioHealthOrchPostProcessor TAP - Exited ").log();
		return result;
	}

}
