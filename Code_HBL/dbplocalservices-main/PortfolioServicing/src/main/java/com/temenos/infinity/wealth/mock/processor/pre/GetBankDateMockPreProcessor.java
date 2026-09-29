/**
 * 
 */
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

/**
 * @author muthukumarv
 *
 */
public class GetBankDateMockPreProcessor implements DataPreProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("rawtypes")
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		diagnostic.prepareInfo("==========> GetBankDateMockPreProcessor Mock - Entered").log();
		try {
			String wealthCore = EnvironmentConfigurationsHandler.getValue(TemenosConstants.WEALTH_CORE, request);
			result.addParam(TemenosConstants.WEALTH_CORE, wealthCore);
			request.addRequestParam_(TemenosConstants.WEALTH_CORE, wealthCore);
			if (wealthCore != null && (wealthCore.equalsIgnoreCase("Mock"))) {
				diagnostic.prepareInfo("==========> GetBankDateMockPreProcessor Mock - Core check done").log();
				return true;
			} else {
				result.addOpstatusParam("0");
				result.addHttpStatusCodeParam("200");
				result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				diagnostic.prepareInfo("==========> GetBankDateMockPreProcessor Mock - Exiting ").log();
				return false;
			}
				
		} catch (Exception e) {
			alert.prepareError("==========> GetBankDateMockPreProcessor Mock - Error: " + e.getMessage()).log();
			e.getMessage();
			return false;
		}
	}

}
