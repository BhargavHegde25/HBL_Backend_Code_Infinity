package com.temenos.infinity.api.docmanagement.businessdelegate.api;


import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.transaction.dto.TransactionDTO;

public interface TransactionReportBusinessDelegate extends BusinessDelegate {
	TransactionDTO getTransactionById(String trsactionId, String claimsToken);
	
	TransactionDTO getBillTransactionById(String trsactionId, String claimsToken, DataControllerRequest request);
}
