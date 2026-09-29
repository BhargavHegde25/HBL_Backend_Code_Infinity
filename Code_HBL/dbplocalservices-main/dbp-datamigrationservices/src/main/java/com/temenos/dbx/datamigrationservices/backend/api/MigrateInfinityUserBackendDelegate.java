package com.temenos.dbx.datamigrationservices.backend.api;

import com.dbp.core.api.BackendDelegate;
import com.google.gson.JsonObject;
import com.konylabs.middleware.controller.DataControllerRequest;

public interface MigrateInfinityUserBackendDelegate extends BackendDelegate {
	
	/**
	 * @param coreCustomerId
	 * @param legalEntityId
	 * @param request
	 * @return JsonObject
	 */
	public JsonObject getCoreCustomerAccountDetails(String coreCustomerId, String legalEntityId, DataControllerRequest request);

	/**
	 * @param contractId
	 * @param request
	 * @return JsonObject
	 */
	public JsonObject getContractUsers(String contractId, DataControllerRequest request);

}
