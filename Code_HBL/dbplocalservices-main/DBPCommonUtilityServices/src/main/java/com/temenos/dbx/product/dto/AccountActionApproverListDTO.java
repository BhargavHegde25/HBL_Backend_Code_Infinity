package com.temenos.dbx.product.dto;

/**
 * 
 * @author KH2627
 * @version 1.0
 * implements {@link DBPDTO}
 * 
 *
 */

import com.dbp.core.api.DBPDTO;

public class AccountActionApproverListDTO implements DBPDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 136067348552556409L;
	private String organizationId;
	private String accountId;
	private String approvalActionList;
	private String featureId;
	private String id;
	private String userName;
	private String groupId;
	private String actionId;
	private String firstName;
	private String lastName;
	private String contractId;
	private String cif;
	
	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getApprovalActionList() {
		return approvalActionList;
	}

	public void setApprovalActionList(String approvalActionList) {
		this.approvalActionList = approvalActionList;
	}

	public String getFeatureId() {
		return featureId;
	}

	public void setFeatureId(String featureId) {
		this.featureId = featureId;
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

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	public String getCif() {
		return cif;
	}
	public void setCif(String cif) {
		this.cif = cif;
	}

}
