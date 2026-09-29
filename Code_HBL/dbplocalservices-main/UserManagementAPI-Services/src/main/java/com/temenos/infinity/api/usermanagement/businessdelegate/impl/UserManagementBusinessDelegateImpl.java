package com.temenos.infinity.api.usermanagement.businessdelegate.impl;

import java.util.HashMap;
import java.util.Set;

	import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.error.DBPApplicationException;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.api.usermanagement.backenddelegate.api.UserManagementAPIBackendDelegate;
import com.temenos.infinity.api.usermanagement.backenddelegate.impl.UserManagementAPIBackendDelegateImpl;
import com.temenos.infinity.api.usermanagement.businessdelegate.api.UserManagementBusinessDelegate;
import com.temenos.infinity.api.usermanagement.config.ServerConfigurations;
import com.temenos.infinity.api.usermanagement.dto.CustomerDetailsDTO;
import com.temenos.infinity.api.usermanagement.dto.UserAccountSettingsDTO;

public class UserManagementBusinessDelegateImpl implements UserManagementBusinessDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
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
	public String uploadDocsForChangeRequest(HashMap<String, Object> dataMap)throws ApplicationException {
		UserManagementAPIBackendDelegate orderBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(UserManagementAPIBackendDelegate.class);
		HashMap<String, Object> headerMap = new HashMap<String, Object>();
		//Result result=new Result();
		String result = null;
				try {
					result = orderBackendDelegate.uploadDocsForChangeRequest(
							dataMap,headerMap);
				} catch (Exception e) {
					alert.prepareError("Error in UserMAnagementAPIBusinessDelegateImpl : uploadMultipleDocuments"+ e.getMessage()).log();
} 
		return result;
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
	
	@Override
	public JSONArray getUserEmailDetails(String username,String customerId,String bankId, HashMap<String, Object> headerMap) throws Exception {
		UserManagementAPIBackendDelegate orderBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(UserManagementAPIBackendDelegate.class);
		@SuppressWarnings("rawtypes")
		JSONArray userEmailDetails = new JSONArray();
		userEmailDetails = orderBackendDelegate.getUserEmailDetails(username, customerId, bankId, headerMap);
	    return userEmailDetails;
	}

}