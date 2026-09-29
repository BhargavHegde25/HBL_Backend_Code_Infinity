package com.temenos.dbx.product.payeeservices.businessdelegate.api;

import java.util.List;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.payeeservices.dto.ExternalPayeeBackendDTO;

/**
 * Handles all the operations on External Payees
 * @author KH2638
 * extends {@link BusinessDelegate}
 */
public interface ExternalPayeeBusinessDelegate extends BusinessDelegate {
	
	/**
     * Gets all the Payee details from external payee product tables
     * @return List of {@link ExternalPayeeBackendDTO}
     */
	public List<ExternalPayeeBackendDTO> fetchPayeesFromDBXOrch(Map<String, Object> headerParams, DataControllerRequest dcRequest);
	
}
