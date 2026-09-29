package com.temenos.infinity.api.wealthOrder.mockdata;

import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONObject;

import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthOrder.config.WealthAPIServices;
import com.temenos.infinity.api.wealthOrder.util.WealthMockUtil;

public class ViewInstrumentTransactionsMock implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	
	
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		WealthMockUtil wealthMockUtil = new WealthMockUtil();
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];

		try {
			JSONObject jsonResult = wealthMockUtil.mockViewInstrumentTransactions(inputParams);
			return Utilities.constructResultFromJSONObject(jsonResult);

		} catch (Exception e) {
			alert.prepareError("Error while invoking Transact - "
					+ WealthAPIServices.WEALTH_VIEWINSTRUMENTTRANSACTIONS.getOperationName() + "  : " + e).log();
			return null;
		}
	}

}
