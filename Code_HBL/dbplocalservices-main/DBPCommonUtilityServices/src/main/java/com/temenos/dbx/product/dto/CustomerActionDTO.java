package com.temenos.dbx.product.dto;

import com.dbp.core.api.DBPDTO;

public class CustomerActionDTO implements DBPDTO {

    /**
     * 
     */
    private static final long serialVersionUID = 1941881354728555468L;
    private String id;
    private String roleTpeId;
    private String customerId;
    private String contractId;
    private String coreCustomerId;
    private String featureId;
    private String actionId;
    private String accountId;
    private String isAllowed;
    private String policyId;
    private String limitGroupId;
    private String limitTypeId;
    private String value;
    private String createdby;
    private String modifiedby;
    private String createdts;
    private String lastmodifiedts;
    private String synctimestamp;
    private String softdeleteflag;
    private String companyLegalUnit;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleTpeId() {
        return roleTpeId;
    }

    public void setRoleTpeId(String roleTpeId) {
        this.roleTpeId = roleTpeId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getCoreCustomerId() {
        return coreCustomerId;
    }

    public void setCoreCustomerId(String coreCustomerId) {
        this.coreCustomerId = coreCustomerId;
    }

    public String getFeatureId() {
        return featureId;
    }

    public void setFeatureId(String featureId) {
        this.featureId = featureId;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getIsAllowed() {
        return isAllowed;
    }

    public void setIsAllowed(String isAllowed) {
        this.isAllowed = isAllowed;
    }

    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }

    public String getLimitGroupId() {
        return limitGroupId;
    }

    public void setLimitGroupId(String limitGroupId) {
        this.limitGroupId = limitGroupId;
    }

    public String getLimitTypeId() {
        return limitTypeId;
    }

    public void setLimitTypeId(String limitTypeId) {
        this.limitTypeId = limitTypeId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public String getLastmodifiedts() {
        return lastmodifiedts;
    }

    public void setLastmodifiedts(String lastmodifiedts) {
        this.lastmodifiedts = lastmodifiedts;
    }

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

	public String getCompanyLegalUnit() {
		return companyLegalUnit;
	}

	public void setCompanyLegalUnit(String companyLegalUnit) {
		this.companyLegalUnit = companyLegalUnit;
	}

}
