package com.temenos.infinity.api.usermanagement.businessdelegate.api;

import java.util.HashMap;
import org.json.JSONArray;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.error.DBPApplicationException;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.api.usermanagement.dto.CustomerDetailsDTO;
import com.temenos.infinity.api.usermanagement.dto.UserAccountSettingsDTO;

public interface UserManagementBusinessDelegate extends BusinessDelegate {

	UserAccountSettingsDTO updateUserAccountSettings(UserAccountSettingsDTO userAccount,
			HashMap<String, Object> headerMap) throws ApplicationException;

	CustomerDetailsDTO updateCustomerDetails(CustomerDetailsDTO customerDetailsDTO, HashMap<String, Object> headerMap)
			throws ApplicationException;
	
	String uploadDocsForChangeRequest(HashMap<String, Object> dataMap)
			throws ApplicationException;
	
	boolean isValidEmailId(String email, String username, String customerId, String bankId,
			HashMap<String, Object> headerMap) throws Exception;
	
	JSONArray getUserEmailDetails(String username,String customerId,String bankId, HashMap<String, Object> headerMap) 
			throws Exception;
}
