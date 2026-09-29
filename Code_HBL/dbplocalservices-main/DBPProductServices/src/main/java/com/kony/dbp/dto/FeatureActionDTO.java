package com.kony.dbp.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeatureActionDTO {

    private String actionId;
    private String actionName;
    private String actionDescription;
    private String isActionAllowed;
    private String isAccountLevel;
    private String isAllowedForCustomer;
    private String actionType;
    private Map<String, String> actionLimit;
    private String featureName;
    private String featureDescription;
    private String featureId;
    public String getIsAllowedForCustomer() {
		return isAllowedForCustomer;
	}

	public void setIsAllowedForCustomer(String isAllowedForCustomer) {
		this.isAllowedForCustomer = isAllowedForCustomer;
	}

	private String featureStatus;
    private Map<String, Map<String, String>> accountActionLimit;
    private List<String> enabledAccountLevelActions;
    private List<String> disabledAccountLevelActions;

    public List<String> getDisabledAccountLevelActions() {
		return disabledAccountLevelActions;
	}

	public void setDisabledAccountLevelActions(String accountId) {
		this.disabledAccountLevelActions.add(accountId);
	}

	public FeatureActionDTO() {
        this.actionLimit = new HashMap<>();
        this.accountActionLimit = new HashMap<>();
        this.enabledAccountLevelActions = new ArrayList<>();
        this.disabledAccountLevelActions = new ArrayList<>();
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionDescription(String actionDescription) {
        this.actionDescription = actionDescription;
    }

    public String getActionDescription() {
        return actionDescription;
    }

    public void setIsAccountLevel(String isAccountLevel) {
        this.isAccountLevel = isAccountLevel;
    }

    public String getIsAccountLevel() {
        return isAccountLevel;
    }

    public void setActiontype(String actionType) {
        this.actionType = actionType;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionLimit(String limitType_id, String value) {
        this.actionLimit.put(limitType_id, value);
    }

    public Map<String, String> getActionLimit() {
        return this.actionLimit;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureDescription(String featureDescription) {
        this.featureDescription = featureDescription;
    }

    public String getFeatureDescritpion() {
        return this.featureDescription;
    }

    public void setFeatureId(String featureId) {
        this.featureId = featureId;
    }

    public String getFeatureId() {
        return this.featureId;
    }

    public void setFeatureStatus(String featureStatus) {
        this.featureStatus = featureStatus;
    }

    public String getFeatureStatus() {
        return this.featureStatus;
    }

    public void setAccountActionLimit(String accountId, String limitTypeId, String value) {
        if (this.accountActionLimit.containsKey(accountId))
            this.accountActionLimit.get(accountId).put(limitTypeId, value);
        else {
            Map<String, String> limits = new HashMap<>();
            limits.put(limitTypeId, value);
            this.accountActionLimit.put(accountId, limits);
        }
    }

    public Map<String, Map<String, String>> getAccountActionLimit() {
        return this.accountActionLimit;
    }

    public void setEnabledAccountActions(String accountId) {
        this.enabledAccountLevelActions.add(accountId);
    }

    public List<String> getEnabledAccountActions() {
        return this.enabledAccountLevelActions;
    }

    public String getIsActionAllowed() {
        return isActionAllowed;
    }

    public void setIsActionAllowed(String isActionAllowed) {
        this.isActionAllowed = isActionAllowed;
    }

    public String getIsAccountEnabled() {
        return isAllowedForCustomer;
    }

    public void setIsAccountEnabled(String isAccountEnabled) {
        this.isAllowedForCustomer = isAccountEnabled;
    }

}
