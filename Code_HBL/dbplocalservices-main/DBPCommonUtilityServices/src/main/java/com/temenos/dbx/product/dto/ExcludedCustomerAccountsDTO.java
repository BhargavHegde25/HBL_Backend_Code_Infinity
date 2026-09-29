package com.temenos.dbx.product.dto;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPDTO;

public class ExcludedCustomerAccountsDTO implements DBPDTO {

    /**
     * 
     */
    private static final long serialVersionUID = 6339729069523660272L;
    Map<String, String> map = new HashMap<>();
    private String id;
    private String customerId;
    private String membershipId;
    private String accountId;
    private String organizationId;
    private String accountName;
    private String favouriteStatus;
    private String isViewAllowed;
    private String isDepositAllowed;
    private String isWithdrawAllowed;
    private String isOrganizationAccount;
    private String isOrgAccountUnLinked;
    private String createdBy;
    private String createdts;
    private String lastmodifiedts;
    /**
     * @return the map
     */
    public Map<String, String> getMap() {
        return map;
    }
    /**
     * @param map the map to set
     */
    public void setMap(Map<String, String> map) {
        this.map = map;
    }
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * @return the customerId
     */
    public String getCustomerId() {
        return customerId;
    }
    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    /**
     * @return the membershipId
     */
    public String getMembershipId() {
        return membershipId;
    }
    /**
     * @param membershipId the membershipId to set
     */
    public void setMembershipId(String membershipId) {
        this.membershipId = membershipId;
    }
    /**
     * @return the accountId
     */
    public String getAccountId() {
        return accountId;
    }
    /**
     * @param accountId the accountId to set
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
    /**
     * @return the organizationId
     */
    public String getOrganizationId() {
        return organizationId;
    }
    /**
     * @param organizationId the organizationId to set
     */
    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }
    /**
     * @return the accountName
     */
    public String getAccountName() {
        return accountName;
    }
    /**
     * @param accountName the accountName to set
     */
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
    /**
     * @return the favouriteStatus
     */
    public String getFavouriteStatus() {
        return favouriteStatus;
    }
    /**
     * @param favouriteStatus the favouriteStatus to set
     */
    public void setFavouriteStatus(String favouriteStatus) {
        this.favouriteStatus = favouriteStatus;
    }
    /**
     * @return the isViewAllowed
     */
    public String getIsViewAllowed() {
        return isViewAllowed;
    }
    /**
     * @param isViewAllowed the isViewAllowed to set
     */
    public void setIsViewAllowed(String isViewAllowed) {
        this.isViewAllowed = isViewAllowed;
    }
    /**
     * @return the isDepositAllowed
     */
    public String getIsDepositAllowed() {
        return isDepositAllowed;
    }
    /**
     * @param isDepositAllowed the isDepositAllowed to set
     */
    public void setIsDepositAllowed(String isDepositAllowed) {
        this.isDepositAllowed = isDepositAllowed;
    }
    /**
     * @return the isWithdrawAllowed
     */
    public String getIsWithdrawAllowed() {
        return isWithdrawAllowed;
    }
    /**
     * @param isWithdrawAllowed the isWithdrawAllowed to set
     */
    public void setIsWithdrawAllowed(String isWithdrawAllowed) {
        this.isWithdrawAllowed = isWithdrawAllowed;
    }
    /**
     * @return the isOrganizationAccount
     */
    public String getIsOrganizationAccount() {
        return isOrganizationAccount;
    }
    /**
     * @param isOrganizationAccount the isOrganizationAccount to set
     */
    public void setIsOrganizationAccount(String isOrganizationAccount) {
        this.isOrganizationAccount = isOrganizationAccount;
    }
    /**
     * @return the isOrgAccountUnLinked
     */
    public String getIsOrgAccountUnLinked() {
        return isOrgAccountUnLinked;
    }
    /**
     * @param isOrgAccountUnLinked the isOrgAccountUnLinked to set
     */
    public void setIsOrgAccountUnLinked(String isOrgAccountUnLinked) {
        this.isOrgAccountUnLinked = isOrgAccountUnLinked;
    }
    /**
     * @return the createdBy
     */
    public String getCreatedBy() {
        return createdBy;
    }
    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    /**
     * @return the createdts
     */
    public String getCreatedts() {
        return createdts;
    }
    /**
     * @param createdts the createdts to set
     */
    public void setCreatedts(String createdts) {
        this.createdts = createdts;
    }
    /**
     * @return the lastmodifiedts
     */
    public String getLastmodifiedts() {
        return lastmodifiedts;
    }
    /**
     * @param lastmodifiedts the lastmodifiedts to set
     */
    public void setLastmodifiedts(String lastmodifiedts) {
        this.lastmodifiedts = lastmodifiedts;
    }
    /**
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    
    }

}
