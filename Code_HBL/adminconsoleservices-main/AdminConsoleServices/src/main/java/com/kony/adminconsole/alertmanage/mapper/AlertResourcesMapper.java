package com.kony.adminconsole.alertmanage.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.kony.adminconsole.alertmanage.resource.CustExternalAlertSubscriptionResource;
import com.kony.adminconsole.alertmanage.resource.impl.CustExternalAlertSubscriptionResourceImpl;

public class AlertResourcesMapper implements DBPAPIMapper<Resource> {

    @Override
    public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
        Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
        map.put(CustExternalAlertSubscriptionResource.class, CustExternalAlertSubscriptionResourceImpl.class);
        return map;
    }
}
