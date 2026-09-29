package com.kony.adminconsole.service.servicedefinition.dto;

import com.dbp.core.api.DBPDTO;

public class LimitDTO implements DBPDTO{
	
	private static final long serialVersionUID = 6472469197834613701L;
	
	private String actionId;
	private double minTransactionLimit;
	private double maxTransactionLimit;
	private double dailyLimit;
	private double weeklyLimit;
	private String companyLegalUnit;
	
	public LimitDTO() {
		super();
	}

	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	public double getMinTransactionLimit() {
		return minTransactionLimit;
	}

	public void setMinTransactionLimit(double minTransactionLimit) {
		this.minTransactionLimit = minTransactionLimit;
	}

	public double getMaxTransactionLimit() {
		return maxTransactionLimit;
	}

	public void setMaxTransactionLimit(double maxTransactionLimit) {
		this.maxTransactionLimit = maxTransactionLimit;
	}

	public double getDailyLimit() {
		return dailyLimit;
	}

	public void setDailyLimit(double dailyLimit) {
		this.dailyLimit = dailyLimit;
	}

	public double getWeeklyLimit() {
		return weeklyLimit;
	}

	public void setWeeklyLimit(double weeklyLimit) {
		this.weeklyLimit = weeklyLimit;
	}
	
    public String getCompanyLegalUnit() {
        return companyLegalUnit;
    }

    public void setCompanyLegalUnit(String companyLegalUnit) {
        this.companyLegalUnit = companyLegalUnit;
    }

    public LimitDTO(String actionId, double minTransactionLimit, double maxTransactionLimit, double dailyLimit,
			double weeklyLimit, String companyLegalUnit) {
		super();
		this.actionId = actionId;
		this.minTransactionLimit = minTransactionLimit;
		this.maxTransactionLimit = maxTransactionLimit;
		this.dailyLimit = dailyLimit;
		this.weeklyLimit = weeklyLimit;
		this.companyLegalUnit = companyLegalUnit;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actionId == null) ? 0 : actionId.hashCode());
		result = prime * result + ((companyLegalUnit == null) ? 0 : companyLegalUnit.hashCode());
		long temp;
		temp = Double.doubleToLongBits(dailyLimit);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(maxTransactionLimit);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(minTransactionLimit);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(weeklyLimit);
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
		LimitDTO other = (LimitDTO) obj;
		if (actionId == null) {
			if (other.actionId != null)
				return false;
		} else if (!actionId.equals(other.actionId))
			return false;
		return true;
	}

	
	
}
