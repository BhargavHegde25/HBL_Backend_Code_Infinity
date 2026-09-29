package com.hbl.productservicesExtn.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.hbl.productservicesExtn.api.ScheduledTransactionsBackendDelegate;
import com.hbl.productservicesExtn.impl.BillPayTransactionBackendDelegateImplExtn;
import com.hbl.productservicesExtn.impl.InterBankFundTransferBackendDelegateImplExtn;
import com.hbl.productservicesExtn.impl.IntraBankFundTransferBackendDelegateImplExtn;
import com.hbl.productservicesExtn.impl.ScheduledTransactionsBackendDelegateImpl;
import com.hbl.productservicesExtn.impl.TransactionLimitsBusinessDelegateImplExtn;
import com.temenos.dbx.product.commons.businessdelegate.api.TransactionLimitsBusinessDelegate;
import com.temenos.dbx.product.transactionservices.backenddelegate.api.BillPayTransactionBackendDelegate;
import com.temenos.dbx.product.transactionservices.backenddelegate.api.InterBankFundTransferBackendDelegate;
import com.temenos.dbx.product.transactionservices.backenddelegate.api.IntraBankFundTransferBackendDelegate;

public class HBLBackendDelegateMapper implements DBPAPIMapper<BackendDelegate> {

	@Override
	public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {
		Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();
		map.put(InterBankFundTransferBackendDelegate.class, InterBankFundTransferBackendDelegateImplExtn.class);
		map.put(BillPayTransactionBackendDelegate.class, BillPayTransactionBackendDelegateImplExtn.class);
		map.put(IntraBankFundTransferBackendDelegate.class, IntraBankFundTransferBackendDelegateImplExtn.class);
		map.put(ScheduledTransactionsBackendDelegate.class, ScheduledTransactionsBackendDelegateImpl.class);
		
		return map;
	}

}
