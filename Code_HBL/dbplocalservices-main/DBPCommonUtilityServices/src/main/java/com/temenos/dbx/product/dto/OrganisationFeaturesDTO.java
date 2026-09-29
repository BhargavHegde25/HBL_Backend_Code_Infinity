package com.temenos.dbx.product.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class OrganisationFeaturesDTO implements DBPDTO {

    /**
     * 
     */
    private static final long serialVersionUID = 2488937589763086123L;

    private String  id;
    private String  organisationId;
    private String  featureId;
    private String  featureStatus;
    private String  createdts;
    private String  lastmodifiedts;
    private String  synctimestamp;
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * @return the organisationId
     */
    public String getOrganisationId() {
        return organisationId;
    }
    /**
     * @param organisationId the organisationId to set
     */
    public void setOrganisationId(String organisationId) {
        this.organisationId = organisationId;
    }
    /**
     * @return the featureId
     */
    public String getFeatureId() {
        return featureId;
    }
    /**
     * @param featureId the featureId to set
     */
    public void setFeatureId(String featureId) {
        this.featureId = featureId;
    }
    /**
     * @return the featureStatus
     */
    public String getFeatureStatus() {
        return featureStatus;
    }
    /**
     * @param featureStatus the featureStatus to set
     */
    public void setFeatureStatus(String featureStatus) {
        this.featureStatus = featureStatus;
    }
    /**
     * @return the createdts
     */
    public String getCreatedts() {
        return createdts;
    }
    /**
     * @param createdts the createdts to set
     */
    public void setCreatedts(String createdts) {
        this.createdts = createdts;
    }
    /**
     * @return the lastmodifiedts
     */
    public String getLastmodifiedts() {
        return lastmodifiedts;
    }
    /**
     * @param lastmodifiedts the lastmodifiedts to set
     */
    public void setLastmodifiedts(String lastmodifiedts) {
        this.lastmodifiedts = lastmodifiedts;
    }
    /**
     * @return the synctimestamp
     */
    public String getSynctimestamp() {
        return synctimestamp;
    }
    /**
     * @param synctimestamp the synctimestamp to set
     */
    public void setSynctimestamp(String synctimestamp) {
        this.synctimestamp = synctimestamp;
    }
    /**
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    };
    
    
}