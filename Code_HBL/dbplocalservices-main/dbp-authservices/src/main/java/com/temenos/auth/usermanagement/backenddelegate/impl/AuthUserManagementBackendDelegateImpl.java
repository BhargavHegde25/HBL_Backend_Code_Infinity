package com.temenos.auth.usermanagement.backenddelegate.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.auth.usermanagement.backenddelegate.api.AuthUserManagementBackendDelegate;

public class AuthUserManagementBackendDelegateImpl implements AuthUserManagementBackendDelegate {

	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Result getCustomerActiveLegalEntities(String customerId) throws ApplicationException {
		if(StringUtils.isNotBlank(customerId)) {
			String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + customerId
					+ DBPUtilitiesConstants.AND
					+ "Status_id" + DBPUtilitiesConstants.EQUAL + "SID_CUS_ACTIVE";
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put(DBPUtilitiesConstants.FILTER, filter);
			JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(inputParams, null, 
					URLConstants.CUSTOMERLEGALENTITY_GET);
			if(!response.isJsonNull()) {
				return JSONToResult.convert(response.toString());
			}
		}
		diagnostic.prepareDebug("either customer id or response is null").log();
		return null;
	}

}
