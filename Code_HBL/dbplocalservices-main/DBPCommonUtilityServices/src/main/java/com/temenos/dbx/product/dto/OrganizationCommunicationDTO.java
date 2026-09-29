package com.temenos.dbx.product.dto;

import com.dbp.core.api.DBPDTO;

public class OrganizationCommunicationDTO implements DBPDTO {

    /**
     * 
     */
    private static final long serialVersionUID = -8247522835338535195L;
    private String id;
    private String typeId;
    private String organizationId;
    private String sequence;
    private String value;
    private String extension;
    private String description;
    private String isPrefferedContactMethod;
    private String prefferedContactTime;
    private String createdBy;
    private String modifiedBy;

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

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsPrefferedContactMethod() {
        return isPrefferedContactMethod;
    }

    public void setIsPrefferedContactMethod(String isPrefferedContactMethod) {
        this.isPrefferedContactMethod = isPrefferedContactMethod;
    }

    public String getPrefferedContactTime() {
        return prefferedContactTime;
    }

    public void setPrefferedContactTime(String prefferedContactTime) {
        this.prefferedContactTime = prefferedContactTime;
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
