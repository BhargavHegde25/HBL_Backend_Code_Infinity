package com.hbl.backenddeligate.api;

import java.util.Map;

import com.google.gson.JsonObject;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.InfinityUserManagementBackendDelegate;

public interface InfinityUserManagementBackendDelegateExtn extends InfinityUserManagementBackendDelegate{
	 public void getInfinityUser(JsonObject jsonObject, Map<String, Object> headerMap, String customerId,
	            String loggedInUserId,String contractIdInput,String coreCustomerIdInput, boolean isSuperAdmin, String legalEntityId);

}
