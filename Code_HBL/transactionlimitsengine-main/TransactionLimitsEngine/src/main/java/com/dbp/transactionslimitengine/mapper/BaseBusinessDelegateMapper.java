package com.dbp.transactionslimitengine.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.dbp.transactionslimitengine.businessdelegate.api.TransactionsLimitBusinessDelegate;
import com.dbp.transactionslimitengine.businessdelegate.impl.TransactionsLimitBusinessDelegateImpl;

public class BaseBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

	@Override
    public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
        Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
        map.put(TransactionsLimitBusinessDelegate.class, TransactionsLimitBusinessDelegateImpl.class);
        return map;
    }

}
