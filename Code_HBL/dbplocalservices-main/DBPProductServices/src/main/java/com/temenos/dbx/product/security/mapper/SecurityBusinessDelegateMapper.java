package com.temenos.dbx.product.security.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.dbx.product.security.businessdelegate.api.CaptchaBusinessDelegate;
import com.temenos.dbx.product.security.businessdelegate.impl.CaptchaBusinessDelegateImpl;

public class SecurityBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {
    @Override
    public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
        Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();

        map.put(CaptchaBusinessDelegate.class, CaptchaBusinessDelegateImpl.class);
        return map;
    }
}
