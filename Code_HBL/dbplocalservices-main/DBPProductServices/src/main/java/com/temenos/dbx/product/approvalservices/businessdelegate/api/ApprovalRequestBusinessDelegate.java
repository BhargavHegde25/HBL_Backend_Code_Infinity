package com.temenos.dbx.product.approvalservices.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.dbx.product.approvalservices.dto.ApprovalRequestCountDTO;
import com.temenos.dbx.product.approvalservices.dto.ApprovalRuleDTO;

/**
 * 
 * @author KH1755
 * @version 1.0
 * Interface for ApprovalRequestBusinessDelegate extends {@link BusinessDelegate}
 *
 */
public interface ApprovalRequestBusinessDelegate extends BusinessDelegate {

	/**
	 *  method to get the Approval and request counts for the given customerId
	 *  return list of {@link ApprovalRuleDTO}
	 */
	ApprovalRequestCountDTO getCounts(String customerId, String approveActionList, String createActionList);
}