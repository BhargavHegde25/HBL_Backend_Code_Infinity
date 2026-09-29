package com.temenos.dbx.product.approvalsframework.approvalsframeworkcommons.dto;

import com.kony.dbx.objects.Customer;
import com.konylabs.middleware.dataobject.Record;
import com.temenos.dbx.product.constants.Constants;

public class RequestHistoryDTO {

    String approvalId;
    String requestId;
    String assocRequestId;
    String transactionId;
    String featureActionId;
    String requestStatus;
    String action;
    String comments;
    String companyId;
    String requestActedBy;
    String groupName;
    String requestActionts;
    Customer customerInfo;

    public RequestHistoryDTO() {
    }

    public RequestHistoryDTO(String approvalId, String requestId, String assocRequestId, String transactionId, String featureActionId, String requestStatus, String action, String comments, String companyId, String requestActedBy, String groupName, String requestActionts, Customer customerInfo) {
        this.approvalId = approvalId;
        this.requestId = requestId;
        this.assocRequestId = assocRequestId;
        this.transactionId = transactionId;
        this.featureActionId = featureActionId;
        this.requestStatus = requestStatus;
        this.action = action;
        this.comments = comments;
        this.companyId = companyId;
        this.requestActedBy = requestActedBy;
        this.groupName = groupName;
        this.requestActionts = requestActionts;
        this.customerInfo = customerInfo;
    }

    public String getApprovalId() {
        return approvalId;
    }

    public void setApprovalId(String approvalId) {
        this.approvalId = approvalId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getAssocRequestId() {
        return assocRequestId;
    }

    public void setAssocRequestId(String assocRequestId) {
        this.assocRequestId = assocRequestId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getFeatureActionId() {
        return featureActionId;
    }

    public void setFeatureActionId(String featureActionId) {
        this.featureActionId = featureActionId;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getRequestActedBy() {
        return requestActedBy;
    }

    public void setRequestActedBy(String requestActedBy) {
        this.requestActedBy = requestActedBy;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getRequestActionts() {
        return requestActionts;
    }

    public void setRequestActionts(String requestActionts) {
        this.requestActionts = requestActionts;
    }

    public Customer getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(Customer customerInfo) {
        this.customerInfo = customerInfo;
    }

    public Record getAsRecord(){
        Record record = new Record();
        record.addParam(Constants.REQUESTID, requestId);
        record.addParam(Constants.ASSOCREQUESTID, assocRequestId);
        record.addParam(Constants.APPROVALID, approvalId);
        record.addParam(Constants.STATUS, requestStatus);
        record.addParam(Constants.ACTION, action);
        record.addParam(Constants.COMMENTS, comments);
        record.addParam(Constants.ACTIONTS, requestActionts);
        record.addParam(Constants.COMPANYID, companyId);
        record.addParam(Constants.USERNAME, customerInfo.getFirstName() + " " + customerInfo.getLastName());
        record.addParam(Constants.ROLE, customerInfo.getRole());
        return record;
    }
}
