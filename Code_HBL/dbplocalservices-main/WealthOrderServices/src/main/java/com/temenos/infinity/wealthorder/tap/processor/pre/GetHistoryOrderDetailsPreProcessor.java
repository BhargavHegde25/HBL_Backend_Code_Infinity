package com.temenos.infinity.wealthorder.tap.processor.pre;

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
 * (INFO) If status is set as a part of the request , the operation is exited
 * else operation is executed.
 * 
 * @author muthukumarv
 *
 */

public class GetHistoryOrderDetailsPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			diagnostic.prepareDebug("==========>  GetHistoryOrderDetailsPreProcessor TAP - Entered").log();
			if (request.getParameter(TemenosConstants.ORDERSVIEW_TYPE) != null
					&& request.getParameter(TemenosConstants.ORDERSVIEW_TYPE).equalsIgnoreCase("history")
					|| ((request.getParameter("pendingOrderID_Authentication") != null
							&& request.getParameter("pendingOrderID_Authentication").equalsIgnoreCase("false")))) {

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
				TAPTokenGenPreProcessor obj = new TAPTokenGenPreProcessor();
				obj.execute(inputMap, request, response, result);
				diagnostic.prepareDebug("==========>  GetHistoryOrderDetailsPreProcessor TAP - Token Generation Succeeded").log();
				return true;

			} else {
				result.addOpstatusParam("0");
				result.addHttpStatusCodeParam("200");
				result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				diagnostic.prepareDebug("==========>  GetHistoryOrderDetailsPreProcessor TAP - Exiting without Token Generation").log();
				return false;
			}

		} catch (Exception e) {
			alert.prepareError("==========>  GetHistoryOrderDetailsPreProcessor TAP - Error: " + e.getMessage()).log();
			e.getMessage();
		}
		return false;
	}

}
