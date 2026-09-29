package com.temenos.infinity.api.arrangements.businessdelegate.api;

import java.util.HashMap;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.api.arrangements.dto.CustomerDetailsDTO;
import com.temenos.infinity.api.arrangements.dto.UserAccountSettingsDTO;

public interface UserManagementBusinessDelegate extends BusinessDelegate {

	UserAccountSettingsDTO updateUserAccountSettings(UserAccountSettingsDTO userAccount,
			HashMap<String, Object> headerMap) throws ApplicationException;

	CustomerDetailsDTO updateCustomerDetails(CustomerDetailsDTO customerDetailsDTO, HashMap<String, Object> headerMap)
			throws ApplicationException;
	
	boolean isValidEmailId(String email, String username, String customerId, String bankId,
			HashMap<String, Object> headerMap) throws Exception;

}
