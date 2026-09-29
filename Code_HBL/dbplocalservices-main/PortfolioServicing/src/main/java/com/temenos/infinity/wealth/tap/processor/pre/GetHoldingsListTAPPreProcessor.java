/**
 * 
 */
package com.temenos.infinity.wealth.tap.processor.pre;

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
import com.temenos.infinity.api.wealthservices.tap.preandpostprocessors.TAPTokenGenPreProcessor;
/**
 * @author balaji.krishnan
 *
 */
public class GetHoldingsListTAPPreProcessor implements DataPreProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			diagnostic.prepareDebug("==========> GetHoldingsListTAPPreProcessor TAP - Entered ").log();
			if (request.getParameter(TemenosConstants.WEALTH_CORE) != null
					&& (request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("TAP,Refinitiv")
							|| request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("TAP"))) {

			TAPTokenGenPreProcessor obj = new TAPTokenGenPreProcessor();
			obj.execute(inputMap, request, response, result);
			diagnostic.prepareDebug("==========>  GetHoldingsListTAPPreProcessor TAP - Token Generation Succeeded").log();
			String isIncludeOrders = request.getParameter(TemenosConstants.ISINCLUEORDERS);
			//String isIncludeOrders = "false";// disabling includeOrders implementation for tap
			if(isIncludeOrders.equalsIgnoreCase("true")) {
				inputMap.put("minStatusE", "Cancelled");
				request.addRequestParam_(TemenosConstants.ISINCLUEORDERS,"true");
			}else {
				inputMap.put("minStatusE", "Accounted");
				request.addRequestParam_(TemenosConstants.ISINCLUEORDERS,"false");
			}
			inputMap.put("maxStatusE", "Accounted");
			diagnostic.prepareDebug("==========>  GetHoldingsListTAPPreProcessor TAP - End:Manipulation of input parameters").log();
			return true;
			}
			else {
				result.addOpstatusParam("0");
				result.addHttpStatusCodeParam("200");
				result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				diagnostic.prepareDebug("==========>  GetHoldingsListTAPPreProcessor TAP - Exiting without Token Generation").log();
				return false;
			}
		} catch (Exception e) {
			alert.prepareError("==========>  GetHoldingsListTAPPreProcessor TAP - Error: " + e.getMessage()).log();
			e.getMessage();
		}
		return false;
	}
}