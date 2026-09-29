package com.temenos.dbx.usermanagement.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.InfinityUserManagementBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.ProfileManagementBusinessDelegate;
import com.temenos.dbx.usermanagement.businessdelegate.api.PartyRelationsUserManagementBusinessDelegate;
import com.temenos.dbx.usermanagement.businessdelegate.api.PartyUserManagementBusinessDelegate;
import com.temenos.dbx.usermanagement.businessdelegate.impl.PartyInfinityUserManagementBusinessDelegateImpl;
import com.temenos.dbx.usermanagement.businessdelegate.impl.PartyProfileManagementBusinessDelegateImpl;
import com.temenos.dbx.usermanagement.businessdelegate.impl.PartyRelationsUserManagementBusinessDelegateImpl;
import com.temenos.dbx.usermanagement.businessdelegate.impl.PartyUserManagementBusinessDelegateImpl;

public class PartyUserManagementBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

	@Override
	public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
		Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();

		map.put(PartyUserManagementBusinessDelegate.class, PartyUserManagementBusinessDelegateImpl.class);
		map.put(PartyRelationsUserManagementBusinessDelegate.class, PartyRelationsUserManagementBusinessDelegateImpl.class);
		map.put(InfinityUserManagementBusinessDelegate.class,PartyInfinityUserManagementBusinessDelegateImpl.class);
		map.put(ProfileManagementBusinessDelegate.class,PartyProfileManagementBusinessDelegateImpl.class);
		return map;
	}

}
