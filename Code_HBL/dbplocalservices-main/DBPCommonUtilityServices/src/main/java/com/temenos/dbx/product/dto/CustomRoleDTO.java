package com.temenos.dbx.product.dto;

import java.util.List;

import com.dbp.core.api.DBPDTO;

public class CustomRoleDTO implements DBPDTO{

	private static final long serialVersionUID = -5275389031167326763L;
	
	private String synctimestamp;
	private String status_id;
	private String softdeleteflag;
	private String parent_id;
	private String organization_id;
	private String name;
	private String modifiedby;
	private String lastmodifiedts;
	private String id;
	private String description;
	private String createdts;
	private String createdby;
	private String userName;
	private String parentRoleName;
	private String statusValue;
	private List<CustomRoleActionLimitDTO> actionLimits;
	
	public CustomRoleDTO() {
		super();
	}

	public List<CustomRoleActionLimitDTO> getActionLimits() {
		return actionLimits;
	}

	public void setActionLimits(List<CustomRoleActionLimitDTO> actionLimits) {
		this.actionLimits = actionLimits;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getParentRoleName() {
		return parentRoleName;
	}

	public void setParentRoleName(String parentRoleName) {
		this.parentRoleName = parentRoleName;
	}

	public String getStatusValue() {
		return statusValue;
	}

	public void setStatusValue(String statusValue) {
		this.statusValue = statusValue;
	}

	public String getSynctimestamp() {
		return synctimestamp;
	}

	public void setSynctimestamp(String synctimestamp) {
		this.synctimestamp = synctimestamp;
	}

	public String getStatus_id() {
		return status_id;
	}

	public void setStatus_id(String status_id) {
		this.status_id = status_id;
	}

	public String getSoftdeleteflag() {
		return softdeleteflag;
	}

	public void setSoftdeleteflag(String softdeleteflag) {
		this.softdeleteflag = softdeleteflag;
	}

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	public String getOrganization_id() {
		return organization_id;
	}

	public void setOrganization_id(String organization_id) {
		this.organization_id = organization_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreatedts() {
		return createdts;
	}

	public void setCreatedts(String createdts) {
		this.createdts = createdts;
	}

	public String getCreatedby() {
		return createdby;
	}

	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}
	
}