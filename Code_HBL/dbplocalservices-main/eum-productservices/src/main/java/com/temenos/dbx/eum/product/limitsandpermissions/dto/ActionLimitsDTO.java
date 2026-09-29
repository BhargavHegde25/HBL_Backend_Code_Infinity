package com.temenos.dbx.eum.product.limitsandpermissions.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActionLimitsDTO implements DBPDTO{

	private static final long serialVersionUID = 6472269632834613801L;
	
	private String actionId;
	private String serviceDefinitionId;
	private double minTransactionLimitValue;
	private double maxTransactionLimitValue;
	private double dailyLimitValue;
	private double weeklyLimitValue;
	private String contractId;
	private String customerId;
	private String featureId;
	private String coreCustomerId;
	private String customRoleId;
	private Boolean isAccountLevel;
	private Boolean isMonetory;
	private String limitGroupId;
	private String roleId;
	private String accountId;
	private String isNewAction;
	private String isNewFeature;
	private String companyLegalUnit;
	
	public ActionLimitsDTO(String actionId, String serviceDefinitionId, double minTransactionLimitValue,
			double maxTransactionLimitValue, double dailyLimitValue, double weeklyLimitValue, String contractId,
			String groupId, String customerId) {
		super();
		this.actionId = actionId;
		this.serviceDefinitionId = serviceDefinitionId;
		this.minTransactionLimitValue = minTransactionLimitValue;
		this.maxTransactionLimitValue = maxTransactionLimitValue;
		this.dailyLimitValue = dailyLimitValue;
		this.weeklyLimitValue = weeklyLimitValue;
		this.contractId = contractId;
		this.customerId = customerId;
	}

	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}
	
	public String getCompanyLegalUnit() {
        return companyLegalUnit;
    }

    public void setCompanyLegalUnit(String companyLegalUnit) {
        this.companyLegalUnit = companyLegalUnit;
    }

	public String getServiceDefinitionId() {
		return serviceDefinitionId;
	}

	public void setServiceDefinitionId(String serviceDefinitionId) {
		this.serviceDefinitionId = serviceDefinitionId;
	}

	public double getMinTransactionLimitValue() {
		return minTransactionLimitValue;
	}

	public void setMinTransactionLimitValue(double minTransactionLimitValue) {
		this.minTransactionLimitValue = minTransactionLimitValue;
	}

	public double getMaxTransactionLimitValue() {
		return maxTransactionLimitValue;
	}

	public void setMaxTransactionLimitValue(double maxTransactionLimitValue) {
		this.maxTransactionLimitValue = maxTransactionLimitValue;
	}

	public double getDailyLimitValue() {
		return dailyLimitValue;
	}

	public void setDailyLimitValue(double dailyLimitValue) {
		this.dailyLimitValue = dailyLimitValue;
	}

	public double getWeeklyLimitValue() {
		return weeklyLimitValue;
	}

	public void setWeeklyLimitValue(double weeklyLimitValue) {
		this.weeklyLimitValue = weeklyLimitValue;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getFeatureId() {
		return featureId;
	}

	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}

	public String getCoreCustomerId() {
		return coreCustomerId;
	}

	public void setCoreCustomerId(String coreCustomerId) {
		this.coreCustomerId = coreCustomerId;
	}

	public String getCustomRoleId() {
		return customRoleId;
	}

	public void setCustomRoleId(String customRoleId) {
		this.customRoleId = customRoleId;
	}

	public Boolean isAccountLevel() {
		return isAccountLevel;
	}

	public void setAccountLevel(Boolean isAccountLevel) {
		this.isAccountLevel = isAccountLevel;
	}

	public Boolean isMonetory() {
		return isMonetory;
	}

	public void setMonetory(Boolean isMonetory) {
		this.isMonetory = isMonetory;
	}

	public String getLimitGroupId() {
		return limitGroupId;
	}

	public void setLimitGroupId(String limitGroupId) {
		this.limitGroupId = limitGroupId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	
	public ActionLimitsDTO() {
		super();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actionId == null) ? 0 : actionId.hashCode());
		result = prime * result + ((contractId == null) ? 0 : contractId.hashCode());
		result = prime * result + ((customerId == null) ? 0 : customerId.hashCode());
		long temp;
		temp = Double.doubleToLongBits(dailyLimitValue);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(maxTransactionLimitValue);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(minTransactionLimitValue);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((serviceDefinitionId == null) ? 0 : serviceDefinitionId.hashCode());
		temp = Double.doubleToLongBits(weeklyLimitValue);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((featureId == null) ? 0 : featureId.hashCode());
		result = prime * result + ((coreCustomerId == null) ? 0 : coreCustomerId.hashCode());
		result = prime * result + ((customRoleId == null) ? 0 : customRoleId.hashCode());
		result = prime * result + ((isAccountLevel == null) ? 0 : isAccountLevel.hashCode());
		result = prime * result + ((isMonetory == null) ? 0 : isMonetory.hashCode());
		result = prime * result + ((limitGroupId == null) ? 0 : limitGroupId.hashCode());
		result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
		result = prime * result + ((accountId == null) ? 0 : accountId.hashCode());
		result = prime * result + ((isNewAction == null) ? 0 : isNewAction.hashCode());
		result = prime * result + ((isNewFeature == null) ? 0 : isNewFeature.hashCode());
		
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
		ActionLimitsDTO other = (ActionLimitsDTO) obj;
		if (actionId == null) {
			if (other.actionId != null)
				return false;
		} else if (!actionId.equals(other.actionId))
			return false;
		if (contractId == null) {
			if (other.contractId != null)
				return false;
		} else if (!contractId.equals(other.contractId))
			return false;
		if (customerId == null) {
			if (other.customerId != null)
				return false;
		} else if (!customerId.equals(other.customerId))
			return false;
		if (Double.doubleToLongBits(dailyLimitValue) != Double.doubleToLongBits(other.dailyLimitValue))
			return false;
		if (Double.doubleToLongBits(maxTransactionLimitValue) != Double
				.doubleToLongBits(other.maxTransactionLimitValue))
			return false;
		if (Double.doubleToLongBits(minTransactionLimitValue) != Double
				.doubleToLongBits(other.minTransactionLimitValue))
			return false;
		if (serviceDefinitionId == null) {
			if (other.serviceDefinitionId != null)
				return false;
		} else if (!serviceDefinitionId.equals(other.serviceDefinitionId))
			return false;
		if (Double.doubleToLongBits(weeklyLimitValue) != Double.doubleToLongBits(other.weeklyLimitValue))
			return false;
		
		if (featureId == null) {
			if (other.featureId != null)
				return false;
		} else if (!featureId.equals(other.featureId))
			return false;
		
		if (coreCustomerId == null) {
			if (other.coreCustomerId != null)
				return false;
		} else if (!coreCustomerId.equals(other.coreCustomerId))
			return false;
		
		if (customRoleId == null) {
			if (other.customRoleId != null)
				return false;
		} else if (!customRoleId.equals(other.customRoleId))
			return false;
		
		
		if (isAccountLevel == null) {
			if (other.isAccountLevel != null)
				return false;
		} else if (!isAccountLevel.equals(other.isAccountLevel))
			return false;
		
		if (isMonetory == null) {
			if (other.isMonetory != null)
				return false;
		} else if (!isMonetory.equals(other.isMonetory))
			return false;
		if (limitGroupId == null) {
			if (other.limitGroupId != null)
				return false;
		} else if (!limitGroupId.equals(other.limitGroupId))
			return false;
		
		if (roleId == null) {
			if (other.roleId != null)
				return false;
		} else if (!roleId.equals(other.roleId))
			return false;
		
		if (accountId == null) {
			if (other.accountId != null)
				return false;
		} else if (!accountId.equals(other.accountId))
			return false;
		
		if (isNewAction == null) {
			if (other.isNewAction != null)
				return false;
		} else if (!isNewAction.equals(other.isNewAction))
			return false;
		
		if (isNewFeature == null) {
			if (other.isNewFeature != null)
				return false;
		} else if (!isNewFeature.equals(other.isNewFeature))
			return false;
		
		
		return true;
	}

	public String getIsNewAction() {
		return isNewAction;
	}

	public void setIsNewAction(String isNewAction) {
		this.isNewAction = isNewAction;
	}

	public String getIsNewFeature() {
		return isNewFeature;
	}

	public void setIsNewFeature(String isNewFeature) {
		this.isNewFeature = isNewFeature;
	}

}