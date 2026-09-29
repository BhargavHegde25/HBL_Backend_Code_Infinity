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
public class GetHistoryOrdersTAPPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			diagnostic.prepareDebug("==========> GetHistoryOrdersTAPPreProcessor TAP - Entered ").log();
			if (request.getParameter(TemenosConstants.WEALTH_CORE) != null
					&& (request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("TAP,Refinitiv")
							|| request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("TAP"))) {
				if (request.getParameter(TemenosConstants.ORDERSVIEW_TYPE) != null
						&& request.getParameter(TemenosConstants.ORDERSVIEW_TYPE).equalsIgnoreCase("history")
						|| ((request.getParameter("pendingOrderID_Authentication") != null
								&& request.getParameter("pendingOrderID_Authentication").equalsIgnoreCase("false")))) {
					diagnostic.prepareDebug("==========>  GetHistoryOrdersTAPPreProcessor TAP - Begin:Manipulation of input parameters, Type:History").log();
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
					inputMap.put("minStatusE", "Executed");
					inputMap.put("maxStatusE", "Executed");
					inputMap.put("calcFromD", request.getParameter(TemenosConstants.STARTDATE));
					inputMap.put("calcTillD", request.getParameter(TemenosConstants.ENDDATE));
					diagnostic.prepareDebug("==========>  GetHistoryOrdersTAPPreProcessor TAP - End:Manipulation of input parameters, Type:History").log();
					TAPTokenGenPreProcessor obj = new TAPTokenGenPreProcessor();
					obj.execute(inputMap, request, response, result);
					diagnostic.prepareDebug("==========>  GetHistoryOrdersTAPPreProcessor TAP - Token Generation Succeeded").log();
					return true;

				} else {
					result.addOpstatusParam("0");
					result.addHttpStatusCodeParam("200");
					result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
					diagnostic.prepareDebug("==========>  GetHistoryOrdersTAPPreProcessor TAP - Not Order Type - History").log();
					return false;
				}
			} else {
				result.addOpstatusParam("0");
				result.addHttpStatusCodeParam("200");
				result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				diagnostic.prepareDebug("==========>  GetHistoryOrdersTAPPreProcessor TAP - Exiting without Token Generation").log();
				return false;
			}

		} catch (Exception e) {
			alert.prepareError("==========>  GetHistoryOrdersTAPPreProcessor TAP - Error: " + e.getMessage()).log();
			e.getMessage();
		}
		return false;
	}

}
