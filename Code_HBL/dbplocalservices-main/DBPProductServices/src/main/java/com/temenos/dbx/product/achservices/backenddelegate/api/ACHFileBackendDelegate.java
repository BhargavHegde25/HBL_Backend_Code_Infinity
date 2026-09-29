package com.temenos.dbx.product.achservices.backenddelegate.api;

import java.util.List;
import java.util.Set;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.achservices.dto.ACHFileDTO;
import com.temenos.dbx.product.approvalservices.dto.ApprovalRequestDTO;

public interface ACHFileBackendDelegate extends BackendDelegate {

	/**
	 * method to execute ACH File At Backend (Calls ACH Vendor Mock Service)
	 * @param ACHFileDTO achfileDTO - contains file details
	 * @return ACHFileDTO - contains file details
	 */
	public ACHFileDTO createTransactionWithoutApproval(ACHFileDTO achfileDTO, DataControllerRequest dcr);
	
	/**
	 * fetches the status of  all ACH Payment with given confirmationNumbers from backend /Vendor service
	 * @param confirmationNumber
	 * @return
	 */
	public List<ACHFileDTO> fetchFileStatus(List<String> confirmationNumbers, DataControllerRequest dcr);

	/**
     * Invokes line of business service for ach file to create at the Core with pending(INAU) status
     * @param {@link ACHFileDTO}
     * @return {@link ACHFileDTO}
     */
	public ACHFileDTO createPendingTransaction(ACHFileDTO achfileDTO, DataControllerRequest request);
	
	/**
     * Invokes line of business service for ach file to approve at the Core
     * @param referenceId
     * @return {@link ACHFileDTO}
     */
	public ACHFileDTO approveTransaction(String referenceId, DataControllerRequest request);
	
	/**
     * Invokes line of business service for ach file to reject at the Core
     * @param referenceId
     * @param transactionType
     * @return {@link ACHFileDTO}
     */
	public ACHFileDTO rejectTransaction(String referenceId, String transactionType, DataControllerRequest request);
	
	/**
	 * Invokes line of business service for ach file to withdraw at the backend
	 * @param referenceId
	 * @param transactionType
	 * @param request
	 * @return {@link ACHFileDTO}
	 */
	public ACHFileDTO withdrawTransaction(String referenceId, String transactionType, DataControllerRequest request);
	
	/**
	 * Invokes line of business service for ach file to fetch transaction by transactionId from the backend
	 * @param transactionId
	 * @param request
	 * @return {@link ACHFileDTO}
	 */
	public ACHFileDTO fetchTransactionById(String referenceId, DataControllerRequest dataControllerRequest);
		
	/**
     * Invokes line of business service for ach file to edit at the backend
     * @param {@link ACHFileDTO}
     * @return {@link ACHFileDTO}
     */
	public ACHFileDTO validateTransaction(ACHFileDTO input, DataControllerRequest request);

	/**
	 * Invokes line of business service for ACH file to delete at the backend
	 * @param referenceId
	 * @param transactionType
	 * @param request
	 * @return {@link ACHFileDTO}
	 */
	public ACHFileDTO deleteTransaction( String referenceId, String transactionType,DataControllerRequest dataControllerRequest);

	/**
	 * Invokes line of business service for ACH file to edit at the backend
	 * @return {@link ACHFileDTO}
	 * @param request
	 * @return {@link ACHFileDTO}
	 */
	public ACHFileDTO editTransaction(ACHFileDTO fileDTO, DataControllerRequest request);

	/**
	 * Fetches backend information with given ach file ids for approvals
	 * @param Set<String> achFileIds
	 * @param request
	 * @return List of {@link ApprovalRequestDTO}
	 */
	public List<ApprovalRequestDTO> fetchBackendTransactionsForApproval(Set<String> achFileIds, DataControllerRequest dcr);

}
