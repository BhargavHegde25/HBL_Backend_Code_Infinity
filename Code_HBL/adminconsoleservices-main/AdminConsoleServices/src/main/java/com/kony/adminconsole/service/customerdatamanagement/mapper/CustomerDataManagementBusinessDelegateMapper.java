package com.kony.adminconsole.service.customerdatamanagement.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.kony.adminconsole.service.customerdatamanagement.businessdelegate.api.CustomerDataManagementBusinessDelegate;
import com.kony.adminconsole.service.customerdatamanagement.businessdelegate.impl.CustomerDataManagementBusinessDelegateImpl;

public class CustomerDataManagementBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

    @Override
    public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
        Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
        map.put(CustomerDataManagementBusinessDelegate.class, CustomerDataManagementBusinessDelegateImpl.class);
        return map;
    }
}
