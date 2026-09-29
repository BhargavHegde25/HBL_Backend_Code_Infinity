package com.kony.adminconsole.service.contract.resource.api;

import com.dbp.core.api.Resource;
import com.kony.adminconsole.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;


/**
 * @author kaushik.mondal
 *
 */

public interface ApprovalMatrixManageResource extends Resource {
	
	public Result updateApprovalMatrixStatus(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws ApplicationException;
	
	public Result isApprovalMatrixDisabled(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws ApplicationException;
	
	public Result getApprovalMatrix(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws ApplicationException;

}
