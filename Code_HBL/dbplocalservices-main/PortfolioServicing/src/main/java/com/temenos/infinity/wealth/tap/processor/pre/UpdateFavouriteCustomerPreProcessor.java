package com.temenos.infinity.wealth.tap.processor.pre;

import java.util.HashMap;
import com.kony.dbputilities.util.HelperMethods;
import org.apache.logging.log4j.LogManager;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * @author muthukumarv
 *
 */
public class UpdateFavouriteCustomerPreProcessor implements DataPreProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("rawtypes")
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {

			Object backendIdObj = request.getParameter(TemenosConstants.BACKENDID);
			Object operationObj = request.getParameter(TemenosConstants.OPERATION);
			String backendId = null;
			String customerId = HelperMethods.getCustomerIdFromSession(request);
			// Map<String, Object> customer = CustomerSession.getCustomerMap(request);
			// String customerId = CustomerSession.getCustomerId(customer);
			// String customerId = HelperMethods.getCustomerIdFromSession(request);
			if (customerId == null || customerId.equals("")) {
				// diagnostic.prepareDebug("==========> GetWatchlistDBPreProcessor Mock -Error::
				// invalid userid").log();

				return unauthAccess(result, TemenosConstants.USERID);
			}
			if (operationObj == null || operationObj.equals("")
					|| !(request.getParameter(TemenosConstants.OPERATION).toString().equalsIgnoreCase("ADD") || request
							.getParameter(TemenosConstants.OPERATION).toString().equalsIgnoreCase("REMOVE"))) {
				// diagnostic.prepareDebug("==========> GetWatchlistDBPreProcessor Mock -Error::
				// invalid operation").log();
				return unauthAccess(result, TemenosConstants.OPERATION);
			}
			if (backendIdObj == null || backendIdObj.equals("")) {
				// diagnostic.prepareDebug("==========> GetWatchlistDBPreProcessor Mock -Error::
				// invalid operation").log();
				return unauthAccess(result, TemenosConstants.BACKENDID);
			}
			if (backendIdObj != null) {
				backendId = request.getParameter("backendId").toString();
				request.addRequestParam_("backendId", backendId);
			} else {
				backendId = "";
				request.addRequestParam_("backendId", backendId);
			}
			return true;
		} catch (Exception e) {
			alert.prepareError("==========> GetWatchlistDBPreProcessor Mock - Error: " + e.getMessage()).log();
			e.getMessage();
			return false;
		}
	}

	public static boolean unauthAccess (Result result, String param) {
		alert.prepareError("Error:Invalid input. Mandatory fields not given").log();
		result.addParam("status", "Failure");
		result.addParam("error", "Invalid Input! " + param + " is mandatory.");
		result.addHttpStatusCodeParam("200");
		return false;

	}

}
