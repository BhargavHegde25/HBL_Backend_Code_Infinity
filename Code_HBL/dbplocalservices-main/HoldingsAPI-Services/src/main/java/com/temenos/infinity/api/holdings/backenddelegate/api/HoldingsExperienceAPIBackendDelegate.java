package com.temenos.infinity.api.holdings.backenddelegate.api;

import java.util.List;

import com.dbp.core.api.BackendDelegate;
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
public interface HoldingsExperienceAPIBackendDelegate extends BackendDelegate {

    /**
     * method to get the Account Transactions return List<AccountTransactionsDTO> of account transactions
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
            DataControllerRequest request,String authToken) throws ApplicationException;

    List<AccountTransactionsDTO> getDeviceRegistrationDetails(AccountTransactionsDTO inputPayLoad,
            DataControllerRequest request) throws ApplicationException;

	List<AccountTransactionsDTO> getSearchDetailsFromHoldingsMicroService(AccountTransactionsDTO inputDTO,String authToken)
			throws ApplicationException;

	List<AccountTransactionsDTO> getLoanScheduleTransactions(AccountTransactionsDTO inputDTO,
			String isFutureRequired, DataControllerRequest request, String authToken) throws ApplicationException;

}
