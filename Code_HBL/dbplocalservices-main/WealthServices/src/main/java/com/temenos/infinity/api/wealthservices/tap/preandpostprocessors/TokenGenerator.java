/**
 * 
 */
package com.temenos.infinity.api.wealthservices.tap.preandpostprocessors;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;

/**
 * @author himaja.sridhar
 *
 */
public class TokenGenerator implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("rawtypes")
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		String backendToken = PortfolioWealthUtils.getTokenFromCache(request);
		if (StringUtils.isNotBlank(backendToken)) {
			request.addRequestParam_("Authorization", backendToken);
			request.addRequestParam_("x-channel", "PCK_TCIB_PM_DESKTOP");
			alert.prepareError("Token Generated From Cache").log();
		} else {
			TAPTokenGenPreProcessor obj = new TAPTokenGenPreProcessor();
			obj.execute(inputMap, request, response, result);
			alert.prepareError("New Token Generated").log();
		}
		return true;
	}

}
