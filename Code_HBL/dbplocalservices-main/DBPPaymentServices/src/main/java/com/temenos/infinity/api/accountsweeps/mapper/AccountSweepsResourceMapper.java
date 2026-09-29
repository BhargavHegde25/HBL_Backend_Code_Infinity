package com.temenos.infinity.api.accountsweeps.mapper;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.infinity.api.accountsweeps.resource.api.AccountSweepsResource;
import com.temenos.infinity.api.accountsweeps.resource.impl.AccountSweepsResourceImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * @author naveen.yerra
 */
public class AccountSweepsResourceMapper implements DBPAPIMapper<Resource> {
    @Override
    public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
        Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
        map.put(AccountSweepsResource.class, AccountSweepsResourceImpl.class);
        return map;
    }
}
