package com.temenos.dbx.product.approvalsframework.approvalsframeworkcommons.dto;

import java.util.List;

public class SignatoryGroupRuleDTO {

    String approvalruleId;
    String approvalruleName;
    String limitTypeId;
    String groupList;
    String groupRule;
    String pendingGroupList;
    String groupRuleValue;
    Integer isApproved;
    List<SignatoryGroupDTO> groupDTOList;

    public SignatoryGroupRuleDTO() {
    }

    public SignatoryGroupRuleDTO(String approvalruleId, String approvalruleName, String limitTypeId, String groupList, String groupRule, String pendingGroupList, String groupRuleValue, Integer isApproved) {
        this.approvalruleId = approvalruleId;
        this.approvalruleName = approvalruleName;
        this.limitTypeId = limitTypeId;
        this.groupList = groupList;
        this.groupRule = groupRule;
        this.pendingGroupList = pendingGroupList;
        this.groupRuleValue = groupRuleValue;
        this.isApproved = isApproved;
    }

    public String getApprovalruleId() {
        return approvalruleId;
    }

    public void setApprovalruleId(String approvalruleId) {
        this.approvalruleId = approvalruleId;
    }

    public String getApprovalruleName() {
        return approvalruleName;
    }

    public void setApprovalruleName(String approvalruleName) {
        this.approvalruleName = approvalruleName;
    }

    public String getLimitTypeId() {
        return limitTypeId;
    }

    public void setLimitTypeId(String limitTypeId) {
        this.limitTypeId = limitTypeId;
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

    public Integer getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Integer isApproved) {
        this.isApproved = isApproved;
    }

    public List<SignatoryGroupDTO> getGroupDTOList() {
        return groupDTOList;
    }

    public void setGroupDTOList(List<SignatoryGroupDTO> groupDTOList) {
        this.groupDTOList = groupDTOList;
    }
}
