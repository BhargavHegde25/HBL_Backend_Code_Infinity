package com.kony.adminconsole.service.customer.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.kony.adminconsole.service.customer.businessdelegate.api.CustomerBusinessDelegate;
import com.kony.adminconsole.service.customer.businessdelegate.api.InfinityCustomerBusinessDelegate;
import com.kony.adminconsole.service.customer.businessdelegate.api.InfinityUserManagementBusinessDelegate;
import com.kony.adminconsole.service.customer.businessdelegate.api.PartyUserManagementBusinessDelegate;
import com.kony.adminconsole.service.customer.businessdelegate.impl.CustomerBusinessDelegateImpl;
import com.kony.adminconsole.service.customer.businessdelegate.impl.InfinityCustomerBusinessDelegateImpl;
import com.kony.adminconsole.service.customer.businessdelegate.impl.InfinityUserManagementBusinessDelegateImpl;
import com.kony.adminconsole.service.customer.businessdelegate.impl.PartyUserManagementBusinessDelegateImpl;

public class CustomerBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

    @Override
    public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
        Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
        map.put(CustomerBusinessDelegate.class, CustomerBusinessDelegateImpl.class);
        map.put(InfinityUserManagementBusinessDelegate.class, InfinityUserManagementBusinessDelegateImpl.class);
        map.put(PartyUserManagementBusinessDelegate.class, PartyUserManagementBusinessDelegateImpl.class);
        map.put(InfinityCustomerBusinessDelegate.class, InfinityCustomerBusinessDelegateImpl.class);

        return map;
    }
}
