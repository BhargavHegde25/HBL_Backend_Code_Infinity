package com.temenos.dbx.product.achservices.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.dbx.product.achservices.businessdelegate.api.ACHCommonsBusinessDelegate;
import com.temenos.dbx.product.achservices.businessdelegate.api.ACHFileBusinessDelegate;
import com.temenos.dbx.product.achservices.businessdelegate.api.ACHFileRecordBusinessDelegate;
import com.temenos.dbx.product.achservices.businessdelegate.api.ACHFileSubrecordBusinessDelegate;
import com.temenos.dbx.product.achservices.businessdelegate.api.ACHTemplateBusinessDelegate;
import com.temenos.dbx.product.achservices.businessdelegate.api.ACHTransactionBusinessDelegate;
import com.temenos.dbx.product.achservices.businessdelegate.impl.ACHCommonsBusinessDelegateImpl;
import com.temenos.dbx.product.achservices.businessdelegate.impl.ACHFileBusinessDelegateImpl;
import com.temenos.dbx.product.achservices.businessdelegate.impl.ACHFileRecordBusinessDelegateImpl;
import com.temenos.dbx.product.achservices.businessdelegate.impl.ACHFileSubrecordBusinessDelegateImpl;
import com.temenos.dbx.product.achservices.businessdelegate.impl.ACHTemplateBusinessDelegateImpl;
import com.temenos.dbx.product.achservices.businessdelegate.impl.ACHTransactionBusinessDelegateImpl;
import com.temenos.dbx.product.commons.businessdelegate.api.AccountBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.impl.AccountBusinessDelegateImpl;
import com.temenos.dbx.product.commons.businessdelegate.impl.ApplicationBusinessDelegateImpl;

public class ACHServicesBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate>{

	@Override
	public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {

		Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
		
		//Add Mapping of Business Delegates interface and their implementation
        map.put(ACHFileBusinessDelegate.class, ACHFileBusinessDelegateImpl.class);
        map.put(ACHTemplateBusinessDelegate.class, ACHTemplateBusinessDelegateImpl.class);
        map.put(ACHTransactionBusinessDelegate.class, ACHTransactionBusinessDelegateImpl.class);
        map.put(ACHCommonsBusinessDelegate.class, ACHCommonsBusinessDelegateImpl.class);
        map.put(ACHFileRecordBusinessDelegate.class, ACHFileRecordBusinessDelegateImpl.class);
        map.put(ACHFileSubrecordBusinessDelegate.class, ACHFileSubrecordBusinessDelegateImpl.class);
        map.put(AccountBusinessDelegate.class, AccountBusinessDelegateImpl.class);
        map.put(ApplicationBusinessDelegate.class, ApplicationBusinessDelegateImpl.class);
        return map;
	}

}