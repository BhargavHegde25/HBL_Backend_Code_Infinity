package com.temenos.infinity.api.wealth.mock.preandpostprocessors;

import java.io.FileInputStream;
import java.lang.Math;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealth.config.PortfolioWealthAPIServices;
import com.temenos.infinity.api.wealth.constants.OperationName;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;

public class GeneratePastProposalPostProcessor implements DataPostProcessor2  {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

		//JSONObject response1 = new JSONObject();
		try {
			
			JSONObject response1 = new JSONObject();
			String portfolioId = request.getParameterValues(TemenosConstants.PORTFOLIOID)[0];
			String navPage = request.getParameterValues(TemenosConstants.NAVPAGE)[0];
			
			if (portfolioId.equalsIgnoreCase("100777-4") || portfolioId.equalsIgnoreCase("100777-5")) {
				if(navPage != null && navPage.equalsIgnoreCase("pastProposal")) {
				
				 String operationName = OperationName.BASE64;

		            Result final_result = new Result();
		            final_result.addParam("base",operationName);
					final_result.addOpstatusParam("0");
					final_result.addHttpStatusCodeParam("200");
					final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
					result.appendResult(final_result);

			}else {
				result.appendResult(PortfolioWealthUtils.validateMandatoryFields("navpage"));
				return result;
			} 
			}
				else if (portfolioId.equalsIgnoreCase("100777-1") || portfolioId.equalsIgnoreCase("100777-2")
					|| portfolioId.equalsIgnoreCase("100777-3")) {
				String errorText = "This operation not supported for non advisiory portfolio";
				response1.put("status", "success");
				response1.put("opstatus", "0");
				response1.put("httpStatusCode", "200");
				response1.put("errorText", errorText);
				Result final_result = Utilities.constructResultFromJSONObject(response1);
				result.appendResult(final_result);

			}
		}catch (Exception e) {
			e.getMessage();
			alert.prepareError("Error while invoking GetAllocationHCPostProcessor MOCK - "
					+ PortfolioWealthAPIServices.WEALTH_GETPORTFOLIOHEALTHALLOCATION.getOperationName() + "  : " + e).log();
		}
		
		return result;
	}

}
