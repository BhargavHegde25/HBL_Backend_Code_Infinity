package com.hbl.resource.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.hbl.backenddeligate.api.CustomerAccountsBusinessDelegateExtn;
import com.hbl.businessdelegate.impl.CoreCustomerBusinessDelegateImplExtn;
import com.hbl.businessdelegate.impl.CustomerAccountsBusinessDelegateImplExtn;
import com.hbl.resource.impl.CustomerActionsBusinessDelegateImplExtn;
import com.hbl.resource.impl.InfinityUserManagementBusinessDelegateImplExtn;
import com.temenos.dbx.eum.product.contract.businessdelegate.api.CoreCustomerBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.CustomerAccountsBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.CustomerActionsBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.InfinityUserManagementBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.impl.CustomerActionsBusinessDelegateImpl;

public class HBLBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate>{

	@Override
	public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
		// TODO Auto-generated method stub
		Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
		map.put(InfinityUserManagementBusinessDelegate.class, InfinityUserManagementBusinessDelegateImplExtn.class);
		map.put(CustomerActionsBusinessDelegate.class, CustomerActionsBusinessDelegateImplExtn.class);
		map.put(CoreCustomerBusinessDelegate.class, CoreCustomerBusinessDelegateImplExtn.class);
		map.put(CustomerAccountsBusinessDelegate.class, CustomerAccountsBusinessDelegateImplExtn.class);
		map.put(CustomerAccountsBusinessDelegateExtn.class, CustomerAccountsBusinessDelegateImplExtn.class);
		
		return map;
	}
 

}
