package com.temenos.infinity.api.wealth.mock.preandpostprocessors;

import java.lang.Math;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;
import java.security.SecureRandom;

import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealth.config.PortfolioWealthAPIServices;
import com.temenos.infinity.api.wealth.constants.OperationName;
import com.temenos.infinity.api.wealth.constants.ServiceId;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;

public class ConfirmRecomStratPostProcessor implements DataPostProcessor2  {
	
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
				            String StrategyId = "STRATEGY" + String.valueOf((int)id);
				            Result final_result = new Result();
				            final_result.addParam("id",StrategyId);
							final_result.addOpstatusParam("0");
							final_result.addHttpStatusCodeParam("200");
							final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
							result.appendResult(final_result);
			
					}
					
					else {
						
						result.appendResult(PortfolioWealthUtils.validateMandatoryFields("strategyId"));
						return result;
			
					}
				}
					
				else {
					result.appendResult(PortfolioWealthUtils.validateMandatoryFields("strategyName"));
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
			alert.prepareError("Error while invoking GetMockMyStrategyPostProcessor - "
					+ PortfolioWealthAPIServices.WEALTHSERVICESORCHESTRATION.getOperationName() + "  : " + e).log();
		}
		return result;
	}

}
