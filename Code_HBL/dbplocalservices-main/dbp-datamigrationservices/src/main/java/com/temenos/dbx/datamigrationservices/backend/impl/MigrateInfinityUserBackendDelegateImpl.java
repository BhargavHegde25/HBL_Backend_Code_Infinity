package com.temenos.dbx.datamigrationservices.backend.impl;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.datamigrationservices.backend.api.MigrateInfinityUserBackendDelegate;
import com.temenos.dbx.product.utils.InfinityConstants;

public class MigrateInfinityUserBackendDelegateImpl implements MigrateInfinityUserBackendDelegate {

	@Override
	public JsonObject getCoreCustomerAccountDetails(String coreCustomerId, String legalEntityId,
			DataControllerRequest request) {
		return new JsonObject();
	}

	@Override
	public JsonObject getContractUsers(String contractId, DataControllerRequest request) {
		Map<String, Object> inputParams = new HashMap<>();
        String filter = InfinityConstants.contractId + DBPUtilitiesConstants.EQUAL + contractId;
        inputParams.put(DBPUtilitiesConstants.FILTER,filter);
        return ServiceCallHelper.invokeServiceAndGetJson(inputParams, request.getHeaderMap(),
                URLConstants.CONTRACT_CUSTOMERS_GET);
	}

}
