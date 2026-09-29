package com.hbl.productservicesExtn.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.hbl.productservicesExtn.api.ContractBusinessDelegateExtn;
import com.hbl.productservicesExtn.impl.BillPayTransactionBusinessDelegateImplExtn;
import com.hbl.productservicesExtn.impl.ContractBusinessDelegateImplExtn;
import com.hbl.productservicesExtn.impl.CustomerBusinessDelegateImplExtn;
import com.hbl.productservicesExtn.impl.FeatureActionBusinessDelegateImplExtn;
import com.hbl.productservicesExtn.impl.InterBankFundTransferBusinessDelegateImplExtn;
import com.hbl.productservicesExtn.impl.LimitGroupBusinessDelegateImplExtn;
import com.hbl.productservicesExtn.impl.TransactionLimitsBusinessDelegateImplExtn;
import com.temenos.dbx.product.commons.businessdelegate.api.ContractBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.CustomerBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.FeatureActionBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.LimitGroupBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.TransactionLimitsBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.impl.LimitGroupBusinessDelegateImpl;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.BillPayTransactionBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.InterBankFundTransferBusinessDelegate;

public class HBLBusinessDeligateMapper implements DBPAPIMapper<BusinessDelegate>{

	@Override
	public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
		// TODO Auto-generated method stub
		Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
		map.put(TransactionLimitsBusinessDelegate.class, TransactionLimitsBusinessDelegateImplExtn.class);
		map.put(ContractBusinessDelegate.class, ContractBusinessDelegateImplExtn.class);
		map.put(FeatureActionBusinessDelegate.class, FeatureActionBusinessDelegateImplExtn.class);
		map.put(LimitGroupBusinessDelegate.class, LimitGroupBusinessDelegateImplExtn.class);
		map.put(CustomerBusinessDelegate.class, CustomerBusinessDelegateImplExtn.class);
		map.put(BillPayTransactionBusinessDelegate.class, BillPayTransactionBusinessDelegateImplExtn.class);
		map.put(InterBankFundTransferBusinessDelegate.class, InterBankFundTransferBusinessDelegateImplExtn.class);
		
		return map;
	}
}
