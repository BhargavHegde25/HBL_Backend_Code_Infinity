package com.temenos.dbx.product.dto;

import com.dbp.core.api.DBPDTO;

public class OrganisationDTO implements DBPDTO {

    /**
     * 
     */
    private static final long serialVersionUID = 8248134925276409516L;
    private String id;
    private String typeId;
    private String name;
    private String description;
    private String businessTypeId;
    private String statusId;
    private String faxId;
    private String createdby;
    private String createdts;
    private String rejectedby;
    private String rejectedts;
    private String rejectedReason;

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

    public String getRejectedby() {
        return rejectedby;
    }

    public void setRejectedby(String rejectedby) {
        this.rejectedby = rejectedby;
    }

    public String getRejectedts() {
        return rejectedts;
    }

    public void setRejectedts(String rejectedts) {
        this.rejectedts = rejectedts;
    }

    private BusinessTypeDTO businessType;

    public BusinessTypeDTO getBusinessType() {
        return businessType;
    }

    public void setBusinessType(BusinessTypeDTO businessType) {
        this.businessType = businessType;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(String businessTypeId) {
        this.businessTypeId = businessTypeId;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getFaxId() {
        return faxId;
    }

    public void setFaxId(String faxId) {
        this.faxId = faxId;
    }

    public String getRejectedReason() {
        return rejectedReason;
    }

    public void setRejectedReason(String rejectedReason) {
        this.rejectedReason = rejectedReason;
    }
}
