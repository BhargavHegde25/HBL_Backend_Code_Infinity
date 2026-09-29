package com.temenos.dbx.transaction.mapper;
import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.dbx.transaction.businessdelegate.api.BulkTransferBusinessDelegate;
import com.temenos.dbx.transaction.businessdelegate.impl.BulkTransferBusinessDelegateImpl;


public class BulkTransferBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate>{

	@Override
	public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
		Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();

		//Add Mapping of Business Delegates interface and their implementation
	//	map.put(BulkTransferBusinessDelegate.class, BulkTransferBusinessDelegateImpl.class);

		return map;
	}
}
