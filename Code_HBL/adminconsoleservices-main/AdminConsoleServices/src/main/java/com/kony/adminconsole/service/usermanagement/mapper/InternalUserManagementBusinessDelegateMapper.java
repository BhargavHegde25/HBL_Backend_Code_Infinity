package com.kony.adminconsole.service.usermanagement.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.kony.adminconsole.service.usermanagement.businessdelegate.api.EmployeeEntitlementBusinessDelegate;
import com.kony.adminconsole.service.usermanagement.businessdelegate.api.EmployeePermissionBusinessDelegate;
import com.kony.adminconsole.service.usermanagement.businessdelegate.api.EmployeeRoleBusinessDelegate;
import com.kony.adminconsole.service.usermanagement.businessdelegate.api.InternalUserManagementBusinessDelegate;
import com.kony.adminconsole.service.usermanagement.businessdelegate.api.UserFeatureBusinessDelegate;
import com.kony.adminconsole.service.usermanagement.businessdelegate.impl.EmployeeEntitlementBusinessDelegateImpl;
import com.kony.adminconsole.service.usermanagement.businessdelegate.impl.EmployeePermissionBusinessDelegateImpl;
import com.kony.adminconsole.service.usermanagement.businessdelegate.impl.EmployeeRoleBusinessDelegateImpl;
import com.kony.adminconsole.service.usermanagement.businessdelegate.impl.InternalUserManagementBusinessDelegateImpl;
import com.kony.adminconsole.service.usermanagement.businessdelegate.impl.UserFeatureBusinessDelegateImpl;

public class InternalUserManagementBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

    @Override
    public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
        Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
        map.put(InternalUserManagementBusinessDelegate.class, InternalUserManagementBusinessDelegateImpl.class);
        map.put(UserFeatureBusinessDelegate.class, UserFeatureBusinessDelegateImpl.class);
        map.put(EmployeePermissionBusinessDelegate.class, EmployeePermissionBusinessDelegateImpl.class);
        map.put(EmployeeEntitlementBusinessDelegate.class, EmployeeEntitlementBusinessDelegateImpl.class);
        map.put(EmployeeRoleBusinessDelegate.class, EmployeeRoleBusinessDelegateImpl.class);
        return map;
    }
}
