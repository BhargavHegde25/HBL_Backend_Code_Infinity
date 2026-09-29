package com.temenos.dbx.eum.product.usermanagement.businessdelegate.api;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.CustomerAccountsDTO;

public interface CustomerAccountsBusinessDelegate extends BusinessDelegate {

    /**
     * 
     * @param dto
     * @param headersMap
     * @return
     * @throws ApplicationException
     */
    public CustomerAccountsDTO createCustomerAccounts(CustomerAccountsDTO dto, Map<String, Object> headersMap)
            throws ApplicationException;

    /**
     * 
     * @param dto
     * @param headersMap
     * @return
     * @throws ApplicationException
     */
    public List<CustomerAccountsDTO> getCustomerAccounts(CustomerAccountsDTO dto, Map<String, Object> headersMap)
            throws ApplicationException;

    /**
     * 
     * @param dto
     * @param headersMap
     * @return
     * @throws ApplicationException
     */
    public List<CustomerAccountsDTO> getCustomerAccountsOnCustomerId(CustomerAccountsDTO dto,
            Map<String, Object> headersMap)
            throws ApplicationException;

    /**
     * 
     * @param userId
     * @param contractId
     * @param coreCustomerID
     * @param accounts
     * @param headersMap
     * @throws ApplicationException
     */
    public void createCustomerAccounts(String userId, String contractId, String coreCustomerID, String legalEntityId, Set<String> accounts,
            Map<String, Object> headersMap) throws ApplicationException;

    /**
     * 
     * @param userId
     * @param contractId
     * @param coreCustomerID
     * @param accounts
     * @param headersMap
     * @return customer accounts that are not consumed
     * @throws ApplicationException
     */
	public Set<String> getValidCustomerAccounts(Set<String> customerAccountsList, String customerId,
			String coreCustomerId, Map<String, Object> headersMap) throws ApplicationException;
 
    /**
     * 
     * @param userId
     * @return customer accounts that are not consumed per core customer
     * @throws ApplicationException
     */
    public Map<String,Set<String>> getValidCustomerAccounts(String customerId,
            Map<String, Object> headersMap)
            throws ApplicationException;

    /**
     * 
     * @param accountNumber
     * @param accountNumber
     * @param headersMap
     * @return true if all permissions are granted
     * @throws ApplicationException
     */

    public boolean checkIfAccountPermissionEnabled(String userId, String accountNumber, Set<String> permissions,
            Map<String, Object> headersMap) throws ApplicationException;

    /**
     * 
     * @param accountNumber
     * @param customerNumber
     * @param headersMap
     * @return true if the customer has this account
     * @throws ApplicationException
     */
    public boolean checkIfCustomerHasThisAccount(String accountNnumber, String customerNumber,
            Map<String, Object> headersMap)
            throws ApplicationException;

}
