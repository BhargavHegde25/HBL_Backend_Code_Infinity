package com.temenos.dbx.transaction.mapper;

import java.util.HashMap;
import java.util.Map;
import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.dbx.transaction.businessdelegate.api.DirectDebitsBusinessDelegate;
import com.temenos.dbx.transaction.businessdelegate.impl.DirectDebitsBusinessDelegateImpl;

/**
 * @author sribarani.vasthan
 */
public class DirectDebitsBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {
	@Override
	public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
		Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();

		// Add Mapping of Business Delegates interface and their implementation
		map.put(DirectDebitsBusinessDelegate.class, DirectDebitsBusinessDelegateImpl.class);

		return map;
	}

}
