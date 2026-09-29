package com.dbp.combinedaccessalerts.service;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.combinedaccessalerts.dbconnections.UpdateAndDeleteAccountPreferences;
import com.dbp.combinedaccessalerts.util.Constants;
import com.dbp.combinedaccessalerts.util.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;

public class ActivateCombinedAccess implements JavaService2 {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");
	private static String schemaname = null;
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		if (schemaname == null)
			schemaname = HelperMethods.getConfigProperty("DBX_SCHEMA_NAME");
		String customerId = request.getParameter(Constants.NEWCUSTOMERID);
		String deactivatedccustomerId = request.getParameter(Constants.DEACTIVATEDCUSTOMERID);
		diagnostic.prepareDebug("newCustomerId "+request.getParameter(Constants.NEWCUSTOMERID)+" value "+customerId).log();
		diagnostic.prepareDebug("deactivated customerid "+request.getParameter(Constants.DEACTIVATEDCUSTOMERID)+" value "+deactivatedccustomerId).log();
		if (!HelperMethods.isValidInput(customerId) || !HelperMethods.isValidInput(deactivatedccustomerId)) {
			return HelperMethods.returnResult(false, Constants.INVALIDINPUT);
		}
		if (!UpdateAndDeleteAccountPreferences.updateAndDelete(customerId, deactivatedccustomerId,request)) {
			return HelperMethods.returnResult(false, Constants.ERROR);
		}
		return HelperMethods.returnResult(true, Constants.SUCCESS);
	}
	
	public static String getSchemaName() {
		return schemaname;
	}

}
