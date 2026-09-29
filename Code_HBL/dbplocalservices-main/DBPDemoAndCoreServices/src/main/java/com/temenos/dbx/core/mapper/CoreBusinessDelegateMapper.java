package com.temenos.dbx.core.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.dbx.core.businessdelegate.api.CoreCustomerBusinessDelegate;
import com.temenos.dbx.core.businessdelegate.impl.CoreCustomerBusinessDelegateImpl;

public class CoreBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

    @Override
    public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
        Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();

        map.put(CoreCustomerBusinessDelegate.class, CoreCustomerBusinessDelegateImpl.class);
        
        return map;
    }

}
