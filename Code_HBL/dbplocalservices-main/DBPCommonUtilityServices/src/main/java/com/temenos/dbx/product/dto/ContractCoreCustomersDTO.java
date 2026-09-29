package com.temenos.dbx.product.dto;

import java.util.List;

import com.dbp.core.api.DBPDTO;

public class ContractCoreCustomersDTO implements DBPDTO {

    /**
     * 
     */
    private static final long serialVersionUID = 6189410655697668001L;
    private String id;
    private String contractId;
    private String taxId;
    private String coreCustomerId;
    private String coreCustomerName;
    private String isPrimary;
    private String isBusiness;
    private String sectorId;
    private String createdby;
    private String createdts;
    private String lastmodifiedts;
    private List<ContractAccountsDTO> accounts;
    private String addressLine1;
    private String addressLine2;
    private String cityName;
    private String country;
    private String zipCode;
    private String state;
    private String phone;
    private String email;
    private String industry;
    private String implicitAccountAccess;
    private String companyLegalUnit;

    public String getImplicitAccountAccess() {
        return implicitAccountAccess;
    }

    public void setImplicitAccountAccess(String implicitAccountAccess) {
        this.implicitAccountAccess = implicitAccountAccess;
    }

    public String getSectorId() {
        return sectorId;
    }

    public void setSectorId(String sectorId) {
        this.sectorId = sectorId;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
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

    public String getIsBusiness() {
        return isBusiness;
    }

    public void setIsBusiness(String isBusiness) {
        this.isBusiness = isBusiness;
    }

    public String getId() {
        return id;
    }

    public List<ContractAccountsDTO> getCustomerAccounts() {
        return accounts;
    }

    public void setCustomerAccounts(List<ContractAccountsDTO> customerAccounts) {
        this.accounts = customerAccounts;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getCompanyLegalUnit() {
        return companyLegalUnit;
    }

    public void setCompanyLegalUnit(String companyLegalUnit) {
        this.companyLegalUnit = companyLegalUnit;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
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

    public String getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(String isPrimary) {

        if(null != isPrimary && isPrimary.equals("1")){

            this.isPrimary = "true";
            return;
        } else if(null != isPrimary && isPrimary.equals("0")){
            this.isPrimary = "false";
            return;
        }
        this.isPrimary = isPrimary;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public String getCreatedts() {
        return createdts;
    }

    public void setCreatedts(String createdts) {
        this.createdts = createdts;
    }

    public String getLastmodifiedts() {
        return lastmodifiedts;
    }

    public void setLastmodifiedts(String lastmodifiedts) {
        this.lastmodifiedts = lastmodifiedts;
    }

    public List<ContractAccountsDTO> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<ContractAccountsDTO> accounts) {
        this.accounts = accounts;
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

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
