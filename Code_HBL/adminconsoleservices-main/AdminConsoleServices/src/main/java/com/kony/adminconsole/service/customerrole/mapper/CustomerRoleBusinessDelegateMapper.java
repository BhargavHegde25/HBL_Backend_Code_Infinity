package com.kony.adminconsole.service.customerrole.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.kony.adminconsole.service.customerrole.businessdelegate.api.CustomerRoleBusinessDelegate;
import com.kony.adminconsole.service.customerrole.businessdelegate.impl.CustomerRoleBusinessDelegateImpl;

public class CustomerRoleBusinessDelegateMapper  implements DBPAPIMapper<BusinessDelegate> {

	@Override
	public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
        Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
        map.put(CustomerRoleBusinessDelegate.class,CustomerRoleBusinessDelegateImpl.class);
        return map;
	}

}
