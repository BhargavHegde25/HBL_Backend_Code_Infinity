package com.temenos.dbx.product.signatorygroupservices.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.dbx.product.signatorygroupservices.resource.api.ApprovalModeResource;
import com.temenos.dbx.product.signatorygroupservices.resource.api.SignatoryGroupResource;
import com.temenos.dbx.product.signatorygroupservices.resource.impl.ApprovalModeResourceImpl;
import com.temenos.dbx.product.signatorygroupservices.resource.impl.SignatoryGroupResourceImpl;

/**
 * 
 * @author KH1755
 * @version 1.0
 * implements {@link DBPAPIMapper}
 */
public class ApprovalModeResourceMapper implements DBPAPIMapper<Resource> {
	

	@Override
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
		Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
		
		//Add Mapping of Business Delegates interface and their implementation
		map.put(ApprovalModeResource.class, ApprovalModeResourceImpl.class);
		
        
        return map;
	}

}
