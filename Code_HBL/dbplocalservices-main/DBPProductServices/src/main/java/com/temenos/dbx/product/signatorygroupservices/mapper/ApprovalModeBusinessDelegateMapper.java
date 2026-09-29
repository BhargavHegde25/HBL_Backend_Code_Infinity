package com.temenos.dbx.product.signatorygroupservices.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.dbx.product.signatorygroupservices.businessdelegate.api.ApprovalModeBusinessDelegate;
import com.temenos.dbx.product.signatorygroupservices.businessdelegate.api.SignatoryGroupBusinessDelegate;
import com.temenos.dbx.product.signatorygroupservices.businessdelegate.impl.ApprovalModeBusinessDelegateImpl;
import com.temenos.dbx.product.signatorygroupservices.businessdelegate.impl.SignatoryGroupBusinessDelegateImpl;


/**
 * 
 * @author KH1755
 * @version 1.0
 * implements {@link DBPAPIMapper}
 */
public class ApprovalModeBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate>{

	@Override
	public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
		Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
		
		//Add Mapping of Business Delegates interface and their implementation
       
		map.put(ApprovalModeBusinessDelegate.class, ApprovalModeBusinessDelegateImpl.class);
		
        return map;
	}

}
