package com.temenos.dbx.product.combinedaccess.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.dbx.product.combinedaccess.resource.api.UserLinkingResource;
import com.temenos.dbx.product.combinedaccess.resource.impl.UserlinkingResourceImpl;

/**
 * 
 * @author muthukumarv
 * @version 1.0 {@link DBPAPIMapper}
 */
public class UserlinkingResourceMapper implements DBPAPIMapper<Resource> {

	@Override
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {

		Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();

		map.put(UserLinkingResource.class, UserlinkingResourceImpl.class);

		return map;
	}
}
