package com.temenos.dbx.product.usermanagement.businessdelegate.api;

import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.ContractCustomersDTO;
import com.temenos.dbx.product.dto.CustomerAccountsDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.UserCustomerViewDTO;

public interface InfinityUserManagementBusinessDelegate extends BusinessDelegate {

    /**
     * 
     * @param contractCustomerDTO
     * @param headerMap
     * @return DBXResult.response containing the list of customers information
     * @throws ApplicationException
     */
    DBXResult getAssociatedCustomers(ContractCustomersDTO contractCustomerDTO, Map<String, Object> headerMap)
            throws ApplicationException;

    /**
     * 
     * @param coreCustomerId
     * @param headerMap
     * @return DBXResult of relative custoomers info
     * @throws ApplicationException
     */
    DBXResult getAllEligibleRelationalCustomers(String coreCustomerId, Map<String, Object> headerMap)
            throws ApplicationException;

    DBXResult createInfinityUser(JsonObject jsonObject, Map<String, Object> headerMap);

    DBXResult editInfinityUser(JsonObject jsonObject, Map<String, Object> headerMap);

    DBXResult getInfinityUser(JsonObject inputObject, Map<String, Object> headerMap);

    DBXResult getCoreCustomerFeatureActionLimits(String roleId, String coreCustomerId, String userId,
            Map<String, Object> headerMap)
            throws ApplicationException;

    /**
     * 
     * @param contractCustomerDTO
     * @param headersMap
     * @return
     * @throws ApplicationException
     */
    DBXResult getCoreCustomerInformation(ContractCustomersDTO contractCustomerDTO, Map<String, Object> headersMap)
            throws ApplicationException;

    /**
     * 
     * @param dto
     * @param headersMap
     * @return json response of containing the logged in user contracts and core customer information
     * @throws ApplicationException
     */
    DBXResult getInfinityUserContractCustomerDetails(UserCustomerViewDTO dto, Map<String, Object> headersMap)
            throws ApplicationException;

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
    DBXResult verifyCustomRole(JsonObject jsonObject, Map<String, Object> headerMap);

    /**
     * 
     * @param jsonObject
     * @param headerMap
     * @return
     */
    DBXResult getCustomRole(JsonObject jsonObject, Map<String, Object> headerMap);

    /**
     * 
     * @param contractCustomerDTO
     * @param headerMap
     * @return
     * @throws ApplicationException
     */
    public DBXResult getInfinityUserContractCoreCustomerActions(ContractCustomersDTO contractCustomerDTO,
            Map<String, Object> headerMap) throws ApplicationException;

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
     * @param contractCustomerDTO
     * @param headerMap
     * @return infinity user id based contract details
     * @throws ApplicationException
     */
    DBXResult getInfinityUserContractDetails(ContractCustomersDTO contractCustomerDTO,
            Map<String, Object> headerMap) throws ApplicationException;

    /**
     * 
     * @param dto
     * @param headersMap
     * @return
     * @throws ApplicationException
     */
    DBXResult getInfinityUserAccountsForAdmin(CustomerAccountsDTO dto, Map<String, Object> headersMap)
            throws ApplicationException;

    /**
     * 
     * @param userNameLength
     * @param activationCodeLength
     * @param inputParams
     * @return
     * @throws ApplicationException
     */
    public DBXResult generateInfinityUserActivationCodeAndUsername(Map<String, String> C360BundleConfigurations,
            Map<String, String> inputParams, Map<String, Object> headersMap) throws ApplicationException;

    /**
     * 
     * @param accounts
     * @param customerId
     * @param headersMap
     * @return
     * @throws ApplicationException
     */
    public DBXResult processNewAccounts(String accounts, String customerId, Map<String, Object> headersMap)
            throws ApplicationException;

    /**
     * 
     * @param inputObject
     * @param headerMap
     * @return
     */
    public DBXResult getInfinityUserPrimaryRetailContract(JsonObject inputObject, Map<String, Object> headerMap);

    /**
     * 
     * @param userId
     * @param userName
     * @param headersMap
     * @return
     * @throws ApplicationException
     */
    public DBXResult getUserApprovalPermissions(String userId, String userName, Map<String, Object> headersMap)
            throws ApplicationException;

    /**
     * 
     * @param lastName
     * @param taxId
     * @param dateOfBirth
     * @param headersMap
     * @return
     * @throws ApplicationException
     */
    public DBXResult validateCustomerEnrollmentDetails(String lastName, String taxId, String dateOfBirth, Map<String, Object> headersMap)
            throws ApplicationException;
}
