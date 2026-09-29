package com.temenos.dbx.product.combinedaccess.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.dbx.product.combinedaccess.businessdelegate.api.UserLinkingBusinessDelegate;
import com.temenos.dbx.product.combinedaccess.businessdelegate.impl.UserLinkingBusinessDelegateImpl;
/**
 * 
 * @author muthukumarv
 * @version 1.0 {@link DBPAPIMapper}
 */
public class UserlinkingBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

	@Override
	public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {

		Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();

		map.put(UserLinkingBusinessDelegate.class, UserLinkingBusinessDelegateImpl.class);

		return map;
	}

}
