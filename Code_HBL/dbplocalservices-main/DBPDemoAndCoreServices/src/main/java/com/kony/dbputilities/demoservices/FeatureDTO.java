package com.kony.dbputilities.demoservices;

import java.util.ArrayList;
import java.util.List;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.temenos.dbx.product.commons.dto.LimitsDTO;
import com.temenos.dbx.product.dto.LimitDTO;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeatureDTO implements DBPDTO {

    /**
     * 
     */
    private static final long serialVersionUID = -4814174675129387407L;

    private String featureID;
    private ArrayList<FeatureActionDTO> actions;
    private LimitsDTO limits;
    private boolean isContractLevel;

    public FeatureDTO(String featureID, ArrayList<FeatureActionDTO> actions, boolean isContractLevel) {
        super();
        this.featureID = featureID;
        this.actions = actions;
        this.isContractLevel = isContractLevel;
    }

    public FeatureDTO() {
        super();
    }

    @Override
    public String toString() {
        if (!getIsContractLevel())
            return ("{\"featureID\":\"" + this.featureID + "\",\"Actions\":" + this.actions.toString() + "}");
        else
            return ("{\"featureId\":\"" + this.featureID + "\",\"actions\":" + this.actions.toString() + "}");
    }

    public void addFeatureAction(String featureId, String actionId, String actionType, String isEnabled,
            String isAccountLevel, List<AccountDTO> accounts, LimitDTO limits, String limitGroupId) {
        FeatureActionDTO actionDto =
                new FeatureActionDTO(featureId, actionType, isEnabled, actionId, isAccountLevel, accounts, limits,
                        limitGroupId);

        if (!"true".equalsIgnoreCase(isAccountLevel)) {
            actionDto.setAccounts(null);
        }

        this.actions.add(actionDto);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((actions == null) ? 0 : actions.hashCode());
        result = prime * result + ((featureID == null) ? 0 : featureID.hashCode());
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
        FeatureDTO other = (FeatureDTO) obj;
        if (actions == null) {
            if (other.actions != null)
                return false;
        } else if (!actions.equals(other.actions))
            return false;
        if (featureID == null) {
            if (other.featureID != null)
                return false;
        } else if (!featureID.equals(other.featureID))
            return false;
        return true;
    }

    public String getFeatureID() {
        return featureID;
    }

    public void setFeatureID(String featureID) {
        this.featureID = featureID;
    }

    public ArrayList<FeatureActionDTO> getActions() {
        return actions;
    }

    public void setActions(ArrayList<FeatureActionDTO> actions) {
        this.actions = actions;
    }

    public boolean getIsContractLevel() {
        return isContractLevel;
    }

    public void setIsContractLevel(boolean isContractLevel) {
        this.isContractLevel = isContractLevel;
    }

}