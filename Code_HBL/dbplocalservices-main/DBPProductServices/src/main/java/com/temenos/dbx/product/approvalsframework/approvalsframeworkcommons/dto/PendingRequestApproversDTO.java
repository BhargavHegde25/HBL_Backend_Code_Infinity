package com.temenos.dbx.product.approvalsframework.approvalsframeworkcommons.dto;

import com.kony.dbx.objects.Customer;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.temenos.dbx.product.constants.Constants;

import java.util.List;
import java.util.Map;

public class PendingRequestApproversDTO {

    String requestId;
    String assocRequestId;
    String featureActionId;
    Integer isGroupMatrix;
    String status;
    Map<String, SignatoryGroupRuleDTO> pendingGroupRules;
    Map<String, UserRuleDTO> pendingUserRules;

    public PendingRequestApproversDTO() {
    }

    public PendingRequestApproversDTO(String requestId, String assocRequestId, String featureActionId, String status, Integer isGroupMatrix) {
        this.requestId = requestId;
        this.assocRequestId = assocRequestId;
        this.status = status;
        this.featureActionId = featureActionId;
        this.isGroupMatrix = isGroupMatrix;
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

    public String getFeatureActionId() {
        return featureActionId;
    }

    public void setFeatureActionId(String featureActionId) {
        this.featureActionId = featureActionId;
    }

    public Integer getIsGroupMatrix() {
        return isGroupMatrix;
    }

    public void setIsGroupMatrix(Integer isGroupMatrix) {
        this.isGroupMatrix = isGroupMatrix;
    }

    public Map<String, SignatoryGroupRuleDTO> getPendingGroupRules() {
        return pendingGroupRules;
    }

    public void setPendingGroupRules(Map<String, SignatoryGroupRuleDTO> pendingGroupRules) {
        this.pendingGroupRules = pendingGroupRules;
    }

    public Map<String, UserRuleDTO> getPendingUserRules() {
        return pendingUserRules;
    }

    public void setPendingUserRules(Map<String, UserRuleDTO> pendingUserRules) {
        this.pendingUserRules = pendingUserRules;
    }

    public Record getAsRecord() {
        Record record = new Record();
        record.addParam(Constants.REQUESTID, requestId);
        record.addParam(Constants.ASSOCREQUESTID, assocRequestId);
        record.addParam(Constants.ISGROUPMATRIX, isGroupMatrix.toString());
        if (isGroupMatrix == 1) {
            Dataset pendingGroupRulesDataset = new Dataset("pendingGroupRules");
            for (Map.Entry<String, SignatoryGroupRuleDTO> groupRuleDTOEntry : pendingGroupRules.entrySet()) {
                SignatoryGroupRuleDTO signatoryGroupRuleDTO = groupRuleDTOEntry.getValue();
                Record groupRuleRecord = new Record();
                groupRuleRecord.addParam(Constants.GROUPLIST, signatoryGroupRuleDTO.getGroupList());
                groupRuleRecord.addParam(Constants.GROUPRULE, signatoryGroupRuleDTO.getGroupRule());
                groupRuleRecord.addParam(Constants.PENDINGGROUPLIST, signatoryGroupRuleDTO.getPendingGroupList());
                groupRuleRecord.addParam(Constants.GROUPRULEVALUE, signatoryGroupRuleDTO.getGroupRuleValue());
                groupRuleRecord.addParam(Constants.LIMITTYPEID, signatoryGroupRuleDTO.getLimitTypeId());
                Dataset signatoryGroupsDataset = new Dataset("pendingGroupDetails");
                for (SignatoryGroupDTO signatoryGroupDTO : signatoryGroupRuleDTO.getGroupDTOList()) {
                    Record groupRecord = new Record();
                    groupRecord.addParam(Constants.SIGNATORYGROUPID, signatoryGroupDTO.getSignatoryGroupId());
                    groupRecord.addParam(Constants.SIGNATORYGROUPNAME, signatoryGroupDTO.getSignatoryGroupName());
                    Dataset signatoryApproversDataset = new Dataset(Constants.SIGNATORYAPPROVERS);
                    for (ApproverDTO customer : signatoryGroupDTO.getSignatoryApprovers()) {
                        Record customerRecord = new Record();
                        customerRecord.addParam(Constants.CUSTOMERID, customer.getCustomerId());
                        customerRecord.addParam(Constants.FirstName, customer.getFirstName());
                        customerRecord.addParam(Constants.LastName, customer.getLastName());
                        customerRecord.addParam(Constants.ROLE, customer.getRoleName());
                        customerRecord.addParam(Constants.USERNAME, customer.getUserName());
                        signatoryApproversDataset.addRecord(customerRecord);
                    }
                    groupRecord.addDataset(signatoryApproversDataset);
                    signatoryGroupsDataset.addRecord(groupRecord);
                }
                groupRuleRecord.addDataset(signatoryGroupsDataset);
                pendingGroupRulesDataset.addRecord(groupRuleRecord);
            }
            record.addDataset(pendingGroupRulesDataset);
        } else {
            Dataset pendingApproversDataset = new Dataset("pendingApproverRules");
            for (Map.Entry<String, UserRuleDTO> userRuleDTOEntry : pendingUserRules.entrySet()) {
                UserRuleDTO userRuleDTO = userRuleDTOEntry.getValue();
                Record userRuleRecord = new Record();
                userRuleRecord.addParam(Constants.LIMITTYPEID, userRuleDTO.getLimitTypeId());
                userRuleRecord.addParam(Constants.APPROVALRULEID, userRuleDTO.getApprovalruleId());
                userRuleRecord.addParam(Constants.APPROVALRULENAME, userRuleDTO.getApprovalruleName());
                userRuleRecord.addParam(Constants.RECEIVEDAPPROVALS, userRuleDTO.getReceivedApprovals().toString());
                Dataset approversDataset = new Dataset(Constants.APPROVERS);
                for (ApproverDTO customer : userRuleDTO.getApprovers()) {
                    Record customerRecord = new Record();
                    customerRecord.addParam(Constants.CUSTOMERID, customer.getCustomerId());
                    customerRecord.addParam(Constants.FirstName, customer.getFirstName());
                    customerRecord.addParam(Constants.LastName, customer.getLastName());
                    customerRecord.addParam(Constants.ROLE, customer.getRoleName());
                    customerRecord.addParam(Constants.USERNAME, customer.getUserName());
                    approversDataset.addRecord(customerRecord);
                }
                userRuleRecord.addDataset(approversDataset);
                pendingApproversDataset.addRecord(userRuleRecord);
            }
            record.addDataset(pendingApproversDataset);
        }
        return record;
    }


    @Override
    public String toString() {
        return "PendingRequestApproversDTO{" +
                "requestId='" + requestId + '\'' +
                ", assocRequestId='" + assocRequestId + '\'' +
                ", featureActionId='" + featureActionId + '\'' +
                ", isGroupMatrix='" + isGroupMatrix + '\'' +
                ", pendingGroupRules=" + pendingGroupRules +
                ", pendingUserRules=" + pendingUserRules +
                '}';
    }
}
