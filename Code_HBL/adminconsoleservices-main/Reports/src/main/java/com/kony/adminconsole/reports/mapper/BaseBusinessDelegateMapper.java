package com.kony.adminconsole.reports.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.kony.adminconsole.reports.businessdelegate.api.FabricReportsManagementBusinessDelegate;
import com.kony.adminconsole.reports.businessdelegate.api.ManageDataSourcesBusinessDelegate;
import com.kony.adminconsole.reports.businessdelegate.api.ManageReportsBusinessDelegate;
import com.kony.adminconsole.reports.businessdelegate.impl.FabricReportsManagementBusinessDelegateImpl;
import com.kony.adminconsole.reports.businessdelegate.impl.ManageDataSourcesBusinessDelegateImpl;
import com.kony.adminconsole.reports.businessdelegate.impl.ManageReportsBusinessDelegateImpl;

public class BaseBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

    @Override
    public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
        Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
        map.put(ManageReportsBusinessDelegate.class, ManageReportsBusinessDelegateImpl.class);
		map.put(FabricReportsManagementBusinessDelegate.class, FabricReportsManagementBusinessDelegateImpl.class);
        map.put(ManageDataSourcesBusinessDelegate.class, ManageDataSourcesBusinessDelegateImpl.class);
		return map;
    }

}
