package com.hbl.productservicesExtn.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.hbl.productservicesExtn.impl.ApprovalQueueResourceImplExtn;
import com.hbl.productservicesExtn.impl.BillPayTransactionResourceImplExtn;
import com.hbl.productservicesExtn.impl.InterBankFundTransferResourceImplExtn;
import com.hbl.productservicesExtn.impl.IntraBankFundTransferResourceImplExtn;
import com.hbl.productservicesExtn.impl.OwnAccountFundTransferResourceImplExtn;
import com.temenos.dbx.product.approvalservices.resource.api.ApprovalQueueResource;
import com.temenos.dbx.product.transactionservices.resource.api.BillPayTransactionResource;
import com.temenos.dbx.product.transactionservices.resource.api.InterBankFundTransferResource;
import com.temenos.dbx.product.transactionservices.resource.api.IntraBankFundTransferResource;
import com.temenos.dbx.product.transactionservices.resource.api.OwnAccountFundTransferResource;
import com.temenos.dbx.product.transactionservices.resource.impl.InterBankFundTransferResourceImpl;
import com.temenos.dbx.product.transactionservices.resource.impl.OwnAccountFundTransferResourceImpl;

public class HBLResourceMapper implements DBPAPIMapper<Resource>{

	@Override
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
		// TODO Auto-generated method stub
		Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
		map.put(ApprovalQueueResource.class, ApprovalQueueResourceImplExtn.class);
		map.put(OwnAccountFundTransferResource.class, OwnAccountFundTransferResourceImplExtn.class);
		map.put(IntraBankFundTransferResource.class, IntraBankFundTransferResourceImplExtn.class);
		map.put(InterBankFundTransferResource.class, InterBankFundTransferResourceImplExtn.class);
		map.put(BillPayTransactionResource.class, BillPayTransactionResourceImplExtn.class);
		
		return map;
	}
}
