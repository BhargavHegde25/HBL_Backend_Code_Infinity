package com.temenos.infinity.wealth.t24.processor.pre;

import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * (INFO) If status is set as a part of the request , the operation is exited
 * else operation is executed.
 *
 */

public class GetHoldingsListT24PreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("rawtypes")
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			diagnostic.prepareDebug("==========> GetHoldingsListT24PreProcessor T24 - Entered ").log();
			if (request.getParameter(TemenosConstants.WEALTH_CORE) != null
					&& (request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("T24,Refinitiv")
							|| request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("T24"))) {
				/*
				 * String backendToken =
				 * TokenGenerator.generateAuthToken(T24CertificateConstants.BACKEND,
				 * ServerConfigurations.DBP_HOST_URL.getValue(),
				 * PortfolioWealthUtils.getUserAttributeFromIdentity(request, "UserName"),
				 * PortfolioWealthUtils.getUserAttributeFromIdentity(request, "customer_id"),
				 * T24CertificateConstants.ROLEID, T24CertificateConstants.TOKEN_VALIDITY,
				 * true);
				 * 
				 * request.addRequestParam_(TemenosConstants.AUTHORIZATION, backendToken);
				 * request.addRequestParam_("onlineUpdate", "Y");
				 */
				request.addRequestParam_("onlineUpdate", "Y");
				diagnostic.prepareDebug("==========> GetHoldingsListT24PreProcessor T24 - Token Generation").log();
				return true;
			} else {
				result.addOpstatusParam("0");
				result.addHttpStatusCodeParam("200");
				result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				diagnostic.prepareDebug("==========> GetHoldingsListT24PreProcessor T24 - Exiting without Token Generation").log();
				return false;
			}

		} catch (Exception e) {
			alert.prepareError("==========> GetHoldingsListT24PreProcessor T24 - Error: " + e.getMessage()).log();
			return false;
		}
	}

}
