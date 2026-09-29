package com.temenos.dbx.product.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class FeatureActionDTO implements DBPDTO {

    /**
     * 
     */
    private static final long serialVersionUID = -7806512108826791885L;
    private String  id;
    private String  feature_id;
    private String  app_id;
    private String  type_id;
	private String  rrole_id;
    private String  name;
    private String  description;
    private String  isAccountLevel;
    private String  isMFAApplicable;
    private String  mfa_id;
    private String  termsAndConditions_id;
    private String  notes;
    private String  isPrimary;
    private String  displaySequence;
    private String  createdby;
    private String  modifiedby;
    private String  createdts;
    private String  lastmodifiedts;
    private String  synctimestamp;
    private String  softdeleteflag;
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
     * @return the feature_id
     */
    public String getFeature_id() {
        return feature_id;
    }
    /**
     * @param feature_id the feature_id to set
     */
    public void setFeature_id(String feature_id) {
        this.feature_id = feature_id;
    }
    /**
     * @return the app_id
     */
    public String getApp_id() {
        return app_id;
    }
    /**
     * @param app_id the app_id to set
     */
    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }
    /**
     * @return the type_id
     */
    public String getType_id() {
        return type_id;
    }
    /**
     * @param rrole_id the rrole_id to set
     */
    public void setRrole_id(String rrole_id) {
        this.rrole_id = rrole_id;
    }
    /**
     * @return the rrole_id
     */
    public String getRrole_id() {
        return rrole_id;
    }
        
    /**
     * @return if this appAction is Monetory or not
     */
    public boolean isMonetory() {
        return ("MONETARY".toLowerCase().equalsIgnoreCase(type_id.toLowerCase()));
    }
    
    /**
     * @param type_id the type_id to set
     */
    public void setType_id(String type_id) {
        this.type_id = type_id;
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * @return the isAccountLevel
     */
    public boolean isAccountLevel() {
        return Boolean.parseBoolean(isAccountLevel);
    }
    /**
     * @param isAccountLevel the isAccountLevel to set
     */
    public void setIsAccountLevel(boolean isAccountLevel) {
        this.isAccountLevel = String.valueOf(isAccountLevel);
    }
    /**
     * @return the isMFAApplicable
     */
    public String getIsMFAApplicable() {
        return isMFAApplicable;
    }
    /**
     * @param isMFAApplicable the isMFAApplicable to set
     */
    public void setIsMFAApplicable(String isMFAApplicable) {
        this.isMFAApplicable = isMFAApplicable;
    }
    /**
     * @return the mfa_id
     */
    public String getMfa_id() {
        return mfa_id;
    }
    /**
     * @param mfa_id the mfa_id to set
     */
    public void setMfa_id(String mfa_id) {
        this.mfa_id = mfa_id;
    }
    /**
     * @return the termsAndConditions_id
     */
    public String getTermsAndConditions_id() {
        return termsAndConditions_id;
    }
    /**
     * @param termsAndConditions_id the termsAndConditions_id to set
     */
    public void setTermsAndConditions_id(String termsAndConditions_id) {
        this.termsAndConditions_id = termsAndConditions_id;
    }
    /**
     * @return the notes
     */
    public String getNotes() {
        return notes;
    }
    /**
     * @param notes the notes to set
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }
    /**
     * @return the isPrimary
     */
    public String getIsPrimary() {
        return isPrimary;
    }
    /**
     * @param isPrimary the isPrimary to set
     */
    public void setIsPrimary(String isPrimary) {
        this.isPrimary = isPrimary;
    }
    /**
     * @return the displaySequence
     */
    public String getDisplaySequence() {
        return displaySequence;
    }
    /**
     * @param displaySequence the displaySequence to set
     */
    public void setDisplaySequence(String displaySequence) {
        this.displaySequence = displaySequence;
    }
    /**
     * @return the createdby
     */
    public String getCreatedby() {
        return createdby;
    }
    /**
     * @param createdby the createdby to set
     */
    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }
    /**
     * @return the modifiedby
     */
    public String getModifiedby() {
        return modifiedby;
    }
    /**
     * @param modifiedby the modifiedby to set
     */
    public void setModifiedby(String modifiedby) {
        this.modifiedby = modifiedby;
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
     * @return the softdeleteflag
     */
    public String getSoftdeleteflag() {
        return softdeleteflag;
    }
    /**
     * @param softdeleteflag the softdeleteflag to set
     */
    public void setSoftdeleteflag(String softdeleteflag) {
        this.softdeleteflag = softdeleteflag;
    }
    /**
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    
    
}
