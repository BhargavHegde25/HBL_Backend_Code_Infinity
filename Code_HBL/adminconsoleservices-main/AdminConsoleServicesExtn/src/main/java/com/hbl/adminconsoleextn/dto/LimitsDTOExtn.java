package com.hbl.adminconsoleextn.dto;

import java.util.Objects;

import com.temenos.dbx.product.commons.dto.LimitsDTO;

public class LimitsDTOExtn {

	

/**
	 * 
	 */
	private static final long serialVersionUID = -6524157394485402692L;
private String actionId;
private double minTransactionLimit;
private double maxTransactionLimit;
private double dailyLimit;
private double weeklyLimit;
private String companyLegalUnit;

	private double minMBTransactionLimit;
	private double maxMbTransactionLimit;
	private double dailyMBLimit;
	private double weeklyMBLimit;
	public LimitsDTOExtn() {
		this.minTransactionLimit = new Double(0);
		this.maxTransactionLimit = new Double(0);
		this.dailyLimit = new Double(0);
		this.weeklyLimit = new Double(0);

		this.minMBTransactionLimit= new Double(0);
		this.maxMbTransactionLimit= new Double(0);
		this.dailyMBLimit= new Double(0);
		this.weeklyMBLimit= new Double(0);
		this.weeklyMBLimit = new Double(0);
		
	}
	public LimitsDTOExtn(String actionId, double minTransactionLimit, double maxTransactionLimit, double dailyLimit,
			double weeklyLimit, String companyLegalUnit, double minMBTransactionLimit, double maxMbTransactionLimit,
			double dailyMBLimit, double weeklyMBLimit) {
		super();
		this.actionId = actionId;
		this.minTransactionLimit = minTransactionLimit;
		this.maxTransactionLimit = maxTransactionLimit;
		this.dailyLimit = dailyLimit;
		this.weeklyLimit = weeklyLimit;
		this.companyLegalUnit = companyLegalUnit;
		this.minMBTransactionLimit = minMBTransactionLimit;
		this.maxMbTransactionLimit = maxMbTransactionLimit;
		this.dailyMBLimit = dailyMBLimit;
		this.weeklyMBLimit = weeklyMBLimit;
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
	public double getMinMBTransactionLimit() {
		return minMBTransactionLimit;
	}
	public void setMinMBTransactionLimit(double minMBTransactionLimit) {
		this.minMBTransactionLimit = minMBTransactionLimit;
	}
	public double getMaxMbTransactionLimit() {
		return maxMbTransactionLimit;
	}
	public void setMaxMbTransactionLimit(double maxMbTransactionLimit) {
		this.maxMbTransactionLimit = maxMbTransactionLimit;
	}
	public double getDailyMBLimit() {
		return dailyMBLimit;
	}
	public void setDailyMBLimit(double dailyMBLimit) {
		this.dailyMBLimit = dailyMBLimit;
	}
	public double getWeeklyMBLimit() {
		return weeklyMBLimit;
	}
	public void setWeeklyMBLimit(double weeklyMBLimit) {
		this.weeklyMBLimit = weeklyMBLimit;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ Objects.hash(actionId, companyLegalUnit, dailyLimit, dailyMBLimit, maxMbTransactionLimit,
						maxTransactionLimit, minMBTransactionLimit, minTransactionLimit, weeklyLimit, weeklyMBLimit);
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		LimitsDTOExtn other = (LimitsDTOExtn) obj;
		return Objects.equals(actionId, other.actionId) && Objects.equals(companyLegalUnit, other.companyLegalUnit)
				&& Double.doubleToLongBits(dailyLimit) == Double.doubleToLongBits(other.dailyLimit)
				&& Double.doubleToLongBits(dailyMBLimit) == Double.doubleToLongBits(other.dailyMBLimit)
				&& Double.doubleToLongBits(maxMbTransactionLimit) == Double
						.doubleToLongBits(other.maxMbTransactionLimit)
				&& Double.doubleToLongBits(maxTransactionLimit) == Double.doubleToLongBits(other.maxTransactionLimit)
				&& Double.doubleToLongBits(minMBTransactionLimit) == Double
						.doubleToLongBits(other.minMBTransactionLimit)
				&& Double.doubleToLongBits(minTransactionLimit) == Double.doubleToLongBits(other.minTransactionLimit)
				&& Double.doubleToLongBits(weeklyLimit) == Double.doubleToLongBits(other.weeklyLimit)
				&& Double.doubleToLongBits(weeklyMBLimit) == Double.doubleToLongBits(other.weeklyMBLimit);
	}
	
	
}
