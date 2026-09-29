package com.temenos.infinity.wealthorder.t24.processor.pre;

import java.util.HashMap;
import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

public class GetNoDataReturnedPreProcessor implements DataPreProcessor2 {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "rawtypes" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		diagnostic.prepareDebug("==========> GetNoDataReturnedPreProcessor T24 - Entered ").log();
		String wealthCore = EnvironmentConfigurationsHandler.getValue(TemenosConstants.WEALTH_CORE, request);
		if (wealthCore != null
				&& (wealthCore.equalsIgnoreCase("T24,Refinitiv") || wealthCore.equalsIgnoreCase("T24"))) {
			diagnostic.prepareDebug("==========> GetNoDataReturnedPreProcessor T24 - Entering into integration").log();
			return true;
		} else {
			result.addOpstatusParam("0");
			result.addHttpStatusCodeParam("200");
			result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			diagnostic.prepareDebug("==========> GetNoDataReturnedPreProcessor T24 - Exiting integration ").log();
			return false;
		}
	}

}
