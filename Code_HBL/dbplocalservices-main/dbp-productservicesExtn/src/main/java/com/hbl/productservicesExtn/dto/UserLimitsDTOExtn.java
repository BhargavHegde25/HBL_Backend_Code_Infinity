package com.hbl.productservicesExtn.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.temenos.dbx.product.commons.dto.UserLimitsDTO;

public class UserLimitsDTOExtn extends UserLimitsDTO{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8433994598350961323L;

	@JsonAlias({"PRE_APPROVED_TRANSACTION_LIMIT"})
	private Double preApprovedTransactionLimit = new Double(0);
	
	@JsonAlias({"AUTO_DENIED_TRANSACTION_LIMIT"})
	private Double autoDeniedTransactionLimit = new Double(0);
	
	@JsonAlias({"PRE_APPROVED_DAILY_LIMIT"})
	private Double preApprovedDailyLimit = new Double(0);
	
	@JsonAlias({"AUTO_DENIED_DAILY_LIMIT"})
	private Double autoDeniedDailyLimit = new Double(0);
	
	@JsonAlias({"PRE_APPROVED_WEEKLY_LIMIT"})
	private Double preApprovedWeeklyLimit = new Double(0);
	
	@JsonAlias({"AUTO_DENIED_WEEKLY_LIMIT"})
	private Double autoDeniedWeeklyLimit = new Double(0);
	
	@JsonAlias({"MAX_TRANSACTION_LIMIT"})
	private Double maxTransactionLimit = new Double(0);
	
	@JsonAlias({"MIN_TRANSACTION_LIMIT"})
	private Double minTransactionLimit = new Double(0);
	
	@JsonAlias({"DAILY_LIMIT"})
	private Double dailyLimit = new Double(0);
	
	@JsonAlias({"WEEKLY_LIMIT"})
	private Double weeklyLimit = new Double(0);
	
	private Double monthlyLimit = new Double(0);
	
	
	@JsonAlias({"PRE_APPROVED_MB_TRANSACTION_LIMIT"})
	private Double preApprovedMBTransactionLimit = new Double(0);
	
	@JsonAlias({"AUTO_DENIED_MB_TRANSACTION_LIMIT"})
	private Double autoDeniedMBTransactionLimit = new Double(0);
	
	@JsonAlias({"PRE_APPROVED_MB_DAILY_LIMIT"})
	private Double preApprovedMBDailyLimit = new Double(0);
	
	@JsonAlias({"AUTO_DENIED_MB_DAILY_LIMIT"})
	private Double autoDeniedMBDailyLimit = new Double(0);
	
	@JsonAlias({"PRE_APPROVED_MB_WEEKLY_LIMIT"})
	private Double preApprovedMBWeeklyLimit = new Double(0);
	
	@JsonAlias({"AUTO_DENIED_MB_WEEKLY_LIMIT"})
	private Double autoDeniedMBWeeklyLimit = new Double(0);
	
	@JsonAlias({"MB_MAX_TRANSACTION_LIMIT"})
	private Double maxMBTransactionLimit = new Double(0);
	
	@JsonAlias({"MB_MIN_TRANSACTION_LIMIT"})
	private Double minMBTransactionLimit = new Double(0);
	
	@JsonAlias({"MB_DAILY_LIMIT"})
	private Double dailyMBLimit = new Double(0);
	
	@JsonAlias({"MB_WEEKLY_LIMIT"})
	private Double weeklyMBLimit = new Double(0);
	
	private Double monthlyMBLimit = new Double(0);
	
	
	

	public UserLimitsDTOExtn() {
		super();
	}




	public UserLimitsDTOExtn(Double preApprovedTransactionLimit, Double autoDeniedTransactionLimit,
			Double preApprovedDailyLimit, Double autoDeniedDailyLimit, Double preApprovedWeeklyLimit,
			Double autoDeniedWeeklyLimit, Double maxTransactionLimit, Double minTransactionLimit, Double dailyLimit,
			Double weeklyLimit, Double monthlyLimit, Double preApprovedMBTransactionLimit,
			Double autoDeniedMBTransactionLimit, Double preApprovedMBDailyLimit, Double autoDeniedMBDailyLimit,
			Double preApprovedMBWeeklyLimit, Double autoDeniedMBWeeklyLimit, Double maxMBTransactionLimit,
			Double minMBTransactionLimit, Double dailyMBLimit, Double weeklyMBLimit, Double monthlyMBLimit) {
		super();
		this.preApprovedTransactionLimit = preApprovedTransactionLimit;
		this.autoDeniedTransactionLimit = autoDeniedTransactionLimit;
		this.preApprovedDailyLimit = preApprovedDailyLimit;
		this.autoDeniedDailyLimit = autoDeniedDailyLimit;
		this.preApprovedWeeklyLimit = preApprovedWeeklyLimit;
		this.autoDeniedWeeklyLimit = autoDeniedWeeklyLimit;
		this.maxTransactionLimit = maxTransactionLimit;
		this.minTransactionLimit = minTransactionLimit;
		this.dailyLimit = dailyLimit;
		this.weeklyLimit = weeklyLimit;
		this.monthlyLimit = monthlyLimit;
		this.preApprovedMBTransactionLimit = preApprovedMBTransactionLimit;
		this.autoDeniedMBTransactionLimit = autoDeniedMBTransactionLimit;
		this.preApprovedMBDailyLimit = preApprovedMBDailyLimit;
		this.autoDeniedMBDailyLimit = autoDeniedMBDailyLimit;
		this.preApprovedMBWeeklyLimit = preApprovedMBWeeklyLimit;
		this.autoDeniedMBWeeklyLimit = autoDeniedMBWeeklyLimit;
		this.maxMBTransactionLimit = maxMBTransactionLimit;
		this.minMBTransactionLimit = minMBTransactionLimit;
		this.dailyMBLimit = dailyMBLimit;
		this.weeklyMBLimit = weeklyMBLimit;
		this.monthlyMBLimit = monthlyMBLimit;
	}




	public Double getPreApprovedTransactionLimit() {
		return preApprovedTransactionLimit;
	}




	public void setPreApprovedTransactionLimit(Double preApprovedTransactionLimit) {
		this.preApprovedTransactionLimit = preApprovedTransactionLimit;
	}




	public Double getAutoDeniedTransactionLimit() {
		return autoDeniedTransactionLimit;
	}




	public void setAutoDeniedTransactionLimit(Double autoDeniedTransactionLimit) {
		this.autoDeniedTransactionLimit = autoDeniedTransactionLimit;
	}




	public Double getPreApprovedDailyLimit() {
		return preApprovedDailyLimit;
	}




	public void setPreApprovedDailyLimit(Double preApprovedDailyLimit) {
		this.preApprovedDailyLimit = preApprovedDailyLimit;
	}




	public Double getAutoDeniedDailyLimit() {
		return autoDeniedDailyLimit;
	}




	public void setAutoDeniedDailyLimit(Double autoDeniedDailyLimit) {
		this.autoDeniedDailyLimit = autoDeniedDailyLimit;
	}




	public Double getPreApprovedWeeklyLimit() {
		return preApprovedWeeklyLimit;
	}




	public void setPreApprovedWeeklyLimit(Double preApprovedWeeklyLimit) {
		this.preApprovedWeeklyLimit = preApprovedWeeklyLimit;
	}




	public Double getAutoDeniedWeeklyLimit() {
		return autoDeniedWeeklyLimit;
	}




	public void setAutoDeniedWeeklyLimit(Double autoDeniedWeeklyLimit) {
		this.autoDeniedWeeklyLimit = autoDeniedWeeklyLimit;
	}




	public Double getMaxTransactionLimit() {
		return maxTransactionLimit;
	}




	public void setMaxTransactionLimit(Double maxTransactionLimit) {
		this.maxTransactionLimit = maxTransactionLimit;
	}




	public Double getMinTransactionLimit() {
		return minTransactionLimit;
	}




	public void setMinTransactionLimit(Double minTransactionLimit) {
		this.minTransactionLimit = minTransactionLimit;
	}




	public Double getDailyLimit() {
		return dailyLimit;
	}




	public void setDailyLimit(Double dailyLimit) {
		this.dailyLimit = dailyLimit;
	}




	public Double getWeeklyLimit() {
		return weeklyLimit;
	}




	public void setWeeklyLimit(Double weeklyLimit) {
		this.weeklyLimit = weeklyLimit;
	}




	public Double getMonthlyLimit() {
		return monthlyLimit;
	}




	public void setMonthlyLimit(Double monthlyLimit) {
		this.monthlyLimit = monthlyLimit;
	}




	public Double getPreApprovedMBTransactionLimit() {
		return preApprovedMBTransactionLimit;
	}




	public void setPreApprovedMBTransactionLimit(Double preApprovedMBTransactionLimit) {
		this.preApprovedMBTransactionLimit = preApprovedMBTransactionLimit;
	}




	public Double getAutoDeniedMBTransactionLimit() {
		return autoDeniedMBTransactionLimit;
	}




	public void setAutoDeniedMBTransactionLimit(Double autoDeniedMBTransactionLimit) {
		this.autoDeniedMBTransactionLimit = autoDeniedMBTransactionLimit;
	}




	public Double getPreApprovedMBDailyLimit() {
		return preApprovedMBDailyLimit;
	}




	public void setPreApprovedMBDailyLimit(Double preApprovedMBDailyLimit) {
		this.preApprovedMBDailyLimit = preApprovedMBDailyLimit;
	}




	public Double getAutoDeniedMBDailyLimit() {
		return autoDeniedMBDailyLimit;
	}




	public void setAutoDeniedMBDailyLimit(Double autoDeniedMBDailyLimit) {
		this.autoDeniedMBDailyLimit = autoDeniedMBDailyLimit;
	}




	public Double getPreApprovedMBWeeklyLimit() {
		return preApprovedMBWeeklyLimit;
	}




	public void setPreApprovedMBWeeklyLimit(Double preApprovedMBWeeklyLimit) {
		this.preApprovedMBWeeklyLimit = preApprovedMBWeeklyLimit;
	}




	public Double getAutoDeniedMBWeeklyLimit() {
		return autoDeniedMBWeeklyLimit;
	}




	public void setAutoDeniedMBWeeklyLimit(Double autoDeniedMBWeeklyLimit) {
		this.autoDeniedMBWeeklyLimit = autoDeniedMBWeeklyLimit;
	}




	public Double getMaxMBTransactionLimit() {
		return maxMBTransactionLimit;
	}




	public void setMaxMBTransactionLimit(Double maxMBTransactionLimit) {
		this.maxMBTransactionLimit = maxMBTransactionLimit;
	}




	public Double getMinMBTransactionLimit() {
		return minMBTransactionLimit;
	}




	public void setMinMBTransactionLimit(Double minMBTransactionLimit) {
		this.minMBTransactionLimit = minMBTransactionLimit;
	}




	public Double getDailyMBLimit() {
		return dailyMBLimit;
	}




	public void setDailyMBLimit(Double dailyMBLimit) {
		this.dailyMBLimit = dailyMBLimit;
	}




	public Double getWeeklyMBLimit() {
		return weeklyMBLimit;
	}




	public void setWeeklyMBLimit(Double weeklyMBLimit) {
		this.weeklyMBLimit = weeklyMBLimit;
	}




	public Double getMonthlyMBLimit() {
		return monthlyMBLimit;
	}




	public void setMonthlyMBLimit(Double monthlyMBLimit) {
		this.monthlyMBLimit = monthlyMBLimit;
	}




	public static long getSerialversionuid() {
		return serialVersionUID;
	}




	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(autoDeniedDailyLimit, autoDeniedMBDailyLimit,
				autoDeniedMBTransactionLimit, autoDeniedMBWeeklyLimit, autoDeniedTransactionLimit,
				autoDeniedWeeklyLimit, dailyLimit, dailyMBLimit, maxMBTransactionLimit, maxTransactionLimit,
				minMBTransactionLimit, minTransactionLimit, monthlyLimit, monthlyMBLimit, preApprovedDailyLimit,
				preApprovedMBDailyLimit, preApprovedMBTransactionLimit, preApprovedMBWeeklyLimit,
				preApprovedTransactionLimit, preApprovedWeeklyLimit, weeklyLimit, weeklyMBLimit);
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
		UserLimitsDTOExtn other = (UserLimitsDTOExtn) obj;
		return Objects.equals(autoDeniedDailyLimit, other.autoDeniedDailyLimit)
				&& Objects.equals(autoDeniedMBDailyLimit, other.autoDeniedMBDailyLimit)
				&& Objects.equals(autoDeniedMBTransactionLimit, other.autoDeniedMBTransactionLimit)
				&& Objects.equals(autoDeniedMBWeeklyLimit, other.autoDeniedMBWeeklyLimit)
				&& Objects.equals(autoDeniedTransactionLimit, other.autoDeniedTransactionLimit)
				&& Objects.equals(autoDeniedWeeklyLimit, other.autoDeniedWeeklyLimit)
				&& Objects.equals(dailyLimit, other.dailyLimit) && Objects.equals(dailyMBLimit, other.dailyMBLimit)
				&& Objects.equals(maxMBTransactionLimit, other.maxMBTransactionLimit)
				&& Objects.equals(maxTransactionLimit, other.maxTransactionLimit)
				&& Objects.equals(minMBTransactionLimit, other.minMBTransactionLimit)
				&& Objects.equals(minTransactionLimit, other.minTransactionLimit)
				&& Objects.equals(monthlyLimit, other.monthlyLimit)
				&& Objects.equals(monthlyMBLimit, other.monthlyMBLimit)
				&& Objects.equals(preApprovedDailyLimit, other.preApprovedDailyLimit)
				&& Objects.equals(preApprovedMBDailyLimit, other.preApprovedMBDailyLimit)
				&& Objects.equals(preApprovedMBTransactionLimit, other.preApprovedMBTransactionLimit)
				&& Objects.equals(preApprovedMBWeeklyLimit, other.preApprovedMBWeeklyLimit)
				&& Objects.equals(preApprovedTransactionLimit, other.preApprovedTransactionLimit)
				&& Objects.equals(preApprovedWeeklyLimit, other.preApprovedWeeklyLimit)
				&& Objects.equals(weeklyLimit, other.weeklyLimit) && Objects.equals(weeklyMBLimit, other.weeklyMBLimit);
	}


	
	


	
}
