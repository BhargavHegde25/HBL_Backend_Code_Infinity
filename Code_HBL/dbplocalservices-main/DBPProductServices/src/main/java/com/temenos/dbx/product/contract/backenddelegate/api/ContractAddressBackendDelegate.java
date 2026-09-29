package com.temenos.dbx.product.contract.backenddelegate.api;

import java.util.List;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.AddressDTO;
import com.temenos.dbx.product.dto.ContractAddressDTO;

public interface ContractAddressBackendDelegate extends BackendDelegate {

    public List<AddressDTO> getContractAddress(String contractId, Map<String, Object> headersMap)
            throws ApplicationException;

    public ContractAddressDTO createContractAddress(ContractAddressDTO dto, Map<String, Object> headersMap)
            throws ApplicationException;
}
