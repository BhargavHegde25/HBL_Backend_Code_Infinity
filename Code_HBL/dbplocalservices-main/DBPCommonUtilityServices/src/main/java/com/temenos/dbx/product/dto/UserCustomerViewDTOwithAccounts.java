package com.temenos.dbx.product.dto;

import com.dbp.core.api.DBPDTO;

public class UserCustomerViewDTOwithAccounts implements DBPDTO {

    /**
     * 
     */
    private static final long serialVersionUID = -463737512548185914L;
    private String customerId;
    private String coreCustomerId;
    private String contractId;
    private String autoSyncAccounts;
    private String contractName;
    private String isPrimary;
    private String coreCustomerName;
    private String isBusiness;
    private String serviceDefinitionId;
    private String serviceDefinitionName;
    private String serviceDefinitionType;
    private String roleId;
    private String userRole;
    private String accountId;
    private String accountName;
    private String statusDesc;
    private String typeId;
    private String companyLegalUnit;
    
    

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCoreCustomerId() {
        return coreCustomerId;
    }

    public void setCoreCustomerId(String coreCustomerId) {
        this.coreCustomerId = coreCustomerId;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(String isPrimary) {
        this.isPrimary = isPrimary;
    }

    public String getCoreCustomerName() {
        return coreCustomerName;
    }

    public void setCoreCustomerName(String coreCustomerName) {
        this.coreCustomerName = coreCustomerName;
    }

    public String getIsBusiness() {
        return isBusiness;
    }

    public void setIsBusiness(String isBusiness) {
        this.isBusiness = isBusiness;
    }

    public String getServiceDefinitionId() {
        return serviceDefinitionId;
    }

    public void setServiceDefinitionId(String serviceDefinitionId) {
        this.serviceDefinitionId = serviceDefinitionId;
    }

    public String getServiceDefinitionName() {
        return serviceDefinitionName;
    }

    public void setServiceDefinitionName(String serviceDefinitionName) {
        this.serviceDefinitionName = serviceDefinitionName;
    }

    public String getServiceDefinitionType() {
        return serviceDefinitionType;
    }

    public void setServiceDefinitionType(String serviceDefinitionType) {
        this.serviceDefinitionType = serviceDefinitionType;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getAutoSyncAccounts() {
        return autoSyncAccounts;
    }

    public void setAutoSyncAccounts(String autoSyncAccounts) {
        this.autoSyncAccounts = autoSyncAccounts;
    }

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

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getCompanyLegalUnit() {
		return companyLegalUnit;
	}

	public void setCompanyLegalUnit(String companyLegalUnit) {
		this.companyLegalUnit = companyLegalUnit;
	}
}