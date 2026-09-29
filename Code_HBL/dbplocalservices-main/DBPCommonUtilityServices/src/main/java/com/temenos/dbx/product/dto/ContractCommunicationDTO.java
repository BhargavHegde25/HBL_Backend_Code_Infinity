package com.temenos.dbx.product.dto;

import com.dbp.core.api.DBPDTO;

public class ContractCommunicationDTO implements DBPDTO {

    /**
     * 
     */
    private static final long serialVersionUID = -4843759673842728759L;
    private String id;
    private String typeId;
    private String contractId;
    private String value;
    private String extension;
    private String phoneCountryCode;
    private String isPreferredContactMethod;
    private String preferredContactTime;
    private String createdby;
    private String createdts;
    private String modifiedby;
    private String lastmodifiedts;
    private String softdeleteflag;
    private String companyLegalUnit;

    public String getPhoneCountryCode() {
        return phoneCountryCode;
    }

    public void setPhoneCountryCode(String phoneCountryCode) {
        this.phoneCountryCode = phoneCountryCode;
    }

    public String getSoftdeleteflag() {
        return softdeleteflag;
    }

    public void setSoftdeleteflag(String softdeleteflag) {
        this.softdeleteflag = softdeleteflag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
    
    public String getCompanyLegalUnit() {
        return companyLegalUnit;
    }

    public void setCompanyLegalUnit(String companyLegalUnit) {
        this.companyLegalUnit = companyLegalUnit;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getIsPreferredContactMethod() {
        return isPreferredContactMethod;
    }

    public void setIsPreferredContactMethod(String isPreferredContactMethod) {
        this.isPreferredContactMethod = isPreferredContactMethod;
    }

    public String getPreferredContactTime() {
        return preferredContactTime;
    }

    public void setPreferredContactTime(String preferredContactTime) {
        this.preferredContactTime = preferredContactTime;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public String getCreatedts() {
        return createdts;
    }

    public void setCreatedts(String createdts) {
        this.createdts = createdts;
    }

    public String getModifiedby() {
        return modifiedby;
    }

    public void setModifiedby(String modifiedby) {
        this.modifiedby = modifiedby;
    }

    public String getLastmodifiedts() {
        return lastmodifiedts;
    }

    public void setLastmodifiedts(String lastmodifiedts) {
        this.lastmodifiedts = lastmodifiedts;
    }

}
