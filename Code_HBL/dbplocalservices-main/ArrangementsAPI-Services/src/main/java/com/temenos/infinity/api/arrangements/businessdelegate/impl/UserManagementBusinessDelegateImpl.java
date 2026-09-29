package com.temenos.infinity.api.arrangements.businessdelegate.impl;

import java.util.HashMap;
import java.util.Set;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.api.arrangements.backenddelegate.api.UserManagementAPIBackendDelegate;
import com.temenos.infinity.api.arrangements.businessdelegate.api.UserManagementBusinessDelegate;
import com.temenos.infinity.api.arrangements.dto.CustomerDetailsDTO;
import com.temenos.infinity.api.arrangements.dto.UserAccountSettingsDTO;

public class UserManagementBusinessDelegateImpl implements UserManagementBusinessDelegate {

	public UserAccountSettingsDTO updateUserAccountSettings(UserAccountSettingsDTO userAccount,
			HashMap<String, Object> headerMap) throws ApplicationException {
		UserManagementAPIBackendDelegate orderBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(UserManagementAPIBackendDelegate.class);
		UserAccountSettingsDTO userAccountOrder = orderBackendDelegate.updateUserAccountSettings(userAccount,
				headerMap);
		return userAccountOrder;
	}

	@Override
	public CustomerDetailsDTO updateCustomerDetails(CustomerDetailsDTO customerDetailsDTO,
			HashMap<String, Object> headerMap) throws ApplicationException {
		UserManagementAPIBackendDelegate orderBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(UserManagementAPIBackendDelegate.class);
		CustomerDetailsDTO customerDetailsOrderDTO = orderBackendDelegate.updateCustomerDetails(customerDetailsDTO,
				headerMap);
		return customerDetailsOrderDTO;
	}
	
	@Override
	public boolean isValidEmailId(String emailId,String username,String customerId,String bankId, HashMap<String, Object> headerMap) throws Exception {
		UserManagementAPIBackendDelegate orderBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(UserManagementAPIBackendDelegate.class);
		@SuppressWarnings("rawtypes")
		Set<String> validEmailIds = orderBackendDelegate.getUserEmailIds(username, customerId, bankId, headerMap);
			if(validEmailIds.contains(emailId))
			return true;
			else
			return false;
	}

}