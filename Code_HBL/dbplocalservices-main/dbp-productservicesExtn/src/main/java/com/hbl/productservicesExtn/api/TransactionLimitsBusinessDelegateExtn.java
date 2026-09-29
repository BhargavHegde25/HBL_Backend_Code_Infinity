package com.hbl.productservicesExtn.api;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.commons.businessdelegate.api.TransactionLimitsBusinessDelegate;
import com.temenos.dbx.product.commons.dto.TransactionStatusDTO;
import com.temenos.dbx.product.constants.TransactionStatusEnum;

public interface TransactionLimitsBusinessDelegateExtn extends TransactionLimitsBusinessDelegate{
	public TransactionStatusDTO validateForLimits (String customerId, String companyId, String accountId, 
			String featureActionID, Double amount, TransactionStatusEnum transactionStatus, String date, String transactionCurrency, String serviceCharge, DataControllerRequest request);
	public TransactionStatusDTO validateForMobileLimits(String userId, String companyId, String accountId, String featureActionID, Double amount,
			TransactionStatusEnum transactionStatus, String date, String transactionCurrency, String serviceCharge, DataControllerRequest request);
}
