package com.temenos.dbx.product.signatorygroupservices.businessdelegate.api;

import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.signatorygroupservices.dto.ApprovalModeDTO;

/**
 * 
 * @author KH2174
 * @version 1.0 Interface for ApproversBusinessDelegate extends
 *          {@link BusinessDelegate}
 *
 */
public interface ApprovalModeBusinessDelegate extends BusinessDelegate {

	ApprovalModeDTO fetchApprovalMode(String coreCustomerId, String contractId, String userId,
			Map<String, Object> headersMap, DataControllerRequest request);

	ApprovalModeDTO updateApprovalMode(String coreCustomerId, String contractId, Boolean isGroupLevel,
			Map<String, Object> headersMap, DataControllerRequest request);

	ApprovalModeDTO deleteApprovalMode(String coreCustomerId, String contractId, Map<String, Object> headersMap,
			DataControllerRequest request);
	
	boolean checkForApprovalMatrixPermission(String coreCustomerId, String contractId, String userId,
			Map<String, Object> headersMap, DataControllerRequest request);
	
	/**
	 * Method to check if approval mode is groupLevel using customerId 
	 * @param coreCustomerId
	 * @return boolean
	 */
	public boolean isGroupLevel(String coreCustomerId);

}
