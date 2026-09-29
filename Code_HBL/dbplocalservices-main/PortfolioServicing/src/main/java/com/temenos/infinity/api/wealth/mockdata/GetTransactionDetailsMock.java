package com.temenos.infinity.api.wealth.mockdata;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealth.javaservice.GetTransactionDetails;
import com.temenos.infinity.api.wealth.util.WealthMockUtil;
import com.temenos.infinity.api.wealth.config.PortfolioWealthAPIServices;

/**
 * (INFO) Prepares Result object from the mock data returned by the Util.
 *
 */
public class GetTransactionDetailsMock implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	WealthMockUtil wealthMockUtil = new WealthMockUtil();

	@SuppressWarnings("unchecked")
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {

		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];

		try {
			JSONObject jsonResult = wealthMockUtil.mockGetTransactionList(inputParams);
			return Utilities.constructResultFromJSONObject(jsonResult);

		} catch (Exception e) {
			alert.prepareError("Error while invoking Transact - "
					+ PortfolioWealthAPIServices.WEALTH_GETTRANSACTIONDETAILS.getOperationName() + "  : " + e).log();
			return null;
		}
	}

}
