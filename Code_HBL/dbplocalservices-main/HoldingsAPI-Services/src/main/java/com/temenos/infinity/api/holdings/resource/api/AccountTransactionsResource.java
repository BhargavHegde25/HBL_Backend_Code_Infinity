package com.temenos.infinity.api.holdings.resource.api;

import java.util.Map;

import com.dbp.core.api.Resource;
import com.dbp.core.error.DBPApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.exception.ApplicationException;

/**
 * 
 * @author KH2281
 * @version 1.0 Interface for AccountTransactionsResource extends {@link Resource}
 *
 */

public interface AccountTransactionsResource extends Resource {

    /**
     * method to get the Account transactions
     * 
     * @param order
     *            order of the records like asc or desc
     * @param offset
     *            starting index of the returned records
     * @param limit
     *            ending index of the returned records
     * @param accountID
     *            ID of the account
     * @param transactionType
     *            transactionType of the records to be returned
     * @param sortBy
     *            sorting parameter like amount or transaction date
     * @param authToken
     * 			Authorization Token for MS
     * @param request 
     * @return array of JSON containing all the posted transactions
     * @throws ApplicationException
     * @throws Exception
     * 
     */
    Result getAccountTransactions(String order, String offset, String limit, String accountID, String transactionType,
            String sortBy,String authToken, DataControllerRequest request) throws ApplicationException, Exception;

    /**
     * method to get the Account Pending transactions
     *
     * @param accountID
     *            ID of the account
     * @param searchStartDate
     *            the startingDate of pending transactions
     * @param authToken
     *            jwtToken used to fetch pending transactions from T24
     * @return array of JSON containing all pending transactions
     * @throws ApplicationException
     * 
     */
    Result getAccountPendingTransactions(String accountID, String searchStartDate, DataControllerRequest request)
            throws ApplicationException;

    /**
     * method to get the Account Pending transactions
     * 
     * @param order
     *            order of the records like asc or desc
     * @param offset
     *            starting index of the returned records
     * @param limit
     *            ending index of the returned records
     * @param accountID
     *            ID of the account
     * @param transactionType
     *            transactionType of the records to be returned
     * @param sortBy
     *            sorting parameter like amount or transaction date
     * @param searchStartDate
     *            the startingDate of pending transactions
     * @param authToken
     *            jwtToken used to fetch pending transactions from T24
     * @return array of JSON containing all the approval rules
     * @throws ApplicationException
     * @throws Exception 
     * @throws DBPApplicationException 
     * 
     */
    Result getAccountPendingAndPostedTransactions(String order, String offset, String limit, String accountID,
            String transactionType, String sortBy, String searchStartDate, DataControllerRequest request,String authToken)
            throws ApplicationException, DBPApplicationException, Exception;

    Result getAccountPostedTransactionsPreview(String userName, String did, String offset, String limit,
            String accountId, DataControllerRequest request, String authToken) throws ApplicationException, Exception;

	Result searchAccountTransactions(Map<String, Object> inputParams, DataControllerRequest request,String authToken) throws Exception;

	Result getLoanScheduleTransactions(String order, String offset, String limit, String accountID,
			String transactionType, String sortBy, String searchStartDate, String isFutureRequired, DataControllerRequest request, String authToken)
			throws ApplicationException;

}
