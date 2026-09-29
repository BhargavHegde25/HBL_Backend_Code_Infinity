package com.temenos.dbx.core.backenddelegate.api;

import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.PartyDTO;

public interface CoreCustomerBackendDelegate extends BackendDelegate {

	DBXResult saveCustomerFromParty(PartyDTO partyDTO, Map<String, Object> map, String coreURL);

	DBXResult updateCustomerfromParty(PartyDTO partyDTO, Map<String, Object> map, String coreURL);
	
	DBXResult saveCustomerFromDBX(CustomerDTO customerDTO, Map<String, Object> map, String coreURL);

	DBXResult updateCustomerfromDBX(CustomerDTO customerDTO, Map<String, Object> map, String coreURL);

    DBXResult activateCustomer(CustomerDTO customerDTO, Map<String, Object> map, String coreURL);
	
}
