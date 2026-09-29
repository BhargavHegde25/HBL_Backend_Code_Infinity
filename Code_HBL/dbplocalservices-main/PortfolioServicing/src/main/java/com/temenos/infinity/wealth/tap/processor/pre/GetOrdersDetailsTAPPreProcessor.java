/**
 * 
 */
package com.temenos.infinity.wealth.tap.processor.pre;

import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.tap.preandpostprocessors.TAPTokenGenPreProcessor;

/**
 * @author himaja.sridhar
 *
 */
public class GetOrdersDetailsTAPPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			diagnostic.prepareDebug("==========> GetOrdersDetailsTAPPreProcessor TAP - Entered ").log();
			if (request.getParameter(TemenosConstants.WEALTH_CORE) != null
					&& (request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("TAP,Refinitiv")
							|| request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("TAP"))) {
				diagnostic.prepareDebug("==========>  GetOrdersDetailsTAPPreProcessor TAP - Begin:Manipulation of input parameters").log();
				// added for sorting
				String orderBy = inputMap.get(TemenosConstants.ORDERBY) != null
						? inputMap.get(TemenosConstants.ORDERBY).toString()
						: "";
				String orderType = inputMap.get(TemenosConstants.SORTTYPE) != null
						? inputMap.get(TemenosConstants.SORTTYPE).toString().toLowerCase()
						: "";
				if (orderBy.length() > 0) {
					orderBy = orderBy.concat("%20").concat(orderType);
					inputMap.put(TemenosConstants.ORDERBY, orderBy);
				} else {
					inputMap.put(TemenosConstants.ORDERBY, "");
				}
				inputMap.put("minStatusE", "Cancelled");
				inputMap.put("maxStatusE", "Accounted");
				diagnostic.prepareDebug("==========>  GetOrdersDetailsTAPPreProcessor TAP - End:Manipulation of input parameters").log();
				TAPTokenGenPreProcessor obj = new TAPTokenGenPreProcessor();
				obj.execute(inputMap, request, response, result);
				diagnostic.prepareDebug("==========>  GetOrdersDetailsTAPPreProcessor TAP - Token Generation Succeeded").log();
				return true;
			} else {
				result.addOpstatusParam("0");
				result.addHttpStatusCodeParam("200");
				result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				diagnostic.prepareDebug("==========>  GetOrdersDetailsTAPPreProcessor TAP - Exiting without Token Generation").log();
				return false;
			}
		} catch (Exception e) {
			alert.prepareError("==========>  GetOrdersDetailsTAPPreProcessor TAP - Error: " + e.getMessage()).log();
			e.getMessage();
		}
		return false;
	}

}
