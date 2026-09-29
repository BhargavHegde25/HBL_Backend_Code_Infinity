package com.temenos.infinity.api.wealth.mock.preandpostprocessors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.IntStream;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealth.config.PortfolioWealthAPIServices;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

public class GetRecommendedInstrIPPostProcessor implements DataPostProcessor2 {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

		JSONObject recommendedInstrObj = new JSONObject();
		JSONObject responseN = new JSONObject();
		String portfolioId = request.getParameterValues(TemenosConstants.PORTFOLIOID)[0];
		String type = request.getParameterValues(TemenosConstants.ORDERSVIEW_TYPE)[0];
		
	    try {
		    if ((type != null && type.equalsIgnoreCase("pastProposal") && portfolioId.equalsIgnoreCase("100777-4")
				|| portfolioId.equalsIgnoreCase("100777-5"))
				|| ((type == null || !type.equalsIgnoreCase("pastProposal"))
						&& (portfolioId.equalsIgnoreCase("100777-5")))) {
			String instrumentName[] = new String[] {};
			String instrumentDetails[] = new String[] {};
			String header = "No issues";

			ArrayList<HashMap<String, String>> alList = new ArrayList<HashMap<String, String>>();
			IntStream.range(0, instrumentName.length).forEach(index -> {
				HashMap<String, String> hMap = new HashMap<String, String>();
				hMap.put("instrumentName", instrumentName[index]);
				hMap.put("instrumentDetails", instrumentDetails[index]);
				alList.add(hMap);
			});
			recommendedInstrObj.put("recommendedInstrumentComment", header);
			recommendedInstrObj.put("recommendedInstrumentDetails", alList);
			recommendedInstrObj.put("recommendedInstrumentStatus", "0");
			responseN.put("recommendedDetails", recommendedInstrObj);
		} else {
			responseN.put("recommendedDetails", new ArrayList<String>());
		}

		responseN.put("portfolioID", portfolioId);
		responseN.put("opstatus", "0");
		responseN.put("status", "success");
		responseN.put("httpStatusCode", "200");
		Result final_result = Utilities.constructResultFromJSONObject(responseN);
		result.appendResult(final_result);
		
	} catch (Exception e) {
		e.getMessage();
		alert.prepareError("Error while invoking GetRecommendedInstrIPPostProcessor MOCK - "
				+ PortfolioWealthAPIServices.WEALTH_GETPORTFOLIOHEALTHRECOMMENDEDINSTRUMENTS.getOperationName() + "  : " + e).log();
	}
	return result;
	}
}


