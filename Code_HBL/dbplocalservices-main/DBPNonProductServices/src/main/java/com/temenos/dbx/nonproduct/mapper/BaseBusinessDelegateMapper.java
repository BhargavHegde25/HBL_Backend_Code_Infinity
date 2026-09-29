package com.temenos.dbx.nonproduct.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.dbx.bank.businessdelegate.api.BankBusinessDelegate;
import com.temenos.dbx.bank.businessdelegate.impl.BankBusinessDelegateImpl;
import com.temenos.dbx.transaction.businessdelegate.api.BulkTransferBusinessDelegate;
import com.temenos.dbx.transaction.businessdelegate.api.DisputeTransactionBusinessDelegate;
import com.temenos.dbx.transaction.businessdelegate.api.TransactionReportBusinessDelegate;
import com.temenos.dbx.transaction.businessdelegate.impl.BulkTransferBusinessDelegateImpl;
import com.temenos.dbx.transaction.businessdelegate.impl.DisputeTransactionBusinessDelegateImpl;
import com.temenos.dbx.transaction.businessdelegate.impl.TransactionReportBusinessDelegateImpl;

public class BaseBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {


        

	@Override
	public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
		Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
		map.put(BankBusinessDelegate.class, BankBusinessDelegateImpl.class);
		map.put(TransactionReportBusinessDelegate.class, TransactionReportBusinessDelegateImpl.class);
		map.put(BulkTransferBusinessDelegate.class, BulkTransferBusinessDelegateImpl.class);
		map.put(DisputeTransactionBusinessDelegate.class, DisputeTransactionBusinessDelegateImpl.class);


		return map;
	}

}
