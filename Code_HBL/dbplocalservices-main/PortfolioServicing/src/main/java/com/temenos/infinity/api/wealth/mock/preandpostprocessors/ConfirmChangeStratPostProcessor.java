package com.temenos.infinity.api.wealth.mock.preandpostprocessors;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import java.security.SecureRandom;
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
 * @author shreya.singh
 *
 */

public class ConfirmChangeStratPostProcessor implements DataPostProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {
		
			String portfolioId = request.getParameterValues(TemenosConstants.PORTFOLIOID)[0];
			String strategyName = request.getParameterValues("strategyName")[0];
			String strategyId = request.getParameterValues("strategyId")[0];
			
			if(portfolioId != null &&  portfolioId.toString().trim().length() > 0 ) {
				if(strategyName != null &&  strategyName.toString().trim().length() > 0) {
					if(strategyId != null && strategyId.toString().trim().length() > 0)
					{

			SecureRandom sr = new SecureRandom();
			double id = sr.nextInt(100000);
			String StrategyId = "STRATEGY" + String.valueOf((int) id);
			result.addParam("id", StrategyId);
			result.addOpstatusParam("0");
			result.addHttpStatusCodeParam("200");
			result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		} 
			
		else {
						
				result.appendResult(PortfolioWealthUtils.validateMandatoryFields("strategyId"));
				result.addHttpStatusCodeParam("0");
				return result;
					}
				}
					
				else {
					result.appendResult(PortfolioWealthUtils.validateMandatoryFields("strategyName"));
					result.addHttpStatusCodeParam("0");
					return result;	
					}
			}
			else {
				result.appendResult(PortfolioWealthUtils.validateMandatoryFields("TemenosConstants.PORTFOLIOID)"));
				return result;
			}
		}
		catch (Exception e) {
			e.getMessage();
			alert.prepareError("Error while invoking GetMockconfirmChangeStratPostProcessor - "
					+ PortfolioWealthAPIServices.WEALTHSERVICESORCHESTRATION.getOperationName() + "  : " + e).log();
		}
		return result;
	}

}
