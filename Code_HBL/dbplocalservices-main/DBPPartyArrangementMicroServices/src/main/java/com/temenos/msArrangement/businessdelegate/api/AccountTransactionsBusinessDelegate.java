package com.temenos.msArrangement.businessdelegate.api;

import java.util.List;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.msArrangement.dto.AccountTransactionsDTO;


/**
 * 
 * @author KH2281
 * @version 1.0
 * Interface for AccountTransactionsResource extends {@link BusinessDelegate}
 *
 */
public interface AccountTransactionsBusinessDelegate extends BusinessDelegate {

    
	/**
	 *  method to get the Account Transactions
	 *  return List<AccountTransactionsDTO> of account transactions
	 */
	List<AccountTransactionsDTO> getDetailsFromHoldingsMicroService(AccountTransactionsDTO inputDTO);

}
