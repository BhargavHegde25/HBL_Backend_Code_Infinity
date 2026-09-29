package com.temenos.infinity.api.wealthOrder.tap.preandpostprocessors;

import java.util.HashMap;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.tap.preandpostprocessors.TAPTokenGenPreProcessor;
import com.temenos.infinity.api.wealthOrder.tap.preandpostprocessors.CancelDMAOrdersPreProcessor;

/**
 * (INFO) If status is set as a part of the request , the operation is exited
 * else operation is executed.
 * 
 * @author balaji.krishnan
 *
 */

public class CancelDMAOrdersPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("rawtypes")
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			if (request.getParameter("OrderID_Authentication") != null
					&& request.getParameter("OrderID_Authentication").equalsIgnoreCase("true")) {
				if (request.getParameter(TemenosConstants.OPSTATUS).equalsIgnoreCase("0")) {
					if ((request.getParameter("message") != null
							&& request.getParameter("message").length() > 0)
							|| (request.getParameter("errorDetails") != null
									&& request.getParameter("errorDetails").length() > 0)) {
						result.addOpstatusParam("0");
						result.addHttpStatusCodeParam("200");
						result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
						return false;
					} else {
						
						return true;
					}
				}
			} else {
				result.addOpstatusParam("0");
				result.addHttpStatusCodeParam("200");
				result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				return false;
			}
		} catch (Exception e) {
			alert.prepareError("Error in CancelNDMAOrdersPreProcessor" + e).log();
			e.getMessage();
			return false;
		}
		return false;
	}

}
