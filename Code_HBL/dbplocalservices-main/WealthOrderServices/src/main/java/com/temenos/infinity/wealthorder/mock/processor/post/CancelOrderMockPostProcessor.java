/**
 * 
 */
package com.temenos.infinity.wealthorder.mock.processor.post;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * @author muthukumarv
 *
 */
public class CancelOrderMockPostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		diagnostic.prepareDebug("==========> CancelOrderMockPostProcessor Mock - Entered ").log();
		try {
			String portfolioId = (String) request.getParameter(TemenosConstants.PORTFOLIOID);
			String orderId = (String) request.getParameter(TemenosConstants.ORDER_ID);
			JSONObject resp = new JSONObject();

			resp.put("portfolioId", portfolioId);
			resp.put("orderId", orderId);
			resp.put("opstatus", "0");
			resp.put("httpStatusCode", "200");
			diagnostic.prepareDebug("==========> CancelOrderMockPostProcessor Mock - Exiting with success").log();
			return Utilities.constructResultFromJSONObject(resp);
			// return resp;
		} catch (Exception e) {
			alert.prepareError("==========> CancelOrderMockPostProcessor Mock - Error: " + e.getMessage()).log();
			return null;
		}
	}
}
