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
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.temenos.dbx.party.utils.PartyPropertyConstants;
import com.temenos.dbx.party.utils.PartyURLFinder;
import com.temenos.dbx.party.utils.PartyUtils;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.utils.HTTPOperations;
import com.temenos.dbx.usermanagement.backenddelegate.api.PartyBackendDelegate;

public class PartyBackendDelegateImpl implements PartyBackendDelegate {
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static LoggerUtil logger = new LoggerUtil(PartyBackendDelegateImpl.class);


    @Override
    public DBXResult getReferenceByID(String referenceByID, Map<String, Object> headerMap) {
        DBXResult dbxResult = new DBXResult();
        try {
            PartyUtils.addJWTAuthHeader(headerMap, AuthConstants.PRE_LOGIN_FLOW);

            String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL)+
                    PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_REFERENCE)+referenceByID;

            dbxResult = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET,
                    partyURL, null, headerMap);

            if(dbxResult.getResponse() != null) {
                JsonElement element = new JsonParser().parse((String) dbxResult.getResponse());
                if (element.isJsonObject()) {
                    dbxResult.setResponse(element.getAsJsonObject().toString());
                }
                else if(element.isJsonArray()) {
                    dbxResult.setResponse(element.getAsJsonArray().get(0).getAsJsonObject().toString());
                }
            }

        }catch(Exception e) {
            alert.prepareError("Error in PartyBackendDelegateImpl-getReferences : "+ e.toString()).log();
        }

        return dbxResult;
    }


    @Override
    public void getUpdateAddressType(Map<String, String> input, Map<String, Object> headerMap) {
        try {
            PartyUtils.addJWTAuthHeader(headerMap, AuthConstants.PRE_LOGIN_FLOW);
            String partyID = input.get("partyId");
            String communicationNature = input.get("communicationNature");
            String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL)+
                    PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_ADDRESS_TYPE_UPDATE, partyID)+communicationNature;
            JsonObject jsonObject = new JsonObject();
            String addressesReference = input.get("addressesReference");
            jsonObject.addProperty("addressesReference", addressesReference);
            String addressType = input.get("addressType");
            jsonObject.addProperty("addressType", addressType);
            JsonArray jsonArray = new JsonArray();
            jsonArray.add(jsonObject);
            JsonObject inputObject = new JsonObject();
            inputObject.add("addresses", jsonArray);
            diagnostic.prepareDebug("PartyDTO for update Party AddressType Service is : " + inputObject.toString()).log();
            HTTPOperations.sendHttpRequest(HTTPOperations.operations.PUT,
                    partyURL, inputObject.toString(), headerMap);
        }catch(Exception e) {
            alert.prepareError("Error in PartyBackendDelegateImpl-getReferences : "+ e.toString()).log();
        }

    }
}