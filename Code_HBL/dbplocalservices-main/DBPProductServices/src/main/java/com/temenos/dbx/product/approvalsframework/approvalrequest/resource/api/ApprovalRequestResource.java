package com.temenos.dbx.product.approvalsframework.approvalrequest.resource.api;

import com.dbp.core.api.Resource;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.approvalservices.dto.ApprovalRequestDTO;
import com.temenos.dbx.product.approvalservices.dto.BBRequestDTO;

import java.util.List;
import java.util.Map;

public interface ApprovalRequestResource extends Resource {

    /**
     * @description this service determines whether approval is required for the particular feature action.
     * returns the "status" identifier as a response. If status is Pending -> request has been raised and is in a pending state.
     * If status is Sent -> there was no need for raising a request, and the feature action can proceed for straight through processing
     * If status is Approved -> this means that while validating, the requester has self-approved the request, and can proceed for further processing
     * @param requestMap
     * @param dcRequest
     * @return
     * @throws ApplicationException
     */
    public Result validateForApprovals(Map<String, Object> requestMap, DataControllerRequest dcRequest) throws ApplicationException, Exception;

    public Result approveRequest(Map<String, Object> requestMap, DataControllerRequest dcRequest) throws ApplicationException, Exception;

    public Result rejectRequest(Map<String, Object> requestMap, DataControllerRequest dcRequest) throws ApplicationException, Exception;

    public Result withdrawRequest(Map<String, Object> requestMap, DataControllerRequest dcRequest) throws ApplicationException, Exception;

    public Result fetchAllPendingRequests(Map<String, Object> requestMap, DataControllerRequest dcRequest) throws ApplicationException;

    public Result fetchAllPendingApprovals(Map<String, Object> requestMap, DataControllerRequest dcRequest) throws ApplicationException;

    public Result fetchAllRequestHistory(Map<String, Object> requestMap, DataControllerRequest dcRequest) throws ApplicationException;

    public Result fetchAllApprovalHistory(Map<String, Object> requestMap, DataControllerRequest dcRequest) throws ApplicationException;

    public Result fetchApprovalHistoryInformation(Map<String, Object> requestMap, DataControllerRequest dcRequest) throws ApplicationException;

    public Result fetchApprovalRequestsCounts(Map<String, Object> requestMap, DataControllerRequest dcRequest) throws ApplicationException;

    public Result fetchRequestsWithApprovalInfo(Map<String, Object> requestMap, DataControllerRequest dcRequest) throws ApplicationException;

    public Result fetchRequestHistory(Map<String, Object> requestMap, DataControllerRequest dcRequest) throws ApplicationException;

	public List<ApprovalRequestDTO> fetchAllRequests(List<BBRequestDTO> requests, DataControllerRequest dcr);
    
}
