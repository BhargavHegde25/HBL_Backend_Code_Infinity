package com.temenos.dbx.product.approvalmatrixservices.dto;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;

import com.dbp.core.api.DBPDTO;
import com.temenos.dbx.product.constants.Constants;
/**
 * 
 * @author KH9450
 * @version 1.0
 * implements {@link DBPDTO}
 * 
 *
 */
public class ApprovalMatrixActionDTO implements DBPDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8456108141611121945L;
	private String actionId;
	private String actionName;
	private String actionDescription;
	private String featureId;
	private String featureName;
	private String featureStatus;
	private String actionType;
	private String maxAmount;
	private String isAccountLevel;
	
	private List<ApprovalMatrixLimitDTO> limits = new ArrayList<>();
	public ApprovalMatrixActionDTO(){		
	}	
	public ApprovalMatrixActionDTO(String actionId)
	{
		this.actionId = actionId;
		this.actionName = "";
		this.actionDescription = "";
		this.featureId = "";
		this.featureName = "" ;
		this.featureStatus = "";
		this.maxAmount = "";
		this.limits = new ArrayList<>();
	}	
	public ApprovalMatrixActionDTO(String actionId,String actionName,String actionDescription,String featureId,String featureName,String featureStatus,String actionType, List<ApprovalMatrixLimitDTO> limits, String maxAmount, String isAccountLevel)
	{
		this.actionId = actionId;
		this.actionName = actionName;
		this.actionDescription = actionDescription;
		this.featureId = featureId;
		this.featureName = featureName ;
		this.featureStatus = featureStatus;
		this.actionType = actionType;
		this.limits = limits;
		this.maxAmount =  maxAmount;
		this.isAccountLevel = isAccountLevel;
	}
	public String getActionId() {
		return actionId;
	}
	public void setActionId(String actionId) {
		this.actionId = actionId;
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
	public String getFeatureStatus() {
		return featureStatus;
	}
	public void setFeatureStatus(String featureStatus) {
		this.featureStatus = featureStatus;
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
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public List<ApprovalMatrixLimitDTO> getLimits() {
		return limits;
	}
	public void setLimits(List<ApprovalMatrixLimitDTO> limits) {
		this.limits = limits;
	}
	public void add(ApprovalMatrixLimitDTO limit)
	{
		limits.add(limit);
	}
	public String getMaxAmount() {
		return maxAmount;
	}
	public void setMaxAmount(String maxAmount) {
		this.maxAmount = maxAmount;
	}
	public String getIsAccountLevel() {
		return isAccountLevel;
	}
	public void setIsAccountLevel(String isAccountLevel) {
		this.isAccountLevel = isAccountLevel;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actionDescription == null) ? 0 : actionDescription.hashCode());
		result = prime * result + ((actionId == null) ? 0 : actionId.hashCode());
		result = prime * result + ((actionName == null) ? 0 : actionName.hashCode());
		result = prime * result + ((actionType == null) ? 0 : actionType.hashCode());
		result = prime * result + ((featureId == null) ? 0 : featureId.hashCode());
		result = prime * result + ((featureName == null) ? 0 : featureName.hashCode());
		result = prime * result + ((featureStatus == null) ? 0 : featureStatus.hashCode());
		result = prime * result + ((maxAmount == null) ? 0 : maxAmount.hashCode());
		result = prime * result + ((isAccountLevel == null) ? 0 : isAccountLevel.hashCode());
		result = prime * result + ((limits == null) ? 0 : limits.hashCode());
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
		ApprovalMatrixActionDTO other = (ApprovalMatrixActionDTO) obj;
		if (actionDescription == null) {
			if (other.actionDescription != null)
				return false;
		} else if (!actionDescription.equals(other.actionDescription))
			return false;
		if (actionId == null) {
			if (other.actionId != null)
				return false;
		} else if (!actionId.equals(other.actionId))
			return false;
		if (actionName == null) {
			if (other.actionName != null)
				return false;
		} else if (!actionName.equals(other.actionName))
			return false;
		if (actionType == null) {
			if (other.actionType != null)
				return false;
		} else if (!actionType.equals(other.actionType))
			return false;
		if (featureId == null) {
			if (other.featureId != null)
				return false;
		} else if (!featureId.equals(other.featureId))
			return false;
		if (featureName == null) {
			if (other.featureName != null)
				return false;
		} else if (!featureName.equals(other.featureName))
			return false;
		if (featureStatus == null) {
			if (other.featureStatus != null)
				return false;
		} else if (!featureStatus.equals(other.featureStatus))
			return false;
		if (limits == null) {
			if (other.limits != null)
				return false;
		} else if (!limits.equals(other.limits))
			return false;
		if (maxAmount == null) {
			if (other.maxAmount != null)
				return false;
		} else if (!maxAmount.equals(other.maxAmount))
			return false;
		if (isAccountLevel == null) {
			if (other.isAccountLevel != null)
				return false;
		} else if (!isAccountLevel.equals(other.isAccountLevel))
			return false;
		return true;
	}
	public static Map<String,Map<String, ApprovalMatrixActionDTO>> getActionsList(SortedSet<String> featureActions, HashMap<String,String> actionTypeMap) {
		Map<String, ApprovalMatrixActionDTO> maxActions = new TreeMap<>();
		Map<String, ApprovalMatrixActionDTO> dailyActions = new TreeMap<>();
		Map<String, ApprovalMatrixActionDTO> weeklyActions = new TreeMap<>();
		Map<String, ApprovalMatrixActionDTO> nonFinancialActions = new TreeMap<>();
		
		for (String actionId : featureActions) {
			if (actionTypeMap.get(actionId).equalsIgnoreCase("MONETARY")) {
				dailyActions.put(actionId, new ApprovalMatrixActionDTO(actionId));
				maxActions.put(actionId, new ApprovalMatrixActionDTO(actionId));
				weeklyActions.put(actionId, new ApprovalMatrixActionDTO(actionId));
			} else if (actionTypeMap.get(actionId).equalsIgnoreCase("NON_MONETARY")) {
				nonFinancialActions.put(actionId, new ApprovalMatrixActionDTO(actionId));
			}
		}
		Map<String,Map<String, ApprovalMatrixActionDTO>> result = new HashMap<>();
		result.put(Constants.DAILY_LIMIT,dailyActions);
		result.put(Constants.MAX_TRANSACTION_LIMIT,maxActions);
		result.put(Constants.WEEKLY_LIMIT,weeklyActions);
		result.put(Constants.NON_MONETARY_LIMIT,nonFinancialActions);
		return result;
	}

	public void clearLimits() {
		limits.clear();
	}
}
