package com.temenos.dbx.product.dto;

import com.dbp.core.api.DBPDTO;

public class OrganizationMembershipDTO implements DBPDTO {

    /**
     * 
     */
    private static final long serialVersionUID = 3377960275286527508L;

    private String id;
    private String organisationId;
    private String membershipId;
    private String taxId;

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(String organisationId) {
        this.organisationId = organisationId;
    }

    public String getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(String membershipId) {
        this.membershipId = membershipId;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
