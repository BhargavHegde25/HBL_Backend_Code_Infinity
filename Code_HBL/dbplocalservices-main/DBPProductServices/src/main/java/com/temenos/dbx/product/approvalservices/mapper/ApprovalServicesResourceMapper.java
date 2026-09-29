package com.temenos.dbx.product.approvalservices.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.dbx.product.approvalservices.resource.api.ApprovalQueueResource;
import com.temenos.dbx.product.approvalservices.resource.api.ApprovalRequestResource;
import com.temenos.dbx.product.approvalservices.resource.api.ApprovalRuleResource;
import com.temenos.dbx.product.approvalservices.resource.impl.ApprovalQueueResourceImpl;
import com.temenos.dbx.product.approvalservices.resource.impl.ApprovalRequestResourceImpl;
import com.temenos.dbx.product.approvalservices.resource.impl.ApprovalRuleResourceImpl;


/**
 * 
 * @author KH2174
 * version 1.0
 * implements {@link DBPAPIMapper}
 */

public class ApprovalServicesResourceMapper implements DBPAPIMapper<Resource> {
	

	@Override
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
		Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
		
		//Add Mapping of Business Delegates interface and their implementation
        map.put(ApprovalRuleResource.class, ApprovalRuleResourceImpl.class);
        map.put(ApprovalRequestResource.class, ApprovalRequestResourceImpl.class);
        map.put(ApprovalQueueResource.class, ApprovalQueueResourceImpl.class);
        
        return map;
	}

}
