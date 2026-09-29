package com.temenos.dbx.product.contract.businessdelegate.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonArray;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.temenos.dbx.product.contract.backenddelegate.api.ContractAccountBackendDelegate;
import com.temenos.dbx.product.contract.businessdelegate.api.ContractAccountsBusinessDelegate;
import com.temenos.dbx.product.dto.ContractAccountsDTO;
import com.temenos.dbx.product.dto.DBXResult;

public class ContractAccountsBusinessDelegateImpl implements ContractAccountsBusinessDelegate {

    @Override
    public ContractAccountsDTO createContractAccount(ContractAccountsDTO inputAccountDTO,
            Map<String, Object> headersMap) throws ApplicationException {
        ContractAccountBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(ContractAccountBackendDelegate.class);
        return backendDelegate.createContractAccount(inputAccountDTO, headersMap);
    }

    @Override
    public List<ContractAccountsDTO> getContractCustomerAccounts(String contractId, String customerId,
            Map<String, Object> headersMap) throws ApplicationException {
        ContractAccountBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(ContractAccountBackendDelegate.class);
        return backendDelegate.getContractCustomerAccounts(contractId, customerId, headersMap);

    }

    @Override
    public DBXResult validateAccountsContractAssociation(JsonArray jsonarray, Map<String, Object> headersMap)
            throws ApplicationException {
        DBXResult response = new DBXResult();
        try {
            ContractAccountBackendDelegate backendDelegate =
                    DBPAPIAbstractFactoryImpl.getBackendDelegate(ContractAccountBackendDelegate.class);
            response = backendDelegate.validateAccountsContractAssociation(jsonarray, headersMap);
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_00000);
        }
        return response;
    }

    @Override
    public boolean checkIfGivenAccountsAreValid(Set<String> accountsList, Map<String, Object> headersMap)
            throws ApplicationException {
        ContractAccountBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(ContractAccountBackendDelegate.class);
        return backendDelegate.checkIfGivenAccountsAreValid(accountsList, headersMap);
    }
}
