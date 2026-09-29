package com.hbl.productservicesExtn.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.temenos.dbx.product.commons.dto.LimitsDTO;

public class LimitsDTOExtn extends LimitsDTO {
	
	private Double maxTransactionLimit = null;
	@JsonAlias({"Daily"})
	private Double dailyLimit = null;
	
	@JsonAlias({"Weekly"})
	private Double weeklyLimit = null;
	
	@JsonAlias({"Monthly"})
	private Double monthlyLimit = null;
	
	private Double minTransactionLimit = null;
	
	private Double minMBTransactionLimit = null;
	
	private Double maxMBTransactionLimit = null;
	
	@JsonAlias({"MB_Daily"})
	private Double dailyMBLimit = null;
	
	@JsonAlias({"MB_Weekly"})
	private Double weeklyMBLimit = null;
	
	@JsonAlias({"MB_Monthly"})
	private Double monthlyMBLimit = null;
	
	
	private String dbpErrCode;
	private String dbpErrMsg;
	
	public LimitsDTOExtn() {
		this.maxTransactionLimit = new Double(0);
		this.dailyLimit = new Double(0);
		this.weeklyLimit = new Double(0);
		this.monthlyLimit = new Double(0);

		this.minMBTransactionLimit= new Double(0);
		this.maxMBTransactionLimit= new Double(0);
		this.dailyMBLimit= new Double(0);
		this.weeklyMBLimit= new Double(0);
		this.monthlyMBLimit = new Double(0);
		this.minTransactionLimit = new Double(0);
		
	}

	public LimitsDTOExtn(Double maxTransactionLimit, Double dailyLimit, Double weeklyLimit, Double monthlyLimit,
			Double monthlyMBLimit, Double minMBTransactionLimit, Double maxMBTransactionLimit, Double dailyMBLimit,
			Double weeklyMBLimit, String dbpErrCode, String dbpErrMsg,  Double minTransactionLimit) {
		super();
		this.maxTransactionLimit = maxTransactionLimit;
		this.dailyLimit = dailyLimit;
		this.weeklyLimit = weeklyLimit;
		this.monthlyLimit = monthlyLimit;
		this.monthlyMBLimit = monthlyMBLimit;
		this.minMBTransactionLimit = minMBTransactionLimit;
		this.minTransactionLimit = minTransactionLimit;
		this.maxMBTransactionLimit = maxMBTransactionLimit;
		this.dailyMBLimit = dailyMBLimit;
		this.weeklyMBLimit = weeklyMBLimit;
		this.dbpErrCode = dbpErrCode;
		this.dbpErrMsg = dbpErrMsg;
	}

	public Double getMaxTransactionLimit() {
		return maxTransactionLimit;
	}

	public void setMaxTransactionLimit(Double maxTransactionLimit) {
		this.maxTransactionLimit = maxTransactionLimit;
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
	
	

	public Double getMinTransactionLimit() {
		return minTransactionLimit;
	}

	public void setMinTransactionLimit(Double minTransactionLimit) {
		this.minTransactionLimit = minTransactionLimit;
	}

	public Double getMonthlyLimit() {
		return monthlyLimit;
	}

	public void setMonthlyLimit(Double monthlyLimit) {
		this.monthlyLimit = monthlyLimit;
	}

	public Double getMonthlyMBLimit() {
		return monthlyMBLimit;
	}

	public void setMonthlyMBLimit(Double monthlyMBLimit) {
		this.monthlyMBLimit = monthlyMBLimit;
	}

	public Double getMinMBTransactionLimit() {
		return minMBTransactionLimit;
	}

	public void setMinMBTransactionLimit(Double minMBTransactionLimit) {
		this.minMBTransactionLimit = minMBTransactionLimit;
	}

	public Double getMaxMBTransactionLimit() {
		return maxMBTransactionLimit;
	}

	public void setMaxMBTransactionLimit(Double maxMBTransactionLimit) {
		this.maxMBTransactionLimit = maxMBTransactionLimit;
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

	public String getDbpErrCode() {
		return dbpErrCode;
	}

	public void setDbpErrCode(String dbpErrCode) {
		this.dbpErrCode = dbpErrCode;
	}

	public String getDbpErrMsg() {
		return dbpErrMsg;
	}

	public void setDbpErrMsg(String dbpErrMsg) {
		this.dbpErrMsg = dbpErrMsg;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(dailyLimit, dailyMBLimit, dbpErrCode, dbpErrMsg, maxMBTransactionLimit,
				maxTransactionLimit, minMBTransactionLimit, minTransactionLimit, monthlyLimit, monthlyMBLimit,
				weeklyLimit, weeklyMBLimit);
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
		return Objects.equals(dailyLimit, other.dailyLimit) && Objects.equals(dailyMBLimit, other.dailyMBLimit)
				&& Objects.equals(dbpErrCode, other.dbpErrCode) && Objects.equals(dbpErrMsg, other.dbpErrMsg)
				&& Objects.equals(maxMBTransactionLimit, other.maxMBTransactionLimit)
				&& Objects.equals(maxTransactionLimit, other.maxTransactionLimit)
				&& Objects.equals(minMBTransactionLimit, other.minMBTransactionLimit)
				&& Objects.equals(minTransactionLimit, other.minTransactionLimit)
				&& Objects.equals(monthlyLimit, other.monthlyLimit)
				&& Objects.equals(monthlyMBLimit, other.monthlyMBLimit)
				&& Objects.equals(weeklyLimit, other.weeklyLimit) && Objects.equals(weeklyMBLimit, other.weeklyMBLimit);
	}

	

	
	
	

}
