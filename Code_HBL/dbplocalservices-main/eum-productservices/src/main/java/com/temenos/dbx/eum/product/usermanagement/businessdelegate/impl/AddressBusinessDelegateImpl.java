package com.temenos.dbx.eum.product.usermanagement.businessdelegate.impl;

import java.util.List;
import java.util.Map;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.AddressBackendDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.AddressBusinessDelegate;
import com.temenos.dbx.product.dto.CustomerAddressDTO;
import com.temenos.dbx.product.dto.CustomerAddressViewDTO;
import com.temenos.dbx.product.dto.DBXResult;

public class AddressBusinessDelegateImpl implements AddressBusinessDelegate {

	@Override
	public List<CustomerAddressViewDTO> getCustomerAddress(String customerId, String legalEntityId, Map<String, Object> headerMap)
			throws ApplicationException {
		AddressBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(AddressBackendDelegate.class);
		return backendDelegate.getCustomerAddress(customerId, legalEntityId, headerMap);
	}

	@Override
	public DBXResult getCustomerAddressForUserResponse(CustomerAddressViewDTO addressDTO, Map<String, Object> headerMap) {
		AddressBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(AddressBackendDelegate.class);
		return backendDelegate.getCustomerAddressForUserResponse(addressDTO, headerMap);
	}

}
