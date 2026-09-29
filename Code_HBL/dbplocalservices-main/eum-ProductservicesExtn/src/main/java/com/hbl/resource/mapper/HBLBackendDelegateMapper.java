package com.hbl.resource.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.hbl.backenddeligate.impl.InfinityUserManagementBackendDelegateImplExtn;
import com.hbl.resource.impl.CommunicationBackendDelegateImplExtn;
import com.hbl.resource.impl.ContractBackendDelegateImplExtn;
import com.hbl.resource.impl.CoreCustomerBackendDelegateImplExtn;
import com.hbl.resource.impl.ProfileManagementBackendDelegateImplExtn;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.ContractBackendDelegate;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.CoreCustomerBackendDelegate;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.InfinityUserManagementBackendDelegate;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.ProfileManagementBackendDelegate;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.impl.ProfileManagementBackendDelegateImpl;
import com.temenos.dbx.product.usermanagement.backenddelegate.api.CommunicationBackendDelegate;

public class HBLBackendDelegateMapper implements DBPAPIMapper<BackendDelegate>{

	@Override
	public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {
		// TODO Auto-generated method stub
		Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();
		map.put(ContractBackendDelegate.class, ContractBackendDelegateImplExtn.class);
		map.put(CoreCustomerBackendDelegate.class, CoreCustomerBackendDelegateImplExtn.class);
		map.put(ProfileManagementBackendDelegate.class, ProfileManagementBackendDelegateImplExtn.class);
		map.put(InfinityUserManagementBackendDelegate.class, InfinityUserManagementBackendDelegateImplExtn.class);
		map.put(CommunicationBackendDelegate.class, CommunicationBackendDelegateImplExtn.class);
		return map;
	}

}
