package com.temenos.infinity.api.wealth.mock.preandpostprocessors;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealth.config.PortfolioWealthAPIServices;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;

/**
 * (INFO) If status is set as a part of the request , the operation is exited
 * else operation is executed.
 * 
 * @author vinayranjan.sharma
 *
 */

public class RejectProposalPostProcessor implements DataPostProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {
			String portfolioId = request.getParameterValues(TemenosConstants.PORTFOLIOID)[0];			
			if(portfolioId != null &&  portfolioId.toString().trim().length() > 0 ) {

				            Result final_result = new Result();
							final_result.addOpstatusParam("0");
							final_result.addHttpStatusCodeParam("200");
							final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
							final_result.addParam("message", "Update succeeded.");
							result.appendResult(final_result);
											}
			else {
				result.appendResult(PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.PORTFOLIOID));
				return result;
			}
		}
		 catch (Exception e) {
			e.getMessage();
			alert.prepareError("Error while invoking RejectProposalPostProcessor - "
					+ e).log();
		}
		return result;
	}

}
