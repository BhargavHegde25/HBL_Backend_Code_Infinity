package com.temenos.dbx.product.usermanagement.businessdelegate.api;

import java.util.List;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.CustomerAddressDTO;
import com.temenos.dbx.product.dto.CustomerAddressViewDTO;
import com.temenos.dbx.product.dto.DBXResult;

public interface AddressBusinessDelegate extends BusinessDelegate {

    public List<CustomerAddressViewDTO> getCustomerAddress(String customerId, Map<String, Object> headerMap)
            throws ApplicationException;

	public DBXResult getCustomerAddressForUserResponse(CustomerAddressViewDTO addressDTO, Map<String, Object> headerMap);

}
