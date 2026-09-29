package com.temenos.dbx.product.payeeservices.backenddelegate.api;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.payeeservices.dto.BillPayPayeeBackendDTO;

/**
 * Handles all the operations on Bill Pay Payees
 * @author KH2624
 * extends {@link BackendDelegate}
 */
public interface BillPayPayeeBackendDelegate extends BackendDelegate {
	
	/**
     * Creates Payee at Backend - payee table
     * @param BillPayPayeeBackendDTO billPayPayeeBackendDTO - contains details for backend table
     * @param headerParams - request header params
     * @param DataControllerRequest dcRequest - contains identity handler which is used to fetch UserId at core service
     * @return {@link BillPayPayeeBackendDTO}
     */
	public BillPayPayeeBackendDTO createPayee(BillPayPayeeBackendDTO billPayPayeeBackendDTO,
			Map<String, Object> headerParams, DataControllerRequest dcRequest);
	
	/**
     * Gets all the Payees associated to user using cif
     * @param Set<String> payeeIds
     * @param headerParams - request header params
     * @param dcRequest - contains identity handler which is used to fetch UserId at core service
     * @return List of {@link BillPayPayeeBackendDTO}
     */
	public List<BillPayPayeeBackendDTO> fetchPayees(Set<String> payeeIds, 
			Map<String, Object> headerParams, DataControllerRequest dcRequest);
	
	/**
     * Edits Payee at backend table - payee table
     * @param BillPayPayeeBackendDTO billPayPayeeBackendDTO - contains details for backend table
     * @param headerParams - request header params
     * @param DataControllerRequest dcRequest - contains identity handler which is used to fetch UserId at core service
     * @return {@link BillPayPayeeBackendDTO}
     */
	public BillPayPayeeBackendDTO editPayee(BillPayPayeeBackendDTO billPayPayeeBackendDTO,
			Map<String, Object> headerParams, DataControllerRequest dcRequest);
	
	/**
     * Deletes Payee at backend table - payee table
     * @param BillPayPayeeBackendDTO billPayPayeeBackendDTO - contains details for backend table
     * @param headerParams - request header params
     * @param DataControllerRequest dcRequest - contains identity handler which is used to fetch UserId at core service
     * @return {@link BillPayPayeeBackendDTO}
     */
	public BillPayPayeeBackendDTO deletePayee(BillPayPayeeBackendDTO billPayPayeeBackendDTO,
			Map<String, Object> headerParams, DataControllerRequest dcRequest);
	
}

