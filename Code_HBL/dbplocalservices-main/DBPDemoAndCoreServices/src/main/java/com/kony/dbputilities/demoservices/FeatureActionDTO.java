package com.kony.dbputilities.demoservices;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.temenos.dbx.product.dto.LimitDTO;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeatureActionDTO implements DBPDTO {

    /**
     * 
     */
    private static final long serialVersionUID = 5002067027575767143L;

    private String featureId;
    private String actionType;
    private String isEnabled;
    private String actionId;
    private String isAccountLevel;
    private String isAllowed;
    private List<AccountDTO> accounts;
    LimitDTO limits;
    private String limitGroupId;

    public FeatureActionDTO() {
        super();
    }

    public FeatureActionDTO(String featureId, String actionType, String isEnabled, String actionId,
            String isAccountLevel,
            List<AccountDTO> accounts, LimitDTO limits, String limitGroupId) {
        super();
        this.setFeatureId(featureId);
        this.actionType = actionType;
        this.isEnabled = isEnabled;
        this.actionId = actionId;
        this.isAccountLevel = isAccountLevel;
        this.isAllowed = isEnabled;
        this.accounts = accounts;
        if ("MONETARY".equalsIgnoreCase(this.actionType))
            this.limits = limits;
        this.limitGroupId = limitGroupId;
    }

    @Override
    public String toString() {

        String retValue = "\"actionType\":\"" + this.actionType + "\",\"isEnabled\":\"" + this.isEnabled
                + "\",\"actionId\":\"" + this.actionId
                + "\",\"isAllowed\":\"" + this.isAllowed
                + "\",\"isAccountLevel\":\"" + this.isAccountLevel + "\"";

        if (StringUtils.isNotBlank(limitGroupId)) {
            retValue = retValue + ",\"limitGroupId\" : \"" + this.limitGroupId + "\"";
        }

        if ("true".equals(this.isAccountLevel) && accounts != null) {
            retValue = retValue + ",\"Accounts\":" + this.accounts.toString();
        }
        if (limits != null) {
            retValue = retValue + ",\"limits\" : " + this.limits.getContractsLimitsString();
        }

        return "{" + retValue + "}";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((accounts == null) ? 0 : accounts.hashCode());
        result = prime * result + ((actionId == null) ? 0 : actionId.hashCode());
        result = prime * result + ((actionType == null) ? 0 : actionType.hashCode());
        result = prime * result + ((isAccountLevel == null) ? 0 : isAccountLevel.hashCode());
        result = prime * result + ((isEnabled == null) ? 0 : isEnabled.hashCode());
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
        FeatureActionDTO other = (FeatureActionDTO) obj;
        if (accounts == null) {
            if (other.accounts != null)
                return false;
        } else if (!accounts.equals(other.accounts))
            return false;
        if (actionId == null) {
            if (other.actionId != null)
                return false;
        } else if (!actionId.equals(other.actionId))
            return false;
        if (actionType == null) {
            if (other.actionType != null)
                return false;
        } else if (!actionType.equals(other.actionType))
            return false;
        if (isAccountLevel == null) {
            if (other.isAccountLevel != null)
                return false;
        } else if (!isAccountLevel.equals(other.isAccountLevel))
            return false;
        if (isEnabled == null) {
            if (other.isEnabled != null)
                return false;
        } else if (!isEnabled.equals(other.isEnabled))
            return false;
        return true;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(String isEnabled) {
        this.isEnabled = isEnabled;
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

    public List<AccountDTO> getAccounts() {
        return accounts;
    }

    public void setAccounts(ArrayList<AccountDTO> accounts) {
        this.accounts = accounts;
    }

    public String getFeatureId() {
        return featureId;
    }

    public void setFeatureId(String featureId) {
        this.featureId = featureId;
    }

    public String getIsAllowed() {
        return isAllowed;
    }

    public void setIsAllowed(String isAllowed) {
        this.isAllowed = isAllowed;
    }

}