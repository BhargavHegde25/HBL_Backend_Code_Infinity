package com.temenos.dbx.product.transactionservices.businessdelegate.api;

import java.util.List;

import com.dbp.core.api.BusinessDelegate;
import com.google.gson.JsonObject;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.dbx.product.transactionservices.dto.GeneralTransactionDTO;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * @author KH2638
 * @version 1.0 Interface for GeneralTransactionsBusinessDelegate extends
 *          {@link BusinessDelegate}
 */

public interface GeneralTransactionsBusinessDelegate extends BusinessDelegate{
	
	/**
	 * method to get general transactions with given parameters
	 * @param customerId
	 * @param transactionId
	 * @param featureActionId
	 * @param {@link FilterDTO} contains all filter parameters required for procedure
	 * @return List of {@link GeneralTransactionDTO}
	 */
	public List<GeneralTransactionDTO> fetchGeneralTransactions(String customerId, String transactionId, String featureActionId, FilterDTO filters);
	

	/**
	 * general transactions execution after approval
	 * @param transactionId
	 * @param featureActionId
	 * @param request
	 */
	public void executeTransactionAfterApproval(String transactionId, String featureActionId, DataControllerRequest request);
	
	/**
	 * upadte general transactions
	 * @param transactionId
	 * @param featureActionId
	 * @param status
	 * 
	 * @return Boolean
	 */
	public Boolean updateStatus(String transactionId, String featureActionId, String status);


	/**
	 * fetch transaction details
	 * @param transactionId
	 * @param featureActionId
	 * @param request
	 */
	public JsonObject fetchTransactionEntry(String transactionId, String featureActionId, String createdBy);
	
	/**
	 * method to get general transaction with given confirmation number and featureactionId
	 * @param confirmationNumber
	 * @param featureActionId
	 * @return
	 */
	public GeneralTransactionDTO fetchExecutedTranscationEntry(String confirmationNumber, String featureActionId);


	/**
	 * method to get general transaction with given transactionId and featureactionId
	 * @param transactionId
	 * @param featureActionId
	 * @param request
	 * @return
	 */
	public List<GeneralTransactionDTO> fetchTransactionById(String transactionId, String featureActionId,
			DataControllerRequest request);
	
	/**
	 * general transactions execution after reject
	 * @param transactionId
	 * @param featureActionId
	 * @param request
	 */
	public void rejectGeneralTransaction(String transactionId, String featureActionId, DataControllerRequest request);
	
	/**
	 * general transactions execution after withdraw
	 * @param transactionId
	 * @param featureActionId
	 * @param request
	 */
	public void withdrawGeneralTransaction(String transactionId, String featureActionId, DataControllerRequest request);


	public Result getPurposeCodesById(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception;


	public Result getExternalCodes(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception;
}
