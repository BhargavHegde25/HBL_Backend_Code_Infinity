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

public class GroupActionsDTO implements DBPDTO {

    /**
     * 
     */
    private static final long serialVersionUID = 5070774993153498148L;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getLimitTyeId() {
        return limitTyeId;
    }

    public void setLimitTyeId(String limitTyeId) {
        this.limitTyeId = limitTyeId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
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

    public String getFeatureId() {
        return featureId;
    }

    public void setFeatureId(String featureId) {
        this.featureId = featureId;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    private String groupId;
    private String limitTyeId;
    private String value;
    private String actionId;
    private String actionType;
    private String actionName;
    private String actionDescription;
    private String featureId;

}
