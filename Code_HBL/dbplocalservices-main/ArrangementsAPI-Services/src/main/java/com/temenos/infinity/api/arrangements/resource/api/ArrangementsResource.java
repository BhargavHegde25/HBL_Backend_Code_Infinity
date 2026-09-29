package com.temenos.infinity.api.arrangements.resource.api;

import com.dbp.core.api.Resource;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.exception.ApplicationException;

/**
 * 
 * @author smugesh
 * @version 1.0 Interface for AccountsResource extends {@link Resource}
 *
 */

public interface ArrangementsResource extends Resource {

    /**
     * method to get the Arrangement Accounts
     * 
     * @param methodID
     *            operationName
     * @param inputArray
     *            input parameters
     * @param request
     *            DataControllerRequest
     * @param response
     *            DataControllerResponse
     * @return array of JSON containing all the approval rules
     * 
     */
    Result getArrangementAccounts(String backendUserId, String customerType, String customerID, String productLineId,
            String accountID, String CompanyId, DataControllerRequest request,String authToken) throws ApplicationException;

    // Overview account
    Result getAccountOverview(String backendUserId, String customerType, String customerID, String productLineId,
            String accountID, DataControllerRequest request,String authToken) throws ApplicationException;

    Result getArrangementPreviewAccounts(String did, String userName, String backendUserId, String customerType,
            String productLine, String customerID, DataControllerRequest request,String authToken) throws ApplicationException;

    Result getUserDetailsFromDBX(String userName, DataControllerRequest request) throws ApplicationException;

	Result getAccountDetailsForCombinedStatements(String accountID, String customerType, String authToken, String companyId, DataControllerRequest request) throws Exception;
	
	Result getSimulationResults(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws ApplicationException,Exception;
	
	Result createAndGetPayOffSimulatedResults(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws ApplicationException,Exception;

	
	
	public Result getArrangementAccountsForAdmin(String backendUserId, String customerType, String customerID,
            String productLineId, String Account_id, String CompanyId, DataControllerRequest request, String authToken)
            throws ApplicationException;

	public Result getCoreCustomerIdsAndAccounts(DataControllerRequest request, String customerId) throws ApplicationException;
	public Result updateCoreCustomerFavoriteStatus(DataControllerRequest request, String customerId) throws ApplicationException;

	Result getAccountLevelPermissionsforCoreCustomerIds(DataControllerRequest request, String customerId) throws HttpCallException, ApplicationException;

	Result getLatestBalances(String Account_id,  String authToken)
			throws ApplicationException;
}
