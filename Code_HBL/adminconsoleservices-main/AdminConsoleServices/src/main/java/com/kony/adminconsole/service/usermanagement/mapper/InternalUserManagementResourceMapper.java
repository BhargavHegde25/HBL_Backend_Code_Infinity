package com.kony.adminconsole.service.usermanagement.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.kony.adminconsole.service.usermanagement.resource.api.EmployeeEntitlementResource;
import com.kony.adminconsole.service.usermanagement.resource.api.EmployeePermissionResource;
import com.kony.adminconsole.service.usermanagement.resource.api.EmployeeRoleResource;
import com.kony.adminconsole.service.usermanagement.resource.api.InternalUserManagementResource;
import com.kony.adminconsole.service.usermanagement.resource.api.UserFeatureResource;
import com.kony.adminconsole.service.usermanagement.resource.impl.EmployeeEntitlementResourceImpl;
import com.kony.adminconsole.service.usermanagement.resource.impl.EmployeePermissionResourceImpl;
import com.kony.adminconsole.service.usermanagement.resource.impl.EmployeeRoleResourceImpl;
import com.kony.adminconsole.service.usermanagement.resource.impl.InternalUserManagementResourceImpl;
import com.kony.adminconsole.service.usermanagement.resource.impl.UserFeatureResourceImpl;

/**
 * 
 * @author This class maps resource interface to its implementation and it can be changed whenever required
 *
 */

public class InternalUserManagementResourceMapper implements DBPAPIMapper<Resource> {

    @Override
    public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
        Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
        /* all resource interface to implementation mappings are done here */
        map.put(InternalUserManagementResource.class, InternalUserManagementResourceImpl.class);
        map.put(UserFeatureResource.class, UserFeatureResourceImpl.class);
        map.put(EmployeePermissionResource.class, EmployeePermissionResourceImpl.class);
        map.put(EmployeeEntitlementResource.class, EmployeeEntitlementResourceImpl.class);
        map.put(EmployeeRoleResource.class, EmployeeRoleResourceImpl.class);
        return map;
    }
}
