package com.temenos.dbx.product.contract.businessdelegate.api;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.ContractCoreCustomersDTO;

public interface ContractCoreCustomerBusinessDelegate extends BusinessDelegate {

    public ContractCoreCustomersDTO createContractCustomer(ContractCoreCustomersDTO customerDTO,
            Map<String, Object> headersMap) throws ApplicationException;

    public List<ContractCoreCustomersDTO> getContractCoreCustomers(String contractID, boolean getAccountsDetails,
            boolean getCompleteDetails,
            Map<String, Object> headersMap) throws ApplicationException;

    public List<ContractCoreCustomersDTO> getContractCoreCustomerDetails(ContractCoreCustomersDTO customerDTO,
            Map<String, Object> headersMap) throws ApplicationException;

    public ContractCoreCustomersDTO updateContractCustomer(ContractCoreCustomersDTO customerDTO,
            Map<String, Object> headersMap) throws ApplicationException;

    public Set<String> getValidCoreContractCustomers(Set<String> customersList,
            Map<String, Object> headersMap) throws ApplicationException;
    
    public Set<String> getValidCoreContractCustomers(Set<String> customersList, String legalEntityId,
            Map<String, Object> headersMap) throws ApplicationException;

    public void deleteContractCoreCustomers(Set<String> contractCoreCustomers, String contractId,
            Map<String, Object> headersMap) throws ApplicationException;

    public Map<String, Set<String>> getCoreCustomerAccountsFeaturesActions(String contractId, String coreCustomerId,
            Map<String, Object> headersMap) throws ApplicationException;

    public void deleteCoreCustomerAccounts(Set<String> accounts, String contractId, String coreCustomerId,
            Map<String, Object> headersMap) throws ApplicationException;

    public void deleteCoreCustomerFeatures(Set<String> features, String contractId, String coreCustomerId,
            Map<String, Object> headersMap) throws ApplicationException;

    public void deleteCoreCustomerActions(Set<String> actions, String contractId, String coreCustomerId,
            Map<String, Object> headersMap) throws ApplicationException;

    /**
     * 
     * @param contractCoreCustomerDTO
     * @param headersMap
     * @return ContractCoreCustomerDetails
     * @throws ApplicationException
     */
    public ContractCoreCustomersDTO getContractCoreCustomers(ContractCoreCustomersDTO contractCoreCustomerDTO,
            Map<String, Object> headersMap) throws ApplicationException;

}
