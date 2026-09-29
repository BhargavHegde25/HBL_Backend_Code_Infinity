package com.temenos.infinity.api.accountaggregation.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.infinity.api.accountaggregation.businessdelegate.api.AccountAggregationBusinessDelegate;
import com.temenos.infinity.api.accountaggregation.businessdelegate.impl.AccountAggregationBusinessDelegateImpl;;

/**
 * Mapper between Business Delegate Interfaces & corresponding Business Delegate Implementation classes
 * 
 * @author Aditya Mankal
 *
 */
public class AccountAggregationBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

    @Override
    public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
        Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();

        // Register your Business Delegate Implementation classes here
        map.put(AccountAggregationBusinessDelegate.class, AccountAggregationBusinessDelegateImpl.class);
        return map;
    }

}
