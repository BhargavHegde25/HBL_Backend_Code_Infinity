/**
 * 
 */
package com.temenos.infinity.wealthorder.mock.processor.pre;

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
public class ModifyOrderMockPreProcessor implements DataPreProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("rawtypes")
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		diagnostic.prepareDebug("==========> ModifyOrderMockPreProcessor Mock - Entered ").log();
		try {
			String wealthCore = EnvironmentConfigurationsHandler.getValue(TemenosConstants.WEALTH_CORE, request);
			result.addParam(TemenosConstants.WEALTH_CORE, wealthCore);
			if (wealthCore != null && (wealthCore.equalsIgnoreCase("Mock"))) {
				diagnostic.prepareDebug("==========> ModifyOrderMockPreProcessor Mock - Entering into mock integration ").log();
				return true;
			} else {
				result.addOpstatusParam("0");
				result.addHttpStatusCodeParam("200");
				result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				diagnostic.prepareDebug("==========> ModifyOrderMockPreProcessor Mock - Exiting into mock integration ").log();
				return false;
			}
		} catch (Exception e) {
			alert.prepareError("==========> ModifyOrderMockPreProcessor Mock - Error: " + e.getMessage()).log();
			e.getMessage();
			return false;
		}
	}

}
