package com.temenos.infinity.api.holdings.businessdelegate.api;

import java.util.List;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.api.holdings.dto.AccountTransactionsDTO;

/**
 * 
 * @author KH2281
 * @version 1.0 Interface for AccountTransactionsResource extends {@link BusinessDelegate}
 *
 */
public interface AccountTransactionsBusinessDelegate extends BusinessDelegate {

    /**
     * method to get the Posted Account Transactions return List<AccountTransactionsDTO> of account transactions
     * 
     * @throws ApplicationException
     */
    List<AccountTransactionsDTO> getDetailsFromHoldingsMicroService(AccountTransactionsDTO inputDTO,String authToken)
            throws ApplicationException;

    /**
     * method to get the Account Pending Transactions return List<AccountTransactionsDTO> of account transactions
     * 
     * @throws ApplicationException
     */
    List<AccountTransactionsDTO> getPendingTransactionsDetailsFromT24(AccountTransactionsDTO inputDTO,
            DataControllerRequest request) throws ApplicationException;

    /**
     * method to get the Account Pending and Posted Transactions return List<AccountTransactionsDTO> of account
     * transactions
     * 
     * @throws ApplicationException
     */
    List<AccountTransactionsDTO> getPendingAndPostedTransactions(AccountTransactionsDTO inputDTO,
            DataControllerRequest request, String authToken) throws ApplicationException;

    /**
     * method to get the Transactions sorted based on transaction date return List<AccountTransactionsDTO> of account
     * transactions
     */
    List<AccountTransactionsDTO> sortBasedOnTransactionDate(List<AccountTransactionsDTO> transactionsDTOList,
            String order, String sortKey);

    /**
     * method to get the Transactions sorted based on amount return List<AccountTransactionsDTO> of account transactions
     */
    List<AccountTransactionsDTO> sortBasedOnAmount(List<AccountTransactionsDTO> transactionsDTOList, String order,
            String sortKey);

    /**
     * method to get the paginated Transactions return List<AccountTransactionsDTO> of account transactions
     */
    List<AccountTransactionsDTO> paginationBasedOnOffsetAndLimit(int offset, int limit,
            List<AccountTransactionsDTO> transactionsDTOList);

    /**
     * method to get the filtered Transactions return List<AccountTransactionsDTO> of account transactions
     */
    List<AccountTransactionsDTO> filterRecordsBasedOnTransactionType(String transactionType,
            List<AccountTransactionsDTO> transactionsDTOList);

    /**
     * method to get the Device Registration Details
     * 
     * @throws ApplicationException
     */
    List<AccountTransactionsDTO> getDeviceRegistrationDetails(AccountTransactionsDTO inputPayLoad,
            DataControllerRequest request) throws ApplicationException;

	List<AccountTransactionsDTO> getSearchDetailsFromHoldingsMicroService(AccountTransactionsDTO inputPayLoad,String authToken) throws ApplicationException;

	List<AccountTransactionsDTO> getloanScheduleTransactions(AccountTransactionsDTO inputDTO,
			String isFutureRequired, DataControllerRequest request, String authToken) throws ApplicationException;

}
