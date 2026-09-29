package com.temenos.dbx.usermanagement.businessdelegate.api;

import java.util.Map;
import com.dbp.core.api.BusinessDelegate;
import com.google.gson.JsonObject;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.PartyDTO;

public interface PartyRelationsUserManagementBusinessDelegate  extends BusinessDelegate 
{

	public DBXResult create(JsonObject partyRelationsObj, Map<String, Object> headerMap);
	
	public DBXResult get(JsonObject partyRelationsObj, Map<String, Object> headerMap);

	public DBXResult update(JsonObject partyRelationsObj, Map<String, Object> headerMap);

}
