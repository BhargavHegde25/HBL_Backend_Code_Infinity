package com.temenos.dbx.bank.businessdelegate.api;


import com.dbp.core.api.BusinessDelegate;
import com.temenos.dbx.transaction.dto.BankDTO;

public interface BankBusinessDelegate extends BusinessDelegate {
	
	BankDTO getBank();
	
}