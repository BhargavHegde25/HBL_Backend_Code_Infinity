package com.temenos.dbx.usermanagement.backenddelegate.impl;

import java.util.Map;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.infinity.dbx.dbp.jwt.auth.AuthConstants;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.temenos.dbx.party.utils.PartyConstants;
import com.temenos.dbx.party.utils.PartyPropertyConstants;
import com.temenos.dbx.party.utils.PartyURLFinder;
import com.temenos.dbx.party.utils.PartyUtils;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.PartyDTO;
//import com.temenos.dbx.product.dto.PartyDTO;
import com.temenos.dbx.product.utils.HTTPOperations;
import com.temenos.dbx.usermanagement.backenddelegate.api.PartyRelationsUserManagementBackendDelegate;
//import com.temenos.dbx.usermanagement.backenddelegate.api.PartyUserManagementBackendDelegate;
//import com.temenos.dbx.usermanagement.dto.PartySearchDTO;

public class PartyRelationsUserManagementBackendDelegateImpl implements PartyRelationsUserManagementBackendDelegate
{
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	 //partyrelations create function
	@Override
    public DBXResult create(JsonObject partyRelationsObj, Map<String, Object> headerMap) 
	{

        DBXResult dbxResult = new DBXResult();
        String party = partyRelationsObj.toString();
        String partyId = partyRelationsObj.get(PartyConstants.partyId).getAsString();
        
        diagnostic.prepareDebug("PartyDTO for create PartyRelationsService is : " + party).log();

        String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL) +
                PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_RELATIONS,partyId);

        PartyUtils.addJWTAuthHeader(headerMap, AuthConstants.PRE_LOGIN_FLOW);
        DBXResult response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.POST, partyURL, party, headerMap);

        try {
            JsonElement jsonElement = new JsonParser().parse((String) response.getResponse());
            JsonObject jsonObject;

            jsonObject = jsonElement.isJsonObject() ? jsonElement.getAsJsonObject()
                    : jsonElement.getAsJsonArray().get(0).getAsJsonObject();
            
            if ((jsonObject.has(PartyConstants.partyId))&&(jsonObject.has(PartyConstants.partyRelations))) 
            {
                String id = jsonObject.get(PartyConstants.partyId).getAsString();
                JsonArray partyRelations =  jsonObject.get(PartyConstants.partyRelations).getAsJsonArray();
                dbxResult.setResponse(id);
            } 
            else 
            {
                if (jsonObject.has("message")) {
                    dbxResult.setDbpErrMsg(jsonObject.get("message").getAsString());
                }

                if (jsonObject.has("status")) {
                    dbxResult.setDbpErrCode(jsonObject.get("status").getAsString());
                }
            }
        } catch (Exception e) 
        {
            alert.prepareError("Caught exception while parsing the save PartyRelationsresponse: ", e).log();
            dbxResult.setDbpErrMsg("PartyRelations create Failed");
            dbxResult.setDbpErrMsg(response.getDbpErrMsg());
            return dbxResult;
        }

        return dbxResult;
    }
	
//partyRelations get function
	@Override
    public DBXResult get(JsonObject partyRelationsObj, Map<String, Object> headerMap) 
	{
        DBXResult dbxResult = new DBXResult();
        String partyRelations = partyRelationsObj.toString();
        String partyId = partyRelationsObj.get(PartyConstants.partyId).getAsString();
        String relationType = partyRelationsObj.get(PartyConstants.relationType).getAsString();
        String hierarchyType = partyRelationsObj.get(PartyConstants.hierarchyType).getAsString();
        
        String url2="?relationType="+relationType+"&hierarchyType="+hierarchyType;
        
        
        diagnostic.prepareDebug("PartyRelations for get PartyRelations Service is : " + partyRelations).log();

        String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL) +
                PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_RELATIONS, partyId)+ url2;
        PartyUtils.addJWTAuthHeader(headerMap, AuthConstants.PRE_LOGIN_FLOW);
        
        //String url=partyURL.concat(url2);
        DBXResult response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET, partyURL, null, headerMap);
      
        
       //add relation and heirarchy type
        try {
            JsonElement jsonElement = new JsonParser().parse((String) response.getResponse());
            JsonObject jsonObject;

            jsonObject = jsonElement.isJsonObject() ? jsonElement.getAsJsonObject()
                    : jsonElement.getAsJsonArray().get(0).getAsJsonObject();
            if (jsonObject.has(PartyConstants.partyRelations)) {
                                dbxResult.setResponse(jsonObject);
            } else {
                if (jsonObject.has("message")) {
                    dbxResult.setDbpErrMsg(jsonObject.get("message").getAsString());
                }

                if (jsonObject.has("status")) {
                    dbxResult.setDbpErrCode(jsonObject.get("status").getAsString());
                }
            }

        } catch (Exception e) {
            alert.prepareError("Caught exception while getting PartyRelations: ", e).log();
            dbxResult.setDbpErrMsg(response.getDbpErrMsg());
            return dbxResult;
        }

        return dbxResult;
    }
	//other methods

//partyrelations updatefunction
	@Override
	public DBXResult update(JsonObject partyRelationsObj, Map<String, Object> headerMap) {

        DBXResult dbxResult = new DBXResult();
        String party = partyRelationsObj.toString();
        String relationType = partyRelationsObj.get(PartyConstants.relationType).getAsString();
        String partyId = partyRelationsObj.get(PartyConstants.partyId).getAsString();

        diagnostic.prepareDebug("PartyDTO for update Party Service is : " + party).log();

        // DBXResult response = new DBXResult();

        String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL) +
                PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_UPDATE, partyId)+ "?relationType=" + relationType;
        PartyUtils.addJWTAuthHeader(headerMap, AuthConstants.PRE_LOGIN_FLOW);
        DBXResult response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.PUT, partyURL, party, headerMap);
        String id = "";
        try {
            JsonElement jsonElement = new JsonParser().parse((String) response.getResponse());
            JsonObject jsonObject;

            jsonObject = jsonElement.isJsonObject() ? jsonElement.getAsJsonObject()
                    : jsonElement.getAsJsonArray().get(0).getAsJsonObject();
            if (jsonObject.has(PartyConstants.id)) {
                id = jsonObject.get(PartyConstants.id).getAsString();
                dbxResult.setResponse(id);
            } else {
                if (jsonObject.has("message")) {
                    dbxResult.setDbpErrMsg(jsonObject.get("message").getAsString());
                }

                if (jsonObject.has("status")) {
                    dbxResult.setDbpErrCode(jsonObject.get("status").getAsString());
                }
            }

        } catch (Exception e) {
            alert.prepareError("Caught exception while getting Party: ", e).log();
            dbxResult.setDbpErrMsg(response.getDbpErrMsg());
            return dbxResult;
        }

        return dbxResult;

    }



}
