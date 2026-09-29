package com.temenos.dbx.product.commons.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserLimitsDTO implements DBPDTO{
	
	private static final long serialVersionUID = 8765866214607543701L;
	
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
	
	public UserLimitsDTO() {

	}

	public UserLimitsDTO(Double preApprovedTransactionLimit, Double autoDeniedTransactionLimit,
			Double preApprovedDailyLimit, Double autoDeniedDailyLimit, Double preApprovedWeeklyLimit,
			Double autoDeniedWeeklyLimit, Double maxTransactionLimit, Double minTransactionLimit, 
			Double dailyLimit, Double weeklyLimit) {
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
	
	public Double getMinTransactionLimit() {
		return minTransactionLimit;
	}

	public void setMinTransactionLimit(Double minTransactionLimit) {
		this.minTransactionLimit = minTransactionLimit;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((autoDeniedDailyLimit == null) ? 0 : autoDeniedDailyLimit.hashCode());
		result = prime * result + ((autoDeniedTransactionLimit == null) ? 0 : autoDeniedTransactionLimit.hashCode());
		result = prime * result + ((autoDeniedWeeklyLimit == null) ? 0 : autoDeniedWeeklyLimit.hashCode());
		result = prime * result + ((dailyLimit == null) ? 0 : dailyLimit.hashCode());
		result = prime * result + ((maxTransactionLimit == null) ? 0 : maxTransactionLimit.hashCode());
		result = prime * result + ((minTransactionLimit == null) ? 0 : minTransactionLimit.hashCode());
		result = prime * result + ((preApprovedDailyLimit == null) ? 0 : preApprovedDailyLimit.hashCode());
		result = prime * result + ((preApprovedTransactionLimit == null) ? 0 : preApprovedTransactionLimit.hashCode());
		result = prime * result + ((preApprovedWeeklyLimit == null) ? 0 : preApprovedWeeklyLimit.hashCode());
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
		UserLimitsDTO other = (UserLimitsDTO) obj;
		if (autoDeniedDailyLimit == null) {
			if (other.autoDeniedDailyLimit != null)
				return false;
		} else if (!autoDeniedDailyLimit.equals(other.autoDeniedDailyLimit))
			return false;
		if (autoDeniedTransactionLimit == null) {
			if (other.autoDeniedTransactionLimit != null)
				return false;
		} else if (!autoDeniedTransactionLimit.equals(other.autoDeniedTransactionLimit))
			return false;
		if (autoDeniedWeeklyLimit == null) {
			if (other.autoDeniedWeeklyLimit != null)
				return false;
		} else if (!autoDeniedWeeklyLimit.equals(other.autoDeniedWeeklyLimit))
			return false;
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
		if (minTransactionLimit == null) {
			if (other.minTransactionLimit != null)
				return false;
		} else if (!minTransactionLimit.equals(other.minTransactionLimit))
			return false;
		if (preApprovedDailyLimit == null) {
			if (other.preApprovedDailyLimit != null)
				return false;
		} else if (!preApprovedDailyLimit.equals(other.preApprovedDailyLimit))
			return false;
		if (preApprovedTransactionLimit == null) {
			if (other.preApprovedTransactionLimit != null)
				return false;
		} else if (!preApprovedTransactionLimit.equals(other.preApprovedTransactionLimit))
			return false;
		if (preApprovedWeeklyLimit == null) {
			if (other.preApprovedWeeklyLimit != null)
				return false;
		} else if (!preApprovedWeeklyLimit.equals(other.preApprovedWeeklyLimit))
			return false;
		if (weeklyLimit == null) {
			if (other.weeklyLimit != null)
				return false;
		} else if (!weeklyLimit.equals(other.weeklyLimit))
			return false;
		return true;
	}
	
}
