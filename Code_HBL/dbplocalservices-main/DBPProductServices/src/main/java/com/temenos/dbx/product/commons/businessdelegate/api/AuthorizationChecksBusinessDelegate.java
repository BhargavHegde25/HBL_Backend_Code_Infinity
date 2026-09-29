package com.temenos.dbx.product.commons.businessdelegate.api;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.exceptions.MiddlewareException;

public interface AuthorizationChecksBusinessDelegate extends BusinessDelegate{
	
	/**
	 *  Verifies whether user is authorized to given feature action for the given account(account details will be fetched from cache), If account is null,it checks for only given feature action
	 *  @param userId
	 *  @param featureAction
	 *  @param accountIds
	 *  @param isCombinedUser
	 *  @return Boolean
	 */
	public boolean isUserAuthorizedForFeatureAction (String userId, String featureAction, String accountIds, boolean isCombinedUser);
	
	/**
	 *  Method to verify whether user has permission to perform given payee operation
	 *  @param featureAction
	 *  @param isBusinessPayee
	 *  @param headerParams
	 *  @param dcRequest
	 *  @return Boolean
	 */
	public boolean isUserAuthorizedForPayeeOperations(String featureAction, String isBusinessPayee, Map<String, Object> headerParams, DataControllerRequest dcRequest);



	/**
	 *  Method to verify whether user has permission to perform fetch payee operation
	 *  @param featureAction
	 *  @param permissionType
	 *  @param headerParams
	 *  @param dcRequest
	 *  @return Boolean
	 */

	public boolean isUserAuthorizedForFetchPayees(String featureAction, String permissionType, Map<String, Object> headerParams, DataControllerRequest dcRequest);
	
	/**
	 *  Method to fetch UnAuthorized Accounts for user
	 *  @param userId
	 *  @param featureAction
	 *  @param isCombinedUser
	 *  @return Set<String>
	 */
	public Set<String> fetchUnAuthorizedAccounts(String userId, String featureAction, boolean isCombinedUser);

	/**
	 * checks whether given fromAccount belongs to given customerId(createdby) or not
	 * @param createdby
	 * @param fromAccount
	 * @return
	 * @throws MiddlewareException
	 */
	public boolean isOneOfMyAccounts(String createdby, String fromAccount);
	
	/**
	 * checks whether given list of fromAccounts belongs to given customerId(createdby) or not
	 * @param createdby
	 * @param fromAccount
	 * @return
	 */
	public boolean isOneOfMyAccounts(String createdby, List<String> fromAccount);

	/**
	 * 
	 * @param createdby
	 * @return
	 */
	public JSONObject fetchMyAccounts(String createdby);

	/**
	 *  Checks whether user has access to given cifs for given featureAction 
	 *  @param List of featureActions
	 *  @param contractCifMap 
	 *  @param headerParams
	 *  @param dcRequest
	 *  @return boolean - true if authorized otherwise false
	 */
	public boolean isUserAuthorizedForFeatureAction(List<String> featureActions, Map<String, List<String>> contractCifMap, Map<String, Object> headerParams,
			DataControllerRequest dcRequest);

	/**
	 *  Method to fetch cifs for which the user has action for given featureAction
	 *  @param List of featureActions
	 *  @param headerParams
	 *  @param dcRequest
	 *  @return Set<String> - list of authorized cifs
	 */
	public Set<String> getUserAuthorizedCifsForFeatureAction(List<String> featureActions, Map<String, Object> headerMap,
			DataControllerRequest request);

	Map<String, List<String>> getAuthorizedCifs(List<String> featureActions, Map<String, Object> headerParams, DataControllerRequest dcRequest);

	/**
	 *  Verifies whether user is authorized to given feature action for the given account
	 *  @param userId
	 *  @param featureAction
	 *  @param accountIds
	 *  @param isCombinedUser
	 *  @return Boolean
	 */
	public boolean isUserAuthorizedForFeatureActionForAccount(String userId, String featureAction, String accountIds,
			boolean isCombinedUser);
}
