package com.temenos.dbx.product.dto;

import com.dbp.core.api.DBPDTO;

public class CustomRoleActionLimitDTO implements DBPDTO {
	
	private static final long serialVersionUID = 8002579660416096634L;
	
	private String account_id;
	private String action_id;
	private String actionName;
	private String createdby;
	private String createdts;
	private String featureId;
	private String customRole_id;
	private String id;
	private String isAllowed;
	private String lastmodifiedts;
	private String limitType_id;
	private String modifiedby;
	private String softdeleteflag;
	private String value;
	private String featureName;
	private String featureDescription;
	private String accountName;
	private String isAccountLevel;
	private String actionType;
	private String actionDescription;
	
	public CustomRoleActionLimitDTO() {
		super();
	}

	public String getFeatureId() {
		return featureId;
	}

	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getFeatureName() {
		return featureName;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

	public String getFeatureDescription() {
		return featureDescription;
	}

	public void setFeatureDescription(String featureDescription) {
		this.featureDescription = featureDescription;
	}

	public String getAccount_id() {
		return account_id;
	}

	public void setAccount_id(String account_id) {
		this.account_id = account_id;
	}

	public String getAction_id() {
		return action_id;
	}

	public void setAction_id(String action_id) {
		this.action_id = action_id;
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

	public String getCustomRole_id() {
		return customRole_id;
	}

	public void setCustomRole_id(String customRole_id) {
		this.customRole_id = customRole_id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIsAllowed() {
		return isAllowed;
	}

	public void setIsAllowed(String isAllowed) {
		this.isAllowed = isAllowed;
	}

	public String getLastmodifiedts() {
		return lastmodifiedts;
	}

	public void setLastmodifiedts(String lastmodifiedts) {
		this.lastmodifiedts = lastmodifiedts;
	}

	public String getLimitType_id() {
		return limitType_id;
	}

	public void setLimitType_id(String limitType_id) {
		this.limitType_id = limitType_id;
	}

	public String getModifiedby() {
		return modifiedby;
	}

	public void setModifiedby(String modifiedby) {
		this.modifiedby = modifiedby;
	}

	public String getSoftdeleteflag() {
		return softdeleteflag;
	}

	public void setSoftdeleteflag(String softdeleteflag) {
		this.softdeleteflag = softdeleteflag;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getIsAccountLevel() {
		return isAccountLevel;
	}

	public void setIsAccountLevel(String isAccountLevel) {
		this.isAccountLevel = isAccountLevel;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getActionDescription() {
		return actionDescription;
	}

	public void setActionDescription(String actionDescription) {
		this.actionDescription = actionDescription;
	}

}