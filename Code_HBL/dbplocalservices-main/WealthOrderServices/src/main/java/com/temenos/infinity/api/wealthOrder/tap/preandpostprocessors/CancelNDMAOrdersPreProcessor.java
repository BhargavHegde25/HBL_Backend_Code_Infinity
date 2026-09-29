package com.temenos.infinity.api.wealthOrder.tap.preandpostprocessors;

import java.util.HashMap;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.tap.preandpostprocessors.TAPTokenGenPreProcessor;
import com.temenos.infinity.api.wealthOrder.tap.preandpostprocessors.CancelNDMAOrdersPreProcessor;

/**
 * (INFO) If status is set as a part of the request , the operation is exited
 * else operation is executed.
 * 
 * @author balaji.krishnan
 *
 */

public class CancelNDMAOrdersPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("rawtypes")
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			if (request.getParameter("OrderID_Authentication") != null
					&& request.getParameter("OrderID_Authentication").equalsIgnoreCase("true")) {
				if (inputMap.get(TemenosConstants.ASSETTYPE) != null
						&& StringUtils.isNotBlank(inputMap.get(TemenosConstants.ASSETTYPE).toString())) {
					String assetType = inputMap.get(TemenosConstants.ASSETTYPE).toString();
					if (assetType.equalsIgnoreCase("Fund Share")) {
						return true;
					} else {
						result.addOpstatusParam("0");
						result.addHttpStatusCodeParam("200");
						result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
						return false;
					}
				} else {
					result.addOpstatusParam("0");
					result.addHttpStatusCodeParam("200");
					result.addParam(TemenosConstants.STATUS, "Failure");
					result.addParam("error", "Invalid Input! " + TemenosConstants.ASSETTYPE + " is mandatory.");
					return false;
				}
			} else {
				result.addOpstatusParam("0");
				result.addHttpStatusCodeParam("200");
				result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				return false;
			}
		} catch (Exception e) {
			alert.prepareError("Error in CancelDMAOrdersPreProcessor" + e).log();
			e.getMessage();
			return false;
		}
	}

}
