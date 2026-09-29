package com.temenos.dbx.product.approvalsframework.approvalmatrix.resource.api;

import com.dbp.core.api.Resource;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

import java.util.Map;

public interface ApprovalMatrixResource extends Resource {

    /**
     * @param requestMap
     * @param dcRequest
     * @return
     * @throws ApplicationException
     */
    public Result fetchApprovalMatrix(Map<String, Object> requestMap, DataControllerRequest dcRequest) throws ApplicationException;

    /**
     * @param requestMap
     * @param dcRequest
     * @return
     * @throws ApplicationException
     */
    public Result createOrUpdateApprovalRule(Map<String, Object> requestMap, DataControllerRequest dcRequest) throws ApplicationException;

    /**
     * @param requestMap
     * @param dcRequest
     * @return
     * @throws ApplicationException
     */
    public Result deleteApprovalRule(Map<String, Object> requestMap, DataControllerRequest dcRequest) throws ApplicationException;

    /**
     * @param requestMap
     * @param dcRequest
     * @return
     * @throws ApplicationException
     */
    public Result updateApprovalMode(Map<String, Object> requestMap, DataControllerRequest dcRequest) throws ApplicationException;

    /**
     * @param requestMap
     * @param dcRequest
     * @return
     * @throws ApplicationException
     */
    public Result fetchApprovalMode(Map<String, Object> requestMap, DataControllerRequest dcRequest) throws ApplicationException;

    /**
     * @param requestMap
     * @param dcRequest
     * @return
     * @throws ApplicationException
     */
    public Result fetchApprovalMatrixStatus(Map<String, Object> requestMap, DataControllerRequest dcRequest) throws ApplicationException;

}
