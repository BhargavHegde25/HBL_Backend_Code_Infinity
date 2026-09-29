package com.temenos.dbx.product.accounts.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.dbx.product.accounts.resource.api.AccountsResource;
import com.temenos.dbx.product.accounts.resource.api.CustomViewResource;
import com.temenos.dbx.product.accounts.resource.impl.AccountsResourceImpl;
import com.temenos.dbx.product.accounts.resource.impl.CustomViewResourceImpl;

/**
 * 
 * @author sowmya.vandanapu
 * @version 1.0 {@link DBPAPIMapper}
 */
public class AccountsResourceMapper implements DBPAPIMapper<Resource> {

	@Override
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {

		Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();

		map.put(AccountsResource.class, AccountsResourceImpl.class);
		map.put(CustomViewResource.class, CustomViewResourceImpl.class);

		return map;
	}
}
