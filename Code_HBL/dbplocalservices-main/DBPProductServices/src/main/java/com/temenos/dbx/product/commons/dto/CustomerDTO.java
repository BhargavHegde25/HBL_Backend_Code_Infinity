package com.temenos.dbx.product.commons.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerDTO implements DBPDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 374520292763112795L;

	private String id;
	
	//@JsonAlias({"UserName"})
	private String userName;
	
	// alias for userName
	@JsonProperty("UserName")
	private String uname;
	
	@JsonProperty("FirstName")
	private String firstName;
	
	@JsonProperty("LastName")
	private String lastName;
	
	private String fullName;
	
	//@JsonAlias({"Organization_Id"})
	private String organizationId;
	
	@JsonProperty("Organization_Id")
	private String orgId;
	
	public CustomerDTO() {
		super();
	}

	public CustomerDTO(String id, String userName, String uname, String organizationId, String orgId, String firstName, String lastName, String fullName) {
		super();
		this.id = id;
		this.userName = userName;
		this.uname = uname;
		this.organizationId = organizationId;
		this.orgId = orgId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.fullName = fullName;
	}

	
	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = null;
		this.setUserName(uname);
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = null;
		this.setOrganizationId(orgId);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
		setFullName(this.firstName + " " + this.lastName);
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
		setFullName(this.firstName + " " + this.lastName);
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((orgId == null) ? 0 : orgId.hashCode());
		result = prime * result + ((organizationId == null) ? 0 : organizationId.hashCode());
		result = prime * result + ((uname == null) ? 0 : uname.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((fullName == null) ? 0 : fullName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomerDTO other = (CustomerDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
