package com.temenos.dbx.usermanagement.backenddelegate.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.MWConstants;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.temenos.dbx.party.utils.PartyPropertyConstants;
import com.temenos.dbx.party.utils.PartyURLFinder;
import com.temenos.dbx.party.utils.PartyUtils;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.PartyDTO;
import com.temenos.dbx.product.usermanagement.backenddelegate.api.ProspectBackendDelegate;
import com.temenos.dbx.product.utils.DTOConstants;
import com.temenos.dbx.product.utils.HTTPOperations;

public class PartyProspectBackendDelegateImpl implements ProspectBackendDelegate {
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static LoggerUtil logger = new LoggerUtil(PartyProspectBackendDelegateImpl.class);

	@Override
	public DBXResult update(CustomerDTO customerDTO, Map<String, Object> headerMap) {
		// TODO Auto-generated method stub
		
		DBXResult dbxResult = new DBXResult();

        logger = new LoggerUtil(PartyProspectBackendDelegateImpl.class);

        String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL);
        
        String partyId = getPartyIdFromBackendIdentifier(customerDTO.getId(), headerMap);
        
        partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL)+
                PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_GET, partyId);
        
        DBXResult response =  HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET, partyURL, null, headerMap);
        PartyDTO  partyDTO = new PartyDTO();

        try {
            JsonObject jsonObject = new JsonParser().parse((String) response.getResponse()).getAsJsonObject();
            partyDTO.loadFromJson(jsonObject);        
        }catch (Exception e) {
            alert.prepareError("Caught exception while getting Party: ",  e).log();
        }

        partyDTO = PartyUtils.buildPartyDTO(customerDTO, partyDTO);

        String party = partyDTO.toStringJson().toString();

        diagnostic.prepareDebug("PartyDTO for update Party Service is : "+ party).log();

        partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL)+
                PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_UPDATE, partyId);
        
        response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.PUT, partyURL , party, headerMap);	

        String id = "";
        try {
            JsonObject jsonObject = new JsonParser().parse((String) response.getResponse()).getAsJsonObject();

            if(jsonObject.has("id")){

                id = jsonObject.get("id").getAsString();
            }
            else{
                if(jsonObject.has("message")) {
                    dbxResult.setDbpErrMsg(jsonObject.get("message").getAsString());
                }
                
                if(jsonObject.has("status")) {
                    dbxResult.setDbpErrCode(jsonObject.get("status").getAsString());
                }
            }


        }catch (Exception e) {
            alert.prepareError("Caught exception while updating Party: ",  e).log();
            dbxResult.setDbpErrMsg(response.getDbpErrMsg());
            return dbxResult;
        }

        dbxResult.setResponse(id);
        return dbxResult;
	}

	@Override
	public DBXResult create(CustomerDTO customerDTO, Map<String, Object> headerMap) {
		DBXResult dbxResult = new DBXResult();

        logger = new LoggerUtil(PartyProspectBackendDelegateImpl.class);

        String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL);
        
        PartyDTO  partyDTO = new PartyDTO();

        partyDTO = PartyUtils.buildPartyDTO(customerDTO, partyDTO);

        String party = partyDTO.toStringJson().toString();

        diagnostic.prepareDebug("PartyDTO for update Party Service is : "+ party).log();

        partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL)+
                PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_CREATE);
        
        DBXResult response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.POST, partyURL, party, headerMap);	

        String id = "";
        try {
            JsonObject jsonObject = new JsonParser().parse((String) response.getResponse()).getAsJsonObject();

            if(jsonObject.has("id")){

                id = jsonObject.get("id").getAsString();
            }
            else{
                if(jsonObject.has("message")) {
                    dbxResult.setDbpErrMsg(jsonObject.get("message").getAsString());
                }
                
                if(jsonObject.has("status")) {
                    dbxResult.setDbpErrCode(jsonObject.get("status").getAsString());
                }
            }


        }catch (Exception e) {
            alert.prepareError("Caught exception while updating Party: ",  e).log();
            dbxResult.setDbpErrMsg(response.getDbpErrMsg());
            return dbxResult;
        }

        dbxResult.setResponse(id);
        return dbxResult;
	}

	
	private String getPartyIdFromBackendIdentifier(String customerId, Map<String, Object> headerMap) {
		
		String filter = "Customer_id" +DBPUtilitiesConstants.EQUAL + customerId + DBPUtilitiesConstants.AND + DTOConstants.BACKENDTYPE + DBPUtilitiesConstants.EQUAL + DTOConstants.PARTY;

		Map<String, Object> inputParams = new HashMap<String, Object>();
		
		inputParams.put(DBPUtilitiesConstants.FILTER, filter);
		
		String authToken = (String) headerMap.get(MWConstants.X_KONY_AUTHORIZATION_HEADER);
		
		Map<String, String> headers = new HashMap<String, String>();
		
		for(Entry<String, Object> entry : headerMap.entrySet()) {
			headers.put(entry.getKey(), (String) entry.getValue());
		}
		
		try {
			JsonObject result = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap, URLConstants.BACKENDIDENTIFIER_GET, authToken);
					
			if(result.has("backendidentifier")&& result.get("backendidentifier").isJsonNull() && result.get("backendidentifier").isJsonArray() && result.get("backendidentifier").getAsJsonArray().size() > 0) {
				JsonArray jsonArray = result.get("backendidentifier").getAsJsonArray();
				
				JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();

				if(jsonObject.has("BackendId")) {
					return jsonObject.get("BackendId").getAsString();
				}
				
			}
					
		} catch (Exception e) {
			alert.prepareError("Caught exception while getting PartyId: ", e).log();
		}
		
		return null;
	}

}
