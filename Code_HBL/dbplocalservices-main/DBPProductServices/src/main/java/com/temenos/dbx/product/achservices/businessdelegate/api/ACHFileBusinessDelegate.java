package com.temenos.dbx.product.achservices.businessdelegate.api;

import java.io.File;
import java.util.List;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.achservices.dto.ACHFileDTO;
import com.temenos.dbx.product.achservices.dto.ACHFileFormatTypeDTO;
import com.temenos.dbx.product.achservices.dto.ACHFileRecordDTO;
import com.temenos.dbx.product.approvalservices.dto.ApprovalRequestDTO;
import com.temenos.dbx.product.approvalservices.dto.BBRequestDTO;
import com.temenos.dbx.product.commons.dto.FilterDTO;

/**
 * @author KH2638
 * @version 1.0 Interface for ACHFileServicesBusinessDelegate extends
 *          {@link BusinessDelegate}
 */

public interface ACHFileBusinessDelegate extends BusinessDelegate{

	/**
	 * method to create ACH File At DBX
	 * @param ACHFileDTO  
	 * @return ACHFileDTO
	 */
	public ACHFileDTO uploadACHFileAtDBX(ACHFileDTO achfileDTO);
	
	/**
	 * method to get ACH File Formats
	 * @return list of {@link ACHFileDTO}
	 */
	public List<ACHFileFormatTypeDTO> getACHFileFormats();
	
	/**
	 * method to update requestId
	 * @param achFileId
	 * @param requestId  
	 * @return boolean
	 */
	boolean updateRequestId(String achFileId, String requestId);
	
	/**
	 * method to update requestId
	 * @param achFileId
	 * @param status  
	 * @param confirmationNumber
	 * @return ACHFileDTO
	 */
	public ACHFileDTO updateStatus(String achFileId, String status, String confirmationNumber);
	
	/**
	 * method to get ACH Files with given parameters
	 * @param customerId
	 * @param achFileId
	 * @param {@link FilterDTO} contains all filter parameters required for procedure
	 * @return List of {@link ACHFileDTO}
	 */
	public List<ACHFileDTO> fetchACHFiles(String customerId, String achFileId, FilterDTO filters, DataControllerRequest dcr);
	
	/**
	 * executes the given ACH File after approval
	 * @param achFileId
	 * @param featureActionId 
	 */
	public void executeACHFileAfterApproval(String achFileId, String featureActionId, DataControllerRequest dcr);
	
	/**
	 * fetches the ACHFile from DBX tables
	 * @param achFileId
	 * @return ACHFileDTO
	 */
	public ACHFileDTO fetchACHFileEntry(String achFileId);

	/**
	 * Creates File record and subrecords
	 * @param ACHFileRecordDTO records
	 * @return boolean - true if successfully created otherwise false
	 */
	public boolean createFileRecordAndSubRecords(List<ACHFileRecordDTO> records);

	/**
	 * Deletes ACH File
	 * @param String achfileId
	 * @return boolean - true if success and false in case of failure
	 */
	public boolean deleteACHFileAtDBX(String achfileId);
	
	/**
	 * Validates the ACH file
	 * @param achFile
	 * @return
	 */
	public ACHFileDTO validateACHFile(File achFile);

	/**
	 * fetches the ACHFiles based on List of BBRequestDTOs and the returned value contains the merged information
	 * @param requests
	 * @param dcr
	 * @return List<ApprovalRequestDTO>
	 */
	public List<ApprovalRequestDTO> fetchACHFilesWithApprovalInfo(List<BBRequestDTO> requests, DataControllerRequest dcr);

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
	 * fetches file entry from achfile table for the given confirmationNumber
	 * @param confirmationNumber
	 * @param companyIds
	 * @param createdby
	 * @return {@link ACHFileDTO}
	 */
	public ACHFileDTO fetchExecutedTranscationEntry(String confirmationNumber, List<String> companyIds,
			String createdby);

	/**
	 * fetches file entries from achfile table with given filter
	 * @param filter
	 * @param dcr
	 * @return List of {@link ApprovalRequestDTO}
	 */
	public List<ApprovalRequestDTO> fetchACHFilesForApprovalInfo(String filter, DataControllerRequest dcr);

	/**
	 * Method to update status in dbx infinity tables using achFileId
	 * @param achFileId
	 * @param status
	 * @param confirmationNumber
	 * @return {@link ACHFileDTO}
	 */
	public ACHFileDTO updateStatusUsingTransactionId(String achFileId, String status, String confirmationNumber);
	
}
