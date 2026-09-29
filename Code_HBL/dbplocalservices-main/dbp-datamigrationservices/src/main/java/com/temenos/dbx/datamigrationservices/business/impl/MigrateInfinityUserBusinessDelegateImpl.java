package com.temenos.dbx.datamigrationservices.business.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonObject;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.datamigrationservices.backend.api.MigrateInfinityUserBackendDelegate;
import com.temenos.dbx.datamigrationservices.business.api.MigrateInfinityUserBusinessDelegate;

public class MigrateInfinityUserBusinessDelegateImpl implements MigrateInfinityUserBusinessDelegate {

	@Override
	public JsonObject getCoreCustomerAccountDetails(String coreCustomerId, String legalEntityId,
			DataControllerRequest request) {
		JsonObject result = new JsonObject();
		MigrateInfinityUserBackendDelegate migrationDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(MigrateInfinityUserBackendDelegate.class);
		migrationDelegate.getCoreCustomerAccountDetails(coreCustomerId, legalEntityId, request);
		return result;
	}

	@Override
	public JsonObject getContractUsers(String contractId, DataControllerRequest request) {
		MigrateInfinityUserBackendDelegate migrationDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(MigrateInfinityUserBackendDelegate.class);
		return migrationDelegate.getContractUsers(contractId, request);
	}

}
