package com.temenos.dbx.eum.product.contract.backenddelegate.api;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dbp.core.api.BackendDelegate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.dto.ContractCoreCustomersDTO;
import com.temenos.dbx.product.dto.ContractAccountsDTO;
import com.temenos.dbx.product.dto.ContractActionLimitsDTO;
import com.temenos.dbx.product.dto.ContractDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.FeatureActionLimitsDTO;

public interface ContractBackendDelegate extends BackendDelegate {

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
     * @param inputParams
     * @param headersMap
     * @return contracts that are searched
     * @throws ApplicationException
     */
    public DBXResult searchContracts(Map<String, String> inputParams, Map<String, Object> headersMap)
            throws ApplicationException;

    /**
     * 
     * @param contractID
     * @param headersMap
     * @return
     * @throws ApplicationException
     */
    public ContractDTO getContract(String contractId, String legalEntityId, Map<String, Object> headersMap)
            throws ApplicationException;

    /**
     * 
     * @param dto
     * @param serviceDefinitionId
     * @param contractId
     * @param roleId
     * @param coreCustomerId
     * @param headersMap
     * @return FeatureActionLimitsDTO
     * @throws ApplicationException
     */
    public FeatureActionLimitsDTO getRestrictiveFeatureActionLimits(String serviceDefinitionId, String contractId,
			String roleId, String coreCustomerId, String userId, Map<String, Object> headersMap, boolean isSuperAdmin,
			String accessPolicyIdList, String legalEntityId) throws ApplicationException;

    /**
     * 
     * @param userId
     * @param coreCustomerId
     * @return
     * @throws ApplicationException
     */
    public Set<String> fetchUserCoreCustomerActions(String userId, String coreCustomerId,
            Map<String, Object> headersMap) throws ApplicationException;

    public JsonArray getContractInfinityUsers(String contractId, Map<String, Object> headersMap)
            throws ApplicationException;

    public List<ContractDTO> getListOfContractsByStatus(String statusId, String legalEntityId, Map<String, Object> headerMap)
            throws ApplicationException;

    public boolean updateContractStatus(String contractId, String statusId, Map<String, Object> headerMap)
            throws ApplicationException;
    
    public boolean updateContractStatus(ContractDTO contractDTO, Map<String, Object> headerMap)
            throws ApplicationException;

    public ContractDTO updateContract(ContractDTO contractDTO, Map<String, Object> headersMap)
            throws ApplicationException;

    public ContractDTO getContractDetails(ContractDTO contractDTO, Map<String, Object> headersMap)
            throws ApplicationException;

    public List<ContractActionLimitsDTO> getContractActions(ContractActionLimitsDTO dto, Map<String, Object> headersMap)
            throws ApplicationException;
    
    Map<String,Map<String, Set<String>>> getProductLevelPermissions(Set<String> productIds)
            throws ApplicationException;

    public DBXResult createContractActionLimit(JsonObject contractActionObj, String contractId,
            String legalEntityId, Map<String, Object> headerMap) throws ApplicationException;



    FeatureActionLimitsDTO getServiceDefinitionPermissions(String serviceDefinitionId, Map<String, Object> headersMap)
            throws ApplicationException;
    
    public Map<String, Set<String>> getArrangementRolePermissions( String role, String rolemapping,Map<String, Object> map)
            throws ApplicationException, JsonMappingException, JsonProcessingException;


    public DBXResult getNormalizedContractFeatureActionLimits(
            Map<String, List<ContractAccountsDTO>> coreCustomerAccounts, Map<String,Map<String, Set<String>>> productIdPermissions
            ,List<ContractCoreCustomersDTO> contractCustomers, FeatureActionLimitsDTO serviceDefinitionFeatureActionDTO
            ,Map<String, FeatureActionLimitsDTO> contractFeatureActionDTO)
            throws ApplicationException;

    public DBXResult getNormalizedProductArrangementFeatureActionLimits(String coreCustomerId,
            String serviceDefinitionId, String roleId, JsonArray productArray,
            FeatureActionLimitsDTO serviceDefinitionFeatureActionDTO, Map<String, FeatureActionLimitsDTO>
            contractFeatureActionDTOMap,
            List<ContractAccountsDTO> corecustomerAccounts, String rolemapping, Map<String, Object> headerMap) throws JsonMappingException, JsonProcessingException, ApplicationException;

    public JsonObject retrieveAccountDetails(String account, List<ContractAccountsDTO> corecustomerAccounts);

    public FeatureActionLimitsDTO getRestrictiveFeatureActionLimits(String serviceDefinitionId, String contractId,
			String roleId, String coreCustomerId, String userId, Map<String, Object> headersMap, boolean isSuperAdmin,
			String accessPolicyIdList) throws ApplicationException;

    public Result getProductLevelPermissions(String productRef) throws ApplicationException;
    
    public boolean updateContractStatus(String contractId, String statusId, String legalEntityId, Map<String, Object> headerMap)
            throws ApplicationException;

}
