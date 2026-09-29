/**
 * 
 */
package com.temenos.infinity.wealthorder.refinitiv.processor.pre;

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

/**
 * @author muthukumarv
 *
 */
public class GetCurrencyGraphRefinitivOrchPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "rawtypes"})
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			diagnostic.prepareDebug("==========> GetCurrencyGraphRefinitivOrchPreProcessor Refinitiv - Entered ").log();
			String mkt_applicationID = (EnvironmentConfigurationsHandler.getValue(TemenosConstants.INF_WLTH_MKT_APPID,
					request) != null
							? EnvironmentConfigurationsHandler.getValue(TemenosConstants.INF_WLTH_MKT_APPID, request)
									.toString().trim()
							: "");
			String mkt_username = (EnvironmentConfigurationsHandler.getValue(TemenosConstants.INF_WLTH_MKT_USER,
					request) != null
							? EnvironmentConfigurationsHandler.getValue(TemenosConstants.INF_WLTH_MKT_USER, request)
									.toString().trim()
							: "");
			String mkt_password = (EnvironmentConfigurationsHandler.getValue(TemenosConstants.INF_WLTH_MKT_PWD,
					request) != null
							? EnvironmentConfigurationsHandler.getValue(TemenosConstants.INF_WLTH_MKT_PWD, request)
									.toString().trim()
							: "");
			if (mkt_applicationID.equals("") || mkt_username.equals("") || mkt_password.equals("")) {
				diagnostic.prepareDebug(
						"==========> GetCurrencyGraphRefinitivOrchPreProcessor Refinitiv - Exiting refinitiv integration").log();
				return false;
			} else {
				diagnostic.prepareDebug(
						"==========> GetCurrencyGraphRefinitivOrchPreProcessor Refinitiv - Entering refinitiv integration").log();
				return true;
			}

		} catch (Exception e) {
			alert.prepareError("==========> GetCurrencyGraphRefinitivOrchPreProcessor Refinitiv - Error: " + e.getMessage()).log();
			return false;
		}

	}

}
