package com.temenos.dbx.eum.product.commons.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerAccountsDTO implements DBPDTO{

	private static final long serialVersionUID = -5304724637443347409L;
	
	private String id;
	
	@JsonAlias({"Customer_id","userId"})
	private String userId;
	
	@JsonAlias({"Account_id","accountId"})
	private String accountId;
	
	@JsonAlias({"Organization_id","organizationId"})
	private String organizationId;
	
	@JsonAlias({"AccountName","accountName"})
	private String accountName;
	
	@JsonAlias({"FavouriteStatus","favouriteStatus"})
	private String favouriteStatus;
	
	@JsonAlias({"IsViewAllowed","isViewAllowed"})
	private String isViewAllowed;
	
	@JsonAlias({"IsDepositAllowed","isDepositAllowed"})
	private String isDepositAllowed;
	
	@JsonAlias({"IsWithdrawAllowed","isWithdrawAllowed"})
	private String isWithdrawAllowed;
	
	@JsonAlias({"IsOrganizationAccount","isOrganizationAccount"})
	private String isOrganizationAccount;
	
	@JsonAlias({"IsOrgAccountUnLinked","isOrgAccountUnLinked"})
	private String isOrgAccountUnLinked;
	private String contractId;
	
	@JsonAlias({"coreCustomerId","customerId"})
	private String coreCustomerId;
	
	@JsonAlias({"coreCustomerName","customerName"})
	private String coreCustomerName;
	
	private String accountType;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getFavouriteStatus() {
		return favouriteStatus;
	}
	public void setFavouriteStatus(String favouriteStatus) {
		this.favouriteStatus = favouriteStatus;
	}
	public String getIsViewAllowed() {
		return isViewAllowed;
	}
	public void setIsViewAllowed(String isViewAllowed) {
		this.isViewAllowed = isViewAllowed;
	}
	public String getIsDepositAllowed() {
		return isDepositAllowed;
	}
	public void setIsDepositAllowed(String isDepositAllowed) {
		this.isDepositAllowed = isDepositAllowed;
	}
	public String getIsWithdrawAllowed() {
		return isWithdrawAllowed;
	}
	public void setIsWithdrawAllowed(String isWithdrawAllowed) {
		this.isWithdrawAllowed = isWithdrawAllowed;
	}
	public String getIsOrganizationAccount() {
		return isOrganizationAccount;
	}
	public void setIsOrganizationAccount(String isOrganizationAccount) {
		this.isOrganizationAccount = isOrganizationAccount;
	}
	public String getIsOrgAccountUnLinked() {
		return isOrgAccountUnLinked;
	}
	public void setIsOrgAccountUnLinked(String isOrgAccountUnLinked) {
		this.isOrgAccountUnLinked = isOrgAccountUnLinked;
	}
	public String getContractId() {
		return contractId;
	}
	public void setContractId(String contractId) {
		this.contractId = contractId;
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	
		
}
