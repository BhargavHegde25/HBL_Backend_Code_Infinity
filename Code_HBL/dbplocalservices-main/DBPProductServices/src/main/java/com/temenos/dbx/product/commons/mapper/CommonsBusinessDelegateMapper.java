package com.temenos.dbx.product.commons.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.dbx.product.commons.businessdelegate.api.AccountBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.AuthorizationChecksBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.ContractBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.CustomerBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.FeatureActionBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.FeatureBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.LimitGroupBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.OrganisationBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.TransactionLimitsBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.UserRoleBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.impl.AccountBusinessDelegateImpl;
import com.temenos.dbx.product.commons.businessdelegate.impl.ApplicationBusinessDelegateImpl;
import com.temenos.dbx.product.commons.businessdelegate.impl.AuthorizationChecksBusinessDelegateImpl;
import com.temenos.dbx.product.commons.businessdelegate.impl.ContractBusinessDelegateImpl;
import com.temenos.dbx.product.commons.businessdelegate.impl.CustomerBusinessDelegateImpl;
import com.temenos.dbx.product.commons.businessdelegate.impl.FeatureActionBusinessDelegateImpl;
import com.temenos.dbx.product.commons.businessdelegate.impl.FeatureBusinessDelegateImpl;
import com.temenos.dbx.product.commons.businessdelegate.impl.LimitGroupBusinessDelegateImpl;
import com.temenos.dbx.product.commons.businessdelegate.impl.OrganisationBusinessDelegateImpl;
import com.temenos.dbx.product.commons.businessdelegate.impl.TransactionLimitsBusinessDelegateImpl;
import com.temenos.dbx.product.commons.businessdelegate.impl.UserRoleBusinessDelegateImpl;


/**
 * 
 * @author KH1755
 * @version 1.0
 * implements {@link DBPAPIMapper}
 */
public class CommonsBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate>{

	@Override
	public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
		Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
		
		//Add Mapping of Business Delegates interface and their implementation
        map.put(TransactionLimitsBusinessDelegate.class, TransactionLimitsBusinessDelegateImpl.class);
        map.put(AuthorizationChecksBusinessDelegate.class, AuthorizationChecksBusinessDelegateImpl.class);
        map.put(CustomerBusinessDelegate.class, CustomerBusinessDelegateImpl.class);
        map.put(OrganisationBusinessDelegate.class, OrganisationBusinessDelegateImpl.class);
        map.put(UserRoleBusinessDelegate.class, UserRoleBusinessDelegateImpl.class);
        map.put(AccountBusinessDelegate.class, AccountBusinessDelegateImpl.class);
        map.put(ApplicationBusinessDelegate.class, ApplicationBusinessDelegateImpl.class);
        map.put(FeatureActionBusinessDelegate.class, FeatureActionBusinessDelegateImpl.class);
        map.put(LimitGroupBusinessDelegate.class, LimitGroupBusinessDelegateImpl.class);
        map.put(FeatureBusinessDelegate.class, FeatureBusinessDelegateImpl.class);
        map.put(ContractBusinessDelegate.class, ContractBusinessDelegateImpl.class);
        
        return map;
	}

}
