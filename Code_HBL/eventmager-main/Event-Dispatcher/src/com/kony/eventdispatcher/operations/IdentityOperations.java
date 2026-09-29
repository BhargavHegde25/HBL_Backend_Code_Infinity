package com.kony.eventdispatcher.operations;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.utils.HelperMethods;
import com.kony.utils.URLConstants;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.controller.DataControllerRequest;

public class IdentityOperations {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	public static String getCustomerIdFromSession(DataControllerRequest request) {
		try {
			String authkey = request.getHeader("X-Kony-Authorization");
			String customerId = HelperMethods.getParamFromIToken(authkey, URLConstants.PROVIDER_USER_ID);
			if (StringUtils.isBlank(customerId))
				customerId = request.getServicesManager().getIdentityHandler().getUserAttributes().get("user_id")
						.toString();
			return customerId;

		} catch (Exception e) {
			diagnostic.prepareDebug(URLConstants.EXCEPTION, e).log();
		}
		return null;

	}

	public static String getCustomerIdFromSession(FabricRequestManager fabricRequestManager) {
		String customerId = null;
		try {

			String authKey = fabricRequestManager.getHeadersHandler().getHeader(URLConstants.XKONYAUTHORIZATION);
			customerId = HelperMethods.getParamFromIToken(authKey, URLConstants.PROVIDER_USER_ID);
			if ((StringUtils.isBlank(customerId) || customerId.equalsIgnoreCase("anonymous"))
					&& fabricRequestManager.getServicesManager() != null
					&& fabricRequestManager.getServicesManager().getIdentityHandler() != null) {
				customerId = fabricRequestManager.getServicesManager().getIdentityHandler().getUserId();
			}
		} catch (Exception e) {
			alert.prepareError(URLConstants.EXCEPTION, e).log();
		}
		return customerId;
	}

}
