package com.temenos.infinity.wealth.mock.processor.pre;

import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;

/**
 * @author muthukumarv
 *
 */
public class GetRecentActivityMockPreProcessor implements DataPreProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("rawtypes")
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			diagnostic.prepareDebug("==========> GetRecentActivityMockPreProcessor Mock - Entered ").log();
			String wealthCore = EnvironmentConfigurationsHandler.getValue(TemenosConstants.WEALTH_CORE, request);
			result.addParam(TemenosConstants.WEALTH_CORE, wealthCore);
			request.addRequestParam_(TemenosConstants.WEALTH_CORE, wealthCore);
//			String customerId = PortfolioWealthUtils.getCustomerFromCache(request);
//			request.addRequestParam_(TemenosConstants.CUSTOMERID, customerId);
			if (wealthCore != null && (wealthCore.equalsIgnoreCase("Mock"))) {
				diagnostic.prepareDebug("==========> GetRecentActivityMockPreProcessor Mock - Core check done").log();
				return true;

			} else {
				result.addOpstatusParam("0");
				result.addHttpStatusCodeParam("200");
				result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				diagnostic.prepareDebug("==========> GetRecentActivityMockPreProcessor Mock - Exiting for token generation").log();
				return false;
			}

		} catch (Exception e) {
			alert.prepareError("==========> GetRecentActivityMockPreProcessor Mock - Error: " + e.getMessage()).log();
			e.getMessage();
			return false;
		}
	}
}
