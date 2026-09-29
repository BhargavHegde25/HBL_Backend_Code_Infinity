package com.temenos.dbx.usermanagement.resource.impl;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.IntegrationTemplateURLFinder;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.party.utils.PartyConstants;
import com.temenos.infinity.api.arrangements.resource.api.InfinityAccountsResource;
import com.temenos.dbx.product.dto.BackendIdentifierDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.utils.DTOConstants;
import com.temenos.dbx.product.utils.HTTPOperations;
import com.temenos.dbx.product.utils.InfinityConstants;

public class PartyInfinityAccountsResourceImpl implements InfinityAccountsResource {
    
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object getInfinityAccounts(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) {

        Map<String, String> map = HelperMethods.getInputParamMap(inputArray);
        String id = map.containsKey(InfinityConstants.id) && StringUtils.isNotBlank(map.get(InfinityConstants.id))
                ? map.get(InfinityConstants.id)
                : request.getParameter(InfinityConstants.id);
        String coreCustomerId = map.containsKey(InfinityConstants.coreCustomerId)
                && StringUtils.isNotBlank(map.get(InfinityConstants.coreCustomerId))
                        ? map.get(InfinityConstants.coreCustomerId)
                        : request.getParameter(InfinityConstants.coreCustomerId);
        String partyId =
                map.containsKey(InfinityConstants.partyId) && StringUtils.isNotBlank(map.get(InfinityConstants.partyId))
                        ? map.get(InfinityConstants.partyId)
                        : request.getParameter(InfinityConstants.partyId);

        boolean IS_Integrated = IntegrationTemplateURLFinder.isIntegrated;

        if (StringUtils.isBlank(coreCustomerId) && StringUtils.isNotBlank(id)) {
            BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
            backendIdentifierDTO.setCustomer_id(id);
            if (IS_Integrated) {
                backendIdentifierDTO
                        .setBackendType(IntegrationTemplateURLFinder.getBackendURL(InfinityConstants.BackendType));
            } else {
                backendIdentifierDTO.setBackendType(DTOConstants.CORE);
            }

            backendIdentifierDTO = (BackendIdentifierDTO) backendIdentifierDTO.loadDTO();

            if (backendIdentifierDTO != null) {
                coreCustomerId = backendIdentifierDTO.getBackendId();
            }
        }

        if (StringUtils.isNotBlank(coreCustomerId) && StringUtils.isBlank(id)) {
            BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
            backendIdentifierDTO.setBackendId(coreCustomerId);
            if (IS_Integrated) {
                backendIdentifierDTO
                        .setBackendType(IntegrationTemplateURLFinder.getBackendURL(InfinityConstants.BackendType));
            } else {
                backendIdentifierDTO.setBackendType(DTOConstants.CORE);
            }

            backendIdentifierDTO = (BackendIdentifierDTO) backendIdentifierDTO.loadDTO();

            if (backendIdentifierDTO != null) {
                id = backendIdentifierDTO.getCustomer_id();
            }
        }
        
        if (StringUtils.isBlank(coreCustomerId) && StringUtils.isNotBlank(partyId)) {
            BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
            backendIdentifierDTO.setBackendId(partyId);
            backendIdentifierDTO
                    .setBackendType(DTOConstants.PARTY);

            backendIdentifierDTO = (BackendIdentifierDTO) backendIdentifierDTO.loadDTO();

            if (backendIdentifierDTO != null) {
                id = backendIdentifierDTO.getCustomer_id();
                backendIdentifierDTO = new BackendIdentifierDTO();
                backendIdentifierDTO.setCustomer_id(id);
                if (IS_Integrated) {
                    backendIdentifierDTO
                            .setBackendType(IntegrationTemplateURLFinder.getBackendURL(InfinityConstants.BackendType));
                } else {
                    backendIdentifierDTO.setBackendType(DTOConstants.CORE);
                }

                backendIdentifierDTO = (BackendIdentifierDTO) backendIdentifierDTO.loadDTO();

                if (backendIdentifierDTO != null) {
                    coreCustomerId = backendIdentifierDTO.getBackendId();
                }
            } else {
                Result result = new PartyUserManagementResourceImpl().partyGet(methodId, inputArray, request, response);
                if (result.getDatasetById(PartyConstants.parties) != null
                        && result.getDatasetById(PartyConstants.parties).getAllRecords().size() > 0) {
                    Record party = result.getDatasetById(PartyConstants.parties).getAllRecords().get(0);
                    coreCustomerId = party.getNameOfAllParams().contains(PartyConstants.coreCustomerId)
                            && StringUtils.isNotBlank(party.getParamValueByName(PartyConstants.coreCustomerId))
                                    ? party.getParamValueByName(PartyConstants.coreCustomerId)
                                    : null;
                }
            }
        }

        Map<String, String> input = new HashMap<String, String>();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(InfinityConstants.id, id);
        jsonObject.addProperty(InfinityConstants.coreCustomerId, coreCustomerId);

        input.put(InfinityConstants.id, id);
        input.put(InfinityConstants.coreCustomerId, coreCustomerId);

        String url = EnvironmentConfigurationsHandler.getValue(DBPUtilitiesConstants.DBX_HOST_URL)
                + URLFinder.getPathUrl(URLConstants.ACCOUNTS_POST);
        Result result = new Result();
        try {
            result = HelperMethods.callExternalApi(input, HelperMethods.getHeaders(request), url);
        } catch (HttpCallException e) {
           alert.prepareError("Exception", e).log();
        }
        
        if(StringUtils.isNotBlank(id)) {
            Map map2 = new HashMap();
            map2.put(InfinityConstants.userId, id);
            Result result1 = new Result();
            url = EnvironmentConfigurationsHandler.getValue(DBPUtilitiesConstants.DBX_HOST_URL)+URLFinder.getPathUrl(URLConstants.INFINITY_USER_ACCOUNTS_GET_OBJECT);
            try {
               result1 = HelperMethods.callExternalApi(map2, HelperMethods.getHeaders(request), url);
            } catch (HttpCallException e1) {
            	alert.prepareError("Exception", e1).log();
            }
            
            Dataset contracts = result1.getDatasetById("contracts");
            if(contracts==null || contracts.getAllRecords().size()<=0) {
                return result;
            }
            
            Map<String, Record> accountsMap = new HashMap<String, Record>();
            Dataset accounts = result.getDatasetById("Accounts");
            
            if(accounts != null && accounts.getAllRecords().size() >0) {
                for(Record record : accounts.getAllRecords()) {
                    if(StringUtils.isNotBlank(record.getParamValueByName("accountID"))){
                        accountsMap.put(record.getParamValueByName("accountID"), record);
                    }
                    else if(StringUtils.isNotBlank(record.getParamValueByName("Account_id"))){
                        accountsMap.put(record.getParamValueByName("Account_id"), record);
                    }
                    else if(StringUtils.isNotBlank(record.getParamValueByName("account_id"))){
                        accountsMap.put(record.getParamValueByName("account_id"), record);
                    }
                }
            }
            
            for(Record contract : contracts.getAllRecords()) {
                Dataset contractCustomers = contract.getDatasetById("contractCustomers");
                if(contractCustomers!=null && contractCustomers.getAllRecords().size()>0) {
                    for(Record contractCustomer : contractCustomers.getAllRecords()) {
                        Dataset coreCustomerAccounts = contractCustomer.getDatasetById("coreCustomerAccounts");
                        if(coreCustomerAccounts!=null && coreCustomerAccounts.getAllRecords().size()>0) {
                            for(Record coreCustomerAccount : coreCustomerAccounts.getAllRecords()) {
                                String accountId = coreCustomerAccount.getParamValueByName("accountId");
                                if(accountsMap.containsKey(accountId) && accountsMap.get(accountId) !=null) {
                                    coreCustomerAccount.addAllParams(accountsMap.get(accountId).getAllParams());
                                }
                            }
                        }
                    }
                }
            }
            
            return result1;
        }

        return result;
    }
}
