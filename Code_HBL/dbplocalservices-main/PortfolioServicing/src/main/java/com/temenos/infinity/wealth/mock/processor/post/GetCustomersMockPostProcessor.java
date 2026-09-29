package com.temenos.infinity.wealth.mock.processor.post;


/**
 * (INFO) If status is set as a part of the request , the operation is exited
 * else operation is executed.
 * 
 * @author sarah
 *
 */

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

public class GetCustomersMockPostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("Customers", new JSONArray());
			jsonObj.put("isWealthUser", "true");
			jsonObj.put("isMultiCustomer", "true");
			jsonObj.put("opstatus", "0");
			jsonObj.put("httpStatusCode", "200");
			diagnostic.prepareDebug("==========> GetCustomersT24PostProcessor T24 - Executed ").log();
			return Utilities.constructResultFromJSONObject(jsonObj);
			
		} catch (Exception e) {
			alert.prepareError("==========> GetCustomersMockPostProcessor Mock - Error: " + e.getMessage()).log();
			e.getMessage();
		}
		return result;
	}

}

