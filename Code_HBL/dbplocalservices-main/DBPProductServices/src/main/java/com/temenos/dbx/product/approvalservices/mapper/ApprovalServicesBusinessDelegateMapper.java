package com.temenos.dbx.product.approvalservices.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.dbx.product.achservices.businessdelegate.api.ACHFileBusinessDelegate;
import com.temenos.dbx.product.achservices.businessdelegate.api.ACHTransactionBusinessDelegate;
import com.temenos.dbx.product.achservices.businessdelegate.impl.ACHFileBusinessDelegateImpl;
import com.temenos.dbx.product.achservices.businessdelegate.impl.ACHTransactionBusinessDelegateImpl;
import com.temenos.dbx.product.approvalservices.businessdelegate.api.ApprovalRequestBusinessDelegate;
import com.temenos.dbx.product.approvalservices.businessdelegate.api.ApprovalRuleBusinessDelegate;
import com.temenos.dbx.product.approvalservices.businessdelegate.api.ApproversBusinessDelegate;
import com.temenos.dbx.product.approvalservices.businessdelegate.impl.ApprovalRequestBusinessDelegateImpl;
import com.temenos.dbx.product.approvalservices.businessdelegate.impl.ApprovalRuleBusinessDelegateImpl;
import com.temenos.dbx.product.approvalservices.businessdelegate.impl.ApproversBusinessDelegateImpl;
import com.temenos.dbx.product.commons.businessdelegate.api.AccountBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.CustomerBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.FeatureActionBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.FeatureBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.LimitGroupBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.OrganisationBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.impl.AccountBusinessDelegateImpl;
import com.temenos.dbx.product.commons.businessdelegate.impl.ApplicationBusinessDelegateImpl;
import com.temenos.dbx.product.commons.businessdelegate.impl.CustomerBusinessDelegateImpl;
import com.temenos.dbx.product.commons.businessdelegate.impl.FeatureActionBusinessDelegateImpl;
import com.temenos.dbx.product.commons.businessdelegate.impl.FeatureBusinessDelegateImpl;
import com.temenos.dbx.product.commons.businessdelegate.impl.LimitGroupBusinessDelegateImpl;
import com.temenos.dbx.product.commons.businessdelegate.impl.OrganisationBusinessDelegateImpl;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.GeneralTransactionsBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.impl.GeneralTransactionsBusinessDelegateImpl;

/**
 * 
 * @author KH2174
 * version 1.0
 * implements {@link DBPAPIMapper}
 */

public class ApprovalServicesBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate>{

	@Override
	public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
		Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
		
		//Add Mapping of Business Delegates interface and their implementation
        map.put(ApprovalRuleBusinessDelegate.class, ApprovalRuleBusinessDelegateImpl.class);
        map.put(CustomerBusinessDelegate.class,CustomerBusinessDelegateImpl.class);
        map.put(ApprovalRequestBusinessDelegate.class, ApprovalRequestBusinessDelegateImpl.class);
        map.put(ApproversBusinessDelegate.class, ApproversBusinessDelegateImpl.class);
		map.put(OrganisationBusinessDelegate.class, OrganisationBusinessDelegateImpl.class);
		map.put(ACHFileBusinessDelegate.class, ACHFileBusinessDelegateImpl.class);
		map.put(ACHTransactionBusinessDelegate.class, ACHTransactionBusinessDelegateImpl.class);
		map.put(GeneralTransactionsBusinessDelegate.class, GeneralTransactionsBusinessDelegateImpl.class);
		map.put(AccountBusinessDelegate.class, AccountBusinessDelegateImpl.class);
		map.put(ApplicationBusinessDelegate.class, ApplicationBusinessDelegateImpl.class);
		map.put(FeatureActionBusinessDelegate.class, FeatureActionBusinessDelegateImpl.class);
		map.put(LimitGroupBusinessDelegate.class, LimitGroupBusinessDelegateImpl.class);
		map.put(FeatureBusinessDelegate.class, FeatureBusinessDelegateImpl.class);
        
        return map;
	}

}
