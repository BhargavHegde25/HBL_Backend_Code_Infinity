package com.temenos.dbx.party.businessdelegate.api;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.BusinessDelegate;
import com.google.gson.JsonArray;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.PartyDTO;

public interface PartyCustomerBusinessDelegate extends BusinessDelegate {

    DBXResult saveCustomer(CustomerDTO customerDTO, Map<String, Object> map);

    DBXResult updateCustomer(CustomerDTO customerDTO, Map<String, Object> map);

    DBXResult getReferenceByID(String referenceByID, Map<String, Object> headerMap);
    
    DBXResult activateCustomer(String id, Map<String, Object> headers);

	DBXResult addIdentifier(PartyDTO party, Map<String, Object> headers);

	DBXResult addClassification(PartyDTO party, Map<String, Object> headers, boolean isClassificationUpdate);
	
	JSONArray getClassification(String partyId, Map<String, Object> headers);
	
	DBXResult addPartyRelation(String partyId ,JSONObject payload, Map<String, Object> headers);

	DBXResult getCustomer(String partyID, Map<String, Object> headers);

	DBXResult createEmployments(PartyDTO party, Map<String, Object> headers,boolean isUpdate);
	
	DBXResult updateIdentifier(String partyId,JsonArray partyIdentities,Map<String,Object> headers);
    
}
