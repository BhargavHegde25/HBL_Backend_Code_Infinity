package com.temenos.dbx.core.businessdelegate.api;

import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.PartyDTO;

public interface CoreCustomerBusinessDelegate extends BusinessDelegate {

	DBXResult saveCustomerFromParty(PartyDTO partyDTO, Map<String, Object> map, String coreURL);

	DBXResult updateCustomerfromParty(PartyDTO partyDTO, Map<String, Object> map, String coreURL);
	
	DBXResult saveCustomerFromDBX(CustomerDTO customerDTO, Map<String, Object> map, String coreURL);

	DBXResult updateCustomerfromDBX(CustomerDTO customerDTO, Map<String, Object> map, String coreURL);

    DBXResult activateCustomer(CustomerDTO customerDTO, Map<String, Object> map, String coreURL);
	
}
