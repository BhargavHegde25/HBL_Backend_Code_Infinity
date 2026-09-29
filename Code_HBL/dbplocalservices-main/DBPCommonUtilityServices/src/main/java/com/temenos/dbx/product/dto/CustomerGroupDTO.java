package com.temenos.dbx.product.dto;

import com.dbp.core.api.DBPDTO;

public class CustomerGroupDTO implements DBPDTO {

    /**
     * 
     */
    private static final long serialVersionUID = 333002552221660186L;
    private String customerId;
    private String groupId;
    private String createdBy;
    private String modifiedBy;
    private String coreCustomerId;
    private String contractId;
    private String companyLegalUnit;

    public String getCompanyLegalUnit() {
		return companyLegalUnit;
	}

	public void setCompanyLegalUnit(String companyLegalUnit) {
		this.companyLegalUnit = companyLegalUnit;
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

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

}
