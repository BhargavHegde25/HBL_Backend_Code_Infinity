package com.temenos.dbx.usermanagement.businessdelegate.impl;

import java.util.Map;

import org.json.JSONObject;

import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.PartyDTO;
import com.temenos.dbx.usermanagement.backenddelegate.api.PartyUserManagementBackendDelegate;
import com.temenos.dbx.usermanagement.businessdelegate.api.PartyUserManagementBusinessDelegate;
import com.temenos.dbx.usermanagement.dto.PartySearchDTO;

public class PartyUserManagementBusinessDelegateImpl implements PartyUserManagementBusinessDelegate {


	@Override
	public DBXResult update(PartyDTO partyDTO, Map<String, Object> headerMap) {

		PartyUserManagementBackendDelegate managementBackendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BackendDelegateFactory.class)
				.getBackendDelegate(PartyUserManagementBackendDelegate.class);

		return managementBackendDelegate.update(partyDTO, headerMap);

	}

	@Override
	public DBXResult get(PartyDTO partyDTO, Map<String, Object> headerMap) {
		PartyUserManagementBackendDelegate managementBackendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BackendDelegateFactory.class)
				.getBackendDelegate(PartyUserManagementBackendDelegate.class);

		return managementBackendDelegate.get(partyDTO, headerMap);

	}

	@Override
	public DBXResult create(PartyDTO partyDTO, Map<String, Object> headerMap) {

		PartyUserManagementBackendDelegate managementBackendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BackendDelegateFactory.class)
				.getBackendDelegate(PartyUserManagementBackendDelegate.class);

		return managementBackendDelegate.create(partyDTO, headerMap);

	}

    @Override
    public DBXResult searchParty(PartySearchDTO searchDTO, Map<String, Object> headers) {
        PartyUserManagementBackendDelegate managementBackendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BackendDelegateFactory.class)
                .getBackendDelegate(PartyUserManagementBackendDelegate.class);

        return managementBackendDelegate.searchParty(searchDTO, headers);
    }


	@Override
	public DBXResult GetPartyData(PartyDTO partyDTO, Map<String, Object> headerMap) {
		PartyUserManagementBackendDelegate managementBackendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BackendDelegateFactory.class)
				.getBackendDelegate(PartyUserManagementBackendDelegate.class);

		return managementBackendDelegate.GetPartyData(partyDTO, headerMap);

	}

	@Override
	public String getT24Customer(String coreCustomerId, DataControllerRequest request) {
		PartyUserManagementBackendDelegate managementBackendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BackendDelegateFactory.class)
				.getBackendDelegate(PartyUserManagementBackendDelegate.class);

		String response = managementBackendDelegate.getT24Customer(coreCustomerId, request);
		return response;
	}


}
