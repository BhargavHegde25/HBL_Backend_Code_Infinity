package com.temenos.dbx.product.approvalmatrixservices.businessdelegate.api;

import java.util.List;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.dbx.product.approvalmatrixservices.dto.ApprovalRuleDTO;
/**
 * 
 * @author KH2387
 * @version 1.0
 * Interface for ApprovalRuleBusinessDelegate extends {@link BusinessDelegate}
 *
 */
public interface ApprovalRuleBusinessDelegate extends BusinessDelegate {
	
	/**
	 *  method to get the Approval rules
	 *  return list of {@link ApprovalRuleDTO}
	 */
	List<ApprovalRuleDTO> getApprovalRules();
}
