package com.dbp.combinedaccessalerts.service;

import com.dbp.combinedaccessalerts.dbconnections.DelinkAccountPreferences;
import com.dbp.combinedaccessalerts.util.Constants;
import com.dbp.combinedaccessalerts.util.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;

public class DeactivateCombinedAccess implements JavaService2 {

	private static String schemaname = null;
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		if (schemaname == null)
			schemaname = HelperMethods.getConfigProperty("DBX_SCHEMA_NAME");
		String combinedCustomerId = request.getParameter(Constants.COMBINEDCUSTOMERID);
		String newCustomerId = request.getParameter(Constants.NEWCUSTOMERID);
		String accountIds = request.getParameter(Constants.ACCOUNTIDS);
		if (!HelperMethods.isValidInput(combinedCustomerId) || !HelperMethods.isValidInput(newCustomerId)
				|| !HelperMethods.isValidInput(accountIds)) {
			return HelperMethods.returnResult(false, Constants.INVALIDINPUT);
		}
		if (!DelinkAccountPreferences.update(combinedCustomerId, newCustomerId, accountIds, request)) {
			return HelperMethods.returnResult(false, Constants.ERROR);
		}
		return HelperMethods.returnResult(true, Constants.SUCCESS);

	}
	public static String getSchemaName() {
		return schemaname;
	}

}
