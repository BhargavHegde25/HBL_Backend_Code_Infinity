package com.temenos.dbx.product.usermanagement.backenddelegate.api;

import java.util.List;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.ContractCustomersDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.UserCustomerViewDTO;

public interface InfinityUserManagementBackendDelegate extends BackendDelegate {

    /**
     * 
     * @param contractCustomerDTO
     * @param headerMap
     * @return users associated with the customers
     * @throws ApplicationException
     */
    List<UserCustomerViewDTO> getAssociatedCustomers(ContractCustomersDTO contractCustomerDTO,
            Map<String, Object> headerMap)
            throws ApplicationException;

    /**
     * 
     * @param jsonObject
     * @param headerMap
     * @return
     */
    DBXResult createInfinityUser(JsonObject jsonObject, Map<String, Object> headerMap);

    /**
     * 
     * @param jsonObject
     * @param headerMap
     * @return
     */
    DBXResult editInfinityUser(JsonObject jsonObject, Map<String, Object> headerMap);

    /**
     * 
     * @param inputJson
     * @param headerMap
     * @return
     */
    DBXResult getInfinityUser(JsonObject inputJson, Map<String, Object> headerMap);

    /**
     * 
     * @param jsonObject
     * @param headerMap
     * @return
     */
    DBXResult createCustomRole(JsonObject jsonObject, Map<String, Object> headerMap);

    /**
     * 
     * @param jsonObject
     * @param headerMap
     * @return
     */
    DBXResult editCustomRole(JsonObject jsonObject, Map<String, Object> headerMap);

    /**
     * 
     * @param jsonObject
     * @param headerMap
     * @return
     */
    DBXResult getCustomRole(JsonObject jsonObject, Map<String, Object> headerMap);

    /**
     * 
     * @param jsonObject
     * @param headerMap
     * @return
     */
    DBXResult verifyCustomRole(JsonObject jsonObject, Map<String, Object> headerMap);

    /**
     * 
     * @param jsonObject
     * @param headerMap
     * @return
     */
    DBXResult getCustomRoleByCompanyID(JsonObject jsonObject, Map<String, Object> headerMap);

    /**
     * 
     * @param jsonObject
     * @param headerMap
     * @return
     */
    DBXResult getCompanyLevelCustomRoles(JsonObject jsonObject, Map<String, Object> headerMap);

    /**
     * 
     * @param jsonObject
     * @param headerMap
     * @return
     */
    DBXResult getAssociatedContractUsers(JsonObject jsonObject, Map<String, Object> headerMap);

    /**
     * 
     * @param dto
     * @param headersMap
     * @return
     * @throws ApplicationException
     */
    List<ContractCustomersDTO> getInfinityContractCustomers(ContractCustomersDTO dto, String filter,
            Map<String, Object> headersMap)
            throws ApplicationException;

    /**
     * 
     * @param infinityUserId
     * @param headersMap
     * @return
     * @throws ApplicationException
     */
    DBXResult getInfinityUserContractDetailsGetOperation(String infinityUserId, Map<String, Object> headersMap)
            throws ApplicationException;

    /**
     * Generates the userName for the Infinity User
     * 
     * @param userNameLength
     * @return
     */
    public DBXResult generateInfinityUserName(String userNameLength);

    /**
     * 
     * Generates the activation code for the Infinity user activation process
     * 
     * @param activationCodeLength
     * @return
     */
    public DBXResult generateActivationCode(String activationCodeLength);

    /**
     * 
     * @param customerId
     * @param headersMap
     * @return
     * @throws ApplicationException
     */
    public DBXResult processNewAccounts(String customerId, Map<String, Object> headersMap) throws ApplicationException;

    /**
     * 
     * @param inputJson
     * @param headerMap
     * @return
     */
    public DBXResult getInfinityUserPrimaryRetailContract(JsonObject inputJson, Map<String, Object> headerMap);

}
