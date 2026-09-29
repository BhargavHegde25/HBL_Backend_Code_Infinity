package com.temenos.dbx.product.dto;

/**
 * 
 * @author 
 * @version 1.0
 * implements {@link DBPDTO}
 * 
 *
 */

import com.dbp.core.api.DBPDTO;

public class OrganizationAccountsDTO implements DBPDTO {

    /**
     * 
     */
    private static final long serialVersionUID = 7737448490001349243L;
    private String accountId;
    private String isOrganizationAccount;
    private String membershipId;
    private String taxId;
    private String accountHolder;
    private String accountName;
    private String typeId;
    private String arrangementId;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getIsOrganisationAccount() {
        return isOrganizationAccount;
    }

    public void setIsOrganisationAccount(String isOrganizationAccount) {
        this.isOrganizationAccount = isOrganizationAccount;
    }

    public String getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(String membershipId) {
        this.membershipId = membershipId;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
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

    public String getArrangementId() {
        return arrangementId;
    }

    public void setArrangementId(String arrangementId) {
        this.arrangementId = arrangementId;
    }

}
