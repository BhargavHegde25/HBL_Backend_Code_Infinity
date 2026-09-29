package com.temenos.infinity.api.accountaggregation.dto;

import java.io.Serializable;

import com.dbp.core.api.DBPDTO;

public class BankDTO implements Serializable, DBPDTO {

    /**
     * 
     */
    private static final long serialVersionUID = 2483378820155469411L;
    private String bankCode;
    private String[] supportedAccountNaturess;
    private String internalBankId;
    private String[] supportedFetchScopess;
    private String timeZone;
    private String bankName;
    private String isActive;
    private String[] supportedAccountTypess;
    private String logoUrl;
    private String mode;
    private String homeUrl;
    private String provider;
    private String loginUrl;
    private String countryCode;
    private String providerStatus;

    public BankDTO() {
        super();

    }

    public BankDTO(String bankCode, String[] supportedAccountNaturess, String internalBankId,
            String[] supportedFetchScopess, String timeZone, String bankName, String isActive,
            String[] supportedAccountTypess, String logoUrl, String mode, String homeUrl, String provider,
            String loginUrl, String countryCode, String providerStatus) {
        super();
        this.bankCode = bankCode;
        this.supportedAccountNaturess = supportedAccountNaturess;
        this.internalBankId = internalBankId;
        this.supportedFetchScopess = supportedFetchScopess;
        this.timeZone = timeZone;
        this.bankName = bankName;
        this.isActive = isActive;
        this.supportedAccountTypess = supportedAccountTypess;
        this.logoUrl = logoUrl;
        this.mode = mode;
        this.homeUrl = homeUrl;
        this.provider = provider;
        this.loginUrl = loginUrl;
        this.countryCode = countryCode;
        this.providerStatus = providerStatus;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String[] getSupportedAccountNaturess() {
        return supportedAccountNaturess;
    }

    public void setSupportedAccountNaturess(String[] supportedAccountNaturess) {
        this.supportedAccountNaturess = supportedAccountNaturess;
    }

    public String getInternalBankId() {
        return internalBankId;
    }

    public void setInternalBankId(String internalBankId) {
        this.internalBankId = internalBankId;
    }

    public String[] getSupportedFetchScopess() {
        return supportedFetchScopess;
    }

    public void setSupportedFetchScopess(String[] supportedFetchScopess) {
        this.supportedFetchScopess = supportedFetchScopess;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String[] getSupportedAccountTypess() {
        return supportedAccountTypess;
    }

    public void setSupportedAccountTypess(String[] supportedAccountTypess) {
        this.supportedAccountTypess = supportedAccountTypess;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getHomeUrl() {
        return homeUrl;
    }

    public void setHomeUrl(String homeUrl) {
        this.homeUrl = homeUrl;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getProviderStatus() {
        return providerStatus;
    }

    public void setProviderStatus(String providerStatus) {
        this.providerStatus = providerStatus;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bankCode == null) ? 0 : bankCode.hashCode());
        result = prime * result + ((bankName == null) ? 0 : bankName.hashCode());
        result = prime * result + ((countryCode == null) ? 0 : countryCode.hashCode());
        result = prime * result + ((homeUrl == null) ? 0 : homeUrl.hashCode());
        result = prime * result + ((internalBankId == null) ? 0 : internalBankId.hashCode());
        result = prime * result + ((isActive == null) ? 0 : isActive.hashCode());
        result = prime * result + ((loginUrl == null) ? 0 : loginUrl.hashCode());
        result = prime * result + ((logoUrl == null) ? 0 : logoUrl.hashCode());
        result = prime * result + ((mode == null) ? 0 : mode.hashCode());
        result = prime * result + ((provider == null) ? 0 : provider.hashCode());
        result = prime * result + ((providerStatus == null) ? 0 : providerStatus.hashCode());
        result = prime * result + ((supportedAccountNaturess == null) ? 0 : supportedAccountNaturess.hashCode());
        result = prime * result + ((supportedAccountTypess == null) ? 0 : supportedAccountTypess.hashCode());
        result = prime * result + ((supportedFetchScopess == null) ? 0 : supportedFetchScopess.hashCode());
        result = prime * result + ((timeZone == null) ? 0 : timeZone.hashCode());
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
        BankDTO other = (BankDTO) obj;
        if (bankCode == null) {
            if (other.bankCode != null)
                return false;
        } else if (!bankCode.equals(other.bankCode))
            return false;
        if (bankName == null) {
            if (other.bankName != null)
                return false;
        } else if (!bankName.equals(other.bankName))
            return false;
        if (countryCode == null) {
            if (other.countryCode != null)
                return false;
        } else if (!countryCode.equals(other.countryCode))
            return false;
        if (homeUrl == null) {
            if (other.homeUrl != null)
                return false;
        } else if (!homeUrl.equals(other.homeUrl))
            return false;
        if (internalBankId == null) {
            if (other.internalBankId != null)
                return false;
        } else if (!internalBankId.equals(other.internalBankId))
            return false;
        if (isActive == null) {
            if (other.isActive != null)
                return false;
        } else if (!isActive.equals(other.isActive))
            return false;
        if (loginUrl == null) {
            if (other.loginUrl != null)
                return false;
        } else if (!loginUrl.equals(other.loginUrl))
            return false;
        if (logoUrl == null) {
            if (other.logoUrl != null)
                return false;
        } else if (!logoUrl.equals(other.logoUrl))
            return false;
        if (mode == null) {
            if (other.mode != null)
                return false;
        } else if (!mode.equals(other.mode))
            return false;
        if (provider == null) {
            if (other.provider != null)
                return false;
        } else if (!provider.equals(other.provider))
            return false;
        if (providerStatus == null) {
            if (other.providerStatus != null)
                return false;
        } else if (!providerStatus.equals(other.providerStatus))
            return false;
        if (supportedAccountNaturess == null) {
            if (other.supportedAccountNaturess != null)
                return false;
        } else if (!supportedAccountNaturess.equals(other.supportedAccountNaturess))
            return false;
        if (supportedAccountTypess == null) {
            if (other.supportedAccountTypess != null)
                return false;
        } else if (!supportedAccountTypess.equals(other.supportedAccountTypess))
            return false;
        if (supportedFetchScopess == null) {
            if (other.supportedFetchScopess != null)
                return false;
        } else if (!supportedFetchScopess.equals(other.supportedFetchScopess))
            return false;
        if (timeZone == null) {
            if (other.timeZone != null)
                return false;
        } else if (!timeZone.equals(other.timeZone))
            return false;
        return true;
    }

}