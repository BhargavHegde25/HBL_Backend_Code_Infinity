package com.temenos.dbx.product.dto;

import java.util.List;

import com.dbp.core.api.DBPDTO;

public class AllAccountsViewDTO implements DBPDTO {

    /**
     * 
     */
    private static final long serialVersionUID = 2882283010371142327L;
    private String accountName;
    private String accountId;
    private String accountType;
    private String accountHolderName;
    private String membershipId;
    private String typeId;
    private String taxId;
    private String membershipName;
    private String arrangementId;
    private String ownership;
    private String accountStatus;
    private List<MembershipOwnerDTO> membershipownerDTO;
    private String productId;

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

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public List<MembershipOwnerDTO> getMembershipownerDTO() {
        return membershipownerDTO;
    }

    public void setMembershipownerDTO(List<MembershipOwnerDTO> membershipownerDTO) {
        this.membershipownerDTO = membershipownerDTO;
    }

    public String getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(String membershipId) {
        this.membershipId = membershipId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getArrangementId() {
        return arrangementId;
    }

    public void setArrangementId(String arrangementId) {
        this.arrangementId = arrangementId;
    }

    public String getOwnership() {
        return ownership;
    }

    public void setOwnership(String ownership) {
        this.ownership = ownership;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }
    
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

}
