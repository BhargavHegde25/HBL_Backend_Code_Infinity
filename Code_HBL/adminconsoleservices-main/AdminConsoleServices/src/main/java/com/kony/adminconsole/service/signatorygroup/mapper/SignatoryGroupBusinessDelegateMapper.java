package com.kony.adminconsole.service.signatorygroup.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.kony.adminconsole.service.signatorygroup.businessdelegate.api.SignatoryGroupBusinessDelegate;
import com.kony.adminconsole.service.signatorygroup.businessdelegate.impl.SignatoryGroupBusinessDelegateImpl;

public class SignatoryGroupBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

    @Override
    public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
        Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
        map.put(SignatoryGroupBusinessDelegate.class, SignatoryGroupBusinessDelegateImpl.class);
        return map;
    }
}