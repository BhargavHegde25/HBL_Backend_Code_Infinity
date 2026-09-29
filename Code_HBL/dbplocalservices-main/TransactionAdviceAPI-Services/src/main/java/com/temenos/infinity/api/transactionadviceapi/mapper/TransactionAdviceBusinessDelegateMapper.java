package com.temenos.infinity.api.transactionadviceapi.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.infinity.api.transactionadviceapi.businessdelegate.api.TransactionAdviceBusinessDelegate;
import com.temenos.infinity.api.transactionadviceapi.businessdelegate.impl.TransactionAdviceBusinessDelegateImpl;

/**
 * 
 * @author KH2281 version 1.0 implements {@link DBPAPIMapper}
 */
public class TransactionAdviceBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

    @Override
    public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
        Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();

        // Add Mapping of Business Delegates interface and their implementation
        map.put(TransactionAdviceBusinessDelegate.class, TransactionAdviceBusinessDelegateImpl.class);

        return map;
    }

}
