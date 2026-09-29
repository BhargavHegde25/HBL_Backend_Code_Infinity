package com.temenos.dbx.party.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * 
 * @author KH2627
 * @version 1.0 Interface for AuditActivityUpdateResource extends {@link Resource}
 *
 */

public interface AuditActivityUpdateResource extends Resource {

    /**
     * method to update audit activity records
     * 
     * @param methodID
     *            operationName
     * @param inputArray
     *            input parameters
     * @param request
     *            DataControllerRequest
     * @param response
     *            DataControllerResponse
     * @return Result containing the list of approvers for the account and actionId
     * 
     */
	
	public Result updatePartyAuditLogsWithCustomerInformation(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse);
}
