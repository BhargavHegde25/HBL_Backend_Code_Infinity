package com.temenos.dbx.nonproduct.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.dbx.transaction.resource.api.BulkTransferResource;
import com.temenos.dbx.transaction.resource.api.DisputeTransactionResource;
import com.temenos.dbx.transaction.resource.api.TransactionReportDownloadResource;
import com.temenos.dbx.transaction.resource.impl.BulkTransferResourceImpl;
import com.temenos.dbx.transaction.resource.impl.DisputeTransactionResourceImpl;
import com.temenos.dbx.transaction.resource.impl.TransactionReportDownloadResourceImpl;

public class BaseResourcesMapper implements DBPAPIMapper<Resource> {


    
	@Override
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
		Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
		map.put(TransactionReportDownloadResource.class, TransactionReportDownloadResourceImpl.class);
		map.put(BulkTransferResource.class, BulkTransferResourceImpl.class);
		map.put(DisputeTransactionResource.class, DisputeTransactionResourceImpl.class);


		return map;
	}
}
