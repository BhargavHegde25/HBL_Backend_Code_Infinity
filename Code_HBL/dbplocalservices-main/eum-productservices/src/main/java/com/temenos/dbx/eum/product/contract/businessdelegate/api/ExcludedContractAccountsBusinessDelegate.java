package com.temenos.dbx.eum.product.contract.businessdelegate.api;

import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.ExcludedContractAccountDTO;

public interface ExcludedContractAccountsBusinessDelegate extends BusinessDelegate {

    public ExcludedContractAccountDTO createExcludedContractAccount(ExcludedContractAccountDTO dto,
            Map<String, Object> headersMap)
            throws ApplicationException;

    public ExcludedContractAccountDTO getExcludedContractAccount(ExcludedContractAccountDTO dto,
            Map<String, Object> headersMap)
            throws ApplicationException;
    
    public void deleteExcludedContractAccount(String accountId, String contractId, String coreCustomerId,
			Map<String, Object> headersMap);

}
