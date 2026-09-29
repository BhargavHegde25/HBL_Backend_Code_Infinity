package com.temenos.dbx.product.limitsandpermissions.dto;

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
	private String groupId;
	private String customerId;
	
	public ActionLimitsDTO() {
		super();
	}

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
		this.groupId = groupId;
		this.customerId = customerId;
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

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
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
		result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
		temp = Double.doubleToLongBits(maxTransactionLimitValue);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(minTransactionLimitValue);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((serviceDefinitionId == null) ? 0 : serviceDefinitionId.hashCode());
		temp = Double.doubleToLongBits(weeklyLimitValue);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		if (groupId == null) {
			if (other.groupId != null)
				return false;
		} else if (!groupId.equals(other.groupId))
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
		return true;
	}

}