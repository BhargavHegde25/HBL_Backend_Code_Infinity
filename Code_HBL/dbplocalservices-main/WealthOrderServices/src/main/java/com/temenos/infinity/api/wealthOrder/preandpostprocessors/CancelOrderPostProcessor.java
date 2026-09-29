package com.temenos.infinity.api.wealthOrder.preandpostprocessors;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONObject;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthOrder.config.WealthAPIServices;
import com.temenos.infinity.api.wealthservices.util.T24ErrorAndOverrideHandling;

/**
 * (INFO) If status is set as a part of the request , the operation is exited 
 * 		  else operation is executed.
 * @author balaji.krishnan
 *
 */

public class CancelOrderPostProcessor implements DataPostProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

		try {
			T24ErrorAndOverrideHandling errorHandlingutil = T24ErrorAndOverrideHandling.getInstance();
			if (errorHandlingutil.isErrorResult(result)) {
				return result;
			}

			Record headerRec = result.getRecordById("header");
			String statusVal = headerRec.getParamValueByName("status");
			JSONObject finresponse = new JSONObject();
			if (statusVal != null && statusVal.trim().equalsIgnoreCase("success")) {
				finresponse.put("status", statusVal);
				finresponse.put("orderId", headerRec.getParamValueByName("id"));
				finresponse.put("opstatus", "0");
				finresponse.put("httpStatusCode", "200");
				if (result.getParamValueByName("messageDetails") == null) {

				} else {
					finresponse.put("messageDetails", result.getParamValueByName("messageDetails"));
				}

			} else {
				Record errorRec = result.getRecordById("error");
				if (errorRec != null) {
					Dataset errorData = errorRec.getDatasetById("errorDetails");
					Record error = errorData.getAllRecords().get(0);
					finresponse.put("errormessage", error.getParamValueByName("message"));
				} else {
					finresponse.put("errormessage", "");
				}
				finresponse.put("status", statusVal);
				finresponse.put("orderId", headerRec.getParamValueByName("id"));

			}

			return Utilities.constructResultFromJSONObject(finresponse);
		} catch (Exception e) {

			alert.prepareError("Error while invoking OrderBlotterPostProcessor - "
					+ WealthAPIServices.WEALTHSERVICESORCHESTRATION.getOperationName() + "  : " + e).log();
		}

		return result;
	}
}
