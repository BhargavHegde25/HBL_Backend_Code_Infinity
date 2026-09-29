package com.temenos.dbx.eum.product.usermanagement.backenddelegate.api;

import java.util.List;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.AddressDTO;
import com.temenos.dbx.product.dto.CustomerAddressViewDTO;
import com.temenos.dbx.product.dto.DBXResult;

public interface AddressBackendDelegate extends BackendDelegate {

    public List<CustomerAddressViewDTO> getCustomerAddress(String customerId, String legalEntityId, Map<String, Object> headerMap)
            throws ApplicationException;

    public DBXResult getCustomerAddressForUserResponse(CustomerAddressViewDTO addressDTO,
            Map<String, Object> headerMap);

    public AddressDTO getAddressDetails(String addressId, Map<String, Object> headerMap)
            throws ApplicationException;

}
