package com.temenos.dbx.product.approvalsframework.approvalsframeworkcommons.dto;

import com.kony.dbx.objects.Customer;
import com.temenos.dbx.product.commons.dto.CustomerDTO;

import java.util.List;

public class UserRuleDTO {
    String approvalruleId;
    String approvalruleName;
    String limitTypeId;
    Integer receivedApprovals;
    List<ApproverDTO> approvers;

    public UserRuleDTO() {
    }

    public UserRuleDTO(String approvalruleId, String approvalruleName, String limitTypeId, Integer receivedApprovals) {
        this.approvalruleId = approvalruleId;
        this.approvalruleName = approvalruleName;
        this.limitTypeId = limitTypeId;
        this.receivedApprovals = receivedApprovals;
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

    public Integer getReceivedApprovals() {
        return receivedApprovals;
    }

    public void setReceivedApprovals(Integer receivedApprovals) {
        this.receivedApprovals = receivedApprovals;
    }

    public List<ApproverDTO> getApprovers() {
        return approvers;
    }

    public void setApprovers(List<ApproverDTO> approvers) {
        this.approvers = approvers;
    }
}
