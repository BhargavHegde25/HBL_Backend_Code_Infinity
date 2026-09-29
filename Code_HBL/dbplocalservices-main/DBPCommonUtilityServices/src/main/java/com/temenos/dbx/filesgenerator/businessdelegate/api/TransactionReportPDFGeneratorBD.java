package com.temenos.dbx.filesgenerator.businessdelegate.api;

import java.io.IOException;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.dbx.transaction.dto.BankDTO;
import com.temenos.dbx.transaction.dto.TransactionDTO;

public interface TransactionReportPDFGeneratorBD extends BusinessDelegate {
	
	public byte[] generateFileAsByte(TransactionDTO transaction, BankDTO bank) throws IOException;
	
}
