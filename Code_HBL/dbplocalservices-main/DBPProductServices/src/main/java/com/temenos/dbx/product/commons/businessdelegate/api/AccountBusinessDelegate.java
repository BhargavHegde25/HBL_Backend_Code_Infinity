package com.temenos.dbx.product.commons.businessdelegate.api;

import java.util.List;
import java.util.Set;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.dbx.product.commons.dto.CustomerAccountsDTO;

public interface AccountBusinessDelegate extends BusinessDelegate {

	
	/**
	 * Checks whether the given accountID is business account or not.
	 * @param accountID
	 * @return
	 */
	public boolean isBusinessAccount(String accountID);
	
	/**
	 * Gets all the business accounts associated with a user
	 * @param customerID
	 * @return
	 */
	public List<String> getUserBusinessAccounts(String customerID);
	
	/**
	 * Get CIF of the given account
	 * @param customerId
	 * @param accountId
	 * @return CustomerAccountsDTO
	 */
	public CustomerAccountsDTO getAccountDetails(String customerId, String accountId);

	/**
	 * Get accountIds of the given cif and contractId
	 * @param accountId
	 * @return
	 */
	public List<String> fetchCifAccounts(String cif, String contractId);
	
	/**
	 * method to fetch Customer Accounts
	 * @param customerId
	 * @param Set<String>  
	 * @return List<CustomerAccountsDTO>
	 */
	public List<CustomerAccountsDTO> getAccountDetails(String customerId, Set<String> accounts);
	
	/**
	 * method to know whether all accounts belong to same contract customer
	 * @param customerId
	 * @param Set<String>  
	 * @return CustomerAccountsDTO
	 */
	public CustomerAccountsDTO getCommonContractCustomer(String customerId, Set<String> accounts);
	
}
