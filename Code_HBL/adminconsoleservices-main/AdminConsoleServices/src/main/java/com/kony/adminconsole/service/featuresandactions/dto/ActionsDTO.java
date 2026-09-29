package com.kony.adminconsole.service.featuresandactions.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActionsDTO implements DBPDTO{
	
	private static final long serialVersionUID = 8472269187344613901L;
	
	private String id;
	@JsonAlias({"Action_id"})
	private String actionId;
	@JsonAlias({"LimitType_id"})
	private String limitTypeId;
	private String localeId;
	@JsonAlias({"status"})
    private String statusId;
	private String value;
	private String displayName;
	private String displayDescription;
	private double minTxLimit;
	private double maxTxLimit;
	private double dailyLimit;
	private double weeklyLimit;
	private String modifiedby;
	private String lastmodifiedts;
	private String companyLegalUnit;
	
	public ActionsDTO() {
		super();
	}

	public ActionsDTO(String id, String actionId, String limitTypeId, String localeId, String statusId, String value,
			String displayName, String displayDescription, double minTxLimit, double maxTxLimit, double dailyLimit,
			double weeklyLimit, String modifiedby, String lastmodifiedts, String companyLegalUnit) {
		super();
		this.id = id;
		this.actionId = actionId;
		this.limitTypeId = limitTypeId;
		this.localeId = localeId;
		this.statusId = statusId;
		this.value = value;
		this.displayName = displayName;
		this.displayDescription = displayDescription;
		this.minTxLimit = minTxLimit;
		this.maxTxLimit = maxTxLimit;
		this.dailyLimit = dailyLimit;
		this.weeklyLimit = weeklyLimit;
		this.modifiedby = modifiedby;
		this.lastmodifiedts = lastmodifiedts;
		this.companyLegalUnit = companyLegalUnit;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	public String getLocaleId() {
		return localeId;
	}

	public void setLocaleId(String localeId) {
		this.localeId = localeId;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayDescription() {
		return displayDescription;
	}

	public void setDisplayDescription(String displayDescription) {
		this.displayDescription = displayDescription;
	}

	public double getMinTxLimit() {
		return minTxLimit;
	}

	public void setMinTxLimit(double minTxLimit) {
		this.minTxLimit = minTxLimit;
	}

	public double getMaxTxLimit() {
		return maxTxLimit;
	}

	public void setMaxTxLimit(double maxTxLimit) {
		this.maxTxLimit = maxTxLimit;
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

	public String getModifiedby() {
		return modifiedby;
	}

	public void setModifiedby(String modifiedby) {
		this.modifiedby = modifiedby;
	}

	public String getLastmodifiedts() {
		return lastmodifiedts;
	}

	public void setLastmodifiedts(String lastmodifiedts) {
		this.lastmodifiedts = lastmodifiedts;
	}

	public String getLimitTypeId() {
		return limitTypeId;
	}

	public void setLimitTypeId(String limitTypeId) {
		this.limitTypeId = limitTypeId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String getCompanyLegalUnit() {
        return companyLegalUnit; 
    }

    public void setCompanyLegalUnit(String companyLegalUnit) {
        this.companyLegalUnit = companyLegalUnit;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actionId == null) ? 0 : actionId.hashCode());
		long temp;
		temp = Double.doubleToLongBits(dailyLimit);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((displayDescription == null) ? 0 : displayDescription.hashCode());
		result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lastmodifiedts == null) ? 0 : lastmodifiedts.hashCode());
		result = prime * result + ((limitTypeId == null) ? 0 : limitTypeId.hashCode());
		result = prime * result + ((localeId == null) ? 0 : localeId.hashCode());
		temp = Double.doubleToLongBits(maxTxLimit);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(minTxLimit);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((modifiedby == null) ? 0 : modifiedby.hashCode());
		result = prime * result + ((companyLegalUnit == null) ? 0 : companyLegalUnit.hashCode());
		result = prime * result + ((statusId == null) ? 0 : statusId.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		ActionsDTO other = (ActionsDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	

}