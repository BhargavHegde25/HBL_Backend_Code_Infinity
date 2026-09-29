package com.kony.adminconsole.campaign.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.kony.adminconsole.campaign.resource.ApplicationResource;
import com.kony.adminconsole.campaign.resource.CustomerManagementResource;
import com.kony.adminconsole.campaign.resource.DataContextResource;
import com.kony.adminconsole.campaign.resource.UpdateUsersForSegmentsResource;
import com.kony.adminconsole.campaign.resource.impl.ApplicationResourceImpl;
import com.kony.adminconsole.campaign.resource.impl.CustomerManagementResourceImpl;
import com.kony.adminconsole.campaign.resource.impl.DataContextResourceImpl;
import com.kony.adminconsole.campaign.resource.impl.UpdateUsersForSegmentsResourceImpl;

public class BaseResourcesMapper implements DBPAPIMapper<Resource> {

    @Override
    public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
        Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
        map.put(UpdateUsersForSegmentsResource.class, UpdateUsersForSegmentsResourceImpl.class);
        map.put(DataContextResource.class, DataContextResourceImpl.class);
        map.put(CustomerManagementResource.class, CustomerManagementResourceImpl.class);
        map.put(ApplicationResource.class, ApplicationResourceImpl.class);
        return map;
    }
}
