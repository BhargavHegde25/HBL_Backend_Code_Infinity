package com.temenos.dbx.usermanagement.backenddelegate.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.infinity.dbx.dbp.jwt.auth.AuthConstants;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.temenos.dbx.party.utils.PartyPropertyConstants;
import com.temenos.dbx.party.utils.PartyURLFinder;
import com.temenos.dbx.party.utils.PartyUtils;
import com.temenos.dbx.product.dto.BackendIdentifierDTO;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.PartyIdentifier;
import com.temenos.dbx.product.usermanagement.backenddelegate.api.BackendIdentifiersBackendDelegate;
import com.temenos.dbx.product.utils.DTOConstants;
import com.temenos.dbx.product.utils.HTTPOperations;
import com.temenos.dbx.usermanagement.backenddelegate.api.PartyIdentifierBackendDelegate;

public class PartyIdentifierBackendDelegateImpl implements PartyIdentifierBackendDelegate {
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static LoggerUtil logger = new LoggerUtil(PartyIdentifierBackendDelegateImpl.class);

    @Override
    public DBXResult update(CustomerDTO customerDTO, Map<String, Object> headerMap) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DBXResult get(CustomerDTO customerDTO, Map<String, Object> headerMap) {
        DBXResult dbxResult = new DBXResult();

        List<PartyIdentifier> dtoList = new ArrayList<>();

        if (StringUtils.isBlank(customerDTO.getId())) {
            return dbxResult;
        }

        BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
        backendIdentifierDTO.setCustomer_id(customerDTO.getId());
        backendIdentifierDTO.setBackendType(DTOConstants.PARTY);

        try {
            dbxResult = DBPAPIAbstractFactoryImpl.getBackendDelegate(BackendIdentifiersBackendDelegate.class).get(backendIdentifierDTO, headerMap);
        } catch (ApplicationException e1) {
            // TODO Auto-generated catch block
            alert.prepareError("Error while fetching backend identifier for customer ID " +customerDTO.getId()).log();
        }

        try {
            String partyId = null;
            if(dbxResult.getResponse() != null) {
                BackendIdentifierDTO identifierDTO = (BackendIdentifierDTO) dbxResult.getResponse();
                dbxResult = new DBXResult();
                partyId = identifierDTO.getBackendId();
            }
            else partyId = customerDTO.getId();

            String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL)+
                    PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_IDENTIFIER, partyId);

            PartyUtils.addJWTAuthHeader(headerMap, AuthConstants.PRE_LOGIN_FLOW);
            DBXResult response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET,
                    partyURL, null, headerMap);

            JsonObject jsonObject = new JsonParser().parse((String) response.getResponse()).getAsJsonObject();
            if(jsonObject.has(DTOConstants.PARTYIDENTIFIERS) && !jsonObject.get(DTOConstants.PARTYIDENTIFIERS).isJsonNull()){
                List<PartyIdentifier> partyIdentifiers = PartyIdentifier.loadFromJsonArray(jsonObject.get(DTOConstants.PARTYIDENTIFIERS).getAsJsonArray());
                dbxResult.setResponse(partyIdentifiers);
            }

        }
        catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }

        return dbxResult;
    }

    @Override
    public DBXResult create(CustomerDTO customerDTO, Map<String, Object> headerMap) {
        // TODO Auto-generated method stub
        return null;
    }


}