package com.temenos.msArrangement.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.msArrangement.businessdelegate.api.AccountTransactionsBusinessDelegate;
import com.temenos.msArrangement.businessdelegate.api.ArrangementsBusinessDelegate;
import com.temenos.msArrangement.businessdelegate.impl.AccountTransactionsBusinessDelegateImpl;
import com.temenos.msArrangement.businessdelegate.impl.ArrangementsBusinessDelegateImpl;

/**
 * 
 * @author KH2281
 * version 1.0
 * implements {@link DBPAPIMapper}
 */
public class HoldingsAndAMSBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

    @Override
    public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
        Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
        
        //Add Mapping of Business Delegates interface and their implementation
        map.put(AccountTransactionsBusinessDelegate.class, AccountTransactionsBusinessDelegateImpl.class);
        map.put(ArrangementsBusinessDelegate.class, ArrangementsBusinessDelegateImpl.class);
        return map;
    } 

}
