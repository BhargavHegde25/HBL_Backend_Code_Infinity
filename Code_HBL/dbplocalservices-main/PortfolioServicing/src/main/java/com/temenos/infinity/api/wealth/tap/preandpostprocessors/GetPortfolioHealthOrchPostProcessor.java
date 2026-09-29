package com.temenos.infinity.api.wealth.tap.preandpostprocessors;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.Record;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

public class GetPortfolioHealthOrchPostProcessor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		
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
		return result;
	}

}
