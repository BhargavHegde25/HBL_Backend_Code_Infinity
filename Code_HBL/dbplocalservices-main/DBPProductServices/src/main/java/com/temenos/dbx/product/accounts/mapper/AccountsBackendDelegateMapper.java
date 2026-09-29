package com.temenos.dbx.product.accounts.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.dbx.product.accounts.backenddelegate.api.AccountHolderBackendDelegate;
import com.temenos.dbx.product.accounts.backenddelegate.api.AccountInformationBackendDelegate;
import com.temenos.dbx.product.accounts.backenddelegate.api.AccountsBackendDelegate;
import com.temenos.dbx.product.accounts.backenddelegate.impl.AccountHolderBackendDelegateImpl;
import com.temenos.dbx.product.accounts.backenddelegate.impl.AccountInformationBackendDelegateImpl;
import com.temenos.dbx.product.accounts.backenddelegate.impl.AccountsBackendDelegateImpl;

public class AccountsBackendDelegateMapper implements DBPAPIMapper<BackendDelegate> {

    @Override
    public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {

        Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();

        map.put(AccountsBackendDelegate.class, AccountsBackendDelegateImpl.class);

        map.put(AccountHolderBackendDelegate.class, AccountHolderBackendDelegateImpl.class);

        map.put(AccountInformationBackendDelegate.class, AccountInformationBackendDelegateImpl.class);

        return map;
    }

}
