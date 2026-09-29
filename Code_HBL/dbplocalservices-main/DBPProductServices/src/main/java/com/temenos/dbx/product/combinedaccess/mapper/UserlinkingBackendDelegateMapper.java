package com.temenos.dbx.product.combinedaccess.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.dbx.product.combinedaccess.backenddelegate.api.UserLinkingBackendDelegate;
import com.temenos.dbx.product.combinedaccess.backenddelegate.impl.UserLinkingBackendDelegateImpl;

public class UserlinkingBackendDelegateMapper implements DBPAPIMapper<BackendDelegate> {

	@Override
	public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {

		Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();

		map.put(UserLinkingBackendDelegate.class, UserLinkingBackendDelegateImpl.class);

		return map;
	}

}
