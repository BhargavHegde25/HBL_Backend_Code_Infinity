package com.temenos.dbx.mfa.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.dbx.mfa.businessdelegate.api.MFAServiceBusinessDelegate;

import com.temenos.mfa.businessdelegate.impl.MFAServiceBusinessDelegateImpl;

public class MFABusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

	@Override
	public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
		Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();

		map.put(MFAServiceBusinessDelegate.class, MFAServiceBusinessDelegateImpl.class);

		return map;
	}
}
