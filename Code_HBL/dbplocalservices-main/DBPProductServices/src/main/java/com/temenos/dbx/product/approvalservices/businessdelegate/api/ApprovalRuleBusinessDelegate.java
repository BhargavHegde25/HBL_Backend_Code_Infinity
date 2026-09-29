package com.temenos.dbx.product.approvalservices.businessdelegate.api;

import java.util.List;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.dbx.product.approvalservices.dto.ApprovalRuleDTO;

/**
 * 
 * @author KH2174
 * @version 1.0
 * Interface for ApprovalRuleResource extends {@link BusinessDelegate}
 *
 */
public interface ApprovalRuleBusinessDelegate extends BusinessDelegate {
	
	/**
	 *  method to get the Approval rules
	 *  return list of {@link ApprovalRuleDTO}
	 */
	List<ApprovalRuleDTO> getAllRules();
	
}
