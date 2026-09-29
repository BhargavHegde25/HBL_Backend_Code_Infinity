package com.temenos.dbx.eum.product.contract.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.dbx.eum.product.contract.resource.api.ContractResource;
import com.temenos.dbx.eum.product.contract.resource.api.CoreCustomerResource;
import com.temenos.dbx.eum.product.contract.resource.impl.ContractResourceImpl;
import com.temenos.dbx.eum.product.contract.resource.impl.CoreCustomerResourceImpl;

public class ContractResourceMapper implements DBPAPIMapper<Resource> {

    public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {

        Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();

        map.put(ContractResource.class, ContractResourceImpl.class);

        map.put(CoreCustomerResource.class, CoreCustomerResourceImpl.class);

        return map;
    }
}
