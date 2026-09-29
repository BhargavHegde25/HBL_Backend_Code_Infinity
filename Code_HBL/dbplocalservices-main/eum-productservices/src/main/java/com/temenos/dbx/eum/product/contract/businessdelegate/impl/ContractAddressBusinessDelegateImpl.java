package com.temenos.dbx.eum.product.contract.businessdelegate.impl;

import java.util.List;
import java.util.Map;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.ContractAddressBackendDelegate;
import com.temenos.dbx.eum.product.contract.businessdelegate.api.ContractAddressBusinessDelegate;
import com.temenos.dbx.product.dto.AddressDTO;
import com.temenos.dbx.product.dto.ContractAddressDTO;

public class ContractAddressBusinessDelegateImpl implements ContractAddressBusinessDelegate {

    @Override
    public ContractAddressDTO createContractAddress(ContractAddressDTO inputDTO, Map<String, Object> headersMap)
            throws ApplicationException {
        ContractAddressBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(ContractAddressBackendDelegate.class);
        return backendDelegate.createContractAddress(inputDTO, headersMap);
    }

    @Override
    public List<AddressDTO> getContractAddress(String contractId, Map<String, Object> headersMap)
            throws ApplicationException {
        ContractAddressBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(ContractAddressBackendDelegate.class);
        return backendDelegate.getContractAddress(contractId, headersMap);
    }

}
