package com.temenos.dbx.product.accounts.backenddelegate.api;

import java.util.List;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.AccountsDTO;
import com.temenos.dbx.product.dto.AllAccountsViewDTO;
import com.temenos.dbx.product.dto.MembershipOwnerDTO;

public interface AccountHolderBackendDelegate extends BackendDelegate {

    /**
     * 
     * @param accountDTO
     * @param headersMap
     * @return List<AllAccountsViewDTO> - Account information and account holder information
     * @throws ApplicationException
     */
    public List<AllAccountsViewDTO> getAccountInformation(AllAccountsViewDTO accountDTO,
            Map<String, Object> headersMap)
            throws ApplicationException;

    /**
     * 
     * @param inputDTO
     * @param headersMap
     * @return
     * @throws ApplicationException
     */
    public List<MembershipOwnerDTO> getOrganizationAccountHolderDetails(AccountsDTO inputDTO,
            Map<String, Object> headersMap)
            throws ApplicationException;
}
