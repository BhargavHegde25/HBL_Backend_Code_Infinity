package com.temenos.dbx.product.accounts.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.dbx.product.accounts.businessdelegate.api.AccountsBusinessDelegate;
import com.temenos.dbx.product.accounts.businessdelegate.api.CustomViewBusinessDelegate;
import com.temenos.dbx.product.accounts.businessdelegate.impl.AccountsBusinessDelegateImpl;
import com.temenos.dbx.product.accounts.businessdelegate.impl.CustomViewBusinessDelegateImpl;

public class AccountsBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

	@Override
	public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {

		Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();

		map.put(AccountsBusinessDelegate.class, AccountsBusinessDelegateImpl.class);
		map.put(CustomViewBusinessDelegate.class, CustomViewBusinessDelegateImpl.class);

		return map;
	}

}
