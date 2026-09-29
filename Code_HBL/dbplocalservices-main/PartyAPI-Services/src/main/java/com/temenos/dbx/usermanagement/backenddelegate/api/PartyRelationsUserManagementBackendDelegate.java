package com.temenos.dbx.usermanagement.backenddelegate.api;

import java.util.Map;
import com.dbp.core.api.BackendDelegate;
import com.google.gson.JsonObject;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.PartyDTO;

public interface PartyRelationsUserManagementBackendDelegate extends BackendDelegate
{
	public DBXResult create(JsonObject partyRelationsObj, Map<String, Object> headerMap);
	
    public DBXResult get(JsonObject partyRelationsObj, Map<String, Object> headerMap);
    
    public DBXResult update(JsonObject partyRelationsobj, Map<String, Object> headerMap);
    
}
