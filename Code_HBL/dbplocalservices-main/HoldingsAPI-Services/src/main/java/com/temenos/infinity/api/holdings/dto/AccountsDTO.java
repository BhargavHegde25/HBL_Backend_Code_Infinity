package com.temenos.infinity.api.holdings.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class AccountsDTO implements DBPDTO {

	
	/**
	 * Unique ID for Serialization
	 */
	private static final long serialVersionUID = 887984530375346133L;
	
	
	public AccountsDTO() {
		super();
	}
	
	public AccountsDTO(String accountId, String accountName, String accountType, String arrangementId, String productId,
			String currencyCode, String companyCode, String accountIdWithCompanyId) {
		super();
		this.accountId = accountId;
		this.accountName = accountName;
		this.accountType = accountType;
		this.arrangementId = arrangementId;
		this.productId = productId;
		this.currencyCode = currencyCode;
		this.companyCode = companyCode;
		this.accountIdWithCompanyId = accountIdWithCompanyId;
	}
	private String accountId;
	private String accountName;
	private String accountType;
	private String arrangementId;
	private String productId;
	private String currencyCode;
	private String companyCode;
	private String accountIdWithCompanyId;
	
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getArrangementId() {
		return arrangementId;
	}
	public void setArrangementId(String arrangementId) {
		this.arrangementId = arrangementId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	
	public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getAccountIdWithCompanyId() {
        return accountIdWithCompanyId;
    }

    public void setAccountIdWithCompanyId(String accountIdWithCompanyId) {
        this.accountIdWithCompanyId = accountIdWithCompanyId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((accountId == null) ? 0 : accountId.hashCode());
        result = prime * result + ((accountIdWithCompanyId == null) ? 0 : accountIdWithCompanyId.hashCode());
        result = prime * result + ((accountName == null) ? 0 : accountName.hashCode());
        result = prime * result + ((accountType == null) ? 0 : accountType.hashCode());
        result = prime * result + ((arrangementId == null) ? 0 : arrangementId.hashCode());
        result = prime * result + ((companyCode == null) ? 0 : companyCode.hashCode());
        result = prime * result + ((currencyCode == null) ? 0 : currencyCode.hashCode());
        result = prime * result + ((productId == null) ? 0 : productId.hashCode());
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
        AccountsDTO other = (AccountsDTO) obj;
        if (accountId == null) {
            if (other.accountId != null)
                return false;
        } else if (!accountId.equals(other.accountId))
            return false;
        if (accountIdWithCompanyId == null) {
            if (other.accountIdWithCompanyId != null)
                return false;
        } else if (!accountIdWithCompanyId.equals(other.accountIdWithCompanyId))
            return false;
        if (accountName == null) {
            if (other.accountName != null)
                return false;
        } else if (!accountName.equals(other.accountName))
            return false;
        if (accountType == null) {
            if (other.accountType != null)
                return false;
        } else if (!accountType.equals(other.accountType))
            return false;
        if (arrangementId == null) {
            if (other.arrangementId != null)
                return false;
        } else if (!arrangementId.equals(other.arrangementId))
            return false;
        if (companyCode == null) {
            if (other.companyCode != null)
                return false;
        } else if (!companyCode.equals(other.companyCode))
            return false;
        if (currencyCode == null) {
            if (other.currencyCode != null)
                return false;
        } else if (!currencyCode.equals(other.currencyCode))
            return false;
        if (productId == null) {
            if (other.productId != null)
                return false;
        } else if (!productId.equals(other.productId))
            return false;
        return true;
    }	
	
}
