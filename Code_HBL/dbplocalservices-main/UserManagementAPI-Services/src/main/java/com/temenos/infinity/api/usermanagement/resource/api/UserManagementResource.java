package com.temenos.infinity.api.usermanagement.resource.api;

import java.util.HashMap;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.usermanagement.dto.CustomerDetailsDTO;
import com.temenos.infinity.api.usermanagement.dto.UserAccountSettingsDTO;

public interface UserManagementResource extends Resource {

	Result updateAccountDetails(UserAccountSettingsDTO userAccount, DataControllerRequest request,
			HashMap<String, Object> headerMap);
	
	Result updateCustomerDetails(CustomerDetailsDTO customerDetailsDTO, DataControllerRequest request, 
			HashMap<String, Object> headerMap);
	
	String uploadDocsForChangeRequest(DataControllerRequest request);

}