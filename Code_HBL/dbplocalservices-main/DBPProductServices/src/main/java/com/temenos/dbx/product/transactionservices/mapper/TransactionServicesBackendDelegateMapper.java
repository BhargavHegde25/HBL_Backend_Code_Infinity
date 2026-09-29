package com.temenos.dbx.product.transactionservices.mapper;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.dbx.product.transactionservices.backenddelegate.api.*;
import com.temenos.dbx.product.transactionservices.backenddelegate.impl.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author KH2624
 * @version 1.0
 * implements {@link DBPAPIMapper}
 */
public class TransactionServicesBackendDelegateMapper implements DBPAPIMapper<BackendDelegate>{

	@Override
	public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {
		Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();

		//Add Mapping of Backend Delegates interface and their implementation
		
		map.put(BillPayTransactionBackendDelegate.class,BillPayTransactionBackendDelegateImpl.class);
		map.put(InterBankFundTransferBackendDelegate.class, InterBankFundTransferBackendDelegateImpl.class );
		map.put(OwnAccountFundTransferBackendDelegate.class, OwnAccountFundTransferBackendDelegateImpl.class);
		map.put(P2PTransactionBackendDelegate.class, P2PTransactionBackendDelegateImpl.class);
		map.put(DomesticWireTransactionBackendDelegate.class, DomesticWireTransactionBackendDelegateImpl.class);
		map.put(InternationalWireTransactionBackendDelegate.class, InternationalWireTransactionBackendDelegateImpl.class);
		map.put(InternationalFundTransferBackendDelegate.class, InternationalFundTransferBackendDelegateImpl.class);
		map.put(IntraBankFundTransferBackendDelegate.class, IntraBankFundTransferBackendDelegateImpl.class);
		return map;
	}
}