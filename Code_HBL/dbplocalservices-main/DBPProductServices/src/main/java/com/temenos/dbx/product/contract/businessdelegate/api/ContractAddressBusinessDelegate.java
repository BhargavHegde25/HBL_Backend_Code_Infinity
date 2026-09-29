package com.temenos.dbx.product.contract.businessdelegate.api;

import java.util.List;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.AddressDTO;
import com.temenos.dbx.product.dto.ContractAddressDTO;

public interface ContractAddressBusinessDelegate extends BusinessDelegate {

    public ContractAddressDTO createContractAddress(ContractAddressDTO dto, Map<String, Object> headersMap)
            throws ApplicationException;

    public List<AddressDTO> getContractAddress(String contractId, Map<String, Object> headersMap)
            throws ApplicationException;
}
