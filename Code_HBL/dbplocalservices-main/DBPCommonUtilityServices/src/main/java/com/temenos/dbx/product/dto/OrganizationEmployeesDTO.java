package com.temenos.dbx.product.dto;

import com.dbp.core.api.DBPDTO;

public class OrganizationEmployeesDTO implements DBPDTO {

    /**
     * 
     */
    private static final long serialVersionUID = -2594089263295526665L;
    private String id;
    private String organizationId;
    private String customerId;
    private String isAdmin;
    private String isOwner;
    private String isAuthSignatory;
    private String createdBy;
    private String modifiedBy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(String isOwner) {
        this.isOwner = isOwner;
    }

    public String getIsAuthSignatory() {
        return isAuthSignatory;
    }

    public void setIsAuthSignatory(String isAuthSignatory) {
        this.isAuthSignatory = isAuthSignatory;
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
