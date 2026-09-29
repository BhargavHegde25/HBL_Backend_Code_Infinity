package com.temenos.dbx.product.approvalmatrixservices.mapper;

import java.util.HashMap;
import java.util.Map;

import com.temenos.dbx.product.approvalmatrixservices.resource.api.ApprovalMatrixResource;
import com.temenos.dbx.product.approvalmatrixservices.resource.api.ApprovalRuleResource;
import com.temenos.dbx.product.approvalmatrixservices.resource.impl.ApprovalMatrixResourceImpl;
import com.temenos.dbx.product.approvalmatrixservices.resource.impl.ApprovalRuleResourceImpl;
import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;

/**
 * 
 * @author KH2387
 * version 1.0
 * implements {@link DBPAPIMapper}
 */
public class ApprovalMatrixResourcesMapper implements DBPAPIMapper<Resource> {

    @Override
    public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
        Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
        
        //Add Mapping of Business Delegates interface and their implementation
        map.put(ApprovalRuleResource.class, ApprovalRuleResourceImpl.class);
        map.put(ApprovalMatrixResource.class,ApprovalMatrixResourceImpl.class);
        return map;
    }
}
