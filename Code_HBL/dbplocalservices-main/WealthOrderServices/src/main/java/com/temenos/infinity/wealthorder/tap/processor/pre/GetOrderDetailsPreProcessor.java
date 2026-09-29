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
 * @author balaji.krishnan
 *
 */

public class GetOrderDetailsPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			diagnostic.prepareDebug("==========>  GetOrderDetailsPreProcessor TAP - Entered").log();
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
			TAPTokenGenPreProcessor obj = new TAPTokenGenPreProcessor();
			obj.execute(inputMap, request, response, result);
			diagnostic.prepareDebug("==========>  GetOrderDetailsPreProcessor TAP - Token Generation Succeeded").log();
			return true;
		} catch (Exception e) {
			alert.prepareError("==========>  GetOrderDetailsPreProcessor TAP - Error: " + e.getMessage()).log();
			e.getMessage();
		}
		return false;
	}

}
