package com.temenos.dbx.product.dto;

/**
 * 
 * @author KH2627
 * @version 1.0
 * implements {@link DBPDTO}
 * 
 *
 */

import com.dbp.core.api.DBPDTO;

public class OrganizationActionsDTO implements DBPDTO {

    /**
     * 
     */
    private static final long serialVersionUID = 5079774993153498158L;

    private String organizationId;
    private String featureId;
    private String featureName;
    private String featureDescription;
    private String fiFeatureStatus;
    private String orgFeatureStatus;
    private String actionType;
    private String actionId;
    private String actionName;
    private String actionDescription;
    private String limitTypeId;
    private String orgLimitValue;
    private String fiLimitValue;

    public String getIsAccountLevel() {
        return isAccountLevel;
    }

    public void setIsAccountLevel(String isAccountLevel) {
        this.isAccountLevel = isAccountLevel;
    }

    private String isAccountLevel;

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getFeatureId() {
        return featureId;
    }

    public void setFeatureId(String featureId) {
        this.featureId = featureId;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public String getFeatureDescription() {
        return featureDescription;
    }

    public void setFeatureDescription(String featureDescription) {
        this.featureDescription = featureDescription;
    }

    public String getFiFeatureStatus() {
        return fiFeatureStatus;
    }

    public void setFiFeatureStatus(String fiFeatureStatus) {
        this.fiFeatureStatus = fiFeatureStatus;
    }

    public String getOrgFeatureStatus() {
        return orgFeatureStatus;
    }

    public void setOrgFeatureStatus(String orgFeatureStatus) {
        this.orgFeatureStatus = orgFeatureStatus;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getActionDescription() {
        return actionDescription;
    }

    public void setActionDescription(String actionDescription) {
        this.actionDescription = actionDescription;
    }

    public String getLimitTypeId() {
        return limitTypeId;
    }

    public void setLimitTypeId(String limitTypeId) {
        this.limitTypeId = limitTypeId;
    }

    public String getOrgLimitValue() {
        return orgLimitValue;
    }

    public void setOrgLimitValue(String orgLimitValue) {
        this.orgLimitValue = orgLimitValue;
    }

    public String getFiLimitValue() {
        return fiLimitValue;
    }

    public void setFiLimitValue(String fiLimitValue) {
        this.fiLimitValue = fiLimitValue;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
