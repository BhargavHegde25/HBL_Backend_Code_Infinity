package com.temenos.dbx.product.transactionservices.mapper;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.dbx.product.transactionservices.resource.api.*;
import com.temenos.dbx.product.transactionservices.resource.impl.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author KH1755
 * @version 1.0
 * implements {@link DBPAPIMapper}
 */
public class TransactionServicesResourceMapper implements DBPAPIMapper<Resource> {
	

	@Override
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
		Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
		
		//Add Mapping of Business Delegates interface and their implementation
        map.put(BillPayTransactionResource.class, BillPayTransactionResourceImpl.class);
        map.put(P2PTransactionResource.class, P2PTransactionResourceImpl.class);
        map.put(InternationalWireTransferResource.class, InternationalWireTransferResourceImpl.class);
        map.put(IntraBankFundTransferResource.class, IntraBankFundTransferResourceImpl.class);
        map.put(InternationalFundTransferResource.class, InternationalFundTransferResourceImpl.class);
        map.put(DomesticWireTransferResource.class, DomesticWireTransferResourceImpl.class);
        map.put(InterBankFundTransferResource.class, InterBankFundTransferResourceImpl.class);
        map.put(OwnAccountFundTransferResource.class, OwnAccountFundTransferResourceImpl.class);
       
        map.put(BulkWireTransactionsResource.class, BulkWireTransactionsResourceImpl.class);
		map.put(BulkWireFileResource.class, BulkWireFileResourceImpl.class);
		map.put(BWFileValidationResource.class, BWFileValidationResourceImpl.class);
		
		map.put(GeneralTransactionsResource.class, GeneralTransactionsResourceImpl.class);
		
		map.put(RDCResource.class, RDCResourceImpl.class);
		map.put(BulkWiresResource.class, BulkWiresResourceImpl.class);
        
        return map;
	}

}
