package com.kony.adminconsole.service.productmanagement.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.kony.adminconsole.service.productmanagement.businessdelegate.api.ProductBusinessDelegate;
import com.kony.adminconsole.service.productmanagement.businessdelegate.impl.ProductBusinessDelegateImpl;

public class ProductBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

    @Override
    public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
        Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
        map.put(ProductBusinessDelegate.class, ProductBusinessDelegateImpl.class);
        return map;
    }
}