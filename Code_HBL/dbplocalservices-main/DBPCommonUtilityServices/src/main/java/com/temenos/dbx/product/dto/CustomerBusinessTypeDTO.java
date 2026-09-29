package com.temenos.dbx.product.dto;

import com.dbp.core.api.DBPDTO;

public class CustomerBusinessTypeDTO implements DBPDTO {

    /**
     * 
     */
    private static final long serialVersionUID = 4073618217528338263L;
    private String customerId;
    private String businessTypeId;
    private String signatoryTypeId;
    private String createdBy;
    private String modifiedBy;

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(String businessTypeId) {
        this.businessTypeId = businessTypeId;
    }

    public String getSignatoryTypeId() {
        return signatoryTypeId;
    }

    public void setSignatoryTypeId(String signatoryTypeId) {
        this.signatoryTypeId = signatoryTypeId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

}
