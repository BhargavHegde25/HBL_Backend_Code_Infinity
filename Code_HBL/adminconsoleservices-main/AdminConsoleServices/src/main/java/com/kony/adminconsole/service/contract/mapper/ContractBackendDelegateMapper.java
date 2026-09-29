package com.kony.adminconsole.service.contract.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.kony.adminconsole.service.contract.backenddelegate.api.ContractBackendDelegate;
import com.kony.adminconsole.service.contract.backenddelegate.impl.ContractBackendDelegateImpl;

public class ContractBackendDelegateMapper implements DBPAPIMapper<BackendDelegate> {
    @Override
    public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {

        Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();

        /* mappings of backend delegate interface and implementation to be added */
        map.put(ContractBackendDelegate.class, ContractBackendDelegateImpl.class);

        return map;
    }

}
