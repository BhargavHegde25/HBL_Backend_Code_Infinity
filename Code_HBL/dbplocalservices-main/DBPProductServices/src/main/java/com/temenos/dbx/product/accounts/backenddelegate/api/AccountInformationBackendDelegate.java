package com.temenos.dbx.product.accounts.backenddelegate.api;

import java.util.List;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.AllAccountsViewDTO;

public interface AccountInformationBackendDelegate extends BackendDelegate {

    /**
     * 
     * @param accountDTO
     * @param headersMap
     * @return AllAccountsViewDTO
     * @throws ApplicationException
     */
    public List<AllAccountsViewDTO> getAllAccountsInformation(AllAccountsViewDTO accountDTO,
            Map<String, Object> headersMap) throws ApplicationException;
}
