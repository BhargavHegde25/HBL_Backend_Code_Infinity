package com.temenos.dbx.product.organization.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.dbx.product.organization.backenddelegate.api.AuthorizedSignatoriesBackendDelegate;
import com.temenos.dbx.product.organization.backenddelegate.api.MembershipBackendDelegate;
import com.temenos.dbx.product.organization.backenddelegate.api.MembershipOwnerBackendDelegate;
import com.temenos.dbx.product.organization.backenddelegate.impl.AuthorizedSignatoriesBackendDelegateImpl;
import com.temenos.dbx.product.organization.backenddelegate.impl.MembershipBackendDelegateImpl;
import com.temenos.dbx.product.organization.backenddelegate.impl.MembershipOwnerBackendDelegateImpl;

public class OrganizationBackendDelegateMapper implements DBPAPIMapper<BackendDelegate> {

    @Override
    public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {

        Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();
        map.put(AuthorizedSignatoriesBackendDelegate.class, AuthorizedSignatoriesBackendDelegateImpl.class);
        map.put(MembershipBackendDelegate.class, MembershipBackendDelegateImpl.class);
        map.put(MembershipOwnerBackendDelegate.class, MembershipOwnerBackendDelegateImpl.class);
        return map;
    }

}
