/**
 * 
 */
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
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;

/**
 * @author muthukumarv
 *
 */
public class ModifyOrderMockPostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		diagnostic.prepareDebug("==========> ModifyOrderMockPostProcessor Mock - Entered").log();
		String orderType = (String) request.getParameter(TemenosConstants.ORDERTYPE);
		String validate_only = (String) request.getParameter(TemenosConstants.VALIDATEONLY);
		try {
			if (orderType.equalsIgnoreCase("MARKET") || orderType.equalsIgnoreCase("LIMIT")
					|| orderType.equalsIgnoreCase("STOP") || orderType.equalsIgnoreCase("STOP.LIMIT")) {

				boolean validate = (validate_only != null && validate_only.length() > 0) ? true : false;
				// inputJSON.put(TemenosConstants.VALIDATEONLY, validate);
				// JSONObject resultJSON = wealthMockUtil.mockForexOrders(inputJSON);

				JSONObject response1 = new JSONObject();
				if (validate) {
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
					String feesD = feesObj.toString();
					response1.put("feeDetails", feesD);

					// JSONObject messageObj = new JSONObject();
					// JSONArray messageArray = new JSONArray();
					// messageObj.put("id", "SC.ORD.DATE.GT.TODAY");
					// messageObj.put("message", "Message:ORDER DATE GREATER THAN TODAY");
					// messageArray.put(messageObj);
					// response.put("messageDetails", messageArray.toString());
				}

				response1.put("id", "FX" + CurrencyBackendDelegateImpl.getUniqueNumber());
				response1.put("fees", "31.36");
				response1.put("status", "success");
				response1.put("uniqueIdentifier", "SEAT" + CurrencyBackendDelegateImpl.getUniqueNumber() + ".00");
				response1.put("opstatus", "0");
				response1.put("httpStatusCode", "200");

				JSONObject resultJSON = response1;
				diagnostic.prepareDebug("==========> ModifyOrderMockPostProcessor Mock - Exiting with success").log();
				return Utilities.constructResultFromJSONObject(resultJSON);
			} else {
				diagnostic.prepareDebug("==========> ModifyOrderMockPostProcessor Mock - Error:: Invalid order type").log();
				return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.ORDERTYPE);
			}
		} catch (Exception e) {
			alert.prepareError("==========> ModifyOrderMockPostProcessor Mock - Error: " + e.getMessage()).log();
			return null;
		}
	}
}
