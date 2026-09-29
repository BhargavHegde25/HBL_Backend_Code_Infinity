package com.temenos.dbx.product.payeeservices.backenddelegate.api;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.payeeservices.dto.InternationalPayeeBackendDTO;

/**
 * @author Subrahmanyam Yadav
 * @version 1.0
 * @Interface InternationalPayeeBackendDelegate extends {@link BackendDelegate}
 */
public interface InternationalPayeeBackendDelegate extends BackendDelegate{
	
	/**
     * Filters Payee Based on User Request Input
     * @param Set<String> payeeIds
     * @param headerParams - request header params
     * @param dcRequest - contains identity handler which is used to fetch UserId at core service
     * @return List of {@link internationalpayeeBackendDTO}
     */
	public List<InternationalPayeeBackendDTO> fetchPayees(Set<String> payeeIds, 
			Map<String, Object> headerParams, DataControllerRequest dcRequest);
	/**
     * Creates Payee at Backend - payee table
     * @param InternationalpayeeBackendDTO internationalpayeeBackendDTO - contains details for backend table
     * @param headerParams - request header params
     * @param DataControllerRequest dcRequest - contains identity handler which is used to fetch UserId at core service
     * @return {@link internationalpayeeBackendDTO}
     */
	public InternationalPayeeBackendDTO createPayee(InternationalPayeeBackendDTO internationalpayeeBackendDTO,
			Map<String, Object> headerParams, DataControllerRequest dcRequest);
	
	/**
     * Deletes Payee at backend table - payee table
     * @param BillPayPayeeBackendDTO billPayPayeeBackendDTO - contains details for backend table
     * @param headerParams - request header params
     * @param DataControllerRequest dcRequest - contains identity handler which is used to fetch UserId at core service
     * @return {@link InternationalpayeeBackendDTO}
     */
	public InternationalPayeeBackendDTO deletePayee(InternationalPayeeBackendDTO internationalpayeeBackendDTO,
			Map<String, Object> headerParams, DataControllerRequest dcRequest);
	
	/**
     * Edits Payee at backend table - payee table
     * @param InternationalpayeeBackendDTO internationalpayeeBackendDTO - contains details for backend table
     * @param headerParams - request header params
     * @param DataControllerRequest dcRequest - contains identity handler which is used to fetch UserId at core service
     * @return {@link internationalpayeeBackendDTO}
     */
	public InternationalPayeeBackendDTO editPayee(InternationalPayeeBackendDTO internationalpayeeBackendDTO,
			Map<String, Object> headerParams, DataControllerRequest dcRequest);
	
	public boolean isValidIbanAndSwiftCode(String iban, String swiftcode, DataControllerRequest dcr);

	public String validateForApprovals( DataControllerRequest request, Map<String, Object> requestParameters);

	String checkIfPayeeStatusInPending(DataControllerRequest request, Map<String, Object> requestParameters );
}
