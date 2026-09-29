package com.temenos.dbx.product.businessdelegate.api;

import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.AddressDTO;

public interface AddressBusinessDelegate extends BusinessDelegate {

    public AddressDTO getAddressDetails(String addressId, Map<String, Object> headerMap)
            throws ApplicationException;

    public AddressDTO createAddress(AddressDTO inputDTO, Map<String, Object> headerMap)
            throws ApplicationException;

}
