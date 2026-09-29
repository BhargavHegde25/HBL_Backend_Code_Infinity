package com.temenos.dbx.product.commons.businessdelegate.api;

import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.achservices.dto.ACHFileDTO;
import com.temenos.dbx.product.commons.dto.TransactionStatusDTO;
import com.temenos.dbx.product.constants.TransactionStatusEnum;

public interface TransactionLimitsBusinessDelegate extends BusinessDelegate {

	/**
	 * validates the given entry for the limits and returns the status of the entry to be
	 * @param customerId
	 * @param companyId
	 * @param accountId
	 * @param featureActionID
	 * @param amount
	 * @param transactionStatus
	 * @param date
	 * @param transactionCurrency
	 * @param serviceCharge 
	 * @return
	 */
	public TransactionStatusDTO validateForLimits (String customerId, String companyId, String accountId, 
			String featureActionID, Double amount, TransactionStatusEnum transactionStatus, String date, String transactionCurrency, String serviceCharge, DataControllerRequest request);
	
	/**
	 * Validates offset account limits
	 * @param ACHFileDTO file details
	 * @param String userId
	 * @param String companyId
	 * @param Map<String, Double> offsetDetails - hashmap containing offset account as key and debit amount as value
	 * @param TransactionStatusEnum fileStatus
	 * @return boolean - true if success and false in case of failure
	 */
	public TransactionStatusDTO validateOffsetLimitsForACHFile (ACHFileDTO achfileDTOValidate, String userId,
			String companyId, Map<String, Double> offsetDetails, TransactionStatusEnum fileStatus, DataControllerRequest request);

	/**
	 * validates non monetary action and returns the status
	 * @param request 
	 * @param customerId
	 * @param accountId
	 * @param featureActionID
	 * @return
	 */
	/*public TransactionStatusDTO validateForLimitsForNonMonetaryActions(String customerId, String accountId,
			String featureActionID);*/
	
	public TransactionStatusDTO validateForLimitsForNonMonetaryActions(TransactionStatusDTO inputTransactionStatusDTO, DataControllerRequest request);

	public TransactionStatusDTO validateLimitsForMonetaryAction(String customerId, String contractId, String coreCustomerId, String featureActionId, String accountId, Double transactionAmount, String serviceCharge, String transactionDate, String transactionCurrency, TransactionStatusEnum status, DataControllerRequest dcRequest) throws ApplicationException;

}
