package com.temenos.infinity.api.wealth.preandpostprocessors;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.util.TokenUtils;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealth.config.PortfolioWealthAPIServices;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;

/**
 * (INFO) Generates Transact Token. Needs to be included for all transact
 * services.
 *
 */
public class GetOrderDetailsPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("rawtypes")
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {

		try {
			String status = request.getParameter("status");
			Map<String, String> inputparamMap = new HashMap<>();
			inputparamMap.put("userName",PortfolioWealthUtils.getUserAttributeFromIdentity(request, "UserName"));
			inputparamMap.put("customerId",PortfolioWealthUtils.getUserAttributeFromIdentity(request, "customer_id"));
			String backendToken = TokenUtils.getPortfolioWealthMSAuthToken(inputparamMap);
			request.addRequestParam_("Authorization", backendToken);

			if (StringUtils.isNotBlank(status) && !"success".equalsIgnoreCase(status)) {
				return false;
			}

			return true;
		} catch (Exception e) {
			alert.prepareError("Error while invoking GetOrderDetailsPreProcessor - "
					+ PortfolioWealthAPIServices.WEALTHSERVICESORCHESTRATION.getOperationName() + "  : " + e).log();
		}
		return false;
	}

}
