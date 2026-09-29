package com.temenos.dbx.product.approvalsframework.approvalrequest.mapper;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.dbx.product.approvalsframework.approvalrequest.businessdelegate.api.ApprovalRequestBusinessDelegate;
import com.temenos.dbx.product.approvalsframework.approvalrequest.businessdelegate.impl.ApprovalRequestBusinessDelegateImpl;

import java.util.HashMap;
import java.util.Map;

public class ApprovalRequestBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {
    @Override
    public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
        Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
        /* all resource interface to implementation mappings are done here */
        map.put(ApprovalRequestBusinessDelegate.class, ApprovalRequestBusinessDelegateImpl.class);
        return map;
    }
}
