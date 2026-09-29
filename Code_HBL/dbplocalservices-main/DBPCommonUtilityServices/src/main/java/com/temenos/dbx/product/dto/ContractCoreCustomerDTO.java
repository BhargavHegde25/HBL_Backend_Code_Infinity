package com.temenos.dbx.product.dto;

import com.dbp.core.api.DBPDTO;

public class ContractCoreCustomerDTO implements DBPDTO {

    /**
     * 
     */
    private static final long serialVersionUID = 6027479807775083200L;

    private String id;
    private String contractid;
    private String taxId;
    private String coreCustomerId;
    private String coreCustomerName;
    private String isPrimary;
    private String companyLegalUnit;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContractid() {
        return contractid;
    }

    public void setContractid(String contractid) {
        this.contractid = contractid;
    }
    
    public String getCompanyLegalUnit() {
        return companyLegalUnit;
    }

    public void setCompanyLegalUnit(String companyLegalUnit) {
        this.companyLegalUnit = companyLegalUnit;
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
        this.isPrimary = isPrimary;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
