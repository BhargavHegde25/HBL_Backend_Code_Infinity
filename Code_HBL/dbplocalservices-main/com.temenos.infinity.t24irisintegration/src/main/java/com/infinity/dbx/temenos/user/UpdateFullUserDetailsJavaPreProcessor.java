package com.infinity.dbx.temenos.user;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class UpdateFullUserDetailsJavaPreProcessor extends TemenosBasePreProcessor {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {

		if (StringUtils.isNotEmpty(request.getParameter(UserConstants.PARTY_ID))) {
			try {
				// Get customer id for the given party id
				Map<String, Object> svcParams = new HashMap<String, Object>();
				svcParams.put(Constants.PARAM_DOLLAR_FILTER,
						"BackendId eq " + request.getParameter(UserConstants.PARTY_ID));
				Result getCustomerIdResult = CommonUtils.callIntegrationService(request, svcParams, null,
						Constants.DBX_DB_SERVICE_NAME, Constants.DBX_DB_BACKEND_IDENTIFIER_GET, false);
				Record backendIdentifierRec = getCustomerIdResult.getDatasetById(Constants.DS_BACKEND_IDENTIFIER)
						.getRecord(0);
				String customerId = backendIdentifierRec.getParamValueByName("Customer_id");

				// Get core id for the above customer id
				svcParams = new HashMap<String, Object>();
				svcParams.put(Constants.PARAM_DOLLAR_FILTER, "BackendType eq T24 and Customer_id eq " + customerId);
				Result getCoreIdResult = CommonUtils.callIntegrationService(request, svcParams, null,
						Constants.DBX_DB_SERVICE_NAME, Constants.DBX_DB_BACKEND_IDENTIFIER_GET, false);
				Record coreIdentifierRec = getCoreIdResult.getDatasetById(Constants.DS_BACKEND_IDENTIFIER).getRecord(0);
				String coreId = coreIdentifierRec.getParamValueByName("BackendId");
				request.addRequestParam_(UserConstants.CORE_CUSTOMER_ID, coreId);
			} catch (Exception exception) {
				alert.prepareError("Error in UpdateFullUserDetailsJavaPreProcessor:" + exception.toString()).log();
			}
		}

		if (StringUtils.isEmpty(request.getParameter(UserConstants.CORE_CUSTOMER_ID))) {
			result.addOpstatusParam(0);
			result.addHttpStatusCodeParam(200);
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

}
