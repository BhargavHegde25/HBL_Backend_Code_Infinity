package com.temenos.dbx.eum.product.commons.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.temenos.dbx.eum.product.commons.dto.LimitsDTO;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LimitsDTO implements DBPDTO{
	
	private static final long serialVersionUID = -7613806281296510318L;
	
	private Double maxTransactionLimit = null;
	
	@JsonAlias({"Daily"})
	private Double dailyLimit = null;
	
	@JsonAlias({"Weekly"})
	private Double weeklyLimit = null;
	
	private String dbpErrCode;
	private String dbpErrMsg;
	
	public LimitsDTO() {
		this.maxTransactionLimit = new Double(0);
		this.dailyLimit = new Double(0);
		this.weeklyLimit = new Double(0);
	}
	
	public LimitsDTO( Double maxTransactionLimit, Double dailyLimit,
			Double weeklyLimit) {
		this.maxTransactionLimit = maxTransactionLimit;
		this.dailyLimit = dailyLimit;
		this.weeklyLimit = weeklyLimit;
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
		int result = 1;
		result = prime * result + ((dailyLimit == null) ? 0 : dailyLimit.hashCode());
		result = prime * result + ((dbpErrMsg == null) ? 0 : dbpErrMsg.hashCode());
		result = prime * result + ((dbpErrCode == null) ? 0 : dbpErrCode.hashCode());
		result = prime * result + ((maxTransactionLimit == null) ? 0 : maxTransactionLimit.hashCode());
		result = prime * result + ((weeklyLimit == null) ? 0 : weeklyLimit.hashCode());
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
		LimitsDTO other = (LimitsDTO) obj;
		if (dailyLimit == null) {
			if (other.dailyLimit != null)
				return false;
		} else if (!dailyLimit.equals(other.dailyLimit))
			return false;
		if (maxTransactionLimit == null) {
			if (other.maxTransactionLimit != null)
				return false;
		} else if (!maxTransactionLimit.equals(other.maxTransactionLimit))
			return false;
		if (weeklyLimit == null) {
			if (other.weeklyLimit != null)
				return false;
		} else if (!weeklyLimit.equals(other.weeklyLimit))
			return false;
		return true;
	}
}
