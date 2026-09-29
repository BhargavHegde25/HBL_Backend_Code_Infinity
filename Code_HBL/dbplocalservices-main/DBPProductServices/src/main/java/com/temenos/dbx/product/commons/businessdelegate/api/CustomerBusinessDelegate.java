package com.temenos.dbx.product.commons.businessdelegate.api;

import java.util.List;
import java.util.Set;

import org.json.JSONObject;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.dbx.product.commons.dto.CustomerDTO;
import com.temenos.dbx.product.commons.dto.LimitsDTO;
import com.temenos.dbx.product.commons.dto.UserLimitsDTO;

public interface CustomerBusinessDelegate extends BusinessDelegate {

	
	/**
	 * Fetches the user role for a given  contractId , coreCustomerId and customerId
	 * @param customerId
	 * @return userRole
	 */
	public String getUserContractCustomerRole(String contractId, String coreCustomerId, String customerId);
	
	/**
	 * Fetches user level limits for a given customerId, featureactionID and accountId. This is possible only in the case of Business Users
	 * @param customerId
	 * @param featureActionID
	 * @param accountId
	 * @return {@link UserLimitsDTO}
	 */
	public UserLimitsDTO fetchCustomerLimits(String customerId, String featureActionID, String accountId);
	
	/**
	 * fetched the exhausted User level limits for a given customerId and featureActionID
	 * @param customerId
	 * @param featureActionID
	 * @param date
	 * @return {@link LimitsDTO}
	 */
	public LimitsDTO fetchExhaustedLimits(String customerId, String featureActionID, String date);
	
	/**
	 * fetched the exhausted User level limits for a given customerId, featureActionID and accountId
	 * @param customerId
	 * @param featureActionID
	 * @param date
	 * @param accountId
	 * @return {@link LimitsDTO}
	 */
	public LimitsDTO fetchExhaustedLimits(String customerId, String featureActionID, String date, String accountId);
	
	/**
	 * Fetches the user details for a given customerId
	 * @param customerId
	 *  @return customer Record
	 */
	public JSONObject getUserDetails(String customerId);
	
	/**
	 * Fetches the Combined UserId of the given customerId
	 * @param customerId
	 * @return List<String>
	 */
	public List<String> getCombinedUserIds(String customerId);
    
    /**
     * Fetches the List of CustomerDTOS the given list of customerId
     * @param Set of customerId
     * @return List<CustomerDTO>
     */
    public List<CustomerDTO> getCustomerInfo(Set<String> customerIds);
}
