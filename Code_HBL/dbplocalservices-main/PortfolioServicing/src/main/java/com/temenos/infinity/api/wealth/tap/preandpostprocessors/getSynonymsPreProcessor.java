/**
 * 
 */
package com.temenos.infinity.api.wealth.tap.preandpostprocessors;

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

/**
 * (INFO) Prepares the input for the TAP service in the desired format.
 * 
 * @author himaja.sridhar
 *
 */
public class getSynonymsPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
	
		inputMap.put("id", request.getParameter("id"));
		try {
			Map<String, String> inputparamMap = new HashMap<>();
			inputparamMap.put("userName","dbpolbuser");
			inputparamMap.put("customerId","1026540");
			String backendToken = TokenUtils.getPortfolioWealthMSAuthToken(inputparamMap);

			if (StringUtils.isBlank(backendToken)) {
				return false;
			}
			alert.prepareError("JWT TOKEN" +backendToken).log();
			backendToken = "Bearer ".concat(backendToken);
			request.addRequestParam_("Authorization", backendToken);
			request.addRequestParam_("x-channel", "PCK_TCIB_PM_DESKTOP");
		return true;
	}catch(Exception e) {}
		return false;
	}
	

}
