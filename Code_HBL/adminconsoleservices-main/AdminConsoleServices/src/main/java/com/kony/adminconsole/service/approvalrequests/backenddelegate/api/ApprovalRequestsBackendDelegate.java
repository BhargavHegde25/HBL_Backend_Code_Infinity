package com.kony.adminconsole.service.approvalrequests.backenddelegate.api;

import com.dbp.core.api.BackendDelegate;
import org.json.JSONArray;

import java.util.Map;

public interface ApprovalRequestsBackendDelegate extends BackendDelegate {

    /**
     * @param reqParams
     * @param reqHeaders
     * @return a JSONArray consisting of all the records as per the defined OData request parameters
     * @description performs basic GET operation on dbxdb.approvalrequests table
     * @author Sourav Ray Chaudhuri
     */
    public JSONArray getApprovalRequestDBData(Map<String, Object> reqParams, Map<String, Object> reqHeaders);

    /**
     * @param reqParams
     * @param reqHeaders
     * @return a JSONArray consisting of all the records as per the defined proc
     * @description performs the dbxdb_fetch_approvalrequests_counts_proc to return the counts of
     * pendingRequests, pendingApprovals, requestHistory and approvalHistory
     * for the logged in user
     * @author Sourav Ray Chaudhuri
     */
    public JSONArray getApprovalRequestCountsDBData(Map<String, Object> reqParams, Map<String, Object> reqHeaders);

    /**
     * @param reqParams
     * @param reqHeaders
     * @return a JSONArray consisting of all the records containing the list of pending approvals for the given user
     * @description performs the fetch_pending_approvals_proc to return the records of requests
     * pending for approval by the logged in user
     * @author Sourav Ray Chaudhuri
     */
    public JSONArray getUserPendingApprovalsDBData(Map<String, Object> reqParams, Map<String, Object> reqHeaders);

    /**
     * @param reqParams
     * @param reqHeaders
     * @return a JSONArray consisting of all the records acted upon by the given user
     * @description performs the fetch_approval_history_proc to return the records of requests
     * acted upon by the logged in user
     * @author Sourav Ray Chaudhuri
     */
    public JSONArray getUserApprovalHistoryDBData(Map<String, Object> reqParams, Map<String, Object> reqHeaders);
}
