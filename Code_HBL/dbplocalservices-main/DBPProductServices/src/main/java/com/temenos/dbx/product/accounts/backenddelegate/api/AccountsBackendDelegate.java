package com.temenos.dbx.product.accounts.backenddelegate.api;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dbp.core.api.BackendDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.AccountTypeDTO;
import com.temenos.dbx.product.dto.AccountsDTO;
import com.temenos.dbx.product.dto.AllAccountsViewDTO;

/**
 * 
 * @author sowmya.vandanapu
 * @version 1.0
 */
public interface AccountsBackendDelegate extends BackendDelegate {

    /**
     * 
     * @param accountDTO
     * @param headersMap
     * @return List<AccountsDTO>
     * @throws ApplicationException
     */
    public List<AccountsDTO> checkIfAccountAvailable(AccountsDTO accountDTO, Map<String, Object> headersMap)
            throws ApplicationException;

    /**
     * 
     * @param headersMap
     * @return
     * @throws ApplicationException
     */
    public List<AccountTypeDTO> getAccountTypes(Map<String, Object> headersMap) throws ApplicationException;

    /**
     * 
     * @param accountId
     * @param headersMap
     * @return
     * @throws ApplicationException
     */
    public AccountsDTO getAccountDetailsByAccountID(String accountId, Map<String, Object> headersMap)
            throws ApplicationException;

    /**
     * 
     * @param dto
     * @param headersMap
     * @return
     * @throws ApplicationException
     */
    public AccountsDTO createAccount(AccountsDTO dto, Map<String, Object> headersMap) throws ApplicationException;

    /**
     * 
     * @param dto
     * @param headersMap
     * @return
     * @throws ApplicationException
     */
    public AccountsDTO updateAccount(AccountsDTO dto, Map<String, Object> headersMap) throws ApplicationException;

    /**
     * 
     * @param AccountsDTO
     *            inputDTO
     * @param headersMap
     * @return
     * @throws ApplicationException
     */
    public List<AccountsDTO> getOrganizationAccounts(AccountsDTO inputDTO, Map<String, Object> headersMap)
            throws ApplicationException;

    public Map<String,String> processNewAccounts(List<AllAccountsViewDTO> allAccounts, String customerId,
            Map<String, Object> headersMap) throws ApplicationException;

}
