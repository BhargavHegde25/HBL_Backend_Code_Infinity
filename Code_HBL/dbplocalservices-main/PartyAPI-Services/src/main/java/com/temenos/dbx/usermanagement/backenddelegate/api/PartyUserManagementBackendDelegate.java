package com.temenos.dbx.usermanagement.backenddelegate.api;

import java.util.Map;

import org.json.JSONObject;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.PartyDTO;
import com.temenos.dbx.usermanagement.dto.PartySearchDTO;

public interface PartyUserManagementBackendDelegate extends BackendDelegate {

    public DBXResult update(PartyDTO partyDTO, Map<String, Object> headerMap);
    
    public DBXResult get(PartyDTO partyDTO, Map<String, Object> headerMap);

	public DBXResult create(PartyDTO partyDTO, Map<String, Object> headerMap);

    public DBXResult searchParty(PartySearchDTO searchDTO, Map<String, Object> headers);

	DBXResult GetPartyData(PartyDTO partyDTO, Map<String, Object> headerMap);
    
	public String getT24Customer(String coreCustomerId, DataControllerRequest request);
}
