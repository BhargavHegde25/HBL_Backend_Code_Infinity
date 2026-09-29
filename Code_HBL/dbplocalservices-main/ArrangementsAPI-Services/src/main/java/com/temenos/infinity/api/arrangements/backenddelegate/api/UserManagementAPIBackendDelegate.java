package com.temenos.infinity.api.arrangements.backenddelegate.api;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;

import com.dbp.core.api.BackendDelegate;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.api.arrangements.dto.CustomerDetailsDTO;
import com.temenos.infinity.api.arrangements.dto.UserAccountSettingsDTO;

public interface UserManagementAPIBackendDelegate extends BackendDelegate {

	UserAccountSettingsDTO updateUserAccountSettings(UserAccountSettingsDTO userAccount,
			HashMap<String, Object> headerMap) throws ApplicationException;

	CustomerDetailsDTO updateCustomerDetails(CustomerDetailsDTO customerDetailsDTO, HashMap<String, Object> headerMap)
			throws ApplicationException;

	Set<String> getUserEmailIds(String username, String customerId, String bankId, Map<String, Object> map)
			throws JSONException, UnsupportedEncodingException;
}