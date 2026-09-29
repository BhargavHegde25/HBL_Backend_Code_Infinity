package com.temenos.dbx.product.contract.businessdelegate.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.contract.backenddelegate.api.ContractCoreCustomerBackendDelegate;
import com.temenos.dbx.product.contract.businessdelegate.api.ContractCoreCustomerBusinessDelegate;
import com.temenos.dbx.product.dto.ContractCoreCustomersDTO;

public class ContractCoreCustomerBusinessDelegateImpl implements ContractCoreCustomerBusinessDelegate {

    @Override
    public ContractCoreCustomersDTO createContractCustomer(ContractCoreCustomersDTO inputCustomerDTO,
            Map<String, Object> headersMap) throws ApplicationException {

        ContractCoreCustomerBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(ContractCoreCustomerBackendDelegate.class);
        return backendDelegate.createContractCustomer(inputCustomerDTO, headersMap);
    }

    @Override
    public List<ContractCoreCustomersDTO> getContractCoreCustomers(String contractId, boolean getAccountDetails,
            boolean getCompleteDetails,
            Map<String, Object> headersMap)
            throws ApplicationException {
        ContractCoreCustomerBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(ContractCoreCustomerBackendDelegate.class);
        return backendDelegate.getContractCoreCustomers(contractId, getAccountDetails, getCompleteDetails, headersMap);
    }

    @Override
    public Set<String> getValidCoreContractCustomers(Set<String> customersList, Map<String, Object> headersMap)
            throws ApplicationException {
        ContractCoreCustomerBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(ContractCoreCustomerBackendDelegate.class);
        return backendDelegate.getValidCoreContractCustomers(customersList, headersMap);
    }
    
    @Override
    public Set<String> getValidCoreContractCustomers(Set<String> customersList, String legalEntityId, Map<String, Object> headersMap)
            throws ApplicationException {
        ContractCoreCustomerBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(ContractCoreCustomerBackendDelegate.class);
        return backendDelegate.getValidCoreContractCustomers(customersList, legalEntityId, headersMap);
    }

    @Override
    public void deleteContractCoreCustomers(Set<String> contractCoreCustomers, String contractId,
            Map<String, Object> headersMap) throws ApplicationException {
        ContractCoreCustomerBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(ContractCoreCustomerBackendDelegate.class);
        backendDelegate.deleteContractCoreCustomers(contractCoreCustomers, contractId, headersMap);
    }

    @Override
    public Map<String, Set<String>> getCoreCustomerAccountsFeaturesActions(String contractId, String coreCustomerId,
            Map<String, Object> headersMap) throws ApplicationException {
        ContractCoreCustomerBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(ContractCoreCustomerBackendDelegate.class);
        return backendDelegate.getCoreCustomerAccountsFeaturesActions(contractId, coreCustomerId, headersMap);
    }

    @Override
    public void deleteCoreCustomerAccounts(Set<String> accounts, String contractId, String coreCustomerId,
            Map<String, Object> headersMap) throws ApplicationException {
        ContractCoreCustomerBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(ContractCoreCustomerBackendDelegate.class);
        backendDelegate.deleteCoreCustomerAccounts(accounts, contractId, coreCustomerId, headersMap);
    }

    @Override
    public void deleteCoreCustomerFeatures(Set<String> features, String contractId, String coreCustomerId,
            Map<String, Object> headersMap) throws ApplicationException {
        ContractCoreCustomerBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(ContractCoreCustomerBackendDelegate.class);
        backendDelegate.deleteCoreCustomerFeatures(features, contractId, coreCustomerId, headersMap);

    }

    @Override
    public void deleteCoreCustomerActions(Set<String> actions, String contractId, String coreCustomerId,
            Map<String, Object> headersMap) throws ApplicationException {
        ContractCoreCustomerBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(ContractCoreCustomerBackendDelegate.class);
        backendDelegate.deleteCoreCustomerActions(actions, contractId, coreCustomerId, headersMap);
    }

    @Override
    public ContractCoreCustomersDTO getContractCoreCustomers(ContractCoreCustomersDTO contractCoreCustomerDTO,
            Map<String, Object> headersMap) throws ApplicationException {
        ContractCoreCustomerBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(ContractCoreCustomerBackendDelegate.class);
        return backendDelegate.getContractCoreCustomers(contractCoreCustomerDTO, headersMap);
    }

    @Override
    public List<ContractCoreCustomersDTO> getContractCoreCustomerDetails(ContractCoreCustomersDTO customerDTO,
            Map<String, Object> headersMap) throws ApplicationException {
        ContractCoreCustomerBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(ContractCoreCustomerBackendDelegate.class);
        return backendDelegate.getContractCoreCustomerDetails(customerDTO, headersMap);
    }

    @Override
    public ContractCoreCustomersDTO updateContractCustomer(ContractCoreCustomersDTO customerDTO,
            Map<String, Object> headersMap) throws ApplicationException {
        ContractCoreCustomerBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(ContractCoreCustomerBackendDelegate.class);
        return backendDelegate.updateContractCustomer(customerDTO, headersMap);
    }
}
