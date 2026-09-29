package com.temenos.dbx.product.approvalsframework.approvalsframeworkcommons.dto;

import com.kony.dbx.objects.Customer;

import java.util.List;

public class SignatoryGroupDTO {
    String signatoryGroupId;
    String signatoryGroupName;
    String signatoryGroupDescription;
    List<ApproverDTO> signatoryApprovers;

    public SignatoryGroupDTO() {
    }

    public SignatoryGroupDTO(String signatoryGroupId, String signatoryGroupName, String signatoryGroupDescription) {
        this.signatoryGroupId = signatoryGroupId;
        this.signatoryGroupName = signatoryGroupName;
        this.signatoryGroupDescription = signatoryGroupDescription;
    }

    public String getSignatoryGroupId() {
        return signatoryGroupId;
    }

    public void setSignatoryGroupId(String signatoryGroupId) {
        this.signatoryGroupId = signatoryGroupId;
    }

    public String getSignatoryGroupName() {
        return signatoryGroupName;
    }

    public void setSignatoryGroupName(String signatoryGroupName) {
        this.signatoryGroupName = signatoryGroupName;
    }

    public String getSignatoryGroupDescription() {
        return signatoryGroupDescription;
    }

    public void setSignatoryGroupDescription(String signatoryGroupDescription) {
        this.signatoryGroupDescription = signatoryGroupDescription;
    }

    public List<ApproverDTO> getSignatoryApprovers() {
        return signatoryApprovers;
    }

    public void setSignatoryApprovers(List<ApproverDTO> signatoryApprovers) {
        this.signatoryApprovers = signatoryApprovers;
    }
}
