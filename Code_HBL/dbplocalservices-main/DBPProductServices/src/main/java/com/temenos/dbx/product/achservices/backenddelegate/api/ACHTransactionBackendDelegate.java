package com.temenos.dbx.product.achservices.backenddelegate.api;

import java.util.List;
import java.util.Set;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.achservices.dto.ACHTransactionDTO;
import com.temenos.dbx.product.approvalservices.dto.ApprovalRequestDTO;

public interface ACHTransactionBackendDelegate extends BackendDelegate {

	/**
	 * creates a transaction at backend
	 * @version 1.0
	 * @param ACHTransactionDTO
	 * @return referenceId
	 **/
	public ACHTransactionDTO createTransactionWithoutApproval(ACHTransactionDTO transactionDTO, DataControllerRequest dcr);

	/**
	 * edits a given transaction at backend
	 * @version 1.0
	 * @param ACHTransactionDTO
	 * @return referenceId
	 **/
	public ACHTransactionDTO editTransaction(ACHTransactionDTO transactionDTO, DataControllerRequest dcr);
	
	/**
	 * fetches the status of  all ACH Payment with given confirmationNumbers from backend /Vendor service
	 * @param confirmationNumber
	 * @return
	 */
	public List<ACHTransactionDTO> fetchCollectionStatus(List<String> confirmationNumbers, DataControllerRequest dcr);
	
	/**
	 * fetches the status of all ACH Payment with given confirmationNumbers from backend /Vendor service
	 * @param confirmationNumber
	 * @return
	 */
	public List<ACHTransactionDTO> fetchPaymentStatus(List<String> confirmationNumbers, DataControllerRequest dcr);
	
	/**
     * Invokes line of business service for ach to create at the Core with pending(INAU) status
     * @param {@link ACHTransactionDTO}
     * @return {@link ACHTransactionDTO}
     */
	public ACHTransactionDTO createPendingTransaction(ACHTransactionDTO achTransactiondto, DataControllerRequest request);
	
	/**
     * Invokes line of business service for ach to approve at the Core
     * @param referenceId
     * @return {@link ACHTransactionDTO}
     */
	public ACHTransactionDTO approveTransaction(String referenceId, DataControllerRequest request);
	
	/**
     * Invokes line of business service for ach to reject at the Core
     * @param referenceId
     * @param transactionType
     * @return {@link ACHTransactionDTO}
     */
	public ACHTransactionDTO rejectTransaction(String referenceId, String transactionType, DataControllerRequest request);
	
	/**
	 * Invokes line of business service for ach to withdraw at the backend
	 * @param referenceId
	 * @param transactionType
	 * @param request
	 * @return {@link ACHTransactionDTO}
	 */
	public ACHTransactionDTO withdrawTransaction(String referenceId, String transactionType, DataControllerRequest request);
	
	/**
	 * Invokes line of business service for ach to fetch transaction by transactionId from the backend
	 * @param transactionId
	 * @param request
	 * @return {@link ACHTransactionDTO}
	 */
	public ACHTransactionDTO fetchTransactionById(String referenceId, DataControllerRequest dataControllerRequest);
		
	/**
     * Invokes line of business service for ach to edit at the backend
     * @param {@link ACHTransactionDTO}
     * @return {@link ACHTransactionDTO}
     */
	public ACHTransactionDTO validateTransaction(ACHTransactionDTO input, DataControllerRequest request);
	
	/**
	 * Invokes line of business service for ACH to delete at the backend
	 * @param referenceId
	 * @param transactionType
	 * @param request
	 * @return {@link ACHTransactionDTO}
	 */
	public ACHTransactionDTO deleteTransaction( String referenceId, String transactionType,DataControllerRequest dataControllerRequest);

	public List<ApprovalRequestDTO> fetchBackendTransactionsForApproval(Set<String> achTransactionIds,
			DataControllerRequest dcr);
	
	
}
