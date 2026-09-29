package com.temenos.dbx.product.payeeservices.mapper;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.dbx.product.payeeservices.backenddelegate.api.*;
import com.temenos.dbx.product.payeeservices.backenddelegate.impl.*;

import java.util.HashMap;
import java.util.Map;

/**
* This is a mapper class which maps backend delegate interface to its implementations so that
* the implementation can be changed whenever required.
*
* @author KH2624
*
**/

public class PayeeManagementBackendDelegateMapper implements DBPAPIMapper<BackendDelegate> {

	@Override
	public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {


        Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();

       /* mappings of backend delegate interface and implementation to be added */
        map.put(BillPayPayeeBackendDelegate.class, BillPayPayeeBackendDelegateImpl.class);
        map.put(P2PPayeeBackendDelegate.class, P2PPayeeBackendDelegateImpl.class);
        map.put(ExternalPayeeBackendDelegate.class, ExternalPayeeBackendDelegateImpl.class);
        map.put(InterBankPayeeBackendDelegate.class, InterBankPayeeBackendDelegateImpl.class);
        map.put(InternationalPayeeBackendDelegate.class, InternationalPayeeBackendDelegateImpl.class);
        map.put(IntraBankPayeeBackendDelegate.class, IntraBankPayeeBackendDelegateImpl.class);
        map.put(WireTransfersPayeeBackendDelegate.class, WireTransfersPayeeBackendDelegateImpl.class);
        map.put(BulkPaymentsPayeeBackendDelegate.class, BulkPaymentsPayeeBackendDelegateImpl.class);
        map.put(GetExternalPayeesBackendDelegate.class, GetExternalPayeesBackendDelegateImpl.class);
        
        return map;
	}
	
}
