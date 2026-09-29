package com.temenos.dbx.product.dto;

import com.dbp.core.api.DBPDTO;

public class OrganizationsFeatureActionsDTO implements DBPDTO {

    /**
     * 
     */
    private static final long serialVersionUID = -2788485287007007600L;
    private String features;
    private String organisationType;
    private String organisationId;

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getOrganisationType() {
        return organisationType;
    }

    public void setOrganisationType(String organisationType) {
        this.organisationType = organisationType;
    }

    public String getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(String organisationId) {
        this.organisationId = organisationId;
    }

}
