package com.temenos.dbx.product.payeeservices.backenddelegate.api;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.payeeservices.dto.InterBankPayeeBackendDTO;

/**
 * @author Subrahmanyam Yadav
 * @version 1.0
 * @Interface InterBankPayeeBackendDelegate extends {@link BackendDelegate}
 */
public interface InterBankPayeeBackendDelegate extends BackendDelegate {

	/**
     * Gets all the Payees associated to user using companyId and customerId
     * @param Set<String> payeeIds
     * @param headerParams - request header params
     * @param dcRequest - contains identity handler which is used to fetch UserId at core service
     * @return List of {@link InterBankPayeeBackendDTO}
     */
	public List<InterBankPayeeBackendDTO> fetchPayees(Set<String> payeeIds, 
			Map<String, Object> headerParams, DataControllerRequest dcRequest);
	
	/**
     * Gets all the Payees associated to user using companyId and customerId
     * @param interBankPayeeBackendDTO - contains customerId, companyId, search string, sortby, sort order and other filter params
     * @param headerParams - request header params
     * @param dcRequest - contains identity handler which is used to fetch UserId at core service
     * @return List of {@link InterBankPayeeBackendDTO}
     */
    public InterBankPayeeBackendDTO createPayee(InterBankPayeeBackendDTO interBankPayeeBackendDTO,
                                                         Map<String, Object> headerParams, DataControllerRequest dcRequest);
    
    /**
     * Deletes Payee at backend table - payee table
     * @param  interBankPayeeBackendDTO - contains details for backend table
     * @param  headerParams - request header params
     * @param  dcRequest - contains identity handler which is used to fetch UserId at core service
     * @return {@link InterBankPayeeBackendDTO}
     */
    public InterBankPayeeBackendDTO deletePayee(InterBankPayeeBackendDTO interBankPayeeBackendDTO,
                                                         Map<String, Object> headerParams, DataControllerRequest dcRequest);
    
    /**
     * Edits Payee at backend table - payee table
     * @param interBankPayeeBackendDTO - contains details for backend table
     * @param headerParams - request header params
     * @param dcRequest - contains identity handler which is used to fetch UserId at core service
     * @return {@link InterBankPayeeBackendDTO}
     */
    public InterBankPayeeBackendDTO editPayee(InterBankPayeeBackendDTO interBankPayeeBackendDTO,
                                                       Map<String, Object> headerParams, DataControllerRequest dcRequest);

	public boolean isValidIbanAndSwiftCode(String iban, String swiftcode, DataControllerRequest dcr);

    public String validateForApprovals( DataControllerRequest request, Map<String, Object> requestParameters);

    String checkIfPayeeStatusInPending(DataControllerRequest request, Map<String, Object> requestParameters );
}
