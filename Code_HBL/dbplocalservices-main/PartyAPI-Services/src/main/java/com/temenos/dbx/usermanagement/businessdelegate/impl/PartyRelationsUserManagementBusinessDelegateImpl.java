package com.temenos.dbx.usermanagement.businessdelegate.impl;

import java.util.Map;

import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonObject;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.PartyDTO;
import com.temenos.dbx.usermanagement.backenddelegate.api.PartyRelationsUserManagementBackendDelegate;
import com.temenos.dbx.usermanagement.backenddelegate.api.PartyUserManagementBackendDelegate;
import com.temenos.dbx.usermanagement.businessdelegate.api.PartyRelationsUserManagementBusinessDelegate;

public class PartyRelationsUserManagementBusinessDelegateImpl implements PartyRelationsUserManagementBusinessDelegate 
{

	@Override
	public DBXResult create(JsonObject partyRelationsObj, Map<String, Object> headerMap) 
	{
		PartyRelationsUserManagementBackendDelegate managementBackendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BackendDelegateFactory.class)
				.getBackendDelegate(PartyRelationsUserManagementBackendDelegate.class);

		return managementBackendDelegate.create(partyRelationsObj, headerMap);

	}
	
	@Override
	public DBXResult get(JsonObject partyRelationsObj, Map<String, Object> headerMap) {
		PartyRelationsUserManagementBackendDelegate managementBackendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BackendDelegateFactory.class)
				.getBackendDelegate(PartyRelationsUserManagementBackendDelegate.class);

		return managementBackendDelegate.get(partyRelationsObj, headerMap);

	}

	@Override	
	public DBXResult update(JsonObject partyRelationsobj, Map<String, Object> headerMap) {

		PartyRelationsUserManagementBackendDelegate managementBackendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BackendDelegateFactory.class)
				.getBackendDelegate(PartyRelationsUserManagementBackendDelegate.class);

		return managementBackendDelegate.update(partyRelationsobj, headerMap);

	}

}
