package com.temenos.dbx.party.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.dbx.party.resource.api.AuditActivityUpdateResource;
import com.temenos.dbx.party.resource.api.CustomerResource;
import com.temenos.dbx.party.resource.api.DueDiligenceResource;
import com.temenos.dbx.party.resource.api.UpdateCustomerApplicationResource;
import com.temenos.dbx.party.resource.impl.AuditActivityUpdateResourceImpl;
import com.temenos.dbx.party.resource.impl.CustomerResourceImpl;
import com.temenos.dbx.party.resource.impl.DueDiligenceResourceImpl;
import com.temenos.dbx.party.resource.impl.UpdateCustomerApplicationResourceImpl;

public class PartyResourceMapper implements DBPAPIMapper<Resource> {

    @Override
    public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
        Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
        map.put(CustomerResource.class, CustomerResourceImpl.class);
        map.put(UpdateCustomerApplicationResource.class, UpdateCustomerApplicationResourceImpl.class);
        map.put(AuditActivityUpdateResource.class, AuditActivityUpdateResourceImpl.class);
        map.put(DueDiligenceResource.class, DueDiligenceResourceImpl.class);
        
        return map;
    }
}
