/**
 * 
 */
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
 * @author himaja.sridhar
 *
 */
public class GetOrdersDetailsT24PreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			diagnostic.prepareDebug("==========> GetOrdersDetailsT24PreProcessor T24 - Entered ").log();
			if (request.getParameter(TemenosConstants.WEALTH_CORE) != null
					&& (request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("T24,Refinitiv")
							|| request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("T24"))) {
				inputMap.put("fromDate", request.getParameter("startDate") != null ? request.getParameter("startDate").toString() : "");
				inputMap.put("toDate", request.getParameter("endDate")!=null ? request.getParameter("endDate").toString() : "");
				diagnostic.prepareDebug("==========> GetOrdersDetailsT24PreProcessor T24 - Token Generation").log();
				return true;
			} else {
				result.addOpstatusParam("0");
				result.addHttpStatusCodeParam("200");
				result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				diagnostic.prepareDebug("==========> GetOrdersDetailsT24PreProcessor T24 - Exiting without Token Generation").log();
				return false;
			}

		} catch (Exception e) {
			alert.prepareError("==========> GetOrdersDetailsT24PreProcessor T24 - Error: " + e.getMessage()).log();
			return false;
		}
	}

}
