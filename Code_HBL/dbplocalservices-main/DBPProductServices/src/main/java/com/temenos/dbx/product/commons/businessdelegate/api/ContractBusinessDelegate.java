package com.temenos.dbx.product.commons.businessdelegate.api;

import java.util.List;
import java.util.Set;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.dbx.product.commons.dto.ContractCoreCustomersDTO;
import com.temenos.dbx.product.commons.dto.LimitsDTO;

public interface ContractBusinessDelegate extends BusinessDelegate{
	
	/**
	 * method to fetch Contract Customers
	 * @return List<ContractCoreCustomersDTO>
	 */
	//public List<ContractCoreCustomersDTO> fetchContractCustomers();
	
	/**
	 * method to fetch Contract Customers for given userId
	 * @return List<ContractCoreCustomersDTO>
	 */
	public List<String> fetchContractCustomers(String userId);
	public List<ContractCoreCustomersDTO> fetchContractCustomers(Set<String> coreCustomerIds);
	
	/**
	 * fetches the limits of a given contractId, coreCustomerId and actionId
	 *
	 * @param contractId
	 * @param legalEntityId
	 * @return LimitsDTO
	 */
	public LimitsDTO fetchLimits(String contractId, String coreCustomerId, String actionId, String legalEntityId);
	
	/**
	 * fetched the exhausted contract-customer level limits for a given featureActionID
	 * @param companyId
	 * @param featureActionID
	 * @param date
	 * @return {@link LimitsDTO}
	 */
	public LimitsDTO fetchExhaustedLimits(String contractId, String coreCustomerId, String featureActionID, String date);
	
	/**
	 * fetches the active featureaction list for give contractId and coreCustomerId
	 * @param contractId
	 * @param coreCustomerId
	 * @return Set<String>
	 */
	public Set<String> fetchFeatureActions(String contractId, String coreCustomerId);
}
