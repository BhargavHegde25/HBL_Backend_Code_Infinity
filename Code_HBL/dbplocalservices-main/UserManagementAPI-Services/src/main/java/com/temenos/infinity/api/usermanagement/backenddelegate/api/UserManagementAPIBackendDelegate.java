package com.temenos.infinity.api.usermanagement.backenddelegate.api;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.error.DBPApplicationException;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.api.usermanagement.dto.CustomerDetailsDTO;
import com.temenos.infinity.api.usermanagement.dto.UserAccountSettingsDTO;

public interface UserManagementAPIBackendDelegate extends BackendDelegate {

	UserAccountSettingsDTO updateUserAccountSettings(UserAccountSettingsDTO userAccount,
			HashMap<String, Object> headerMap) throws ApplicationException;

	CustomerDetailsDTO updateCustomerDetails(CustomerDetailsDTO customerDetailsDTO, HashMap<String, Object> headerMap)
			throws ApplicationException;

	String uploadDocsForChangeRequest(HashMap<String, Object> dataMap,HashMap<String, Object> headerMap)
			throws ApplicationException, DBPApplicationException;
	
	Set<String> getUserEmailIds(String username, String customerId, String bankId, Map<String, Object> map)
			throws JSONException, UnsupportedEncodingException;
	
	JSONArray getUserEmailDetails(String username, String customerId, String bankId, Map<String, Object> map)
			throws JSONException, UnsupportedEncodingException;
}
