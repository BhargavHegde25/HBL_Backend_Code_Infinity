package com.temenos.dbx.product.dto;


import java.util.ArrayList;

import java.util.List;

import java.util.Map;


import org.apache.commons.lang3.StringUtils;


import com.kony.dbputilities.util.DBPUtilitiesConstants;

import com.kony.dbputilities.util.ServiceCallHelper;

import com.kony.dbputilities.util.URLConstants;

import com.temenos.dbx.product.api.DBPDTOEXT;

import com.temenos.dbx.product.utils.DTOConstants;

import com.temenos.dbx.product.utils.DTOUtils;


public class AssociatedContractUsersDTO implements DBPDTOEXT {



	/**

	 * 

	 */

	private static final long serialVersionUID = 908651236189166105L;

	private String id;

	private String contractId;

	private String contractName;

	private String coreCustomerId;

	private String coreCustomerName;

	private String customerId;

	private String groupId;

	private String firstName;

	private String lastName;

	private String userName ;

	private String backendId;

	private String lastlogintime;

	private String statusId;
	
	private String companyLegalUnit;


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

	public String getContractId() {

		return contractId;

	}

	public void setContractId(String contractId) {

		this.contractId = contractId;

	}

	public String getId() {

		return id;

	}

	public void setId(String id) {

		this.id = id;

	}

	public String getContractName() {

		return contractName;

	}

	public void setContractName(String contractName) {

		this.contractName = contractName;

	}

	public String getCoreCustomerId() {

		return coreCustomerId;

	}

	public void setCoreCustomerId(String coreCustomerId) {

		this.coreCustomerId = coreCustomerId;

	}

	public String getCoreCustomerName() {

		return coreCustomerName;

	}

	public void setCoreCustomerName(String coreCustomerName) {

		this.coreCustomerName = coreCustomerName;

	}

	public String getCustomerId() {

		return customerId;

	}

	public void setCustomerId(String customerId) {

		this.customerId = customerId;

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

	public String getUserName() {

		return userName;

	}

	public void setUserName(String userName) {

		this.userName = userName;

	}

	public String isBackendId() {

		return backendId;

	}

	public void setBackendId(String backendId) {

		this.backendId = backendId;

	}

	public String getLastlogintime() {

		return lastlogintime;

	}

	public void setLastlogintime(String lastlogintime) {

		this.lastlogintime = lastlogintime;

	}

	public String getStatusId() {

		return statusId;

	}

	public void setStatusId(String statusId) {

		this.statusId = statusId;

	}

	public String getCompanyLegalUnit() {
		return companyLegalUnit;
	}

	public void setCompanyLegalUnit(String companyLegalUnit) {
		this.companyLegalUnit = companyLegalUnit;
	}

}