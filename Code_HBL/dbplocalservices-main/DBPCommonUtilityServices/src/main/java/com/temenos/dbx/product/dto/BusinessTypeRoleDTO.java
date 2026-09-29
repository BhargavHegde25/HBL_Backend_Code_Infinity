package com.temenos.dbx.product.dto;

import java.util.Map;

import com.temenos.dbx.product.api.DBPDTOEXT;

public class BusinessTypeRoleDTO implements DBPDTOEXT{

	/**
	 * 
	 */
	private static final long serialVersionUID = -716891699818830898L;
	
	private String businessTypeId;
	private String groupId;
	private String groupName;
	private String groupDescription;
	private String isDefaultGroup;
	

	public String getBusinessTypeId() {
		return businessTypeId;
	}

	public void setBusinessTypeId(String businessTypeId) {
		this.businessTypeId = businessTypeId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupDescription() {
		return groupDescription;
	}

	public void setGroupDescription(String groupDescription) {
		this.groupDescription = groupDescription;
	}

	public String getIsDefaultGroup() {
		return isDefaultGroup;
	}

	public void setIsDefaultGroup(String isDefaultGroup) {
		this.isDefaultGroup = isDefaultGroup;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public boolean persist(Map<String, Object> input, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object loadDTO(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object loadDTO() {
		// TODO Auto-generated method stub
		return null;
	}

}
