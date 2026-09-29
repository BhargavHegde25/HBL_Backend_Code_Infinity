package com.temenos.dbx.product.approvalsframework.approvalsframeworkcommons.dto;

import java.util.Set;

public class ApprovalRuleDTO {
    private Double lowerLimit, upperLimit;
    private String approvalruleId, groupList, groupRule, pendingGroupList, groupRuleValue;
    private Set<String> approvers;
    private Integer receivedApprovals;
    private Boolean isApproved;
    private Integer isGroupRuleApproved;

    public ApprovalRuleDTO() {

    }

    public ApprovalRuleDTO(Double lowerLimit, Double upperLimit, String approvalruleId, String groupList, String groupRule, Set<String> approvers) {
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        this.approvalruleId = approvalruleId;
        this.groupList = groupList;
        this.groupRule = groupRule;
        this.approvers = approvers;
    }

    public ApprovalRuleDTO(String approvalruleId, String groupList, String pendingGroupList, String groupRuleValue, Set<String> approvers, Integer receivedApprovals, Integer isGroupRuleApproved) {
        this.approvalruleId = approvalruleId;
        this.groupList = groupList;
        this.pendingGroupList = pendingGroupList;
        this.groupRuleValue = groupRuleValue;
        this.approvers = approvers;
        this.receivedApprovals = receivedApprovals;
        this.isGroupRuleApproved = isGroupRuleApproved;
    }

    public Double getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(Double lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public Double getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(Double upperLimit) {
        this.upperLimit = upperLimit;
    }

    public String getApprovalruleId() {
        return approvalruleId;
    }

    public void setApprovalruleId(String approvalruleId) {
        this.approvalruleId = approvalruleId;
    }

    public String getGroupList() {
        return groupList;
    }

    public void setGroupList(String groupList) {
        this.groupList = groupList;
    }

    public String getGroupRule() {
        return groupRule;
    }

    public void setGroupRule(String groupRule) {
        this.groupRule = groupRule;
    }

    public Set<String> getApprovers() {
        return approvers;
    }

    public void setApprovers(Set<String> approvers) {
        this.approvers = approvers;
    }

    public String getPendingGroupList() {
        return pendingGroupList;
    }

    public void setPendingGroupList(String pendingGroupList) {
        this.pendingGroupList = pendingGroupList;
    }

    public String getGroupRuleValue() {
        return groupRuleValue;
    }

    public void setGroupRuleValue(String groupRuleValue) {
        this.groupRuleValue = groupRuleValue;
    }

    public Integer getReceivedApprovals() {
        return receivedApprovals;
    }

    public void setReceivedApprovals(Integer receivedApprovals) {
        this.receivedApprovals = receivedApprovals;
    }

    public Boolean getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }

    public Integer getIsGroupRuleApproved() {
        return isGroupRuleApproved;
    }

    public void setIsGroupRuleApproved(Integer isGroupRuleApproved) {
        this.isGroupRuleApproved = isGroupRuleApproved;
    }

    @Override
    public String toString() {
        return "ApprovalRuleDTO{" +
                "lowerLimit=" + lowerLimit +
                ", upperLimit=" + upperLimit +
                ", approvalruleId='" + approvalruleId + '\'' +
                ", groupList='" + groupList + '\'' +
                ", groupRule='" + groupRule + '\'' +
                ", pendingGroupList='" + pendingGroupList + '\'' +
                ", groupRuleValue='" + groupRuleValue + '\'' +
                ", approvers=" + approvers +
                ", receivedApprovals=" + receivedApprovals +
                '}';
    }
}
