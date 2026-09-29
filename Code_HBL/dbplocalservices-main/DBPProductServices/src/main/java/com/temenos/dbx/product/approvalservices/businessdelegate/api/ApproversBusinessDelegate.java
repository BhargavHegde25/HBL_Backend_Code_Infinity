package com.temenos.dbx.product.approvalservices.businessdelegate.api;

import java.util.List;

import com.dbp.core.api.BusinessDelegate;

/**
 * 
 * @author KH2174
 * @version 1.0
 * Interface for ApproversBusinessDelegate extends {@link BusinessDelegate}
 *
 */
public interface ApproversBusinessDelegate extends BusinessDelegate {
	
	/**
	 *  method to fetch approver Ids for a given actionId,accountId and organizationId
	 *  @param String contractId
	 *  @param String cif
	 *  @param String accountId
	 *  @param String actionId
	 *  return list of String
	 */
	List<String> getAccountActionApproverList(String contractId, String cif, String accountIds, String actionId);
	
	/**
	 *  method to fetch approver Ids for a given requestId
	 *  @param String requestId
	 *  return list of String
	 */
	List<String> getRequestApproversList(String requestId);
	
	/**
	 *  method to fetch acted approver Ids for a given requestId
	 *  @param String requestId
	 *  return list of String
	 */
	List<String> getRequestActedApproversList(String requestId, String status);
	
}
