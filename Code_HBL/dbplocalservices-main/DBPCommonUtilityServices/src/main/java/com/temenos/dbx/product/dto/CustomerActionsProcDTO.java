package com.temenos.dbx.product.dto;

import com.dbp.core.api.DBPDTO;

public class CustomerActionsProcDTO implements DBPDTO {

    /**
     * 
     */
    private static final long serialVersionUID = 2857941053054243259L;
    private String customerId;
    private String accountId;
    private String isAllowed;
    private String actionId;
    private String isAccountLevel;
    private String featureStatusId;
    private String featureId;
    private String roleTypeId;
    private String limitTypeId;
    private String value;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getIsAccountLevel() {
        return isAccountLevel;
    }

    public void setIsAccountLevel(String isAccountLevel) {
        this.isAccountLevel = isAccountLevel;
    }

    public String getFeatureStatusId() {
        return featureStatusId;
    }

    public void setFeatureStatusId(String featureStatusId) {
        this.featureStatusId = featureStatusId;
    }

    public String getFeatureId() {
        return featureId;
    }

    public void setFeatureId(String featureId) {
        this.featureId = featureId;
    }

    public String getRoleTypeId() {
        return roleTypeId;
    }

    public void setRoleTypeId(String roleTypeId) {
        this.roleTypeId = roleTypeId;
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

}
