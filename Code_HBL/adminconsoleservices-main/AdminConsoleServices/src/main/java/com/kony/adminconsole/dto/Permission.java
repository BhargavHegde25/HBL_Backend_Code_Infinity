/**
 * 
 */
package com.kony.adminconsole.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO of Permission
 * 
 * @author Aditya Mankal
 *
 */
@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Permission {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("status")
    private String status;

    @JsonProperty("PermissionValue")
    private boolean isEnabled;

    @JsonProperty("softdeleteflag")
    private int softdeleteflag;

    private String parentPermissionId;
    
    @JsonProperty("Role_id")
    private String Role_id;
    
    @JsonProperty("isComposite")
    private String isComposite;
    
    @JsonProperty("companyLegalUnit")
    private String companyLegalUnit;

    /**
     * 
     */
    public Permission() {
        super();
    }

    /**
     * @param id
     * @param name
     * @param status
     * @param isEnabled
     * @param softdeleteflag
     * @param parentPermissionId
     * @param Role_id
     * @param isComposite
     * @param companyLegalUnit
     */
    public Permission(String id, String name, String status, boolean isEnabled, int softdeleteflag,
            String parentPermissionId, String Role_id, String isComposite) {
        super();
        this.id = id;
        this.name = name;
        this.status = status;
        this.isEnabled = isEnabled;
        this.softdeleteflag = softdeleteflag;
        this.parentPermissionId = parentPermissionId;
        this.Role_id = Role_id;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the isEnabled
     */
    public boolean isEnabled() {
        return isEnabled;
    }

    /**
     * @param isEnabled
     *            the isEnabled to set
     */
    public void setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    /**
     * @return the softdeleteflag
     */
    public int getSoftdeleteflag() {
        return softdeleteflag;
    }

    /**
     * @param softdeleteflag
     *            the softdeleteflag to set
     */
    public void setSoftdeleteflag(int softdeleteflag) {
        this.softdeleteflag = softdeleteflag;
    }

    /**
     * @return the parentPermissionId
     */
    public String getParentPermissionId() {
        return parentPermissionId;
    }

    /**
     * @param parentPermissionId
     *            the parentPermissionId to set
     */
    public void setParentPermissionId(String parentPermissionId) {
        this.parentPermissionId = parentPermissionId;
    }

    /**
     * @return the Role_id
     */
	public String getRole_id() {
		return Role_id;
	}

	/**
     * @param role_id
     *            the role_id to set
     */
	public void setRole_id(String Role_id) {
		this.Role_id = Role_id;
	}

	/**
     * @return the isComposite
     */
	public String getIsComposite() {
		return isComposite;
	}
 
	/**
     * @param isComposite
     *            the isComposite to set
     */
	public void setIsComposite(String isComposite) {
		this.isComposite = isComposite;
	}
	
	/**
     * @return the companyLegalUnit
     */
	public String getCompanyLegalUnit() {
		return companyLegalUnit;
	}
 
	/**
     * @param companyLegalUnit
     *            the companyLegalUnit to set
     */
	public void setCompanyLegalUnit(String companyLegalUnit) {
		this.companyLegalUnit = companyLegalUnit;
	}
	
}
