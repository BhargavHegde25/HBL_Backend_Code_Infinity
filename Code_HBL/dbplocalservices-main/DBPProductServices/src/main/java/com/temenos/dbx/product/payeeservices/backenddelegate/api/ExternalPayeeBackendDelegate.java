package com.temenos.dbx.product.payeeservices.backenddelegate.api;

import java.util.List;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.payeeservices.dto.ExternalPayeeBackendDTO;
import com.temenos.dbx.product.payeeservices.dto.PayeesFilterDTO;

/**
 * @author Subrahmanyam Yadav
 * @version 1.0
 * @Interface ExternalPayeeBackendDelegate extends {@link BackendDelegate}
 */
public interface ExternalPayeeBackendDelegate extends BackendDelegate {

	/**
     * Gets all the Payees associated to user using companyId and customerId
     * @param PayeesFilterDTO payeesFilterDTO - contains customerId, companyId, search string, sortby, sort order and other filter params
     * @param headerParams - request header params
     * @param DataControllerRequest dcRequest - contains identity handler which is used to fetch UserId at core service
     * @return List of {@link ExternalPayeeBackendDTO}
     */
	
	public List<ExternalPayeeBackendDTO> fetchPayees(PayeesFilterDTO payeesFilterDTO, 
			Map<String, Object> headerParams, DataControllerRequest dcRequest);
	
	public Map<String, String> fetchUserNameByCoreCustomerId(Map<String, Object> serviceReqMap, 
			Map<String, Object> headerParams, DataControllerRequest dcRequest);
	
}
