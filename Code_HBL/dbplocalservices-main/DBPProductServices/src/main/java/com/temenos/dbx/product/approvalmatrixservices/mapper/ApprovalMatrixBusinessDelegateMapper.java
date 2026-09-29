package com.temenos.dbx.product.approvalmatrixservices.mapper;

import java.util.HashMap;
import java.util.Map;

import com.temenos.dbx.product.approvalmatrixservices.businessdelegate.api.ApprovalMatrixBusinessDelegate;
import com.temenos.dbx.product.approvalmatrixservices.businessdelegate.api.ApprovalRuleBusinessDelegate;
import com.temenos.dbx.product.approvalmatrixservices.businessdelegate.impl.ApprovalMatrixBusinessDelegateImpl;
import com.temenos.dbx.product.approvalmatrixservices.businessdelegate.impl.ApprovalRuleBusinessDelegateImpl;
import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;

/**
 * 
 * @author KH2387
 * version 1.0
 * implements {@link DBPAPIMapper}
 */
public class ApprovalMatrixBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

    @Override
    public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
        Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
        
        //Add Mapping of Business Delegates interface and their implementation
        map.put(ApprovalRuleBusinessDelegate.class, ApprovalRuleBusinessDelegateImpl.class);
        map.put(ApprovalMatrixBusinessDelegate.class, ApprovalMatrixBusinessDelegateImpl.class);
        
        return map;
    }

}
