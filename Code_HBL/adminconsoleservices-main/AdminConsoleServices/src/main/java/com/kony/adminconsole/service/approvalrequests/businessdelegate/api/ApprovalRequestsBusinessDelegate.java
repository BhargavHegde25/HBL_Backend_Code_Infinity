package com.kony.adminconsole.service.approvalrequests.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.kony.adminconsole.dto.DashboardCountsBean;
import com.kony.adminconsole.dto.RequestBean;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ApprovalRequestsBusinessDelegate extends BusinessDelegate {

    /**
     * @param permissionSet  - a set of all the permissions enabled for the user
     * @param loggedInUserId
     * @return DashboardCountsBean
     * @description - returns a DashboardCountsBean consisting of the individual counts of
     * pendingRequests, pendingApprovals, requestHistory and approvalHistory
     * @author Sourav Ray Chaudhuri
     */
    public DashboardCountsBean getApprovalDashboardCounts(Set<String> permissionSet, String loggedInUserId);

    /**
     * @param loggedInUserId
     * @return List<RequestBean>
     * @description - returns a list of RequestBean's, consisting of all the requests pending for the given user_id
     * @author Sourav Ray Chaudhuri
     */
    public List<RequestBean> getAllPendingRequests(String loggedInUserId);

    /**
     * @param permissionSet  - a set of all the permissions enabled for the user
     * @param loggedInUserId
     * @param filterParams
     * @return List<RequestBean>
     * @description - returns a list of RequestBean's, consisting of all the pending requests,
     * which could be approved by the given user_id
     * @author Sourav Ray Chaudhuri
     */
    public List<RequestBean> getAllPendingApprovals(Set<String> permissionSet, String loggedInUserId, Map<String, Object> filterParams);

    /**
     * @param loggedInUserId
     * @return List<RequestBean>
     * @description - returns a list of RequestBean's, consisting of all the requests created by the user_id
     * @author Sourav Ray Chaudhuri
     */
    public List<RequestBean> getAllRequestsHistory(String loggedInUserId);

    /**
     * @param loggedInUserId
     * @param filterParams
     * @return List<RequestBean>
     * @description - returns a list of RequestBean's, consisting of all the requests acted upon by the user_id
     * @author Sourav Ray Chaudhuri
     */
    public List<RequestBean> getAllApprovalsHistory(String loggedInUserId, Map<String, Object> filterParams);

}
