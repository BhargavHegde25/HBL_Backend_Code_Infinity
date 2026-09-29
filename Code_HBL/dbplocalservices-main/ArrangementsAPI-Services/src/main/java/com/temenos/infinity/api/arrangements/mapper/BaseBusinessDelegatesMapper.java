package com.temenos.infinity.api.arrangements.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.infinity.api.arrangements.businessdelegate.api.DocumentStorageBusinessDelegate;
import com.temenos.infinity.api.arrangements.businessdelegate.impl.DocumentStorageBusinessDelegateImpl;


public class BaseBusinessDelegatesMapper implements DBPAPIMapper<BusinessDelegate> {

	@Override
	public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
		Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
		map.put(DocumentStorageBusinessDelegate.class, DocumentStorageBusinessDelegateImpl.class);
		return map;
	}

}
