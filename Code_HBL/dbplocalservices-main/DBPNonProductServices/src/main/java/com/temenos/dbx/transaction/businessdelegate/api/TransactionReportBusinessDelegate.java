package com.temenos.dbx.transaction.businessdelegate.api;


import com.dbp.core.api.BusinessDelegate;
import com.temenos.dbx.transaction.dto.TransactionDTO;

public interface TransactionReportBusinessDelegate extends BusinessDelegate {
	TransactionDTO getTransactionById(String trsactionId, String claimsToken);
}
