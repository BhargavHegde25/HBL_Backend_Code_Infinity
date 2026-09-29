package com.temenos.infinity.wealth.mock.processor.post;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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

public class GetSuitabilityProfilePostProcessor implements DataPostProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

		try {
			diagnostic.prepareDebug("==========> GetSuitabilityProfilePostProcessor Mock - Entered ").log();
			String portfolioId = request.getParameterValues(TemenosConstants.PORTFOLIOID)[0];
			
			if (portfolioId.equalsIgnoreCase("100777-4")) {

				JSONObject returnObj = new JSONObject();
				
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.MONTH, 3);
				returnObj.put("expiryDate", sdf.format(cal.getTime()));
				returnObj.put("isValid", "true");
				returnObj.put("message", "Valid");
				returnObj.put(TemenosConstants.PORTFOLIOID, portfolioId);

				Result final_result = Utilities.constructResultFromJSONObject(returnObj);
				final_result.addOpstatusParam("0");
				final_result.addHttpStatusCodeParam("200");
				final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				result.appendResult(final_result);

			} else if (portfolioId.equalsIgnoreCase("100777-5")) {
				JSONObject returnObj = new JSONObject();

				returnObj.put("expiryDate", "10/09/2022");
				returnObj.put("isValid", "false");
				returnObj.put("message", "Current profile expired");
				returnObj.put(TemenosConstants.PORTFOLIOID, portfolioId);

				Result final_result = Utilities.constructResultFromJSONObject(returnObj);
				final_result.addOpstatusParam("0");
				final_result.addHttpStatusCodeParam("200");
				final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				result.appendResult(final_result);
			}

		} catch (Exception e) {
			e.getMessage();
			alert.prepareError("==========> GetSuitabilityProfilePostProcessor Mock - Error: " + e.getMessage()).log();
		}
		alert.prepareError("==========> GetSuitabilityProfilePostProcessor Mock - Exited ").log();
		return result;
	}

}
