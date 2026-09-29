package com.temenos.infinity.wealthorder.mock.processor.pre;

import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.wealthorder.common.util.OrderServiceUtils;

public class UpdateWatchlistDBPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("rawtypes")
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		diagnostic.prepareDebug("==========> GetWatchlistDBPreProcessor Mock - Entered ").log();
		// TODO Auto-generated method stub
		// Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		try {
			Object ricObj = request.getParameter(TemenosConstants.RICCODE);
			Object instrumentObj = request.getParameter(TemenosConstants.INSTRUMENTID);
			Object operationObj = request.getParameter(TemenosConstants.OPERATION);
			String ricCode = null, instrumentId = null, operation = null, application = null;
			String userId = HelperMethods.getUserIdFromSession(request);
			// Map<String, Object> customer = CustomerSession.getCustomerMap(request);
			// String customerId = CustomerSession.getCustomerId(customer);
			// String customerId = HelperMethods.getCustomerIdFromSession(request);
			if (userId == null || userId.equals("")) {
				diagnostic.prepareDebug("==========> GetWatchlistDBPreProcessor Mock -Error:: invalid userid").log();

				return OrderServiceUtils.unauthAccess(result, TemenosConstants.USERID);
			}
			if (operationObj == null || operationObj.equals("")
					|| !(request.getParameter(TemenosConstants.OPERATION).toString().equalsIgnoreCase("ADD") || request
							.getParameter(TemenosConstants.OPERATION).toString().equalsIgnoreCase("REMOVE"))) {
				diagnostic.prepareDebug("==========> GetWatchlistDBPreProcessor Mock -Error:: invalid operation").log();
				return OrderServiceUtils.unauthAccess(result, TemenosConstants.OPERATION);
			}
			if (ricObj != null) {
				ricCode = request.getParameter(TemenosConstants.RICCODE).toString();
				request.addRequestParam_(TemenosConstants.RICCODE, ricCode);
			} else {
				ricCode = "";
				request.addRequestParam_(TemenosConstants.RICCODE, ricCode);
			}
			if (operationObj != null) {
				operation = request.getParameter(TemenosConstants.OPERATION).toString();
			}
			if (instrumentObj != null) {
				instrumentId = request.getParameter(TemenosConstants.INSTRUMENTID).toString();
				request.addRequestParam_(TemenosConstants.INSTRUMENTID, instrumentId);
			} else {
				instrumentId = "";
				request.addRequestParam_(TemenosConstants.INSTRUMENTID, instrumentId);
			}

			if (request.getParameter(TemenosConstants.APPLICATION) != null
					&& request.getParameter(TemenosConstants.APPLICATION).toString().length() > 0) {
				application = request.getParameter(TemenosConstants.APPLICATION).toString();
				instrumentId = (instrumentId + "~" + application).trim();
				request.addRequestParam_(TemenosConstants.INSTRUMENTID, instrumentId);
			}
			diagnostic.prepareDebug("==========> GetWatchlistDBPreProcessor Mock - Entering into mock integration ").log();
			return true;
		} catch (Exception e) {
			alert.prepareError("==========> GetWatchlistDBPreProcessor Mock - Error: " + e.getMessage()).log();
			e.getMessage();
			return false;
		}
	}

}
