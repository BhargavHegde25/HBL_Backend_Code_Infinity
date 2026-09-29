package com.temenos.infinity.wealthorder.mock.processor.post;

import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthOrder.backenddelegate.impl.CurrencyBackendDelegateImpl;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * (INFO) If status is set as a part of the request , the operation is exited
 * else operation is executed.
 * 
 * @author balaji.krishnan
 *
 */

public class createCurrencyOrderMockPostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {
			diagnostic.prepareDebug("==========> createCurrencyOrderMockPostProcessor Mock - Entered ").log();
			JSONObject responsemain = new JSONObject();
			String str = "";
			if (request.getParameter(TemenosConstants.VALIDATEONLY) != null) {
				boolean validate_only = Boolean
						.parseBoolean(request.getParameter(TemenosConstants.VALIDATEONLY).toString());
				if (validate_only) {
					JSONObject feesObj = new JSONObject();
					HashMap<String, String> hm = new HashMap<String, String>();
					hm.put("safekeepChargeInTradeCurrency", "19.69565");
					hm.put("safekeepChargeInChargeCurrency", "19.69565");
					hm.put("InducementFeesInChargeCurrency", "16.3085");
					hm.put("InducementFeesInTradeCurrency", "16.3085");
					hm.put("advisoryFeesInChargeCurrency", "18.8175");
					hm.put("advisoryFeesInTradeCurrency", "18.8175");
					hm.put("tradeCurrency", "USD");
					hm.put("chargeCurrency", "USD");
					hm.forEach((key, value) -> feesObj.put(key, value));
					str = feesObj.toString();
				}
			}
			Result final_result = Utilities.constructResultFromJSONObject(responsemain);
			final_result.addOpstatusParam("0");
			final_result.addHttpStatusCodeParam("200");
			final_result.addParam("feeDetails", str);
			final_result.addParam("id", "FX" + CurrencyBackendDelegateImpl.getUniqueNumber());
			final_result.addParam("fees", "31.36");
			final_result.addParam("uniqueIdentifier", "SEAT" + CurrencyBackendDelegateImpl.getUniqueNumber() + ".00");
			final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			diagnostic.prepareDebug("==========> createCurrencyOrderMockPostProcessor Mock - Exiting with success").log();
			result.appendResult(final_result);
		} catch (Exception e) {
			alert.prepareError("==========> createCurrencyOrderMockPostProcessor Mock - Error: " + e.getMessage()).log();
			e.getMessage();
		}
		return result;
	}

}
