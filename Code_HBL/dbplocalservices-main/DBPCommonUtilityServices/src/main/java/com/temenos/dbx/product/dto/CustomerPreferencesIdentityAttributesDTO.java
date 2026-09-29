package com.temenos.dbx.product.dto;

import com.dbp.core.api.DBPDTO;

public class CustomerPreferencesIdentityAttributesDTO implements DBPDTO {
    /**
     * 
     */
    private static final long serialVersionUID = -4629169870325703474L;
    private String customerId;
    private String defaultAccountCardless;
    private String defaultAccountWire;
    private String defaultFromAccountP2P;
    private String showBillpayFromAccountPopup;
    private String defaultAccountTransfers;
    private String defaultToAccountP2P;
    private String defaultAccountPayments;
    private String defaultAccountDeposit;
    private String defaultAccountBillPay;
    private String phone;
    private String email;
    private String country;
    private String zipCode;
    private String latitude;
    private String isPreferredAddress;
    private String longitude;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String state;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getDefaultAccountCardless() {
        return defaultAccountCardless;
    }

    public void setDefaultAccountCardless(String defaultAccountCardless) {
        this.defaultAccountCardless = defaultAccountCardless;
    }

    public String getDefaultAccountWire() {
        return defaultAccountWire;
    }

    public void setDefaultAccountWire(String defaultAccountWire) {
        this.defaultAccountWire = defaultAccountWire;
    }

    public String getDefaultFromAccountP2P() {
        return defaultFromAccountP2P;
    }

    public void setDefaultFromAccountP2P(String defaultFromAccountP2P) {
        this.defaultFromAccountP2P = defaultFromAccountP2P;
    }

    public String getShowBillpayFromAccountPopup() {
        return showBillpayFromAccountPopup;
    }

    public void setShowBillpayFromAccountPopup(String showBillpayFromAccountPopup) {
        this.showBillpayFromAccountPopup = showBillpayFromAccountPopup;
    }

    public String getDefaultAccountTransfers() {
        return defaultAccountTransfers;
    }

    public void setDefaultAccountTransfers(String defaultAccountTransfers) {
        this.defaultAccountTransfers = defaultAccountTransfers;
    }

    public String getDefaultToAccountP2P() {
        return defaultToAccountP2P;
    }

    public void setDefaultToAccountP2P(String defaultToAccountP2P) {
        this.defaultToAccountP2P = defaultToAccountP2P;
    }

    public String getDefaultAccountPayments() {
        return defaultAccountPayments;
    }

    public void setDefaultAccountPayments(String defaultAccountPayments) {
        this.defaultAccountPayments = defaultAccountPayments;
    }

    public String getDefaultAccountDeposit() {
        return defaultAccountDeposit;
    }

    public void setDefaultAccountDeposit(String defaultAccountDeposit) {
        this.defaultAccountDeposit = defaultAccountDeposit;
    }

    public String getDefaultAccountBillPay() {
        return defaultAccountBillPay;
    }

    public void setDefaultAccountBillPay(String defaultAccountBillPay) {
        this.defaultAccountBillPay = defaultAccountBillPay;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getIsPreferredAddress() {
        return isPreferredAddress;
    }

    public void setIsPreferredAddress(String isPreferredAddress) {
        this.isPreferredAddress = isPreferredAddress;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getAddressLine3() {
        return addressLine3;
    }

    public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
