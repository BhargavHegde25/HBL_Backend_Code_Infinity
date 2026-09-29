package com.temenos.dbx.product.accounts.businessdelegate.api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.dbp.core.api.BusinessDelegate;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.AccountTypeDTO;
import com.temenos.dbx.product.dto.AccountsDTO;
import com.temenos.dbx.product.dto.AllAccountsViewDTO;

/**
 * 
 * @author sowmya.vandanapu
 * @version 1.0
 *
 */
public interface AccountsBusinessDelegate extends BusinessDelegate {

    /**
     * 
     * @param accountDTO
     * @param headersMap
     * @return AllAccountsViewDTO validated input DTO
     * @throws ApplicationException
     */
    public AllAccountsViewDTO validateGetAccountInformationInput(Map<String, String> inputParams)
            throws ApplicationException;

    /**
     * Fetches the account information based on input
     * 
     * @param accountDTO
     * @param headersMap
     * @return List<AllAccountsViewDTO>
     * @throws ApplicationException
     */
    public List<AllAccountsViewDTO> getAccountInformation(AllAccountsViewDTO accountDTO, Map<String, Object> headersMap)
            throws ApplicationException;

    /**
     * validates the Account information against input
     * 
     * @param accountsInformation
     * @return
     * @throws ApplicationException
     */
    public AllAccountsViewDTO validateGetAccountInformationOutput(List<AllAccountsViewDTO> accountHolder,
            Map<String, String> inputParams)
            throws ApplicationException;

    /**
     * 
     * @param accountDTO
     * @return JsonObject containing the required params that are to be set as part of dcRequest
     */
    public JsonObject getAccountInformationServiceKeyParameters(AllAccountsViewDTO accountDTO);

    /**
     * 
     * @param accountDTO
     * @param headersMap
     * @return
     * @throws ApplicationException
     */
    public boolean checkAccountIfAvailable(AccountsDTO accountDTO, Map<String, Object> headersMap)
            throws ApplicationException;

    /**
     * 
     * @param accountDTO
     * @param headersMap
     * @return
     * @throws ApplicationException
     */
    public List<AllAccountsViewDTO> getAllAccountsInformation(AllAccountsViewDTO accountDTO,
            Map<String, Object> headersMap) throws ApplicationException;

    /**
     * 
     * @param accountsCSV
     * @param headersMap
     * @return
     * @throws ApplicationException
     */
    public String[] getUnUsedAccountsList(String accountsCSV, Map<String, Object> headersMap)
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
     * @return accounts linked to organization accounts
     * @throws ApplicationException
     */
    public List<AccountsDTO> getOrganizationAccounts(AccountsDTO inputDTO, Map<String, Object> headersMap)
            throws ApplicationException;

    /**
     * 
     * @param AccountsDTO
     *            inputDTO
     * @param headersMap
     * @return MembershipOwnerDTO account holder details for input accountId
     * @throws ApplicationException
     */
    public List<AllAccountsViewDTO> getAccountHolderDetails(AllAccountsViewDTO inputDTO, Map<String, Object> headersMap)
            throws ApplicationException;

    /**
     * 
     * @param dtoList
     * @param headersMap
     * @return
     * @throws ApplicationException
     */
    public List<AllAccountsViewDTO> getUnUsedAccountsFromArray(List<AllAccountsViewDTO> dtoList,
            Map<String, Object> headersMap) throws ApplicationException;

    /**
     * 
     * @param membershipId
     * @param headersMap
     * @return
     * @throws ApplicationException
     */
    public boolean checkIfUnUsedAccountsExistsByMembershipId(String membershipId, Map<String, Object> headersMap)
            throws ApplicationException;

    /**
     * 
     * @param membershipId
     * @param headersMap
     * @return
     * @throws ApplicationException
     */
    public List<AllAccountsViewDTO> getUnUsedAccountsFromMembership(String membershipId, Map<String, Object> headersMap)
            throws ApplicationException;

    /**
     * 
     * @param serviceKeyPayload
     * @param masterServiceKeyPayload
     * @param serviceKey
     * @param masterServiceKey
     * @param headersMap
     * @return
     * @throws ApplicationException
     * @throws IOException
     */
    public JSONObject getCustomerCentricAccounts(JsonObject serviceKeyPayload, JsonObject masterServiceKeyPayload,
            String serviceKey, String masterServiceKey, Map<String, Object> headersMap)
            throws ApplicationException, IOException;

    /**
     * 
     * @param inputDTO
     * @param headersMap
     * @return
     * @throws ApplicationException
     */
    public List<AccountsDTO> getOrganizationAccountHolderDetails(List<AccountsDTO> inputDTO,
            Map<String, Object> headersMap)
            throws ApplicationException;
}
