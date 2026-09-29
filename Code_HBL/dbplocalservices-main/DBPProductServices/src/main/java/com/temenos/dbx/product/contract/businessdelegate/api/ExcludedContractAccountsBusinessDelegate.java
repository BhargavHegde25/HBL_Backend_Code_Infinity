package com.temenos.dbx.product.contract.businessdelegate.api;

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

}
