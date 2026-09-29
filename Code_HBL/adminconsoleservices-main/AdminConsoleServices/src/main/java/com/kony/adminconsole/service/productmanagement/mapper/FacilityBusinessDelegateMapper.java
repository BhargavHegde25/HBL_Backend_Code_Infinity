package com.kony.adminconsole.service.productmanagement.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.kony.adminconsole.service.productmanagement.businessdelegate.api.FacilityBusinessDelegate;
import com.kony.adminconsole.service.productmanagement.businessdelegate.impl.FacilityBusinessDelegateImpl;

public class FacilityBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

    @Override
    public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
        Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
        map.put(FacilityBusinessDelegate.class, FacilityBusinessDelegateImpl.class);
        return map;
    }
}
