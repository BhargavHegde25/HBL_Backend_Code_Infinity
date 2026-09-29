package com.temenos.auth.security.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.auth.security.backenddelegate.api.CaptchaBackendDelegate;
import com.temenos.auth.security.backenddelegate.impl.CaptchaBackendDelegateImpl;

public class SecurityBackendDelegateMapper implements DBPAPIMapper<BackendDelegate> {
    @Override
    public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {

        Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();
        map.put(CaptchaBackendDelegate.class, CaptchaBackendDelegateImpl.class);
        return map;
    }

}
