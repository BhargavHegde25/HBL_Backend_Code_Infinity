package com.kony.adminconsole.service.termandcondition.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.kony.adminconsole.service.termandcondition.businessdelegate.api.TnCBusinessDelegate;
import com.kony.adminconsole.service.termandcondition.businessdelegate.impl.TnCBusinessDelegateImpl;

public class TnCBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

    @Override
    public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
        Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
        map.put(TnCBusinessDelegate.class, TnCBusinessDelegateImpl.class);
        return map;
    }
}