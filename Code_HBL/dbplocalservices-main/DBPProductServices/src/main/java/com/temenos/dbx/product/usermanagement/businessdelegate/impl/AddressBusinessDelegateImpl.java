package com.temenos.dbx.product.usermanagement.businessdelegate.impl;

import java.util.List;
import java.util.Map;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.CustomerAddressDTO;
import com.temenos.dbx.product.dto.CustomerAddressViewDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.usermanagement.backenddelegate.api.AddressBackendDelegate;
import com.temenos.dbx.product.usermanagement.businessdelegate.api.AddressBusinessDelegate;

public class AddressBusinessDelegateImpl implements AddressBusinessDelegate {

	@Override
	public List<CustomerAddressViewDTO> getCustomerAddress(String customerId, Map<String, Object> headerMap)
			throws ApplicationException {
		AddressBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(AddressBackendDelegate.class);
		return backendDelegate.getCustomerAddress(customerId, headerMap);
	}

	@Override
	public DBXResult getCustomerAddressForUserResponse(CustomerAddressViewDTO addressDTO, Map<String, Object> headerMap) {
		AddressBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(AddressBackendDelegate.class);
		return backendDelegate.getCustomerAddressForUserResponse(addressDTO, headerMap);
	}

}
