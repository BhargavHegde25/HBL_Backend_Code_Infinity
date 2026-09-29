package com.kony.adminconsole.reports.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.kony.adminconsole.reports.resource.api.FabricReportsManagementResource;
import com.kony.adminconsole.reports.resource.api.ManageDataSourcesResource;
import com.kony.adminconsole.reports.resource.api.ReportsManagementResource;
import com.kony.adminconsole.reports.resource.impl.FabricReportsManagementResourceImpl;
import com.kony.adminconsole.reports.resource.impl.ManageDataSourcesResourceImpl;
import com.kony.adminconsole.reports.resource.impl.ReportsManagementResourceImpl;

public class BaseResourcesMapper implements DBPAPIMapper<Resource> {

    @Override
    public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
        Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
        map.put(ReportsManagementResource.class, ReportsManagementResourceImpl.class);
		map.put(FabricReportsManagementResource.class, FabricReportsManagementResourceImpl.class);
        map.put(ManageDataSourcesResource.class, ManageDataSourcesResourceImpl.class);
		return map;
    }
}
