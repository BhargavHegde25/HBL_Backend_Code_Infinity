package com.temenos.infinity.api.accountsweeps.mapper;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.infinity.api.accountsweeps.businessdelegate.api.AccountSweepsBusinessDelegate;
import com.temenos.infinity.api.accountsweeps.businessdelegate.impl.AccountSweepsBusinessDelegateImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * @author naveen.yerra
 */
public class AccountSweepsBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {
    @Override
    public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
        Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
        map.put(AccountSweepsBusinessDelegate.class, AccountSweepsBusinessDelegateImpl.class);
        return map;
    }
}
