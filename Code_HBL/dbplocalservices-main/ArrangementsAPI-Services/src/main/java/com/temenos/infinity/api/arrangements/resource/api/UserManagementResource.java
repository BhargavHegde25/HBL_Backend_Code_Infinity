package com.temenos.infinity.api.arrangements.resource.api;

import java.util.HashMap;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.arrangements.dto.CustomerDetailsDTO;
import com.temenos.infinity.api.arrangements.dto.UserAccountSettingsDTO;

public interface UserManagementResource extends Resource {

	Result updateAccountDetails(UserAccountSettingsDTO userAccount, DataControllerRequest request,
			HashMap<String, Object> headerMap);
	
	Result updateCustomerDetails(CustomerDetailsDTO customerDetailsDTO, HashMap<String, Object> headerMap);

}
