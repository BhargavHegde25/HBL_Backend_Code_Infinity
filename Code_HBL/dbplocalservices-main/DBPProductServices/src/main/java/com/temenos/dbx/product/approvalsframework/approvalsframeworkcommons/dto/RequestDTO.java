package com.temenos.dbx.product.approvalsframework.approvalsframeworkcommons.dto;

import com.konylabs.middleware.dataobject.Record;
import com.temenos.dbx.product.constants.Constants;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class RequestDTO {
    String requestId;
    String assocRequestId;
    String transactionId;
    String featureActionId;
    String coreCustomerId;
    String contractId;
    String accountId;
    String status;
    Integer isGroupMatrix;
    Integer requiredSets;
    Integer receivedSets;
    String createdBy;
    String createdTs;
    String additionalMeta;
    String comments;
    /**
     * @note Map contains the mentioned information: <matrixId, matrixRuleDTO>
     */
    Map<String, ApprovalRuleDTO> matrixInfo;
    private Set<String> actingSignatoryGroups;

    public RequestDTO() {

    }

    public RequestDTO(String requestId, String assocRequestId, String transactionId, String featureActionId, String coreCustomerId, String contractId, String accountId, String status, String createdBy, String createdTs, Integer isGroupMatrix, Integer requiredSets, Integer receivedSets, String additionalMeta) {
        this.requestId = requestId;
        this.assocRequestId = assocRequestId;
        this.transactionId = transactionId;
        this.featureActionId = featureActionId;
        this.coreCustomerId = coreCustomerId;
        this.contractId = contractId;
        this.accountId = accountId;
        this.status = status;
        this.createdBy = createdBy;
        this.createdTs = createdTs;
        this.isGroupMatrix = isGroupMatrix;
        this.requiredSets = requiredSets;
        this.receivedSets = receivedSets;
        this.additionalMeta = additionalMeta;
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

    public String getCoreCustomerId() {
        return coreCustomerId;
    }

    public void setCoreCustomerId(String coreCustomerId) {
        this.coreCustomerId = coreCustomerId;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getIsGroupMatrix() {
        return isGroupMatrix;
    }

    public void setIsGroupMatrix(Integer isGroupMatrix) {
        this.isGroupMatrix = isGroupMatrix;
    }

    public Integer getRequiredSets() {
        return requiredSets;
    }

    public void setRequiredSets(Integer requiredSets) {
        this.requiredSets = requiredSets;
    }

    public Integer getReceivedSets() {
        return receivedSets;
    }

    public void setReceivedSets(Integer receivedSets) {
        this.receivedSets = receivedSets;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedTs() {
        return createdTs;
    }

    public void setCreatedTs(String createdTs) {
        this.createdTs = createdTs;
    }

    public String getAdditionalMeta() {
        return additionalMeta;
    }

    public void setAdditionalMeta(String additionalMeta) {
        this.additionalMeta = additionalMeta;
    }

    public Map<String, ApprovalRuleDTO> getMatrixInfo() {
        return matrixInfo;
    }

    public void setMatrixInfo(Map<String, ApprovalRuleDTO> matrixInfo) {
        this.matrixInfo = matrixInfo;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Set<String> getActingSignatoryGroups() {
        return actingSignatoryGroups;
    }

    public void setActingSignatoryGroups(Set<String> actingSignatoryGroups) {
        this.actingSignatoryGroups = actingSignatoryGroups;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestDTO that = (RequestDTO) o;
        return getRequestId().equals(that.getRequestId()) && getAssocRequestId().equals(that.getAssocRequestId()) && getTransactionId().equals(that.getTransactionId()) && getFeatureActionId().equals(that.getFeatureActionId()) && getCoreCustomerId().equals(that.getCoreCustomerId()) && getContractId().equals(that.getContractId()) && Objects.equals(getAccountId(), that.getAccountId()) && getStatus().equals(that.getStatus()) && getIsGroupMatrix().equals(that.getIsGroupMatrix()) && getRequiredSets().equals(that.getRequiredSets()) && getReceivedSets().equals(that.getReceivedSets()) && getCreatedBy().equals(that.getCreatedBy()) && getCreatedTs().equals(that.getCreatedTs()) && getAdditionalMeta().equals(that.getAdditionalMeta());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRequestId(), getAssocRequestId(), getTransactionId(), getFeatureActionId(), getCoreCustomerId(), getContractId(), getAccountId(), getStatus(), getIsGroupMatrix(), getRequiredSets(), getReceivedSets(), getCreatedBy(), getCreatedTs(), getAdditionalMeta());
    }

    public Record getAsRecord() {
        Record record = new Record();
        record.addParam(Constants.REQUESTID, requestId);
        record.addParam(Constants.ASSOCREQUESTID, assocRequestId);
        record.addParam(Constants.CONTRACTID, contractId);
        record.addParam(Constants.CORECUSTOMERID, coreCustomerId);
        record.addParam(Constants.STATUS, status);
        record.addParam(Constants.TRANSACTIONID, transactionId);
        record.addParam(Constants.CONFIRMATION_NUMBER, transactionId);
        record.addParam(Constants.CREATED_BY, createdBy);
        record.addParam(Constants.CREATEDTS, createdTs);
        return record;
    }
}
