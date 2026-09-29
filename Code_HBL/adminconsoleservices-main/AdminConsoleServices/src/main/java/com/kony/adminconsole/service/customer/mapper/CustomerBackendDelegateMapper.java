package com.kony.adminconsole.service.customer.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.kony.adminconsole.service.customer.backenddelegate.api.CustomerBackendDelegate;
import com.kony.adminconsole.service.customer.backenddelegate.api.InfinityCustomerBackendDelegate;
import com.kony.adminconsole.service.customer.backenddelegate.api.InfinityUserManagementBackendDelegate;
import com.kony.adminconsole.service.customer.backenddelegate.impl.CustomerBackendDelegateImpl;
import com.kony.adminconsole.service.customer.backenddelegate.impl.InfinityCustomerBackendDelegateImpl;
import com.kony.adminconsole.service.customer.backenddelegate.impl.InfinityUserManagementBackendDelegateImpl;

public class CustomerBackendDelegateMapper implements DBPAPIMapper<BackendDelegate> {
    @Override
    public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {

        Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();

        /* mappings of backend delegate interface and implementation to be added */
        map.put(CustomerBackendDelegate.class, CustomerBackendDelegateImpl.class);
        map.put(InfinityUserManagementBackendDelegate.class, InfinityUserManagementBackendDelegateImpl.class);
        map.put(InfinityCustomerBackendDelegate.class, InfinityCustomerBackendDelegateImpl.class);
        return map;
    }

}
