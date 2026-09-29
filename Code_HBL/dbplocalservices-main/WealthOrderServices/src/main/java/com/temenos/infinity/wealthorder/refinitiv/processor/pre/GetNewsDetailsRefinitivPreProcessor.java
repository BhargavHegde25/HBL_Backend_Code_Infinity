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
 * @author himaja.sridhar
 *
 */
public class GetNewsDetailsRefinitivPreProcessor implements DataPreProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		diagnostic.prepareDebug("==========> GetNewsDetailsRefinitivPreProcessor Refinitiv - Entered ").log();
		try {
			String wealthCore = EnvironmentConfigurationsHandler.getValue(TemenosConstants.WEALTH_CORE, request);

			if (wealthCore != null
					&& (wealthCore.equalsIgnoreCase("TAP,Refinitiv") || wealthCore.equalsIgnoreCase("T24,Refinitiv"))) {
				inputMap.put("languageCode", "en");
				inputMap.put("instrumentCode", request.getParameter("instrumentCode").toString());
				diagnostic.prepareDebug("==========> GetNewsDetailsRefinitivPreProcessor Refinitiv - Entering integration ").log();
				return true;
			} else {
				result.addOpstatusParam("0");
				result.addHttpStatusCodeParam("200");
				result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				diagnostic.prepareDebug("==========> GetNewsDetailsRefinitivPreProcessor Refinitiv - Exiting integration ").log();
				return false;
			}
		} catch (Exception e) {
			alert.prepareError("==========> GetNewsDetailsRefinitivPreProcessor Refinitiv - Error: " + e.getMessage()).log();
			return false;
		}
	}

}
