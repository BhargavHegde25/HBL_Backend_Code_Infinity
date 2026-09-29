package com.temenos.dbx.product.dto;

import java.util.List;

import com.dbp.core.api.DBPDTO;

public class AccountsDTO implements DBPDTO {

    /**
     * 
     */
    private static final long serialVersionUID = 2290759526951288652L;
    private String accountId;
    private String organizationId;
    private String isBusinessAccount;
    private String accountHolder;
    private String accountName;
    private String typeId;
    private String statusDescription;
    private String membershipId;
    private String arrangementId;
    private String taxId;
    private String membershipName;
    private String userId;
    private String accountType;
    private List<MembershipOwnerDTO> membershipownerDTOList;
    private String companyLegalUnit;

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getMembershipName() {
        return membershipName;
    }

    public void setMembershipName(String membershipName) {
        this.membershipName = membershipName;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getArrangementId() {
        return arrangementId;
    }

    public void setArrangementId(String arrangementId) {
        this.arrangementId = arrangementId;
    }

    public String getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(String membershipId) {
        this.membershipId = membershipId;
    }

    public String getIsBusinessAccount() {
        return isBusinessAccount;
    }

    public void setIsBusinessAccount(String isBusinessAccount) {
        this.isBusinessAccount = isBusinessAccount;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public void setAccountHolder(String accountHolder) {
        this.accountHolder = accountHolder;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
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

    public List<MembershipOwnerDTO> getMembershipownerDTOList() {
        return membershipownerDTOList;
    }

    public void setMembershipownerDTOList(List<MembershipOwnerDTO> membershipownerDTOList) {
        this.membershipownerDTOList = membershipownerDTOList;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

	public String getCompanyLegalUnit() {
		return companyLegalUnit;
	}

	public void setCompanyLegalUnit(String companyLegalUnit) {
		this.companyLegalUnit = companyLegalUnit;
	}

}
