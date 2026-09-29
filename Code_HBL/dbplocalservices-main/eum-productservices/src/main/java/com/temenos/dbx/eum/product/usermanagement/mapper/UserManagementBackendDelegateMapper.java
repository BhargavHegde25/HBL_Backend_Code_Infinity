package com.temenos.dbx.eum.product.usermanagement.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.CoreCustomerBackendDelegate;
import com.temenos.dbx.eum.product.contract.backenddelegate.impl.CoreCustomerBackendDelegateImpl;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.AddressBackendDelegate;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.InfinityUserManagementBackendDelegate;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.ProfileManagementBackendDelegate;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.impl.AddressBackendDelegateImpl;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.impl.CommunicationBackendDelegateImpl;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.impl.InfinityUserManagementBackendDelegateImpl;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.impl.ProfileManagementBackendDelegateImpl;
import com.temenos.dbx.product.usermanagement.backenddelegate.api.CommunicationBackendDelegate;

public class UserManagementBackendDelegateMapper implements DBPAPIMapper<BackendDelegate> {

	@Override
	public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {

		Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();

		map.put(AddressBackendDelegate.class, AddressBackendDelegateImpl.class);
		map.put(CommunicationBackendDelegate.class, CommunicationBackendDelegateImpl.class);
		map.put(ProfileManagementBackendDelegate.class, ProfileManagementBackendDelegateImpl.class);
		map.put(InfinityUserManagementBackendDelegate.class, InfinityUserManagementBackendDelegateImpl.class);
		map.put(CoreCustomerBackendDelegate.class, CoreCustomerBackendDelegateImpl.class);
        
		return map;
	}

}
