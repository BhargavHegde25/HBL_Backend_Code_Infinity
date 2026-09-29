package com.hbl.transactionlimitsengine.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.dbp.transactionslimitengine.businessdelegate.api.TransactionsLimitBusinessDelegate;
import com.hbl.transactionlimitsengine.impl.TransactionsLimitBusinessDelegateImplExtn;

public class HBLBusinessDeligateMapper implements DBPAPIMapper<BusinessDelegate>{

	@Override
	public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
		// TODO Auto-generated method stub
		Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
		map.put(TransactionsLimitBusinessDelegate.class, TransactionsLimitBusinessDelegateImplExtn.class);
		
		return map;
	}
}
