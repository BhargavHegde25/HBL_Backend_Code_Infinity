package com.hbl.dto;

import java.util.Objects;

import com.dbp.core.api.DBPDTO;

public class ActionLimitsDTOExtn implements DBPDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
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
	private double minMBTransactionLimitValue;
	private double maxMBTransactionLimitValue;
	private double dailyMBLimitValue;
	private double weeklyMBLimitValue;
	public ActionLimitsDTOExtn() {
		super();
	}
	public ActionLimitsDTOExtn(String actionId, String serviceDefinitionId, double minTransactionLimitValue,
			double maxTransactionLimitValue, double dailyLimitValue, double weeklyLimitValue, String contractId,
			String customerId, String featureId, String coreCustomerId, String customRoleId, Boolean isAccountLevel,
			Boolean isMonetory, String limitGroupId, String roleId, String accountId, String isNewAction,
			String isNewFeature, String companyLegalUnit, double minMBTransactionLimitValue,
			double maxMBTransactionLimitValue, double dailyMBLimitValue, double weeklyMBLimitValue) {
		super();
		this.actionId = actionId;
		this.serviceDefinitionId = serviceDefinitionId;
		this.minTransactionLimitValue = minTransactionLimitValue;
		this.maxTransactionLimitValue = maxTransactionLimitValue;
		this.dailyLimitValue = dailyLimitValue;
		this.weeklyLimitValue = weeklyLimitValue;
		this.contractId = contractId;
		this.customerId = customerId;
		this.featureId = featureId;
		this.coreCustomerId = coreCustomerId;
		this.customRoleId = customRoleId;
		this.isAccountLevel = isAccountLevel;
		this.isMonetory = isMonetory;
		this.limitGroupId = limitGroupId;
		this.roleId = roleId;
		this.accountId = accountId;
		this.isNewAction = isNewAction;
		this.isNewFeature = isNewFeature;
		this.companyLegalUnit = companyLegalUnit;
		this.minMBTransactionLimitValue = minMBTransactionLimitValue;
		this.maxMBTransactionLimitValue = maxMBTransactionLimitValue;
		this.dailyMBLimitValue = dailyMBLimitValue;
		this.weeklyMBLimitValue = weeklyMBLimitValue;
	}
	public String getActionId() {
		return actionId;
	}
	public void setActionId(String actionId) {
		this.actionId = actionId;
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
	public String getCompanyLegalUnit() {
		return companyLegalUnit;
	}
	public void setCompanyLegalUnit(String companyLegalUnit) {
		this.companyLegalUnit = companyLegalUnit;
	}
	public double getMinMBTransactionLimitValue() {
		return minMBTransactionLimitValue;
	}
	public void setMinMBTransactionLimitValue(double minMBTransactionLimitValue) {
		this.minMBTransactionLimitValue = minMBTransactionLimitValue;
	}
	public double getMaxMBTransactionLimitValue() {
		return maxMBTransactionLimitValue;
	}
	public void setMaxMBTransactionLimitValue(double maxMBTransactionLimitValue) {
		this.maxMBTransactionLimitValue = maxMBTransactionLimitValue;
	}
	public double getDailyMBLimitValue() {
		return dailyMBLimitValue;
	}
	public void setDailyMBLimitValue(double dailyMBLimitValue) {
		this.dailyMBLimitValue = dailyMBLimitValue;
	}
	public double getWeeklyMBLimitValue() {
		return weeklyMBLimitValue;
	}
	public void setWeeklyMBLimitValue(double weeklyMBLimitValue) {
		this.weeklyMBLimitValue = weeklyMBLimitValue;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public int hashCode() {
		return Objects.hash(accountId, actionId, companyLegalUnit, contractId, coreCustomerId, customRoleId, customerId,
				dailyLimitValue, dailyMBLimitValue, featureId, isAccountLevel, isMonetory, isNewAction, isNewFeature,
				limitGroupId, maxMBTransactionLimitValue, maxTransactionLimitValue, minMBTransactionLimitValue,
				minTransactionLimitValue, roleId, serviceDefinitionId, weeklyLimitValue, weeklyMBLimitValue);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActionLimitsDTOExtn other = (ActionLimitsDTOExtn) obj;
		return Objects.equals(accountId, other.accountId) && Objects.equals(actionId, other.actionId)
				&& Objects.equals(companyLegalUnit, other.companyLegalUnit)
				&& Objects.equals(contractId, other.contractId) && Objects.equals(coreCustomerId, other.coreCustomerId)
				&& Objects.equals(customRoleId, other.customRoleId) && Objects.equals(customerId, other.customerId)
				&& Double.doubleToLongBits(dailyLimitValue) == Double.doubleToLongBits(other.dailyLimitValue)
				&& Double.doubleToLongBits(dailyMBLimitValue) == Double.doubleToLongBits(other.dailyMBLimitValue)
				&& Objects.equals(featureId, other.featureId) && Objects.equals(isAccountLevel, other.isAccountLevel)
				&& Objects.equals(isMonetory, other.isMonetory) && Objects.equals(isNewAction, other.isNewAction)
				&& Objects.equals(isNewFeature, other.isNewFeature) && Objects.equals(limitGroupId, other.limitGroupId)
				&& Double.doubleToLongBits(maxMBTransactionLimitValue) == Double
						.doubleToLongBits(other.maxMBTransactionLimitValue)
				&& Double.doubleToLongBits(maxTransactionLimitValue) == Double
						.doubleToLongBits(other.maxTransactionLimitValue)
				&& Double.doubleToLongBits(minMBTransactionLimitValue) == Double
						.doubleToLongBits(other.minMBTransactionLimitValue)
				&& Double.doubleToLongBits(minTransactionLimitValue) == Double
						.doubleToLongBits(other.minTransactionLimitValue)
				&& Objects.equals(roleId, other.roleId)
				&& Objects.equals(serviceDefinitionId, other.serviceDefinitionId)
				&& Double.doubleToLongBits(weeklyLimitValue) == Double.doubleToLongBits(other.weeklyLimitValue)
				&& Double.doubleToLongBits(weeklyMBLimitValue) == Double.doubleToLongBits(other.weeklyMBLimitValue);
	}
	

}
