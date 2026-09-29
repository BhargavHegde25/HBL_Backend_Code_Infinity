package com.temenos.dbx.product.approvalsframework.approvalmatrix.mapper;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.dbx.product.approvalsframework.approvalmatrix.businessdelegate.api.ApprovalMatrixBusinessDelegate;
import com.temenos.dbx.product.approvalsframework.approvalmatrix.businessdelegate.impl.ApprovalMatrixBusinessDelegateImpl;

import java.util.HashMap;
import java.util.Map;

public class ApprovalMatrixBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {
    @Override
    public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
        Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
        /* all resource interface to implementation mappings are done here */
        map.put(ApprovalMatrixBusinessDelegate.class, ApprovalMatrixBusinessDelegateImpl.class);
        return map;
    }
}
