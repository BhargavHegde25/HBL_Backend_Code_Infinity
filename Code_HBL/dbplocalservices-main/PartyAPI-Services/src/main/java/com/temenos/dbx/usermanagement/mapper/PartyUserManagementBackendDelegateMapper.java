package com.temenos.dbx.usermanagement.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.CoreCustomerBackendDelegate;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.AddressBackendDelegate;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.InfinityUserManagementBackendDelegate;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.ProfileManagementBackendDelegate;
import com.temenos.dbx.product.usermanagement.backenddelegate.api.CommunicationBackendDelegate;
import com.temenos.dbx.usermanagement.backenddelegate.api.PartyBackendDelegate;
import com.temenos.dbx.usermanagement.backenddelegate.api.PartyIdentifierBackendDelegate;
import com.temenos.dbx.usermanagement.backenddelegate.api.PartyRelationsUserManagementBackendDelegate;
import com.temenos.dbx.usermanagement.backenddelegate.api.PartyUserManagementBackendDelegate;
import com.temenos.dbx.usermanagement.backenddelegate.impl.CoreCustomerPartyBackendDelegateImpl;
import com.temenos.dbx.usermanagement.backenddelegate.impl.PartyAddressBackendDelegateImpl;
import com.temenos.dbx.usermanagement.backenddelegate.impl.PartyBackendDelegateImpl;
import com.temenos.dbx.usermanagement.backenddelegate.impl.PartyCommunicationBackendDelegateImpl;
import com.temenos.dbx.usermanagement.backenddelegate.impl.PartyIdentifierBackendDelegateImpl;
import com.temenos.dbx.usermanagement.backenddelegate.impl.PartyInfinityUserManagementBackendDelegateImpl;
import com.temenos.dbx.usermanagement.backenddelegate.impl.PartyProfileManagementBackendDelegateImpl;
import com.temenos.dbx.usermanagement.backenddelegate.impl.PartyRelationsUserManagementBackendDelegateImpl;
import com.temenos.dbx.usermanagement.backenddelegate.impl.PartyUserManagementBackendDelegateImpl;

public class PartyUserManagementBackendDelegateMapper implements DBPAPIMapper<BackendDelegate> {

	@Override
	public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {
		Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();
		map.put(AddressBackendDelegate.class,PartyAddressBackendDelegateImpl.class);
		map.put(PartyBackendDelegate.class,PartyBackendDelegateImpl.class);
		map.put(CommunicationBackendDelegate.class,PartyCommunicationBackendDelegateImpl.class);
		map.put(ProfileManagementBackendDelegate.class,PartyProfileManagementBackendDelegateImpl.class);
		map.put(CoreCustomerBackendDelegate.class, CoreCustomerPartyBackendDelegateImpl.class);
		map.put(PartyIdentifierBackendDelegate.class,PartyIdentifierBackendDelegateImpl.class);
		map.put(InfinityUserManagementBackendDelegate.class,PartyInfinityUserManagementBackendDelegateImpl.class);
		map.put(PartyUserManagementBackendDelegate.class,PartyUserManagementBackendDelegateImpl.class);
		map.put(PartyRelationsUserManagementBackendDelegate.class,PartyRelationsUserManagementBackendDelegateImpl.class);
		return map;
	}

}
