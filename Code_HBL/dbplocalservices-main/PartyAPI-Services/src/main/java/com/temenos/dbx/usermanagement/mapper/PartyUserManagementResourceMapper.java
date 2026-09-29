package com.temenos.dbx.usermanagement.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.dbx.eum.product.usermanagement.resource.api.InfinityUserManagementResource;
import com.temenos.dbx.eum.product.usermanagement.resource.api.ProfileManagementResource;
import com.temenos.infinity.api.arrangements.resource.api.InfinityAccountsResource;
import com.temenos.dbx.usermanagement.resource.api.PartyRelationsUserManagementResource;
import com.temenos.dbx.usermanagement.resource.api.PartyUserManagementResource;
import com.temenos.dbx.usermanagement.resource.impl.PartyInfinityAccountsResourceImpl;
import com.temenos.dbx.usermanagement.resource.impl.PartyInfinityUserManagementResourceImpl;
import com.temenos.dbx.usermanagement.resource.impl.PartyProfileManagementResourceImpl;
import com.temenos.dbx.usermanagement.resource.impl.PartyRelationsUserManagementResourceImpl;
import com.temenos.dbx.usermanagement.resource.impl.PartyUserManagementResourceImpl;

public class PartyUserManagementResourceMapper implements DBPAPIMapper<Resource> {

    @Override
    public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
        Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();

        map.put(PartyUserManagementResource.class, PartyUserManagementResourceImpl.class);
        map.put(ProfileManagementResource.class, PartyProfileManagementResourceImpl.class);
        map.put(InfinityAccountsResource.class, PartyInfinityAccountsResourceImpl.class);
        map.put(PartyRelationsUserManagementResource.class, PartyRelationsUserManagementResourceImpl.class);
        map.put(InfinityUserManagementResource.class,PartyInfinityUserManagementResourceImpl.class);
        return map;
    }
}
