package com.temenos.dbx.product.transactionservices.backenddelegate.api;


import java.util.List;
import java.util.Set;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.approvalservices.dto.ApprovalRequestDTO;
import com.temenos.dbx.product.transactionservices.dto.WireTransactionBackendDTO;
import com.temenos.dbx.product.transactionservices.dto.WireTransactionDTO;

/**
 * Handles all backend operations on domestic wire transaction
 * @author KH2624
 * extends {@link BackendDelegate}
 */

public interface DomesticWireTransactionBackendDelegate extends BackendDelegate {
	
	/**
     * Invokes line of business service for Domestic Wire transfer to create at the Backend
     * @param {@link WireTransactionBackendDTO}
     * @return {@link WireTransactionDTO}
     */
	public WireTransactionDTO createTransactionWithoutApproval(WireTransactionBackendDTO wireTransactionBackendDTO, DataControllerRequest request);

	/**
     * Invokes line of business service for domestic wire transfer to create at the Core with pending(INAU) status
     * @param {@link WireTransactionDTO}
     * @return {@link WireTransactionDTO}
     */
	public WireTransactionDTO createPendingTransaction(WireTransactionBackendDTO wireTransactiondto, DataControllerRequest request);
	
	/**
     * Invokes line of business service for domestic wire transfer to approve at the Core
     * @param referenceId
     * @return {@link WireTransactionDTO}
     */
	public WireTransactionDTO approveTransaction(String referenceId, DataControllerRequest request, String frequency);
	
	/**
     * Invokes line of business service for domestic wire transfer to reject at the Core
     * @param referenceId
     * @param transactionType
     * @return {@link WireTransactionDTO}
     */
	public WireTransactionDTO rejectTransaction(String referenceId, String transactionType, DataControllerRequest request, String frequency);
	
	/**
	 * Invokes line of business service for domestic wire to withdraw at the backend
	 * @param referenceId
	 * @param transactionType
	 * @param request
	 * @return {@link WireTransactionDTO}
	 */
	public WireTransactionDTO withdrawTransaction(String referenceId, String transactionType, DataControllerRequest request, String frequency);
	
	/**
	 * Invokes line of business service for domestic wire to fetch transaction by transactionId from the backend
	 * @param transactionId
	 * @param request
	 * @return {@link WireTransactionDTO}
	 */
	public WireTransactionDTO fetchTransactionById(String referenceId, DataControllerRequest dataControllerRequest);
		
	/**
     * Invokes line of business service for domestic wire to edit at the backend
     * @param {@link WireTransactionDTO}
     * @return {@link WireTransactionDTO}
     */
	public WireTransactionDTO validateTransaction(WireTransactionBackendDTO input, DataControllerRequest request);

	/**
	 * Invokes line of business service for domestic wire transfer to delete at the backend
	 * @param referenceId
	 * @param transactionType
	 * @param request
	 * @return {@link WireTransactionDTO}
	 */
	public WireTransactionDTO deleteTransaction(String referenceId, String transactionType, DataControllerRequest dataControllerRequest);
	
	/**
	 * Invokes line of business service for domestic wire transfer to edit at the backend
	 * @param {@link WireTransactionDTO}
	 * @param request
     * @return {@link WireTransactionDTO}
	 */
	public WireTransactionDTO editTransaction(WireTransactionBackendDTO wireTransactionBackendDTO, DataControllerRequest request);

	/**
	 * fetches the transactions based on input set of ids
	 * @param billPayTransIds
	 * @param dcr
	 * @return List of {@link ApprovalRequestDTO}
	 */
	public List<ApprovalRequestDTO> fetchBackendTransactionsForApproval(Set<String> wireTransIds,
			DataControllerRequest dcr);
	
}
