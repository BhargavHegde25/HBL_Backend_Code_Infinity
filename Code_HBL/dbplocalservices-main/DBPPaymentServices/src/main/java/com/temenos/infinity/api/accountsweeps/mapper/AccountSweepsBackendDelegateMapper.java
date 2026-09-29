package com.temenos.infinity.api.accountsweeps.mapper;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.infinity.api.accountsweeps.backenddelegate.api.AccountSweepsBackendDelegate;
import com.temenos.infinity.api.accountsweeps.backenddelegate.impl.AccountSweepsBackendDelegateImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * @author naveen.yerra
 */
public class AccountSweepsBackendDelegateMapper implements DBPAPIMapper<BackendDelegate> {
    @Override
    public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {
        Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();
        map.put(AccountSweepsBackendDelegate.class, AccountSweepsBackendDelegateImpl.class);
        return map;
    }
}
