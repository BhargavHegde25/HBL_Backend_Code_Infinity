package com.kony.adminconsole.service.alertmanagement.pojo;

public class AutoSubscribedAlertView {
	
	String categoryid;
	String groupid;
	String alertsubtypeid;
	String name;
	int isAccountLevel;
	String attributeId;
	String alertConditionId;
	String value1;
	String value2;
	String Status_id;
	String Description;
	int isGlobal;
	String defaultFrequencyId;
	String defaultFrequencyValue;
	String defaultFrequencyTime;
	int recipienttype;
	String createdby;
	String modifiedby;
	String createdts;
	String lastmodifiedts;
	String synctimestamp;
	boolean softdeleteflag;
	boolean isAutoSubscribeEnabled;
	int externalSystem;
	
	public String getCategoryid() {
		return categoryid;
	}
	public void setCategoryid(String categoryid) {
		this.categoryid = categoryid;
	}
	public String getGroupid() {
		return groupid;
	}
	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}
	public String getAlertsubtypeid() {
		return alertsubtypeid;
	}
	public void setAlertsubtypeid(String alertsubtypeid) {
		this.alertsubtypeid = alertsubtypeid;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getIsAccountLevel() {
		return isAccountLevel;
	}
	public void setIsAccountLevel(int isAccountLevel) {
		this.isAccountLevel = isAccountLevel;
	}
	public String getAttributeId() {
		return attributeId;
	}
	public void setAttributeId(String attributeId) {
		this.attributeId = attributeId;
	}
	public String getAlertConditionId() {
		return alertConditionId;
	}
	public void setAlertConditionId(String alertConditionId) {
		this.alertConditionId = alertConditionId;
	}
	public String getValue1() {
		return value1;
	}
	public void setValue1(String value1) {
		this.value1 = value1;
	}
	public String getValue2() {
		return value2;
	}
	public void setValue2(String value2) {
		this.value2 = value2;
	}
	public String getStatus_id() {
		return Status_id;
	}
	public void setStatus_id(String status_id) {
		Status_id = status_id;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public int getIsGlobal() {
		return isGlobal;
	}
	public void setIsGlobal(int isGlobal) {
		this.isGlobal = isGlobal;
	}
	public String getDefaultFrequencyId() {
		return defaultFrequencyId;
	}
	public void setDefaultFrequencyId(String defaultFrequencyId) {
		this.defaultFrequencyId = defaultFrequencyId;
	}
	public String getDefaultFrequencyValue() {
		return defaultFrequencyValue;
	}
	public void setDefaultFrequencyValue(String defaultFrequencyValue) {
		this.defaultFrequencyValue = defaultFrequencyValue;
	}
	public int getRecipienttype() {
		return recipienttype;
	}
	public void setRecipienttype(int recipienttype) {
		this.recipienttype = recipienttype;
	}
	public String getCreatedby() {
		return createdby;
	}
	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}
	public String getModifiedby() {
		return modifiedby;
	}
	public void setModifiedby(String modifiedby) {
		this.modifiedby = modifiedby;
	}
	
	public boolean isSoftdeleteflag() {
		return softdeleteflag;
	}
	public void setSoftdeleteflag(boolean softdeleteflag) {
		this.softdeleteflag = softdeleteflag;
	}
	
	public boolean isAutoSubscribeEnabled() {
		return isAutoSubscribeEnabled;
	}
	public void setAutoSubscribeEnabled(boolean isAutoSubscribeEnabled) {
		this.isAutoSubscribeEnabled = isAutoSubscribeEnabled;
	}
	public int getExternalSystem() {
		return externalSystem;
	}
	public void setExternalSystem(int externalSystem) {
		this.externalSystem = externalSystem;
	}
	public String getDefaultFrequencyTime() {
		return defaultFrequencyTime;
	}
	public void setDefaultFrequencyTime(String defaultFrequencyTime) {
		this.defaultFrequencyTime = defaultFrequencyTime;
	}
	public String getCreatedts() {
		return createdts;
	}
	public void setCreatedts(String createdts) {
		this.createdts = createdts;
	}
	public String getLastmodifiedts() {
		return lastmodifiedts;
	}
	public void setLastmodifiedts(String lastmodifiedts) {
		this.lastmodifiedts = lastmodifiedts;
	}
	public String getSynctimestamp() {
		return synctimestamp;
	}
	public void setSynctimestamp(String synctimestamp) {
		this.synctimestamp = synctimestamp;
	}
	@Override
	public String toString() {
		return "AllAlertInfo [categoryid=" + categoryid + ", groupid=" + groupid + ", alertsubtypeid=" + alertsubtypeid
				+ ", name=" + name + ", isAccountLevel=" + isAccountLevel + ", attributeId=" + attributeId
				+ ", alertConditionId=" + alertConditionId + ", value1=" + value1 + ", value2=" + value2
				+ ", Status_id=" + Status_id + ", Description=" + Description + ", isGlobal=" + isGlobal
				+ ", defaultFrequencyId=" + defaultFrequencyId + ", defaultFrequencyValue=" + defaultFrequencyValue
				+ ", defaultFrequencyTime=" + defaultFrequencyTime + ", recipienttype=" + recipienttype + ", createdby="
				+ createdby + ", modifiedby=" + modifiedby + ", createdts=" + createdts + ", lastmodifiedts="
				+ lastmodifiedts + ", synctimestamp=" + synctimestamp + ", softdeleteflag=" + softdeleteflag
				+ ", isAutoSubscribeEnabled=" + isAutoSubscribeEnabled + ", externalSystem=" + externalSystem + "]";
	}
	
	
	

}
