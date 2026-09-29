package com.temenos.dbx.product.payeeservices.backenddelegate.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dbp.core.api.BackendDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.payeeservices.dto.IntraBankPayeeBackendDTO;

/**
 * @author Subrahmanyam Yadav
 * @version 1.0
 * @Interface IntraBankPayeeBackendDelegate extends {@link BackendDelegate}
 */
public interface IntraBankPayeeBackendDelegate extends BackendDelegate {
	
	/**
     * Filters Payee Based on User Request Input
     * @param Set<String> payeeIds
     * @param headerParams - request header params
     * @param dcRequest - contains identity handler which is used to fetch UserId at core service
     * @return List of {@link IntraBankPayeeBackendDTO}
     */
	public List<IntraBankPayeeBackendDTO> fetchPayees(Set<String> payeeIds, 
			Map<String, Object> headerParams, DataControllerRequest dcRequest);
	
	/**
     * Creates Payee at Backend - payee table
     * @param BillPayPayeeBackendDTO billPayPayeeBackendDTO - contains details for backend table
     * @param headerParams - request header params
     * @param DataControllerRequest dcRequest - contains identity handler which is used to fetch UserId at core service
     * @return {@link IntraBankPayeeBackendDTO}
     */
	public IntraBankPayeeBackendDTO createPayee(IntraBankPayeeBackendDTO intraBankPayeeBackendDTO,
			Map<String, Object> headerParams, DataControllerRequest dcRequest);
	
	/**
     * Deletes Payee at backend table - payee table
     * @param BillPayPayeeBackendDTO billPayPayeeBackendDTO - contains details for backend table
     * @param headerParams - request header params
     * @param DataControllerRequest dcRequest - contains identity handler which is used to fetch UserId at core service
     * @return {@link IntraBankPayeeBackendDTO}
     */
	public IntraBankPayeeBackendDTO deletePayee(IntraBankPayeeBackendDTO intraBankPayeeBackendDTO,
			Map<String, Object> headerParams, DataControllerRequest dcRequest);	

	/**
     * Edits Payee at backend table - payee table
     * @param BillPayPayeeBackendDTO billPayPayeeBackendDTO - contains details for backend table
     * @param headerParams - request header params
     * @param DataControllerRequest dcRequest - contains identity handler which is used to fetch UserId at core service
     * @return {@link IntraBankPayeeBackendDTO}
     */
	public IntraBankPayeeBackendDTO editPayee(IntraBankPayeeBackendDTO intraBankPayeeBackendDTO,
			Map<String, Object> headerParams, DataControllerRequest dcRequest);

	public boolean validateBeneficiaryNameFromAccountId(String accountNumber, String beneficiaryName,
			DataControllerRequest dcr);

	String validateForApprovals( DataControllerRequest request, Map<String, Object> requestParameters);

    String checkIfPayeeStatusInPending( DataControllerRequest request, HashMap<String, Object> requestParameters );
}
