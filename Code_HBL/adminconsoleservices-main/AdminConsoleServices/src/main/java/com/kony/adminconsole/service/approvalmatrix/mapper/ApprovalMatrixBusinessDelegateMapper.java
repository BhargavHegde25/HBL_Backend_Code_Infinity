package com.kony.adminconsole.service.approvalmatrix.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.kony.adminconsole.service.approvalmatrix.businessdelegate.api.ApprovalMatrixBusinessDelegate;
import com.kony.adminconsole.service.approvalmatrix.businessdelegate.impl.ApprovalMatrixBusinessDelegateImpl;


public class ApprovalMatrixBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

    @Override
    public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
        Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
        map.put(ApprovalMatrixBusinessDelegate.class, ApprovalMatrixBusinessDelegateImpl.class);
        return map;
    }
}