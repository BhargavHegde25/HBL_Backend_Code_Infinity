package com.temenos.dbx.product.transactionservices.mapper;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.dbx.product.approvalservices.businessdelegate.api.ApprovalQueueBusinessDelegate;
import com.temenos.dbx.product.approvalservices.businessdelegate.impl.ApprovalQueueBusinessDelegateImpl;
import com.temenos.dbx.product.commons.businessdelegate.api.*;
import com.temenos.dbx.product.commons.businessdelegate.impl.*;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.*;
import com.temenos.dbx.product.transactionservices.businessdelegate.impl.*;

import java.util.HashMap;
import java.util.Map;


/**
 * 
 * @author KH1755
 * @version 1.0
 * implements {@link DBPAPIMapper}
 */
public class TransactionServicesBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate>{

	@Override
	public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
		Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
		
		//Add Mapping of Business Delegates interface and their implementation
        map.put(BillPayTransactionBusinessDelegate.class, BillPayTransactionBusinessDelegateImpl.class);
        map.put(P2PTransactionBusinessDelegate.class, P2PTransactionBusinessDelegateImpl.class);
        map.put(DomesticWireTransactionBusinessDelegate.class, DomesticWireTransactionBusinessDelegateImpl.class);
        map.put(InternationalWireTransactionBusinessDelegate.class, InternationalWireTransactionBusinessDelegateImpl.class);
        map.put(IntraBankFundTransferBusinessDelegate.class, IntraBankFundTransferBusinessDelegateImpl.class);
        map.put(InternationalFundTransferBusinessDelegate.class, InternationalFundTransferBusinessDelegateImpl.class);
        map.put(InterBankFundTransferBusinessDelegate.class, InterBankFundTransferBusinessDelegateImpl.class);
        map.put(OwnAccountFundTransferBusinessDelegate.class, OwnAccountFundTransferBusinessDelegateImpl.class);
        
        
        map.put(TransactionLimitsBusinessDelegate.class, TransactionLimitsBusinessDelegateImpl.class);
		map.put(BulkWireTransactionsBusinessDelegate.class,BulkWireTransactionsBusinessDelegateImpl.class);
		map.put(CustomerBusinessDelegate.class, CustomerBusinessDelegateImpl.class);
		map.put(ApprovalQueueBusinessDelegate.class, ApprovalQueueBusinessDelegateImpl.class);
		map.put(AuthorizationChecksBusinessDelegate.class, AuthorizationChecksBusinessDelegateImpl.class);
		map.put(BulkWireFileBusinessDelegate.class, BulkWireFileBusinessDelegateImpl.class);
		
		map.put(UserRoleBusinessDelegate.class, UserRoleBusinessDelegateImpl.class);
		
		map.put(GeneralTransactionsBusinessDelegate.class, GeneralTransactionsBusinessDelegateImpl.class);
		
		map.put(RDCBusinessDelegate.class, RDCBusinessDelegateImpl.class);
		map.put(BulkWiresBusinessDelegate.class, BulkWiresBusinessDelegateImpl.class);
		map.put(AccountBusinessDelegate.class, AccountBusinessDelegateImpl.class);
		map.put(ApplicationBusinessDelegate.class, ApplicationBusinessDelegateImpl.class);

		return map;
	}

}
