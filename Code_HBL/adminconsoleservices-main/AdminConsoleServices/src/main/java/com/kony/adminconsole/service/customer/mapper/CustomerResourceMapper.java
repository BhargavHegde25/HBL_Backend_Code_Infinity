package com.kony.adminconsole.service.customer.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.kony.adminconsole.service.customer.resource.api.CustomerResource;
import com.kony.adminconsole.service.customer.resource.api.InfinityCustomerResource;
import com.kony.adminconsole.service.customer.resource.api.InfinityUserManagementResource;
import com.kony.adminconsole.service.customer.resource.api.PartyUserManagementResource;
import com.kony.adminconsole.service.customer.resource.impl.CustomerResourceImpl;
import com.kony.adminconsole.service.customer.resource.impl.InfinityCustomerResourceImpl;
import com.kony.adminconsole.service.customer.resource.impl.InfinityUserManagementResourceImpl;
import com.kony.adminconsole.service.customer.resource.impl.PartyUserManagementResourceImpl;

/**
 * 
 * @author This class maps resource interface to its implementation and it can be changed whenever required
 *
 */

public class CustomerResourceMapper implements DBPAPIMapper<Resource> {

    @Override
    public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
        Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
        /* all resource interface to implementation mappings are done here */
        map.put(CustomerResource.class, CustomerResourceImpl.class);
        map.put(InfinityUserManagementResource.class, InfinityUserManagementResourceImpl.class);
        map.put(PartyUserManagementResource.class, PartyUserManagementResourceImpl.class);
        map.put(InfinityCustomerResource.class, InfinityCustomerResourceImpl.class);
        return map;
    }
}
