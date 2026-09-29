package com.kony.adminconsole.service.approvalrequests.resource.api;

import java.io.IOException;

import com.dbp.core.api.Resource;
import com.kony.adminconsole.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.exceptions.MiddlewareException;
import com.konylabs.middleware.registry.AppRegistryException;

public interface ApprovalRequestsResource extends Resource {

    /**
     * @description returns a Result object, containing a consolidated requests and approvals counts (integration layer) for the logged in user
     * @param dcRequest
     * @return Result - dashboardCountsResult
     *
     * @author Sourav Ray Chaudhuri
     */
    public Result getDashboardCounts(DataControllerRequest dcRequest) throws MiddlewareException, AppRegistryException, ApplicationException, IOException ;

    /**
     * @description returns a Result object, containing all the pending requests created by the logged in user
     * @param dcRequest
     * @return Result - dashboardCountsResult
     *
     * @author Sourav Ray Chaudhuri
     */
    public Result getAllPendingRequests(DataControllerRequest dcRequest);

    /**
     * @description returns a Result object, containing all the requests pending for approval from the logged in user
     * @param dcRequest
     * @return Result - dashboardCountsResult
     *
     * @author Sourav Ray Chaudhuri
     */
    public Result getAllPendingApprovals(DataControllerRequest dcRequest)  throws MiddlewareException, AppRegistryException, ApplicationException, IOException ;

    /**
     * @description returns a Result object, containing all the requests created by the logged in user
     * @param dcRequest
     * @return Result - dashboardCountsResult
     *
     * @author Sourav Ray Chaudhuri
     */
    public Result getAllRequestHistory(DataControllerRequest dcRequest);

    /**
     * @description returns a Result object, containing all the requests acted upon by the logged in user
     * @param dcRequest
     * @return Result - dashboardCountsResult
     *
     * @author Sourav Ray Chaudhuri
     */
    public Result getAllApprovalHistory(DataControllerRequest dcRequest);

}
