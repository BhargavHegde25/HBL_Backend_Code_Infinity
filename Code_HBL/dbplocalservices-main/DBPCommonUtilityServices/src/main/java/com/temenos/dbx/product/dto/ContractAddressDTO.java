package com.temenos.dbx.product.dto;

import com.dbp.core.api.DBPDTO;

public class ContractAddressDTO implements DBPDTO {

    /**
     * 
     */
    private static final long serialVersionUID = 551431400455767483L;
    private String id;
    private String contractId;
    private String addressId;
    private String isPrimary;
    private String createdby;
    private String modifiedby;
    private String createdts;
    private String lastmodifiedts;
    private String synctimestamp;
    private String softdeleteflag;
    private String companyLegalUnit;

    private AddressDTO address;

    public String getSynctimestamp() {
        return synctimestamp;
    }

    public void setSynctimestamp(String synctimestamp) {
        this.synctimestamp = synctimestamp;
    }

    public String getSoftdeleteflag() {
        return softdeleteflag;
    }

    public void setSoftdeleteflag(String softdeleteflag) {
        this.softdeleteflag = softdeleteflag;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }
    
    public String getCompanyLegalUnit() {
        return companyLegalUnit;
    }

    public void setCompanyLegalUnit(String companyLegalUnit) {
        this.companyLegalUnit = companyLegalUnit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(String isPrimary) {
        this.isPrimary = isPrimary;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public String getModifiedby() {
        return modifiedby;
    }

    public void setModifiedby(String modifiedby) {
        this.modifiedby = modifiedby;
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

}
