package com.temenos.dbx.product.approvalsframework.approvalsfactory.api;

import com.kony.dbp.exception.ApplicationException;

public interface ApprovalsFrameworkDelegateFactory {

    public void executeOperationBeforeApproval() throws ApplicationException;
    public void executeOperationAfterApproval() throws ApplicationException;
    public void fetchFeatureDataWithApprovalInfo() throws ApplicationException;
    public void approveOperation() throws ApplicationException;
    public void rejectOperation() throws ApplicationException;
    public void withdrawOperation() throws ApplicationException;

}
