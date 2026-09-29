package com.temenos.dbx.product.contract.businessdelegate.api;

import java.util.List;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.google.gson.JsonArray;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.ContractAccountsDTO;
import com.temenos.dbx.product.dto.ContractActionLimitsDTO;
import com.temenos.dbx.product.dto.ContractCoreCustomersDTO;
import com.temenos.dbx.product.dto.ContractDTO;
import com.temenos.dbx.product.dto.DBXResult;

public interface ContractBusinessDelegate extends BusinessDelegate {

    /**
     * 
     * @param contractDTO
     * @param headersMap
     * @return ContractDTO
     * @throws ApplicationException
     */
    public ContractDTO createContract(ContractDTO contractDTO, Map<String, Object> headersMap)
            throws ApplicationException;

    /**
     * 
     * @param methodID
     * @param inputArray
     * @param dcRequest
     * @param dcResponse
     * @return result of response containing the contracts searched
     * @throws ApplicationException
     */
    public DBXResult searchContracts(Map<String, String> inputParams, Map<String, Object> headersMap)
            throws ApplicationException;

    /**
     * 
     * @param contractId
     * @param headersMap
     * @return
     * @throws ApplicationException
     */
    public ContractDTO getContractDetails(String contractId,
            Map<String, Object> headersMap) throws ApplicationException;

    /**
     * 
     * @param contractId
     * @param headersMap
     * @return
     * @throws ApplicationException
     */
    public ContractDTO getCompleteContractDetails(String contractId,
            Map<String, Object> headersMap) throws ApplicationException;

    /**
     * 
     * @param customers
     * @param headersMap
     * @return maps isAssociated flag , if customers are present as part of any contract
     * @throws ApplicationException
     */
    public DBXResult validateCustomersForContract(JsonArray customers, Map<String, Object> headersMap)
            throws ApplicationException;

    /**
     * 
     * @param dto
     * @param headersMap
     * @return response of contract feature action limits
     * @throws ApplicationException
     */
    public DBXResult getContractFeatureActionLimits(ContractCoreCustomersDTO dto, Map<String, Object> headersMap)
            throws ApplicationException;

    public JsonArray getContractInfinityUsers(String contractId, Map<String, Object> headersMap)
            throws ApplicationException;

    public List<ContractDTO> getListOfContractsByStatus(String statusId, Map<String, Object> headerMap)
            throws ApplicationException;

    public boolean updateContractStatus(String contractId, String statusId, Map<String, Object> headerMap)
            throws ApplicationException;

    /**
     * 
     * @param coreCustomerRoleIdList
     * @param headersMap
     * @return Corecustomer intersect roleid action limits
     * @throws ApplicationException
     */
    public DBXResult getCoreCustomerGroupFeatureActionLimits(String coreCustomerRoleIdList,
            Map<String, Object> headersMap) throws ApplicationException;

    /**
     * 
     * @param methodID
     * @param inputArray
     * @param dcRequest
     * @param dcResponse
     * @return contract details based on core customer id
     * @throws ApplicationException
     */
    public DBXResult getCoreCustomerBasedContractDetails(String coreCustomerId, Map<String, Object> headersMap)
            throws ApplicationException;

    /**
     * 
     * @param methodID
     * @param inputArray
     * @param dcRequest
     * @param dcResponse
     * @return contract details based on relative core customers for input core customer id
     * @throws ApplicationException
     */
    public DBXResult getRelativeCoreCustomerBasedContractDetails(Map<String, String> configurations,
            String coreCustomerId, Map<String, Object> headersMap)
            throws ApplicationException;

    /**
     * 
     * @param contractId
     * @param headersMap
     * @return list of contract accounts
     * @throws ApplicationException
     */
    public List<ContractAccountsDTO> getContractAccounts(String contractId, Map<String, Object> headersMap)
            throws ApplicationException;

    public ContractDTO updateContract(ContractDTO contractDTO, Map<String, Object> headersMap)
            throws ApplicationException;

    public ContractDTO getContractDetails(ContractDTO contractDTO, Map<String, Object> headersMap)
            throws ApplicationException;

    public List<ContractActionLimitsDTO> getContractActions(ContractActionLimitsDTO dto, Map<String, Object> headersMap)
            throws ApplicationException;
}
