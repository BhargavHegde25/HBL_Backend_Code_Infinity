package com.kony.dbp.dto;

public class CustomerBasicDTO {

	private String id;
	private String UserName;
	private String Status_id;
	private String Organization_Id;
	boolean isBusinessUser = false;
	
	public boolean isBusinessUser() {
		return isBusinessUser;
	}
	public void setBusinessUser(boolean isBusinessUser) {
		this.isBusinessUser = isBusinessUser;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getStatus_id() {
		return Status_id;
	}
	public void setStatus_id(String status_id) {
		Status_id = status_id;
	}
	public String getOrganization_Id() {
		return Organization_Id;
	}
	public void setOrganization_Id(String organization_Id) {
		Organization_Id = organization_Id;
	}
	
	
}
