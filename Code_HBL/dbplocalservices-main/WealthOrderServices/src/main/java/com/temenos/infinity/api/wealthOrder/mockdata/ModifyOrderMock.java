package com.temenos.infinity.api.wealthOrder.mockdata;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthOrder.config.WealthAPIServices;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;
import com.temenos.infinity.api.wealthOrder.util.WealthMockUtil;


/**
 * (INFO) Prepares Result object from the mock data returned by the Util.
 *
 */
public class ModifyOrderMock implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("unchecked")
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		WealthMockUtil wealthMockUtil = new WealthMockUtil();
		Map<String, Object> inputJSON = new HashMap<>();
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];

		try {
			if (inputParams.get(TemenosConstants.ORDERTYPE).toString().equalsIgnoreCase("MARKET")
					|| inputParams.get(TemenosConstants.ORDERTYPE).toString().equalsIgnoreCase("LIMIT")
					|| inputParams.get(TemenosConstants.ORDERTYPE).toString().equalsIgnoreCase("STOP")
					|| inputParams.get(TemenosConstants.ORDERTYPE).toString().equalsIgnoreCase("STOP.LIMIT")) {

				boolean validate = (request.getParameter(TemenosConstants.VALIDATEONLY) != null
						&& request.getParameter(TemenosConstants.VALIDATEONLY).toString().length() > 0) ? true : false;
				inputJSON.put(TemenosConstants.VALIDATEONLY, validate);
				JSONObject resultJSON = wealthMockUtil.mockForexOrders(inputJSON);
				return Utilities.constructResultFromJSONObject(resultJSON);
			} else {
				return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.ORDERTYPE);
			}
		} catch (Exception e) {
			alert.prepareError("Error while invoking Transact - "
					+ WealthAPIServices.WEALTH_GETTRANSACTIONDETAILS.getOperationName() + "  : " + e).log();
			return null;
		}
	}

}
